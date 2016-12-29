package org.gprom.tdebug.db_connection;

import java.io.IOException;
import java.util.Properties;

public interface DBConfigInterface {
	
	public static final String configFile = "tdebug.properties"; 
	
	public enum AuditTable {
		FGA_LOG,
		UNIFIED_AUDIT
	}
	
	public enum ConfigProperty {
		HOST,
		SID,
		PORT,
		USERNAME,
		PASSWORD,
		AUDIT_TABLE,
		GPROM_PATH
	}
	
	public static final String DEFAULT_HOST = "127.0.0.1";
	public static final String DEFAULT_SID = "orcl";
	public static final String DEFAULT_PORT = "1521";
	public static final String DEFAULT_USERNAME = "gprom";
	public static final String DEFAULT_PASSWORD = "gprom";
	public static final AuditTable DEFAULT_AUDIT_TABLE = AuditTable.FGA_LOG;
	public static final String DEFAUL_GPROM_PATH = "/usr/local/bin/gprom";
	
	public void setProperties (Properties p);
	public Properties getProperties();
	public void loadProperties() throws IOException;
	public String getConnectionProperty(ConfigProperty prop);
	public void setConnectionProperty(ConfigProperty prop, String value);
			
}
