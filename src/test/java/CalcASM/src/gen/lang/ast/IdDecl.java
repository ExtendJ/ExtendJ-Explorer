/* This file was generated with JastAdd2 (http://jastadd.org) version 2.1.13-47-g075c2ed */
package CalcASM.src.gen.lang.ast;

import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.TreeSet;
/**
 * @ast node
 * @declaredat /home/gda10jth/exjobb/jastadddebugger-exjobb/CalcASM/src/jastadd/calc.ast:14
 * @production IdDecl : {@link ASTNode} ::= <span class="component">&lt;ID:String&gt;</span>;

 */
public class IdDecl extends ASTNode<ASTNode> implements Cloneable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/gda10jth/exjobb/jastadddebugger-exjobb/CalcASM/src/jastadd/PrettyPrint.jrag:35
   */
  public void prettyPrint(PrintStream out, String ind) {
		out.print(getID());
	}
  /**
   * @declaredat ASTNode:1
   */
  public IdDecl() {
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
  public IdDecl(String p0) {
    setID(p0);
  }
  /**
   * @declaredat ASTNode:15
   */
  public IdDecl(beaver.Symbol p0) {
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
    address_reset();
    localIndex_reset();
    isMultiplyDeclared_reset();
    isUnknown_reset();
    lookup_String_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:38
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:44
   */
  public IdDecl clone() throws CloneNotSupportedException {
    IdDecl node = (IdDecl) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:51
   */
  public IdDecl copy() {
    try {
      IdDecl node = (IdDecl) clone();
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
   * @declaredat ASTNode:70
   */
  @Deprecated
  public IdDecl fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:80
   */
  public IdDecl treeCopyNoTransform() {
    IdDecl tree = (IdDecl) copy();
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
   * @declaredat ASTNode:100
   */
  public IdDecl treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:107
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) && (tokenString_ID == ((IdDecl)node).tokenString_ID);    
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
  protected boolean address_visited = false;
  /**
   * @apilevel internal
   */
  private void address_reset() {
    address_computed = false;
    address_value = null;
    address_visited = false;
  }
  /**
   * @apilevel internal
   */
  protected boolean address_computed = false;
  /**
   * @apilevel internal
   */
  protected String address_value;

  /**
   * Address of local variable variable in the current stack frame.
   * @attribute syn
   * @aspect CodeGen
   * @declaredat /home/gda10jth/exjobb/jastadddebugger-exjobb/CalcASM/src/jastadd/CodeGen.jrag:171
   */
  @ASTNodeAnnotation.Attribute(kind=ASTNodeAnnotation.Kind.SYN)
  @ASTNodeAnnotation.Source(aspect="CodeGen", declaredAt="/home/gda10jth/exjobb/jastadddebugger-exjobb/CalcASM/src/jastadd/CodeGen.jrag:171")
  public String address() {
    ASTNode$State state = state();
    if (address_computed) {
      return address_value;
    }
    if (address_visited) {
      throw new RuntimeException("Circular definition of attr: address in class: org.jastadd.ast.AST.SynDecl");
    }
    address_visited = true;
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    address_value = "-"+(localIndex()*8)+"(%rbp)";
    if (true) {
      address_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;
    address_visited = false;
    return address_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean localIndex_visited = false;
  /**
   * @apilevel internal
   */
  private void localIndex_reset() {
    localIndex_computed = false;
    localIndex_visited = false;
  }
  /**
   * @apilevel internal
   */
  protected boolean localIndex_computed = false;
  /**
   * @apilevel internal
   */
  protected int localIndex_value;

  /**
   * Local variable numbering.
   * @attribute syn
   * @aspect CodeGen
   * @declaredat /home/gda10jth/exjobb/jastadddebugger-exjobb/CalcASM/src/jastadd/CodeGen.jrag:181
   */
  @ASTNodeAnnotation.Attribute(kind=ASTNodeAnnotation.Kind.SYN)
  @ASTNodeAnnotation.Source(aspect="CodeGen", declaredAt="/home/gda10jth/exjobb/jastadddebugger-exjobb/CalcASM/src/jastadd/CodeGen.jrag:181")
  public int localIndex() {
    ASTNode$State state = state();
    if (localIndex_computed) {
      return localIndex_value;
    }
    if (localIndex_visited) {
      throw new RuntimeException("Circular definition of attr: localIndex in class: org.jastadd.ast.AST.SynDecl");
    }
    localIndex_visited = true;
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    localIndex_value = prevNode().localIndex() + 1;
    if (true) {
      localIndex_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;
    localIndex_visited = false;
    return localIndex_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isMultiplyDeclared_visited = false;
  /**
   * @apilevel internal
   */
  private void isMultiplyDeclared_reset() {
    isMultiplyDeclared_computed = false;
    isMultiplyDeclared_visited = false;
  }
  /**
   * @apilevel internal
   */
  protected boolean isMultiplyDeclared_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isMultiplyDeclared_value;

  /**
   * @attribute syn
   * @aspect NameAnalysis
   * @declaredat /home/gda10jth/exjobb/jastadddebugger-exjobb/CalcASM/src/jastadd/NameAnalysis.jrag:27
   */
  @ASTNodeAnnotation.Attribute(kind=ASTNodeAnnotation.Kind.SYN)
  @ASTNodeAnnotation.Source(aspect="NameAnalysis", declaredAt="/home/gda10jth/exjobb/jastadddebugger-exjobb/CalcASM/src/jastadd/NameAnalysis.jrag:27")
  public boolean isMultiplyDeclared() {
    ASTNode$State state = state();
    if (isMultiplyDeclared_computed) {
      return isMultiplyDeclared_value;
    }
    if (isMultiplyDeclared_visited) {
      throw new RuntimeException("Circular definition of attr: isMultiplyDeclared in class: org.jastadd.ast.AST.SynDecl");
    }
    isMultiplyDeclared_visited = true;
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    isMultiplyDeclared_value = lookup(getID()) != this;
    if (true) {
      isMultiplyDeclared_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;
    isMultiplyDeclared_visited = false;
    return isMultiplyDeclared_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isUnknown_visited = false;
  /**
   * @apilevel internal
   */
  private void isUnknown_reset() {
    isUnknown_computed = false;
    isUnknown_visited = false;
  }
  /**
   * @apilevel internal
   */
  protected boolean isUnknown_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isUnknown_value;

  /**
   * @attribute syn
   * @aspect UnknownDecl
   * @declaredat /home/gda10jth/exjobb/jastadddebugger-exjobb/CalcASM/src/jastadd/UnknownDecl.jrag:7
   */
  @ASTNodeAnnotation.Attribute(kind=ASTNodeAnnotation.Kind.SYN)
  @ASTNodeAnnotation.Source(aspect="UnknownDecl", declaredAt="/home/gda10jth/exjobb/jastadddebugger-exjobb/CalcASM/src/jastadd/UnknownDecl.jrag:7")
  public boolean isUnknown() {
    ASTNode$State state = state();
    if (isUnknown_computed) {
      return isUnknown_value;
    }
    if (isUnknown_visited) {
      throw new RuntimeException("Circular definition of attr: isUnknown in class: org.jastadd.ast.AST.SynDecl");
    }
    isUnknown_visited = true;
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    isUnknown_value = false;
    if (true) {
      isUnknown_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;
    isUnknown_visited = false;
    return isUnknown_value;
  }
  /**
   * @attribute inh
   * @aspect NameAnalysis
   * @declaredat /home/gda10jth/exjobb/jastadddebugger-exjobb/CalcASM/src/jastadd/NameAnalysis.jrag:26
   */
  /**
   * @attribute inh
   * @aspect NameAnalysis
   * @declaredat /home/gda10jth/exjobb/jastadddebugger-exjobb/CalcASM/src/jastadd/NameAnalysis.jrag:26
   */
  @ASTNodeAnnotation.Attribute(kind=ASTNodeAnnotation.Kind.INH)
  @ASTNodeAnnotation.Source(aspect="NameAnalysis", declaredAt="/home/gda10jth/exjobb/jastadddebugger-exjobb/CalcASM/src/jastadd/NameAnalysis.jrag:26")
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
  protected void collect_contributors_Program_errors() {
    // @declaredat /home/gda10jth/exjobb/jastadddebugger-exjobb/CalcASM/src/jastadd/Errors.jrag:38
    if (isMultiplyDeclared()) {
      {
        Program ref = (Program) (program());
        if (ref != null) {
          ref.Program_errors_contributors().add(this);
        }
      }
    }
    super.collect_contributors_Program_errors();
  }
  protected void contributeTo_Program_errors(Set<ErrorMessage> collection) {
    super.contributeTo_Program_errors(collection);
    if (isMultiplyDeclared()) {
      collection.add(error("symbol '" + getID() + "' is already declared!"));
    }
  }
}
