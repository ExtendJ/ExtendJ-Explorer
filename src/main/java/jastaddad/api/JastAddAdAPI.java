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
    public final static String DEFAULT_FILTER_NAME = "filter.fcl";

	private ASTAPI api;

    private Object root;
    private String filterPath;
    private boolean done;
	private boolean listRoot;
	
	public JastAddAdAPI(Object root){
        this.root = root;
		filterPath = DEFAULT_FILTER_NAME;
        done = false;
	}

	public JastAddAdAPI(Object root, boolean listRoot){
		this.root = root;
		filterPath = DEFAULT_FILTER_NAME;
		done = false;
		this.listRoot = listRoot;
	}

    public boolean hasRun(){
        return done;
    }

    /**
     * Sets the directory of the projects
     * @param dir
     */
    public void setFilterPath(String dir){filterPath = dir;}

    /**
     * run() generates the AST
     */
	public void run(){
        api = new ASTAPI(root, filterPath, listRoot);
        done = true;
	}

    public ASTAPI api(){ return api;};

	public GenericTreeNode getFilteredTree(){
		return api.getFilteredTree();
	}

    /**
     * main function for starting a JastAddAdAPI session.
     * @param args
     */
	public static void main(String[] args) {
		String filename = "sample.fcl";
		try{
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
			System.out.println("File not found: " + filename);
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}