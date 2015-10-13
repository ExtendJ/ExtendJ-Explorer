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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/grammar/Generics.ast:27
 * @production WildcardExtendsType : {@link AbstractWildcardType} ::= <span class="component">{@link Access}</span>;

 */
public class WildcardExtendsType extends AbstractWildcardType implements Cloneable {
  /**
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:773
   */
  public Access substitute(Parameterization parTypeDecl) {
    if(!usesTypeVariable())
      return super.substitute(parTypeDecl);
    return new WildcardExtends(getAccess().type().substitute(parTypeDecl));
  }
  /**
   * @declaredat ASTNode:1
   */
  public WildcardExtendsType() {
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
  }
  /**
   * @declaredat ASTNode:14
   */
  public WildcardExtendsType(Modifiers p0, String p1, List<BodyDecl> p2, Access p3) {
    setChild(p0, 0);
    setID(p1);
    setChild(p2, 1);
    setChild(p3, 2);
  }
  /**
   * @declaredat ASTNode:20
   */
  public WildcardExtendsType(Modifiers p0, beaver.Symbol p1, List<BodyDecl> p2, Access p3) {
    setChild(p0, 0);
    setID(p1);
    setChild(p2, 1);
    setChild(p3, 2);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:29
   */
  protected int numChildren() {
    return 3;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:35
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:41
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    usesTypeVariable_reset();
    involvesTypeParameters_reset();
    subtype_TypeDecl_reset();
    containedIn_TypeDecl_reset();
    sameStructure_TypeDecl_reset();
    instanceOf_TypeDecl_reset();
    strictSubtype_TypeDecl_reset();
    strictContainedIn_TypeDecl_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:55
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:61
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:67
   */
  public WildcardExtendsType clone() throws CloneNotSupportedException {
    WildcardExtendsType node = (WildcardExtendsType) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:74
   */
  public WildcardExtendsType copy() {
    try {
      WildcardExtendsType node = (WildcardExtendsType) clone();
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
   * @declaredat ASTNode:93
   */
  public WildcardExtendsType fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:102
   */
  public WildcardExtendsType treeCopyNoTransform() {
    WildcardExtendsType tree = (WildcardExtendsType) copy();
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
   * @declaredat ASTNode:122
   */
  public WildcardExtendsType treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:129
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_ID == ((WildcardExtendsType)node).tokenString_ID);    
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
   * Replaces the Access child.
   * @param node The new node to replace the Access child.
   * @apilevel high-level
   */
  public void setAccess(Access node) {
    setChild(node, 2);
  }
  /**
   * Retrieves the Access child.
   * @return The current node used as the Access child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Access")
  public Access getAccess() {
    return (Access) getChild(2);
  }
  /**
   * Retrieves the Access child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Access child.
   * @apilevel low-level
   */
  public Access getAccessNoTransform() {
    return (Access) getChildNoTransform(2);
  }
  /**
   * @attribute syn
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:584
   */
  @ASTNodeAnnotation.Attribute
  public boolean sameSignature(Access a) {
    ASTNode$State state = state();
    try {
        if(a instanceof WildcardExtends)
          return getAccess().type().sameSignature(((WildcardExtends)a).getAccess());
        return super.sameSignature(a);
      }
    finally {
    }
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
        new_usesTypeVariable_value = getAccess().type().usesTypeVariable();
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
        boolean $tmp = getAccess().type().usesTypeVariable();
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
      new_usesTypeVariable_value = getAccess().type().usesTypeVariable();
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
  @ASTNodeAnnotation.Attribute
  public TypeDecl extendsType() {
    ASTNode$State state = state();
    TypeDecl extendsType_value = getAccess().type();

    return extendsType_value;
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
        new_involvesTypeParameters_value = extendsType().involvesTypeParameters();
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
        boolean $tmp = extendsType().involvesTypeParameters();
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
      new_involvesTypeParameters_value = extendsType().involvesTypeParameters();
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
  public boolean supertypeWildcard(WildcardType type) {
    ASTNode$State state = state();
    boolean supertypeWildcard_WildcardType_value = typeObject().subtype(this);

    return supertypeWildcard_WildcardType_value;
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
        new_subtype_TypeDecl_value = type.supertypeWildcardExtends(this);
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
        boolean $tmp = type.supertypeWildcardExtends(this);
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_subtype_TypeDecl_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_subtype_TypeDecl_value = type.supertypeWildcardExtends(this);
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
  @ASTNodeAnnotation.Attribute
  public boolean supertypeWildcardExtends(WildcardExtendsType type) {
    ASTNode$State state = state();
    boolean supertypeWildcardExtends_WildcardExtendsType_value = type.extendsType().subtype(extendsType());

    return supertypeWildcardExtends_WildcardExtendsType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeClassDecl(ClassDecl type) {
    ASTNode$State state = state();
    boolean supertypeClassDecl_ClassDecl_value = type.subtype(extendsType());

    return supertypeClassDecl_ClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeInterfaceDecl(InterfaceDecl type) {
    ASTNode$State state = state();
    boolean supertypeInterfaceDecl_InterfaceDecl_value = type.subtype(extendsType());

    return supertypeInterfaceDecl_InterfaceDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeParClassDecl(ParClassDecl type) {
    ASTNode$State state = state();
    boolean supertypeParClassDecl_ParClassDecl_value = type.subtype(extendsType());

    return supertypeParClassDecl_ParClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeParInterfaceDecl(ParInterfaceDecl type) {
    ASTNode$State state = state();
    boolean supertypeParInterfaceDecl_ParInterfaceDecl_value = type.subtype(extendsType());

    return supertypeParInterfaceDecl_ParInterfaceDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeRawClassDecl(RawClassDecl type) {
    ASTNode$State state = state();
    boolean supertypeRawClassDecl_RawClassDecl_value = type.subtype(extendsType());

    return supertypeRawClassDecl_RawClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeRawInterfaceDecl(RawInterfaceDecl type) {
    ASTNode$State state = state();
    boolean supertypeRawInterfaceDecl_RawInterfaceDecl_value = type.subtype(extendsType());

    return supertypeRawInterfaceDecl_RawInterfaceDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeTypeVariable(TypeVariable type) {
    ASTNode$State state = state();
    boolean supertypeTypeVariable_TypeVariable_value = type.subtype(extendsType());

    return supertypeTypeVariable_TypeVariable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeArrayDecl(ArrayDecl type) {
    ASTNode$State state = state();
    boolean supertypeArrayDecl_ArrayDecl_value = type.subtype(extendsType());

    return supertypeArrayDecl_ArrayDecl_value;
  }
/**
 * @apilevel internal
 */
private void containedIn_TypeDecl_reset() {
  containedIn_TypeDecl_values = null;
}  
  protected java.util.Map containedIn_TypeDecl_values;
  @ASTNodeAnnotation.Attribute
  public boolean containedIn(TypeDecl type) {
    Object _parameters = type;
    if (containedIn_TypeDecl_values == null) containedIn_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    ASTNode$State.CircularValue _value;
    if(containedIn_TypeDecl_values.containsKey(_parameters)) {
      Object _o = containedIn_TypeDecl_values.get(_parameters);
      if(!(_o instanceof ASTNode$State.CircularValue)) {
        return ((Boolean)_o).booleanValue();
      } else {
        _value = (ASTNode$State.CircularValue) _o;
      }
    } else {
      _value = new ASTNode$State.CircularValue();
      containedIn_TypeDecl_values.put(_parameters, _value);
      _value.value = Boolean.valueOf(true);
    }
    ASTNode$State state = state();
    boolean new_containedIn_TypeDecl_value;
    if (!state.IN_CIRCLE) {
      state.IN_CIRCLE = true;
      int num = state.boundariesCrossed;
      boolean isFinal = this.is$Final();
      // TODO: fixme
      // state().CIRCLE_INDEX = 1;
      do {
        _value.visited = new Integer(state.CIRCLE_INDEX);
        state.CHANGE = false;
        new_containedIn_TypeDecl_value = containedIn_compute(type);
        if (new_containedIn_TypeDecl_value != ((Boolean)_value.value).booleanValue()) {
          state.CHANGE = true;
          _value.value = Boolean.valueOf(new_containedIn_TypeDecl_value);
        }
        state.CIRCLE_INDEX++;
      } while (state.CHANGE);
      if (isFinal && num == state().boundariesCrossed) {
        containedIn_TypeDecl_values.put(_parameters, new_containedIn_TypeDecl_value);
      } else {
        containedIn_TypeDecl_values.remove(_parameters);
        state.RESET_CYCLE = true;
        boolean $tmp = containedIn_compute(type);
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_containedIn_TypeDecl_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_containedIn_TypeDecl_value = containedIn_compute(type);
      if (state.RESET_CYCLE) {
        containedIn_TypeDecl_values.remove(_parameters);
      }
      else if (new_containedIn_TypeDecl_value != ((Boolean)_value.value).booleanValue()) {
        state.CHANGE = true;
        _value.value = new_containedIn_TypeDecl_value;
      }
      state.INTERMEDIATE_VALUE = true;
      return new_containedIn_TypeDecl_value;
    }
    state.INTERMEDIATE_VALUE = true;
    return ((Boolean)_value.value).booleanValue();
  }
  /**
   * @apilevel internal
   */
  private boolean containedIn_compute(TypeDecl type) {
      if(type == this || type instanceof WildcardType)
        return true;
      else if(type instanceof WildcardExtendsType)
        return extendsType().subtype(((WildcardExtendsType)type).extendsType());
      else
        return false;
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
        new_sameStructure_TypeDecl_value = super.sameStructure(t) ||
            t instanceof WildcardExtendsType && ((WildcardExtendsType)t).extendsType().sameStructure(extendsType());
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
        boolean $tmp = super.sameStructure(t) ||
            t instanceof WildcardExtendsType && ((WildcardExtendsType)t).extendsType().sameStructure(extendsType());
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_sameStructure_TypeDecl_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_sameStructure_TypeDecl_value = super.sameStructure(t) ||
          t instanceof WildcardExtendsType && ((WildcardExtendsType)t).extendsType().sameStructure(extendsType());
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
  public boolean strictSupertypeWildcard(WildcardType type) {
    ASTNode$State state = state();
    boolean strictSupertypeWildcard_WildcardType_value = typeObject().strictSubtype(this);

    return strictSupertypeWildcard_WildcardType_value;
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
        new_strictSubtype_TypeDecl_value = type.strictSupertypeWildcardExtends(this);
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
        boolean $tmp = type.strictSupertypeWildcardExtends(this);
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_strictSubtype_TypeDecl_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_strictSubtype_TypeDecl_value = type.strictSupertypeWildcardExtends(this);
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
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeWildcardExtends(WildcardExtendsType type) {
    ASTNode$State state = state();
    boolean strictSupertypeWildcardExtends_WildcardExtendsType_value = type.extendsType().strictSubtype(extendsType());

    return strictSupertypeWildcardExtends_WildcardExtendsType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeClassDecl(ClassDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeClassDecl_ClassDecl_value = type.strictSubtype(extendsType());

    return strictSupertypeClassDecl_ClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeInterfaceDecl(InterfaceDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeInterfaceDecl_InterfaceDecl_value = type.strictSubtype(extendsType());

    return strictSupertypeInterfaceDecl_InterfaceDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeParClassDecl(ParClassDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeParClassDecl_ParClassDecl_value = type.strictSubtype(extendsType());

    return strictSupertypeParClassDecl_ParClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeParInterfaceDecl(ParInterfaceDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeParInterfaceDecl_ParInterfaceDecl_value = type.strictSubtype(extendsType());

    return strictSupertypeParInterfaceDecl_ParInterfaceDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeRawClassDecl(RawClassDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeRawClassDecl_RawClassDecl_value = type.strictSubtype(extendsType());

    return strictSupertypeRawClassDecl_RawClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeRawInterfaceDecl(RawInterfaceDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeRawInterfaceDecl_RawInterfaceDecl_value = type.strictSubtype(extendsType());

    return strictSupertypeRawInterfaceDecl_RawInterfaceDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeTypeVariable(TypeVariable type) {
    ASTNode$State state = state();
    boolean strictSupertypeTypeVariable_TypeVariable_value = type.strictSubtype(extendsType());

    return strictSupertypeTypeVariable_TypeVariable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeArrayDecl(ArrayDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeArrayDecl_ArrayDecl_value = type.strictSubtype(extendsType());

    return strictSupertypeArrayDecl_ArrayDecl_value;
  }
/**
 * @apilevel internal
 */
private void strictContainedIn_TypeDecl_reset() {
  strictContainedIn_TypeDecl_values = null;
}  
  protected java.util.Map strictContainedIn_TypeDecl_values;
  @ASTNodeAnnotation.Attribute
  public boolean strictContainedIn(TypeDecl type) {
    Object _parameters = type;
    if (strictContainedIn_TypeDecl_values == null) strictContainedIn_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    ASTNode$State.CircularValue _value;
    if(strictContainedIn_TypeDecl_values.containsKey(_parameters)) {
      Object _o = strictContainedIn_TypeDecl_values.get(_parameters);
      if(!(_o instanceof ASTNode$State.CircularValue)) {
        return ((Boolean)_o).booleanValue();
      } else {
        _value = (ASTNode$State.CircularValue) _o;
      }
    } else {
      _value = new ASTNode$State.CircularValue();
      strictContainedIn_TypeDecl_values.put(_parameters, _value);
      _value.value = Boolean.valueOf(true);
    }
    ASTNode$State state = state();
    boolean new_strictContainedIn_TypeDecl_value;
    if (!state.IN_CIRCLE) {
      state.IN_CIRCLE = true;
      int num = state.boundariesCrossed;
      boolean isFinal = this.is$Final();
      // TODO: fixme
      // state().CIRCLE_INDEX = 1;
      do {
        _value.visited = new Integer(state.CIRCLE_INDEX);
        state.CHANGE = false;
        new_strictContainedIn_TypeDecl_value = strictContainedIn_compute(type);
        if (new_strictContainedIn_TypeDecl_value != ((Boolean)_value.value).booleanValue()) {
          state.CHANGE = true;
          _value.value = Boolean.valueOf(new_strictContainedIn_TypeDecl_value);
        }
        state.CIRCLE_INDEX++;
      } while (state.CHANGE);
      if (isFinal && num == state().boundariesCrossed) {
        strictContainedIn_TypeDecl_values.put(_parameters, new_strictContainedIn_TypeDecl_value);
      } else {
        strictContainedIn_TypeDecl_values.remove(_parameters);
        state.RESET_CYCLE = true;
        boolean $tmp = strictContainedIn_compute(type);
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_strictContainedIn_TypeDecl_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_strictContainedIn_TypeDecl_value = strictContainedIn_compute(type);
      if (state.RESET_CYCLE) {
        strictContainedIn_TypeDecl_values.remove(_parameters);
      }
      else if (new_strictContainedIn_TypeDecl_value != ((Boolean)_value.value).booleanValue()) {
        state.CHANGE = true;
        _value.value = new_strictContainedIn_TypeDecl_value;
      }
      state.INTERMEDIATE_VALUE = true;
      return new_strictContainedIn_TypeDecl_value;
    }
    state.INTERMEDIATE_VALUE = true;
    return ((Boolean)_value.value).booleanValue();
  }
  /**
   * @apilevel internal
   */
  private boolean strictContainedIn_compute(TypeDecl type) {
      if(type == this || type instanceof WildcardType)
        return true;
      else if(type instanceof WildcardExtendsType)
        return extendsType().strictSubtype(((WildcardExtendsType)type).extendsType());
      else
        return false;
    }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
