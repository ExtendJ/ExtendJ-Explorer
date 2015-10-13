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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/grammar/Lambda.ast:1
 * @production LambdaExpr : {@link Expr} ::= <span class="component">{@link LambdaParameters}</span> <span class="component">{@link LambdaBody}</span>;

 */
public class LambdaExpr extends Expr implements Cloneable, VariableScope {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/PrettyPrint.jadd:98
   */
  public void prettyPrint(StringBuffer sb) {
		getLambdaParameters().prettyPrint(sb);
		sb.append(" -> ");
		getLambdaBody().prettyPrint(sb);
	}
  /**
   * @aspect TypeCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TypeCheck.jrag:109
   */
  public void typeCheck() {
		if(!assignmentContext() && !castContext() && !invocationContext()) {
			// 15.27
			error("Lambda expressions must target a functional interface");
			return;
		}

		// This means there was an error in the overload resolution, will be reported elsewhere
		if(invocationContext() && targetType() == unknownType())
			return;

		if(!targetType().isFunctionalInterface()) {
			// 15.27
			error("Lambda expressions must target a functional interface");
			return;
		}	
		
		InterfaceDecl iDecl = targetInterface();

		if(!iDecl.isFunctional()) {
			// 15.27
			error("Interface " + iDecl.typeName() + " is not functional and can therefore not be targeted by a lambda expression");
			return;
		}
		
		FunctionDescriptor f = iDecl.functionDescriptor();
		
		if(f.isGeneric()) {
			// 15.27
			error("Illegal lambda expression: Method " + iDecl.functionDescriptor().method.name() + 
				" in interface " + iDecl.typeName() + " is generic");
			return;
		}
		
		if(!getLambdaParameters().congruentTo(f))
			error("Lambda expression parameters incompatible with parameters in method " + f.method.name()
					+ " in interface " + iDecl.typeName());
		
		if(getLambdaBody() instanceof ExprLambdaBody) {
			ExprLambdaBody exprBody = (ExprLambdaBody)getLambdaBody();
			if(f.method.type().isVoid()) {
				if(!exprBody.getExpr().stmtCompatible())
					error("Lambda expression body must be a statement expression, because the method "
					      + f.method.name() + " in interface " + iDecl.typeName() + " has return type void");
			}
			else {
				if(!exprBody.getExpr().type().assignConversionTo(f.method.type(), exprBody.getExpr())) 
					error("Lambda expression body is not compatible with the return type " + f.method.type().typeName() +
							" in method " + f.method.name() + " in interface " + iDecl.typeName());
			}
		}
		
		else {
			BlockLambdaBody blockBody = (BlockLambdaBody)getLambdaBody();
			if(f.method.type().isVoid()) {
				if(!blockBody.voidCompatible())
					error("Lambda expression body is not allowed to return a value, because the method "
					      + f.method.name() + " in interface " + iDecl.typeName() + " has return type void");
			}
			else if(!blockBody.valueCompatible()) {
				error("Lambda expression body must not complete normally or contain empty return statments, because the method "
				       + f.method.name() + " in interface " + iDecl.typeName() + " has a return type which is non-void");
			}
		}	
	}
  /**
   * @declaredat ASTNode:1
   */
  public LambdaExpr() {
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
  public LambdaExpr(LambdaParameters p0, LambdaBody p1) {
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
    arity_reset();
    numParameters_reset();
    isImplicit_reset();
    isExplicit_reset();
    congruentTo_FunctionDescriptor_reset();
    compatibleStrictContext_TypeDecl_reset();
    compatibleLooseContext_TypeDecl_reset();
    pertinentToApplicability_Expr_BodyDecl_int_reset();
    moreSpecificThan_TypeDecl_TypeDecl_reset();
    potentiallyCompatible_TypeDecl_BodyDecl_reset();
    isPolyExpression_reset();
    assignConversionTo_TypeDecl_reset();
    targetInterface_reset();
    type_reset();
    enclosingLambda_reset();
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
  public LambdaExpr clone() throws CloneNotSupportedException {
    LambdaExpr node = (LambdaExpr) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:72
   */
  public LambdaExpr copy() {
    try {
      LambdaExpr node = (LambdaExpr) clone();
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
  public LambdaExpr fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:100
   */
  public LambdaExpr treeCopyNoTransform() {
    LambdaExpr tree = (LambdaExpr) copy();
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
   * @declaredat ASTNode:120
   */
  public LambdaExpr treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:127
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) ;    
  }
  /**
   * Replaces the LambdaParameters child.
   * @param node The new node to replace the LambdaParameters child.
   * @apilevel high-level
   */
  public void setLambdaParameters(LambdaParameters node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the LambdaParameters child.
   * @return The current node used as the LambdaParameters child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="LambdaParameters")
  public LambdaParameters getLambdaParameters() {
    return (LambdaParameters) getChild(0);
  }
  /**
   * Retrieves the LambdaParameters child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the LambdaParameters child.
   * @apilevel low-level
   */
  public LambdaParameters getLambdaParametersNoTransform() {
    return (LambdaParameters) getChildNoTransform(0);
  }
  /**
   * Replaces the LambdaBody child.
   * @param node The new node to replace the LambdaBody child.
   * @apilevel high-level
   */
  public void setLambdaBody(LambdaBody node) {
    setChild(node, 1);
  }
  /**
   * Retrieves the LambdaBody child.
   * @return The current node used as the LambdaBody child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="LambdaBody")
  public LambdaBody getLambdaBody() {
    return (LambdaBody) getChild(1);
  }
  /**
   * Retrieves the LambdaBody child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the LambdaBody child.
   * @apilevel low-level
   */
  public LambdaBody getLambdaBodyNoTransform() {
    return (LambdaBody) getChildNoTransform(1);
  }
  @ASTNodeAnnotation.Attribute
  public boolean modifiedInScope(Variable var) {
    ASTNode$State state = state();
    boolean modifiedInScope_Variable_value = getLambdaBody().modifiedInScope(var);

    return modifiedInScope_Variable_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean arity_computed = false;
  /**
   * @apilevel internal
   */
  protected int arity_value;
/**
 * @apilevel internal
 */
private void arity_reset() {
  arity_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public int arity() {
    if(arity_computed) {
      return arity_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    arity_value = numParameters();
    if (isFinal && num == state().boundariesCrossed) {
      arity_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return arity_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean numParameters_computed = false;
  /**
   * @apilevel internal
   */
  protected int numParameters_value;
/**
 * @apilevel internal
 */
private void numParameters_reset() {
  numParameters_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public int numParameters() {
    if(numParameters_computed) {
      return numParameters_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    numParameters_value = getLambdaParameters().numParameters();
    if (isFinal && num == state().boundariesCrossed) {
      numParameters_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return numParameters_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isImplicit_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isImplicit_value;
/**
 * @apilevel internal
 */
private void isImplicit_reset() {
  isImplicit_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isImplicit() {
    if(isImplicit_computed) {
      return isImplicit_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isImplicit_value = getLambdaParameters() instanceof InferredLambdaParameters;
    if (isFinal && num == state().boundariesCrossed) {
      isImplicit_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isImplicit_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isExplicit_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isExplicit_value;
/**
 * @apilevel internal
 */
private void isExplicit_reset() {
  isExplicit_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isExplicit() {
    if(isExplicit_computed) {
      return isExplicit_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isExplicit_value = !isImplicit();
    if (isFinal && num == state().boundariesCrossed) {
      isExplicit_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isExplicit_value;
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
  		if(f.isGeneric())
  			return false;
  		if(!getLambdaParameters().congruentTo(f))
  			return false;
  		if(!getLambdaBody().congruentTo(f))
  			return false;
  		return true;
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
  		if(!type.isFunctionalInterface())
  			return false;
  		InterfaceDecl iDecl = (InterfaceDecl)type;
  	
  		return congruentTo(iDecl.functionDescriptor());
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
  		return compatibleStrictContext(type);
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
  		if(isImplicit())
  			return false;
  		if(decl instanceof GenericMethodDecl && !(access instanceof ParMethodAccess) 
  				&& ((GenericMethodDecl)decl).getParameter(argIndex).type().isTypeVariable()) {
  			GenericMethodDecl genericDecl = (GenericMethodDecl)decl;
  			TypeVariable typeVar = (TypeVariable)genericDecl.getParameter(argIndex).type();
  			for(int i = 0; i < genericDecl.getNumTypeParameter(); i++) {
  				if(typeVar == genericDecl.getTypeParameter(i))
  					return false;
  			}
  		}
  		else if(decl instanceof GenericConstructorDecl && !(access instanceof ParConstructorAccess) 
  				&& !(access instanceof ParSuperConstructorAccess) && !(access instanceof ParClassInstanceExpr) 
  				&& ((GenericConstructorDecl)decl).getParameter(argIndex).type().isTypeVariable()) {
  			GenericConstructorDecl genericDecl = (GenericConstructorDecl)decl;
  			TypeVariable typeVar = (TypeVariable)genericDecl.getParameter(argIndex).type();
  			for(int i = 0; i < genericDecl.getNumTypeParameter(); i++) {
  				if(typeVar == genericDecl.getTypeParameter(i))
  					return false;
  			}	
  		}
  		if(getLambdaBody() instanceof ExprLambdaBody) {
  			ExprLambdaBody exprBody = (ExprLambdaBody)getLambdaBody();
  			if(!exprBody.getExpr().pertinentToApplicability(access, decl, argIndex))
  				return false;
  		}
  		else {
  			BlockLambdaBody blockBody = (BlockLambdaBody)getLambdaBody();
  			ArrayList<ReturnStmt> returnList = blockBody.lambdaReturns();
  			for(ReturnStmt returnStmt : returnList) {
  				if(returnStmt.hasResult() && !returnStmt.getResult().pertinentToApplicability(access, decl, argIndex))
  					return false;
  			}
  		}
  		
  		return true;
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
  		if(!type1.isFunctionalInterface() || !type2.isFunctionalInterface())
  			return false;
  		if(type2.instanceOf(type1))
  			return false;
  		InterfaceDecl iDecl1 = (InterfaceDecl)type1;
  		InterfaceDecl iDecl2 = (InterfaceDecl)type2;
  
  		if(isImplicit())
  			return false;
  		
  		FunctionDescriptor f1 = iDecl1.functionDescriptor();
  		FunctionDescriptor f2 = iDecl2.functionDescriptor();
  		
  		// First bullet
  		if(f2.method.type().isVoid())
  			return true;
  			
  		// Second bullet
  		if(f1.method.type().instanceOf(f2.method.type()))
  			return true;
  			
  		// Third bullet
  		if(f1.method.type().isFunctionalInterface() && f2.method.type().isFunctionalInterface()) {
  			if(getLambdaBody().isBlockBody()) {
  				BlockLambdaBody blockBody = (BlockLambdaBody)getLambdaBody();
  				boolean allMoreSpecific = true;
  				ArrayList<ReturnStmt> returnList = blockBody.lambdaReturns();
  				for(ReturnStmt returnStmt : returnList) {
  					if(returnStmt.hasResult() && !returnStmt.getResult().moreSpecificThan(f1.method.type(), f2.method.type())) {
  						allMoreSpecific = false;
  						break;
  					}
  				}
  				return allMoreSpecific;
  			}
  			else {
  				ExprLambdaBody exprBody = (ExprLambdaBody)getLambdaBody();
  				return exprBody.getExpr().moreSpecificThan(f1.method.type(), f2.method.type());
  			}
  		}
  		
  		// Fourth bullet
  		if(f1.method.type().isPrimitiveType() && f2.method.type().isReferenceType()) {
  			if(getLambdaBody().isBlockBody()) {
  				BlockLambdaBody blockBody = (BlockLambdaBody)getLambdaBody();
  				boolean allPrimitive = true;
  				ArrayList<ReturnStmt> returnList = blockBody.lambdaReturns();
  				for(ReturnStmt returnStmt : returnList) {
  					if(returnStmt.hasResult() && returnStmt.getResult().isPolyExpression()) {
  						allPrimitive = false;
  						break;
  					}
  					else if(returnStmt.hasResult() && !returnStmt.getResult().type().isPrimitiveType()) {
  						allPrimitive = false;
  						break;
  					}
  				}
  				return allPrimitive;
  			}
  			else {
  				ExprLambdaBody exprBody = (ExprLambdaBody)getLambdaBody();
  				if(exprBody.getExpr().isPolyExpression())
  					return false;
  				return exprBody.getExpr().type().isPrimitiveType();
  			}
  		}
  		
  		// Fifth bullet
  		if(f1.method.type().isReferenceType() && f2.method.type().isPrimitiveType()) {
  			if(getLambdaBody().isBlockBody()) {
  				BlockLambdaBody blockBody = (BlockLambdaBody)getLambdaBody();
  				boolean allReference = true;
  				ArrayList<ReturnStmt> returnList = blockBody.lambdaReturns();
  				for(ReturnStmt returnStmt : returnList) {
  					if(returnStmt.hasResult() && !returnStmt.getResult().isPolyExpression() 
  							&& !returnStmt.getResult().type().isReferenceType()) {
  						allReference = false;
  						break;
  					}
  				}
  				return allReference;
  			}
  			else {
  				ExprLambdaBody exprBody = (ExprLambdaBody)getLambdaBody();
  				if(exprBody.getExpr().isPolyExpression())
  					return true;
  				return exprBody.getExpr().type().isReferenceType();
  			}
  		}
  		
  		return false;
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
  		if(type.isTypeVariable()) {
  			if(candidateDecl instanceof GenericMethodDecl) {
  				GenericMethodDecl genericMethod = (GenericMethodDecl)candidateDecl;
  				boolean foundTypeVariable = false;
  				for(int i = 0; i < genericMethod.getNumTypeParameter(); i++) {
  					if(type == genericMethod.getTypeParameter(i)) {
  						foundTypeVariable = true;
  						break;
  					}
  				}
  				return foundTypeVariable;
  			}
  			else if(candidateDecl instanceof GenericConstructorDecl) {
  				GenericConstructorDecl genericConstructor = (GenericConstructorDecl)candidateDecl;
  				boolean foundTypeVariable = false;
  				for(int i = 0; i < genericConstructor.getNumTypeParameter(); i++) {
  					if(type == genericConstructor.getTypeParameter(i)) {
  						foundTypeVariable = true;
  						break;
  					}
  				}
  				return foundTypeVariable;
  			}
  			else
  				return false;
  		}
  		
  		if(!type.isFunctionalInterface())
  			return false;
  		InterfaceDecl iDecl = (InterfaceDecl)type;
  
  		if(arity() != iDecl.functionDescriptor().method.arity())
  			return false;
  		if(iDecl.functionDescriptor().method.type().isVoid()) {
  			if(getLambdaBody().isExprBody()) {
  				ExprLambdaBody exprBody = (ExprLambdaBody)getLambdaBody();
  				if(!exprBody.getExpr().stmtCompatible())
  					return false;
  			}
  			else {
  				BlockLambdaBody blockBody = (BlockLambdaBody)getLambdaBody();
  				if(!blockBody.voidCompatible())
  					return false;
  			}
  		}
  		else {
  			if(getLambdaBody().isBlockBody()) {
  				BlockLambdaBody blockBody = (BlockLambdaBody)getLambdaBody();
  				if(!blockBody.valueCompatible()) 
  					return false;
  			}
  		}
  		return true;
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
    isPolyExpression_value = true;
    if (isFinal && num == state().boundariesCrossed) {
      isPolyExpression_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isPolyExpression_value;
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
  		if(!type.isFunctionalInterface())
  			return false;
  		FunctionDescriptor f = ((InterfaceDecl)type).functionDescriptor();
  		return congruentTo(f);
  	}
  /**
   * @apilevel internal
   */
  protected boolean targetInterface_computed = false;
  /**
   * @apilevel internal
   */
  protected InterfaceDecl targetInterface_value;
/**
 * @apilevel internal
 */
private void targetInterface_reset() {
  targetInterface_computed = false;
  targetInterface_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public InterfaceDecl targetInterface() {
    if(targetInterface_computed) {
      return targetInterface_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    targetInterface_value = targetInterface_compute();
    if (isFinal && num == state().boundariesCrossed) {
      targetInterface_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return targetInterface_value;
  }
  /**
   * @apilevel internal
   */
  private InterfaceDecl targetInterface_compute() {
  		if(targetType().isNull())
  			return null;
  		else if(!(targetType() instanceof InterfaceDecl))
  			return null;
  		else 
  			return (InterfaceDecl)targetType();
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
  		// 15.27.3
  		if(!assignmentContext() && !castContext() && !invocationContext()) 
  			return unknownType();
  		if(targetInterface() == null)
  			return unknownType();
  		
  		InterfaceDecl iDecl = targetInterface();
  		if(!iDecl.isFunctional()) {
  			return unknownType();
  		}
  		if(congruentTo(iDecl.functionDescriptor()))
  			return iDecl;
  		else
  			return unknownType();
  	}
  /**
   * @attribute inh
   * @aspect EnclosingLambda
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/EnclosingLambda.jrag:38
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
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/EffectivelyFinal.jrag:34
   * @apilevel internal
   */
  public boolean Define_boolean_inhModifiedInScope(ASTNode caller, ASTNode child, Variable var) {
    if (caller == getLambdaParametersNoTransform()) {
      return modifiedInScope(var);
    }
    else {
      return getParent().Define_boolean_inhModifiedInScope(this, caller, var);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/EnclosingLambda.jrag:44
   * @apilevel internal
   */
  public LambdaExpr Define_LambdaExpr_enclosingLambda(ASTNode caller, ASTNode child) {
    if (caller == getLambdaParametersNoTransform()) {
      return this;
    }
    else if (caller == getLambdaBodyNoTransform()) {
      return this;
    }
    else {
      return getParent().Define_LambdaExpr_enclosingLambda(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/LambdaExpr.jrag:155
   * @apilevel internal
   */
  public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
    if (caller == getLambdaBodyNoTransform()){
		/* Supposed to be uncheckedexception, but apparently
			the properties have been implemented in inverse in 
			JastAddJ */
		if(exceptionType.isCheckedException())
			return true;
		InterfaceDecl iDecl = targetInterface();
		if(iDecl == null)
			return false;
		else if(!iDecl.isFunctional())
			return false;
		for(TypeDecl exception : iDecl.functionDescriptor().throwsList) {
			if(exceptionType.strictSubtype(exception))
				return true;
		}
		return false;
	}
    else {
      return getParent().Define_boolean_handlesException(this, caller, exceptionType);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/LookupVariable.jrag:52
   * @apilevel internal
   */
  public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
    if (caller == getLambdaBodyNoTransform()){ 	
		if(getLambdaParameters() instanceof DeclaredLambdaParameters) {
			SimpleSet decls = ((DeclaredLambdaParameters)getLambdaParameters()).parameterDeclaration(name);
			if(!decls.isEmpty()) {
				return decls;
			}
		}
		else if(getLambdaParameters() instanceof InferredLambdaParameters) {
			SimpleSet decls = ((InferredLambdaParameters)getLambdaParameters()).parameterDeclaration(name);
			if(!decls.isEmpty()) {
				return decls;
			}
		}		
		return lookupVariable(name);
 	}
    else {
      return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/NameCheck.jrag:35
   * @apilevel internal
   */
  public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
    if (caller == getLambdaBodyNoTransform()) {
      return this;
    }
    else if (caller == getLambdaParametersNoTransform()) {
      return this;
    }
    else {
      return getParent().Define_VariableScope_outerScope(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/Tmp.jrag:29
   * @apilevel internal
   */
  public FinallyHost Define_FinallyHost_enclosingFinally(ASTNode caller, ASTNode child, Stmt branch) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return null;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/Tmp.jrag:30
   * @apilevel internal
   */
  public Stmt Define_Stmt_branchTarget(ASTNode caller, ASTNode child, Stmt branch) {
     {
      int childIndex = this.getIndexOfChild(caller);
{
		throw new Error("Found no branch targets for " + branch.getClass().getName());
	}
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/Tmp.jrag:33
   * @apilevel internal
   */
  public SimpleSet Define_SimpleSet_otherLocalClassDecls(ASTNode caller, ASTNode child, String name) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return SimpleSet.emptySet;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TypeCheck.jrag:33
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_unknownType(ASTNode caller, ASTNode child) {
    if (caller == getLambdaBodyNoTransform()) {
      return unknownType();
    }
    else if (caller == getLambdaParametersNoTransform()) {
      return unknownType();
    }
    else {
      return getParent().Define_TypeDecl_unknownType(this, caller);
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
