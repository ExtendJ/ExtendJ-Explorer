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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/grammar/ConstructorReference.ast:1
 * @production ConstructorReference : {@link Expr} ::= <span class="component">TypeAccess:{@link Access}</span>;

 */
public abstract class ConstructorReference extends Expr implements Cloneable {
  /**
   * @aspect TypeCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TypeCheck.jrag:335
   */
  public void typeCheck() {
		// 15.13.1
		if(!assignmentContext() && !castContext() && !invocationContext()) {
			error("Constructor references must target a functional interface");
			return;
		}
		
		// This means there was an error in the overload resolution, will be reported elsewhere
		if(invocationContext() && targetType() == unknownType())
			return;

		if(!targetType().isFunctionalInterface()) {
			error("Constructor references must target a functional interface");
			return;
		}	
		InterfaceDecl iDecl = targetInterface();
		
		if(!iDecl.isFunctional()) {
			error("Interface " + iDecl.typeName() + " is not functional and can therefore not be targeted by a constructor reference");
			return;
		}
		
		FunctionDescriptor f = iDecl.functionDescriptor();
		
		if(this instanceof ClassReference) {
			ClassReference ref = (ClassReference)this;
			ConstructorDecl decl = ref.targetConstructor(f);
			if(unknownConstructor() == decl) {
				error("No constructor for the type " + getTypeAccess().type().typeName() 
				     + " that is compatible with the method " + f.method.fullSignature() + 
				     " in the interface " + iDecl.typeName() + " was found");
			}
			if(!f.method.type().isVoid()) {
				// 15.13.1
				TypeDecl returnType = ref.syntheticInstanceExpr(f).type();
				if(!returnType.assignConversionTo(f.method.type(), null))
					error("Return type of method " + f.method.fullSignature()
						+ " in interface " + iDecl.typeName() + " is not compatible with referenced constructor" 
						+ " which has return type: " + returnType.typeName());
			}
			for(int i = 0; i < decl.getNumException(); i++) {
				TypeDecl exception = decl.getException(i).type();
				/* This is supposed to be unchecked exception, but the attribute name
				   is currently inverted! */
				if(exception.isCheckedException())
					continue;
				
				boolean legalException = false;
				for(TypeDecl descriptorThrows : iDecl.functionDescriptor().throwsList) {
					if(exception.strictSubtype(descriptorThrows)) {
						legalException = true;
						break;
					}
				}
				if(!legalException) {
					// 15.13.1
					error("Referenced constructor " + decl.name() + " throws unhandled exception type " + exception.typeName()); 
				}
			}
			ref.syntheticInstanceExpr(f).typeCheck();
			
		}
		
		else {
			ArrayReference ref = (ArrayReference)this;
			if(f.method.getNumParameter() != 1) {
				error("Array reference not compatible with method " + f.method.fullSignature() 
						+ " in interface " + iDecl.typeName() + ", should have a single parameter of type int");
				return;
			}
			if(!f.method.getParameter(0).type().assignConversionTo(iDecl.typeInt(), null)) {
				error("Array reference not compatible with method " + f.method.fullSignature() 
						+ " in interface " + iDecl.typeName() + ", should have a single parameter of type int");
				return;
			}
			if(!f.method.type().isVoid()) {
				if(!getTypeAccess().type().assignConversionTo(f.method.type(), null)) {
					error("Return type " + f.method.type().typeName() + " of method " + 
							f.method.fullSignature() + " in interface " + iDecl.typeName() + 
							" is not compatible with the array reference type " + getTypeAccess().type().typeName());
				}
			}
		}
	}
  /**
   * @declaredat ASTNode:1
   */
  public ConstructorReference() {
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
  public ConstructorReference(Access p0) {
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
    compatibleStrictContext_TypeDecl_reset();
    compatibleLooseContext_TypeDecl_reset();
    pertinentToApplicability_Expr_BodyDecl_int_reset();
    moreSpecificThan_TypeDecl_TypeDecl_reset();
    potentiallyCompatible_TypeDecl_BodyDecl_reset();
    isPolyExpression_reset();
    assignConversionTo_TypeDecl_reset();
    targetInterface_reset();
    type_reset();
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
  public ConstructorReference clone() throws CloneNotSupportedException {
    ConstructorReference node = (ConstructorReference) super.clone();
    return node;
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @deprecated Please use emitTreeCopy or emitTreeCopyNoTransform instead
   * @declaredat ASTNode:69
   */
  public abstract ConstructorReference fullCopy();
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:76
   */
  public abstract ConstructorReference treeCopyNoTransform();
  /**
   * Create a deep copy of the AST subtree at this node.
   * The subtree of this node is traversed to trigger rewrites before copy.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:84
   */
  public abstract ConstructorReference treeCopy();
  /**
   * Replaces the TypeAccess child.
   * @param node The new node to replace the TypeAccess child.
   * @apilevel high-level
   */
  public void setTypeAccess(Access node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the TypeAccess child.
   * @return The current node used as the TypeAccess child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="TypeAccess")
  public Access getTypeAccess() {
    return (Access) getChild(0);
  }
  /**
   * Retrieves the TypeAccess child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the TypeAccess child.
   * @apilevel low-level
   */
  public Access getTypeAccessNoTransform() {
    return (Access) getChildNoTransform(0);
  }
  /**
   * @attribute syn
   * @aspect ConstructorReference
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/ConstructorReference.jrag:67
   */
  @ASTNodeAnnotation.Attribute
  public abstract boolean congruentTo(FunctionDescriptor f);
  /**
   * @attribute syn
   * @aspect ConstructorReference
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/ConstructorReference.jrag:116
   */
  @ASTNodeAnnotation.Attribute
  public abstract boolean isExact();
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
  		if(!isExact())
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
  
  		if(!isExact())
  			return false;
  		
  		FunctionDescriptor f1 = iDecl1.functionDescriptor();
  		FunctionDescriptor f2 = iDecl2.functionDescriptor();
  		
  		if(f1.method.arity() != f2.method.arity())
  			return false;
  		
  		for(int i = 0; i < f1.method.getNumParameter(); i++) {
  			if(f1.method.getParameter(i).type() != f2.method.getParameter(i).type())
  				return false;
  		}
  		
  		// First bullet
  		if(f2.method.type().isVoid())
  			return true;
  		
  		// Second bullet
  		if(f1.method.type().instanceOf(f2.method.type()))
  			return true;
  			
  		// Third bullet
  		if(f1.method.type().isPrimitiveType() && f2.method.type().isReferenceType()) {
  			// A constructor can never have primitive return type
  			return false;
  		}
  		
  		// Fourth bullet
  		if(f1.method.type().isReferenceType() && f2.method.type().isPrimitiveType()) {
  			// A constructor always have reference return type
  			return true;
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
  		// 15.13.1
  		if(!assignmentContext() && !castContext() && !invocationContext()) 
  			return unknownType();
  		if(targetInterface() == null)
  			return unknownType();
  			
  		InterfaceDecl iDecl = targetInterface();
  		if(!iDecl.isFunctional()) {
  			return unknownType();
  		}
  		
  		return iDecl;
  	}
  /**
   * @attribute inh
   * @aspect ConstructorReference
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/ConstructorReference.jrag:29
   */
  @ASTNodeAnnotation.Attribute
  public ConstructorDecl unknownConstructor() {
    ASTNode$State state = state();
    ConstructorDecl unknownConstructor_value = getParent().Define_ConstructorDecl_unknownConstructor(this, null);

    return unknownConstructor_value;
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/ConstructorReference.jrag:65
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
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:404
   * @apilevel internal
   */
  public boolean Define_boolean_assignmentContext(ASTNode caller, ASTNode child) {
    if (caller == getTypeAccessNoTransform()) {
      return false;
    }
    else {
      return getParent().Define_boolean_assignmentContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:405
   * @apilevel internal
   */
  public boolean Define_boolean_invocationContext(ASTNode caller, ASTNode child) {
    if (caller == getTypeAccessNoTransform()) {
      return false;
    }
    else {
      return getParent().Define_boolean_invocationContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:406
   * @apilevel internal
   */
  public boolean Define_boolean_castContext(ASTNode caller, ASTNode child) {
    if (caller == getTypeAccessNoTransform()) {
      return false;
    }
    else {
      return getParent().Define_boolean_castContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:407
   * @apilevel internal
   */
  public boolean Define_boolean_stringContext(ASTNode caller, ASTNode child) {
    if (caller == getTypeAccessNoTransform()) {
      return false;
    }
    else {
      return getParent().Define_boolean_stringContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:408
   * @apilevel internal
   */
  public boolean Define_boolean_numericContext(ASTNode caller, ASTNode child) {
    if (caller == getTypeAccessNoTransform()) {
      return false;
    }
    else {
      return getParent().Define_boolean_numericContext(this, caller);
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
