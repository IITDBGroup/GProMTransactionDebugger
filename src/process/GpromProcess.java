/**
 * Created by Kai Yao on 6/17/2016.
 * use this class to run GProm. 
 * Please use constants provided by DBUtility interface 
 * to set GProm database parameters.
 */

package process;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.sql.ResultSet;

import dbConnection.DBManager;
import dbConnection.DBUtility;

public class GpromProcess implements DBUtility{

//	private String XID = "";
//	
//	public GpromProcess(String transactionID) {
//		this.XID = transactionID;
//	}
//	

	public static ResultSet getTransactionIntermediateSQLOutput(String sql) {
		//sql = "select * from company"; //fake table
		DBManager dbManager = DBManager.getInstance();
		if (dbManager.getConnection() == null) 
		{
			System.out.println("connect oracle database failed！");
		} 
		else 
		{
			//System.out.println("connect oracle database succeed！");
		}
		ResultSet result = dbManager.executeQuery(sql);
		//dbManager.close();
		return result;
	}
	
	
	public static String getTransactionIntermediateSQL(String XID) {
        //for old gprom version
//		ProcessBuilder pb = new ProcessBuilder("./test/testrewriter",  "-host",  HOST,  "-db",  SID, 
//				"-port", PORT, "-user",  USERNAME,  "-passwd", PASSWORD, "-log", "-loglevel",  "0",  "-sql",
//				"select * from employee;", "-activate", "treefiy_prov_rewrite_input");

        //for VM		
//		ProcessBuilder pb = new ProcessBuilder("./src/command_line/gprom",  "-host",  HOST,  "-db",  SID, 
//				"-port", PORT, "-user",  USERNAME,  "-passwd", PASSWORD, "-log", "-loglevel",  "0",  "-sql",
//				"PROVENANCE WITH ONLY UPDATED SHOW INTERMEDIATE OF TRANSACTION '"  + XID + "';", "-backend", "oracle","-Pexecutor", "sql");

		//for dockor
		ProcessBuilder pb = new ProcessBuilder("./src/command_line/gprom",  "-host",  HOST,  "-db",  SID, 
				"-port", PORT, "-user",  USERNAME,  "-passwd", PASSWORD, "-log", "-loglevel",  "0",  "-sql",
				"PROVENANCE WITH ONLY UPDATED SHOW INTERMEDIATE OF TRANSACTION '"  + XID + "';", "-backend", "oracle","-Pexecutor", "sql",
				"-Boracle.audittable", "fga_log$");

				pb.directory(new File("/Users/nebula/Documents/GP_GUI/provenance-rewriter-prototype/"));// Gprom absolute path
		//System.out.println("pb string : "+pb.command().toString());
		
//		File log = new File("/Users/nebula/Documents/GP_GUI/log.txt");
//		pb.redirectErrorStream(true);
//		pb.redirectOutput(Redirect.appendTo(log));
		 
		Process process = null;
		try {
			process = pb.start(); 

			int errorInt = process.waitFor();
			if (errorInt != 0) {
				System.out.println("Some error with Gprom Process: " + errorInt);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("after try");
		String sql = streamtoString(process.getInputStream()).trim();
		sql = sql.substring(0, sql.length() - 1); //get rid of semicolon
//		System.out.println("sql length : "+sql.length());
		System.out.println("sql" + sql);
//		System.out.println("finish");
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
	        	e.printStackTrace();
	        }
        return sb.toString().trim();
    }

}
