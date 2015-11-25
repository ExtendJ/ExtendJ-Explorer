package jastaddad.api;

import configAST.*;
import jastaddad.api.nodeinfo.NodeInfo;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * This class contains the configurations specified by the configuration language.
 * It can read and parse the file with the configuration
 */

public class Config{
    private DebuggerConfig configs; //The top node of configurations
    private boolean noError;
    private ASTAPI api; //Reference to the ASTAPI, mainly used to contribute errors

    //Variable farm
    private final String filterFileName = "filter.cfg";
    private final String filterTmpFileName = "filter-tmp.cfg";

    private static final String CONFIG_FILTER = "filter";
    private static final String CONFIG_GLOBAL = "global";
    private static final String CONFIG_INCLUDE = "include";

    private static final String FILTER_LIST = "-filter";
    private static final String STYLE_LIST = "-style";
    private static final String DISPLAY_ATTRIBUTES_LIST = "-display-attributes";

    private String filterDir; //Directory of the filter file.

    public Config(ASTAPI api, String filterDir){
        this.api = api;
        this.filterDir = filterDir;
        noError = readFilter(filterFileName);
    }

    /**
     * Scanns and parses the file with the given filename.
     * If no such file can found it will create a standard Configuration file, and then parse that one.
     * Sets the configuration to the new one if no errors are thrown.
     * @param fileName
     * @return
     */
    private boolean readFilter(String fileName){
        String fullFilePath = filterDir + fileName;
        //System.out.println(fullFilePath);
        DebuggerConfig tmpFilter;

        ConfigScanner scanner;
        try {
            // check if file exists
            File f = new File(fullFilePath);
            PrintWriter writer = null;
            if(!f.exists()) {
                writer = new PrintWriter(fullFilePath, "UTF-8");
                writer.print("-configs{\n\tfilter = true;\n\tglobal = false;\n\tinclude = false;\n}" +
                        "\n-global{\n\t-filter{}\n\t-style{}\n\t-display-attributes{}\n}\n" +
                        "-include{\n\n}\n");
                writer.close();
            }
            // create the scanner
            scanner = new ConfigScanner(new FileReader(fullFilePath));
            try{

                ConfigParser parser = new ConfigParser();
                // parse the config file
                tmpFilter = (DebuggerConfig) parser.parse(scanner);

                if (!tmpFilter.errors().isEmpty()) {
                    // something went wrong, tell the user the error.
                    String error = "\n";
                    error += "Errors: ";
                    for (ErrorMessage e: tmpFilter.errors()) {
                        error += "- " + e;
                    }
                    System.err.println(error);
                    api.getErrors(ASTAPI.FILTER_ERROR).add(error);
                    return false;
                }

                configs = tmpFilter;
            } catch (IOException e) {
                api.getErrors(ASTAPI.FILTER_ERROR).add("IOException when reading filter file");
                e.printStackTrace(System.err);
                return false;
            } catch (ConfigParser.SyntaxError e) {
                api.getErrors(ASTAPI.FILTER_ERROR).add(e.getMessage());
                return false;
            }catch (Exception e) {
                api.getErrors(ASTAPI.FILTER_ERROR).add("Exception when reading filter file: " + e.toString());
                //e.printStackTrace();
                return false;
            }
        }catch (FileNotFoundException e) {
            String errorText = "Filter file not found! Maybe the program does not have the rights to create the file for you?" +
                    "\n Create a file called filter.cfg and add -include{} inside it to get started";
            api.getErrors(ASTAPI.FILTER_ERROR).add(errorText);
            System.out.println(errorText);
            return false;
        }catch (UnsupportedEncodingException e) {
            String errorText = "Filter file not found! Maybe the program does not have the rights to create the file for you?" +
                    "\n Create a file called filter.cfg and add -include{} inside it to get started";
            e.printStackTrace();
            api.getErrors(ASTAPI.FILTER_ERROR).add(errorText);
        }
        return true;
    }

