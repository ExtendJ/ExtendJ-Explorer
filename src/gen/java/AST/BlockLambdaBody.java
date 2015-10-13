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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/grammar/Lambda.ast:12
 * @production BlockLambdaBody : {@link LambdaBody} ::= <span class="component">{@link Block}</span>;

 */
public class BlockLambdaBody extends LambdaBody implements Cloneable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/PrettyPrint.jadd:130
   */
  public void prettyPrint(StringBuffer sb) {
		getBlock().prettyPrint(sb);
	}
  /**
   * @aspect ReturnCompatible
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/LambdaBody.jrag:57
   */
  public boolean noReturnsHasResult() {
		ArrayList<ReturnStmt> returnList = lambdaReturns();
		for(int i = 0; i < returnList.size(); i++) {
			if(returnList.get(i).hasResult())
				return false;
		}
		return true;	
	}
  /**
   * @aspect ReturnCompatible
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/LambdaBody.jrag:65
   */
  public boolean allReturnsHasResult() {
		ArrayList<ReturnStmt> returnList = lambdaReturns();
		for(int i = 0; i < returnList.size(); i++) {
			if(!returnList.get(i).hasResult())
				return false;
		}
		return true;
	}
  /**
   * @aspect TypeCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TypeCheck.jrag:175
   */
  public void typeCheck() {
		// 15.27.2
		if(!voidCompatible() && !valueCompatible()) {
			error("Block lambda bodies must be either void or value compatible");
		}
	}
  /**
   * @declaredat ASTNode:1
   */
  public BlockLambdaBody() {
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
  }
  /**
   * @declaredat ASTNode:13
   */
  public BlockLambdaBody(Block p0) {
    setChild(p0, 0);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:19
   */
  protected int numChildren() {
    return 1;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:25
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:31
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    isBlockBody_reset();
    isExprBody_reset();
    voidCompatible_reset();
    valueCompatible_reset();
    congruentTo_FunctionDescriptor_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:42
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
    BlockLambdaBody_lambdaReturns_computed = false;
    BlockLambdaBody_lambdaReturns_value = null;
        BlockLambdaBody_lambdaReturns_contributors = null;
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
  public BlockLambdaBody clone() throws CloneNotSupportedException {
    BlockLambdaBody node = (BlockLambdaBody) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:64
   */
  public BlockLambdaBody copy() {
    try {
      BlockLambdaBody node = (BlockLambdaBody) clone();
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
  public BlockLambdaBody fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:92
   */
  public BlockLambdaBody treeCopyNoTransform() {
    BlockLambdaBody tree = (BlockLambdaBody) copy();
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
   * @declaredat ASTNode:112
   */
  public BlockLambdaBody treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:119
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
  @ASTNodeAnnotation.Attribute
  public boolean modifiedInScope(Variable var) {
    ASTNode$State state = state();
    boolean modifiedInScope_Variable_value = getBlock().modifiedInScope(var);

    return modifiedInScope_Variable_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isBlockBody_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isBlockBody_value;
/**
 * @apilevel internal
 */
private void isBlockBody_reset() {
  isBlockBody_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isBlockBody() {
    if(isBlockBody_computed) {
      return isBlockBody_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isBlockBody_value = true;
    if (isFinal && num == state().boundariesCrossed) {
      isBlockBody_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isBlockBody_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isExprBody_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isExprBody_value;
/**
 * @apilevel internal
 */
private void isExprBody_reset() {
  isExprBody_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isExprBody() {
    if(isExprBody_computed) {
      return isExprBody_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isExprBody_value = false;
    if (isFinal && num == state().boundariesCrossed) {
      isExprBody_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isExprBody_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean voidCompatible_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean voidCompatible_value;
/**
 * @apilevel internal
 */
private void voidCompatible_reset() {
  voidCompatible_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean voidCompatible() {
    if(voidCompatible_computed) {
      return voidCompatible_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    voidCompatible_value = noReturnsHasResult();
    if (isFinal && num == state().boundariesCrossed) {
      voidCompatible_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return voidCompatible_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean valueCompatible_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean valueCompatible_value;
/**
 * @apilevel internal
 */
private void valueCompatible_reset() {
  valueCompatible_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean valueCompatible() {
    if(valueCompatible_computed) {
      return valueCompatible_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    valueCompatible_value = allReturnsHasResult() && !getBlock().canCompleteNormally();
    if (isFinal && num == state().boundariesCrossed) {
      valueCompatible_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return valueCompatible_value;
  }
  protected java.util.Map congruentTo_FunctionDescriptor_values;
/**
 * @apilevel internal
 */
private void congruentTo_FunctionDescriptor_reset() {
  congruentTo_FunctionDescriptor_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean congruentTo(FunctionDescriptor f) {
    Object _parameters = f;
    if (congruentTo_FunctionDescriptor_values == null) congruentTo_FunctionDescriptor_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(congruentTo_FunctionDescriptor_values.containsKey(_parameters)) {
      return ((Boolean)congruentTo_FunctionDescriptor_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean congruentTo_FunctionDescriptor_value = congruentTo_compute(f);
    if (isFinal && num == state().boundariesCrossed) {
      congruentTo_FunctionDescriptor_values.put(_parameters, Boolean.valueOf(congruentTo_FunctionDescriptor_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return congruentTo_FunctionDescriptor_value;
  }
  /**
   * @apilevel internal
   */
  private boolean congruentTo_compute(FunctionDescriptor f) {
  		if(f.method.type().isVoid()) {
  			return voidCompatible();
  		}
  		else {
  			if(!valueCompatible())
  				return false;
  			for(ReturnStmt returnStmt : lambdaReturns()) {
  				if(!returnStmt.getResult().assignConversionTo(f.method.type()))
  					return false;
  			}
  			return true;
  		}
  	}
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:222
   * @apilevel internal
   */
  public boolean Define_boolean_assignmentContext(ASTNode caller, ASTNode child) {
    if (caller == getBlockNoTransform()) {
      return false;
    }
    else {
      return getParent().Define_boolean_assignmentContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:223
   * @apilevel internal
   */
  public boolean Define_boolean_invocationContext(ASTNode caller, ASTNode child) {
    if (caller == getBlockNoTransform()) {
      return false;
    }
    else {
      return getParent().Define_boolean_invocationContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:224
   * @apilevel internal
   */
  public boolean Define_boolean_castContext(ASTNode caller, ASTNode child) {
    if (caller == getBlockNoTransform()) {
      return false;
    }
    else {
      return getParent().Define_boolean_castContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:225
   * @apilevel internal
   */
  public boolean Define_boolean_stringContext(ASTNode caller, ASTNode child) {
    if (caller == getBlockNoTransform()) {
      return false;
    }
    else {
      return getParent().Define_boolean_stringContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:226
   * @apilevel internal
   */
  public boolean Define_boolean_numericContext(ASTNode caller, ASTNode child) {
    if (caller == getBlockNoTransform()) {
      return false;
    }
    else {
      return getParent().Define_boolean_numericContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TypeCheck.jrag:40
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_returnType(ASTNode caller, ASTNode child) {
    if (caller == getBlockNoTransform()){
		TypeDecl decl = enclosingLambda().targetType();
		if(decl == null)
			return unknownType();
		else if(!(decl instanceof InterfaceDecl))
			return unknownType();
		else {
			InterfaceDecl iDecl = (InterfaceDecl)decl;
			if(!iDecl.isFunctional())
				return unknownType();
			else {
				return iDecl.functionDescriptor().method.type();
			}
		}
	}
    else {
      return getParent().Define_TypeDecl_returnType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/UnreachableStatements.jrag:29
   * @apilevel internal
   */
  public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
    if (caller == getBlockNoTransform()) {
      return true;
    }
    else {
      return getParent().Define_boolean_reachable(this, caller);
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }  /**
   * @attribute coll
   * @aspect ReturnCompatible
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/LambdaBody.jrag:49
   */
  @ASTNodeAnnotation.Attribute
  public ArrayList<ReturnStmt> lambdaReturns() {
    if(BlockLambdaBody_lambdaReturns_computed) {
      return BlockLambdaBody_lambdaReturns_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    BlockLambdaBody_lambdaReturns_value = lambdaReturns_compute();
    if (isFinal && num == state().boundariesCrossed) {
      BlockLambdaBody_lambdaReturns_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return BlockLambdaBody_lambdaReturns_value;
  }
  java.util.Set BlockLambdaBody_lambdaReturns_contributors;

  /**
   * @apilevel internal
   * @return the contributor set for lambdaReturns
   */
  public java.util.Set BlockLambdaBody_lambdaReturns_contributors() {
    if(BlockLambdaBody_lambdaReturns_contributors == null)
      BlockLambdaBody_lambdaReturns_contributors  = new ASTNode$State.IdentityHashSet(4);
    return BlockLambdaBody_lambdaReturns_contributors;
  }

  /**
   * @apilevel internal
   */
  private ArrayList<ReturnStmt> lambdaReturns_compute() {
    ASTNode node = this;
    while(node.getParent() != null && !(node instanceof Program)) {
      node = node.getParent();
    }
    Program root = (Program) node;
    root.collect_contributors_BlockLambdaBody_lambdaReturns();
    BlockLambdaBody_lambdaReturns_value = new ArrayList<ReturnStmt>();
    if(BlockLambdaBody_lambdaReturns_contributors != null)
    for (java.util.Iterator iter = BlockLambdaBody_lambdaReturns_contributors.iterator(); iter.hasNext(); ) {
      ASTNode contributor = (ASTNode) iter.next();
      contributor.contributeTo_BlockLambdaBody_BlockLambdaBody_lambdaReturns(BlockLambdaBody_lambdaReturns_value);
    }
    // TODO: disabled temporarily since collections may not be cached
    //BlockLambdaBody_lambdaReturns_contributors = null;
    return BlockLambdaBody_lambdaReturns_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean BlockLambdaBody_lambdaReturns_computed = false;
  /**
   * @apilevel internal
   */
  protected ArrayList<ReturnStmt> BlockLambdaBody_lambdaReturns_value;
/**
 * @apilevel internal
 */
private void BlockLambdaBody_lambdaReturns_reset() {
  BlockLambdaBody_lambdaReturns_computed = false;
  BlockLambdaBody_lambdaReturns_value = null;
}  
}
