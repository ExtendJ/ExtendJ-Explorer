package jastaddad;

import configAST.*;
import jastaddad.filteredtree.GenericTreeNode;
import jastaddad.objectinfo.NodeInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Config{
    private DebuggerConfig configs;
    private boolean noError;
    private HashMap<String, ArrayList<String>> errors;

    private final String filterFileName = "filter.cfg";
    private final String filterTmpFileName = "filter-tmp.cfg";

    private static final String IGNORE_FILTER = "ignore-filter";
    private static final String IGNORE_GLOBAL = "ignore-global";
    private static final String IGNORE_INCLUDE = "ignore-include";

    private static final String FILTER = "filter";
    private static final String FILTER_LIST = "-filter";
    private static final String STYLE_LIST = "-style";
    private static final String DISPLAYED_ATTRIBUTES_LIST = "-displayed-attributes";

    public Config( HashMap<String, ArrayList<String>> errors){
        this.errors = errors;
        noError = readFilter(filterFileName);
    }

    private boolean readFilter(String fileName){
        DebuggerConfig tmpFilter;

        ConfigScanner scanner;
        try {
            // check if file exists
            File f = new File(fileName);
            PrintWriter writer = null;
            if(!f.exists()) {
                writer = new PrintWriter(fileName, "UTF-8");
                writer.print("-configs{\n\tignore-filter = true;\n\tignore-global = false;\n\tignore-include = false;\n}" +
                        "\n-global{\n\t-filter{}\n\t-style{}\n\t-displayed-attributes{}\n}\n" +
                        "-include{\n\n}\n");
                writer.close();
            }
            // create the scanner
            scanner = new ConfigScanner(new FileReader(fileName));
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
                    errors.get(FILTER).add(error);
                    return false;
                }

                configs = tmpFilter;
            } catch (IOException e) {
                errors.get(FILTER).add("IOException when reading filter file");
                e.printStackTrace(System.err);
                return false;
            } catch (ConfigParser.SyntaxError e) {
                errors.get(FILTER).add(e.getMessage());
                return false;
            }catch (Exception e) {
                errors.get(FILTER).add("Exception when reading filter file: " + e.toString());
                //e.printStackTrace();
                return false;
            }
        }catch (FileNotFoundException e) {
            String errorText = "Filter file not found! Maybe the program does not have the rights to create the file for you?" +
                    "\n Create a file called filter.cfg and add -include{} inside it to get started";
            errors.get(FILTER).add(errorText);
            System.out.println(errorText);
            return false;
        }catch (UnsupportedEncodingException e) {
            String errorText = "Filter file not found! Maybe the program does not have the rights to create the file for you?" +
                    "\n Create a file called filter.cfg and add -include{} inside it to get started";
            e.printStackTrace();
            errors.get(FILTER).add(errorText);
        }
        return true;
    }

    public boolean saveAndUpdateFilter(String text){
        System.out.println("SAVE");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("filter-tmp.cfg", "UTF-8");
            writer.print(text);
            writer.close();
        } catch (FileNotFoundException e) {
            errors.get(FILTER).add("File not found when writing to filter file");
            e.printStackTrace();
            noError = false;
        } catch (UnsupportedEncodingException e) {
            errors.get(FILTER).add("Unsupported encoding exception");
            e.printStackTrace();
            noError = false;
        }
        noError = readFilter(filterTmpFileName);
        try {
            if (noError) {
                File oldFilter = new File(filterFileName);
                File newFilter = new File(filterTmpFileName);
                oldFilter.delete();
                newFilter.renameTo(oldFilter);
            } else {
                File newFilter = new File(filterTmpFileName);
                newFilter.delete();
            }
        }catch(Exception e){
            e.printStackTrace();
            errors.get(FILTER).add("Could not delete or remove new or old filter file. Permission problem?");
        }
        return noError;
    }

    private boolean isIgnored(String name){
        HashMap<String, Value> filterConfigs = configs.configs();
        return filterConfigs.containsKey(name) && filterConfigs.get(name).getBool();
    }

    private boolean isNotIgnored(String name){
        HashMap<String, Value> filterConfigs = configs.configs();
        return !filterConfigs.containsKey(name) || !filterConfigs.get(name).getBool();
    }

    public boolean isEnabled(Node node){
        if(!noError)
            return false;

        if(isIgnored(IGNORE_FILTER))
            return true;

        // Add all bin expressions to one hashmap. This will allow expressions to override each other
        HashMap<String, BinExpr> binExprs = new HashMap<>();

        // If there a global filter add it to the hashmap
        if(isNotIgnored(IGNORE_GLOBAL) && configs.getGlobal() != null && configs.getGlobal().getBinExprList(FILTER_LIST) != null) {
            for (BinExpr be : configs.getGlobal().getBinExprList(FILTER_LIST).getBinExprList()) {
                binExprs.put(be.getDecl().getID(), be);
            }
        }

        // don't do this if the -ignore-include is set to true
        if(isNotIgnored(IGNORE_INCLUDE)) {
            // try to find the node in the Include.
            boolean className = configs.getNodes().containsKey(node.className); // Div;
            boolean tellingName = configs.getNodes().containsKey(node.fullName); // Div:Left

            // not found
            if(!tellingName && !className)
                return false;

            // First add class specific bin expressions
            NodeConfig cNode = configs.getNodes().get(node.className);
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

            NodeInfo a = node.getNodeContent().compute(node.node, decl);
            if(a == null)
                return false;
            if(be.isDoubleDecl()){
                String decl2 = ((IdDecl)be.getValue()).getID();
                NodeInfo b = node.getNodeContent().compute(node.node, decl2);
                if(b == null)
                    return false;
                return a.getReturnType().equals(b.getReturnType()) && be.validateExpr(a.getValue(), b.getValue(), a.getReturnType(), decl);
            }else
                return be.validateExpr(a.getValue());
        }
        return true;
    }

    public HashMap<String, Value> getNodeStyle(Node node){
        HashMap<String, Value> map = new HashMap<>();

        // If there a global style add it to the hashmap if -ignore-global == false;
        if(isNotIgnored(IGNORE_GLOBAL) && configs.getGlobal() != null && configs.getGlobal().getBindingList(STYLE_LIST) != null) {
            for(Binding b : configs.getGlobal().getBindingList(STYLE_LIST).getBindingList()){
              map.put(b.getName().print(), b.getValue());
            }
        }

        if(!isNotIgnored(IGNORE_INCLUDE))
            return map;

        boolean className = configs.getNodes().containsKey(node.className); // Div;
        boolean tellingName = configs.getNodes().containsKey(node.fullName); // Div:Left

        if(!tellingName && !className)
            return map;

        // Add all class and telling bin expressions to one hashmap. this will let the telling expressions to override

        NodeConfig cNode = configs.getNodes().get(node.className);
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

    public HashSet<String> getDisplayedAttributes(Node node){
        HashSet<String> set = new HashSet();

        if(isNotIgnored(IGNORE_GLOBAL) && configs.getGlobal().getIdDeclList(DISPLAYED_ATTRIBUTES_LIST) != null){
            for (IdDecl decl : configs.getGlobal().getIdDeclList(DISPLAYED_ATTRIBUTES_LIST).getIdDeclList()){
                set.add(decl.getID());
            }
        }

        if(!isNotIgnored(IGNORE_INCLUDE))
            return set;

        boolean className = configs.getNodes().containsKey(node.className); // Div;
        boolean tellingName = configs.getNodes().containsKey(node.fullName); // Div:Left

        if(!tellingName && !className)
            return set;

        NodeConfig cNode = configs.getNodes().get(node.className);
        if(className && cNode.getIdDeclList(DISPLAYED_ATTRIBUTES_LIST) != null) {
            for(IdDecl decl : cNode.getIdDeclList(DISPLAYED_ATTRIBUTES_LIST).getIdDeclList()){
                set.add(decl.getID());
            }
        }
        cNode = configs.getNodes().get(node.fullName);
        if(tellingName && cNode.getIdDeclList(DISPLAYED_ATTRIBUTES_LIST) != null) {
            for(IdDecl decl : cNode.getIdDeclList(DISPLAYED_ATTRIBUTES_LIST).getIdDeclList()){
                set.add(decl.getID());
            }
        }
        return set;
    }
}