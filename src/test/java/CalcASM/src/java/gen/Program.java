/* This file was generated with JastAdd2 (http://jastadd.org) version 2.1.13-47-g075c2ed */
package CalcASM.src.java.gen;

import java.io.PrintStream;
import java.util.Set;
/**
 * @ast node
 * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/calc.ast:1
 * @production Program : {@link ASTNode} ::= <span class="component">{@link Expr}</span>;

 */
public class Program extends ASTNode<ASTNode> implements Cloneable {
  /**
   * @aspect CodeGen
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/CodeGen.jrag:6
   */
  public void genCode(PrintStream out) {
		out.println(".global _start");
		out.println(".data");
		out.println("ask_message: .ascii \"Please enter a number: \"");
		out.println("ask_msg_len: .quad 23");
		out.println("buf: .skip 1024");
		out.println();
		out.println(".text");
		out.println("_start:");

		// allocate space for local variables (bindings):
		out.println("        pushq %rbp");
		out.println("        movq %rsp, %rbp");
		out.println("        subq $" + (getExpr().numLocals()*8) + ", %rsp");

		getExpr().genEval(out);// stores result in RAX


		// print result:
		out.println("        pushq %rax");
		out.println("        call print");
		out.println("        addq $8, %rsp");

		// de-allocate local variables:
		out.println("        movq %rbp, %rsp");
		out.println("        popq %rbp");

		// call sys_exit:
		out.println("        movq $0, %rdi");
		out.println("        movq $60, %rax");
		out.println("        syscall"); // done!

		// helper functions
		out.println("# Procedure to read number from stdin");
		out.println("# C signature: long int read(void)");
		out.println("read:");
		out.println("        pushq %rbp");
		out.println("        movq %rsp, %rbp");
		out.println("        movq $0, %rdi");
		out.println("        movq $buf, %rsi");
		out.println("        movq $1024, %rdx");
		out.println("        movq $0, %rax");
		out.println("        syscall                 # %rax = sys_read(0, buf, 1024)");
		out.println("        ### convert string to integer:");
		out.println("        ### %rax contains nchar");
		out.println("        ### %rsi contains ptr");
		out.println("        movq $0, %rdx           # sum = 0");
		out.println("atoi_loop:");
		out.println("        cmpq $0, %rax           # while (nchar > 0)");
		out.println("        jle atoi_done           # leave loop if nchar <= 0");
		out.println("        movzbq (%rsi), %rbx     # move byte, and sign extend to qword");
		out.println("        cmpq $0x30, %rbx        # test if < '0'");
		out.println("        jl atoi_done            # character is not numeric");
		out.println("        cmpq $0x39, %rbx        # test if > '9'");
		out.println("        jg atoi_done            # character is not numeric");
		out.println("        imulq $10, %rdx         # multiply sum by 10");
		out.println("        subq $0x30, %rbx        # value of character");
		out.println("        addq %rbx, %rdx         # add to sum");
		out.println("        incq %rsi               # step to next char");
		out.println("        decq %rax               # nchar--");
		out.println("        jmp atoi_loop           # loop back");
		out.println("atoi_done:");
		out.println("        movq %rdx, %rax         # return value in RAX");
		out.println("        popq %rbp");
		out.println("        ret");
		out.println();
		out.println("# Procedure to print number to stdout");
		out.println("# C signature: void print(long int)");
		out.println("print:");
		out.println("        pushq %rbp");
		out.println("        movq %rsp, %rbp");
		out.println("        ### convert integer to string");
		out.println("        movq 16(%rbp), %rax     # parameter");
		out.println("        movq $(buf+1023), %rsi  # write ptr (start from end of buf)");
		out.println("        movb $0x0a, (%rsi)      # insert newline");
		out.println("        movq $1, %rcx           # string length");
		out.println("itoa_loop:                      # do.. while (at least one iteration)");
		out.println("        movq $10, %rbx");
		out.println("        movq $0, %rdx");
		out.println("        idivq %rbx              # divide rdx:rax by 10");
		out.println("        addb $0x30, %dl         # remainder + '0'");
		out.println("        decq %rsi               # move string pointer");
		out.println("        movb %dl, (%rsi)");
		out.println("        incq %rcx               # increment string length");
		out.println("        cmpq $0, %rax");
		out.println("        jg itoa_loop            # produce more digits");
		out.println("itoa_done:");
		out.println("        movq $1, %rdi");
		out.println("        movq %rcx, %rdx");
		out.println("        movq $1, %rax");
		out.println("        syscall");
		out.println("        popq %rbp");
		out.println("        ret");
		out.println();
		out.println("print_string:");
		out.println("        pushq %rbp");
		out.println("        movq %rsp, %rbp");
		out.println("        movq $1, %rdi");
		out.println("        movq 16(%rbp), %rsi");
		out.println("        movq 24(%rbp), %rdx");
		out.println("        movq $1, %rax");
		out.println("        syscall");
		out.println("        popq %rbp");
		out.println("        ret");
	}
  /**
   * @declaredat ASTNode:1
   */
  public Program() {
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
  }
  /**
   * @declaredat ASTNode:13
   */
  public Program(Expr p0) {
    setChild(p0, 0);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:19
   */
  protected int numChildren() {
    return 1;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:25
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    localIndex_reset();
    unknownDecl_reset();
    nodeCount_reset();
    getIdDecl_IdUse_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:35
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
    Program_idDecls_visited = false;
    Program_idDecls_computed = false;
    Program_idDecls_value = null;
    Program_idDecls_contributors = null;
    collect_contributors_Program_idDecls = false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:46
   */
  public Program clone() throws CloneNotSupportedException {
    Program node = (Program) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:53
   */
  public Program copy() {
    try {
      Program node = (Program) clone();
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
   * @declaredat ASTNode:72
   */
  @Deprecated
  public Program fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:82
   */
  public Program treeCopyNoTransform() {
    Program tree = (Program) copy();
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
   * @declaredat ASTNode:102
   */
  public Program treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:109
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node);    
  }
  /**
   * Replaces the Expr child.
   * @param node The new node to replace the Expr child.
   * @apilevel high-level
   */
  public void setExpr(Expr node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the Expr child.
   * @return The current node used as the Expr child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Expr")
  public Expr getExpr() {
    return (Expr) getChild(0);
  }
  /**
   * Retrieves the Expr child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Expr child.
   * @apilevel low-level
   */
  public Expr getExprNoTransform() {
    return (Expr) getChildNoTransform(0);
  }
  /**
   * @aspect <NoAspect>
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/UserTest.jrag:2
   */
    private boolean collect_contributors_Program_idDecls = false;
  protected void collect_contributors_Program_idDecls() {
    if (collect_contributors_Program_idDecls) {
      return;
    }
    super.collect_contributors_Program_idDecls();
    collect_contributors_Program_idDecls = true;
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
    localIndex_value = 0;
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
   * @attribute syn
   * @aspect UnknownDecl
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/UnknownDecl.jrag:2
   */
  @ASTNodeAnnotation.Attribute(kind=ASTNodeAnnotation.Kind.SYN, isNTA=true)
  @ASTNodeAnnotation.Source(aspect="UnknownDecl", declaredAt="/home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/UnknownDecl.jrag:2")
  public UnknownDecl unknownDecl() {
    ASTNode$State state = state();
    if (unknownDecl_computed) {
      return unknownDecl_value;
    }
    if (unknownDecl_visited) {
      throw new RuntimeException("Circular definition of attr: unknownDecl in class: org.jastadd.ast.AST.SynDecl");
    }
    unknownDecl_visited = true;
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    unknownDecl_value = new UnknownDecl("<unknown>");
    unknownDecl_value.setParent(this);
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
  protected boolean nodeCount_visited = false;
  /**
   * @apilevel internal
   */
  private void nodeCount_reset() {
    nodeCount_computed = false;
    nodeCount_visited = false;
  }
  /**
   * @apilevel internal
   */
  protected boolean nodeCount_computed = false;
  /**
   * @apilevel internal
   */
  protected int nodeCount_value;

  /**
   * @attribute syn
   * @aspect UserTest
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/UserTest.jrag:7
   */
  @ASTNodeAnnotation.Attribute(kind=ASTNodeAnnotation.Kind.SYN)
  @ASTNodeAnnotation.Source(aspect="UserTest", declaredAt="/home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/UserTest.jrag:7")
  public int nodeCount() {
    ASTNode$State state = state();
    if (nodeCount_computed) {
      return nodeCount_value;
    }
    if (nodeCount_visited) {
      throw new RuntimeException("Circular definition of attr: nodeCount in class: org.jastadd.ast.AST.SynDecl");
    }
    nodeCount_visited = true;
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    nodeCount_value = idDecls().value();
    if (true) {
      nodeCount_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;
    nodeCount_visited = false;
    return nodeCount_value;
  }
  /**
   * @apilevel internal
   */
  protected Set getIdDecl_IdUse_visited;
  /**
   * @apilevel internal
   */
  private void getIdDecl_IdUse_reset() {
    getIdDecl_IdUse_values = null;
    getIdDecl_IdUse_visited = null;
  }
  /**
   * @apilevel internal
   */
  protected java.util.Map getIdDecl_IdUse_values;
  /**
   * @attribute syn
   * @aspect UserTest
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/UserTest.jrag:9
   */
  @ASTNodeAnnotation.Attribute(kind=ASTNodeAnnotation.Kind.SYN)
  @ASTNodeAnnotation.Source(aspect="UserTest", declaredAt="/home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/UserTest.jrag:9")
  public IdDecl getIdDecl(IdUse idUse) {
    Object _parameters = idUse;
    if (getIdDecl_IdUse_visited == null) getIdDecl_IdUse_visited = new java.util.HashSet(4);
    if (getIdDecl_IdUse_values == null) getIdDecl_IdUse_values = new java.util.HashMap(4);
    ASTNode$State state = state();
    if (getIdDecl_IdUse_values.containsKey(_parameters)) {
      return (IdDecl) getIdDecl_IdUse_values.get(_parameters);
    }
    if (getIdDecl_IdUse_visited.contains(_parameters)) {
      throw new RuntimeException("Circular definition of attr: getIdDecl in class: org.jastadd.ast.AST.SynDecl");
    }
    getIdDecl_IdUse_visited.add(_parameters);
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    IdDecl getIdDecl_IdUse_value = idUse.decl();
    if (true) {
      getIdDecl_IdUse_values.put(_parameters, getIdDecl_IdUse_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;
    getIdDecl_IdUse_visited.remove(_parameters);
    return getIdDecl_IdUse_value;
  }
  /**
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/Errors.jrag:28
   * @apilevel internal
   */
  public Program Define_program(ASTNode _callerNode, ASTNode _childNode) {
    int childIndex = this.getIndexOfChild(_callerNode);
    return this;
  }
  protected boolean canDefine_program(ASTNode _callerNode, ASTNode _childNode) {
    return true;
  }
  /**
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/NameAnalysis.jrag:26
   * @apilevel internal
   */
  public IdDecl Define_lookup(ASTNode _callerNode, ASTNode _childNode, String name) {
    int childIndex = this.getIndexOfChild(_callerNode);
    return unknownDecl();
  }
  protected boolean canDefine_lookup(ASTNode _callerNode, ASTNode _childNode, String name) {
    return true;
  }
  /**
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/NameAnalysis.jrag:34
   * @apilevel internal
   */
  public boolean Define_inExprOf(ASTNode _callerNode, ASTNode _childNode, IdDecl decl) {
    int childIndex = this.getIndexOfChild(_callerNode);
    return false;
  }
  protected boolean canDefine_inExprOf(ASTNode _callerNode, ASTNode _childNode, IdDecl decl) {
    return true;
  }
  /**
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/UnknownDecl.jrag:4
   * @apilevel internal
   */
  public UnknownDecl Define_unknownDecl(ASTNode _callerNode, ASTNode _childNode) {
    int childIndex = this.getIndexOfChild(_callerNode);
    return unknownDecl();
  }
  protected boolean canDefine_unknownDecl(ASTNode _callerNode, ASTNode _childNode) {
    return true;
  }
  /**
   * @attribute coll
   * @aspect UserTest
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/UserTest.jrag:2
   */
  /**
   * @attribute coll
   * @aspect UserTest
   * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/UserTest.jrag:2
   */
  @ASTNodeAnnotation.Attribute(kind=ASTNodeAnnotation.Kind.COLL)
  @ASTNodeAnnotation.Source(aspect="UserTest", declaredAt="/home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/UserTest.jrag:2")
  public Counter idDecls() {
    ASTNode$State state = state();
    if (Program_idDecls_computed) {
      return Program_idDecls_value;
    }
    if (Program_idDecls_visited) {
      throw new RuntimeException("Circular definition of attr: idDecls in class: org.jastadd.ast.AST.CollDecl");
    }
    Program_idDecls_visited = true;
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    Program_idDecls_value = idDecls_compute();
    if (true) {
      Program_idDecls_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;
    Program_idDecls_visited = false;
    return Program_idDecls_value;
  }
  java.util.Collection Program_idDecls_contributors;

  /**
   * @apilevel internal
   * @return the contributor set for idDecls
   */
  public java.util.Collection Program_idDecls_contributors() {
    if (Program_idDecls_contributors == null)
      Program_idDecls_contributors  = new java.util.LinkedList();
    return Program_idDecls_contributors;
  }

  /**
   * @apilevel internal
   */
  private Counter idDecls_compute() {
    ASTNode node = this;
    while (node != null && !(node instanceof Program)) {
      node = node.getParent();
    }
    Program root = (Program) node;
    root.collect_contributors_Program_idDecls();
    Counter _computedValue = new Counter();
    if (Program_idDecls_contributors != null) {
      for (java.util.Iterator iter = Program_idDecls_contributors.iterator(); iter.hasNext(); ) {
        ASTNode contributor = (ASTNode) iter.next();
        contributor.contributeTo_Program_idDecls(_computedValue);
      }
    }
    return _computedValue;
  }
  /**
   * @apilevel internal
   */
  protected boolean Program_idDecls_visited = false;
  /**
   * @apilevel internal
   */
  private void Program_idDecls_reset() {
    Program_idDecls_computed = false;
    Program_idDecls_value = null;
    Program_idDecls_visited = false;
  }
  /**
   * @apilevel internal
   */
  protected boolean Program_idDecls_computed = false;
  /**
   * @apilevel internal
   */
  protected Counter Program_idDecls_value;

}
