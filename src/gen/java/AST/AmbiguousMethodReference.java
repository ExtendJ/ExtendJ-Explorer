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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/grammar/MethodReference.ast:5
 * @production AmbiguousMethodReference : {@link MethodReference} ::= <span class="component">AmbiguousName:{@link Access}</span>;

 */
public class AmbiguousMethodReference extends MethodReference implements Cloneable {
  /**
   * @declaredat ASTNode:1
   */
  public AmbiguousMethodReference() {
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
  public AmbiguousMethodReference(List<Access> p0, String p1, Access p2) {
    setChild(p0, 0);
    setID(p1);
    setChild(p2, 1);
  }
  /**
   * @declaredat ASTNode:19
   */
  public AmbiguousMethodReference(List<Access> p0, beaver.Symbol p1, Access p2) {
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
    return true;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:39
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    congruentTo_FunctionDescriptor_reset();
    potentiallyApplicableMethods_FunctionDescriptor_reset();
    exactCompileTimeDeclaration_reset();
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
  public AmbiguousMethodReference clone() throws CloneNotSupportedException {
    AmbiguousMethodReference node = (AmbiguousMethodReference) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:67
   */
  public AmbiguousMethodReference copy() {
    try {
      AmbiguousMethodReference node = (AmbiguousMethodReference) clone();
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
  public AmbiguousMethodReference fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:95
   */
  public AmbiguousMethodReference treeCopyNoTransform() {
    AmbiguousMethodReference tree = (AmbiguousMethodReference) copy();
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
  public AmbiguousMethodReference treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:122
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_ID == ((AmbiguousMethodReference)node).tokenString_ID);    
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
   * Replaces the AmbiguousName child.
   * @param node The new node to replace the AmbiguousName child.
   * @apilevel high-level
   */
  public void setAmbiguousName(Access node) {
    setChild(node, 1);
  }
  /**
   * Retrieves the AmbiguousName child.
   * @return The current node used as the AmbiguousName child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="AmbiguousName")
  public Access getAmbiguousName() {
    return (Access) getChild(1);
  }
  /**
   * Retrieves the AmbiguousName child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the AmbiguousName child.
   * @apilevel low-level
   */
  public Access getAmbiguousNameNoTransform() {
    return (Access) getChildNoTransform(1);
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
  		return false;
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
    ArrayList<MethodDecl> potentiallyApplicableMethods_FunctionDescriptor_value = new ArrayList<MethodDecl>();
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
    exactCompileTimeDeclaration_value = unknownMethod();
    if (isFinal && num == state().boundariesCrossed) {
      exactCompileTimeDeclaration_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return exactCompileTimeDeclaration_value;
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/MethodReference.jrag:202
   * @apilevel internal
   */
  public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
    if (caller == getAmbiguousNameNoTransform()) {
      return NameType.AMBIGUOUS_NAME;
    }
    else {
      return super.Define_NameType_nameType(caller, child);
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    // Declared at @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/MethodReference.jrag:205
    if (!getAmbiguousName().isTypeAccess()) {
      state().duringMethodReference++;
      ASTNode result = rewriteRule0();
      state().duringMethodReference--;
      return result;
    }    // Declared at @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/MethodReference.jrag:212
    if (getAmbiguousName().isTypeAccess()) {
      state().duringMethodReference++;
      ASTNode result = rewriteRule1();
      state().duringMethodReference--;
      return result;
    }    return super.rewriteTo();
  }  /**
   * @declaredat @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/MethodReference.jrag:205
   * @apilevel internal
   */  private ExprMethodReference rewriteRule0() {
{
				return new ExprMethodReference(getTypeArgumentList(), getID(), getAmbiguousName());
			}  }
  /**
   * @declaredat @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/MethodReference.jrag:212
   * @apilevel internal
   */  private TypeMethodReference rewriteRule1() {
{
				return new TypeMethodReference(getTypeArgumentList(), getID(), getAmbiguousName());
			}  }
}
