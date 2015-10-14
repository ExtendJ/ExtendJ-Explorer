package jastaddad;


import jastaddadui.JastAddAdUi;

import java.util.ArrayList;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;

public class JastAddAd{

  public JastAddAd(Object root){
    Node tree = new Node(root, false, 1); //Unknown if root is a list, TODO check this shit out
    //System.out.println("asdasd");
    JastAddAdUi ui = new JastAddAdUi(tree);
    //ui.launch(new String[0]);
  }
}
