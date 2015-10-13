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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:18
 * @production VarAccess : {@link Access} ::= <span class="component">&lt;ID:String&gt;</span>;

 */
public class VarAccess extends Access implements Cloneable {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:497
   */
  public void prettyPrint(StringBuffer sb) {
    sb.append(name());
  }
  /**
   * @aspect DefiniteAssignment
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:95
   */
  public void definiteAssignment() {
    if(isSource()) {
      if(decl() instanceof VariableDeclaration) {
        VariableDeclaration v = (VariableDeclaration) decl();
        //System.err.println("Is " + v + " final? " + v.isFinal() + ", DAbefore: " + isDAbefore(v));
        if(v.isValue()) {
        }
        else if(v.isBlankFinal()) {
          //if(!isDAbefore(v) && !v.hasInit() && !v.getInit().isConstant())
          if(!isDAbefore(v))
            error("Final variable " + v.name() + " is not assigned before used");
        }
        else {
          //if(!v.hasInit() && !isDAbefore(v)) {
          if(!isDAbefore(v))
            error("Local variable " + v.name() + " is not assigned before used");
        }
      }

      else if(decl() instanceof FieldDeclaration && !isQualified()) {
        FieldDeclaration f = (FieldDeclaration)decl();
        //if(f.isFinal() && f.isInstanceVariable() && !isDAbefore(f)) {
        //if(f.isFinal() && !isDAbefore(f) && (!f.hasInit() || !f.getInit().isConstant())) {
        //if(f.isFinal() && (!f.hasInit() || !f.getInit().isConstant()) && !isDAbefore(f)) {
        if(f.isFinal() && !f.hasInit() && !isDAbefore(f)) {
          error("Final field " + f + " is not assigned before used");
        }
      }

    }
    if(isDest()) {
      Variable v = decl();
      // Blank final field
      if(v.isFinal() && v.isBlank() && !hostType().instanceOf(v.hostType()))
        error("The final variable is not a blank final in this context, so it may not be assigned.");
      else if(v.isFinal() && isQualified() && (!qualifier().isThisAccess() || ((Access)qualifier()).isQualified()))
        error("the blank final field " + v.name() + " may only be assigned by simple name");

      // local variable or parameter
      else if(v instanceof VariableDeclaration) {
        VariableDeclaration var = (VariableDeclaration) v;
        //System.out.println("### is variable");
        if(!var.isValue() && var.getParent().getParent().getParent() instanceof SwitchStmt && var.isFinal()) {
          if(!isDUbefore(var))
            error("Final variable " + var.name() + " may only be assigned once");
        }
        else if(var.isValue()) {
          if(var.hasInit() || !isDUbefore(var))
            error("Final variable " + var.name() + " may only be assigned once");
        }
        else if(var.isBlankFinal()) {
          if(var.hasInit() || !isDUbefore(var))
            error("Final variable " + var.name() + " may only be assigned once");
        }
        if(var.isFinal() && (var.hasInit() || !isDUbefore(var))) {
        //if(var.isFinal() && ((var.hasInit() && var.getInit().isConstant()) || !isDUbefore(var))) {
        }
      }
      // field
      else if(v instanceof FieldDeclaration) {
        FieldDeclaration f = (FieldDeclaration)v;
        if(f.isFinal()) {
          if(f.hasInit())
            error("already initialized final field " + f.name() + " can not be assigned");
          else {
            BodyDecl bodyDecl = enclosingBodyDecl();
            if(!(bodyDecl instanceof ConstructorDecl) && !(bodyDecl instanceof InstanceInitializer) && !(bodyDecl instanceof StaticInitializer) && !(bodyDecl instanceof FieldDeclaration))
              error("final field " + f.name() + " may only be assigned in constructors and initializers");
            else if(!isDUbefore(f))
              error("blank final field " + f.name() + " may only be assigned once");
          }
        }
      }
      else if(v.isParameter()) {
        // 8.4.1
        if(v.isFinal()) {
          error("Final parameter " + v.name() + " may not be assigned");
        }
      }

    }
  }
  /**
   * @aspect DA
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:475
   */
  protected boolean checkDUeverywhere(Variable v) {
    if(isDest() && decl() == v)
      return false;
    return super.checkDUeverywhere(v);
  }
  /**
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:242
   */
  public BodyDecl closestBodyDecl(TypeDecl t) {
    ASTNode node = this;
    while(!(node.getParent().getParent() instanceof Program) && node.getParent().getParent() != t) {
      node = node.getParent();
    }
    if(node instanceof BodyDecl)
      return (BodyDecl)node;
    return null;
  }
  /**
   * @aspect NodeConstructors
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NodeConstructors.jrag:38
   */
  public VarAccess(String name, int start, int end) {
    this(name);
    this.start = this.IDstart = start;
    this.end = this.IDend = end;
  }
  /**
   * @aspect Annotations
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:353
   */
  public void checkModifiers() {
    if(decl() instanceof FieldDeclaration) {
      FieldDeclaration f = (FieldDeclaration)decl();
      if(f.isDeprecated() &&
        !withinDeprecatedAnnotation() &&
        hostType().topLevelType() != f.hostType().topLevelType() &&
        !withinSuppressWarnings("deprecation"))
          warning(f.name() + " in " + f.hostType().typeName() + " has been deprecated");
    }
  }
  /**
   * @aspect Enums
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Enums.jrag:464
   */
  protected void checkEnum(EnumDecl enumDecl) {
    super.checkEnum(enumDecl);
    if(decl().isStatic() && decl().hostType() == enumDecl && !isConstant())
      error("may not reference a static field of an enum type from here");
  }
  /**
   * @declaredat ASTNode:1
   */
  public VarAccess() {
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
  public VarAccess(String p0) {
    setID(p0);
  }
  /**
   * @declaredat ASTNode:15
   */
  public VarAccess(beaver.Symbol p0) {
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
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:33
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    isConstant_reset();
    isDAafter_Variable_reset();
    decls_reset();
    decl_reset();
    isFieldAccess_reset();
    type_reset();
    enclosingLambda_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:46
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:52
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:58
   */
  public VarAccess clone() throws CloneNotSupportedException {
    VarAccess node = (VarAccess) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:65
   */
  public VarAccess copy() {
    try {
      VarAccess node = (VarAccess) clone();
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
   * @declaredat ASTNode:84
   */
  public VarAccess fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:93
   */
  public VarAccess treeCopyNoTransform() {
    VarAccess tree = (VarAccess) copy();
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
   * @declaredat ASTNode:113
   */
  public VarAccess treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:120
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_ID == ((VarAccess)node).tokenString_ID);    
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
   * @aspect Java8NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/NameCheck.jrag:176
   */
   
	public void nameCheck() {
		if(decls().isEmpty() && (!isQualified() || !qualifier().type().isUnknown() || qualifier().isPackageAccess()))
			error("no field named " + name() + " is accessible");
		if (decls().size() > 1) {
			StringBuffer sb = new StringBuffer();
			sb.append("several fields named " + name());
			for (Iterator iter = decls().iterator(); iter.hasNext(); ) {
				Variable v = (Variable)iter.next();
				sb.append("\n    " + v.type().typeName() + "." + v.name() + " declared in " + v.hostType().typeName());
			}
			error(sb.toString());
		}

		// 8.8.5.1
		if(inExplicitConstructorInvocation() && !isQualified() && decl().isInstanceVariable() && hostType() == decl().hostType())
			error("instance variable " + name() + " may not be accessed in an explicit constructor invocation");

		Variable v = decl();
		if(!v.isFinal() && !v.isClassVariable() && !v.isInstanceVariable() && v.hostType() != hostType() && !v.isEffectivelyFinal())
			error("A parameter/variable used but not declared in an inner class must be final or effectively final");
		

		// 8.3.2.3
		if ((decl().isInstanceVariable() || decl().isClassVariable()) && !isQualified()) {
			if (hostType() != null && !hostType().declaredBeforeUse(decl(), this)) {
				if (inSameInitializer() && !simpleAssignment() && inDeclaringClass()) {
					BodyDecl b = closestBodyDecl(hostType());
					error("variable " + decl().name() + " is used in " + b.prettyPrint() + " before it is declared");
				}
			}
		}
		
		if(!v.isClassVariable() && !v.isInstanceVariable() && enclosingLambda() != null) {
			if(v instanceof ParameterDeclaration) {
				ParameterDeclaration decl = (ParameterDeclaration)v;
				if(decl.enclosingLambda() != enclosingLambda()) {
					// 15.27.2
					if(!decl.isFinal() && !decl.isEffectivelyFinal())
						error("Parameter " + v.name() + " must be effectively final");
				}
			}
			else if(v instanceof InferredParameterDeclaration) {
				InferredParameterDeclaration decl = (InferredParameterDeclaration)v;
				if(decl.enclosingLambda() != enclosingLambda()) {
					// 15.27.2
					if(!decl.isFinal() && !decl.isEffectivelyFinal())
						error("Parameter " + v.name() + " must be effectively final");
				}
			}
			else if(v instanceof VariableDeclaration) {
				VariableDeclaration decl = (VariableDeclaration)v;
				if(decl.enclosingLambda() != enclosingLambda()) {
					// 15.27.2
					if(!decl.isFinal() && !decl.isEffectivelyFinal())
						error("Variable " + v.name() + " must be effectively final");
					// 15.27.2
					if(!enclosingLambda().isDAbefore(decl))
						error("Variable " + v.name() + " must be definitely assigned before used in a lambda");
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
  @ASTNodeAnnotation.Attribute
  public Constant constant() {
    ASTNode$State state = state();
    Constant constant_value = type().cast(decl().getInit().constant());

    return constant_value;
  }
  /**
   * @apilevel internal
   */
  protected int isConstant_visited = -1;
/**
 * @apilevel internal
 */
private void isConstant_reset() {
  isConstant_computed = false;
  isConstant_initialized = false;
  isConstant_visited = -1;
}  
  /**
   * @apilevel internal
   */
  protected boolean isConstant_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isConstant_initialized = false;
  /**
   * @apilevel internal
   */
  protected boolean isConstant_value;
  @ASTNodeAnnotation.Attribute
  public boolean isConstant() {
    if(isConstant_computed) {
      return isConstant_value;
    }
    ASTNode$State state = state();
    boolean new_isConstant_value;
    if (!isConstant_initialized) {
      isConstant_initialized = true;
      isConstant_value = false;
    }
    if (!state.IN_CIRCLE) {
      state.IN_CIRCLE = true;
      int num = state.boundariesCrossed;
      boolean isFinal = this.is$Final();
      do {
        isConstant_visited = state.CIRCLE_INDEX;
        state.CHANGE = false;
        new_isConstant_value = isConstant_compute();
        if (new_isConstant_value != isConstant_value) {
          state.CHANGE = true;
        }
        isConstant_value = new_isConstant_value;
        state.CIRCLE_INDEX++;
      } while (state.CHANGE);
      if (isFinal && num == state().boundariesCrossed) {
        isConstant_computed = true;
      } else {
        state.RESET_CYCLE = true;
        boolean $tmp = isConstant_compute();
        state.RESET_CYCLE = false;
        isConstant_computed = false;
        isConstant_initialized = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return isConstant_value;
    }
    if(isConstant_visited != state.CIRCLE_INDEX) {
      isConstant_visited = state.CIRCLE_INDEX;
      if (state.RESET_CYCLE) {
        isConstant_computed = false;
        isConstant_initialized = false;
        isConstant_visited = -1;
        return isConstant_value;
      }
      new_isConstant_value = isConstant_compute();
      if (new_isConstant_value != isConstant_value) {
        state.CHANGE = true;
      }
      isConstant_value = new_isConstant_value;
      state.INTERMEDIATE_VALUE = true;
      return isConstant_value;
    }
    state.INTERMEDIATE_VALUE = true;
    return isConstant_value;
  }
  /**
   * @apilevel internal
   */
  private boolean isConstant_compute() {
      Variable v = decl();
      if(v instanceof FieldDeclaration) {
        FieldDeclaration f = (FieldDeclaration)v;
        return f.isConstant() && (!isQualified() || (isQualified() && qualifier().isTypeAccess()));
      }
      boolean result = v.isFinal() && v.hasInit() && v.getInit().isConstant() && (v.type().isPrimitive() || v.type().isString());
      return result && (!isQualified() || (isQualified() && qualifier().isTypeAccess()));
    }
  @ASTNodeAnnotation.Attribute
  public Variable varDecl() {
    ASTNode$State state = state();
    Variable varDecl_value = decl();

    return varDecl_value;
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
    boolean isDAafter_Variable_value = isDAbefore(v);
    if (isFinal && num == state().boundariesCrossed) {
      isDAafter_Variable_values.put(_parameters, Boolean.valueOf(isDAafter_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDAafter_Variable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isDUafter(Variable v) {
    ASTNode$State state = state();
    boolean isDUafter_Variable_value = isDUbefore(v);

    return isDUafter_Variable_value;
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
        if(isDest() && decl() == v && enclosingStmt().reachable()) {
          return false;
        }
        return super.unassignedEverywhere(v, stmt);
      }
    finally {
    }
  }
  /**
   * @apilevel internal
   */
  protected boolean decls_computed = false;
  /**
   * @apilevel internal
   */
  protected SimpleSet decls_value;
/**
 * @apilevel internal
 */
private void decls_reset() {
  decls_computed = false;
  decls_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public SimpleSet decls() {
    if(decls_computed) {
      return decls_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    decls_value = decls_compute();
    if (isFinal && num == state().boundariesCrossed) {
      decls_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return decls_value;
  }
  /**
   * @apilevel internal
   */
  private SimpleSet decls_compute() {
      SimpleSet set = lookupVariable(name());
      if(set.size() == 1) {
        Variable v = (Variable)set.iterator().next();
        if(!isQualified() && inStaticContext()) {
          if(v.isInstanceVariable() && !hostType().memberFields(v.name()).isEmpty())
            return SimpleSet.emptySet;
        }
        else if(isQualified() && qualifier().staticContextQualifier()) {
          if(v.isInstanceVariable())
            return SimpleSet.emptySet;
        }
      }
      return set;
    }
  /**
   * @apilevel internal
   */
  protected boolean decl_computed = false;
  /**
   * @apilevel internal
   */
  protected Variable decl_value;
/**
 * @apilevel internal
 */
private void decl_reset() {
  decl_computed = false;
  decl_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Variable decl() {
    if(decl_computed) {
      return decl_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    decl_value = decl_compute();
    if (isFinal && num == state().boundariesCrossed) {
      decl_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return decl_value;
  }
  /**
   * @apilevel internal
   */
  private Variable decl_compute() {
      SimpleSet decls = decls();
      if(decls.size() == 1)
        return (Variable)decls.iterator().next();
      return unknownField();
    }
  /**
   * @attribute syn
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:252
   */
  @ASTNodeAnnotation.Attribute
  public boolean inSameInitializer() {
    ASTNode$State state = state();
    try {
        BodyDecl b = closestBodyDecl(decl().hostType());
        if(b == null) return false;
        if(b instanceof FieldDeclaration && ((FieldDeclaration)b).isStatic() == decl().isStatic())
          return true;
        if(b instanceof InstanceInitializer && !decl().isStatic())
          return true;
        if(b instanceof StaticInitializer && decl().isStatic())
          return true;
        return false;
      }
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public boolean simpleAssignment() {
    ASTNode$State state = state();
    boolean simpleAssignment_value = isDest() && getParent() instanceof AssignSimpleExpr;

    return simpleAssignment_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean inDeclaringClass() {
    ASTNode$State state = state();
    boolean inDeclaringClass_value = hostType() == decl().hostType();

    return inDeclaringClass_value;
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
  protected boolean isFieldAccess_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isFieldAccess_value;
/**
 * @apilevel internal
 */
private void isFieldAccess_reset() {
  isFieldAccess_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isFieldAccess() {
    if(isFieldAccess_computed) {
      return isFieldAccess_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isFieldAccess_value = decl().isClassVariable() || decl().isInstanceVariable();
    if (isFinal && num == state().boundariesCrossed) {
      isFieldAccess_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isFieldAccess_value;
  }
  @ASTNodeAnnotation.Attribute
  public NameType predNameType() {
    ASTNode$State state = state();
    NameType predNameType_value = NameType.AMBIGUOUS_NAME;

    return predNameType_value;
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
    type_value = decl().type();
    if (isFinal && num == state().boundariesCrossed) {
      type_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return type_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isVariable() {
    ASTNode$State state = state();
    boolean isVariable_value = true;

    return isVariable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isEnumConstant() {
    ASTNode$State state = state();
    boolean isEnumConstant_value = varDecl() instanceof EnumConstant;

    return isEnumConstant_value;
  }
  @ASTNodeAnnotation.Attribute
  public Collection<TypeDecl> throwTypes() {
    ASTNode$State state = state();
    Collection<TypeDecl> throwTypes_value = decl().throwTypes();

    return throwTypes_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean modifiedInScope(Variable var) {
    ASTNode$State state = state();
    boolean modifiedInScope_Variable_value = false;

    return modifiedInScope_Variable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isVariable(Variable var) {
    ASTNode$State state = state();
    boolean isVariable_Variable_value = decl() == var;

    return isVariable_Variable_value;
  }
  /**
   * @attribute inh
   * @aspect TypeHierarchyCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeHierarchyCheck.jrag:141
   */
  @ASTNodeAnnotation.Attribute
  public boolean inExplicitConstructorInvocation() {
    ASTNode$State state = state();
    boolean inExplicitConstructorInvocation_value = getParent().Define_boolean_inExplicitConstructorInvocation(this, null);

    return inExplicitConstructorInvocation_value;
  }
  /**
   * @attribute inh
   * @aspect EnclosingLambda
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/EnclosingLambda.jrag:32
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
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
