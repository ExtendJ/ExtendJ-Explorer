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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/grammar/Java.ast:1
 * @production Program : {@link ASTNode} ::= <span class="component">{@link CompilationUnit}*</span>;

 */
public class Program extends ASTNode<ASTNode> implements Cloneable {
  /**
   * @aspect LibraryCompilationUnits
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LibCompilationUnits.jadd:2
   */
  public Map<String, CompilationUnit> getLibCompilationUnitValueMap() {

		return getLibCompilationUnit_String_values;
	}
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:39
   */
  public void prettyPrint(StringBuffer sb) {
    for (Iterator iter = compilationUnitIterator(); iter.hasNext(); ) {
      CompilationUnit cu = (CompilationUnit) iter.next();
      if (cu.fromSource()) {
        cu.prettyPrint(sb);
      }
    }
  }
  /**
   * @aspect PrettyPrint
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:871
   */
  public String dumpTree() {
    StringBuffer sb = new StringBuffer();
    for (Iterator iter = compilationUnitIterator(); iter.hasNext(); ) {
      CompilationUnit cu = (CompilationUnit)iter.next();
      if (cu.fromSource()) {
        sb.append(cu.dumpTree());
      }
    }
    return sb.toString();
  }
  /**
   * @aspect ClassPath
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ClassPath.jrag:26
   */
  protected BytecodeReader bytecodeReader;
  /**
   * @aspect ClassPath
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ClassPath.jrag:27
   */
  public void initBytecodeReader(BytecodeReader r) {
    bytecodeReader = r;
  }
  /**
   * @aspect ClassPath
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ClassPath.jrag:31
   */
  protected JavaParser javaParser;
  /**
   * @aspect ClassPath
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ClassPath.jrag:32
   */
  public void initJavaParser(JavaParser p) {
    javaParser = p;
  }
  /**
   * Parse the source file and add the compilation unit to the list of
   * compilation units in the program.
   * 
   * @param fileName file name of the source file
   * @return The CompilationUnit representing the source file,
   * or <code>null</code> if the source file could not be parsed
   * @aspect ClassPath
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ClassPath.jrag:63
   */
  public CompilationUnit addSourceFile(String fileName) {
    try {
      SourceFilePath pathPart = new SourceFilePath(fileName);
      CompilationUnit cu = pathPart.getCompilationUnit(this, fileName);
      if (cu != null) {
        classPath.addPackage(cu.packageName());
        addCompilationUnit(cu);
      }
      return cu;
    } catch (IOException e) {
      return null;
    }
  }
  /**
   * Iterate over all source files and on-demand loaded compilation units.
   * @aspect ClassPath
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ClassPath.jrag:80
   */
  public Iterator<CompilationUnit> compilationUnitIterator() {
    return new Iterator<CompilationUnit>() {
      int index = 0;
      public boolean hasNext() {
        return index < getNumCompilationUnit();
      }
      public CompilationUnit next() {
        if (getNumCompilationUnit() == index) {
          throw new java.util.NoSuchElementException();
        }
        return getCompilationUnit(index++);
      }
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }
  /**
   * Get the input stream for a compilation unit specified using a canonical
   * name. This is used by the bytecode reader to load nested types.
   * @param name The canonical name of the compilation unit.
   * @aspect ClassPath
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ClassPath.jrag:103
   */
  public InputStream getInputStream(String name) {
    return classPath.getInputStream(name);
  }
  /**
   * @aspect ClassPath
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ClassPath.jrag:107
   */
  private final ClassPath classPath = new ClassPath(this);
  /**
   * Load a compilation unit from disk, selecting a class file
   * if one exists that is not older than a corresponding source
   * file, otherwise the source file is selected.
   * 
   * This method is called by the LibCompilationUnit NTA.  We rely on the
   * result of this method being cached because it will return a newly parsed
   * compilation unit each time it is called.
   * 
   * @param name Qualified name of class to load.
   * @return the loaded compilation unit, or {@code null} if no compilation
   * unit was found
   * @aspect ClassPath
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ClassPath.jrag:366
   */
  public CompilationUnit getCompilationUnit(String name) {
    return classPath.getCompilationUnit(name);
  }
  /**
   * @return <code>true</code> if there is a package with the given name on
   * the classpath
   * @aspect ClassPath
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ClassPath.jrag:374
   */
  public boolean isPackage(String name) {
    return classPath.isPackage(name);
  }
  /**
   * Add a path part to the library class path.
   * @aspect ClassPath
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ClassPath.jrag:394
   */
  public void addClassPath(PathPart pathPart) {
    classPath.addClassPath(pathPart);
  }
  /**
   * Add a path part to the user class path.
   * @aspect ClassPath
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ClassPath.jrag:401
   */
  public void addSourcePath(PathPart pathPart) {
    classPath.addSourcePath(pathPart);
  }
  /**
   * @aspect FrontendMain
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/FrontendMain.jrag:35
   */
  protected long javaParseTime;
  /**
   * @aspect FrontendMain
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/FrontendMain.jrag:36
   */
  protected long bytecodeParseTime;
  /**
   * @aspect FrontendMain
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/FrontendMain.jrag:37
   */
  protected long codeGenTime;
  /**
   * @aspect FrontendMain
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/FrontendMain.jrag:38
   */
  protected long errorCheckTime;
  /**
   * @aspect FrontendMain
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/FrontendMain.jrag:39
   */
  protected int numJavaFiles;
  /**
   * @aspect FrontendMain
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/FrontendMain.jrag:40
   */
  protected int numClassFiles;
  /**
   * @aspect LookupFullyQualifiedTypes
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:104
   */
  public int classFileReadTime;
  /**
   * @declaredat ASTNode:1
   */
  public Program() {
    super();
    is$Final(true);
  }
  /**
   * Initializes the child array to the correct size.
   * Initializes List and Opt nta children.
   * @apilevel internal
   * @ast method
   * @declaredat ASTNode:11
   */
  public void init$Children() {
    children = new ASTNode[1];
    setChild(new List(), 0);
  }
  /**
   * @declaredat ASTNode:15
   */
  public Program(List<CompilationUnit> p0) {
    setChild(p0, 0);
    is$Final(true);
  }
  /**
   * @apilevel low-level
   * @declaredat ASTNode:22
   */
  protected int numChildren() {
    return 1;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:28
   */
  public boolean mayHaveRewrite() {
    return false;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:34
   */
  public void flushAttrCache() {
    super.flushAttrCache();
    wildcards_reset();
    typeObject_reset();
    typeCloneable_reset();
    typeSerializable_reset();
    typeBoolean_reset();
    typeByte_reset();
    typeShort_reset();
    typeChar_reset();
    typeInt_reset();
    typeLong_reset();
    typeFloat_reset();
    typeDouble_reset();
    typeString_reset();
    typeVoid_reset();
    typeNull_reset();
    unknownType_reset();
    hasPackage_String_reset();
    lookupType_String_String_reset();
    lookupLibType_String_String_reset();
    getLibCompilationUnit_String_reset();
    getPrimitiveCompilationUnit_reset();
    unknownConstructor_reset();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:62
   */
  public void flushCollectionCache() {
    super.flushCollectionCache();
        collect_contributors_BlockLambdaBody_lambdaReturns = false;
  }
  /**
   * @api internal
   * @declaredat ASTNode:69
   */
  public void flushRewriteCache() {
    super.flushRewriteCache();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:75
   */
  public Program clone() throws CloneNotSupportedException {
    Program node = (Program) super.clone();
    return node;
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:82
   */
  public Program copy() {
    try {
      Program node = (Program) clone();
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
   * @declaredat ASTNode:101
   */
  public Program fullCopy() {
    return treeCopyNoTransform();
  }
  /**
   * Create a deep copy of the AST subtree at this node.
   * The copy is dangling, i.e. has no parent.
   * @return dangling copy of the subtree at this node
   * @apilevel low-level
   * @declaredat ASTNode:110
   */
  public Program treeCopyNoTransform() {
    Program tree = (Program) copy();
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
   * @declaredat ASTNode:130
   */
  public Program treeCopy() {
    doFullTraversal();
    return treeCopyNoTransform();
  }
  /**
   * @apilevel internal
   * @declaredat ASTNode:137
   */
  protected boolean is$Equal(ASTNode node) {
    return super.is$Equal(node) ;    
  }
  /**
   * Replaces the CompilationUnit list.
   * @param list The new list node to be used as the CompilationUnit list.
   * @apilevel high-level
   */
  public void setCompilationUnitList(List<CompilationUnit> list) {
    setChild(list, 0);
  }
  /**
   * Retrieves the number of children in the CompilationUnit list.
   * @return Number of children in the CompilationUnit list.
   * @apilevel high-level
   */
  public int getNumCompilationUnit() {
    return getCompilationUnitList().getNumChild();
  }
  /**
   * Retrieves the number of children in the CompilationUnit list.
   * Calling this method will not trigger rewrites.
   * @return Number of children in the CompilationUnit list.
   * @apilevel low-level
   */
  public int getNumCompilationUnitNoTransform() {
    return getCompilationUnitListNoTransform().getNumChildNoTransform();
  }
  /**
   * Retrieves the element at index {@code i} in the CompilationUnit list.
   * @param i Index of the element to return.
   * @return The element at position {@code i} in the CompilationUnit list.
   * @apilevel high-level
   */
  public CompilationUnit getCompilationUnit(int i) {
    return (CompilationUnit) getCompilationUnitList().getChild(i);
  }
  /**
   * Check whether the CompilationUnit list has any children.
   * @return {@code true} if it has at least one child, {@code false} otherwise.
   * @apilevel high-level
   */
  public boolean hasCompilationUnit() {
    return getCompilationUnitList().getNumChild() != 0;
  }
  /**
   * Append an element to the CompilationUnit list.
   * @param node The element to append to the CompilationUnit list.
   * @apilevel high-level
   */
  public void addCompilationUnit(CompilationUnit node) {
    List<CompilationUnit> list = (parent == null || state == null) ? getCompilationUnitListNoTransform() : getCompilationUnitList();
    list.addChild(node);
  }
  /**
   * @apilevel low-level
   */
  public void addCompilationUnitNoTransform(CompilationUnit node) {
    List<CompilationUnit> list = getCompilationUnitListNoTransform();
    list.addChild(node);
  }
  /**
   * Replaces the CompilationUnit list element at index {@code i} with the new node {@code node}.
   * @param node The new node to replace the old list element.
   * @param i The list index of the node to be replaced.
   * @apilevel high-level
   */
  public void setCompilationUnit(CompilationUnit node, int i) {
    List<CompilationUnit> list = getCompilationUnitList();
    list.setChild(node, i);
  }
  /**
   * Retrieves the CompilationUnit list.
   * @return The node representing the CompilationUnit list.
   * @apilevel high-level
   */
  @ASTNodeAnnotation.ListChild(name="CompilationUnit")
  public List<CompilationUnit> getCompilationUnitList() {
    List<CompilationUnit> list = (List<CompilationUnit>) getChild(0);
    list.getNumChild();
    return list;
  }
  /**
   * Retrieves the CompilationUnit list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the CompilationUnit list.
   * @apilevel low-level
   */
  public List<CompilationUnit> getCompilationUnitListNoTransform() {
    return (List<CompilationUnit>) getChildNoTransform(0);
  }
  /**
   * Retrieves the CompilationUnit list.
   * @return The node representing the CompilationUnit list.
   * @apilevel high-level
   */
  public List<CompilationUnit> getCompilationUnits() {
    return getCompilationUnitList();
  }
  /**
   * Retrieves the CompilationUnit list.
   * <p><em>This method does not invoke AST transformations.</em></p>
   * @return The node representing the CompilationUnit list.
   * @apilevel low-level
   */
  public List<CompilationUnit> getCompilationUnitsNoTransform() {
    return getCompilationUnitListNoTransform();
  }
  /**
   * @aspect <NoAspect>
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/LambdaBody.jrag:49
   */
    private boolean collect_contributors_BlockLambdaBody_lambdaReturns = false;
  protected void collect_contributors_BlockLambdaBody_lambdaReturns() {
    if(collect_contributors_BlockLambdaBody_lambdaReturns) return;
    super.collect_contributors_BlockLambdaBody_lambdaReturns();
    collect_contributors_BlockLambdaBody_lambdaReturns = true;
  }

  /**
   * @apilevel internal
   */
  protected boolean wildcards_computed = false;
  /**
   * @apilevel internal
   */
  protected WildcardsCompilationUnit wildcards_value;
/**
 * @apilevel internal
 */
private void wildcards_reset() {
  wildcards_computed = false;
  wildcards_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public WildcardsCompilationUnit wildcards() {
    if(wildcards_computed) {
      return wildcards_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    wildcards_value = wildcards_compute();
    wildcards_value.setParent(this);
    wildcards_value.is$Final = true;
    if (true) {
      wildcards_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return wildcards_value;
  }
  /**
   * @apilevel internal
   */
  private WildcardsCompilationUnit wildcards_compute() {
      return new WildcardsCompilationUnit(
        "wildcards",
        new List(),
        new List()
      );
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
    typeObject_value = lookupType("java.lang", "Object");
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
  protected boolean typeCloneable_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeCloneable_value;
/**
 * @apilevel internal
 */
private void typeCloneable_reset() {
  typeCloneable_computed = false;
  typeCloneable_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeCloneable() {
    if(typeCloneable_computed) {
      return typeCloneable_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeCloneable_value = lookupType("java.lang", "Cloneable");
    if (isFinal && num == state().boundariesCrossed) {
      typeCloneable_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeCloneable_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeSerializable_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeSerializable_value;
/**
 * @apilevel internal
 */
private void typeSerializable_reset() {
  typeSerializable_computed = false;
  typeSerializable_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeSerializable() {
    if(typeSerializable_computed) {
      return typeSerializable_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeSerializable_value = lookupType("java.io", "Serializable");
    if (isFinal && num == state().boundariesCrossed) {
      typeSerializable_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeSerializable_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeBoolean_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeBoolean_value;
/**
 * @apilevel internal
 */
private void typeBoolean_reset() {
  typeBoolean_computed = false;
  typeBoolean_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeBoolean() {
    if(typeBoolean_computed) {
      return typeBoolean_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeBoolean_value = lookupType(PRIMITIVE_PACKAGE_NAME, "boolean");
    if (isFinal && num == state().boundariesCrossed) {
      typeBoolean_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeBoolean_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeByte_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeByte_value;
/**
 * @apilevel internal
 */
private void typeByte_reset() {
  typeByte_computed = false;
  typeByte_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeByte() {
    if(typeByte_computed) {
      return typeByte_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeByte_value = lookupType(PRIMITIVE_PACKAGE_NAME , "byte");
    if (isFinal && num == state().boundariesCrossed) {
      typeByte_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeByte_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeShort_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeShort_value;
/**
 * @apilevel internal
 */
private void typeShort_reset() {
  typeShort_computed = false;
  typeShort_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeShort() {
    if(typeShort_computed) {
      return typeShort_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeShort_value = lookupType(PRIMITIVE_PACKAGE_NAME , "short");
    if (isFinal && num == state().boundariesCrossed) {
      typeShort_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeShort_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeChar_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeChar_value;
/**
 * @apilevel internal
 */
private void typeChar_reset() {
  typeChar_computed = false;
  typeChar_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeChar() {
    if(typeChar_computed) {
      return typeChar_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeChar_value = lookupType(PRIMITIVE_PACKAGE_NAME , "char");
    if (isFinal && num == state().boundariesCrossed) {
      typeChar_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeChar_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeInt_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeInt_value;
/**
 * @apilevel internal
 */
private void typeInt_reset() {
  typeInt_computed = false;
  typeInt_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeInt() {
    if(typeInt_computed) {
      return typeInt_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeInt_value = lookupType(PRIMITIVE_PACKAGE_NAME , "int");
    if (isFinal && num == state().boundariesCrossed) {
      typeInt_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeInt_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeLong_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeLong_value;
/**
 * @apilevel internal
 */
private void typeLong_reset() {
  typeLong_computed = false;
  typeLong_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeLong() {
    if(typeLong_computed) {
      return typeLong_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeLong_value = lookupType(PRIMITIVE_PACKAGE_NAME , "long");
    if (isFinal && num == state().boundariesCrossed) {
      typeLong_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeLong_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeFloat_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeFloat_value;
/**
 * @apilevel internal
 */
private void typeFloat_reset() {
  typeFloat_computed = false;
  typeFloat_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeFloat() {
    if(typeFloat_computed) {
      return typeFloat_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeFloat_value = lookupType(PRIMITIVE_PACKAGE_NAME , "float");
    if (isFinal && num == state().boundariesCrossed) {
      typeFloat_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeFloat_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeDouble_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeDouble_value;
/**
 * @apilevel internal
 */
private void typeDouble_reset() {
  typeDouble_computed = false;
  typeDouble_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeDouble() {
    if(typeDouble_computed) {
      return typeDouble_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeDouble_value = lookupType(PRIMITIVE_PACKAGE_NAME , "double");
    if (isFinal && num == state().boundariesCrossed) {
      typeDouble_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeDouble_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeString_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeString_value;
/**
 * @apilevel internal
 */
private void typeString_reset() {
  typeString_computed = false;
  typeString_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeString() {
    if(typeString_computed) {
      return typeString_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeString_value = lookupType("java.lang", "String");
    if (isFinal && num == state().boundariesCrossed) {
      typeString_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeString_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeVoid_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeVoid_value;
/**
 * @apilevel internal
 */
private void typeVoid_reset() {
  typeVoid_computed = false;
  typeVoid_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeVoid() {
    if(typeVoid_computed) {
      return typeVoid_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeVoid_value = lookupType(PRIMITIVE_PACKAGE_NAME, "void");
    if (isFinal && num == state().boundariesCrossed) {
      typeVoid_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeVoid_value;
  }
  /**
   * @apilevel internal
   */
  protected boolean typeNull_computed = false;
  /**
   * @apilevel internal
   */
  protected TypeDecl typeNull_value;
/**
 * @apilevel internal
 */
private void typeNull_reset() {
  typeNull_computed = false;
  typeNull_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl typeNull() {
    if(typeNull_computed) {
      return typeNull_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    typeNull_value = lookupType(PRIMITIVE_PACKAGE_NAME, "null");
    if (isFinal && num == state().boundariesCrossed) {
      typeNull_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return typeNull_value;
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
    unknownType_value = lookupType(PRIMITIVE_PACKAGE_NAME, "Unknown");
    if (isFinal && num == state().boundariesCrossed) {
      unknownType_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return unknownType_value;
  }
  protected java.util.Map hasPackage_String_values;
/**
 * @apilevel internal
 */
private void hasPackage_String_reset() {
  hasPackage_String_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public boolean hasPackage(String packageName) {
    Object _parameters = packageName;
    if (hasPackage_String_values == null) hasPackage_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(hasPackage_String_values.containsKey(_parameters)) {
      return ((Boolean)hasPackage_String_values.get(_parameters)).booleanValue();
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    boolean hasPackage_String_value = hasPackage_compute(packageName);
    if (isFinal && num == state().boundariesCrossed) {
      hasPackage_String_values.put(_parameters, Boolean.valueOf(hasPackage_String_value));
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return hasPackage_String_value;
  }
  /**
   * @apilevel internal
   */
  private boolean hasPackage_compute(String packageName) {
      return isPackage(packageName);
    }
  protected java.util.Map lookupType_String_String_values;
/**
 * @apilevel internal
 */
private void lookupType_String_String_reset() {
  lookupType_String_String_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl lookupType(String packageName, String typeName) {
    java.util.List _parameters = new java.util.ArrayList(2);
    _parameters.add(packageName);
    _parameters.add(typeName);
    if (lookupType_String_String_values == null) lookupType_String_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(lookupType_String_String_values.containsKey(_parameters)) {
      return (TypeDecl)lookupType_String_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    TypeDecl lookupType_String_String_value = lookupType_compute(packageName, typeName);
    if (isFinal && num == state().boundariesCrossed) {
      lookupType_String_String_values.put(_parameters, lookupType_String_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return lookupType_String_String_value;
  }
  /**
   * @apilevel internal
   */
  private TypeDecl lookupType_compute(String packageName, String typeName) {
      String fullName = packageName.equals("") ? typeName : packageName + "." + typeName;
      // Check for type in source
      for(int i = 0; i < getNumCompilationUnit(); i++) {
        for(int j = 0; j < getCompilationUnit(i).getNumTypeDecl(); j++) {
          TypeDecl type = getCompilationUnit(i).getTypeDecl(j);
          if(type.fullName().equals(fullName)) {
            return type;
          }
        }
      }
      // Check for type in library
      return lookupLibType(packageName, typeName);
    }
  protected java.util.Map lookupLibType_String_String_values;
/**
 * @apilevel internal
 */
private void lookupLibType_String_String_reset() {
  lookupLibType_String_String_values = null;
}  
  @ASTNodeAnnotation.Attribute
  public TypeDecl lookupLibType(String packageName, String typeName) {
    java.util.List _parameters = new java.util.ArrayList(2);
    _parameters.add(packageName);
    _parameters.add(typeName);
    if (lookupLibType_String_String_values == null) lookupLibType_String_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(lookupLibType_String_String_values.containsKey(_parameters)) {
      return (TypeDecl)lookupLibType_String_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    TypeDecl lookupLibType_String_String_value = lookupLibType_compute(packageName, typeName);
    if (isFinal && num == state().boundariesCrossed) {
      lookupLibType_String_String_values.put(_parameters, lookupLibType_String_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return lookupLibType_String_String_value;
  }
  /**
   * @apilevel internal
   */
  private TypeDecl lookupLibType_compute(String packageName, String typeName) {
      String fullName = packageName.equals("") ? typeName : packageName + "." + typeName;
      // Check the primitive types
      if (packageName.equals(PRIMITIVE_PACKAGE_NAME)) {
        PrimitiveCompilationUnit unit = getPrimitiveCompilationUnit();
        if (typeName.equals("boolean")) return unit.typeBoolean();
        if (typeName.equals("byte")) return unit.typeByte();
        if (typeName.equals("short")) return unit.typeShort();
        if (typeName.equals("char")) return unit.typeChar();
        if (typeName.equals("int")) return unit.typeInt();
        if (typeName.equals("long")) return unit.typeLong();
        if (typeName.equals("float")) return unit.typeFloat();
        if (typeName.equals("double")) return unit.typeDouble();
        if (typeName.equals("null")) return unit.typeNull();
        if (typeName.equals("void")) return unit.typeVoid();
        if (typeName.equals("Unknown")) return unit.unknownType(); // Is this needed?
      }
      // Check the library:
      //  A type may not be in the library but an NTA cannot map to null.
      //  We need to do some double work to step around this.
      //  We check the classpath directly (the same thing the library NTA does)
      //  to prevent that we call the nta for a name that gives null back
      //else if (getCompilationUnit(fullName) != null) {
  
      // Found a library unit, check it for type
      CompilationUnit libUnit = getLibCompilationUnit(fullName);
      if (libUnit != null) {
        for(int j = 0; j < libUnit.getNumTypeDecl(); j++) {
          TypeDecl type = libUnit.getTypeDecl(j);
          if(type.fullName().equals(fullName)) {
            return type;
          }
        }
      }
      // No type found in the library
      return null;
    }
  /**
   * @apilevel internal
   */
  protected java.util.Map getLibCompilationUnit_String_values;
  /**
   * @apilevel internal
   */
  protected List getLibCompilationUnit_String_list;
/**
 * @apilevel internal
 */
private void getLibCompilationUnit_String_reset() {
  getLibCompilationUnit_String_values = null;
  getLibCompilationUnit_String_list = null;
}  
  @ASTNodeAnnotation.Attribute
  public CompilationUnit getLibCompilationUnit(String fullName) {
    Object _parameters = fullName;
    if (getLibCompilationUnit_String_values == null) getLibCompilationUnit_String_values = new org.jastadd.util.RobustMap(new java.util.HashMap());
    if(getLibCompilationUnit_String_values.containsKey(_parameters)) {
      return (CompilationUnit)getLibCompilationUnit_String_values.get(_parameters);
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    CompilationUnit getLibCompilationUnit_String_value = getLibCompilationUnit_compute(fullName);
    if(getLibCompilationUnit_String_list == null) {
      getLibCompilationUnit_String_list = new List();
      getLibCompilationUnit_String_list.is$Final = true;
      getLibCompilationUnit_String_list.setParent(this);
    }
    getLibCompilationUnit_String_list.add(getLibCompilationUnit_String_value);
    if(getLibCompilationUnit_String_value != null) {
      getLibCompilationUnit_String_value = (CompilationUnit) getLibCompilationUnit_String_list.getChild(getLibCompilationUnit_String_list.numChildren-1);
      getLibCompilationUnit_String_value.is$Final = true;
    }
    if (true) {
      getLibCompilationUnit_String_values.put(_parameters, getLibCompilationUnit_String_value);
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return getLibCompilationUnit_String_value;
  }
  /**
   * @apilevel internal
   */
  private CompilationUnit getLibCompilationUnit_compute(String fullName) {
      return getCompilationUnit(fullName);
    }
  /**
   * @apilevel internal
   */
  protected boolean getPrimitiveCompilationUnit_computed = false;
  /**
   * @apilevel internal
   */
  protected PrimitiveCompilationUnit getPrimitiveCompilationUnit_value;
/**
 * @apilevel internal
 */
private void getPrimitiveCompilationUnit_reset() {
  getPrimitiveCompilationUnit_computed = false;
  getPrimitiveCompilationUnit_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public PrimitiveCompilationUnit getPrimitiveCompilationUnit() {
    if(getPrimitiveCompilationUnit_computed) {
      return getPrimitiveCompilationUnit_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    getPrimitiveCompilationUnit_value = getPrimitiveCompilationUnit_compute();
    getPrimitiveCompilationUnit_value.setParent(this);
    getPrimitiveCompilationUnit_value.is$Final = true;
    if (true) {
      getPrimitiveCompilationUnit_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return getPrimitiveCompilationUnit_value;
  }
  /**
   * @apilevel internal
   */
  private PrimitiveCompilationUnit getPrimitiveCompilationUnit_compute() {
      PrimitiveCompilationUnit u = new PrimitiveCompilationUnit();
      u.setPackageDecl(PRIMITIVE_PACKAGE_NAME);
      return u;
    }
  /**
   * @apilevel internal
   */
  protected boolean unknownConstructor_computed = false;
  /**
   * @apilevel internal
   */
  protected ConstructorDecl unknownConstructor_value;
/**
 * @apilevel internal
 */
private void unknownConstructor_reset() {
  unknownConstructor_computed = false;
  unknownConstructor_value = null;
}  
  @ASTNodeAnnotation.Attribute
  public ConstructorDecl unknownConstructor() {
    if(unknownConstructor_computed) {
      return unknownConstructor_value;
    }
    ASTNode$State state = state();
    boolean intermediate = state.INTERMEDIATE_VALUE;
    state.INTERMEDIATE_VALUE = false;
    int num = state.boundariesCrossed;
    boolean isFinal = this.is$Final();
    unknownConstructor_value = unknownConstructor_compute();
    if (isFinal && num == state().boundariesCrossed) {
      unknownConstructor_computed = true;
    } else {
    }
    state.INTERMEDIATE_VALUE |= intermediate;

    return unknownConstructor_value;
  }
  /**
   * @apilevel internal
   */
  private ConstructorDecl unknownConstructor_compute() {
      return (ConstructorDecl)unknownType().constructors().iterator().next();
    }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PrettyPrint.jadd:398
   * @apilevel internal
   */
  public String Define_String_typeDeclIndent(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return "";
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:281
   * @apilevel internal
   */
  public boolean Define_boolean_inExtendsOrImplements(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:1177
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeWildcard(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return wildcards().typeWildcard();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:1188
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_lookupWildcardExtends(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return wildcards().lookupWildcardExtends(typeDecl);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:1201
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_lookupWildcardSuper(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return wildcards().lookupWildcardSuper(typeDecl);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:1223
   * @apilevel internal
   */
  public LUBType Define_LUBType_lookupLUBType(ASTNode caller, ASTNode child, Collection bounds) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return wildcards().lookupLUBType(bounds);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:1263
   * @apilevel internal
   */
  public GLBType Define_GLBType_lookupGLBType(ASTNode caller, ASTNode child, ArrayList bounds) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return wildcards().lookupGLBType(bounds);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericsParTypeDecl.jrag:47
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_genericDecl(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return null;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/AnonymousClasses.jrag:14
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_superType(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return null;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/AnonymousClasses.jrag:27
   * @apilevel internal
   */
  public ConstructorDecl Define_ConstructorDecl_constructorDecl(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return unknownConstructor();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Arrays.jrag:19
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_componentType(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return unknownType();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BranchTarget.jrag:233
   * @apilevel internal
   */
  public LabeledStmt Define_LabeledStmt_lookupLabel(ASTNode caller, ASTNode child, String name) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return null;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DeclareBeforeUse.jrag:14
   * @apilevel internal
   */
  public int Define_int_blockIndex(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return -1;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:16
   * @apilevel internal
   */
  public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:26
   * @apilevel internal
   */
  public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return true;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:50
   * @apilevel internal
   */
  public boolean Define_boolean_isIncOrDec(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:339
   * @apilevel internal
   */
  public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return true;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/DefiniteAssignment.jrag:732
   * @apilevel internal
   */
  public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return true;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ExceptionHandling.jrag:19
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeException(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return lookupType("java.lang", "Exception");
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ExceptionHandling.jrag:21
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeRuntimeException(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return lookupType("java.lang", "RuntimeException");
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ExceptionHandling.jrag:23
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeError(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return lookupType("java.lang", "Error");
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ExceptionHandling.jrag:25
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeNullPointerException(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return lookupType("java.lang", "NullPointerException");
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ExceptionHandling.jrag:27
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeThrowable(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return lookupType("java.lang", "Throwable");
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ExceptionHandling.jrag:138
   * @apilevel internal
   */
  public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
     {
      int childIndex = this.getIndexOfChild(caller);
{
    throw new Error("Operation handlesException not supported");
  }
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupConstructor.jrag:15
   * @apilevel internal
   */
  public Collection Define_Collection_lookupConstructor(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return Collections.EMPTY_LIST;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupConstructor.jrag:24
   * @apilevel internal
   */
  public Collection Define_Collection_lookupSuperConstructor(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return Collections.EMPTY_LIST;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupMethod.jrag:41
   * @apilevel internal
   */
  public Expr Define_Expr_nestedScope(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
{ throw new UnsupportedOperationException(); }
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupMethod.jrag:53
   * @apilevel internal
   */
  public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return Collections.EMPTY_LIST;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:18
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeObject(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return typeObject();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:19
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeCloneable(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return typeCloneable();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:20
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeSerializable(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return typeSerializable();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:31
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeBoolean(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return typeBoolean();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:32
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeByte(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return typeByte();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:33
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeShort(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return typeShort();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:34
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeChar(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return typeChar();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:35
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeInt(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return typeInt();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:36
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeLong(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return typeLong();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:37
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeFloat(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return typeFloat();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:38
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeDouble(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return typeDouble();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:39
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeString(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return typeString();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:42
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeVoid(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return typeVoid();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:44
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_typeNull(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return typeNull();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:47
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_unknownType(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return unknownType();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:84
   * @apilevel internal
   */
  public boolean Define_boolean_hasPackage(ASTNode caller, ASTNode child, String packageName) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return hasPackage(packageName);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:109
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_lookupType(ASTNode caller, ASTNode child, String packageName, String typeName) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return lookupType(packageName, typeName);
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupType.jrag:232
   * @apilevel internal
   */
  public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return SimpleSet.emptySet;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/LookupVariable.jrag:24
   * @apilevel internal
   */
  public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return SimpleSet.emptySet;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:290
   * @apilevel internal
   */
  public boolean Define_boolean_mayBePublic(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:291
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeProtected(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:292
   * @apilevel internal
   */
  public boolean Define_boolean_mayBePrivate(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:293
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeStatic(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:294
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:295
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeAbstract(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:296
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeVolatile(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:297
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeTransient(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:298
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeStrictfp(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:299
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeSynchronized(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Modifiers.jrag:300
   * @apilevel internal
   */
  public boolean Define_boolean_mayBeNative(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:275
   * @apilevel internal
   */
  public ASTNode Define_ASTNode_enclosingBlock(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return null;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:326
   * @apilevel internal
   */
  public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
{
    throw new UnsupportedOperationException("outerScope() not defined");
  }
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:395
   * @apilevel internal
   */
  public boolean Define_boolean_insideLoop(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:402
   * @apilevel internal
   */
  public boolean Define_boolean_insideSwitch(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/NameCheck.jrag:454
   * @apilevel internal
   */
  public Case Define_Case_bind(ASTNode caller, ASTNode child, Case c) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return null;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/SyntacticClassification.jrag:64
   * @apilevel internal
   */
  public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return NameType.NO_NAME;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:219
   * @apilevel internal
   */
  public boolean Define_boolean_isAnonymous(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:233
   * @apilevel internal
   */
  public Variable Define_Variable_unknownField(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return unknownType().findSingleVariable("unknown");
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:237
   * @apilevel internal
   */
  public MethodDecl Define_MethodDecl_unknownMethod(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
{
    for(Iterator iter = unknownType().memberMethods("unknown").iterator(); iter.hasNext(); ) {
      MethodDecl m = (MethodDecl)iter.next();
      return m;
    }
    throw new Error("Could not find method unknown in type Unknown");
  }
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:244
   * @apilevel internal
   */
  public ConstructorDecl Define_ConstructorDecl_unknownConstructor(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return unknownConstructor();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:258
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_declType(ASTNode caller, ASTNode child) {
     {
      int i = this.getIndexOfChild(caller);
      return null;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:524
   * @apilevel internal
   */
  public BodyDecl Define_BodyDecl_enclosingBodyDecl(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return null;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:542
   * @apilevel internal
   */
  public boolean Define_boolean_isMemberType(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeAnalysis.jrag:590
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_hostType(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return null;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeCheck.jrag:363
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_switchType(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return unknownType();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeCheck.jrag:409
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_returnType(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return typeVoid();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeCheck.jrag:510
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_enclosingInstance(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return null;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeHierarchyCheck.jrag:14
   * @apilevel internal
   */
  public String Define_String_methodHost(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
{
    throw new Error("Needs extra equation for methodHost()");
  }
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeHierarchyCheck.jrag:147
   * @apilevel internal
   */
  public boolean Define_boolean_inExplicitConstructorInvocation(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeHierarchyCheck.jrag:156
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_enclosingExplicitConstructorHostType(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return null;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/TypeHierarchyCheck.jrag:164
   * @apilevel internal
   */
  public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/UnreachableStatements.jrag:163
   * @apilevel internal
   */
  public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return true;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/VariableDeclaration.jrag:68
   * @apilevel internal
   */
  public boolean Define_boolean_isMethodParameter(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/VariableDeclaration.jrag:69
   * @apilevel internal
   */
  public boolean Define_boolean_isConstructorParameter(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/VariableDeclaration.jrag:70
   * @apilevel internal
   */
  public boolean Define_boolean_isExceptionHandlerParameter(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:94
   * @apilevel internal
   */
  public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:188
   * @apilevel internal
   */
  public ElementValue Define_ElementValue_lookupElementTypeValue(ASTNode caller, ASTNode child, String name) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return null;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:278
   * @apilevel internal
   */
  public boolean Define_boolean_withinSuppressWarnings(ASTNode caller, ASTNode child, String annot) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:380
   * @apilevel internal
   */
  public boolean Define_boolean_withinDeprecatedAnnotation(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:440
   * @apilevel internal
   */
  public Annotation Define_Annotation_lookupAnnotation(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
     {
      int i = this.getIndexOfChild(caller);
      return null;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Annotations.jrag:473
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_enclosingAnnotationDecl(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return unknownType();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/GenericMethodsInference.jrag:39
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_assignConvertedType(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return typeNull();
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/VariableArityParameters.jrag:24
   * @apilevel internal
   */
  public boolean Define_boolean_variableArityValid(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Diamond.jrag:74
   * @apilevel internal
   */
  public ClassInstanceExpr Define_ClassInstanceExpr_getClassInstanceExpr(ASTNode caller, ASTNode child) {
     {
      int i = this.getIndexOfChild(caller);
      return null;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Diamond.jrag:466
   * @apilevel internal
   */
  public boolean Define_boolean_isAnonymousDecl(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/Diamond.jrag:483
   * @apilevel internal
   */
  public boolean Define_boolean_isExplicitGenericConstructorAccess(ASTNode caller, ASTNode child) {
     {
      int i = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/PreciseRethrow.jrag:179
   * @apilevel internal
   */
  public boolean Define_boolean_isCatchParam(ASTNode caller, ASTNode child) {
     {
      int i = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/PreciseRethrow.jrag:187
   * @apilevel internal
   */
  public CatchClause Define_CatchClause_catchClause(ASTNode caller, ASTNode child) {
     {
      int i = this.getIndexOfChild(caller);
{
    throw new IllegalStateException("Could not find parent " +
        "catch clause");
  }
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java7/frontend/TryWithResources.jrag:154
   * @apilevel internal
   */
  public boolean Define_boolean_resourcePreviouslyDeclared(ASTNode caller, ASTNode child, String name) {
     {
      int i = this.getIndexOfChild(caller);
      return false;
    }
  }
  /**
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/TargetType.jrag:32
   * @apilevel internal
   */
  public TypeDecl Define_TypeDecl_targetType(ASTNode caller, ASTNode child) {
     {
      int childIndex = this.getIndexOfChild(caller);
      return typeNull();
    }
  }
  /**
   * @apilevel internal
   */
  public ASTNode rewriteTo() {    return super.rewriteTo();
  }}
