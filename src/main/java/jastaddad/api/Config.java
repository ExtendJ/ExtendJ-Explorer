package jastaddad.api;

import configAST.*;
import jastaddad.api.nodeinfo.NodeInfo;

import java.io.*;
import java.util.ArrayList;
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

    public static final String NTA_DEPTH = "NTA-depth";
    public static final String NTA_COMPUTED = "NTA-computed";

    private HashMap<String, ArrayList<Expr>> filterCache;
    private HashMap<String, HashMap<String, Value>> styleCache;
    private HashMap<String, HashSet<String>> showCache;
    private HashMap<String, Value> configsCache;

    private String filterDir; //Directory of the filter file.

    public Config(ASTAPI api, String filterDir){
        this.api = api;
        this.filterDir = filterDir;
        filterCache = new HashMap<>();
        styleCache = new HashMap<>();
        showCache = new HashMap<>();
        configsCache = new HashMap<>();
        noError = readFilter(filterFileName);
    }

    /**
     * Scans and parses the file with the given filename.
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
                writer.print("configs{\n\tuse = f1;\n}\nfilter f1{\n\t/*\n\t:ASTNode{\n\t\twhen{}\n\t\tshow{}\n\t\tstyle{}\n\t}\n\t*/\n}");
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
                    String error = "";
                    error += "Errors: ";
                    if(tmpFilter.errors().size() > 1 )
                        api.putError(ASTAPI.FILTER_ERROR, "Multiple errors: ");
                    else
                        api.putError(ASTAPI.FILTER_ERROR, "");
                    for (ErrorMessage e: tmpFilter.errors()) {
                        api.putError(ASTAPI.FILTER_ERROR, e.toString());
                        error += "\n " + e;
                    }
                    System.err.println(error);

                    return false;
                }

                configs = tmpFilter;
            } catch (IOException e) {
                api.putError(ASTAPI.FILTER_ERROR, "IOException when reading filter file");
                e.printStackTrace(System.err);
                return false;
            } catch (ConfigParser.SyntaxError e) {
                api.putError(ASTAPI.FILTER_ERROR, e.getMessage());
                return false;
            }catch (Exception e) {
                api.putError(ASTAPI.FILTER_ERROR, "Exception when reading filter file: " + e.toString());
                //e.printStackTrace();
                return false;
            }
        }catch (FileNotFoundException e) {
            String errorText = "Filter file not found! Maybe the program does not have the rights to create the file for you?" +
                    "\n Create a file called filter.cfg and add -include{} inside it to get started";
            api.putError(ASTAPI.FILTER_ERROR, errorText);
            System.out.println(errorText);
            return false;
        }catch (UnsupportedEncodingException e) {
            String errorText = "Filter file not found! Maybe the program does not have the rights to create the file for you?" +
                    "\n Create a file called filter.cfg and add -include{} inside it to get started";
            e.printStackTrace();
            api.putError(ASTAPI.FILTER_ERROR, errorText);
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
            api.putError(ASTAPI.FILTER_ERROR, "File not found when writing to filter file");
            e.printStackTrace();
            noError = false;
        } catch (UnsupportedEncodingException e) {
            api.putError(ASTAPI.FILTER_ERROR, "Unsupported encoding exception");
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
                clearCaches();
            } else {
                File newFilter = new File(fullTmpFilePath);
                newFilter.delete();
            }
        }catch(Exception e){
            e.printStackTrace();
            api.putError(ASTAPI.FILTER_ERROR, "Could not delete or remove new or old filter file. Permission problem?");
        }
        return noError;
    }

    private void clearCaches(){
        filterCache.clear();
        styleCache.clear();
        showCache.clear();
        configsCache.clear();
    }

    /**
     * Get the int value for the config with the name "name"
     * @param name
     * @return
     */
    public int getInt(String name){
        Value v = getConfigValue(name);
        return v != null && v.isInt() ? v.getInt() : 0;
    }

    /**
     * Get the boolean value for the config with the name "name"
     * @param name
     * @return
     */
    public boolean getBoolean(String name){
        Value v = getConfigValue(name);
        return v != null && v.isBool() ? v.getBool() : false;
    }

    /**
     * Call to the debuggerConfig to get the config value, and caches the config
     * @param name
     * @return
     */

    public Value getConfigValue(String name) {
        Value v;
        if (configsCache.containsKey(name))
            v = configsCache.get(name);
        else {
            v = configs.getConfigs(name);
            configsCache.put(name, v);
        }
        return v;
    }

    /**
     * This method will determine if the given node is filtered or not.
     * It will compute the attributes specified in the configuration language, and check if they match with the one in the filter.
     * NOTE: if "filter" is set to false, in the -config block for configuration language, this method will always return true.
     * @param node
     * @return true if not filtered.
     */
    public boolean isEnabled(Node node){
        if(configs == null){
            //api.putWarning(ASTAPI.FILTER_WARNING, "Filter is null! Will enable all nodes.");
            return false;
        }

        if(!configs.getConfigs().hasUse())
            return true;
        ArrayList<Expr> exprs;
        if(filterCache.containsKey(node.simpleNameClass))
            exprs = filterCache.get(node.simpleNameClass);
        else{
            exprs = configs.getFilterExpressions(node);
            filterCache.put(node.simpleNameClass, exprs);
        }

        if(exprs == null)
            return false;

        boolean success = true;
        for(Expr expr : exprs){
            String decl = expr.getDecl().getID();

            if(!expr.isBinExpression()){
                success =  expr.validateExpr(node);
            }else {
                BinExpr be = (BinExpr) expr;

                NodeInfo a = node.getNodeContent().computeMethod(decl);
                if (a != null) {
                    if (be.isDoubleDecl()) {
                        String decl2 = ((LangDecl) be.getValue()).getID();
                        NodeInfo b = node.getNodeContent().computeMethod(decl2);
                        if (b == null)
                            success =  false;
                        else
                            success = a.getReturnType().equals(b.getReturnType()) && be.validateExpr(a.getValue(), b.getValue(), a.getReturnType(), decl);
                    } else
                        success = be.validateExpr(a.getValue());
                } else {
                    success = false;
                }
            }
            if(!success)
                return false;
            }
        return success;
    }

    /**
     * Returns a HashMap with the styles for the the node. Where the String is the name of the style and the Value is obviously the value.
     * @param node
     * @return
     */
    public HashMap<String, Value> getNodeStyle(Node node){
        HashMap<String, Value> map;
        if(configs == null){
            //api.putWarning(ASTAPI.FILTER_WARNING, "Filter is null! No styles will be applied.");
            return new HashMap<>();
        }
        if(styleCache.containsKey(node.simpleNameClass))
            return styleCache.get(node.simpleNameClass);

        map = configs.getNodeStyles(node);
        styleCache.put(node.simpleNameClass, map);
        return map;
    }

    /**
     * Returns a HashSet of the attributes that should be displayed. for the specified node.
     * @param node
     * @return
     */
    public HashSet<String> getDisplayedAttributes(Node node){
        HashSet<String> show;
        if(configs == null){
            return new HashSet<>();
        }
        if(showCache.containsKey(node.simpleNameClass))
            show = showCache.get(node.simpleNameClass);
        else{
            show = configs.getShow(node);
            showCache.put(node.simpleNameClass, show);
        }
        return show;
    }
}