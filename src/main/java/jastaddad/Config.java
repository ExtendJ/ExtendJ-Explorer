package jastaddad;

import configAST.*;
import jastaddad.objectinfo.NodeInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Config{
    private DebuggerConfig configs;
    private boolean noError;
    private HashMap<String, ArrayList<String>> errors;

    private final String filterFileName = "filter.cfg";
    private final String filterTmpFileName = "filter-tmp.cfg";

    public Config( HashMap<String, ArrayList<String>> errors){
        this.errors = errors;
        noError = readFilter(filterFileName);
    }

    private boolean readFilter(String fileName){
        DebuggerConfig tmpFilter;

        ConfigScanner scanner;
        try {
            scanner = new ConfigScanner(new FileReader(fileName));
        }catch (FileNotFoundException e) {
            errors.get("filter").add("Filter file not found!");
            System.out.println("File not found!");
            return false;
        }

        try{
            ConfigParser parser = new ConfigParser();

            tmpFilter = (DebuggerConfig) parser.parse(scanner);

            if (!tmpFilter.errors().isEmpty()) {
                String error = "\n";
                error += "Errors: ";
                for (ErrorMessage e: tmpFilter.errors()) {
                    error += "- " + e;
                }
                System.err.println(error);
                errors.get("filter").add(error);
                return false;
            }
        } catch (IOException e) {
            errors.get("filter").add("IOException when reading filter file");
            e.printStackTrace(System.err);
            return false;
        } catch (ConfigParser.SyntaxError e) {
            errors.get("filter").add(e.getMessage());
            return false;
        }catch (Exception e) {
            errors.get("filter").add("Exception when reading filter file: " + e.toString());
            //e.printStackTrace();
            return false;
        }
        configs = tmpFilter;
        return true;
    }

    public boolean isEnabled(Node node){
        if(!noError)
            return false;

        HashMap<String, Value> global = configs.configs();
        if(global.containsKey("show-all") && global.get("show-all").getBool())
            return true;

        // Add all bin expressions to one hashmap. This will allow expressions to override each other
        HashMap<String, BinExpr> binExprs = new HashMap<>();

        // If there a global filter add it to the hashmap
        if(configs.getInclude().hasGlobal() && configs.getInclude().getGlobal().hasFilter()){
            for (BinExpr be : configs.getInclude().getGlobal().getFilter().getBinExprList()) {
                binExprs.put(be.getDecl().getID(), be);
            }
        }

        // don't do this if the only-global is set to true
        if(!global.containsKey("only-global") || !(global.get("only-global").getBool())) {
            // try to find the node in the Include.
            boolean className = configs.getNodes().containsKey(node.className); // Div;
            boolean tellingName = configs.getNodes().containsKey(node.fullName); // Div:Left

            // not found
            if(!tellingName && !className)
                return false;

            // First add class specific bin expressions
            NodeConfig cNode = configs.getNodes().get(node.className);
            if (className && cNode.hasFilter()) {
                for (BinExpr be : cNode.getFilter().getBinExprList()) {
                    binExprs.put(be.getDecl().getID(), be);
                }
            }

            // then add class name specific bin expressions, eventual overriding of class expressions
            NodeConfig tNode = configs.getNodes().get(node.fullName);
            if (tellingName && tNode.hasFilter()) {
                for (BinExpr be : tNode.getFilter().getBinExprList()) {
                    binExprs.put(be.getDecl().getID(), be);
                }
            }
        }

        // Loop through the whole map to see if the node is enabled
        for(Map.Entry<String, BinExpr> entry : binExprs.entrySet()){

            String decl = entry.getKey() + "()";
            BinExpr be = entry.getValue();

            //System.out.println(decl);
            if(!node.containsAttributeOrToken(decl)) {
                return false;
            }
            if(be.isDoubleDecl()){
                String decl2 = ((IdDecl)be.getValue()).getID() + "()";
                if(!node.containsAttributeOrToken(decl2))
                    return false;
                NodeInfo a = node.getAttributeOrTokenValue(decl);
                NodeInfo b = node.getAttributeOrTokenValue(decl2);
                if(!a.getReturnType().equals(b.getReturnType()))
                    return false;
                if (!be.validateExpr(a.getValue(), b.getValue(), a.getReturnType(), decl)) {

                    return false;
                }

            }else{
                if(!be.validateExpr(node.getAttributeOrTokenValue(decl).getValue())) {
                    //System.out.println("END2: " + be.getValue().getStr());
                    return false;
                }
            }
        }
        return true;
    }

    public HashMap<String, Value> getNodeStyle(Node node){

        boolean className = configs.getNodes().containsKey(node.className); // Div;
        boolean tellingName = configs.getNodes().containsKey(node.fullName); // Div:Left

        if(!tellingName && !className)
            return new HashMap<>();

        // Add all class and telling bin expressions to one hashmap. this will let the telling expressions to override
        HashMap<String, Value> map = new HashMap<>();


        NodeConfig cNode = configs.getNodes().get(node.className);
        if(className && cNode.hasStyle()) {
            for(Binding b : cNode.getStyle().getBindingList().getBindingList()){
                map.put(b.getName().print(), b.getValue());
            }
        }
        NodeConfig tNode = configs.getNodes().get(node.fullName);
        if(tellingName && tNode.hasStyle()) {
            for(Binding b : tNode.getStyle().getBindingList().getBindingList()){
                map.put(b.getName().print(), b.getValue());
            }
        }
        return map;
    }

    public boolean saveAndUpdateFilter(String text){
        System.out.println("SAVE");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("filter-tmp.cfg", "UTF-8");
            writer.print(text);
            writer.close();
        } catch (FileNotFoundException e) {
            errors.get("filter").add("File not found when writing to filter file");
            e.printStackTrace();
            noError = false;
        } catch (UnsupportedEncodingException e) {
            errors.get("filter").add("Unsupported encoding exception");
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
            errors.get("filter").add("Could not delete or remove new or old filter file. Permission problem?");
        }
        return noError;
    }
}