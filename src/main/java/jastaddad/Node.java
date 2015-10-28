package jastaddad;

import AST.List;
import jastaddad.objectinfo.NodeContent;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Node{
    private Object node;
    public final int id;
    public final String name;
    public final String className;
    public final String fullName;
    public final ArrayList<Node> children;
    private boolean isList;
    private boolean isOpt;
    private int level;
    private NodeContent nodeContent;

    public Node(Object root, boolean isList, boolean isOpt, int level){
        this.children = new ArrayList<>();
        this.name = "";
        this.className = root.getClass().getName();
        fullName = className;
        id = System.identityHashCode(this.toString());
        init(root, isList, isOpt, level);

    }

    public Node(Object root, String name, boolean isList, boolean isOpt, int level){
        this.children = new ArrayList<>();
        this.className = root.getClass().getName();

        if(name == className){
            this.name = "";
            fullName = className;
        }else {
            this.name = name;
            fullName = className + ":" + name;
        }
        id = System.identityHashCode(this.toString());
        //System.out.println(name + " : " + isList + " : " + className);
        init(root, isList, isOpt, level);
    }

    private void init(Object root, boolean isList, boolean isOpt, int level){
        //System.out.println("Node " + root.getClass().getName());
        node = root;
        this.isOpt = isOpt;
        this.isList = isList;
        this.nodeContent = new NodeContent();
        this.level = level;

        if(isList) {
            for (Object child: (Iterable<?>) root) {
                System.out.println(child.getClass());
                // TODO: Find a better solution for Lists with List children
                children.add(new Node(child, isOpt ? name : "", child instanceof List, false, 1));
                traversDown(root, isList);
            }
        } else {
            traversDown(root, isList);
        }
    }

    private void traversDown(Object root, boolean isList){
        //System.out.println("Node : " + root);
        try {
            for (Method m : root.getClass().getMethods()) {
                for (Annotation a: m.getAnnotations()) {
                    if(ASTAnnotation.isChild(a)) {
                        System.out.println(m.getName());
                        children.add(new Node(m.invoke(root, new Object[m.getParameterCount()]),
                                getName(a, root),
                                !ASTAnnotation.isSingleChild(a),
                                ASTAnnotation.isOptChild(a),
                                level + 1));
                    }
                    nodeContent.add(root, m, a);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private String getName(Annotation a, Object node) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (a.getClass().getMethod("name") != null)
            return (String) a.getClass().getMethod("name").invoke(a, new Object[]{});
        return "";
    }

    public String nodeName() { return node.toString(); }
    public boolean isOpt(){return isOpt;}
    public boolean isList(){ return isList; }
    public String toString() {
        return "<html>" + className + "</html>";
    }

    public int getLevel(){ return level;}

    public NodeContent getNodeContent(){ return nodeContent;}
}
