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
 * The abstract base class for all literals.
 * @ast node
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/grammar/Literals.ast:4
 * @production Literal : {@link PrimaryExpr} ::= <span class="component">&lt;LITERAL:String&gt;</span>;

 */
public abstract class Literal extends PrimaryExpr implements Cloneable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:304
   */
  public void prettyPrint(StringBuffer sb) {
    sb.append(getLITERAL());
  }
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:331
   */
  protected static String escape(String str) {
    StringBuffer sb = new StringBuffer();
    for (int i=0; i < str.length(); i++) {
      switch(str.charAt(i)) {
        case '\b': sb.append("\\b"); break;
        case '\t': sb.append("\\t"); break;
        case '\n': sb.append("\\n"); break;
        case '\f': sb.append("\\f"); break;
        case '\r': sb.append("\\r"); break;
        case '\"': sb.append("\\\""); break;
        case '\'': sb.append("\\\'"); break;
        case '\\': sb.append("\\\\"); break;
        default:
          int value = (int)str.charAt(i);
          if (value < 0x20 || (value > 0x7e)) {
            sb.append(asEscape(value));
          } else {
            sb.append(str.charAt(i));
          }
      }
    }
    return sb.toString();
  }
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:355
   */
  protected static String asEscape(int value) {
    StringBuffer sb = new StringBuffer("\\u");
    String hex = Integer.toHexString(value);
    for (int i = 0; i < 4-hex.length(); i++) {
      sb.append("0");
    }
    sb.append(hex);
    return sb.toString();
  }
  /**
   * @aspect BytecodeCONSTANT
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BytecodeCONSTANT.jrag:75
   */
  public static Literal buildBooleanLiteral(boolean value) {
    return new BooleanLiteral(value ? "true" : "false");
  }
  /**
   * @aspect BytecodeCONSTANT
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BytecodeCONSTANT.jrag:79
   */
  public static Literal buildStringLiteral(String value) {
    return new StringLiteral(value);
  }
  /**
   * @declaredat ASTNode:1
   */
  public Literal() {
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
  public Literal(String p0) {
    setLITERAL(p0);
  }
  /**
   * @declaredat ASTNode:15
   */
  public Literal(beaver.Symbol p0) {
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
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:33
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    constant_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:40
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:46
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:52
   */
  public Literal clone() throws CloneNotSupportedException {
    Literal node = (Literal) super.clone();
    return node;
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @deprecated Please use emitTreeCopy or emitTreeCopyNoTransform instead
   * @declaredat ASTNode:63
   */
  public abstract Literal fullCopy();
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:70
   */
  public abstract Literal treeCopyNoTransform();
  /**
   * Create a deep copy of the AST subtree at this node.
   * The subtree of this node is traversed to trigger rewrites before copy.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:78
   */
  public abstract Literal treeCopy();
  /**
   * Replaces the lexeme LITERAL.
   * @param value The new value for the lexeme LITERAL.
   * @apilevel high-level
   */
  public void setLITERAL(String value) {
    tokenString_LITERAL = value;
  }
  /**
   * @apilevel internal
   */
  protected String tokenString_LITERAL;
  /**
   */
  public int LITERALstart;
  /**
   */
  public int LITERALend;
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
   * @return a fresh double literal representing the given value
   * @aspect Java7Literals
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Literals.jrag:50
   */
    public static Literal buildDoubleLiteral(double value) {
    String digits = Double.toString(value);
    NumericLiteral lit = new DoubleLiteral(digits);
    lit.setDigits(digits);
    lit.setKind(NumericLiteral.DECIMAL);
    return lit;
  }
  /**
   * @return a fresh float literal representing the given value
   * @aspect Java7Literals
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Literals.jrag:62
   */
    public static Literal buildFloatLiteral(float value) {
    String digits = Float.toString(value);
    NumericLiteral lit = new FloatingPointLiteral(digits);
    lit.setDigits(digits);
    lit.setKind(NumericLiteral.DECIMAL);
    return lit;
  }
  /**
   * @return a fresh integer literal representing the given value
   * @aspect Java7Literals
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Literals.jrag:74
   */
    public static Literal buildIntegerLiteral(int value) {
    String digits = Integer.toHexString(value);
    NumericLiteral lit = new IntegerLiteral("0x"+digits);
    lit.setDigits(digits.toLowerCase());
    lit.setKind(NumericLiteral.HEXADECIMAL);
    return lit;
  }
  /**
   * @return a fresh long literal representing the given value
   * @aspect Java7Literals
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Literals.jrag:86
   */
    public static Literal buildLongLiteral(long value) {
    String digits = Long.toHexString(value);
    NumericLiteral lit = new LongLiteral("0x"+digits);
    lit.setDigits(digits.toLowerCase());
    lit.setKind(NumericLiteral.HEXADECIMAL);
    return lit;
  }
  @ASTNodeAnnotation.Attribute
  public String dumpString() {
    ASTNode$State state = state();
    String dumpString_value = getClass().getName() + " [" + getLITERAL() + "]";

    return dumpString_value;
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
      throw new UnsupportedOperationException("ConstantExpression operation constant" +
        " not supported for type " + getClass().getName());
    }
  @ASTNodeAnnotation.Attribute
  public boolean isConstant() {
    ASTNode$State state = state();
    boolean isConstant_value = true;

    return isConstant_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isTrue() {
    ASTNode$State state = state();
    boolean isTrue_value = false;

    return isTrue_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isFalse() {
    ASTNode$State state = state();
    boolean isFalse_value = false;

    return isFalse_value;
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
