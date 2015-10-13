/* This file was generated with JastAdd2 (http://jastadd.org) version 2.1.8 */
package AST;

import java.util.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.io.File;
import java.util.Set;
import java.util.ArrayList;
import beaver.*;
import org.jastadd.util.*;
import java.util.zip.*;
import java.io.*;
import java.io.FileNotFoundException;
/**
 * @ast node
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/grammar/IntersectionCasts.ast:1
 * @production IntersectionCastExpr : {@link Expr} ::= <span class="component">TypeAccess:{@link Access}</span> <span class="component">TypeList:{@link Access}*</span> <span class="component">{@link Expr}</span>;

 */
public class IntersectionCastExpr extends Expr implements Cloneable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/PrettyPrint.jadd:83
   */
  public void prettyPrint(StringBuffer sb) {
		sb.append("(");
		getTypeAccess().prettyPrint(sb);
		if(getNumTypeList() > 0) {
			sb.append(" & ");
			getTypeList(0).prettyPrint(sb);
			for(int i = 1; i < getNumTypeList(); i++) {
				sb.append(" & ");
				getTypeList(i).prettyPrint(sb);
			}
		}
		sb.append(")");
		getExpr().prettyPrint(sb);
	}
  /**
   * @declaredat ASTNode:1
   */
  public IntersectionCastExpr() {
    super();
  }
  /**
   * Initializes the child array to the correct size.
   * Initializes List and Opt nta children.
   * @apilevel internal
   * @ast method
   * @declaredat ASTNode:10
   */
  public void init$Children() {
    children = new ASTNode[3];
    setChild(new List(), 1);
  }
  /**
   * @declaredat ASTNode:14
   */
  public IntersectionCastExpr(Access p0, List<Access> p1, Expr p2) {
    setChild(p0, 0);
    setChild(p1, 1);
    setChild(p2, 2);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:22
   */
  protected int numChildren() {
    return 3;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:28
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:34
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    type_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:41
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:47
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:53
   */
  public IntersectionCastExpr clone() throws CloneNotSupportedException {
    IntersectionCastExpr node = (IntersectionCastExpr) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:60
   */
  public IntersectionCastExpr copy() {
    try {
      IntersectionCastExpr node = (IntersectionCastExpr) clone();
      node.parent = null;
      if(children != null) {
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
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @deprecated Please use emitTreeCopy or emitTreeCopyNoTransform instead
   * @declaredat ASTNode:79
   */
  public IntersectionCastExpr fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:88
   */
  public IntersectionCastExpr treeCopyNoTransform() {
    IntersectionCastExpr tree = (IntersectionCastExpr) copy();
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        ASTNode child = (ASTNode) children[i];
        if(child != null) {
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
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:108
   */
  public IntersectionCastExpr treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:115
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) ;    
  }
  /**
   * Replaces the TypeAccess child.
   * @param node The new node to replace the TypeAccess child.
   * @apilevel high-level
   */
  public void setTypeAccess(Access node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the TypeAccess child.
   * @return The current node used as the TypeAccess child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="TypeAccess")
  public Access getTypeAccess() {
    return (Access) getChild(0);
  }
  /**
   * Retrieves the TypeAccess child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the TypeAccess child.
   * @apilevel low-level
   */
  public Access getTypeAccessNoTransform() {
    return (Access) getChildNoTransform(0);
  }
  /**
   * Replaces the TypeList list.
   * @param list The new list node to be used as the TypeList list.
   * @apilevel high-level
   */
  public void setTypeListList(List<Access> list) {
    setChild(list, 1);
  }
  /**
   * Retrieves the number of children in the TypeList list.
   * @return Number of children in the TypeList list.
   * @apilevel high-level
   */
  public int getNumTypeList() {
    return getTypeListList().getNumChild();
  }
  /**
   * Retrieves the number of children in the TypeList list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the TypeList list.
   * @apilevel low-level
   */
  public int getNumTypeListNoTransform() {
    return getTypeListListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the TypeList list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the TypeList list.
   * @apilevel high-level
   */
  public Access getTypeList(int i) {
    return (Access) getTypeListList().getChild(i);
  }
  /**
   * Check whether the TypeList list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasTypeList() {
    return getTypeListList().getNumChild() != 0;
  }
  /**
   * Append an element to the TypeList list.
   * @param node The element to append to the TypeList list.
   * @apilevel high-level
   */
  public void addTypeList(Access node) {
    List<Access> list = (parent == null || state == null) ? getTypeListListNoTransform() : getTypeListList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addTypeListNoTransform(Access node) {
    List<Access> list = getTypeListListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the TypeList list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setTypeList(Access node, int i) {
    List<Access> list = getTypeListList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the TypeList list.
   * @return The node representing the TypeList list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="TypeList")
  public List<Access> getTypeListList() {
    List<Access> list = (List<Access>) getChild(1);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the TypeList list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the TypeList list.
   * @apilevel low-level
   */
  public List<Access> getTypeListListNoTransform() {
    return (List<Access>) getChildNoTransform(1);
  }
  /**
   * Retrieves the TypeList list.
   * @return The node representing the TypeList list.
   * @apilevel high-level
   */
  public List<Access> getTypeLists() {
    return getTypeListList();
  }
  /**
   * Retrieves the TypeList list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the TypeList list.
   * @apilevel low-level
   */
  public List<Access> getTypeListsNoTransform() {
    return getTypeListListNoTransform();
  }
  /**
   * Replaces the Expr child.
   * @param node The new node to replace the Expr child.
   * @apilevel high-level
   */
  public void setExpr(Expr node) {
    setChild(node, 2);
  }
  /**
   * Retrieves the Expr child.
   * @return The current node used as the Expr child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Expr")
  public Expr getExpr() {
    return (Expr) getChild(2);
  }
  /**
   * Retrieves the Expr child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Expr child.
   * @apilevel low-level
   */
  public Expr getExprNoTransform() {
    return (Expr) getChildNoTransform(2);
  }
  /**
   * @apilevel internal
   */
  protected boolean type_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl type_value;
/**
 * @apilevel internal
 */
private void type_reset() {
  type_computed = false;
  type_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl type() {
    if(type_computed) {
      return type_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    type_value = unknownType();
    if (isFinal && num == state().boundariesCrossed) {
      type_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return type_value;
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
