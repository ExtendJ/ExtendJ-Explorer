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
 * Type access for a generic class with an empty type parameter list.
 * @ast node
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/grammar/Diamond.ast:4
 * @production DiamondAccess : {@link Access} ::= <span class="component">TypeAccess:{@link Access}</span>;

 */
public class DiamondAccess extends Access implements Cloneable {
  /**
   * @aspect Diamond
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Diamond.jrag:76
   */
  protected static SimpleSet mostSpecific(
      SimpleSet maxSpecific, MethodDecl decl) {
    if (maxSpecific.isEmpty()) {
      maxSpecific = maxSpecific.add(decl);
    } else {
      if (decl.moreSpecificThan(
            (MethodDecl)maxSpecific.iterator().next()))
        maxSpecific = SimpleSet.emptySet.add(decl);
      else if (!((MethodDecl)maxSpecific.iterator().next()).
          moreSpecificThan(decl))
        maxSpecific = maxSpecific.add(decl);
    }
    return maxSpecific;
  }
  /**
   * Choose a constructor for the diamond operator using placeholder
   * methods.
   * @aspect Diamond
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Diamond.jrag:95
   */
  protected SimpleSet chooseConstructor() {
    ClassInstanceExpr instanceExpr = getClassInstanceExpr();
    TypeDecl type = getTypeAccess().type();

    assert instanceExpr != null;
    assert type instanceof ParClassDecl;

    GenericClassDecl genericType =
      (GenericClassDecl) ((ParClassDecl)type).genericDecl();

    List<StandInMethodDecl> placeholderMethods =
      genericType.getStandInMethodList();

    SimpleSet maxSpecific = SimpleSet.emptySet;
    Collection<MethodDecl> potentiallyApplicable =
      potentiallyApplicable(placeholderMethods);
    for (MethodDecl candidate : potentiallyApplicable) {
      if (applicableBySubtyping(instanceExpr, candidate) ||
          applicableByMethodInvocationConversion(
            instanceExpr, candidate) ||
          applicableByVariableArity(instanceExpr, candidate))
        maxSpecific = mostSpecific(maxSpecific, candidate);

    }
    return maxSpecific;
  }
  /**
   * Select potentially applicable method declarations
   * from a set of candidates.
   * Type inference is applied to the (potentially) applicable candidates.
   * @aspect Diamond
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Diamond.jrag:228
   */
  protected Collection<MethodDecl> potentiallyApplicable(
      List<StandInMethodDecl> candidates) {
    Collection<MethodDecl> potentiallyApplicable =
      new LinkedList<MethodDecl>();
    for (GenericMethodDecl candidate : candidates) {
      if (potentiallyApplicable(candidate)) {
        MethodDecl decl = candidate.lookupParMethodDecl(
            typeArguments(candidate));
        potentiallyApplicable.add(decl);
      }
    }
    return potentiallyApplicable;
  }
  /**
   * @return false if the candidate method is not applicable.
   * @aspect Diamond
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Diamond.jrag:245
   */
  protected boolean potentiallyApplicable(
      GenericMethodDecl candidate) {
    if (candidate.isVariableArity() &&
        !(getClassInstanceExpr().arity() >= candidate.arity()-1))
      return false;
    if (!candidate.isVariableArity() &&
        !(getClassInstanceExpr().arity() == candidate.arity()))
      return false;

    java.util.List<TypeDecl> typeArgs = typeArguments(candidate);
    if (typeArgs.size() != 0) {
      if (candidate.getNumTypeParameter() != typeArgs.size())
        return false;
      for (int i = 0; i < candidate.getNumTypeParameter(); i++)
        if (!typeArgs.get(i).subtype(
              candidate.original().getTypeParameter(i)))
          return false;
    }
    return true;
  }
  /**
   * Diamond type inference.
   * @aspect Diamond
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Diamond.jrag:301
   */
  public Collection<TypeDecl> computeConstraints(
      GenericMethodDecl decl) {
    Constraints c = new Constraints();
    // store type parameters
    for (int i = 0; i < decl.original().getNumTypeParameter(); i++)
      c.addTypeVariable(decl.original().getTypeParameter(i));

    ClassInstanceExpr instanceExpr = getClassInstanceExpr();
    for (int i = 0; i < instanceExpr.getNumArg(); i++) {
      TypeDecl A = instanceExpr.getArg(i).type();
      int index = i >= decl.getNumParameter() ?
        decl.getNumParameter() - 1 : i;
      TypeDecl F = decl.getParameter(index).type();
      if (decl.getParameter(index) instanceof
          VariableArityParameterDeclaration &&
          (instanceExpr.getNumArg() != decl.getNumParameter() ||
          !A.isArrayDecl())) {
        F = F.componentType();
      }
      c.convertibleTo(A, F);
    }
    if (c.rawAccess)
      return new ArrayList();

    c.resolveEqualityConstraints();
    c.resolveSupertypeConstraints();

    if (c.unresolvedTypeArguments()) {
      TypeDecl S = assignConvertedType();
      if (S.isUnboxedPrimitive())
        S = S.boxed();
      TypeDecl R = decl.type();
      if (R.isVoid())
        R = typeObject();

      c.convertibleFrom(S, R);
      c.resolveEqualityConstraints();
      c.resolveSupertypeConstraints();
      c.resolveSubtypeConstraints();
    }

    return c.typeArguments();
  }
  /**
   * @return true if the method is applicable by subtyping
   * @aspect Diamond
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Diamond.jrag:348
   */
  protected boolean applicableBySubtyping(
      ClassInstanceExpr expr, MethodDecl method) {
    if (method.getNumParameter() != expr.getNumArg())
      return false;
    for (int i = 0; i < method.getNumParameter(); i++)
      if(!expr.getArg(i).type().instanceOf(method.getParameter(i).type()))
        return false;
    return true;
  }
  /**
   * @return true if the method is applicable by method invocation conversion
   * @aspect Diamond
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Diamond.jrag:361
   */
  protected boolean applicableByMethodInvocationConversion(
      ClassInstanceExpr expr, MethodDecl method) {
    if (method.getNumParameter() != expr.getNumArg())
      return false;
    for (int i = 0; i < method.getNumParameter(); i++)
      if (!expr.getArg(i).type().methodInvocationConversionTo(
            method.getParameter(i).type()))
        return false;
    return true;
  }
  /**
   * @return true if the method is applicable by variable arity
   * @aspect Diamond
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Diamond.jrag:375
   */
  protected boolean applicableByVariableArity(
      ClassInstanceExpr expr, MethodDecl method) {
    for (int i = 0; i < method.getNumParameter() - 1; i++)
      if(!expr.getArg(i).type().methodInvocationConversionTo(
            method.getParameter(i).type()))
        return false;
    for (int i = method.getNumParameter() - 1; i < expr.getNumArg(); i++)
      if (!expr.getArg(i).type().methodInvocationConversionTo(
            method.lastParameter().type().componentType()))
        return false;
    return true;
  }
  /**
   * Checks if this diamond access is legal.
   * The diamond access is not legal if it either is part of an inner class
   * declaration, if it is used to access a non-generic type, or if it is
   * part of a call to a generic constructor with explicit type arguments.
   * @aspect Diamond
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Diamond.jrag:491
   */
  public void typeCheck() {
    if (isAnonymousDecl())
      error("the diamond operator can not be used with "+
          "anonymous classes");
    if (isExplicitGenericConstructorAccess())
      error("the diamond operator may not be used with generic "+
          "constructors with explicit type parameters");
    if (getClassInstanceExpr() == null)
      error("the diamond operator can only be used in "+
          "class instance expressions");
    if (!(getTypeAccess().type() instanceof ParClassDecl))
      error("the diamond operator can only be used to "+
          "instantiate generic classes");
  }
  /**
   * Pretty printing of diamond access.
   * @aspect Diamond
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Diamond.jrag:509
   */
  public void prettyPrint(StringBuffer sb) {
    getTypeAccess().prettyPrint(sb);
    sb.append("<>");
  }
  /**
   * @declaredat ASTNode:1
   */
  public DiamondAccess() {
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
  public DiamondAccess(Access p0) {
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
    type_reset();
    typeArguments_MethodDecl_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:39
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:45
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:51
   */
  public DiamondAccess clone() throws CloneNotSupportedException {
    DiamondAccess node = (DiamondAccess) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:58
   */
  public DiamondAccess copy() {
    try {
      DiamondAccess node = (DiamondAccess) clone();
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
   * @declaredat ASTNode:77
   */
  public DiamondAccess fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:86
   */
  public DiamondAccess treeCopyNoTransform() {
    DiamondAccess tree = (DiamondAccess) copy();
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
   * @declaredat ASTNode:106
   */
  public DiamondAccess treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:113
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) ;    
  }
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
      TypeDecl accessType = getTypeAccess().type();
  
      if (isAnonymousDecl()) {
        return accessType;
      }
  
      if (getClassInstanceExpr() == null) {
        // it is an error if the DiamondAccess does not occurr
        // within a class instance creation expression, but this
        // error is handled in typeCheck
        return accessType;
      }
  
      if (!(accessType instanceof ParClassDecl)) {
        // it is an error if the TypeDecl of a DiamondAccess is not
        // a generic type, but this error is handled in typeCheck
        return accessType;
      }
  
      SimpleSet maxSpecific = chooseConstructor();
  
      if (maxSpecific.isEmpty()) {
        return getTypeAccess().type();
      }
  
      MethodDecl constructor = (MethodDecl) maxSpecific.iterator().next();
      return constructor.type();
    }
  @ASTNodeAnnotation.Attribute
  public boolean isDiamond() {
    ASTNode$State state = state();
    boolean isDiamond_value = true;

    return isDiamond_value;
  }
  protected java.util.Map typeArguments_MethodDecl_values;
/**
 * @apilevel internal
 */
private void typeArguments_MethodDecl_reset() {
  typeArguments_MethodDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public java.util.List<TypeDecl> typeArguments(MethodDecl decl) {
    Object _parameters = decl;
    if (typeArguments_MethodDecl_values == null) typeArguments_MethodDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(typeArguments_MethodDecl_values.containsKey(_parameters)) {
      return (java.util.List<TypeDecl>)typeArguments_MethodDecl_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    java.util.List<TypeDecl> typeArguments_MethodDecl_value = typeArguments_compute(decl);
    if (isFinal && num == state().boundariesCrossed) {
      typeArguments_MethodDecl_values.put(_parameters, typeArguments_MethodDecl_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeArguments_MethodDecl_value;
  }
  /**
   * @apilevel internal
   */
  private java.util.List<TypeDecl> typeArguments_compute(MethodDecl decl) {
      java.util.List<TypeDecl> typeArguments = new LinkedList<TypeDecl>();
      if (decl instanceof GenericMethodDecl) {
        GenericMethodDecl method = (GenericMethodDecl) decl;
        Collection<TypeDecl> arguments = computeConstraints(method);
        if (arguments.isEmpty())
          return typeArguments;
        int i = 0;
        for (TypeDecl argument : arguments) {
          if (argument == null) {
            TypeVariable v = method.original().getTypeParameter(i);
            if (v.getNumTypeBound() == 0)
              argument = typeObject();
            else if (v.getNumTypeBound() == 1)
              argument = v.getTypeBound(0).type();
            else
              argument = v.lubType();
          }
          typeArguments.add(argument);
  
          i += 1;
        }
      }
      return typeArguments;
    }
  /**
   * @attribute inh
   * @aspect Diamond
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Diamond.jrag:72
   */
  @ASTNodeAnnotation.Attribute
  public ClassInstanceExpr getClassInstanceExpr() {
    ASTNode$State state = state();
    ClassInstanceExpr getClassInstanceExpr_value = getParent().Define_ClassInstanceExpr_getClassInstanceExpr(this, null);

    return getClassInstanceExpr_value;
  }
  /**
   * @attribute inh
   * @aspect Diamond
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Diamond.jrag:266
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeObject() {
    ASTNode$State state = state();
    TypeDecl typeObject_value = getParent().Define_TypeDecl_typeObject(this, null);

    return typeObject_value;
  }
  /**
   * @return true if this access is part of an anonymous class declaration
   * @attribute inh
   * @aspect Diamond
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Diamond.jrag:456
   */
  @ASTNodeAnnotation.Attribute
  public boolean isAnonymousDecl() {
    ASTNode$State state = state();
    boolean isAnonymousDecl_value = getParent().Define_boolean_isAnonymousDecl(this, null);

    return isAnonymousDecl_value;
  }
  /**
   * @return true if the Access is part of a generic constructor invocation
   * with explicit type arguments
   * @attribute inh
   * @aspect Diamond
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Diamond.jrag:472
   */
  @ASTNodeAnnotation.Attribute
  public boolean isExplicitGenericConstructorAccess() {
    ASTNode$State state = state();
    boolean isExplicitGenericConstructorAccess_value = getParent().Define_boolean_isExplicitGenericConstructorAccess(this, null);

    return isExplicitGenericConstructorAccess_value;
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
