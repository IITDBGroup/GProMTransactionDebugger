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
	
	private static GpromProcess inst = new GpromProcess();
	
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
	
	
	public static String getTransactionIntermediateSQL(String XID) throws Exception {
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

//		ProcessBuilder pb = new ProcessBuilder("./gprom",  
//				"-host",  DBConfig.inst.getConnectionProperty(ConfigProperty.HOST), 
//				"-db",  DBConfig.inst.getConnectionProperty(ConfigProperty.SID), 
//				"-port", DBConfig.inst.getConnectionProperty(ConfigProperty.PORT), 
//				"-user",  DBConfig.inst.getConnectionProperty(ConfigProperty.USERNAME),  
//				"-passwd", DBConfig.inst.getConnectionProperty(ConfigProperty.PASSWORD), 
//				"-log", "-loglevel",  "0",  
//				"-sql", "PROVENANCE WITH ONLY UPDATED SHOW INTERMEDIATE STATEMENT ANNOTATIONS OF TRANSACTION '"  + XID + "';", 
//				"-backend", "oracle","-Pexecutor", "sql",
//				"-Boracle.audittable", auditTable);
//
//		pb.directory(new File(DBConfig.inst.getConnectionProperty(ConfigProperty.GPROM_PATH)));// Gprom absolute path
//		log.info("pb string : "+pb.command().toString());
//		log.info("pb dir: " + pb.directory());
//
////		File log = new File("/Users/nebula/Documents/GP_GUI/log.txt");
////		pb.redirectErrorStream(true);
////		pb.redirectOutput(Redirect.appendTo(log));
//		 
//		Process process = null;
//		try {
//			process = pb.start(); 
//
//			int errorInt = process.waitFor();
//			if (errorInt != 0) {
//				log.info("Some error with Gprom Process: " + errorInt);
//			}
//		} catch (IOException e) {
//			LoggerUtil.logException(e,log);
//		} catch (InterruptedException e) {
//			LoggerUtil.logException(e,log);
//		}
//		log.info("after try");
//		String sql = streamtoString(process.getInputStream()).trim();
//		sql = sql.substring(0, sql.length() - 1); //get rid of semicolon
////		log.info("sql length : "+sql.length());
//		log.info("sql" + sql);
////		log.info("finish");
//		return sql;
//		
		 
		StringBuilder out = new StringBuilder();
		String command = "PROVENANCE WITH ONLY UPDATED SHOW INTERMEDIATE STATEMENT ANNOTATIONS OF TRANSACTION '"  + XID + "';";
		
		runGProM(command, out);
		
		return out.toString();
	}

	public static String getReenactSQL(String scn, String statements) throws Exception {
//		
//		ProcessBuilder pb = new ProcessBuilder("./gprom",  
//				"-host",  DBConfig.inst.getConnectionProperty(ConfigProperty.HOST), 
//				"-db",  DBConfig.inst.getConnectionProperty(ConfigProperty.SID), 
//				"-port", DBConfig.inst.getConnectionProperty(ConfigProperty.PORT), 
//				"-user",  DBConfig.inst.getConnectionProperty(ConfigProperty.USERNAME),  
//				"-passwd", DBConfig.inst.getConnectionProperty(ConfigProperty.PASSWORD), 
//				"-log", "-loglevel",  "0",  
//				"-sql", "REENACT WITH ISOLATION LEVEL READCOMMITTED COMMIT SCN " + scn + " PROVENANCE ONLY UPDATED SHOW INTERMEDIATE STATEMENT ANNOTATIONS ("  + statements + ");", 
//				"-backend", "oracle","-Pexecutor", "sql");
//
//		pb.directory(new File(DBConfig.inst.getConnectionProperty(ConfigProperty.GPROM_PATH)));// Gprom absolute path
//		log.info("pb string : "+pb.command().toString());
//		
//		Process process = null;
//		try {
//			process = pb.start(); 
//
//			int errorInt = process.waitFor();
//			if (errorInt != 0) {
//				log.info("Some error with Gprom Process: " + errorInt);
//			}
//		} catch (IOException e) {
//			LoggerUtil.logException(e,log);
//		} catch (InterruptedException e) {
//			LoggerUtil.logException(e,log);
//		}
//		log.info("after try");
//		String sql = streamtoString(process.getInputStream()).trim();
//		sql = sql.substring(0, sql.length() - 1); //get rid of semicolon
////		log.info("sql length : "+sql.length());
//		log.info("sql" + sql);
//		
//		return sql;
//		
		StringBuilder out = new StringBuilder();
		String command = "REENACT WITH ISOLATION LEVEL READCOMMITTED COMMIT SCN " + scn + " PROVENANCE ONLY UPDATED SHOW INTERMEDIATE STATEMENT ANNOTATIONS ("  + statements + ");";
		
		runGProM(command, out);
		
		return out.toString();
	}
	
	
