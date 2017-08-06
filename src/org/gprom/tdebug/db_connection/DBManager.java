package org.gprom.tdebug.db_connection;
import java.sql.*;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.gprom.tdebug.db_connection.DBConfigInterface.AuditTable;
import org.gprom.tdebug.db_connection.DBConfigInterface.ConfigProperty;
import org.gprom.util.LoggerUtil;

/**
 * this class is use to manager the database, connect and exectue sql and it
 * will use single design pattern
 * 
 * @author lingxue zheng
 *
 */
public class DBManager {
	
	/** logger **/
	static Logger log = Logger.getLogger(DBManager.class);
	private static final String URL_TEMPLATE = "jdbc:oracle:thin:@<HOST>:<PORT>/<SID>";
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	
	private static final String UNIFIED_QUERY = "SELECT * FROM UNIFIED_AUDIT_TRAIL "
			+ "WHERE  EVENT_TIMESTAMP > to_date('07/29/2017', 'MM/DD/YYYY') "
			+ "ORDER BY EVENT_TIMESTAMP ASC";
	//private static final String FGA_QUERY = "SELECT * FROM SYS.FGA_LOG$" + " WHERE  NTIMESTAMP# > to_date('07/29/2016', 'MM/DD/YYYY') ";
	private static final String FGA_QUERY = "SELECT STMT_TYPE, " 
									//+"LSQLTEXT, "
									//+ "CASE WHEN DBMS_LOB.GETLENGTH(LSQLTEXT) <= 4000 THEN DBMS_LOB.SUBSTR(LSQLTEXT,4000) ELSE LSQLTEXT END, "
									+ "CASE WHEN DBMS_LOB.GETLENGTH(lsqltext) > 4000 THEN lsqltext ELSE NULL END AS LSQL,"
									+ "CASE WHEN DBMS_LOB.GETLENGTH(lsqltext) <= 4000 THEN DBMS_LOB.SUBSTR(lsqltext,4000) ELSE NULL END AS LSQLTEXT, "
									+ "SCN, "
									+ "NTIMESTAMP#, "
									+ "OSUID, "
									+ "SESSIONID, "
									+ "XID "
									+ "FROM SYS.FGA_LOG$ " 									 
									+ "WHERE  NTIMESTAMP# > to_date('07/20/2017', 'MM/DD/YYYY') "
									+ "ORDER BY NTIMESTAMP# ASC";

	/** the url to the database **/
	private String url;
	/** user name in database **/
	private String user;
	/** password in database **/
	private String password;
	/** this is the single object of Connection **/
	private Connection connection = null;
	/** the Statement when execute sql **/
	Statement stmt;
	private Properties connProperties = new Properties();
	
	/** singleton pattern **/
	private static DBManager dbManager = null;
	
	
	/**
	 * constructer will be private in singleton pattern
	 */
	private DBManager() {
	}

	/**
	 * java single desgin pattern get the unique object;
	 * 
	 * @return
	 */
	public static DBManager getInstance() {
		if (dbManager == null) {
			dbManager = new DBManager();
		}
		return dbManager;
	}
	

    public ResultSet getData() throws Exception{
    	Connection connection = getConnection();
    	Statement statement = null;
    	String query;
    	ResultSet resultSet = null;
    	
//    	ROWNUM <=1000 AND
		try {
			statement = connection.createStatement();
			
			switch(AuditTable.valueOf(DBConfig.inst.getConnectionProperty(ConfigProperty.AUDIT_TABLE)))
			{
			case FGA_LOG:
				query = FGA_QUERY;
				break;
			case UNIFIED_AUDIT:
				query = UNIFIED_QUERY;
				break;
			default:
				query = "";
				break;
			}
			//query ="SELECT * FROM SYS.FGA_LOG$";  //WHERE  EVENT_TIMESTAMP > to_date('02/18/2016', 'MM/DD/YYYY') ORDER BY EVENT_TIMESTAMP ASC";//UNIFIED_AUDIT_TRAIL  SYS.fga_log$
			resultSet = statement.executeQuery(query);
		}catch (SQLException e) {
			LoggerUtil.logException(e, log);
			throw(e);
		}
		return resultSet;
    }
    
