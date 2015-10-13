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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:82
 * @production VariableDeclaration : {@link ASTNode} ::= <span class="component">{@link Modifiers}</span> <span class="component">TypeAccess:{@link Access}</span> <span class="component">&lt;ID:String&gt;</span> <span class="component">[Init:{@link Expr}]</span>;

 */
public class VariableDeclaration extends ASTNode<ASTNode> implements Cloneable, SimpleSet, Iterator, Variable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:205
   */
  public void prettyPrint(StringBuffer sb) {
    sb.append(indent());
    getModifiers().prettyPrint(sb);
    getTypeAccess().prettyPrint(sb);
    sb.append(" " + name());
    if(hasInit()) {
      sb.append(" = ");
      getInit().prettyPrint(sb);
    }
    sb.append(";");
  }
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DataStructures.jrag:99
   */
  public SimpleSet add(Object o) {
    return new SimpleSetImpl().add(this).add(o);
  }
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DataStructures.jrag:103
   */
  public boolean isSingleton() { return true; }
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DataStructures.jrag:104
   */
  public boolean isSingleton(Object o) { return contains(o); }
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DataStructures.jrag:107
   */
  private VariableDeclaration iterElem;
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DataStructures.jrag:108
   */
  public Iterator iterator() { iterElem = this; return this; }
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DataStructures.jrag:109
   */
  public boolean hasNext() { return iterElem != null; }
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DataStructures.jrag:110
   */
  public Object next() { Object o = iterElem; iterElem = null; return o; }
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DataStructures.jrag:111
   */
  public void remove() { throw new UnsupportedOperationException(); }
  /**
   * @aspect NodeConstructors
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NodeConstructors.jrag:74
   */
  public VariableDeclaration(Access type, String name, Expr init) {
    this(new Modifiers(new List()), type, name, new Opt(init));
  }
  /**
   * @aspect NodeConstructors
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NodeConstructors.jrag:78
   */
  public VariableDeclaration(Access type, String name) {
    this(new Modifiers(new List()), type, name, new Opt());
  }
  /**
   * @aspect TypeCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeCheck.jrag:22
   */
  public void typeCheck() {
    if (hasInit()) {
      TypeDecl source = getInit().type();
      TypeDecl dest = type();
      if (!source.assignConversionTo(dest, getInit())) {
        error("can not assign variable " + name() + " of type " +
            dest.typeName() + " a value of type " + source.typeName());
      }
    }
  }
  /**
   * @aspect UncheckedConversion
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/UncheckedConversion.jrag:20
   */
  public void checkWarnings() {
    if (hasInit() && !suppressWarnings("unchecked"))
      checkUncheckedConversion(getInit().type(), type());
  }
  /**
   * @declaredat ASTNode:1
   */
  public VariableDeclaration() {
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
    children = new ASTNode[3];
    setChild(new Opt(), 2);
  }
  /**
   * @declaredat ASTNode:14
   */
  public VariableDeclaration(Modifiers p0, Access p1, String p2, Opt<Expr> p3) {
    setChild(p0, 0);
    setChild(p1, 1);
    setID(p2);
    setChild(p3, 2);
  }
  /**
   * @declaredat ASTNode:20
   */
  public VariableDeclaration(Modifiers p0, Access p1, beaver.Symbol p2, Opt<Expr> p3) {
    setChild(p0, 0);
    setChild(p1, 1);
    setID(p2);
    setChild(p3, 2);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:29
   */
  protected int numChildren() {
    return 3;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:35
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:41
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    sourceVariableDecl_reset();
    isDAafter_Variable_reset();
    isDUafter_Variable_reset();
    constant_reset();
    throwTypes_reset();
    isEffectivelyFinal_reset();
    blockIndex_reset();
    enclosingLambda_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:55
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:61
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:67
   */
  public VariableDeclaration clone() throws CloneNotSupportedException {
    VariableDeclaration node = (VariableDeclaration) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:74
   */
  public VariableDeclaration copy() {
    try {
      VariableDeclaration node = (VariableDeclaration) clone();
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
   * @declaredat ASTNode:93
   */
  public VariableDeclaration fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:102
   */
  public VariableDeclaration treeCopyNoTransform() {
    VariableDeclaration tree = (VariableDeclaration) copy();
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
   * @declaredat ASTNode:122
   */
  public VariableDeclaration treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:129
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_ID == ((VariableDeclaration)node).tokenString_ID);    
  }
  /**
   * Replaces the Modifiers child.
   * @param node The new node to replace the Modifiers child.
   * @apilevel high-level
   */
  public void setModifiers(Modifiers node) {
    setChild(node, 0);
  }
  /**
   * Retrieves the Modifiers child.
   * @return The current node used as the Modifiers child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="Modifiers")
  public Modifiers getModifiers() {
    return (Modifiers) getChild(0);
  }
  /**
   * Retrieves the Modifiers child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the Modifiers child.
   * @apilevel low-level
   */
  public Modifiers getModifiersNoTransform() {
    return (Modifiers) getChildNoTransform(0);
  }
  /**
   * Replaces the TypeAccess child.
   * @param node The new node to replace the TypeAccess child.
   * @apilevel high-level
   */
  public void setTypeAccess(Access node) {
    setChild(node, 1);
  }
  /**
   * Retrieves the TypeAccess child.
   * @return The current node used as the TypeAccess child.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.Child(name="TypeAccess")
  public Access getTypeAccess() {
    return (Access) getChild(1);
  }
  /**
   * Retrieves the TypeAccess child.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The current node used as the TypeAccess child.
   * @apilevel low-level
   */
  public Access getTypeAccessNoTransform() {
    return (Access) getChildNoTransform(1);
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
    if(symbol.value != null && !(symbol.value instanceof String))
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
   * Replaces the optional node for the Init child. This is the <code>Opt</code>
   * node containing the child Init, not the actual child!
   * @param opt The new node to be used as the optional node for the Init child.
   * @apilevel low-level
   */
  public void setInitOpt(Opt<Expr> opt) {
    setChild(opt, 2);
  }
  /**
   * Replaces the (optional) Init child.
   * @param node The new node to be used as the Init child.
   * @apilevel high-level
   */
  public void setInit(Expr node) {
    getInitOpt().setChild(node, 0);
  }
  /**
   * Check whether the optional Init child exists.
   * @return {@code true} if the optional Init child exists, {@code false} if it does not.
   * @apilevel high-level
   */
  public boolean hasInit() {
    return getInitOpt().getNumChild() != 0;
  }
  /**
   * Retrieves the (optional) Init child.
   * @return The Init child, if it exists. Returns {@code null} otherwise.
   * @apilevel low-level
   */
  public Expr getInit() {
    return (Expr) getInitOpt().getChild(0);
  }
  /**
   * Retrieves the optional node for the Init child. This is the <code>Opt</code> node containing the child Init, not the actual child!
   * @return The optional node for child the Init child.
   * @apilevel low-level
   */
  @ASTNodeAnnotation.OptChild(name="Init")
  public Opt<Expr> getInitOpt() {
    return (Opt<Expr>) getChild(2);
  }
  /**
   * Retrieves the optional node for child Init. This is the <code>Opt</code> node containing the child Init, not the actual child!
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The optional node for child Init.
   * @apilevel low-level
   */
  public Opt<Expr> getInitOptNoTransform() {
    return (Opt<Expr>) getChildNoTransform(2);
  }
  /**
   * @aspect MultiCatch
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/MultiCatch.jrag:240
   */
   
  public void refined_MultiCatch_VariableDeclaration_nameCheck() {
    SimpleSet decls = outerScope().lookupVariable(name());
    for(Iterator iter = decls.iterator(); iter.hasNext(); ) {
      Variable var = (Variable)iter.next();
      if(var instanceof VariableDeclaration) {
        VariableDeclaration decl = (VariableDeclaration)var;
        if(decl != this && decl.enclosingBodyDecl() == enclosingBodyDecl())
          error("duplicate declaration of local variable " + name());
      }
      // 8.4.1
      else if(var instanceof ParameterDeclaration) {
        ParameterDeclaration decl = (ParameterDeclaration)var;
        if(decl.enclosingBodyDecl() == enclosingBodyDecl())
          error("duplicate declaration of local variable " + name());
      } else if(var instanceof CatchParameterDeclaration) {
        CatchParameterDeclaration decl = (CatchParameterDeclaration)var;
        if(decl.enclosingBodyDecl() == enclosingBodyDecl())
          error("duplicate declaration of local variable " + name());
      }
    }
    if(getParent().getParent() instanceof Block) {
      Block block = (Block)getParent().getParent();
      for(int i = 0; i < block.getNumStmt(); i++) {
        if(block.getStmt(i) instanceof Variable) {
          Variable v = (Variable)block.getStmt(i);
          if(v.name().equals(name()) && v != this) {
            error("duplicate declaration of local variable " + name());
          }
        }
      }
    }
  }
  /**
   * @aspect Java8NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/NameCheck.jrag:135
   */
   
	public void nameCheck() {
		SimpleSet decls = outerScope().lookupVariable(name());
		for(Iterator iter = decls.iterator(); iter.hasNext(); ) {
			Variable var = (Variable)iter.next();
			if(var instanceof VariableDeclaration) {
				VariableDeclaration decl = (VariableDeclaration)var;
				if(decl != this && decl.enclosingBodyDecl() == enclosingBodyDecl())
					error("duplicate declaration of local variable " + name());
			}
			// 8.4.1
			else if(var instanceof ParameterDeclaration) {
				ParameterDeclaration decl = (ParameterDeclaration)var;
				if(decl.enclosingBodyDecl() == enclosingBodyDecl())
					error("duplicate declaration of local variable " + name());
			} 
			else if(var instanceof CatchParameterDeclaration) {
				CatchParameterDeclaration decl = (CatchParameterDeclaration)var;
				if(decl.enclosingBodyDecl() == enclosingBodyDecl())
					error("duplicate declaration of local variable " + name());
			} 
			else if(var instanceof InferredParameterDeclaration) {
				InferredParameterDeclaration decl = (InferredParameterDeclaration)var;
				if(decl.enclosingBodyDecl() == enclosingBodyDecl())
					error("duplicate declaration of local variable " + name());
			}
		}
		if(getParent().getParent() instanceof Block) {
			Block block = (Block)getParent().getParent();
			for(int i = 0; i < block.getNumStmt(); i++) {
				if(block.getStmt(i) instanceof Variable) {
					Variable v = (Variable)block.getStmt(i);
					if(v.name().equals(name()) && v != this) {
						error("duplicate declaration of local variable " + name());
					}
				}
			}
		}
	}
  @ASTNodeAnnotation.Attribute
  public String dumpString() {
    ASTNode$State state = state();
    String dumpString_value = getClass().getName() + " [" + getID() + "]";

    return dumpString_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean sourceVariableDecl_computed = false;
  /**
   * @apilevel internal
   */
  protected Variable sourceVariableDecl_value;
/**
 * @apilevel internal
 */
private void sourceVariableDecl_reset() {
  sourceVariableDecl_computed = false;
  sourceVariableDecl_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Variable sourceVariableDecl() {
    if(sourceVariableDecl_computed) {
      return sourceVariableDecl_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    sourceVariableDecl_value = this;
    if (isFinal && num == state().boundariesCrossed) {
      sourceVariableDecl_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return sourceVariableDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public int size() {
    ASTNode$State state = state();
    int size_value = 1;

    return size_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isEmpty() {
    ASTNode$State state = state();
    boolean isEmpty_value = false;

    return isEmpty_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean contains(Object o) {
    ASTNode$State state = state();
    boolean contains_Object_value = this == o;

    return contains_Object_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isBlankFinal() {
    ASTNode$State state = state();
    boolean isBlankFinal_value = isFinal() && (!hasInit() || !getInit().isConstant());

    return isBlankFinal_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isValue() {
    ASTNode$State state = state();
    boolean isValue_value = isFinal() && hasInit() && getInit().isConstant();

    return isValue_value;
  }
  protected java.util.Map isDAafter_Variable_values;
/**
 * @apilevel internal
 */
private void isDAafter_Variable_reset() {
  isDAafter_Variable_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isDAafter(Variable v) {
    Object _parameters = v;
    if (isDAafter_Variable_values == null) isDAafter_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(isDAafter_Variable_values.containsKey(_parameters)) {
      return ((Boolean)isDAafter_Variable_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean isDAafter_Variable_value = isDAafter_compute(v);
    if (isFinal && num == state().boundariesCrossed) {
      isDAafter_Variable_values.put(_parameters, Boolean.valueOf(isDAafter_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDAafter_Variable_value;
  }
  /**
   * @apilevel internal
   */
  private boolean isDAafter_compute(Variable v) {
      if (v == this) {
        return hasInit();
      }
      return hasInit() ? getInit().isDAafter(v) : isDAbefore(v);
    }
  protected java.util.Map isDUafter_Variable_values;
/**
 * @apilevel internal
 */
private void isDUafter_Variable_reset() {
  isDUafter_Variable_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isDUafter(Variable v) {
    Object _parameters = v;
    if (isDUafter_Variable_values == null) isDUafter_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(isDUafter_Variable_values.containsKey(_parameters)) {
      return ((Boolean)isDUafter_Variable_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean isDUafter_Variable_value = isDUafter_compute(v);
    if (isFinal && num == state().boundariesCrossed) {
      isDUafter_Variable_values.put(_parameters, Boolean.valueOf(isDUafter_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDUafter_Variable_value;
  }
  /**
   * @apilevel internal
   */
  private boolean isDUafter_compute(Variable v) {
      if(v == this)
        return !hasInit();
      return hasInit() ? getInit().isDUafter(v) : isDUbefore(v);
    }
  @ASTNodeAnnotation.Attribute
  public boolean declaresVariable(String name) {
    ASTNode$State state = state();
    boolean declaresVariable_String_value = getID().equals(name);

    return declaresVariable_String_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isSynthetic() {
    ASTNode$State state = state();
    boolean isSynthetic_value = getModifiers().isSynthetic();

    return isSynthetic_value;
  }
  @ASTNodeAnnotation.Attribute
  public TypeDecl type() {
    ASTNode$State state = state();
    TypeDecl type_value = getTypeAccess().type();

    return type_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isParameter() {
    ASTNode$State state = state();
    boolean isParameter_value = false;

    return isParameter_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isClassVariable() {
    ASTNode$State state = state();
    boolean isClassVariable_value = false;

    return isClassVariable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isInstanceVariable() {
    ASTNode$State state = state();
    boolean isInstanceVariable_value = false;

    return isInstanceVariable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isMethodParameter() {
    ASTNode$State state = state();
    boolean isMethodParameter_value = false;

    return isMethodParameter_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isConstructorParameter() {
    ASTNode$State state = state();
    boolean isConstructorParameter_value = false;

    return isConstructorParameter_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isExceptionHandlerParameter() {
    ASTNode$State state = state();
    boolean isExceptionHandlerParameter_value = false;

    return isExceptionHandlerParameter_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isLocalVariable() {
    ASTNode$State state = state();
    boolean isLocalVariable_value = true;

    return isLocalVariable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isFinal() {
    ASTNode$State state = state();
    boolean isFinal_value = getModifiers().isFinal();

    return isFinal_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isVolatile() {
    ASTNode$State state = state();
    boolean isVolatile_value = getModifiers().isVolatile();

    return isVolatile_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isBlank() {
    ASTNode$State state = state();
    boolean isBlank_value = !hasInit();

    return isBlank_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isStatic() {
    ASTNode$State state = state();
    boolean isStatic_value = false;

    return isStatic_value;
  }
  @ASTNodeAnnotation.Attribute
  public String name() {
    ASTNode$State state = state();
    String name_value = getID();

    return name_value;
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
    constant_value = type().cast(getInit().constant());
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
  protected boolean throwTypes_computed = false;
  /**
   * @apilevel internal
   */
  protected Collection<TypeDecl> throwTypes_value;
/**
 * @apilevel internal
 */
private void throwTypes_reset() {
  throwTypes_computed = false;
  throwTypes_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Collection<TypeDecl> throwTypes() {
    if(throwTypes_computed) {
      return throwTypes_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    throwTypes_value = throwTypes_compute();
    if (isFinal && num == state().boundariesCrossed) {
      throwTypes_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return throwTypes_value;
  }
  /**
   * @apilevel internal
   */
  private Collection<TypeDecl> throwTypes_compute() {
      Collection<TypeDecl> tts = new LinkedList<TypeDecl>();
      tts.add(type());
      return tts;
    }
  @ASTNodeAnnotation.Attribute
  public boolean modifiedInScope(Variable var) {
    ASTNode$State state = state();
    boolean modifiedInScope_Variable_value = hasInit() && getInit().modifiedInScope(var);

    return modifiedInScope_Variable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean hasAnnotationSuppressWarnings(String annot) {
    ASTNode$State state = state();
    boolean hasAnnotationSuppressWarnings_String_value = getModifiers().hasAnnotationSuppressWarnings(annot);

    return hasAnnotationSuppressWarnings_String_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean suppressWarnings(String type) {
    ASTNode$State state = state();
    boolean suppressWarnings_String_value = hasAnnotationSuppressWarnings(type) || withinSuppressWarnings(type);

    return suppressWarnings_String_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isEffectivelyFinal_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isEffectivelyFinal_value;
/**
 * @apilevel internal
 */
private void isEffectivelyFinal_reset() {
  isEffectivelyFinal_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isEffectivelyFinal() {
    if(isEffectivelyFinal_computed) {
      return isEffectivelyFinal_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isEffectivelyFinal_value = isEffectivelyFinal_compute();
    if (isFinal && num == state().boundariesCrossed) {
      isEffectivelyFinal_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isEffectivelyFinal_value;
  }
  /**
   * @apilevel internal
   */
  private boolean isEffectivelyFinal_compute() {
  		return !isFinal() && !inhModifiedInScope(this);
  	}
  /**
   * @attribute inh
   * @aspect DeclareBeforeUse
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DeclareBeforeUse.jrag:13
   */
  @ASTNodeAnnotation.Attribute
  public int blockIndex() {
    if(blockIndex_computed) {
      return blockIndex_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    blockIndex_value = getParent().Define_int_blockIndex(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      blockIndex_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return blockIndex_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean blockIndex_computed = false;
  /**
   * @apilevel internal
   */
  protected int blockIndex_value;
/**
 * @apilevel internal
 */
private void blockIndex_reset() {
  blockIndex_computed = false;
}  
  /**
   * @attribute inh
   * @aspect DA
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:234
   */
  @ASTNodeAnnotation.Attribute
  public boolean isDAbefore(Variable v) {
    ASTNode$State state = state();
    boolean isDAbefore_Variable_value = getParent().Define_boolean_isDAbefore(this, null, v);

    return isDAbefore_Variable_value;
  }
  /**
   * @attribute inh
   * @aspect DU
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:714
   */
  @ASTNodeAnnotation.Attribute
  public boolean isDUbefore(Variable v) {
    ASTNode$State state = state();
    boolean isDUbefore_Variable_value = getParent().Define_boolean_isDUbefore(this, null, v);

    return isDUbefore_Variable_value;
  }
  /**
   * @attribute inh
   * @aspect VariableScope
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupVariable.jrag:21
   */
  @ASTNodeAnnotation.Attribute
  public SimpleSet lookupVariable(String name) {
    ASTNode$State state = state();
    SimpleSet lookupVariable_String_value = getParent().Define_SimpleSet_lookupVariable(this, null, name);

    return lookupVariable_String_value;
  }
  /**
   * @attribute inh
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:320
   */
  @ASTNodeAnnotation.Attribute
  public VariableScope outerScope() {
    ASTNode$State state = state();
    VariableScope outerScope_value = getParent().Define_VariableScope_outerScope(this, null);

    return outerScope_value;
  }
  /**
   * @attribute inh
   * @aspect NestedTypes
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:523
   */
  @ASTNodeAnnotation.Attribute
  public BodyDecl enclosingBodyDecl() {
    ASTNode$State state = state();
    BodyDecl enclosingBodyDecl_value = getParent().Define_BodyDecl_enclosingBodyDecl(this, null);

    return enclosingBodyDecl_value;
  }
  /**
   * @attribute inh
   * @aspect NestedTypes
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:596
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl hostType() {
    ASTNode$State state = state();
    TypeDecl hostType_value = getParent().Define_TypeDecl_hostType(this, null);

    return hostType_value;
  }
  /**
   * @attribute inh
   * @aspect SuppressWarnings
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/SuppressWarnings.jrag:13
   */
  @ASTNodeAnnotation.Attribute
  public boolean withinSuppressWarnings(String annot) {
    ASTNode$State state = state();
    boolean withinSuppressWarnings_String_value = getParent().Define_boolean_withinSuppressWarnings(this, null, annot);

    return withinSuppressWarnings_String_value;
  }
  /**
   * @attribute inh
   * @aspect TryWithResources
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/TryWithResources.jrag:144
   */
  @ASTNodeAnnotation.Attribute
  public boolean resourcePreviouslyDeclared(String name) {
    ASTNode$State state = state();
    boolean resourcePreviouslyDeclared_String_value = getParent().Define_boolean_resourcePreviouslyDeclared(this, null, name);

    return resourcePreviouslyDeclared_String_value;
  }
  /**
   * @attribute inh
   * @aspect PreciseRethrow
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/EffectivelyFinal.jrag:32
   */
  @ASTNodeAnnotation.Attribute
  public boolean inhModifiedInScope(Variable var) {
    ASTNode$State state = state();
    boolean inhModifiedInScope_Variable_value = getParent().Define_boolean_inhModifiedInScope(this, null, var);

    return inhModifiedInScope_Variable_value;
  }
  /**
   * @attribute inh
   * @aspect EnclosingLambda
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/EnclosingLambda.jrag:36
   */
  @ASTNodeAnnotation.Attribute
  public LambdaExpr enclosingLambda() {
    if(enclosingLambda_computed) {
      return enclosingLambda_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    enclosingLambda_value = getParent().Define_LambdaExpr_enclosingLambda(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      enclosingLambda_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return enclosingLambda_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean enclosingLambda_computed = false;
  /**
   * @apilevel internal
   */
  protected LambdaExpr enclosingLambda_value;
/**
 * @apilevel internal
 */
private void enclosingLambda_reset() {
  enclosingLambda_computed = false;
  enclosingLambda_value = null;
}  
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:40
   * @apilevel internal
   */
  public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
    if (caller == getInitOptNoTransform()) {
      return true;
    }
    else {
      return getParent().Define_boolean_isSource(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:519
   * @apilevel internal
   */
  public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
    if (caller == getInitOptNoTransform()) {
      return isDAbefore(v);
    }
    else {
      return getParent().Define_boolean_isDAbefore(this, caller, v);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:939
   * @apilevel internal
   */
  public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
    if (caller == getInitOptNoTransform()) {
      return isDUbefore(v);
    }
    else {
      return getParent().Define_boolean_isDUbefore(this, caller, v);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:286
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
    if (caller == getModifiersNoTransform()) {
      return true;
    }
    else {
      return getParent().Define_boolean_mayBeFinal(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/SyntacticClassification.jrag:85
   * @apilevel internal
   */
  public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
    if (caller == getTypeAccessNoTransform()) {
      return NameType.TYPE_NAME;
    }
    else {
      return getParent().Define_NameType_nameType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:263
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_declType(ASTNode caller, ASTNode child) {
    if (caller == getInitOptNoTransform()) {
      return type();
    }
    else {
      return getParent().Define_TypeDecl_declType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:92
   * @apilevel internal
   */
  public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
    if (caller == getModifiersNoTransform()) {
      return name.equals("LOCAL_VARIABLE");
    }
    else {
      return getParent().Define_boolean_mayUseAnnotationTarget(this, caller, name);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericMethodsInference.jrag:34
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_assignConvertedType(ASTNode caller, ASTNode child) {
    if (caller == getInitOptNoTransform()) {
      return type();
    }
    else {
      return getParent().Define_TypeDecl_assignConvertedType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:39
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_targetType(ASTNode caller, ASTNode child) {
    if (caller == getInitOptNoTransform()) {
      return getTypeAccess().type();
    }
    else {
      return getParent().Define_TypeDecl_targetType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:356
   * @apilevel internal
   */
  public boolean Define_boolean_assignmentContext(ASTNode caller, ASTNode child) {
    if (caller == getInitOptNoTransform()) {
      return true;
    }
    else {
      return getParent().Define_boolean_assignmentContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:357
   * @apilevel internal
   */
  public boolean Define_boolean_invocationContext(ASTNode caller, ASTNode child) {
    if (caller == getInitOptNoTransform()) {
      return false;
    }
    else {
      return getParent().Define_boolean_invocationContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:358
   * @apilevel internal
   */
  public boolean Define_boolean_castContext(ASTNode caller, ASTNode child) {
    if (caller == getInitOptNoTransform()) {
      return false;
    }
    else {
      return getParent().Define_boolean_castContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:359
   * @apilevel internal
   */
  public boolean Define_boolean_numericContext(ASTNode caller, ASTNode child) {
    if (caller == getInitOptNoTransform()) {
      return false;
    }
    else {
      return getParent().Define_boolean_numericContext(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:360
   * @apilevel internal
   */
  public boolean Define_boolean_stringContext(ASTNode caller, ASTNode child) {
    if (caller == getInitOptNoTransform()) {
      return false;
    }
    else {
      return getParent().Define_boolean_stringContext(this, caller);
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
