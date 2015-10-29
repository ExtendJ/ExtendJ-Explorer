package jastaddad;


import uicomponent.UIComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import AST.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.System;

public class JastAddAd{

  public JastAddAd(Object root){
    HashMap<Object, Node> nodes = new HashMap();
    Node tree = new Node(nodes, root, false, false, 1);
    ASTAPI api = new ASTAPI(tree);
    //System.out.println("asdasd");
    UIComponent ui = new UIComponent(api);
    //javafx.application.Application.launch(JastAddAdUi.class);
  }
  
  public static void main(String[] args) {
	  try{
		String filename = "testInput.cfg";
		ConfigScanner scanner = new ConfigScanner(new FileReader(filename));
		ConfigParser parser = new ConfigParser();
		DebuggerConfig program = (DebuggerConfig) parser.parse(scanner);
		if (!program.errors().isEmpty()) {
			  System.err.println();
			  System.err.println("Errors: ");
			  for (ErrorMessage e: program.errors()) {
				  System.err.println("- " + e);
			  }
		} else {
			  JastAddAd debugger = new JastAddAd(program);
			  //program.genCode(System.out);
		}

    } catch (FileNotFoundException e) {
	  System.out.println("File not found!");
	  System.exit(1);
    } catch (IOException e) {
	  e.printStackTrace(System.err);
    } catch (Exception e) {
	  e.printStackTrace();
    }
  }
}
