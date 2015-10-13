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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/grammar/Generics.ast:41
 * @production ClassDeclSubstituted : {@link ClassDecl} ::= <span class="component">&lt;Original:TypeDecl&gt;</span> <span class="component">{@link BodyDecl}*</span>;

 */
public class ClassDeclSubstituted extends ClassDecl implements Cloneable, MemberSubstitutor {
  /**
   * @declaredat ASTNode:1
   */
  public ClassDeclSubstituted() {
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
    setChild(new Opt(), 1);
    setChild(new List(), 2);
    setChild(new Opt(), 3);
    setChild(new List(), 4);
  }
  /**
   * @declaredat ASTNode:17
   */
  public ClassDeclSubstituted(Modifiers p0, String p1, Opt<Access> p2, List<Access> p3, TypeDecl p4) {
    setChild(p0, 0);
    setID(p1);
    setChild(p2, 1);
    setChild(p3, 2);
    setOriginal(p4);
  }
  /**
   * @declaredat ASTNode:24
   */
  public ClassDeclSubstituted(Modifiers p0, beaver.Symbol p1, Opt<Access> p2, List<Access> p3, TypeDecl p4) {
    setChild(p0, 0);
    setID(p1);
    setChild(p2, 1);
    setChild(p3, 2);
    setOriginal(p4);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:34
   */
  protected int numChildren() {
    return 3;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:40
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:46
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    getBodyDeclList_reset();
    sourceTypeDecl_reset();
    instanceOf_TypeDecl_reset();
    subtype_TypeDecl_reset();
    strictSubtype_TypeDecl_reset();
    localMethodsSignatureMap_reset();
    localFields_String_reset();
    localTypeDecls_String_reset();
    constructors_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:61
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:67
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:73
   */
  public ClassDeclSubstituted clone() throws CloneNotSupportedException {
    ClassDeclSubstituted node = (ClassDeclSubstituted) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:80
   */
  public ClassDeclSubstituted copy() {
    try {
      ClassDeclSubstituted node = (ClassDeclSubstituted) clone();
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
   * @declaredat ASTNode:99
   */
  public ClassDeclSubstituted fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:108
   */
  public ClassDeclSubstituted treeCopyNoTransform() {
    ClassDeclSubstituted tree = (ClassDeclSubstituted) copy();
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        switch (i) {
        case 3:
          tree.children[i] = new Opt();
          continue;
        case 4:
          tree.children[i] = new List();
          continue;
        }
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
   * @declaredat ASTNode:136
   */
  public ClassDeclSubstituted treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:143
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_ID == ((ClassDeclSubstituted)node).tokenString_ID) && (tokenTypeDecl_Original == ((ClassDeclSubstituted)node).tokenTypeDecl_Original);    
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
   * Replaces the optional node for the SuperClass child. This is the <code>Opt</code>
   * node containing the child SuperClass, not the actual child!
   * @param opt The new node to be used as the optional node for the SuperClass child.
   * @apilevel low-level
   */
  public void setSuperClassOpt(Opt<Access> opt) {
    setChild(opt, 1);
  }
  /**
   * Replaces the (optional) SuperClass child.
   * @param node The new node to be used as the SuperClass child.
   * @apilevel high-level
   */
  public void setSuperClass(Access node) {
    getSuperClassOpt().setChild(node, 0);
  }
  /**
   * Check whether the optional SuperClass child exists.
   * @return {@code true} if the optional SuperClass child exists, {@code false} if it does not.
   * @apilevel high-level
   */
  public boolean hasSuperClass() {
    return getSuperClassOpt().getNumChild() != 0;
  }
  /**
   * Retrieves the (optional) SuperClass child.
   * @return The SuperClass child, if it exists. Returns {@code null} otherwise.
   * @apilevel low-level
   */
  public Access getSuperClass() {
    return (Access) getSuperClassOpt().getChild(0);
  }
  /**
   * Retrieves the optional node for the SuperClass child. This is the <code>Opt</code> node containing the child SuperClass, not the actual child!
   * @return The optional node for child the SuperClass child.
   * @apilevel low-level
   */
  @ASTNodeAnnotation.OptChild(name="SuperClass")
  public Opt<Access> getSuperClassOpt() {
    return (Opt<Access>) getChild(1);
  }
  /**
   * Retrieves the optional node for child SuperClass. This is the <code>Opt</code> node containing the child SuperClass, not the actual child!
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The optional node for child SuperClass.
   * @apilevel low-level
   */
  public Opt<Access> getSuperClassOptNoTransform() {
    return (Opt<Access>) getChildNoTransform(1);
  }
  /**
   * Replaces the Implements list.
   * @param list The new list node to be used as the Implements list.
   * @apilevel high-level
   */
  public void setImplementsList(List<Access> list) {
    setChild(list, 2);
  }
  /**
   * Retrieves the number of children in the Implements list.
   * @return Number of children in the Implements list.
   * @apilevel high-level
   */
  public int getNumImplements() {
    return getImplementsList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Implements list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Implements list.
   * @apilevel low-level
   */
  public int getNumImplementsNoTransform() {
    return getImplementsListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Implements list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Implements list.
   * @apilevel high-level
   */
  public Access getImplements(int i) {
    return (Access) getImplementsList().getChild(i);
  }
  /**
   * Check whether the Implements list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasImplements() {
    return getImplementsList().getNumChild() != 0;
  }
  /**
   * Append an element to the Implements list.
   * @param node The element to append to the Implements list.
   * @apilevel high-level
   */
  public void addImplements(Access node) {
    List<Access> list = (parent == null || state == null) ? getImplementsListNoTransform() : getImplementsList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addImplementsNoTransform(Access node) {
    List<Access> list = getImplementsListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Implements list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setImplements(Access node, int i) {
    List<Access> list = getImplementsList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Implements list.
   * @return The node representing the Implements list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Implements")
  public List<Access> getImplementsList() {
    List<Access> list = (List<Access>) getChild(2);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the Implements list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Implements list.
   * @apilevel low-level
   */
  public List<Access> getImplementsListNoTransform() {
    return (List<Access>) getChildNoTransform(2);
  }
  /**
   * Retrieves the Implements list.
   * @return The node representing the Implements list.
   * @apilevel high-level
   */
  public List<Access> getImplementss() {
    return getImplementsList();
  }
  /**
   * Retrieves the Implements list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Implements list.
   * @apilevel low-level
   */
  public List<Access> getImplementssNoTransform() {
    return getImplementsListNoTransform();
  }
  /**
   * Replaces the lexeme Original.
   * @param value The new value for the lexeme Original.
   * @apilevel high-level
   */
  public void setOriginal(TypeDecl value) {
    tokenTypeDecl_Original = value;
  }
  /**
   * @apilevel internal
   */
  protected TypeDecl tokenTypeDecl_Original;
  /**
   * Retrieves the value for the lexeme Original.
   * @return The value for the lexeme Original.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="Original")
  public TypeDecl getOriginal() {
    return tokenTypeDecl_Original;
  }
  /**
   * Replaces the optional node for the ImplicitConstructor child. This is the <code>Opt</code>
   * node containing the child ImplicitConstructor, not the actual child!
   * @param opt The new node to be used as the optional node for the ImplicitConstructor child.
   * @apilevel low-level
   */
  public void setImplicitConstructorOpt(Opt<ConstructorDecl> opt) {
    setChild(opt, 3);
  }
  /**
   * Replaces the (optional) ImplicitConstructor child.
   * @param node The new node to be used as the ImplicitConstructor child.
   * @apilevel high-level
   */
  public void setImplicitConstructor(ConstructorDecl node) {
    getImplicitConstructorOpt().setChild(node, 0);
  }
  /**
   * Check whether the optional ImplicitConstructor child exists.
   * @return {@code true} if the optional ImplicitConstructor child exists, {@code false} if it does not.
   * @apilevel high-level
   */
  public boolean hasImplicitConstructor() {
    return getImplicitConstructorOpt().getNumChild() != 0;
  }
  /**
   * Retrieves the (optional) ImplicitConstructor child.
   * @return The ImplicitConstructor child, if it exists. Returns {@code null} otherwise.
   * @apilevel low-level
   */
  public ConstructorDecl getImplicitConstructor() {
    return (ConstructorDecl) getImplicitConstructorOpt().getChild(0);
  }
  /**
   * Retrieves the optional node for child ImplicitConstructor. This is the <code>Opt</code> node containing the child ImplicitConstructor, not the actual child!
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The optional node for child ImplicitConstructor.
   * @apilevel low-level
   */
  public Opt<ConstructorDecl> getImplicitConstructorOptNoTransform() {
    return (Opt<ConstructorDecl>) getChildNoTransform(3);
  }
  /**
   * Retrieves the child position of the optional child ImplicitConstructor.
   * @return The the child position of the optional child ImplicitConstructor.
   * @apilevel low-level
   */
  protected int getImplicitConstructorOptChildPosition() {
    return 3;
  }
  /**
   * Replaces the BodyDecl list.
   * @param list The new list node to be used as the BodyDecl list.
   * @apilevel high-level
   */
  public void setBodyDeclList(List<BodyDecl> list) {
    setChild(list, 4);
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
   * Retrieves the child position of the BodyDecl list.
   * @return The the child position of the BodyDecl list.
   * @apilevel low-level
   */
  protected int getBodyDeclListChildPosition() {
    return 4;
  }
  /**
   * Retrieves the BodyDecl list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the BodyDecl list.
   * @apilevel low-level
   */
  public List<BodyDecl> getBodyDeclListNoTransform() {
    return (List<BodyDecl>) getChildNoTransform(4);
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
  @ASTNodeAnnotation.Attribute
  public TypeDecl hostType() {
    ASTNode$State state = state();
    TypeDecl hostType_value = getOriginal();

    return hostType_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean getBodyDeclList_computed = false;
  /**
   * @apilevel internal
   */
  protected List getBodyDeclList_value;
/**
 * @apilevel internal
 */
private void getBodyDeclList_reset() {
  getBodyDeclList_computed = false;
  getBodyDeclList_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public List getBodyDeclList() {
    if(getBodyDeclList_computed) {
      return (List) getChild(getBodyDeclListChildPosition());
    }
    if(getBodyDeclList_computed) {
      return getBodyDeclList_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    getBodyDeclList_value = new BodyDeclList();
    setBodyDeclList(getBodyDeclList_value);
    if (isFinal && num == state().boundariesCrossed) {
      getBodyDeclList_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    List node = (List) this.getChild(getBodyDeclListChildPosition());
    return node;
  }
  @ASTNodeAnnotation.Attribute
  public TypeDecl original() {
    ASTNode$State state = state();
    TypeDecl original_value = getOriginal().original();

    return original_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean sourceTypeDecl_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl sourceTypeDecl_value;
/**
 * @apilevel internal
 */
private void sourceTypeDecl_reset() {
  sourceTypeDecl_computed = false;
  sourceTypeDecl_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl sourceTypeDecl() {
    if(sourceTypeDecl_computed) {
      return sourceTypeDecl_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    sourceTypeDecl_value = original().sourceTypeDecl();
    if (isFinal && num == state().boundariesCrossed) {
      sourceTypeDecl_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return sourceTypeDecl_value;
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
        new_subtype_TypeDecl_value = type.supertypeClassDeclSubstituted(this);
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
        boolean $tmp = type.supertypeClassDeclSubstituted(this);
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_subtype_TypeDecl_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_subtype_TypeDecl_value = type.supertypeClassDeclSubstituted(this);
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
  public boolean supertypeClassDeclSubstituted(ClassDeclSubstituted type) {
    ASTNode$State state = state();
    boolean supertypeClassDeclSubstituted_ClassDeclSubstituted_value = original() == type.original() && type.enclosingType().subtype(enclosingType()) || super.supertypeClassDeclSubstituted(type);

    return supertypeClassDeclSubstituted_ClassDeclSubstituted_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeClassDecl(ClassDecl type) {
    ASTNode$State state = state();
    boolean supertypeClassDecl_ClassDecl_value = super.supertypeClassDecl(type) || original().supertypeClassDecl(type);

    return supertypeClassDecl_ClassDecl_value;
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
        new_strictSubtype_TypeDecl_value = type.strictSupertypeClassDeclSubstituted(this);
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
        boolean $tmp = type.strictSupertypeClassDeclSubstituted(this);
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_strictSubtype_TypeDecl_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_strictSubtype_TypeDecl_value = type.strictSupertypeClassDeclSubstituted(this);
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
  public boolean strictSupertypeClassDeclSubstituted(ClassDeclSubstituted type) {
    ASTNode$State state = state();
    boolean strictSupertypeClassDeclSubstituted_ClassDeclSubstituted_value = original() == type.original() && type.enclosingType().strictSubtype(enclosingType()) || super.strictSupertypeClassDeclSubstituted(type);

    return strictSupertypeClassDeclSubstituted_ClassDeclSubstituted_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeClassDecl(ClassDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeClassDecl_ClassDecl_value = super.strictSupertypeClassDecl(type) || original().strictSupertypeClassDecl(type);

    return strictSupertypeClassDecl_ClassDecl_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean localMethodsSignatureMap_computed = false;
  /**
   * @apilevel internal
   */
  protected HashMap localMethodsSignatureMap_value;
/**
 * @apilevel internal
 */
private void localMethodsSignatureMap_reset() {
  localMethodsSignatureMap_computed = false;
  localMethodsSignatureMap_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public HashMap localMethodsSignatureMap() {
    if(localMethodsSignatureMap_computed) {
      return localMethodsSignatureMap_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    localMethodsSignatureMap_value = localMethodsSignatureMap_compute();
    if (true) {
      localMethodsSignatureMap_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return localMethodsSignatureMap_value;
  }
  /**
   * @apilevel internal
   */
  private HashMap localMethodsSignatureMap_compute() {
      HashMap map = new HashMap();
      for(Iterator iter = original().localMethodsIterator(); iter.hasNext(); ) {
        MethodDecl decl = (MethodDecl)iter.next();
  
        if(!decl.isStatic() && (decl.usesTypeVariable() || isRawType())) {
          BodyDecl copyDecl = ((BodyDeclList)getBodyDeclList()).localMethodSignatureCopy(decl, this);
          decl = (MethodDecl) copyDecl;
        }
        map.put(decl.signature(), decl);
  
      }
      return map;
    }
  protected java.util.Map localFields_String_values;
/**
 * @apilevel internal
 */
private void localFields_String_reset() {
  localFields_String_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public SimpleSet localFields(String name) {
    Object _parameters = name;
    if (localFields_String_values == null) localFields_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(localFields_String_values.containsKey(_parameters)) {
      return (SimpleSet)localFields_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    SimpleSet localFields_String_value = localFields_compute(name);
    if (true) {
      localFields_String_values.put(_parameters, localFields_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return localFields_String_value;
  }
  /**
   * @apilevel internal
   */
  private SimpleSet localFields_compute(String name) {
      SimpleSet set = SimpleSet.emptySet;
      for(Iterator iter = original().localFields(name).iterator(); iter.hasNext(); ) {
        FieldDeclaration f = (FieldDeclaration)iter.next();
  
        if(!f.isStatic() && (f.usesTypeVariable() || isRawType())) {
          BodyDecl fCopy = ((BodyDeclList)getBodyDeclList()).localFieldCopy(f, this);
          f = (FieldDeclaration) fCopy;
        }
        set = set.add(f);
  
      }
      return set;
    }
/**
 * @apilevel internal
 */
private void localTypeDecls_String_reset() {
  localTypeDecls_String_values = null;
}  
  protected java.util.Map localTypeDecls_String_values;
  @ASTNodeAnnotation.Attribute
  public SimpleSet localTypeDecls(String name) {
    Object _parameters = name;
    if (localTypeDecls_String_values == null) localTypeDecls_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    ASTNode$State.CircularValue _value;
    if(localTypeDecls_String_values.containsKey(_parameters)) {
      Object _o = localTypeDecls_String_values.get(_parameters);
      if(!(_o instanceof ASTNode$State.CircularValue)) {
        return (SimpleSet)_o;
      } else {
        _value = (ASTNode$State.CircularValue) _o;
      }
    } else {
      _value = new ASTNode$State.CircularValue();
      localTypeDecls_String_values.put(_parameters, _value);
      _value.value = SimpleSet.emptySet;
    }
    ASTNode$State state = state();
    SimpleSet new_localTypeDecls_String_value;
    if (!state.IN_CIRCLE) {
      state.IN_CIRCLE = true;
      int num = state.boundariesCrossed;
      boolean isFinal = this.is$Final();
      // TODO: fixme
      // state().CIRCLE_INDEX = 1;
      do {
        _value.visited = new Integer(state.CIRCLE_INDEX);
        state.CHANGE = false;
        new_localTypeDecls_String_value = localTypeDecls_compute(name);
        if ((new_localTypeDecls_String_value==null && (SimpleSet)_value.value!=null) || (new_localTypeDecls_String_value!=null && !new_localTypeDecls_String_value.equals((SimpleSet)_value.value))) {
          state.CHANGE = true;
          _value.value = new_localTypeDecls_String_value;
        }
        state.CIRCLE_INDEX++;
      } while (state.CHANGE);
      if (true) {
        localTypeDecls_String_values.put(_parameters, new_localTypeDecls_String_value);
      } else {
        localTypeDecls_String_values.remove(_parameters);
        state.RESET_CYCLE = true;
        SimpleSet $tmp = localTypeDecls_compute(name);
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_localTypeDecls_String_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_localTypeDecls_String_value = localTypeDecls_compute(name);
      if (state.RESET_CYCLE) {
        localTypeDecls_String_values.remove(_parameters);
      }
      else if ((new_localTypeDecls_String_value==null && (SimpleSet)_value.value!=null) || (new_localTypeDecls_String_value!=null && !new_localTypeDecls_String_value.equals((SimpleSet)_value.value))) {
        state.CHANGE = true;
        _value.value = new_localTypeDecls_String_value;
      }
      state.INTERMEDIATE_VALUE = true;
      return new_localTypeDecls_String_value;
    }
    state.INTERMEDIATE_VALUE = true;
    return (SimpleSet)_value.value;
  }
  /**
   * @apilevel internal
   */
  private SimpleSet localTypeDecls_compute(String name) {
      SimpleSet set = SimpleSet.emptySet;
      for(Iterator iter = original().localTypeDecls(name).iterator(); iter.hasNext(); ) {
        TypeDecl t = (TypeDecl)iter.next();
  
        if(t.isStatic()) {
          set = set.add(t);
        } else if (t instanceof ClassDecl) {
          MemberClassDecl copy = ((BodyDeclList)getBodyDeclList()).localClassDeclCopy((ClassDecl)t, this);
          set = set.add(copy.getClassDecl());
        } else if (t instanceof InterfaceDecl) {
          MemberInterfaceDecl copy = ((BodyDeclList)getBodyDeclList()).localInterfaceDeclCopy((InterfaceDecl)t, this);
          set = set.add(copy.getInterfaceDecl());
        }
      }
      return set;
    }
  /**
   * @apilevel internal
   */
  protected boolean constructors_computed = false;
  /**
   * @apilevel internal
   */
  protected Collection constructors_value;
/**
 * @apilevel internal
 */
private void constructors_reset() {
  constructors_computed = false;
  constructors_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Collection constructors() {
    if(constructors_computed) {
      return constructors_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    constructors_value = constructors_compute();
    if (isFinal && num == state().boundariesCrossed) {
      constructors_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return constructors_value;
  }
  /**
   * @apilevel internal
   */
  private Collection constructors_compute() {
      Collection set = new ArrayList();
      for(Iterator iter = original().constructors().iterator(); iter.hasNext(); ) {
        ConstructorDecl c = (ConstructorDecl)iter.next();
  
        BodyDecl b = ((BodyDeclList)getBodyDeclList()).constructorCopy(c, this);
        set.add(b);
      }
      return set;
    }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
