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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/BoundNames.ast:6
 * @production BoundFieldAccess : {@link VarAccess} ::= <span class="component">&lt;FieldDeclaration:FieldDeclaration&gt;</span>;

 */
public class BoundFieldAccess extends VarAccess implements Cloneable {
  /**
   * @aspect BoundNames
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BoundNames.jrag:68
   */
  public BoundFieldAccess(FieldDeclaration f) {
    this(f.name(), f);
  }
  /**
   * @aspect BoundNames
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BoundNames.jrag:73
   */
  public boolean isExactVarAccess() {
    return false;
  }
  /**
   * @declaredat ASTNode:1
   */
  public BoundFieldAccess() {
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
  }
  /**
   * @declaredat ASTNode:12
   */
  public BoundFieldAccess(String p0, FieldDeclaration p1) {
    setID(p0);
    setFieldDeclaration(p1);
  }
  /**
   * @declaredat ASTNode:16
   */
  public BoundFieldAccess(beaver.Symbol p0, FieldDeclaration p1) {
    setID(p0);
    setFieldDeclaration(p1);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:23
   */
  protected int numChildren() {
    return 0;
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
    decl_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:42
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:48
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:54
   */
  public BoundFieldAccess clone() throws CloneNotSupportedException {
    BoundFieldAccess node = (BoundFieldAccess) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:61
   */
  public BoundFieldAccess copy() {
    try {
      BoundFieldAccess node = (BoundFieldAccess) clone();
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
   * @declaredat ASTNode:80
   */
  public BoundFieldAccess fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:89
   */
  public BoundFieldAccess treeCopyNoTransform() {
    BoundFieldAccess tree = (BoundFieldAccess) copy();
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
   * @declaredat ASTNode:109
   */
  public BoundFieldAccess treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:116
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_ID == ((BoundFieldAccess)node).tokenString_ID) && (tokenFieldDeclaration_FieldDeclaration == ((BoundFieldAccess)node).tokenFieldDeclaration_FieldDeclaration);    
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
   * Replaces the lexeme FieldDeclaration.
   * @param value The new value for the lexeme FieldDeclaration.
   * @apilevel high-level
   */
  public void setFieldDeclaration(FieldDeclaration value) {
    tokenFieldDeclaration_FieldDeclaration = value;
  }
  /**
   * @apilevel internal
   */
  protected FieldDeclaration tokenFieldDeclaration_FieldDeclaration;
  /**
   * Retrieves the value for the lexeme FieldDeclaration.
   * @return The value for the lexeme FieldDeclaration.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="FieldDeclaration")
  public FieldDeclaration getFieldDeclaration() {
    return tokenFieldDeclaration_FieldDeclaration;
  }
  /**
   * @apilevel internal
   */
  protected boolean decl_computed = false;
  /**
   * @apilevel internal
   */
  protected Variable decl_value;
/**
 * @apilevel internal
 */
private void decl_reset() {
  decl_computed = false;
  decl_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Variable decl() {
    if(decl_computed) {
      return decl_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    decl_value = getFieldDeclaration();
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
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
