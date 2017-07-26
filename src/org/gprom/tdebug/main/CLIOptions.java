/**
 * 
 */
package org.gprom.tdebug.main;


import java.io.File;

import org.kohsuke.args4j.Option;

/**
 * @author lord_pretzel
 *
 */
public class CLIOptions {

	@Option(name = "-help", usage = "show this help message")
	private boolean showHelp = false;
	
	@Option(name = "-c", usage = "location of configuration file")
	private File configFilePath = null;

	private static CLIOptions inst = new CLIOptions();
	
	public static CLIOptions getInst() {
		return inst;
	}
	
	public boolean isShowHelp() {
		return showHelp;
	}

	public void setShowHelp(boolean showHelp) {
		this.showHelp = showHelp;
	}

	public File getConfigFilePath() {
		return configFilePath;
	}

	public void setConfigFilePath(File configFilePath) {
		this.configFilePath = configFilePath;
	}
	
}
