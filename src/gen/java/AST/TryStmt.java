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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:223
 * @production TryStmt : {@link Stmt} ::= <span class="component">{@link Block}</span> <span class="component">{@link CatchClause}*</span> <span class="component">[Finally:{@link Block}]</span> <span class="component">ExceptionHandler:{@link Block}</span>;

 */
public class TryStmt extends Stmt implements Cloneable, FinallyHost {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:755
   */
  public void prettyPrint(StringBuffer sb) {
    sb.append(indent());
    sb.append("try ");
    getBlock().prettyPrint(sb);
    for(int i = 0; i < getNumCatchClause(); i++) {
      sb.append(indent());
      getCatchClause(i).prettyPrint(sb);
    }
    if(hasFinally()) {
      sb.append(indent());
      sb.append("finally ");
      getFinally().prettyPrint(sb);
    }
  }
  /**
   * @aspect BranchTarget
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BranchTarget.jrag:91
   */
  public void collectBranches(Collection<Stmt> c) {
    c.addAll(escapedBranches());
  }
  /**
   * @aspect DU
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:974
   */
  public Block getFinallyBlock() {
    return getFinally();
  }
  /**
   * @aspect ExceptionHandling
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ExceptionHandling.jrag:232
   */
  protected boolean reachedException(TypeDecl type) {
    boolean found = false;
    // found is true if the exception type is caught by a catch clause
    for(int i = 0; i < getNumCatchClause() && !found; i++)
      if(getCatchClause(i).handles(type))
        found = true;
    // if an exception is thrown in the block and the exception is not caught and
    // either there is no finally block or the finally block can complete normally
    if(!found && (!hasNonEmptyFinally() || getFinally().canCompleteNormally()) )
      if(getBlock().reachedException(type))
        return true;
    // even if the exception is caught by the catch clauses they may
    // throw new exceptions
    for(int i = 0; i < getNumCatchClause(); i++)
      if(getCatchClause(i).reachedException(type))
        return true;
    return hasNonEmptyFinally() && getFinally().reachedException(type);
  }
  /**
   * @declaredat ASTNode:1
   */
  public TryStmt() {
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
    setChild(new List(), 1);
    setChild(new Opt(), 2);
  }
  /**
   * @declaredat ASTNode:15
   */
  public TryStmt(Block p0, List<CatchClause> p1, Opt<Block> p2) {
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
    branches_reset();
    escapedBranches_reset();
    isDAafter_Variable_reset();
    isDUbefore_Variable_reset();
    isDUafter_Variable_reset();
    hasNonEmptyFinally_reset();
    catchableException_TypeDecl_reset();
    getExceptionHandler_reset();
    canCompleteNormally_reset();
    handlesException_TypeDecl_reset();
    typeError_reset();
    typeRuntimeException_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:53
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:59
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:65
   */
  public TryStmt clone() throws CloneNotSupportedException {
    TryStmt node = (TryStmt) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:72
   */
  public TryStmt copy() {
    try {
      TryStmt node = (TryStmt) clone();
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
   * @declaredat ASTNode:91
   */
  public TryStmt fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:100
   */
  public TryStmt treeCopyNoTransform() {
    TryStmt tree = (TryStmt) copy();
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        switch (i) {
        case 3:
          tree.children[i] = null;
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
   * @declaredat ASTNode:125
   */
  public TryStmt treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:132
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) ;    
  }
  /**
   * Replaces the Block child.
   * @param node The new node to replace the Block child.
   * @apilevel high-level
   */
  public void setBlock(Block node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the Block child.
   * @return The current node used as the Block child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Block")
  public Block getBlock() {
    return (Block) getChild(0);
  }
  /**
   * Retrieves the Block child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Block child.
   * @apilevel low-level
   */
  public Block getBlockNoTransform() {
    return (Block) getChildNoTransform(0);
  }
  /**
   * Replaces the CatchClause list.
   * @param list The new list node to be used as the CatchClause list.
   * @apilevel high-level
   */
  public void setCatchClauseList(List<CatchClause> list) {
    setChild(list, 1);
  }
  /**
   * Retrieves the number of children in the CatchClause list.
   * @return Number of children in the CatchClause list.
   * @apilevel high-level
   */
  public int getNumCatchClause() {
    return getCatchClauseList().getNumChild();
  }
  /**
   * Retrieves the number of children in the CatchClause list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the CatchClause list.
   * @apilevel low-level
   */
  public int getNumCatchClauseNoTransform() {
    return getCatchClauseListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the CatchClause list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the CatchClause list.
   * @apilevel high-level
   */
  public CatchClause getCatchClause(int i) {
    return (CatchClause) getCatchClauseList().getChild(i);
  }
  /**
   * Check whether the CatchClause list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasCatchClause() {
    return getCatchClauseList().getNumChild() != 0;
  }
  /**
   * Append an element to the CatchClause list.
   * @param node The element to append to the CatchClause list.
   * @apilevel high-level
   */
  public void addCatchClause(CatchClause node) {
    List<CatchClause> list = (parent == null || state == null) ? getCatchClauseListNoTransform() : getCatchClauseList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addCatchClauseNoTransform(CatchClause node) {
    List<CatchClause> list = getCatchClauseListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the CatchClause list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setCatchClause(CatchClause node, int i) {
    List<CatchClause> list = getCatchClauseList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the CatchClause list.
   * @return The node representing the CatchClause list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="CatchClause")
  public List<CatchClause> getCatchClauseList() {
    List<CatchClause> list = (List<CatchClause>) getChild(1);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the CatchClause list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the CatchClause list.
   * @apilevel low-level
   */
  public List<CatchClause> getCatchClauseListNoTransform() {
    return (List<CatchClause>) getChildNoTransform(1);
  }
  /**
   * Retrieves the CatchClause list.
   * @return The node representing the CatchClause list.
   * @apilevel high-level
   */
  public List<CatchClause> getCatchClauses() {
    return getCatchClauseList();
  }
  /**
   * Retrieves the CatchClause list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the CatchClause list.
   * @apilevel low-level
   */
  public List<CatchClause> getCatchClausesNoTransform() {
    return getCatchClauseListNoTransform();
  }
  /**
   * Replaces the optional node for the Finally child. This is the <code>Opt</code>
   * node containing the child Finally, not the actual child!
   * @param opt The new node to be used as the optional node for the Finally child.
   * @apilevel low-level
   */
  public void setFinallyOpt(Opt<Block> opt) {
    setChild(opt, 2);
  }
  /**
   * Replaces the (optional) Finally child.
   * @param node The new node to be used as the Finally child.
   * @apilevel high-level
   */
  public void setFinally(Block node) {
    getFinallyOpt().setChild(node, 0);
  }
  /**
   * Check whether the optional Finally child exists.
   * @return {@code true} if the optional Finally child exists, {@code false} if it does not.
   * @apilevel high-level
   */
  public boolean hasFinally() {
    return getFinallyOpt().getNumChild() != 0;
  }
  /**
   * Retrieves the (optional) Finally child.
   * @return The Finally child, if it exists. Returns {@code null} otherwise.
   * @apilevel low-level
   */
  public Block getFinally() {
    return (Block) getFinallyOpt().getChild(0);
  }
  /**
   * Retrieves the optional node for the Finally child. This is the <code>Opt</code> node containing the child Finally, not the actual child!
   * @return The optional node for child the Finally child.
   * @apilevel low-level
   */
  @ASTNodeAnnotation.OptChild(name="Finally")
  public Opt<Block> getFinallyOpt() {
    return (Opt<Block>) getChild(2);
  }
  /**
   * Retrieves the optional node for child Finally. This is the <code>Opt</code> node containing the child Finally, not the actual child!
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The optional node for child Finally.
   * @apilevel low-level
   */
  public Opt<Block> getFinallyOptNoTransform() {
    return (Opt<Block>) getChildNoTransform(2);
  }
  /**
   * Replaces the ExceptionHandler child.
   * @param node The new node to replace the ExceptionHandler child.
   * @apilevel high-level
   */
  public void setExceptionHandler(Block node) {
    setChild(node, 3);
  }
  /**
   * Retrieves the ExceptionHandler child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the ExceptionHandler child.
   * @apilevel low-level
   */
  public Block getExceptionHandlerNoTransform() {
    return (Block) getChildNoTransform(3);
  }
  /**
   * Retrieves the child position of the optional child ExceptionHandler.
   * @return The the child position of the optional child ExceptionHandler.
   * @apilevel low-level
   */
  protected int getExceptionHandlerChildPosition() {
    return 3;
  }
  /**
   * @apilevel internal
   */
  protected boolean branches_computed = false;
  /**
   * @apilevel internal
   */
  protected Collection<Stmt> branches_value;
/**
 * @apilevel internal
 */
private void branches_reset() {
  branches_computed = false;
  branches_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Collection<Stmt> branches() {
    if(branches_computed) {
      return branches_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    branches_value = branches_compute();
    if (isFinal && num == state().boundariesCrossed) {
      branches_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return branches_value;
  }
  /**
   * @apilevel internal
   */
  private Collection<Stmt> branches_compute() {
      Collection<Stmt> set = new HashSet<Stmt>();
      getBlock().collectBranches(set);
      for (int i = 0; i < getNumCatchClause(); i++) {
        getCatchClause(i).collectBranches(set);
      }
      return set;
    }
  /**
   * @apilevel internal
   */
  protected boolean escapedBranches_computed = false;
  /**
   * @apilevel internal
   */
  protected Collection<Stmt> escapedBranches_value;
/**
 * @apilevel internal
 */
private void escapedBranches_reset() {
  escapedBranches_computed = false;
  escapedBranches_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Collection<Stmt> escapedBranches() {
    if(escapedBranches_computed) {
      return escapedBranches_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    escapedBranches_value = escapedBranches_compute();
    if (isFinal && num == state().boundariesCrossed) {
      escapedBranches_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return escapedBranches_value;
  }
  /**
   * @apilevel internal
   */
  private Collection<Stmt> escapedBranches_compute() {
      Collection<Stmt> set = new HashSet<Stmt>();
      if (hasNonEmptyFinally()) {
        // branches from finally
        getFinally().collectBranches(set);
      }
      if (!hasFinally() || getFinally().canCompleteNormally()) {
        set.addAll(branches());
      }
      return set;
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
      // 16.2.15 4th bullet
      if(!hasNonEmptyFinally()) {
        if(!getBlock().isDAafter(v))
          return false;
        for(int i = 0; i < getNumCatchClause(); i++)
          if(!getCatchClause(i).getBlock().isDAafter(v))
            return false;
        return true;
      }
      else {
        // 16.2.15 5th bullet
        if(getFinally().isDAafter(v))
          return true;
        if(!getBlock().isDAafter(v))
          return false;
        for(int i = 0; i < getNumCatchClause(); i++)
          if(!getCatchClause(i).getBlock().isDAafter(v))
            return false;
        return true;
      }
    }
  @ASTNodeAnnotation.Attribute
  public boolean isDUafterFinally(Variable v) {
    ASTNode$State state = state();
    boolean isDUafterFinally_Variable_value = getFinally().isDUafter(v);

    return isDUafterFinally_Variable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isDAafterFinally(Variable v) {
    ASTNode$State state = state();
    boolean isDAafterFinally_Variable_value = getFinally().isDAafter(v);

    return isDAafterFinally_Variable_value;
  }
/**
 * @apilevel internal
 */
private void isDUbefore_Variable_reset() {
  isDUbefore_Variable_values = null;
}  
  protected java.util.Map isDUbefore_Variable_values;
  @ASTNodeAnnotation.Attribute
  public boolean isDUbefore(Variable v) {
    Object _parameters = v;
    if (isDUbefore_Variable_values == null) isDUbefore_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    ASTNode$State.CircularValue _value;
    if(isDUbefore_Variable_values.containsKey(_parameters)) {
      Object _o = isDUbefore_Variable_values.get(_parameters);
      if(!(_o instanceof ASTNode$State.CircularValue)) {
        return ((Boolean)_o).booleanValue();
      } else {
        _value = (ASTNode$State.CircularValue) _o;
      }
    } else {
      _value = new ASTNode$State.CircularValue();
      isDUbefore_Variable_values.put(_parameters, _value);
      _value.value = Boolean.valueOf(true);
    }
    ASTNode$State state = state();
    boolean new_isDUbefore_Variable_value;
    if (!state.IN_CIRCLE) {
      state.IN_CIRCLE = true;
      int num = state.boundariesCrossed;
      boolean isFinal = this.is$Final();
      // TODO: fixme
      // state().CIRCLE_INDEX = 1;
      do {
        _value.visited = new Integer(state.CIRCLE_INDEX);
        state.CHANGE = false;
        new_isDUbefore_Variable_value = super.isDUbefore(v);
        if (new_isDUbefore_Variable_value != ((Boolean)_value.value).booleanValue()) {
          state.CHANGE = true;
          _value.value = Boolean.valueOf(new_isDUbefore_Variable_value);
        }
        state.CIRCLE_INDEX++;
      } while (state.CHANGE);
      if (isFinal && num == state().boundariesCrossed) {
        isDUbefore_Variable_values.put(_parameters, new_isDUbefore_Variable_value);
      } else {
        isDUbefore_Variable_values.remove(_parameters);
        state.RESET_CYCLE = true;
        boolean $tmp = super.isDUbefore(v);
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_isDUbefore_Variable_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_isDUbefore_Variable_value = super.isDUbefore(v);
      if (state.RESET_CYCLE) {
        isDUbefore_Variable_values.remove(_parameters);
      }
      else if (new_isDUbefore_Variable_value != ((Boolean)_value.value).booleanValue()) {
        state.CHANGE = true;
        _value.value = new_isDUbefore_Variable_value;
      }
      state.INTERMEDIATE_VALUE = true;
      return new_isDUbefore_Variable_value;
    }
    state.INTERMEDIATE_VALUE = true;
    return ((Boolean)_value.value).booleanValue();
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
      // 16.2.14 4th bullet
      if(!hasNonEmptyFinally()) {
        if(!getBlock().isDUafter(v))
          return false;
        for(int i = 0; i < getNumCatchClause(); i++)
          if(!getCatchClause(i).getBlock().isDUafter(v))
            return false;
        return true;
      }
      else
        return getFinally().isDUafter(v);
    }
  /**
   * @apilevel internal
   */
  protected boolean hasNonEmptyFinally_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean hasNonEmptyFinally_value;
/**
 * @apilevel internal
 */
private void hasNonEmptyFinally_reset() {
  hasNonEmptyFinally_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean hasNonEmptyFinally() {
    if(hasNonEmptyFinally_computed) {
      return hasNonEmptyFinally_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    hasNonEmptyFinally_value = hasFinally() && getFinally().getNumStmt() > 0;
    if (isFinal && num == state().boundariesCrossed) {
      hasNonEmptyFinally_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return hasNonEmptyFinally_value;
  }
  protected java.util.Map catchableException_TypeDecl_values;
/**
 * @apilevel internal
 */
private void catchableException_TypeDecl_reset() {
  catchableException_TypeDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean catchableException(TypeDecl type) {
    Object _parameters = type;
    if (catchableException_TypeDecl_values == null) catchableException_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(catchableException_TypeDecl_values.containsKey(_parameters)) {
      return ((Boolean)catchableException_TypeDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean catchableException_TypeDecl_value = getBlock().reachedException(type);
    if (isFinal && num == state().boundariesCrossed) {
      catchableException_TypeDecl_values.put(_parameters, Boolean.valueOf(catchableException_TypeDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return catchableException_TypeDecl_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean getExceptionHandler_computed = false;
  /**
   * @apilevel internal
   */
  protected Block getExceptionHandler_value;
/**
 * @apilevel internal
 */
private void getExceptionHandler_reset() {
  getExceptionHandler_computed = false;
  getExceptionHandler_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Block getExceptionHandler() {
    if(getExceptionHandler_computed) {
      return (Block) getChild(getExceptionHandlerChildPosition());
    }
    if(getExceptionHandler_computed) {
      return getExceptionHandler_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    getExceptionHandler_value = getExceptionHandler_compute();
      setExceptionHandler(getExceptionHandler_value);
    if (isFinal && num == state().boundariesCrossed) {
      getExceptionHandler_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    Block node = (Block) this.getChild(getExceptionHandlerChildPosition());
    return node;
  }
  /**
   * @apilevel internal
   */
  private Block getExceptionHandler_compute() {
      if (hasNonEmptyFinally()) {
        NTAFinallyBlock ntaBlock = new NTAFinallyBlock(this);
        ntaBlock.addStmt((Block) getFinally().fullCopy());
        return ntaBlock;
      } else {
        return new NTAFinallyBlock();
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
    canCompleteNormally_value = canCompleteNormally_compute();
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
  private boolean canCompleteNormally_compute() {
       boolean anyCatchClauseCompleteNormally = false;
       for(int i = 0; i < getNumCatchClause() && !anyCatchClauseCompleteNormally; i++)
         anyCatchClauseCompleteNormally = getCatchClause(i).getBlock().canCompleteNormally();
       return (getBlock().canCompleteNormally() || anyCatchClauseCompleteNormally) &&
         (!hasNonEmptyFinally() || getFinally().canCompleteNormally());
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
        if (getBlock().modifiedInScope(var))
          return true;
        for (CatchClause cc : getCatchClauseList())
          if (cc.modifiedInScope(var))
            return true;
        return hasNonEmptyFinally() && getFinally().modifiedInScope(var);
      }
    finally {
    }
  }
  /**
   * @attribute inh
   * @aspect ExceptionHandling
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ExceptionHandling.jrag:55
   */
  @ASTNodeAnnotation.Attribute
  public boolean handlesException(TypeDecl exceptionType) {
    Object _parameters = exceptionType;
    if (handlesException_TypeDecl_values == null) handlesException_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(handlesException_TypeDecl_values.containsKey(_parameters)) {
      return ((Boolean)handlesException_TypeDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean handlesException_TypeDecl_value = getParent().Define_boolean_handlesException(this, null, exceptionType);
    if (isFinal && num == state().boundariesCrossed) {
      handlesException_TypeDecl_values.put(_parameters, Boolean.valueOf(handlesException_TypeDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return handlesException_TypeDecl_value;
  }
  protected java.util.Map handlesException_TypeDecl_values;
/**
 * @apilevel internal
 */
private void handlesException_TypeDecl_reset() {
  handlesException_TypeDecl_values = null;
}  
  /**
   * @attribute inh
   * @aspect UnreachableStatements
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/UnreachableStatements.jrag:141
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeError() {
    if(typeError_computed) {
      return typeError_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeError_value = getParent().Define_TypeDecl_typeError(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      typeError_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeError_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeError_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeError_value;
/**
 * @apilevel internal
 */
private void typeError_reset() {
  typeError_computed = false;
  typeError_value = null;
}  
  /**
   * @attribute inh
   * @aspect UnreachableStatements
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/UnreachableStatements.jrag:142
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeRuntimeException() {
    if(typeRuntimeException_computed) {
      return typeRuntimeException_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeRuntimeException_value = getParent().Define_TypeDecl_typeRuntimeException(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      typeRuntimeException_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeRuntimeException_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeRuntimeException_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeRuntimeException_value;
/**
 * @apilevel internal
 */
private void typeRuntimeException_reset() {
  typeRuntimeException_computed = false;
  typeRuntimeException_value = null;
}  
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BranchTarget.jrag:249
   * @apilevel internal
   */
  public FinallyHost Define_FinallyHost_enclosingFinally(ASTNode caller, ASTNode child, Stmt branch) {
    if (caller == getFinallyOptNoTransform()) {
      return enclosingFinally(branch);
    }
    else  {
      int childIndex = this.getIndexOfChild(caller);
      return hasNonEmptyFinally() ? this : enclosingFinally(branch);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:687
   * @apilevel internal
   */
  public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
    if (caller == getFinallyOptNoTransform()) {
      return isDAbefore(v);
    }
    else if (caller == getCatchClauseListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return getBlock().isDAbefore(v);
    }
    else if (caller == getBlockNoTransform()) {
      return isDAbefore(v);
    }
    else {
      return getParent().Define_boolean_isDAbefore(this, caller, v);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:1286
   * @apilevel internal
   */
  public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
    if (caller == getFinallyOptNoTransform()){
    if(!getBlock().isDUeverywhere(v))
      return false;
    for(int i = 0; i < getNumCatchClause(); i++)
      if(!getCatchClause(i).getBlock().unassignedEverywhere(v, this))
        return false;
    return true;
  }
    else if (caller == getCatchClauseListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      {
    if(!getBlock().isDUafter(v))
      return false;
    if(!getBlock().isDUeverywhere(v))
      return false;
    return true;
  }
    }
    else if (caller == getBlockNoTransform()) {
      return isDUbefore(v);
    }
    else {
      return getParent().Define_boolean_isDUbefore(this, caller, v);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ExceptionHandling.jrag:203
   * @apilevel internal
   */
  public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
    if (caller == getBlockNoTransform()){
    for(int i = 0; i < getNumCatchClause(); i++)
      if(getCatchClause(i).handles(exceptionType))
        return true;
    if(hasNonEmptyFinally() && !getFinally().canCompleteNormally())
      return true;
    return handlesException(exceptionType);
  }
    else if (caller == getCatchClauseListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      {
    if(hasNonEmptyFinally() && !getFinally().canCompleteNormally())
      return true;
    return handlesException(exceptionType);
  }
    }
    else {
      return getParent().Define_boolean_handlesException(this, caller, exceptionType);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/UnreachableStatements.jrag:124
   * @apilevel internal
   */
  public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
    if (caller == getFinallyOptNoTransform()) {
      return reachable();
    }
    else if (caller == getBlockNoTransform()) {
      return reachable();
    }
    else {
      return getParent().Define_boolean_reachable(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/UnreachableStatements.jrag:131
   * @apilevel internal
   */
  public boolean Define_boolean_reachableCatchClause(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
    if (caller == getCatchClauseListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      {
    for(int i = 0; i < childIndex; i++)
      if(getCatchClause(i).handles(exceptionType))
        return false;
    if(catchableException(exceptionType))
      return true;
    if(exceptionType.mayCatch(typeError()) || exceptionType.mayCatch(typeRuntimeException()))
      return true;
    return false;
  }
    }
    else {
      return getParent().Define_boolean_reachableCatchClause(this, caller, exceptionType);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/UnreachableStatements.jrag:159
   * @apilevel internal
   */
  public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
    if (caller == getFinallyOptNoTransform()) {
      return reachable();
    }
    else if (caller == getCatchClauseListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return reachable();
    }
    else if (caller == getBlockNoTransform()) {
      return reachable();
    }
    else {
      return getParent().Define_boolean_reportUnreachable(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/PreciseRethrow.jrag:195
   * @apilevel internal
   */
  public Collection<TypeDecl> Define_Collection_TypeDecl__caughtExceptions(ASTNode caller, ASTNode child) {
    if (caller == getCatchClauseListNoTransform()) {
      int index = caller.getIndexOfChild(child);
      {
    Collection<TypeDecl> excp = new HashSet<TypeDecl>();
    getBlock().collectExceptions(excp, this);
    Collection<TypeDecl> caught = new LinkedList<TypeDecl>();
    Iterator<TypeDecl> iter = excp.iterator();
    while (iter.hasNext()) {
      TypeDecl exception = iter.next();
      // this catch clause handles the exception
      if (!getCatchClause(index).handles(exception))
        continue;
      // no previous catch clause handles the exception
      boolean already = false;
      for (int i = 0; i < index; ++i) {
        if (getCatchClause(i).handles(exception)) {
          already = true;
          break;
        }
      }
      if (!already) {
        caught.add(exception);
      }
    }
    return caught;
  }
    }
    else {
      return getParent().Define_Collection_TypeDecl__caughtExceptions(this, caller);
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
