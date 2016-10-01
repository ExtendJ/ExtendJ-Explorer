/* This file was generated with JastAdd2 (http://jastadd.org) version 2.1.13-47-g075c2ed */
package CalcASM.src.java.gen;

import java.io.PrintStream;

/**
 * @ast node
 * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/calc.ast:6
 * @production Div : {@link BinExpr};
 */
public class Div extends BinExpr implements Cloneable {
  /**
   * @aspect CodeGen
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/CodeGen.jrag:137
   */
  public void genEval(PrintStream out) {
    getLeft().genEval(out);
    out.println("        pushq %rax");
    getRight().genEval(out);
    out.println("        movq %rax, %rbx");
    out.println("        popq %rax");
    out.println("        movq $0, %rdx");// NB: clear RDX to prepare division RDX:RAX / RBX
    out.println("        idivq %rbx");
  }

  /**
   * @aspect PrettyPrint
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/PrettyPrint.jrag:21
   */
  public void prettyPrint(PrintStream out, String ind) {
    getLeft().prettyPrint(out, ind);
    out.print(" / ");
    getRight().prettyPrint(out, ind);
  }

  /**
   * @declaredat ASTNode:1
   */
  public Div() {
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
  public Div(Expr p0, Expr p1) {
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
  public Div clone() throws CloneNotSupportedException {
    Div node = (Div) super.clone();
    return node;
  }

  /**
   * @apilevel internal
   * @declaredat ASTNode:45
   */
  public Div copy() {
    try {
      Div node = (Div) clone();
      node.parent = null;
      if (children != null) {
        node.children = (ASTNode[]) children.clone();
      }
      return node;
    } catch (CloneNotSupportedException e) {
      throw new Error("Error: clone not supported for " + getClass().getName());
    }
  }

  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   *
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:64
   * @deprecated Please use treeCopy or treeCopyNoTransform instead
   */
  @Deprecated public Div fullCopy() {
    return treeCopyNoTransform();
  }

  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   *
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:74
   */
  public Div treeCopyNoTransform() {
    Div tree = (Div) copy();
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        ASTNode child = (ASTNode) children[i];
        if (child != null) {
          child = child.treeCopyNoTransform();
          tree.setChild(child, i);
        }
      }
    }
    return tree;
  }

  /**
   * Create a deep copy of the AST subtree at this node.
   * The subtree of this node is traversed to trigger rewrites before copy.
   * The copy is dangling, i.e. has no parent.
   *
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:94
   */
  public Div treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }

  /**
   * @apilevel internal
   * @declaredat ASTNode:101
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node);
  }

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
