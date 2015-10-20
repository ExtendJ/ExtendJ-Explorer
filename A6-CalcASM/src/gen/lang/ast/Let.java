/* This file was generated with JastAdd2 (http://jastadd.org) version 2.1.13 */
package lang.ast;

import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.TreeSet;
/**
 * @ast node
 * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/A6-CalcASM/src/jastadd/calc.ast:11
 * @production Let : {@link Expr} ::= <span class="component">{@link Binding}*</span> <span class="component">{@link Expr}</span>;

 */
public class Let extends Expr implements Cloneable {
  /**
   * @aspect CodeGen
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/A6-CalcASM/src/jastadd/CodeGen.jrag:147
   */
  public void genEval(PrintStream out) {
		// evaluate bindings:
		for (int i = 0; i < getNumBinding(); ++i) {
			getBinding(i).genCode(out);
		}
		getExpr().genEval(out);
	}
  /**
   * @aspect PrettyPrint
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/A6-CalcASM/src/jastadd/PrettyPrint.jrag:39
   */
  public void prettyPrint(PrintStream out, String ind) {
		out.println("let");
		for (int i = 0; i < getNumBinding(); ++i) {
			out.print(ind+"  ");
			getBinding(i).prettyPrint(out, ind+"  ");
			out.println();
		}
		out.println(ind + "in");
		out.print(ind+"  ");
		getExpr().prettyPrint(out, ind+"  ");
		out.println();
		out.print(ind + "end");
	}
  /**
   * @declaredat ASTNode:1
   */
  public Let() {
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
    children = new ASTNode[2];
    setChild(new List(), 0);
  }
  /**
   * @declaredat ASTNode:14
   */
  public Let(List<Binding> p0, Expr p1) {
    setChild(p0, 0);
    setChild(p1, 1);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:21
   */
  protected int numChildren() {
    return 2;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:27
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    localLookup_String_int_reset();
    lookup_String_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:35
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:41
   */
  public Let clone() throws CloneNotSupportedException {
    Let node = (Let) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:48
   */
  public Let copy() {
    try {
      Let node = (Let) clone();
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
   * @declaredat ASTNode:67
   */
  @Deprecated
  public Let fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:77
   */
  public Let treeCopyNoTransform() {
    Let tree = (Let) copy();
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
   * @declaredat ASTNode:97
   */
  public Let treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:104
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node);    
  }
  /**
   * Replaces the Binding list.
   * @param list The new list node to be used as the Binding list.
   * @apilevel high-level
   */
  public void setBindingList(List<Binding> list) {
    setChild(list, 0);
  }
  /**
   * Retrieves the number of children in the Binding list.
   * @return Number of children in the Binding list.
   * @apilevel high-level
   */
  public int getNumBinding() {
    return getBindingList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Binding list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Binding list.
   * @apilevel low-level
   */
  public int getNumBindingNoTransform() {
    return getBindingListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Binding list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Binding list.
   * @apilevel high-level
   */
  public Binding getBinding(int i) {
    return (Binding) getBindingList().getChild(i);
  }
  /**
   * Check whether the Binding list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasBinding() {
    return getBindingList().getNumChild() != 0;
  }
  /**
   * Append an element to the Binding list.
   * @param node The element to append to the Binding list.
   * @apilevel high-level
   */
  public void addBinding(Binding node) {
    List<Binding> list = (parent == null) ? getBindingListNoTransform() : getBindingList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addBindingNoTransform(Binding node) {
    List<Binding> list = getBindingListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Binding list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setBinding(Binding node, int i) {
    List<Binding> list = getBindingList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Binding list.
   * @return The node representing the Binding list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Binding")
  public List<Binding> getBindingList() {
    List<Binding> list = (List<Binding>) getChild(0);
    return list;
  }
  /**
   * Retrieves the Binding list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Binding list.
   * @apilevel low-level
   */
  public List<Binding> getBindingListNoTransform() {
    return (List<Binding>) getChildNoTransform(0);
  }
  /**
   * Retrieves the Binding list.
   * @return The node representing the Binding list.
   * @apilevel high-level
   */
  public List<Binding> getBindings() {
    return getBindingList();
  }
  /**
   * Retrieves the Binding list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Binding list.
   * @apilevel low-level
   */
  public List<Binding> getBindingsNoTransform() {
    return getBindingListNoTransform();
  }
  /**
   * Replaces the Expr child.
   * @param node The new node to replace the Expr child.
   * @apilevel high-level
   */
  public void setExpr(Expr node) {
    setChild(node, 1);
  }
  /**
   * Retrieves the Expr child.
   * @return The current node used as the Expr child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Expr")
  public Expr getExpr() {
    return (Expr) getChild(1);
  }
  /**
   * Retrieves the Expr child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Expr child.
   * @apilevel low-level
   */
  public Expr getExprNoTransform() {
    return (Expr) getChildNoTransform(1);
  }
  /**
   * @apilevel internal
   */
  protected java.util.Set localLookup_String_int_visited;
  /**
   * @apilevel internal
   */
  protected java.util.Map localLookup_String_int_values;
  /**
   * @apilevel internal
   */
  private void localLookup_String_int_reset() {
    localLookup_String_int_values = null;
    localLookup_String_int_visited = null;
  }
  @ASTNodeAnnotation.Attribute
  public IdDecl localLookup(String name, int until) {
    java.util.List _parameters = new java.util.ArrayList(2);
    _parameters.add(name);
    _parameters.add(until);
    if (localLookup_String_int_visited == null) localLookup_String_int_visited = new java.util.HashSet(4);
    if (localLookup_String_int_values == null) localLookup_String_int_values = new java.util.HashMap(4);
    ASTNode$State state = state();
    if (localLookup_String_int_values.containsKey(_parameters)) {
      return (IdDecl) localLookup_String_int_values.get(_parameters);
    }
    if (localLookup_String_int_visited.contains(_parameters)) {
      throw new RuntimeException("Circular definition of attr: localLookup in class: org.jastadd.ast.AST.SynDecl");
    }
    localLookup_String_int_visited.add(_parameters);
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    IdDecl localLookup_String_int_value = localLookup_compute(name, until);
    if (true) {
      localLookup_String_int_values.put(_parameters, localLookup_String_int_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    localLookup_String_int_visited.remove(_parameters);
    return localLookup_String_int_value;
  }
  /**
   * @apilevel internal
   */
  private IdDecl localLookup_compute(String name, int until) {
  		for (int i = 0; i <= until; i++) {
  			if (getBinding(i).getIdDecl().getID().equals(name)) {
  				return getBinding(i).getIdDecl();
  			}
  		}
  		return unknownDecl();
  	}
  /**
   * @attribute inh
   * @aspect NameAnalysis
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/A6-CalcASM/src/jastadd/NameAnalysis.jrag:10
   */
  @ASTNodeAnnotation.Attribute
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
  protected java.util.Set lookup_String_visited;
  /**
   * @apilevel internal
   */
  protected java.util.Map lookup_String_values;
  /**
   * @apilevel internal
   */
  private void lookup_String_reset() {
    lookup_String_values = null;
    lookup_String_visited = null;
  }
  /**
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/A6-CalcASM/src/jastadd/NameAnalysis.jrag:26
   * @apilevel internal
   */
  public IdDecl Define_lookup(ASTNode caller, ASTNode child, String name) {
    if (caller == getBindingListNoTransform()) {
      // @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/A6-CalcASM/src/jastadd/NameAnalysis.jrag:21
      int index = caller.getIndexOfChild(child);
      {
      		IdDecl decl = localLookup(name, index);
      		return !decl.isUnknown() ? decl : lookup(name);
      	}
    }
    else if (caller == getExprNoTransform()) {
      // @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/A6-CalcASM/src/jastadd/NameAnalysis.jrag:5
      {
      		IdDecl decl = localLookup(name, getNumBinding()-1);
      		return !decl.isUnknown() ? decl : lookup(name);
      	}
    }
    else {
      return getParent().Define_lookup(this, caller, name);
    }
  }
}
