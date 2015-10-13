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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:74
 * @production ConstructorDecl : {@link BodyDecl} ::= <span class="component">{@link Modifiers}</span> <span class="component">&lt;ID:String&gt;</span> <span class="component">Parameter:{@link ParameterDeclaration}*</span> <span class="component">Exception:{@link Access}*</span> <span class="component">[ConstructorInvocation:{@link Stmt}]</span> <span class="component">{@link Block}</span>;

 */
public class ConstructorDecl extends BodyDecl implements Cloneable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:135
   */
  public void prettyPrint(StringBuffer sb) {
    if(isImplicitConstructor()) return;
    sb.append(indent());
    getModifiers().prettyPrint(sb);
    sb.append(name() + "(");
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

    sb.append(" {");
    if(hasConstructorInvocation()) {
      getConstructorInvocation().prettyPrint(sb);
    }
    for(int i = 0; i < getBlock().getNumStmt(); i++) {
      getBlock().getStmt(i).prettyPrint(sb);
    }
    sb.append(indent());
    sb.append("}");
  }
  /**
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:1079
   */
  public BodyDecl substitutedBodyDecl(Parameterization parTypeDecl) {
    ConstructorDecl c = new ConstructorDeclSubstituted(
      (Modifiers)getModifiers().fullCopy(),
      getID(),
      getParameterList().substitute(parTypeDecl),
      getExceptionList().substitute(parTypeDecl),
      new Opt(),
      new Block(),
      this
    );
    return c;
  }
  /**
   * @aspect ConstructorDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupConstructor.jrag:157
   */
  public boolean applicable(List argList) {
    if(getNumParameter() != argList.getNumChild())
      return false;
    for(int i = 0; i < getNumParameter(); i++) {
      TypeDecl arg = ((Expr)argList.getChild(i)).type();
      TypeDecl parameter = getParameter(i).type();
      if(!arg.instanceOf(parameter)) {
        return false;
      }
    }
    return true;
  }
  /**
   * Flag to indicate if this constructor is an auto-generated
   * default constructor. Implicit constructors are not pretty
   * printed.
   * @aspect ImplicitConstructor
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupConstructor.jrag:187
   */
  private boolean isImplicitConstructor = false;
  /**
   * Set the default constructor flag. Causes this constructor
   * to not be pretty printed.
   * @aspect ImplicitConstructor
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupConstructor.jrag:193
   */
  public void setImplicitConstructor() {
    isImplicitConstructor = true;
  }
  /**
   * @aspect Modifiers
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:110
   */
  public void checkModifiers() {
    super.checkModifiers();
  }
  /**
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:97
   */
  public void nameCheck() {
    super.nameCheck();
    // 8.8
    if(!hostType().name().equals(name()))
      error("constructor " + name() +" does not have the same name as the simple name of the host class " + hostType().name());

    // 8.8.2
    if(hostType().lookupConstructor(this) != this)
      error("constructor with signature " + signature() + " is multiply declared in type " + hostType().typeName());

    if(circularThisInvocation(this))
      error("The constructor " + signature() + " may not directly or indirectly invoke itself");
  }
  /**
   * @aspect TypeCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeCheck.jrag:427
   */
  public void typeCheck() {
    // 8.8.4 (8.4.4)
    TypeDecl exceptionType = typeThrowable();
    for(int i = 0; i < getNumException(); i++) {
      TypeDecl typeDecl = getException(i).type();
      if(!typeDecl.instanceOf(exceptionType))
        error(signature() + " throws non throwable type " + typeDecl.fullName());
    }
  }
  /**
   * @aspect Enums
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Enums.jrag:141
   */
  protected void transformEnumConstructors() {
    // make sure constructor is private
    Modifiers newModifiers = new Modifiers(new List());
    for (int i = 0; i < getModifiers().getNumModifier(); ++i) {
      String modifier = getModifiers().getModifier(i).getID();
      if (modifier.equals("public") || modifier.equals("private") ||
        modifier.equals("protected"))
          continue;
      newModifiers.addModifier(new Modifier(modifier));
    }
    newModifiers.addModifier(new Modifier("private"));
    setModifiers(newModifiers);

    // add implicit super constructor access since we are traversing
    // without doing rewrites
    if(!hasConstructorInvocation()) {
      setConstructorInvocation(
        new ExprStmt(
          new SuperConstructorAccess("super", new List())
        )
      );
    }
    super.transformEnumConstructors();
    getParameterList().insertChild(
      new ParameterDeclaration(new TypeAccess("java.lang", "String"), "@p0"),
      0
    );
    getParameterList().insertChild(
      new ParameterDeclaration(new TypeAccess("int"), "@p1"),
      1
    );
  }
  /**
   * @declaredat ASTNode:1
   */
  public ConstructorDecl() {
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
    children = new ASTNode[5];
    setChild(new List(), 1);
    setChild(new List(), 2);
    setChild(new Opt(), 3);
  }
  /**
   * @declaredat ASTNode:16
   */
  public ConstructorDecl(Modifiers p0, String p1, List<ParameterDeclaration> p2, List<Access> p3, Opt<Stmt> p4, Block p5) {
    setChild(p0, 0);
    setID(p1);
    setChild(p2, 1);
    setChild(p3, 2);
    setChild(p4, 3);
    setChild(p5, 4);
  }
  /**
   * @declaredat ASTNode:24
   */
  public ConstructorDecl(Modifiers p0, beaver.Symbol p1, List<ParameterDeclaration> p2, List<Access> p3, Opt<Stmt> p4, Block p5) {
    setChild(p0, 0);
    setID(p1);
    setChild(p2, 1);
    setChild(p3, 2);
    setChild(p4, 3);
    setChild(p5, 4);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:35
   */
  protected int numChildren() {
    return 5;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:41
   */
  public boolean mayHaveRewrite() {
    return true;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:47
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    sourceConstructorDecl_reset();
    accessibleFrom_TypeDecl_reset();
    isDAafter_Variable_reset();
    isDUafter_Variable_reset();
    throwsException_TypeDecl_reset();
    name_reset();
    signature_reset();
    sameSignature_ConstructorDecl_reset();
    moreSpecificThan_ConstructorDecl_reset();
    parameterDeclaration_String_reset();
    circularThisInvocation_ConstructorDecl_reset();
    handlesException_TypeDecl_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:65
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:71
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:77
   */
  public ConstructorDecl clone() throws CloneNotSupportedException {
    ConstructorDecl node = (ConstructorDecl) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:84
   */
  public ConstructorDecl copy() {
    try {
      ConstructorDecl node = (ConstructorDecl) clone();
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
   * @declaredat ASTNode:103
   */
  public ConstructorDecl fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:112
   */
  public ConstructorDecl treeCopyNoTransform() {
    ConstructorDecl tree = (ConstructorDecl) copy();
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
   * @declaredat ASTNode:132
   */
  public ConstructorDecl treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:139
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_ID == ((ConstructorDecl)node).tokenString_ID);    
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
   * Replaces the Parameter list.
   * @param list The new list node to be used as the Parameter list.
   * @apilevel high-level
   */
  public void setParameterList(List<ParameterDeclaration> list) {
    setChild(list, 1);
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
    List<ParameterDeclaration> list = (List<ParameterDeclaration>) getChild(1);
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
    return (List<ParameterDeclaration>) getChildNoTransform(1);
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
    setChild(list, 2);
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
    List<Access> list = (List<Access>) getChild(2);
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
    return (List<Access>) getChildNoTransform(2);
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
   * Replaces the optional node for the ConstructorInvocation child. This is the <code>Opt</code>
   * node containing the child ConstructorInvocation, not the actual child!
   * @param opt The new node to be used as the optional node for the ConstructorInvocation child.
   * @apilevel low-level
   */
  public void setConstructorInvocationOpt(Opt<Stmt> opt) {
    setChild(opt, 3);
  }
  /**
   * Replaces the (optional) ConstructorInvocation child.
   * @param node The new node to be used as the ConstructorInvocation child.
   * @apilevel high-level
   */
  public void setConstructorInvocation(Stmt node) {
    getConstructorInvocationOpt().setChild(node, 0);
  }
  /**
   * Check whether the optional ConstructorInvocation child exists.
   * @return {@code true} if the optional ConstructorInvocation child exists, {@code false} if it does not.
   * @apilevel high-level
   */
  public boolean hasConstructorInvocation() {
    return getConstructorInvocationOpt().getNumChild() != 0;
  }
  /**
   * Retrieves the (optional) ConstructorInvocation child.
   * @return The ConstructorInvocation child, if it exists. Returns {@code null} otherwise.
   * @apilevel low-level
   */
  public Stmt getConstructorInvocation() {
    return (Stmt) getConstructorInvocationOpt().getChild(0);
  }
  /**
   * Retrieves the optional node for the ConstructorInvocation child. This is the <code>Opt</code> node containing the child ConstructorInvocation, not the actual child!
   * @return The optional node for child the ConstructorInvocation child.
   * @apilevel low-level
   */
  @ASTNodeAnnotation.OptChild(name="ConstructorInvocation")
  public Opt<Stmt> getConstructorInvocationOpt() {
    return (Opt<Stmt>) getChild(3);
  }
  /**
   * Retrieves the optional node for child ConstructorInvocation. This is the <code>Opt</code> node containing the child ConstructorInvocation, not the actual child!
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The optional node for child ConstructorInvocation.
   * @apilevel low-level
   */
  public Opt<Stmt> getConstructorInvocationOptNoTransform() {
    return (Opt<Stmt>) getChildNoTransform(3);
  }
  /**
   * Replaces the Block child.
   * @param node The new node to replace the Block child.
   * @apilevel high-level
   */
  public void setBlock(Block node) {
    setChild(node, 4);
  }
  /**
   * Retrieves the Block child.
   * @return The current node used as the Block child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Block")
  public Block getBlock() {
    return (Block) getChild(4);
  }
  /**
   * Retrieves the Block child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Block child.
   * @apilevel low-level
   */
  public Block getBlockNoTransform() {
    return (Block) getChildNoTransform(4);
  }
  /**
   * @aspect ConstructorDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupConstructor.jrag:149
   */
  private boolean refined_ConstructorDecl_ConstructorDecl_moreSpecificThan_ConstructorDecl(ConstructorDecl m)
{
    for(int i = 0; i < getNumParameter(); i++) {
      if(!getParameter(i).type().instanceOf(m.getParameter(i).type()))
        return false;
    }
    return true;
  }
  /**
   * @apilevel internal
   */
  protected boolean sourceConstructorDecl_computed = false;
  /**
   * @apilevel internal
   */
  protected ConstructorDecl sourceConstructorDecl_value;
/**
 * @apilevel internal
 */
private void sourceConstructorDecl_reset() {
  sourceConstructorDecl_computed = false;
  sourceConstructorDecl_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public ConstructorDecl sourceConstructorDecl() {
    if(sourceConstructorDecl_computed) {
      return sourceConstructorDecl_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    sourceConstructorDecl_value = this;
    if (isFinal && num == state().boundariesCrossed) {
      sourceConstructorDecl_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return sourceConstructorDecl_value;
  }
  protected java.util.Map accessibleFrom_TypeDecl_values;
/**
 * @apilevel internal
 */
private void accessibleFrom_TypeDecl_reset() {
  accessibleFrom_TypeDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean accessibleFrom(TypeDecl type) {
    Object _parameters = type;
    if (accessibleFrom_TypeDecl_values == null) accessibleFrom_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(accessibleFrom_TypeDecl_values.containsKey(_parameters)) {
      return ((Boolean)accessibleFrom_TypeDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean accessibleFrom_TypeDecl_value = accessibleFrom_compute(type);
    if (isFinal && num == state().boundariesCrossed) {
      accessibleFrom_TypeDecl_values.put(_parameters, Boolean.valueOf(accessibleFrom_TypeDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return accessibleFrom_TypeDecl_value;
  }
  /**
   * @apilevel internal
   */
  private boolean accessibleFrom_compute(TypeDecl type) {
      if(!hostType().accessibleFrom(type))
        return false;
      else if(isPublic())
        return true;
      else if(isProtected()) {
        return true;
      }
      else if(isPrivate()) {
        return hostType().topLevelType() == type.topLevelType();
      }
      else
        return hostPackage().equals(type.hostPackage());
    }
  protected java.util.Map isDAafter_Variable_values;
/**
 * @apilevel internal
 */
private void isDAafter_Variable_reset() {
  isDAafter_Variable_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isDAafter(Variable v) {
    Object _parameters = v;
    if (isDAafter_Variable_values == null) isDAafter_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(isDAafter_Variable_values.containsKey(_parameters)) {
      return ((Boolean)isDAafter_Variable_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean isDAafter_Variable_value = getBlock().isDAafter(v) && getBlock().checkReturnDA(v);
    if (isFinal && num == state().boundariesCrossed) {
      isDAafter_Variable_values.put(_parameters, Boolean.valueOf(isDAafter_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDAafter_Variable_value;
  }
  protected java.util.Map isDUafter_Variable_values;
/**
 * @apilevel internal
 */
private void isDUafter_Variable_reset() {
  isDUafter_Variable_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isDUafter(Variable v) {
    Object _parameters = v;
    if (isDUafter_Variable_values == null) isDUafter_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(isDUafter_Variable_values.containsKey(_parameters)) {
      return ((Boolean)isDUafter_Variable_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean isDUafter_Variable_value = getBlock().isDUafter(v) && getBlock().checkReturnDU(v);
    if (isFinal && num == state().boundariesCrossed) {
      isDUafter_Variable_values.put(_parameters, Boolean.valueOf(isDUafter_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDUafter_Variable_value;
  }
  protected java.util.Map throwsException_TypeDecl_values;
/**
 * @apilevel internal
 */
private void throwsException_TypeDecl_reset() {
  throwsException_TypeDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean throwsException(TypeDecl exceptionType) {
    Object _parameters = exceptionType;
    if (throwsException_TypeDecl_values == null) throwsException_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(throwsException_TypeDecl_values.containsKey(_parameters)) {
      return ((Boolean)throwsException_TypeDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean throwsException_TypeDecl_value = throwsException_compute(exceptionType);
    if (isFinal && num == state().boundariesCrossed) {
      throwsException_TypeDecl_values.put(_parameters, Boolean.valueOf(throwsException_TypeDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return throwsException_TypeDecl_value;
  }
  /**
   * @apilevel internal
   */
  private boolean throwsException_compute(TypeDecl exceptionType) {
      for(int i = 0; i < getNumException(); i++)
        if(exceptionType.instanceOf(getException(i).type()))
          return true;
      return false;
    }
  /**
   * @apilevel internal
   */
  protected boolean name_computed = false;
  /**
   * @apilevel internal
   */
  protected String name_value;
/**
 * @apilevel internal
 */
private void name_reset() {
  name_computed = false;
  name_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public String name() {
    if(name_computed) {
      return name_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    name_value = getID();
    if (isFinal && num == state().boundariesCrossed) {
      name_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return name_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean signature_computed = false;
  /**
   * @apilevel internal
   */
  protected String signature_value;
/**
 * @apilevel internal
 */
private void signature_reset() {
  signature_computed = false;
  signature_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public String signature() {
    if(signature_computed) {
      return signature_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    signature_value = signature_compute();
    if (isFinal && num == state().boundariesCrossed) {
      signature_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return signature_value;
  }
  /**
   * @apilevel internal
   */
  private String signature_compute() {
      StringBuffer s = new StringBuffer();
      s.append(name() + "(");
      for(int i = 0; i < getNumParameter(); i++) {
        s.append(getParameter(i));
        if(i != getNumParameter() - 1)
          s.append(", ");
      }
      s.append(")");
      return s.toString();
    }
  protected java.util.Map sameSignature_ConstructorDecl_values;
/**
 * @apilevel internal
 */
private void sameSignature_ConstructorDecl_reset() {
  sameSignature_ConstructorDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean sameSignature(ConstructorDecl c) {
    Object _parameters = c;
    if (sameSignature_ConstructorDecl_values == null) sameSignature_ConstructorDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(sameSignature_ConstructorDecl_values.containsKey(_parameters)) {
      return ((Boolean)sameSignature_ConstructorDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean sameSignature_ConstructorDecl_value = sameSignature_compute(c);
    if (isFinal && num == state().boundariesCrossed) {
      sameSignature_ConstructorDecl_values.put(_parameters, Boolean.valueOf(sameSignature_ConstructorDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return sameSignature_ConstructorDecl_value;
  }
  /**
   * @apilevel internal
   */
  private boolean sameSignature_compute(ConstructorDecl c) {
      if(!name().equals(c.name()))
        return false;
      if(c.getNumParameter() != getNumParameter())
        return false;
      for(int i = 0; i < getNumParameter(); i++)
        if(!c.getParameter(i).type().equals(getParameter(i).type()))
          return false;
      return true;
    }
  protected java.util.Map moreSpecificThan_ConstructorDecl_values;
/**
 * @apilevel internal
 */
private void moreSpecificThan_ConstructorDecl_reset() {
  moreSpecificThan_ConstructorDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean moreSpecificThan(ConstructorDecl m) {
    Object _parameters = m;
    if (moreSpecificThan_ConstructorDecl_values == null) moreSpecificThan_ConstructorDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(moreSpecificThan_ConstructorDecl_values.containsKey(_parameters)) {
      return ((Boolean)moreSpecificThan_ConstructorDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean moreSpecificThan_ConstructorDecl_value = moreSpecificThan_compute(m);
    if (isFinal && num == state().boundariesCrossed) {
      moreSpecificThan_ConstructorDecl_values.put(_parameters, Boolean.valueOf(moreSpecificThan_ConstructorDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return moreSpecificThan_ConstructorDecl_value;
  }
  /**
   * @apilevel internal
   */
  private boolean moreSpecificThan_compute(ConstructorDecl m) {
      if(!isVariableArity() && !m.isVariableArity())
        return refined_ConstructorDecl_ConstructorDecl_moreSpecificThan_ConstructorDecl(m);
      int num = Math.max(getNumParameter(), m.getNumParameter());
      for(int i = 0; i < num; i++) {
        TypeDecl t1 = i < getNumParameter() - 1 ? getParameter(i).type() : getParameter(getNumParameter()-1).type().componentType();
        TypeDecl t2 = i < m.getNumParameter() - 1 ? m.getParameter(i).type() : m.getParameter(m.getNumParameter()-1).type().componentType();
        if(!t1.instanceOf(t2))
          return false;
      }
      return true;
    }
  @ASTNodeAnnotation.Attribute
  public boolean isImplicitConstructor() {
    ASTNode$State state = state();
    boolean isImplicitConstructor_value = isImplicitConstructor;

    return isImplicitConstructor_value;
  }
  protected java.util.Map parameterDeclaration_String_values;
/**
 * @apilevel internal
 */
private void parameterDeclaration_String_reset() {
  parameterDeclaration_String_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public SimpleSet parameterDeclaration(String name) {
    Object _parameters = name;
    if (parameterDeclaration_String_values == null) parameterDeclaration_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(parameterDeclaration_String_values.containsKey(_parameters)) {
      return (SimpleSet)parameterDeclaration_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    SimpleSet parameterDeclaration_String_value = parameterDeclaration_compute(name);
    if (isFinal && num == state().boundariesCrossed) {
      parameterDeclaration_String_values.put(_parameters, parameterDeclaration_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return parameterDeclaration_String_value;
  }
  /**
   * @apilevel internal
   */
  private SimpleSet parameterDeclaration_compute(String name) {
      for(int i = 0; i < getNumParameter(); i++)
        if(getParameter(i).name().equals(name))
          return (ParameterDeclaration)getParameter(i);
      return SimpleSet.emptySet;
    }
  @ASTNodeAnnotation.Attribute
  public boolean isSynthetic() {
    ASTNode$State state = state();
    boolean isSynthetic_value = getModifiers().isSynthetic();

    return isSynthetic_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isPublic() {
    ASTNode$State state = state();
    boolean isPublic_value = getModifiers().isPublic();

    return isPublic_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isPrivate() {
    ASTNode$State state = state();
    boolean isPrivate_value = getModifiers().isPrivate();

    return isPrivate_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isProtected() {
    ASTNode$State state = state();
    boolean isProtected_value = getModifiers().isProtected();

    return isProtected_value;
  }
/**
 * @apilevel internal
 */
private void circularThisInvocation_ConstructorDecl_reset() {
  circularThisInvocation_ConstructorDecl_values = null;
}  
  protected java.util.Map circularThisInvocation_ConstructorDecl_values;
  @ASTNodeAnnotation.Attribute
  public boolean circularThisInvocation(ConstructorDecl decl) {
    Object _parameters = decl;
    if (circularThisInvocation_ConstructorDecl_values == null) circularThisInvocation_ConstructorDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    ASTNode$State.CircularValue _value;
    if(circularThisInvocation_ConstructorDecl_values.containsKey(_parameters)) {
      Object _o = circularThisInvocation_ConstructorDecl_values.get(_parameters);
      if(!(_o instanceof ASTNode$State.CircularValue)) {
        return ((Boolean)_o).booleanValue();
      } else {
        _value = (ASTNode$State.CircularValue) _o;
      }
    } else {
      _value = new ASTNode$State.CircularValue();
      circularThisInvocation_ConstructorDecl_values.put(_parameters, _value);
      _value.value = Boolean.valueOf(true);
    }
    ASTNode$State state = state();
    boolean new_circularThisInvocation_ConstructorDecl_value;
    if (!state.IN_CIRCLE) {
      state.IN_CIRCLE = true;
      int num = state.boundariesCrossed;
      boolean isFinal = this.is$Final();
      // TODO: fixme
      // state().CIRCLE_INDEX = 1;
      do {
        _value.visited = new Integer(state.CIRCLE_INDEX);
        state.CHANGE = false;
        new_circularThisInvocation_ConstructorDecl_value = circularThisInvocation_compute(decl);
        if (new_circularThisInvocation_ConstructorDecl_value != ((Boolean)_value.value).booleanValue()) {
          state.CHANGE = true;
          _value.value = Boolean.valueOf(new_circularThisInvocation_ConstructorDecl_value);
        }
        state.CIRCLE_INDEX++;
      } while (state.CHANGE);
      if (isFinal && num == state().boundariesCrossed) {
        circularThisInvocation_ConstructorDecl_values.put(_parameters, new_circularThisInvocation_ConstructorDecl_value);
      } else {
        circularThisInvocation_ConstructorDecl_values.remove(_parameters);
        state.RESET_CYCLE = true;
        boolean $tmp = circularThisInvocation_compute(decl);
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_circularThisInvocation_ConstructorDecl_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_circularThisInvocation_ConstructorDecl_value = circularThisInvocation_compute(decl);
      if (state.RESET_CYCLE) {
        circularThisInvocation_ConstructorDecl_values.remove(_parameters);
      }
      else if (new_circularThisInvocation_ConstructorDecl_value != ((Boolean)_value.value).booleanValue()) {
        state.CHANGE = true;
        _value.value = new_circularThisInvocation_ConstructorDecl_value;
      }
      state.INTERMEDIATE_VALUE = true;
      return new_circularThisInvocation_ConstructorDecl_value;
    }
    state.INTERMEDIATE_VALUE = true;
    return ((Boolean)_value.value).booleanValue();
  }
  /**
   * @apilevel internal
   */
  private boolean circularThisInvocation_compute(ConstructorDecl decl) {
      if(hasConstructorInvocation()) {
        Expr e = ((ExprStmt)getConstructorInvocation()).getExpr();
        if(e instanceof ConstructorAccess) {
          ConstructorDecl constructorDecl = ((ConstructorAccess)e).decl();
          if(constructorDecl == decl)
            return true;
          return constructorDecl.circularThisInvocation(decl);
        }
      }
      return false;
    }
  @ASTNodeAnnotation.Attribute
  public TypeDecl type() {
    ASTNode$State state = state();
    TypeDecl type_value = unknownType();

    return type_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isVoid() {
    ASTNode$State state = state();
    boolean isVoid_value = true;

    return isVoid_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean hasAnnotationSuppressWarnings(String annot) {
    ASTNode$State state = state();
    boolean hasAnnotationSuppressWarnings_String_value = getModifiers().hasAnnotationSuppressWarnings(annot);

    return hasAnnotationSuppressWarnings_String_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isDeprecated() {
    ASTNode$State state = state();
    boolean isDeprecated_value = getModifiers().hasDeprecatedAnnotation();

    return isDeprecated_value;
  }
  /**
   * @attribute syn
   * @aspect MethodSignature15
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/MethodSignature.jrag:190
   */
  @ASTNodeAnnotation.Attribute
  public boolean applicableBySubtyping(List argList) {
    ASTNode$State state = state();
    try {
        if(getNumParameter() != argList.getNumChild())
          return false;
        for(int i = 0; i < getNumParameter(); i++) {
          TypeDecl arg = ((Expr)argList.getChild(i)).type();
          if(!arg.instanceOf(getParameter(i).type()))
            return false;
        }
        return true;
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect MethodSignature15
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/MethodSignature.jrag:210
   */
  @ASTNodeAnnotation.Attribute
  public boolean applicableByMethodInvocationConversion(List argList) {
    ASTNode$State state = state();
    try {
        if(getNumParameter() != argList.getNumChild())
          return false;
        for(int i = 0; i < getNumParameter(); i++) {
          TypeDecl arg = ((Expr)argList.getChild(i)).type();
          if(!arg.methodInvocationConversionTo(getParameter(i).type()))
            return false;
        }
        return true;
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect MethodSignature15
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/MethodSignature.jrag:231
   */
  @ASTNodeAnnotation.Attribute
  public boolean applicableVariableArity(List argList) {
    ASTNode$State state = state();
    try {
        for(int i = 0; i < getNumParameter() - 1; i++) {
          TypeDecl arg = ((Expr)argList.getChild(i)).type();
          if(!arg.methodInvocationConversionTo(getParameter(i).type()))
            return false;
        }
        for(int i = getNumParameter() - 1; i < argList.getNumChild(); i++) {
          TypeDecl arg = ((Expr)argList.getChild(i)).type();
          if(!arg.methodInvocationConversionTo(lastParameter().type().componentType()))
            return false;
        }
        return true;
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect MethodSignature15
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/MethodSignature.jrag:318
   */
  @ASTNodeAnnotation.Attribute
  public boolean potentiallyApplicable(List argList) {
    ASTNode$State state = state();
    try {
    		int argArity = argList.getNumChild();
    		if(!isVariableArity()) {
    			if(argArity != arity())
    				return false;
    			for(int i = 0; i < argArity; i++) {
    				Expr expr = (Expr)argList.getChild(i);
    				if(!expr.potentiallyCompatible(getParameter(i).type(), this))
    					return false;
    			}
    		}
    		if(isVariableArity()) {
    			if(!(argArity >= arity()-1))
    				return false;
    			for(int i = 0; i < arity() - 2; i++) {
    				Expr expr = (Expr)argList.getChild(i);
    				if(!expr.potentiallyCompatible(getParameter(i).type(), this))
    					return false;
    			}
    			TypeDecl varArgType = getParameter(arity()-1).type();
    			if(argArity == arity()) {
    				Expr expr = (Expr)argList.getChild(argArity - 1);
    				if(!expr.potentiallyCompatible(varArgType, this) &&
    						!expr.potentiallyCompatible(varArgType.componentType(), this))
    					return false;
    			}
    			else if(argArity > arity()) {
    				for(int i = arity()-1; i < argArity; i++) {
    					Expr expr = (Expr)argList.getChild(i);
    					if(!expr.potentiallyCompatible(varArgType.componentType(), this))
    						return false;
    				}
    			}
    		}
    		
    		return true;
    	}
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public int arity() {
    ASTNode$State state = state();
    int arity_value = getNumParameter();

    return arity_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isVariableArity() {
    ASTNode$State state = state();
    boolean isVariableArity_value = getNumParameter() == 0 ? false : getParameter(getNumParameter()-1).isVariableArity();

    return isVariableArity_value;
  }
  @ASTNodeAnnotation.Attribute
  public ParameterDeclaration lastParameter() {
    ASTNode$State state = state();
    ParameterDeclaration lastParameter_value = getParameter(getNumParameter() - 1);

    return lastParameter_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean hasAnnotationSafeVarargs() {
    ASTNode$State state = state();
    boolean hasAnnotationSafeVarargs_value = getModifiers().hasAnnotationSafeVarargs();

    return hasAnnotationSafeVarargs_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean hasIllegalAnnotationSafeVarargs() {
    ASTNode$State state = state();
    boolean hasIllegalAnnotationSafeVarargs_value = hasAnnotationSafeVarargs() && !isVariableArity();

    return hasIllegalAnnotationSafeVarargs_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean modifiedInScope(Variable var) {
    ASTNode$State state = state();
    boolean modifiedInScope_Variable_value = getBlock().modifiedInScope(var);

    return modifiedInScope_Variable_value;
  }
  /**
   * @attribute syn
   * @aspect MethodSignature18
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/MethodSignature.jrag:880
   */
  @ASTNodeAnnotation.Attribute
  public boolean applicableByStrictInvocation(Expr expr, List argList) {
    ASTNode$State state = state();
    try {
    		if(getNumParameter() != argList.getNumChild())
    			return false;
    		for(int i = 0; i < getNumParameter(); i++) {
    			Expr arg = (Expr)argList.getChild(i);
    			if(!arg.pertinentToApplicability(expr, this, i)) {
    				continue;
    			}
    			if(!arg.compatibleStrictContext(getParameter(i).type()))
    				return false;
    		}
    		return true;
    	}
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect MethodSignature18
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/MethodSignature.jrag:894
   */
  @ASTNodeAnnotation.Attribute
  public boolean applicableByLooseInvocation(Expr expr, List argList) {
    ASTNode$State state = state();
    try {
    		if(getNumParameter() != argList.getNumChild())
    			return false;
    		for(int i = 0; i < getNumParameter(); i++) {
    			Expr arg = (Expr)argList.getChild(i);
    			if(!arg.pertinentToApplicability(expr, this, i))
    				continue;
    			if(!arg.compatibleLooseContext(getParameter(i).type()))
    				return false;
    		}
    		return true;
    	}
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect MethodSignature18
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/MethodSignature.jrag:907
   */
  @ASTNodeAnnotation.Attribute
  public boolean applicableByVariableArityInvocation(Expr expr, List argList) {
    ASTNode$State state = state();
    try {
    		for(int i = 0; i < getNumParameter() - 1; i++) {
    			Expr arg = (Expr)argList.getChild(i);
    			if(!arg.pertinentToApplicability(expr, this, i))
    				continue;
    			if(!arg.compatibleLooseContext(getParameter(i).type()))
    				return false;
    		}
    		for(int i = getNumParameter() - 1; i < argList.getNumChild(); i++) {
    			Expr arg = (Expr)argList.getChild(i);
    			if(!arg.pertinentToApplicability(expr, this, i))
    				continue;
    			if(!arg.compatibleLooseContext(lastParameter().type().componentType()))
    				return false;
    		}
    		return true;
    	}
    finally {
    }
  }
  /**
   * @attribute inh
   * @aspect ExceptionHandling
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ExceptionHandling.jrag:56
   */
  @ASTNodeAnnotation.Attribute
  public boolean handlesException(TypeDecl exceptionType) {
    Object _parameters = exceptionType;
    if (handlesException_TypeDecl_values == null) handlesException_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(handlesException_TypeDecl_values.containsKey(_parameters)) {
      return ((Boolean)handlesException_TypeDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean handlesException_TypeDecl_value = getParent().Define_boolean_handlesException(this, null, exceptionType);
    if (isFinal && num == state().boundariesCrossed) {
      handlesException_TypeDecl_values.put(_parameters, Boolean.valueOf(handlesException_TypeDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return handlesException_TypeDecl_value;
  }
  protected java.util.Map handlesException_TypeDecl_values;
/**
 * @apilevel internal
 */
private void handlesException_TypeDecl_reset() {
  handlesException_TypeDecl_values = null;
}  
  /**
   * @attribute inh
   * @aspect TypeAnalysis
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:269
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl unknownType() {
    ASTNode$State state = state();
    TypeDecl unknownType_value = getParent().Define_TypeDecl_unknownType(this, null);

    return unknownType_value;
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:313
   * @apilevel internal
   */
  public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
    if (caller == getBlockNoTransform()) {
      return hasConstructorInvocation() ? getConstructorInvocation().isDAafter(v) : isDAbefore(v);
    }
    else {
      return super.Define_boolean_isDAbefore(caller, child, v);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:790
   * @apilevel internal
   */
  public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
    if (caller == getBlockNoTransform()) {
      return hasConstructorInvocation() ? getConstructorInvocation().isDUafter(v) : isDUbefore(v);
    }
    else {
      return super.Define_boolean_isDUbefore(caller, child, v);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ExceptionHandling.jrag:157
   * @apilevel internal
   */
  public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
    if (caller == getConstructorInvocationOptNoTransform()) {
      return throwsException(exceptionType) || handlesException(exceptionType);
    }
    else if (caller == getBlockNoTransform()) {
      return throwsException(exceptionType) || handlesException(exceptionType);
    }
    else {
      return getParent().Define_boolean_handlesException(this, caller, exceptionType);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupMethod.jrag:65
   * @apilevel internal
   */
  public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
    if (caller == getConstructorInvocationOptNoTransform()){
    Collection c = new ArrayList();
    for(Iterator iter = lookupMethod(name).iterator(); iter.hasNext(); ) {
      MethodDecl m = (MethodDecl)iter.next();
      if(!hostType().memberMethods(name).contains(m) || m.isStatic())
        c.add(m);
    }
    return c;
  }
    else {
      return getParent().Define_Collection_lookupMethod(this, caller, name);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupVariable.jrag:65
   * @apilevel internal
   */
  public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
    if (caller == getParameterListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return parameterDeclaration(name);
    }
    else if (caller == getConstructorInvocationOptNoTransform()){
    SimpleSet set = parameterDeclaration(name);
    if(!set.isEmpty()) return set;
    for(Iterator iter = lookupVariable(name).iterator(); iter.hasNext(); ) {
      Variable v = (Variable)iter.next();
      if(!hostType().memberFields(name).contains(v) || v.isStatic())
        set = set.add(v);
    }
    return set;
  }
    else if (caller == getBlockNoTransform()){
    SimpleSet set = parameterDeclaration(name);
    if(!set.isEmpty()) return set;
    return lookupVariable(name);
  }
    else {
      return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:282
   * @apilevel internal
   */
  public boolean Define_boolean_mayBePublic(ASTNode caller, ASTNode child) {
    if (caller == getModifiersNoTransform()) {
      return true;
    }
    else {
      return getParent().Define_boolean_mayBePublic(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:283
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeProtected(ASTNode caller, ASTNode child) {
    if (caller == getModifiersNoTransform()) {
      return true;
    }
    else {
      return getParent().Define_boolean_mayBeProtected(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:284
   * @apilevel internal
   */
  public boolean Define_boolean_mayBePrivate(ASTNode caller, ASTNode child) {
    if (caller == getModifiersNoTransform()) {
      return true;
    }
    else {
      return getParent().Define_boolean_mayBePrivate(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:273
   * @apilevel internal
   */
  public ASTNode Define_ASTNode_enclosingBlock(ASTNode caller, ASTNode child) {
    if (caller == getBlockNoTransform()) {
      return this;
    }
    else {
      return getParent().Define_ASTNode_enclosingBlock(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/SyntacticClassification.jrag:117
   * @apilevel internal
   */
  public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
    if (caller == getConstructorInvocationOptNoTransform()) {
      return NameType.EXPRESSION_NAME;
    }
    else if (caller == getExceptionListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return NameType.TYPE_NAME;
    }
    else if (caller == getParameterListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return NameType.TYPE_NAME;
    }
    else {
      return getParent().Define_NameType_nameType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeCheck.jrag:521
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_enclosingInstance(ASTNode caller, ASTNode child) {
    if (caller == getConstructorInvocationOptNoTransform()) {
      return unknownType();
    }
    else {
      return getParent().Define_TypeDecl_enclosingInstance(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeHierarchyCheck.jrag:151
   * @apilevel internal
   */
  public boolean Define_boolean_inExplicitConstructorInvocation(ASTNode caller, ASTNode child) {
    if (caller == getConstructorInvocationOptNoTransform()) {
      return true;
    }
    else {
      return getParent().Define_boolean_inExplicitConstructorInvocation(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeHierarchyCheck.jrag:159
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_enclosingExplicitConstructorHostType(ASTNode caller, ASTNode child) {
    if (caller == getConstructorInvocationOptNoTransform()) {
      return hostType();
    }
    else {
      return getParent().Define_TypeDecl_enclosingExplicitConstructorHostType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeHierarchyCheck.jrag:171
   * @apilevel internal
   */
  public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
    if (caller == getConstructorInvocationOptNoTransform()) {
      return false;
    }
    else if (caller == getBlockNoTransform()) {
      return false;
    }
    else {
      return getParent().Define_boolean_inStaticContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/UnreachableStatements.jrag:32
   * @apilevel internal
   */
  public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
    if (caller == getBlockNoTransform()) {
      return !hasConstructorInvocation() ? true : getConstructorInvocation().canCompleteNormally();
    }
    else if (caller == getConstructorInvocationOptNoTransform()) {
      return true;
    }
    else {
      return getParent().Define_boolean_reachable(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/VariableDeclaration.jrag:58
   * @apilevel internal
   */
  public boolean Define_boolean_isMethodParameter(ASTNode caller, ASTNode child) {
    if (caller == getParameterListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else {
      return getParent().Define_boolean_isMethodParameter(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/VariableDeclaration.jrag:59
   * @apilevel internal
   */
  public boolean Define_boolean_isConstructorParameter(ASTNode caller, ASTNode child) {
    if (caller == getParameterListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return true;
    }
    else {
      return getParent().Define_boolean_isConstructorParameter(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/VariableDeclaration.jrag:60
   * @apilevel internal
   */
  public boolean Define_boolean_isExceptionHandlerParameter(ASTNode caller, ASTNode child) {
    if (caller == getParameterListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else {
      return getParent().Define_boolean_isExceptionHandlerParameter(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:89
   * @apilevel internal
   */
  public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
    if (caller == getModifiersNoTransform()) {
      return name.equals("CONSTRUCTOR");
    }
    else {
      return getParent().Define_boolean_mayUseAnnotationTarget(this, caller, name);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/VariableArityParameters.jrag:21
   * @apilevel internal
   */
  public boolean Define_boolean_variableArityValid(ASTNode caller, ASTNode child) {
    if (caller == getParameterListNoTransform()) {
      int i = caller.getIndexOfChild(child);
      return i == getNumParameter() - 1;
    }
    else {
      return getParent().Define_boolean_variableArityValid(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/EffectivelyFinal.jrag:61
   * @apilevel internal
   */
  public boolean Define_boolean_inhModifiedInScope(ASTNode caller, ASTNode child, Variable var) {
    if (caller == getConstructorInvocationOptNoTransform()) {
      return false;
    }
    else if (caller == getParameterListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      {
		return getBlock().modifiedInScope(var) || getConstructorInvocation().modifiedInScope(var);
	}
    }
    else {
      return getParent().Define_boolean_inhModifiedInScope(this, caller, var);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/PreciseRethrow.jrag:180
   * @apilevel internal
   */
  public boolean Define_boolean_isCatchParam(ASTNode caller, ASTNode child) {
    if (caller == getParameterListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else {
      return getParent().Define_boolean_isCatchParam(this, caller);
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    // Declared at @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupConstructor.jrag:297
    if (!hasConstructorInvocation() && !hostType().isObject()) {
      state().duringImplicitConstructor++;
      ASTNode result = rewriteRule0();
      state().duringImplicitConstructor--;
      return result;
    }    return super.rewriteTo();
  }  /**
   * @declaredat @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupConstructor.jrag:297
   * @apilevel internal
   */  private ConstructorDecl rewriteRule0() {
{
      setConstructorInvocation(
        new ExprStmt(
          new SuperConstructorAccess("super", new List())
          )
        );
      return this;
    }  }
}
