package jastaddad;


import uicomponent.UIComponent;

import java.util.ArrayList;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;

public class JastAddAd{

  public JastAddAd(Object root){
    Node tree = new Node(root, false, 1); //Unknown if root is a list, TODO check this shit out
    ASTAPI api = new ASTAPI(tree);
    //System.out.println("asdasd");
    UIComponent ui = new UIComponent(api);
    //javafx.application.Application.launch(JastAddAdUi.class);
  }
  
  public static void main(String[] args) {
        System.out.println("JastAddAd is running, this is a test message");
        JastAddAd ui = new JastAddAd(new Object());
  }
}
