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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/grammar/Annotations.ast:13
 * @production ElementArrayValue : {@link ElementValue} ::= <span class="component">{@link ElementValue}*</span>;

 */
public class ElementArrayValue extends ElementValue implements Cloneable {
  /**
   * @aspect AnnotationPrettyPrinting
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:633
   */
  public void prettyPrint(StringBuffer sb) {
    sb.append("{");
    for (int i = 0; i < getNumElementValue(); i++) {
      getElementValue(i).prettyPrint(sb);
      sb.append(", ");
    }
    sb.append("}");
  }
  /**
   * @declaredat ASTNode:1
   */
  public ElementArrayValue() {
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
    setChild(new List(), 0);
  }
  /**
   * @declaredat ASTNode:14
   */
  public ElementArrayValue(List<ElementValue> p0) {
    setChild(p0, 0);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:20
   */
  protected int numChildren() {
    return 1;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:26
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:32
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:38
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:44
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:50
   */
  public ElementArrayValue clone() throws CloneNotSupportedException {
    ElementArrayValue node = (ElementArrayValue) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:57
   */
  public ElementArrayValue copy() {
    try {
      ElementArrayValue node = (ElementArrayValue) clone();
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
   * @declaredat ASTNode:76
   */
  public ElementArrayValue fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:85
   */
  public ElementArrayValue treeCopyNoTransform() {
    ElementArrayValue tree = (ElementArrayValue) copy();
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
   * @declaredat ASTNode:105
   */
  public ElementArrayValue treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:112
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) ;    
  }
  /**
   * Replaces the ElementValue list.
   * @param list The new list node to be used as the ElementValue list.
   * @apilevel high-level
   */
  public void setElementValueList(List<ElementValue> list) {
    setChild(list, 0);
  }
  /**
   * Retrieves the number of children in the ElementValue list.
   * @return Number of children in the ElementValue list.
   * @apilevel high-level
   */
  public int getNumElementValue() {
    return getElementValueList().getNumChild();
  }
  /**
   * Retrieves the number of children in the ElementValue list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the ElementValue list.
   * @apilevel low-level
   */
  public int getNumElementValueNoTransform() {
    return getElementValueListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the ElementValue list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the ElementValue list.
   * @apilevel high-level
   */
  public ElementValue getElementValue(int i) {
    return (ElementValue) getElementValueList().getChild(i);
  }
  /**
   * Check whether the ElementValue list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasElementValue() {
    return getElementValueList().getNumChild() != 0;
  }
  /**
   * Append an element to the ElementValue list.
   * @param node The element to append to the ElementValue list.
   * @apilevel high-level
   */
  public void addElementValue(ElementValue node) {
    List<ElementValue> list = (parent == null || state == null) ? getElementValueListNoTransform() : getElementValueList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addElementValueNoTransform(ElementValue node) {
    List<ElementValue> list = getElementValueListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the ElementValue list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setElementValue(ElementValue node, int i) {
    List<ElementValue> list = getElementValueList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the ElementValue list.
   * @return The node representing the ElementValue list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="ElementValue")
  public List<ElementValue> getElementValueList() {
    List<ElementValue> list = (List<ElementValue>) getChild(0);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the ElementValue list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the ElementValue list.
   * @apilevel low-level
   */
  public List<ElementValue> getElementValueListNoTransform() {
    return (List<ElementValue>) getChildNoTransform(0);
  }
  /**
   * Retrieves the ElementValue list.
   * @return The node representing the ElementValue list.
   * @apilevel high-level
   */
  public List<ElementValue> getElementValues() {
    return getElementValueList();
  }
  /**
   * Retrieves the ElementValue list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the ElementValue list.
   * @apilevel low-level
   */
  public List<ElementValue> getElementValuesNoTransform() {
    return getElementValueListNoTransform();
  }
  /**
   * @attribute syn
   * @aspect Annotations
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:57
   */
  @ASTNodeAnnotation.Attribute
  public boolean validTarget(Annotation a) {
    ASTNode$State state = state();
    try {
        for(int i = 0;  i < getNumElementValue(); i++)
          if(getElementValue(i).validTarget(a))
            return true;
        return false;
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect Annotations
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:189
   */
  @ASTNodeAnnotation.Attribute
  public ElementValue definesElementTypeValue(String name) {
    ASTNode$State state = state();
    try {
        for(int i = 0; i < getNumElementValue(); i++)
          if(getElementValue(i).definesElementTypeValue(name) != null)
            return getElementValue(i).definesElementTypeValue(name);
        return null;
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect Annotations
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:304
   */
  @ASTNodeAnnotation.Attribute
  public boolean hasValue(String annot) {
    ASTNode$State state = state();
    try {
        for(int i = 0;  i < getNumElementValue(); i++)
          if(getElementValue(i).hasValue(annot))
            return true;
        return false;
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect Annotations
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:505
   */
  @ASTNodeAnnotation.Attribute
  public boolean commensurateWithArrayDecl(ArrayDecl type) {
    ASTNode$State state = state();
    try {
        for(int i = 0; i < getNumElementValue(); i++)
          if(!type.componentType().commensurateWith(getElementValue(i)))
            return false;
        return true;
      }
    finally {
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:186
   * @apilevel internal
   */
  public ElementValue Define_ElementValue_lookupElementTypeValue(ASTNode caller, ASTNode child, String name) {
    if (caller == getElementValueListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return definesElementTypeValue(name);
    }
    else {
      return getParent().Define_ElementValue_lookupElementTypeValue(this, caller, name);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:524
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_declType(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return declType().elementType();
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
