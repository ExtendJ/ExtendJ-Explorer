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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:22
 * @production TypeAccess : {@link Access} ::= <span class="component">&lt;Package:String&gt;</span> <span class="component">&lt;ID:String&gt;</span>;

 */
public class TypeAccess extends Access implements Cloneable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:527
   */
  public void prettyPrint(StringBuffer sb) {
    if(decl().isReferenceType())
      sb.append(nameWithPackage());
    else
      sb.append(decl().name());
  }
  /**
   * @aspect GenericsTypeAnalysis
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:312
   */
  public boolean isRaw() {
    /*
    if(hasNextAccess())
      return false;
    */
    ASTNode parent = getParent();
    while(parent instanceof AbstractDot)
      parent = parent.getParent();
    if(parent instanceof ParTypeAccess)
      return false;
    if(parent instanceof ImportDecl)
      return false;
    /*
    Access a = this;
    while(a.isTypeAccess() && hasNextAccess())
      a = a.nextAccess();
    if(a.isThisAccess() || a.isSuperAccess())
      return false;
    */
    return true;
  }
  /**
   * @aspect GenericsTypeCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:451
   */
  public void typeCheck() {
    TypeDecl type = type();
    if(type.isRawType() && type.isNestedType() && type.enclosingType().isParameterizedType() && !type.enclosingType().isRawType())
      error("Can not access a member type of a paramterized type as a raw type");
  }
  /**
   * @aspect AccessControl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/AccessControl.jrag:128
   */
  public void accessControl() {
    super.accessControl();
    TypeDecl hostType = hostType();
    if(hostType != null && !hostType.isUnknown() && !type().accessibleFrom(hostType)) {
      error("" + this + " in " + hostType().fullName() + " can not access type " + type().fullName());
    }
    else if((hostType == null || hostType.isUnknown())  && !type().accessibleFromPackage(hostPackage())) {
      error("" + this + " can not access type " + type().fullName());
    }
  }
  /**
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:185
   */
  public void nameCheck() {
    if(isQualified() && !qualifier().isTypeAccess() && !qualifier().isPackageAccess())
      error("can not access the type named " + decl().typeName() + " in this context");
    if(decls().isEmpty())
      error("no visible type named " + typeName());
    if (decls().size() > 1) {
      StringBuilder sb = new StringBuilder();
      sb.append("several types named " + name() + ":");
      for (Iterator iter = decls().iterator(); iter.hasNext(); ) {
        TypeDecl t = (TypeDecl)iter.next();
        sb.append(" " + t.typeName());
      }
      error(sb.toString());
    }
  }
  /**
   * @aspect NodeConstructors
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NodeConstructors.jrag:23
   */
  public TypeAccess(String name, int start, int end) {
    this(name);
    this.start = this.IDstart = start;
    this.end = this.IDend = end;
  }
  /**
   * @aspect NodeConstructors
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NodeConstructors.jrag:44
   */
  public TypeAccess(String typeName) {
    this("", typeName);
  }
  /**
   * @aspect Annotations
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:337
   */
  public void checkModifiers() {
    if(decl().isDeprecated() &&
       !withinDeprecatedAnnotation() &&
       (hostType() == null || hostType().topLevelType() != decl().topLevelType()) &&
       !withinSuppressWarnings("deprecation"))
      warning(decl().typeName() + " has been deprecated");
  }
  /**
   * @aspect FunctionalInterface
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/FunctionalInterface.jrag:200
   */
  public boolean sameType(TypeAccess t) {
		/*
		First, two type variables that are to be compared are checked to see if they are 
		both declared by methods and that one of the methods is not inside the scope of the other
		method. If this is the case it is enough to simply check that the positions are equal. 
		Otherwise the types has to equal. 
		*/
		
		if(type() instanceof TypeVariable && t.type() instanceof TypeVariable) {
			TypeVariable t1 = (TypeVariable)type();
			TypeVariable t2 = (TypeVariable)t.type();
			if(t1.typeVarInMethod() && t2.typeVarInMethod() && t1.genericMethodLevel() == t2.genericMethodLevel()) {
				return t1.typeVarPosition() == t2.typeVarPosition();
			}
			else {
				return t1 == t2;
			}
		}
		else if(type() instanceof TypeVariable && !(t.type() instanceof TypeVariable) ||
			t.type() instanceof TypeVariable && !(type() instanceof TypeVariable) ) {
			return false;
		}
		else if(type() == t.type())
			return true;
		else
			return false;
	}
  /**
   * @declaredat ASTNode:1
   */
  public TypeAccess() {
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
  public TypeAccess(String p0, String p1) {
    setPackage(p0);
    setID(p1);
  }
  /**
   * @declaredat ASTNode:16
   */
  public TypeAccess(beaver.Symbol p0, beaver.Symbol p1) {
    setPackage(p0);
    setID(p1);
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
    decls_reset();
    decl_reset();
    type_reset();
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
  public TypeAccess clone() throws CloneNotSupportedException {
    TypeAccess node = (TypeAccess) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:63
   */
  public TypeAccess copy() {
    try {
      TypeAccess node = (TypeAccess) clone();
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
  public TypeAccess fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:91
   */
  public TypeAccess treeCopyNoTransform() {
    TypeAccess tree = (TypeAccess) copy();
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
  public TypeAccess treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:118
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_Package == ((TypeAccess)node).tokenString_Package) && (tokenString_ID == ((TypeAccess)node).tokenString_ID);    
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
  /**
   * @aspect TypeScopePropagation
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:196
   */
  private TypeDecl refined_TypeScopePropagation_TypeAccess_decl()
{
    SimpleSet decls = decls();
    if(decls.size() == 1) {
      return (TypeDecl)decls.iterator().next();
    }
    return unknownType();
  }
  @ASTNodeAnnotation.Attribute
  public String dumpString() {
    ASTNode$State state = state();
    String dumpString_value = getClass().getName() + " [" + getPackage() + ", " + getID() + "]";

    return dumpString_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean usesTypeVariable() {
    ASTNode$State state = state();
    boolean usesTypeVariable_value = decl().usesTypeVariable() || super.usesTypeVariable();

    return usesTypeVariable_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean decls_computed = false;
  /**
   * @apilevel internal
   */
  protected SimpleSet decls_value;
/**
 * @apilevel internal
 */
private void decls_reset() {
  decls_computed = false;
  decls_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public SimpleSet decls() {
    if(decls_computed) {
      return decls_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    decls_value = decls_compute();
    if (isFinal && num == state().boundariesCrossed) {
      decls_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return decls_value;
  }
  /**
   * @apilevel internal
   */
  private SimpleSet decls_compute() {
      if(packageName().equals(""))
        return lookupType(name());
      else {
        TypeDecl typeDecl = lookupType(packageName(), name());
        if(typeDecl != null)
          return SimpleSet.emptySet.add(typeDecl);
        return SimpleSet.emptySet;
      }
    }
  /**
   * @apilevel internal
   */
  protected boolean decl_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl decl_value;
/**
 * @apilevel internal
 */
private void decl_reset() {
  decl_computed = false;
  decl_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl decl() {
    if(decl_computed) {
      return decl_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    decl_value = decl_compute();
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
  private TypeDecl decl_compute() {
      TypeDecl decl = refined_TypeScopePropagation_TypeAccess_decl();
      if(decl instanceof GenericTypeDecl && isRaw())
        return ((GenericTypeDecl)decl).lookupParTypeDecl(new ArrayList());
      return decl;
    }
  /**
   * @attribute syn
   * @aspect VariableScope
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupVariable.jrag:180
   */
  @ASTNodeAnnotation.Attribute
  public SimpleSet qualifiedLookupVariable(String name) {
    ASTNode$State state = state();
    try {
        if(type().accessibleFrom(hostType())) {
          SimpleSet c = type().memberFields(name);
          c = keepAccessibleFields(c);
          if(type().isClassDecl() && c.size() == 1)
            c = removeInstanceVariables(c);
          return c;
        }
        return SimpleSet.emptySet;
      }
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public String name() {
    ASTNode$State state = state();
    String name_value = getID();

    return name_value;
  }
  @ASTNodeAnnotation.Attribute
  public String packageName() {
    ASTNode$State state = state();
    String packageName_value = getPackage();

    return packageName_value;
  }
  @ASTNodeAnnotation.Attribute
  public String nameWithPackage() {
    ASTNode$State state = state();
    String nameWithPackage_value = getPackage().equals("") ? name() : (getPackage() + "." + name());

    return nameWithPackage_value;
  }
  @ASTNodeAnnotation.Attribute
  public String typeName() {
    ASTNode$State state = state();
    String typeName_value = isQualified() ? (qualifier().typeName() + "." + name()) : nameWithPackage();

    return typeName_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isTypeAccess() {
    ASTNode$State state = state();
    boolean isTypeAccess_value = true;

    return isTypeAccess_value;
  }
  @ASTNodeAnnotation.Attribute
  public NameType predNameType() {
    ASTNode$State state = state();
    NameType predNameType_value = NameType.PACKAGE_OR_TYPE_NAME;

    return predNameType_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean type_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl type_value;
/**
 * @apilevel internal
 */
private void type_reset() {
  type_computed = false;
  type_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl type() {
    if(type_computed) {
      return type_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    type_value = decl();
    if (isFinal && num == state().boundariesCrossed) {
      type_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return type_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean staticContextQualifier() {
    ASTNode$State state = state();
    boolean staticContextQualifier_value = true;

    return staticContextQualifier_value;
  }
  /**
   * Builds a copy of this Access node where all occurrences
   * of type variables in the original type parameter list have been replaced
   * by the substitution type parameters.
   * 
   * @return the substituted Access node
   * @attribute syn
   * @aspect Diamond
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Diamond.jrag:410
   */
  @ASTNodeAnnotation.Attribute
  public Access substituted(Collection<TypeVariable> original, List<TypeVariable> substitution) {
    ASTNode$State state = state();
    try {
        TypeDecl decl = decl();
        int i = 0;
        for (TypeVariable typeVar : original) {
          if (typeVar == decl)
            return new TypeAccess(substitution.getChild(i).getID());
          i += 1;
        }
        return super.substituted(original, substitution);
      }
    finally {
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
