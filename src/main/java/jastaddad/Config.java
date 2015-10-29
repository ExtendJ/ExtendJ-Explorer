package jastaddad;

import AST.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Config{
    private DebuggerConfig configs;
    private boolean noError;

    public Config(){
        noError = readFilter();
    }

    private boolean readFilter(){
        DebuggerConfig tmpFilter;
        try{
            String filename = "filter.cfg";
            ConfigScanner scanner = new ConfigScanner(new FileReader(filename));
            ConfigParser parser = new ConfigParser();
            tmpFilter = (DebuggerConfig) parser.parse(scanner);
            if (!tmpFilter.errors().isEmpty()) {
                System.err.println();
                System.err.println("Errors: ");
                for (ErrorMessage e: tmpFilter.errors()) {
                    System.err.println("- " + e);
                }
                return false;
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            return false;
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        configs = tmpFilter;
        return true;
    }

    public boolean isEnabled(Node node){
        if(!noError)
            return false;
        if(configs.configs().get("showAll").getBool())
            return true;

        boolean className = configs.getNodes().containsKey(node.className); // Div;
        boolean tellingName = configs.getNodes().containsKey(node.fullName); // Div:Left

        if(!tellingName && !className)
            return false;

        // Add all class and telling bin expressions to one hashmap. this will öet the telling expressions to override
        HashMap<String, BinExpr> binExprs = new HashMap<>();
        NodeConfig cNode = configs.getNodes().get(node.className);
        if(className && cNode.hasFilter()) {
            for (BinExpr be : cNode.getFilter().getBinExprList()) {
                binExprs.put(be.getDecl().getID(), be);
            }
        }

        NodeConfig tNode = configs.getNodes().get(node.fullName);
        if(tellingName && tNode.hasFilter()) {
            for (BinExpr be : tNode.getFilter().getBinExprList()) {
                binExprs.put(be.getDecl().getID(), be);
            }
        }


        for(Map.Entry<String, BinExpr> entry : binExprs.entrySet()){

            String decl = entry.getKey() + "()";
            BinExpr be = entry.getValue();

            System.out.println(decl);
            if(!node.containsAttributeOrToken(decl)) {
                System.out.println("END1");
                return false;
            }
            if(be.isDoubleDecl()){
                System.out.println("TWO");
                String decl2 = ((IdDecl)be.getValue()).getID() + "()";
                if(node.containsAttributeOrToken(decl2)) {

                    if (!be.validateExpr(node.getAttributeOrTokenValue(decl).getValue(), node.getAttributeOrTokenValue(decl2).getValue())) {
                        System.out.println("END ");
                        return false;
                    }
                }
            }else{
                System.out.println("alone");

                if(!be.validateExpr(node.getAttributeOrTokenValue(decl).getValue())) {
                    //System.out.println("END2: " + be.getValue().getStr());
                    return false;
                }
            }
        }
        if(className){

        }

        return true;
    }

    public boolean saveAndUpdateFilter(String text){
        System.out.println("SAVE");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("filter.cfg", "UTF-8");
            writer.print(text);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            noError = false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            noError = false;
        }
        noError = readFilter();
        return noError;
    }
}