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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/grammar/Generics.ast:18
 * @production TypeVariable : {@link ReferenceType} ::= <span class="component">{@link Modifiers}</span> <span class="component">&lt;ID:String&gt;</span> <span class="component">{@link BodyDecl}*</span> <span class="component">TypeBound:{@link Access}*</span>;

 */
public class TypeVariable extends ReferenceType implements Cloneable {
  /**
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:754
   */
  public Access substitute(Parameterization parTypeDecl) {
    if(parTypeDecl.isRawType())
      return erasure().createBoundAccess();
    return parTypeDecl.substitute(this).createBoundAccess();
  }
  /**
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:799
   */
  public Access substituteReturnType(Parameterization parTypeDecl) {
    if(parTypeDecl.isRawType())
      return erasure().createBoundAccess();
    TypeDecl typeDecl = parTypeDecl.substitute(this);
    if(typeDecl instanceof WildcardType) {
      // the bound of this type variable
      return createBoundAccess();
      //return lubType().createBoundAccess();
      //return typeObject().createBoundAccess();
    }
    else if(typeDecl instanceof WildcardExtendsType) {
      if(typeDecl.instanceOf(this))
        return ((WildcardExtendsType)typeDecl).extendsType().createBoundAccess();
      else
        return createBoundAccess();

      // the bound of this type variable of the bound of the wild card if it is more specific
      //return ((WildcardExtendsType)typeDecl).extendsType().createBoundAccess();
    }
    else if(typeDecl instanceof WildcardSuperType) {
      // the bound of this type variable
      return createBoundAccess();
      //return typeObject().createBoundAccess();
    }
    return typeDecl.createBoundAccess();
  }
  /**
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:833
   */
  public Access substituteParameterType(Parameterization parTypeDecl) {
    if(parTypeDecl.isRawType())
      return erasure().createBoundAccess();
    TypeDecl typeDecl = parTypeDecl.substitute(this);
    if(typeDecl instanceof WildcardType)
      return typeNull().createQualifiedAccess();
    else if(typeDecl instanceof WildcardExtendsType)
      return typeNull().createQualifiedAccess();
    else if(typeDecl instanceof WildcardSuperType)
      return ((WildcardSuperType)typeDecl).superType().createBoundAccess();
    return typeDecl.createBoundAccess();
  }
  /**
   * @aspect NewGenerics
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:1290
   */
  public Access createQualifiedAccess() {
    return createBoundAccess();
  }
  /**
   * @aspect GenericTypeVariables
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericTypeVariables.jrag:28
   */
  public void nameCheck() {
    if(extractSingleType(lookupType(name())) != this)
      error("*** Semantic Error: type variable " + name() + " is multiply declared");
  }
  /**
   * @aspect GenricTypeVariablesTypeAnalysis
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericTypeVariables.jrag:66
   */
  public void typeCheck() {
    if(!getTypeBound(0).type().isTypeVariable() && !getTypeBound(0).type().isClassDecl() && !getTypeBound(0).type().isInterfaceDecl()) {
      error("the first type bound must be either a type variable, or a class or interface type which " +
        getTypeBound(0).type().fullName() + " is not");
    }
    for(int i = 1; i < getNumTypeBound(); i++) {
      if(!getTypeBound(i).type().isInterfaceDecl()) {
        error("type bound " + i + " must be an interface type which " +
          getTypeBound(i).type().fullName() + " is not");
      }
    }
    HashSet typeSet = new HashSet();
    for(int i = 0; i < getNumTypeBound(); i++) {
      TypeDecl type = getTypeBound(i).type();
      TypeDecl erasure = type.erasure();
      if(typeSet.contains(erasure)) {
        if(type != erasure) {
          error("the erasure " + erasure.fullName() + " of typebound " + getTypeBound(i).prettyPrint() + " is multiply declared in " + this);
        }
        else {
          error(type.fullName() + " is multiply declared");
        }
      }
      typeSet.add(erasure);
    }

    for(int i = 0; i < getNumTypeBound(); i++) {
      TypeDecl type = getTypeBound(i).type();
      for(Iterator iter = type.methodsIterator(); iter.hasNext(); ) {
        MethodDecl m = (MethodDecl)iter.next();
        for(int j = i+1; j < getNumTypeBound(); j++) {
          TypeDecl destType = getTypeBound(j).type();
          for(Iterator destIter = destType.memberMethods(m.name()).iterator(); destIter.hasNext(); ) {
            MethodDecl n = (MethodDecl)destIter.next();
            if(m.sameSignature(n) && m.type() != n.type()) {
              error("the two bounds, " + type.name() + " and " + destType.name() + ", in type variable " + name() +
                " have a method " + m.signature() + " with conflicting return types " + m.type().name() + " and " + n.type().name());
            }
          }
        }
      }
    }


  }
  /**
   * @aspect GenericsPrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericsPrettyPrint.jrag:11
   */
  public void prettyPrint(StringBuffer sb) {
    sb.append(name());
    if (getNumTypeBound() > 0) {
      sb.append(" extends ");
      getTypeBound(0).prettyPrint(sb);
      for (int i = 1; i < getNumTypeBound(); i++) {
        sb.append(" & ");
        getTypeBound(i).prettyPrint(sb);
      }
    }
  }
  /**
   * @declaredat ASTNode:1
   */
  public TypeVariable() {
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
    setChild(new List(), 2);
  }
  /**
   * @declaredat ASTNode:15
   */
  public TypeVariable(Modifiers p0, String p1, List<BodyDecl> p2, List<Access> p3) {
    setChild(p0, 0);
    setID(p1);
    setChild(p2, 1);
    setChild(p3, 2);
  }
  /**
   * @declaredat ASTNode:21
   */
  public TypeVariable(Modifiers p0, beaver.Symbol p1, List<BodyDecl> p2, List<Access> p3) {
    setChild(p0, 0);
    setID(p1);
    setChild(p2, 1);
    setChild(p3, 2);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:30
   */
  protected int numChildren() {
    return 3;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:36
   */
  public boolean mayHaveRewrite() {
    return true;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:42
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    toInterface_reset();
    castingConversionTo_TypeDecl_reset();
    erasure_reset();
    fullName_reset();
    lubType_reset();
    usesTypeVariable_reset();
    accessibleFrom_TypeDecl_reset();
    typeName_reset();
    involvesTypeParameters_reset();
    memberFields_String_reset();
    sameStructure_TypeDecl_reset();
    subtype_TypeDecl_reset();
    getSubstitutedTypeBound_int_TypeDecl_reset();
    instanceOf_TypeDecl_reset();
    strictSubtype_TypeDecl_reset();
    typeVarPosition_reset();
    genericMethodLevel_reset();
    typeVarInMethod_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:66
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:72
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:78
   */
  public TypeVariable clone() throws CloneNotSupportedException {
    TypeVariable node = (TypeVariable) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:85
   */
  public TypeVariable copy() {
    try {
      TypeVariable node = (TypeVariable) clone();
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
   * @declaredat ASTNode:104
   */
  public TypeVariable fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:113
   */
  public TypeVariable treeCopyNoTransform() {
    TypeVariable tree = (TypeVariable) copy();
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
   * @declaredat ASTNode:133
   */
  public TypeVariable treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:140
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_ID == ((TypeVariable)node).tokenString_ID);    
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
   * Replaces the BodyDecl list.
   * @param list The new list node to be used as the BodyDecl list.
   * @apilevel high-level
   */
  public void setBodyDeclList(List<BodyDecl> list) {
    setChild(list, 1);
  }
  /**
   * Retrieves the number of children in the BodyDecl list.
   * @return Number of children in the BodyDecl list.
   * @apilevel high-level
   */
  public int getNumBodyDecl() {
    return getBodyDeclList().getNumChild();
  }
  /**
   * Retrieves the number of children in the BodyDecl list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the BodyDecl list.
   * @apilevel low-level
   */
  public int getNumBodyDeclNoTransform() {
    return getBodyDeclListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the BodyDecl list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the BodyDecl list.
   * @apilevel high-level
   */
  public BodyDecl getBodyDecl(int i) {
    return (BodyDecl) getBodyDeclList().getChild(i);
  }
  /**
   * Check whether the BodyDecl list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasBodyDecl() {
    return getBodyDeclList().getNumChild() != 0;
  }
  /**
   * Append an element to the BodyDecl list.
   * @param node The element to append to the BodyDecl list.
   * @apilevel high-level
   */
  public void addBodyDecl(BodyDecl node) {
    List<BodyDecl> list = (parent == null || state == null) ? getBodyDeclListNoTransform() : getBodyDeclList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addBodyDeclNoTransform(BodyDecl node) {
    List<BodyDecl> list = getBodyDeclListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the BodyDecl list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setBodyDecl(BodyDecl node, int i) {
    List<BodyDecl> list = getBodyDeclList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the BodyDecl list.
   * @return The node representing the BodyDecl list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="BodyDecl")
  public List<BodyDecl> getBodyDeclList() {
    List<BodyDecl> list = (List<BodyDecl>) getChild(1);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the BodyDecl list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the BodyDecl list.
   * @apilevel low-level
   */
  public List<BodyDecl> getBodyDeclListNoTransform() {
    return (List<BodyDecl>) getChildNoTransform(1);
  }
  /**
   * Retrieves the BodyDecl list.
   * @return The node representing the BodyDecl list.
   * @apilevel high-level
   */
  public List<BodyDecl> getBodyDecls() {
    return getBodyDeclList();
  }
  /**
   * Retrieves the BodyDecl list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the BodyDecl list.
   * @apilevel low-level
   */
  public List<BodyDecl> getBodyDeclsNoTransform() {
    return getBodyDeclListNoTransform();
  }
  /**
   * Replaces the TypeBound list.
   * @param list The new list node to be used as the TypeBound list.
   * @apilevel high-level
   */
  public void setTypeBoundList(List<Access> list) {
    setChild(list, 2);
  }
  /**
   * Retrieves the number of children in the TypeBound list.
   * @return Number of children in the TypeBound list.
   * @apilevel high-level
   */
  public int getNumTypeBound() {
    return getTypeBoundList().getNumChild();
  }
  /**
   * Retrieves the number of children in the TypeBound list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the TypeBound list.
   * @apilevel low-level
   */
  public int getNumTypeBoundNoTransform() {
    return getTypeBoundListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the TypeBound list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the TypeBound list.
   * @apilevel high-level
   */
  public Access getTypeBound(int i) {
    return (Access) getTypeBoundList().getChild(i);
  }
  /**
   * Check whether the TypeBound list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasTypeBound() {
    return getTypeBoundList().getNumChild() != 0;
  }
  /**
   * Append an element to the TypeBound list.
   * @param node The element to append to the TypeBound list.
   * @apilevel high-level
   */
  public void addTypeBound(Access node) {
    List<Access> list = (parent == null || state == null) ? getTypeBoundListNoTransform() : getTypeBoundList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addTypeBoundNoTransform(Access node) {
    List<Access> list = getTypeBoundListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the TypeBound list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setTypeBound(Access node, int i) {
    List<Access> list = getTypeBoundList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the TypeBound list.
   * @return The node representing the TypeBound list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="TypeBound")
  public List<Access> getTypeBoundList() {
    List<Access> list = (List<Access>) getChild(2);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the TypeBound list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the TypeBound list.
   * @apilevel low-level
   */
  public List<Access> getTypeBoundListNoTransform() {
    return (List<Access>) getChildNoTransform(2);
  }
  /**
   * Retrieves the TypeBound list.
   * @return The node representing the TypeBound list.
   * @apilevel high-level
   */
  public List<Access> getTypeBounds() {
    return getTypeBoundList();
  }
  /**
   * Retrieves the TypeBound list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the TypeBound list.
   * @apilevel low-level
   */
  public List<Access> getTypeBoundsNoTransform() {
    return getTypeBoundListNoTransform();
  }
  /**
   * @apilevel internal
   */
  protected boolean toInterface_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl toInterface_value;
/**
 * @apilevel internal
 */
private void toInterface_reset() {
  toInterface_computed = false;
  toInterface_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl toInterface() {
    if(toInterface_computed) {
      return toInterface_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    toInterface_value = toInterface_compute();
    toInterface_value.setParent(this);
    toInterface_value.is$Final = true;
    if (true) {
      toInterface_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return toInterface_value;
  }
  /**
   * @apilevel internal
   */
  private TypeDecl toInterface_compute() {
      // convert var to interface
      InterfaceDecl ITj = new InterfaceDecl();
      ITj.setID("ITj_" + hashCode());
      // I'm assuming that TypeVariable has no members of it's own.
      // TODO: would it be enough to add only public members of a bound
      // that is TypeVariable or ClassDecl and add other (interface)
      // bounds as superinterfaces to ITj
      // TODO: Is it really necessary to add public members to the new
      // interface? Or is an empty interface more than enough since java
      // has a nominal type system.
      for (int i = 0; i < getNumTypeBound(); i++) {
        TypeDecl bound = getTypeBound(i).type();
        for (int j = 0; j < bound.getNumBodyDecl(); j++) {
          BodyDecl bd = bound.getBodyDecl(j);
          if (bd instanceof FieldDeclaration) {
            FieldDeclaration fd = (FieldDeclaration) bd.fullCopy();
            if (fd.isPublic())
              ITj.addBodyDecl(fd);
          }
          else if (bd instanceof MethodDecl) {
            MethodDecl md = (MethodDecl) bd;
            if (md.isPublic())
              ITj.addBodyDecl((BodyDecl)md.fullCopy());
          }
        }
      }
      return ITj;
    }
  protected java.util.Map castingConversionTo_TypeDecl_values;
/**
 * @apilevel internal
 */
private void castingConversionTo_TypeDecl_reset() {
  castingConversionTo_TypeDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean castingConversionTo(TypeDecl type) {
    Object _parameters = type;
    if (castingConversionTo_TypeDecl_values == null) castingConversionTo_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(castingConversionTo_TypeDecl_values.containsKey(_parameters)) {
      return ((Boolean)castingConversionTo_TypeDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean castingConversionTo_TypeDecl_value = castingConversionTo_compute(type);
    if (isFinal && num == state().boundariesCrossed) {
      castingConversionTo_TypeDecl_values.put(_parameters, Boolean.valueOf(castingConversionTo_TypeDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return castingConversionTo_TypeDecl_value;
  }
  /**
   * @apilevel internal
   */
  private boolean castingConversionTo_compute(TypeDecl type) {
      if(!type.isReferenceType())
        return false;
      if(getNumTypeBound() == 0) return true;
      for(int i = 0; i < getNumTypeBound(); i++)
        if(getTypeBound(i).type().castingConversionTo(type))
          return true;
      return false;
    }
  @ASTNodeAnnotation.Attribute
  public boolean isNestedType() {
    ASTNode$State state = state();
    boolean isNestedType_value = false;

    return isNestedType_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean erasure_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl erasure_value;
/**
 * @apilevel internal
 */
private void erasure_reset() {
  erasure_computed = false;
  erasure_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl erasure() {
    if(erasure_computed) {
      return erasure_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    erasure_value = getTypeBound(0).type().erasure();
    if (isFinal && num == state().boundariesCrossed) {
      erasure_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return erasure_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean fullName_computed = false;
  /**
   * @apilevel internal
   */
  protected String fullName_value;
/**
 * @apilevel internal
 */
private void fullName_reset() {
  fullName_computed = false;
  fullName_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public String fullName() {
    if(fullName_computed) {
      return fullName_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    fullName_value = fullName_compute();
    if (isFinal && num == state().boundariesCrossed) {
      fullName_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return fullName_value;
  }
  /**
   * @apilevel internal
   */
  private String fullName_compute() {
      if(getParent().getParent() instanceof TypeDecl) {
        TypeDecl typeDecl = (TypeDecl)getParent().getParent();
        return typeDecl.fullName() + "@" + name();
      }
      return super.fullName();
    }
  @ASTNodeAnnotation.Attribute
  public boolean sameSignature(Access a) {
    ASTNode$State state = state();
    boolean sameSignature_Access_value = a.type() == this;

    return sameSignature_Access_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean lubType_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl lubType_value;
/**
 * @apilevel internal
 */
private void lubType_reset() {
  lubType_computed = false;
  lubType_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl lubType() {
    if(lubType_computed) {
      return lubType_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    lubType_value = lubType_compute();
    if (isFinal && num == state().boundariesCrossed) {
      lubType_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return lubType_value;
  }
  /**
   * @apilevel internal
   */
  private TypeDecl lubType_compute() {
      ArrayList list = new ArrayList();
      for(int i = 0; i < getNumTypeBound(); i++)
        list.add(getTypeBound(i).type());
      return lookupLUBType(list);
    }
  /**
   * @apilevel internal
   */
  protected int usesTypeVariable_visited = -1;
/**
 * @apilevel internal
 */
private void usesTypeVariable_reset() {
  usesTypeVariable_computed = false;
  usesTypeVariable_initialized = false;
  usesTypeVariable_visited = -1;
}  
  /**
   * @apilevel internal
   */
  protected boolean usesTypeVariable_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean usesTypeVariable_initialized = false;
  /**
   * @apilevel internal
   */
  protected boolean usesTypeVariable_value;
  @ASTNodeAnnotation.Attribute
  public boolean usesTypeVariable() {
    if(usesTypeVariable_computed) {
      return usesTypeVariable_value;
    }
    ASTNode$State state = state();
    boolean new_usesTypeVariable_value;
    if (!usesTypeVariable_initialized) {
      usesTypeVariable_initialized = true;
      usesTypeVariable_value = false;
    }
    if (!state.IN_CIRCLE) {
      state.IN_CIRCLE = true;
      int num = state.boundariesCrossed;
      boolean isFinal = this.is$Final();
      do {
        usesTypeVariable_visited = state.CIRCLE_INDEX;
        state.CHANGE = false;
        new_usesTypeVariable_value = true;
        if (new_usesTypeVariable_value != usesTypeVariable_value) {
          state.CHANGE = true;
        }
        usesTypeVariable_value = new_usesTypeVariable_value;
        state.CIRCLE_INDEX++;
      } while (state.CHANGE);
      if (isFinal && num == state().boundariesCrossed) {
        usesTypeVariable_computed = true;
      } else {
        state.RESET_CYCLE = true;
        boolean $tmp = true;
        state.RESET_CYCLE = false;
        usesTypeVariable_computed = false;
        usesTypeVariable_initialized = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return usesTypeVariable_value;
    }
    if(usesTypeVariable_visited != state.CIRCLE_INDEX) {
      usesTypeVariable_visited = state.CIRCLE_INDEX;
      if (state.RESET_CYCLE) {
        usesTypeVariable_computed = false;
        usesTypeVariable_initialized = false;
        usesTypeVariable_visited = -1;
        return usesTypeVariable_value;
      }
      new_usesTypeVariable_value = true;
      if (new_usesTypeVariable_value != usesTypeVariable_value) {
        state.CHANGE = true;
      }
      usesTypeVariable_value = new_usesTypeVariable_value;
      state.INTERMEDIATE_VALUE = true;
      return usesTypeVariable_value;
    }
    state.INTERMEDIATE_VALUE = true;
    return usesTypeVariable_value;
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
    boolean accessibleFrom_TypeDecl_value = true;
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
  protected boolean typeName_computed = false;
  /**
   * @apilevel internal
   */
  protected String typeName_value;
/**
 * @apilevel internal
 */
private void typeName_reset() {
  typeName_computed = false;
  typeName_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public String typeName() {
    if(typeName_computed) {
      return typeName_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeName_value = name();
    if (isFinal && num == state().boundariesCrossed) {
      typeName_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeName_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isTypeVariable() {
    ASTNode$State state = state();
    boolean isTypeVariable_value = true;

    return isTypeVariable_value;
  }
  /**
   * @apilevel internal
   */
  protected int involvesTypeParameters_visited = -1;
/**
 * @apilevel internal
 */
private void involvesTypeParameters_reset() {
  involvesTypeParameters_computed = false;
  involvesTypeParameters_initialized = false;
  involvesTypeParameters_visited = -1;
}  
  /**
   * @apilevel internal
   */
  protected boolean involvesTypeParameters_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean involvesTypeParameters_initialized = false;
  /**
   * @apilevel internal
   */
  protected boolean involvesTypeParameters_value;
  @ASTNodeAnnotation.Attribute
  public boolean involvesTypeParameters() {
    if(involvesTypeParameters_computed) {
      return involvesTypeParameters_value;
    }
    ASTNode$State state = state();
    boolean new_involvesTypeParameters_value;
    if (!involvesTypeParameters_initialized) {
      involvesTypeParameters_initialized = true;
      involvesTypeParameters_value = false;
    }
    if (!state.IN_CIRCLE) {
      state.IN_CIRCLE = true;
      int num = state.boundariesCrossed;
      boolean isFinal = this.is$Final();
      do {
        involvesTypeParameters_visited = state.CIRCLE_INDEX;
        state.CHANGE = false;
        new_involvesTypeParameters_value = true;
        if (new_involvesTypeParameters_value != involvesTypeParameters_value) {
          state.CHANGE = true;
        }
        involvesTypeParameters_value = new_involvesTypeParameters_value;
        state.CIRCLE_INDEX++;
      } while (state.CHANGE);
      if (isFinal && num == state().boundariesCrossed) {
        involvesTypeParameters_computed = true;
      } else {
        state.RESET_CYCLE = true;
        boolean $tmp = true;
        state.RESET_CYCLE = false;
        involvesTypeParameters_computed = false;
        involvesTypeParameters_initialized = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return involvesTypeParameters_value;
    }
    if(involvesTypeParameters_visited != state.CIRCLE_INDEX) {
      involvesTypeParameters_visited = state.CIRCLE_INDEX;
      if (state.RESET_CYCLE) {
        involvesTypeParameters_computed = false;
        involvesTypeParameters_initialized = false;
        involvesTypeParameters_visited = -1;
        return involvesTypeParameters_value;
      }
      new_involvesTypeParameters_value = true;
      if (new_involvesTypeParameters_value != involvesTypeParameters_value) {
        state.CHANGE = true;
      }
      involvesTypeParameters_value = new_involvesTypeParameters_value;
      state.INTERMEDIATE_VALUE = true;
      return involvesTypeParameters_value;
    }
    state.INTERMEDIATE_VALUE = true;
    return involvesTypeParameters_value;
  }
  @ASTNodeAnnotation.Attribute
  public TypeDecl lowerBound() {
    ASTNode$State state = state();
    TypeDecl lowerBound_value = getTypeBound(0).type();

    return lowerBound_value;
  }
  /**
   * @attribute syn
   * @aspect MemberMethods
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupMethod.jrag:255
   */
  @ASTNodeAnnotation.Attribute
  public Collection<MethodDecl> memberMethods(String name) {
    ASTNode$State state = state();
    try {
        Collection list = new HashSet();
        for(int i = 0; i < getNumTypeBound(); i++) {
          for(Iterator iter = getTypeBound(i).type().memberMethods(name).iterator(); iter.hasNext(); ) {
            MethodDecl decl = (MethodDecl)iter.next();
            //if(decl.accessibleFrom(hostType()))
              list.add(decl);
          }
        }
        return list;
      }
    finally {
    }
  }
  protected java.util.Map memberFields_String_values;
/**
 * @apilevel internal
 */
private void memberFields_String_reset() {
  memberFields_String_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public SimpleSet memberFields(String name) {
    Object _parameters = name;
    if (memberFields_String_values == null) memberFields_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(memberFields_String_values.containsKey(_parameters)) {
      return (SimpleSet)memberFields_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    SimpleSet memberFields_String_value = memberFields_compute(name);
    if (isFinal && num == state().boundariesCrossed) {
      memberFields_String_values.put(_parameters, memberFields_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return memberFields_String_value;
  }
  /**
   * @apilevel internal
   */
  private SimpleSet memberFields_compute(String name) {
      SimpleSet set = SimpleSet.emptySet;
      for(int i = 0; i < getNumTypeBound(); i++) {
        for(Iterator iter = getTypeBound(i).type().memberFields(name).iterator(); iter.hasNext(); ) {
          FieldDeclaration decl = (FieldDeclaration)iter.next();
          //if(decl.accessibleFrom(hostType()))
            set = set.add(decl);
        }
      }
      return set;
    }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeWildcard(WildcardType type) {
    ASTNode$State state = state();
    boolean supertypeWildcard_WildcardType_value = true;

    return supertypeWildcard_WildcardType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeWildcardExtends(WildcardExtendsType type) {
    ASTNode$State state = state();
    boolean supertypeWildcardExtends_WildcardExtendsType_value = type.extendsType().subtype(this);

    return supertypeWildcardExtends_WildcardExtendsType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeWildcardSuper(WildcardSuperType type) {
    ASTNode$State state = state();
    boolean supertypeWildcardSuper_WildcardSuperType_value = type.superType().subtype(this);

    return supertypeWildcardSuper_WildcardSuperType_value;
  }
/**
 * @apilevel internal
 */
private void sameStructure_TypeDecl_reset() {
  sameStructure_TypeDecl_values = null;
}  
  protected java.util.Map sameStructure_TypeDecl_values;
  @ASTNodeAnnotation.Attribute
  public boolean sameStructure(TypeDecl t) {
    Object _parameters = t;
    if (sameStructure_TypeDecl_values == null) sameStructure_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    ASTNode$State.CircularValue _value;
    if(sameStructure_TypeDecl_values.containsKey(_parameters)) {
      Object _o = sameStructure_TypeDecl_values.get(_parameters);
      if(!(_o instanceof ASTNode$State.CircularValue)) {
        return ((Boolean)_o).booleanValue();
      } else {
        _value = (ASTNode$State.CircularValue) _o;
      }
    } else {
      _value = new ASTNode$State.CircularValue();
      sameStructure_TypeDecl_values.put(_parameters, _value);
      _value.value = Boolean.valueOf(true);
    }
    ASTNode$State state = state();
    boolean new_sameStructure_TypeDecl_value;
    if (!state.IN_CIRCLE) {
      state.IN_CIRCLE = true;
      int num = state.boundariesCrossed;
      boolean isFinal = this.is$Final();
      // TODO: fixme
      // state().CIRCLE_INDEX = 1;
      do {
        _value.visited = new Integer(state.CIRCLE_INDEX);
        state.CHANGE = false;
        new_sameStructure_TypeDecl_value = sameStructure_compute(t);
        if (new_sameStructure_TypeDecl_value != ((Boolean)_value.value).booleanValue()) {
          state.CHANGE = true;
          _value.value = Boolean.valueOf(new_sameStructure_TypeDecl_value);
        }
        state.CIRCLE_INDEX++;
      } while (state.CHANGE);
      if (isFinal && num == state().boundariesCrossed) {
        sameStructure_TypeDecl_values.put(_parameters, new_sameStructure_TypeDecl_value);
      } else {
        sameStructure_TypeDecl_values.remove(_parameters);
        state.RESET_CYCLE = true;
        boolean $tmp = sameStructure_compute(t);
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_sameStructure_TypeDecl_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_sameStructure_TypeDecl_value = sameStructure_compute(t);
      if (state.RESET_CYCLE) {
        sameStructure_TypeDecl_values.remove(_parameters);
      }
      else if (new_sameStructure_TypeDecl_value != ((Boolean)_value.value).booleanValue()) {
        state.CHANGE = true;
        _value.value = new_sameStructure_TypeDecl_value;
      }
      state.INTERMEDIATE_VALUE = true;
      return new_sameStructure_TypeDecl_value;
    }
    state.INTERMEDIATE_VALUE = true;
    return ((Boolean)_value.value).booleanValue();
  }
  /**
   * @apilevel internal
   */
  private boolean sameStructure_compute(TypeDecl t) {
      if(!(t instanceof TypeVariable))
        return false;
      if(t == this)
        return true;
      TypeVariable type = (TypeVariable)t;
      if(type.getNumTypeBound() != getNumTypeBound())
        return false;
      for(int i = 0; i < getNumTypeBound(); i++) {
        boolean found = false;
        for(int j = i; !found && j < getNumTypeBound(); j++)
          if(getTypeBound(i).type().sameStructure(type.getTypeBound(j).type()))
            found = true;
        if(!found)
          return false;
      }
      return true;
    }
  /**
   * @attribute syn
   * @aspect GenericsSubtype
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericsSubtype.jrag:454
   */
  @ASTNodeAnnotation.Attribute
  public boolean supertypeArrayDecl(ArrayDecl type) {
    ASTNode$State state = state();
    try {
        for(int i = 0; i < getNumTypeBound(); i++)
          if(type.subtype(getTypeBound(i).type())) {
            return true;
          }
        return false;
      }
    finally {
    }
  }
/**
 * @apilevel internal
 */
private void subtype_TypeDecl_reset() {
  subtype_TypeDecl_values = null;
}  
  protected java.util.Map subtype_TypeDecl_values;
  @ASTNodeAnnotation.Attribute
  public boolean subtype(TypeDecl type) {
    Object _parameters = type;
    if (subtype_TypeDecl_values == null) subtype_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    ASTNode$State.CircularValue _value;
    if(subtype_TypeDecl_values.containsKey(_parameters)) {
      Object _o = subtype_TypeDecl_values.get(_parameters);
      if(!(_o instanceof ASTNode$State.CircularValue)) {
        return ((Boolean)_o).booleanValue();
      } else {
        _value = (ASTNode$State.CircularValue) _o;
      }
    } else {
      _value = new ASTNode$State.CircularValue();
      subtype_TypeDecl_values.put(_parameters, _value);
      _value.value = Boolean.valueOf(true);
    }
    ASTNode$State state = state();
    boolean new_subtype_TypeDecl_value;
    if (!state.IN_CIRCLE) {
      state.IN_CIRCLE = true;
      int num = state.boundariesCrossed;
      boolean isFinal = this.is$Final();
      // TODO: fixme
      // state().CIRCLE_INDEX = 1;
      do {
        _value.visited = new Integer(state.CIRCLE_INDEX);
        state.CHANGE = false;
        new_subtype_TypeDecl_value = type.supertypeTypeVariable(this);
        if (new_subtype_TypeDecl_value != ((Boolean)_value.value).booleanValue()) {
          state.CHANGE = true;
          _value.value = Boolean.valueOf(new_subtype_TypeDecl_value);
        }
        state.CIRCLE_INDEX++;
      } while (state.CHANGE);
      if (isFinal && num == state().boundariesCrossed) {
        subtype_TypeDecl_values.put(_parameters, new_subtype_TypeDecl_value);
      } else {
        subtype_TypeDecl_values.remove(_parameters);
        state.RESET_CYCLE = true;
        boolean $tmp = type.supertypeTypeVariable(this);
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_subtype_TypeDecl_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_subtype_TypeDecl_value = type.supertypeTypeVariable(this);
      if (state.RESET_CYCLE) {
        subtype_TypeDecl_values.remove(_parameters);
      }
      else if (new_subtype_TypeDecl_value != ((Boolean)_value.value).booleanValue()) {
        state.CHANGE = true;
        _value.value = new_subtype_TypeDecl_value;
      }
      state.INTERMEDIATE_VALUE = true;
      return new_subtype_TypeDecl_value;
    }
    state.INTERMEDIATE_VALUE = true;
    return ((Boolean)_value.value).booleanValue();
  }
  /**
   * @attribute syn
   * @aspect GenericsSubtype
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericsSubtype.jrag:291
   */
  @ASTNodeAnnotation.Attribute
  public boolean supertypeTypeVariable(TypeVariable type) {
    ASTNode$State state = state();
    try {
        if(type == this)
          return true;
        for(int i = 0; i < getNumTypeBound(); i++) {
          boolean found = false;
          for(int j = 0; !found && j < type.getNumTypeBound(); j++) {
            if(type.getSubstitutedTypeBound(j, this).type().subtype(getTypeBound(i).type()))
              found = true;
          }
          if(!found)
            return false;
        }
        return true;
      }
    finally {
    }
  }
  protected java.util.Map getSubstitutedTypeBound_int_TypeDecl_values;
/**
 * @apilevel internal
 */
private void getSubstitutedTypeBound_int_TypeDecl_reset() {
  getSubstitutedTypeBound_int_TypeDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public Access getSubstitutedTypeBound(int i, TypeDecl type) {
    java.util.List _parameters = new java.util.ArrayList(2);
    _parameters.add(Integer.valueOf(i));
    _parameters.add(type);
    if (getSubstitutedTypeBound_int_TypeDecl_values == null) getSubstitutedTypeBound_int_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(getSubstitutedTypeBound_int_TypeDecl_values.containsKey(_parameters)) {
      return (Access)getSubstitutedTypeBound_int_TypeDecl_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    Access getSubstitutedTypeBound_int_TypeDecl_value = getSubstitutedTypeBound_compute(i, type);
    if (isFinal && num == state().boundariesCrossed) {
      getSubstitutedTypeBound_int_TypeDecl_values.put(_parameters, getSubstitutedTypeBound_int_TypeDecl_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return getSubstitutedTypeBound_int_TypeDecl_value;
  }
  /**
   * @apilevel internal
   */
  private Access getSubstitutedTypeBound_compute(int i, TypeDecl type) {
      Access bound = getTypeBound(i);
      if(!bound.type().usesTypeVariable())
        return bound;
      final TypeDecl typeDecl = type;
      Access access = bound.type().substitute(
        new Parameterization() {
          public boolean isRawType() {
            return false;
          }
          public TypeDecl substitute(TypeVariable typeVariable) {
            return typeVariable == TypeVariable.this ? typeDecl : typeVariable;
          }
        }
      );
      access.setParent(this);
      return access;
    }
  /**
   * @attribute syn
   * @aspect GenericsSubtype
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericsSubtype.jrag:421
   */
  @ASTNodeAnnotation.Attribute
  public boolean supertypeClassDecl(ClassDecl type) {
    ASTNode$State state = state();
    try {
        for(int i = 0; i < getNumTypeBound(); i++)
          if(!type.subtype(getSubstitutedTypeBound(i, type).type()))
            return false;
        return true;
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect GenericsSubtype
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericsSubtype.jrag:439
   */
  @ASTNodeAnnotation.Attribute
  public boolean supertypeInterfaceDecl(InterfaceDecl type) {
    ASTNode$State state = state();
    try {
        for(int i = 0; i < getNumTypeBound(); i++)
          if(!type.subtype(getSubstitutedTypeBound(i, type).type()))
            return false;
        return true;
      }
    finally {
    }
  }
  protected java.util.Map instanceOf_TypeDecl_values;
/**
 * @apilevel internal
 */
private void instanceOf_TypeDecl_reset() {
  instanceOf_TypeDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean instanceOf(TypeDecl type) {
    Object _parameters = type;
    if (instanceOf_TypeDecl_values == null) instanceOf_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(instanceOf_TypeDecl_values.containsKey(_parameters)) {
      return ((Boolean)instanceOf_TypeDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean instanceOf_TypeDecl_value = subtype(type);
    if (isFinal && num == state().boundariesCrossed) {
      instanceOf_TypeDecl_values.put(_parameters, Boolean.valueOf(instanceOf_TypeDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return instanceOf_TypeDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isReifiable() {
    ASTNode$State state = state();
    boolean isReifiable_value = false;

    return isReifiable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeWildcard(WildcardType type) {
    ASTNode$State state = state();
    boolean strictSupertypeWildcard_WildcardType_value = true;

    return strictSupertypeWildcard_WildcardType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeWildcardExtends(WildcardExtendsType type) {
    ASTNode$State state = state();
    boolean strictSupertypeWildcardExtends_WildcardExtendsType_value = type.extendsType().strictSubtype(this);

    return strictSupertypeWildcardExtends_WildcardExtendsType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeWildcardSuper(WildcardSuperType type) {
    ASTNode$State state = state();
    boolean strictSupertypeWildcardSuper_WildcardSuperType_value = type.superType().strictSubtype(this);

    return strictSupertypeWildcardSuper_WildcardSuperType_value;
  }
  /**
   * @attribute syn
   * @aspect StrictSubtype
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/GenericsSubtype.jrag:374
   */
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeArrayDecl(ArrayDecl type) {
    ASTNode$State state = state();
    try {
        return false;
      }
    finally {
    }
  }
/**
 * @apilevel internal
 */
private void strictSubtype_TypeDecl_reset() {
  strictSubtype_TypeDecl_values = null;
}  
  protected java.util.Map strictSubtype_TypeDecl_values;
  @ASTNodeAnnotation.Attribute
  public boolean strictSubtype(TypeDecl type) {
    Object _parameters = type;
    if (strictSubtype_TypeDecl_values == null) strictSubtype_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    ASTNode$State.CircularValue _value;
    if(strictSubtype_TypeDecl_values.containsKey(_parameters)) {
      Object _o = strictSubtype_TypeDecl_values.get(_parameters);
      if(!(_o instanceof ASTNode$State.CircularValue)) {
        return ((Boolean)_o).booleanValue();
      } else {
        _value = (ASTNode$State.CircularValue) _o;
      }
    } else {
      _value = new ASTNode$State.CircularValue();
      strictSubtype_TypeDecl_values.put(_parameters, _value);
      _value.value = Boolean.valueOf(true);
    }
    ASTNode$State state = state();
    boolean new_strictSubtype_TypeDecl_value;
    if (!state.IN_CIRCLE) {
      state.IN_CIRCLE = true;
      int num = state.boundariesCrossed;
      boolean isFinal = this.is$Final();
      // TODO: fixme
      // state().CIRCLE_INDEX = 1;
      do {
        _value.visited = new Integer(state.CIRCLE_INDEX);
        state.CHANGE = false;
        new_strictSubtype_TypeDecl_value = type.strictSupertypeTypeVariable(this);
        if (new_strictSubtype_TypeDecl_value != ((Boolean)_value.value).booleanValue()) {
          state.CHANGE = true;
          _value.value = Boolean.valueOf(new_strictSubtype_TypeDecl_value);
        }
        state.CIRCLE_INDEX++;
      } while (state.CHANGE);
      if (isFinal && num == state().boundariesCrossed) {
        strictSubtype_TypeDecl_values.put(_parameters, new_strictSubtype_TypeDecl_value);
      } else {
        strictSubtype_TypeDecl_values.remove(_parameters);
        state.RESET_CYCLE = true;
        boolean $tmp = type.strictSupertypeTypeVariable(this);
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_strictSubtype_TypeDecl_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_strictSubtype_TypeDecl_value = type.strictSupertypeTypeVariable(this);
      if (state.RESET_CYCLE) {
        strictSubtype_TypeDecl_values.remove(_parameters);
      }
      else if (new_strictSubtype_TypeDecl_value != ((Boolean)_value.value).booleanValue()) {
        state.CHANGE = true;
        _value.value = new_strictSubtype_TypeDecl_value;
      }
      state.INTERMEDIATE_VALUE = true;
      return new_strictSubtype_TypeDecl_value;
    }
    state.INTERMEDIATE_VALUE = true;
    return ((Boolean)_value.value).booleanValue();
  }
  /**
   * @attribute syn
   * @aspect StrictSubtype
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/GenericsSubtype.jrag:251
   */
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeTypeVariable(TypeVariable type) {
    ASTNode$State state = state();
    try {
    		if(typeVarInMethod() && type.typeVarInMethod() && genericMethodLevel() == type.genericMethodLevel()) {
    			if(typeVarPosition() == type.typeVarPosition() || this == type)
    				return true;
    		}
    		else {
    			if(this == type)
    				return true;
    		}
    		for(int i = 0; i < type.getNumTypeBound(); i++) {
    			if(type.getTypeBound(i).type().strictSubtype(this)) {
    				return true;
    			}
    		}
    		return false;
    	}
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect StrictSubtype
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/GenericsSubtype.jrag:341
   */
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeClassDecl(ClassDecl type) {
    ASTNode$State state = state();
    try {
        return false;
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect StrictSubtype
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/GenericsSubtype.jrag:359
   */
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeInterfaceDecl(InterfaceDecl type) {
    ASTNode$State state = state();
    try {
        return false;
      }
    finally {
    }
  }
  /**
   * @attribute inh
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:790
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeObject() {
    ASTNode$State state = state();
    TypeDecl typeObject_value = getParent().Define_TypeDecl_typeObject(this, null);

    return typeObject_value;
  }
  /**
   * @attribute inh
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:832
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeNull() {
    ASTNode$State state = state();
    TypeDecl typeNull_value = getParent().Define_TypeDecl_typeNull(this, null);

    return typeNull_value;
  }
  /**
   * @attribute inh
   * @aspect TypeVariablePositions
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TypeVariablePositions.jrag:29
   */
  @ASTNodeAnnotation.Attribute
  public int typeVarPosition() {
    if(typeVarPosition_computed) {
      return typeVarPosition_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeVarPosition_value = getParent().Define_int_typeVarPosition(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      typeVarPosition_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeVarPosition_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeVarPosition_computed = false;
  /**
   * @apilevel internal
   */
  protected int typeVarPosition_value;
/**
 * @apilevel internal
 */
private void typeVarPosition_reset() {
  typeVarPosition_computed = false;
}  
  /**
   * @attribute inh
   * @aspect TypeVariablePositions
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TypeVariablePositions.jrag:30
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
   * @attribute inh
   * @aspect TypeVariablePositions
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TypeVariablePositions.jrag:32
   */
  @ASTNodeAnnotation.Attribute
  public boolean typeVarInMethod() {
    if(typeVarInMethod_computed) {
      return typeVarInMethod_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeVarInMethod_value = getParent().Define_boolean_typeVarInMethod(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      typeVarInMethod_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeVarInMethod_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeVarInMethod_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean typeVarInMethod_value;
/**
 * @apilevel internal
 */
private void typeVarInMethod_reset() {
  typeVarInMethod_computed = false;
}  
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericTypeVariables.jrag:13
   * @apilevel internal
   */
  public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
    if (caller == getTypeBoundListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return NameType.TYPE_NAME;
    }
    else {
      return super.Define_NameType_nameType(caller, child);
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    // Declared at @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericTypeVariables.jrag:16
    if (getNumTypeBound() == 0) {
      state().duringGenericTypeVariables++;
      ASTNode result = rewriteRule0();
      state().duringGenericTypeVariables--;
      return result;
    }    return super.rewriteTo();
  }  /**
   * @declaredat @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericTypeVariables.jrag:16
   * @apilevel internal
   */  private TypeVariable rewriteRule0() {
{
      addTypeBound(
        new TypeAccess(
          "java.lang",
          "Object"
        )
      );
      return this;
    }  }
}
