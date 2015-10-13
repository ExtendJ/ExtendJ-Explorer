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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:186
 * @production ConditionalExpr : {@link Expr} ::= <span class="component">Condition:{@link Expr}</span> <span class="component">TrueExpr:{@link Expr}</span> <span class="component">FalseExpr:{@link Expr}</span>;

 */
public class ConditionalExpr extends Expr implements Cloneable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:471
   */
  public void prettyPrint(StringBuffer sb) {
    getCondition().prettyPrint(sb);
    sb.append(" ? ");
    getTrueExpr().prettyPrint(sb);
    sb.append(" : ");
    getFalseExpr().prettyPrint(sb);
  }
  /**
   * @aspect TypeCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeCheck.jrag:566
   */
  public void typeCheck() {
    if(!getCondition().type().isBoolean())
      error("The first operand of a conditional expression must be a boolean");
    if(type().isUnknown() && !getTrueExpr().type().isUnknown() && !getFalseExpr().type().isUnknown()) {
      error("The types of the second and third operand in this conditional expression do not match");
    }
  }
  /**
   * @declaredat ASTNode:1
   */
  public ConditionalExpr() {
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
    children = new ASTNode[3];
  }
  /**
   * @declaredat ASTNode:13
   */
  public ConditionalExpr(Expr p0, Expr p1, Expr p2) {
    setChild(p0, 0);
    setChild(p1, 1);
    setChild(p2, 2);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:21
   */
  protected int numChildren() {
    return 3;
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
    constant_reset();
    isConstant_reset();
    booleanOperator_reset();
    type_reset();
    compatibleStrictContext_TypeDecl_reset();
    compatibleLooseContext_TypeDecl_reset();
    pertinentToApplicability_Expr_BodyDecl_int_reset();
    moreSpecificThan_TypeDecl_TypeDecl_reset();
    potentiallyCompatible_TypeDecl_BodyDecl_reset();
    isBooleanExpression_reset();
    isBooleanConditional_reset();
    isNumericExpression_reset();
    isNumericConditional_reset();
    isReferenceConditional_reset();
    isPolyExpression_reset();
    assignConversionTo_TypeDecl_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:55
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:61
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:67
   */
  public ConditionalExpr clone() throws CloneNotSupportedException {
    ConditionalExpr node = (ConditionalExpr) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:74
   */
  public ConditionalExpr copy() {
    try {
      ConditionalExpr node = (ConditionalExpr) clone();
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
   * @declaredat ASTNode:93
   */
  public ConditionalExpr fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:102
   */
  public ConditionalExpr treeCopyNoTransform() {
    ConditionalExpr tree = (ConditionalExpr) copy();
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
   * @declaredat ASTNode:122
   */
  public ConditionalExpr treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:129
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) ;    
  }
  /**
   * Replaces the Condition child.
   * @param node The new node to replace the Condition child.
   * @apilevel high-level
   */
  public void setCondition(Expr node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the Condition child.
   * @return The current node used as the Condition child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Condition")
  public Expr getCondition() {
    return (Expr) getChild(0);
  }
  /**
   * Retrieves the Condition child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Condition child.
   * @apilevel low-level
   */
  public Expr getConditionNoTransform() {
    return (Expr) getChildNoTransform(0);
  }
  /**
   * Replaces the TrueExpr child.
   * @param node The new node to replace the TrueExpr child.
   * @apilevel high-level
   */
  public void setTrueExpr(Expr node) {
    setChild(node, 1);
  }
  /**
   * Retrieves the TrueExpr child.
   * @return The current node used as the TrueExpr child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="TrueExpr")
  public Expr getTrueExpr() {
    return (Expr) getChild(1);
  }
  /**
   * Retrieves the TrueExpr child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the TrueExpr child.
   * @apilevel low-level
   */
  public Expr getTrueExprNoTransform() {
    return (Expr) getChildNoTransform(1);
  }
  /**
   * Replaces the FalseExpr child.
   * @param node The new node to replace the FalseExpr child.
   * @apilevel high-level
   */
  public void setFalseExpr(Expr node) {
    setChild(node, 2);
  }
  /**
   * Retrieves the FalseExpr child.
   * @return The current node used as the FalseExpr child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="FalseExpr")
  public Expr getFalseExpr() {
    return (Expr) getChild(2);
  }
  /**
   * Retrieves the FalseExpr child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the FalseExpr child.
   * @apilevel low-level
   */
  public Expr getFalseExprNoTransform() {
    return (Expr) getChildNoTransform(2);
  }
  /**
   * @aspect TypeAnalysis
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:365
   */
  private TypeDecl refined_TypeAnalysis_ConditionalExpr_type()
{
    TypeDecl trueType = getTrueExpr().type();
    TypeDecl falseType = getFalseExpr().type();

    if(trueType == falseType) return trueType;

    if(trueType.isNumericType() && falseType.isNumericType()) {
      if(trueType.isByte() && falseType.isShort()) return falseType;
      if(trueType.isShort() && falseType.isByte()) return trueType;
      if((trueType.isByte() || trueType.isShort() || trueType.isChar()) &&
         falseType.isInt() && getFalseExpr().isConstant() && getFalseExpr().representableIn(trueType))
        return trueType;
      if((falseType.isByte() || falseType.isShort() || falseType.isChar()) &&
         trueType.isInt() && getTrueExpr().isConstant() && getTrueExpr().representableIn(falseType))
        return falseType;
      return trueType.binaryNumericPromotion(falseType);
    }
    else if(trueType.isBoolean() && falseType.isBoolean()) {
      return trueType;
    }
    else if(trueType.isReferenceType() && falseType.isNull()) {
      return trueType;
    }
    else if(trueType.isNull() && falseType.isReferenceType()) {
      return falseType;
    }
    else if(trueType.isReferenceType() && falseType.isReferenceType()) {
      if(trueType.assignConversionTo(falseType, null))
        return falseType;
      if(falseType.assignConversionTo(trueType, null))
        return trueType;
      return unknownType();
    }
    else
      return unknownType();
  }
  /**
   * @aspect AutoBoxing
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/AutoBoxing.jrag:181
   */
  private TypeDecl refined_AutoBoxing_ConditionalExpr_type()
{
    TypeDecl trueType = getTrueExpr().type();
    TypeDecl falseType = getFalseExpr().type();
    if(trueType.isBoolean() && falseType.isBoolean()) {
      if(trueType == falseType)
        return trueType;
      if(trueType.isReferenceType())
        return trueType.unboxed();
      return trueType;
    }
    return refined_TypeAnalysis_ConditionalExpr_type();
  }
  /**
   * @apilevel internal
   */
  protected boolean constant_computed = false;
  /**
   * @apilevel internal
   */
  protected Constant constant_value;
/**
 * @apilevel internal
 */
private void constant_reset() {
  constant_computed = false;
  constant_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Constant constant() {
    if(constant_computed) {
      return constant_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    constant_value = type().questionColon(getCondition().constant(), getTrueExpr().constant(),getFalseExpr().constant());
    if (isFinal && num == state().boundariesCrossed) {
      constant_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return constant_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isConstant_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isConstant_value;
/**
 * @apilevel internal
 */
private void isConstant_reset() {
  isConstant_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isConstant() {
    if(isConstant_computed) {
      return isConstant_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isConstant_value = getCondition().isConstant() && getTrueExpr().isConstant() && getFalseExpr().isConstant();
    if (isFinal && num == state().boundariesCrossed) {
      isConstant_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isConstant_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean booleanOperator_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean booleanOperator_value;
/**
 * @apilevel internal
 */
private void booleanOperator_reset() {
  booleanOperator_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean booleanOperator() {
    if(booleanOperator_computed) {
      return booleanOperator_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    booleanOperator_value = getTrueExpr().type().isBoolean() && getFalseExpr().type().isBoolean();
    if (isFinal && num == state().boundariesCrossed) {
      booleanOperator_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return booleanOperator_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isDAafterTrue(Variable v) {
    ASTNode$State state = state();
    boolean isDAafterTrue_Variable_value = (getTrueExpr().isDAafterTrue(v) && getFalseExpr().isDAafterTrue(v)) || isFalse();

    return isDAafterTrue_Variable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isDAafterFalse(Variable v) {
    ASTNode$State state = state();
    boolean isDAafterFalse_Variable_value = (getTrueExpr().isDAafterFalse(v) && getFalseExpr().isDAafterFalse(v)) || isTrue();

    return isDAafterFalse_Variable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isDAafter(Variable v) {
    ASTNode$State state = state();
    boolean isDAafter_Variable_value = booleanOperator() ? isDAafterTrue(v) && isDAafterFalse(v) : getTrueExpr().isDAafter(v) && getFalseExpr().isDAafter(v);

    return isDAafter_Variable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isDUafterTrue(Variable v) {
    ASTNode$State state = state();
    boolean isDUafterTrue_Variable_value = getTrueExpr().isDUafterTrue(v) && getFalseExpr().isDUafterTrue(v);

    return isDUafterTrue_Variable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isDUafterFalse(Variable v) {
    ASTNode$State state = state();
    boolean isDUafterFalse_Variable_value = getTrueExpr().isDUafterFalse(v) && getFalseExpr().isDUafterFalse(v);

    return isDUafterFalse_Variable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isDUafter(Variable v) {
    ASTNode$State state = state();
    boolean isDUafter_Variable_value = booleanOperator() ? isDUafterTrue(v) && isDUafterFalse(v) : getTrueExpr().isDUafter(v) && getFalseExpr().isDUafter(v);

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
    type_value = type_compute();
    if (isFinal && num == state().boundariesCrossed) {
      type_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return type_value;
  }
  /**
   * @apilevel internal
   */
  private TypeDecl type_compute() {
      TypeDecl type = refined_AutoBoxing_ConditionalExpr_type();
      TypeDecl trueType = getTrueExpr().type();
      TypeDecl falseType = getFalseExpr().type();
  
      if(type.isUnknown()) {
        if(!trueType.isReferenceType() && !trueType.boxed().isUnknown())
          trueType = trueType.boxed();
        if(!falseType.isReferenceType() && !falseType.boxed().isUnknown())
          falseType = falseType.boxed();
  
        ArrayList list = new ArrayList();
        list.add(trueType);
        list.add(falseType);
        return type.lookupLUBType(list);
      }
      return type;
    }
  @ASTNodeAnnotation.Attribute
  public boolean modifiedInScope(Variable var) {
    ASTNode$State state = state();
    boolean modifiedInScope_Variable_value = getCondition().modifiedInScope(var) ||
        getTrueExpr().modifiedInScope(var) ||
        getFalseExpr().modifiedInScope(var);

    return modifiedInScope_Variable_value;
  }
  protected java.util.Map compatibleStrictContext_TypeDecl_values;
/**
 * @apilevel internal
 */
private void compatibleStrictContext_TypeDecl_reset() {
  compatibleStrictContext_TypeDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean compatibleStrictContext(TypeDecl type) {
    Object _parameters = type;
    if (compatibleStrictContext_TypeDecl_values == null) compatibleStrictContext_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(compatibleStrictContext_TypeDecl_values.containsKey(_parameters)) {
      return ((Boolean)compatibleStrictContext_TypeDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean compatibleStrictContext_TypeDecl_value = compatibleStrictContext_compute(type);
    if (isFinal && num == state().boundariesCrossed) {
      compatibleStrictContext_TypeDecl_values.put(_parameters, Boolean.valueOf(compatibleStrictContext_TypeDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return compatibleStrictContext_TypeDecl_value;
  }
  /**
   * @apilevel internal
   */
  private boolean compatibleStrictContext_compute(TypeDecl type) {
  		if(!isPolyExpression())
  			return super.compatibleStrictContext(type);
  		else
  			return getTrueExpr().compatibleStrictContext(type) && getFalseExpr().compatibleStrictContext(type);
  	}
  protected java.util.Map compatibleLooseContext_TypeDecl_values;
/**
 * @apilevel internal
 */
private void compatibleLooseContext_TypeDecl_reset() {
  compatibleLooseContext_TypeDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean compatibleLooseContext(TypeDecl type) {
    Object _parameters = type;
    if (compatibleLooseContext_TypeDecl_values == null) compatibleLooseContext_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(compatibleLooseContext_TypeDecl_values.containsKey(_parameters)) {
      return ((Boolean)compatibleLooseContext_TypeDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean compatibleLooseContext_TypeDecl_value = compatibleLooseContext_compute(type);
    if (isFinal && num == state().boundariesCrossed) {
      compatibleLooseContext_TypeDecl_values.put(_parameters, Boolean.valueOf(compatibleLooseContext_TypeDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return compatibleLooseContext_TypeDecl_value;
  }
  /**
   * @apilevel internal
   */
  private boolean compatibleLooseContext_compute(TypeDecl type) {
  		if(!isPolyExpression())
  			return super.compatibleLooseContext(type);
  		else
  			return getTrueExpr().compatibleLooseContext(type) && getFalseExpr().compatibleLooseContext(type);
  	}
  protected java.util.Map pertinentToApplicability_Expr_BodyDecl_int_values;
/**
 * @apilevel internal
 */
private void pertinentToApplicability_Expr_BodyDecl_int_reset() {
  pertinentToApplicability_Expr_BodyDecl_int_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean pertinentToApplicability(Expr access, BodyDecl decl, int argIndex) {
    java.util.List _parameters = new java.util.ArrayList(3);
    _parameters.add(access);
    _parameters.add(decl);
    _parameters.add(Integer.valueOf(argIndex));
    if (pertinentToApplicability_Expr_BodyDecl_int_values == null) pertinentToApplicability_Expr_BodyDecl_int_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(pertinentToApplicability_Expr_BodyDecl_int_values.containsKey(_parameters)) {
      return ((Boolean)pertinentToApplicability_Expr_BodyDecl_int_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean pertinentToApplicability_Expr_BodyDecl_int_value = pertinentToApplicability_compute(access, decl, argIndex);
    if (isFinal && num == state().boundariesCrossed) {
      pertinentToApplicability_Expr_BodyDecl_int_values.put(_parameters, Boolean.valueOf(pertinentToApplicability_Expr_BodyDecl_int_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return pertinentToApplicability_Expr_BodyDecl_int_value;
  }
  /**
   * @apilevel internal
   */
  private boolean pertinentToApplicability_compute(Expr access, BodyDecl decl, int argIndex) {
  		return getFalseExpr().pertinentToApplicability(access, decl, argIndex) && getTrueExpr().pertinentToApplicability(access, decl, argIndex);
  	}
  protected java.util.Map moreSpecificThan_TypeDecl_TypeDecl_values;
/**
 * @apilevel internal
 */
private void moreSpecificThan_TypeDecl_TypeDecl_reset() {
  moreSpecificThan_TypeDecl_TypeDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean moreSpecificThan(TypeDecl type1, TypeDecl type2) {
    java.util.List _parameters = new java.util.ArrayList(2);
    _parameters.add(type1);
    _parameters.add(type2);
    if (moreSpecificThan_TypeDecl_TypeDecl_values == null) moreSpecificThan_TypeDecl_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(moreSpecificThan_TypeDecl_TypeDecl_values.containsKey(_parameters)) {
      return ((Boolean)moreSpecificThan_TypeDecl_TypeDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean moreSpecificThan_TypeDecl_TypeDecl_value = moreSpecificThan_compute(type1, type2);
    if (isFinal && num == state().boundariesCrossed) {
      moreSpecificThan_TypeDecl_TypeDecl_values.put(_parameters, Boolean.valueOf(moreSpecificThan_TypeDecl_TypeDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return moreSpecificThan_TypeDecl_TypeDecl_value;
  }
  /**
   * @apilevel internal
   */
  private boolean moreSpecificThan_compute(TypeDecl type1, TypeDecl type2) {
  		if(super.moreSpecificThan(type1, type2))
  			return true;
  			
  		return getTrueExpr().moreSpecificThan(type1, type2) && getFalseExpr().moreSpecificThan(type1, type2);
  	}
  protected java.util.Map potentiallyCompatible_TypeDecl_BodyDecl_values;
/**
 * @apilevel internal
 */
private void potentiallyCompatible_TypeDecl_BodyDecl_reset() {
  potentiallyCompatible_TypeDecl_BodyDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean potentiallyCompatible(TypeDecl type, BodyDecl candidateDecl) {
    java.util.List _parameters = new java.util.ArrayList(2);
    _parameters.add(type);
    _parameters.add(candidateDecl);
    if (potentiallyCompatible_TypeDecl_BodyDecl_values == null) potentiallyCompatible_TypeDecl_BodyDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(potentiallyCompatible_TypeDecl_BodyDecl_values.containsKey(_parameters)) {
      return ((Boolean)potentiallyCompatible_TypeDecl_BodyDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean potentiallyCompatible_TypeDecl_BodyDecl_value = potentiallyCompatible_compute(type, candidateDecl);
    if (isFinal && num == state().boundariesCrossed) {
      potentiallyCompatible_TypeDecl_BodyDecl_values.put(_parameters, Boolean.valueOf(potentiallyCompatible_TypeDecl_BodyDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return potentiallyCompatible_TypeDecl_BodyDecl_value;
  }
  /**
   * @apilevel internal
   */
  private boolean potentiallyCompatible_compute(TypeDecl type, BodyDecl candidateDecl) {
  		if(!isPolyExpression())
  			return true;
  		if(getTrueExpr().potentiallyCompatible(type, candidateDecl) && 
  				getFalseExpr().potentiallyCompatible(type, candidateDecl))
  			return true;
  		return false;
  	}
  /**
   * @apilevel internal
   */
  protected boolean isBooleanExpression_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isBooleanExpression_value;
/**
 * @apilevel internal
 */
private void isBooleanExpression_reset() {
  isBooleanExpression_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isBooleanExpression() {
    if(isBooleanExpression_computed) {
      return isBooleanExpression_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isBooleanExpression_value = isBooleanExpression_compute();
    if (isFinal && num == state().boundariesCrossed) {
      isBooleanExpression_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isBooleanExpression_value;
  }
  /**
   * @apilevel internal
   */
  private boolean isBooleanExpression_compute() {
  		return isBooleanConditional();
  	}
  /**
   * @apilevel internal
   */
  protected boolean isBooleanConditional_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isBooleanConditional_value;
/**
 * @apilevel internal
 */
private void isBooleanConditional_reset() {
  isBooleanConditional_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isBooleanConditional() {
    if(isBooleanConditional_computed) {
      return isBooleanConditional_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isBooleanConditional_value = isBooleanConditional_compute();
    if (isFinal && num == state().boundariesCrossed) {
      isBooleanConditional_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isBooleanConditional_value;
  }
  /**
   * @apilevel internal
   */
  private boolean isBooleanConditional_compute() {
  		return getTrueExpr().isBooleanExpression() && getFalseExpr().isBooleanExpression();
  	}
  /**
   * @apilevel internal
   */
  protected boolean isNumericExpression_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isNumericExpression_value;
/**
 * @apilevel internal
 */
private void isNumericExpression_reset() {
  isNumericExpression_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isNumericExpression() {
    if(isNumericExpression_computed) {
      return isNumericExpression_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isNumericExpression_value = isNumericExpression_compute();
    if (isFinal && num == state().boundariesCrossed) {
      isNumericExpression_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isNumericExpression_value;
  }
  /**
   * @apilevel internal
   */
  private boolean isNumericExpression_compute() {
  		return isNumericConditional();
  	}
  /**
   * @apilevel internal
   */
  protected boolean isNumericConditional_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isNumericConditional_value;
/**
 * @apilevel internal
 */
private void isNumericConditional_reset() {
  isNumericConditional_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isNumericConditional() {
    if(isNumericConditional_computed) {
      return isNumericConditional_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isNumericConditional_value = isNumericConditional_compute();
    if (isFinal && num == state().boundariesCrossed) {
      isNumericConditional_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isNumericConditional_value;
  }
  /**
   * @apilevel internal
   */
  private boolean isNumericConditional_compute() {
  		return getTrueExpr().isNumericExpression() && getFalseExpr().isNumericExpression();
  	}
  /**
   * @apilevel internal
   */
  protected boolean isReferenceConditional_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isReferenceConditional_value;
/**
 * @apilevel internal
 */
private void isReferenceConditional_reset() {
  isReferenceConditional_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isReferenceConditional() {
    if(isReferenceConditional_computed) {
      return isReferenceConditional_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isReferenceConditional_value = isReferenceConditional_compute();
    if (isFinal && num == state().boundariesCrossed) {
      isReferenceConditional_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isReferenceConditional_value;
  }
  /**
   * @apilevel internal
   */
  private boolean isReferenceConditional_compute() {
  		return !isBooleanConditional() && !isNumericConditional();
  	}
  /**
   * @apilevel internal
   */
  protected boolean isPolyExpression_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isPolyExpression_value;
/**
 * @apilevel internal
 */
private void isPolyExpression_reset() {
  isPolyExpression_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isPolyExpression() {
    if(isPolyExpression_computed) {
      return isPolyExpression_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isPolyExpression_value = isPolyExpression_compute();
    if (isFinal && num == state().boundariesCrossed) {
      isPolyExpression_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isPolyExpression_value;
  }
  /**
   * @apilevel internal
   */
  private boolean isPolyExpression_compute() {
  		return isReferenceConditional() && (assignmentContext() || invocationContext());
  	}
  protected java.util.Map assignConversionTo_TypeDecl_values;
/**
 * @apilevel internal
 */
private void assignConversionTo_TypeDecl_reset() {
  assignConversionTo_TypeDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean assignConversionTo(TypeDecl type) {
    Object _parameters = type;
    if (assignConversionTo_TypeDecl_values == null) assignConversionTo_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(assignConversionTo_TypeDecl_values.containsKey(_parameters)) {
      return ((Boolean)assignConversionTo_TypeDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean assignConversionTo_TypeDecl_value = assignConversionTo_compute(type);
    if (isFinal && num == state().boundariesCrossed) {
      assignConversionTo_TypeDecl_values.put(_parameters, Boolean.valueOf(assignConversionTo_TypeDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return assignConversionTo_TypeDecl_value;
  }
  /**
   * @apilevel internal
   */
  private boolean assignConversionTo_compute(TypeDecl type) {
  		if(!isPolyExpression())
  			return type().assignConversionTo(type, this);
  		else
  			return getTrueExpr().assignConversionTo(type) && getFalseExpr().assignConversionTo(type);
  	}
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:402
   * @apilevel internal
   */
  public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
    if (caller == getFalseExprNoTransform()) {
      return getCondition().isDAafterFalse(v);
    }
    else if (caller == getTrueExprNoTransform()) {
      return getCondition().isDAafterTrue(v);
    }
    else if (caller == getConditionNoTransform()) {
      return isDAbefore(v);
    }
    else {
      return getParent().Define_boolean_isDAbefore(this, caller, v);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:858
   * @apilevel internal
   */
  public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
    if (caller == getFalseExprNoTransform()) {
      return getCondition().isDUafterFalse(v);
    }
    else if (caller == getTrueExprNoTransform()) {
      return getCondition().isDUafterTrue(v);
    }
    else if (caller == getConditionNoTransform()) {
      return isDUbefore(v);
    }
    else {
      return getParent().Define_boolean_isDUbefore(this, caller, v);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:35
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_targetType(ASTNode caller, ASTNode child) {
    if (caller == getFalseExprNoTransform()) {
      return targetType();
    }
    else if (caller == getTrueExprNoTransform()) {
      return targetType();
    }
    else {
      return getParent().Define_TypeDecl_targetType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:282
   * @apilevel internal
   */
  public boolean Define_boolean_assignmentContext(ASTNode caller, ASTNode child) {
    if (caller == getFalseExprNoTransform()) {
      return isPolyExpression() ? assignmentContext() : false;
    }
    else if (caller == getTrueExprNoTransform()) {
      return isPolyExpression() ? assignmentContext() : false;
    }
    else if (caller == getConditionNoTransform()) {
      return false;
    }
    else {
      return getParent().Define_boolean_assignmentContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:283
   * @apilevel internal
   */
  public boolean Define_boolean_invocationContext(ASTNode caller, ASTNode child) {
    if (caller == getFalseExprNoTransform()) {
      return isPolyExpression() ? invocationContext() : false;
    }
    else if (caller == getTrueExprNoTransform()) {
      return isPolyExpression() ? invocationContext() : false;
    }
    else if (caller == getConditionNoTransform()) {
      return false;
    }
    else {
      return getParent().Define_boolean_invocationContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:285
   * @apilevel internal
   */
  public boolean Define_boolean_castContext(ASTNode caller, ASTNode child) {
    if (caller == getFalseExprNoTransform()) {
      return false;
    }
    else if (caller == getTrueExprNoTransform()) {
      return false;
    }
    else if (caller == getConditionNoTransform()) {
      return false;
    }
    else {
      return getParent().Define_boolean_castContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:287
   * @apilevel internal
   */
  public boolean Define_boolean_numericContext(ASTNode caller, ASTNode child) {
    if (caller == getFalseExprNoTransform()) {
      return false;
    }
    else if (caller == getTrueExprNoTransform()) {
      return false;
    }
    else if (caller == getConditionNoTransform()) {
      return false;
    }
    else {
      return getParent().Define_boolean_numericContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:286
   * @apilevel internal
   */
  public boolean Define_boolean_stringContext(ASTNode caller, ASTNode child) {
    if (caller == getFalseExprNoTransform()) {
      return false;
    }
    else if (caller == getTrueExprNoTransform()) {
      return false;
    }
    else if (caller == getConditionNoTransform()) {
      return false;
    }
    else {
      return getParent().Define_boolean_stringContext(this, caller);
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
