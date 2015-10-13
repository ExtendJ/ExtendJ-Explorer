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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:202
 * @production ConstCase : {@link Case} ::= <span class="component">Value:{@link Expr}</span>;

 */
public class ConstCase extends Case implements Cloneable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:606
   */
  public void prettyPrint(StringBuffer sb) {
    sb.append(indent());
    sb.append("case ");
    getValue().prettyPrint(sb);
    sb.append(":");
  }
  /**
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:433
   */
  public void nameCheck() {
    if (getValue().isConstant() && bind(this) != this) {
      error("constant expression " + getValue().prettyPrint() +
          " is multiply declared in two case statements");
    }
  }
  /**
   * @declaredat ASTNode:1
   */
  public ConstCase() {
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
  public ConstCase(Expr p0) {
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
  public ConstCase clone() throws CloneNotSupportedException {
    ConstCase node = (ConstCase) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:56
   */
  public ConstCase copy() {
    try {
      ConstCase node = (ConstCase) clone();
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
  public ConstCase fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:84
   */
  public ConstCase treeCopyNoTransform() {
    ConstCase tree = (ConstCase) copy();
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
  public ConstCase treeCopy() {
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
   * Replaces the Value child.
   * @param node The new node to replace the Value child.
   * @apilevel high-level
   */
  public void setValue(Expr node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the Value child.
   * @return The current node used as the Value child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Value")
  public Expr getValue() {
    return (Expr) getChild(0);
  }
  /**
   * Retrieves the Value child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Value child.
   * @apilevel low-level
   */
  public Expr getValueNoTransform() {
    return (Expr) getChildNoTransform(0);
  }
  /**
   * @aspect Enums
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Enums.jrag:506
   */
    public void refined_Enums_ConstCase_typeCheck() {
    boolean isEnumConstant = getValue().isEnumConstant();
    if(switchType().isEnumDecl() && !isEnumConstant) {
      error("Unqualified enumeration constant required");
    } else {
      TypeDecl switchType = switchType();
      TypeDecl type = getValue().type();
      if(!type.assignConversionTo(switchType, getValue()))
        error("Constant expression must be assignable to Expression");
      if(!getValue().isConstant() && !getValue().type().isUnknown() &&
          !isEnumConstant)
        error("Switch expression must be constant");
    }
  }
  /**
   * <p>Improve the type checking error messages given for case labels.
   * @aspect StringsInSwitch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/StringsInSwitch.jrag:68
   */
    public void typeCheck() {
    boolean isEnumConstant = getValue().isEnumConstant();
    TypeDecl switchType = switchType();
    TypeDecl type = getValue().type();
    if (switchType.isEnumDecl() && !isEnumConstant)
      error("Unqualified enumeration constant required");
    if (!type.assignConversionTo(switchType, getValue()))
      error("Case label has incompatible type "+switchType.name()+
          ", expected type compatible with "+type.name());
    if (!getValue().isConstant() && !getValue().type().isUnknown() &&
        !isEnumConstant)
      error("Case label must have constant expression");
  }
  /**
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:461
   */
  private boolean refined_NameCheck_ConstCase_constValue_Case(Case c)
{
    if(!(c instanceof ConstCase) || !getValue().isConstant())
      return false;
    if(!getValue().type().assignableToInt() || !((ConstCase)c).getValue().type().assignableToInt())
      return false;
    return getValue().constant().intValue() == ((ConstCase)c).getValue().constant().intValue();
  }
  /**
   * @aspect Enums
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Enums.jrag:520
   */
  private boolean refined_Enums_ConstCase_constValue_Case(Case c)
{
    if(switchType().isEnumDecl()) {
      if(!(c instanceof ConstCase) || !getValue().isConstant())
        return false;
      return getValue().varDecl() == ((ConstCase)c).getValue().varDecl();
    }
    else
      return refined_NameCheck_ConstCase_constValue_Case(c);
  }
  /**
   * @attribute syn
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:460
   */
  @ASTNodeAnnotation.Attribute
  public boolean constValue(Case c) {
    ASTNode$State state = state();
    try {
        if (isDefaultCase() || c.isDefaultCase())
          return isDefaultCase() && c.isDefaultCase();
    
        Expr myValue = getValue();
        Expr otherValue = ((ConstCase) c).getValue();
        TypeDecl myType = myValue.type();
        TypeDecl otherType = otherValue.type();
        if (myType.isString() || otherType.isString()) {
          if (!myType.isString() || !otherType.isString())
            return false;
          if (!myValue.isConstant() || !otherValue.isConstant())
            return false;
          return myValue.constant().stringValue().equals(
              otherValue.constant().stringValue());
        }
    
        return refined_Enums_ConstCase_constValue_Case(c);
      }
    finally {
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Enums.jrag:500
   * @apilevel internal
   */
  public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
    if (caller == getValueNoTransform()) {
      return switchType().isEnumDecl() ? switchType().memberFields(name) : lookupVariable(name);
    }
    else {
      return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
