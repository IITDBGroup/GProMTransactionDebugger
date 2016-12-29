/**
 * 
 */
package org.gprom.tdebug.db_connection;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author lord_pretzel
 *
 */
public class DBConfig implements DBConfigInterface {

	static Logger log = Logger.getLogger(DBConfig.class);
	
	private Properties p = new Properties();
	
	static public DBConfig inst = new DBConfig();
	
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
			p.load(this.getClass().getClassLoader().getResourceAsStream(DBConfig.configFile));
		}
		catch (IOException e) {
			log.error("did not find configuration tdebug.properties");
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.gprom.tdebug.db_connection.DBConfigInterface#getConnectionProperty(org.gprom.tdebug.db_connection.DBConfigInterface.ConfigProperty)
	 */
	@Override
	public String getConnectionProperty(ConfigProperty prop) {
		return p.getProperty(prop.toString());
	}

	/* (non-Javadoc)
	 * @see org.gprom.tdebug.db_connection.DBConfigInterface#setConnectionProperty(org.gprom.tdebug.db_connection.DBConfigInterface.ConfigProperty, java.lang.String)
	 */
	@Override
	public void setConnectionProperty(ConfigProperty prop, String value) {
		p.setProperty(prop.toString(), value);
	}

}
