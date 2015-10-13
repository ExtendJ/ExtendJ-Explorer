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
 * @aspect Variable
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/Variable.jadd:29
 */
 interface Variable {

		 
		public String name();

		 
		public TypeDecl type();

		 
		public Collection<TypeDecl> throwTypes();

		 
		public boolean isParameter();

		// 4.5.3
		 
		// 4.5.3
		public boolean isClassVariable();

		 
		public boolean isInstanceVariable();

		 
		public boolean isMethodParameter();

		 
		public boolean isConstructorParameter();

		 
		public boolean isExceptionHandlerParameter();

		 
		public boolean isLocalVariable();

		// 4.5.4
		 
		// 4.5.4
		public boolean isFinal();

		 
		public boolean isVolatile();

		
		// 4.12.4
		 
		
		// 4.12.4
		public boolean isEffectivelyFinal();


		 

		public boolean isBlank();

		 
		public boolean isStatic();

		 
		public boolean isSynthetic();


		 

		public TypeDecl hostType();


		 

		public Expr getInit();

		 
		public boolean hasInit();


		 

		public Constant constant();


		 

		public Modifiers getModifiers();
  /**
   * @attribute syn
   * @aspect SourceDeclarations
   * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/Generics.jrag:1314
   */
  @ASTNodeAnnotation.Attribute
  public Variable sourceVariableDecl();
}
