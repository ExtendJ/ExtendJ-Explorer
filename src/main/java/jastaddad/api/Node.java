package jastaddad.api;

import jastaddad.api.objectinfo.NodeContent;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

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

    public Node(Object root, ASTAPI api){
        this.children = new ArrayList<>();
        this.name = "";
        this.className = root.getClass().getSimpleName();
        this.node = root;
        fullName = className;
        id = System.identityHashCode(this.toString());
        init(root, false, false, 1, api);
    }

    public Node(Object root, String name, boolean isList, boolean isOpt, int level, ASTAPI api){
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
        init(root, isList, isOpt, level, api);
    }

    private void init(Object root, boolean isList, boolean isOpt, int level, ASTAPI api){
        this.isOpt = isOpt;
        this.isList = isList;
        this.nodeContent = new NodeContent(this);
        this.level = level;
        api.addObjectReference(node);
        if(isList) {
            for (Object child: (Iterable<?>) root) {
                if(child instanceof Collection && child.getClass().getSimpleName().equals("List") && isOpt)
                    api.putWarning(ASTAPI.AST_STRUCTURE_WARNING, "A List is a direct child to a Opt parent, parent : " + root + ", -> child : " + child);
                children.add(new Node(child, isOpt ? name : "", child instanceof Collection, false, 1, api));
            }
        }
        traversDown(root, api);
    }

    private void traversDown(Object root, ASTAPI api) {
        try {
            for (Method m : root.getClass().getMethods()) {
                for (Annotation a: m.getAnnotations()) {
                    if(ASTAnnotation.isChild(a)) {
                        Object obj = m.invoke(root, new Object[m.getParameterCount()]);
                        if(obj != null) {
                            children.add(new Node(obj, getName(a),
                                    !ASTAnnotation.isSingleChild(a),
                                    ASTAnnotation.isOptChild(a),
                                    level + 1, api));
                        }else{
                            api.putError(ASTAPI.AST_STRUCTURE_ERROR, String.format("The child %s is null, can't continue the traversal of this path", getName(a)));
                        }
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
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
