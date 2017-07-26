/**
 * 
 */
package org.gprom.tdebug.db_connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.gprom.tdebug.db_connection.DBConfigInterface.AuditTable;
import org.gprom.tdebug.main.CLIOptions;

/**
 * @author lord_pretzel
 *
 */
public class DBConfig implements DBConfigInterface {

	static Logger log = Logger.getLogger(DBConfig.class);
	
	private Properties p = new Properties();
	
	public static final Map<AuditTable,String> auditTableNames = new HashMap<AuditTable, String> ();
	
	static {
		auditTableNames.put(AuditTable.FGA_LOG, "FGA_LOG$");
		auditTableNames.put(AuditTable.UNIFIED_AUDIT, "UNIFIED_AUDIT_TRAIL");
	}
	
	public static DBConfig inst = new DBConfig();
	
	
	
	private DBConfig() {
		p.setProperty(ConfigProperty.AUDIT_TABLE.toString(), DEFAULT_AUDIT_TABLE.toString());
		p.setProperty(ConfigProperty.HOST.toString(), DEFAULT_HOST);
		p.setProperty(ConfigProperty.PASSWORD.toString(), DEFAULT_PASSWORD);
		p.setProperty(ConfigProperty.PORT.toString(), DEFAULT_PORT);
		p.setProperty(ConfigProperty.SID.toString(), DEFAULT_SID);
		p.setProperty(ConfigProperty.USERNAME.toString(), DEFAULT_USERNAME);
		p.setProperty(ConfigProperty.GPROM_PATH.toString(), DEFAUL_GPROM_PATH);
	}
	
	/* (non-Javadoc)
	 * @see org.gprom.tdebug.db_connection.DBConfigInterface#setProperties(java.util.Properties)
	 */
	@Override
	public void setProperties(Properties p) {
		this.p = p;
	}

	/* (non-Javadoc)
	 * @see org.gprom.tdebug.db_connection.DBConfigInterface#getProperties()
	 */
	@Override
	public Properties getProperties() {
		return p;
	}

	/* (non-Javadoc)
	 * @see org.gprom.tdebug.db_connection.DBConfigInterface#loadProperties()
	 */
	@Override
	public void loadProperties()  {
		try {
			File confFile = CLIOptions.getInst().getConfigFilePath();
			
			if (confFile == null) {
				p.load(this.getClass().getClassLoader().getResourceAsStream(DBConfig.configFile));
			}
			else {
				if (!confFile.exists()) {
					System.err.println("configuration file does not exist <" + confFile.getAbsolutePath() + ">");
					System.exit(1);
				}
				p.load(new FileInputStream(confFile));
			}
		}
		catch (IOException e) {
			log.error("did not find configuration file tdebug.properties");
			e.printStackTrace();
			System.exit(1);
		}
	}

	/* (non-Javadoc)
	 * @see org.gprom.tdebug.db_connection.DBConfigInterface#getConnectionProperty(org.gprom.tdebug.db_connection.DBConfigInterface.ConfigProperty)
	 */
	@Override
	public String getConnectionProperty(ConfigProperty prop) {
		return p.getProperty(prop.toString());
	}
	
	public boolean hasConnectionProperty(ConfigProperty prop) {
		return p.containsKey(prop.toString());
	}

	/* (non-Javadoc)
	 * @see org.gprom.tdebug.db_connection.DBConfigInterface#setConnectionProperty(org.gprom.tdebug.db_connection.DBConfigInterface.ConfigProperty, java.lang.String)
	 */
	@Override
	public void setConnectionProperty(ConfigProperty prop, String value) {
		p.setProperty(prop.toString(), value);
	}

	/* (non-Javadoc)
	 * @see org.gprom.tdebug.db_connection.DBConfigInterface#getAuditTableName(org.gprom.tdebug.db_connection.DBConfigInterface.AuditTable)
	 */
	@Override
	public String getAuditTableName(String a) {
		return auditTableNames.get(AuditTable.valueOf(a));
	}

}
