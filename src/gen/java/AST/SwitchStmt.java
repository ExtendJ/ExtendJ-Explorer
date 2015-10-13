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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:200
 * @production SwitchStmt : {@link BranchTargetStmt} ::= <span class="component">{@link Expr}</span> <span class="component">{@link Block}</span>;

 */
public class SwitchStmt extends BranchTargetStmt implements Cloneable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:598
   */
  public void prettyPrint(StringBuffer sb) {
    sb.append(indent());
    sb.append("switch (");
    getExpr().prettyPrint(sb);
    sb.append(")");
    getBlock().prettyPrint(sb);
  }
  /**
   * @declaredat ASTNode:1
   */
  public SwitchStmt() {
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
  public SwitchStmt(Expr p0, Block p1) {
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
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:32
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    isDAafter_Variable_reset();
    isDUafter_Variable_reset();
    canCompleteNormally_reset();
    typeInt_reset();
    typeLong_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:43
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:49
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:55
   */
  public SwitchStmt clone() throws CloneNotSupportedException {
    SwitchStmt node = (SwitchStmt) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:62
   */
  public SwitchStmt copy() {
    try {
      SwitchStmt node = (SwitchStmt) clone();
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
   * @declaredat ASTNode:81
   */
  public SwitchStmt fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:90
   */
  public SwitchStmt treeCopyNoTransform() {
    SwitchStmt tree = (SwitchStmt) copy();
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
   * @declaredat ASTNode:110
   */
  public SwitchStmt treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:117
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) ;    
  }
  /**
   * Replaces the Expr child.
   * @param node The new node to replace the Expr child.
   * @apilevel high-level
   */
  public void setExpr(Expr node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the Expr child.
   * @return The current node used as the Expr child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Expr")
  public Expr getExpr() {
    return (Expr) getChild(0);
  }
  /**
   * Retrieves the Expr child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Expr child.
   * @apilevel low-level
   */
  public Expr getExprNoTransform() {
    return (Expr) getChildNoTransform(0);
  }
  /**
   * Replaces the Block child.
   * @param node The new node to replace the Block child.
   * @apilevel high-level
   */
  public void setBlock(Block node) {
    setChild(node, 1);
  }
  /**
   * Retrieves the Block child.
   * @return The current node used as the Block child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Block")
  public Block getBlock() {
    return (Block) getChild(1);
  }
  /**
   * Retrieves the Block child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Block child.
   * @apilevel low-level
   */
  public Block getBlockNoTransform() {
    return (Block) getChildNoTransform(1);
  }
  /**
   * @aspect Enums
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Enums.jrag:494
   */
    public void refined_Enums_SwitchStmt_typeCheck() {
     TypeDecl type = getExpr().type();
    if((!type.isIntegralType() || type.isLong()) && !type.isEnumDecl())
      error("Switch expression must be of char, byte, short, int, or enum type");
  }
  /**
   * <p>Overrides the type checking of the switch statement's expression.
   * 
   * <p>In JSR 334 a switch statement may use an expression of type String.
   * @aspect StringsInSwitch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/StringsInSwitch.jrag:25
   */
    public void typeCheck() {
    TypeDecl type = getExpr().type();
    if ((!type.isIntegralType() || type.isLong()) && !type.isEnumDecl()
        && !type.isString())
      error("Switch expression must be of type " +
          "char, byte, short, int, enum, or string");
  }
  @ASTNodeAnnotation.Attribute
  public boolean potentialTargetOf(Stmt branch) {
    ASTNode$State state = state();
    boolean potentialTargetOf_Stmt_value = branch.canBranchTo(this);

    return potentialTargetOf_Stmt_value;
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
    boolean isDAafter_Variable_value = isDAafter_compute(v);
    if (isFinal && num == state().boundariesCrossed) {
      isDAafter_Variable_values.put(_parameters, Boolean.valueOf(isDAafter_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDAafter_Variable_value;
  }
  /**
   * @apilevel internal
   */
  private boolean isDAafter_compute(Variable v) {
      if(!(!noDefaultLabel() || getExpr().isDAafter(v))) {
        return false;
      }
      if(!(!switchLabelEndsBlock() || getExpr().isDAafter(v))) {
        return false;
      }
      if(!assignedAfterLastStmt(v)) {
        return false;
      }
      for(Iterator iter = targetBreaks().iterator(); iter.hasNext(); ) {
        BreakStmt stmt = (BreakStmt)iter.next();
        if(!stmt.isDAafterReachedFinallyBlocks(v))
          return false;
      }
      return true;
    }
  @ASTNodeAnnotation.Attribute
  public boolean assignedAfterLastStmt(Variable v) {
    ASTNode$State state = state();
    boolean assignedAfterLastStmt_Variable_value = getBlock().isDAafter(v);

    return assignedAfterLastStmt_Variable_value;
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
    boolean isDUafter_Variable_value = isDUafter_compute(v);
    if (isFinal && num == state().boundariesCrossed) {
      isDUafter_Variable_values.put(_parameters, Boolean.valueOf(isDUafter_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDUafter_Variable_value;
  }
  /**
   * @apilevel internal
   */
  private boolean isDUafter_compute(Variable v) {
      if(!(!noDefaultLabel() || getExpr().isDUafter(v)))
        return false;
      if(!(!switchLabelEndsBlock() || getExpr().isDUafter(v)))
        return false;
      if(!unassignedAfterLastStmt(v))
        return false;
      for(Iterator iter = targetBreaks().iterator(); iter.hasNext(); ) {
        BreakStmt stmt = (BreakStmt)iter.next();
        if(!stmt.isDUafterReachedFinallyBlocks(v))
          return false;
      }
      return true;
    }
  @ASTNodeAnnotation.Attribute
  public boolean unassignedAfterLastStmt(Variable v) {
    ASTNode$State state = state();
    boolean unassignedAfterLastStmt_Variable_value = getBlock().isDUafter(v);

    return unassignedAfterLastStmt_Variable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean switchLabelEndsBlock() {
    ASTNode$State state = state();
    boolean switchLabelEndsBlock_value = getBlock().getNumStmt() > 0 && getBlock().getStmt(getBlock().getNumStmt()-1) instanceof ConstCase;

    return switchLabelEndsBlock_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean lastStmtCanCompleteNormally() {
    ASTNode$State state = state();
    boolean lastStmtCanCompleteNormally_value = getBlock().canCompleteNormally();

    return lastStmtCanCompleteNormally_value;
  }
  /**
   * @attribute syn
   * @aspect UnreachableStatements
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/UnreachableStatements.jrag:65
   */
  @ASTNodeAnnotation.Attribute
  public boolean noStmts() {
    ASTNode$State state = state();
    try {
        for(int i = 0; i < getBlock().getNumStmt(); i++)
          if(!(getBlock().getStmt(i) instanceof Case))
            return false;
        return true;
      }
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public boolean noStmtsAfterLastLabel() {
    ASTNode$State state = state();
    boolean noStmtsAfterLastLabel_value = getBlock().getNumStmt() > 0 && getBlock().getStmt(getBlock().getNumStmt()-1) instanceof Case;

    return noStmtsAfterLastLabel_value;
  }
  /**
   * @attribute syn
   * @aspect UnreachableStatements
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/UnreachableStatements.jrag:75
   */
  @ASTNodeAnnotation.Attribute
  public boolean noDefaultLabel() {
    ASTNode$State state = state();
    try {
        for(int i = 0; i < getBlock().getNumStmt(); i++)
          if(getBlock().getStmt(i) instanceof DefaultCase)
            return false;
        return true;
      }
    finally {
    }
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
    canCompleteNormally_value = lastStmtCanCompleteNormally() || noStmts() || noStmtsAfterLastLabel() || noDefaultLabel() || reachableBreak();
    if (isFinal && num == state().boundariesCrossed) {
      canCompleteNormally_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return canCompleteNormally_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean modifiedInScope(Variable var) {
    ASTNode$State state = state();
    boolean modifiedInScope_Variable_value = getBlock().modifiedInScope(var);

    return modifiedInScope_Variable_value;
  }
  /**
   * @attribute inh
   * @aspect SpecialClasses
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:61
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeInt() {
    if(typeInt_computed) {
      return typeInt_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeInt_value = getParent().Define_TypeDecl_typeInt(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      typeInt_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeInt_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeInt_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeInt_value;
/**
 * @apilevel internal
 */
private void typeInt_reset() {
  typeInt_computed = false;
  typeInt_value = null;
}  
  /**
   * @attribute inh
   * @aspect SpecialClasses
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:63
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeLong() {
    if(typeLong_computed) {
      return typeLong_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeLong_value = getParent().Define_TypeDecl_typeLong(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      typeLong_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeLong_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeLong_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeLong_value;
/**
 * @apilevel internal
 */
private void typeLong_reset() {
  typeLong_computed = false;
  typeLong_value = null;
}  
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BranchTarget.jrag:225
   * @apilevel internal
   */
  public Stmt Define_Stmt_branchTarget(ASTNode caller, ASTNode child, Stmt branch) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return branch.canBranchTo(this) ? this : branchTarget(branch);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:590
   * @apilevel internal
   */
  public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
    if (caller == getBlockNoTransform()) {
      return getExpr().isDAafter(v);
    }
    else if (caller == getExprNoTransform()){
    if(((ASTNode)v).isDescendantTo(this))
      return false;
    boolean result = isDAbefore(v);
    return result;
  }
    else {
      return getParent().Define_boolean_isDAbefore(this, caller, v);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:1097
   * @apilevel internal
   */
  public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
    if (caller == getBlockNoTransform()) {
      return getExpr().isDUafter(v);
    }
    else if (caller == getExprNoTransform()) {
      return isDUbefore(v);
    }
    else {
      return getParent().Define_boolean_isDUbefore(this, caller, v);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:404
   * @apilevel internal
   */
  public boolean Define_boolean_insideSwitch(ASTNode caller, ASTNode child) {
    if (caller == getBlockNoTransform()) {
      return true;
    }
    else {
      return getParent().Define_boolean_insideSwitch(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:447
   * @apilevel internal
   */
  public Case Define_Case_bind(ASTNode caller, ASTNode child, Case c) {
    if (caller == getBlockNoTransform()){
    Block b = getBlock();
    for(int i = 0; i < b.getNumStmt(); i++)
      if(b.getStmt(i) instanceof Case && ((Case)b.getStmt(i)).constValue(c))
        return (Case)b.getStmt(i);
    return null;
  }
    else {
      return getParent().Define_Case_bind(this, caller, c);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeCheck.jrag:362
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_switchType(ASTNode caller, ASTNode child) {
    if (caller == getBlockNoTransform()) {
      return getExpr().type();
    }
    else {
      return getParent().Define_TypeDecl_switchType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/UnreachableStatements.jrag:85
   * @apilevel internal
   */
  public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
    if (caller == getBlockNoTransform()) {
      return reachable();
    }
    else {
      return getParent().Define_boolean_reachable(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/UnreachableStatements.jrag:161
   * @apilevel internal
   */
  public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
    if (caller == getBlockNoTransform()) {
      return reachable();
    }
    else {
      return getParent().Define_boolean_reportUnreachable(this, caller);
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
