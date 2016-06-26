package drast.model;

import drast.model.filteredtree.GenericTreeNode;

/**
 * This is the main class for the DrAST system. This class will create an ASTBrain object that will generate the
 * filtered AST.
 *
 * DrAST can be started by calling run() method on this class or DrASTGUI (in the DrAST.views.gui package).
 *
 */
public class DrAST {
	public static final String VERSION = "build-1.0.5";

	public final static String FILE_NAME = "DrAST-result";
    public final static String CLUSTER_STRING = "cluster";
    public final static String DEFAULT_FILTER_NAME = "filter.fcl";

	private ASTBrain brain;

    private Object root;
    private String filterPath;
    private boolean done;
	private boolean listRoot;
	private boolean noAstData;

	public DrAST(){
		done = false;
		noAstData = true;
	}
	
	public DrAST(Object root){
        this.root = root;
		filterPath = DEFAULT_FILTER_NAME;
        done = false;
		noAstData = false;
	}

	public DrAST(Object root, boolean listRoot){
		this.root = root;
		filterPath = DEFAULT_FILTER_NAME;
		done = false;
		this.listRoot = listRoot;
		noAstData = false;
	}

    public boolean hasRun(){ return done; }

	public boolean noRoot(){ return noAstData; }

    /**
     * Sets the directory of the projects
     * @param dir
     */
    public void setFilterPath(String dir){filterPath = dir;}

    /**
     * run() generates the AST
     */
	public void run(){
		brain = noAstData ? new ASTBrain() : new ASTBrain(root, filterPath, listRoot);
        done = true;
	}

    public ASTBrain getBrain(){ return brain; }

    /**
     * main function for starting a DrASTAPI session.
     * @param args
     */
	public static void main(String[] args) {
		System.exit(0);
	}
}