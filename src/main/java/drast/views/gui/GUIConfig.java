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
	public static String PREV_FIRST_ARGS = "prevFirstArg";

	public static final String FILE_NAME = "DrASTGUI.cfg";

	public static final String NODE_THRESHOLD = "nodeThreshold";
	public static final String SHOW_NODES = "showNodes";
	public static final String SHOW_EDGES = "showEdges";
	public static final String NICE_EDGES = "niceEdges";

	public static final String NORMAL_EDGE_WIDTH = "normalEdgeWidth";
	public static final String REF_EDGE_WIDTH = "refEdgeWidth";
	public static final String DASHED_EDGE_WIDTH = "dashedEdgeWidth";

	public static final String NORMAL_VERTEX_EDGE_WIDTH = "normalVertexEdgeWidth";
	public static final String DASHED_VERTEX_EDGE_WIDTH = "dashedVertexEdgeWidth";

	public static final float F_NORMAL_EDGE_WIDTH = 1.0f;
	public static final float F_REF_EDGE_WIDTH = 2.0f;
	public static final float F_DASHED_EDGE_WIDTH = 0.2f;

	public static final float F_NORMAL_VERTEX_EDGE_WIDTH = 1.0f;
	public static final float F_DASHED_VERTEX_EDGE_WIDTH = 0.2f;

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

		writer.println(NORMAL_EDGE_WIDTH + "=" + F_NORMAL_EDGE_WIDTH);
		writer.println(REF_EDGE_WIDTH + "=" + F_REF_EDGE_WIDTH);
		writer.println(DASHED_EDGE_WIDTH + "=" + F_DASHED_EDGE_WIDTH);

		writer.println(NORMAL_VERTEX_EDGE_WIDTH + "="+ F_NORMAL_VERTEX_EDGE_WIDTH);
		writer.println(DASHED_VERTEX_EDGE_WIDTH + "=" + F_DASHED_VERTEX_EDGE_WIDTH);
	}
}
