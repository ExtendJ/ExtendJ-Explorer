package jastaddad;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.lang.annotation.Annotation;

public class Node{
  public final String name;
  public final ArrayList<Node> children;

  public Node(Object root, boolean isList){
    children = new ArrayList<Node>();
    name = root.getClass().getName();
    if(isList) {
      for (Object child: (Iterable<?>) root) {
          traversDown(child);
      }
    } else {
      traversDown(root);
    }
  }

  private void traversDown(Object root){
    System.out.println("trav - " + root);
    try {
      for (Method m : root.getClass().getMethods()) {
        for (Annotation a: m.getAnnotations()) {
          if(ASTAnnotation.isListChild(a)
              || ASTAnnotation.isOptChild(a)) {
              System.out.println(m.getName());
            children.add(new Node(m.invoke(root, new Object[]{}), true));
          } else if(ASTAnnotation.isChild(a)){
            System.out.println(m.getName());
            children.add(new Node(m.invoke(root, new Object[]{}), false));
          }
            /*if (a.getClass().getMethod("name") != null) {
              name = (String) a.getClass().getMethod("name").invoke(a, new Object[]{});
            }*/
        }
      }
		} catch (Throwable e) {
			e.printStackTrace();
		}
  }

  public String toString() {
	  return name;
  }
}