public static String getSerializableReenactSQL(String scn, String statements) throws Exception {
		
//		ProcessBuilder pb = new ProcessBuilder("./gprom",  
//				"-host",  DBConfig.inst.getConnectionProperty(ConfigProperty.HOST), 
//				"-db",  DBConfig.inst.getConnectionProperty(ConfigProperty.SID), 
//				"-port", DBConfig.inst.getConnectionProperty(ConfigProperty.PORT), 
//				"-user",  DBConfig.inst.getConnectionProperty(ConfigProperty.USERNAME),  
//				"-passwd", DBConfig.inst.getConnectionProperty(ConfigProperty.PASSWORD), 
//				"-log", "-loglevel",  "0",  
//				"-sql", "REENACT AS OF SCN " + scn + " WITH PROVENANCE ONLY UPDATED SHOW INTERMEDIATE STATEMENT ANNOTATIONS ("  + statements + ");", 
//				"-backend", "oracle","-Pexecutor", "sql");
//
//		pb.directory(new File(DBConfig.inst.getConnectionProperty(ConfigProperty.GPROM_PATH)));// Gprom absolute path
//		log.info("pb string : "+pb.command().toString());
//		
//		Process process = null;
//		try {
//			process = pb.start(); 
//
//			int errorInt = process.waitFor();
//			if (errorInt != 0) {
//				log.info("Some error with Gprom Process: " + errorInt);
//			}
//		} catch (IOException e) {
//			LoggerUtil.logException(e,log);
//		} catch (InterruptedException e) {
//			LoggerUtil.logException(e,log);
//		}
//		log.info("after try");
//		String sql = streamtoString(process.getInputStream()).trim();
//		sql = sql.substring(0, sql.length() - 1); //get rid of semicolon
////		log.info("sql length : "+sql.length());
//		log.info("sql" + sql);
//		
//		return sql;	

		StringBuilder out = new StringBuilder();
		String command = "REENACT AS OF SCN " + scn + " WITH PROVENANCE ONLY UPDATED SHOW INTERMEDIATE STATEMENT ANNOTATIONS ("  + statements + ");";
		
		runGProM(command, out);
		
		return out.toString();
	}
	
	public static String getSerializableReenactAllSQL(String scn, String statements) throws Exception {
		
//		ProcessBuilder pb = new ProcessBuilder("./gprom",  
//				"-host",  DBConfig.inst.getConnectionProperty(ConfigProperty.HOST), 
//				"-db",  DBConfig.inst.getConnectionProperty(ConfigProperty.SID), 
//				"-port", DBConfig.inst.getConnectionProperty(ConfigProperty.PORT), 
//				"-user",  DBConfig.inst.getConnectionProperty(ConfigProperty.USERNAME),  
//				"-passwd", DBConfig.inst.getConnectionProperty(ConfigProperty.PASSWORD), 
//				"-log", "-loglevel",  "0",  
//				"-sql", "REENACT AS OF SCN " + scn + " WITH PROVENANCE SHOW INTERMEDIATE STATEMENT ANNOTATIONS("  + statements + ");", 
//				"-backend", "oracle","-Pexecutor", "sql");
//
//		pb.directory(new File(DBConfig.inst.getConnectionProperty(ConfigProperty.GPROM_PATH)));// Gprom absolute path
//		log.info("pb string : "+pb.command().toString());
//		
//		Process process = null;
//		try {
//			process = pb.start(); 
//
//			int errorInt = process.waitFor();
//			if (errorInt != 0) {
//				log.info("Some error with Gprom Process: " + errorInt);
//			}
//		} catch (IOException e) {
//			LoggerUtil.logException(e,log);
//		} catch (InterruptedException e) {
//			LoggerUtil.logException(e,log);
//		}
//		log.info("after try");
//		String sql = streamtoString(process.getInputStream()).trim();
//		sql = sql.substring(0, sql.length() - 1); //get rid of semicolon
////		log.info("sql length : "+sql.length());
//		log.info("sql" + sql);
//		
		StringBuilder out = new StringBuilder();
		String command = "REENACT AS OF SCN " + scn + " WITH PROVENANCE SHOW INTERMEDIATE STATEMENT ANNOTATIONS("  + statements + ");";
		
		runGProM(command, out);
		
		return out.toString();
	}
	
	public static String getReenactAllSQL(String scn, String statements) throws Exception {
//		
//		ProcessBuilder pb = new ProcessBuilder("./gprom",  
//				"-host",  DBConfig.inst.getConnectionProperty(ConfigProperty.HOST), 
//				"-db",  DBConfig.inst.getConnectionProperty(ConfigProperty.SID), 
//				"-port", DBConfig.inst.getConnectionProperty(ConfigProperty.PORT), 
//				"-user",  DBConfig.inst.getConnectionProperty(ConfigProperty.USERNAME),  
//				"-passwd", DBConfig.inst.getConnectionProperty(ConfigProperty.PASSWORD), 
//				"-log", "-loglevel",  "0",  
//				"-sql", "REENACT WITH ISOLATION LEVEL READCOMMITTED COMMIT SCN " + scn +" PROVENANCE SHOW INTERMEDIATE STATEMENT ANNOTATIONS ("  + statements + ");", 
//				"-backend", "oracle","-Pexecutor", "sql");
//
//		pb.directory(new File(DBConfig.inst.getConnectionProperty(ConfigProperty.GPROM_PATH)));// Gprom absolute path
//		log.info("pb string : "+pb.command().toString());
//		
//		Process process = null;
//		try {
//			process = pb.start(); 
//
//			int errorInt = process.waitFor();
//			if (errorInt != 0) {
//				log.info("Some error with Gprom Process: " + errorInt);
//			}
//		} catch (IOException e) {
//			LoggerUtil.logException(e,log);
//		} catch (InterruptedException e) {
//			LoggerUtil.logException(e,log);
//		}
//		log.info("after try");
//		String sql = streamtoString(process.getInputStream()).trim();
//		sql = sql.substring(0, sql.length() - 1); //get rid of semicolon
////		log.info("sql length : "+sql.length());
//		log.info("sql" + sql);
//		
		StringBuilder out = new StringBuilder();
		String command =  "REENACT WITH ISOLATION LEVEL READCOMMITTED COMMIT SCN " + scn +" PROVENANCE SHOW INTERMEDIATE STATEMENT ANNOTATIONS ("  + statements + ");";
		
		runGProM(command, out);
		
		return out.toString();
	}
	
	
	
