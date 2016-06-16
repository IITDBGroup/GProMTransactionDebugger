/**
 * 
 */
package timebars.eventmonitoring.swing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
//import org.sesam.utils.swt.PropertyManager; 
//
//import org.sesame.utils.PropertyManager;

//import java.util.Properties;

//        /Users/xun/Documents/java_workspace/gprom-gui/dotin.dot


/**
 *
 * Part of Project PermBrowser
 * @author Boris Glavic
 *
 */
public class DotWrapper {

	static Logger log = Logger.getLogger (DotWrapper.class.getName());
	
	public static final String DOTIN_FILENAME = "dotin.dot";
	public static final String DOTOUT_FILENAME = "dotout.png";
	
	private ProcessBuilder dotCommand;
	private Process dotProcess;
	private StringBuffer output;
	
	private ProcessBuilder getDotCommand;
	
	public DotWrapper () {
		
	}
	
	public void init () throws URISyntaxException, IOException {
		File inFile;
		File outFile;
		String dotPath;
		
//		String runDotPath;
		
//		runDotPath = "/Users/xun/Documents/java_workspace/GPROM/provenance-rewriter-prototype/test/run1.sh";
//		inFile = getDotFile(DOTIN_FILENAME);
//		Process process = null;        
//        try {
//              process = Runtime.getRuntime().exec(runDotPath);
//              process.waitFor();             
//        } catch (Exception e) {        
//              System.out.println("runtime erroe");
//        } finally {
//            try {
//                  process.destroy();             
//            } catch (Exception e) {        
//              }
//        }
		
		//./testrewriter -host ligeti.cs.iit.edu -db orcl -port 1521 -user fga_user 
		//-passwd fga -log -loglevel 4 -activate treefiy_prov_rewrite_input -sql "SELECT * FROM R;"
		
		
		
//		getDotCommand = new ProcessBuilder ();
		//getDotCommand.command(runDotPath, "-host ligeti.cs.iit.edu -db orcl -port 1521 -user fga_user -passwd fga -log -loglevel 0 -activate treefiy_prov_rewrite_input -sql \"SELECT * FROM R;\" -activate graphviz >> ", runInFile.getAbsolutePath());
//		getDotCommand.command(runDotPath, "-host", "ligeti.cs.iit.edu", "-db", "orcl", "-port", "1521", "-user", "fga_user", "-passwd", "fga", "-log", "-loglevel", "0", "-activate", "treefiy_prov_rewrite_input", "-sql", "\"SELECT * FROM R;\"", ">" , inFile.getAbsolutePath());
//		getDotCommand.command(runDotPath, inFile.getAbsolutePath());

//		Process p1 = getDotCommand.start();
		//p1.waitFor();
//		dotPath = PropertyManager.getInstance().getProperty("DotCommand");
		
//		dotPath = "/usr/local/bin/dot";
//		dotPath = "/usr/local/Cellar/graphviz/2.38.0/bin/dot"; 
		dotPath = "/usr/bin/dot";
		// graphviz path
		
		inFile = getDotFile(DOTIN_FILENAME);
		outFile = getDotFile(DOTOUT_FILENAME);
		System.out.println(inFile);
		System.out.println(outFile);
		dotCommand = new ProcessBuilder ();
		dotCommand.command(dotPath, "-Tpng", "-o", outFile.getAbsolutePath(), inFile.getAbsolutePath());
		
		Process p = dotCommand.start();
		
		log.debug(dotCommand.command().toString());
	}
	
	public void runDot (String commands) throws Exception {
		InputStream dotOut;
		InputStream dotErr;
		OutputStream dotIn;
		BufferedWriter commandFile;
		int e;
		
		output = new StringBuffer ();
		commandFile = new BufferedWriter (new FileWriter (getDotFile(DOTIN_FILENAME)));
		commandFile.write(commands);
		commandFile.close();
		
		dotProcess = dotCommand.start();
		
		dotOut = dotProcess.getInputStream();
		dotErr = dotProcess.getErrorStream();
		dotIn = dotProcess.getOutputStream();
		
		while((e = getExitValue ()) == -1) {
			Thread.sleep(100);
			readStreams (dotOut,dotErr);
		}
		
		readStreams (dotOut, dotErr);
		
		log.debug("dot returned exit value: " + e);
		log.debug(output);
		
		if (e != 0)
			throw new Exception ("An error occured in dot execution: " + output);
	}
	
	private File getDotFile (String name) throws URISyntaxException, IOException {
//		URI dirUri;
//		URI uri;
		File result;
		
//		dirUri = ClassLoader.getSystemResource("dottemp/").toURI(); 
//		uri = URI.create(dirUri.toString() + name);
//		uri = URI.create("/Users/xun/Documents/java_workspace/gprom-gui/" + name);
		
		
//		result = new File("/Users/xun/Documents/java_workspace/gprom-gui/" + name);
		result = new File(this.getClass().getResource("/").getPath() + name); // use relative path
//		System.out.println(this.getClass().getResource("/").getPath());
//		System.out.println("hello");
		if(!result.exists())
			result.createNewFile();
		
		return result;
	}
	
	private void readStreams (InputStream out, InputStream err) throws IOException {
		int c;
		
		while ((c = out.read()) != -1)
			output.append((char) c);
		
		while ((c = err.read()) != -1)
			output.append((char) c);
	}
	
	private int getExitValue () {
		int exitValue;
		
		try {
			log.debug("get exit value");
			exitValue = dotProcess.exitValue();
			log.debug("exit value finished");
			return exitValue;
		}
		catch (IllegalThreadStateException e) {
			log.debug("exit value finished");
			return -1;
		}
	}
	
}






//public int execute()  
//{  
//    int rs = 0;  
//    String[] cmds = {...};//command and arg    
//    ProcessBuilder builder = new ProcessBuilder(cmds);    
//    builder.redirectErrorStream(true);    
//    Process process = builder.start();    
//    BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));    
//    String output = null;    
//    while (null != br.readLine())  
//    {    
//        System.out.print(output);     
//    }    
//    rs = process.waitFor();  
//    return rs;  
//} 










