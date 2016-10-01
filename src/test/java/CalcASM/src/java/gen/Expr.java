/* This file was generated with JastAdd2 (http://jastadd.org) version 2.1.13-47-g075c2ed */
package CalcASM.src.java.gen;

import java.io.PrintStream;

/**
 * @ast node
 * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/calc.ast:3
 * @production Expr : {@link ASTNode};
 */
public abstract class Expr extends ASTNode<ASTNode> implements Cloneable {
  /**
   * Generate code to evaluate the expression and
   * store the result in RAX.
   * <p>
   * This must be implemented for every subclass of Expr!
   *
   * @aspect CodeGen
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/CodeGen.jrag:118
   */
  abstract public void genEval(PrintStream out);

  /**
   * @declaredat ASTNode:1
   */
  public Expr() {
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
  }

  /**
   * @apilevel low-level
   * @declaredat ASTNode:15
   */
  protected int numChildren() {
    return 0;
  }

  /**
   * @apilevel internal
   * @declaredat ASTNode:21
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }

  /**
   * @apilevel internal
   * @declaredat ASTNode:27
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }

  /**
   * @apilevel internal
   * @declaredat ASTNode:33
   */
  public Expr clone() throws CloneNotSupportedException {
    Expr node = (Expr) super.clone();
    return node;
  }

  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   *
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:44
   * @deprecated Please use treeCopy or treeCopyNoTransform instead
   */
  @Deprecated public abstract Expr fullCopy();

  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   *
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:52
   */
  public abstract Expr treeCopyNoTransform();

  /**
   * Create a deep copy of the AST subtree at this node.
   * The subtree of this node is traversed to trigger rewrites before copy.
   * The copy is dangling, i.e. has no parent.
   *
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:60
   */
  public abstract Expr treeCopy();
}
