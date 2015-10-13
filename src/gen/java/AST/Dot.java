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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:16
 * @production Dot : {@link AbstractDot};

 */
public class Dot extends AbstractDot implements Cloneable {
  /**
   * @aspect QualifiedNames
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ResolveAmbiguousNames.jrag:99
   */
  public Dot lastDot() {
    Dot node = this;
    while(node.getRightNoTransform() instanceof Dot)
      node = (Dot)node.getRightNoTransform();
    return node;
  }
  /**
   * @aspect QualifiedNames
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ResolveAmbiguousNames.jrag:113
   */
  public Dot qualifiesAccess(Access access) {
    Dot lastDot = lastDot();
    Expr l = lastDot.getRightNoTransform();
    Dot dot = new Dot(lastDot.getRightNoTransform(), access);
    dot.setStart(l.getStart());
    dot.setEnd(access.getEnd());
    lastDot.setRight(dot);
    return this;
  }
  /**
   * @aspect QualifiedNames
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ResolveAmbiguousNames.jrag:124
   */
  private Access qualifyTailWith(Access expr) {
    if(getRight/*NoTransform*/() instanceof AbstractDot) {
      AbstractDot dot = (AbstractDot)getRight/*NoTransform*/();
      return expr.qualifiesAccess(dot.getRight/*NoTransform*/());
    }
    return expr;
  }
  /**
   * @aspect QualifiedNames
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ResolveAmbiguousNames.jrag:141
   */
  public Access extractLast() {
    return lastDot().getRightNoTransform();
  }
  /**
   * @aspect QualifiedNames
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ResolveAmbiguousNames.jrag:144
   */
  public void replaceLast(Access access) {
    lastDot().setRight(access);
  }
  /**
   * @declaredat ASTNode:1
   */
  public Dot() {
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
    children = new ASTNode[2];
  }
  /**
   * @declaredat ASTNode:13
   */
  public Dot(Expr p0, Access p1) {
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
  public boolean mayHaveRewrite() {
    return true;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:32
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:38
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:44
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:50
   */
  public Dot clone() throws CloneNotSupportedException {
    Dot node = (Dot) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:57
   */
  public Dot copy() {
    try {
      Dot node = (Dot) clone();
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
   * @declaredat ASTNode:76
   */
  public Dot fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:85
   */
  public Dot treeCopyNoTransform() {
    Dot tree = (Dot) copy();
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
   * @declaredat ASTNode:105
   */
  public Dot treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:112
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) ;    
  }
  /**
   * Replaces the Left child.
   * @param node The new node to replace the Left child.
   * @apilevel high-level
   */
  public void setLeft(Expr node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the Left child.
   * @return The current node used as the Left child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Left")
  public Expr getLeft() {
    return (Expr) getChild(0);
  }
  /**
   * Retrieves the Left child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Left child.
   * @apilevel low-level
   */
  public Expr getLeftNoTransform() {
    return (Expr) getChildNoTransform(0);
  }
  /**
   * Replaces the Right child.
   * @param node The new node to replace the Right child.
   * @apilevel high-level
   */
  public void setRight(Access node) {
    setChild(node, 1);
  }
  /**
   * Retrieves the Right child.
   * @return The current node used as the Right child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Right")
  public Access getRight() {
    return (Access) getChild(1);
  }
  /**
   * Retrieves the Right child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Right child.
   * @apilevel low-level
   */
  public Access getRightNoTransform() {
    return (Access) getChildNoTransform(1);
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    // Declared at @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ResolveAmbiguousNames.jrag:210
    if (!duringSyntacticClassification() && leftSide().isPackageAccess() && rightSide().isPackageAccess()) {
      state().duringNameResolution++;
      ASTNode result = rewriteRule0();
      state().duringNameResolution--;
      return result;
    }    // Declared at @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ResolveAmbiguousNames.jrag:222
    if (!duringSyntacticClassification() && leftSide().isPackageAccess() && !((Access)leftSide()).hasPrevExpr() && rightSide() instanceof TypeAccess) {
      state().duringNameResolution++;
      ASTNode result = rewriteRule1();
      state().duringNameResolution--;
      return result;
    }    return super.rewriteTo();
  }  /**
   * @declaredat @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ResolveAmbiguousNames.jrag:210
   * @apilevel internal
   */  private Access rewriteRule0() {
{
      PackageAccess left = (PackageAccess)leftSide();
      PackageAccess right = (PackageAccess)rightSide();
      left.setPackage(left.getPackage() + "." + right.getPackage());
      left.setEnd(right.end());
      return qualifyTailWith(left);
    }  }
  /**
   * @declaredat @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ResolveAmbiguousNames.jrag:222
   * @apilevel internal
   */  private Access rewriteRule1() {
{
      PackageAccess left = (PackageAccess)leftSide();
      TypeAccess right = (TypeAccess)rightSide();
      right.setPackage(left.getPackage());
      right.setStart(left.start());
      return qualifyTailWith(right);
    }  }
}
