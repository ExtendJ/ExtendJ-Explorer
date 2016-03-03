/* This file was generated with JastAdd2 (http://jastadd.org) version 2.1.13-47-g075c2ed */
package CalcASM.src.java.gen;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
/**
 * @ast node
 * @production ASTNode;

 */
public class ASTNode<T extends ASTNode> extends beaver.Symbol implements Cloneable, Iterable<T> {
  /**
   * @aspect DumpTree
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/DumpTree.jrag:9
   */
  private String DUMP_TREE_INDENT = "  ";
  /**
   * @aspect DumpTree
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/DumpTree.jrag:11
   */
  public String dumpTree() {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		dumpTree(new PrintStream(bytes));
		return bytes.toString();
	}
  /**
   * @aspect DumpTree
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/DumpTree.jrag:17
   */
  public void dumpTree(PrintStream out) {
		dumpTree(out, "");
		out.flush();
	}
  /**
   * @aspect DumpTree
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/DumpTree.jrag:22
   */
  public void dumpTree(PrintStream out, String indent) {
		out.print(indent + getClass().getSimpleName());
		out.println(getTokens());
		String childIndent = indent + DUMP_TREE_INDENT;
		for (ASTNode child : this) {
			if (child == null)  {
				out.println(childIndent + "null");
			} else {
				child.dumpTree(out, childIndent);
			}
		}
	}
  /**
   * @aspect DumpTree
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/DumpTree.jrag:35
   */
  public String getTokens() {
		StringBuilder sb = new StringBuilder();
		java.lang.reflect.Method[] methods = getClass().getMethods();
		for (java.lang.reflect.Method method : getClass().getMethods()) {
			ASTNodeAnnotation.Token token = method.getAnnotation(ASTNodeAnnotation.Token.class);
			if (token != null) {
				try {
					sb.append(" " + token.name() + "=\"" + method.invoke(this) + "\"");
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				}
			}
		}
		return sb.toString();
	}
  /**
   * @aspect Errors
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/Errors.jrag:22
   */
  protected ErrorMessage error(String message) {
		return new ErrorMessage(message, getLine(getStart()));
	}
  /**
   * @aspect PrettyPrint
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/PrettyPrint.jrag:4
   */
  public void prettyPrint(PrintStream out) {
		prettyPrint(out, "");
		out.println();
	}
  /**
   * @aspect PrettyPrint
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/PrettyPrint.jrag:9
   */
  public void prettyPrint(PrintStream out, String ind) {
		for (int i = 0; i < getNumChild(); ++i) {
			getChild(i).prettyPrint(out, ind);
		}
	}
  /**
   * @declaredat ASTNode:1
   */
  public ASTNode() {
    super();
    init$Children();
  }
  /**
   * Initializes the child array to the correct size.
   * Initializes List and Opt nta children.
   * @apilevel internal
   * @ast method
   * @declaredat ASTNode:11
   */
  public void init$Children() {
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:16
   */
  private int childIndex;
  /**
   * @apilevel low-level
   * @declaredat ASTNode:21
   */
  public int getIndexOfChild(ASTNode node) {
    if (node == null) {
      return -1;
    }
    if (node.childIndex < numChildren && node == children[node.childIndex]) {
      return node.childIndex;
    }
    for(int i = 0; children != null && i < children.length; i++) {
      if (children[i] == node) {
        node.childIndex = i;
        return i;
      }
    }
    return -1;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:40
   */
  public static final boolean generatedWithCacheCycle = true;
  /**
   * @apilevel internal
   * @declaredat ASTNode:44
   */
  public static final boolean generatedWithComponentCheck = false;
  /**
   * Parent pointer
   * @apilevel low-level
   * @declaredat ASTNode:50
   */
  protected ASTNode parent;
  /**
   * Child array
   * @apilevel low-level
   * @declaredat ASTNode:56
   */
  protected ASTNode[] children;
  /**
   * @apilevel internal
   * @declaredat ASTNode:62
   */
  private static ASTNode$State state = new ASTNode$State();
  /**
   * @apilevel internal
   * @declaredat ASTNode:67
   */
  public final ASTNode$State state() {
    return state;
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:75
   */
  public T getChild(int i) {

    // No rewrites
    ASTNode child = getChildNoTransform(i);
    return (T) child;

  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:85
   */
  public void addChild(T node) {
    setChild(node, getNumChildNoTransform());
  }
  /**
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @apilevel low-level
   * @declaredat ASTNode:92
   */
  public final T getChildNoTransform(int i) {
    if (children == null) {
      return null;
    }
    T child = (T)children[i];
    return child;
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:102
   */
  protected int numChildren;
  /**
   * @apilevel low-level
   * @declaredat ASTNode:107
   */
  protected int numChildren() {
    return numChildren;
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:114
   */
  public int getNumChild() {
    return numChildren();
  }
  /**
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @apilevel low-level
   * @declaredat ASTNode:122
   */
  public final int getNumChildNoTransform() {
    return numChildren();
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:128
   */
  public void setChild(ASTNode node, int i) {
    if (children == null) {
      children = new ASTNode[(i+1>4 || !(this instanceof List))?i+1:4];
    } else if (i >= children.length) {
      ASTNode c[] = new ASTNode[i << 1];
      System.arraycopy(children, 0, c, 0, children.length);
      children = c;
    }
    children[i] = node;
    if (i >= numChildren) {
      numChildren = i+1;
    }
    if (node != null) {
      node.setParent(this);
      node.childIndex = i;
    }
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:148
   */
  public void insertChild(ASTNode node, int i) {
    if (children == null) {
      children = new ASTNode[(i+1>4 || !(this instanceof List))?i+1:4];
      children[i] = node;
    } else {
      ASTNode c[] = new ASTNode[children.length + 1];
      System.arraycopy(children, 0, c, 0, i);
      c[i] = node;
      if (i < children.length) {
        System.arraycopy(children, i, c, i+1, children.length-i);
        for(int j = i+1; j < c.length; ++j) {
          if (c[j] != null) {
            c[j].childIndex = j;
          }
        }
      }
      children = c;
    }
    numChildren++;
    if (node != null) {
      node.setParent(this);
      node.childIndex = i;
    }
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:175
   */
  public void removeChild(int i) {
    if (children != null) {
      ASTNode child = (ASTNode) children[i];
      if (child != null) {
        child.parent = null;
        child.childIndex = -1;
      }
      // Adding a check of this instance to make sure its a List, a move of children doesn't make
      // any sense for a node unless its a list. Also, there is a problem if a child of a non-List node is removed
      // and siblings are moved one step to the right, with null at the end.
      if (this instanceof List || this instanceof Opt) {
        System.arraycopy(children, i+1, children, i, children.length-i-1);
        children[children.length-1] = null;
        numChildren--;
        // fix child indices
        for(int j = i; j < numChildren; ++j) {
          if (children[j] != null) {
            child = (ASTNode) children[j];
            child.childIndex = j;
          }
        }
      } else {
        children[i] = null;
      }
    }
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:204
   */
  public ASTNode getParent() {
    return (ASTNode) parent;
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:210
   */
  public void setParent(ASTNode node) {
    parent = node;
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:272
   */
  public java.util.Iterator<T> iterator() {
    return new java.util.Iterator<T>() {
      private int counter = 0;
      public boolean hasNext() {
        return counter < getNumChild();
      }
      public T next() {
        if (hasNext())
          return (T)getChild(counter++);
        else
          return null;
      }
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:292
   */
  public void flushTreeCache() {
    flushCache();
    if (children == null) {
      return;
    }
    for (int i = 0; i < children.length; i++) {
      if (children[i] != null) {
        ((ASTNode)children[i]).flushTreeCache();
      }
    }
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:306
   */
  public void flushCache() {
    flushAttrAndCollectionCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:312
   */
  public void flushAttrAndCollectionCache() {
    flushAttrCache();
    flushCollectionCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:319
   */
  public void flushAttrCache() {
    numLocals_reset();
    localIndex_reset();
    lastNode_reset();
    prevNode_int_reset();
    prevNode_reset();
    program_reset();
    unknownDecl_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:331
   */
  public void flushCollectionCache() {
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:336
   */
  public ASTNode<T> clone() throws CloneNotSupportedException {
    ASTNode node = (ASTNode) super.clone();
    node.flushAttrAndCollectionCache();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:344
   */
  public ASTNode<T> copy() {
    try {
      ASTNode node = (ASTNode) clone();
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
   * @declaredat ASTNode:363
   */
  @Deprecated
  public ASTNode<T> fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:373
   */
  public ASTNode<T> treeCopyNoTransform() {
    ASTNode tree = (ASTNode) copy();
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
   * @declaredat ASTNode:393
   */
  public ASTNode<T> treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * Performs a full traversal of the tree using getChild to trigger rewrites
   * @apilevel low-level
   * @declaredat ASTNode:401
   */
  public void doFullTraversal() {
    for (int i = 0; i < getNumChild(); i++) {
      getChild(i).doFullTraversal();
    }
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:409
   */
  protected boolean is$Equal(ASTNode n1, ASTNode n2) {
    if (n1 == null && n2 == null) return true;
    if (n1 == null || n2 == null) return false;
    return n1.is$Equal(n2);
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:417
   */
  protected boolean is$Equal(ASTNode node) {
    if (getClass() != node.getClass()) {
      return false;
    }
    if (numChildren != node.numChildren) {
      return false;
    }
    for (int i = 0; i < numChildren; i++) {
      if (children[i] == null && node.children[i] != null) {
        return false;
      }
      if (!((ASTNode)children[i]).is$Equal(((ASTNode)node.children[i]))) {
        return false;
      }
    }
    return true;
  }
  /**
   * @aspect <NoAspect>
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/UserTest.jrag:2
   */
    protected void collect_contributors_Program_idDecls() {
    for (int i = 0; i < getNumChild(); i++) {
      getChild(i).collect_contributors_Program_idDecls();
    }
  }
  protected void contributeTo_Program_idDecls(Counter collection) {
  }

  /**
   * @apilevel internal
   */
  protected boolean numLocals_visited = false;
  /**
   * @apilevel internal
   */
  private void numLocals_reset() {
    numLocals_computed = false;
    numLocals_visited = false;
  }
  /**
   * @apilevel internal
   */
  protected boolean numLocals_computed = false;
  /**
   * @apilevel internal
   */
  protected int numLocals_value;

  /**
   * Local variable counting.
   * @attribute syn
   * @aspect CodeGen
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/CodeGen.jrag:176
   */
  @ASTNodeAnnotation.Attribute(kind=ASTNodeAnnotation.Kind.SYN)
  @ASTNodeAnnotation.Source(aspect="CodeGen", declaredAt="/home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/CodeGen.jrag:176")
  public int numLocals() {
    ASTNode$State state = state();
    if (numLocals_computed) {
      return numLocals_value;
    }
    if (numLocals_visited) {
      throw new RuntimeException("Circular definition of attr: numLocals in class: org.jastadd.ast.AST.SynDecl");
    }
    numLocals_visited = true;
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    numLocals_value = lastNode().localIndex() - localIndex();
    if (true) {
      numLocals_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;
    numLocals_visited = false;
    return numLocals_value;
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
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/CodeGen.jrag:181
   */
  @ASTNodeAnnotation.Attribute(kind=ASTNodeAnnotation.Kind.SYN)
  @ASTNodeAnnotation.Source(aspect="CodeGen", declaredAt="/home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/CodeGen.jrag:181")
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
    localIndex_value = prevNode().localIndex();
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
  protected boolean lastNode_visited = false;
  /**
   * @apilevel internal
   */
  private void lastNode_reset() {
    lastNode_computed = false;
    lastNode_value = null;
    lastNode_visited = false;
  }
  /**
   * @apilevel internal
   */
  protected boolean lastNode_computed = false;
  /**
   * @apilevel internal
   */
  protected ASTNode lastNode_value;

  /**
   * @attribute syn
   * @aspect CodeGen
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/CodeGen.jrag:187
   */
  @ASTNodeAnnotation.Attribute(kind=ASTNodeAnnotation.Kind.SYN)
  @ASTNodeAnnotation.Source(aspect="CodeGen", declaredAt="/home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/CodeGen.jrag:187")
  public ASTNode lastNode() {
    ASTNode$State state = state();
    if (lastNode_computed) {
      return lastNode_value;
    }
    if (lastNode_visited) {
      throw new RuntimeException("Circular definition of attr: lastNode in class: org.jastadd.ast.AST.SynDecl");
    }
    lastNode_visited = true;
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    lastNode_value = prevNode(getNumChild());
    if (true) {
      lastNode_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;
    lastNode_visited = false;
    return lastNode_value;
  }
  /**
   * @apilevel internal
   */
  protected Set prevNode_int_visited;
  /**
   * @apilevel internal
   */
  private void prevNode_int_reset() {
    prevNode_int_values = null;
    prevNode_int_visited = null;
  }
  /**
   * @apilevel internal
   */
  protected java.util.Map prevNode_int_values;
  /**
   * @attribute syn
   * @aspect CodeGen
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/CodeGen.jrag:188
   */
  @ASTNodeAnnotation.Attribute(kind=ASTNodeAnnotation.Kind.SYN)
  @ASTNodeAnnotation.Source(aspect="CodeGen", declaredAt="/home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/CodeGen.jrag:188")
  public ASTNode prevNode(int i) {
    Object _parameters = i;
    if (prevNode_int_visited == null) prevNode_int_visited = new java.util.HashSet(4);
    if (prevNode_int_values == null) prevNode_int_values = new java.util.HashMap(4);
    ASTNode$State state = state();
    if (prevNode_int_values.containsKey(_parameters)) {
      return (ASTNode) prevNode_int_values.get(_parameters);
    }
    if (prevNode_int_visited.contains(_parameters)) {
      throw new RuntimeException("Circular definition of attr: prevNode in class: org.jastadd.ast.AST.SynDecl");
    }
    prevNode_int_visited.add(_parameters);
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    ASTNode prevNode_int_value = i>0 ? getChild(i-1).lastNode() : this;
    if (true) {
      prevNode_int_values.put(_parameters, prevNode_int_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;
    prevNode_int_visited.remove(_parameters);
    return prevNode_int_value;
  }
  /**
   * @attribute inh
   * @aspect CodeGen
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/CodeGen.jrag:185
   */
  /**
   * @attribute inh
   * @aspect CodeGen
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/CodeGen.jrag:185
   */
  @ASTNodeAnnotation.Attribute(kind=ASTNodeAnnotation.Kind.INH)
  @ASTNodeAnnotation.Source(aspect="CodeGen", declaredAt="/home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/CodeGen.jrag:185")
  public ASTNode prevNode() {
    ASTNode$State state = state();
    if (prevNode_computed) {
      return prevNode_value;
    }
    if (prevNode_visited) {
      throw new RuntimeException("Circular definition of attr: prevNode in class: org.jastadd.ast.AST.InhDecl");
    }
    prevNode_visited = true;
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    prevNode_value = getParent().Define_prevNode(this, null);
    if (true) {
      prevNode_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;
    prevNode_visited = false;
    return prevNode_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean prevNode_visited = false;
  /**
   * @apilevel internal
   */
  private void prevNode_reset() {
    prevNode_computed = false;
    prevNode_value = null;
    prevNode_visited = false;
  }
  /**
   * @apilevel internal
   */
  protected boolean prevNode_computed = false;
  /**
   * @apilevel internal
   */
  protected ASTNode prevNode_value;

  /**
   * @attribute inh
   * @aspect Errors
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/Errors.jrag:28
   */
  /**
   * @attribute inh
   * @aspect Errors
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/Errors.jrag:28
   */
  @ASTNodeAnnotation.Attribute(kind=ASTNodeAnnotation.Kind.INH)
  @ASTNodeAnnotation.Source(aspect="Errors", declaredAt="/home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/Errors.jrag:28")
  public Program program() {
    ASTNode$State state = state();
    if (program_computed) {
      return program_value;
    }
    if (program_visited) {
      throw new RuntimeException("Circular definition of attr: program in class: org.jastadd.ast.AST.InhDecl");
    }
    program_visited = true;
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    program_value = getParent().Define_program(this, null);
    if (true) {
      program_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;
    program_visited = false;
    return program_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean program_visited = false;
  /**
   * @apilevel internal
   */
  private void program_reset() {
    program_computed = false;
    program_value = null;
    program_visited = false;
  }
  /**
   * @apilevel internal
   */
  protected boolean program_computed = false;
  /**
   * @apilevel internal
   */
  protected Program program_value;

  /**
   * @attribute inh
   * @aspect UnknownDecl
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/UnknownDecl.jrag:4
   */
  /**
   * @attribute inh
   * @aspect UnknownDecl
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/UnknownDecl.jrag:4
   */
  @ASTNodeAnnotation.Attribute(kind=ASTNodeAnnotation.Kind.INH)
  @ASTNodeAnnotation.Source(aspect="UnknownDecl", declaredAt="/home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/UnknownDecl.jrag:4")
  public UnknownDecl unknownDecl() {
    ASTNode$State state = state();
    if (unknownDecl_computed) {
      return unknownDecl_value;
    }
    if (unknownDecl_visited) {
      throw new RuntimeException("Circular definition of attr: unknownDecl in class: org.jastadd.ast.AST.InhDecl");
    }
    unknownDecl_visited = true;
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    unknownDecl_value = getParent().Define_unknownDecl(this, null);
    if (true) {
      unknownDecl_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;
    unknownDecl_visited = false;
    return unknownDecl_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean unknownDecl_visited = false;
  /**
   * @apilevel internal
   */
  private void unknownDecl_reset() {
    unknownDecl_computed = false;
    unknownDecl_value = null;
    unknownDecl_visited = false;
  }
  /**
   * @apilevel internal
   */
  protected boolean unknownDecl_computed = false;
  /**
   * @apilevel internal
   */
  protected UnknownDecl unknownDecl_value;

  /**
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/CodeGen.jrag:185
   * @apilevel internal
   */
  public ASTNode Define_prevNode(ASTNode _callerNode, ASTNode _childNode) {
    int i = this.getIndexOfChild(_callerNode);
    return prevNode(i);
  }
  protected boolean canDefine_prevNode(ASTNode _callerNode, ASTNode _childNode) {
    return true;
  }
  /**
   * @apilevel internal
   */
  public Program Define_program(ASTNode _callerNode, ASTNode _childNode) {
    ASTNode self = this;
    ASTNode parent = getParent();
    while (parent != null && !parent.canDefine_program(self, _callerNode)) {
      _callerNode = self;
      self = parent;
      parent = self.getParent();
    }
    return parent.Define_program(self, _callerNode);
  }

  /**
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/Errors.jrag:29
   * @apilevel internal
   * @return {@code true} if this node has an equation for the inherited attribute program
   */
  protected boolean canDefine_program(ASTNode _callerNode, ASTNode _childNode) {
    return false;
  }
  /**
   * @apilevel internal
   */
  public IdDecl Define_lookup(ASTNode _callerNode, ASTNode _childNode, String name) {
    ASTNode self = this;
    ASTNode parent = getParent();
    while (parent != null && !parent.canDefine_lookup(self, _callerNode, name)) {
      _callerNode = self;
      self = parent;
      parent = self.getParent();
    }
    return parent.Define_lookup(self, _callerNode, name);
  }

  /**
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/NameAnalysis.jrag:21
   * @apilevel internal
   * @return {@code true} if this node has an equation for the inherited attribute lookup
   */
  protected boolean canDefine_lookup(ASTNode _callerNode, ASTNode _childNode, String name) {
    return false;
  }
  /**
   * @apilevel internal
   */
  public boolean Define_inExprOf(ASTNode _callerNode, ASTNode _childNode, IdDecl decl) {
    ASTNode self = this;
    ASTNode parent = getParent();
    while (parent != null && !parent.canDefine_inExprOf(self, _callerNode, decl)) {
      _callerNode = self;
      self = parent;
      parent = self.getParent();
    }
    return parent.Define_inExprOf(self, _callerNode, decl);
  }

  /**
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/NameAnalysis.jrag:35
   * @apilevel internal
   * @return {@code true} if this node has an equation for the inherited attribute inExprOf
   */
  protected boolean canDefine_inExprOf(ASTNode _callerNode, ASTNode _childNode, IdDecl decl) {
    return false;
  }
  /**
   * @apilevel internal
   */
  public UnknownDecl Define_unknownDecl(ASTNode _callerNode, ASTNode _childNode) {
    ASTNode self = this;
    ASTNode parent = getParent();
    while (parent != null && !parent.canDefine_unknownDecl(self, _callerNode)) {
      _callerNode = self;
      self = parent;
      parent = self.getParent();
    }
    return parent.Define_unknownDecl(self, _callerNode);
  }

  /**
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/UnknownDecl.jrag:5
   * @apilevel internal
   * @return {@code true} if this node has an equation for the inherited attribute unknownDecl
   */
  protected boolean canDefine_unknownDecl(ASTNode _callerNode, ASTNode _childNode) {
    return false;
  }
}
