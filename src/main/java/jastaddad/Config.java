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
        //System.out.println(node.fullName);
        return noError && (configs.configs().get("showAll").getBool() || configs.getNodes().containsKey(node.className) || configs.getNodes().containsKey(node.fullName));
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