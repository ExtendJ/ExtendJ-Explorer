/* This file was generated with JastAdd2 (http://jastadd.org) version 2.1.13-47-g075c2ed */
package CalcASM.src.java.gen;

/**
 * @ast node
 * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/calc.ast:4
 * @production BinExpr : {@link Expr} ::= <span class="component">Left:{@link Expr}</span> <span class="component">Right:{@link Expr}</span>;
 */
public abstract class BinExpr extends Expr implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public BinExpr() {
    super();
  }

  /**
   * Initializes the child array to the correct size.
   * Initializes List and Opt nta children.
   *
   * @apilevel internal
   * @ast method
   * @declaredat ASTNode:10
   */
  public void init$Children() {
    children = new ASTNode[2];
  }

  /**
   * @declaredat ASTNode:13
   */
  public BinExpr(Expr p0, Expr p1) {
    setChild(p0, 0);
    setChild(p1, 1);
  }

  /**
   * @apilevel low-level
   * @declaredat ASTNode:20
   */
  protected int numChildren() {
    return 2;
  }

  /**
   * @apilevel internal
   * @declaredat ASTNode:26
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }

  /**
   * @apilevel internal
   * @declaredat ASTNode:32
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }

  /**
   * @apilevel internal
   * @declaredat ASTNode:38
   */
  public BinExpr clone() throws CloneNotSupportedException {
    BinExpr node = (BinExpr) super.clone();
    return node;
  }

  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   *
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:49
   * @deprecated Please use treeCopy or treeCopyNoTransform instead
   */
  @Deprecated public abstract BinExpr fullCopy();

  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   *
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:57
   */
  public abstract BinExpr treeCopyNoTransform();

  /**
   * Create a deep copy of the AST subtree at this node.
   * The subtree of this node is traversed to trigger rewrites before copy.
   * The copy is dangling, i.e. has no parent.
   *
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:65
   */
  public abstract BinExpr treeCopy();

  /**
   * Replaces the Left child.
   *
   * @param node The new node to replace the Left child.
   * @apilevel high-level
   */
  public void setLeft(Expr node) {
    setChild(node, 0);
  }

  /**
   * Retrieves the Left child.
   *
   * @return The current node used as the Left child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name = "Left") public Expr getLeft() {
    return (Expr) getChild(0);
  }

  /**
   * Retrieves the Left child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   *
   * @return The current node used as the Left child.
   * @apilevel low-level
   */
  public Expr getLeftNoTransform() {
    return (Expr) getChildNoTransform(0);
  }

  /**
   * Replaces the Right child.
   *
   * @param node The new node to replace the Right child.
   * @apilevel high-level
   */
  public void setRight(Expr node) {
    setChild(node, 1);
  }

  /**
   * Retrieves the Right child.
   *
   * @return The current node used as the Right child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name = "Right") public Expr getRight() {
    return (Expr) getChild(1);
  }

  /**
   * Retrieves the Right child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   *
   * @return The current node used as the Right child.
   * @apilevel low-level
   */
  public Expr getRightNoTransform() {
    return (Expr) getChildNoTransform(1);
  }
}
