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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:195
 * @production Block : {@link Stmt} ::= <span class="component">{@link Stmt}*</span>;

 */
public class Block extends Stmt implements Cloneable, VariableScope {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:570
   */
  public void prettyPrint(StringBuffer sb) {
    String indent = indent();
    sb.append(shouldHaveIndent() ? indent : "");
    sb.append("{");
    for(int i = 0; i < getNumStmt(); i++) {
      getStmt(i).prettyPrint(sb);
    }
    sb.append(shouldHaveIndent() ? indent : indent.substring(0, indent.length()-2));
    sb.append("}");
  }
  /**
   * @declaredat ASTNode:1
   */
  public Block() {
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
  public Block(List<Stmt> p0) {
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
    checkReturnDA_Variable_reset();
    isDAafter_Variable_reset();
    checkReturnDU_Variable_reset();
    isDUafter_Variable_reset();
    localVariableDeclaration_String_reset();
    canCompleteNormally_reset();
    lookupType_String_reset();
    lookupVariable_String_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:46
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:52
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:58
   */
  public Block clone() throws CloneNotSupportedException {
    Block node = (Block) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:65
   */
  public Block copy() {
    try {
      Block node = (Block) clone();
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
   * @declaredat ASTNode:84
   */
  public Block fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:93
   */
  public Block treeCopyNoTransform() {
    Block tree = (Block) copy();
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
   * @declaredat ASTNode:113
   */
  public Block treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:120
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) ;    
  }
  /**
   * Replaces the Stmt list.
   * @param list The new list node to be used as the Stmt list.
   * @apilevel high-level
   */
  public void setStmtList(List<Stmt> list) {
    setChild(list, 0);
  }
  /**
   * Retrieves the number of children in the Stmt list.
   * @return Number of children in the Stmt list.
   * @apilevel high-level
   */
  public int getNumStmt() {
    return getStmtList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Stmt list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Stmt list.
   * @apilevel low-level
   */
  public int getNumStmtNoTransform() {
    return getStmtListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Stmt list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Stmt list.
   * @apilevel high-level
   */
  public Stmt getStmt(int i) {
    return (Stmt) getStmtList().getChild(i);
  }
  /**
   * Check whether the Stmt list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasStmt() {
    return getStmtList().getNumChild() != 0;
  }
  /**
   * Append an element to the Stmt list.
   * @param node The element to append to the Stmt list.
   * @apilevel high-level
   */
  public void addStmt(Stmt node) {
    List<Stmt> list = (parent == null || state == null) ? getStmtListNoTransform() : getStmtList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addStmtNoTransform(Stmt node) {
    List<Stmt> list = getStmtListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Stmt list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setStmt(Stmt node, int i) {
    List<Stmt> list = getStmtList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Stmt list.
   * @return The node representing the Stmt list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Stmt")
  public List<Stmt> getStmtList() {
    List<Stmt> list = (List<Stmt>) getChild(0);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the Stmt list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Stmt list.
   * @apilevel low-level
   */
  public List<Stmt> getStmtListNoTransform() {
    return (List<Stmt>) getChildNoTransform(0);
  }
  /**
   * Retrieves the Stmt list.
   * @return The node representing the Stmt list.
   * @apilevel high-level
   */
  public List<Stmt> getStmts() {
    return getStmtList();
  }
  /**
   * Retrieves the Stmt list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Stmt list.
   * @apilevel low-level
   */
  public List<Stmt> getStmtsNoTransform() {
    return getStmtListNoTransform();
  }
  @ASTNodeAnnotation.Attribute
  public boolean addsIndentationLevel() {
    ASTNode$State state = state();
    boolean addsIndentationLevel_value = shouldHaveIndent();

    return addsIndentationLevel_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean shouldHaveIndent() {
    ASTNode$State state = state();
    boolean shouldHaveIndent_value = getParent() instanceof List && getParent().getParent() instanceof Block;

    return shouldHaveIndent_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean declaredBeforeUse(VariableDeclaration decl, int indexUse) {
    ASTNode$State state = state();
    boolean declaredBeforeUse_VariableDeclaration_int_value = decl.blockIndex() < indexUse;

    return declaredBeforeUse_VariableDeclaration_int_value;
  }
  protected java.util.Map checkReturnDA_Variable_values;
/**
 * @apilevel internal
 */
private void checkReturnDA_Variable_reset() {
  checkReturnDA_Variable_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean checkReturnDA(Variable v) {
    Object _parameters = v;
    if (checkReturnDA_Variable_values == null) checkReturnDA_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(checkReturnDA_Variable_values.containsKey(_parameters)) {
      return ((Boolean)checkReturnDA_Variable_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean checkReturnDA_Variable_value = checkReturnDA_compute(v);
    if (isFinal && num == state().boundariesCrossed) {
      checkReturnDA_Variable_values.put(_parameters, Boolean.valueOf(checkReturnDA_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return checkReturnDA_Variable_value;
  }
  /**
   * @apilevel internal
   */
  private boolean checkReturnDA_compute(Variable v) {
      HashSet set = new HashSet();
      collectBranches(set);
      for(Iterator iter = set.iterator(); iter.hasNext(); ) {
        Object o = iter.next();
        if(o instanceof ReturnStmt) {
          ReturnStmt stmt = (ReturnStmt)o;
          if(!stmt.isDAafterReachedFinallyBlocks(v))
            return false;
        }
      }
      return true;
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
    boolean isDAafter_Variable_value = getNumStmt() == 0 ? isDAbefore(v) : getStmt(getNumStmt()-1).isDAafter(v);
    if (isFinal && num == state().boundariesCrossed) {
      isDAafter_Variable_values.put(_parameters, Boolean.valueOf(isDAafter_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDAafter_Variable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isDUeverywhere(Variable v) {
    ASTNode$State state = state();
    boolean isDUeverywhere_Variable_value = isDUbefore(v) && checkDUeverywhere(v);

    return isDUeverywhere_Variable_value;
  }
  protected java.util.Map checkReturnDU_Variable_values;
/**
 * @apilevel internal
 */
private void checkReturnDU_Variable_reset() {
  checkReturnDU_Variable_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean checkReturnDU(Variable v) {
    Object _parameters = v;
    if (checkReturnDU_Variable_values == null) checkReturnDU_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(checkReturnDU_Variable_values.containsKey(_parameters)) {
      return ((Boolean)checkReturnDU_Variable_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean checkReturnDU_Variable_value = checkReturnDU_compute(v);
    if (isFinal && num == state().boundariesCrossed) {
      checkReturnDU_Variable_values.put(_parameters, Boolean.valueOf(checkReturnDU_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return checkReturnDU_Variable_value;
  }
  /**
   * @apilevel internal
   */
  private boolean checkReturnDU_compute(Variable v) {
      HashSet set = new HashSet();
      collectBranches(set);
      for(Iterator iter = set.iterator(); iter.hasNext(); ) {
        Object o = iter.next();
        if(o instanceof ReturnStmt) {
          ReturnStmt stmt = (ReturnStmt)o;
          if(!stmt.isDUafterReachedFinallyBlocks(v))
            return false;
        }
      }
      return true;
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
    boolean isDUafter_Variable_value = getNumStmt() == 0 ? isDUbefore(v) : getStmt(getNumStmt()-1).isDUafter(v);
    if (isFinal && num == state().boundariesCrossed) {
      isDUafter_Variable_values.put(_parameters, Boolean.valueOf(isDUafter_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDUafter_Variable_value;
  }
  protected java.util.Map localVariableDeclaration_String_values;
/**
 * @apilevel internal
 */
private void localVariableDeclaration_String_reset() {
  localVariableDeclaration_String_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public VariableDeclaration localVariableDeclaration(String name) {
    Object _parameters = name;
    if (localVariableDeclaration_String_values == null) localVariableDeclaration_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(localVariableDeclaration_String_values.containsKey(_parameters)) {
      return (VariableDeclaration)localVariableDeclaration_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    VariableDeclaration localVariableDeclaration_String_value = localVariableDeclaration_compute(name);
    if (isFinal && num == state().boundariesCrossed) {
      localVariableDeclaration_String_values.put(_parameters, localVariableDeclaration_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return localVariableDeclaration_String_value;
  }
  /**
   * @apilevel internal
   */
  private VariableDeclaration localVariableDeclaration_compute(String name) {
      for (Stmt stmt: getStmtList()) {
        VariableDeclaration decl = stmt.variableDeclaration(name);
        if (decl != null) {
          return decl;
        }
      }
      return null;
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
    canCompleteNormally_value = getNumStmt() == 0 ? reachable() : getStmt(getNumStmt() - 1).canCompleteNormally();
    if (isFinal && num == state().boundariesCrossed) {
      canCompleteNormally_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return canCompleteNormally_value;
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
        for (Stmt stmt : getStmtList())
          if (stmt.modifiedInScope(var))
            return true;
        return false;
      }
    finally {
    }
  }
  /**
   * @attribute inh
   * @aspect TypeScopePropagation
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:229
   */
  @ASTNodeAnnotation.Attribute
  public SimpleSet lookupType(String name) {
    Object _parameters = name;
    if (lookupType_String_values == null) lookupType_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(lookupType_String_values.containsKey(_parameters)) {
      return (SimpleSet)lookupType_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    SimpleSet lookupType_String_value = getParent().Define_SimpleSet_lookupType(this, null, name);
    if (isFinal && num == state().boundariesCrossed) {
      lookupType_String_values.put(_parameters, lookupType_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return lookupType_String_value;
  }
  protected java.util.Map lookupType_String_values;
/**
 * @apilevel internal
 */
private void lookupType_String_reset() {
  lookupType_String_values = null;
}  
  /**
   * @attribute inh
   * @aspect VariableScope
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupVariable.jrag:17
   */
  @ASTNodeAnnotation.Attribute
  public SimpleSet lookupVariable(String name) {
    Object _parameters = name;
    if (lookupVariable_String_values == null) lookupVariable_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(lookupVariable_String_values.containsKey(_parameters)) {
      return (SimpleSet)lookupVariable_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    SimpleSet lookupVariable_String_value = getParent().Define_SimpleSet_lookupVariable(this, null, name);
    if (isFinal && num == state().boundariesCrossed) {
      lookupVariable_String_values.put(_parameters, lookupVariable_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return lookupVariable_String_value;
  }
  protected java.util.Map lookupVariable_String_values;
/**
 * @apilevel internal
 */
private void lookupVariable_String_reset() {
  lookupVariable_String_values = null;
}  
  /**
   * @attribute inh
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:471
   */
  @ASTNodeAnnotation.Attribute
  public SimpleSet otherLocalClassDecls(String name) {
    ASTNode$State state = state();
    SimpleSet otherLocalClassDecls_String_value = getParent().Define_SimpleSet_otherLocalClassDecls(this, null, name);

    return otherLocalClassDecls_String_value;
  }
  /**
   * @attribute inh
   * @aspect UnreachableStatements
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/UnreachableStatements.jrag:28
   */
  @ASTNodeAnnotation.Attribute
  public boolean reachable() {
    ASTNode$State state = state();
    boolean reachable_value = getParent().Define_boolean_reachable(this, null);

    return reachable_value;
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DeclareBeforeUse.jrag:15
   * @apilevel internal
   */
  public int Define_int_blockIndex(ASTNode caller, ASTNode child) {
    if (caller == getStmtListNoTransform()) {
      int index = caller.getIndexOfChild(child);
      return index;
    }
    else {
      return getParent().Define_int_blockIndex(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:52
   * @apilevel internal
   */
  public boolean Define_boolean_isIncOrDec(ASTNode caller, ASTNode child) {
    if (caller == getStmtListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else {
      return getParent().Define_boolean_isIncOrDec(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:459
   * @apilevel internal
   */
  public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
    if (caller == getStmtListNoTransform()) {
      int index = caller.getIndexOfChild(child);
      return index == 0 ? isDAbefore(v) : getStmt(index - 1).isDAafter(v);
    }
    else {
      return getParent().Define_boolean_isDAbefore(this, caller, v);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:924
   * @apilevel internal
   */
  public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
    if (caller == getStmtListNoTransform()) {
      int index = caller.getIndexOfChild(child);
      return index == 0 ? isDUbefore(v) : getStmt(index - 1).isDUafter(v);
    }
    else {
      return getParent().Define_boolean_isDUbefore(this, caller, v);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:352
   * @apilevel internal
   */
  public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
    if (caller == getStmtListNoTransform()) {
      int index = caller.getIndexOfChild(child);
      {
    SimpleSet c = SimpleSet.emptySet;
    for(int i = index; i >= 0 && !(getStmt(i) instanceof Case); i--) {
      if(getStmt(i) instanceof LocalClassDeclStmt) {
        TypeDecl t = ((LocalClassDeclStmt)getStmt(i)).getClassDecl();
        if(t.name().equals(name)) {
          c = c.add(t);
        }
      }
    }
    if(!c.isEmpty())
      return c;
    return lookupType(name);
  }
    }
    else {
      return getParent().Define_SimpleSet_lookupType(this, caller, name);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupVariable.jrag:80
   * @apilevel internal
   */
  public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
    if (caller == getStmtListNoTransform()) {
      int index = caller.getIndexOfChild(child);
      {
    VariableDeclaration v = localVariableDeclaration(name);
    // declare before use and shadowing
    if(v != null && declaredBeforeUse(v, index))
      return v;
    return lookupVariable(name);
  }
    }
    else {
      return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:322
   * @apilevel internal
   */
  public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
    if (caller == getStmtListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return this;
    }
    else {
      return getParent().Define_VariableScope_outerScope(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:475
   * @apilevel internal
   */
  public SimpleSet Define_SimpleSet_otherLocalClassDecls(ASTNode caller, ASTNode child, String name) {
    if (caller == getStmtListNoTransform()) {
      int index = caller.getIndexOfChild(child);
      {
    SimpleSet local = SimpleSet.emptySet;
    for (int i = index-1; i >= 0 && !(getStmt(i) instanceof Case); --i) {
      if (getStmt(i) instanceof LocalClassDeclStmt) {
        TypeDecl t = ((LocalClassDeclStmt)getStmt(i)).getClassDecl();
        if (t.name().equals(name)) {
          local = local.add(t);
        }
      }
    }
    if (!local.isEmpty())
      return local;
    else
      return otherLocalClassDecls(name);
  }
    }
    else {
      return getParent().Define_SimpleSet_otherLocalClassDecls(this, caller, name);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/SyntacticClassification.jrag:116
   * @apilevel internal
   */
  public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
    if (caller == getStmtListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return NameType.EXPRESSION_NAME;
    }
    else {
      return getParent().Define_NameType_nameType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/UnreachableStatements.jrag:40
   * @apilevel internal
   */
  public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
    if (caller == getStmtListNoTransform()) {
      int index = caller.getIndexOfChild(child);
      return index == 0 ? reachable() : getStmt(index-1).canCompleteNormally();
    }
    else {
      return getParent().Define_boolean_reachable(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/UnreachableStatements.jrag:151
   * @apilevel internal
   */
  public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
    if (caller == getStmtListNoTransform()) {
      int i = caller.getIndexOfChild(child);
      return i == 0 ? reachable() : getStmt(i-1).reachable();
    }
    else {
      return getParent().Define_boolean_reportUnreachable(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/EffectivelyFinal.jrag:52
   * @apilevel internal
   */
  public boolean Define_boolean_inhModifiedInScope(ASTNode caller, ASTNode child, Variable var) {
    if (caller == getStmtListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return modifiedInScope(var);
    }
    else {
      return getParent().Define_boolean_inhModifiedInScope(this, caller, var);
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
