/**
 * Created by Kai Yao on 6/17/2016.
 * use this class to run GProm. 
 * Please use constants provided by DBUtility interface 
 * to set GProm database parameters.
 */

package org.gprom.tdebug.cli_process;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.gprom.tdebug.db_connection.DBConfig;
import org.gprom.tdebug.db_connection.DBConfigInterface.AuditTable;
import org.gprom.tdebug.db_connection.DBConfigInterface.ConfigProperty;
import org.gprom.tdebug.db_connection.DBManager;
import org.gprom.util.LoggerUtil;

public class GpromProcess {

	static Logger log = Logger.getLogger(GpromProcess.class);
	
//	private String XID = "";
//	
//	public GpromProcess(String transactionID) {
//		this.XID = transactionID;
//	}
//	

//	public static ResultSet getTransactionIntermediateSQLOutput(String sql) {
//		DBManager dbManager = DBManager.getInstance();
//		if (dbManager.getConnection() == null) 
//		{
//			log.info("connect oracle database failed！");
//		} 
//		else 
//		{
//			//log.info("connect oracle database succeed！");
//		}
//		ResultSet result = dbManager.executeQuery(sql);
//		//dbManager.close();
//		return result;
//	}
	
	
	public static String getTransactionIntermediateSQL(String XID) {
		String auditTable = null;
        //for old gprom version
//		ProcessBuilder pb = new ProcessBuilder("./test/testrewriter",  "-host",  HOST,  "-db",  SID, 
//				"-port", PORT, "-user",  USERNAME,  "-passwd", PASSWORD, "-log", "-loglevel",  "0",  "-sql",
//				"select * from employee;", "-activate", "treefiy_prov_rewrite_input");
		switch(AuditTable.valueOf(DBConfig.inst.getConnectionProperty(ConfigProperty.AUDIT_TABLE)))
		{
		case FGA_LOG:
			auditTable= "fga_log$";
			break;
		case UNIFIED_AUDIT:
			auditTable= "unified_audit";
			break;
		default:
			break;
		}
		// UNIFIED_AUDIT_TRAIL		
//		ProcessBuilder pb = new ProcessBuilder("./src/command_line/gprom",  "-host",  HOST,  "-db",  SID, 
//				"-port", PORT, "-user",  USERNAME,  "-passwd", PASSWORD, "-log", "-loglevel",  "0",  "-sql",
//				"PROVENANCE WITH ONLY UPDATED SHOW INTERMEDIATE OF TRANSACTION '"  + XID + "';", "-backend", "oracle","-Pexecutor", "sql");

		ProcessBuilder pb = new ProcessBuilder("./gprom",  
				"-host",  DBConfig.inst.getConnectionProperty(ConfigProperty.HOST), 
				"-db",  DBConfig.inst.getConnectionProperty(ConfigProperty.SID), 
				"-port", DBConfig.inst.getConnectionProperty(ConfigProperty.PORT), 
				"-user",  DBConfig.inst.getConnectionProperty(ConfigProperty.USERNAME),  
				"-passwd", DBConfig.inst.getConnectionProperty(ConfigProperty.PASSWORD), 
				"-log", "-loglevel",  "0",  
				"-sql", "PROVENANCE WITH ONLY UPDATED SHOW INTERMEDIATE OF TRANSACTION '"  + XID + "';", 
				"-backend", "oracle","-Pexecutor", "sql",
				"-Boracle.audittable", auditTable);

		pb.directory(new File(DBConfig.inst.getConnectionProperty(ConfigProperty.GPROM_PATH)));// Gprom absolute path
		log.info("pb string : "+pb.command().toString());

//		File log = new File("/Users/nebula/Documents/GP_GUI/log.txt");
//		pb.redirectErrorStream(true);
//		pb.redirectOutput(Redirect.appendTo(log));
		 
		Process process = null;
		try {
			process = pb.start(); 

			int errorInt = process.waitFor();
			if (errorInt != 0) {
				log.info("Some error with Gprom Process: " + errorInt);
			}
		} catch (IOException e) {
			LoggerUtil.logException(e,log);
		} catch (InterruptedException e) {
			LoggerUtil.logException(e,log);
		}
		log.info("after try");
		String sql = streamtoString(process.getInputStream()).trim();
		sql = sql.substring(0, sql.length() - 1); //get rid of semicolon
//		log.info("sql length : "+sql.length());
		log.info("sql" + sql);
//		log.info("finish");
		return sql;
	}

