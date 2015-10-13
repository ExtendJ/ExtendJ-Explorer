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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:99
 * @production MemberInterfaceDecl : {@link MemberTypeDecl} ::= <span class="component">{@link InterfaceDecl}</span>;

 */
public class MemberInterfaceDecl extends MemberTypeDecl implements Cloneable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:252
   */
  public void prettyPrint(StringBuffer sb) {
    sb.append(indent());
    getInterfaceDecl().prettyPrint(sb);
  }
  /**
   * @aspect Modifiers
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:186
   */
  public void checkModifiers() {
    super.checkModifiers();
    if(hostType().isInnerClass())
      error("*** Inner classes may not declare member interfaces");
  }
  /**
   * @declaredat ASTNode:1
   */
  public MemberInterfaceDecl() {
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
  public MemberInterfaceDecl(InterfaceDecl p0) {
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
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:37
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:43
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:49
   */
  public MemberInterfaceDecl clone() throws CloneNotSupportedException {
    MemberInterfaceDecl node = (MemberInterfaceDecl) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:56
   */
  public MemberInterfaceDecl copy() {
    try {
      MemberInterfaceDecl node = (MemberInterfaceDecl) clone();
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
   * @declaredat ASTNode:75
   */
  public MemberInterfaceDecl fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:84
   */
  public MemberInterfaceDecl treeCopyNoTransform() {
    MemberInterfaceDecl tree = (MemberInterfaceDecl) copy();
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
   * @declaredat ASTNode:104
   */
  public MemberInterfaceDecl treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:111
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) ;    
  }
  /**
   * Replaces the InterfaceDecl child.
   * @param node The new node to replace the InterfaceDecl child.
   * @apilevel high-level
   */
  public void setInterfaceDecl(InterfaceDecl node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the InterfaceDecl child.
   * @return The current node used as the InterfaceDecl child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="InterfaceDecl")
  public InterfaceDecl getInterfaceDecl() {
    return (InterfaceDecl) getChild(0);
  }
  /**
   * Retrieves the InterfaceDecl child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the InterfaceDecl child.
   * @apilevel low-level
   */
  public InterfaceDecl getInterfaceDeclNoTransform() {
    return (InterfaceDecl) getChildNoTransform(0);
  }
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeDecl() {
    ASTNode$State state = state();
    TypeDecl typeDecl_value = getInterfaceDecl();

    return typeDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean modifiedInScope(Variable var) {
    ASTNode$State state = state();
    boolean modifiedInScope_Variable_value = getInterfaceDecl().modifiedInScope(var);

    return modifiedInScope_Variable_value;
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:539
   * @apilevel internal
   */
  public boolean Define_boolean_isMemberType(ASTNode caller, ASTNode child) {
    if (caller == getInterfaceDeclNoTransform()) {
      return true;
    }
    else {
      return getParent().Define_boolean_isMemberType(this, caller);
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
