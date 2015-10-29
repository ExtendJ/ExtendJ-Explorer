package jastaddad;

import AST.List;
import jastaddad.objectinfo.NodeContent;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

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

    public Node(HashMap<Object, Node> nodes, Object root){
        this.children = new ArrayList<>();
        this.name = "";
        this.className = root.getClass().getSimpleName();
        fullName = className;
        id = System.identityHashCode(this.toString());
        init(nodes, root, false, false, 1);
    }

    public Node(HashMap<Object, Node> nodes, Object root, String name, boolean isList, boolean isOpt, int level){
        this.children = new ArrayList<>();
        this.className = root.getClass().getSimpleName();

        if(name == className || name.length() == 0){
            this.name = "";
            fullName = className;
        }else {
            this.name = name;
            fullName = className + ":" + name;
        }
        id = System.identityHashCode(this.toString());
        init(nodes, root, isList, isOpt, level);
    }

    private void init(HashMap<Object, Node> nodes, Object root, boolean isList, boolean isOpt, int level){
        node = root;
        this.isOpt = isOpt;
        this.isList = isList;
        this.nodeContent = new NodeContent();
        this.level = level;

        nodes.put(root, this);
        if(isList) {
            for (Object child: (Iterable<?>) root) {
                children.add(new Node(nodes, child, isOpt ? name : "", child instanceof List, false, 1));
                traversDown(nodes, root);
            }
        } else {
            traversDown(nodes, root);
        }
    }

    public boolean containsAttributeOrToken(String key){
        return getNodeContent().containsAttribute(key) || getNodeContent().containsToken(key);
    }

    public boolean getAttributeOrTokenValue(String key){
        return getNodeContent().containsAttribute(key) || getNodeContent().containsToken(key);
    }

    private void traversDown(HashMap<Object, Node> nodes, Object root){
        try {
            for (Method m : root.getClass().getMethods()) {
                for (Annotation a: m.getAnnotations()) {
                    if(ASTAnnotation.isChild(a)) {
                        children.add(new Node(nodes,m.invoke(root, new Object[m.getParameterCount()]),
                                getName(a),
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

    private String getName(Annotation a) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
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
