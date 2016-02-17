package drast.model;

import configAST.Value;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Config{

	public static final String DYNAMIC_VALUES = "dynamic-values";
	public static final String NTA_DEPTH = "NTA-depth";
	public static final String NTA_COMPUTED = "NTA-computed";
	public static final String NTA_CACHED = "NTA-cached";

	private HashMap<String, String> configs;
	public static final String FILE_NAME = "DrAST.cfg";
	private String fullFilePath;

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
	 * Get the int value for the config with the name "name"
	 * @param name
	 * @return
	 */
	public int getInt(String name){
		return  configs.get(name) != null ? Integer.parseInt(configs.get(name)) : 0;
	}

	/**
	 * Get the boolean value for the config with the name "name"
	 * @param name
	 * @return
	 */
	public boolean getBoolean(String name){
		return configs.get(name) != null && Integer.parseInt(configs.get(name)) == 1;
	}

	public boolean isEnabled(String name){
		String cfg = configs.get(name);
		return cfg != null && cfg.equals("1");
	}

	private void readConfigFile(){
		try{
			File file = new File(fullFilePath);
			if(!file.exists()) {
				PrintWriter writer = new PrintWriter(fullFilePath, "UTF-8");
				writer.println(DYNAMIC_VALUES + "=0");
				writer.println(NTA_COMPUTED + "=0");
				writer.println(NTA_CACHED + "=1");
				writer.println(NTA_DEPTH + "=10");
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
