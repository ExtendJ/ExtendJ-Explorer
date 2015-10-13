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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/grammar/Generics.ast:46
 * @production LUBType : {@link ReferenceType} ::= <span class="component">{@link Modifiers}</span> <span class="component">&lt;ID:String&gt;</span> <span class="component">{@link BodyDecl}*</span> <span class="component">TypeBound:{@link Access}*</span>;

 */
public class LUBType extends ReferenceType implements Cloneable {
  /**
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:1238
   */
  public HashSet implementedInterfaces(){
       HashSet ret = new HashSet();
       for (int i = 0; i < getNumTypeBound(); i++) {
           ret.addAll(getTypeBound(i).type().implementedInterfaces());
       }
       return ret;
   }
  /**
   * @aspect GenericMethodsInference
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericMethodsInference.jrag:668
   */
  public static HashSet EC(ArrayList list) {
      HashSet result = new HashSet();
      boolean first = true;
      for(Iterator iter = list.iterator(); iter.hasNext(); ) {
        TypeDecl U = (TypeDecl)iter.next();
        // erased supertype set of U
        HashSet EST = LUBType.EST(U);
        if(first) {
          result.addAll(EST);
          first = false;
        }
        else
          result.retainAll(EST);
      }
      return result;
    }
  /**
   * The minimal erased candidate set for Tj
   * is MEC = {V | V in EC, forall  W != V in EC, not W <: V}
   * @return minimal erased candidate set for Tj
   * @aspect GenericMethodsInference
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericMethodsInference.jrag:690
   */
  public static HashSet MEC(ArrayList list) {
      HashSet EC = LUBType.EC(list);
      if(EC.size() == 1)
        return EC;
      HashSet MEC = new HashSet();
      for(Iterator iter = EC.iterator(); iter.hasNext(); ) {
        TypeDecl V = (TypeDecl)iter.next();
        boolean keep = true;
        for(Iterator i2 = EC.iterator(); i2.hasNext(); ) {
          TypeDecl W = (TypeDecl)i2.next();
          if(!(V instanceof TypeVariable) && V != W && W.instanceOf(V))
            keep = false;
        }
        if(keep)
          MEC.add(V);
      }
      return MEC;
    }
  /**
   * relevant invocations of G, Inv(G)
   * Inv(G) = {V | 1 <= i <= k, V in ST(Ui), V = G<...>}
   * @return set of relevant invocations of G, Inv(G)
   * @aspect GenericMethodsInference
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericMethodsInference.jrag:714
   */
  public static HashSet Inv(TypeDecl G, ArrayList Us) {
      HashSet result = new HashSet();
      for(Iterator iter = Us.iterator(); iter.hasNext(); ) {
        TypeDecl U = (TypeDecl)iter.next();
        for(Iterator i2 = LUBType.ST(U).iterator(); i2.hasNext(); ) {
          TypeDecl V = (TypeDecl)i2.next();
          if(V instanceof ParTypeDecl && !V.isRawType() && ((ParTypeDecl)V).genericDecl() == G)
            result.add(V);
        }
      }
      return result;
    }
  /**
   * @return least containing invocation (lci)
   * @aspect GenericMethodsInference
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericMethodsInference.jrag:730
   */
  public TypeDecl lci(HashSet set, TypeDecl G) {
      ArrayList list = new ArrayList();
      boolean first = true;
      for(Iterator iter = set.iterator(); iter.hasNext(); ) {
        ParTypeDecl decl = (ParTypeDecl)iter.next();
        if(first) {
          first = false;
          for(int i = 0; i < decl.getNumArgument(); i++)
            list.add(decl.getArgument(i).type());
        }
        else {
          for(int i = 0; i < decl.getNumArgument(); i++)
            list.set(i, lcta((TypeDecl)list.get(i), decl.getArgument(i).type()));
        }
      }
      return ((GenericTypeDecl)G).lookupParTypeDecl(list);
    }
  /**
   * least containing type arguments
   * @aspect GenericMethodsInference
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericMethodsInference.jrag:751
   */
  public TypeDecl lcta(TypeDecl X, TypeDecl Y) {
      //System.err.println("Computing lcta for " + X.typeName() + " and " + Y.typeName());
      if(!X.isWildcard() && !Y.isWildcard()) {
        TypeDecl U = X;
        TypeDecl V = Y;
        return U == V ? U : lub(U, V).asWildcardExtends();
      }
      else if(!X.isWildcard() && Y instanceof WildcardExtendsType) {
        TypeDecl U = X;
        TypeDecl V = ((WildcardExtendsType)Y).getAccess().type();
        return lub(U, V).asWildcardExtends();
      }
      else if(!Y.isWildcard() && X instanceof WildcardExtendsType) {
        TypeDecl U = Y;
        TypeDecl V = ((WildcardExtendsType)X).getAccess().type();
        return lub(U, V).asWildcardExtends();
      }
      else if(!X.isWildcard() && Y instanceof WildcardSuperType) {
        TypeDecl U = X;
        TypeDecl V = ((WildcardSuperType)Y).getAccess().type();
        ArrayList bounds = new ArrayList();
        bounds.add(U);
        bounds.add(V);
        return GLBTypeFactory.glb(bounds).asWildcardSuper();
      }
      else if(!Y.isWildcard() && X instanceof WildcardSuperType) {
        TypeDecl U = Y;
        TypeDecl V = ((WildcardSuperType)X).getAccess().type();
        ArrayList bounds = new ArrayList();
        bounds.add(U);
        bounds.add(V);
        return GLBTypeFactory.glb(bounds).asWildcardSuper();
      }
      else if(X instanceof WildcardExtendsType && Y instanceof WildcardExtendsType) {
        TypeDecl U = ((WildcardExtendsType)X).getAccess().type();
        TypeDecl V = ((WildcardExtendsType)Y).getAccess().type();
        return lub(U, V).asWildcardExtends();
      }
      else if(X instanceof WildcardExtendsType && Y instanceof WildcardSuperType) {
        TypeDecl U = ((WildcardExtendsType)X).getAccess().type();
        TypeDecl V = ((WildcardSuperType)Y).getAccess().type();
        return U == V ? U : U.typeWildcard();
      }
      else if(Y instanceof WildcardExtendsType && X instanceof WildcardSuperType) {
        TypeDecl U = ((WildcardExtendsType)Y).getAccess().type();
        TypeDecl V = ((WildcardSuperType)X).getAccess().type();
        return U == V ? U : U.typeWildcard();
      }
      else if(X instanceof WildcardSuperType && Y instanceof WildcardSuperType) {
        TypeDecl U = ((WildcardSuperType)X).getAccess().type();
        TypeDecl V = ((WildcardSuperType)Y).getAccess().type();
        ArrayList bounds = new ArrayList();
        bounds.add(U);
        bounds.add(V);
        return GLBTypeFactory.glb(bounds).asWildcardSuper();
      }
      else
        throw new Error("lcta not defined for (" + X.getClass().getName() + ", " + Y.getClass().getName() + ")");
    }
  /**
   * @aspect GenericMethodsInference
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericMethodsInference.jrag:811
   */
  public TypeDecl lub(TypeDecl X, TypeDecl Y) {
      ArrayList list = new ArrayList(2);
      list.add(X);
      list.add(Y);
      return lub(list);
    }
  /**
   * @aspect GenericMethodsInference
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericMethodsInference.jrag:818
   */
  public TypeDecl lub(ArrayList list) {
      return lookupLUBType(list);
    }
  /**
   * @aspect GenericMethodsInference
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericMethodsInference.jrag:823
   */
  public static HashSet EST(TypeDecl t) {
      HashSet result = new HashSet();
      for(Iterator iter = LUBType.ST(t).iterator(); iter.hasNext(); ) {
        TypeDecl typeDecl = (TypeDecl)iter.next();
        if(typeDecl instanceof TypeVariable)
          result.add(typeDecl);
        else
          result.add(typeDecl.erasure());
      }
      return result;
    }
  /**
   * @return supertype set of T
   * @aspect GenericMethodsInference
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericMethodsInference.jrag:838
   */
  public static HashSet ST(TypeDecl t) {
      HashSet result = new HashSet();
      LUBType.addSupertypes(result, t);
      return result;
    }
  /**
   * @aspect GenericMethodsInference
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericMethodsInference.jrag:844
   */
  public static void addSupertypes(HashSet set, TypeDecl t) {
      set.add(t);
      if(t instanceof ClassDecl) {
        ClassDecl type = (ClassDecl)t;
        if(type.hasSuperclass()) {
          addSupertypes(set, type.superclass());
        }
        for(int i = 0; i < type.getNumImplements(); i++) {
          addSupertypes(set, type.getImplements(i).type());
        }
      }
      else if(t instanceof InterfaceDecl) {
        InterfaceDecl type = (InterfaceDecl)t;
        for(int i = 0; i < type.getNumSuperInterface(); i++) {
          addSupertypes(set, type.getSuperInterface(i).type());
        }
        if(type.getNumSuperInterface() == 0)
          set.add(type.typeObject());
      }
      else if(t instanceof TypeVariable) {
        TypeVariable type = (TypeVariable)t;
        for(int i = 0; i < type.getNumTypeBound(); i++) {
          addSupertypes(set, type.getTypeBound(i).type());
        }
        if(type.getNumTypeBound() == 0)
          set.add(type.typeObject());
      }
      else if(t instanceof LUBType) {
        LUBType type = (LUBType)t;
        for(int i = 0; i < type.getNumTypeBound(); i++) {
          addSupertypes(set, type.getTypeBound(i).type());
        }
        if(type.getNumTypeBound() == 0)
          set.add(type.typeObject());
      }
      else
        throw new Error("Operation not supported for " + t.fullName() + ", " + t.getClass().getName());
    }
  /**
   * @declaredat ASTNode:1
   */
  public LUBType() {
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
    setChild(new List(), 1);
    setChild(new List(), 2);
  }
  /**
   * @declaredat ASTNode:15
   */
  public LUBType(Modifiers p0, String p1, List<BodyDecl> p2, List<Access> p3) {
    setChild(p0, 0);
    setID(p1);
    setChild(p2, 1);
    setChild(p3, 2);
  }
  /**
   * @declaredat ASTNode:21
   */
  public LUBType(Modifiers p0, beaver.Symbol p1, List<BodyDecl> p2, List<Access> p3) {
    setChild(p0, 0);
    setID(p1);
    setChild(p2, 1);
    setChild(p3, 2);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:30
   */
  protected int numChildren() {
    return 3;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:36
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:42
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    lub_reset();
    subtype_TypeDecl_reset();
    strictSubtype_TypeDecl_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:51
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:57
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:63
   */
  public LUBType clone() throws CloneNotSupportedException {
    LUBType node = (LUBType) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:70
   */
  public LUBType copy() {
    try {
      LUBType node = (LUBType) clone();
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
   * @declaredat ASTNode:89
   */
  public LUBType fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:98
   */
  public LUBType treeCopyNoTransform() {
    LUBType tree = (LUBType) copy();
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
   * @declaredat ASTNode:118
   */
  public LUBType treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:125
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node)  && (tokenString_ID == ((LUBType)node).tokenString_ID);    
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
   * Replaces the BodyDecl list.
   * @param list The new list node to be used as the BodyDecl list.
   * @apilevel high-level
   */
  public void setBodyDeclList(List<BodyDecl> list) {
    setChild(list, 1);
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
   * Retrieves the BodyDecl list.
   * @return The node representing the BodyDecl list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="BodyDecl")
  public List<BodyDecl> getBodyDeclList() {
    List<BodyDecl> list = (List<BodyDecl>) getChild(1);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the BodyDecl list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the BodyDecl list.
   * @apilevel low-level
   */
  public List<BodyDecl> getBodyDeclListNoTransform() {
    return (List<BodyDecl>) getChildNoTransform(1);
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
  /**
   * Replaces the TypeBound list.
   * @param list The new list node to be used as the TypeBound list.
   * @apilevel high-level
   */
  public void setTypeBoundList(List<Access> list) {
    setChild(list, 2);
  }
  /**
   * Retrieves the number of children in the TypeBound list.
   * @return Number of children in the TypeBound list.
   * @apilevel high-level
   */
  public int getNumTypeBound() {
    return getTypeBoundList().getNumChild();
  }
  /**
   * Retrieves the number of children in the TypeBound list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the TypeBound list.
   * @apilevel low-level
   */
  public int getNumTypeBoundNoTransform() {
    return getTypeBoundListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the TypeBound list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the TypeBound list.
   * @apilevel high-level
   */
  public Access getTypeBound(int i) {
    return (Access) getTypeBoundList().getChild(i);
  }
  /**
   * Check whether the TypeBound list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasTypeBound() {
    return getTypeBoundList().getNumChild() != 0;
  }
  /**
   * Append an element to the TypeBound list.
   * @param node The element to append to the TypeBound list.
   * @apilevel high-level
   */
  public void addTypeBound(Access node) {
    List<Access> list = (parent == null || state == null) ? getTypeBoundListNoTransform() : getTypeBoundList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addTypeBoundNoTransform(Access node) {
    List<Access> list = getTypeBoundListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the TypeBound list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setTypeBound(Access node, int i) {
    List<Access> list = getTypeBoundList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the TypeBound list.
   * @return The node representing the TypeBound list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="TypeBound")
  public List<Access> getTypeBoundList() {
    List<Access> list = (List<Access>) getChild(2);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the TypeBound list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the TypeBound list.
   * @apilevel low-level
   */
  public List<Access> getTypeBoundListNoTransform() {
    return (List<Access>) getChildNoTransform(2);
  }
  /**
   * Retrieves the TypeBound list.
   * @return The node representing the TypeBound list.
   * @apilevel high-level
   */
  public List<Access> getTypeBounds() {
    return getTypeBoundList();
  }
  /**
   * Retrieves the TypeBound list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the TypeBound list.
   * @apilevel low-level
   */
  public List<Access> getTypeBoundsNoTransform() {
    return getTypeBoundListNoTransform();
  }
  /**
   * @attribute syn
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:1226
   */
  @ASTNodeAnnotation.Attribute
  public String typeName() {
    ASTNode$State state = state();
    try {
        if (getNumTypeBound() == 0) {
          return "<NOTYPE>";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getTypeBound(0).type().typeName());
        for (int i = 1; i < getNumTypeBound(); i++) {
          sb.append(" & " + getTypeBound(i).type().typeName());
        }
        return sb.toString();
      }
    finally {
    }
  }
  /**
   * @apilevel internal
   */
  protected boolean lub_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl lub_value;
/**
 * @apilevel internal
 */
private void lub_reset() {
  lub_computed = false;
  lub_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl lub() {
    if(lub_computed) {
      return lub_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    lub_value = lub_compute();
    if (isFinal && num == state().boundariesCrossed) {
      lub_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return lub_value;
  }
  /**
   * @apilevel internal
   */
  private TypeDecl lub_compute() {
      ArrayList list = new ArrayList();
      for(int i = 0; i < getNumTypeBound(); i++)
        list.add(getTypeBound(i).type());
      ArrayList bounds = new ArrayList();
      for(Iterator iter = LUBType.MEC(list).iterator(); iter.hasNext(); ) {
        TypeDecl W = (TypeDecl)iter.next();
        TypeDecl C = W instanceof GenericTypeDecl ? lci(Inv(W, list), W) : W;
        bounds.add(C);
      }
      if(bounds.size() == 1)
        return (TypeDecl)bounds.iterator().next();
      return lookupLUBType(bounds);
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
        new_subtype_TypeDecl_value = type.supertypeLUBType(this);
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
        boolean $tmp = type.supertypeLUBType(this);
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_subtype_TypeDecl_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_subtype_TypeDecl_value = type.supertypeLUBType(this);
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
  public boolean supertypeClassDecl(ClassDecl type) {
    ASTNode$State state = state();
    boolean supertypeClassDecl_ClassDecl_value = type.subtype(lub());

    return supertypeClassDecl_ClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeInterfaceDecl(InterfaceDecl type) {
    ASTNode$State state = state();
    boolean supertypeInterfaceDecl_InterfaceDecl_value = type.subtype(lub());

    return supertypeInterfaceDecl_InterfaceDecl_value;
  }
  /**
   * @attribute syn
   * @aspect GenericsSubtype
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericsSubtype.jrag:366
   */
  @ASTNodeAnnotation.Attribute
  public boolean supertypeGLBType(GLBType type) {
    ASTNode$State state = state();
    try {
        ArrayList bounds = new ArrayList(getNumTypeBound());
        for (int i = 0; i < getNumTypeBound(); i++) {
          bounds.add(getTypeBound(i));
        }
        return type == lookupGLBType(bounds);
      }
    finally {
    }
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
        new_strictSubtype_TypeDecl_value = type.strictSupertypeLUBType(this);
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
        boolean $tmp = type.strictSupertypeLUBType(this);
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_strictSubtype_TypeDecl_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_strictSubtype_TypeDecl_value = type.strictSupertypeLUBType(this);
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
  public boolean strictSupertypeClassDecl(ClassDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeClassDecl_ClassDecl_value = type.strictSubtype(lub());

    return strictSupertypeClassDecl_ClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeInterfaceDecl(InterfaceDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeInterfaceDecl_InterfaceDecl_value = type.strictSubtype(lub());

    return strictSupertypeInterfaceDecl_InterfaceDecl_value;
  }
  /**
   * @attribute syn
   * @aspect StrictSubtype
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/GenericsSubtype.jrag:304
   */
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeGLBType(GLBType type) {
    ASTNode$State state = state();
    try {
        ArrayList bounds = new ArrayList(getNumTypeBound());
        for (int i = 0; i < getNumTypeBound(); i++) {
          bounds.add(getTypeBound(i));
        }
        return type == lookupGLBType(bounds);
      }
    finally {
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
