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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:132
 * @production ArrayCreationExpr : {@link PrimaryExpr} ::= <span class="component">TypeAccess:{@link Access}</span> <span class="component">[{@link ArrayInit}]</span>;

 */
public class ArrayCreationExpr extends PrimaryExpr implements Cloneable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:400
   */
  public void prettyPrint(StringBuffer sb) {
    sb.append("new ");
    getTypeAccess().prettyPrint(sb);
    if(hasArrayInit()) {
      getArrayInit().prettyPrint(sb);
    }
  }
  /**
   * @declaredat ASTNode:1
   */
  public ArrayCreationExpr() {
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
    setChild(new Opt(), 1);
  }
  /**
   * @declaredat ASTNode:14
   */
  public ArrayCreationExpr(Access p0, Opt<ArrayInit> p1) {
    setChild(p0, 0);
    setChild(p1, 1);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:21
   */
  protected int numChildren() {
    return 2;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:27
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:33
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    type_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:40
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:46
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:52
   */
  public ArrayCreationExpr clone() throws CloneNotSupportedException {
    ArrayCreationExpr node = (ArrayCreationExpr) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:59
   */
  public ArrayCreationExpr copy() {
    try {
      ArrayCreationExpr node = (ArrayCreationExpr) clone();
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
   * @declaredat ASTNode:78
   */
  public ArrayCreationExpr fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:87
   */
  public ArrayCreationExpr treeCopyNoTransform() {
    ArrayCreationExpr tree = (ArrayCreationExpr) copy();
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
   * @declaredat ASTNode:107
   */
  public ArrayCreationExpr treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:114
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
   * Replaces the optional node for the ArrayInit child. This is the <code>Opt</code>
   * node containing the child ArrayInit, not the actual child!
   * @param opt The new node to be used as the optional node for the ArrayInit child.
   * @apilevel low-level
   */
  public void setArrayInitOpt(Opt<ArrayInit> opt) {
    setChild(opt, 1);
  }
  /**
   * Replaces the (optional) ArrayInit child.
   * @param node The new node to be used as the ArrayInit child.
   * @apilevel high-level
   */
  public void setArrayInit(ArrayInit node) {
    getArrayInitOpt().setChild(node, 0);
  }
  /**
   * Check whether the optional ArrayInit child exists.
   * @return {@code true} if the optional ArrayInit child exists, {@code false} if it does not.
   * @apilevel high-level
   */
  public boolean hasArrayInit() {
    return getArrayInitOpt().getNumChild() != 0;
  }
  /**
   * Retrieves the (optional) ArrayInit child.
   * @return The ArrayInit child, if it exists. Returns {@code null} otherwise.
   * @apilevel low-level
   */
  public ArrayInit getArrayInit() {
    return (ArrayInit) getArrayInitOpt().getChild(0);
  }
  /**
   * Retrieves the optional node for the ArrayInit child. This is the <code>Opt</code> node containing the child ArrayInit, not the actual child!
   * @return The optional node for child the ArrayInit child.
   * @apilevel low-level
   */
  @ASTNodeAnnotation.OptChild(name="ArrayInit")
  public Opt<ArrayInit> getArrayInitOpt() {
    return (Opt<ArrayInit>) getChild(1);
  }
  /**
   * Retrieves the optional node for child ArrayInit. This is the <code>Opt</code> node containing the child ArrayInit, not the actual child!
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The optional node for child ArrayInit.
   * @apilevel low-level
   */
  public Opt<ArrayInit> getArrayInitOptNoTransform() {
    return (Opt<ArrayInit>) getChildNoTransform(1);
  }
  @ASTNodeAnnotation.Attribute
  public boolean isDAafterCreation(Variable v) {
    ASTNode$State state = state();
    boolean isDAafterCreation_Variable_value = getTypeAccess().isDAafter(v);

    return isDAafterCreation_Variable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isDAafter(Variable v) {
    ASTNode$State state = state();
    boolean isDAafter_Variable_value = hasArrayInit() ? getArrayInit().isDAafter(v) : isDAafterCreation(v);

    return isDAafter_Variable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isDUafterCreation(Variable v) {
    ASTNode$State state = state();
    boolean isDUafterCreation_Variable_value = getTypeAccess().isDUafter(v);

    return isDUafterCreation_Variable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isDUafter(Variable v) {
    ASTNode$State state = state();
    boolean isDUafter_Variable_value = hasArrayInit() ? getArrayInit().isDUafter(v) : isDUafterCreation(v);

    return isDUafter_Variable_value;
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
    type_value = getTypeAccess().type();
    if (isFinal && num == state().boundariesCrossed) {
      type_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return type_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean modifiedInScope(Variable var) {
    ASTNode$State state = state();
    boolean modifiedInScope_Variable_value = hasArrayInit() && getArrayInit().modifiedInScope(var);

    return modifiedInScope_Variable_value;
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:449
   * @apilevel internal
   */
  public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
    if (caller == getArrayInitOptNoTransform()) {
      return isDAafterCreation(v);
    }
    else {
      return getParent().Define_boolean_isDAbefore(this, caller, v);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:916
   * @apilevel internal
   */
  public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
    if (caller == getArrayInitOptNoTransform()) {
      return isDUafterCreation(v);
    }
    else {
      return getParent().Define_boolean_isDUbefore(this, caller, v);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/SyntacticClassification.jrag:87
   * @apilevel internal
   */
  public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
    if (caller == getTypeAccessNoTransform()) {
      return NameType.TYPE_NAME;
    }
    else {
      return getParent().Define_NameType_nameType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:264
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_declType(ASTNode caller, ASTNode child) {
    if (caller == getArrayInitOptNoTransform()) {
      return type();
    }
    else {
      return getParent().Define_TypeDecl_declType(this, caller);
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
