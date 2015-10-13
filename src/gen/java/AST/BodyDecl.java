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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:71
 * @production BodyDecl : {@link ASTNode};

 */
public abstract class BodyDecl extends ASTNode<ASTNode> implements Cloneable {
  /**
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:1034
   */
  public BodyDecl substitutedBodyDecl(Parameterization parTypeDecl) {
    throw new Error("Operation substitutedBodyDecl not supported for " + getClass().getName());
  }
  /**
   * We must report illegal uses of the SafeVarargs annotation.
   * It is only allowed on variable arity method and constructor declarations.
   * @aspect SafeVarargs
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/SafeVarargs.jrag:93
   */
  public void checkWarnings() {
    if (hasIllegalAnnotationSafeVarargs())
      error("@SafeVarargs is only allowed for variable " +
        "arity method and constructor declarations");
  }
  /**
   * @declaredat ASTNode:1
   */
  public BodyDecl() {
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
   * @apilevel low-level
   * @declaredat ASTNode:15
   */
  protected int numChildren() {
    return 0;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:21
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:27
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    isDAafter_Variable_reset();
    isDAbefore_Variable_reset();
    isDUafter_Variable_reset();
    isDUbefore_Variable_reset();
    typeThrowable_reset();
    lookupVariable_String_reset();
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
  public BodyDecl clone() throws CloneNotSupportedException {
    BodyDecl node = (BodyDecl) super.clone();
    return node;
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @deprecated Please use emitTreeCopy or emitTreeCopyNoTransform instead
   * @declaredat ASTNode:62
   */
  public abstract BodyDecl fullCopy();
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:69
   */
  public abstract BodyDecl treeCopyNoTransform();
  /**
   * Create a deep copy of the AST subtree at this node.
   * The subtree of this node is traversed to trigger rewrites before copy.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:77
   */
  public abstract BodyDecl treeCopy();
  @ASTNodeAnnotation.Attribute
  public boolean addsIndentationLevel() {
    ASTNode$State state = state();
    boolean addsIndentationLevel_value = true;

    return addsIndentationLevel_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean visibleTypeParameters() {
    ASTNode$State state = state();
    boolean visibleTypeParameters_value = true;

    return visibleTypeParameters_value;
  }
  protected java.util.Map isDAafter_Variable_values;
/**
 * @apilevel internal
 */
private void isDAafter_Variable_reset() {
  isDAafter_Variable_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isDAafter(Variable v) {
    Object _parameters = v;
    if (isDAafter_Variable_values == null) isDAafter_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(isDAafter_Variable_values.containsKey(_parameters)) {
      return ((Boolean)isDAafter_Variable_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean isDAafter_Variable_value = true;
    if (isFinal && num == state().boundariesCrossed) {
      isDAafter_Variable_values.put(_parameters, Boolean.valueOf(isDAafter_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDAafter_Variable_value;
  }
  protected java.util.Map isDAbefore_Variable_values;
/**
 * @apilevel internal
 */
private void isDAbefore_Variable_reset() {
  isDAbefore_Variable_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isDAbefore(Variable v) {
    Object _parameters = v;
    if (isDAbefore_Variable_values == null) isDAbefore_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(isDAbefore_Variable_values.containsKey(_parameters)) {
      return ((Boolean)isDAbefore_Variable_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean isDAbefore_Variable_value = isDAbefore(v, this);
    if (isFinal && num == state().boundariesCrossed) {
      isDAbefore_Variable_values.put(_parameters, Boolean.valueOf(isDAbefore_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDAbefore_Variable_value;
  }
  protected java.util.Map isDUafter_Variable_values;
/**
 * @apilevel internal
 */
private void isDUafter_Variable_reset() {
  isDUafter_Variable_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isDUafter(Variable v) {
    Object _parameters = v;
    if (isDUafter_Variable_values == null) isDUafter_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(isDUafter_Variable_values.containsKey(_parameters)) {
      return ((Boolean)isDUafter_Variable_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean isDUafter_Variable_value = true;
    if (isFinal && num == state().boundariesCrossed) {
      isDUafter_Variable_values.put(_parameters, Boolean.valueOf(isDUafter_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDUafter_Variable_value;
  }
  protected java.util.Map isDUbefore_Variable_values;
/**
 * @apilevel internal
 */
private void isDUbefore_Variable_reset() {
  isDUbefore_Variable_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isDUbefore(Variable v) {
    Object _parameters = v;
    if (isDUbefore_Variable_values == null) isDUbefore_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(isDUbefore_Variable_values.containsKey(_parameters)) {
      return ((Boolean)isDUbefore_Variable_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean isDUbefore_Variable_value = isDUbefore(v, this);
    if (isFinal && num == state().boundariesCrossed) {
      isDUbefore_Variable_values.put(_parameters, Boolean.valueOf(isDUbefore_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDUbefore_Variable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean declaresType(String name) {
    ASTNode$State state = state();
    boolean declaresType_String_value = false;

    return declaresType_String_value;
  }
  @ASTNodeAnnotation.Attribute
  public TypeDecl type(String name) {
    ASTNode$State state = state();
    TypeDecl type_String_value = null;

    return type_String_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isVoid() {
    ASTNode$State state = state();
    boolean isVoid_value = false;

    return isVoid_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean hasAnnotationSuppressWarnings(String annot) {
    ASTNode$State state = state();
    boolean hasAnnotationSuppressWarnings_String_value = false;

    return hasAnnotationSuppressWarnings_String_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isDeprecated() {
    ASTNode$State state = state();
    boolean isDeprecated_value = false;

    return isDeprecated_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isEnumConstant() {
    ASTNode$State state = state();
    boolean isEnumConstant_value = false;

    return isEnumConstant_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean hasAnnotationSafeVarargs() {
    ASTNode$State state = state();
    boolean hasAnnotationSafeVarargs_value = false;

    return hasAnnotationSafeVarargs_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean hasIllegalAnnotationSafeVarargs() {
    ASTNode$State state = state();
    boolean hasIllegalAnnotationSafeVarargs_value = hasAnnotationSafeVarargs();

    return hasIllegalAnnotationSafeVarargs_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean modifiedInScope(Variable var) {
    ASTNode$State state = state();
    boolean modifiedInScope_Variable_value = false;

    return modifiedInScope_Variable_value;
  }
  /**
   * @attribute inh
   * @aspect DA
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:252
   */
  @ASTNodeAnnotation.Attribute
  public boolean isDAbefore(Variable v, BodyDecl b) {
    ASTNode$State state = state();
    boolean isDAbefore_Variable_BodyDecl_value = getParent().Define_boolean_isDAbefore(this, null, v, b);

    return isDAbefore_Variable_BodyDecl_value;
  }
  /**
   * @attribute inh
   * @aspect DU
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:740
   */
  @ASTNodeAnnotation.Attribute
  public boolean isDUbefore(Variable v, BodyDecl b) {
    ASTNode$State state = state();
    boolean isDUbefore_Variable_BodyDecl_value = getParent().Define_boolean_isDUbefore(this, null, v, b);

    return isDUbefore_Variable_BodyDecl_value;
  }
  /**
   * @attribute inh
   * @aspect ExceptionHandling
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ExceptionHandling.jrag:28
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeThrowable() {
    if(typeThrowable_computed) {
      return typeThrowable_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeThrowable_value = getParent().Define_TypeDecl_typeThrowable(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      typeThrowable_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeThrowable_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeThrowable_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeThrowable_value;
/**
 * @apilevel internal
 */
private void typeThrowable_reset() {
  typeThrowable_computed = false;
  typeThrowable_value = null;
}  
  /**
   * @attribute inh
   * @aspect LookupMethod
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupMethod.jrag:45
   */
  @ASTNodeAnnotation.Attribute
  public Collection lookupMethod(String name) {
    ASTNode$State state = state();
    Collection lookupMethod_String_value = getParent().Define_Collection_lookupMethod(this, null, name);

    return lookupMethod_String_value;
  }
  /**
   * @attribute inh
   * @aspect LookupFullyQualifiedTypes
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:100
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl lookupType(String packageName, String typeName) {
    ASTNode$State state = state();
    TypeDecl lookupType_String_String_value = getParent().Define_TypeDecl_lookupType(this, null, packageName, typeName);

    return lookupType_String_String_value;
  }
  /**
   * @attribute inh
   * @aspect TypeScopePropagation
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:227
   */
  @ASTNodeAnnotation.Attribute
  public SimpleSet lookupType(String name) {
    ASTNode$State state = state();
    SimpleSet lookupType_String_value = getParent().Define_SimpleSet_lookupType(this, null, name);

    return lookupType_String_value;
  }
  /**
   * @attribute inh
   * @aspect VariableScope
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupVariable.jrag:15
   */
  @ASTNodeAnnotation.Attribute
  public SimpleSet lookupVariable(String name) {
    Object _parameters = name;
    if (lookupVariable_String_values == null) lookupVariable_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(lookupVariable_String_values.containsKey(_parameters)) {
      return (SimpleSet)lookupVariable_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    SimpleSet lookupVariable_String_value = getParent().Define_SimpleSet_lookupVariable(this, null, name);
    if (isFinal && num == state().boundariesCrossed) {
      lookupVariable_String_values.put(_parameters, lookupVariable_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return lookupVariable_String_value;
  }
  protected java.util.Map lookupVariable_String_values;
/**
 * @apilevel internal
 */
private void lookupVariable_String_reset() {
  lookupVariable_String_values = null;
}  
  /**
   * @attribute inh
   * @aspect SyntacticClassification
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/SyntacticClassification.jrag:21
   */
  @ASTNodeAnnotation.Attribute
  public NameType nameType() {
    ASTNode$State state = state();
    NameType nameType_value = getParent().Define_NameType_nameType(this, null);

    return nameType_value;
  }
  /**
   * @attribute inh
   * @aspect NestedTypes
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:577
   */
  @ASTNodeAnnotation.Attribute
  public String hostPackage() {
    ASTNode$State state = state();
    String hostPackage_value = getParent().Define_String_hostPackage(this, null);

    return hostPackage_value;
  }
  /**
   * @attribute inh
   * @aspect NestedTypes
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:593
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl hostType() {
    ASTNode$State state = state();
    TypeDecl hostType_value = getParent().Define_TypeDecl_hostType(this, null);

    return hostType_value;
  }
  /**
   * @attribute inh
   * @aspect Annotations
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:277
   */
  @ASTNodeAnnotation.Attribute
  public boolean withinSuppressWarnings(String annot) {
    ASTNode$State state = state();
    boolean withinSuppressWarnings_String_value = getParent().Define_boolean_withinSuppressWarnings(this, null, annot);

    return withinSuppressWarnings_String_value;
  }
  /**
   * @attribute inh
   * @aspect Annotations
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:382
   */
  @ASTNodeAnnotation.Attribute
  public boolean withinDeprecatedAnnotation() {
    ASTNode$State state = state();
    boolean withinDeprecatedAnnotation_value = getParent().Define_boolean_withinDeprecatedAnnotation(this, null);

    return withinDeprecatedAnnotation_value;
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:397
   * @apilevel internal
   */
  public String Define_String_typeDeclIndent(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return indent();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BranchTarget.jrag:208
   * @apilevel internal
   */
  public Stmt Define_Stmt_branchTarget(ASTNode caller, ASTNode child, Stmt branch) {
     {
      int childIndex = this.getIndexOfChild(caller);
{
    throw new Error("Found no branch targets for " +
        branch.getClass().getName());
  }
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BranchTarget.jrag:244
   * @apilevel internal
   */
  public FinallyHost Define_FinallyHost_enclosingFinally(ASTNode caller, ASTNode child, Stmt branch) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return null;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:246
   * @apilevel internal
   */
  public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return isDAbefore(v, this);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:734
   * @apilevel internal
   */
  public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return isDUbefore(v, this);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:473
   * @apilevel internal
   */
  public SimpleSet Define_SimpleSet_otherLocalClassDecls(ASTNode caller, ASTNode child, String name) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return SimpleSet.emptySet;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:525
   * @apilevel internal
   */
  public BodyDecl Define_BodyDecl_enclosingBodyDecl(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return this;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:280
   * @apilevel internal
   */
  public boolean Define_boolean_withinSuppressWarnings(ASTNode caller, ASTNode child, String annot) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return hasAnnotationSuppressWarnings(annot) || hasAnnotationSuppressWarnings(annot) ||
    withinSuppressWarnings(annot);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:384
   * @apilevel internal
   */
  public boolean Define_boolean_withinDeprecatedAnnotation(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return isDeprecated() || isDeprecated() || withinDeprecatedAnnotation();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/TryWithResources.jrag:153
   * @apilevel internal
   */
  public boolean Define_boolean_resourcePreviouslyDeclared(ASTNode caller, ASTNode child, String name) {
     {
      int i = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
