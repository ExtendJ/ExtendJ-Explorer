package jastaddad;

import jastaddad.objectinfo.NodeContent;
import jastaddad.objectinfo.NodeInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class Node{
    public final Object node;
    public final int id;
    public final String name;
    public final String className;
    public final String fullName;
    public final ArrayList<Node> children;
    private boolean isList;
    private boolean isOpt;
    private int level;
    private NodeContent nodeContent;

    public Node(Object root, HashSet<Object> futureReferences){
        this.children = new ArrayList<>();
        this.name = "";
        this.className = root.getClass().getSimpleName();
        this.node = root;
        fullName = className;
        id = System.identityHashCode(this.toString());
        init(root, false, false, 1, futureReferences);
    }

    public Node(Object root, String name, boolean isList, boolean isOpt, int level, HashSet<Object> futureReferences){
        this.children = new ArrayList<>();
        this.className = root.getClass().getSimpleName();
        this.node = root;
        if(name == className || name.length() == 0){
            this.name = "";
            fullName = className;
        }else {
            this.name = name;
            fullName = className + ":" + name;
        }
        id = System.identityHashCode(this.toString());
        init(root, isList, isOpt, level, futureReferences);
    }

    private void init(Object root, boolean isList, boolean isOpt, int level, HashSet<Object> futureReferences){
        this.isOpt = isOpt;
        this.isList = isList;
        this.nodeContent = new NodeContent();
        this.level = level;
        futureReferences.add(node);
        if(isList) {
            for (Object child: (Iterable<?>) root) {
                children.add(new Node(child, isOpt ? name : "", child instanceof Collection, false, 1, futureReferences));
            }
        }
        traversDown(root, futureReferences);
    }

    public boolean containsAttributeOrToken(String key){
        return getNodeContent().containsAttribute(key) || getNodeContent().containsToken(key);
    }

    public NodeInfo getAttributeOrTokenValue(String key){
        return getNodeContent().get(key);
    }

    private void traversDown(Object root, HashSet<Object> futureReferences){
        try {
            for (Method m : root.getClass().getMethods()) {
                for (Annotation a: m.getAnnotations()) {
                    if(ASTAnnotation.isChild(a)) {
                        children.add(new Node(m.invoke(root, new Object[m.getParameterCount()]),
                                getName(a),
                                !ASTAnnotation.isSingleChild(a),
                                ASTAnnotation.isOptChild(a),
                                level + 1,
                                futureReferences));
                    }
                    //nodeContent.add(root, m, a);
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
        return className;
    }

    public int getLevel(){ return level;}

    public NodeContent getNodeContent(){ return nodeContent;}
}
