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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:92
 * @production ArrayInit : {@link Expr} ::= <span class="component">Init:{@link Expr}*</span>;

 */
public class ArrayInit extends Expr implements Cloneable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:262
   */
  public void prettyPrint(StringBuffer sb) {
    sb.append("{ ");
    if(getNumInit() > 0) {
      getInit(0).prettyPrint(sb);
      for(int i = 1; i < getNumInit(); i++) {
        sb.append(", ");
        getInit(i).prettyPrint(sb);
      }
    }
    sb.append(" } ");
  }
  /**
   * @aspect TypeCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeCheck.jrag:143
   */
  public void typeCheck() {
    TypeDecl initializerType = declType().componentType();
    if(initializerType.isUnknown())
      error("the dimension of the initializer is larger than the expected dimension");
    for(int i = 0; i < getNumInit(); i++) {
      Expr e = getInit(i);
      if(!e.type().assignConversionTo(initializerType, e))
        error("the type " + e.type().name() + " of the initializer is not compatible with " + initializerType.name());
    }
  }
  /**
   * @declaredat ASTNode:1
   */
  public ArrayInit() {
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
    children = new ASTNode[1];
    setChild(new List(), 0);
  }
  /**
   * @declaredat ASTNode:14
   */
  public ArrayInit(List<Expr> p0) {
    setChild(p0, 0);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:20
   */
  protected int numChildren() {
    return 1;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:26
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:32
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    computeDABefore_int_Variable_reset();
    computeDUbefore_int_Variable_reset();
    type_reset();
    declType_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:42
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:48
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:54
   */
  public ArrayInit clone() throws CloneNotSupportedException {
    ArrayInit node = (ArrayInit) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:61
   */
  public ArrayInit copy() {
    try {
      ArrayInit node = (ArrayInit) clone();
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
   * @declaredat ASTNode:80
   */
  public ArrayInit fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:89
   */
  public ArrayInit treeCopyNoTransform() {
    ArrayInit tree = (ArrayInit) copy();
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
   * @declaredat ASTNode:109
   */
  public ArrayInit treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:116
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) ;    
  }
  /**
   * Replaces the Init list.
   * @param list The new list node to be used as the Init list.
   * @apilevel high-level
   */
  public void setInitList(List<Expr> list) {
    setChild(list, 0);
  }
  /**
   * Retrieves the number of children in the Init list.
   * @return Number of children in the Init list.
   * @apilevel high-level
   */
  public int getNumInit() {
    return getInitList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Init list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Init list.
   * @apilevel low-level
   */
  public int getNumInitNoTransform() {
    return getInitListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Init list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Init list.
   * @apilevel high-level
   */
  public Expr getInit(int i) {
    return (Expr) getInitList().getChild(i);
  }
  /**
   * Check whether the Init list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasInit() {
    return getInitList().getNumChild() != 0;
  }
  /**
   * Append an element to the Init list.
   * @param node The element to append to the Init list.
   * @apilevel high-level
   */
  public void addInit(Expr node) {
    List<Expr> list = (parent == null || state == null) ? getInitListNoTransform() : getInitList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addInitNoTransform(Expr node) {
    List<Expr> list = getInitListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Init list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setInit(Expr node, int i) {
    List<Expr> list = getInitList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Init list.
   * @return The node representing the Init list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Init")
  public List<Expr> getInitList() {
    List<Expr> list = (List<Expr>) getChild(0);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the Init list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Init list.
   * @apilevel low-level
   */
  public List<Expr> getInitListNoTransform() {
    return (List<Expr>) getChildNoTransform(0);
  }
  /**
   * Retrieves the Init list.
   * @return The node representing the Init list.
   * @apilevel high-level
   */
  public List<Expr> getInits() {
    return getInitList();
  }
  /**
   * Retrieves the Init list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Init list.
   * @apilevel low-level
   */
  public List<Expr> getInitsNoTransform() {
    return getInitListNoTransform();
  }
  /**
   * @attribute syn
   * @aspect ConstantExpression
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ConstantExpression.jrag:199
   */
  @ASTNodeAnnotation.Attribute
  public boolean representableIn(TypeDecl t) {
    ASTNode$State state = state();
    try {
        for(int i = 0; i < getNumInit(); i++)
          if(!getInit(i).representableIn(t))
            return false;
        return true;
      }
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public boolean isDAafter(Variable v) {
    ASTNode$State state = state();
    boolean isDAafter_Variable_value = getNumInit() == 0 ? isDAbefore(v) : getInit(getNumInit()-1).isDAafter(v);

    return isDAafter_Variable_value;
  }
  protected java.util.Map computeDABefore_int_Variable_values;
/**
 * @apilevel internal
 */
private void computeDABefore_int_Variable_reset() {
  computeDABefore_int_Variable_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean computeDABefore(int childIndex, Variable v) {
    java.util.List _parameters = new java.util.ArrayList(2);
    _parameters.add(Integer.valueOf(childIndex));
    _parameters.add(v);
    if (computeDABefore_int_Variable_values == null) computeDABefore_int_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(computeDABefore_int_Variable_values.containsKey(_parameters)) {
      return ((Boolean)computeDABefore_int_Variable_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean computeDABefore_int_Variable_value = computeDABefore_compute(childIndex, v);
    if (isFinal && num == state().boundariesCrossed) {
      computeDABefore_int_Variable_values.put(_parameters, Boolean.valueOf(computeDABefore_int_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return computeDABefore_int_Variable_value;
  }
  /**
   * @apilevel internal
   */
  private boolean computeDABefore_compute(int childIndex, Variable v) {
      if(childIndex == 0) return isDAbefore(v);
      int index = childIndex-1;
      while(index > 0 && getInit(index).isConstant())
        index--;
      return getInit(childIndex-1).isDAafter(v);
    }
  @ASTNodeAnnotation.Attribute
  public boolean isDUafter(Variable v) {
    ASTNode$State state = state();
    boolean isDUafter_Variable_value = getNumInit() == 0 ? isDUbefore(v) : getInit(getNumInit()-1).isDUafter(v);

    return isDUafter_Variable_value;
  }
  protected java.util.Map computeDUbefore_int_Variable_values;
/**
 * @apilevel internal
 */
private void computeDUbefore_int_Variable_reset() {
  computeDUbefore_int_Variable_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean computeDUbefore(int childIndex, Variable v) {
    java.util.List _parameters = new java.util.ArrayList(2);
    _parameters.add(Integer.valueOf(childIndex));
    _parameters.add(v);
    if (computeDUbefore_int_Variable_values == null) computeDUbefore_int_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(computeDUbefore_int_Variable_values.containsKey(_parameters)) {
      return ((Boolean)computeDUbefore_int_Variable_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean computeDUbefore_int_Variable_value = computeDUbefore_compute(childIndex, v);
    if (isFinal && num == state().boundariesCrossed) {
      computeDUbefore_int_Variable_values.put(_parameters, Boolean.valueOf(computeDUbefore_int_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return computeDUbefore_int_Variable_value;
  }
  /**
   * @apilevel internal
   */
  private boolean computeDUbefore_compute(int childIndex, Variable v) {
      if(childIndex == 0) return isDUbefore(v);
      int index = childIndex-1;
      while(index > 0 && getInit(index).isConstant())
        index--;
      return getInit(childIndex-1).isDUafter(v);
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
    type_value = declType();
    if (isFinal && num == state().boundariesCrossed) {
      type_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return type_value;
  }
  /**
   * @attribute syn
   * @aspect PreciseRethrow
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/PreciseRethrow.jrag:117
   */
  @ASTNodeAnnotation.Attribute
  public boolean modifiedInScope(Variable var) {
    ASTNode$State state = state();
    try {
        for (int i = 0; i < getNumInit(); ++i) {
          if (getInit(i).modifiedInScope(var)) {
            return true;
          }
        }
        return false;
      }
    finally {
    }
  }
  /**
   * @attribute inh
   * @aspect TypeAnalysis
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:257
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl declType() {
    if(declType_computed) {
      return declType_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    declType_value = getParent().Define_TypeDecl_declType(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      declType_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return declType_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean declType_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl declType_value;
/**
 * @apilevel internal
 */
private void declType_reset() {
  declType_computed = false;
  declType_value = null;
}  
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:42
   * @apilevel internal
   */
  public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
    if (caller == getInitListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return true;
    }
    else {
      return getParent().Define_boolean_isSource(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:522
   * @apilevel internal
   */
  public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
    if (caller == getInitListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return computeDABefore(childIndex, v);
    }
    else {
      return getParent().Define_boolean_isDAbefore(this, caller, v);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:942
   * @apilevel internal
   */
  public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
    if (caller == getInitListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return computeDUbefore(childIndex, v);
    }
    else {
      return getParent().Define_boolean_isDUbefore(this, caller, v);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:265
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_declType(ASTNode caller, ASTNode child) {
    if (caller == getInitListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return declType().componentType();
    }
    else {
      return getParent().Define_TypeDecl_declType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericMethodsInference.jrag:37
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_assignConvertedType(ASTNode caller, ASTNode child) {
    if (caller == getInitListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return declType().componentType();
    }
    else {
      return getParent().Define_TypeDecl_assignConvertedType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:75
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_targetType(ASTNode caller, ASTNode child) {
    if (caller == getInitListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      {
		if(!(targetType() instanceof ArrayDecl)) {
			return targetType();
		}
		else {
			return ((ArrayDecl)targetType()).componentType();
		}
	}
    }
    else {
      return getParent().Define_TypeDecl_targetType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:350
   * @apilevel internal
   */
  public boolean Define_boolean_assignmentContext(ASTNode caller, ASTNode child) {
    if (caller == getInitListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return true;
    }
    else {
      return getParent().Define_boolean_assignmentContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:351
   * @apilevel internal
   */
  public boolean Define_boolean_invocationContext(ASTNode caller, ASTNode child) {
    if (caller == getInitListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else {
      return getParent().Define_boolean_invocationContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:352
   * @apilevel internal
   */
  public boolean Define_boolean_castContext(ASTNode caller, ASTNode child) {
    if (caller == getInitListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else {
      return getParent().Define_boolean_castContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:353
   * @apilevel internal
   */
  public boolean Define_boolean_stringContext(ASTNode caller, ASTNode child) {
    if (caller == getInitListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else {
      return getParent().Define_boolean_stringContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:354
   * @apilevel internal
   */
  public boolean Define_boolean_numericContext(ASTNode caller, ASTNode child) {
    if (caller == getInitListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else {
      return getParent().Define_boolean_numericContext(this, caller);
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
