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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/grammar/Generics.ast:6
 * @production ParClassDecl : {@link ClassDecl} ::= <span class="component">Argument:{@link Access}*</span> <span class="component">[SuperClass:{@link Access}]</span> <span class="component">Implements:{@link Access}*</span> <span class="component">{@link BodyDecl}*</span>;

 */
public class ParClassDecl extends ClassDecl implements Cloneable, ParTypeDecl, MemberSubstitutor {
  /**
   * @aspect GenericsNameBinding
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:567
   */
  public void collectErrors() {
    // Disable error check for ParClassDecl which is an instanciated GenericClassDecl
  }
  /**
   * @aspect GenericsPrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericsPrettyPrint.jrag:35
   */
  public void prettyPrint(StringBuffer sb) {
    getModifiers().prettyPrint(sb);
    sb.append("class " + getID());
    sb.append('<');
    if (getNumArgument() > 0) {
      getArgument(0).prettyPrint(sb);
      for (int i = 1; i < getNumArgument(); i++) {
        sb.append(", ");
        getArgument(i).prettyPrint(sb);
      }
    }
    sb.append('>');
    if (hasSuperClass()) {
      sb.append(" extends ");
      getSuperClass().prettyPrint(sb);
    }
    if(getNumImplements() > 0) {
      sb.append(" implements ");
      getImplements(0).prettyPrint(sb);
      for(int i = 1; i < getNumImplements(); i++) {
        sb.append(", ");
        getImplements(i).prettyPrint(sb);
      }
    }
    ppBodyDecls(sb);
  }
  /**
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:711
   */
  public TypeDecl substitute(TypeVariable typeVariable) {
    for(int i = 0; i < numTypeParameter(); i++)
      if(typeParameter(i) == typeVariable)
        return getArgument(i).type();
    return super.substitute(typeVariable);
  }
  /**
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:724
   */
  public int numTypeParameter() {
    return ((GenericTypeDecl)original()).getNumTypeParameter();
  }
  /**
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:727
   */
  public TypeVariable typeParameter(int index) {
    return ((GenericTypeDecl)original()).getTypeParameter(index);
  }
  /**
   * @aspect GenericsParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericsParTypeDecl.jrag:74
   */
  public Access createQualifiedAccess() {
    List typeArgumentList = new List();
    for(int i = 0; i < getNumArgument(); i++) {
      Access a = (Access)getArgument(i);
      if(a instanceof TypeAccess)
        typeArgumentList.add(a.type().createQualifiedAccess());
      else
        typeArgumentList.add(a.fullCopy());
    }
    if(!isTopLevelType()) {
      if(isRawType())
        return enclosingType().createQualifiedAccess().qualifiesAccess(
          new TypeAccess("", getID())
        );
      else
        return enclosingType().createQualifiedAccess().qualifiesAccess(
          new ParTypeAccess(new TypeAccess("", getID()), typeArgumentList)
        );
    }
    else {
      if(isRawType())
        return new TypeAccess(packageName(), getID());
      else
        return new ParTypeAccess(new TypeAccess(packageName(), getID()), typeArgumentList);
    }
  }
  /**
   * @aspect LambdaExpr
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/LambdaExpr.jrag:120
   */
   
