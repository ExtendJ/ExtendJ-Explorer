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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:40
 * @production TypeDecl : {@link ASTNode} ::= <span class="component">{@link Modifiers}</span> <span class="component">&lt;ID:String&gt;</span> <span class="component">{@link BodyDecl}*</span>;

 */
public abstract class TypeDecl extends ASTNode<ASTNode> implements Cloneable, SimpleSet, Iterator, VariableScope {
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:78
   */
  protected void ppBodyDecls(StringBuffer sb) {
    sb.append(" {");
    for(int i=0; i < getNumBodyDecl(); i++) {
      getBodyDecl(i).prettyPrint(sb);
    }
    sb.append(indent() + "}");
  }
  /**
   * @aspect Generics
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:198
   */
  public TypeDecl makeGeneric(Signatures.ClassSignature s) {
    return this;
  }
  /**
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:706
   */
  public TypeDecl substitute(TypeVariable typeVariable) {
    if(isTopLevelType())
      return typeVariable;
    return enclosingType().substitute(typeVariable);
  }
  /**
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:744
   */
  public Access substitute(Parameterization parTypeDecl) {
    if(parTypeDecl instanceof ParTypeDecl && ((ParTypeDecl)parTypeDecl).genericDecl() == this)
      return ((TypeDecl)parTypeDecl).createBoundAccess();
    if(isTopLevelType())
      return createBoundAccess();
    return enclosingType().substitute(parTypeDecl).qualifiesAccess(new TypeAccess(name()));
  }
  /**
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:784
   */
  public Access substituteReturnType(Parameterization parTypeDecl) {
    return substitute(parTypeDecl);
  }
  /**
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:828
   */
  public Access substituteParameterType(Parameterization parTypeDecl) {
    return substitute(parTypeDecl);
  }
  /**
   * @aspect AnonymousClasses
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/AnonymousClasses.jrag:29
   */
  public int anonymousIndex = 0;
  /**
   * @aspect AnonymousClasses
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/AnonymousClasses.jrag:46
   */
  public int nextAnonymousIndex() {
    if(isNestedType())
      return enclosingType().nextAnonymousIndex();
    return anonymousIndex++;
  }
  /**
   * @aspect BoundNames
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BoundNames.jrag:24
   */
  public MethodDecl addMemberMethod(MethodDecl m) {
    addBodyDecl(m);
    return (MethodDecl)getBodyDecl(getNumBodyDecl()-1);
    /*
    HashMap map = methodsNameMap();
    ArrayList list = (ArrayList)map.get(m.name());
    if(list == null) {
      list = new ArrayList(4);
      map.put(m.name(), list);
    }
    list.add(m);
    if(!memberMethods(m.name()).contains(m))
      throw new Error("The method " + m.signature() + " added to " + typeName() + " can not be found using lookupMemberMethod");
    */
  }
  /**
   * @aspect BoundNames
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BoundNames.jrag:40
   */
  public ConstructorDecl addConstructor(ConstructorDecl c) {
    addBodyDecl(c);
    return (ConstructorDecl)getBodyDecl(getNumBodyDecl()-1);
  }
  /**
   * @aspect BoundNames
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BoundNames.jrag:45
   */
  public ClassDecl addMemberClass(ClassDecl c) {
    addBodyDecl(new MemberClassDecl(c));
    return ((MemberClassDecl)getBodyDecl(getNumBodyDecl()-1)).getClassDecl();
  }
  /**
   * @aspect BoundNames
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BoundNames.jrag:52
   */
  public FieldDeclaration addMemberField(FieldDeclaration f) {
    addBodyDecl(f);
    return (FieldDeclaration)getBodyDecl(getNumBodyDecl()-1);
    //if(!memberFields(f.name()).contains(f))
    //  throw new Error("The field " + f.name() + " added to " + typeName() + " can not be found using lookupMemberField");
  }
  /**
   * @aspect BoundNames
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BoundNames.jrag:90
   */
  public TypeAccess createBoundAccess() {
    return new BoundTypeAccess("", name(), this);
  }
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DataStructures.jrag:135
   */
  public SimpleSet add(Object o) {
    return new SimpleSetImpl().add(this).add(o);
  }
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DataStructures.jrag:139
   */
  public boolean isSingleton() { return true; }
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DataStructures.jrag:140
   */
  public boolean isSingleton(Object o) { return contains(o); }
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DataStructures.jrag:143
   */
  private TypeDecl iterElem;
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DataStructures.jrag:144
   */
  public Iterator iterator() { iterElem = this; return this; }
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DataStructures.jrag:145
   */
  public boolean hasNext() { return iterElem != null; }
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DataStructures.jrag:146
   */
  public Object next() { Object o = iterElem; iterElem = null; return o; }
  /**
   * @aspect DataStructures
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DataStructures.jrag:147
   */
  public void remove() { throw new UnsupportedOperationException(); }
  /**
   * @aspect DeclareBeforeUse
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DeclareBeforeUse.jrag:31
   */
  public boolean declaredBeforeUse(Variable decl, ASTNode use) {
    int indexDecl = ((ASTNode)decl).varChildIndex(this);
    int indexUse = use.varChildIndex(this);
    return indexDecl < indexUse;
  }
  /**
   * @aspect DeclareBeforeUse
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DeclareBeforeUse.jrag:37
   */
  public boolean declaredBeforeUse(Variable decl, int indexUse) {
    int indexDecl = ((ASTNode)decl).varChildIndex(this);
    return indexDecl < indexUse;
  }
  /**
   * @aspect ConstructorLookup
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupConstructor.jrag:93
   */
  public ConstructorDecl lookupConstructor(ConstructorDecl signature) {
    for (Iterator iter = constructors().iterator(); iter.hasNext(); ) {
      ConstructorDecl decl = (ConstructorDecl)iter.next();
      if (decl.sameSignature(signature)) {
        return decl;
      }
    }
    return null;
  }
  /**
   * @return true if the method access may access the method
   * @aspect MethodDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupMethod.jrag:234
   */
  public boolean mayAccess(MethodAccess access, MethodDecl method) {
    if (instanceOf(method.hostType())
        && access.qualifier().type().instanceOf(this))
        return true;

    if (isNestedType())
      return enclosingType().mayAccess(access, method);
    else
      return false;
  }
  /**
   * Iterate over all local methods in the type.
   * @return method iterator
   * @aspect MemberMethods
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupMethod.jrag:285
   */
  public Iterator<MethodDecl> localMethodsIterator() {
    return new Iterator<MethodDecl>() {
      private Iterator<SimpleSet> outer = localMethodsSignatureMap().values().iterator();
      private Iterator inner = null;
      public boolean hasNext() {
        if ((inner == null || !inner.hasNext()) && outer.hasNext()) {
          inner = outer.next().iterator();
        }
        return inner == null ? false : inner.hasNext();
      }
      public MethodDecl next() {
        return (MethodDecl) inner.next();
      }
      public void remove() { throw new UnsupportedOperationException(); }
    };
  }
  /**
   * Iterate over all member methods in the type.
   * @return method iterator
   * @aspect MemberMethods
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupMethod.jrag:371
   */
  public Iterator<MethodDecl> methodsIterator() {
    return new Iterator<MethodDecl>() {
      private Iterator<SimpleSet> outer = methodsSignatureMap().values().iterator();
      private Iterator inner = null;
      public boolean hasNext() {
        if((inner == null || !inner.hasNext()) && outer.hasNext())
          inner = outer.next().iterator();
        return inner != null ? inner.hasNext() : false;
      }
      public MethodDecl next() {
        return (MethodDecl) inner.next();
      }
      public void remove() { throw new UnsupportedOperationException(); }
    };
  }
  /**
   * @aspect MemberMethods
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupMethod.jrag:452
   */
  protected boolean allMethodsAbstract(SimpleSet set) {
    if (set == null) {
      return true;
    }
    for (Iterator iter = set.iterator(); iter.hasNext(); ) {
      MethodDecl m = (MethodDecl)iter.next();
      if (!m.isAbstract()) {
        return false;
      }
    }
    return true;
  }
  /**
   * @return true if the expression may access the field
   * @aspect VariableScope
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupVariable.jrag:245
   */
  public boolean mayAccess(Expr expr, FieldDeclaration field) {
    if (instanceOf(field.hostType())) {
      if (!field.isInstanceVariable()
          || expr.isSuperAccess()
          || expr.type().instanceOf(this))
        return true;
    }

    if (isNestedType()) {
      return enclosingType().mayAccess(expr, field);
    } else {
      return false;
    }
  }
  /**
   * @aspect Fields
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupVariable.jrag:347
   */
  public Iterator fieldsIterator() {
    return new Iterator() {
      private Iterator outer = memberFieldsMap().values().iterator();
      private Iterator inner = null;
      public boolean hasNext() {
        if((inner == null || !inner.hasNext()) && outer.hasNext())
          inner = ((SimpleSet)outer.next()).iterator();
        return inner != null ? inner.hasNext() : false;
      }
      public Object next() {
        return inner.next();
      }
      public void remove() { throw new UnsupportedOperationException(); }
    };
  }
  /**
   * @aspect Modifiers
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:68
   */
  public void refined_Modifiers_TypeDecl_checkModifiers() {
    super.checkModifiers();
    // 8.1.1
    if(isPublic() && !isTopLevelType() && !isMemberType())
      error("public pertains only to top level types and member types");

    // 8.1.1
    if((isProtected() || isPrivate()) && !(isMemberType() && enclosingType().isClassDecl()))
      error("protected and private may only be used on member types within a directly enclosing class declaration");

    // 8.1.1
    if(isStatic() && !isMemberType())
      error("static pertains only to member types");


    // 8.4.3.1
    // 8.1.1.1
    if (!isAbstract() && hasAbstract()) {
      StringBuilder sb = new StringBuilder();
      sb.append("" + name() + " is not declared abstract but contains abstract members: \n");
      for (Iterator iter = unimplementedMethods().iterator(); iter.hasNext(); ) {
        MethodDecl m = (MethodDecl)iter.next();
        sb.append("  " + m.signature() + " in " + m.hostType().typeName() + "\n");
      }
      error(sb.toString());
    }
  }
  /**
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:277
   */
  public void nameCheck() {
    if(isTopLevelType() && lookupType(packageName(), name()) != this)
      error("duplicate type " + name() + " in package " + packageName());

    if(!isTopLevelType() && !isAnonymous() && !isLocalClass() && extractSingleType(enclosingType().memberTypes(name())) != this)
      error("duplicate member type " + name() + " in type " + enclosingType().typeName());

    // 14.3
    if(isLocalClass()) {
      TypeDecl typeDecl = extractSingleType(lookupType(name()));
      if(typeDecl != null && typeDecl != this && typeDecl.isLocalClass() && enclosingBlock() == typeDecl.enclosingBlock())
        error("local class named " + name() + " may not be redeclared as a local class in the same block");
    }

    if(!packageName().equals("") && hasPackage(fullName()))
      error("type name conflicts with a package using the same name: " + name());

    // 8.1 & 9.1
    if(hasEnclosingTypeDecl(name())) {
      error("type may not have the same simple name as an enclosing type declaration");
    }
  }
  /**
   * @aspect CreateQualifiedAccesses
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/QualifiedNames.jrag:97
   */
  public Access createQualifiedAccess() {
    if(isLocalClass() || isAnonymous()) {
      return new TypeAccess(name());
    }
    else if(!isTopLevelType()) {
      return enclosingType().createQualifiedAccess().qualifiesAccess(new TypeAccess(name()));
    }
    else {
      return new TypeAccess(packageName(), name());
    }
  }
  /**
   * @aspect TypeAnalysis
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:234
   */
  public FieldDeclaration findSingleVariable(String name) {
    return (FieldDeclaration)memberFields(name).iterator().next();
  }
  /**
   * @aspect TypeHierarchyCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeHierarchyCheck.jrag:184
   */
  public void refined_TypeHierarchyCheck_TypeDecl_typeCheck() {
    // 8.4.6.4 & 9.4.1
    for (Iterator iter1 = localMethodsIterator(); iter1.hasNext(); ) {
      MethodDecl m = (MethodDecl)iter1.next();
      ASTNode target = m.hostType() == this ? (ASTNode)m : (ASTNode)this;

      for (Iterator i2 = ancestorMethods(m.signature()).iterator(); i2.hasNext(); ) {
        MethodDecl decl = (MethodDecl)i2.next();
        if (m.overrides(decl)) {
          // 8.4.6.1
          if (!m.isStatic() && decl.isStatic()) {
            target.error("an instance method may not override a static method");
          }

          // regardless of overriding
          // 8.4.6.3
          if (!m.mayOverride(decl)) {
            target.error("the return type of method " + m.fullSignature() +
                " in " + m.hostType().typeName() +
                " does not match the return type of method " +
                decl.fullSignature() + " in " + decl.hostType().typeName() +
                " and may thus not be overriden");
          }

          // regardless of overriding
          // 8.4.4
          for (int i = 0; i < m.getNumException(); i++) {
            Access e = m.getException(i);
            boolean found = false;
            for (int j = 0; !found && j < decl.getNumException(); j++) {
              if (e.type().instanceOf(decl.getException(j).type())) {
                found = true;
              }
            }
            if (!found && e.type().isUncheckedException()) {
              target.error(m.fullSignature() + " in " + m.hostType().typeName() +
                  " may not throw more checked exceptions than overridden method " +
                  decl.fullSignature() + " in " + decl.hostType().typeName());
            }
          }
          // 8.4.6.3
          if (decl.isPublic() && !m.isPublic()) {
            target.error("overriding access modifier error");
          }
          // 8.4.6.3
          if (decl.isProtected() && !(m.isPublic() || m.isProtected())) {
            target.error("overriding access modifier error");
          }
          // 8.4.6.3
          if ((!decl.isPrivate() && !decl.isProtected() && !decl.isPublic()) && m.isPrivate()) {
            target.error("overriding access modifier error");
          }
          // regardless of overriding
          if (decl.isFinal()) {
            target.error("method " + m.fullSignature() + " in " +
                hostType().typeName() + " can not override final method " +
                decl.fullSignature() + " in " + decl.hostType().typeName());
          }
        }
        if (m.hides(decl)) {
          // 8.4.6.2
          if(m.isStatic() && !decl.isStatic()) {
            target.error("a static method may not hide an instance method");
          }
          // 8.4.6.3
          if (!m.mayOverride(decl)) {
            target.error("can not hide a method with a different return type");
          }
          // 8.4.4
          for (int i = 0; i < m.getNumException(); i++) {
            Access e = m.getException(i);
            boolean found = false;
            for (int j = 0; !found && j < decl.getNumException(); j++) {
              if (e.type().instanceOf(decl.getException(j).type())) {
                found = true;
              }
            }
            if (!found) {
              target.error("may not throw more checked exceptions than hidden method");
            }
          }
          // 8.4.6.3
          if (decl.isPublic() && !m.isPublic()) {
            target.error("hiding access modifier error: public method " +
                decl.fullSignature() + " in " + decl.hostType().typeName() +
                " is hidden by non public method " + m.fullSignature() + " in " +
                m.hostType().typeName());
          }
          // 8.4.6.3
          if (decl.isProtected() && !(m.isPublic() || m.isProtected())) {
            target.error("hiding access modifier error: protected method " +
                decl.fullSignature() + " in " + decl.hostType().typeName() +
                " is hidden by non (public|protected) method " +
                m.fullSignature() + " in " + m.hostType().typeName());
          }
          // 8.4.6.3
          if ((!decl.isPrivate() && !decl.isProtected() && !decl.isPublic()) && m.isPrivate()) {
            target.error("hiding access modifier error: default method " +
                decl.fullSignature() + " in " + decl.hostType().typeName() +
                " is hidden by private method " + m.fullSignature() + " in " +
                m.hostType().typeName());
          }
          if (decl.isFinal()) {
            target.error("method " + m.fullSignature() + " in " +
                hostType().typeName() + " can not hide final method " +
                decl.fullSignature() + " in " + decl.hostType().typeName());
          }
        }
      }
    }
  }
  /**
   * Error-check two interface method declarations.
   * @aspect TypeHierarchyCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeHierarchyCheck.jrag:416
   */
  protected void refined_TypeHierarchyCheck_TypeDecl_checkAbstractMethodDecls(MethodDecl m1, MethodDecl m2) {
    if (!m1.mayOverride(m2) && !m2.mayOverride(m1)) {
      StringBuilder err = new StringBuilder();
      TypeDecl host1 = m1.hostType();
      TypeDecl host2 = m2.hostType();
      if (host1 != this || host2 != this) {
        err.append("inherited ");
      }
      err.append("method " + m1.fullSignature() + " is multiply declared " +
          "with incompatible return types in " + fullName());
      error(err.toString());
    }
  }
  /**
   * @aspect Java8NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/NameCheck.jrag:275
   */
  protected void checkInterfaceMethodDecls(MethodDecl m1, MethodDecl m2) {
		if(m1.isAbstract() && m2.isAbstract()) {
			checkAbstractMethodDecls(m1, m2);
			return;
		}
		else {
			StringBuilder err = new StringBuilder();
			TypeDecl host1 = m1.hostType();
			TypeDecl host2 = m2.hostType();
			String inh1 = "";
			String inh2 = "";
			if (host1 != this) {
				inh1 = "inherited ";
			}
			if (host2 != this) {
				inh2 = "inherited ";
			}
			
			//9.4
			err.append(inh1 + "method " + m1.fullSignature() + " and " + inh2 + "method " + m2.fullSignature() + " are multiply declared " +
				"in " + fullName());
			error(err.toString());
		}
	}
  /**
   * @declaredat ASTNode:1
   */
  public TypeDecl() {
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
    setChild(new List(), 1);
  }
  /**
   * @declaredat ASTNode:14
   */
  public TypeDecl(Modifiers p0, String p1, List<BodyDecl> p2) {
    setChild(p0, 0);
    setID(p1);
    setChild(p2, 1);
  }
  /**
   * @declaredat ASTNode:19
   */
  public TypeDecl(Modifiers p0, beaver.Symbol p1, List<BodyDecl> p2) {
    setChild(p0, 0);
    setID(p1);
    setChild(p2, 1);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:27
   */
  protected int numChildren() {
    return 2;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:33
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:39
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    erasure_reset();
    implementedInterfaces_reset();
    usesTypeVariable_reset();
    sourceTypeDecl_reset();
    accessibleFromPackage_String_reset();
    accessibleFromExtend_TypeDecl_reset();
    accessibleFrom_TypeDecl_reset();
    dimension_reset();
    elementType_reset();
    arrayType_reset();
    isException_reset();
    isCheckedException_reset();
    isUncheckedException_reset();
    mayCatch_TypeDecl_reset();
    constructors_reset();
    unqualifiedLookupMethod_String_reset();
    methodsNameMap_reset();
    localMethodsSignatureMap_reset();
    methodsSignatureMap_reset();
    ancestorMethods_String_reset();
    localTypeDecls_String_reset();
    memberTypes_String_reset();
    localFields_String_reset();
    localFieldsMap_reset();
    memberFieldsMap_reset();
    memberFields_String_reset();
    hasAbstract_reset();
    unimplementedMethods_reset();
    isPublic_reset();
    isStatic_reset();
    fullName_reset();
    typeName_reset();
    narrowingConversionTo_TypeDecl_reset();
    methodInvocationConversionTo_TypeDecl_reset();
    castingConversionTo_TypeDecl_reset();
    isString_reset();
    isObject_reset();
    instanceOf_TypeDecl_reset();
    isCircular_reset();
    boxed_reset();
    unboxed_reset();
    isIterable_reset();
    involvesTypeParameters_reset();
    containedIn_TypeDecl_reset();
    sameStructure_TypeDecl_reset();
    subtype_TypeDecl_reset();
    isFunctionalInterface_reset();
    strictContainedIn_TypeDecl_reset();
    strictSubtype_TypeDecl_reset();
    componentType_reset();
    isDAbefore_Variable_reset();
    isDUbefore_Variable_reset();
    typeException_reset();
    typeRuntimeException_reset();
    typeError_reset();
    lookupMethod_String_reset();
    typeObject_reset();
    lookupType_String_reset();
    lookupVariable_String_reset();
    packageName_reset();
    isAnonymous_reset();
    unknownType_reset();
    inExplicitConstructorInvocation_reset();
    inStaticContext_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:109
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
  }
  /**
   * @api internal
   * @declaredat ASTNode:115
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:121
   */
  public TypeDecl clone() throws CloneNotSupportedException {
    TypeDecl node = (TypeDecl) super.clone();
    return node;
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @deprecated Please use emitTreeCopy or emitTreeCopyNoTransform instead
   * @declaredat ASTNode:132
   */
  public abstract TypeDecl fullCopy();
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:139
   */
  public abstract TypeDecl treeCopyNoTransform();
  /**
   * Create a deep copy of the AST subtree at this node.
   * The subtree of this node is traversed to trigger rewrites before copy.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:147
   */
  public abstract TypeDecl treeCopy();
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
   * @aspect Modifiers
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/Modifiers.jrag:101
   */
   
  public void checkModifiers() {
    refined_Modifiers_TypeDecl_checkModifiers();
    if (getModifiers().numModifier("default") != 0) {
      error("the default modifier is only legal for interface method declarations");
    }
  }
  /**
   * @aspect GenericsTypeCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:373
   */
    public void refined_GenericsTypeCheck_TypeDecl_typeCheck() {
    refined_TypeHierarchyCheck_TypeDecl_typeCheck();
    ArrayList list = new ArrayList();
    list.addAll(implementedInterfaces());
    for(int i = 0; i < list.size(); i++) {
      InterfaceDecl decl = (InterfaceDecl)list.get(i);
      if(decl instanceof ParInterfaceDecl) {
        ParInterfaceDecl p = (ParInterfaceDecl)decl;
        for(Iterator i2 = list.listIterator(i); i2.hasNext(); ) {
          InterfaceDecl decl2 = (InterfaceDecl)i2.next();
          if(decl2 instanceof ParInterfaceDecl) {
            ParInterfaceDecl q = (ParInterfaceDecl)decl2;
            if(p != q && p.genericDecl() == q.genericDecl() && !p.sameArgument(q))
              error(p.genericDecl().name() + " cannot be inherited with different arguments: " +
                p.typeName() + " and " + q.typeName());
          }
        }
      }
    }
  }
  /**
   * @aspect TypeCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericMethods.jrag:125
   */
    protected void refined_TypeCheck_TypeDecl_checkAbstractMethodDecls(MethodDecl m1, MethodDecl m2) {

    if (!m1.sameSignature(m2)) {
      error("method declarations " + m1.fullSignature() + " and " +
          m2.fullSignature() + " in interface " + fullName() +
          " are incompatible");
    } else {
      refined_TypeHierarchyCheck_TypeDecl_checkAbstractMethodDecls(m1, m2);
    }
  }
  /**
   * @aspect Java8TypeHierarchyCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TypeHierarchyCheck.jrag:30
   */
   
	public void typeCheck() {
	// 8.4.6.4 & 9.4.1
		for (Iterator iter1 = localMethodsIterator(); iter1.hasNext(); ) {
			MethodDecl m = (MethodDecl)iter1.next();
			ASTNode target = m.hostType() == this ? (ASTNode)m : (ASTNode)this;
		
			for (Iterator i2 = ancestorMethods(m.signature()).iterator(); i2.hasNext(); ) {
				MethodDecl decl = (MethodDecl)i2.next();
				if (m.overrides(decl)) {
					// 8.4.6.1
					if (!decl.hostType().isInterfaceDecl() && !m.isStatic() && decl.isStatic()) {
						target.error("an instance method may not override a static method");
					}
		
					// regardless of overriding
					// 8.4.6.3
					if (!m.mayOverride(decl)) {
						// 9.4.3
						if(m.isDefault() && decl.hostType() == m.type().typeObject() && !decl.isPrivate())
							target.error("default methods may not override methods in Object");
						else
							target.error("the return type of method " + m.fullSignature() +
										" in " + m.hostType().typeName() +
										" does not match the return type of method " +
										decl.fullSignature() + " in " + decl.hostType().typeName() +
										" and may thus not be overriden");
					}
		
					// regardless of overriding
					// 8.4.4
					for (int i = 0; i < m.getNumException(); i++) {
						Access e = m.getException(i);
						boolean found = false;
						for (int j = 0; !found && j < decl.getNumException(); j++) {
							if (e.type().instanceOf(decl.getException(j).type())) {
								found = true;
							}
						}
						if (!found && e.type().isUncheckedException()) {
							target.error(m.fullSignature() + " in " + m.hostType().typeName() +
										" may not throw more checked exceptions than overridden method " +
										decl.fullSignature() + " in " + decl.hostType().typeName());
						}
					}
					// 8.4.6.3
					if (decl.isPublic() && !m.isPublic()) {
						target.error("overriding access modifier error");
					}
					// 8.4.6.3
					if (decl.isProtected() && !(m.isPublic() || m.isProtected())) {
						target.error("overriding access modifier error");
					}
					// 8.4.6.3
					if ((!decl.isPrivate() && !decl.isProtected() && !decl.isPublic()) && m.isPrivate()) {
						target.error("overriding access modifier error");
					}
					// regardless of overriding
					if (decl.isFinal()) {
						target.error("method " + m.fullSignature() + " in " +
									hostType().typeName() + " can not override final method " +
									decl.fullSignature() + " in " + decl.hostType().typeName());
					}
				}
				if (m.hides(decl)) {
					// 8.4.6.2
					if(m.isStatic() && !decl.isStatic()) {
						target.error("a static method may not hide an instance method");
					}
					// 8.4.6.3
					if (!m.mayOverride(decl)) {
						target.error("can not hide a method with a different return type");
					}
					// 8.4.4
					for (int i = 0; i < m.getNumException(); i++) {
						Access e = m.getException(i);
						boolean found = false;
						for (int j = 0; !found && j < decl.getNumException(); j++) {
							if (e.type().instanceOf(decl.getException(j).type())) {
								found = true;
							}
						}
						if (!found) {
							target.error("may not throw more checked exceptions than hidden method");
						}
					}
					// 8.4.6.3
					if (decl.isPublic() && !m.isPublic()) {
						target.error("hiding access modifier error: public method " +
									decl.fullSignature() + " in " + decl.hostType().typeName() +
									" is hidden by non public method " + m.fullSignature() + " in " +
									m.hostType().typeName());
					}
					// 8.4.6.3
					if (decl.isProtected() && !(m.isPublic() || m.isProtected())) {
						target.error("hiding access modifier error: protected method " +
									decl.fullSignature() + " in " + decl.hostType().typeName() +
									" is hidden by non (public|protected) method " +
									m.fullSignature() + " in " + m.hostType().typeName());
					}
					// 8.4.6.3
					if ((!decl.isPrivate() && !decl.isProtected() && !decl.isPublic()) && m.isPrivate()) {
						target.error("hiding access modifier error: default method " +
									decl.fullSignature() + " in " + decl.hostType().typeName() +
									" is hidden by private method " + m.fullSignature() + " in " +
									m.hostType().typeName());
					}
					if (decl.isFinal()) {
						target.error("method " + m.fullSignature() + " in " +
									hostType().typeName() + " can not hide final method " +
									decl.fullSignature() + " in " + decl.hostType().typeName());
					}
				}
			}
		}
		
		
		// different parameterizations of the same generic interface may not be implemented
		ArrayList list = new ArrayList();
		list.addAll(implementedInterfaces());
		for(int i = 0; i < list.size(); i++) {
			InterfaceDecl decl = (InterfaceDecl)list.get(i);
			if(decl instanceof ParInterfaceDecl) {
				ParInterfaceDecl p = (ParInterfaceDecl)decl;
				for(Iterator i2 = list.listIterator(i); i2.hasNext(); ) {
					InterfaceDecl decl2 = (InterfaceDecl)i2.next();
					if(decl2 instanceof ParInterfaceDecl) {
						ParInterfaceDecl q = (ParInterfaceDecl)decl2;
						if(p != q && p.genericDecl() == q.genericDecl() && !p.sameArgument(q))
							error(p.genericDecl().name() + " cannot be inherited with different arguments: " +
									p.typeName() + " and " + q.typeName());
					}
				}
			}
		}
	}
  /**
   * @aspect Java8NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/NameCheck.jrag:241
   */
    
	protected void checkAbstractMethodDecls(MethodDecl m1, MethodDecl m2) {
		if(!m1.subsignatureTo(m2) && !m2.subsignatureTo(m1)) {
			StringBuilder err = new StringBuilder();
			TypeDecl host1 = m1.hostType();
			TypeDecl host2 = m2.hostType();
			String inh1 = "";
			String inh2 = "";
			if (host1 != this) {
				inh1 = "inherited ";
			}
			if (host2 != this) {
				inh2 = "inherited ";
			}
			
			//8.4.8.3
			err.append(inh1 + "method " + m1.fullSignature() + " and " + inh2 + "method " + m2.fullSignature() + " are multiply declared " +
				"with same erasure but not override-equivalent signatures in " + fullName());
			error(err.toString());
		}
		// DON'T FORGET TO CHECK THIS, REALLY OK TO CHECK BOTH WAYS???
		if (!m1.returnTypeSubstitutableFor(m2) && !m2.returnTypeSubstitutableFor(m1)) {
			StringBuilder err = new StringBuilder();
			TypeDecl host1 = m1.hostType();
			TypeDecl host2 = m2.hostType();
			if (host1 != this || host2 != this) {
				err.append("inherited ");
			}
			err.append("method " + m1.fullSignature() + " is multiply declared " +
						"with incompatible return types in " + fullName());
			error(err.toString());
		}
	}
  /**
   * @aspect TypeConversion
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:59
   */
  private boolean refined_TypeConversion_TypeDecl_assignConversionTo_TypeDecl_Expr(TypeDecl type, Expr expr)
{
    //System.out.println("@@@ " + fullName() + " assign conversion to " + type.fullName() + ", expr: " + expr);
    boolean sourceIsConstant = expr != null ? expr.isConstant() : false;
    //System.out.println("@@@ sourceIsConstant: " + sourceIsConstant);
    if(identityConversionTo(type) || wideningConversionTo(type))
      return true;
    //System.out.println("@@@ narrowing conversion needed");
    //System.out.println("@@@ value: " + expr.value());
    if(sourceIsConstant && (isInt() || isChar() || isShort() || isByte()) &&
        (type.isByte() || type.isShort() || type.isChar()) &&
        narrowingConversionTo(type) && expr.representableIn(type))
      return true;
    //System.out.println("@@@ false");
    return false;
  }
  /**
   * @aspect TypeConversion
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:76
   */
  private boolean refined_TypeConversion_TypeDecl_methodInvocationConversionTo_TypeDecl(TypeDecl type)
{
    return identityConversionTo(type) || wideningConversionTo(type);
  }
  /**
   * @aspect TypeConversion
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:81
   */
  private boolean refined_TypeConversion_TypeDecl_castingConversionTo_TypeDecl(TypeDecl type)
{ return identityConversionTo(type) ||
    wideningConversionTo(type) || narrowingConversionTo(type); }
  @ASTNodeAnnotation.Attribute
  public boolean addsIndentationLevel() {
    ASTNode$State state = state();
    boolean addsIndentationLevel_value = true;

    return addsIndentationLevel_value;
  }
  @ASTNodeAnnotation.Attribute
  public String dumpString() {
    ASTNode$State state = state();
    String dumpString_value = getClass().getName() + " [" + getID() + "]";

    return dumpString_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isGenericType() {
    ASTNode$State state = state();
    boolean isGenericType_value = false;

    return isGenericType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isParameterizedType() {
    ASTNode$State state = state();
    boolean isParameterizedType_value = false;

    return isParameterizedType_value;
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
    erasure_value = erasure_compute();
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
  private TypeDecl erasure_compute() {
      if(isAnonymous() || isLocalClass())
        return this;
      if(!isNestedType())
        return this;
      return extractSingleType(enclosingType().erasure().memberTypes(name()));
    }
  /**
   * @apilevel internal
   */
  protected boolean implementedInterfaces_computed = false;
  /**
   * @apilevel internal
   */
  protected HashSet implementedInterfaces_value;
/**
 * @apilevel internal
 */
private void implementedInterfaces_reset() {
  implementedInterfaces_computed = false;
  implementedInterfaces_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public HashSet implementedInterfaces() {
    if(implementedInterfaces_computed) {
      return implementedInterfaces_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    implementedInterfaces_value = new HashSet();
    if (isFinal && num == state().boundariesCrossed) {
      implementedInterfaces_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return implementedInterfaces_value;
  }
  /**
   * @attribute syn
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:584
   */
  @ASTNodeAnnotation.Attribute
  public boolean sameSignature(Access a) {
    ASTNode$State state = state();
    try {
        if(a instanceof ParTypeAccess) return false;
        if(a instanceof AbstractWildcard) return false;
        return this == a.type();
      }
    finally {
    }
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
        new_usesTypeVariable_value = isNestedType() && enclosingType().usesTypeVariable();
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
        boolean $tmp = isNestedType() && enclosingType().usesTypeVariable();
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
      new_usesTypeVariable_value = isNestedType() && enclosingType().usesTypeVariable();
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
  @ASTNodeAnnotation.Attribute
  public TypeDecl original() {
    ASTNode$State state = state();
    TypeDecl original_value = this;

    return original_value;
  }
  @ASTNodeAnnotation.Attribute
  public TypeDecl asWildcardExtends() {
    ASTNode$State state = state();
    TypeDecl asWildcardExtends_value = lookupWildcardExtends(this);

    return asWildcardExtends_value;
  }
  @ASTNodeAnnotation.Attribute
  public TypeDecl asWildcardSuper() {
    ASTNode$State state = state();
    TypeDecl asWildcardSuper_value = lookupWildcardSuper(this);

    return asWildcardSuper_value;
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
    sourceTypeDecl_value = this;
    if (isFinal && num == state().boundariesCrossed) {
      sourceTypeDecl_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return sourceTypeDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isTypeVariable() {
    ASTNode$State state = state();
    boolean isTypeVariable_value = false;

    return isTypeVariable_value;
  }
  protected java.util.Map accessibleFromPackage_String_values;
/**
 * @apilevel internal
 */
private void accessibleFromPackage_String_reset() {
  accessibleFromPackage_String_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean accessibleFromPackage(String packageName) {
    Object _parameters = packageName;
    if (accessibleFromPackage_String_values == null) accessibleFromPackage_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(accessibleFromPackage_String_values.containsKey(_parameters)) {
      return ((Boolean)accessibleFromPackage_String_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean accessibleFromPackage_String_value = !isPrivate() && (isPublic() || hostPackage().equals(packageName));
    if (isFinal && num == state().boundariesCrossed) {
      accessibleFromPackage_String_values.put(_parameters, Boolean.valueOf(accessibleFromPackage_String_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return accessibleFromPackage_String_value;
  }
  protected java.util.Map accessibleFromExtend_TypeDecl_values;
/**
 * @apilevel internal
 */
private void accessibleFromExtend_TypeDecl_reset() {
  accessibleFromExtend_TypeDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean accessibleFromExtend(TypeDecl type) {
    Object _parameters = type;
    if (accessibleFromExtend_TypeDecl_values == null) accessibleFromExtend_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(accessibleFromExtend_TypeDecl_values.containsKey(_parameters)) {
      return ((Boolean)accessibleFromExtend_TypeDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean accessibleFromExtend_TypeDecl_value = accessibleFromExtend_compute(type);
    if (isFinal && num == state().boundariesCrossed) {
      accessibleFromExtend_TypeDecl_values.put(_parameters, Boolean.valueOf(accessibleFromExtend_TypeDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return accessibleFromExtend_TypeDecl_value;
  }
  /**
   * @apilevel internal
   */
  private boolean accessibleFromExtend_compute(TypeDecl type) {
      if(type == this)
        return true;
      if(isInnerType()) {
        if(!enclosingType().accessibleFrom(type)) {
          return false;
        }
      }
      if(isPublic())
        return true;
      else if(isProtected()) {
        // isProtected implies a nested type
        if(hostPackage().equals(type.hostPackage())) {
          return true;
        }
        if(type.isNestedType() && type.enclosingType().withinBodyThatSubclasses(enclosingType()) != null)
          return true;
        return false;
      }
      else if(isPrivate()) {
        return topLevelType() == type.topLevelType();
      }
      else
        return hostPackage().equals(type.hostPackage());
    }
  protected java.util.Map accessibleFrom_TypeDecl_values;
/**
 * @apilevel internal
 */
private void accessibleFrom_TypeDecl_reset() {
  accessibleFrom_TypeDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean accessibleFrom(TypeDecl type) {
    Object _parameters = type;
    if (accessibleFrom_TypeDecl_values == null) accessibleFrom_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(accessibleFrom_TypeDecl_values.containsKey(_parameters)) {
      return ((Boolean)accessibleFrom_TypeDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean accessibleFrom_TypeDecl_value = accessibleFrom_compute(type);
    if (isFinal && num == state().boundariesCrossed) {
      accessibleFrom_TypeDecl_values.put(_parameters, Boolean.valueOf(accessibleFrom_TypeDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return accessibleFrom_TypeDecl_value;
  }
  /**
   * @apilevel internal
   */
  private boolean accessibleFrom_compute(TypeDecl type) {
      if(type == this)
        return true;
      if(isInnerType()) {
        if(!enclosingType().accessibleFrom(type)) {
          return false;
        }
      }
      if(isPublic()) {
        return true;
      }
      else if(isProtected()) {
        if(hostPackage().equals(type.hostPackage())) {
          return true;
        }
        if(isMemberType()) {
          TypeDecl typeDecl = type;
          while(typeDecl != null && !typeDecl.instanceOf(enclosingType()))
            typeDecl = typeDecl.enclosingType();
          if(typeDecl != null) {
            return true;
          }
        }
        return false;
      }
      else if(isPrivate()) {
        return topLevelType() == type.topLevelType();
      }
      else {
        return hostPackage().equals(type.hostPackage());
      }
    }
  /**
   * @apilevel internal
   */
  protected boolean dimension_computed = false;
  /**
   * @apilevel internal
   */
  protected int dimension_value;
/**
 * @apilevel internal
 */
private void dimension_reset() {
  dimension_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public int dimension() {
    if(dimension_computed) {
      return dimension_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    dimension_value = 0;
    if (isFinal && num == state().boundariesCrossed) {
      dimension_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return dimension_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean elementType_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl elementType_value;
/**
 * @apilevel internal
 */
private void elementType_reset() {
  elementType_computed = false;
  elementType_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl elementType() {
    if(elementType_computed) {
      return elementType_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    elementType_value = this;
    if (isFinal && num == state().boundariesCrossed) {
      elementType_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return elementType_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean arrayType_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl arrayType_value;
/**
 * @apilevel internal
 */
private void arrayType_reset() {
  arrayType_computed = false;
  arrayType_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl arrayType() {
    if(arrayType_computed) {
      return arrayType_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    arrayType_value = arrayType_compute();
    arrayType_value.setParent(this);
    arrayType_value.is$Final = true;
    if (true) {
      arrayType_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return arrayType_value;
  }
  /**
   * @apilevel internal
   */
  private TypeDecl arrayType_compute() {
      String name = name() + "[]";
  
      List body = new List();
      body.add(
        new FieldDeclaration(
          new Modifiers(new List().add(new Modifier("public")).add(new Modifier("final"))),
          new PrimitiveTypeAccess("int"),
          "length",
          new Opt() // [Init:Expr]
        )
      );
      MethodDecl clone = null;
      TypeDecl typeObject = typeObject();
      for(int i = 0; clone == null && i < typeObject.getNumBodyDecl(); i++) {
        if(typeObject.getBodyDecl(i) instanceof MethodDecl) {
          MethodDecl m = (MethodDecl)typeObject.getBodyDecl(i);
          if(m.name().equals("clone"))
            clone = m;
        }
      }
      if(clone != null) {
        body.add(
            // we create a substituted method that substitutes the clone method in object
            // this has the following two consequences: the return value will be cast to the
            // expected return type rather than object, and the invoked method will be the
            // method in object rather in the array
            new MethodDeclSubstituted(
              new Modifiers(new List().add(new Modifier("public"))),
              new ArrayTypeAccess(createQualifiedAccess()),
              "clone",
              new List(),
              new List(),
              new Opt(new Block()),
              (MethodDecl)typeObject().memberMethods("clone").iterator().next()
            )
        );
      }
      TypeDecl typeDecl =
        new ArrayDecl(
          new Modifiers(new List().add(new Modifier("public"))),
          name,
          new Opt(typeObject().createQualifiedAccess()), // [SuperClass]
          new List().add(typeCloneable().createQualifiedAccess()).add(typeSerializable().createQualifiedAccess()), // Implements*
          body // BodyDecl*
        );
      return typeDecl;
    }
  /**
   * @attribute syn
   * @aspect ConstantExpression
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ConstantExpression.jrag:51
   */
  @ASTNodeAnnotation.Attribute
  public Constant cast(Constant c) {
    ASTNode$State state = state();
    try {
        throw new UnsupportedOperationException("ConstantExpression operation cast" +
          " not supported for type " + getClass().getName());
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect ConstantExpression
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ConstantExpression.jrag:65
   */
  @ASTNodeAnnotation.Attribute
  public Constant plus(Constant c) {
    ASTNode$State state = state();
    try {
        throw new UnsupportedOperationException("ConstantExpression operation plus" +
          " not supported for type " + getClass().getName());
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect ConstantExpression
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ConstantExpression.jrag:74
   */
  @ASTNodeAnnotation.Attribute
  public Constant minus(Constant c) {
    ASTNode$State state = state();
    try {
        throw new UnsupportedOperationException("ConstantExpression operation minus" +
          " not supported for type " + getClass().getName());
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect ConstantExpression
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ConstantExpression.jrag:83
   */
  @ASTNodeAnnotation.Attribute
  public Constant bitNot(Constant c) {
    ASTNode$State state = state();
    try {
        throw new UnsupportedOperationException("ConstantExpression operation bitNot" +
          " not supported for type " + getClass().getName());
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect ConstantExpression
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ConstantExpression.jrag:90
   */
  @ASTNodeAnnotation.Attribute
  public Constant mul(Constant c1, Constant c2) {
    ASTNode$State state = state();
    try {
        throw new UnsupportedOperationException("ConstantExpression operation mul" +
          " not supported for type " + getClass().getName());
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect ConstantExpression
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ConstantExpression.jrag:99
   */
  @ASTNodeAnnotation.Attribute
  public Constant div(Constant c1, Constant c2) {
    ASTNode$State state = state();
    try {
        throw new UnsupportedOperationException("ConstantExpression operation div" +
          " not supported for type " + getClass().getName());
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect ConstantExpression
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ConstantExpression.jrag:108
   */
  @ASTNodeAnnotation.Attribute
  public Constant mod(Constant c1, Constant c2) {
    ASTNode$State state = state();
    try {
        throw new UnsupportedOperationException("ConstantExpression operation mod" +
          " not supported for type " + getClass().getName());
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect ConstantExpression
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ConstantExpression.jrag:117
   */
  @ASTNodeAnnotation.Attribute
  public Constant add(Constant c1, Constant c2) {
    ASTNode$State state = state();
    try {
        throw new UnsupportedOperationException("ConstantExpression operation add" +
          " not supported for type " + getClass().getName());
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect ConstantExpression
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ConstantExpression.jrag:127
   */
  @ASTNodeAnnotation.Attribute
  public Constant sub(Constant c1, Constant c2) {
    ASTNode$State state = state();
    try {
        throw new UnsupportedOperationException("ConstantExpression operation sub" +
          " not supported for type " + getClass().getName());
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect ConstantExpression
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ConstantExpression.jrag:136
   */
  @ASTNodeAnnotation.Attribute
  public Constant lshift(Constant c1, Constant c2) {
    ASTNode$State state = state();
    try {
        throw new UnsupportedOperationException("ConstantExpression operation lshift" +
          " not supported for type " + getClass().getName());
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect ConstantExpression
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ConstantExpression.jrag:143
   */
  @ASTNodeAnnotation.Attribute
  public Constant rshift(Constant c1, Constant c2) {
    ASTNode$State state = state();
    try {
        throw new UnsupportedOperationException("ConstantExpression operation rshift" +
          " not supported for type " + getClass().getName());
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect ConstantExpression
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ConstantExpression.jrag:150
   */
  @ASTNodeAnnotation.Attribute
  public Constant urshift(Constant c1, Constant c2) {
    ASTNode$State state = state();
    try {
        throw new UnsupportedOperationException("ConstantExpression operation urshift" +
          " not supported for type " + getClass().getName());
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect ConstantExpression
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ConstantExpression.jrag:157
   */
  @ASTNodeAnnotation.Attribute
  public Constant andBitwise(Constant c1, Constant c2) {
    ASTNode$State state = state();
    try {
        throw new UnsupportedOperationException("ConstantExpression operation andBitwise" +
          " not supported for type " + getClass().getName());
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect ConstantExpression
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ConstantExpression.jrag:165
   */
  @ASTNodeAnnotation.Attribute
  public Constant xorBitwise(Constant c1, Constant c2) {
    ASTNode$State state = state();
    try {
        throw new UnsupportedOperationException("ConstantExpression operation xorBitwise" +
          " not supported for type " + getClass().getName());
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect ConstantExpression
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ConstantExpression.jrag:173
   */
  @ASTNodeAnnotation.Attribute
  public Constant orBitwise(Constant c1, Constant c2) {
    ASTNode$State state = state();
    try {
        throw new UnsupportedOperationException("ConstantExpression operation orBitwise" +
          " not supported for type " + getClass().getName());
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect ConstantExpression
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ConstantExpression.jrag:181
   */
  @ASTNodeAnnotation.Attribute
  public Constant questionColon(Constant cond, Constant c1, Constant c2) {
    ASTNode$State state = state();
    try {
        throw new UnsupportedOperationException("ConstantExpression operation questionColon" +
          " not supported for type " + getClass().getName());
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect ConstantExpression
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ConstantExpression.jrag:295
   */
  @ASTNodeAnnotation.Attribute
  public boolean eqIsTrue(Expr left, Expr right) {
    ASTNode$State state = state();
    try {
        System.err.println("Evaluation eqIsTrue for unknown type: " + getClass().getName());
        return false;
      }
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public boolean ltIsTrue(Expr left, Expr right) {
    ASTNode$State state = state();
    boolean ltIsTrue_Expr_Expr_value = false;

    return ltIsTrue_Expr_Expr_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean leIsTrue(Expr left, Expr right) {
    ASTNode$State state = state();
    boolean leIsTrue_Expr_Expr_value = false;

    return leIsTrue_Expr_Expr_value;
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
  /**
   * @apilevel internal
   */
  protected boolean isException_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isException_value;
/**
 * @apilevel internal
 */
private void isException_reset() {
  isException_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isException() {
    if(isException_computed) {
      return isException_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isException_value = instanceOf(typeException());
    if (isFinal && num == state().boundariesCrossed) {
      isException_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isException_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isCheckedException_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isCheckedException_value;
/**
 * @apilevel internal
 */
private void isCheckedException_reset() {
  isCheckedException_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isCheckedException() {
    if(isCheckedException_computed) {
      return isCheckedException_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isCheckedException_value = isException() &&
        (instanceOf(typeRuntimeException()) || instanceOf(typeError()));
    if (isFinal && num == state().boundariesCrossed) {
      isCheckedException_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isCheckedException_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isUncheckedException_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isUncheckedException_value;
/**
 * @apilevel internal
 */
private void isUncheckedException_reset() {
  isUncheckedException_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isUncheckedException() {
    if(isUncheckedException_computed) {
      return isUncheckedException_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isUncheckedException_value = isException() && !isCheckedException();
    if (isFinal && num == state().boundariesCrossed) {
      isUncheckedException_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isUncheckedException_value;
  }
  protected java.util.Map mayCatch_TypeDecl_values;
/**
 * @apilevel internal
 */
private void mayCatch_TypeDecl_reset() {
  mayCatch_TypeDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean mayCatch(TypeDecl thrownType) {
    Object _parameters = thrownType;
    if (mayCatch_TypeDecl_values == null) mayCatch_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(mayCatch_TypeDecl_values.containsKey(_parameters)) {
      return ((Boolean)mayCatch_TypeDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean mayCatch_TypeDecl_value = thrownType.instanceOf(this) || this.instanceOf(thrownType);
    if (isFinal && num == state().boundariesCrossed) {
      mayCatch_TypeDecl_values.put(_parameters, Boolean.valueOf(mayCatch_TypeDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return mayCatch_TypeDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public Collection lookupSuperConstructor() {
    ASTNode$State state = state();
    Collection lookupSuperConstructor_value = Collections.EMPTY_LIST;

    return lookupSuperConstructor_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean constructors_computed = false;
  /**
   * @apilevel internal
   */
  protected Collection<ConstructorDecl> constructors_value;
/**
 * @apilevel internal
 */
private void constructors_reset() {
  constructors_computed = false;
  constructors_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Collection<ConstructorDecl> constructors() {
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
  private Collection<ConstructorDecl> constructors_compute() {
      Collection<ConstructorDecl> c = new ArrayList();
      for (int i = 0; i < getNumBodyDecl(); i++) {
        if (getBodyDecl(i) instanceof ConstructorDecl) {
          c.add((ConstructorDecl) getBodyDecl(i));
        }
      }
      return c;
    }
  protected java.util.Map unqualifiedLookupMethod_String_values;
/**
 * @apilevel internal
 */
private void unqualifiedLookupMethod_String_reset() {
  unqualifiedLookupMethod_String_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public Collection unqualifiedLookupMethod(String name) {
    Object _parameters = name;
    if (unqualifiedLookupMethod_String_values == null) unqualifiedLookupMethod_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(unqualifiedLookupMethod_String_values.containsKey(_parameters)) {
      return (Collection)unqualifiedLookupMethod_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    Collection unqualifiedLookupMethod_String_value = unqualifiedLookupMethod_compute(name);
    if (isFinal && num == state().boundariesCrossed) {
      unqualifiedLookupMethod_String_values.put(_parameters, unqualifiedLookupMethod_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return unqualifiedLookupMethod_String_value;
  }
  /**
   * @apilevel internal
   */
  private Collection unqualifiedLookupMethod_compute(String name) {
      Collection c = memberMethods(name);
      if(!c.isEmpty()) return c;
      if(isInnerType())
        return lookupMethod(name);
      return removeInstanceMethods(lookupMethod(name));
    }
  /**
   * @attribute syn
   * @aspect MemberMethods
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupMethod.jrag:255
   */
  @ASTNodeAnnotation.Attribute
  public Collection<MethodDecl> memberMethods(String name) {
    ASTNode$State state = state();
    try {
        Collection<MethodDecl> c = methodsNameMap().get(name);
        if (c != null) {
          return c;
        } else {
          return Collections.emptyList();
        }
      }
    finally {
    }
  }
  /**
   * @apilevel internal
   */
  protected boolean methodsNameMap_computed = false;
  /**
   * @apilevel internal
   */
  protected Map<String,Collection<MethodDecl>> methodsNameMap_value;
/**
 * @apilevel internal
 */
private void methodsNameMap_reset() {
  methodsNameMap_computed = false;
  methodsNameMap_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Map<String,Collection<MethodDecl>> methodsNameMap() {
    if(methodsNameMap_computed) {
      return methodsNameMap_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    methodsNameMap_value = methodsNameMap_compute();
    if (isFinal && num == state().boundariesCrossed) {
      methodsNameMap_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return methodsNameMap_value;
  }
  /**
   * @apilevel internal
   */
  private Map<String,Collection<MethodDecl>> methodsNameMap_compute() {
      Map<String,Collection<MethodDecl>> map =
        new HashMap<String,Collection<MethodDecl>>();
      for (Iterator<MethodDecl> iter = methodsIterator(); iter.hasNext(); ) {
        MethodDecl m = iter.next();
        Collection<MethodDecl> methods = map.get(m.name());
        if (methods == null) {
          methods = new ArrayList<MethodDecl>(4);
          map.put(m.name(), methods);
        }
        methods.add(m);
      }
      return map;
    }
  /**
   * @attribute syn
   * @aspect MemberMethods
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupMethod.jrag:302
   */
  @ASTNodeAnnotation.Attribute
  public SimpleSet localMethodsSignature(String signature) {
    ASTNode$State state = state();
    try {
        SimpleSet set = localMethodsSignatureMap().get(signature);
        if (set != null) {
          return set;
        }
        return SimpleSet.emptySet;
      }
    finally {
    }
  }
  /**
   * @apilevel internal
   */
  protected boolean localMethodsSignatureMap_computed = false;
  /**
   * @apilevel internal
   */
  protected Map<String,SimpleSet> localMethodsSignatureMap_value;
/**
 * @apilevel internal
 */
private void localMethodsSignatureMap_reset() {
  localMethodsSignatureMap_computed = false;
  localMethodsSignatureMap_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Map<String,SimpleSet> localMethodsSignatureMap() {
    if(localMethodsSignatureMap_computed) {
      return localMethodsSignatureMap_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    localMethodsSignatureMap_value = localMethodsSignatureMap_compute();
    if (isFinal && num == state().boundariesCrossed) {
      localMethodsSignatureMap_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return localMethodsSignatureMap_value;
  }
  /**
   * @apilevel internal
   */
  private Map<String,SimpleSet> localMethodsSignatureMap_compute() {
      Map<String,SimpleSet> map = new HashMap<String,SimpleSet>(getNumBodyDecl());
      for (int i = 0; i < getNumBodyDecl(); i++) {
        if (getBodyDecl(i) instanceof MethodDecl) {
          MethodDecl decl = (MethodDecl) getBodyDecl(i);
          map.put(decl.signature(), decl);
        }
      }
      return map;
    }
  /**
   * @attribute syn
   * @aspect MemberMethods
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupMethod.jrag:387
   */
  @ASTNodeAnnotation.Attribute
  public SimpleSet methodsSignature(String signature) {
    ASTNode$State state = state();
    try {
        SimpleSet set = (SimpleSet)methodsSignatureMap().get(signature);
        if(set != null) return set;
        return SimpleSet.emptySet;
      }
    finally {
    }
  }
  /**
   * @apilevel internal
   */
  protected boolean methodsSignatureMap_computed = false;
  /**
   * @apilevel internal
   */
  protected Map<String,SimpleSet> methodsSignatureMap_value;
/**
 * @apilevel internal
 */
private void methodsSignatureMap_reset() {
  methodsSignatureMap_computed = false;
  methodsSignatureMap_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public Map<String,SimpleSet> methodsSignatureMap() {
    if(methodsSignatureMap_computed) {
      return methodsSignatureMap_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    methodsSignatureMap_value = localMethodsSignatureMap();
    if (isFinal && num == state().boundariesCrossed) {
      methodsSignatureMap_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return methodsSignatureMap_value;
  }
  protected java.util.Map ancestorMethods_String_values;
/**
 * @apilevel internal
 */
private void ancestorMethods_String_reset() {
  ancestorMethods_String_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public SimpleSet ancestorMethods(String signature) {
    Object _parameters = signature;
    if (ancestorMethods_String_values == null) ancestorMethods_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(ancestorMethods_String_values.containsKey(_parameters)) {
      return (SimpleSet)ancestorMethods_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    SimpleSet ancestorMethods_String_value = SimpleSet.emptySet;
    if (isFinal && num == state().boundariesCrossed) {
      ancestorMethods_String_values.put(_parameters, ancestorMethods_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return ancestorMethods_String_value;
  }
  /**
   * @attribute syn
   * @aspect TypeScopePropagation
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:330
   */
  @ASTNodeAnnotation.Attribute
  public SimpleSet localLookupType(String name) {
    ASTNode$State state = state();
    try {
        SimpleSet c = memberTypes(name);
        if(!c.isEmpty())
          return c;
        if(name().equals(name))
          return SimpleSet.emptySet.add(this);
    
        c = lookupType(name);
        // 8.5.2
        if(isClassDecl() && isStatic() && !isTopLevelType()) {
          SimpleSet newSet = SimpleSet.emptySet;
          for(Iterator iter = c.iterator(); iter.hasNext(); ) {
            TypeDecl d = (TypeDecl)iter.next();
            //if(d.isStatic() || d.isTopLevelType() || this.instanceOf(d.enclosingType())) {
              newSet = newSet.add(d);
            //}
          }
          c = newSet;
        }
        return c;
      }
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public boolean hasType(String name) {
    ASTNode$State state = state();
    boolean hasType_String_value = !memberTypes(name).isEmpty();

    return hasType_String_value;
  }
  protected java.util.Map localTypeDecls_String_values;
/**
 * @apilevel internal
 */
private void localTypeDecls_String_reset() {
  localTypeDecls_String_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public SimpleSet localTypeDecls(String name) {
    Object _parameters = name;
    if (localTypeDecls_String_values == null) localTypeDecls_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(localTypeDecls_String_values.containsKey(_parameters)) {
      return (SimpleSet)localTypeDecls_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    SimpleSet localTypeDecls_String_value = localTypeDecls_compute(name);
    if (isFinal && num == state().boundariesCrossed) {
      localTypeDecls_String_values.put(_parameters, localTypeDecls_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return localTypeDecls_String_value;
  }
  /**
   * @apilevel internal
   */
  private SimpleSet localTypeDecls_compute(String name) {
      SimpleSet set = SimpleSet.emptySet;
      for(int i = 0; i < getNumBodyDecl(); i++)
        if(getBodyDecl(i).declaresType(name))
          set = set.add(getBodyDecl(i).type(name));
      return set;
    }
  protected java.util.Map memberTypes_String_values;
/**
 * @apilevel internal
 */
private void memberTypes_String_reset() {
  memberTypes_String_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public SimpleSet memberTypes(String name) {
    Object _parameters = name;
    if (memberTypes_String_values == null) memberTypes_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(memberTypes_String_values.containsKey(_parameters)) {
      return (SimpleSet)memberTypes_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    SimpleSet memberTypes_String_value = SimpleSet.emptySet;
    if (isFinal && num == state().boundariesCrossed) {
      memberTypes_String_values.put(_parameters, memberTypes_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return memberTypes_String_value;
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
    SimpleSet localFields_String_value = localFieldsMap().containsKey(name) ? (SimpleSet)localFieldsMap().get(name) : SimpleSet.emptySet;
    if (isFinal && num == state().boundariesCrossed) {
      localFields_String_values.put(_parameters, localFields_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return localFields_String_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean localFieldsMap_computed = false;
  /**
   * @apilevel internal
   */
  protected HashMap localFieldsMap_value;
/**
 * @apilevel internal
 */
private void localFieldsMap_reset() {
  localFieldsMap_computed = false;
  localFieldsMap_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public HashMap localFieldsMap() {
    if(localFieldsMap_computed) {
      return localFieldsMap_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    localFieldsMap_value = localFieldsMap_compute();
    if (isFinal && num == state().boundariesCrossed) {
      localFieldsMap_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return localFieldsMap_value;
  }
  /**
   * @apilevel internal
   */
  private HashMap localFieldsMap_compute() {
      HashMap map = new HashMap();
      for(int i = 0; i < getNumBodyDecl(); i++) {
        if(getBodyDecl(i) instanceof FieldDeclaration) {
          FieldDeclaration decl = (FieldDeclaration)getBodyDecl(i);
          SimpleSet fields = (SimpleSet)map.get(decl.name());
          if(fields == null) fields = SimpleSet.emptySet;
          fields = fields.add(decl);
          map.put(decl.name(), fields);
        }
      }
      return map;
    }
  /**
   * @apilevel internal
   */
  protected boolean memberFieldsMap_computed = false;
  /**
   * @apilevel internal
   */
  protected HashMap memberFieldsMap_value;
/**
 * @apilevel internal
 */
private void memberFieldsMap_reset() {
  memberFieldsMap_computed = false;
  memberFieldsMap_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public HashMap memberFieldsMap() {
    if(memberFieldsMap_computed) {
      return memberFieldsMap_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    memberFieldsMap_value = localFieldsMap();
    if (isFinal && num == state().boundariesCrossed) {
      memberFieldsMap_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return memberFieldsMap_value;
  }
  protected java.util.Map memberFields_String_values;
/**
 * @apilevel internal
 */
private void memberFields_String_reset() {
  memberFields_String_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public SimpleSet memberFields(String name) {
    Object _parameters = name;
    if (memberFields_String_values == null) memberFields_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(memberFields_String_values.containsKey(_parameters)) {
      return (SimpleSet)memberFields_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    SimpleSet memberFields_String_value = localFields(name);
    if (isFinal && num == state().boundariesCrossed) {
      memberFields_String_values.put(_parameters, memberFields_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return memberFields_String_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean hasAbstract_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean hasAbstract_value;
/**
 * @apilevel internal
 */
private void hasAbstract_reset() {
  hasAbstract_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean hasAbstract() {
    if(hasAbstract_computed) {
      return hasAbstract_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    hasAbstract_value = false;
    if (isFinal && num == state().boundariesCrossed) {
      hasAbstract_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return hasAbstract_value;
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
    unimplementedMethods_value = Collections.EMPTY_LIST;
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
  protected boolean isPublic_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isPublic_value;
/**
 * @apilevel internal
 */
private void isPublic_reset() {
  isPublic_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isPublic() {
    if(isPublic_computed) {
      return isPublic_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isPublic_value = getModifiers().isPublic() || isMemberType() && enclosingType().isInterfaceDecl();
    if (isFinal && num == state().boundariesCrossed) {
      isPublic_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isPublic_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isPrivate() {
    ASTNode$State state = state();
    boolean isPrivate_value = getModifiers().isPrivate();

    return isPrivate_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isProtected() {
    ASTNode$State state = state();
    boolean isProtected_value = getModifiers().isProtected();

    return isProtected_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isAbstract() {
    ASTNode$State state = state();
    boolean isAbstract_value = getModifiers().isAbstract();

    return isAbstract_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isStatic_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isStatic_value;
/**
 * @apilevel internal
 */
private void isStatic_reset() {
  isStatic_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isStatic() {
    if(isStatic_computed) {
      return isStatic_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isStatic_value = getModifiers().isStatic() || isMemberType() && enclosingType().isInterfaceDecl();
    if (isFinal && num == state().boundariesCrossed) {
      isStatic_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isStatic_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isFinal() {
    ASTNode$State state = state();
    boolean isFinal_value = getModifiers().isFinal();

    return isFinal_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isStrictfp() {
    ASTNode$State state = state();
    boolean isStrictfp_value = getModifiers().isStrictfp();

    return isStrictfp_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isSynthetic() {
    ASTNode$State state = state();
    boolean isSynthetic_value = getModifiers().isSynthetic();

    return isSynthetic_value;
  }
  /**
   * @attribute syn
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:300
   */
  @ASTNodeAnnotation.Attribute
  public boolean hasEnclosingTypeDecl(String name) {
    ASTNode$State state = state();
    try {
        TypeDecl enclosingType = enclosingType();
        if(enclosingType != null) {
          return enclosingType.name().equals(name) || enclosingType.hasEnclosingTypeDecl(name);
        }
        return false;
      }
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public boolean assignableToInt() {
    ASTNode$State state = state();
    boolean assignableToInt_value = false;

    return assignableToInt_value;
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
        return enclosingType().fullName() + "." + name();
      String packageName = packageName();
      if(packageName.equals(""))
        return name();
      return packageName + "." + name();
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
        return enclosingType().typeName() + "." + name();
      String packageName = packageName();
      if(packageName.equals("") || packageName.equals(PRIMITIVE_PACKAGE_NAME))
        return name();
      return packageName + "." + name();
    }
  @ASTNodeAnnotation.Attribute
  public boolean identityConversionTo(TypeDecl type) {
    ASTNode$State state = state();
    boolean identityConversionTo_TypeDecl_value = this == type;

    return identityConversionTo_TypeDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean wideningConversionTo(TypeDecl type) {
    ASTNode$State state = state();
    boolean wideningConversionTo_TypeDecl_value = instanceOf(type);

    return wideningConversionTo_TypeDecl_value;
  }
  protected java.util.Map narrowingConversionTo_TypeDecl_values;
/**
 * @apilevel internal
 */
private void narrowingConversionTo_TypeDecl_reset() {
  narrowingConversionTo_TypeDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean narrowingConversionTo(TypeDecl type) {
    Object _parameters = type;
    if (narrowingConversionTo_TypeDecl_values == null) narrowingConversionTo_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(narrowingConversionTo_TypeDecl_values.containsKey(_parameters)) {
      return ((Boolean)narrowingConversionTo_TypeDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean narrowingConversionTo_TypeDecl_value = instanceOf(type);
    if (isFinal && num == state().boundariesCrossed) {
      narrowingConversionTo_TypeDecl_values.put(_parameters, Boolean.valueOf(narrowingConversionTo_TypeDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return narrowingConversionTo_TypeDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean stringConversion() {
    ASTNode$State state = state();
    boolean stringConversion_value = true;

    return stringConversion_value;
  }
  /**
   * @attribute syn
   * @aspect TypeConversion
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:59
   */
  @ASTNodeAnnotation.Attribute
  public boolean assignConversionTo(TypeDecl type, Expr expr) {
    ASTNode$State state = state();
    try {
        if(refined_TypeConversion_TypeDecl_assignConversionTo_TypeDecl_Expr(type, expr))
          return true;
        boolean canBoxThis = this instanceof PrimitiveType;
        boolean canBoxType = type instanceof PrimitiveType;
        boolean canUnboxThis = !unboxed().isUnknown();
        boolean canUnboxType = !type.unboxed().isUnknown();
        TypeDecl t = !canUnboxThis && canUnboxType ? type.unboxed() : type;
        boolean sourceIsConstant = expr != null ? expr.isConstant() : false;
        if(sourceIsConstant && (isInt() || isChar() || isShort() || isByte()) &&
            (t.isByte() || t.isShort() || t.isChar()) &&
            narrowingConversionTo(t) && expr.representableIn(t))
          return true;
        if(canBoxThis && !canBoxType && boxed().wideningConversionTo(type))
          return true;
        else if(canUnboxThis && !canUnboxType && unboxed().wideningConversionTo(type))
          return true;
    
        return false;
      }
    finally {
    }
  }
  protected java.util.Map methodInvocationConversionTo_TypeDecl_values;
/**
 * @apilevel internal
 */
private void methodInvocationConversionTo_TypeDecl_reset() {
  methodInvocationConversionTo_TypeDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean methodInvocationConversionTo(TypeDecl type) {
    Object _parameters = type;
    if (methodInvocationConversionTo_TypeDecl_values == null) methodInvocationConversionTo_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(methodInvocationConversionTo_TypeDecl_values.containsKey(_parameters)) {
      return ((Boolean)methodInvocationConversionTo_TypeDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean methodInvocationConversionTo_TypeDecl_value = methodInvocationConversionTo_compute(type);
    if (isFinal && num == state().boundariesCrossed) {
      methodInvocationConversionTo_TypeDecl_values.put(_parameters, Boolean.valueOf(methodInvocationConversionTo_TypeDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return methodInvocationConversionTo_TypeDecl_value;
  }
  /**
   * @apilevel internal
   */
  private boolean methodInvocationConversionTo_compute(TypeDecl type) {
      if(refined_TypeConversion_TypeDecl_methodInvocationConversionTo_TypeDecl(type))
        return true;
      boolean canBoxThis = this instanceof PrimitiveType;
      boolean canBoxType = type instanceof PrimitiveType;
      boolean canUnboxThis = !unboxed().isUnknown();
      boolean canUnboxType = !type.unboxed().isUnknown();
      if(canBoxThis && !canBoxType)
        return boxed().wideningConversionTo(type);
      else if(canUnboxThis && !canUnboxType)
        return unboxed().wideningConversionTo(type);
      return false;
    }
  protected java.util.Map castingConversionTo_TypeDecl_values;
/**
 * @apilevel internal
 */
private void castingConversionTo_TypeDecl_reset() {
  castingConversionTo_TypeDecl_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean castingConversionTo(TypeDecl type) {
    Object _parameters = type;
    if (castingConversionTo_TypeDecl_values == null) castingConversionTo_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(castingConversionTo_TypeDecl_values.containsKey(_parameters)) {
      return ((Boolean)castingConversionTo_TypeDecl_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean castingConversionTo_TypeDecl_value = castingConversionTo_compute(type);
    if (isFinal && num == state().boundariesCrossed) {
      castingConversionTo_TypeDecl_values.put(_parameters, Boolean.valueOf(castingConversionTo_TypeDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return castingConversionTo_TypeDecl_value;
  }
  /**
   * @apilevel internal
   */
  private boolean castingConversionTo_compute(TypeDecl type) {
      if(refined_TypeConversion_TypeDecl_castingConversionTo_TypeDecl(type))
        return true;
      boolean canBoxThis = this instanceof PrimitiveType;
      boolean canBoxType = type instanceof PrimitiveType;
      boolean canUnboxThis = !unboxed().isUnknown();
      boolean canUnboxType = !type.unboxed().isUnknown();
      if(canBoxThis && !canBoxType)
        return boxed().wideningConversionTo(type);
      else if(canUnboxThis && !canUnboxType)
        return unboxed().wideningConversionTo(type);
      return false;
      /*
      else if(boxingConversionTo(type))
        return true;
      else if(unboxingConversionTo(type))
        return true;
      return false;
      */
    }
  @ASTNodeAnnotation.Attribute
  public TypeDecl unaryNumericPromotion() {
    ASTNode$State state = state();
    TypeDecl unaryNumericPromotion_value = this;

    return unaryNumericPromotion_value;
  }
  @ASTNodeAnnotation.Attribute
  public TypeDecl binaryNumericPromotion(TypeDecl type) {
    ASTNode$State state = state();
    TypeDecl binaryNumericPromotion_TypeDecl_value = unknownType();

    return binaryNumericPromotion_TypeDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isReferenceType() {
    ASTNode$State state = state();
    boolean isReferenceType_value = false;

    return isReferenceType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isPrimitiveType() {
    ASTNode$State state = state();
    boolean isPrimitiveType_value = false;

    return isPrimitiveType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isNumericType() {
    ASTNode$State state = state();
    boolean isNumericType_value = false;

    return isNumericType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isIntegralType() {
    ASTNode$State state = state();
    boolean isIntegralType_value = false;

    return isIntegralType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isBoolean() {
    ASTNode$State state = state();
    boolean isBoolean_value = false;

    return isBoolean_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isByte() {
    ASTNode$State state = state();
    boolean isByte_value = false;

    return isByte_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isChar() {
    ASTNode$State state = state();
    boolean isChar_value = false;

    return isChar_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isShort() {
    ASTNode$State state = state();
    boolean isShort_value = false;

    return isShort_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isInt() {
    ASTNode$State state = state();
    boolean isInt_value = false;

    return isInt_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isFloat() {
    ASTNode$State state = state();
    boolean isFloat_value = false;

    return isFloat_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isLong() {
    ASTNode$State state = state();
    boolean isLong_value = false;

    return isLong_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isDouble() {
    ASTNode$State state = state();
    boolean isDouble_value = false;

    return isDouble_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isVoid() {
    ASTNode$State state = state();
    boolean isVoid_value = false;

    return isVoid_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isNull() {
    ASTNode$State state = state();
    boolean isNull_value = false;

    return isNull_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isClassDecl() {
    ASTNode$State state = state();
    boolean isClassDecl_value = false;

    return isClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isInterfaceDecl() {
    ASTNode$State state = state();
    boolean isInterfaceDecl_value = false;

    return isInterfaceDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isArrayDecl() {
    ASTNode$State state = state();
    boolean isArrayDecl_value = false;

    return isArrayDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isPrimitive() {
    ASTNode$State state = state();
    boolean isPrimitive_value = false;

    return isPrimitive_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isString_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isString_value;
/**
 * @apilevel internal
 */
private void isString_reset() {
  isString_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isString() {
    if(isString_computed) {
      return isString_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isString_value = false;
    if (isFinal && num == state().boundariesCrossed) {
      isString_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isString_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isObject_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isObject_value;
/**
 * @apilevel internal
 */
private void isObject_reset() {
  isObject_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isObject() {
    if(isObject_computed) {
      return isObject_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isObject_value = false;
    if (isFinal && num == state().boundariesCrossed) {
      isObject_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isObject_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isUnknown() {
    ASTNode$State state = state();
    boolean isUnknown_value = false;

    return isUnknown_value;
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
    boolean instanceOf_TypeDecl_value = instanceOf_compute(type);
    if (isFinal && num == state().boundariesCrossed) {
      instanceOf_TypeDecl_values.put(_parameters, Boolean.valueOf(instanceOf_TypeDecl_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return instanceOf_TypeDecl_value;
  }
  /**
   * @apilevel internal
   */
  private boolean instanceOf_compute(TypeDecl type) { return subtype(type); }
  @ASTNodeAnnotation.Attribute
  public boolean isSupertypeOfClassDecl(ClassDecl type) {
    ASTNode$State state = state();
    boolean isSupertypeOfClassDecl_ClassDecl_value = type == this;

    return isSupertypeOfClassDecl_ClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isSupertypeOfInterfaceDecl(InterfaceDecl type) {
    ASTNode$State state = state();
    boolean isSupertypeOfInterfaceDecl_InterfaceDecl_value = type == this;

    return isSupertypeOfInterfaceDecl_InterfaceDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isSupertypeOfArrayDecl(ArrayDecl type) {
    ASTNode$State state = state();
    boolean isSupertypeOfArrayDecl_ArrayDecl_value = this == type;

    return isSupertypeOfArrayDecl_ArrayDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isSupertypeOfPrimitiveType(PrimitiveType type) {
    ASTNode$State state = state();
    boolean isSupertypeOfPrimitiveType_PrimitiveType_value = type == this;

    return isSupertypeOfPrimitiveType_PrimitiveType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isSupertypeOfNullType(NullType type) {
    ASTNode$State state = state();
    boolean isSupertypeOfNullType_NullType_value = false;

    return isSupertypeOfNullType_NullType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isSupertypeOfVoidType(VoidType type) {
    ASTNode$State state = state();
    boolean isSupertypeOfVoidType_VoidType_value = false;

    return isSupertypeOfVoidType_VoidType_value;
  }
  /**
   * @attribute syn
   * @aspect NestedTypes
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:507
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl topLevelType() {
    ASTNode$State state = state();
    try {
        if(isTopLevelType())
          return this;
        return enclosingType().topLevelType();
      }
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public boolean isTopLevelType() {
    ASTNode$State state = state();
    boolean isTopLevelType_value = !isNestedType();

    return isTopLevelType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isInnerClass() {
    ASTNode$State state = state();
    boolean isInnerClass_value = false;

    return isInnerClass_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isInnerType() {
    ASTNode$State state = state();
    boolean isInnerType_value = (isLocalClass() || isAnonymous() || (isMemberType() && !isStatic())) && !inStaticContext();

    return isInnerType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isInnerTypeOf(TypeDecl typeDecl) {
    ASTNode$State state = state();
    boolean isInnerTypeOf_TypeDecl_value = typeDecl == this || (isInnerType() && enclosingType().isInnerTypeOf(typeDecl));

    return isInnerTypeOf_TypeDecl_value;
  }
  /**
   * @attribute syn
   * @aspect NestedTypes
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:556
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl withinBodyThatSubclasses(TypeDecl type) {
    ASTNode$State state = state();
    try {
        if(instanceOf(type))
          return this;
        if(!isTopLevelType())
          return enclosingType().withinBodyThatSubclasses(type);
        return null;
      }
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public boolean encloses(TypeDecl type) {
    ASTNode$State state = state();
    boolean encloses_TypeDecl_value = type.enclosedBy(this);

    return encloses_TypeDecl_value;
  }
  /**
   * @attribute syn
   * @aspect NestedTypes
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:566
   */
  @ASTNodeAnnotation.Attribute
  public boolean enclosedBy(TypeDecl type) {
    ASTNode$State state = state();
    try {
        if(this == type)
          return true;
        if(isTopLevelType())
          return false;
        return enclosingType().enclosedBy(type);
      }
    finally {
    }
  }
  @ASTNodeAnnotation.Attribute
  public TypeDecl hostType() {
    ASTNode$State state = state();
    TypeDecl hostType_value = this;

    return hostType_value;
  }
  /**
   * @apilevel internal
   */
  protected int isCircular_visited = -1;
/**
 * @apilevel internal
 */
private void isCircular_reset() {
  isCircular_computed = false;
  isCircular_initialized = false;
  isCircular_visited = -1;
}  
  /**
   * @apilevel internal
   */
  protected boolean isCircular_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isCircular_initialized = false;
  /**
   * @apilevel internal
   */
  protected boolean isCircular_value;
  @ASTNodeAnnotation.Attribute
  public boolean isCircular() {
    if(isCircular_computed) {
      return isCircular_value;
    }
    ASTNode$State state = state();
    boolean new_isCircular_value;
    if (!isCircular_initialized) {
      isCircular_initialized = true;
      isCircular_value = true;
    }
    if (!state.IN_CIRCLE) {
      state.IN_CIRCLE = true;
      int num = state.boundariesCrossed;
      boolean isFinal = this.is$Final();
      do {
        isCircular_visited = state.CIRCLE_INDEX;
        state.CHANGE = false;
        new_isCircular_value = false;
        if (new_isCircular_value != isCircular_value) {
          state.CHANGE = true;
        }
        isCircular_value = new_isCircular_value;
        state.CIRCLE_INDEX++;
      } while (state.CHANGE);
      if (isFinal && num == state().boundariesCrossed) {
        isCircular_computed = true;
      } else {
        state.RESET_CYCLE = true;
        boolean $tmp = false;
        state.RESET_CYCLE = false;
        isCircular_computed = false;
        isCircular_initialized = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return isCircular_value;
    }
    if(isCircular_visited != state.CIRCLE_INDEX) {
      isCircular_visited = state.CIRCLE_INDEX;
      if (state.RESET_CYCLE) {
        isCircular_computed = false;
        isCircular_initialized = false;
        isCircular_visited = -1;
        return isCircular_value;
      }
      new_isCircular_value = false;
      if (new_isCircular_value != isCircular_value) {
        state.CHANGE = true;
      }
      isCircular_value = new_isCircular_value;
      state.INTERMEDIATE_VALUE = true;
      return isCircular_value;
    }
    state.INTERMEDIATE_VALUE = true;
    return isCircular_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isValidAnnotationMethodReturnType() {
    ASTNode$State state = state();
    boolean isValidAnnotationMethodReturnType_value = false;

    return isValidAnnotationMethodReturnType_value;
  }
  @ASTNodeAnnotation.Attribute
  public Annotation annotation(TypeDecl typeDecl) {
    ASTNode$State state = state();
    Annotation annotation_TypeDecl_value = getModifiers().annotation(typeDecl);

    return annotation_TypeDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean hasAnnotationSuppressWarnings(String annot) {
    ASTNode$State state = state();
    boolean hasAnnotationSuppressWarnings_String_value = getModifiers().hasAnnotationSuppressWarnings(annot);

    return hasAnnotationSuppressWarnings_String_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isDeprecated() {
    ASTNode$State state = state();
    boolean isDeprecated_value = getModifiers().hasDeprecatedAnnotation();

    return isDeprecated_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean commensurateWith(ElementValue value) {
    ASTNode$State state = state();
    boolean commensurateWith_ElementValue_value = value.commensurateWithTypeDecl(this);

    return commensurateWith_ElementValue_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isAnnotationDecl() {
    ASTNode$State state = state();
    boolean isAnnotationDecl_value = false;

    return isAnnotationDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean boxingConversionTo(TypeDecl typeDecl) {
    ASTNode$State state = state();
    boolean boxingConversionTo_TypeDecl_value = false;

    return boxingConversionTo_TypeDecl_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean boxed_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl boxed_value;
/**
 * @apilevel internal
 */
private void boxed_reset() {
  boxed_computed = false;
  boxed_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl boxed() {
    if(boxed_computed) {
      return boxed_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boxed_value = unknownType();
    if (isFinal && num == state().boundariesCrossed) {
      boxed_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return boxed_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean unboxingConversionTo(TypeDecl typeDecl) {
    ASTNode$State state = state();
    boolean unboxingConversionTo_TypeDecl_value = false;

    return unboxingConversionTo_TypeDecl_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean unboxed_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl unboxed_value;
/**
 * @apilevel internal
 */
private void unboxed_reset() {
  unboxed_computed = false;
  unboxed_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl unboxed() {
    if(unboxed_computed) {
      return unboxed_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    unboxed_value = unknownType();
    if (isFinal && num == state().boundariesCrossed) {
      unboxed_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return unboxed_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isIterable_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isIterable_value;
/**
 * @apilevel internal
 */
private void isIterable_reset() {
  isIterable_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isIterable() {
    if(isIterable_computed) {
      return isIterable_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isIterable_value = instanceOf(lookupType("java.lang", "Iterable"));
    if (isFinal && num == state().boundariesCrossed) {
      isIterable_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isIterable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isEnumDecl() {
    ASTNode$State state = state();
    boolean isEnumDecl_value = false;

    return isEnumDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isUnboxedPrimitive() {
    ASTNode$State state = state();
    boolean isUnboxedPrimitive_value = this instanceof PrimitiveType && isPrimitive();

    return isUnboxedPrimitive_value;
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
        new_involvesTypeParameters_value = false;
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
        boolean $tmp = false;
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
      new_involvesTypeParameters_value = false;
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
  @ASTNodeAnnotation.Attribute
  public boolean supertypeGenericClassDecl(GenericClassDecl type) {
    ASTNode$State state = state();
    boolean supertypeGenericClassDecl_GenericClassDecl_value = supertypeClassDecl(type);

    return supertypeGenericClassDecl_GenericClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeGenericInterfaceDecl(GenericInterfaceDecl type) {
    ASTNode$State state = state();
    boolean supertypeGenericInterfaceDecl_GenericInterfaceDecl_value = this == type || supertypeInterfaceDecl(type);

    return supertypeGenericInterfaceDecl_GenericInterfaceDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeRawClassDecl(RawClassDecl type) {
    ASTNode$State state = state();
    boolean supertypeRawClassDecl_RawClassDecl_value = supertypeParClassDecl(type);

    return supertypeRawClassDecl_RawClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeRawInterfaceDecl(RawInterfaceDecl type) {
    ASTNode$State state = state();
    boolean supertypeRawInterfaceDecl_RawInterfaceDecl_value = supertypeParInterfaceDecl(type);

    return supertypeRawInterfaceDecl_RawInterfaceDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeWildcard(WildcardType type) {
    ASTNode$State state = state();
    boolean supertypeWildcard_WildcardType_value = false;

    return supertypeWildcard_WildcardType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeWildcardExtends(WildcardExtendsType type) {
    ASTNode$State state = state();
    boolean supertypeWildcardExtends_WildcardExtendsType_value = false;

    return supertypeWildcardExtends_WildcardExtendsType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeWildcardSuper(WildcardSuperType type) {
    ASTNode$State state = state();
    boolean supertypeWildcardSuper_WildcardSuperType_value = false;

    return supertypeWildcardSuper_WildcardSuperType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isWildcard() {
    ASTNode$State state = state();
    boolean isWildcard_value = false;

    return isWildcard_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeParClassDecl(ParClassDecl type) {
    ASTNode$State state = state();
    boolean supertypeParClassDecl_ParClassDecl_value = supertypeClassDecl(type);

    return supertypeParClassDecl_ParClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeParInterfaceDecl(ParInterfaceDecl type) {
    ASTNode$State state = state();
    boolean supertypeParInterfaceDecl_ParInterfaceDecl_value = supertypeInterfaceDecl(type);

    return supertypeParInterfaceDecl_ParInterfaceDecl_value;
  }
/**
 * @apilevel internal
 */
private void containedIn_TypeDecl_reset() {
  containedIn_TypeDecl_values = null;
}  
  protected java.util.Map containedIn_TypeDecl_values;
  @ASTNodeAnnotation.Attribute
  public boolean containedIn(TypeDecl type) {
    Object _parameters = type;
    if (containedIn_TypeDecl_values == null) containedIn_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    ASTNode$State.CircularValue _value;
    if(containedIn_TypeDecl_values.containsKey(_parameters)) {
      Object _o = containedIn_TypeDecl_values.get(_parameters);
      if(!(_o instanceof ASTNode$State.CircularValue)) {
        return ((Boolean)_o).booleanValue();
      } else {
        _value = (ASTNode$State.CircularValue) _o;
      }
    } else {
      _value = new ASTNode$State.CircularValue();
      containedIn_TypeDecl_values.put(_parameters, _value);
      _value.value = Boolean.valueOf(true);
    }
    ASTNode$State state = state();
    boolean new_containedIn_TypeDecl_value;
    if (!state.IN_CIRCLE) {
      state.IN_CIRCLE = true;
      int num = state.boundariesCrossed;
      boolean isFinal = this.is$Final();
      // TODO: fixme
      // state().CIRCLE_INDEX = 1;
      do {
        _value.visited = new Integer(state.CIRCLE_INDEX);
        state.CHANGE = false;
        new_containedIn_TypeDecl_value = containedIn_compute(type);
        if (new_containedIn_TypeDecl_value != ((Boolean)_value.value).booleanValue()) {
          state.CHANGE = true;
          _value.value = Boolean.valueOf(new_containedIn_TypeDecl_value);
        }
        state.CIRCLE_INDEX++;
      } while (state.CHANGE);
      if (isFinal && num == state().boundariesCrossed) {
        containedIn_TypeDecl_values.put(_parameters, new_containedIn_TypeDecl_value);
      } else {
        containedIn_TypeDecl_values.remove(_parameters);
        state.RESET_CYCLE = true;
        boolean $tmp = containedIn_compute(type);
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_containedIn_TypeDecl_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_containedIn_TypeDecl_value = containedIn_compute(type);
      if (state.RESET_CYCLE) {
        containedIn_TypeDecl_values.remove(_parameters);
      }
      else if (new_containedIn_TypeDecl_value != ((Boolean)_value.value).booleanValue()) {
        state.CHANGE = true;
        _value.value = new_containedIn_TypeDecl_value;
      }
      state.INTERMEDIATE_VALUE = true;
      return new_containedIn_TypeDecl_value;
    }
    state.INTERMEDIATE_VALUE = true;
    return ((Boolean)_value.value).booleanValue();
  }
  /**
   * @apilevel internal
   */
  private boolean containedIn_compute(TypeDecl type) {
      if(type == this || type instanceof WildcardType)
        return true;
      else if(type instanceof WildcardExtendsType)
        return this.subtype(((WildcardExtendsType)type).extendsType());
      else if(type instanceof WildcardSuperType)
        return ((WildcardSuperType)type).superType().subtype(this);
      else if(type instanceof TypeVariable)
        return subtype(type);
      return sameStructure(type);
      //return false;
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
        new_sameStructure_TypeDecl_value = t == this;
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
        boolean $tmp = t == this;
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_sameStructure_TypeDecl_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_sameStructure_TypeDecl_value = t == this;
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
   * @attribute syn
   * @aspect GenericsSubtype
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericsSubtype.jrag:291
   */
  @ASTNodeAnnotation.Attribute
  public boolean supertypeTypeVariable(TypeVariable type) {
    ASTNode$State state = state();
    try {
        if(type == this)
          return true;
        for(int i = 0; i < type.getNumTypeBound(); i++)
          if(type.getTypeBound(i).type().subtype(this))
            return true;
        return false;
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect GenericsSubtype
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericsSubtype.jrag:347
   */
  @ASTNodeAnnotation.Attribute
  public boolean supertypeLUBType(LUBType type) {
    ASTNode$State state = state();
    try {
        for(int i = 0; i < type.getNumTypeBound(); i++)
          if(!type.getTypeBound(i).type().subtype(this))
            return false;
        return true;
      }
    finally {
    }
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
        // T1 && .. && Tn <: this, if exists  0 < i <= n Ti <: this
        for(int i = 0; i < type.getNumTypeBound(); i++)
          if(type.getTypeBound(i).type().subtype(this))
            return true;
        return false;
      }
    finally {
    }
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
        new_subtype_TypeDecl_value = type == this;
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
        boolean $tmp = type == this;
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_subtype_TypeDecl_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_subtype_TypeDecl_value = type == this;
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
    boolean supertypeClassDecl_ClassDecl_value = type == this;

    return supertypeClassDecl_ClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeInterfaceDecl(InterfaceDecl type) {
    ASTNode$State state = state();
    boolean supertypeInterfaceDecl_InterfaceDecl_value = type == this;

    return supertypeInterfaceDecl_InterfaceDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeArrayDecl(ArrayDecl type) {
    ASTNode$State state = state();
    boolean supertypeArrayDecl_ArrayDecl_value = this == type;

    return supertypeArrayDecl_ArrayDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypePrimitiveType(PrimitiveType type) {
    ASTNode$State state = state();
    boolean supertypePrimitiveType_PrimitiveType_value = type == this;

    return supertypePrimitiveType_PrimitiveType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeNullType(NullType type) {
    ASTNode$State state = state();
    boolean supertypeNullType_NullType_value = false;

    return supertypeNullType_NullType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeVoidType(VoidType type) {
    ASTNode$State state = state();
    boolean supertypeVoidType_VoidType_value = false;

    return supertypeVoidType_VoidType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeClassDeclSubstituted(ClassDeclSubstituted type) {
    ASTNode$State state = state();
    boolean supertypeClassDeclSubstituted_ClassDeclSubstituted_value = type.original() == this || supertypeClassDecl(type);

    return supertypeClassDeclSubstituted_ClassDeclSubstituted_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeInterfaceDeclSubstituted(InterfaceDeclSubstituted type) {
    ASTNode$State state = state();
    boolean supertypeInterfaceDeclSubstituted_InterfaceDeclSubstituted_value = type.original() == this || supertypeInterfaceDecl(type);

    return supertypeInterfaceDeclSubstituted_InterfaceDeclSubstituted_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeGenericClassDeclSubstituted(GenericClassDeclSubstituted type) {
    ASTNode$State state = state();
    boolean supertypeGenericClassDeclSubstituted_GenericClassDeclSubstituted_value = type.original() == this || supertypeGenericClassDecl(type);

    return supertypeGenericClassDeclSubstituted_GenericClassDeclSubstituted_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean supertypeGenericInterfaceDeclSubstituted(GenericInterfaceDeclSubstituted type) {
    ASTNode$State state = state();
    boolean supertypeGenericInterfaceDeclSubstituted_GenericInterfaceDeclSubstituted_value = type.original() == this || supertypeGenericInterfaceDecl(type);

    return supertypeGenericInterfaceDeclSubstituted_GenericInterfaceDeclSubstituted_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean hasAnnotationSafeVarargs() {
    ASTNode$State state = state();
    boolean hasAnnotationSafeVarargs_value = getModifiers().hasAnnotationSafeVarargs();

    return hasAnnotationSafeVarargs_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isReifiable() {
    ASTNode$State state = state();
    boolean isReifiable_value = true;

    return isReifiable_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean isUncheckedConversionTo(TypeDecl dest) {
    ASTNode$State state = state();
    boolean isUncheckedConversionTo_TypeDecl_value = (!dest.isRawType()) && this.isRawType();

    return isUncheckedConversionTo_TypeDecl_value;
  }
  /**
   * @attribute syn
   * @aspect PreciseRethrow
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/EffectivelyFinal.jrag:39
   */
  @ASTNodeAnnotation.Attribute
  public boolean modifiedInScope(Variable var) {
    ASTNode$State state = state();
    try {
        	for(int i = 0; i < getNumBodyDecl(); i++) {
        		BodyDecl body = getBodyDecl(i);
        		if(body.modifiedInScope(var))
        			return true;
        	}
        	return false;
        }
    finally {
    }
  }
  /**
   * @apilevel internal
   */
  protected boolean isFunctionalInterface_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isFunctionalInterface_value;
/**
 * @apilevel internal
 */
private void isFunctionalInterface_reset() {
  isFunctionalInterface_computed = false;
}  
  @ASTNodeAnnotation.Attribute
  public boolean isFunctionalInterface() {
    if(isFunctionalInterface_computed) {
      return isFunctionalInterface_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isFunctionalInterface_value = false;
    if (isFinal && num == state().boundariesCrossed) {
      isFunctionalInterface_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isFunctionalInterface_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeGenericClassDecl(GenericClassDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeGenericClassDecl_GenericClassDecl_value = strictSupertypeClassDecl(type);

    return strictSupertypeGenericClassDecl_GenericClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeGenericInterfaceDecl(GenericInterfaceDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeGenericInterfaceDecl_GenericInterfaceDecl_value = this == type || strictSupertypeInterfaceDecl(type);

    return strictSupertypeGenericInterfaceDecl_GenericInterfaceDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeRawClassDecl(RawClassDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeRawClassDecl_RawClassDecl_value = strictSupertypeParClassDecl(type);

    return strictSupertypeRawClassDecl_RawClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeRawInterfaceDecl(RawInterfaceDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeRawInterfaceDecl_RawInterfaceDecl_value = strictSupertypeParInterfaceDecl(type);

    return strictSupertypeRawInterfaceDecl_RawInterfaceDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeWildcard(WildcardType type) {
    ASTNode$State state = state();
    boolean strictSupertypeWildcard_WildcardType_value = false;

    return strictSupertypeWildcard_WildcardType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeWildcardExtends(WildcardExtendsType type) {
    ASTNode$State state = state();
    boolean strictSupertypeWildcardExtends_WildcardExtendsType_value = false;

    return strictSupertypeWildcardExtends_WildcardExtendsType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeWildcardSuper(WildcardSuperType type) {
    ASTNode$State state = state();
    boolean strictSupertypeWildcardSuper_WildcardSuperType_value = false;

    return strictSupertypeWildcardSuper_WildcardSuperType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeParClassDecl(ParClassDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeParClassDecl_ParClassDecl_value = strictSupertypeClassDecl(type);

    return strictSupertypeParClassDecl_ParClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeParInterfaceDecl(ParInterfaceDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeParInterfaceDecl_ParInterfaceDecl_value = strictSupertypeInterfaceDecl(type);

    return strictSupertypeParInterfaceDecl_ParInterfaceDecl_value;
  }
/**
 * @apilevel internal
 */
private void strictContainedIn_TypeDecl_reset() {
  strictContainedIn_TypeDecl_values = null;
}  
  protected java.util.Map strictContainedIn_TypeDecl_values;
  @ASTNodeAnnotation.Attribute
  public boolean strictContainedIn(TypeDecl type) {
    Object _parameters = type;
    if (strictContainedIn_TypeDecl_values == null) strictContainedIn_TypeDecl_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    ASTNode$State.CircularValue _value;
    if(strictContainedIn_TypeDecl_values.containsKey(_parameters)) {
      Object _o = strictContainedIn_TypeDecl_values.get(_parameters);
      if(!(_o instanceof ASTNode$State.CircularValue)) {
        return ((Boolean)_o).booleanValue();
      } else {
        _value = (ASTNode$State.CircularValue) _o;
      }
    } else {
      _value = new ASTNode$State.CircularValue();
      strictContainedIn_TypeDecl_values.put(_parameters, _value);
      _value.value = Boolean.valueOf(true);
    }
    ASTNode$State state = state();
    boolean new_strictContainedIn_TypeDecl_value;
    if (!state.IN_CIRCLE) {
      state.IN_CIRCLE = true;
      int num = state.boundariesCrossed;
      boolean isFinal = this.is$Final();
      // TODO: fixme
      // state().CIRCLE_INDEX = 1;
      do {
        _value.visited = new Integer(state.CIRCLE_INDEX);
        state.CHANGE = false;
        new_strictContainedIn_TypeDecl_value = strictContainedIn_compute(type);
        if (new_strictContainedIn_TypeDecl_value != ((Boolean)_value.value).booleanValue()) {
          state.CHANGE = true;
          _value.value = Boolean.valueOf(new_strictContainedIn_TypeDecl_value);
        }
        state.CIRCLE_INDEX++;
      } while (state.CHANGE);
      if (isFinal && num == state().boundariesCrossed) {
        strictContainedIn_TypeDecl_values.put(_parameters, new_strictContainedIn_TypeDecl_value);
      } else {
        strictContainedIn_TypeDecl_values.remove(_parameters);
        state.RESET_CYCLE = true;
        boolean $tmp = strictContainedIn_compute(type);
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_strictContainedIn_TypeDecl_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_strictContainedIn_TypeDecl_value = strictContainedIn_compute(type);
      if (state.RESET_CYCLE) {
        strictContainedIn_TypeDecl_values.remove(_parameters);
      }
      else if (new_strictContainedIn_TypeDecl_value != ((Boolean)_value.value).booleanValue()) {
        state.CHANGE = true;
        _value.value = new_strictContainedIn_TypeDecl_value;
      }
      state.INTERMEDIATE_VALUE = true;
      return new_strictContainedIn_TypeDecl_value;
    }
    state.INTERMEDIATE_VALUE = true;
    return ((Boolean)_value.value).booleanValue();
  }
  /**
   * @apilevel internal
   */
  private boolean strictContainedIn_compute(TypeDecl type) {
      if(type == this || type instanceof WildcardType)
        return true;
      else if(type instanceof WildcardExtendsType)
        return this.strictSubtype(((WildcardExtendsType)type).extendsType());
      else if(type instanceof WildcardSuperType)
        return ((WildcardSuperType)type).superType().strictSubtype(this);
      else if(type instanceof TypeVariable)
        return strictSubtype(type);
      return sameStructure(type);
      //return false;
    }
  /**
   * @attribute syn
   * @aspect StrictSubtype
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/GenericsSubtype.jrag:251
   */
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeTypeVariable(TypeVariable type) {
    ASTNode$State state = state();
    try {
        if(type == this)
          return true;
        for(int i = 0; i < type.getNumTypeBound(); i++)
          if(type.getTypeBound(i).type().strictSubtype(this))
            return true;
        return false;
      }
    finally {
    }
  }
  /**
   * @attribute syn
   * @aspect StrictSubtype
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/GenericsSubtype.jrag:285
   */
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeLUBType(LUBType type) {
    ASTNode$State state = state();
    try {
        for(int i = 0; i < type.getNumTypeBound(); i++)
          if(!type.getTypeBound(i).type().strictSubtype(this))
            return false;
        return true;
      }
    finally {
    }
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
        // T1 && .. && Tn <: this, if exists  0 < i <= n Ti <: this
        for(int i = 0; i < type.getNumTypeBound(); i++)
          if(type.getTypeBound(i).type().strictSubtype(this))
            return true;
        return false;
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
        new_strictSubtype_TypeDecl_value = type == this;
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
        boolean $tmp = type == this;
        state.RESET_CYCLE = false;
      }
      state.IN_CIRCLE = false;
      state.INTERMEDIATE_VALUE = false;
      return new_strictSubtype_TypeDecl_value;
    }
    if (!new Integer(state.CIRCLE_INDEX).equals(_value.visited)) {
      _value.visited = new Integer(state.CIRCLE_INDEX);
      new_strictSubtype_TypeDecl_value = type == this;
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
    boolean strictSupertypeClassDecl_ClassDecl_value = type == this;

    return strictSupertypeClassDecl_ClassDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeInterfaceDecl(InterfaceDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeInterfaceDecl_InterfaceDecl_value = type == this;

    return strictSupertypeInterfaceDecl_InterfaceDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeArrayDecl(ArrayDecl type) {
    ASTNode$State state = state();
    boolean strictSupertypeArrayDecl_ArrayDecl_value = this == type;

    return strictSupertypeArrayDecl_ArrayDecl_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypePrimitiveType(PrimitiveType type) {
    ASTNode$State state = state();
    boolean strictSupertypePrimitiveType_PrimitiveType_value = type == this;

    return strictSupertypePrimitiveType_PrimitiveType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeNullType(NullType type) {
    ASTNode$State state = state();
    boolean strictSupertypeNullType_NullType_value = false;

    return strictSupertypeNullType_NullType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeVoidType(VoidType type) {
    ASTNode$State state = state();
    boolean strictSupertypeVoidType_VoidType_value = false;

    return strictSupertypeVoidType_VoidType_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeClassDeclSubstituted(ClassDeclSubstituted type) {
    ASTNode$State state = state();
    boolean strictSupertypeClassDeclSubstituted_ClassDeclSubstituted_value = type.original() == this || strictSupertypeClassDecl(type);

    return strictSupertypeClassDeclSubstituted_ClassDeclSubstituted_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeInterfaceDeclSubstituted(InterfaceDeclSubstituted type) {
    ASTNode$State state = state();
    boolean strictSupertypeInterfaceDeclSubstituted_InterfaceDeclSubstituted_value = type.original() == this || strictSupertypeInterfaceDecl(type);

    return strictSupertypeInterfaceDeclSubstituted_InterfaceDeclSubstituted_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeGenericClassDeclSubstituted(GenericClassDeclSubstituted type) {
    ASTNode$State state = state();
    boolean strictSupertypeGenericClassDeclSubstituted_GenericClassDeclSubstituted_value = type.original() == this || strictSupertypeGenericClassDecl(type);

    return strictSupertypeGenericClassDeclSubstituted_GenericClassDeclSubstituted_value;
  }
  @ASTNodeAnnotation.Attribute
  public boolean strictSupertypeGenericInterfaceDeclSubstituted(GenericInterfaceDeclSubstituted type) {
    ASTNode$State state = state();
    boolean strictSupertypeGenericInterfaceDeclSubstituted_GenericInterfaceDeclSubstituted_value = type.original() == this || strictSupertypeGenericInterfaceDecl(type);

    return strictSupertypeGenericInterfaceDeclSubstituted_GenericInterfaceDeclSubstituted_value;
  }
  /**
   * @attribute inh
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:1176
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeWildcard() {
    ASTNode$State state = state();
    TypeDecl typeWildcard_value = getParent().Define_TypeDecl_typeWildcard(this, null);

    return typeWildcard_value;
  }
  /**
   * @attribute inh
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:1189
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl lookupWildcardExtends(TypeDecl typeDecl) {
    ASTNode$State state = state();
    TypeDecl lookupWildcardExtends_TypeDecl_value = getParent().Define_TypeDecl_lookupWildcardExtends(this, null, typeDecl);

    return lookupWildcardExtends_TypeDecl_value;
  }
  /**
   * @attribute inh
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:1202
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl lookupWildcardSuper(TypeDecl typeDecl) {
    ASTNode$State state = state();
    TypeDecl lookupWildcardSuper_TypeDecl_value = getParent().Define_TypeDecl_lookupWildcardSuper(this, null, typeDecl);

    return lookupWildcardSuper_TypeDecl_value;
  }
  /**
   * @attribute inh
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:1222
   */
  @ASTNodeAnnotation.Attribute
  public LUBType lookupLUBType(Collection bounds) {
    ASTNode$State state = state();
    LUBType lookupLUBType_Collection_value = getParent().Define_LUBType_lookupLUBType(this, null, bounds);

    return lookupLUBType_Collection_value;
  }
  /**
   * @attribute inh
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:1262
   */
  @ASTNodeAnnotation.Attribute
  public GLBType lookupGLBType(ArrayList bounds) {
    ASTNode$State state = state();
    GLBType lookupGLBType_ArrayList_value = getParent().Define_GLBType_lookupGLBType(this, null, bounds);

    return lookupGLBType_ArrayList_value;
  }
  /**
   * @attribute inh
   * @aspect Arrays
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Arrays.jrag:21
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl componentType() {
    if(componentType_computed) {
      return componentType_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    componentType_value = getParent().Define_TypeDecl_componentType(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      componentType_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return componentType_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean componentType_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl componentType_value;
/**
 * @apilevel internal
 */
private void componentType_reset() {
  componentType_computed = false;
  componentType_value = null;
}  
  /**
   * @attribute inh
   * @aspect Arrays
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Arrays.jrag:50
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeCloneable() {
    ASTNode$State state = state();
    TypeDecl typeCloneable_value = getParent().Define_TypeDecl_typeCloneable(this, null);

    return typeCloneable_value;
  }
  /**
   * @attribute inh
   * @aspect Arrays
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Arrays.jrag:51
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeSerializable() {
    ASTNode$State state = state();
    TypeDecl typeSerializable_value = getParent().Define_TypeDecl_typeSerializable(this, null);

    return typeSerializable_value;
  }
  /**
   * @attribute inh
   * @aspect ClassPath
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ClassPath.jrag:52
   */
  @ASTNodeAnnotation.Attribute
  public CompilationUnit compilationUnit() {
    ASTNode$State state = state();
    CompilationUnit compilationUnit_value = getParent().Define_CompilationUnit_compilationUnit(this, null);

    return compilationUnit_value;
  }
  /**
   * @attribute inh
   * @aspect DA
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:242
   */
  @ASTNodeAnnotation.Attribute
  public boolean isDAbefore(Variable v) {
    Object _parameters = v;
    if (isDAbefore_Variable_values == null) isDAbefore_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(isDAbefore_Variable_values.containsKey(_parameters)) {
      return ((Boolean)isDAbefore_Variable_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean isDAbefore_Variable_value = getParent().Define_boolean_isDAbefore(this, null, v);
    if (isFinal && num == state().boundariesCrossed) {
      isDAbefore_Variable_values.put(_parameters, Boolean.valueOf(isDAbefore_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDAbefore_Variable_value;
  }
  protected java.util.Map isDAbefore_Variable_values;
/**
 * @apilevel internal
 */
private void isDAbefore_Variable_reset() {
  isDAbefore_Variable_values = null;
}  
  /**
   * @attribute inh
   * @aspect DU
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:728
   */
  @ASTNodeAnnotation.Attribute
  public boolean isDUbefore(Variable v) {
    Object _parameters = v;
    if (isDUbefore_Variable_values == null) isDUbefore_Variable_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(isDUbefore_Variable_values.containsKey(_parameters)) {
      return ((Boolean)isDUbefore_Variable_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean isDUbefore_Variable_value = getParent().Define_boolean_isDUbefore(this, null, v);
    if (isFinal && num == state().boundariesCrossed) {
      isDUbefore_Variable_values.put(_parameters, Boolean.valueOf(isDUbefore_Variable_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isDUbefore_Variable_value;
  }
  protected java.util.Map isDUbefore_Variable_values;
/**
 * @apilevel internal
 */
private void isDUbefore_Variable_reset() {
  isDUbefore_Variable_values = null;
}  
  /**
   * @attribute inh
   * @aspect ExceptionHandling
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ExceptionHandling.jrag:20
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeException() {
    if(typeException_computed) {
      return typeException_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeException_value = getParent().Define_TypeDecl_typeException(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      typeException_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeException_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeException_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeException_value;
/**
 * @apilevel internal
 */
private void typeException_reset() {
  typeException_computed = false;
  typeException_value = null;
}  
  /**
   * @attribute inh
   * @aspect ExceptionHandling
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ExceptionHandling.jrag:22
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeRuntimeException() {
    if(typeRuntimeException_computed) {
      return typeRuntimeException_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeRuntimeException_value = getParent().Define_TypeDecl_typeRuntimeException(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      typeRuntimeException_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeRuntimeException_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeRuntimeException_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeRuntimeException_value;
/**
 * @apilevel internal
 */
private void typeRuntimeException_reset() {
  typeRuntimeException_computed = false;
  typeRuntimeException_value = null;
}  
  /**
   * @attribute inh
   * @aspect ExceptionHandling
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ExceptionHandling.jrag:24
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeError() {
    if(typeError_computed) {
      return typeError_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeError_value = getParent().Define_TypeDecl_typeError(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      typeError_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeError_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeError_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeError_value;
/**
 * @apilevel internal
 */
private void typeError_reset() {
  typeError_computed = false;
  typeError_value = null;
}  
  /**
   * @attribute inh
   * @aspect LookupMethod
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupMethod.jrag:46
   */
  @ASTNodeAnnotation.Attribute
  public Collection lookupMethod(String name) {
    Object _parameters = name;
    if (lookupMethod_String_values == null) lookupMethod_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(lookupMethod_String_values.containsKey(_parameters)) {
      return (Collection)lookupMethod_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    Collection lookupMethod_String_value = getParent().Define_Collection_lookupMethod(this, null, name);
    if (isFinal && num == state().boundariesCrossed) {
      lookupMethod_String_values.put(_parameters, lookupMethod_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return lookupMethod_String_value;
  }
  protected java.util.Map lookupMethod_String_values;
/**
 * @apilevel internal
 */
private void lookupMethod_String_reset() {
  lookupMethod_String_values = null;
}  
  /**
   * @attribute inh
   * @aspect SpecialClasses
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:62
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeInt() {
    ASTNode$State state = state();
    TypeDecl typeInt_value = getParent().Define_TypeDecl_typeInt(this, null);

    return typeInt_value;
  }
  /**
   * @attribute inh
   * @aspect SpecialClasses
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:65
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeObject() {
    if(typeObject_computed) {
      return typeObject_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeObject_value = getParent().Define_TypeDecl_typeObject(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      typeObject_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeObject_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeObject_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeObject_value;
/**
 * @apilevel internal
 */
private void typeObject_reset() {
  typeObject_computed = false;
  typeObject_value = null;
}  
  /**
   * @attribute inh
   * @aspect LookupFullyQualifiedTypes
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:101
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl lookupType(String packageName, String typeName) {
    ASTNode$State state = state();
    TypeDecl lookupType_String_String_value = getParent().Define_TypeDecl_lookupType(this, null, packageName, typeName);

    return lookupType_String_String_value;
  }
  /**
   * @attribute inh
   * @aspect TypeScopePropagation
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:226
   */
  @ASTNodeAnnotation.Attribute
  public SimpleSet lookupType(String name) {
    Object _parameters = name;
    if (lookupType_String_values == null) lookupType_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(lookupType_String_values.containsKey(_parameters)) {
      return (SimpleSet)lookupType_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    SimpleSet lookupType_String_value = getParent().Define_SimpleSet_lookupType(this, null, name);
    if (isFinal && num == state().boundariesCrossed) {
      lookupType_String_values.put(_parameters, lookupType_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return lookupType_String_value;
  }
  protected java.util.Map lookupType_String_values;
/**
 * @apilevel internal
 */
private void lookupType_String_reset() {
  lookupType_String_values = null;
}  
  /**
   * @attribute inh
   * @aspect VariableScope
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupVariable.jrag:14
   */
  @ASTNodeAnnotation.Attribute
  public SimpleSet lookupVariable(String name) {
    Object _parameters = name;
    if (lookupVariable_String_values == null) lookupVariable_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(lookupVariable_String_values.containsKey(_parameters)) {
      return (SimpleSet)lookupVariable_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    SimpleSet lookupVariable_String_value = getParent().Define_SimpleSet_lookupVariable(this, null, name);
    if (isFinal && num == state().boundariesCrossed) {
      lookupVariable_String_values.put(_parameters, lookupVariable_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return lookupVariable_String_value;
  }
  protected java.util.Map lookupVariable_String_values;
/**
 * @apilevel internal
 */
private void lookupVariable_String_reset() {
  lookupVariable_String_values = null;
}  
  /**
   * @attribute inh
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:268
   */
  @ASTNodeAnnotation.Attribute
  public boolean hasPackage(String packageName) {
    ASTNode$State state = state();
    boolean hasPackage_String_value = getParent().Define_boolean_hasPackage(this, null, packageName);

    return hasPackage_String_value;
  }
  /**
   * @attribute inh
   * @aspect NameCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:271
   */
  @ASTNodeAnnotation.Attribute
  public ASTNode enclosingBlock() {
    ASTNode$State state = state();
    ASTNode enclosingBlock_value = getParent().Define_ASTNode_enclosingBlock(this, null);

    return enclosingBlock_value;
  }
  /**
   * @attribute inh
   * @aspect TypeName
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/QualifiedNames.jrag:90
   */
  @ASTNodeAnnotation.Attribute
  public String packageName() {
    if(packageName_computed) {
      return packageName_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    packageName_value = getParent().Define_String_packageName(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      packageName_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return packageName_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean packageName_computed = false;
  /**
   * @apilevel internal
   */
  protected String packageName_value;
/**
 * @apilevel internal
 */
private void packageName_reset() {
  packageName_computed = false;
  packageName_value = null;
}  
  /**
   * @attribute inh
   * @aspect TypeAnalysis
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:216
   */
  @ASTNodeAnnotation.Attribute
  public boolean isAnonymous() {
    if(isAnonymous_computed) {
      return isAnonymous_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    isAnonymous_value = getParent().Define_boolean_isAnonymous(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      isAnonymous_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return isAnonymous_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean isAnonymous_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean isAnonymous_value;
/**
 * @apilevel internal
 */
private void isAnonymous_reset() {
  isAnonymous_computed = false;
}  
  /**
   * @attribute inh
   * @aspect NestedTypes
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:506
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl enclosingType() {
    ASTNode$State state = state();
    TypeDecl enclosingType_value = getParent().Define_TypeDecl_enclosingType(this, null);

    return enclosingType_value;
  }
  /**
   * @attribute inh
   * @aspect NestedTypes
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:522
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
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:529
   */
  @ASTNodeAnnotation.Attribute
  public boolean isNestedType() {
    ASTNode$State state = state();
    boolean isNestedType_value = getParent().Define_boolean_isNestedType(this, null);

    return isNestedType_value;
  }
  /**
   * @attribute inh
   * @aspect NestedTypes
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:537
   */
  @ASTNodeAnnotation.Attribute
  public boolean isMemberType() {
    ASTNode$State state = state();
    boolean isMemberType_value = getParent().Define_boolean_isMemberType(this, null);

    return isMemberType_value;
  }
  /**
   * @attribute inh
   * @aspect NestedTypes
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:551
   */
  @ASTNodeAnnotation.Attribute
  public boolean isLocalClass() {
    ASTNode$State state = state();
    boolean isLocalClass_value = getParent().Define_boolean_isLocalClass(this, null);

    return isLocalClass_value;
  }
  /**
   * @attribute inh
   * @aspect NestedTypes
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:576
   */
  @ASTNodeAnnotation.Attribute
  public String hostPackage() {
    ASTNode$State state = state();
    String hostPackage_value = getParent().Define_String_hostPackage(this, null);

    return hostPackage_value;
  }
  /**
   * @attribute inh
   * @aspect Circularity
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:685
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl unknownType() {
    if(unknownType_computed) {
      return unknownType_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    unknownType_value = getParent().Define_TypeDecl_unknownType(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      unknownType_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return unknownType_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean unknownType_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl unknownType_value;
/**
 * @apilevel internal
 */
private void unknownType_reset() {
  unknownType_computed = false;
  unknownType_value = null;
}  
  /**
   * @attribute inh
   * @aspect TypeCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeCheck.jrag:405
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeVoid() {
    ASTNode$State state = state();
    TypeDecl typeVoid_value = getParent().Define_TypeDecl_typeVoid(this, null);

    return typeVoid_value;
  }
  /**
   * @attribute inh
   * @aspect TypeCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeCheck.jrag:509
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl enclosingInstance() {
    ASTNode$State state = state();
    TypeDecl enclosingInstance_value = getParent().Define_TypeDecl_enclosingInstance(this, null);

    return enclosingInstance_value;
  }
  /**
   * @attribute inh
   * @aspect TypeHierarchyCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeHierarchyCheck.jrag:146
   */
  @ASTNodeAnnotation.Attribute
  public boolean inExplicitConstructorInvocation() {
    if(inExplicitConstructorInvocation_computed) {
      return inExplicitConstructorInvocation_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    inExplicitConstructorInvocation_value = getParent().Define_boolean_inExplicitConstructorInvocation(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      inExplicitConstructorInvocation_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return inExplicitConstructorInvocation_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean inExplicitConstructorInvocation_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean inExplicitConstructorInvocation_value;
/**
 * @apilevel internal
 */
private void inExplicitConstructorInvocation_reset() {
  inExplicitConstructorInvocation_computed = false;
}  
  /**
   * @attribute inh
   * @aspect TypeHierarchyCheck
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeHierarchyCheck.jrag:162
   */
  @ASTNodeAnnotation.Attribute
  public boolean inStaticContext() {
    if(inStaticContext_computed) {
      return inStaticContext_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    inStaticContext_value = getParent().Define_boolean_inStaticContext(this, null);
    if (isFinal && num == state().boundariesCrossed) {
      inStaticContext_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return inStaticContext_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean inStaticContext_computed = false;
  /**
   * @apilevel internal
   */
  protected boolean inStaticContext_value;
/**
 * @apilevel internal
 */
private void inStaticContext_reset() {
  inStaticContext_computed = false;
}  
  /**
   * @attribute inh
   * @aspect Annotations
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:289
   */
  @ASTNodeAnnotation.Attribute
  public boolean withinSuppressWarnings(String annot) {
    ASTNode$State state = state();
    boolean withinSuppressWarnings_String_value = getParent().Define_boolean_withinSuppressWarnings(this, null, annot);

    return withinSuppressWarnings_String_value;
  }
  /**
   * @attribute inh
   * @aspect Annotations
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:389
   */
  @ASTNodeAnnotation.Attribute
  public boolean withinDeprecatedAnnotation() {
    ASTNode$State state = state();
    boolean withinDeprecatedAnnotation_value = getParent().Define_boolean_withinDeprecatedAnnotation(this, null);

    return withinDeprecatedAnnotation_value;
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Arrays.jrag:20
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_componentType(ASTNode caller, ASTNode child) {
    if (caller == arrayType_value) {
      return this;
    }
    else {
      return getParent().Define_TypeDecl_componentType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:20
   * @apilevel internal
   */
  public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:30
   * @apilevel internal
   */
  public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return true;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:254
   * @apilevel internal
   */
  public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v, BodyDecl b) {
     {
      int childIndex = this.getIndexOfChild(caller);
{
    //if(b instanceof MethodDecl || b instanceof MemberTypeDecl) {
    if(!v.isInstanceVariable() && !v.isClassVariable()) {
      if(v.hostType() != this)
        return isDAbefore(v);
      return false;
    }
    if(b instanceof FieldDeclaration && !((FieldDeclaration)b).isStatic() && v.isClassVariable())
      return true;

    if(b instanceof MethodDecl) {
      return true;
    }
    if(b instanceof MemberTypeDecl && v.isBlank() && v.isFinal() && v.hostType() == this)
      return true;
    if(v.isClassVariable() || v.isInstanceVariable()) {
      if(v.isFinal() &&  v.hostType() != this && instanceOf(v.hostType()))
        return true;

      boolean search = true;
      if (b instanceof ConstructorDecl) {
        search = false;
      }

      for (int i = getNumBodyDecl() - 1; i >= 0; --i) {
        BodyDecl decl = getBodyDecl(i);
        if (b == decl) {
          search = false;
          continue;
        }
        if (search) {
          continue;
        }
        if (decl instanceof FieldDeclaration) {
          FieldDeclaration f = (FieldDeclaration) decl;
          if ((v.isClassVariable() && f.isStatic()) || (v.isInstanceVariable() && !f.isStatic())) {
            boolean c = f.isDAafter(v);
            //System.err.println("DefiniteAssignment: is " + v.name() + " DA after index " + i + ", " + f + ": " + c);
            return c;
            //return f.isDAafter(v);
          }
        } else if (decl instanceof StaticInitializer && v.isClassVariable()) {
          StaticInitializer si = (StaticInitializer) decl;
          return si.isDAafter(v);
        } else if (decl instanceof InstanceInitializer && v.isInstanceVariable()) {
          InstanceInitializer ii = (InstanceInitializer) decl;
          return ii.isDAafter(v);
        }
      }
    }
    return isDAbefore(v);
  }
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:742
   * @apilevel internal
   */
  public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v, BodyDecl b) {
     {
      int childIndex = this.getIndexOfChild(caller);
{
    if (b instanceof MethodDecl || b instanceof MemberTypeDecl) {
      return false;
    }
    if (v.isClassVariable() || v.isInstanceVariable()) {
      boolean search = true;
      if (b instanceof ConstructorDecl) {
        search = false;
      }

      for (int i = getNumBodyDecl() - 1; i >= 0; --i) {
        BodyDecl decl = getBodyDecl(i);
        if (b == decl) {
          search = false;
          continue;
        }
        if (search) {
          continue;
        }
        if (decl instanceof FieldDeclaration) {
          FieldDeclaration f = (FieldDeclaration) decl;
          //System.err.println("  working on field " + f.name() + " which is child " + i);
          if (f == v)
            return !f.hasInit();
          if ((v.isClassVariable() && f.isStatic()) || (v.isInstanceVariable() && !f.isStatic()))
            return f.isDUafter(v);
          //System.err.println("  field " + f.name() + " can not affect " + v.name());
        } else if (decl instanceof StaticInitializer && v.isClassVariable()) {
          StaticInitializer si = (StaticInitializer) decl;
          //System.err.println("  working on static initializer which is child " + i);
          return si.isDUafter(v);
        } else if (decl instanceof InstanceInitializer && v.isInstanceVariable()) {
          InstanceInitializer ii = (InstanceInitializer) decl;
          //System.err.println("  working on instance initializer which is child " + i);
          return ii.isDUafter(v);
        }
      }
    }
    //System.err.println("Reached TypeDecl when searching for DU for variable");
    return isDUbefore(v);
  }
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupConstructor.jrag:16
   * @apilevel internal
   */
  public Collection Define_Collection_lookupConstructor(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return constructors();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupConstructor.jrag:20
   * @apilevel internal
   */
  public Collection Define_Collection_lookupSuperConstructor(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return lookupSuperConstructor();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupMethod.jrag:54
   * @apilevel internal
   */
  public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
    if (caller == getBodyDeclListNoTransform()) {
      int i = caller.getIndexOfChild(child);
      return unqualifiedLookupMethod(name);
    }
    else {
      return getParent().Define_Collection_lookupMethod(this, caller, name);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:324
   * @apilevel internal
   */
  public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
    if (caller == getBodyDeclListNoTransform()) {
      int index = caller.getIndexOfChild(child);
      return localLookupType(name);
    }
    else {
      return getParent().Define_SimpleSet_lookupType(this, caller, name);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupVariable.jrag:27
   * @apilevel internal
   */
  public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
    if (caller == getBodyDeclListNoTransform()) {
      int i = caller.getIndexOfChild(child);
      {
    SimpleSet list = memberFields(name);
    if(!list.isEmpty()) return list;
    list = lookupVariable(name);
    if(inStaticContext() || isStatic())
      list = removeInstanceVariables(list);
    return list;
  }
    }
    else {
      return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:301
   * @apilevel internal
   */
  public boolean Define_boolean_mayBePublic(ASTNode caller, ASTNode child) {
    if (caller == getBodyDeclListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else if (caller == getModifiersNoTransform()) {
      return true;
    }
    else {
      return getParent().Define_boolean_mayBePublic(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:302
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeProtected(ASTNode caller, ASTNode child) {
    if (caller == getBodyDeclListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else if (caller == getModifiersNoTransform()) {
      return true;
    }
    else {
      return getParent().Define_boolean_mayBeProtected(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:303
   * @apilevel internal
   */
  public boolean Define_boolean_mayBePrivate(ASTNode caller, ASTNode child) {
    if (caller == getBodyDeclListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else if (caller == getModifiersNoTransform()) {
      return true;
    }
    else {
      return getParent().Define_boolean_mayBePrivate(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:306
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeAbstract(ASTNode caller, ASTNode child) {
    if (caller == getBodyDeclListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else if (caller == getModifiersNoTransform()) {
      return true;
    }
    else {
      return getParent().Define_boolean_mayBeAbstract(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:304
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeStatic(ASTNode caller, ASTNode child) {
    if (caller == getBodyDeclListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else if (caller == getModifiersNoTransform()) {
      return true;
    }
    else {
      return getParent().Define_boolean_mayBeStatic(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:309
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeStrictfp(ASTNode caller, ASTNode child) {
    if (caller == getBodyDeclListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else if (caller == getModifiersNoTransform()) {
      return true;
    }
    else {
      return getParent().Define_boolean_mayBeStrictfp(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:305
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
    if (caller == getBodyDeclListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else {
      return getParent().Define_boolean_mayBeFinal(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:307
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeVolatile(ASTNode caller, ASTNode child) {
    if (caller == getBodyDeclListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else {
      return getParent().Define_boolean_mayBeVolatile(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:308
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeTransient(ASTNode caller, ASTNode child) {
    if (caller == getBodyDeclListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else {
      return getParent().Define_boolean_mayBeTransient(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:310
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeSynchronized(ASTNode caller, ASTNode child) {
    if (caller == getBodyDeclListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else {
      return getParent().Define_boolean_mayBeSynchronized(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:311
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeNative(ASTNode caller, ASTNode child) {
    if (caller == getBodyDeclListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else {
      return getParent().Define_boolean_mayBeNative(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:323
   * @apilevel internal
   */
  public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return this;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:396
   * @apilevel internal
   */
  public boolean Define_boolean_insideLoop(ASTNode caller, ASTNode child) {
    if (caller == getBodyDeclListNoTransform()) {
      int i = caller.getIndexOfChild(child);
      return false;
    }
    else {
      return getParent().Define_boolean_insideLoop(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:403
   * @apilevel internal
   */
  public boolean Define_boolean_insideSwitch(ASTNode caller, ASTNode child) {
    if (caller == getBodyDeclListNoTransform()) {
      int i = caller.getIndexOfChild(child);
      return false;
    }
    else {
      return getParent().Define_boolean_insideSwitch(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/SyntacticClassification.jrag:118
   * @apilevel internal
   */
  public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
    if (caller == getBodyDeclListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return NameType.EXPRESSION_NAME;
    }
    else {
      return getParent().Define_NameType_nameType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:218
   * @apilevel internal
   */
  public boolean Define_boolean_isAnonymous(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:502
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_enclosingType(ASTNode caller, ASTNode child) {
    if (caller == getBodyDeclListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return this;
    }
    else {
      return getParent().Define_TypeDecl_enclosingType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:531
   * @apilevel internal
   */
  public boolean Define_boolean_isNestedType(ASTNode caller, ASTNode child) {
    if (caller == getBodyDeclListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return true;
    }
    else {
      return getParent().Define_boolean_isNestedType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:553
   * @apilevel internal
   */
  public boolean Define_boolean_isLocalClass(ASTNode caller, ASTNode child) {
    if (caller == getBodyDeclListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return false;
    }
    else {
      return getParent().Define_boolean_isLocalClass(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:582
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_hostType(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return this;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeCheck.jrag:407
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_returnType(ASTNode caller, ASTNode child) {
    if (caller == getBodyDeclListNoTransform()) {
      int childIndex = caller.getIndexOfChild(child);
      return typeVoid();
    }
    else {
      return getParent().Define_TypeDecl_returnType(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeCheck.jrag:513
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_enclosingInstance(ASTNode caller, ASTNode child) {
    if (caller == getBodyDeclListNoTransform()) {
      int index = caller.getIndexOfChild(child);
      {
    if(getBodyDecl(index) instanceof MemberTypeDecl && !((MemberTypeDecl)getBodyDecl(index)).typeDecl().isInnerType())
      return null;
    if(getBodyDecl(index) instanceof ConstructorDecl)
      return enclosingInstance();
    return this;
  }
    }
    else {
      return getParent().Define_TypeDecl_enclosingInstance(this, caller);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeHierarchyCheck.jrag:12
   * @apilevel internal
   */
  public String Define_String_methodHost(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return typeName();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeHierarchyCheck.jrag:165
   * @apilevel internal
   */
  public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return isStatic() || inStaticContext();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/UnreachableStatements.jrag:162
   * @apilevel internal
   */
  public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return true;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:74
   * @apilevel internal
   */
  public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
    if (caller == getModifiersNoTransform()) {
      return name.equals("TYPE");
    }
    else {
      return getParent().Define_boolean_mayUseAnnotationTarget(this, caller, name);
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
