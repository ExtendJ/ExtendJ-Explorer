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
 * A catch parameter with disjunct exception type.
 * @ast node
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/grammar/MultiCatch.ast:19
 * @production CatchParameterDeclaration : {@link ASTNode} ::= <span class="component">{@link Modifiers}</span> <span class="component">TypeAccess:{@link Access}*</span> <span class="component">&lt;ID:String&gt;</span>;

 */
public class CatchParameterDeclaration extends ASTNode<ASTNode> implements Cloneable, Variable, SimpleSet, Iterator {
  /**
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:63
   */
  public SimpleSet add(Object o) {
    return new SimpleSetImpl().add(this).add(o);
  }
  /**
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:67
   */
  public boolean isSingleton() { return true; }
  /**
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:68
   */
  public boolean isSingleton(Object o) { return contains(o); }
  /**
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:71
   */
  private CatchParameterDeclaration iterElem;
  /**
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:72
   */
  public Iterator iterator() { iterElem = this; return this; }
  /**
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:73
   */
  public boolean hasNext() { return iterElem != null; }
  /**
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:74
   */
  public Object next() { Object o = iterElem; iterElem = null; return o; }
  /**
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:75
   */
  public void remove() { throw new UnsupportedOperationException(); }
  /**
   * Type checking.
   * The types given in a disjunction type may not be
   * subtypes of each other.
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:109
   */
  public void typeCheck() {
    boolean pass = true;
    for (int i = 0; i < getNumTypeAccess(); ++i) {
      for (int j = 0; j < getNumTypeAccess(); ++j) {
        if (i == j) continue;
        TypeDecl t1 = getTypeAccess(i).type();
        TypeDecl t2 = getTypeAccess(j).type();
        if (t2.instanceOf(t1)) {
          error(t2.fullName() + " is a subclass of " +
              t1.fullName());
          pass = false;
        }
      }
    }
  }
  /**
   * Pretty printing of catch parameter declaration.
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:156
   */
  public void prettyPrint(StringBuffer sb) {
    getModifiers().prettyPrint(sb);
    for (int i = 0; i < getNumTypeAccess(); ++i) {
      if (i > 0) sb.append(" | ");
      getTypeAccess(i).prettyPrint(sb);
    }
    sb.append(" "+getID());
  }
  /**
   * @declaredat ASTNode:1
   */
  public CatchParameterDeclaration() {
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
    setChild(new List(), 1);
  }
  /**
   * @declaredat ASTNode:14
   */
  public CatchParameterDeclaration(Modifiers p0, List<Access> p1, String p2) {
    setChild(p0, 0);
    setChild(p1, 1);
    setID(p2);
  }
  /**
   * @declaredat ASTNode:19
   */
  public CatchParameterDeclaration(Modifiers p0, List<Access> p1, beaver.Symbol p2) {
    setChild(p0, 0);
    setChild(p1, 1);
    setID(p2);
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
    sourceVariableDecl_reset();
    throwTypes_reset();
    isEffectivelyFinal_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:48
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:54
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:60
   */
  public CatchParameterDeclaration clone() throws CloneNotSupportedException {
    CatchParameterDeclaration node = (CatchParameterDeclaration) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:67
   */
  public CatchParameterDeclaration copy() {
    try {
      CatchParameterDeclaration node = (CatchParameterDeclaration) clone();
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
   * @declaredat ASTNode:86
   */
  public CatchParameterDeclaration fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:95
   */
  public CatchParameterDeclaration treeCopyNoTransform() {
    CatchParameterDeclaration tree = (CatchParameterDeclaration) copy();
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
   * @declaredat ASTNode:115
   */
  public CatchParameterDeclaration treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:122
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_ID == ((CatchParameterDeclaration)node).tokenString_ID);    
  }
  /**
   * Replaces the Modifiers child.
   * @param node The new node to replace the Modifiers child.
   * @apilevel high-level
   */
  public void setModifiers(Modifiers node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the Modifiers child.
   * @return The current node used as the Modifiers child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Modifiers")
  public Modifiers getModifiers() {
    return (Modifiers) getChild(0);
  }
  /**
   * Retrieves the Modifiers child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Modifiers child.
   * @apilevel low-level
   */
  public Modifiers getModifiersNoTransform() {
    return (Modifiers) getChildNoTransform(0);
  }
  /**
   * Replaces the TypeAccess list.
   * @param list The new list node to be used as the TypeAccess list.
   * @apilevel high-level
   */
  public void setTypeAccessList(List<Access> list) {
    setChild(list, 1);
  }
  /**
   * Retrieves the number of children in the TypeAccess list.
   * @return Number of children in the TypeAccess list.
   * @apilevel high-level
   */
  public int getNumTypeAccess() {
    return getTypeAccessList().getNumChild();
  }
  /**
   * Retrieves the number of children in the TypeAccess list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the TypeAccess list.
   * @apilevel low-level
   */
  public int getNumTypeAccessNoTransform() {
    return getTypeAccessListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the TypeAccess list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the TypeAccess list.
   * @apilevel high-level
   */
  public Access getTypeAccess(int i) {
    return (Access) getTypeAccessList().getChild(i);
  }
  /**
   * Check whether the TypeAccess list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasTypeAccess() {
    return getTypeAccessList().getNumChild() != 0;
  }
  /**
   * Append an element to the TypeAccess list.
   * @param node The element to append to the TypeAccess list.
   * @apilevel high-level
   */
  public void addTypeAccess(Access node) {
    List<Access> list = (parent == null || state == null) ? getTypeAccessListNoTransform() : getTypeAccessList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addTypeAccessNoTransform(Access node) {
    List<Access> list = getTypeAccessListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the TypeAccess list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setTypeAccess(Access node, int i) {
    List<Access> list = getTypeAccessList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the TypeAccess list.
   * @return The node representing the TypeAccess list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="TypeAccess")
  public List<Access> getTypeAccessList() {
    List<Access> list = (List<Access>) getChild(1);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the TypeAccess list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the TypeAccess list.
   * @apilevel low-level
   */
  public List<Access> getTypeAccessListNoTransform() {
    return (List<Access>) getChildNoTransform(1);
  }
  /**
   * Retrieves the TypeAccess list.
   * @return The node representing the TypeAccess list.
   * @apilevel high-level
   */
  public List<Access> getTypeAccesss() {
    return getTypeAccessList();
  }
  /**
   * Retrieves the TypeAccess list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the TypeAccess list.
   * @apilevel low-level
   */
  public List<Access> getTypeAccesssNoTransform() {
    return getTypeAccessListNoTransform();
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
   * @aspect Java8NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/NameCheck.jrag:70
   */
   
	public void nameCheck() {
		SimpleSet decls = outerScope().lookupVariable(name());
		for(Iterator iter = decls.iterator(); iter.hasNext(); ) {
			Variable var = (Variable)iter.next();
			if(var instanceof VariableDeclaration) {
				VariableDeclaration decl = (VariableDeclaration)var;
				if (decl.enclosingBodyDecl() == enclosingBodyDecl())
					error("duplicate declaration of " + "catch parameter " + name());
			} 
			else if(var instanceof ParameterDeclaration) {
				ParameterDeclaration decl = (ParameterDeclaration)var;
				if(decl.enclosingBodyDecl() == enclosingBodyDecl())
					error("duplicate declaration of "+ "catch parameter " + name());
			} 
			else if(var instanceof InferredParameterDeclaration) {
				InferredParameterDeclaration decl = (InferredParameterDeclaration)var;
				if(decl.enclosingBodyDecl() == enclosingBodyDecl())
					error("duplicate declaration of "+ "catch parameter " + name());
			} 
			else if (var instanceof CatchParameterDeclaration) {
				CatchParameterDeclaration decl = (CatchParameterDeclaration)var;
				if (decl.enclosingBodyDecl() == enclosingBodyDecl())
					error("duplicate declaration of " + "catch parameter " + name());
			}
		}
	
		// 8.4.1
		if (!lookupVariable(name()).contains(this))
			error("duplicate declaration of catch parameter " + name());
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
  public boolean isLocalVariable() {
    ASTNode$State state = state();
    boolean isLocalVariable_value = false;

    return isLocalVariable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isFinal() {
    ASTNode$State state = state();
    boolean isFinal_value = true;

    return isFinal_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isVolatile() {
    ASTNode$State state = state();
    boolean isVolatile_value = getModifiers().isVolatile();

    return isVolatile_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isBlank() {
    ASTNode$State state = state();
    boolean isBlank_value = false;

    return isBlank_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isStatic() {
    ASTNode$State state = state();
    boolean isStatic_value = false;

    return isStatic_value;
  }
  @ASTNodeAnnotation.Attribute
  public String name() {
    ASTNode$State state = state();
    String name_value = getID();

    return name_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean hasInit() {
    ASTNode$State state = state();
    boolean hasInit_value = false;

    return hasInit_value;
  }
  /**
   * @attribute syn
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:42
   */
  @ASTNodeAnnotation.Attribute
  public Expr getInit() {
    ASTNode$State state = state();
    try {
        throw new UnsupportedOperationException();
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:45
   */
  @ASTNodeAnnotation.Attribute
  public Constant constant() {
    ASTNode$State state = state();
    try {
        throw new UnsupportedOperationException();
      }
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public boolean isSynthetic() {
    ASTNode$State state = state();
    boolean isSynthetic_value = getModifiers().isSynthetic();

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
   * A catch parameter declared with a disjunction type has the
   * effective type lub(t1, t2, ...)
   * 
   * @see "JLSv3 &sect;15.12.2.7"
   * @attribute syn
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:173
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl type() {
    ASTNode$State state = state();
    try {
        ArrayList<TypeDecl> list = new ArrayList<TypeDecl>();
        for (int i = 0; i < getNumTypeAccess(); i++)
          list.add(getTypeAccess(i).type());
        return lookupLUBType(list).lub();
      }
    finally {
    }
  }
  /**
   * @apilevel internal
   */
  protected boolean throwTypes_computed = false;
  /**
   * @apilevel internal
   */
  protected Collection<TypeDecl> throwTypes_value;
/**
 * @apilevel internal
 */
private void throwTypes_reset() {
  throwTypes_computed = false;
  throwTypes_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Collection<TypeDecl> throwTypes() {
    if(throwTypes_computed) {
      return throwTypes_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    throwTypes_value = catchClause().caughtExceptions();
    if (isFinal && num == state().boundariesCrossed) {
      throwTypes_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return throwTypes_value;
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
    isEffectivelyFinal_value = false;
    if (isFinal && num == state().boundariesCrossed) {
      isEffectivelyFinal_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isEffectivelyFinal_value;
  }
  /**
   * Inherit the lookupVariable attribute.
   * @attribute inh
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:14
   */
  @ASTNodeAnnotation.Attribute
  public SimpleSet lookupVariable(String name) {
    ASTNode$State state = state();
    SimpleSet lookupVariable_String_value = getParent().Define_SimpleSet_lookupVariable(this, null, name);

    return lookupVariable_String_value;
  }
  /**
   * @attribute inh
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:22
   */
  @ASTNodeAnnotation.Attribute
  public boolean isMethodParameter() {
    ASTNode$State state = state();
    boolean isMethodParameter_value = getParent().Define_boolean_isMethodParameter(this, null);

    return isMethodParameter_value;
  }
  /**
   * @attribute inh
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:23
   */
  @ASTNodeAnnotation.Attribute
  public boolean isConstructorParameter() {
    ASTNode$State state = state();
    boolean isConstructorParameter_value = getParent().Define_boolean_isConstructorParameter(this, null);

    return isConstructorParameter_value;
  }
  /**
   * @attribute inh
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:24
   */
  @ASTNodeAnnotation.Attribute
  public boolean isExceptionHandlerParameter() {
    ASTNode$State state = state();
    boolean isExceptionHandlerParameter_value = getParent().Define_boolean_isExceptionHandlerParameter(this, null);

    return isExceptionHandlerParameter_value;
  }
  /**
   * @attribute inh
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:49
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl hostType() {
    ASTNode$State state = state();
    TypeDecl hostType_value = getParent().Define_TypeDecl_hostType(this, null);

    return hostType_value;
  }
  /**
   * @attribute inh
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:165
   */
  @ASTNodeAnnotation.Attribute
  public LUBType lookupLUBType(Collection bounds) {
    ASTNode$State state = state();
    LUBType lookupLUBType_Collection_value = getParent().Define_LUBType_lookupLUBType(this, null, bounds);

    return lookupLUBType_Collection_value;
  }
  /**
   * @attribute inh
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:180
   */
  @ASTNodeAnnotation.Attribute
  public VariableScope outerScope() {
    ASTNode$State state = state();
    VariableScope outerScope_value = getParent().Define_VariableScope_outerScope(this, null);

    return outerScope_value;
  }
  /**
   * @attribute inh
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:181
   */
  @ASTNodeAnnotation.Attribute
  public BodyDecl enclosingBodyDecl() {
    ASTNode$State state = state();
    BodyDecl enclosingBodyDecl_value = getParent().Define_BodyDecl_enclosingBodyDecl(this, null);

    return enclosingBodyDecl_value;
  }
  /**
   * @attribute inh
   * @aspect PreciseRethrow
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/PreciseRethrow.jrag:185
   */
  @ASTNodeAnnotation.Attribute
  public CatchClause catchClause() {
    ASTNode$State state = state();
    CatchClause catchClause_value = getParent().Define_CatchClause_catchClause(this, null);

    return catchClause_value;
  }
  /**
   * @attribute inh
   * @aspect PreciseRethrow
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/EffectivelyFinal.jrag:47
   */
  @ASTNodeAnnotation.Attribute
  public boolean inhModifiedInScope(Variable var) {
    ASTNode$State state = state();
    boolean inhModifiedInScope_Variable_value = getParent().Define_boolean_inhModifiedInScope(this, null, var);

    return inhModifiedInScope_Variable_value;
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:92
   * @apilevel internal
   */
  public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
    if (caller == getTypeAccessListNoTransform()) {
      int i = caller.getIndexOfChild(child);
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
