package gui;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.jaret.util.ui.timebars.*;




public class DBConnection {
	
   
    
    public static ResultSet getData(){
    	Connection connection = getConnection();
    	Statement statement = null;
    	String query;
    	ResultSet resultSet = null;
    	
//    	ROWNUM <=1000 AND
		try {
			statement = connection.createStatement();
			query ="SELECT * FROM UNIFIED_AUDIT_TRAIL  WHERE  EVENT_TIMESTAMP > to_date('02/18/2016', 'MM/DD/YYYY') ORDER BY EVENT_TIMESTAMP ASC";//UNIFIED_AUDIT_TRAIL  SYS.fga_log$
        	resultSet = statement.executeQuery(query);
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSet;
    }
    
    public static ResultSet getDataCommit(){
    	Connection connection = getConnection();
    	Statement statement = null;
    	String query;
    	ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			query ="SELECT DISTINCT VERSIONS_XID, VERSIONS_STARTTIME FROM hr.employee VERSIONS BETWEEN SCN MINVALUE AND MAXVALUE ORDER BY VERSIONS_XID";//UNIFIED_AUDIT_TRAIL  SYS.fga_log$
        	resultSet = statement.executeQuery(query);
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSet;
    }

    private static Connection getConnection() {
    	Connection connection= null;
    	try {
             	Class.forName("oracle.jdbc.driver.OracleDriver");
             	connection = DriverManager.getConnection(
                                "jdbc:oracle:thin:@104.194.106.54:1521/orcl", "sys as sysdba", "oracle");//104.194.111.226 需要根据自己的机器来配置ip
    	} catch (ClassNotFoundException e) {
             	e.printStackTrace();
    	} catch (SQLException e) {
    			e.printStackTrace();
    	}
    	return connection;
	}
}