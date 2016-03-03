/* This file was generated with JastAdd2 (http://jastadd.org) version 2.1.13-47-g075c2ed */
package CalcASM.src.java.gen;

import java.io.PrintStream;
/**
 * @ast node
 * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/calc.ast:8
 * @production Numeral : {@link Expr} ::= <span class="component">&lt;NUMERAL:String&gt;</span>;

 */
public class Numeral extends Expr implements Cloneable {
  /**
   * @aspect CodeGen
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/CodeGen.jrag:120
   */
  public void genEval(PrintStream out) {
		out.println("        movq $" + getNUMERAL() + ", %rax");
	}
  /**
   * @aspect PrettyPrint
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/PrettyPrint.jrag:27
   */
  public void prettyPrint(PrintStream out, String ind) {
		out.print(getNUMERAL());
	}
  /**
   * @declaredat ASTNode:1
   */
  public Numeral() {
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
  public Numeral(String p0) {
    setNUMERAL(p0);
  }
  /**
   * @declaredat ASTNode:15
   */
  public Numeral(beaver.Symbol p0) {
    setNUMERAL(p0);
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
  public void flushAttrCache() {
    super.flushAttrCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:33
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:39
   */
  public Numeral clone() throws CloneNotSupportedException {
    Numeral node = (Numeral) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:46
   */
  public Numeral copy() {
    try {
      Numeral node = (Numeral) clone();
      node.parent = null;
      if (children != null) {
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
   * @deprecated Please use treeCopy or treeCopyNoTransform instead
   * @declaredat ASTNode:65
   */
  @Deprecated
  public Numeral fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:75
   */
  public Numeral treeCopyNoTransform() {
    Numeral tree = (Numeral) copy();
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        ASTNode child = (ASTNode) children[i];
        if (child != null) {
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
   * @declaredat ASTNode:95
   */
  public Numeral treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:102
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) && (tokenString_NUMERAL == ((Numeral)node).tokenString_NUMERAL);    
  }
  /**
   * Replaces the lexeme NUMERAL.
   * @param value The new value for the lexeme NUMERAL.
   * @apilevel high-level
   */
  public void setNUMERAL(String value) {
    tokenString_NUMERAL = value;
  }
  /**
   * @apilevel internal
   */
  protected String tokenString_NUMERAL;
  /**
   */
  public int NUMERALstart;
  /**
   */
  public int NUMERALend;
  /**
   * JastAdd-internal setter for lexeme NUMERAL using the Beaver parser.
   * @param symbol Symbol containing the new value for the lexeme NUMERAL
   * @apilevel internal
   */
  public void setNUMERAL(beaver.Symbol symbol) {
    if (symbol.value != null && !(symbol.value instanceof String))
    throw new UnsupportedOperationException("setNUMERAL is only valid for String lexemes");
    tokenString_NUMERAL = (String)symbol.value;
    NUMERALstart = symbol.getStart();
    NUMERALend = symbol.getEnd();
  }
  /**
   * Retrieves the value for the lexeme NUMERAL.
   * @return The value for the lexeme NUMERAL.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Token(name="NUMERAL")
  public String getNUMERAL() {
    return tokenString_NUMERAL != null ? tokenString_NUMERAL : "";
  }
}
