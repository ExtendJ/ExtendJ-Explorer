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
 * Java floating point literal. Can store any value representable as an
 * IEEE 754 32-bit single-precision floating point number.
 * @ast node
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/grammar/Literals.ast:60
 * @production FloatingPointLiteral : {@link NumericLiteral};

 */
public class FloatingPointLiteral extends NumericLiteral implements Cloneable {
  /**
   * @aspect TypeCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeCheck.jrag:584
   */
  public void typeCheck() {
    if (!isZero() && constant().floatValue() == 0.0f)
      error("It is an error for nonzero floating-point " + getLITERAL() + " to round to zero");
    if (constant().floatValue() == Float.NEGATIVE_INFINITY || constant().floatValue() == Float.POSITIVE_INFINITY)
      error("It is an error for floating-point " + getLITERAL() + " to round to an infinity");
  }
  /**
   * @declaredat ASTNode:1
   */
  public FloatingPointLiteral() {
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
  public FloatingPointLiteral(String p0) {
    setLITERAL(p0);
  }
  /**
   * @declaredat ASTNode:15
   */
  public FloatingPointLiteral(beaver.Symbol p0) {
    setLITERAL(p0);
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
    return true;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:33
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    type_reset();
    isZero_reset();
    constant_reset();
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
  public FloatingPointLiteral clone() throws CloneNotSupportedException {
    FloatingPointLiteral node = (FloatingPointLiteral) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:61
   */
  public FloatingPointLiteral copy() {
    try {
      FloatingPointLiteral node = (FloatingPointLiteral) clone();
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
  public FloatingPointLiteral fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:89
   */
  public FloatingPointLiteral treeCopyNoTransform() {
    FloatingPointLiteral tree = (FloatingPointLiteral) copy();
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
  public FloatingPointLiteral treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:116
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_LITERAL == ((FloatingPointLiteral)node).tokenString_LITERAL);    
  }
  /**
   * Replaces the lexeme LITERAL.
   * @param value The new value for the lexeme LITERAL.
   * @apilevel high-level
   */
  public void setLITERAL(String value) {
    tokenString_LITERAL = value;
  }
  /**
   * JastAdd-internal setter for lexeme LITERAL using the Beaver parser.
   * @param symbol Symbol containing the new value for the lexeme LITERAL
   * @apilevel internal
   */
  public void setLITERAL(beaver.Symbol symbol) {
    if(symbol.value != null && !(symbol.value instanceof String))
    throw new UnsupportedOperationException("setLITERAL is only valid for String lexemes");
    tokenString_LITERAL = (String)symbol.value;
    LITERALstart = symbol.getStart();
    LITERALend = symbol.getEnd();
  }
  /**
   * Retrieves the value for the lexeme LITERAL.
   * @return The value for the lexeme LITERAL.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="LITERAL")
  public String getLITERAL() {
    return tokenString_LITERAL != null ? tokenString_LITERAL : "";
  }
  /**
   * Defer pretty printing to superclass.
   * @aspect Java7Literals
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Literals.jrag:105
   */
    public void prettyPrint(StringBuffer sb) {
    super.prettyPrint(sb);
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
    type_value = typeFloat();
    if (isFinal && num == state().boundariesCrossed) {
      type_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return type_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isZero_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isZero_value;
/**
 * @apilevel internal
 */
private void isZero_reset() {
  isZero_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isZero() {
    if(isZero_computed) {
      return isZero_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isZero_value = isZero_compute();
    if (isFinal && num == state().boundariesCrossed) {
      isZero_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isZero_value;
  }
  /**
   * @apilevel internal
   */
  private boolean isZero_compute() {
      for(int i = 0; i < digits.length(); i++) {
        char c = digits.charAt(i);
        if (c == 'e' || c == 'p') break;
        if (c != '0' && c != '.') {
          return false;
        }
      }
      return true;
    }
  /**
   * @apilevel internal
   */
  protected boolean constant_computed = false;
  /**
   * @apilevel internal
   */
  protected Constant constant_value;
/**
 * @apilevel internal
 */
private void constant_reset() {
  constant_computed = false;
  constant_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Constant constant() {
    if(constant_computed) {
      return constant_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    constant_value = constant_compute();
    if (isFinal && num == state().boundariesCrossed) {
      constant_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return constant_value;
  }
  /**
   * @apilevel internal
   */
  private Constant constant_compute() {
      try {
        return Constant.create(Float.parseFloat(getDigits()));
      }
      catch (NumberFormatException e) {
        Constant c = Constant.create(0.0f);
        c.error = true;
        return c;
      }
    }
  @ASTNodeAnnotation.Attribute
  public boolean needsRewrite() {
    ASTNode$State state = state();
    boolean needsRewrite_value = false;

    return needsRewrite_value;
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
