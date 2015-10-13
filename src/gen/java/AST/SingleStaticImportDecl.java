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
 * 7.5.3 A single-static-import declaration imports all accessible (\u00a76.6) static members
 * with a given simple name from a type. This makes these static members available
 * under their simple name in the class and interface declarations of the
 * compilation unit in which the single-static import declaration appears.
 * @ast node
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/grammar/StaticImports.ast:12
 * @production SingleStaticImportDecl : {@link StaticImportDecl} ::= <span class="component">&lt;ID:String&gt;</span>;

 */
public class SingleStaticImportDecl extends StaticImportDecl implements Cloneable {
  /**
   * @aspect StaticImports
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/StaticImports.jrag:75
   */
  public void typeCheck() {
    if(!getAccess().type().typeName().equals(typeName()) && !getAccess().type().isUnknown())
      error("Single-type import " + typeName() + " is not the canonical name of type " + getAccess().type().typeName());
  }
  /**
   * @aspect StaticImports
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/StaticImports.jrag:107
   */
  public void nameCheck() {
    if(importedFields(name()).isEmpty() && importedMethods(name()).isEmpty() && importedTypes(name()).isEmpty() &&
       !getAccess().type().isUnknown()) {
      error("Semantic Error: At least one static member named " + name() + " must be available in static imported type " + type().fullName());
    }
  }
  /**
   * @aspect StaticImports
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/StaticImports.jrag:221
   */
  public void prettyPrint(StringBuffer sb) {
    sb.append("import static ");
    getAccess().prettyPrint(sb);
    sb.append("." + getID());
    sb.append(";\n");
  }
  /**
   * @declaredat ASTNode:1
   */
  public SingleStaticImportDecl() {
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
  public SingleStaticImportDecl(Access p0, String p1) {
    setChild(p0, 0);
    setID(p1);
  }
  /**
   * @declaredat ASTNode:17
   */
  public SingleStaticImportDecl(Access p0, beaver.Symbol p1) {
    setChild(p0, 0);
    setID(p1);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:24
   */
  protected int numChildren() {
    return 1;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:30
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:36
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    importedFields_String_reset();
    importedMethods_String_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:44
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:50
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:56
   */
  public SingleStaticImportDecl clone() throws CloneNotSupportedException {
    SingleStaticImportDecl node = (SingleStaticImportDecl) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:63
   */
  public SingleStaticImportDecl copy() {
    try {
      SingleStaticImportDecl node = (SingleStaticImportDecl) clone();
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
   * @declaredat ASTNode:82
   */
  public SingleStaticImportDecl fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:91
   */
  public SingleStaticImportDecl treeCopyNoTransform() {
    SingleStaticImportDecl tree = (SingleStaticImportDecl) copy();
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
   * @declaredat ASTNode:111
   */
  public SingleStaticImportDecl treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:118
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_ID == ((SingleStaticImportDecl)node).tokenString_ID);    
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
  protected java.util.Map importedFields_String_values;
/**
 * @apilevel internal
 */
private void importedFields_String_reset() {
  importedFields_String_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public SimpleSet importedFields(String name) {
    Object _parameters = name;
    if (importedFields_String_values == null) importedFields_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(importedFields_String_values.containsKey(_parameters)) {
      return (SimpleSet)importedFields_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    SimpleSet importedFields_String_value = importedFields_compute(name);
    if (isFinal && num == state().boundariesCrossed) {
      importedFields_String_values.put(_parameters, importedFields_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return importedFields_String_value;
  }
  /**
   * @apilevel internal
   */
  private SimpleSet importedFields_compute(String name) {
      if (name.equals(getID())) {
        return super.importedFields(name);
      } else {
        return SimpleSet.emptySet;
      }
    }
  protected java.util.Map importedMethods_String_values;
/**
 * @apilevel internal
 */
private void importedMethods_String_reset() {
  importedMethods_String_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public Collection importedMethods(String name) {
    Object _parameters = name;
    if (importedMethods_String_values == null) importedMethods_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(importedMethods_String_values.containsKey(_parameters)) {
      return (Collection)importedMethods_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    Collection importedMethods_String_value = importedMethods_compute(name);
    if (isFinal && num == state().boundariesCrossed) {
      importedMethods_String_values.put(_parameters, importedMethods_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return importedMethods_String_value;
  }
  /**
   * @apilevel internal
   */
  private Collection importedMethods_compute(String name) {
      if (name.equals(getID())) {
        return super.importedMethods(name);
      } else {
        return Collections.EMPTY_LIST;
      }
    }
  @ASTNodeAnnotation.Attribute
  public TypeDecl type() {
    ASTNode$State state = state();
    TypeDecl type_value = getAccess().type();

    return type_value;
  }
  @ASTNodeAnnotation.Attribute
  public String name() {
    ASTNode$State state = state();
    String name_value = getID();

    return name_value;
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/StaticImports.jrag:217
   * @apilevel internal
   */
  public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
    if (caller == getAccessNoTransform()) {
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
