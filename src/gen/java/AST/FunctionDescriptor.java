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
 * @ast class
 * @aspect FunctionDescriptor
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java8/frontend/FunctionDescriptor.jrag:29
 */
 class FunctionDescriptor extends java.lang.Object {

		ArrayList<TypeDecl> throwsList = null;


		MethodDecl method = null;


		InterfaceDecl fromInterface = null;


		
		public FunctionDescriptor(InterfaceDecl fromInterface) {
			this.fromInterface = fromInterface;
		}


		
		public boolean isGeneric() {
			if(method == null)
				return false;
			else
				return method instanceof GenericMethodDecl;
		}


		
		public InterfaceDecl fromInterface() {
			return this.fromInterface;
		}


		
		public String toString() {
			StringBuilder str = new StringBuilder();
			if(method != null) {
				if(method instanceof GenericMethodDecl) {
					GenericMethodDecl genericMethod = (GenericMethodDecl)method;
					str.append("<" + genericMethod.getTypeParameter(0).prettyPrint());
					for(int i = 1; i < genericMethod.getNumTypeParameter(); i++) {
						str.append(", " + genericMethod.getTypeParameter(i).prettyPrint());
					}
					str.append("> ");
				}
				str.append("(");
				if(method.getNumParameter() > 0) {
					str.append(method.getParameter(0).type().typeName());
					for(int i = 1; i < method.getNumParameter(); i++) {
						str.append(", " + method.getParameter(i).type().typeName());
					}
				}
				str.append(")->");
				str.append(method.type().typeName());
				
				str.append(" throws ");
				if(throwsList.size() > 0) {
					str.append(throwsList.get(0).typeName());
					for(int i = 1; i < throwsList.size(); i++) {
						str.append(", " + throwsList.get(i).typeName());
					}
				}
			}
			
			return str.toString();
		}


}
