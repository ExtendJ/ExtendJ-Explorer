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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:210
 * @production BreakStmt : {@link Stmt} ::= <span class="component">&lt;Label:String&gt;</span> <span class="component">[Finally:{@link Block}]</span>;

 */
public class BreakStmt extends Stmt implements Cloneable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:715
   */
  public void prettyPrint(StringBuffer sb) {
    sb.append(indent());
    sb.append("break ");
    if(hasLabel())
      sb.append(getLabel());
    sb.append(";");
  }
  /**
   * @aspect BranchTarget
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BranchTarget.jrag:79
   */
  public void collectBranches(Collection<Stmt> c) {
    c.add(this);
  }
  /**
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:406
   */
  public void nameCheck() {
    if(!hasLabel() && !insideLoop() && !insideSwitch())
      error("break outside switch or loop");
    else if(hasLabel()) {
      LabeledStmt label = lookupLabel(getLabel());
      if(label == null)
        error("labeled break must have visible matching label");
    }
  }
  /**
   * @declaredat ASTNode:1
   */
  public BreakStmt() {
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
    setChild(new Opt(), 0);
  }
  /**
   * @declaredat ASTNode:14
   */
  public BreakStmt(String p0) {
    setLabel(p0);
  }
  /**
   * @declaredat ASTNode:17
   */
  public BreakStmt(beaver.Symbol p0) {
    setLabel(p0);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:23
   */
  protected int numChildren() {
    return 0;
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
    targetStmt_reset();
    isDAafter_Variable_reset();
    isDUafterReachedFinallyBlocks_Variable_reset();
    isDAafterReachedFinallyBlocks_Variable_reset();
    isDUafter_Variable_reset();
    getFinallyOpt_reset();
    canCompleteNormally_reset();
    lookupLabel_String_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:49
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:55
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:61
   */
  public BreakStmt clone() throws CloneNotSupportedException {
    BreakStmt node = (BreakStmt) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:68
   */
  public BreakStmt copy() {
    try {
      BreakStmt node = (BreakStmt) clone();
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
   * @declaredat ASTNode:87
   */
  public BreakStmt fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:96
   */
  public BreakStmt treeCopyNoTransform() {
    BreakStmt tree = (BreakStmt) copy();
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        switch (i) {
        case 0:
          tree.children[i] = new Opt();
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
   * @declaredat ASTNode:121
   */
  public BreakStmt treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:128
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_Label == ((BreakStmt)node).tokenString_Label);    
  }
  /**
   * Replaces the lexeme Label.
   * @param value The new value for the lexeme Label.
   * @apilevel high-level
   */
  public void setLabel(String value) {
    tokenString_Label = value;
  }
  /**
   * @apilevel internal
   */
  protected String tokenString_Label;
  /**
   */
  public int Labelstart;
  /**
   */
  public int Labelend;
  /**
   * JastAdd-internal setter for lexeme Label using the Beaver parser.
   * @param symbol Symbol containing the new value for the lexeme Label
   * @apilevel internal
   */
  public void setLabel(beaver.Symbol symbol) {
    if(symbol.value != null && !(symbol.value instanceof String))
    throw new UnsupportedOperationException("setLabel is only valid for String lexemes");
    tokenString_Label = (String)symbol.value;
    Labelstart = symbol.getStart();
    Labelend = symbol.getEnd();
  }
  /**
   * Retrieves the value for the lexeme Label.
   * @return The value for the lexeme Label.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="Label")
  public String getLabel() {
    return tokenString_Label != null ? tokenString_Label : "";
  }
  /**
   * Replaces the optional node for the Finally child. This is the <code>Opt</code>
   * node containing the child Finally, not the actual child!
   * @param opt The new node to be used as the optional node for the Finally child.
   * @apilevel low-level
   */
  public void setFinallyOpt(Opt<Block> opt) {
    setChild(opt, 0);
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
   * Retrieves the optional node for child Finally. This is the <code>Opt</code> node containing the child Finally, not the actual child!
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The optional node for child Finally.
   * @apilevel low-level
   */
  public Opt<Block> getFinallyOptNoTransform() {
    return (Opt<Block>) getChildNoTransform(0);
  }
  /**
   * Retrieves the child position of the optional child Finally.
   * @return The the child position of the optional child Finally.
   * @apilevel low-level
   */
  protected int getFinallyOptChildPosition() {
    return 0;
  }
  @ASTNodeAnnotation.Attribute
  public boolean hasLabel() {
    ASTNode$State state = state();
    boolean hasLabel_value = !getLabel().equals("");

    return hasLabel_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean canBranchTo(BranchTargetStmt target) {
    ASTNode$State state = state();
    boolean canBranchTo_BranchTargetStmt_value = !hasLabel();

    return canBranchTo_BranchTargetStmt_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean canBranchTo(LabeledStmt target) {
    ASTNode$State state = state();
    boolean canBranchTo_LabeledStmt_value = hasLabel() && target.getLabel().equals(getLabel());

    return canBranchTo_LabeledStmt_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean canBranchTo(SwitchStmt target) {
    ASTNode$State state = state();
    boolean canBranchTo_SwitchStmt_value = !hasLabel();

    return canBranchTo_SwitchStmt_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean targetStmt_computed = false;
  /**
   * @apilevel internal
   */
  protected Stmt targetStmt_value;
/**
 * @apilevel internal
 */
private void targetStmt_reset() {
  targetStmt_computed = false;
  targetStmt_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Stmt targetStmt() {
    if(targetStmt_computed) {
      return targetStmt_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    targetStmt_value = branchTarget(this);
    if (isFinal && num == state().boundariesCrossed) {
      targetStmt_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return targetStmt_value;
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
    boolean isDAafter_Variable_value = true;
    if (isFinal && num == state().boundariesCrossed) {
      isDAafter_Variable_values.put(_parameters, Boolean.valueOf(isDAafter_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDAafter_Variable_value;
  }
  protected java.util.Map isDUafterReachedFinallyBlocks_Variable_values;
/**
 * @apilevel internal
 */
private void isDUafterReachedFinallyBlocks_Variable_reset() {
  isDUafterReachedFinallyBlocks_Variable_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isDUafterReachedFinallyBlocks(Variable v) {
    Object _parameters = v;
    if (isDUafterReachedFinallyBlocks_Variable_values == null) isDUafterReachedFinallyBlocks_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(isDUafterReachedFinallyBlocks_Variable_values.containsKey(_parameters)) {
      return ((Boolean)isDUafterReachedFinallyBlocks_Variable_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean isDUafterReachedFinallyBlocks_Variable_value = isDUafterReachedFinallyBlocks_compute(v);
    if (isFinal && num == state().boundariesCrossed) {
      isDUafterReachedFinallyBlocks_Variable_values.put(_parameters, Boolean.valueOf(isDUafterReachedFinallyBlocks_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDUafterReachedFinallyBlocks_Variable_value;
  }
  /**
   * @apilevel internal
   */
  private boolean isDUafterReachedFinallyBlocks_compute(Variable v) {
      Iterator<FinallyHost> iter = finallyIterator();
      if (!isDUbefore(v) && !iter.hasNext())
        return false;
      while (iter.hasNext()) {
        FinallyHost f = iter.next();
        if (!f.isDUafterFinally(v))
          return false;
      }
      return true;
    }
  protected java.util.Map isDAafterReachedFinallyBlocks_Variable_values;
/**
 * @apilevel internal
 */
private void isDAafterReachedFinallyBlocks_Variable_reset() {
  isDAafterReachedFinallyBlocks_Variable_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isDAafterReachedFinallyBlocks(Variable v) {
    Object _parameters = v;
    if (isDAafterReachedFinallyBlocks_Variable_values == null) isDAafterReachedFinallyBlocks_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(isDAafterReachedFinallyBlocks_Variable_values.containsKey(_parameters)) {
      return ((Boolean)isDAafterReachedFinallyBlocks_Variable_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean isDAafterReachedFinallyBlocks_Variable_value = isDAafterReachedFinallyBlocks_compute(v);
    if (isFinal && num == state().boundariesCrossed) {
      isDAafterReachedFinallyBlocks_Variable_values.put(_parameters, Boolean.valueOf(isDAafterReachedFinallyBlocks_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDAafterReachedFinallyBlocks_Variable_value;
  }
  /**
   * @apilevel internal
   */
  private boolean isDAafterReachedFinallyBlocks_compute(Variable v) {
      if(isDAbefore(v))
        return true;
      Iterator<FinallyHost> iter = finallyIterator();
      if (!iter.hasNext())
        return false;
      while (iter.hasNext()) {
        FinallyHost f = iter.next();
        if (!f.isDAafterFinally(v))
          return false;
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
    boolean isDUafter_Variable_value = true;
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
  protected boolean getFinallyOpt_computed = false;
  /**
   * @apilevel internal
   */
  protected Opt getFinallyOpt_value;
/**
 * @apilevel internal
 */
private void getFinallyOpt_reset() {
  getFinallyOpt_computed = false;
  getFinallyOpt_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Opt getFinallyOpt() {
    if(getFinallyOpt_computed) {
      return (Opt) getChild(getFinallyOptChildPosition());
    }
    if(getFinallyOpt_computed) {
      return getFinallyOpt_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    getFinallyOpt_value = getFinallyOpt_compute();
    setFinallyOpt(getFinallyOpt_value);
    if (isFinal && num == state().boundariesCrossed) {
      getFinallyOpt_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    Opt node = (Opt) this.getChild(getFinallyOptChildPosition());
    return node;
  }
  /**
   * @apilevel internal
   */
  private Opt getFinallyOpt_compute() {
      return branchFinallyOpt();
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
    canCompleteNormally_value = false;
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
    boolean modifiedInScope_Variable_value = false;

    return modifiedInScope_Variable_value;
  }
  /**
   * @attribute inh
   * @aspect BranchTarget
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BranchTarget.jrag:229
   */
  @ASTNodeAnnotation.Attribute
  public LabeledStmt lookupLabel(String name) {
    Object _parameters = name;
    if (lookupLabel_String_values == null) lookupLabel_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(lookupLabel_String_values.containsKey(_parameters)) {
      return (LabeledStmt)lookupLabel_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    LabeledStmt lookupLabel_String_value = getParent().Define_LabeledStmt_lookupLabel(this, null, name);
    if (isFinal && num == state().boundariesCrossed) {
      lookupLabel_String_values.put(_parameters, lookupLabel_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return lookupLabel_String_value;
  }
  protected java.util.Map lookupLabel_String_values;
/**
 * @apilevel internal
 */
private void lookupLabel_String_reset() {
  lookupLabel_String_values = null;
}  
  /**
   * @attribute inh
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:392
   */
  @ASTNodeAnnotation.Attribute
  public boolean insideLoop() {
    ASTNode$State state = state();
    boolean insideLoop_value = getParent().Define_boolean_insideLoop(this, null);

    return insideLoop_value;
  }
  /**
   * @attribute inh
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:401
   */
  @ASTNodeAnnotation.Attribute
  public boolean insideSwitch() {
    ASTNode$State state = state();
    boolean insideSwitch_value = getParent().Define_boolean_insideSwitch(this, null);

    return insideSwitch_value;
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
