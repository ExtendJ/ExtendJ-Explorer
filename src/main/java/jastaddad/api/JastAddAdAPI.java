package jastaddad.api;

import configAST.ConfigParser;
import configAST.ConfigScanner;
import configAST.DebuggerConfig;
import configAST.ErrorMessage;
import jastaddad.api.filteredtree.GenericTreeNode;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * This is the main class for the JastAddAd system. This class will create an ASTAPI object that will generate the
 * filtered AST.
 *
 * JastAddAd can be started by calling run() method on this class or JastAddAdUI (in the jastaddad.ui package).
 *
 */
public class JastAddAdAPI {
	public final static String FILE_NAME = "jastAddAd-result";
    public final static String CLUSTER_STRING = "cluster";
	private ASTAPI api;

    private Object root;
    private String filterDir;
    private boolean done;
	
	public JastAddAdAPI(Object root){
        this.root = root;
        filterDir = "";
        done = false;
	}

    public boolean hasRun(){
        return done;
    }

    /**
     * Sets the directory of the projects
     * @param dir
     */
    public void setFilterDir(String dir){filterDir = dir;}

    /**
     * run() generates the AST
     */
	public void run(){
        api = new ASTAPI(root, filterDir);
        done = true;
	}

    public ASTAPI api(){return api;};

	public GenericTreeNode getFilteredTree(){
		return api.getFilteredTree();
	}

    /**
     * main function for starting a JastAddAdAPI session.
     * @param args
     */
	public static void main(String[] args) {
		try{
			String filename = "sample.cfg";
			ConfigScanner scanner = new ConfigScanner(new FileReader(filename));
			ConfigParser parser = new ConfigParser();
			DebuggerConfig program = (DebuggerConfig) parser.parse(scanner);
			if (!program.errors().isEmpty()) {
				System.err.println();
				System.err.println("Errors: ");
				for (ErrorMessage e: program.errors()) {
					System.err.println("- " + e);
				}
			} else {
				JastAddAdAPI debugger = new JastAddAdAPI(program);
                debugger.run();
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}