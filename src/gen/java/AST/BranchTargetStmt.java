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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:194
 * @production BranchTargetStmt : {@link Stmt};

 */
public abstract class BranchTargetStmt extends Stmt implements Cloneable {
  /**
   * @aspect BranchTarget
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BranchTarget.jrag:87
   */
  public void collectBranches(Collection<Stmt> c) {
    c.addAll(escapedBranches());
  }
  /**
   * @declaredat ASTNode:1
   */
  public BranchTargetStmt() {
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
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:15
   */
  protected int numChildren() {
    return 0;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:21
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:27
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    targetBranches_reset();
    escapedBranches_reset();
    branches_reset();
    targetContinues_reset();
    targetBreaks_reset();
    reachableBreak_reset();
    reachableContinue_reset();
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
  public BranchTargetStmt clone() throws CloneNotSupportedException {
    BranchTargetStmt node = (BranchTargetStmt) super.clone();
    return node;
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @deprecated Please use emitTreeCopy or emitTreeCopyNoTransform instead
   * @declaredat ASTNode:63
   */
  public abstract BranchTargetStmt fullCopy();
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:70
   */
  public abstract BranchTargetStmt treeCopyNoTransform();
  /**
   * Create a deep copy of the AST subtree at this node.
   * The subtree of this node is traversed to trigger rewrites before copy.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:78
   */
  public abstract BranchTargetStmt treeCopy();
  /**
   * @apilevel internal
   */
  protected boolean targetBranches_computed = false;
  /**
   * @apilevel internal
   */
  protected Collection<Stmt> targetBranches_value;
/**
 * @apilevel internal
 */
private void targetBranches_reset() {
  targetBranches_computed = false;
  targetBranches_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Collection<Stmt> targetBranches() {
    if(targetBranches_computed) {
      return targetBranches_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    targetBranches_value = targetBranches_compute();
    if (isFinal && num == state().boundariesCrossed) {
      targetBranches_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return targetBranches_value;
  }
  /**
   * @apilevel internal
   */
  private Collection<Stmt> targetBranches_compute() {
      Collection<Stmt> set = new HashSet<Stmt>();
      for (Stmt branch : branches()) {
        if (potentialTargetOf(branch)) {
          set.add(branch);
        }
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
      for (Stmt branch : branches()) {
        if (!potentialTargetOf(branch)) {
          set.add(branch);
        } else if(branch instanceof ReturnStmt) {
          set.add(branch);
        }
      }
      return set;
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
      super.collectBranches(set);
      return set;
    }
  @ASTNodeAnnotation.Attribute
  public boolean potentialTargetOf(Stmt branch) {
    ASTNode$State state = state();
    boolean potentialTargetOf_Stmt_value = false;

    return potentialTargetOf_Stmt_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean targetContinues_computed = false;
  /**
   * @apilevel internal
   */
  protected Collection<Stmt> targetContinues_value;
/**
 * @apilevel internal
 */
private void targetContinues_reset() {
  targetContinues_computed = false;
  targetContinues_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Collection<Stmt> targetContinues() {
    if(targetContinues_computed) {
      return targetContinues_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    targetContinues_value = targetContinues_compute();
    if (isFinal && num == state().boundariesCrossed) {
      targetContinues_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return targetContinues_value;
  }
  /**
   * @apilevel internal
   */
  private Collection<Stmt> targetContinues_compute() {
      HashSet<Stmt> set = new HashSet<Stmt>();
      for (Stmt branch : targetBranches()) {
        if (branch instanceof ContinueStmt)
          set.add(branch);
      }
      if (getParent() instanceof LabeledStmt) {
        for (Stmt branch : ((LabeledStmt)getParent()).targetBranches()) {
          if (branch instanceof ContinueStmt)
            set.add(branch);
        }
      }
      return set;
    }
  /**
   * @apilevel internal
   */
  protected boolean targetBreaks_computed = false;
  /**
   * @apilevel internal
   */
  protected Collection<Stmt> targetBreaks_value;
/**
 * @apilevel internal
 */
private void targetBreaks_reset() {
  targetBreaks_computed = false;
  targetBreaks_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Collection<Stmt> targetBreaks() {
    if(targetBreaks_computed) {
      return targetBreaks_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    targetBreaks_value = targetBreaks_compute();
    if (isFinal && num == state().boundariesCrossed) {
      targetBreaks_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return targetBreaks_value;
  }
  /**
   * @apilevel internal
   */
  private Collection<Stmt> targetBreaks_compute() {
      HashSet<Stmt> set = new HashSet<Stmt>();
      for (Stmt branch : targetBranches()) {
        if (branch instanceof BreakStmt)
          set.add(branch);
      }
      return set;
    }
  /**
   * @apilevel internal
   */
  protected boolean reachableBreak_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean reachableBreak_value;
/**
 * @apilevel internal
 */
private void reachableBreak_reset() {
  reachableBreak_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean reachableBreak() {
    if(reachableBreak_computed) {
      return reachableBreak_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    reachableBreak_value = reachableBreak_compute();
    if (isFinal && num == state().boundariesCrossed) {
      reachableBreak_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return reachableBreak_value;
  }
  /**
   * @apilevel internal
   */
  private boolean reachableBreak_compute() {
      for (Iterator iter = targetBreaks().iterator(); iter.hasNext(); ) {
        BreakStmt stmt = (BreakStmt)iter.next();
        if(stmt.reachable())
          return true;
      }
      return false;
    }
  /**
   * @apilevel internal
   */
  protected boolean reachableContinue_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean reachableContinue_value;
/**
 * @apilevel internal
 */
private void reachableContinue_reset() {
  reachableContinue_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean reachableContinue() {
    if(reachableContinue_computed) {
      return reachableContinue_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    reachableContinue_value = reachableContinue_compute();
    if (isFinal && num == state().boundariesCrossed) {
      reachableContinue_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return reachableContinue_value;
  }
  /**
   * @apilevel internal
   */
  private boolean reachableContinue_compute() {
      for(Iterator iter = targetContinues().iterator(); iter.hasNext(); ) {
        Stmt stmt = (Stmt)iter.next();
        if(stmt.reachable())
          return true;
      }
      return false;
    }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BranchTarget.jrag:257
   * @apilevel internal
   */
  public FinallyHost Define_FinallyHost_enclosingFinally(ASTNode caller, ASTNode child, Stmt branch) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return potentialTargetOf(branch) ? null : enclosingFinally(branch);
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