	public static String getReenactSQL(String scn, String statements) {
		
		ProcessBuilder pb = new ProcessBuilder("./gprom",  
				"-host",  DBConfig.inst.getConnectionProperty(ConfigProperty.HOST), 
				"-db",  DBConfig.inst.getConnectionProperty(ConfigProperty.SID), 
				"-port", DBConfig.inst.getConnectionProperty(ConfigProperty.PORT), 
				"-user",  DBConfig.inst.getConnectionProperty(ConfigProperty.USERNAME),  
				"-passwd", DBConfig.inst.getConnectionProperty(ConfigProperty.PASSWORD), 
				"-log", "-loglevel",  "0",  
				"-sql", "REENACT WITH ISOLATION LEVEL READCOMMITTED COMMIT SCN " + scn + " PROVENANCE ONLY UPDATED SHOW INTERMEDIATE("  + statements + ");", 
				"-backend", "oracle","-Pexecutor", "sql");

		pb.directory(new File(DBConfig.inst.getConnectionProperty(ConfigProperty.GPROM_PATH)));// Gprom absolute path
		log.info("pb string : "+pb.command().toString());
		
		Process process = null;
		try {
			process = pb.start(); 

			int errorInt = process.waitFor();
			if (errorInt != 0) {
				log.info("Some error with Gprom Process: " + errorInt);
			}
		} catch (IOException e) {
			LoggerUtil.logException(e,log);
		} catch (InterruptedException e) {
			LoggerUtil.logException(e,log);
		}
		log.info("after try");
		String sql = streamtoString(process.getInputStream()).trim();
		sql = sql.substring(0, sql.length() - 1); //get rid of semicolon
//		log.info("sql length : "+sql.length());
		log.info("sql" + sql);
		
		return sql;
	}
	
	
public static String getSerializableReenactSQL(String scn, String statements) {
		
		ProcessBuilder pb = new ProcessBuilder("./gprom",  
				"-host",  DBConfig.inst.getConnectionProperty(ConfigProperty.HOST), 
				"-db",  DBConfig.inst.getConnectionProperty(ConfigProperty.SID), 
				"-port", DBConfig.inst.getConnectionProperty(ConfigProperty.PORT), 
				"-user",  DBConfig.inst.getConnectionProperty(ConfigProperty.USERNAME),  
				"-passwd", DBConfig.inst.getConnectionProperty(ConfigProperty.PASSWORD), 
				"-log", "-loglevel",  "0",  
				"-sql", "REENACT AS OF SCN " + scn + " WITH PROVENANCE ONLY UPDATED SHOW INTERMEDIATE("  + statements + ");", 
				"-backend", "oracle","-Pexecutor", "sql");

		pb.directory(new File(DBConfig.inst.getConnectionProperty(ConfigProperty.GPROM_PATH)));// Gprom absolute path
		log.info("pb string : "+pb.command().toString());
		
		Process process = null;
		try {
			process = pb.start(); 

			int errorInt = process.waitFor();
			if (errorInt != 0) {
				log.info("Some error with Gprom Process: " + errorInt);
			}
		} catch (IOException e) {
			LoggerUtil.logException(e,log);
		} catch (InterruptedException e) {
			LoggerUtil.logException(e,log);
		}
		log.info("after try");
		String sql = streamtoString(process.getInputStream()).trim();
		sql = sql.substring(0, sql.length() - 1); //get rid of semicolon
//		log.info("sql length : "+sql.length());
		log.info("sql" + sql);
		
		return sql;
	}
	
