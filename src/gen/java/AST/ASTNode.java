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
 * @production ASTNode;

 */
public class ASTNode<T extends ASTNode> extends beaver.Symbol  implements Cloneable, Iterable<T> {
  /**
   * @aspect AddOptionsToProgram
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Options.jadd:14
   */
  public Options options() {
    return state().options;
  }
  /**
   * Pretty-print this ASTNode.
   * @return pretty-printed representation of this AST node
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:16
   */
  public String prettyPrint() {
    StringBuffer sb = new StringBuffer();
    prettyPrint(sb);
    return sb.toString().trim();
  }
  /**
   * Pretty print this AST node to the StringBuffer.
   * @param sb target for pretty printing
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:26
   */
  public void prettyPrint(StringBuffer sb) {
    throw new Error("Operation prettyPrint(StringBuffer) not implemented for " + getClass().getName());
  }
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:31
   */
  public String toString() {
    return getClass().getName();
  }
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:35
   */
  public void toString(StringBuffer sb) {
    prettyPrint(sb);
  }
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:818
   */
  public String dumpTree() {
    StringBuffer sb = new StringBuffer();
    dumpTree(sb, 0);
    return sb.toString();
  }
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:824
   */
  public void dumpTree(StringBuffer sb, int j) {
    for (int i = 0; i < j; i++) {
      sb.append("  ");
    }
    sb.append(dumpString() + "\n");
    for (int i = 0; i < getNumChild(); i++) {
      getChild(i).dumpTree(sb, j + 1);
    }
  }
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:834
   */
  public String dumpTreeNoRewrite() {
    StringBuffer sb = new StringBuffer();
    dumpTreeNoRewrite(sb, 0);
    return sb.toString();
  }
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:839
   */
  protected void dumpTreeNoRewrite(StringBuffer sb, int indent) {
    for (int i = 0; i < indent; i++) {
      sb.append("  ");
    }
    sb.append(dumpString());
    sb.append("\n");
    for (int i = 0; i < getNumChildNoTransform(); i++) {
      getChildNoTransform(i).dumpTreeNoRewrite(sb, indent+1);
    }
  }
  /**
   * @aspect StructuredPrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/StructuredPrettyPrint.jadd:30
   */
  public String structuredPrettyPrint() {
      StringBuffer sb = new StringBuffer();
      // first, transform the tree by wrapping all expressions in ParExpr
      wrapExprs();
      prettyPrint(sb);
      return sb.toString().trim();
  }
  /**
   * Hacky way of inserting parens around all expressions.
   * @aspect StructuredPrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/StructuredPrettyPrint.jadd:41
   */
  private void wrapExprs() {
    for (int i = 0; i < getNumChildNoTransform(); ++i) {
      ASTNode child = getChildNoTransform(i);
      if (child instanceof Expr &&
          !(child instanceof ParExpr) &&
          !(child instanceof Access) &&
          !(child instanceof Literal)) {
        child.setParent(null);
        ParExpr parExpr = new ParExpr((Expr) child);
        setChild(parExpr, i);
      }
      child.wrapExprs();
    }
  }
  /**
   * Checking of the SafeVarargs annotation is only needed for method
   * declarations.
   * @aspect Warnings
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Warnings.jadd:38
   */
  public void checkWarnings() {
	}
  /**
   * @aspect AccessControl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/AccessControl.jrag:125
   */
  public void accessControl() {
  }
  /**
   * @aspect AnonymousClasses
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/AnonymousClasses.jrag:96
   */
  protected void collectExceptions(Collection c, ASTNode target) {
    for(int i = 0; i < getNumChild(); i++)
      getChild(i).collectExceptions(c, target);
  }
  /**
   * @aspect BranchTarget
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BranchTarget.jrag:69
   */
  public void collectBranches(Collection<Stmt> c) {
    for (int i = 0; i < getNumChild(); i++) {
      getChild(i).collectBranches(c);
    }
  }
  /**
   * @aspect DeclareBeforeUse
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DeclareBeforeUse.jrag:21
   */
  public int varChildIndex(TypeDecl t) {
    ASTNode node = this;
    while(node != null && node.getParent() != null && node.getParent().getParent() != t) {
      node = node.getParent();
    }
    if(node == null)
      return -1;
    return t.getBodyDeclListNoTransform().getIndexOfChild(node);
  }
  /**
   * @aspect DefiniteAssignment
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:12
   */
  public void definiteAssignment() {
  }
  /**
   * @aspect DA
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:468
   */
  protected boolean checkDUeverywhere(Variable v) {
    for(int i = 0; i < getNumChild(); i++)
      if(!getChild(i).checkDUeverywhere(v))
        return false;
    return true;
  }
  /**
   * @aspect DA
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:582
   */
  protected boolean isDescendantTo(ASTNode node) {
    if(this == node)
      return true;
    if(getParent() == null)
      return false;
    return getParent().isDescendantTo(node);
  }
  /**
   * @aspect ErrorCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ErrorCheck.jrag:12
   */
  protected String sourceFile() {
    ASTNode node = this;
    while(node != null && !(node instanceof CompilationUnit))
      node = node.getParent();
    if(node == null)
      return "Unknown file";
    CompilationUnit u = (CompilationUnit)node;
    return u.relativeName();
  }
  /**
   * @aspect ErrorCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ErrorCheck.jrag:34
   */
  public ASTNode setLocation(ASTNode node) {
    setStart(node.getStart());
    setEnd(node.getEnd());
    return this;
  }
  /**
   * @aspect ErrorCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ErrorCheck.jrag:40
   */
  public ASTNode setStart(int i) {
    start = i;
    return this;
  }
  /**
   * @aspect ErrorCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ErrorCheck.jrag:44
   */
  public int start() {
    return start;
  }
  /**
   * @aspect ErrorCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ErrorCheck.jrag:47
   */
  public ASTNode setEnd(int i) {
    end = i;
    return this;
  }
  /**
   * @aspect ErrorCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ErrorCheck.jrag:51
   */
  public int end() {
    return end;
  }
  /**
   * @aspect ErrorCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ErrorCheck.jrag:55
   */
  public String location() {
    return "" + lineNumber();
  }
  /**
   * @aspect ErrorCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ErrorCheck.jrag:58
   */
  public String errorPrefix() {
    return sourceFile() + ":" + location() + ":\n" + "  *** Semantic Error: ";
  }
  /**
   * @aspect ErrorCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ErrorCheck.jrag:61
   */
  public String warningPrefix() {
    return sourceFile() + ":" + location() + ":\n" + "  *** WARNING: ";
  }
  /**
   * @aspect ErrorCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ErrorCheck.jrag:173
   */
  public void error(String s) {
    ASTNode node = this;
    while(node != null && !(node instanceof CompilationUnit))
      node = node.getParent();
    CompilationUnit cu = (CompilationUnit)node;
    if(getNumChild() == 0 && getStart() != 0 && getEnd() != 0) {
      int line = getLine(getStart());
      int column = getColumn(getStart());
      int endLine = getLine(getEnd());
      int endColumn = getColumn(getEnd());
      cu.errors.add(new Problem(sourceFile(), s, line, column, endLine, endColumn, Problem.Severity.ERROR, Problem.Kind.SEMANTIC));
    }
    else
      cu.errors.add(new Problem(sourceFile(), s, lineNumber(), Problem.Severity.ERROR, Problem.Kind.SEMANTIC));
  }
  /**
   * @aspect ErrorCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ErrorCheck.jrag:189
   */
  public void warning(String s) {
    ASTNode node = this;
    while(node != null && !(node instanceof CompilationUnit))
      node = node.getParent();
    CompilationUnit cu = (CompilationUnit)node;
    cu.warnings.add(new Problem(sourceFile(), "WARNING: " + s, lineNumber(), Problem.Severity.WARNING));
  }
  /**
   * @aspect ExceptionHandling
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ExceptionHandling.jrag:60
   */
  public void exceptionHandling() {
  }
  /**
   * @aspect ExceptionHandling
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ExceptionHandling.jrag:225
   */
  protected boolean reachedException(TypeDecl type) {
    for(int i = 0; i < getNumChild(); i++)
      if(getChild(i).reachedException(type))
        return true;
    return false;
  }
  /**
   * @aspect LookupMethod
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupMethod.jrag:74
   */
  public static Collection removeInstanceMethods(Collection c) {
    c = new LinkedList(c);
    for(Iterator iter = c.iterator(); iter.hasNext(); ) {
      MethodDecl m = (MethodDecl)iter.next();
      if(!m.isStatic())
        iter.remove();
    }
    return c;
  }
  /**
   * Utility method to put a SimpleSet-item in a signature map.
   * @aspect MemberMethods
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupMethod.jrag:444
   */
  protected static void putSimpleSetElement(Map<String,SimpleSet> map, String key, SimpleSet value) {
    SimpleSet set = map.get(key);
    if (set == null) {
      set = SimpleSet.emptySet;
    }
    map.put(key, set.add(value));
  }
  /**
   * @aspect VariableScope
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupVariable.jrag:214
   */
  public SimpleSet removeInstanceVariables(SimpleSet oldSet) {
    SimpleSet newSet = SimpleSet.emptySet;
    for(Iterator iter = oldSet.iterator(); iter.hasNext(); ) {
      Variable v = (Variable)iter.next();
      if(!v.isInstanceVariable())
        newSet = newSet.add(v);
    }
    return newSet;
  }
  /**
   * @aspect Modifiers
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:11
   */
  void checkModifiers() {
  }
  /**
   * @return a copy of the block as an NTAFinallyBlock
   * @aspect NTAFinally
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NTAFinally.jrag:33
   */
  protected static NTAFinallyBlock ntaFinallyBlock(FinallyHost origin, Stmt branch, Block block) {
    NTAFinallyBlock ntaBlock = new NTAFinallyBlock(origin);
    ntaBlock.addStmt((Block) block.fullCopy());
    /*if (!block.canCompleteNormally()) {
     * // the target block's exit will replace our own exit code
     * // so we can just goto that block!
     * ntaBlock.addGoto(block);
     * } else ...
     */
    if (block.canCompleteNormally()) {
      FinallyHost enclosing = block.enclosingFinally(branch);
      if (enclosing != null) {
        ntaBlock.addStmt(ntaFinallyBlock(enclosing, branch, enclosing.getFinallyBlock()));
      }
    }
    return ntaBlock;
  }
  /**
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:31
   */
  public void nameCheck() {
  }
  /**
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:34
   */
  public TypeDecl extractSingleType(SimpleSet c) {
    if(c.size() != 1)
      return null;
    return (TypeDecl)c.iterator().next();
  }
  /**
   * @aspect PrimitiveTypes
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrimitiveTypes.jrag:11
   */
  protected static final String PRIMITIVE_PACKAGE_NAME = "@primitive";
  /**
   * @aspect TypeCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeCheck.jrag:12
   */
  public void typeCheck() {
  }
  /**
   * @aspect UnreachableStatements
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/UnreachableStatements.jrag:12
   */
  void checkUnreachableStmt() {
  }
  /**
   * @aspect VariableDeclarationTransformation
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/VariableDeclaration.jrag:134
   */
  public void clearLocations() {
    setStart(0);
    setEnd(0);
    for(int i = 0; i < getNumChildNoTransform(); i++)
      getChildNoTransform(i).clearLocations();
  }
  /**
   * @aspect Enums
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Enums.jrag:134
   */
  protected void transformEnumConstructors() {
    for(int i = 0; i < getNumChildNoTransform(); i++) {
      ASTNode child = getChildNoTransform(i);
      if(child != null)
        child.transformEnumConstructors();
    }
  }
  /**
   * @aspect Enums
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Enums.jrag:447
   */
  protected void checkEnum(EnumDecl enumDecl) {
    for(int i = 0; i < getNumChild(); i++)
      getChild(i).checkEnum(enumDecl);
  }
  /**
   * @aspect UncheckedConversion
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/UncheckedConversion.jrag:40
   */
  public void checkUncheckedConversion(TypeDecl source, TypeDecl dest) {
    if (source.isUncheckedConversionTo(dest))
      warning("unchecked conversion from raw type "+source.typeName()+
        " to generic type "+dest.typeName());
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
      if(children[i] == node) {
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
  public static final boolean generatedWithCircularEnabled = true;
  /**
   * @apilevel internal
   * @declaredat ASTNode:44
   */
  public static final boolean generatedWithCacheCycle = false;
  /**
   * @apilevel internal
   * @declaredat ASTNode:48
   */
  public static final boolean generatedWithComponentCheck = false;
  /**
   * Parent pointer
   * @apilevel low-level
   * @declaredat ASTNode:54
   */
  protected ASTNode parent;
  /**
   * Child array
   * @apilevel low-level
   * @declaredat ASTNode:60
   */
  protected ASTNode[] children;
  /**
   * @apilevel internal
   * @declaredat ASTNode:66
   */
  protected static ASTNode$State state = new ASTNode$State();
  /**
   * @apilevel internal
   * @declaredat ASTNode:71
   */
  public final ASTNode$State state() {
    return state;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:78
   */
  public boolean in$Circle = false;
  /**
   * @apilevel internal
   * @declaredat ASTNode:83
   */
  public boolean in$Circle() {
    return in$Circle;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:90
   */
  public void in$Circle(boolean b) {
    in$Circle = b;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:97
   */
  public boolean is$Final = false;
  /**
   * @apilevel internal
   * @declaredat ASTNode:101
   */
  public boolean is$Final() { return is$Final; }
  /**
   * @apilevel internal
   * @declaredat ASTNode:105
   */
  public void is$Final(boolean b) { is$Final = b; }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:110
   */
  public T getChild(int i) {

    ASTNode node = this.getChildNoTransform(i);
    if(node == null) {
      return null;
    }
    if(node.is$Final()) {
      return (T) node;
    }
    if(!node.mayHaveRewrite()) {
      node.is$Final(this.is$Final());
      return (T) node;
    }
    if(!node.in$Circle()) {
      int rewriteState;
      int num = this.state().boundariesCrossed;
      do {
        this.state().push(ASTNode$State.REWRITE_CHANGE);
        ASTNode oldNode = node;
        oldNode.in$Circle(true);
        node = node.rewriteTo();
        if(node != oldNode) {
          this.setChild(node, i);
        }
        oldNode.in$Circle(false);
        rewriteState = this.state().pop();
      } while(rewriteState == ASTNode$State.REWRITE_CHANGE);
      if(rewriteState == ASTNode$State.REWRITE_NOCHANGE && this.is$Final()) {
        node.is$Final(true);
        this.state().boundariesCrossed = num;
      } else {
      }
    } else if(this.is$Final() != node.is$Final()) {
      this.state().boundariesCrossed++;
    } else {
    }
    return (T) node;


  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:153
   */
  public void addChild(T node) {
    setChild(node, getNumChildNoTransform());
  }
  /**
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @apilevel low-level
   * @declaredat ASTNode:160
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
   * @declaredat ASTNode:170
   */
  protected int numChildren;
  /**
   * @apilevel low-level
   * @declaredat ASTNode:175
   */
  protected int numChildren() {
    return numChildren;
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:182
   */
  public int getNumChild() {
    return numChildren();
  }
  /**
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @apilevel low-level
   * @declaredat ASTNode:190
   */
  public final int getNumChildNoTransform() {
    return numChildren();
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:196
   */
  public void setChild(ASTNode node, int i) {
    if(children == null) {
      children = new ASTNode[(i+1>4 || !(this instanceof List))?i+1:4];
    } else if (i >= children.length) {
      ASTNode c[] = new ASTNode[i << 1];
      System.arraycopy(children, 0, c, 0, children.length);
      children = c;
    }
    children[i] = node;
    if(i >= numChildren) {
      numChildren = i+1;
    }
    if(node != null) {
      node.setParent(this);
      node.childIndex = i;
    }
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:216
   */
  public void insertChild(ASTNode node, int i) {
    if(children == null) {
      children = new ASTNode[(i+1>4 || !(this instanceof List))?i+1:4];
      children[i] = node;
    } else {
      ASTNode c[] = new ASTNode[children.length + 1];
      System.arraycopy(children, 0, c, 0, i);
      c[i] = node;
      if(i < children.length) {
        System.arraycopy(children, i, c, i+1, children.length-i);
        for(int j = i+1; j < c.length; ++j) {
          if(c[j] != null) {
            c[j].childIndex = j;
          }
        }
      }
      children = c;
    }
    numChildren++;
    if(node != null) {
      node.setParent(this);
      node.childIndex = i;
    }
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:243
   */
  public void removeChild(int i) {
    if(children != null) {
      ASTNode child = (ASTNode) children[i];
      if(child != null) {
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
          if(children[j] != null) {
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
   * @declaredat ASTNode:272
   */
  public ASTNode getParent() {
    if(parent != null && ((ASTNode) parent).is$Final() != is$Final()) {
      state().boundariesCrossed++;
    }
    ;
    return (ASTNode) parent;
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:282
   */
  public void setParent(ASTNode node) {
    parent = node;
  }
  /**
   * @declaredat ASTNode:321
   */
  protected boolean duringBoundNames() {
  if(state().duringBoundNames == 0) {
    return false;
  } else {
    state().pop();
    state().push(ASTNode$State.REWRITE_INTERRUPT);
    return true;
  }
}
  /**
   * @declaredat ASTNode:330
   */
  protected boolean duringNameResolution() {
  if(state().duringNameResolution == 0) {
    return false;
  } else {
    state().pop();
    state().push(ASTNode$State.REWRITE_INTERRUPT);
    return true;
  }
}
  /**
   * @declaredat ASTNode:339
   */
  protected boolean duringSyntacticClassification() {
  if(state().duringSyntacticClassification == 0) {
    return false;
  } else {
    state().pop();
    state().push(ASTNode$State.REWRITE_INTERRUPT);
    return true;
  }
}
  /**
   * @declaredat ASTNode:348
   */
  protected boolean duringImplicitConstructor() {
  if(state().duringImplicitConstructor == 0) {
    return false;
  } else {
    state().pop();
    state().push(ASTNode$State.REWRITE_INTERRUPT);
    return true;
  }
}
  /**
   * @declaredat ASTNode:357
   */
  protected boolean duringVariableDeclarationTransformation() {
  if(state().duringVariableDeclarationTransformation == 0) {
    return false;
  } else {
    state().pop();
    state().push(ASTNode$State.REWRITE_INTERRUPT);
    return true;
  }
}
  /**
   * @declaredat ASTNode:366
   */
  protected boolean duringJava7Literals() {
  if(state().duringJava7Literals == 0) {
    return false;
  } else {
    state().pop();
    state().push(ASTNode$State.REWRITE_INTERRUPT);
    return true;
  }
}
  /**
   * @declaredat ASTNode:375
   */
  protected boolean duringDU() {
  if(state().duringDU == 0) {
    return false;
  } else {
    state().pop();
    state().push(ASTNode$State.REWRITE_INTERRUPT);
    return true;
  }
}
  /**
   * @declaredat ASTNode:384
   */
  protected boolean duringAnnotations() {
  if(state().duringAnnotations == 0) {
    return false;
  } else {
    state().pop();
    state().push(ASTNode$State.REWRITE_INTERRUPT);
    return true;
  }
}
  /**
   * @declaredat ASTNode:393
   */
  protected boolean duringEnums() {
  if(state().duringEnums == 0) {
    return false;
  } else {
    state().pop();
    state().push(ASTNode$State.REWRITE_INTERRUPT);
    return true;
  }
}
  /**
   * @declaredat ASTNode:402
   */
  protected boolean duringGenericTypeVariables() {
  if(state().duringGenericTypeVariables == 0) {
    return false;
  } else {
    state().pop();
    state().push(ASTNode$State.REWRITE_INTERRUPT);
    return true;
  }
}
  /**
   * @declaredat ASTNode:411
   */
  protected boolean duringMethodReference() {
  if(state().duringMethodReference == 0) {
    return false;
  } else {
    state().pop();
    state().push(ASTNode$State.REWRITE_INTERRUPT);
    return true;
  }
}
  /**
   * @apilevel low-level
   * @declaredat ASTNode:477
   */
  public java.util.Iterator<T> iterator() {
    return new java.util.Iterator<T>() {
      private int counter = 0;
      public boolean hasNext() {
        return counter < getNumChild();
      }
      public T next() {
        if(hasNext())
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
   * @apilevel internal
   * @declaredat ASTNode:497
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:503
   */
  public void flushTreeCache() {
    flushCache();
    if (children == null) {
      return;
    }
    for (int i = 0; i < children.length; i++) {
      if (children[i] != null && ((ASTNode)children[i]).is$Final) {
        ((ASTNode)children[i]).flushTreeCache();
      }
    }
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:517
   */
  public void flushCache() {
    flushAttrCache();
    flushCollectionCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:524
   */
  public void flushAttrCache() {
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:529
   */
  public void flushCollectionCache() {
  }
  /**
   * @api internal
   * @declaredat ASTNode:534
   */
  public void flushRewriteCache() {
    in$Circle(false);
    is$Final(false);
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:541
   */
  public ASTNode<T> clone() throws CloneNotSupportedException {
    ASTNode node = (ASTNode) super.clone();
    if (node.is$Final()) {
      node.flushCache();
    }
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:551
   */
  public ASTNode<T> copy() {
    try {
      ASTNode node = (ASTNode) clone();
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
   * @declaredat ASTNode:570
   */
  public ASTNode<T> fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:579
   */
  public ASTNode<T> treeCopyNoTransform() {
    ASTNode tree = (ASTNode) copy();
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
   * @declaredat ASTNode:599
   */
  public ASTNode<T> treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * Performs a full traversal of the tree using getChild to trigger rewrites
   * @apilevel low-level
   * @declaredat ASTNode:607
   */
  public void doFullTraversal() {
    for (int i = 0; i < getNumChild(); i++) {
      getChild(i).doFullTraversal();
    }
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:615
   */
  protected boolean is$Equal(ASTNode n1, ASTNode n2) {
    if (n1 == null && n2 == null) return true;
    if (n1 == null || n2 == null) return false;
    return n1.is$Equal(n2);
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:623
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
   * The collectErrors method is refined so that it calls
   * the checkWarnings method on each ASTNode to report
   * unchecked warnings.
   * @aspect Warnings
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Warnings.jadd:20
   */
    public void collectErrors() {
		nameCheck();
		typeCheck();
		accessControl();
		exceptionHandling();
		checkUnreachableStmt();
		definiteAssignment();
		checkModifiers();
		checkWarnings();
		for(int i = 0; i < getNumChild(); i++) {
			getChild(i).collectErrors();
		}
	}
  /**
   * @aspect <NoAspect>
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/LambdaBody.jrag:49
   */
    protected void collect_contributors_BlockLambdaBody_lambdaReturns() {
    for(int i = 0; i < getNumChild(); i++) {
      getChild(i).collect_contributors_BlockLambdaBody_lambdaReturns();
    }
  }
  protected void contributeTo_BlockLambdaBody_BlockLambdaBody_lambdaReturns(ArrayList<ReturnStmt> collection) {
  }

  /**
   * @attribute syn
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:792
   */
  @ASTNodeAnnotation.Attribute
  public String indent() {
    ASTNode$State state = state();
    try {
        String indent = extractIndent();
        return indent.startsWith("\n") ? indent : ("\n" + indent);
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:797
   */
  @ASTNodeAnnotation.Attribute
  public String extractIndent() {
    ASTNode$State state = state();
    try {
        if(getParent() == null)
          return "";
        String indent = getParent().extractIndent();
        if(getParent().addsIndentationLevel())
          indent += "  ";
        return indent;
      }
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public boolean addsIndentationLevel() {
    ASTNode$State state = state();
    boolean addsIndentationLevel_value = false;

    return addsIndentationLevel_value;
  }
  @ASTNodeAnnotation.Attribute
  public String dumpString() {
    ASTNode$State state = state();
    String dumpString_value = getClass().getName();

    return dumpString_value;
  }
  /**
   * @attribute syn
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:923
   */
  @ASTNodeAnnotation.Attribute
  public boolean usesTypeVariable() {
    ASTNode$State state = state();
    try {
        for(int i = 0; i < getNumChild(); i++)
          if(getChild(i).usesTypeVariable())
            return true;
        return false;
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect DU
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:1270
   */
  @ASTNodeAnnotation.Attribute
  public boolean unassignedEverywhere(Variable v, TryStmt stmt) {
    ASTNode$State state = state();
    try {
        for(int i = 0; i < getNumChild(); i++) {
          if(!getChild(i).unassignedEverywhere(v, stmt))
            return false;
        }
        return true;
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect ErrorCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ErrorCheck.jrag:22
   */
  @ASTNodeAnnotation.Attribute
  public int lineNumber() {
    ASTNode$State state = state();
    try {
        ASTNode n = this;
        while(n.getParent() != null && n.getStart() == 0) {
          n = n.getParent();
        }
        return getLine(n.getStart());
      }
    finally {
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    if(state().peek() == ASTNode$State.REWRITE_CHANGE) {
      state().pop();
      state().push(ASTNode$State.REWRITE_NOCHANGE);
    }
    return this;
  }  /**
   * @apilevel internal
   */
  public String Define_String_typeDeclIndent(ASTNode caller, ASTNode child) {
    return getParent().Define_String_typeDeclIndent(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_inExtendsOrImplements(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_inExtendsOrImplements(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeWildcard(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_typeWildcard(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_lookupWildcardExtends(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
    return getParent().Define_TypeDecl_lookupWildcardExtends(this, caller, typeDecl);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_lookupWildcardSuper(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
    return getParent().Define_TypeDecl_lookupWildcardSuper(this, caller, typeDecl);
  }
  /**
   * @apilevel internal
   */
  public LUBType Define_LUBType_lookupLUBType(ASTNode caller, ASTNode child, Collection bounds) {
    return getParent().Define_LUBType_lookupLUBType(this, caller, bounds);
  }
  /**
   * @apilevel internal
   */
  public GLBType Define_GLBType_lookupGLBType(ASTNode caller, ASTNode child, ArrayList bounds) {
    return getParent().Define_GLBType_lookupGLBType(this, caller, bounds);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_genericDecl(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_genericDecl(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_superType(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_superType(this, caller);
  }
  /**
   * @apilevel internal
   */
  public ConstructorDecl Define_ConstructorDecl_constructorDecl(ASTNode caller, ASTNode child) {
    return getParent().Define_ConstructorDecl_constructorDecl(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_componentType(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_componentType(this, caller);
  }
  /**
   * @apilevel internal
   */
  public LabeledStmt Define_LabeledStmt_lookupLabel(ASTNode caller, ASTNode child, String name) {
    return getParent().Define_LabeledStmt_lookupLabel(this, caller, name);
  }
  /**
   * @apilevel internal
   */
  public int Define_int_blockIndex(ASTNode caller, ASTNode child) {
    return getParent().Define_int_blockIndex(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_isDest(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_isSource(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_isIncOrDec(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_isIncOrDec(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
    return getParent().Define_boolean_isDAbefore(this, caller, v);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
    return getParent().Define_boolean_isDUbefore(this, caller, v);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeException(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_typeException(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeRuntimeException(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_typeRuntimeException(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeError(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_typeError(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeNullPointerException(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_typeNullPointerException(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeThrowable(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_typeThrowable(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
    return getParent().Define_boolean_handlesException(this, caller, exceptionType);
  }
  /**
   * @apilevel internal
   */
  public Collection Define_Collection_lookupConstructor(ASTNode caller, ASTNode child) {
    return getParent().Define_Collection_lookupConstructor(this, caller);
  }
  /**
   * @apilevel internal
   */
  public Collection Define_Collection_lookupSuperConstructor(ASTNode caller, ASTNode child) {
    return getParent().Define_Collection_lookupSuperConstructor(this, caller);
  }
  /**
   * @apilevel internal
   */
  public Expr Define_Expr_nestedScope(ASTNode caller, ASTNode child) {
    return getParent().Define_Expr_nestedScope(this, caller);
  }
  /**
   * @apilevel internal
   */
  public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
    return getParent().Define_Collection_lookupMethod(this, caller, name);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeObject(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_typeObject(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeCloneable(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_typeCloneable(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeSerializable(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_typeSerializable(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeBoolean(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_typeBoolean(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeByte(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_typeByte(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeShort(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_typeShort(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeChar(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_typeChar(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeInt(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_typeInt(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeLong(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_typeLong(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeFloat(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_typeFloat(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeDouble(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_typeDouble(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeString(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_typeString(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeVoid(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_typeVoid(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeNull(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_typeNull(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_unknownType(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_unknownType(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_hasPackage(ASTNode caller, ASTNode child, String packageName) {
    return getParent().Define_boolean_hasPackage(this, caller, packageName);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_lookupType(ASTNode caller, ASTNode child, String packageName, String typeName) {
    return getParent().Define_TypeDecl_lookupType(this, caller, packageName, typeName);
  }
  /**
   * @apilevel internal
   */
  public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
    return getParent().Define_SimpleSet_lookupType(this, caller, name);
  }
  /**
   * @apilevel internal
   */
  public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
    return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_mayBePublic(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_mayBePublic(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeProtected(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_mayBeProtected(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_mayBePrivate(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_mayBePrivate(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeStatic(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_mayBeStatic(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_mayBeFinal(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeAbstract(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_mayBeAbstract(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeVolatile(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_mayBeVolatile(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeTransient(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_mayBeTransient(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeStrictfp(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_mayBeStrictfp(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeSynchronized(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_mayBeSynchronized(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeNative(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_mayBeNative(this, caller);
  }
  /**
   * @apilevel internal
   */
  public ASTNode Define_ASTNode_enclosingBlock(ASTNode caller, ASTNode child) {
    return getParent().Define_ASTNode_enclosingBlock(this, caller);
  }
  /**
   * @apilevel internal
   */
  public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
    return getParent().Define_VariableScope_outerScope(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_insideLoop(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_insideLoop(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_insideSwitch(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_insideSwitch(this, caller);
  }
  /**
   * @apilevel internal
   */
  public Case Define_Case_bind(ASTNode caller, ASTNode child, Case c) {
    return getParent().Define_Case_bind(this, caller, c);
  }
  /**
   * @apilevel internal
   */
  public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
    return getParent().Define_NameType_nameType(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_isAnonymous(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_isAnonymous(this, caller);
  }
  /**
   * @apilevel internal
   */
  public Variable Define_Variable_unknownField(ASTNode caller, ASTNode child) {
    return getParent().Define_Variable_unknownField(this, caller);
  }
  /**
   * @apilevel internal
   */
  public MethodDecl Define_MethodDecl_unknownMethod(ASTNode caller, ASTNode child) {
    return getParent().Define_MethodDecl_unknownMethod(this, caller);
  }
  /**
   * @apilevel internal
   */
  public ConstructorDecl Define_ConstructorDecl_unknownConstructor(ASTNode caller, ASTNode child) {
    return getParent().Define_ConstructorDecl_unknownConstructor(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_declType(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_declType(this, caller);
  }
  /**
   * @apilevel internal
   */
  public BodyDecl Define_BodyDecl_enclosingBodyDecl(ASTNode caller, ASTNode child) {
    return getParent().Define_BodyDecl_enclosingBodyDecl(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_isMemberType(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_isMemberType(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_hostType(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_hostType(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_switchType(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_switchType(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_returnType(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_returnType(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_enclosingInstance(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_enclosingInstance(this, caller);
  }
  /**
   * @apilevel internal
   */
  public String Define_String_methodHost(ASTNode caller, ASTNode child) {
    return getParent().Define_String_methodHost(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_inExplicitConstructorInvocation(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_inExplicitConstructorInvocation(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_enclosingExplicitConstructorHostType(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_enclosingExplicitConstructorHostType(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_inStaticContext(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_reportUnreachable(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_isMethodParameter(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_isMethodParameter(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_isConstructorParameter(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_isConstructorParameter(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_isExceptionHandlerParameter(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_isExceptionHandlerParameter(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
    return getParent().Define_boolean_mayUseAnnotationTarget(this, caller, name);
  }
  /**
   * @apilevel internal
   */
  public ElementValue Define_ElementValue_lookupElementTypeValue(ASTNode caller, ASTNode child, String name) {
    return getParent().Define_ElementValue_lookupElementTypeValue(this, caller, name);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_withinSuppressWarnings(ASTNode caller, ASTNode child, String annot) {
    return getParent().Define_boolean_withinSuppressWarnings(this, caller, annot);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_withinDeprecatedAnnotation(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_withinDeprecatedAnnotation(this, caller);
  }
  /**
   * @apilevel internal
   */
  public Annotation Define_Annotation_lookupAnnotation(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
    return getParent().Define_Annotation_lookupAnnotation(this, caller, typeDecl);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_enclosingAnnotationDecl(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_enclosingAnnotationDecl(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_assignConvertedType(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_assignConvertedType(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_variableArityValid(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_variableArityValid(this, caller);
  }
  /**
   * @apilevel internal
   */
  public ClassInstanceExpr Define_ClassInstanceExpr_getClassInstanceExpr(ASTNode caller, ASTNode child) {
    return getParent().Define_ClassInstanceExpr_getClassInstanceExpr(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_isAnonymousDecl(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_isAnonymousDecl(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_isExplicitGenericConstructorAccess(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_isExplicitGenericConstructorAccess(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_isCatchParam(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_isCatchParam(this, caller);
  }
  /**
   * @apilevel internal
   */
  public CatchClause Define_CatchClause_catchClause(ASTNode caller, ASTNode child) {
    return getParent().Define_CatchClause_catchClause(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_resourcePreviouslyDeclared(ASTNode caller, ASTNode child, String name) {
    return getParent().Define_boolean_resourcePreviouslyDeclared(this, caller, name);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_targetType(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_targetType(this, caller);
  }
  /**
   * @apilevel internal
   */
  public CompilationUnit Define_CompilationUnit_compilationUnit(ASTNode caller, ASTNode child) {
    return getParent().Define_CompilationUnit_compilationUnit(this, caller);
  }
  /**
   * @apilevel internal
   */
  public SimpleSet Define_SimpleSet_allImportedTypes(ASTNode caller, ASTNode child, String name) {
    return getParent().Define_SimpleSet_allImportedTypes(this, caller, name);
  }
  /**
   * @apilevel internal
   */
  public String Define_String_packageName(ASTNode caller, ASTNode child) {
    return getParent().Define_String_packageName(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_enclosingType(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_enclosingType(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_isNestedType(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_isNestedType(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_isLocalClass(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_isLocalClass(this, caller);
  }
  /**
   * @apilevel internal
   */
  public String Define_String_hostPackage(ASTNode caller, ASTNode child) {
    return getParent().Define_String_hostPackage(this, caller);
  }
  /**
   * @apilevel internal
   */
  public LambdaExpr Define_LambdaExpr_enclosingLambda(ASTNode caller, ASTNode child) {
    return getParent().Define_LambdaExpr_enclosingLambda(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_assignmentContext(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_assignmentContext(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_invocationContext(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_invocationContext(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_castContext(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_castContext(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_stringContext(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_stringContext(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_numericContext(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_numericContext(this, caller);
  }
  /**
   * @apilevel internal
   */
  public int Define_int_typeVarPosition(ASTNode caller, ASTNode child) {
    return getParent().Define_int_typeVarPosition(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_typeVarInMethod(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_typeVarInMethod(this, caller);
  }
  /**
   * @apilevel internal
   */
  public int Define_int_genericMethodLevel(ASTNode caller, ASTNode child) {
    return getParent().Define_int_genericMethodLevel(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v, BodyDecl b) {
    return getParent().Define_boolean_isDAbefore(this, caller, v, b);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v, BodyDecl b) {
    return getParent().Define_boolean_isDUbefore(this, caller, v, b);
  }
  /**
   * @apilevel internal
   */
  public Stmt Define_Stmt_branchTarget(ASTNode caller, ASTNode child, Stmt branch) {
    return getParent().Define_Stmt_branchTarget(this, caller, branch);
  }
  /**
   * @apilevel internal
   */
  public FinallyHost Define_FinallyHost_enclosingFinally(ASTNode caller, ASTNode child, Stmt branch) {
    return getParent().Define_FinallyHost_enclosingFinally(this, caller, branch);
  }
  /**
   * @apilevel internal
   */
  public SimpleSet Define_SimpleSet_otherLocalClassDecls(ASTNode caller, ASTNode child, String name) {
    return getParent().Define_SimpleSet_otherLocalClassDecls(this, caller, name);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
    return getParent().Define_boolean_reachable(this, caller);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_inhModifiedInScope(ASTNode caller, ASTNode child, Variable var) {
    return getParent().Define_boolean_inhModifiedInScope(this, caller, var);
  }
  /**
   * @apilevel internal
   */
  public boolean Define_boolean_reachableCatchClause(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
    return getParent().Define_boolean_reachableCatchClause(this, caller, exceptionType);
  }
  /**
   * @apilevel internal
   */
  public Collection<TypeDecl> Define_Collection_TypeDecl__caughtExceptions(ASTNode caller, ASTNode child) {
    return getParent().Define_Collection_TypeDecl__caughtExceptions(this, caller);
  }
  /**
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_inferredType(ASTNode caller, ASTNode child) {
    return getParent().Define_TypeDecl_inferredType(this, caller);
  }
}