    /**
     * Updates the configurations, will overwrite the old configs with the new if no errors has been thrown.'
     * Will also overwrite the old configuration file.
     * @param text
     * @return
     */
    public boolean saveAndUpdateConfig(String text){
        System.out.println("SAVE");
        String fullTmpFilePath = filterDir + filterTmpFileName;
        String fullFilePath = filterDir + filterFileName;
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fullTmpFilePath, "UTF-8");
            writer.print(text);
            writer.close();
        } catch (FileNotFoundException e) {
            api.getErrors(ASTAPI.FILTER_ERROR).add("File not found when writing to filter file");
            e.printStackTrace();
            noError = false;
        } catch (UnsupportedEncodingException e) {
            api.getErrors(ASTAPI.FILTER_ERROR).add("Unsupported encoding exception");
            e.printStackTrace();
            noError = false;
        }
        noError = readFilter(filterTmpFileName);
        try {
            if (noError) {
                File oldFilter = new File(fullFilePath);
                File newFilter = new File(fullTmpFilePath);
                oldFilter.delete();
                newFilter.renameTo(oldFilter);
            } else {
                File newFilter = new File(fullTmpFilePath);
                newFilter.delete();
            }
        }catch(Exception e){
            e.printStackTrace();
            api.getErrors(ASTAPI.FILTER_ERROR).add("Could not delete or remove new or old filter file. Permission problem?");
        }
        return noError;
    }

    /**
     * Check if a specified config is set in the -global config
     * @param name
     * @return
     */
    private boolean isSet(String name){
        HashMap<String, Value> filterConfigs = configs.configs();
        return !filterConfigs.containsKey(name) || filterConfigs.get(name).getBool();
    }

    /**
     * This method will determine if the given node is filtered or not.
     * It will compute the attributes specified in the configuration language, and check if they match with the one in the filter.
     * NOTE: if "filter" is set to false, in the -config block for configuration language, this method will always return true.
     * @param node
     * @return true if not filtered.
     */
    public boolean isEnabled(Node node){
        if(!noError)
            return false;

        if(!isSet(CONFIG_FILTER))
            return true;

        // Add all bin expressions to one hashmap. This will allow expressions to override each other
        HashMap<String, BinExpr> binExprs = new HashMap<>();

        // If there a global filter add it to the hashmap
        if (isSet(CONFIG_GLOBAL) && configs.getGlobal() != null && configs.getGlobal().getBinExprList(FILTER_LIST) != null) {
            for (BinExpr be : configs.getGlobal().getBinExprList(FILTER_LIST).getBinExprList()) {
                binExprs.put(be.getDecl().getID(), be);
            }
        }

        // don't do this if the -include is set to false
        if (isSet(CONFIG_INCLUDE)) {
            // try to find the node in the Include.
            boolean className = configs.getNodes().containsKey(node.simpleNameClass); // Div;
            boolean tellingName = configs.getNodes().containsKey(node.fullName); // Div:Left

            // not found
            if(!tellingName && !className)
                return false;

            // First add class specific bin expressions
            NodeConfig cNode = configs.getNodes().get(node.simpleNameClass);
            if (className && cNode.getBinExprList(FILTER_LIST) != null) {
                for (BinExpr be : cNode.getBinExprList(FILTER_LIST).getBinExprList()) {
                    binExprs.put(be.getDecl().getID(), be);
                }
            }

            // then add class name specific bin expressions, eventual overriding of class expressions
            NodeConfig tNode = configs.getNodes().get(node.fullName);
            if (tellingName && tNode.getBinExprList(FILTER_LIST) != null) {
                for (BinExpr be : tNode.getBinExprList(FILTER_LIST).getBinExprList()) {
                    binExprs.put(be.getDecl().getID(), be);
                }
            }
        }

        // Loop through the whole map to see if the node is enabled
        for(Map.Entry<String, BinExpr> entry : binExprs.entrySet()){

            String decl = entry.getKey();
            BinExpr be = entry.getValue();

            NodeInfo a = node.getNodeContent().compute(decl);
            if(a == null)
                return false;
            if(be.isDoubleDecl()){
                String decl2 = ((IdDecl)be.getValue()).getID();
                NodeInfo b = node.getNodeContent().compute(decl2);
                if(b == null)
                    return false;
                return a.getReturnType().equals(b.getReturnType()) && be.validateExpr(a.getValue(), b.getValue(), a.getReturnType(), decl);
            }else
                return be.validateExpr(a.getValue());
        }
        return true;
    }

    /**
     * Returns a HashMap with the styles for the the node. Where the String is the name of the style and the Value is obviously the value.
     * @param node
     * @return
     */
    public HashMap<String, Value> getNodeStyle(Node node){
        HashMap<String, Value> map = new HashMap<>();

        // If there a global style add it to the hashmap if -ignore-global == false;
        if (isSet(CONFIG_GLOBAL) && configs.getGlobal() != null && configs.getGlobal().getBindingList(STYLE_LIST) != null) {
            for(Binding b : configs.getGlobal().getBindingList(STYLE_LIST).getBindingList()){
              map.put(b.getName().print(), b.getValue());
            }
        }

        if (!isSet(CONFIG_INCLUDE))
            return map;

        boolean className = configs.getNodes().containsKey(node.simpleNameClass); // Div;
        boolean tellingName = configs.getNodes().containsKey(node.fullName); // Div:Left

        if(!tellingName && !className)
            return map;

        // Add all class and telling bin expressions to one hashmap. this will let the telling expressions to override

        NodeConfig cNode = configs.getNodes().get(node.simpleNameClass);
        if(className && cNode.getBindingList(STYLE_LIST) != null) {
            for(Binding b : cNode.getBindingList(STYLE_LIST).getBindingList()){
                map.put(b.getName().print(), b.getValue());
            }
        }
        cNode = configs.getNodes().get(node.fullName);
        if(tellingName && cNode.getBindingList(STYLE_LIST) != null) {
            for(Binding b : cNode.getBindingList(STYLE_LIST).getBindingList()){
                map.put(b.getName().print(), b.getValue());
            }
        }
        return map;
    }

    /**
     * Returns a HashSet of the attributes that should be displayed. for the specified node.
     * @param node
     * @return
     */
    public HashSet<String> getDisplayedAttributes(Node node){
        HashSet<String> set = new HashSet();

        if (isSet(CONFIG_GLOBAL) && configs.getGlobal().getIdDeclList(DISPLAY_ATTRIBUTES_LIST) != null){
            for (IdDecl decl : configs.getGlobal().getIdDeclList(DISPLAY_ATTRIBUTES_LIST).getIdDeclList()){
                set.add(decl.getID());
            }
        }

        if (!isSet(CONFIG_INCLUDE))
            return set;

        boolean className = configs.getNodes().containsKey(node.simpleNameClass); // Div;
        boolean tellingName = configs.getNodes().containsKey(node.fullName); // Div:Left

        if(!tellingName && !className)
            return set;

        NodeConfig cNode = configs.getNodes().get(node.simpleNameClass);
        if(className && cNode.getIdDeclList(DISPLAY_ATTRIBUTES_LIST) != null) {
            for(IdDecl decl : cNode.getIdDeclList(DISPLAY_ATTRIBUTES_LIST).getIdDeclList()){
                set.add(decl.getID());
            }
        }
        cNode = configs.getNodes().get(node.fullName);
        if(tellingName && cNode.getIdDeclList(DISPLAY_ATTRIBUTES_LIST) != null) {
            for(IdDecl decl : cNode.getIdDeclList(DISPLAY_ATTRIBUTES_LIST).getIdDeclList()){
                set.add(decl.getID());
            }
        }
        return set;
    }
}