//	private static String streamtoString(InputStream inputStream) {
//        StringBuilder sb = new StringBuilder();
//        BufferedReader br = null;
//        try {
//	            br = new BufferedReader(new InputStreamReader(inputStream));
//	            String line = null;
//	            while ((line = br.readLine()) != null) {
//	                sb.append(line + " ");// + System.getProperty("line.separator")
//	            }
//	            br.close();
//	        } catch(IOException e) {
//	        	LoggerUtil.logException(e,log);
//	        }
//        return sb.toString().trim();
//    }
	private static int runGProM (String command, StringBuilder out) throws Exception {
		int retVal = runGProMRaw(command, out);
		out.deleteCharAt(out.lastIndexOf(";"));
		if (retVal == 0)
			log.info("SQL - " + out.toString());
		return retVal;
	}

	private static int runGProMRaw (String command, StringBuilder out) throws Exception {
		int returnVal = -1;
		StringBuilder err = new StringBuilder();
		
		ProcessBuilder pb = new ProcessBuilder("./gprom",  
				"-host",  DBConfig.inst.getConnectionProperty(ConfigProperty.HOST), 
				"-db",  DBConfig.inst.getConnectionProperty(ConfigProperty.SID), 
				"-port", DBConfig.inst.getConnectionProperty(ConfigProperty.PORT), 
				"-user",  DBConfig.inst.getConnectionProperty(ConfigProperty.USERNAME),  
				"-passwd", DBConfig.inst.getConnectionProperty(ConfigProperty.PASSWORD), 
				"-log", "-loglevel",  "0",  
				"-sql", command, 
				"-backend", "oracle","-Pexecutor", "sql");

		File path = new File(DBConfig.inst.getConnectionProperty(ConfigProperty.GPROM_PATH));
		File gpromLoc = new File(path, "gprom");
		String libPathEnvVar;
		
		// get name of variable for dynamic link library path
		log.info("OS: " + System.getProperty("os.name"));
		if (System.getProperty("os.name").contains("Mac"))
			libPathEnvVar = "DYLD_LIBRARY_PATH";
		else
			libPathEnvVar = "LD_LIBRARY_PATH";
		
		// output environment variables relevant for linking Oracle libraries
		if (DBConfig.inst.hasConnectionProperty(ConfigProperty.LD_LIBRARY_PATH)) {
			String val = DBConfig.inst.getConnectionProperty(ConfigProperty.LD_LIBRARY_PATH);
			
			pb.environment().put(libPathEnvVar, val);
			log.info("override " + libPathEnvVar + "=" + val);
		}
		
		if (pb.environment().containsKey(libPathEnvVar)) {
			log.info("DYLD_LIBRARY_PATH=<" + pb.environment().get(libPathEnvVar) + ">");
		}
		
		// check that gprom binary exists
		if (!gpromLoc.exists() || !gpromLoc.isFile())
			throw new Exception("gprom binary not found at <" + gpromLoc.getAbsolutePath() + ">");
		
		log.debug("run gprom command:\n\n" + command);	
		
		pb.directory(path);// Gprom absolute path
		log.info("pb dir <" + pb.directory().getAbsolutePath() + ">\n"+pb.command().toString());
		
		Process process = null;
		try {
			ProcStreamReader outR;
			ProcStreamReader errR;
			
			// start gprom and spawn threads that read from std error and std output
			process = pb.start(); 
			outR = inst.new ProcStreamReader("out", process.getInputStream(), out);
			errR = inst.new ProcStreamReader("err", process.getErrorStream(), err);
			outR.start();
			errR.start();
			
			returnVal = process.waitFor();
			if (returnVal != 0) {
				log.fatal("Some error with Gprom Process - return value:" + returnVal);
				log.fatal("Output was: " + out.toString());
				log.fatal("Error output was " + err.toString());
				throw new Exception("GProM error");
			}
		} catch (IOException e) {
			LoggerUtil.logException(e,log);
		} catch (InterruptedException e) {
			LoggerUtil.logException(e,log);
		}
		
		return returnVal;
	}
	
	private class ProcStreamReader extends Thread {
		InputStream is;
		String type;
		StringBuilder b;

		ProcStreamReader(String type, InputStream is, StringBuilder b) {
			this.type = type;
			this.is = is;
			this.b = b;
		}

		public void run() {
			try	{
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line=null;
				while ( (line = br.readLine()) != null)
				{
					if (b != null)
						b.append(line + "\n");
					log.debug(type + ">" + line);    
				}
			} catch (IOException ioe)
			{
				ioe.printStackTrace();  
			}
		}
	}

}
