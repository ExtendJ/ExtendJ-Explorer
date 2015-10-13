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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:81
 * @production VarDeclStmt : {@link Stmt} ::= <span class="component">{@link Modifiers}</span> <span class="component">TypeAccess:{@link Access}</span> <span class="component">{@link VariableDecl}*</span> <span class="component">SingleDecl:{@link VariableDeclaration}*</span>;

 */
public class VarDeclStmt extends Stmt implements Cloneable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:180
   */
  public void prettyPrint(StringBuffer sb) {
    sb.append(indent());
    getModifiers().prettyPrint(sb);
    getTypeAccess().prettyPrint(sb);
    sb.append(" ");
    boolean first = true;
    for (VariableDecl decl: getVariableDeclList()) {
      if (!first) sb.append(", ");
      first = false;
      decl.prettyPrint(sb);
    }
    sb.append(";");
  }
  /**
   * @aspect ErrorCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ErrorCheck.jrag:217
   */
  public void collectErrors() {
    // check reachability of the multi-declaration
    checkUnreachableStmt();

    // delegate other error checks to NTA single variable declarations
    getSingleDeclList().collectErrors();
  }
  /**
   * @declaredat ASTNode:1
   */
  public VarDeclStmt() {
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
    children = new ASTNode[4];
    setChild(new List(), 2);
    setChild(new List(), 3);
  }
  /**
   * @declaredat ASTNode:15
   */
  public VarDeclStmt(Modifiers p0, Access p1, List<VariableDecl> p2) {
    setChild(p0, 0);
    setChild(p1, 1);
    setChild(p2, 2);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:23
   */
  protected int numChildren() {
    return 3;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:29
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:35
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    isDAafter_Variable_reset();
    isDUafter_Variable_reset();
    canCompleteNormally_reset();
    getSingleDeclList_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:45
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:51
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:57
   */
  public VarDeclStmt clone() throws CloneNotSupportedException {
    VarDeclStmt node = (VarDeclStmt) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:64
   */
  public VarDeclStmt copy() {
    try {
      VarDeclStmt node = (VarDeclStmt) clone();
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
   * @declaredat ASTNode:83
   */
  public VarDeclStmt fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:92
   */
  public VarDeclStmt treeCopyNoTransform() {
    VarDeclStmt tree = (VarDeclStmt) copy();
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        switch (i) {
        case 3:
          tree.children[i] = new List();
          continue;
        }
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
   * @declaredat ASTNode:117
   */
  public VarDeclStmt treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:124
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) ;    
  }
  /**
   * Replaces the Modifiers child.
   * @param node The new node to replace the Modifiers child.
   * @apilevel high-level
   */
  public void setModifiers(Modifiers node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the Modifiers child.
   * @return The current node used as the Modifiers child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Modifiers")
  public Modifiers getModifiers() {
    return (Modifiers) getChild(0);
  }
  /**
   * Retrieves the Modifiers child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Modifiers child.
   * @apilevel low-level
   */
  public Modifiers getModifiersNoTransform() {
    return (Modifiers) getChildNoTransform(0);
  }
  /**
   * Replaces the TypeAccess child.
   * @param node The new node to replace the TypeAccess child.
   * @apilevel high-level
   */
  public void setTypeAccess(Access node) {
    setChild(node, 1);
  }
  /**
   * Retrieves the TypeAccess child.
   * @return The current node used as the TypeAccess child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="TypeAccess")
  public Access getTypeAccess() {
    return (Access) getChild(1);
  }
  /**
   * Retrieves the TypeAccess child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the TypeAccess child.
   * @apilevel low-level
   */
  public Access getTypeAccessNoTransform() {
    return (Access) getChildNoTransform(1);
  }
  /**
   * Replaces the VariableDecl list.
   * @param list The new list node to be used as the VariableDecl list.
   * @apilevel high-level
   */
  public void setVariableDeclList(List<VariableDecl> list) {
    setChild(list, 2);
  }
  /**
   * Retrieves the number of children in the VariableDecl list.
   * @return Number of children in the VariableDecl list.
   * @apilevel high-level
   */
  public int getNumVariableDecl() {
    return getVariableDeclList().getNumChild();
  }
  /**
   * Retrieves the number of children in the VariableDecl list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the VariableDecl list.
   * @apilevel low-level
   */
  public int getNumVariableDeclNoTransform() {
    return getVariableDeclListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the VariableDecl list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the VariableDecl list.
   * @apilevel high-level
   */
  public VariableDecl getVariableDecl(int i) {
    return (VariableDecl) getVariableDeclList().getChild(i);
  }
  /**
   * Check whether the VariableDecl list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasVariableDecl() {
    return getVariableDeclList().getNumChild() != 0;
  }
  /**
   * Append an element to the VariableDecl list.
   * @param node The element to append to the VariableDecl list.
   * @apilevel high-level
   */
  public void addVariableDecl(VariableDecl node) {
    List<VariableDecl> list = (parent == null || state == null) ? getVariableDeclListNoTransform() : getVariableDeclList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addVariableDeclNoTransform(VariableDecl node) {
    List<VariableDecl> list = getVariableDeclListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the VariableDecl list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setVariableDecl(VariableDecl node, int i) {
    List<VariableDecl> list = getVariableDeclList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the VariableDecl list.
   * @return The node representing the VariableDecl list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="VariableDecl")
  public List<VariableDecl> getVariableDeclList() {
    List<VariableDecl> list = (List<VariableDecl>) getChild(2);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the VariableDecl list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the VariableDecl list.
   * @apilevel low-level
   */
  public List<VariableDecl> getVariableDeclListNoTransform() {
    return (List<VariableDecl>) getChildNoTransform(2);
  }
  /**
   * Retrieves the VariableDecl list.
   * @return The node representing the VariableDecl list.
   * @apilevel high-level
   */
  public List<VariableDecl> getVariableDecls() {
    return getVariableDeclList();
  }
  /**
   * Retrieves the VariableDecl list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the VariableDecl list.
   * @apilevel low-level
   */
  public List<VariableDecl> getVariableDeclsNoTransform() {
    return getVariableDeclListNoTransform();
  }
  /**
   * Replaces the SingleDecl list.
   * @param list The new list node to be used as the SingleDecl list.
   * @apilevel high-level
   */
  public void setSingleDeclList(List<VariableDeclaration> list) {
    setChild(list, 3);
  }
  /**
   * Retrieves the number of children in the SingleDecl list.
   * @return Number of children in the SingleDecl list.
   * @apilevel high-level
   */
  public int getNumSingleDecl() {
    return getSingleDeclList().getNumChild();
  }
  /**
   * Retrieves the number of children in the SingleDecl list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the SingleDecl list.
   * @apilevel low-level
   */
  public int getNumSingleDeclNoTransform() {
    return getSingleDeclListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the SingleDecl list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the SingleDecl list.
   * @apilevel high-level
   */
  public VariableDeclaration getSingleDecl(int i) {
    return (VariableDeclaration) getSingleDeclList().getChild(i);
  }
  /**
   * Check whether the SingleDecl list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasSingleDecl() {
    return getSingleDeclList().getNumChild() != 0;
  }
  /**
   * Append an element to the SingleDecl list.
   * @param node The element to append to the SingleDecl list.
   * @apilevel high-level
   */
  public void addSingleDecl(VariableDeclaration node) {
    List<VariableDeclaration> list = (parent == null || state == null) ? getSingleDeclListNoTransform() : getSingleDeclList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addSingleDeclNoTransform(VariableDeclaration node) {
    List<VariableDeclaration> list = getSingleDeclListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the SingleDecl list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setSingleDecl(VariableDeclaration node, int i) {
    List<VariableDeclaration> list = getSingleDeclList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the child position of the SingleDecl list.
   * @return The the child position of the SingleDecl list.
   * @apilevel low-level
   */
  protected int getSingleDeclListChildPosition() {
    return 3;
  }
  /**
   * Retrieves the SingleDecl list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the SingleDecl list.
   * @apilevel low-level
   */
  public List<VariableDeclaration> getSingleDeclListNoTransform() {
    return (List<VariableDeclaration>) getChildNoTransform(3);
  }
  /**
   * Retrieves the SingleDecl list.
   * @return The node representing the SingleDecl list.
   * @apilevel high-level
   */
  public List<VariableDeclaration> getSingleDecls() {
    return getSingleDeclList();
  }
  /**
   * Retrieves the SingleDecl list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the SingleDecl list.
   * @apilevel low-level
   */
  public List<VariableDeclaration> getSingleDeclsNoTransform() {
    return getSingleDeclListNoTransform();
  }
  protected java.util.Map isDAafter_Variable_values;
/**
 * @apilevel internal
 */
private void isDAafter_Variable_reset() {
  isDAafter_Variable_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isDAafter(Variable v) {
    Object _parameters = v;
    if (isDAafter_Variable_values == null) isDAafter_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(isDAafter_Variable_values.containsKey(_parameters)) {
      return ((Boolean)isDAafter_Variable_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean isDAafter_Variable_value = getSingleDecl(getNumSingleDecl()-1).isDAafter(v);
    if (isFinal && num == state().boundariesCrossed) {
      isDAafter_Variable_values.put(_parameters, Boolean.valueOf(isDAafter_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDAafter_Variable_value;
  }
  protected java.util.Map isDUafter_Variable_values;
/**
 * @apilevel internal
 */
private void isDUafter_Variable_reset() {
  isDUafter_Variable_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isDUafter(Variable v) {
    Object _parameters = v;
    if (isDUafter_Variable_values == null) isDUafter_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(isDUafter_Variable_values.containsKey(_parameters)) {
      return ((Boolean)isDUafter_Variable_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean isDUafter_Variable_value = getSingleDecl(getNumSingleDecl()-1).isDUafter(v);
    if (isFinal && num == state().boundariesCrossed) {
      isDUafter_Variable_values.put(_parameters, Boolean.valueOf(isDUafter_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDUafter_Variable_value;
  }
  /**
   * @attribute syn
   * @aspect VariableScope
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupVariable.jrag:150
   */
  @ASTNodeAnnotation.Attribute
  public VariableDeclaration variableDeclaration(String name) {
    ASTNode$State state = state();
    try {
        for (VariableDeclaration decl: getSingleDeclList()) {
          if (decl.declaresVariable(name)) {
            return decl;
          }
        }
        return null;
      }
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public TypeDecl type() {
    ASTNode$State state = state();
    TypeDecl type_value = getTypeAccess().type();

    return type_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean canCompleteNormally_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean canCompleteNormally_value;
/**
 * @apilevel internal
 */
private void canCompleteNormally_reset() {
  canCompleteNormally_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean canCompleteNormally() {
    if(canCompleteNormally_computed) {
      return canCompleteNormally_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    canCompleteNormally_value = reachable();
    if (isFinal && num == state().boundariesCrossed) {
      canCompleteNormally_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return canCompleteNormally_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean getSingleDeclList_computed = false;
  /**
   * @apilevel internal
   */
  protected List<VariableDeclaration> getSingleDeclList_value;
/**
 * @apilevel internal
 */
private void getSingleDeclList_reset() {
  getSingleDeclList_computed = false;
  getSingleDeclList_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public List<VariableDeclaration> getSingleDeclList() {
    if(getSingleDeclList_computed) {
      return (List<VariableDeclaration>) getChild(getSingleDeclListChildPosition());
    }
    if(getSingleDeclList_computed) {
      return getSingleDeclList_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    getSingleDeclList_value = getSingleDeclList_compute();
    setSingleDeclList(getSingleDeclList_value);
    if (isFinal && num == state().boundariesCrossed) {
      getSingleDeclList_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    List<VariableDeclaration> node = (List<VariableDeclaration>) this.getChild(getSingleDeclListChildPosition());
    return node;
  }
  /**
   * @apilevel internal
   */
  private List<VariableDeclaration> getSingleDeclList_compute() {
      List<VariableDeclaration> decls = new List<VariableDeclaration>();
      for (int i = 0; i < getNumVariableDecl(); ++i) {
        VariableDecl varDecl = getVariableDecl(i);
        VariableDeclaration decl = varDecl.createVariableDeclarationFrom(getModifiers(), getTypeAccess());
        decls.add(decl);
      }
      return decls;
    }
  /**
   * @attribute syn
   * @aspect PreciseRethrow
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/PreciseRethrow.jrag:55
   */
  @ASTNodeAnnotation.Attribute
  public boolean modifiedInScope(Variable var) {
    ASTNode$State state = state();
    try {
        for (VariableDeclaration decl: getSingleDeclList()) {
          if (decl.modifiedInScope(var)) {
            return true;
          }
        }
        return false;
      }
    finally {
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:53
   * @apilevel internal
   */
  public boolean Define_boolean_isIncOrDec(ASTNode caller, ASTNode child) {
    if (caller == getSingleDeclListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else {
      return getParent().Define_boolean_isIncOrDec(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:461
   * @apilevel internal
   */
  public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
    if (caller == getSingleDeclListNoTransform()) {
      int index = caller.getIndexOfChild(child);
      return index == 0 ? isDAbefore(v) : getSingleDecl(index - 1).isDAafter(v);
    }
    else {
      return getParent().Define_boolean_isDAbefore(this, caller, v);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:926
   * @apilevel internal
   */
  public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
    if (caller == getSingleDeclListNoTransform()) {
      int index = caller.getIndexOfChild(child);
      return index == 0 ? isDUbefore(v) : getSingleDecl(index - 1).isDUafter(v);
    }
    else {
      return getParent().Define_boolean_isDUbefore(this, caller, v);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupVariable.jrag:67
   * @apilevel internal
   */
  public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
    if (caller == getSingleDeclListNoTransform()) {
      int index = caller.getIndexOfChild(child);
      {
    for (int i = index; i >= 0; --i) {
      if (getSingleDecl(i).declaresVariable(name)) {
        return getSingleDecl(i);
      }
    }
    return lookupVariable(name);
  }
    }
    else {
      return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/SyntacticClassification.jrag:84
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
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:260
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_declType(ASTNode caller, ASTNode child) {
    if (caller == getVariableDeclListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return null;
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