	public Access substitute(Parameterization parTypeDecl) {
		// TODO: include nesting as well....
		if(parTypeDecl.isRawType())
			return ((GenericTypeDecl)genericDecl()).rawType().createBoundAccess();
		/* These lines have been removed because they erase arguments from 
			parameter types when they are not using type variables, for example
			C<String> is substituted to only C, which I don't think is correct?
			And if the ParTypeDecl doesn't use any type variables, why is there
			even any need for further substitution? 
			
		if(!usesTypeVariable())
			return super.substitute(parTypeDecl);*/
		List list = new List();
		for(int i = 0; i < getNumArgument(); i++)
			list.add(getArgument(i).type().substitute(parTypeDecl));
		return new ParTypeAccess(genericDecl().createQualifiedAccess(), list);
		
	}
  /**
   * @declaredat ASTNode:1
   */
  public ParClassDecl() {
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
    children = new ASTNode[6];
    setChild(new List(), 1);
    setChild(new Opt(), 2);
    setChild(new Opt(), 3);
    setChild(new List(), 4);
    setChild(new List(), 5);
  }
  /**
   * @declaredat ASTNode:18
   */
  public ParClassDecl(Modifiers p0, String p1, List<Access> p2) {
    setChild(p0, 0);
    setID(p1);
    setChild(p2, 1);
  }
  /**
   * @declaredat ASTNode:23
   */
  public ParClassDecl(Modifiers p0, beaver.Symbol p1, List<Access> p2) {
    setChild(p0, 0);
    setID(p1);
    setChild(p2, 1);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:31
   */
  protected int numChildren() {
    return 2;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:37
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:43
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    erasure_reset();
    getSuperClassOpt_reset();
    getImplementsList_reset();
    getBodyDeclList_reset();
    involvesTypeParameters_reset();
    subtype_TypeDecl_reset();
    sameStructure_TypeDecl_reset();
    instanceOf_TypeDecl_reset();
    strictSubtype_TypeDecl_reset();
    sameSignature_ArrayList_reset();
    usesTypeVariable_reset();
    sourceTypeDecl_reset();
    fullName_reset();
    typeName_reset();
    unimplementedMethods_reset();
    localMethodsSignatureMap_reset();
    localFields_String_reset();
    localTypeDecls_String_reset();
    constructors_reset();
    genericDecl_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:69
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:75
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:81
   */
  public ParClassDecl clone() throws CloneNotSupportedException {
    ParClassDecl node = (ParClassDecl) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:88
   */
  public ParClassDecl copy() {
    try {
      ParClassDecl node = (ParClassDecl) clone();
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
   * @declaredat ASTNode:107
   */
  public ParClassDecl fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:116
   */
  public ParClassDecl treeCopyNoTransform() {
    ParClassDecl tree = (ParClassDecl) copy();
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        switch (i) {
        case 2:
        case 3:
          tree.children[i] = new Opt();
          continue;
        case 4:
        case 5:
          tree.children[i] = new List();
          continue;
        }
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
   * @declaredat ASTNode:146
   */
  public ParClassDecl treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:153
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_ID == ((ParClassDecl)node).tokenString_ID);    
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
   * Replaces the lexeme ID.
   * @param value The new value for the lexeme ID.
   * @apilevel high-level
   */
  public void setID(String value) {
    tokenString_ID = value;
  }
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
   * Replaces the Argument list.
   * @param list The new list node to be used as the Argument list.
   * @apilevel high-level
   */
  public void setArgumentList(List<Access> list) {
    setChild(list, 1);
  }
  /**
   * Retrieves the number of children in the Argument list.
   * @return Number of children in the Argument list.
   * @apilevel high-level
   */
  public int getNumArgument() {
    return getArgumentList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Argument list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Argument list.
   * @apilevel low-level
   */
  public int getNumArgumentNoTransform() {
    return getArgumentListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Argument list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Argument list.
   * @apilevel high-level
   */
  public Access getArgument(int i) {
    return (Access) getArgumentList().getChild(i);
  }
  /**
   * Check whether the Argument list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasArgument() {
    return getArgumentList().getNumChild() != 0;
  }
  /**
   * Append an element to the Argument list.
   * @param node The element to append to the Argument list.
   * @apilevel high-level
   */
  public void addArgument(Access node) {
    List<Access> list = (parent == null || state == null) ? getArgumentListNoTransform() : getArgumentList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addArgumentNoTransform(Access node) {
    List<Access> list = getArgumentListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Argument list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setArgument(Access node, int i) {
    List<Access> list = getArgumentList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the Argument list.
   * @return The node representing the Argument list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="Argument")
  public List<Access> getArgumentList() {
    List<Access> list = (List<Access>) getChild(1);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the Argument list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Argument list.
   * @apilevel low-level
   */
  public List<Access> getArgumentListNoTransform() {
    return (List<Access>) getChildNoTransform(1);
  }
  /**
   * Retrieves the Argument list.
   * @return The node representing the Argument list.
   * @apilevel high-level
   */
  public List<Access> getArguments() {
    return getArgumentList();
  }
  /**
   * Retrieves the Argument list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Argument list.
   * @apilevel low-level
   */
  public List<Access> getArgumentsNoTransform() {
    return getArgumentListNoTransform();
  }
  /**
   * Replaces the optional node for the ImplicitConstructor child. This is the <code>Opt</code>
   * node containing the child ImplicitConstructor, not the actual child!
   * @param opt The new node to be used as the optional node for the ImplicitConstructor child.
   * @apilevel low-level
   */
  public void setImplicitConstructorOpt(Opt<ConstructorDecl> opt) {
    setChild(opt, 2);
  }
  /**
   * Replaces the (optional) ImplicitConstructor child.
   * @param node The new node to be used as the ImplicitConstructor child.
   * @apilevel high-level
   */
  public void setImplicitConstructor(ConstructorDecl node) {
    getImplicitConstructorOpt().setChild(node, 0);
  }
  /**
   * Check whether the optional ImplicitConstructor child exists.
   * @return {@code true} if the optional ImplicitConstructor child exists, {@code false} if it does not.
   * @apilevel high-level
   */
  public boolean hasImplicitConstructor() {
    return getImplicitConstructorOpt().getNumChild() != 0;
  }
  /**
   * Retrieves the (optional) ImplicitConstructor child.
   * @return The ImplicitConstructor child, if it exists. Returns {@code null} otherwise.
   * @apilevel low-level
   */
  public ConstructorDecl getImplicitConstructor() {
    return (ConstructorDecl) getImplicitConstructorOpt().getChild(0);
  }
  /**
   * Retrieves the optional node for child ImplicitConstructor. This is the <code>Opt</code> node containing the child ImplicitConstructor, not the actual child!
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The optional node for child ImplicitConstructor.
   * @apilevel low-level
   */
  public Opt<ConstructorDecl> getImplicitConstructorOptNoTransform() {
    return (Opt<ConstructorDecl>) getChildNoTransform(2);
  }
  /**
   * Retrieves the child position of the optional child ImplicitConstructor.
   * @return The the child position of the optional child ImplicitConstructor.
   * @apilevel low-level
   */
  protected int getImplicitConstructorOptChildPosition() {
    return 2;
  }
  /**
   * Replaces the optional node for the SuperClass child. This is the <code>Opt</code>
   * node containing the child SuperClass, not the actual child!
   * @param opt The new node to be used as the optional node for the SuperClass child.
   * @apilevel low-level
   */
  public void setSuperClassOpt(Opt<Access> opt) {
    setChild(opt, 3);
  }
  /**
   * Replaces the (optional) SuperClass child.
   * @param node The new node to be used as the SuperClass child.
   * @apilevel high-level
   */
  public void setSuperClass(Access node) {
    getSuperClassOpt().setChild(node, 0);
  }
  /**
   * Check whether the optional SuperClass child exists.
   * @return {@code true} if the optional SuperClass child exists, {@code false} if it does not.
   * @apilevel high-level
   */
  public boolean hasSuperClass() {
    return getSuperClassOpt().getNumChild() != 0;
  }
  /**
   * Retrieves the (optional) SuperClass child.
   * @return The SuperClass child, if it exists. Returns {@code null} otherwise.
   * @apilevel low-level
   */
  public Access getSuperClass() {
    return (Access) getSuperClassOpt().getChild(0);
  }
  /**
   * Retrieves the optional node for child SuperClass. This is the <code>Opt</code> node containing the child SuperClass, not the actual child!
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The optional node for child SuperClass.
   * @apilevel low-level
   */
  public Opt<Access> getSuperClassOptNoTransform() {
    return (Opt<Access>) getChildNoTransform(3);
  }
  /**
   * Retrieves the child position of the optional child SuperClass.
   * @return The the child position of the optional child SuperClass.
   * @apilevel low-level
   */
  protected int getSuperClassOptChildPosition() {
    return 3;
  }
  /**
   * Replaces the Implements list.
   * @param list The new list node to be used as the Implements list.
   * @apilevel high-level
   */
  public void setImplementsList(List<Access> list) {
    setChild(list, 4);
  }
  /**
   * Retrieves the number of children in the Implements list.
   * @return Number of children in the Implements list.
   * @apilevel high-level
   */
  public int getNumImplements() {
    return getImplementsList().getNumChild();
  }
  /**
   * Retrieves the number of children in the Implements list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the Implements list.
   * @apilevel low-level
   */
  public int getNumImplementsNoTransform() {
    return getImplementsListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the Implements list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the Implements list.
   * @apilevel high-level
   */
  public Access getImplements(int i) {
    return (Access) getImplementsList().getChild(i);
  }
  /**
   * Check whether the Implements list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasImplements() {
    return getImplementsList().getNumChild() != 0;
  }
  /**
   * Append an element to the Implements list.
   * @param node The element to append to the Implements list.
   * @apilevel high-level
   */
  public void addImplements(Access node) {
    List<Access> list = (parent == null || state == null) ? getImplementsListNoTransform() : getImplementsList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addImplementsNoTransform(Access node) {
    List<Access> list = getImplementsListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the Implements list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setImplements(Access node, int i) {
    List<Access> list = getImplementsList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the child position of the Implements list.
   * @return The the child position of the Implements list.
   * @apilevel low-level
   */
  protected int getImplementsListChildPosition() {
    return 4;
  }
  /**
   * Retrieves the Implements list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Implements list.
   * @apilevel low-level
   */
  public List<Access> getImplementsListNoTransform() {
    return (List<Access>) getChildNoTransform(4);
  }
  /**
   * Retrieves the Implements list.
   * @return The node representing the Implements list.
   * @apilevel high-level
   */
  public List<Access> getImplementss() {
    return getImplementsList();
  }
  /**
   * Retrieves the Implements list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the Implements list.
   * @apilevel low-level
   */
  public List<Access> getImplementssNoTransform() {
    return getImplementsListNoTransform();
  }
  /**
   * Replaces the BodyDecl list.
   * @param list The new list node to be used as the BodyDecl list.
   * @apilevel high-level
   */
  public void setBodyDeclList(List<BodyDecl> list) {
    setChild(list, 5);
  }
  /**
   * Retrieves the number of children in the BodyDecl list.
   * @return Number of children in the BodyDecl list.
   * @apilevel high-level
   */
  public int getNumBodyDecl() {
    return getBodyDeclList().getNumChild();
  }
  /**
   * Retrieves the number of children in the BodyDecl list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the BodyDecl list.
   * @apilevel low-level
   */
  public int getNumBodyDeclNoTransform() {
    return getBodyDeclListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the BodyDecl list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the BodyDecl list.
   * @apilevel high-level
   */
  public BodyDecl getBodyDecl(int i) {
    return (BodyDecl) getBodyDeclList().getChild(i);
  }
  /**
   * Check whether the BodyDecl list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasBodyDecl() {
    return getBodyDeclList().getNumChild() != 0;
  }
  /**
   * Append an element to the BodyDecl list.
   * @param node The element to append to the BodyDecl list.
   * @apilevel high-level
   */
  public void addBodyDecl(BodyDecl node) {
    List<BodyDecl> list = (parent == null || state == null) ? getBodyDeclListNoTransform() : getBodyDeclList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addBodyDeclNoTransform(BodyDecl node) {
    List<BodyDecl> list = getBodyDeclListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the BodyDecl list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setBodyDecl(BodyDecl node, int i) {
    List<BodyDecl> list = getBodyDeclList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the child position of the BodyDecl list.
   * @return The the child position of the BodyDecl list.
   * @apilevel low-level
   */
  protected int getBodyDeclListChildPosition() {
    return 5;
  }
  /**
   * Retrieves the BodyDecl list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the BodyDecl list.
   * @apilevel low-level
   */
  public List<BodyDecl> getBodyDeclListNoTransform() {
    return (List<BodyDecl>) getChildNoTransform(5);
  }
  /**
   * Retrieves the BodyDecl list.
   * @return The node representing the BodyDecl list.
   * @apilevel high-level
   */
  public List<BodyDecl> getBodyDecls() {
    return getBodyDeclList();
  }
  /**
   * Retrieves the BodyDecl list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the BodyDecl list.
   * @apilevel low-level
   */
  public List<BodyDecl> getBodyDeclsNoTransform() {
    return getBodyDeclListNoTransform();
  }
  @ASTNodeAnnotation.Attribute
  public TypeDecl hostType() {
    ASTNode$State state = state();
    TypeDecl hostType_value = original();

    return hostType_value;
  }
  @ASTNodeAnnotation.Attribute
  public TypeDecl topLevelType() {
    ASTNode$State state = state();
    TypeDecl topLevelType_value = erasure().topLevelType();

    return topLevelType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isRawType() {
    ASTNode$State state = state();
    boolean isRawType_value = isNestedType() && enclosingType().isRawType();

    return isRawType_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean erasure_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl erasure_value;
/**
 * @apilevel internal
 */
private void erasure_reset() {
  erasure_computed = false;
  erasure_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl erasure() {
    if(erasure_computed) {
      return erasure_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    erasure_value = genericDecl();
    if (isFinal && num == state().boundariesCrossed) {
      erasure_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return erasure_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean getSuperClassOpt_computed = false;
  /**
   * @apilevel internal
   */
  protected Opt getSuperClassOpt_value;
/**
 * @apilevel internal
 */
private void getSuperClassOpt_reset() {
  getSuperClassOpt_computed = false;
  getSuperClassOpt_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Opt getSuperClassOpt() {
    if(getSuperClassOpt_computed) {
      return (Opt) getChild(getSuperClassOptChildPosition());
    }
    if(getSuperClassOpt_computed) {
      return getSuperClassOpt_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    getSuperClassOpt_value = getSuperClassOpt_compute();
    setSuperClassOpt(getSuperClassOpt_value);
    if (isFinal && num == state().boundariesCrossed) {
      getSuperClassOpt_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    Opt node = (Opt) this.getChild(getSuperClassOptChildPosition());
    return node;
  }
  /**
   * @apilevel internal
   */
  private Opt getSuperClassOpt_compute() {
      GenericClassDecl decl = (GenericClassDecl)genericDecl();
      Opt opt;
      //System.err.println("Begin substituting extends clause");
      if(decl.hasSuperClass())
        opt = new Opt((decl.getSuperClass().type().substitute(this)));
      else
        opt = new Opt();
      //System.err.println("End substituting extends clause");
      return opt;
    }
  /**
   * @apilevel internal
   */
  protected boolean getImplementsList_computed = false;
  /**
   * @apilevel internal
   */
  protected List getImplementsList_value;
/**
 * @apilevel internal
 */
private void getImplementsList_reset() {
  getImplementsList_computed = false;
  getImplementsList_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public List getImplementsList() {
    if(getImplementsList_computed) {
      return (List) getChild(getImplementsListChildPosition());
    }
    if(getImplementsList_computed) {
      return getImplementsList_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    getImplementsList_value = getImplementsList_compute();
    setImplementsList(getImplementsList_value);
    if (isFinal && num == state().boundariesCrossed) {
      getImplementsList_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    List node = (List) this.getChild(getImplementsListChildPosition());
    return node;
  }
  /**
   * @apilevel internal
   */
  private List getImplementsList_compute() {
      GenericClassDecl decl = (GenericClassDecl)genericDecl();
      //System.err.println("Begin substituting implements list");
      List list = decl.getImplementsList().substitute(this);
      //System.err.println("End substituting implements list");
      return list;
    }
  /**
   * @apilevel internal
   */
  protected boolean getBodyDeclList_computed = false;
  /**
   * @apilevel internal
   */
  protected List getBodyDeclList_value;
/**
 * @apilevel internal
 */
private void getBodyDeclList_reset() {
  getBodyDeclList_computed = false;
  getBodyDeclList_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public List getBodyDeclList() {
    if(getBodyDeclList_computed) {
      return (List) getChild(getBodyDeclListChildPosition());
    }
    if(getBodyDeclList_computed) {
      return getBodyDeclList_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    getBodyDeclList_value = new BodyDeclList();
    setBodyDeclList(getBodyDeclList_value);
    if (isFinal && num == state().boundariesCrossed) {
      getBodyDeclList_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    List node = (List) this.getChild(getBodyDeclListChildPosition());
    return node;
  }
  /**
   * @apilevel internal
   */
  protected int involvesTypeParameters_visited = -1;
/**
 * @apilevel internal
 */
private void involvesTypeParameters_reset() {
  involvesTypeParameters_computed = false;
  involvesTypeParameters_initialized = false;
  involvesTypeParameters_visited = -1;
}  
  /**
   * @apilevel internal
   */
  protected boolean involvesTypeParameters_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean involvesTypeParameters_initialized = false;
  /**
   * @apilevel internal
   */
  protected boolean involvesTypeParameters_value;
  @ASTNodeAnnotation.Attribute
  public boolean involvesTypeParameters() {
    if(involvesTypeParameters_computed) {
      return involvesTypeParameters_value;
    }
    ASTNode$State state = state();
    boolean new_involvesTypeParameters_value;
    if (!involvesTypeParameters_initialized) {
      involvesTypeParameters_initialized = true;
      involvesTypeParameters_value = false;
    }
    if (!state.IN_CIRCLE) {
      state.IN_CIRCLE = true;
      int num = state.boundariesCrossed;
      boolean isFinal = this.is$Final();
      do {
        involvesTypeParameters_visited = state.CIRCLE_INDEX;
        state.CHANGE = false;
        new_involvesTypeParameters_value = involvesTypeParameters_compute();
        if (new_involvesTypeParameters_value != involvesTypeParameters_value) {
          state.CHANGE = true;
        }
        involvesTypeParameters_value = new_involvesTypeParameters_value;
        state.CIRCLE_INDEX++;
      } while (state.CHANGE);
      if (isFinal && num == state().boundariesCrossed) {
        involvesTypeParameters_computed = true;
      } else {
        state.RESET_CYCLE = true;
        boolean $tmp = involvesTypeParameters_compute();
        state.RESET_CYCLE = false;
        involvesTypeParameters_computed = false;
        involvesTypeParameters_initialized = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return involvesTypeParameters_value;
    }
    if(involvesTypeParameters_visited != state.CIRCLE_INDEX) {
      involvesTypeParameters_visited = state.CIRCLE_INDEX;
      if (state.RESET_CYCLE) {
        involvesTypeParameters_computed = false;
        involvesTypeParameters_initialized = false;
        involvesTypeParameters_visited = -1;
        return involvesTypeParameters_value;
      }
      new_involvesTypeParameters_value = involvesTypeParameters_compute();
      if (new_involvesTypeParameters_value != involvesTypeParameters_value) {
        state.CHANGE = true;
      }
      involvesTypeParameters_value = new_involvesTypeParameters_value;
      state.INTERMEDIATE_VALUE = true;
      return involvesTypeParameters_value;
    }
    state.INTERMEDIATE_VALUE = true;
    return involvesTypeParameters_value;
  }
  /**
   * @apilevel internal
   */
  private boolean involvesTypeParameters_compute() {
      for(int i = 0; i < getNumArgument(); i++)
        if(getArgument(i).type().involvesTypeParameters())
          return true;
      return false;
    }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeGenericClassDecl(GenericClassDecl type) {
    ASTNode$State state = state();
    boolean supertypeGenericClassDecl_GenericClassDecl_value = type.subtype(genericDecl().original());

    return supertypeGenericClassDecl_GenericClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeClassDecl(ClassDecl type) {
    ASTNode$State state = state();
    boolean supertypeClassDecl_ClassDecl_value = super.supertypeClassDecl(type);

    return supertypeClassDecl_ClassDecl_value;
  }
/**
 * @apilevel internal
 */
private void subtype_TypeDecl_reset() {
  subtype_TypeDecl_values = null;
}  
  protected java.util.Map subtype_TypeDecl_values;
  @ASTNodeAnnotation.Attribute
  public boolean subtype(TypeDecl type) {
    Object _parameters = type;
    if (subtype_TypeDecl_values == null) subtype_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    ASTNode$State.CircularValue _value;
    if(subtype_TypeDecl_values.containsKey(_parameters)) {
      Object _o = subtype_TypeDecl_values.get(_parameters);
      if(!(_o instanceof ASTNode$State.CircularValue)) {
        return ((Boolean)_o).booleanValue();
      } else {
        _value = (ASTNode$State.CircularValue) _o;
      }
    } else {
      _value = new ASTNode$State.CircularValue();
      subtype_TypeDecl_values.put(_parameters, _value);
      _value.value = Boolean.valueOf(true);
    }
    ASTNode$State state = state();
    boolean new_subtype_TypeDecl_value;
    if (!state.IN_CIRCLE) {
      state.IN_CIRCLE = true;
      int num = state.boundariesCrossed;
      boolean isFinal = this.is$Final();
      // TODO: fixme
      // state().CIRCLE_INDEX = 1;
      do {
        _value.visited = new Integer(state.CIRCLE_INDEX);
        state.CHANGE = false;
        new_subtype_TypeDecl_value = type.supertypeParClassDecl(this);
        if (new_subtype_TypeDecl_value != ((Boolean)_value.value).booleanValue()) {
          state.CHANGE = true;
          _value.value = Boolean.valueOf(new_subtype_TypeDecl_value);
        }
        state.CIRCLE_INDEX++;
      } while (state.CHANGE);
      if (isFinal && num == state().boundariesCrossed) {
        subtype_TypeDecl_values.put(_parameters, new_subtype_TypeDecl_value);
      } else {
        subtype_TypeDecl_values.remove(_parameters);
        state.RESET_CYCLE = true;
        boolean $tmp = type.supertypeParClassDecl(this);
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_subtype_TypeDecl_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_subtype_TypeDecl_value = type.supertypeParClassDecl(this);
      if (state.RESET_CYCLE) {
        subtype_TypeDecl_values.remove(_parameters);
      }
      else if (new_subtype_TypeDecl_value != ((Boolean)_value.value).booleanValue()) {
        state.CHANGE = true;
        _value.value = new_subtype_TypeDecl_value;
      }
      state.INTERMEDIATE_VALUE = true;
      return new_subtype_TypeDecl_value;
    }
    state.INTERMEDIATE_VALUE = true;
    return ((Boolean)_value.value).booleanValue();
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeRawClassDecl(RawClassDecl type) {
    ASTNode$State state = state();
    boolean supertypeRawClassDecl_RawClassDecl_value = type.genericDecl().original().subtype(genericDecl().original());

    return supertypeRawClassDecl_RawClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeRawInterfaceDecl(RawInterfaceDecl type) {
    ASTNode$State state = state();
    boolean supertypeRawInterfaceDecl_RawInterfaceDecl_value = type.genericDecl().original().subtype(genericDecl().original());

    return supertypeRawInterfaceDecl_RawInterfaceDecl_value;
  }
/**
 * @apilevel internal
 */
private void sameStructure_TypeDecl_reset() {
  sameStructure_TypeDecl_values = null;
}  
  protected java.util.Map sameStructure_TypeDecl_values;
  @ASTNodeAnnotation.Attribute
  public boolean sameStructure(TypeDecl t) {
    Object _parameters = t;
    if (sameStructure_TypeDecl_values == null) sameStructure_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    ASTNode$State.CircularValue _value;
    if(sameStructure_TypeDecl_values.containsKey(_parameters)) {
      Object _o = sameStructure_TypeDecl_values.get(_parameters);
      if(!(_o instanceof ASTNode$State.CircularValue)) {
        return ((Boolean)_o).booleanValue();
      } else {
        _value = (ASTNode$State.CircularValue) _o;
      }
    } else {
      _value = new ASTNode$State.CircularValue();
      sameStructure_TypeDecl_values.put(_parameters, _value);
      _value.value = Boolean.valueOf(true);
    }
    ASTNode$State state = state();
    boolean new_sameStructure_TypeDecl_value;
    if (!state.IN_CIRCLE) {
      state.IN_CIRCLE = true;
      int num = state.boundariesCrossed;
      boolean isFinal = this.is$Final();
      // TODO: fixme
      // state().CIRCLE_INDEX = 1;
      do {
        _value.visited = new Integer(state.CIRCLE_INDEX);
        state.CHANGE = false;
        new_sameStructure_TypeDecl_value = sameStructure_compute(t);
        if (new_sameStructure_TypeDecl_value != ((Boolean)_value.value).booleanValue()) {
          state.CHANGE = true;
          _value.value = Boolean.valueOf(new_sameStructure_TypeDecl_value);
        }
        state.CIRCLE_INDEX++;
      } while (state.CHANGE);
      if (isFinal && num == state().boundariesCrossed) {
        sameStructure_TypeDecl_values.put(_parameters, new_sameStructure_TypeDecl_value);
      } else {
        sameStructure_TypeDecl_values.remove(_parameters);
        state.RESET_CYCLE = true;
        boolean $tmp = sameStructure_compute(t);
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_sameStructure_TypeDecl_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_sameStructure_TypeDecl_value = sameStructure_compute(t);
      if (state.RESET_CYCLE) {
        sameStructure_TypeDecl_values.remove(_parameters);
      }
      else if (new_sameStructure_TypeDecl_value != ((Boolean)_value.value).booleanValue()) {
        state.CHANGE = true;
        _value.value = new_sameStructure_TypeDecl_value;
      }
      state.INTERMEDIATE_VALUE = true;
      return new_sameStructure_TypeDecl_value;
    }
    state.INTERMEDIATE_VALUE = true;
    return ((Boolean)_value.value).booleanValue();
  }
  /**
   * @apilevel internal
   */
  private boolean sameStructure_compute(TypeDecl t) {
      if(!(t instanceof ParClassDecl))
        return false;
      ParClassDecl type = (ParClassDecl)t;
      if(type.genericDecl().original() == genericDecl().original() &&
         type.getNumArgument() == getNumArgument()) {
        for(int i = 0; i < getNumArgument(); i++)
          if(!type.getArgument(i).type().sameStructure(getArgument(i).type()))
            return false;
        if(isNestedType() && type.isNestedType())
          return type.enclosingType().sameStructure(enclosingType());
        return true;
      }
      return false;
    }
  /**
   * @attribute syn
   * @aspect GenericsSubtype
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericsSubtype.jrag:125
   */
  @ASTNodeAnnotation.Attribute
  public boolean supertypeParClassDecl(ParClassDecl type) {
    ASTNode$State state = state();
    try {
        if(type.genericDecl().original() == genericDecl().original() &&
           type.getNumArgument() == getNumArgument()) {
          for(int i = 0; i < getNumArgument(); i++)
            if(!type.getArgument(i).type().containedIn(getArgument(i).type()))
              return false;
          if(isNestedType() && type.isNestedType())
            return type.enclosingType().subtype(enclosingType());
          return true;
        }
        return supertypeClassDecl(type);
      }
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeParInterfaceDecl(ParInterfaceDecl type) {
    ASTNode$State state = state();
    boolean supertypeParInterfaceDecl_ParInterfaceDecl_value = false;

    return supertypeParInterfaceDecl_ParInterfaceDecl_value;
  }
  protected java.util.Map instanceOf_TypeDecl_values;
/**
 * @apilevel internal
 */
private void instanceOf_TypeDecl_reset() {
  instanceOf_TypeDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean instanceOf(TypeDecl type) {
    Object _parameters = type;
    if (instanceOf_TypeDecl_values == null) instanceOf_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(instanceOf_TypeDecl_values.containsKey(_parameters)) {
      return ((Boolean)instanceOf_TypeDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean instanceOf_TypeDecl_value = subtype(type);
    if (isFinal && num == state().boundariesCrossed) {
      instanceOf_TypeDecl_values.put(_parameters, Boolean.valueOf(instanceOf_TypeDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return instanceOf_TypeDecl_value;
  }
  /**
   * A type is reifiable if it either refers to a non-parameterized type,
   * is a raw type, is a parameterized type with only unbound wildcard
   * parameters or is an array type with a reifiable type parameter.
   * 
   * @see "JLSv3 &sect;4.7"
   * @attribute syn
   * @aspect SafeVarargs
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/SafeVarargs.jrag:106
   */
  @ASTNodeAnnotation.Attribute
  public boolean isReifiable() {
    ASTNode$State state = state();
    try {
        if (isRawType())
          return true;
        for (int i = 0; i < getNumArgument(); ++i) {
          if (!getArgument(i).type().isWildcard())
            return false;
        }
        return true;
      }
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeGenericClassDecl(GenericClassDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeGenericClassDecl_GenericClassDecl_value = type.strictSubtype(genericDecl().original());

    return strictSupertypeGenericClassDecl_GenericClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeClassDecl(ClassDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeClassDecl_ClassDecl_value = super.strictSupertypeClassDecl(type);

    return strictSupertypeClassDecl_ClassDecl_value;
  }
/**
 * @apilevel internal
 */
private void strictSubtype_TypeDecl_reset() {
  strictSubtype_TypeDecl_values = null;
}  
  protected java.util.Map strictSubtype_TypeDecl_values;
  @ASTNodeAnnotation.Attribute
  public boolean strictSubtype(TypeDecl type) {
    Object _parameters = type;
    if (strictSubtype_TypeDecl_values == null) strictSubtype_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    ASTNode$State.CircularValue _value;
    if(strictSubtype_TypeDecl_values.containsKey(_parameters)) {
      Object _o = strictSubtype_TypeDecl_values.get(_parameters);
      if(!(_o instanceof ASTNode$State.CircularValue)) {
        return ((Boolean)_o).booleanValue();
      } else {
        _value = (ASTNode$State.CircularValue) _o;
      }
    } else {
      _value = new ASTNode$State.CircularValue();
      strictSubtype_TypeDecl_values.put(_parameters, _value);
      _value.value = Boolean.valueOf(true);
    }
    ASTNode$State state = state();
    boolean new_strictSubtype_TypeDecl_value;
    if (!state.IN_CIRCLE) {
      state.IN_CIRCLE = true;
      int num = state.boundariesCrossed;
      boolean isFinal = this.is$Final();
      // TODO: fixme
      // state().CIRCLE_INDEX = 1;
      do {
        _value.visited = new Integer(state.CIRCLE_INDEX);
        state.CHANGE = false;
        new_strictSubtype_TypeDecl_value = type.strictSupertypeParClassDecl(this);
        if (new_strictSubtype_TypeDecl_value != ((Boolean)_value.value).booleanValue()) {
          state.CHANGE = true;
          _value.value = Boolean.valueOf(new_strictSubtype_TypeDecl_value);
        }
        state.CIRCLE_INDEX++;
      } while (state.CHANGE);
      if (isFinal && num == state().boundariesCrossed) {
        strictSubtype_TypeDecl_values.put(_parameters, new_strictSubtype_TypeDecl_value);
      } else {
        strictSubtype_TypeDecl_values.remove(_parameters);
        state.RESET_CYCLE = true;
        boolean $tmp = type.strictSupertypeParClassDecl(this);
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_strictSubtype_TypeDecl_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_strictSubtype_TypeDecl_value = type.strictSupertypeParClassDecl(this);
      if (state.RESET_CYCLE) {
        strictSubtype_TypeDecl_values.remove(_parameters);
      }
      else if (new_strictSubtype_TypeDecl_value != ((Boolean)_value.value).booleanValue()) {
        state.CHANGE = true;
        _value.value = new_strictSubtype_TypeDecl_value;
      }
      state.INTERMEDIATE_VALUE = true;
      return new_strictSubtype_TypeDecl_value;
    }
    state.INTERMEDIATE_VALUE = true;
    return ((Boolean)_value.value).booleanValue();
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeRawClassDecl(RawClassDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeRawClassDecl_RawClassDecl_value = type.genericDecl().original().strictSubtype(genericDecl().original());

    return strictSupertypeRawClassDecl_RawClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeRawInterfaceDecl(RawInterfaceDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeRawInterfaceDecl_RawInterfaceDecl_value = type.genericDecl().original().strictSubtype(genericDecl().original());

    return strictSupertypeRawInterfaceDecl_RawInterfaceDecl_value;
  }
  /**
   * @attribute syn
   * @aspect StrictSubtype
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/GenericsSubtype.jrag:145
   */
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeParClassDecl(ParClassDecl type) {
    ASTNode$State state = state();
    try {
        if(type.genericDecl().original() == genericDecl().original() &&
           type.getNumArgument() == getNumArgument()) {
          for(int i = 0; i < getNumArgument(); i++)
            if(!type.getArgument(i).type().strictContainedIn(getArgument(i).type()))
              return false;
          if(isNestedType() && type.isNestedType())
            return type.enclosingType().strictSubtype(enclosingType());
          return true;
        }
        return strictSupertypeClassDecl(type);
      }
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeParInterfaceDecl(ParInterfaceDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeParInterfaceDecl_ParInterfaceDecl_value = false;

    return strictSupertypeParInterfaceDecl_ParInterfaceDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isParameterizedType() {
    ASTNode$State state = state();
    boolean isParameterizedType_value = true;

    return isParameterizedType_value;
  }
  /**
   * @attribute syn
   * @aspect GenericsTypeCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:394
   */
  @ASTNodeAnnotation.Attribute
  public boolean sameArgument(ParTypeDecl decl) {
    ASTNode$State state = state();
    try {
        if(this == decl) return true;
        if(genericDecl() != decl.genericDecl())
          return false;
        for(int i = 0; i < getNumArgument(); i++) {
          TypeDecl t1 = getArgument(i).type();
          TypeDecl t2 = decl.getArgument(i).type();
          if(t1 instanceof ParTypeDecl && t2 instanceof ParTypeDecl) {
            if(!((ParTypeDecl)t1).sameArgument((ParTypeDecl)t2))
              return false;
          }
          else {
            if(t1 != t2)
              return false;
          }
        }
        return true;
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:590
   */
  @ASTNodeAnnotation.Attribute
  public boolean sameSignature(Access a) {
    ASTNode$State state = state();
    try {
        if(a instanceof ParTypeAccess) {
          ParTypeAccess ta = (ParTypeAccess)a;
          if(genericDecl() != ta.genericDecl())
            return false;
          if(getNumArgument() != ta.getNumTypeArgument())
            return false;
          for(int i = 0; i < getNumArgument(); i++)
            if(!getArgument(i).type().sameSignature(ta.getTypeArgument(i)))
              return false;
          return true;
        }
        else if(a instanceof TypeAccess && ((TypeAccess)a).isRaw())
          return false;
        return super.sameSignature(a);
      }
    finally {
    }
  }
/**
 * @apilevel internal
 */
private void sameSignature_ArrayList_reset() {
  sameSignature_ArrayList_values = null;
}  
  protected java.util.Map sameSignature_ArrayList_values;
  @ASTNodeAnnotation.Attribute
  public boolean sameSignature(ArrayList list) {
    Object _parameters = list;
    if (sameSignature_ArrayList_values == null) sameSignature_ArrayList_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    ASTNode$State.CircularValue _value;
    if(sameSignature_ArrayList_values.containsKey(_parameters)) {
      Object _o = sameSignature_ArrayList_values.get(_parameters);
      if(!(_o instanceof ASTNode$State.CircularValue)) {
        return ((Boolean)_o).booleanValue();
      } else {
        _value = (ASTNode$State.CircularValue) _o;
      }
    } else {
      _value = new ASTNode$State.CircularValue();
      sameSignature_ArrayList_values.put(_parameters, _value);
      _value.value = Boolean.valueOf(true);
    }
    ASTNode$State state = state();
    boolean new_sameSignature_ArrayList_value;
    if (!state.IN_CIRCLE) {
      state.IN_CIRCLE = true;
      int num = state.boundariesCrossed;
      boolean isFinal = this.is$Final();
      // TODO: fixme
      // state().CIRCLE_INDEX = 1;
      do {
        _value.visited = new Integer(state.CIRCLE_INDEX);
        state.CHANGE = false;
        new_sameSignature_ArrayList_value = sameSignature_compute(list);
        if (new_sameSignature_ArrayList_value != ((Boolean)_value.value).booleanValue()) {
          state.CHANGE = true;
          _value.value = Boolean.valueOf(new_sameSignature_ArrayList_value);
        }
        state.CIRCLE_INDEX++;
      } while (state.CHANGE);
      if (isFinal && num == state().boundariesCrossed) {
        sameSignature_ArrayList_values.put(_parameters, new_sameSignature_ArrayList_value);
      } else {
        sameSignature_ArrayList_values.remove(_parameters);
        state.RESET_CYCLE = true;
        boolean $tmp = sameSignature_compute(list);
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_sameSignature_ArrayList_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_sameSignature_ArrayList_value = sameSignature_compute(list);
      if (state.RESET_CYCLE) {
        sameSignature_ArrayList_values.remove(_parameters);
      }
      else if (new_sameSignature_ArrayList_value != ((Boolean)_value.value).booleanValue()) {
        state.CHANGE = true;
        _value.value = new_sameSignature_ArrayList_value;
      }
      state.INTERMEDIATE_VALUE = true;
      return new_sameSignature_ArrayList_value;
    }
    state.INTERMEDIATE_VALUE = true;
    return ((Boolean)_value.value).booleanValue();
  }
  /**
   * @apilevel internal
   */
  private boolean sameSignature_compute(ArrayList list) {
      if(getNumArgument() != list.size())
        return false;
      for(int i = 0; i < list.size(); i++)
        if(getArgument(i).type() != list.get(i))
          return false;
      return true;
    }
  /**
   * @apilevel internal
   */
  protected int usesTypeVariable_visited = -1;
/**
 * @apilevel internal
 */
private void usesTypeVariable_reset() {
  usesTypeVariable_computed = false;
  usesTypeVariable_initialized = false;
  usesTypeVariable_visited = -1;
}  
  /**
   * @apilevel internal
   */
  protected boolean usesTypeVariable_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean usesTypeVariable_initialized = false;
  /**
   * @apilevel internal
   */
  protected boolean usesTypeVariable_value;
  @ASTNodeAnnotation.Attribute
  public boolean usesTypeVariable() {
    if(usesTypeVariable_computed) {
      return usesTypeVariable_value;
    }
    ASTNode$State state = state();
    boolean new_usesTypeVariable_value;
    if (!usesTypeVariable_initialized) {
      usesTypeVariable_initialized = true;
      usesTypeVariable_value = false;
    }
    if (!state.IN_CIRCLE) {
      state.IN_CIRCLE = true;
      int num = state.boundariesCrossed;
      boolean isFinal = this.is$Final();
      do {
        usesTypeVariable_visited = state.CIRCLE_INDEX;
        state.CHANGE = false;
        new_usesTypeVariable_value = usesTypeVariable_compute();
        if (new_usesTypeVariable_value != usesTypeVariable_value) {
          state.CHANGE = true;
        }
        usesTypeVariable_value = new_usesTypeVariable_value;
        state.CIRCLE_INDEX++;
      } while (state.CHANGE);
      if (isFinal && num == state().boundariesCrossed) {
        usesTypeVariable_computed = true;
      } else {
        state.RESET_CYCLE = true;
        boolean $tmp = usesTypeVariable_compute();
        state.RESET_CYCLE = false;
        usesTypeVariable_computed = false;
        usesTypeVariable_initialized = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return usesTypeVariable_value;
    }
    if(usesTypeVariable_visited != state.CIRCLE_INDEX) {
      usesTypeVariable_visited = state.CIRCLE_INDEX;
      if (state.RESET_CYCLE) {
        usesTypeVariable_computed = false;
        usesTypeVariable_initialized = false;
        usesTypeVariable_visited = -1;
        return usesTypeVariable_value;
      }
      new_usesTypeVariable_value = usesTypeVariable_compute();
      if (new_usesTypeVariable_value != usesTypeVariable_value) {
        state.CHANGE = true;
      }
      usesTypeVariable_value = new_usesTypeVariable_value;
      state.INTERMEDIATE_VALUE = true;
      return usesTypeVariable_value;
    }
    state.INTERMEDIATE_VALUE = true;
    return usesTypeVariable_value;
  }
  /**
   * @apilevel internal
   */
  private boolean usesTypeVariable_compute() {
      if(super.usesTypeVariable())
        return true;
      for(int i = 0; i < getNumArgument(); i++)
        if(getArgument(i).type().usesTypeVariable())
          return true;
      return false;
    }
  @ASTNodeAnnotation.Attribute
  public TypeDecl original() {
    ASTNode$State state = state();
    TypeDecl original_value = genericDecl().original();

    return original_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean sourceTypeDecl_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl sourceTypeDecl_value;
/**
 * @apilevel internal
 */
private void sourceTypeDecl_reset() {
  sourceTypeDecl_computed = false;
  sourceTypeDecl_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl sourceTypeDecl() {
    if(sourceTypeDecl_computed) {
      return sourceTypeDecl_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    sourceTypeDecl_value = genericDecl().original().sourceTypeDecl();
    if (isFinal && num == state().boundariesCrossed) {
      sourceTypeDecl_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return sourceTypeDecl_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean fullName_computed = false;
  /**
   * @apilevel internal
   */
  protected String fullName_value;
/**
 * @apilevel internal
 */
private void fullName_reset() {
  fullName_computed = false;
  fullName_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public String fullName() {
    if(fullName_computed) {
      return fullName_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    fullName_value = fullName_compute();
    if (isFinal && num == state().boundariesCrossed) {
      fullName_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return fullName_value;
  }
  /**
   * @apilevel internal
   */
  private String fullName_compute() {
      if(isNestedType())
        return enclosingType().fullName() + "." + nameWithArgs();
      String packageName = packageName();
      if(packageName.equals(""))
        return nameWithArgs();
      return packageName + "." + nameWithArgs();
    }
  /**
   * @apilevel internal
   */
  protected boolean typeName_computed = false;
  /**
   * @apilevel internal
   */
  protected String typeName_value;
/**
 * @apilevel internal
 */
private void typeName_reset() {
  typeName_computed = false;
  typeName_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public String typeName() {
    if(typeName_computed) {
      return typeName_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeName_value = typeName_compute();
    if (isFinal && num == state().boundariesCrossed) {
      typeName_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeName_value;
  }
  /**
   * @apilevel internal
   */
  private String typeName_compute() {
      if(isNestedType())
        return enclosingType().typeName() + "." + nameWithArgs();
      String packageName = packageName();
      if(packageName.equals("") || packageName.equals(PRIMITIVE_PACKAGE_NAME))
        return nameWithArgs();
      return packageName + "." + nameWithArgs();
    }
  /**
   * @attribute syn
   * @aspect GenericsParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericsParTypeDecl.jrag:30
   */
  @ASTNodeAnnotation.Attribute
  public String nameWithArgs() {
    ASTNode$State state = state();
    try {
        StringBuilder sb = new StringBuilder();
        sb.append(name());
        sb.append("<");
        for (int i = 0; i < getNumArgument(); i++) {
          if (i != 0) {
            sb.append(", ");
          }
          sb.append(getArgument(i).type().fullName());
        }
        sb.append(">");
        return sb.toString();
      }
    finally {
    }
  }
  /**
   * @apilevel internal
   */
  protected boolean unimplementedMethods_computed = false;
  /**
   * @apilevel internal
   */
  protected Collection unimplementedMethods_value;
/**
 * @apilevel internal
 */
private void unimplementedMethods_reset() {
  unimplementedMethods_computed = false;
  unimplementedMethods_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Collection unimplementedMethods() {
    if(unimplementedMethods_computed) {
      return unimplementedMethods_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    unimplementedMethods_value = unimplementedMethods_compute();
    if (isFinal && num == state().boundariesCrossed) {
      unimplementedMethods_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return unimplementedMethods_value;
  }
  /**
   * @apilevel internal
   */
  private Collection unimplementedMethods_compute() {
      HashSet set = new HashSet();
      HashSet result = new HashSet();
      for (Iterator iter = genericDecl().unimplementedMethods().iterator(); iter.hasNext(); ) {
        MethodDecl m = (MethodDecl)iter.next();
        set.add(m.sourceMethodDecl());
      }
      for (Iterator iter = super.unimplementedMethods().iterator(); iter.hasNext(); ) {
        MethodDecl m = (MethodDecl)iter.next();
        if (set.contains(m.sourceMethodDecl())) {
          result.add(m);
        }
      }
      return result;
    }
  /**
   * @apilevel internal
   */
  protected boolean localMethodsSignatureMap_computed = false;
  /**
   * @apilevel internal
   */
  protected HashMap localMethodsSignatureMap_value;
/**
 * @apilevel internal
 */
private void localMethodsSignatureMap_reset() {
  localMethodsSignatureMap_computed = false;
  localMethodsSignatureMap_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public HashMap localMethodsSignatureMap() {
    if(localMethodsSignatureMap_computed) {
      return localMethodsSignatureMap_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    localMethodsSignatureMap_value = localMethodsSignatureMap_compute();
    if (true) {
      localMethodsSignatureMap_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return localMethodsSignatureMap_value;
  }
  /**
   * @apilevel internal
   */
  private HashMap localMethodsSignatureMap_compute() {
      HashMap map = new HashMap();
      for(Iterator iter = original().localMethodsIterator(); iter.hasNext(); ) {
        MethodDecl decl = (MethodDecl)iter.next();
  
        if(!decl.isStatic() && (decl.usesTypeVariable() || isRawType())) {
          BodyDecl copyDecl = ((BodyDeclList)getBodyDeclList()).localMethodSignatureCopy(decl, this);
          decl = (MethodDecl) copyDecl;
        }
        map.put(decl.signature(), decl);
  
      }
      return map;
    }
  protected java.util.Map localFields_String_values;
/**
 * @apilevel internal
 */
private void localFields_String_reset() {
  localFields_String_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public SimpleSet localFields(String name) {
    Object _parameters = name;
    if (localFields_String_values == null) localFields_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(localFields_String_values.containsKey(_parameters)) {
      return (SimpleSet)localFields_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    SimpleSet localFields_String_value = localFields_compute(name);
    if (true) {
      localFields_String_values.put(_parameters, localFields_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return localFields_String_value;
  }
  /**
   * @apilevel internal
   */
  private SimpleSet localFields_compute(String name) {
      SimpleSet set = SimpleSet.emptySet;
      for(Iterator iter = original().localFields(name).iterator(); iter.hasNext(); ) {
        FieldDeclaration f = (FieldDeclaration)iter.next();
  
        if(!f.isStatic() && (f.usesTypeVariable() || isRawType())) {
          BodyDecl fCopy = ((BodyDeclList)getBodyDeclList()).localFieldCopy(f, this);
          f = (FieldDeclaration) fCopy;
        }
        set = set.add(f);
  
      }
      return set;
    }
/**
 * @apilevel internal
 */
private void localTypeDecls_String_reset() {
  localTypeDecls_String_values = null;
}  
  protected java.util.Map localTypeDecls_String_values;
  @ASTNodeAnnotation.Attribute
  public SimpleSet localTypeDecls(String name) {
    Object _parameters = name;
    if (localTypeDecls_String_values == null) localTypeDecls_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    ASTNode$State.CircularValue _value;
    if(localTypeDecls_String_values.containsKey(_parameters)) {
      Object _o = localTypeDecls_String_values.get(_parameters);
      if(!(_o instanceof ASTNode$State.CircularValue)) {
        return (SimpleSet)_o;
      } else {
        _value = (ASTNode$State.CircularValue) _o;
      }
    } else {
      _value = new ASTNode$State.CircularValue();
      localTypeDecls_String_values.put(_parameters, _value);
      _value.value = SimpleSet.emptySet;
    }
    ASTNode$State state = state();
    SimpleSet new_localTypeDecls_String_value;
    if (!state.IN_CIRCLE) {
      state.IN_CIRCLE = true;
      int num = state.boundariesCrossed;
      boolean isFinal = this.is$Final();
      // TODO: fixme
      // state().CIRCLE_INDEX = 1;
      do {
        _value.visited = new Integer(state.CIRCLE_INDEX);
        state.CHANGE = false;
        new_localTypeDecls_String_value = localTypeDecls_compute(name);
        if ((new_localTypeDecls_String_value==null && (SimpleSet)_value.value!=null) || (new_localTypeDecls_String_value!=null && !new_localTypeDecls_String_value.equals((SimpleSet)_value.value))) {
          state.CHANGE = true;
          _value.value = new_localTypeDecls_String_value;
        }
        state.CIRCLE_INDEX++;
      } while (state.CHANGE);
      if (true) {
        localTypeDecls_String_values.put(_parameters, new_localTypeDecls_String_value);
      } else {
        localTypeDecls_String_values.remove(_parameters);
        state.RESET_CYCLE = true;
        SimpleSet $tmp = localTypeDecls_compute(name);
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_localTypeDecls_String_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_localTypeDecls_String_value = localTypeDecls_compute(name);
      if (state.RESET_CYCLE) {
        localTypeDecls_String_values.remove(_parameters);
      }
      else if ((new_localTypeDecls_String_value==null && (SimpleSet)_value.value!=null) || (new_localTypeDecls_String_value!=null && !new_localTypeDecls_String_value.equals((SimpleSet)_value.value))) {
        state.CHANGE = true;
        _value.value = new_localTypeDecls_String_value;
      }
      state.INTERMEDIATE_VALUE = true;
      return new_localTypeDecls_String_value;
    }
    state.INTERMEDIATE_VALUE = true;
    return (SimpleSet)_value.value;
  }
  /**
   * @apilevel internal
   */
  private SimpleSet localTypeDecls_compute(String name) {
      SimpleSet set = SimpleSet.emptySet;
      for(Iterator iter = original().localTypeDecls(name).iterator(); iter.hasNext(); ) {
        TypeDecl t = (TypeDecl)iter.next();
  
        if(t.isStatic()) {
          set = set.add(t);
        } else if (t instanceof ClassDecl) {
          MemberClassDecl copy = ((BodyDeclList)getBodyDeclList()).localClassDeclCopy((ClassDecl)t, this);
          set = set.add(copy.getClassDecl());
        } else if (t instanceof InterfaceDecl) {
          MemberInterfaceDecl copy = ((BodyDeclList)getBodyDeclList()).localInterfaceDeclCopy((InterfaceDecl)t, this);
          set = set.add(copy.getInterfaceDecl());
        }
      }
      return set;
    }
  /**
   * @apilevel internal
   */
  protected boolean constructors_computed = false;
  /**
   * @apilevel internal
   */
  protected Collection constructors_value;
/**
 * @apilevel internal
 */
private void constructors_reset() {
  constructors_computed = false;
  constructors_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Collection constructors() {
    if(constructors_computed) {
      return constructors_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    constructors_value = constructors_compute();
    if (isFinal && num == state().boundariesCrossed) {
      constructors_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return constructors_value;
  }
  /**
   * @apilevel internal
   */
  private Collection constructors_compute() {
      Collection set = new ArrayList();
      for(Iterator iter = original().constructors().iterator(); iter.hasNext(); ) {
        ConstructorDecl c = (ConstructorDecl)iter.next();
  
        BodyDecl b = ((BodyDeclList)getBodyDeclList()).constructorCopy(c, this);
        set.add(b);
      }
      return set;
    }
  /**
   * @attribute inh
   * @aspect GenericsParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericsParTypeDecl.jrag:46
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl genericDecl() {
    if(genericDecl_computed) {
      return genericDecl_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    genericDecl_value = getParent().Define_TypeDecl_genericDecl(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      genericDecl_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return genericDecl_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean genericDecl_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl genericDecl_value;
/**
 * @apilevel internal
 */
private void genericDecl_reset() {
  genericDecl_computed = false;
  genericDecl_value = null;
}  
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:489
   * @apilevel internal
   */
  public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
    if (caller == getArgumentListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return NameType.TYPE_NAME;
    }
    else {
      return super.Define_NameType_nameType(caller, child);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericsParTypeDecl.jrag:50
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_genericDecl(ASTNode caller, ASTNode child) {
    if (caller == getBodyDeclListNoTransform()) {
      int index = caller.getIndexOfChild(child);
      {
    if(getBodyDecl(index) instanceof MemberTypeDecl) {
      MemberTypeDecl m = (MemberTypeDecl)getBodyDecl(index);
      return extractSingleType(genericDecl().memberTypes(m.typeDecl().name()));
    }
    return genericDecl();
  }
    }
    else {
      return getParent().Define_TypeDecl_genericDecl(this, caller);
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
