package jastaddad;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.lang.annotation.Annotation;
import java.util.Objects;

public class Node{
    private Object node;
    public final int id;
    public final String name;
    public final String className;
    public final ArrayList<Node> children;
    private boolean isList;
    private int level;
    private Attributes attributes;

    public Node(Object root, boolean isList, int level){
        this.children = new ArrayList<>();
        this.name = root.getClass().getName();
        this.className = name;
        id = System.identityHashCode(this.toString());
        init(root, isList, level);

    }

    public Node(Object root, String name, boolean isList, int level){
        this.children = new ArrayList<Node>();
        this.name = name;
        this.className = root.getClass().getName();
        id = System.identityHashCode(this.toString());
        init(root, isList, level);
    }

    public boolean isList(){ return isList; }

    private void init(Object root, boolean isList, int level){
        node = root;
        this.isList = isList;
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
        //System.out.println("Node : " + root);
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
      return "<html>" + name + "<br>" + className + "</html>";
    }

    public int getLevel(){ return level;}

    public Attributes getAttributes(){ return attributes;}
}
