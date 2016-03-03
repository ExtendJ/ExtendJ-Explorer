package drast.views.gui;

import drast.model.Config;

import java.io.PrintWriter;

/**
 * This class holds all configurations of the program that can be written to a file.
 *
 * Every entry in the configuration file is stored in an HashMap configs, where the key is
 * the name of the configuration, and the value is the value of the configuration.
 *
 * A number of methods have been implemented to get different results depending on
 * what the developer needs.
 * 		getOrEmpty(String key)
 * 		isEnabled(String key)
 * 		get(String key)
 */

public class GUIConfig extends Config{

	public static String PREV_JAR = "prevJar";
	public static String PREV_FILTER = "prevFilter";
	public static String PREV_ARGS = "prevFullArgs";

	public static final String FILE_NAME = "DrASTGUI.cfg";

	public static final String NODE_THRESHOLD = "nodeThreshold";
	public static final String SHOW_NODES = "showNodes";
	public static final String SHOW_EDGES = "showEdges";
	public static final String NICE_EDGES = "niceEdges";

	public GUIConfig(String filePath, String fileName){
		super(filePath, fileName);
	}

	public GUIConfig(String filePath){
		super(filePath, FILE_NAME);
	}

	@Override
	protected void printStandard(PrintWriter writer){
		writer.println(NODE_THRESHOLD + "=1000");
		writer.println(SHOW_NODES + "=1");
		writer.println(SHOW_EDGES + "=1");
		writer.println(NICE_EDGES + "=1");
	}
}
