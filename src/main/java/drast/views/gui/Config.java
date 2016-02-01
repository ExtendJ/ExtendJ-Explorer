package drast.views.gui;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Config{
	private HashMap<String, String> mainConfigs;
	private HashMap<String, String> configs;
	public static final String FILE_NAME = "DrASTUI-config.cfg";
	private String fullFilePath;

	public Config(){
	 	configs = new HashMap<>();
		fullFilePath = FILE_NAME;
		readConfigFile();
		mainConfigs = configs;

	}
	public Config(String filePath){
	 	configs = new HashMap<>();
		fullFilePath = filePath + FILE_NAME;
		readConfigFile();
		mainConfigs = configs;
	}

	public void loadMainConfigs(){
		configs = mainConfigs;
	}

	public void loadConfigsForTest(String fileName){
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

	// does the config exist and is it equals to value
	public boolean isValue(String name, String value){
		String cfg = configs.get(name);
		return cfg != null && cfg.equals(value);
	}

	// does the config exist and is it set to 1
	public boolean isEnabled(String name){
		String cfg = configs.get(name);
		return cfg != null && cfg.equals("1");
	}
	
	// does the config exist and is it set to 1
	public boolean isPrintEnabled(String name){
		String cfg = configs.get(name);
		return cfg == null || cfg.equals("1");
	}

	private void readConfigFile(){
		try{

			File file = new File(fullFilePath);
			if(file.exists()) {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = null;
				while ((line = reader.readLine()) != null) {
					if (line.length() > 0) {
						if (!line.startsWith("#")) {
							String[] one_config = line.split("=");
							configs.put(one_config[0], one_config[1]);
						}
					}
				}
			}else{
				PrintWriter writer = new PrintWriter(fullFilePath, "UTF-8");
				writer.close();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void saveConfigFile(){
		PrintWriter writer = null;
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
