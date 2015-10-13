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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:28
 * @production PackageAccess : {@link Access} ::= <span class="component">&lt;Package:String&gt;</span>;

 */
public class PackageAccess extends Access implements Cloneable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:554
   */
  public void prettyPrint(StringBuffer sb) {
    sb.append(getPackage());
  }
  /**
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:73
   */
  public void nameCheck() {
    if (!hasPackage(packageName())) {
      error("package " + packageName() + " not found");
    }
  }
  /**
   * @aspect NodeConstructors
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NodeConstructors.jrag:18
   */
  public PackageAccess(String name, int start, int end) {
    this(name);
    this.start = this.Packagestart = start;
    this.end = this.Packageend = end;
  }
  /**
   * @declaredat ASTNode:1
   */
  public PackageAccess() {
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
  public PackageAccess(String p0) {
    setPackage(p0);
  }
  /**
   * @declaredat ASTNode:15
   */
  public PackageAccess(beaver.Symbol p0) {
    setPackage(p0);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:21
   */
  protected int numChildren() {
    return 0;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:27
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:33
   */
  public void flushAttrCache() {
    super.flushAttrCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:39
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:45
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:51
   */
  public PackageAccess clone() throws CloneNotSupportedException {
    PackageAccess node = (PackageAccess) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:58
   */
  public PackageAccess copy() {
    try {
      PackageAccess node = (PackageAccess) clone();
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
   * @declaredat ASTNode:77
   */
  public PackageAccess fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:86
   */
  public PackageAccess treeCopyNoTransform() {
    PackageAccess tree = (PackageAccess) copy();
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
   * @declaredat ASTNode:106
   */
  public PackageAccess treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:113
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_Package == ((PackageAccess)node).tokenString_Package);    
  }
  /**
   * Replaces the lexeme Package.
   * @param value The new value for the lexeme Package.
   * @apilevel high-level
   */
  public void setPackage(String value) {
    tokenString_Package = value;
  }
  /**
   * @apilevel internal
   */
  protected String tokenString_Package;
  /**
   */
  public int Packagestart;
  /**
   */
  public int Packageend;
  /**
   * JastAdd-internal setter for lexeme Package using the Beaver parser.
   * @param symbol Symbol containing the new value for the lexeme Package
   * @apilevel internal
   */
  public void setPackage(beaver.Symbol symbol) {
    if(symbol.value != null && !(symbol.value instanceof String))
    throw new UnsupportedOperationException("setPackage is only valid for String lexemes");
    tokenString_Package = (String)symbol.value;
    Packagestart = symbol.getStart();
    Packageend = symbol.getEnd();
  }
  /**
   * Retrieves the value for the lexeme Package.
   * @return The value for the lexeme Package.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="Package")
  public String getPackage() {
    return tokenString_Package != null ? tokenString_Package : "";
  }
  @ASTNodeAnnotation.Attribute
  public String dumpString() {
    ASTNode$State state = state();
    String dumpString_value = getClass().getName() + " [" + getPackage() + "]";

    return dumpString_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean hasQualifiedPackage(String packageName) {
    ASTNode$State state = state();
    boolean hasQualifiedPackage_String_value = hasPackage(packageName() + "." + packageName);

    return hasQualifiedPackage_String_value;
  }
  /**
   * @attribute syn
   * @aspect TypeScopePropagation
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:402
   */
  @ASTNodeAnnotation.Attribute
  public SimpleSet qualifiedLookupType(String name) {
    ASTNode$State state = state();
    try {
        SimpleSet c = SimpleSet.emptySet;
        TypeDecl typeDecl = lookupType(packageName(), name);
        if(nextAccess() instanceof ClassInstanceExpr) {
          if(typeDecl != null && typeDecl.accessibleFrom(hostType()))
            c = c.add(typeDecl);
          return c;
        }
        else {
          if(typeDecl != null) {
            if(hostType() != null && typeDecl.accessibleFrom(hostType()))
              c = c.add(typeDecl);
            else if(hostType() == null && typeDecl.accessibleFromPackage(hostPackage()))
              c = c.add(typeDecl);
          }
          return c;
        }
      }
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public SimpleSet qualifiedLookupVariable(String name) {
    ASTNode$State state = state();
    SimpleSet qualifiedLookupVariable_String_value = SimpleSet.emptySet;

    return qualifiedLookupVariable_String_value;
  }
  @ASTNodeAnnotation.Attribute
  public String name() {
    ASTNode$State state = state();
    String name_value = getPackage();

    return name_value;
  }
  /**
   * @attribute syn
   * @aspect Names
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/QualifiedNames.jrag:25
   */
  @ASTNodeAnnotation.Attribute
  public String packageName() {
    ASTNode$State state = state();
    try {
        StringBuilder sb = new StringBuilder();
        if (hasPrevExpr()) {
          sb.append(prevExpr().packageName());
          sb.append(".");
        }
        sb.append(getPackage());
        return sb.toString();
      }
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public boolean isPackageAccess() {
    ASTNode$State state = state();
    boolean isPackageAccess_value = true;

    return isPackageAccess_value;
  }
  @ASTNodeAnnotation.Attribute
  public NameType predNameType() {
    ASTNode$State state = state();
    NameType predNameType_value = NameType.PACKAGE_NAME;

    return predNameType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isUnknown() {
    ASTNode$State state = state();
    boolean isUnknown_value = !hasPackage(packageName());

    return isUnknown_value;
  }
  /**
   * @attribute inh
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:269
   */
  @ASTNodeAnnotation.Attribute
  public boolean hasPackage(String packageName) {
    ASTNode$State state = state();
    boolean hasPackage_String_value = getParent().Define_boolean_hasPackage(this, null, packageName);

    return hasPackage_String_value;
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
