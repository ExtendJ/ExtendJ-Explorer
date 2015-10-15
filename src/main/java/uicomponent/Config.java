package uicomponent;

import java.util.HashMap;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Config{
    private HashMap<String, String> mainConfigs;
    private HashMap<String, String> configs;
    public static final String FILE_NAME = "uicomponent-config.cfg";

    public Config(){
        configs = new HashMap<String, String>();
        readConfigFile(FILE_NAME);
        mainConfigs = configs;
    }
    public Config(String filePath){
        configs = new HashMap<String, String>();
        readConfigFile(filePath + FILE_NAME);
        mainConfigs = configs;
    }

    public void loadMainConfigs(){
        configs = mainConfigs;
    }

    public void loadConfigsForTest(String fileName){
        readConfigFile(fileName);
    }

    public void put(String name, String value){
        configs.put(name, value);
    }

    public String get(String name){
        return configs.get(name);
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


    public boolean isPrintEnabled(String name){
        String cfg = configs.get(name);
        return cfg == null || cfg.equals("1");
    }

    private void readConfigFile(String fileName){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if(line.length() > 0){
                    line = line.replace(" ", "");
                    if(!line.startsWith("#")){
                        String[] one_config = line.split("=");
                        configs.put(one_config[0], one_config[1]);
                    }
                }
            }
        }catch(IOException e){
            System.out.println("MetricPCE.java: Error reading file:\n");
            e.printStackTrace();
        }
    }
}