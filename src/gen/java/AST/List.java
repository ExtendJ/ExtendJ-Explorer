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
 * @production List : {@link ASTNode};

 */
public class List<T extends ASTNode> extends ASTNode<T> implements Cloneable {
  /**
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:848
   */
  public List substitute(Parameterization parTypeDecl) {
    List list = new List();
    for(int i = 0; i < getNumChild(); i++) {
      ASTNode node = getChild(i);
      if(node instanceof Access) {
        Access a = (Access)node;
        list.add(a.type().substitute(parTypeDecl));
      }
      else if(node instanceof VariableArityParameterDeclaration) {
        VariableArityParameterDeclaration p = (VariableArityParameterDeclaration)node;
        list.add(
          new VariableArityParameterDeclarationSubstituted(
            (Modifiers)p.getModifiers().fullCopy(),
            // use the type acces since VariableArity adds to the dimension
            p.getTypeAccess().type().substituteParameterType(parTypeDecl),
            p.getID(),
            p
          )
        );
      }
      else if(node instanceof ParameterDeclaration) {
        ParameterDeclaration p = (ParameterDeclaration)node;
        list.add(
          new ParameterDeclarationSubstituted(
            (Modifiers)p.getModifiers().fullCopy(),
            p.type().substituteParameterType(parTypeDecl),
            p.getID(),
            p
          )
        );
      }
      else {
        throw new Error("Can only substitute lists of access nodes but node number " + i + " is of type " + node.getClass().getName());
      }
    }
    return list;
  }
  /**
   * @declaredat ASTNode:1
   */
  public List() {
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
  public List(T... initialChildren) {
    children = new ASTNode[initialChildren.length];
    for (int i = 0; i < children.length; ++i) {
      addChild(initialChildren[i]);
    }
  }
  /**
   * @declaredat ASTNode:19
   */
  private boolean list$touched = true;
  /**
   * @declaredat ASTNode:21
   */
  public List<T> add(T node) {
    addChild(node);
    return this;
  }
  /**
   * @declaredat ASTNode:26
   */
  public List<T> addAll(java.util.Collection<? extends T> c) {
    for (T node : c) {
      addChild(node);
    }
    return this;
  }
  /**
   * @declaredat ASTNode:33
   */
  public void insertChild(ASTNode node, int i) {

    list$touched = true;

    super.insertChild(node, i);
  }
  /**
   * @declaredat ASTNode:40
   */
  public void addChild(T node) {

    list$touched = true;

    super.addChild(node);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:50
   */
  public void removeChild(int i) {

    list$touched = true;

    super.removeChild(i);
  }
  /**
   * @declaredat ASTNode:57
   */
  public int getNumChild() {

    if (list$touched) {
      for (int i = 0; i < getNumChildNoTransform(); i++) {
        getChild(i);
      }
      list$touched = false;
    }

    return getNumChildNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:71
   */
  public boolean mayHaveRewrite() {
    return true;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:77
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:83
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:89
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:95
   */
  public List<T> clone() throws CloneNotSupportedException {
    List node = (List) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:102
   */
  public List<T> copy() {
    try {
      List node = (List) clone();
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
   * @declaredat ASTNode:121
   */
  public List<T> fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:130
   */
  public List<T> treeCopyNoTransform() {
    List tree = (List) copy();
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
   * @declaredat ASTNode:150
   */
  public List<T> treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:157
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) ;    
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    if(list$touched) {
      for(int i = 0 ; i < getNumChildNoTransform(); i++) {
        getChild(i);
      }
      list$touched = false;
      return this;
    }    return super.rewriteTo();
  }}
