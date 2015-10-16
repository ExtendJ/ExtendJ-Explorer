package jastaddad;

import AST.ASTNodeAnnotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.lang.annotation.Annotation;

public class Node{

    public final String name;
    public final ArrayList<Node> children;
    private int level;
    private Attributes attributes;

    public Node(Object root, boolean isList, int level){
        this.children = new ArrayList<Node>();
        this.name = root.getClass().getName();
        init(root, isList, level);
    }

    public Node(Object root, String name, boolean isList, int level){
        this.children = new ArrayList<Node>();
        this.name = name;
        init(root, isList, level);
    }

    private void init(Object root, boolean isList, int level){
        this.attributes = new Attributes();
        this.level = level;
        if(isList) {
            for (Object child: (Iterable<?>) root) {
                traversDown(child);
            }
        } else {
            traversDown(root);
        }
    }

    private void traversDown(Object root){
        System.out.println("Node : " + root);
        try {
            for (Method m : root.getClass().getMethods()) {
                for (Annotation a: m.getAnnotations()) {
                    if(ASTAnnotation.isChild(a)) {
                        children.add(new Node(m.invoke(root, new Object[]{}), getName(a), !ASTAnnotation.isSingleChild(a), level+1));
                    }
                    attributes.add(root, m, a);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private String getName(Annotation a) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (a.getClass().getMethod("name") != null)
            return (String) a.getClass().getMethod("name").invoke(a, new Object[]{});
        return null + "";

    }

    public String toString() {
      return name;
    }

    public int getLevel(){ return level;}

    public Attributes getAttributes(){ return attributes;}
}
