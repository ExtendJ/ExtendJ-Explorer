package jastaddad;


import uicomponent.UIComponent;

import java.util.ArrayList;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;

public class JastAddAd{

  public JastAddAd(Object root){
    Node tree = new Node(root, false, 1); //Unknown if root is a list, TODO check this shit out
    //System.out.println("asdasd");
    UIComponent ui = new UIComponent(tree);
    //javafx.application.Application.launch(JastAddAdUi.class);
  }
}
