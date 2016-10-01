package drast.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;

public final class DrASTSettings {

  public static final String DYNAMIC_VALUES = "dynamic-values";
  public static final String NTA_DEPTH = "NTA-depth";
  public static final String NTA_COMPUTED = "NTA-computed";
  public static final String NTA_CACHED = "NTA-cached";

  public static final String PREV_JAR = "prevJar";
  public static final String PREV_FILTER = "prevFilter";
  public static final String PREV_FIRST_ARG = "prevFirstArg";
  public static final String PREV_ARGS = "prevFullArgs";
  public static final String PREV_TAIL_ARGS = "prevRestArgs";

  public static final String NODE_THRESHOLD = "nodeThreshold";
  public static final String SHOW_NODES = "showNodes";
  public static final String SHOW_EDGES = "showEdges";
  public static final String CURVED_EDGES = "curvedEdges";

  public static final String NORMAL_EDGE_WIDTH = "normalEdgeWidth";
  public static final String REF_EDGE_WIDTH = "refEdgeWidth";
  public static final String DASHED_EDGE_WIDTH = "dashedEdgeWidth";

  public static final String NORMAL_VERTEX_EDGE_WIDTH = "normalVertexEdgeWidth";
  public static final String DASHED_VERTEX_EDGE_WIDTH = "dashedVertexEdgeWidth";

  private static final LinkedHashMap<String, String> configs;
  private static final File configFile;

  static {
    File configDirectory = new File(".");
    configFile = new File(configDirectory, "DrAST.cfg");
    configs = new LinkedHashMap<>();

    loadDefaults();

    readConfigFile(configFile);
  }

  private DrASTSettings() {
  }

  private static void loadDefaults() {
    configs.put(DYNAMIC_VALUES, "0");
    configs.put(NTA_COMPUTED, "false");
    configs.put(NTA_CACHED, "true");
    configs.put(NTA_DEPTH, "1");
    configs.put(NODE_THRESHOLD, "1000");
    configs.put(SHOW_NODES, "true");
    configs.put(SHOW_EDGES, "true");
    configs.put(CURVED_EDGES, "true");
    configs.put(NORMAL_EDGE_WIDTH, "1.0");
    configs.put(REF_EDGE_WIDTH, "2.0");
    configs.put(DASHED_EDGE_WIDTH, "0.2");
    configs.put(NORMAL_VERTEX_EDGE_WIDTH, "1.0");
    configs.put(DASHED_VERTEX_EDGE_WIDTH, "0.2");
  }

  public static void put(String name, String value) {
    configs.put(name, value);
    saveConfigFile(configFile);
  }

  public static String get(String name, String defaultValue) {
    String value = configs.get(name);
    return value != null ? value : defaultValue;
  }

  /**
   * Get the int value for the config with the name "name".
   */
  public static int getInt(String name, int defaultValue) {
    try {
      return Integer.parseInt(get(name, ""));
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  /**
   * Get the int value for the config with the name "name"
   * They float, they all float... and when you're down here with me, fat boy, you'll float too.
   */
  public static float getFloat(String name, float defaultValue) {
    try {
      return Float.parseFloat(get(name, ""));
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  /**
   * Get the boolean value for the config with the name "name".
   */
  public static boolean getFlag(String name) {
    switch (get(name, "").toLowerCase()) {
      case "true":
      case "yes":
      case "1":
        return true;
      case "false":
      case "no":
      case "0":
        return false;
    }
    return false;
  }

  /**
   * Try and read the configuration file, and store each entry in the HashMap configs.
   */
  private static void readConfigFile(File path) {
    try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.length() > 0) {
          if (!line.startsWith("#")) {
            String[] one_config = line.split("=");
            configs.put(one_config[0], one_config[1]);
          }
        }
      }
    } catch (FileNotFoundException e) {
      // This happens if running from a new directory where there is no DrAST config file.
      // We fall back on the default configuration.
      System.err.println("Could not open configuration file. Using defaults.");
    } catch (IOException e) {
      System.err.println("Could not open configuration file. Using defaults.");
      e.printStackTrace();
    }
  }

  private static void saveConfigFile(File file) {
    try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
      configs.entrySet().stream().filter(e -> e.getValue().length() > 0)
          .forEach(e -> writer.println(e.getKey() + "=" + e.getValue()));
      writer.close();
    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  public static File getFilterFile() {
    String path = get(PREV_FILTER, "");
    return !path.isEmpty() ? new File(path) : null;
  }
}