    public ResultSet getDataCommit() throws Exception {
	    	Connection connection = getConnection();
	    	Statement statement = null;
	    	String query;
	    	ResultSet resultSet = null;
	    	try {
	    		statement = connection.createStatement();
	    		query ="SELECT DISTINCT VERSIONS_STARTSCN, VERSIONS_XID, VERSIONS_STARTTIME FROM R VERSIONS BETWEEN SCN MINVALUE AND MAXVALUE ORDER BY VERSIONS_STARTTIME";//UNIFIED_AUDIT_TRAIL  SYS.fga_log$
	    		resultSet = statement.executeQuery(query);
	    	}catch (SQLException e) {
	    		LoggerUtil.logException(e, log);
	    		throw(e);
	    	}
	    	return resultSet;
    }

    public ResultSet getIsolationLevel() throws Exception {
	    	Connection connection = getConnection();
	    	Statement statement = null;
	    	String query;
	    	ResultSet resultSet = null;
	    	try {
	    		statement = connection.createStatement();
	    		query ="SELECT CASE WHEN (count(DISTINCT scn) > 1) THEN 1 ELSE 0 END AS readCommmit, xid FROM SYS.fga_log$ GROUP BY xid ORDER BY xid";//UNIFIED_AUDIT_TRAIL  SYS.fga_log$
	    		resultSet = statement.executeQuery(query);
	    	}catch (SQLException e) {
	    		LoggerUtil.logException(e, log);
	    		throw(e);
	    	}
	    	return resultSet;
    }
    
    
	
	/**
	 * get the database connection object
	 * 
	 * @return
	 * @throws Exception 
	 */
	public Connection getConnection() throws Exception {
		url = URL_TEMPLATE.replace("<HOST>", DBConfig.inst.getConnectionProperty(ConfigProperty.HOST))
				.replace("<PORT>", DBConfig.inst.getConnectionProperty(ConfigProperty.PORT))
				.replace("<SID>", DBConfig.inst.getConnectionProperty(ConfigProperty.SID));
		user = DBConfig.inst.getConnectionProperty(ConfigProperty.USERNAME);
		password = DBConfig.inst.getConnectionProperty(ConfigProperty.PASSWORD);
		
		try {
			Class.forName(DRIVER);
		} catch (Exception e) {
			log.info("Fail loading Driver!");
			LoggerUtil.logException(e, log);
			throw(e);
		}
		try {
			if (connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection(url, user, password);
			}
			if (connection.isClosed())
				log.error("fail to connecting to the Database!");
		} catch (SQLException e) {
			log.info("Connection URL or username or password errors!");
			LoggerUtil.logException(e, log);
			throw(e);
		}
		return connection;
	}

	/**
	 * exectue a query sql and get the result set
	 * 
	 * @param sql
	 * @return resultset object
	 */
	public ResultSet executeQuery(String sql) {
		ResultSet rs = null;
		if (stmt == null) {
			try {
				stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); //support scroll in resultset!
			} catch (SQLException e) {
				LoggerUtil.logException(e, log);
				log.info("create Statement fail:" + e.toString());
			}
		}
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			LoggerUtil.logException(e, log, "failed executing query");
		}
		return rs;
	}

	/**
	 * Execute a update sql
	 * 
	 * @param sql
	 * @return if execute successful return true or it will return false;
	 */
	@SuppressWarnings("finally")
	public boolean executeUpdate(String sql) {
		boolean v = false;
		try {
			v = stmt.executeUpdate(sql) > 0 ? true : false;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			return v;
		}
	}

	/**
	 * Close statements and connection.
	 */
	public void close() {
		if (stmt != null) {
			try {
				stmt.close(); // close statement
			} catch (SQLException e) {
				LoggerUtil.logException(e, log, "close statement failed");
			}
		}
		if (connection != null) {
			try {
				connection.close(); // close connection
				connection = null;
			} catch (SQLException e) {
				LoggerUtil.logException(e, log, "close connection failed");
			}
		}
		dbManager = null;
	}
}
