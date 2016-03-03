package drast.views.gui;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

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

public class Config {

	public static String PREV_JAR = "prevJar";
	public static String PREV_FILTER = "prevFilter";
	public static String PREV_ARGS = "prevFullArgs";

	private HashMap<String, String> configs;
	public static final String FILE_NAME = "DrASTGUI.cfg";
	private String fullFilePath;

	public Config(String filePath, String fileName){
		configs = new HashMap<>();
		fullFilePath = filePath + fileName;
		readConfigFile();
	}

	public Config(String filePath){
	 	configs = new HashMap<>();
		fullFilePath = filePath + FILE_NAME;
		readConfigFile();
	}

	public void put(String name, String value){
		configs.put(name, value);
	}

	public String get(String name){
		return configs.get(name);
	}

	/**
	 * Returns the config name or an empty String
	 */
	public String getOrEmpty(String name){
		String tmp = configs.get(name);
		return tmp == null ? "" : tmp;
	}

	/**
	 * This method answares the question: Does the config exist and is it set to 1?
	 * @param name
	 * @return
	 */
	public boolean isEnabled(String name){
		String cfg = configs.get(name);
		return cfg != null && cfg.equals("1");
	}

	/**
	 * Try and read the configuration file, and store each entry in the HashMap configs.
	 *
	 * If the config file does not exist, the program will try and create a new one with some default values.
	 */
	private void readConfigFile(){
		try{

			File file = new File(fullFilePath);
			if(!file.exists()) {
				PrintWriter writer = new PrintWriter(fullFilePath, "UTF-8");
				writer.println("nodeThreshold=1000");
				writer.println("showNodes=1");
				writer.println("showEdges=1");
				writer.println("niceEdges=1");
				writer.close();
			}

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line ;
			while ((line = reader.readLine()) != null) {
				if (line.length() > 0) {
					if (!line.startsWith("#")) {
						String[] one_config = line.split("=");
						configs.put(one_config[0], one_config[1]);
					}
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Save the HashMap configs to file.
	 */
	public void saveConfigFile(){
		PrintWriter writer;
		try {
			writer = new PrintWriter(fullFilePath, "UTF-8");
			for(Map.Entry<String, String> e : configs.entrySet()){
				if(e.getValue().length() > 0)
					writer.println(e.getKey()+"="+e.getValue());
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
