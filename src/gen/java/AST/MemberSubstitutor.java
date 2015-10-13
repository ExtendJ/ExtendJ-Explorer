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
 * @aspect LookupParTypeDecl
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:693
 */
 interface MemberSubstitutor extends Parameterization {

     
    TypeDecl original();

    
   void addBodyDecl(BodyDecl b);

     
    TypeDecl substitute(TypeVariable typeVariable);
  /**
   * @attribute syn
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:951
   */
  @ASTNodeAnnotation.Attribute
  public HashMap localMethodsSignatureMap();
  /**
   * @attribute syn
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:970
   */
  @ASTNodeAnnotation.Attribute
  public SimpleSet localFields(String name);
  /**
   * @attribute syn
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:990
   */
  @ASTNodeAnnotation.Attribute
  public SimpleSet localTypeDecls(String name);
  /**
   * @attribute syn
   * @aspect LookupParTypeDecl
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:1018
   */
  @ASTNodeAnnotation.Attribute
  public Collection constructors();
}
