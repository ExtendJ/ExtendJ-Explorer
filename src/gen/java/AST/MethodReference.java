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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/grammar/MethodReference.ast:1
 * @production MethodReference : {@link Expr} ::= <span class="component">TypeArgument:{@link Access}*</span> <span class="component">&lt;ID:String&gt;</span>;

 */
public abstract class MethodReference extends Expr implements Cloneable {
  /**
   * @aspect Java8NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/NameCheck.jrag:490
   */
  public void nameCheck() {
		for(int i = 0; i < getNumTypeArgument(); i++) {
			if(getTypeArgument(i) instanceof AbstractWildcard) {
				error("Wildcard not allowed in method reference type argument lists");
				break;
			}
		}
	}
  /**
   * @aspect TypeCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TypeCheck.jrag:206
   */
  public void typeCheck() {
		// 15.13.1
		if(!assignmentContext() && !castContext() && !invocationContext()) {
			error("Method references must target a functional interface");
			return;
		}
		
		// This means there was an error in the overload resolution, will be reported elsewhere
		if(invocationContext() && targetType() == unknownType())
			return;

		if(!targetType().isFunctionalInterface()) {
			error("Method references must target a functional interface");
			return;
		}	
		
		InterfaceDecl iDecl = targetInterface();
		
		if(!iDecl.isFunctional()) {
			error("Interface " + iDecl.typeName() + " is not functional and can therefore not be targeted by a method reference");
			return;
		}
		
		MethodDecl found = null;
		FunctionDescriptor f = iDecl.functionDescriptor();
		// Lookup method here and check that one most specific can be found	
		if(this instanceof ExprMethodReference) {
			ExprMethodReference ref = (ExprMethodReference)this;
			found = ref.targetMethod(f);
			if(unknownMethod() == found) {
				// 15.13.1
				error("No method " + name() + " that is compatible with the method " + iDecl.functionDescriptor().method.fullSignature() + 
				 " in the interface " + iDecl.typeName() + " was found");
			}
			else if(found.isStatic()) {
				error("The method " + found.fullSignature() + " in type " + found.hostType().typeName() + 
						" must be accessed in a static way");
			}
			
			// 15.13.2
			else if(ref.getExpr() instanceof Access && ((Access)ref.getExpr()).lastAccess() instanceof SuperAccess) {
				if(found.isAbstract()) {
					error("Cannot directly invoke the abstract method " + found.fullSignature() + 
							" in type " + found.hostType().typeName());
				}
				
				SuperAccess superAccess = (SuperAccess)((Access)ref.getExpr()).lastAccess();
				if(superAccess.isQualified() && superAccess.decl() instanceof InterfaceDecl) {
					if(hostType().isClassDecl()) {
						ClassDecl classDecl = (ClassDecl)hostType();
						if(classDecl.hasOverridingMethodInSuper(found)) 
							error("Cannot make a super reference to method " + found.fullSignature() +
									", there is a more specific override");
					}	
					else if(hostType().isInterfaceDecl()) {
						InterfaceDecl interfaceDecl = (InterfaceDecl)hostType();
						if(interfaceDecl.hasOverridingMethodInSuper(found)) 
							error("Cannot make a super reference to method " + found.fullSignature() +
									", there is a more specific override");
					}
				}
				
			}
		}
		else if(this instanceof TypeMethodReference) {
			TypeMethodReference ref = (TypeMethodReference)this;
			MethodDecl staticMethod = ref.targetStaticMethod(f);
			MethodDecl instanceMethod = ref.targetInstanceMethod(f);
			if(ref.validStaticMethod(f) && ref.validInstanceMethod(f)) {
				error("Ambiguity error: two possible methods " + staticMethod.name() + " was found");
				return;
			}
			else if(unknownMethod() == staticMethod && unknownMethod() == instanceMethod) {
				error("No method " + name() + " that is compatible with the method " + iDecl.functionDescriptor().method.fullSignature() + 
				 " in the interface " + iDecl.typeName() + " was found");
				 return;
			}
			else if(ref.validStaticMethod(f)) {
				if(ref.getTypeAccess() instanceof ParTypeAccess)
					error("Parameterized qualifier is not allowed for static method references");
				else
					found = staticMethod;
			}
			else if(ref.validInstanceMethod(f)) {
				found = instanceMethod;
			}
			else if(unknownMethod() != staticMethod && !staticMethod.isStatic()) {
				error("Cannot make a static reference to the non-static method " + staticMethod.fullSignature() + " in type " 
				 	 	+ staticMethod.hostType().typeName());
				return;
			}
			else if(instanceMethod.isStatic()) {
				error("The method " + instanceMethod.fullSignature() + " in type " + instanceMethod.hostType().typeName() + 
						" must be accessed in a static way");
				return;
			}
		}
		
		if(found != null && unknownMethod() != found) {
			// Check that found is compatible with the function descriptor
			if(!iDecl.functionDescriptor().method.type().isVoid()) {
				// 15.13.1
				if(found.type().isVoid() || !found.type().assignConversionTo(iDecl.functionDescriptor().method.type(), null))
					error("Return type of referenced method " + found.fullSignature() + " is not compatible with method " + 
							iDecl.functionDescriptor().method.fullSignature() + " in interface " + iDecl.typeName());
			}
			
			for(int i = 0; i < found.getNumException(); i++) {
				TypeDecl exception = found.getException(i).type();
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
					error("Referenced method " + found.name() + " throws unhandled exception type " + exception.typeName()); 
				}
			}
		}
	}
  /**
   * @declaredat ASTNode:1
   */
  public MethodReference() {
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
  public MethodReference(List<Access> p0, String p1) {
    setChild(p0, 0);
    setID(p1);
  }
  /**
   * @declaredat ASTNode:18
   */
  public MethodReference(List<Access> p0, beaver.Symbol p1) {
    setChild(p0, 0);
    setID(p1);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:25
   */
  protected int numChildren() {
    return 1;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:31
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:37
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    isExact_reset();
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
  public MethodReference clone() throws CloneNotSupportedException {
    MethodReference node = (MethodReference) super.clone();
    return node;
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @deprecated Please use emitTreeCopy or emitTreeCopyNoTransform instead
   * @declaredat ASTNode:76
   */
  public abstract MethodReference fullCopy();
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:83
   */
  public abstract MethodReference treeCopyNoTransform();
  /**
   * Create a deep copy of the AST subtree at this node.
   * The subtree of this node is traversed to trigger rewrites before copy.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:91
   */
  public abstract MethodReference treeCopy();
  /**
   * Replaces the TypeArgument list.
   * @param list The new list node to be used as the TypeArgument list.
   * @apilevel high-level
   */
  public void setTypeArgumentList(List<Access> list) {
    setChild(list, 0);
  }
  /**
   * Retrieves the number of children in the TypeArgument list.
   * @return Number of children in the TypeArgument list.
   * @apilevel high-level
   */
  public int getNumTypeArgument() {
    return getTypeArgumentList().getNumChild();
  }
  /**
   * Retrieves the number of children in the TypeArgument list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the TypeArgument list.
   * @apilevel low-level
   */
  public int getNumTypeArgumentNoTransform() {
    return getTypeArgumentListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the TypeArgument list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the TypeArgument list.
   * @apilevel high-level
   */
  public Access getTypeArgument(int i) {
    return (Access) getTypeArgumentList().getChild(i);
  }
  /**
   * Check whether the TypeArgument list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasTypeArgument() {
    return getTypeArgumentList().getNumChild() != 0;
  }
  /**
   * Append an element to the TypeArgument list.
   * @param node The element to append to the TypeArgument list.
   * @apilevel high-level
   */
  public void addTypeArgument(Access node) {
    List<Access> list = (parent == null || state == null) ? getTypeArgumentListNoTransform() : getTypeArgumentList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addTypeArgumentNoTransform(Access node) {
    List<Access> list = getTypeArgumentListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the TypeArgument list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setTypeArgument(Access node, int i) {
    List<Access> list = getTypeArgumentList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the TypeArgument list.
   * @return The node representing the TypeArgument list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="TypeArgument")
  public List<Access> getTypeArgumentList() {
    List<Access> list = (List<Access>) getChild(0);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the TypeArgument list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the TypeArgument list.
   * @apilevel low-level
   */
  public List<Access> getTypeArgumentListNoTransform() {
    return (List<Access>) getChildNoTransform(0);
  }
  /**
   * Retrieves the TypeArgument list.
   * @return The node representing the TypeArgument list.
   * @apilevel high-level
   */
  public List<Access> getTypeArguments() {
    return getTypeArgumentList();
  }
  /**
   * Retrieves the TypeArgument list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the TypeArgument list.
   * @apilevel low-level
   */
  public List<Access> getTypeArgumentsNoTransform() {
    return getTypeArgumentListNoTransform();
  }
  /**
   * Replaces the lexeme ID.
   * @param value The new value for the lexeme ID.
   * @apilevel high-level
   */
  public void setID(String value) {
    tokenString_ID = value;
  }
  /**
   * @apilevel internal
   */
  protected String tokenString_ID;
  /**
   */
  public int IDstart;
  /**
   */
  public int IDend;
  /**
   * JastAdd-internal setter for lexeme ID using the Beaver parser.
   * @param symbol Symbol containing the new value for the lexeme ID
   * @apilevel internal
   */
  public void setID(beaver.Symbol symbol) {
    if(symbol.value != null && !(symbol.value instanceof String))
    throw new UnsupportedOperationException("setID is only valid for String lexemes");
    tokenString_ID = (String)symbol.value;
    IDstart = symbol.getStart();
    IDend = symbol.getEnd();
  }
  /**
   * Retrieves the value for the lexeme ID.
   * @return The value for the lexeme ID.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="ID")
  public String getID() {
    return tokenString_ID != null ? tokenString_ID : "";
  }
  /**
   * @attribute syn
   * @aspect MethodReference
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/MethodReference.jrag:219
   */
  @ASTNodeAnnotation.Attribute
  public abstract boolean congruentTo(FunctionDescriptor f);
  /**
   * @attribute syn
   * @aspect MethodReference
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/MethodReference.jrag:257
   */
  @ASTNodeAnnotation.Attribute
  public abstract ArrayList<MethodDecl> potentiallyApplicableMethods(FunctionDescriptor f);
  /**
   * @attribute syn
   * @aspect MethodReference
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/MethodReference.jrag:303
   */
  @ASTNodeAnnotation.Attribute
  public abstract MethodDecl exactCompileTimeDeclaration();
  @ASTNodeAnnotation.Attribute
  public String dumpString() {
    ASTNode$State state = state();
    String dumpString_value = getClass().getName() + " [" + name() + "]";

    return dumpString_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isExact_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isExact_value;
/**
 * @apilevel internal
 */
private void isExact_reset() {
  isExact_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isExact() {
    if(isExact_computed) {
      return isExact_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isExact_value = isExact_compute();
    if (isFinal && num == state().boundariesCrossed) {
      isExact_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isExact_value;
  }
  /**
   * @apilevel internal
   */
  private boolean isExact_compute() {
  		return exactCompileTimeDeclaration() != unknownMethod();
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
  			return exactCompileTimeDeclaration().type().isPrimitiveType();
  		}
  		
  		// Fourth bullet
  		if(f1.method.type().isReferenceType() && f2.method.type().isPrimitiveType()) {
  			return exactCompileTimeDeclaration().type().isReferenceType();
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
  @ASTNodeAnnotation.Attribute
  public String name() {
    ASTNode$State state = state();
    String name_value = getID();

    return name_value;
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
  		
  		if(congruentTo(iDecl.functionDescriptor()))
  			return iDecl;
  		else
  			return unknownType();
  	}
  /**
   * @attribute inh
   * @aspect MethodReference
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/MethodReference.jrag:30
   */
  @ASTNodeAnnotation.Attribute
  public MethodDecl unknownMethod() {
    ASTNode$State state = state();
    MethodDecl unknownMethod_value = getParent().Define_MethodDecl_unknownMethod(this, null);

    return unknownMethod_value;
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/MethodReference.jrag:200
   * @apilevel internal
   */
  public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
    if (caller == getTypeArgumentListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return NameType.TYPE_NAME;
    }
    else {
      return getParent().Define_NameType_nameType(this, caller);
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
