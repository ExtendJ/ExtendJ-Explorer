package drast.model;

/**
 * This is the main class for the DrAST system. This class will create an ASTBrain object that will generate the
 * filtered AST.
 * <p>
 * DrAST can be started by calling run() method on this class or DrASTGUI (in the DrAST.views.gui package).
 */
public class DrAST {
  public static final String DRAST_VERSION = "1.2.0";
  public static final String FILTER_EXTENSION = ".fl2";

  public static final String CLUSTER_STRING = "cluster";

  private final FilteredTreeBuilder traverser;

  private final Object astRoot;

  public DrAST(Object root, TreeFilter filter) {
    this.astRoot = root;
    traverser = new FilteredTreeBuilder(root, filter);
  }

  public FilteredTreeBuilder getTraverser() {
    return traverser;
  }

  public static void main(String[] args) {
    System.exit(0);
  }

  public Object getRoot() {
    return astRoot;
  }
}
