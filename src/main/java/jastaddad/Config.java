package jastaddad;

import AST.*;

import java.io.*;

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

        if(tellingName){
            NodeConfig cNode = configs.getNodes().get(node.fullName);
            if(cNode.hasFilter()){
                for(BinExpr be : cNode.getFilter().getBinExprList()){
                    String decl = be.getDecl().getID();
                    if(!node.containsAttributeOrToken(decl))
                        return false;
                    if(be.isDoubleDecl()){
                        String decl2 = ((IdDecl)be.getValue()).getID();
                        if(node.containsAttributeOrToken(decl2))
                            if(!be.validateExpression(node.getAttributeOrTokenValue(decl), node.getAttributeOrTokenValue(decl2)))
                                return false;
                    }else{
                        if(be.validateExpression(node.getAttributeOrTokenValue(decl)))
                            return false;
                    }
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