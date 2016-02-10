/* This file was generated with JastAdd2 (http://jastadd.org) version 2.1.13-47-g075c2ed */
package CalcASM.src.java.gen;

import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.TreeSet;
/**
 * @ast node
 * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/calc.ast:9
 * @production IdUse : {@link Expr} ::= <span class="component">&lt;ID:String&gt;</span>;

 */
public class IdUse extends Expr implements Cloneable {
  /**
   * @aspect CodeGen
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/CodeGen.jrag:124
   */
  public void genEval(PrintStream out) {
		out.println("        movq " + decl().address() + ", %rax");
	}
  /**
   * @aspect PrettyPrint
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/PrettyPrint.jrag:31
   */
  public void prettyPrint(PrintStream out, String ind) {
		out.print(getID());
	}
  /**
   * @declaredat ASTNode:1
   */
  public IdUse() {
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
  public IdUse(String p0) {
    setID(p0);
  }
  /**
   * @declaredat ASTNode:15
   */
  public IdUse(beaver.Symbol p0) {
    setID(p0);
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
    decl_reset();
    isCircular_reset();
    lookup_String_reset();
    inExprOf_IdDecl_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:37
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:43
   */
  public IdUse clone() throws CloneNotSupportedException {
    IdUse node = (IdUse) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:50
   */
  public IdUse copy() {
    try {
      IdUse node = (IdUse) clone();
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
   * @declaredat ASTNode:69
   */
  @Deprecated
  public IdUse fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:79
   */
  public IdUse treeCopyNoTransform() {
    IdUse tree = (IdUse) copy();
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
   * @declaredat ASTNode:99
   */
  public IdUse treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:106
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) && (tokenString_ID == ((IdUse)node).tokenString_ID);    
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
    if (symbol.value != null && !(symbol.value instanceof String))
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
   * @apilevel internal
   */
  protected boolean decl_visited = false;
  /**
   * @apilevel internal
   */
  private void decl_reset() {
    decl_computed = false;
    decl_value = null;
    decl_visited = false;
  }
  /**
   * @apilevel internal
   */
  protected boolean decl_computed = false;
  /**
   * @apilevel internal
   */
  protected IdDecl decl_value;

  /**
   * @attribute syn
   * @aspect NameAnalysis
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/NameAnalysis.jrag:2
   */
  @ASTNodeAnnotation.Attribute(kind=ASTNodeAnnotation.Kind.SYN)
  @ASTNodeAnnotation.Source(aspect="NameAnalysis", declaredAt="/home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/NameAnalysis.jrag:2")
  public IdDecl decl() {
    ASTNode$State state = state();
    if (decl_computed) {
      return decl_value;
    }
    if (decl_visited) {
      throw new RuntimeException("Circular definition of attr: decl in class: org.jastadd.ast.AST.SynDecl");
    }
    decl_visited = true;
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    decl_value = lookup(getID());
    if (true) {
      decl_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;
    decl_visited = false;
    return decl_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isCircular_visited = false;
  /**
   * @apilevel internal
   */
  private void isCircular_reset() {
    isCircular_computed = false;
    isCircular_visited = false;
  }
  /**
   * @apilevel internal
   */
  protected boolean isCircular_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isCircular_value;

  /**
   * @attribute syn
   * @aspect CircularDefinitions
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/NameAnalysis.jrag:32
   */
  @ASTNodeAnnotation.Attribute(kind=ASTNodeAnnotation.Kind.SYN)
  @ASTNodeAnnotation.Source(aspect="CircularDefinitions", declaredAt="/home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/NameAnalysis.jrag:32")
  public boolean isCircular() {
    ASTNode$State state = state();
    if (isCircular_computed) {
      return isCircular_value;
    }
    if (isCircular_visited) {
      throw new RuntimeException("Circular definition of attr: isCircular in class: org.jastadd.ast.AST.SynDecl");
    }
    isCircular_visited = true;
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    isCircular_value = inExprOf(decl());
    if (true) {
      isCircular_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;
    isCircular_visited = false;
    return isCircular_value;
  }
  /**
   * @attribute inh
   * @aspect NameAnalysis
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/NameAnalysis.jrag:3
   */
  /**
   * @attribute inh
   * @aspect NameAnalysis
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/NameAnalysis.jrag:3
   */
  @ASTNodeAnnotation.Attribute(kind=ASTNodeAnnotation.Kind.INH)
  @ASTNodeAnnotation.Source(aspect="NameAnalysis", declaredAt="/home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/NameAnalysis.jrag:3")
  public IdDecl lookup(String name) {
    Object _parameters = name;
    if (lookup_String_visited == null) lookup_String_visited = new java.util.HashSet(4);
    if (lookup_String_values == null) lookup_String_values = new java.util.HashMap(4);
    ASTNode$State state = state();
    if (lookup_String_values.containsKey(_parameters)) {
      return (IdDecl) lookup_String_values.get(_parameters);
    }
    if (lookup_String_visited.contains(_parameters)) {
      throw new RuntimeException("Circular definition of attr: lookup in class: org.jastadd.ast.AST.InhDecl");
    }
    lookup_String_visited.add(_parameters);
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    IdDecl lookup_String_value = getParent().Define_lookup(this, null, name);
    if (true) {
      lookup_String_values.put(_parameters, lookup_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;
    lookup_String_visited.remove(_parameters);
    return lookup_String_value;
  }
  /**
   * @apilevel internal
   */
  protected Set lookup_String_visited;
  /**
   * @apilevel internal
   */
  private void lookup_String_reset() {
    lookup_String_values = null;
    lookup_String_visited = null;
  }
  /**
   * @apilevel internal
   */
  protected java.util.Map lookup_String_values;
  /**
   * @attribute inh
   * @aspect CircularDefinitions
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/NameAnalysis.jrag:33
   */
  /**
   * @attribute inh
   * @aspect CircularDefinitions
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/NameAnalysis.jrag:33
   */
  @ASTNodeAnnotation.Attribute(kind=ASTNodeAnnotation.Kind.INH)
  @ASTNodeAnnotation.Source(aspect="CircularDefinitions", declaredAt="/home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/NameAnalysis.jrag:33")
  public boolean inExprOf(IdDecl decl) {
    Object _parameters = decl;
    if (inExprOf_IdDecl_visited == null) inExprOf_IdDecl_visited = new java.util.HashSet(4);
    if (inExprOf_IdDecl_values == null) inExprOf_IdDecl_values = new java.util.HashMap(4);
    ASTNode$State state = state();
    if (inExprOf_IdDecl_values.containsKey(_parameters)) {
      return (Boolean) inExprOf_IdDecl_values.get(_parameters);
    }
    if (inExprOf_IdDecl_visited.contains(_parameters)) {
      throw new RuntimeException("Circular definition of attr: inExprOf in class: org.jastadd.ast.AST.InhDecl");
    }
    inExprOf_IdDecl_visited.add(_parameters);
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    boolean inExprOf_IdDecl_value = getParent().Define_inExprOf(this, null, decl);
    if (true) {
      inExprOf_IdDecl_values.put(_parameters, inExprOf_IdDecl_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;
    inExprOf_IdDecl_visited.remove(_parameters);
    return inExprOf_IdDecl_value;
  }
  /**
   * @apilevel internal
   */
  protected Set inExprOf_IdDecl_visited;
  /**
   * @apilevel internal
   */
  private void inExprOf_IdDecl_reset() {
    inExprOf_IdDecl_values = null;
    inExprOf_IdDecl_visited = null;
  }
  /**
   * @apilevel internal
   */
  protected java.util.Map inExprOf_IdDecl_values;
}
