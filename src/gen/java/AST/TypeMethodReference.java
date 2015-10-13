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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/grammar/MethodReference.ast:4
 * @production TypeMethodReference : {@link MethodReference} ::= <span class="component">TypeAccess:{@link Access}</span>;

 */
public class TypeMethodReference extends MethodReference implements Cloneable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/PrettyPrint.jadd:68
   */
  public void prettyPrint(StringBuffer sb) {
		getTypeAccess().prettyPrint(sb);
		sb.append("::");
		if(getNumTypeArgument() > 0) {
			sb.append("<");
			getTypeArgument(0).prettyPrint(sb);
			for(int i = 1; i < getNumTypeArgument(); i++) {
				sb.append(", ");
				getTypeArgument(i).prettyPrint(sb);
			}
			sb.append(">");
		}
		sb.append(name());
	}
  /**
   * @declaredat ASTNode:1
   */
  public TypeMethodReference() {
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
    setChild(new List(), 0);
  }
  /**
   * @declaredat ASTNode:14
   */
  public TypeMethodReference(List<Access> p0, String p1, Access p2) {
    setChild(p0, 0);
    setID(p1);
    setChild(p2, 1);
  }
  /**
   * @declaredat ASTNode:19
   */
  public TypeMethodReference(List<Access> p0, beaver.Symbol p1, Access p2) {
    setChild(p0, 0);
    setID(p1);
    setChild(p2, 1);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:27
   */
  protected int numChildren() {
    return 2;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:33
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:39
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    targetStaticMethod_FunctionDescriptor_reset();
    targetInstanceMethod_FunctionDescriptor_reset();
    validStaticMethod_FunctionDescriptor_reset();
    validInstanceMethod_FunctionDescriptor_reset();
    inferredReferenceType_FunctionDescriptor_reset();
    syntheticStaticAccess_FunctionDescriptor_reset();
    syntheticStaticMethodAccess_FunctionDescriptor_reset();
    syntheticInstanceAccess_FunctionDescriptor_reset();
    syntheticInstanceMethodAccess_FunctionDescriptor_reset();
    congruentTo_FunctionDescriptor_reset();
    potentiallyApplicableMethods_FunctionDescriptor_reset();
    exactCompileTimeDeclaration_reset();
    potentiallyCompatible_TypeDecl_BodyDecl_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:58
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:64
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:70
   */
  public TypeMethodReference clone() throws CloneNotSupportedException {
    TypeMethodReference node = (TypeMethodReference) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:77
   */
  public TypeMethodReference copy() {
    try {
      TypeMethodReference node = (TypeMethodReference) clone();
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
   * @declaredat ASTNode:96
   */
  public TypeMethodReference fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:105
   */
  public TypeMethodReference treeCopyNoTransform() {
    TypeMethodReference tree = (TypeMethodReference) copy();
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
   * @declaredat ASTNode:125
   */
  public TypeMethodReference treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:132
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_ID == ((TypeMethodReference)node).tokenString_ID);    
  }
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
   * Replaces the TypeAccess child.
   * @param node The new node to replace the TypeAccess child.
   * @apilevel high-level
   */
  public void setTypeAccess(Access node) {
    setChild(node, 1);
  }
  /**
   * Retrieves the TypeAccess child.
   * @return The current node used as the TypeAccess child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="TypeAccess")
  public Access getTypeAccess() {
    return (Access) getChild(1);
  }
  /**
   * Retrieves the TypeAccess child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the TypeAccess child.
   * @apilevel low-level
   */
  public Access getTypeAccessNoTransform() {
    return (Access) getChildNoTransform(1);
  }
  protected java.util.Map targetStaticMethod_FunctionDescriptor_values;
/**
 * @apilevel internal
 */
private void targetStaticMethod_FunctionDescriptor_reset() {
  targetStaticMethod_FunctionDescriptor_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public MethodDecl targetStaticMethod(FunctionDescriptor f) {
    Object _parameters = f;
    if (targetStaticMethod_FunctionDescriptor_values == null) targetStaticMethod_FunctionDescriptor_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(targetStaticMethod_FunctionDescriptor_values.containsKey(_parameters)) {
      return (MethodDecl)targetStaticMethod_FunctionDescriptor_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    MethodDecl targetStaticMethod_FunctionDescriptor_value = targetStaticMethod_compute(f);
    if (isFinal && num == state().boundariesCrossed) {
      targetStaticMethod_FunctionDescriptor_values.put(_parameters, targetStaticMethod_FunctionDescriptor_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return targetStaticMethod_FunctionDescriptor_value;
  }
  /**
   * @apilevel internal
   */
  private MethodDecl targetStaticMethod_compute(FunctionDescriptor f) {
  		MethodAccess synAcc = syntheticStaticMethodAccess(f);
  		SimpleSet maxSpecific = synAcc.maxSpecific(synAcc.lookupMethod(synAcc.name()));
  		if(maxSpecific.size() == 1)
  			return (MethodDecl)maxSpecific.iterator().next();
  		else 
  			return unknownMethod();
  	}
  protected java.util.Map targetInstanceMethod_FunctionDescriptor_values;
/**
 * @apilevel internal
 */
private void targetInstanceMethod_FunctionDescriptor_reset() {
  targetInstanceMethod_FunctionDescriptor_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public MethodDecl targetInstanceMethod(FunctionDescriptor f) {
    Object _parameters = f;
    if (targetInstanceMethod_FunctionDescriptor_values == null) targetInstanceMethod_FunctionDescriptor_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(targetInstanceMethod_FunctionDescriptor_values.containsKey(_parameters)) {
      return (MethodDecl)targetInstanceMethod_FunctionDescriptor_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    MethodDecl targetInstanceMethod_FunctionDescriptor_value = targetInstanceMethod_compute(f);
    if (isFinal && num == state().boundariesCrossed) {
      targetInstanceMethod_FunctionDescriptor_values.put(_parameters, targetInstanceMethod_FunctionDescriptor_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return targetInstanceMethod_FunctionDescriptor_value;
  }
  /**
   * @apilevel internal
   */
  private MethodDecl targetInstanceMethod_compute(FunctionDescriptor f) {
  		if(f.method.getNumParameter() == 0 || !f.method.getParameter(0).type().strictSubtype(getTypeAccess().type())) {
  			return unknownMethod();
  		}
  		
  		MethodAccess synAcc = syntheticInstanceMethodAccess(f);
  		SimpleSet maxSpecific = synAcc.maxSpecific(synAcc.lookupMethod(synAcc.name()));
  		if(maxSpecific.size() == 1)
  			return (MethodDecl)maxSpecific.iterator().next();
  		else 
  			return unknownMethod();
  	}
  protected java.util.Map validStaticMethod_FunctionDescriptor_values;
/**
 * @apilevel internal
 */
private void validStaticMethod_FunctionDescriptor_reset() {
  validStaticMethod_FunctionDescriptor_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean validStaticMethod(FunctionDescriptor f) {
    Object _parameters = f;
    if (validStaticMethod_FunctionDescriptor_values == null) validStaticMethod_FunctionDescriptor_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(validStaticMethod_FunctionDescriptor_values.containsKey(_parameters)) {
      return ((Boolean)validStaticMethod_FunctionDescriptor_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean validStaticMethod_FunctionDescriptor_value = validStaticMethod_compute(f);
    if (isFinal && num == state().boundariesCrossed) {
      validStaticMethod_FunctionDescriptor_values.put(_parameters, Boolean.valueOf(validStaticMethod_FunctionDescriptor_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return validStaticMethod_FunctionDescriptor_value;
  }
  /**
   * @apilevel internal
   */
  private boolean validStaticMethod_compute(FunctionDescriptor f) {
  		MethodDecl decl = targetStaticMethod(f);
  		if(decl == unknownMethod() || !decl.isStatic())
  			return false;
  		return true;
  	}
  protected java.util.Map validInstanceMethod_FunctionDescriptor_values;
/**
 * @apilevel internal
 */
private void validInstanceMethod_FunctionDescriptor_reset() {
  validInstanceMethod_FunctionDescriptor_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean validInstanceMethod(FunctionDescriptor f) {
    Object _parameters = f;
    if (validInstanceMethod_FunctionDescriptor_values == null) validInstanceMethod_FunctionDescriptor_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(validInstanceMethod_FunctionDescriptor_values.containsKey(_parameters)) {
      return ((Boolean)validInstanceMethod_FunctionDescriptor_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean validInstanceMethod_FunctionDescriptor_value = validInstanceMethod_compute(f);
    if (isFinal && num == state().boundariesCrossed) {
      validInstanceMethod_FunctionDescriptor_values.put(_parameters, Boolean.valueOf(validInstanceMethod_FunctionDescriptor_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return validInstanceMethod_FunctionDescriptor_value;
  }
  /**
   * @apilevel internal
   */
  private boolean validInstanceMethod_compute(FunctionDescriptor f) {
  		MethodDecl decl = targetInstanceMethod(f);
  		if(decl == unknownMethod() || decl.isStatic())
  			return false;
  		return true; 
  	}
  protected java.util.Map inferredReferenceType_FunctionDescriptor_values;
/**
 * @apilevel internal
 */
private void inferredReferenceType_FunctionDescriptor_reset() {
  inferredReferenceType_FunctionDescriptor_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl inferredReferenceType(FunctionDescriptor f) {
    Object _parameters = f;
    if (inferredReferenceType_FunctionDescriptor_values == null) inferredReferenceType_FunctionDescriptor_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(inferredReferenceType_FunctionDescriptor_values.containsKey(_parameters)) {
      return (TypeDecl)inferredReferenceType_FunctionDescriptor_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    TypeDecl inferredReferenceType_FunctionDescriptor_value = inferredReferenceType_compute(f);
    if (isFinal && num == state().boundariesCrossed) {
      inferredReferenceType_FunctionDescriptor_values.put(_parameters, inferredReferenceType_FunctionDescriptor_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return inferredReferenceType_FunctionDescriptor_value;
  }
  /**
   * @apilevel internal
   */
  private TypeDecl inferredReferenceType_compute(FunctionDescriptor f) {
  		if(f.method.getNumParameter() == 0)
  			return null;
  		else if(!(f.method.getParameter(0).getTypeAccess() instanceof ParTypeAccess))
  			return null;
  		
  		else if(!getTypeAccess().type().isRawType() || !(getTypeAccess() instanceof TypeAccess)) 
  			return null;
  		
  		ParameterDeclaration param = f.method.getParameter(0);
  		if(!param.type().strictSubtype(param.inferredReferenceAccess((TypeAccess)getTypeAccess()).type())) 
  			return null;
  		return param.inferredReferenceAccess((TypeAccess)getTypeAccess()).type();
  	}
  /**
   * @apilevel internal
   */
  protected java.util.Map syntheticStaticAccess_FunctionDescriptor_values;
  /**
   * @apilevel internal
   */
  protected List syntheticStaticAccess_FunctionDescriptor_list;
/**
 * @apilevel internal
 */
private void syntheticStaticAccess_FunctionDescriptor_reset() {
  syntheticStaticAccess_FunctionDescriptor_values = null;
  syntheticStaticAccess_FunctionDescriptor_list = null;
}  
  @ASTNodeAnnotation.Attribute
  public Access syntheticStaticAccess(FunctionDescriptor f) {
    Object _parameters = f;
    if (syntheticStaticAccess_FunctionDescriptor_values == null) syntheticStaticAccess_FunctionDescriptor_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(syntheticStaticAccess_FunctionDescriptor_values.containsKey(_parameters)) {
      return (Access)syntheticStaticAccess_FunctionDescriptor_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    Access syntheticStaticAccess_FunctionDescriptor_value = syntheticStaticAccess_compute(f);
    if(syntheticStaticAccess_FunctionDescriptor_list == null) {
      syntheticStaticAccess_FunctionDescriptor_list = new List();
      syntheticStaticAccess_FunctionDescriptor_list.is$Final = true;
      syntheticStaticAccess_FunctionDescriptor_list.setParent(this);
    }
    syntheticStaticAccess_FunctionDescriptor_list.add(syntheticStaticAccess_FunctionDescriptor_value);
    if(syntheticStaticAccess_FunctionDescriptor_value != null) {
      syntheticStaticAccess_FunctionDescriptor_value = (Access) syntheticStaticAccess_FunctionDescriptor_list.getChild(syntheticStaticAccess_FunctionDescriptor_list.numChildren-1);
      syntheticStaticAccess_FunctionDescriptor_value.is$Final = true;
    }
    if (true) {
      syntheticStaticAccess_FunctionDescriptor_values.put(_parameters, syntheticStaticAccess_FunctionDescriptor_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return syntheticStaticAccess_FunctionDescriptor_value;
  }
  /**
   * @apilevel internal
   */
  private Access syntheticStaticAccess_compute(FunctionDescriptor f) {
  		List<Expr> arguments = new List<Expr>();
  		for(int i = 0; i < f.method.getNumParameter(); i++) {
  			TypeDecl argumentType = f.method.getParameter(i).type();
  			arguments.add(new SyntheticTypeAccess(argumentType));
  		}
  		
  		if(!hasTypeArgument()) {
  			MethodReferenceAccess mAccess = new MethodReferenceAccess(name(), arguments, f);
  			return ((Access)getTypeAccess().fullCopy()).qualifiesAccess(mAccess);
  		}
  		else {
  			ParMethodReferenceAccess pmAccess = new ParMethodReferenceAccess(name(), arguments, (List<Access>)getTypeArgumentList().fullCopy(), f);
  			return ((Access)getTypeAccess().fullCopy()).qualifiesAccess(pmAccess);
  		}
  	}
  protected java.util.Map syntheticStaticMethodAccess_FunctionDescriptor_values;
/**
 * @apilevel internal
 */
private void syntheticStaticMethodAccess_FunctionDescriptor_reset() {
  syntheticStaticMethodAccess_FunctionDescriptor_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public MethodAccess syntheticStaticMethodAccess(FunctionDescriptor f) {
    Object _parameters = f;
    if (syntheticStaticMethodAccess_FunctionDescriptor_values == null) syntheticStaticMethodAccess_FunctionDescriptor_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(syntheticStaticMethodAccess_FunctionDescriptor_values.containsKey(_parameters)) {
      return (MethodAccess)syntheticStaticMethodAccess_FunctionDescriptor_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    MethodAccess syntheticStaticMethodAccess_FunctionDescriptor_value = syntheticStaticMethodAccess_compute(f);
    if (isFinal && num == state().boundariesCrossed) {
      syntheticStaticMethodAccess_FunctionDescriptor_values.put(_parameters, syntheticStaticMethodAccess_FunctionDescriptor_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return syntheticStaticMethodAccess_FunctionDescriptor_value;
  }
  /**
   * @apilevel internal
   */
  private MethodAccess syntheticStaticMethodAccess_compute(FunctionDescriptor f) {
  		Access synAccess = syntheticStaticAccess(f);
  		return (MethodAccess)synAccess.lastAccess();
  	}
  /**
   * @apilevel internal
   */
  protected java.util.Map syntheticInstanceAccess_FunctionDescriptor_values;
  /**
   * @apilevel internal
   */
  protected List syntheticInstanceAccess_FunctionDescriptor_list;
/**
 * @apilevel internal
 */
private void syntheticInstanceAccess_FunctionDescriptor_reset() {
  syntheticInstanceAccess_FunctionDescriptor_values = null;
  syntheticInstanceAccess_FunctionDescriptor_list = null;
}  
  @ASTNodeAnnotation.Attribute
  public Access syntheticInstanceAccess(FunctionDescriptor f) {
    Object _parameters = f;
    if (syntheticInstanceAccess_FunctionDescriptor_values == null) syntheticInstanceAccess_FunctionDescriptor_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(syntheticInstanceAccess_FunctionDescriptor_values.containsKey(_parameters)) {
      return (Access)syntheticInstanceAccess_FunctionDescriptor_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    Access syntheticInstanceAccess_FunctionDescriptor_value = syntheticInstanceAccess_compute(f);
    if(syntheticInstanceAccess_FunctionDescriptor_list == null) {
      syntheticInstanceAccess_FunctionDescriptor_list = new List();
      syntheticInstanceAccess_FunctionDescriptor_list.is$Final = true;
      syntheticInstanceAccess_FunctionDescriptor_list.setParent(this);
    }
    syntheticInstanceAccess_FunctionDescriptor_list.add(syntheticInstanceAccess_FunctionDescriptor_value);
    if(syntheticInstanceAccess_FunctionDescriptor_value != null) {
      syntheticInstanceAccess_FunctionDescriptor_value = (Access) syntheticInstanceAccess_FunctionDescriptor_list.getChild(syntheticInstanceAccess_FunctionDescriptor_list.numChildren-1);
      syntheticInstanceAccess_FunctionDescriptor_value.is$Final = true;
    }
    if (true) {
      syntheticInstanceAccess_FunctionDescriptor_values.put(_parameters, syntheticInstanceAccess_FunctionDescriptor_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return syntheticInstanceAccess_FunctionDescriptor_value;
  }
  /**
   * @apilevel internal
   */
  private Access syntheticInstanceAccess_compute(FunctionDescriptor f) {
  		List<Expr> arguments = new List<Expr>();
  		for(int i = 1; i < f.method.getNumParameter(); i++) {
  			TypeDecl argumentType = f.method.getParameter(i).type();
  			arguments.add(new SyntheticTypeAccess(argumentType));
  		}
  		
  		Access qualifier = null;
  		
  		if(inferredReferenceType(f) != null) {
  			qualifier = new SyntheticTypeAccess(inferredReferenceType(f));
  		}
  		else {
  			qualifier = (Access)getTypeAccess().fullCopy();
  		}
  		
  		if(!hasTypeArgument()) {
  			MethodReferenceAccess mAccess = new MethodReferenceAccess(name(), arguments, f);
  			return qualifier.qualifiesAccess(mAccess);
  		}
  		else {
  			ParMethodReferenceAccess pmAccess = new ParMethodReferenceAccess(name(), arguments, 
  									(List<Access>)getTypeArgumentList().fullCopy(), f);
  			return qualifier.qualifiesAccess(pmAccess);
  		}
  	}
  protected java.util.Map syntheticInstanceMethodAccess_FunctionDescriptor_values;
/**
 * @apilevel internal
 */
private void syntheticInstanceMethodAccess_FunctionDescriptor_reset() {
  syntheticInstanceMethodAccess_FunctionDescriptor_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public MethodAccess syntheticInstanceMethodAccess(FunctionDescriptor f) {
    Object _parameters = f;
    if (syntheticInstanceMethodAccess_FunctionDescriptor_values == null) syntheticInstanceMethodAccess_FunctionDescriptor_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(syntheticInstanceMethodAccess_FunctionDescriptor_values.containsKey(_parameters)) {
      return (MethodAccess)syntheticInstanceMethodAccess_FunctionDescriptor_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    MethodAccess syntheticInstanceMethodAccess_FunctionDescriptor_value = syntheticInstanceMethodAccess_compute(f);
    if (isFinal && num == state().boundariesCrossed) {
      syntheticInstanceMethodAccess_FunctionDescriptor_values.put(_parameters, syntheticInstanceMethodAccess_FunctionDescriptor_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return syntheticInstanceMethodAccess_FunctionDescriptor_value;
  }
  /**
   * @apilevel internal
   */
  private MethodAccess syntheticInstanceMethodAccess_compute(FunctionDescriptor f) {
  		Access synAccess = syntheticInstanceAccess(f);
  		return (MethodAccess)synAccess.lastAccess();
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
  		MethodDecl staticMethod = targetStaticMethod(f);
  		MethodDecl instanceMethod = targetInstanceMethod(f);
  		if(unknownMethod() != staticMethod && unknownMethod() != instanceMethod) {
  			return false;
  		}
  		else if(unknownMethod() == staticMethod && unknownMethod() == instanceMethod) {
  			return false;
  		}
  		MethodDecl found;
  		if(unknownMethod() != staticMethod)
  			found = staticMethod;
  		else
  			found = instanceMethod;
  		if(f.method.type().isVoid())
  			return true;
  		if(found.type().isVoid())
  			return false;
  		return found.type().assignConversionTo(f.method.type(), null);
  	}
  protected java.util.Map potentiallyApplicableMethods_FunctionDescriptor_values;
/**
 * @apilevel internal
 */
private void potentiallyApplicableMethods_FunctionDescriptor_reset() {
  potentiallyApplicableMethods_FunctionDescriptor_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public ArrayList<MethodDecl> potentiallyApplicableMethods(FunctionDescriptor f) {
    Object _parameters = f;
    if (potentiallyApplicableMethods_FunctionDescriptor_values == null) potentiallyApplicableMethods_FunctionDescriptor_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(potentiallyApplicableMethods_FunctionDescriptor_values.containsKey(_parameters)) {
      return (ArrayList<MethodDecl>)potentiallyApplicableMethods_FunctionDescriptor_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    ArrayList<MethodDecl> potentiallyApplicableMethods_FunctionDescriptor_value = potentiallyApplicableMethods_compute(f);
    if (isFinal && num == state().boundariesCrossed) {
      potentiallyApplicableMethods_FunctionDescriptor_values.put(_parameters, potentiallyApplicableMethods_FunctionDescriptor_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return potentiallyApplicableMethods_FunctionDescriptor_value;
  }
  /**
   * @apilevel internal
   */
  private ArrayList<MethodDecl> potentiallyApplicableMethods_compute(FunctionDescriptor f) {
  		Collection<MethodDecl> col = getTypeAccess().type().memberMethods(name());
  		ArrayList<MethodDecl> applicable = new ArrayList<MethodDecl>();
  		for(MethodDecl decl : col) {
  			if(!decl.accessibleFrom(hostType()))
  				continue;
  			if(!(decl.arity() == f.method.arity()) && !(decl.arity() == f.method.arity() - 1))
  				continue;
  			if(hasTypeArgument()) {
  				if(!(decl instanceof GenericMethodDecl))
  					continue;
  				GenericMethodDecl genDecl = (GenericMethodDecl)decl;
  				if(!(getNumTypeArgument() == genDecl.getNumTypeParameter()))
  					continue;
  			}	
  			applicable.add(decl);
  		}
  		return applicable;
  	}
  /**
   * @apilevel internal
   */
  protected boolean exactCompileTimeDeclaration_computed = false;
  /**
   * @apilevel internal
   */
  protected MethodDecl exactCompileTimeDeclaration_value;
/**
 * @apilevel internal
 */
private void exactCompileTimeDeclaration_reset() {
  exactCompileTimeDeclaration_computed = false;
  exactCompileTimeDeclaration_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public MethodDecl exactCompileTimeDeclaration() {
    if(exactCompileTimeDeclaration_computed) {
      return exactCompileTimeDeclaration_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    exactCompileTimeDeclaration_value = exactCompileTimeDeclaration_compute();
    if (isFinal && num == state().boundariesCrossed) {
      exactCompileTimeDeclaration_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return exactCompileTimeDeclaration_value;
  }
  /**
   * @apilevel internal
   */
  private MethodDecl exactCompileTimeDeclaration_compute() {
  		if(getTypeAccess().type().isRawType())
  			return unknownMethod();
  		Collection<MethodDecl> col = getTypeAccess().type().memberMethods(name());
  		int foundCompatible = 0;
  		MethodDecl latestDecl = null;
  		for(MethodDecl decl  : col) {
  			if(decl.accessibleFrom(hostType())) {
  				foundCompatible++;
  				latestDecl = decl;
  			}
  		}
  		if(foundCompatible != 1)
  			return unknownMethod();
  		if(latestDecl.isVariableArity())
  			return unknownMethod();
  		if(latestDecl instanceof GenericMethodDecl) {
  			GenericMethodDecl genericDecl = (GenericMethodDecl)latestDecl;
  			if(getNumTypeArgument() == genericDecl.getNumTypeParameter()) {
  				return latestDecl;
  			}
  			else {
  				return unknownMethod();
  			}
  		}
  		return latestDecl;
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
  		if(super.potentiallyCompatible(type, candidateDecl) && type.isTypeVariable())
  			return true;
  		else if(!super.potentiallyCompatible(type, candidateDecl))
  			return false;
  		
  		InterfaceDecl iDecl = (InterfaceDecl)type;
  		FunctionDescriptor f = iDecl.functionDescriptor();
  		
  		boolean foundMethod = false;
  		for(MethodDecl decl : potentiallyApplicableMethods(f)) {
  			if(decl.isStatic() && f.method.arity() == decl.arity()) {
  				foundMethod = true;
  				break;
  			}
  			else if(!decl.isStatic() && f.method.arity() - 1 == decl.arity()) {
  				foundMethod = true;
  				break;
  			}
  		}
  		return foundMethod;		
  	}
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/MethodReference.jrag:201
   * @apilevel internal
   */
  public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
    if (caller == getTypeAccessNoTransform()) {
      return NameType.TYPE_NAME;
    }
    else {
      return super.Define_NameType_nameType(caller, child);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:398
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
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:399
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
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:400
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
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:401
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
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:402
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
