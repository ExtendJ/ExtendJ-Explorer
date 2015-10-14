package jastaddad;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.lang.annotation.Annotation;

public class Node{
    public final String name;
    public final ArrayList<Node> children;
    private int level;

    public Node(Object root, boolean isList, int level){
        this.level = level;
        children = new ArrayList<Node>();
        name = root.getClass().getName();
        System.out.println(name);
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
                        //System.out.println(m.getName());
                        children.add(new Node(m.invoke(root, new Object[]{}), true, level+1));
                    } else if(ASTAnnotation.isChild(a)){
                        //System.out.println(m.getName());
                        children.add(new Node(m.invoke(root, new Object[]{}), false, level+1));
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

  public int getLevel(){ return level;}
}
