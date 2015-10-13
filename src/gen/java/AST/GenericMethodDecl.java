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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/grammar/GenericMethods.ast:2
 * @production GenericMethodDecl : {@link MethodDecl} ::= <span class="component">TypeParameter:{@link TypeVariable}*</span>;

 */
public class GenericMethodDecl extends MethodDecl implements Cloneable {
  /**
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:1057
   */
  public BodyDecl substitutedBodyDecl(Parameterization parTypeDecl) {
    //System.out.println("Begin substituting generic " + signature() + " in " + hostType().typeName() + " with " + parTypeDecl.typeSignature());
    GenericMethodDecl m = new GenericMethodDecl(
      (Modifiers)getModifiers().fullCopy(),
      getTypeAccess().type().substituteReturnType(parTypeDecl),
      getID(),
      getParameterList().substitute(parTypeDecl),
      getExceptionList().substitute(parTypeDecl),
      new Opt(),
      (List)getTypeParameterList().fullCopy()
    );
    m.original = this;
    //System.out.println("End substituting generic " + signature());
    return m;
  }
  /**
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:1073
   */
  public GenericMethodDecl original;
  /**
   * @aspect GenericMethods
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericMethods.jrag:62
   */
  public ParMethodDecl newParMethodDecl(java.util.List typeArguments) {
    ParMethodDecl methodDecl = typeArguments.isEmpty() ? new RawMethodDecl() : new ParMethodDecl();
    // adding a link to GenericMethodDecl to be used during substitution
    // instead of the not yet existing parent link
    methodDecl.setGenericMethodDecl(this);
    List list = new List();
    if(typeArguments.isEmpty()) {
      GenericMethodDecl original = original();
      for(int i = 0; i < original.getNumTypeParameter(); i++)
        list.add(original.getTypeParameter(i).erasure().createBoundAccess());
    }
    else {
      for(Iterator iter = typeArguments.iterator(); iter.hasNext(); )
        list.add(((TypeDecl)iter.next()).createBoundAccess());
    }
    methodDecl.setTypeArgumentList(list);
    methodDecl.setModifiers((Modifiers)getModifiers().fullCopy());
    methodDecl.setTypeAccess(getTypeAccess().type().substituteReturnType(methodDecl));
    methodDecl.setID(getID());
    methodDecl.setParameterList(getParameterList().substitute(methodDecl));
    methodDecl.setExceptionList(getExceptionList().substitute(methodDecl));
    return methodDecl;
  }
  /**
   * @aspect GenericMethodsPrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericMethods.jrag:216
   */
  private void ppTypeParameters(StringBuffer sb) {
    sb.append(" <");
    for(int i = 0; i < getNumTypeParameter(); i++) {
      if(i != 0) sb.append(", ");
      original().getTypeParameter(i).prettyPrint(sb);
    }
    sb.append("> ");
  }
  /**
   * @aspect GenericMethodsPrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericMethods.jrag:225
   */
  public void prettyPrint(StringBuffer sb) {
    sb.append(indent());
    getModifiers().prettyPrint(sb);

    ppTypeParameters(sb);

    getTypeAccess().prettyPrint(sb);
    sb.append(" " + getID());
    sb.append("(");
    if(getNumParameter() > 0) {
      getParameter(0).prettyPrint(sb);
      for(int i = 1; i < getNumParameter(); i++) {
        sb.append(", ");
        getParameter(i).prettyPrint(sb);
      }
    }
    sb.append(")");
    if(getNumException() > 0) {
      sb.append(" throws ");
      getException(0).prettyPrint(sb);
      for(int i = 1; i < getNumException(); i++) {
        sb.append(", ");
        getException(i).prettyPrint(sb);
      }
    }
    if(hasBlock()) {
      sb.append(" ");
      getBlock().prettyPrint(sb);
    }
    else {
      sb.append(";\n");
    }
  }
  /**
   * @declaredat ASTNode:1
   */
  public GenericMethodDecl() {
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
    children = new ASTNode[6];
    setChild(new List(), 2);
    setChild(new List(), 3);
    setChild(new Opt(), 4);
    setChild(new List(), 5);
  }
  /**
   * @declaredat ASTNode:17
   */
  public GenericMethodDecl(Modifiers p0, Access p1, String p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5, List<TypeVariable> p6) {
    setChild(p0, 0);
    setChild(p1, 1);
    setID(p2);
    setChild(p3, 2);
    setChild(p4, 3);
    setChild(p5, 4);
    setChild(p6, 5);
  }
  /**
   * @declaredat ASTNode:26
   */
  public GenericMethodDecl(Modifiers p0, Access p1, beaver.Symbol p2, List<ParameterDeclaration> p3, List<Access> p4, Opt<Block> p5, List<TypeVariable> p6) {
    setChild(p0, 0);
    setChild(p1, 1);
    setID(p2);
    setChild(p3, 2);
    setChild(p4, 3);
    setChild(p5, 4);
    setChild(p6, 5);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:38
   */
  protected int numChildren() {
    return 6;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:44
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:50
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    rawMethodDecl_reset();
    lookupParMethodDecl_java_util_List_reset();
    subsignatureTo_MethodDecl_reset();
    sameTypeParameters_GenericMethodDecl_reset();
    sameFormalParameters_GenericMethodDecl_reset();
    usesTypeVariable_reset();
    typeVariableInReturn_reset();
    genericMethodLevel_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:64
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:70
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:76
   */
  public GenericMethodDecl clone() throws CloneNotSupportedException {
    GenericMethodDecl node = (GenericMethodDecl) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:83
   */
  public GenericMethodDecl copy() {
    try {
      GenericMethodDecl node = (GenericMethodDecl) clone();
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
   * @declaredat ASTNode:102
   */
  public GenericMethodDecl fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:111
   */
  public GenericMethodDecl treeCopyNoTransform() {
    GenericMethodDecl tree = (GenericMethodDecl) copy();
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
   * @declaredat ASTNode:131
   */
  public GenericMethodDecl treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:138
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_ID == ((GenericMethodDecl)node).tokenString_ID);    
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
   * Replaces the Parameter list.
   * @param list The new list node to be used as the Parameter list.
   * @apilevel high-level
   */
  public void setParameterList(List<ParameterDeclaration> list) {
    setChild(list, 2);
  }
  /**
   * Retrieves the number of children in the Parameter list.
   * @return Number of children in the Parameter list.
   * @apilevel high-level
   */
  public int getNumParameter() {
    return getParameterList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Parameter list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Parameter list.
   * @apilevel low-level
   */
  public int getNumParameterNoTransform() {
    return getParameterListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Parameter list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Parameter list.
   * @apilevel high-level
   */
  public ParameterDeclaration getParameter(int i) {
    return (ParameterDeclaration) getParameterList().getChild(i);
  }
  /**
   * Check whether the Parameter list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasParameter() {
    return getParameterList().getNumChild() != 0;
  }
  /**
   * Append an element to the Parameter list.
   * @param node The element to append to the Parameter list.
   * @apilevel high-level
   */
  public void addParameter(ParameterDeclaration node) {
    List<ParameterDeclaration> list = (parent == null || state == null) ? getParameterListNoTransform() : getParameterList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addParameterNoTransform(ParameterDeclaration node) {
    List<ParameterDeclaration> list = getParameterListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Parameter list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setParameter(ParameterDeclaration node, int i) {
    List<ParameterDeclaration> list = getParameterList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Parameter list.
   * @return The node representing the Parameter list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Parameter")
  public List<ParameterDeclaration> getParameterList() {
    List<ParameterDeclaration> list = (List<ParameterDeclaration>) getChild(2);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the Parameter list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Parameter list.
   * @apilevel low-level
   */
  public List<ParameterDeclaration> getParameterListNoTransform() {
    return (List<ParameterDeclaration>) getChildNoTransform(2);
  }
  /**
   * Retrieves the Parameter list.
   * @return The node representing the Parameter list.
   * @apilevel high-level
   */
  public List<ParameterDeclaration> getParameters() {
    return getParameterList();
  }
  /**
   * Retrieves the Parameter list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Parameter list.
   * @apilevel low-level
   */
  public List<ParameterDeclaration> getParametersNoTransform() {
    return getParameterListNoTransform();
  }
  /**
   * Replaces the Exception list.
   * @param list The new list node to be used as the Exception list.
   * @apilevel high-level
   */
  public void setExceptionList(List<Access> list) {
    setChild(list, 3);
  }
  /**
   * Retrieves the number of children in the Exception list.
   * @return Number of children in the Exception list.
   * @apilevel high-level
   */
  public int getNumException() {
    return getExceptionList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Exception list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Exception list.
   * @apilevel low-level
   */
  public int getNumExceptionNoTransform() {
    return getExceptionListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Exception list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Exception list.
   * @apilevel high-level
   */
  public Access getException(int i) {
    return (Access) getExceptionList().getChild(i);
  }
  /**
   * Check whether the Exception list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasException() {
    return getExceptionList().getNumChild() != 0;
  }
  /**
   * Append an element to the Exception list.
   * @param node The element to append to the Exception list.
   * @apilevel high-level
   */
  public void addException(Access node) {
    List<Access> list = (parent == null || state == null) ? getExceptionListNoTransform() : getExceptionList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addExceptionNoTransform(Access node) {
    List<Access> list = getExceptionListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Exception list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setException(Access node, int i) {
    List<Access> list = getExceptionList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Exception list.
   * @return The node representing the Exception list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Exception")
  public List<Access> getExceptionList() {
    List<Access> list = (List<Access>) getChild(3);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the Exception list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Exception list.
   * @apilevel low-level
   */
  public List<Access> getExceptionListNoTransform() {
    return (List<Access>) getChildNoTransform(3);
  }
  /**
   * Retrieves the Exception list.
   * @return The node representing the Exception list.
   * @apilevel high-level
   */
  public List<Access> getExceptions() {
    return getExceptionList();
  }
  /**
   * Retrieves the Exception list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Exception list.
   * @apilevel low-level
   */
  public List<Access> getExceptionsNoTransform() {
    return getExceptionListNoTransform();
  }
  /**
   * Replaces the optional node for the Block child. This is the <code>Opt</code>
   * node containing the child Block, not the actual child!
   * @param opt The new node to be used as the optional node for the Block child.
   * @apilevel low-level
   */
  public void setBlockOpt(Opt<Block> opt) {
    setChild(opt, 4);
  }
  /**
   * Replaces the (optional) Block child.
   * @param node The new node to be used as the Block child.
   * @apilevel high-level
   */
  public void setBlock(Block node) {
    getBlockOpt().setChild(node, 0);
  }
  /**
   * Check whether the optional Block child exists.
   * @return {@code true} if the optional Block child exists, {@code false} if it does not.
   * @apilevel high-level
   */
  public boolean hasBlock() {
    return getBlockOpt().getNumChild() != 0;
  }
  /**
   * Retrieves the (optional) Block child.
   * @return The Block child, if it exists. Returns {@code null} otherwise.
   * @apilevel low-level
   */
  public Block getBlock() {
    return (Block) getBlockOpt().getChild(0);
  }
  /**
   * Retrieves the optional node for the Block child. This is the <code>Opt</code> node containing the child Block, not the actual child!
   * @return The optional node for child the Block child.
   * @apilevel low-level
   */
  @ASTNodeAnnotation.OptChild(name="Block")
  public Opt<Block> getBlockOpt() {
    return (Opt<Block>) getChild(4);
  }
  /**
   * Retrieves the optional node for child Block. This is the <code>Opt</code> node containing the child Block, not the actual child!
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The optional node for child Block.
   * @apilevel low-level
   */
  public Opt<Block> getBlockOptNoTransform() {
    return (Opt<Block>) getChildNoTransform(4);
  }
  /**
   * Replaces the TypeParameter list.
   * @param list The new list node to be used as the TypeParameter list.
   * @apilevel high-level
   */
  public void setTypeParameterList(List<TypeVariable> list) {
    setChild(list, 5);
  }
  /**
   * Retrieves the number of children in the TypeParameter list.
   * @return Number of children in the TypeParameter list.
   * @apilevel high-level
   */
  public int getNumTypeParameter() {
    return getTypeParameterList().getNumChild();
  }
  /**
   * Retrieves the number of children in the TypeParameter list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the TypeParameter list.
   * @apilevel low-level
   */
  public int getNumTypeParameterNoTransform() {
    return getTypeParameterListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the TypeParameter list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the TypeParameter list.
   * @apilevel high-level
   */
  public TypeVariable getTypeParameter(int i) {
    return (TypeVariable) getTypeParameterList().getChild(i);
  }
  /**
   * Check whether the TypeParameter list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasTypeParameter() {
    return getTypeParameterList().getNumChild() != 0;
  }
  /**
   * Append an element to the TypeParameter list.
   * @param node The element to append to the TypeParameter list.
   * @apilevel high-level
   */
  public void addTypeParameter(TypeVariable node) {
    List<TypeVariable> list = (parent == null || state == null) ? getTypeParameterListNoTransform() : getTypeParameterList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addTypeParameterNoTransform(TypeVariable node) {
    List<TypeVariable> list = getTypeParameterListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the TypeParameter list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setTypeParameter(TypeVariable node, int i) {
    List<TypeVariable> list = getTypeParameterList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the TypeParameter list.
   * @return The node representing the TypeParameter list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="TypeParameter")
  public List<TypeVariable> getTypeParameterList() {
    List<TypeVariable> list = (List<TypeVariable>) getChild(5);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the TypeParameter list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the TypeParameter list.
   * @apilevel low-level
   */
  public List<TypeVariable> getTypeParameterListNoTransform() {
    return (List<TypeVariable>) getChildNoTransform(5);
  }
  /**
   * Retrieves the TypeParameter list.
   * @return The node representing the TypeParameter list.
   * @apilevel high-level
   */
  public List<TypeVariable> getTypeParameters() {
    return getTypeParameterList();
  }
  /**
   * Retrieves the TypeParameter list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the TypeParameter list.
   * @apilevel low-level
   */
  public List<TypeVariable> getTypeParametersNoTransform() {
    return getTypeParameterListNoTransform();
  }
  @ASTNodeAnnotation.Attribute
  public GenericMethodDecl original() {
    ASTNode$State state = state();
    GenericMethodDecl original_value = original != null ? original : this;

    return original_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean rawMethodDecl_computed = false;
  /**
   * @apilevel internal
   */
  protected MethodDecl rawMethodDecl_value;
/**
 * @apilevel internal
 */
private void rawMethodDecl_reset() {
  rawMethodDecl_computed = false;
  rawMethodDecl_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public MethodDecl rawMethodDecl() {
    if(rawMethodDecl_computed) {
      return rawMethodDecl_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    rawMethodDecl_value = lookupParMethodDecl(new ArrayList());
    if (true) {
      rawMethodDecl_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return rawMethodDecl_value;
  }
  /**
   * @apilevel internal
   */
  protected java.util.Map lookupParMethodDecl_java_util_List_values;
  /**
   * @apilevel internal
   */
  protected List lookupParMethodDecl_java_util_List_list;
/**
 * @apilevel internal
 */
private void lookupParMethodDecl_java_util_List_reset() {
  lookupParMethodDecl_java_util_List_values = null;
  lookupParMethodDecl_java_util_List_list = null;
}  
  @ASTNodeAnnotation.Attribute
  public MethodDecl lookupParMethodDecl(java.util.List typeArguments) {
    Object _parameters = typeArguments;
    if (lookupParMethodDecl_java_util_List_values == null) lookupParMethodDecl_java_util_List_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(lookupParMethodDecl_java_util_List_values.containsKey(_parameters)) {
      return (MethodDecl)lookupParMethodDecl_java_util_List_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    MethodDecl lookupParMethodDecl_java_util_List_value = lookupParMethodDecl_compute(typeArguments);
    if(lookupParMethodDecl_java_util_List_list == null) {
      lookupParMethodDecl_java_util_List_list = new List();
      lookupParMethodDecl_java_util_List_list.is$Final = true;
      lookupParMethodDecl_java_util_List_list.setParent(this);
    }
    lookupParMethodDecl_java_util_List_list.add(lookupParMethodDecl_java_util_List_value);
    if(lookupParMethodDecl_java_util_List_value != null) {
      lookupParMethodDecl_java_util_List_value = (MethodDecl) lookupParMethodDecl_java_util_List_list.getChild(lookupParMethodDecl_java_util_List_list.numChildren-1);
      lookupParMethodDecl_java_util_List_value.is$Final = true;
    }
    if (true) {
      lookupParMethodDecl_java_util_List_values.put(_parameters, lookupParMethodDecl_java_util_List_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return lookupParMethodDecl_java_util_List_value;
  }
  /**
   * @apilevel internal
   */
  private MethodDecl lookupParMethodDecl_compute(java.util.List typeArguments) {
      return newParMethodDecl(typeArguments);
    }
  /**
   * @attribute syn
   * @aspect GenericMethodsNameAnalysis
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericMethods.jrag:144
   */
  @ASTNodeAnnotation.Attribute
  public SimpleSet localLookupType(String name) {
    ASTNode$State state = state();
    try {
        for(int i = 0; i < getNumTypeParameter(); i++) {
          if(original().getTypeParameter(i).name().equals(name))
            return SimpleSet.emptySet.add(original().getTypeParameter(i));
        }
        return SimpleSet.emptySet;
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect FunctionalInterface
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/FunctionalInterface.jrag:36
   */
  @ASTNodeAnnotation.Attribute
  public boolean sameSignature(MethodDecl m) {
    ASTNode$State state = state();
    try {
    		if(!(m instanceof GenericMethodDecl))
    			return false;
    		GenericMethodDecl gm = (GenericMethodDecl)m;
    		if(!name().equals(gm.name()) || !sameTypeParameters(gm) || !sameFormalParameters(gm))
    			return false;
    		else
    			return true;
    	}
    finally {
    }
  }
  protected java.util.Map subsignatureTo_MethodDecl_values;
/**
 * @apilevel internal
 */
private void subsignatureTo_MethodDecl_reset() {
  subsignatureTo_MethodDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean subsignatureTo(MethodDecl m) {
    Object _parameters = m;
    if (subsignatureTo_MethodDecl_values == null) subsignatureTo_MethodDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(subsignatureTo_MethodDecl_values.containsKey(_parameters)) {
      return ((Boolean)subsignatureTo_MethodDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean subsignatureTo_MethodDecl_value = subsignatureTo_compute(m);
    if (isFinal && num == state().boundariesCrossed) {
      subsignatureTo_MethodDecl_values.put(_parameters, Boolean.valueOf(subsignatureTo_MethodDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return subsignatureTo_MethodDecl_value;
  }
  /**
   * @apilevel internal
   */
  private boolean subsignatureTo_compute(MethodDecl m) {
  		if(m instanceof GenericMethodDecl) {
  			GenericMethodDecl gm = (GenericMethodDecl)m;
  			if(this == gm)
  				return true;
  			if(!sameSignature(gm))
  				return false;
  			return true;
  		}
  		else {
  			//A generic method can never be subsignature to a non-generic method
  			return false;
  		}
  	}
  protected java.util.Map sameTypeParameters_GenericMethodDecl_values;
/**
 * @apilevel internal
 */
private void sameTypeParameters_GenericMethodDecl_reset() {
  sameTypeParameters_GenericMethodDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean sameTypeParameters(GenericMethodDecl gm) {
    Object _parameters = gm;
    if (sameTypeParameters_GenericMethodDecl_values == null) sameTypeParameters_GenericMethodDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(sameTypeParameters_GenericMethodDecl_values.containsKey(_parameters)) {
      return ((Boolean)sameTypeParameters_GenericMethodDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean sameTypeParameters_GenericMethodDecl_value = sameTypeParameters_compute(gm);
    if (isFinal && num == state().boundariesCrossed) {
      sameTypeParameters_GenericMethodDecl_values.put(_parameters, Boolean.valueOf(sameTypeParameters_GenericMethodDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return sameTypeParameters_GenericMethodDecl_value;
  }
  /**
   * @apilevel internal
   */
  private boolean sameTypeParameters_compute(GenericMethodDecl gm) {
  		if(getNumTypeParameter() != gm.getNumTypeParameter())
  			return false;
  
  		for(int i = 0; i < getNumTypeParameter(); i++) {
  			TypeVariable tv1 = getTypeParameter(i);
  			TypeVariable tv2 = gm.getTypeParameter(i);
  			if(tv1.getNumTypeBound() != tv2.getNumTypeBound())
  				return false;
  				
  			/* The bounds have to be the same in the way that a bound
  			that exists in type variable tv1 must exist exactly the same
  			number of times in tv2, but the order doesn't matter */
  			
  			boolean[] checkedBound = new boolean[tv1.getNumTypeBound()];
  			
  			for(int j = 0; j < tv1.getNumTypeBound(); j++) {
  				boolean found = false;
  				for(int k = 0; k < tv2.getNumTypeBound(); k++) {
  					if(checkedBound[k]) {
  						continue;
  					}
  					Access a1 = tv1.getTypeBound(j);
  					Access a2 = tv2.getTypeBound(k);
  					
  					if(a1.sameType(a2)) {
  						checkedBound[k] = true;
  						found = true;
  						break;
  					}
  				}
  				if(!found) {
  					return false;
  				}
  			}
  		}	
  		return true;
  	}
  protected java.util.Map sameFormalParameters_GenericMethodDecl_values;
/**
 * @apilevel internal
 */
private void sameFormalParameters_GenericMethodDecl_reset() {
  sameFormalParameters_GenericMethodDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean sameFormalParameters(GenericMethodDecl gm) {
    Object _parameters = gm;
    if (sameFormalParameters_GenericMethodDecl_values == null) sameFormalParameters_GenericMethodDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(sameFormalParameters_GenericMethodDecl_values.containsKey(_parameters)) {
      return ((Boolean)sameFormalParameters_GenericMethodDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean sameFormalParameters_GenericMethodDecl_value = sameFormalParameters_compute(gm);
    if (isFinal && num == state().boundariesCrossed) {
      sameFormalParameters_GenericMethodDecl_values.put(_parameters, Boolean.valueOf(sameFormalParameters_GenericMethodDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return sameFormalParameters_GenericMethodDecl_value;
  }
  /**
   * @apilevel internal
   */
  private boolean sameFormalParameters_compute(GenericMethodDecl gm) {
  		if(getNumParameter() != gm.getNumParameter())
  			return false;
  		if(getNumParameter() == 0)
  			return true;
  		
  		for(int i = 0; i < getNumParameter(); i++) {
  			ParameterDeclaration p1 = getParameter(i);
  			ParameterDeclaration p2 = gm.getParameter(i);
  			Access a1 = p1.getTypeAccess();
  			Access a2 = p2.getTypeAccess();
  			if(!a1.sameType(a2))
  				return false;
  		}
  		return true;
  	}
  /**
   * @apilevel internal
   */
  protected boolean usesTypeVariable_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean usesTypeVariable_value;
/**
 * @apilevel internal
 */
private void usesTypeVariable_reset() {
  usesTypeVariable_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean usesTypeVariable() {
    if(usesTypeVariable_computed) {
      return usesTypeVariable_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    usesTypeVariable_value = usesTypeVariable_compute();
    if (isFinal && num == state().boundariesCrossed) {
      usesTypeVariable_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return usesTypeVariable_value;
  }
  /**
   * @apilevel internal
   */
  private boolean usesTypeVariable_compute() { 
  		return	super.usesTypeVariable() || getTypeParameterList().usesTypeVariable();
  	}
  /**
   * @apilevel internal
   */
  protected boolean typeVariableInReturn_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean typeVariableInReturn_value;
/**
 * @apilevel internal
 */
private void typeVariableInReturn_reset() {
  typeVariableInReturn_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean typeVariableInReturn() {
    if(typeVariableInReturn_computed) {
      return typeVariableInReturn_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeVariableInReturn_value = typeVariableInReturn_compute();
    if (isFinal && num == state().boundariesCrossed) {
      typeVariableInReturn_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeVariableInReturn_value;
  }
  /**
   * @apilevel internal
   */
  private boolean typeVariableInReturn_compute() {
  		if(!getTypeAccess().usesTypeVariable())
  			return false;
  		ASTNode current = getTypeAccess();
  		LinkedList<ASTNode> list = new LinkedList<ASTNode>();
  		list.add(current);
  		boolean foundUse = false;
  		while(!list.isEmpty()) {
  			current = list.poll();
  			for(int i = 0; i < current.getNumChild(); i++) {
  				list.add(current.getChild(i));
  			}
  			if(current instanceof TypeAccess) {
  				TypeAccess typeAccess = (TypeAccess)current;
  				if(typeAccess.type().isTypeVariable()) {
  					for(int i = 0; i < getNumTypeParameter(); i++) {
  						if(typeAccess.type() == getTypeParameter(i)) {
  							foundUse = true;
  							break;
  						}
  					}
  					if(foundUse)
  						break;
  				}
  			}
  		}
  		return foundUse;
  	}
  /**
   * @attribute inh
   * @aspect GenericMethodsNameAnalysis
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericMethods.jrag:143
   */
  @ASTNodeAnnotation.Attribute
  public SimpleSet lookupType(String name) {
    ASTNode$State state = state();
    SimpleSet lookupType_String_value = getParent().Define_SimpleSet_lookupType(this, null, name);

    return lookupType_String_value;
  }
  /**
   * @attribute inh
   * @aspect TypeVariablePositions
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TypeVariablePositions.jrag:31
   */
  @ASTNodeAnnotation.Attribute
  public int genericMethodLevel() {
    if(genericMethodLevel_computed) {
      return genericMethodLevel_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    genericMethodLevel_value = getParent().Define_int_genericMethodLevel(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      genericMethodLevel_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return genericMethodLevel_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean genericMethodLevel_computed = false;
  /**
   * @apilevel internal
   */
  protected int genericMethodLevel_value;
/**
 * @apilevel internal
 */
private void genericMethodLevel_reset() {
  genericMethodLevel_computed = false;
}  
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericMethods.jrag:141
   * @apilevel internal
   */
  public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
    if (caller == getTypeParameterListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return NameType.TYPE_NAME;
    }
    else {
      return super.Define_NameType_nameType(caller, child);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericMethods.jrag:151
   * @apilevel internal
   */
  public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return localLookupType(name).isEmpty() ? lookupType(name) : localLookupType(name);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TypeVariablePositions.jrag:41
   * @apilevel internal
   */
  public int Define_int_typeVarPosition(ASTNode caller, ASTNode child) {
    if (caller == getTypeParameterListNoTransform()) {
      int i = caller.getIndexOfChild(child);
      return i;
    }
    else {
      return getParent().Define_int_typeVarPosition(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TypeVariablePositions.jrag:42
   * @apilevel internal
   */
  public boolean Define_boolean_typeVarInMethod(ASTNode caller, ASTNode child) {
    if (caller == getTypeParameterListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return true;
    }
    else {
      return getParent().Define_boolean_typeVarInMethod(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TypeVariablePositions.jrag:55
   * @apilevel internal
   */
  public int Define_int_genericMethodLevel(ASTNode caller, ASTNode child) {
    if (caller == getBlockOptNoTransform()) {
      return genericMethodLevel() + 1;
    }
    else if (caller == getTypeParameterListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return genericMethodLevel() + 1;
    }
    else {
      return getParent().Define_int_genericMethodLevel(this, caller);
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