	public static String getSerializableReenactAllSQL(String scn, String statements) {
		
		ProcessBuilder pb = new ProcessBuilder("./gprom",  
				"-host",  DBConfig.inst.getConnectionProperty(ConfigProperty.HOST), 
				"-db",  DBConfig.inst.getConnectionProperty(ConfigProperty.SID), 
				"-port", DBConfig.inst.getConnectionProperty(ConfigProperty.PORT), 
				"-user",  DBConfig.inst.getConnectionProperty(ConfigProperty.USERNAME),  
				"-passwd", DBConfig.inst.getConnectionProperty(ConfigProperty.PASSWORD), 
				"-log", "-loglevel",  "0",  
				"-sql", "REENACT AS OF SCN " + scn + " WITH PROVENANCE SHOW INTERMEDIATE ("  + statements + ");", 
				"-backend", "oracle","-Pexecutor", "sql");

		pb.directory(new File(DBConfig.inst.getConnectionProperty(ConfigProperty.GPROM_PATH)));// Gprom absolute path
		log.info("pb string : "+pb.command().toString());
		
		Process process = null;
		try {
			process = pb.start(); 

			int errorInt = process.waitFor();
			if (errorInt != 0) {
				log.info("Some error with Gprom Process: " + errorInt);
			}
		} catch (IOException e) {
			LoggerUtil.logException(e,log);
		} catch (InterruptedException e) {
			LoggerUtil.logException(e,log);
		}
		log.info("after try");
		String sql = streamtoString(process.getInputStream()).trim();
		sql = sql.substring(0, sql.length() - 1); //get rid of semicolon
//		log.info("sql length : "+sql.length());
		log.info("sql" + sql);
		
		return sql;
	}
	
	public static String getReenactAllSQL(String scn, String statements) {
		
		ProcessBuilder pb = new ProcessBuilder("./gprom",  
				"-host",  DBConfig.inst.getConnectionProperty(ConfigProperty.HOST), 
				"-db",  DBConfig.inst.getConnectionProperty(ConfigProperty.SID), 
				"-port", DBConfig.inst.getConnectionProperty(ConfigProperty.PORT), 
				"-user",  DBConfig.inst.getConnectionProperty(ConfigProperty.USERNAME),  
				"-passwd", DBConfig.inst.getConnectionProperty(ConfigProperty.PASSWORD), 
				"-log", "-loglevel",  "0",  
				"-sql", "REENACT WITH ISOLATION LEVEL READCOMMITTED COMMIT SCN " + scn +" PROVENANCE SHOW INTERMEDIATE ("  + statements + ");", 
				"-backend", "oracle","-Pexecutor", "sql");

		pb.directory(new File(DBConfig.inst.getConnectionProperty(ConfigProperty.GPROM_PATH)));// Gprom absolute path
		log.info("pb string : "+pb.command().toString());
		
		Process process = null;
		try {
			process = pb.start(); 

			int errorInt = process.waitFor();
			if (errorInt != 0) {
				log.info("Some error with Gprom Process: " + errorInt);
			}
		} catch (IOException e) {
			LoggerUtil.logException(e,log);
		} catch (InterruptedException e) {
			LoggerUtil.logException(e,log);
		}
		log.info("after try");
		String sql = streamtoString(process.getInputStream()).trim();
		sql = sql.substring(0, sql.length() - 1); //get rid of semicolon
//		log.info("sql length : "+sql.length());
		log.info("sql" + sql);
		
		return sql;
	}
	
	
	
	private static String streamtoString(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
	            br = new BufferedReader(new InputStreamReader(inputStream));
	            String line = null;
	            while ((line = br.readLine()) != null) {
	                sb.append(line + " ");// + System.getProperty("line.separator")
	            }
	            br.close();
	        } catch(IOException e) {
	        	LoggerUtil.logException(e,log);
	        }
        return sb.toString().trim();
    }

}
