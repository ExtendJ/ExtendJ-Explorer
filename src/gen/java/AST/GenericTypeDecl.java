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
 * @ast interface
 * @aspect Generics
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:177
 */
 interface GenericTypeDecl {

     
    TypeDecl original();

     
    int getNumTypeParameter();

     
    TypeVariable getTypeParameter(int index);

     
    List getTypeParameterList();

     
    public String fullName();

     
    public String typeName();
public TypeDecl makeGeneric(Signatures.ClassSignature s);

public SimpleSet addTypeVariables(SimpleSet c, String name);

public List createArgumentList(ArrayList params);

  /**
   * @attribute syn
   * @aspect Generics
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:178
   */
  @ASTNodeAnnotation.Attribute
  public boolean isGenericType();
  /**
   * @attribute syn
   * @aspect Generics
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:183
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl rawType();
  /**
   * @attribute syn
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:635
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl lookupParTypeDecl(ParTypeAccess p);
  /**
   * @attribute syn
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:642
   */
  @ASTNodeAnnotation.Attribute
  public TypeDecl lookupParTypeDecl(ArrayList list);
}
