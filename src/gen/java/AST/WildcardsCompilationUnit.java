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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/grammar/Generics.ast:49
 * @production WildcardsCompilationUnit : {@link CompilationUnit};

 */
public class WildcardsCompilationUnit extends CompilationUnit implements Cloneable {
  /**
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:1206
   */
  public static LUBType createLUBType(Collection bounds) {
    List boundList = new List();
    StringBuilder name = new StringBuilder();
    for(Iterator iter = bounds.iterator(); iter.hasNext(); ) {
      TypeDecl typeDecl = (TypeDecl)iter.next();
      boundList.add(typeDecl.createBoundAccess());
      name.append("& " + typeDecl.typeName());
    }
    LUBType decl = new LUBType(
      new Modifiers(new List().add(new Modifier("public"))),
      name.toString(),
      new List(),
      boundList
    );
    return decl;
  }
  /**
   * @declaredat ASTNode:1
   */
  public WildcardsCompilationUnit() {
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
    setChild(new List(), 1);
  }
  /**
   * @declaredat ASTNode:15
   */
  public WildcardsCompilationUnit(String p0, List<ImportDecl> p1, List<TypeDecl> p2) {
    setPackageDecl(p0);
    setChild(p1, 0);
    setChild(p2, 1);
  }
  /**
   * @declaredat ASTNode:20
   */
  public WildcardsCompilationUnit(beaver.Symbol p0, List<ImportDecl> p1, List<TypeDecl> p2) {
    setPackageDecl(p0);
    setChild(p1, 0);
    setChild(p2, 1);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:28
   */
  protected int numChildren() {
    return 2;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:34
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:40
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    typeWildcard_reset();
    lookupWildcardExtends_TypeDecl_reset();
    lookupWildcardSuper_TypeDecl_reset();
    lookupLUBType_Collection_reset();
    lookupGLBType_ArrayList_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:51
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:57
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:63
   */
  public WildcardsCompilationUnit clone() throws CloneNotSupportedException {
    WildcardsCompilationUnit node = (WildcardsCompilationUnit) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:70
   */
  public WildcardsCompilationUnit copy() {
    try {
      WildcardsCompilationUnit node = (WildcardsCompilationUnit) clone();
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
   * @declaredat ASTNode:89
   */
  public WildcardsCompilationUnit fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:98
   */
  public WildcardsCompilationUnit treeCopyNoTransform() {
    WildcardsCompilationUnit tree = (WildcardsCompilationUnit) copy();
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
   * @declaredat ASTNode:118
   */
  public WildcardsCompilationUnit treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:125
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_PackageDecl == ((WildcardsCompilationUnit)node).tokenString_PackageDecl);    
  }
  /**
   * Replaces the lexeme PackageDecl.
   * @param value The new value for the lexeme PackageDecl.
   * @apilevel high-level
   */
  public void setPackageDecl(String value) {
    tokenString_PackageDecl = value;
  }
  /**
   * JastAdd-internal setter for lexeme PackageDecl using the Beaver parser.
   * @param symbol Symbol containing the new value for the lexeme PackageDecl
   * @apilevel internal
   */
  public void setPackageDecl(beaver.Symbol symbol) {
    if(symbol.value != null && !(symbol.value instanceof String))
    throw new UnsupportedOperationException("setPackageDecl is only valid for String lexemes");
    tokenString_PackageDecl = (String)symbol.value;
    PackageDeclstart = symbol.getStart();
    PackageDeclend = symbol.getEnd();
  }
  /**
   * Retrieves the value for the lexeme PackageDecl.
   * @return The value for the lexeme PackageDecl.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="PackageDecl")
  public String getPackageDecl() {
    return tokenString_PackageDecl != null ? tokenString_PackageDecl : "";
  }
  /**
   * Replaces the ImportDecl list.
   * @param list The new list node to be used as the ImportDecl list.
   * @apilevel high-level
   */
  public void setImportDeclList(List<ImportDecl> list) {
    setChild(list, 0);
  }
  /**
   * Retrieves the number of children in the ImportDecl list.
   * @return Number of children in the ImportDecl list.
   * @apilevel high-level
   */
  public int getNumImportDecl() {
    return getImportDeclList().getNumChild();
  }
  /**
   * Retrieves the number of children in the ImportDecl list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the ImportDecl list.
   * @apilevel low-level
   */
  public int getNumImportDeclNoTransform() {
    return getImportDeclListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the ImportDecl list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the ImportDecl list.
   * @apilevel high-level
   */
  public ImportDecl getImportDecl(int i) {
    return (ImportDecl) getImportDeclList().getChild(i);
  }
  /**
   * Check whether the ImportDecl list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasImportDecl() {
    return getImportDeclList().getNumChild() != 0;
  }
  /**
   * Append an element to the ImportDecl list.
   * @param node The element to append to the ImportDecl list.
   * @apilevel high-level
   */
  public void addImportDecl(ImportDecl node) {
    List<ImportDecl> list = (parent == null || state == null) ? getImportDeclListNoTransform() : getImportDeclList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addImportDeclNoTransform(ImportDecl node) {
    List<ImportDecl> list = getImportDeclListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the ImportDecl list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setImportDecl(ImportDecl node, int i) {
    List<ImportDecl> list = getImportDeclList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the ImportDecl list.
   * @return The node representing the ImportDecl list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="ImportDecl")
  public List<ImportDecl> getImportDeclList() {
    List<ImportDecl> list = (List<ImportDecl>) getChild(0);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the ImportDecl list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the ImportDecl list.
   * @apilevel low-level
   */
  public List<ImportDecl> getImportDeclListNoTransform() {
    return (List<ImportDecl>) getChildNoTransform(0);
  }
  /**
   * Retrieves the ImportDecl list.
   * @return The node representing the ImportDecl list.
   * @apilevel high-level
   */
  public List<ImportDecl> getImportDecls() {
    return getImportDeclList();
  }
  /**
   * Retrieves the ImportDecl list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the ImportDecl list.
   * @apilevel low-level
   */
  public List<ImportDecl> getImportDeclsNoTransform() {
    return getImportDeclListNoTransform();
  }
  /**
   * Replaces the TypeDecl list.
   * @param list The new list node to be used as the TypeDecl list.
   * @apilevel high-level
   */
  public void setTypeDeclList(List<TypeDecl> list) {
    setChild(list, 1);
  }
  /**
   * Retrieves the number of children in the TypeDecl list.
   * @return Number of children in the TypeDecl list.
   * @apilevel high-level
   */
  public int getNumTypeDecl() {
    return getTypeDeclList().getNumChild();
  }
  /**
   * Retrieves the number of children in the TypeDecl list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the TypeDecl list.
   * @apilevel low-level
   */
  public int getNumTypeDeclNoTransform() {
    return getTypeDeclListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the TypeDecl list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the TypeDecl list.
   * @apilevel high-level
   */
  public TypeDecl getTypeDecl(int i) {
    return (TypeDecl) getTypeDeclList().getChild(i);
  }
  /**
   * Check whether the TypeDecl list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasTypeDecl() {
    return getTypeDeclList().getNumChild() != 0;
  }
  /**
   * Append an element to the TypeDecl list.
   * @param node The element to append to the TypeDecl list.
   * @apilevel high-level
   */
  public void addTypeDecl(TypeDecl node) {
    List<TypeDecl> list = (parent == null || state == null) ? getTypeDeclListNoTransform() : getTypeDeclList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addTypeDeclNoTransform(TypeDecl node) {
    List<TypeDecl> list = getTypeDeclListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the TypeDecl list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setTypeDecl(TypeDecl node, int i) {
    List<TypeDecl> list = getTypeDeclList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the TypeDecl list.
   * @return The node representing the TypeDecl list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="TypeDecl")
  public List<TypeDecl> getTypeDeclList() {
    List<TypeDecl> list = (List<TypeDecl>) getChild(1);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the TypeDecl list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the TypeDecl list.
   * @apilevel low-level
   */
  public List<TypeDecl> getTypeDeclListNoTransform() {
    return (List<TypeDecl>) getChildNoTransform(1);
  }
  /**
   * Retrieves the TypeDecl list.
   * @return The node representing the TypeDecl list.
   * @apilevel high-level
   */
  public List<TypeDecl> getTypeDecls() {
    return getTypeDeclList();
  }
  /**
   * Retrieves the TypeDecl list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the TypeDecl list.
   * @apilevel low-level
   */
  public List<TypeDecl> getTypeDeclsNoTransform() {
    return getTypeDeclListNoTransform();
  }
  /**
   * @apilevel internal
   */
  protected boolean typeWildcard_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeWildcard_value;
/**
 * @apilevel internal
 */
private void typeWildcard_reset() {
  typeWildcard_computed = false;
  typeWildcard_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeWildcard() {
    if(typeWildcard_computed) {
      return typeWildcard_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeWildcard_value = typeWildcard_compute();
    typeWildcard_value.setParent(this);
    typeWildcard_value.is$Final = true;
    if (true) {
      typeWildcard_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeWildcard_value;
  }
  /**
   * @apilevel internal
   */
  private TypeDecl typeWildcard_compute() {
      TypeDecl decl = new WildcardType(
        new Modifiers(new List().add(new Modifier("public"))),
        "?",
        new List()
      );
      return decl;
    }
  /**
   * @apilevel internal
   */
  protected java.util.Map lookupWildcardExtends_TypeDecl_values;
  /**
   * @apilevel internal
   */
  protected List lookupWildcardExtends_TypeDecl_list;
/**
 * @apilevel internal
 */
private void lookupWildcardExtends_TypeDecl_reset() {
  lookupWildcardExtends_TypeDecl_values = null;
  lookupWildcardExtends_TypeDecl_list = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl lookupWildcardExtends(TypeDecl bound) {
    Object _parameters = bound;
    if (lookupWildcardExtends_TypeDecl_values == null) lookupWildcardExtends_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(lookupWildcardExtends_TypeDecl_values.containsKey(_parameters)) {
      return (TypeDecl)lookupWildcardExtends_TypeDecl_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    TypeDecl lookupWildcardExtends_TypeDecl_value = lookupWildcardExtends_compute(bound);
    if(lookupWildcardExtends_TypeDecl_list == null) {
      lookupWildcardExtends_TypeDecl_list = new List();
      lookupWildcardExtends_TypeDecl_list.is$Final = true;
      lookupWildcardExtends_TypeDecl_list.setParent(this);
    }
    lookupWildcardExtends_TypeDecl_list.add(lookupWildcardExtends_TypeDecl_value);
    if(lookupWildcardExtends_TypeDecl_value != null) {
      lookupWildcardExtends_TypeDecl_value = (TypeDecl) lookupWildcardExtends_TypeDecl_list.getChild(lookupWildcardExtends_TypeDecl_list.numChildren-1);
      lookupWildcardExtends_TypeDecl_value.is$Final = true;
    }
    if (true) {
      lookupWildcardExtends_TypeDecl_values.put(_parameters, lookupWildcardExtends_TypeDecl_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return lookupWildcardExtends_TypeDecl_value;
  }
  /**
   * @apilevel internal
   */
  private TypeDecl lookupWildcardExtends_compute(TypeDecl bound) {
      TypeDecl decl = new WildcardExtendsType(
        new Modifiers(new List().add(new Modifier("public"))),
        "? extends " + bound.fullName(),
        new List(),
        bound.createBoundAccess()
      );
      return decl;
    }
  /**
   * @apilevel internal
   */
  protected java.util.Map lookupWildcardSuper_TypeDecl_values;
  /**
   * @apilevel internal
   */
  protected List lookupWildcardSuper_TypeDecl_list;
/**
 * @apilevel internal
 */
private void lookupWildcardSuper_TypeDecl_reset() {
  lookupWildcardSuper_TypeDecl_values = null;
  lookupWildcardSuper_TypeDecl_list = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl lookupWildcardSuper(TypeDecl bound) {
    Object _parameters = bound;
    if (lookupWildcardSuper_TypeDecl_values == null) lookupWildcardSuper_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(lookupWildcardSuper_TypeDecl_values.containsKey(_parameters)) {
      return (TypeDecl)lookupWildcardSuper_TypeDecl_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    TypeDecl lookupWildcardSuper_TypeDecl_value = lookupWildcardSuper_compute(bound);
    if(lookupWildcardSuper_TypeDecl_list == null) {
      lookupWildcardSuper_TypeDecl_list = new List();
      lookupWildcardSuper_TypeDecl_list.is$Final = true;
      lookupWildcardSuper_TypeDecl_list.setParent(this);
    }
    lookupWildcardSuper_TypeDecl_list.add(lookupWildcardSuper_TypeDecl_value);
    if(lookupWildcardSuper_TypeDecl_value != null) {
      lookupWildcardSuper_TypeDecl_value = (TypeDecl) lookupWildcardSuper_TypeDecl_list.getChild(lookupWildcardSuper_TypeDecl_list.numChildren-1);
      lookupWildcardSuper_TypeDecl_value.is$Final = true;
    }
    if (true) {
      lookupWildcardSuper_TypeDecl_values.put(_parameters, lookupWildcardSuper_TypeDecl_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return lookupWildcardSuper_TypeDecl_value;
  }
  /**
   * @apilevel internal
   */
  private TypeDecl lookupWildcardSuper_compute(TypeDecl bound) {
      TypeDecl decl = new WildcardSuperType(
        new Modifiers(new List().add(new Modifier("public"))),
        "? super " + bound.fullName(),
        new List(),
        bound.createBoundAccess()
      );
      return decl;
    }
  /**
   * @apilevel internal
   */
  protected java.util.Map lookupLUBType_Collection_values;
  /**
   * @apilevel internal
   */
  protected List lookupLUBType_Collection_list;
/**
 * @apilevel internal
 */
private void lookupLUBType_Collection_reset() {
  lookupLUBType_Collection_values = null;
  lookupLUBType_Collection_list = null;
}  
  @ASTNodeAnnotation.Attribute
  public LUBType lookupLUBType(Collection bounds) {
    Object _parameters = bounds;
    if (lookupLUBType_Collection_values == null) lookupLUBType_Collection_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(lookupLUBType_Collection_values.containsKey(_parameters)) {
      return (LUBType)lookupLUBType_Collection_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    LUBType lookupLUBType_Collection_value = createLUBType(bounds);
    if(lookupLUBType_Collection_list == null) {
      lookupLUBType_Collection_list = new List();
      lookupLUBType_Collection_list.is$Final = true;
      lookupLUBType_Collection_list.setParent(this);
    }
    lookupLUBType_Collection_list.add(lookupLUBType_Collection_value);
    if(lookupLUBType_Collection_value != null) {
      lookupLUBType_Collection_value = (LUBType) lookupLUBType_Collection_list.getChild(lookupLUBType_Collection_list.numChildren-1);
      lookupLUBType_Collection_value.is$Final = true;
    }
    if (true) {
      lookupLUBType_Collection_values.put(_parameters, lookupLUBType_Collection_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return lookupLUBType_Collection_value;
  }
  /**
   * @apilevel internal
   */
  protected java.util.Map lookupGLBType_ArrayList_values;
  /**
   * @apilevel internal
   */
  protected List lookupGLBType_ArrayList_list;
/**
 * @apilevel internal
 */
private void lookupGLBType_ArrayList_reset() {
  lookupGLBType_ArrayList_values = null;
  lookupGLBType_ArrayList_list = null;
}  
  @ASTNodeAnnotation.Attribute
  public GLBType lookupGLBType(ArrayList bounds) {
    Object _parameters = bounds;
    if (lookupGLBType_ArrayList_values == null) lookupGLBType_ArrayList_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(lookupGLBType_ArrayList_values.containsKey(_parameters)) {
      return (GLBType)lookupGLBType_ArrayList_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    GLBType lookupGLBType_ArrayList_value = lookupGLBType_compute(bounds);
    if(lookupGLBType_ArrayList_list == null) {
      lookupGLBType_ArrayList_list = new List();
      lookupGLBType_ArrayList_list.is$Final = true;
      lookupGLBType_ArrayList_list.setParent(this);
    }
    lookupGLBType_ArrayList_list.add(lookupGLBType_ArrayList_value);
    if(lookupGLBType_ArrayList_value != null) {
      lookupGLBType_ArrayList_value = (GLBType) lookupGLBType_ArrayList_list.getChild(lookupGLBType_ArrayList_list.numChildren-1);
      lookupGLBType_ArrayList_value.is$Final = true;
    }
    if (true) {
      lookupGLBType_ArrayList_values.put(_parameters, lookupGLBType_ArrayList_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return lookupGLBType_ArrayList_value;
  }
  /**
   * @apilevel internal
   */
  private GLBType lookupGLBType_compute(ArrayList bounds) {
      List boundList = new List();
      StringBuilder name = new StringBuilder();
      for(Iterator iter = bounds.iterator(); iter.hasNext(); ) {
        TypeDecl typeDecl = (TypeDecl)iter.next();
        boundList.add(typeDecl.createBoundAccess());
        name.append("& " + typeDecl.typeName());
      }
      GLBType decl = new GLBType(
        new Modifiers(new List().add(new Modifier("public"))),
        name.toString(),
        new List(),
        boundList
      );
      return decl;
    }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
