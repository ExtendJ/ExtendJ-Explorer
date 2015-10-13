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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/grammar/Lambda.ast:6
 * @production InferredParameterDeclaration : {@link ASTNode} ::= <span class="component">&lt;ID:String&gt;</span>;

 */
public class InferredParameterDeclaration extends ASTNode<ASTNode> implements Cloneable, SimpleSet, Iterator, Variable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/PrettyPrint.jadd:114
   */
  public void prettyPrint(StringBuffer sb) {
		sb.append(name());
	}
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/DataStructures.jrag:34
   */
  public SimpleSet add(Object o) {
		return new SimpleSetImpl().add(this).add(o);
	}
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/DataStructures.jrag:38
   */
  public boolean isSingleton() { return true; }
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/DataStructures.jrag:39
   */
  public boolean isSingleton(Object o) { return contains(o); }
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/DataStructures.jrag:42
   */
  private InferredParameterDeclaration iterElem;
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/DataStructures.jrag:43
   */
  public Iterator iterator() { iterElem = this; return this; }
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/DataStructures.jrag:44
   */
  public boolean hasNext() { return iterElem != null; }
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/DataStructures.jrag:45
   */
  public Object next() { Object o = iterElem; iterElem = null; return o; }
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/DataStructures.jrag:46
   */
  public void remove() { throw new UnsupportedOperationException(); }
  /**
   * @aspect Java8NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/NameCheck.jrag:38
   */
  public void nameCheck() {
		SimpleSet decls = outerScope().lookupVariable(name());
		for(Iterator iter = decls.iterator(); iter.hasNext(); ) {
			Variable var = (Variable)iter.next();
			if(var instanceof VariableDeclaration) {
				VariableDeclaration decl = (VariableDeclaration)var;
				if (decl.enclosingBodyDecl() == enclosingBodyDecl())
					error("duplicate declaration of parameter " + name());
			} 
			else if(var instanceof ParameterDeclaration) {
				ParameterDeclaration decl = (ParameterDeclaration)var;
				if(decl.enclosingBodyDecl() == enclosingBodyDecl())
					error("duplicate declaration of parameter " + name());
			}
			else if(var instanceof InferredParameterDeclaration) {
				InferredParameterDeclaration decl = (InferredParameterDeclaration)var;
				if(decl.enclosingBodyDecl() == enclosingBodyDecl())
					error("duplicate declaration of parameter " + name());
			}  
			else if(var instanceof CatchParameterDeclaration) {
				CatchParameterDeclaration decl = (CatchParameterDeclaration)var;
				if(decl.enclosingBodyDecl() == enclosingBodyDecl())
					error("duplicate declaration of parameter " + name());
			}
		}
	
		// 8.4.1
		if(!lookupVariable(name()).contains(this)) {
			error("duplicate declaration of parameter " + name());
		}
	}
  /**
   * @declaredat ASTNode:1
   */
  public InferredParameterDeclaration() {
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
   * @declaredat ASTNode:12
   */
  public InferredParameterDeclaration(String p0) {
    setID(p0);
  }
  /**
   * @declaredat ASTNode:15
   */
  public InferredParameterDeclaration(beaver.Symbol p0) {
    setID(p0);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:21
   */
  protected int numChildren() {
    return 0;
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
    isEffectivelyFinal_reset();
    sourceVariableDecl_reset();
    enclosingLambda_reset();
    lookupVariable_String_reset();
    inferredType_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:44
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:50
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:56
   */
  public InferredParameterDeclaration clone() throws CloneNotSupportedException {
    InferredParameterDeclaration node = (InferredParameterDeclaration) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:63
   */
  public InferredParameterDeclaration copy() {
    try {
      InferredParameterDeclaration node = (InferredParameterDeclaration) clone();
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
   * @declaredat ASTNode:82
   */
  public InferredParameterDeclaration fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:91
   */
  public InferredParameterDeclaration treeCopyNoTransform() {
    InferredParameterDeclaration tree = (InferredParameterDeclaration) copy();
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
   * @declaredat ASTNode:111
   */
  public InferredParameterDeclaration treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:118
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_ID == ((InferredParameterDeclaration)node).tokenString_ID);    
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
  @ASTNodeAnnotation.Attribute
  public String dumpString() {
    ASTNode$State state = state();
    String dumpString_value = getClass().getName() + " [" + name() + "]";

    return dumpString_value;
  }
  @ASTNodeAnnotation.Attribute
  public int size() {
    ASTNode$State state = state();
    int size_value = 1;

    return size_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isEmpty() {
    ASTNode$State state = state();
    boolean isEmpty_value = false;

    return isEmpty_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean contains(Object o) {
    ASTNode$State state = state();
    boolean contains_Object_value = this == o;

    return contains_Object_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isEffectivelyFinal_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isEffectivelyFinal_value;
/**
 * @apilevel internal
 */
private void isEffectivelyFinal_reset() {
  isEffectivelyFinal_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isEffectivelyFinal() {
    if(isEffectivelyFinal_computed) {
      return isEffectivelyFinal_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isEffectivelyFinal_value = !isFinal() && !inhModifiedInScope(this);
    if (isFinal && num == state().boundariesCrossed) {
      isEffectivelyFinal_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isEffectivelyFinal_value;
  }
  @ASTNodeAnnotation.Attribute
  public String name() {
    ASTNode$State state = state();
    String name_value = getID();

    return name_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isParameter() {
    ASTNode$State state = state();
    boolean isParameter_value = true;

    return isParameter_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isClassVariable() {
    ASTNode$State state = state();
    boolean isClassVariable_value = false;

    return isClassVariable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isInstanceVariable() {
    ASTNode$State state = state();
    boolean isInstanceVariable_value = false;

    return isInstanceVariable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isConstructorParameter() {
    ASTNode$State state = state();
    boolean isConstructorParameter_value = false;

    return isConstructorParameter_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isExceptionHandlerParameter() {
    ASTNode$State state = state();
    boolean isExceptionHandlerParameter_value = false;

    return isExceptionHandlerParameter_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isMethodParameter() {
    ASTNode$State state = state();
    boolean isMethodParameter_value = false;

    return isMethodParameter_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isLocalVariable() {
    ASTNode$State state = state();
    boolean isLocalVariable_value = false;

    return isLocalVariable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isFinal() {
    ASTNode$State state = state();
    boolean isFinal_value = false;

    return isFinal_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isVolatile() {
    ASTNode$State state = state();
    boolean isVolatile_value = false;

    return isVolatile_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isBlank() {
    ASTNode$State state = state();
    boolean isBlank_value = true;

    return isBlank_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isStatic() {
    ASTNode$State state = state();
    boolean isStatic_value = false;

    return isStatic_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isSynthetic() {
    ASTNode$State state = state();
    boolean isSynthetic_value = false;

    return isSynthetic_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean sourceVariableDecl_computed = false;
  /**
   * @apilevel internal
   */
  protected Variable sourceVariableDecl_value;
/**
 * @apilevel internal
 */
private void sourceVariableDecl_reset() {
  sourceVariableDecl_computed = false;
  sourceVariableDecl_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Variable sourceVariableDecl() {
    if(sourceVariableDecl_computed) {
      return sourceVariableDecl_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    sourceVariableDecl_value = this;
    if (isFinal && num == state().boundariesCrossed) {
      sourceVariableDecl_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return sourceVariableDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public Modifiers getModifiers() {
    ASTNode$State state = state();
    Modifiers getModifiers_value = null;

    return getModifiers_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean hasInit() {
    ASTNode$State state = state();
    boolean hasInit_value = false;

    return hasInit_value;
  }
  /**
   * @attribute syn
   * @aspect Variables
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/VariableDeclaration.jrag:47
   */
  @ASTNodeAnnotation.Attribute
  public Expr getInit() {
    ASTNode$State state = state();
    try { throw new UnsupportedOperationException(); }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect Variables
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/VariableDeclaration.jrag:48
   */
  @ASTNodeAnnotation.Attribute
  public Constant constant() {
    ASTNode$State state = state();
    try { throw new UnsupportedOperationException(); }
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public Collection<TypeDecl> throwTypes() {
    ASTNode$State state = state();
    Collection<TypeDecl> throwTypes_value = null;

    return throwTypes_value;
  }
  @ASTNodeAnnotation.Attribute
  public TypeDecl type() {
    ASTNode$State state = state();
    TypeDecl type_value = inferredType();

    return type_value;
  }
  /**
   * @attribute syn
   * @aspect Variables
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/VariableDeclaration.jrag:51
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl hostType() {
    ASTNode$State state = state();
    try {
      		if(getParent().getParent() instanceof LambdaExpr) {
      			return ((LambdaExpr)getParent().getParent()).hostType();
      		}
      		else {
      			return ((LambdaExpr)getParent().getParent().getParent()).hostType();
      		}
      	}
    finally {
    }
  }
  /**
   * @attribute inh
   * @aspect PreciseRethrow
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/EffectivelyFinal.jrag:30
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
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/EnclosingLambda.jrag:35
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
   * @aspect VariableScope
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/LookupVariable.jrag:31
   */
  @ASTNodeAnnotation.Attribute
  public SimpleSet lookupVariable(String name) {
    Object _parameters = name;
    if (lookupVariable_String_values == null) lookupVariable_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(lookupVariable_String_values.containsKey(_parameters)) {
      return (SimpleSet)lookupVariable_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    SimpleSet lookupVariable_String_value = getParent().Define_SimpleSet_lookupVariable(this, null, name);
    if (isFinal && num == state().boundariesCrossed) {
      lookupVariable_String_values.put(_parameters, lookupVariable_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return lookupVariable_String_value;
  }
  protected java.util.Map lookupVariable_String_values;
/**
 * @apilevel internal
 */
private void lookupVariable_String_reset() {
  lookupVariable_String_values = null;
}  
  /**
   * @attribute inh
   * @aspect Java8NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/NameCheck.jrag:29
   */
  @ASTNodeAnnotation.Attribute
  public BodyDecl enclosingBodyDecl() {
    ASTNode$State state = state();
    BodyDecl enclosingBodyDecl_value = getParent().Define_BodyDecl_enclosingBodyDecl(this, null);

    return enclosingBodyDecl_value;
  }
  /**
   * @attribute inh
   * @aspect Java8NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/NameCheck.jrag:30
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
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TypeCheck.jrag:30
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl unknownType() {
    ASTNode$State state = state();
    TypeDecl unknownType_value = getParent().Define_TypeDecl_unknownType(this, null);

    return unknownType_value;
  }
  /**
   * @attribute inh
   * @aspect LambdaParametersInference
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TypeCheck.jrag:431
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl inferredType() {
    if(inferredType_computed) {
      return inferredType_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    inferredType_value = getParent().Define_TypeDecl_inferredType(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      inferredType_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return inferredType_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean inferredType_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl inferredType_value;
/**
 * @apilevel internal
 */
private void inferredType_reset() {
  inferredType_computed = false;
  inferredType_value = null;
}  
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
