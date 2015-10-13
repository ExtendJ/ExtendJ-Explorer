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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:188
 * @production Modifiers : {@link ASTNode} ::= <span class="component">{@link Modifier}*</span>;

 */
public class Modifiers extends ASTNode<ASTNode> implements Cloneable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:479
   */
  public void prettyPrint(StringBuffer sb) {
    for(int i = 0; i < getNumModifier(); i++) {
      getModifier(i).prettyPrint(sb);
      sb.append(" ");
    }
  }
  /**
   * @aspect Modifiers
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:314
   */
  public void refined_Modifiers_Modifiers_checkModifiers() {
    super.checkModifiers();
    if(numProtectionModifiers() > 1)
      error("only one public, protected, private allowed");
    if(numModifier("static") > 1)
      error("only one static allowed");
    // 8.4.3.1
    // 8.4.3.2
    // 8.1.1.2
    if(numCompletenessModifiers() > 1)
      error("only one of final, abstract, volatile allowed");
    if(numModifier("synchronized") > 1)
      error("only one synchronized allowed");
    if(numModifier("transient") > 1)
      error("only one transient allowed");
    if(numModifier("native") > 1)
      error("only one native allowed");
    if(numModifier("strictfp") > 1)
      error("only one strictfp allowed");

    if(isPublic() && !mayBePublic())
      error("modifier public not allowed in this context");
    if(isPrivate() && !mayBePrivate())
      error("modifier private not allowed in this context");
    if(isProtected() && !mayBeProtected())
      error("modifier protected not allowed in this context");
    if(isStatic() && !mayBeStatic())
      error("modifier static not allowed in this context");
    if(isFinal() && !mayBeFinal())
      error("modifier final not allowed in this context");
    if(isAbstract() && !mayBeAbstract())
      error("modifier abstract not allowed in this context");
    if(isVolatile() && !mayBeVolatile())
      error("modifier volatile not allowed in this context");
    if(isTransient() && !mayBeTransient())
      error("modifier transient not allowed in this context");
    if(isStrictfp() && !mayBeStrictfp())
      error("modifier strictfp not allowed in this context");
    if(isSynchronized() && !mayBeSynchronized())
      error("modifier synchronized not allowed in this context");
    if(isNative() && !mayBeNative())
      error("modifier native not allowed in this context");
  }
  /**
   * @declaredat ASTNode:1
   */
  public Modifiers() {
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
  public Modifiers(List<Modifier> p0) {
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
    isPublic_reset();
    isPrivate_reset();
    isProtected_reset();
    isStatic_reset();
    isFinal_reset();
    isAbstract_reset();
    isVolatile_reset();
    isTransient_reset();
    isStrictfp_reset();
    isSynchronized_reset();
    isNative_reset();
    isSynthetic_reset();
    numModifier_String_reset();
    isDefault_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:52
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:58
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:64
   */
  public Modifiers clone() throws CloneNotSupportedException {
    Modifiers node = (Modifiers) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:71
   */
  public Modifiers copy() {
    try {
      Modifiers node = (Modifiers) clone();
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
   * @declaredat ASTNode:90
   */
  public Modifiers fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:99
   */
  public Modifiers treeCopyNoTransform() {
    Modifiers tree = (Modifiers) copy();
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
   * @declaredat ASTNode:119
   */
  public Modifiers treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:126
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) ;    
  }
  /**
   * Replaces the Modifier list.
   * @param list The new list node to be used as the Modifier list.
   * @apilevel high-level
   */
  public void setModifierList(List<Modifier> list) {
    setChild(list, 0);
  }
  /**
   * Retrieves the number of children in the Modifier list.
   * @return Number of children in the Modifier list.
   * @apilevel high-level
   */
  public int getNumModifier() {
    return getModifierList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Modifier list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Modifier list.
   * @apilevel low-level
   */
  public int getNumModifierNoTransform() {
    return getModifierListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Modifier list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Modifier list.
   * @apilevel high-level
   */
  public Modifier getModifier(int i) {
    return (Modifier) getModifierList().getChild(i);
  }
  /**
   * Check whether the Modifier list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasModifier() {
    return getModifierList().getNumChild() != 0;
  }
  /**
   * Append an element to the Modifier list.
   * @param node The element to append to the Modifier list.
   * @apilevel high-level
   */
  public void addModifier(Modifier node) {
    List<Modifier> list = (parent == null || state == null) ? getModifierListNoTransform() : getModifierList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addModifierNoTransform(Modifier node) {
    List<Modifier> list = getModifierListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Modifier list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setModifier(Modifier node, int i) {
    List<Modifier> list = getModifierList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Modifier list.
   * @return The node representing the Modifier list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Modifier")
  public List<Modifier> getModifierList() {
    List<Modifier> list = (List<Modifier>) getChild(0);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the Modifier list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Modifier list.
   * @apilevel low-level
   */
  public List<Modifier> getModifierListNoTransform() {
    return (List<Modifier>) getChildNoTransform(0);
  }
  /**
   * Retrieves the Modifier list.
   * @return The node representing the Modifier list.
   * @apilevel high-level
   */
  public List<Modifier> getModifiers() {
    return getModifierList();
  }
  /**
   * Retrieves the Modifier list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Modifier list.
   * @apilevel low-level
   */
  public List<Modifier> getModifiersNoTransform() {
    return getModifierListNoTransform();
  }
  /**
   * @aspect Modifiers
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/Modifiers.jrag:110
   */
   
  public void checkModifiers() {
    refined_Modifiers_Modifiers_checkModifiers();
    if (numModifier("default") > 1) {
      error("only one default allowed");
    }
  }
  /**
   * @apilevel internal
   */
  protected boolean isPublic_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isPublic_value;
/**
 * @apilevel internal
 */
private void isPublic_reset() {
  isPublic_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isPublic() {
    if(isPublic_computed) {
      return isPublic_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isPublic_value = numModifier("public") != 0;
    if (isFinal && num == state().boundariesCrossed) {
      isPublic_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isPublic_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isPrivate_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isPrivate_value;
/**
 * @apilevel internal
 */
private void isPrivate_reset() {
  isPrivate_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isPrivate() {
    if(isPrivate_computed) {
      return isPrivate_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isPrivate_value = numModifier("private") != 0;
    if (isFinal && num == state().boundariesCrossed) {
      isPrivate_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isPrivate_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isProtected_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isProtected_value;
/**
 * @apilevel internal
 */
private void isProtected_reset() {
  isProtected_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isProtected() {
    if(isProtected_computed) {
      return isProtected_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isProtected_value = numModifier("protected") != 0;
    if (isFinal && num == state().boundariesCrossed) {
      isProtected_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isProtected_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isStatic_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isStatic_value;
/**
 * @apilevel internal
 */
private void isStatic_reset() {
  isStatic_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isStatic() {
    if(isStatic_computed) {
      return isStatic_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isStatic_value = numModifier("static") != 0;
    if (isFinal && num == state().boundariesCrossed) {
      isStatic_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isStatic_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isFinal_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isFinal_value;
/**
 * @apilevel internal
 */
private void isFinal_reset() {
  isFinal_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isFinal() {
    if(isFinal_computed) {
      return isFinal_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isFinal_value = numModifier("final") != 0;
    if (isFinal && num == state().boundariesCrossed) {
      isFinal_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isFinal_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isAbstract_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isAbstract_value;
/**
 * @apilevel internal
 */
private void isAbstract_reset() {
  isAbstract_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isAbstract() {
    if(isAbstract_computed) {
      return isAbstract_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isAbstract_value = numModifier("abstract") != 0;
    if (isFinal && num == state().boundariesCrossed) {
      isAbstract_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isAbstract_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isVolatile_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isVolatile_value;
/**
 * @apilevel internal
 */
private void isVolatile_reset() {
  isVolatile_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isVolatile() {
    if(isVolatile_computed) {
      return isVolatile_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isVolatile_value = numModifier("volatile") != 0;
    if (isFinal && num == state().boundariesCrossed) {
      isVolatile_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isVolatile_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isTransient_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isTransient_value;
/**
 * @apilevel internal
 */
private void isTransient_reset() {
  isTransient_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isTransient() {
    if(isTransient_computed) {
      return isTransient_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isTransient_value = numModifier("transient") != 0;
    if (isFinal && num == state().boundariesCrossed) {
      isTransient_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isTransient_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isStrictfp_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isStrictfp_value;
/**
 * @apilevel internal
 */
private void isStrictfp_reset() {
  isStrictfp_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isStrictfp() {
    if(isStrictfp_computed) {
      return isStrictfp_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isStrictfp_value = numModifier("strictfp") != 0;
    if (isFinal && num == state().boundariesCrossed) {
      isStrictfp_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isStrictfp_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isSynchronized_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isSynchronized_value;
/**
 * @apilevel internal
 */
private void isSynchronized_reset() {
  isSynchronized_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isSynchronized() {
    if(isSynchronized_computed) {
      return isSynchronized_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isSynchronized_value = numModifier("synchronized") != 0;
    if (isFinal && num == state().boundariesCrossed) {
      isSynchronized_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isSynchronized_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isNative_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isNative_value;
/**
 * @apilevel internal
 */
private void isNative_reset() {
  isNative_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isNative() {
    if(isNative_computed) {
      return isNative_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isNative_value = numModifier("native") != 0;
    if (isFinal && num == state().boundariesCrossed) {
      isNative_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isNative_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isSynthetic_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isSynthetic_value;
/**
 * @apilevel internal
 */
private void isSynthetic_reset() {
  isSynthetic_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isSynthetic() {
    if(isSynthetic_computed) {
      return isSynthetic_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isSynthetic_value = numModifier("synthetic") != 0;
    if (isFinal && num == state().boundariesCrossed) {
      isSynthetic_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isSynthetic_value;
  }
  @ASTNodeAnnotation.Attribute
  public int numProtectionModifiers() {
    ASTNode$State state = state();
    int numProtectionModifiers_value = numModifier("public") + numModifier("protected") + numModifier("private");

    return numProtectionModifiers_value;
  }
  @ASTNodeAnnotation.Attribute
  public int numCompletenessModifiers() {
    ASTNode$State state = state();
    int numCompletenessModifiers_value = numModifier("abstract") + numModifier("final") + numModifier("volatile");

    return numCompletenessModifiers_value;
  }
  protected java.util.Map numModifier_String_values;
/**
 * @apilevel internal
 */
private void numModifier_String_reset() {
  numModifier_String_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public int numModifier(String name) {
    Object _parameters = name;
    if (numModifier_String_values == null) numModifier_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(numModifier_String_values.containsKey(_parameters)) {
      return ((Integer)numModifier_String_values.get(_parameters)).intValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    int numModifier_String_value = numModifier_compute(name);
    if (isFinal && num == state().boundariesCrossed) {
      numModifier_String_values.put(_parameters, Integer.valueOf(numModifier_String_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return numModifier_String_value;
  }
  /**
   * @apilevel internal
   */
  private int numModifier_compute(String name) {
      int n = 0;
      for(int i = 0; i < getNumModifier(); i++) {
        String s = getModifier(i).getID();
        if(s.equals(name))
          n++;
      }
      return n;
    }
  /**
   * @attribute syn
   * @aspect Annotations
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:222
   */
  @ASTNodeAnnotation.Attribute
  public Annotation annotation(TypeDecl typeDecl) {
    ASTNode$State state = state();
    try {
        for(int i = 0; i < getNumModifier(); i++) {
          if(getModifier(i) instanceof Annotation) {
            Annotation a = (Annotation)getModifier(i);
            if(a.type() == typeDecl)
              return a;
          }
        }
        return null;
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect Annotations
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:298
   */
  @ASTNodeAnnotation.Attribute
  public boolean hasAnnotationSuppressWarnings(String annot) {
    ASTNode$State state = state();
    try {
        Annotation a = annotation(lookupType("java.lang", "SuppressWarnings"));
        if(a != null && a.getNumElementValuePair() == 1 && a.getElementValuePair(0).getName().equals("value"))
          return a.getElementValuePair(0).getElementValue().hasValue(annot);
        return false;
      }
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public boolean hasDeprecatedAnnotation() {
    ASTNode$State state = state();
    boolean hasDeprecatedAnnotation_value = annotation(lookupType("java.lang", "Deprecated")) != null;

    return hasDeprecatedAnnotation_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean hasAnnotationSafeVarargs() {
    ASTNode$State state = state();
    boolean hasAnnotationSafeVarargs_value = annotation(lookupType("java.lang", "SafeVarargs")) != null;

    return hasAnnotationSafeVarargs_value;
  }
  /**
   * @attribute syn
   * @aspect Annotations
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/Annotations.jrag:45
   */
  @ASTNodeAnnotation.Attribute
  public boolean hasAnnotationFunctionalInterface() {
    ASTNode$State state = state();
    try {
    		Annotation a = annotation(lookupType("java.lang", "FunctionalInterface"));
    		if(a != null)
    			return true;
    		else
    			return false;
    	}
    finally {
    }
  }
  /**
   * @apilevel internal
   */
  protected boolean isDefault_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isDefault_value;
/**
 * @apilevel internal
 */
private void isDefault_reset() {
  isDefault_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isDefault() {
    if(isDefault_computed) {
      return isDefault_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isDefault_value = numModifier("default") != 0;
    if (isFinal && num == state().boundariesCrossed) {
      isDefault_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDefault_value;
  }
  /**
   * @attribute inh
   * @aspect Modifiers
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:358
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl hostType() {
    ASTNode$State state = state();
    TypeDecl hostType_value = getParent().Define_TypeDecl_hostType(this, null);

    return hostType_value;
  }
  /**
   * @attribute inh
   * @aspect Modifiers
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:360
   */
  @ASTNodeAnnotation.Attribute
  public boolean mayBePublic() {
    ASTNode$State state = state();
    boolean mayBePublic_value = getParent().Define_boolean_mayBePublic(this, null);

    return mayBePublic_value;
  }
  /**
   * @attribute inh
   * @aspect Modifiers
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:361
   */
  @ASTNodeAnnotation.Attribute
  public boolean mayBePrivate() {
    ASTNode$State state = state();
    boolean mayBePrivate_value = getParent().Define_boolean_mayBePrivate(this, null);

    return mayBePrivate_value;
  }
  /**
   * @attribute inh
   * @aspect Modifiers
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:362
   */
  @ASTNodeAnnotation.Attribute
  public boolean mayBeProtected() {
    ASTNode$State state = state();
    boolean mayBeProtected_value = getParent().Define_boolean_mayBeProtected(this, null);

    return mayBeProtected_value;
  }
  /**
   * @attribute inh
   * @aspect Modifiers
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:363
   */
  @ASTNodeAnnotation.Attribute
  public boolean mayBeStatic() {
    ASTNode$State state = state();
    boolean mayBeStatic_value = getParent().Define_boolean_mayBeStatic(this, null);

    return mayBeStatic_value;
  }
  /**
   * @attribute inh
   * @aspect Modifiers
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:364
   */
  @ASTNodeAnnotation.Attribute
  public boolean mayBeFinal() {
    ASTNode$State state = state();
    boolean mayBeFinal_value = getParent().Define_boolean_mayBeFinal(this, null);

    return mayBeFinal_value;
  }
  /**
   * @attribute inh
   * @aspect Modifiers
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:365
   */
  @ASTNodeAnnotation.Attribute
  public boolean mayBeAbstract() {
    ASTNode$State state = state();
    boolean mayBeAbstract_value = getParent().Define_boolean_mayBeAbstract(this, null);

    return mayBeAbstract_value;
  }
  /**
   * @attribute inh
   * @aspect Modifiers
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:366
   */
  @ASTNodeAnnotation.Attribute
  public boolean mayBeVolatile() {
    ASTNode$State state = state();
    boolean mayBeVolatile_value = getParent().Define_boolean_mayBeVolatile(this, null);

    return mayBeVolatile_value;
  }
  /**
   * @attribute inh
   * @aspect Modifiers
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:367
   */
  @ASTNodeAnnotation.Attribute
  public boolean mayBeTransient() {
    ASTNode$State state = state();
    boolean mayBeTransient_value = getParent().Define_boolean_mayBeTransient(this, null);

    return mayBeTransient_value;
  }
  /**
   * @attribute inh
   * @aspect Modifiers
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:368
   */
  @ASTNodeAnnotation.Attribute
  public boolean mayBeStrictfp() {
    ASTNode$State state = state();
    boolean mayBeStrictfp_value = getParent().Define_boolean_mayBeStrictfp(this, null);

    return mayBeStrictfp_value;
  }
  /**
   * @attribute inh
   * @aspect Modifiers
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:369
   */
  @ASTNodeAnnotation.Attribute
  public boolean mayBeSynchronized() {
    ASTNode$State state = state();
    boolean mayBeSynchronized_value = getParent().Define_boolean_mayBeSynchronized(this, null);

    return mayBeSynchronized_value;
  }
  /**
   * @attribute inh
   * @aspect Modifiers
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:370
   */
  @ASTNodeAnnotation.Attribute
  public boolean mayBeNative() {
    ASTNode$State state = state();
    boolean mayBeNative_value = getParent().Define_boolean_mayBeNative(this, null);

    return mayBeNative_value;
  }
  /**
   * @attribute inh
   * @aspect Annotations
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:56
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl lookupType(String packageName, String typeName) {
    ASTNode$State state = state();
    TypeDecl lookupType_String_String_value = getParent().Define_TypeDecl_lookupType(this, null, packageName, typeName);

    return lookupType_String_String_value;
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:434
   * @apilevel internal
   */
  public Annotation Define_Annotation_lookupAnnotation(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
    if (caller == getModifierListNoTransform()) {
      int index = caller.getIndexOfChild(child);
      {
    return annotation(typeDecl);
  }
    }
    else {
      return getParent().Define_Annotation_lookupAnnotation(this, caller, typeDecl);
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
