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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/grammar/Lambda.ast:3
 * @production LambdaParameters : {@link ASTNode};

 */
public abstract class LambdaParameters extends ASTNode<ASTNode> implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public LambdaParameters() {
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
    enclosingLambda_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:34
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:40
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:46
   */
  public LambdaParameters clone() throws CloneNotSupportedException {
    LambdaParameters node = (LambdaParameters) super.clone();
    return node;
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @deprecated Please use emitTreeCopy or emitTreeCopyNoTransform instead
   * @declaredat ASTNode:57
   */
  public abstract LambdaParameters fullCopy();
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:64
   */
  public abstract LambdaParameters treeCopyNoTransform();
  /**
   * Create a deep copy of the AST subtree at this node.
   * The subtree of this node is traversed to trigger rewrites before copy.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:72
   */
  public abstract LambdaParameters treeCopy();
  /**
   * @attribute syn
   * @aspect LambdaExpr
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/LambdaExpr.jrag:45
   */
  @ASTNodeAnnotation.Attribute
  public abstract int numParameters();
  /**
   * @attribute syn
   * @aspect LambdaExpr
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/LambdaExpr.jrag:51
   */
  @ASTNodeAnnotation.Attribute
  public abstract boolean congruentTo(FunctionDescriptor f);
  /**
   * @attribute inh
   * @aspect PreciseRethrow
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/EffectivelyFinal.jrag:31
   */
  @ASTNodeAnnotation.Attribute
  public boolean inhModifiedInScope(Variable var) {
    ASTNode$State state = state();
    boolean inhModifiedInScope_Variable_value = getParent().Define_boolean_inhModifiedInScope(this, null, var);

    return inhModifiedInScope_Variable_value;
  }
  /**
   * @attribute inh
   * @aspect EnclosingLambda
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/EnclosingLambda.jrag:30
   */
  @ASTNodeAnnotation.Attribute
  public LambdaExpr enclosingLambda() {
    if(enclosingLambda_computed) {
      return enclosingLambda_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    enclosingLambda_value = getParent().Define_LambdaExpr_enclosingLambda(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      enclosingLambda_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return enclosingLambda_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean enclosingLambda_computed = false;
  /**
   * @apilevel internal
   */
  protected LambdaExpr enclosingLambda_value;
/**
 * @apilevel internal
 */
private void enclosingLambda_reset() {
  enclosingLambda_computed = false;
  enclosingLambda_value = null;
}  
  /**
   * @attribute inh
   * @aspect Java8NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/NameCheck.jrag:31
   */
  @ASTNodeAnnotation.Attribute
  public VariableScope outerScope() {
    ASTNode$State state = state();
    VariableScope outerScope_value = getParent().Define_VariableScope_outerScope(this, null);

    return outerScope_value;
  }
  /**
   * @attribute inh
   * @aspect TypeCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TypeCheck.jrag:29
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl unknownType() {
    ASTNode$State state = state();
    TypeDecl unknownType_value = getParent().Define_TypeDecl_unknownType(this, null);

    return unknownType_value;
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
