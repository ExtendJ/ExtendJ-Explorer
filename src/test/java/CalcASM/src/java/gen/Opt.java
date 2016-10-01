/* This file was generated with JastAdd2 (http://jastadd.org) version 2.1.13-47-g075c2ed */
package CalcASM.src.java.gen;

/**
 * @ast node
 * @production Opt : {@link ASTNode};
 */
public class Opt<T extends ASTNode> extends ASTNode<T> implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public Opt() {
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
   * @declaredat ASTNode:12
   */
  public Opt(T opt) {
    setChild(opt, 0);
  }

  /**
   * @apilevel internal
   * @declaredat ASTNode:18
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }

  /**
   * @apilevel internal
   * @declaredat ASTNode:24
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }

  /**
   * @apilevel internal
   * @declaredat ASTNode:30
   */
  public Opt<T> clone() throws CloneNotSupportedException {
    Opt node = (Opt) super.clone();
    return node;
  }

  /**
   * @apilevel internal
   * @declaredat ASTNode:37
   */
  public Opt<T> copy() {
    try {
      Opt node = (Opt) clone();
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
   * @declaredat ASTNode:56
   * @deprecated Please use treeCopy or treeCopyNoTransform instead
   */
  @Deprecated public Opt<T> fullCopy() {
    return treeCopyNoTransform();
  }

  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   *
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:66
   */
  public Opt<T> treeCopyNoTransform() {
    Opt tree = (Opt) copy();
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
   * @declaredat ASTNode:86
   */
  public Opt<T> treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }

  /**
   * @apilevel internal
   * @declaredat ASTNode:93
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node);
  }
}
