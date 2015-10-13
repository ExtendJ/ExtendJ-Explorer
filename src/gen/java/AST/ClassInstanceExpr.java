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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:36
 * @production ClassInstanceExpr : {@link Access} ::= <span class="component">{@link Access}</span> <span class="component">Arg:{@link Expr}*</span> <span class="component">[{@link TypeDecl}]</span>;

 */
public class ClassInstanceExpr extends Access implements Cloneable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:371
   */
  public void prettyPrint(StringBuffer sb) {
    sb.append("new ");
    getAccess().prettyPrint(sb);
    sb.append("(");
    if(getNumArg() > 0) {
      getArg(0).prettyPrint(sb);
      for(int i = 1; i < getNumArg(); i++) {
        sb.append(", ");
        getArg(i).prettyPrint(sb);
      }
    }
    sb.append(")");

    if(hasTypeDecl()) {
      TypeDecl decl = getTypeDecl();
      sb.append(" {");
      for(int i = 0; i < decl.getNumBodyDecl(); i++) {
        if(!(decl.getBodyDecl(i) instanceof ConstructorDecl))
          decl.getBodyDecl(i).prettyPrint(sb);
      }
      sb.append(typeDeclIndent());
      sb.append("}");
    }
  }
  /**
   * @aspect AccessControl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/AccessControl.jrag:139
   */
  public void accessControl() {
    super.accessControl();
    if(type().isAbstract())
      error("Can not instantiate abstract class " + type().fullName());
    if(!decl().accessibleFrom(hostType()))
      error("constructor " + decl().signature() + " is not accessible");
  }
  /**
   * @aspect ExceptionHandling
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ExceptionHandling.jrag:120
   */
  public void exceptionHandling() {
    for (Access exception : decl().getExceptionList()) {
      TypeDecl exceptionType = exception.type();
      if (!handlesException(exceptionType))
        error("" + this + " may throw uncaught exception " +
            exceptionType.fullName() + "; it must be caught or declared as being thrown");
    }
  }
  /**
   * @aspect ExceptionHandling
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ExceptionHandling.jrag:282
   */
  protected boolean reachedException(TypeDecl catchType) {
    ConstructorDecl decl = decl();
    for(int i = 0; i < decl.getNumException(); i++) {
      TypeDecl exceptionType = decl.getException(i).type();
      if(catchType.mayCatch(exceptionType))
        return true;
    }
    for(int i = 0; i < getNumArg(); i++) {
      if(getArg(i).reachedException(catchType))
        return true;
    }
    return false;
  }
  /**
   * @aspect TypeScopePropagation
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:386
   */
  public SimpleSet keepInnerClasses(SimpleSet c) {
    SimpleSet newSet = SimpleSet.emptySet;
    for(Iterator iter = c.iterator(); iter.hasNext(); ) {
      TypeDecl t = (TypeDecl)iter.next();
      if(t.isInnerType() && t.isClassDecl()) {
        newSet = newSet.add(c);
      }
    }
    return newSet;
  }
  /**
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:167
   */
  public void refined_NameCheck_ClassInstanceExpr_nameCheck() {
    super.nameCheck();
    if (decls().isEmpty()) {
      error("can not instantiate " + type().typeName() + " no matching constructor found in " + type().typeName());
    } else if (decls().size() > 1 && validArgs()) {
      error("several most specific constructors found");
      for(Iterator iter = decls().iterator(); iter.hasNext(); ) {
        error("         " + ((ConstructorDecl)iter.next()).signature());
      }
    }
  }
  /**
   * @aspect NodeConstructors
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NodeConstructors.jrag:82
   */
  public ClassInstanceExpr(Access type, List args) {
    this(type, args, new Opt());
  }
  /**
   * @aspect TypeCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeCheck.jrag:437
   */
  public void typeCheck() {
    if(isQualified() && qualifier().isTypeAccess() && !qualifier().type().isUnknown())
      error("*** The expression in a qualified class instance expr must not be a type name");
    // 15.9
    if(isQualified() && !type().isInnerClass() && !((ClassDecl)type()).superclass().isInnerClass() && !type().isUnknown()) {
      error("*** Qualified class instance creation can only instantiate inner classes and their anonymous subclasses");
    }
    if(!type().isClassDecl()) {
      error("*** Can only instantiate classes, which " + type().typeName() + " is not");
    }
    typeCheckEnclosingInstance();
    typeCheckAnonymousSuperclassEnclosingInstance();
  }
  /**
   * @aspect TypeCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeCheck.jrag:451
   */
  public void typeCheckEnclosingInstance() {
    TypeDecl C = type();
    if(!C.isInnerClass())
      return;

    TypeDecl enclosing = null;
    if(C.isAnonymous()) {
      if(noEnclosingInstance()) {
        enclosing = null;
      }
      else {
        enclosing = hostType();
      }
    }
    else if(C.isLocalClass()) {
      if(C.inStaticContext()) {
        enclosing = null;
      }
      else if(noEnclosingInstance()) {
        enclosing = unknownType();
      }
      else {
        TypeDecl nest = hostType();
        while(nest != null && !nest.instanceOf(C.enclosingType()))
          nest = nest.enclosingType();
        enclosing = nest;
      }
    }
    else if(C.isMemberType()) {
      if(!isQualified()) {
        if(noEnclosingInstance()) {
          error("No enclosing instance to initialize " + C.typeName() + " with");
          //System.err.println("ClassInstanceExpr: Non qualified MemberType " + C.typeName() + " is in a static context when instantiated in " + this);
          enclosing = unknownType();
        }
        else {
          TypeDecl nest = hostType();
          while(nest != null && !nest.instanceOf(C.enclosingType()))
            nest = nest.enclosingType();
          enclosing = nest == null ? unknownType() : nest;
        }
      }
      else {
        enclosing = enclosingInstance();
      }
    }
    if (enclosing != null) {
      if(!enclosing.instanceOf(type().enclosingType())) {
        String msg = enclosing == null ? "None" : enclosing.typeName();
        error("*** Can not instantiate " + type().typeName() + " with the enclosing instance " + msg + " due to incorrect enclosing instance");
      } else if(!isQualified() && C.isMemberType() && inExplicitConstructorInvocation() && enclosing == hostType()) {
        error("*** The innermost enclosing instance of type " + enclosing.typeName() + " is this which is not yet initialized here.");
      }
    }
  }
  /**
   * @aspect TypeCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeCheck.jrag:525
   */
  public void typeCheckAnonymousSuperclassEnclosingInstance() {
    if(type().isAnonymous() && ((ClassDecl)type()).superclass().isInnerType()) {
      TypeDecl S = ((ClassDecl)type()).superclass();
      if(S.isLocalClass()) {
        if(S.inStaticContext()) {
        }
        else if(noEnclosingInstance()) {
          error("*** No enclosing instance to class " + type().typeName() + " due to static context");
        }
        else if(inExplicitConstructorInvocation())
          error("*** No enclosing instance to superclass " + S.typeName() + " of " + type().typeName() + " since this is not initialized yet");
      }
      else if(S.isMemberType()) {
        if(!isQualified()) {
          // 15.9.2 2nd paragraph
          if(noEnclosingInstance()) {
            error("*** No enclosing instance to class " + type().typeName() + " due to static context");
          }
          else {
            TypeDecl nest = hostType();
            while(nest != null && !nest.instanceOf(S.enclosingType()))
              nest = nest.enclosingType();
            if(nest == null) {
              error("*** No enclosing instance to superclass " + S.typeName() + " of " + type().typeName());
            }
            else if(inExplicitConstructorInvocation()) {
              error("*** No enclosing instance to superclass " + S.typeName() + " of " + type().typeName() + " since this is not initialized yet");
            }
          }
        }
      }
    }
  }
  /**
   * @aspect Annotations
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:372
   */
  public void checkModifiers() {
    if(decl().isDeprecated() &&
      !withinDeprecatedAnnotation() &&
      hostType().topLevelType() != decl().hostType().topLevelType() &&
      !withinSuppressWarnings("deprecation"))
        warning(decl().signature() + " in " + decl().hostType().typeName() + " has been deprecated");
  }
  /**
   * @declaredat ASTNode:1
   */
  public ClassInstanceExpr() {
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
    setChild(new List(), 1);
    setChild(new Opt(), 2);
  }
  /**
   * @declaredat ASTNode:15
   */
  public ClassInstanceExpr(Access p0, List<Expr> p1, Opt<TypeDecl> p2) {
    setChild(p0, 0);
    setChild(p1, 1);
    setChild(p2, 2);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:23
   */
  protected int numChildren() {
    return 3;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:29
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:35
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    isDAafterInstance_Variable_reset();
    computeDAbefore_int_Variable_reset();
    computeDUbefore_int_Variable_reset();
    decls_reset();
    decl_reset();
    localLookupType_String_reset();
    type_reset();
    stmtCompatible_reset();
    compatibleStrictContext_TypeDecl_reset();
    compatibleLooseContext_TypeDecl_reset();
    isBooleanExpression_reset();
    isPolyExpression_reset();
    assignConversionTo_TypeDecl_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:54
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:60
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:66
   */
  public ClassInstanceExpr clone() throws CloneNotSupportedException {
    ClassInstanceExpr node = (ClassInstanceExpr) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:73
   */
  public ClassInstanceExpr copy() {
    try {
      ClassInstanceExpr node = (ClassInstanceExpr) clone();
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
   * @declaredat ASTNode:92
   */
  public ClassInstanceExpr fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:101
   */
  public ClassInstanceExpr treeCopyNoTransform() {
    ClassInstanceExpr tree = (ClassInstanceExpr) copy();
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
   * @declaredat ASTNode:121
   */
  public ClassInstanceExpr treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:128
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) ;    
  }
  /**
   * Replaces the Access child.
   * @param node The new node to replace the Access child.
   * @apilevel high-level
   */
  public void setAccess(Access node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the Access child.
   * @return The current node used as the Access child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Access")
  public Access getAccess() {
    return (Access) getChild(0);
  }
  /**
   * Retrieves the Access child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Access child.
   * @apilevel low-level
   */
  public Access getAccessNoTransform() {
    return (Access) getChildNoTransform(0);
  }
  /**
   * Replaces the Arg list.
   * @param list The new list node to be used as the Arg list.
   * @apilevel high-level
   */
  public void setArgList(List<Expr> list) {
    setChild(list, 1);
  }
  /**
   * Retrieves the number of children in the Arg list.
   * @return Number of children in the Arg list.
   * @apilevel high-level
   */
  public int getNumArg() {
    return getArgList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Arg list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Arg list.
   * @apilevel low-level
   */
  public int getNumArgNoTransform() {
    return getArgListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Arg list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Arg list.
   * @apilevel high-level
   */
  public Expr getArg(int i) {
    return (Expr) getArgList().getChild(i);
  }
  /**
   * Check whether the Arg list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasArg() {
    return getArgList().getNumChild() != 0;
  }
  /**
   * Append an element to the Arg list.
   * @param node The element to append to the Arg list.
   * @apilevel high-level
   */
  public void addArg(Expr node) {
    List<Expr> list = (parent == null || state == null) ? getArgListNoTransform() : getArgList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addArgNoTransform(Expr node) {
    List<Expr> list = getArgListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Arg list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setArg(Expr node, int i) {
    List<Expr> list = getArgList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Arg list.
   * @return The node representing the Arg list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Arg")
  public List<Expr> getArgList() {
    List<Expr> list = (List<Expr>) getChild(1);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the Arg list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Arg list.
   * @apilevel low-level
   */
  public List<Expr> getArgListNoTransform() {
    return (List<Expr>) getChildNoTransform(1);
  }
  /**
   * Retrieves the Arg list.
   * @return The node representing the Arg list.
   * @apilevel high-level
   */
  public List<Expr> getArgs() {
    return getArgList();
  }
  /**
   * Retrieves the Arg list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Arg list.
   * @apilevel low-level
   */
  public List<Expr> getArgsNoTransform() {
    return getArgListNoTransform();
  }
  /**
   * Replaces the optional node for the TypeDecl child. This is the <code>Opt</code>
   * node containing the child TypeDecl, not the actual child!
   * @param opt The new node to be used as the optional node for the TypeDecl child.
   * @apilevel low-level
   */
  public void setTypeDeclOpt(Opt<TypeDecl> opt) {
    setChild(opt, 2);
  }
  /**
   * Replaces the (optional) TypeDecl child.
   * @param node The new node to be used as the TypeDecl child.
   * @apilevel high-level
   */
  public void setTypeDecl(TypeDecl node) {
    getTypeDeclOpt().setChild(node, 0);
  }
  /**
   * Check whether the optional TypeDecl child exists.
   * @return {@code true} if the optional TypeDecl child exists, {@code false} if it does not.
   * @apilevel high-level
   */
  public boolean hasTypeDecl() {
    return getTypeDeclOpt().getNumChild() != 0;
  }
  /**
   * Retrieves the (optional) TypeDecl child.
   * @return The TypeDecl child, if it exists. Returns {@code null} otherwise.
   * @apilevel low-level
   */
  public TypeDecl getTypeDecl() {
    return (TypeDecl) getTypeDeclOpt().getChild(0);
  }
  /**
   * Retrieves the optional node for the TypeDecl child. This is the <code>Opt</code> node containing the child TypeDecl, not the actual child!
   * @return The optional node for child the TypeDecl child.
   * @apilevel low-level
   */
  @ASTNodeAnnotation.OptChild(name="TypeDecl")
  public Opt<TypeDecl> getTypeDeclOpt() {
    return (Opt<TypeDecl>) getChild(2);
  }
  /**
   * Retrieves the optional node for child TypeDecl. This is the <code>Opt</code> node containing the child TypeDecl, not the actual child!
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The optional node for child TypeDecl.
   * @apilevel low-level
   */
  public Opt<TypeDecl> getTypeDeclOptNoTransform() {
    return (Opt<TypeDecl>) getChildNoTransform(2);
  }
  /**
   * @aspect Enums
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Enums.jrag:19
   */
    public void nameCheck() {
    if(getAccess().type().isEnumDecl() && !enclosingBodyDecl().isEnumConstant())
      error("enum types may not be instantiated explicitly");
    else
      refined_NameCheck_ClassInstanceExpr_nameCheck();
  }
  protected java.util.Map isDAafterInstance_Variable_values;
/**
 * @apilevel internal
 */
private void isDAafterInstance_Variable_reset() {
  isDAafterInstance_Variable_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isDAafterInstance(Variable v) {
    Object _parameters = v;
    if (isDAafterInstance_Variable_values == null) isDAafterInstance_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(isDAafterInstance_Variable_values.containsKey(_parameters)) {
      return ((Boolean)isDAafterInstance_Variable_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean isDAafterInstance_Variable_value = isDAafterInstance_compute(v);
    if (isFinal && num == state().boundariesCrossed) {
      isDAafterInstance_Variable_values.put(_parameters, Boolean.valueOf(isDAafterInstance_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDAafterInstance_Variable_value;
  }
  /**
   * @apilevel internal
   */
  private boolean isDAafterInstance_compute(Variable v) {
      if(getNumArg() == 0)
        return isDAbefore(v);
      return getArg(getNumArg()-1).isDAafter(v);
    }
  @ASTNodeAnnotation.Attribute
  public boolean isDAafter(Variable v) {
    ASTNode$State state = state();
    boolean isDAafter_Variable_value = isDAafterInstance(v);

    return isDAafter_Variable_value;
  }
  protected java.util.Map computeDAbefore_int_Variable_values;
/**
 * @apilevel internal
 */
private void computeDAbefore_int_Variable_reset() {
  computeDAbefore_int_Variable_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean computeDAbefore(int i, Variable v) {
    java.util.List _parameters = new java.util.ArrayList(2);
    _parameters.add(Integer.valueOf(i));
    _parameters.add(v);
    if (computeDAbefore_int_Variable_values == null) computeDAbefore_int_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(computeDAbefore_int_Variable_values.containsKey(_parameters)) {
      return ((Boolean)computeDAbefore_int_Variable_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean computeDAbefore_int_Variable_value = i == 0 ? isDAbefore(v) : getArg(i-1).isDAafter(v);
    if (isFinal && num == state().boundariesCrossed) {
      computeDAbefore_int_Variable_values.put(_parameters, Boolean.valueOf(computeDAbefore_int_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return computeDAbefore_int_Variable_value;
  }
  /**
   * @attribute syn
   * @aspect DU
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:903
   */
  @ASTNodeAnnotation.Attribute
  public boolean isDUafterInstance(Variable v) {
    ASTNode$State state = state();
    try {
        if(getNumArg() == 0)
          return isDUbefore(v);
        return getArg(getNumArg()-1).isDUafter(v);
      }
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public boolean isDUafter(Variable v) {
    ASTNode$State state = state();
    boolean isDUafter_Variable_value = isDUafterInstance(v);

    return isDUafter_Variable_value;
  }
  protected java.util.Map computeDUbefore_int_Variable_values;
/**
 * @apilevel internal
 */
private void computeDUbefore_int_Variable_reset() {
  computeDUbefore_int_Variable_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean computeDUbefore(int i, Variable v) {
    java.util.List _parameters = new java.util.ArrayList(2);
    _parameters.add(Integer.valueOf(i));
    _parameters.add(v);
    if (computeDUbefore_int_Variable_values == null) computeDUbefore_int_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(computeDUbefore_int_Variable_values.containsKey(_parameters)) {
      return ((Boolean)computeDUbefore_int_Variable_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean computeDUbefore_int_Variable_value = i == 0 ? isDUbefore(v) : getArg(i-1).isDUafter(v);
    if (isFinal && num == state().boundariesCrossed) {
      computeDUbefore_int_Variable_values.put(_parameters, Boolean.valueOf(computeDUbefore_int_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return computeDUbefore_int_Variable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean applicableAndAccessible(ConstructorDecl decl) {
    ASTNode$State state = state();
    boolean applicableAndAccessible_ConstructorDecl_value = decl.applicable(getArgList()) && decl.accessibleFrom(hostType()) &&
        (!decl.isProtected() || hasTypeDecl() || decl.hostPackage().equals(hostPackage()));

    return applicableAndAccessible_ConstructorDecl_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean decls_computed = false;
  /**
   * @apilevel internal
   */
  protected SimpleSet decls_value;
/**
 * @apilevel internal
 */
private void decls_reset() {
  decls_computed = false;
  decls_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public SimpleSet decls() {
    if(decls_computed) {
      return decls_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    decls_value = decls_compute();
    if (isFinal && num == state().boundariesCrossed) {
      decls_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return decls_value;
  }
  /**
   * @apilevel internal
   */
  private SimpleSet decls_compute() {
      TypeDecl typeDecl = hasTypeDecl() ? getTypeDecl() : getAccess().type();
      return chooseConstructor(typeDecl.constructors(), getArgList());
    }
  /**
   * @apilevel internal
   */
  protected boolean decl_computed = false;
  /**
   * @apilevel internal
   */
  protected ConstructorDecl decl_value;
/**
 * @apilevel internal
 */
private void decl_reset() {
  decl_computed = false;
  decl_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public ConstructorDecl decl() {
    if(decl_computed) {
      return decl_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    decl_value = decl_compute();
    if (isFinal && num == state().boundariesCrossed) {
      decl_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return decl_value;
  }
  /**
   * @apilevel internal
   */
  private ConstructorDecl decl_compute() {
      SimpleSet decls = decls();
      if (decls.size() == 1) {
        return (ConstructorDecl) decls.iterator().next();
      } else {
        return unknownConstructor();
      }
    }
  /**
   * @attribute syn
   * @aspect TypeScopePropagation
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:402
   */
  @ASTNodeAnnotation.Attribute
  public SimpleSet qualifiedLookupType(String name) {
    ASTNode$State state = state();
    try {
        SimpleSet c = keepAccessibleTypes(type().memberTypes(name));
        if(!c.isEmpty())
          return c;
        if(type().name().equals(name))
          return SimpleSet.emptySet.add(type());
        return SimpleSet.emptySet;
      }
    finally {
    }
  }
  protected java.util.Map localLookupType_String_values;
/**
 * @apilevel internal
 */
private void localLookupType_String_reset() {
  localLookupType_String_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public SimpleSet localLookupType(String name) {
    Object _parameters = name;
    if (localLookupType_String_values == null) localLookupType_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(localLookupType_String_values.containsKey(_parameters)) {
      return (SimpleSet)localLookupType_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    SimpleSet localLookupType_String_value = localLookupType_compute(name);
    if (isFinal && num == state().boundariesCrossed) {
      localLookupType_String_values.put(_parameters, localLookupType_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return localLookupType_String_value;
  }
  /**
   * @apilevel internal
   */
  private SimpleSet localLookupType_compute(String name) {
      if(hasTypeDecl() && getTypeDecl().name().equals(name))
        return SimpleSet.emptySet.add(getTypeDecl());
      return SimpleSet.emptySet;
    }
  /**
   * @attribute syn
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:160
   */
  @ASTNodeAnnotation.Attribute
  public boolean validArgs() {
    ASTNode$State state = state();
    try {
    		for(int i = 0; i < getNumArg(); i++) {
    			if(!getArg(i).isPolyExpression() && getArg(i).type().isUnknown())
    				return false;
    		}
    		return true;
    	}
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public NameType predNameType() {
    ASTNode$State state = state();
    NameType predNameType_value = NameType.EXPRESSION_NAME;

    return predNameType_value;
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
    type_value = hasTypeDecl() ? getTypeDecl() : getAccess().type();
    if (isFinal && num == state().boundariesCrossed) {
      type_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return type_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean noEnclosingInstance() {
    ASTNode$State state = state();
    boolean noEnclosingInstance_value = isQualified() ? qualifier().staticContextQualifier() : inStaticContext();

    return noEnclosingInstance_value;
  }
  @ASTNodeAnnotation.Attribute
  public int arity() {
    ASTNode$State state = state();
    int arity_value = getNumArg();

    return arity_value;
  }
  /**
   * @attribute syn
   * @aspect VariableArityParameters
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/VariableArityParameters.jrag:54
   */
  @ASTNodeAnnotation.Attribute
  public boolean invokesVariableArityAsArray() {
    ASTNode$State state = state();
    try {
        if(!decl().isVariableArity())
          return false;
        if(arity() != decl().arity())
          return false;
        return getArg(getNumArg()-1).type().methodInvocationConversionTo(decl().lastParameter().type());
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect PreciseRethrow
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/PreciseRethrow.jrag:117
   */
  @ASTNodeAnnotation.Attribute
  public boolean modifiedInScope(Variable var) {
    ASTNode$State state = state();
    try {
    		for (int i = 0; i < getNumArg(); ++i) {
    			if (getArg(i).modifiedInScope(var)) {
    				return true;
    			}
    		}
    		if(hasTypeDecl())
    			return getTypeDecl().modifiedInScope(var);
    		else
    			return false;
    	}
    finally {
    }
  }
  /**
   * @apilevel internal
   */
  protected boolean stmtCompatible_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean stmtCompatible_value;
/**
 * @apilevel internal
 */
private void stmtCompatible_reset() {
  stmtCompatible_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean stmtCompatible() {
    if(stmtCompatible_computed) {
      return stmtCompatible_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    stmtCompatible_value = true;
    if (isFinal && num == state().boundariesCrossed) {
      stmtCompatible_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return stmtCompatible_value;
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
  			return assignConversionTo(type);
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
  			return assignConversionTo(type);
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
  		if(getAccess() instanceof TypeAccess) {
  			TypeAccess typeAccess = (TypeAccess)getAccess();
  			return typeAccess.name().equals("Boolean");
  		}
  		return false;
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
  		return (getAccess() instanceof DiamondAccess) && (assignmentContext() || invocationContext());
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
  			return super.assignConversionTo(type);
  		else {
  			return ((DiamondAccess)getAccess()).getTypeAccess().type().assignConversionTo(type, ((DiamondAccess)getAccess()).getTypeAccess());
  		}
  	}
  /**
   * @attribute inh
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:395
   */
  @ASTNodeAnnotation.Attribute
  public String typeDeclIndent() {
    ASTNode$State state = state();
    String typeDeclIndent_value = getParent().Define_String_typeDeclIndent(this, null);

    return typeDeclIndent_value;
  }
  /**
   * @attribute inh
   * @aspect ExceptionHandling
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ExceptionHandling.jrag:58
   */
  @ASTNodeAnnotation.Attribute
  public boolean handlesException(TypeDecl exceptionType) {
    ASTNode$State state = state();
    boolean handlesException_TypeDecl_value = getParent().Define_boolean_handlesException(this, null, exceptionType);

    return handlesException_TypeDecl_value;
  }
  /**
   * @attribute inh
   * @aspect ConstructScope
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupConstructor.jrag:27
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeObject() {
    ASTNode$State state = state();
    TypeDecl typeObject_value = getParent().Define_TypeDecl_typeObject(this, null);

    return typeObject_value;
  }
  /**
   * @attribute inh
   * @aspect ConstructScope
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupConstructor.jrag:89
   */
  @ASTNodeAnnotation.Attribute
  public ConstructorDecl unknownConstructor() {
    ASTNode$State state = state();
    ConstructorDecl unknownConstructor_value = getParent().Define_ConstructorDecl_unknownConstructor(this, null);

    return unknownConstructor_value;
  }
  /**
   * @attribute inh
   * @aspect TypeCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeCheck.jrag:508
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl enclosingInstance() {
    ASTNode$State state = state();
    TypeDecl enclosingInstance_value = getParent().Define_TypeDecl_enclosingInstance(this, null);

    return enclosingInstance_value;
  }
  /**
   * @attribute inh
   * @aspect TypeHierarchyCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeHierarchyCheck.jrag:145
   */
  @ASTNodeAnnotation.Attribute
  public boolean inExplicitConstructorInvocation() {
    ASTNode$State state = state();
    boolean inExplicitConstructorInvocation_value = getParent().Define_boolean_inExplicitConstructorInvocation(this, null);

    return inExplicitConstructorInvocation_value;
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/AnonymousClasses.jrag:13
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_superType(ASTNode caller, ASTNode child) {
    if (caller == getTypeDeclOptNoTransform()) {
      return getAccess().type();
    }
    else {
      return getParent().Define_TypeDecl_superType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/MethodSignature.jrag:83
   * @apilevel internal
   */
  public ConstructorDecl Define_ConstructorDecl_constructorDecl(ASTNode caller, ASTNode child) {
    if (caller == getTypeDeclOptNoTransform()){
    Collection c = getAccess().type().constructors();
    SimpleSet maxSpecific = chooseConstructor(c, getArgList());
    if(maxSpecific.size() == 1)
      return (ConstructorDecl)maxSpecific.iterator().next();
    return unknownConstructor();
  }
    else {
      return getParent().Define_ConstructorDecl_constructorDecl(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:445
   * @apilevel internal
   */
  public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
    if (caller == getTypeDeclOptNoTransform()) {
      return isDAafterInstance(v);
    }
    else if (caller == getArgListNoTransform()) {
      int i = caller.getIndexOfChild(child);
      return computeDAbefore(i, v);
    }
    else {
      return getParent().Define_boolean_isDAbefore(this, caller, v);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:909
   * @apilevel internal
   */
  public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
    if (caller == getArgListNoTransform()) {
      int i = caller.getIndexOfChild(child);
      return computeDUbefore(i, v);
    }
    else {
      return getParent().Define_boolean_isDUbefore(this, caller, v);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:95
   * @apilevel internal
   */
  public boolean Define_boolean_hasPackage(ASTNode caller, ASTNode child, String packageName) {
    if (caller == getArgListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return unqualifiedScope().hasPackage(packageName);
    }
    else {
      return getParent().Define_boolean_hasPackage(this, caller, packageName);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:376
   * @apilevel internal
   */
  public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
    if (caller == getTypeDeclOptNoTransform()){
    SimpleSet c = localLookupType(name);
    if(!c.isEmpty())
      return c;
    c = lookupType(name);
    if(!c.isEmpty())
      return c;
    return unqualifiedScope().lookupType(name);
  }
    else if (caller == getAccessNoTransform()){
    SimpleSet c = lookupType(name);
    if(c.size() == 1) {
      if(isQualified())
        c = keepInnerClasses(c);
    }
    return c;
  }
    else if (caller == getArgListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return unqualifiedScope().lookupType(name);
    }
    else {
      return getParent().Define_SimpleSet_lookupType(this, caller, name);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupVariable.jrag:169
   * @apilevel internal
   */
  public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
    if (caller == getArgListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return unqualifiedScope().lookupVariable(name);
    }
    else {
      return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/SyntacticClassification.jrag:127
   * @apilevel internal
   */
  public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
    if (caller == getArgListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return NameType.EXPRESSION_NAME;
    }
    else if (caller == getTypeDeclOptNoTransform()) {
      return NameType.TYPE_NAME;
    }
    else if (caller == getAccessNoTransform()) {
      return NameType.TYPE_NAME;
    }
    else {
      return getParent().Define_NameType_nameType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:217
   * @apilevel internal
   */
  public boolean Define_boolean_isAnonymous(ASTNode caller, ASTNode child) {
    if (caller == getTypeDeclOptNoTransform()) {
      return true;
    }
    else {
      return getParent().Define_boolean_isAnonymous(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:541
   * @apilevel internal
   */
  public boolean Define_boolean_isMemberType(ASTNode caller, ASTNode child) {
    if (caller == getTypeDeclOptNoTransform()) {
      return false;
    }
    else {
      return getParent().Define_boolean_isMemberType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:584
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_hostType(ASTNode caller, ASTNode child) {
    if (caller == getTypeDeclOptNoTransform()) {
      return hostType();
    }
    else {
      return getParent().Define_TypeDecl_hostType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeHierarchyCheck.jrag:174
   * @apilevel internal
   */
  public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
    if (caller == getTypeDeclOptNoTransform()) {
      return isQualified() ?
    qualifier().staticContextQualifier() : inStaticContext();
    }
    else {
      return getParent().Define_boolean_inStaticContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Diamond.jrag:73
   * @apilevel internal
   */
  public ClassInstanceExpr Define_ClassInstanceExpr_getClassInstanceExpr(ASTNode caller, ASTNode child) {
    if (caller == getAccessNoTransform()) {
      return this;
    }
    else {
      return getParent().Define_ClassInstanceExpr_getClassInstanceExpr(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Diamond.jrag:461
   * @apilevel internal
   */
  public boolean Define_boolean_isAnonymousDecl(ASTNode caller, ASTNode child) {
    if (caller == getAccessNoTransform()) {
      return hasTypeDecl();
    }
    else {
      return getParent().Define_boolean_isAnonymousDecl(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:108
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_targetType(ASTNode caller, ASTNode child) {
    if (caller == getArgListNoTransform()) {
      int i = caller.getIndexOfChild(child);
      {
		ConstructorDecl decl = decl();
		if(unknownConstructor() == decl)
			return decl.type().unknownType();
		
		if(decl.isVariableArity() && i >= decl.arity() - 1) {
			return decl.getParameter(decl.arity() - 1).type().componentType();
		}
		else
			return decl.getParameter(i).type();
	}
    }
    else {
      return getParent().Define_TypeDecl_targetType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:174
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_assignConvertedType(ASTNode caller, ASTNode child) {
    if (caller == getAccessNoTransform()) {
      return targetType();
    }
    else {
      return getParent().Define_TypeDecl_assignConvertedType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:332
   * @apilevel internal
   */
  public boolean Define_boolean_assignmentContext(ASTNode caller, ASTNode child) {
    if (caller == getArgListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else {
      return getParent().Define_boolean_assignmentContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:333
   * @apilevel internal
   */
  public boolean Define_boolean_invocationContext(ASTNode caller, ASTNode child) {
    if (caller == getArgListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return true;
    }
    else {
      return getParent().Define_boolean_invocationContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:334
   * @apilevel internal
   */
  public boolean Define_boolean_castContext(ASTNode caller, ASTNode child) {
    if (caller == getArgListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else {
      return getParent().Define_boolean_castContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:335
   * @apilevel internal
   */
  public boolean Define_boolean_stringContext(ASTNode caller, ASTNode child) {
    if (caller == getArgListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else {
      return getParent().Define_boolean_stringContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:336
   * @apilevel internal
   */
  public boolean Define_boolean_numericContext(ASTNode caller, ASTNode child) {
    if (caller == getArgListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
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
