package jastaddad.api;


import jastaddad.api.nodeinfo.NodeContent;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This class represents the Node in the AST, it holds all its terminal attributes and references to its children.
 * This class should only be created once, due to that it will traverse the tree from the given Object.
 */
public class Node{
    public final Object node;
    public final int id;
    public final String name;
    public final String className;
    public final String fullName;
    public final ArrayList<Node> children;
    private boolean isList;
    private boolean isOpt;
    private boolean isNull;
    private int level;
    private NodeContent nodeContent;

    /**
     * This THE a constructor for the root node of the AST
     * @param root
     * @param api used for contributing errors and warnings, during the traversal of the AST.
     */
    public Node(Object root, ASTAPI api){
        this.children = new ArrayList<>();
        this.name = "";
        isNull = root == null;
        nullCheck(root, api, "ROOT");
        if(!isNull)
            this.className = root.getClass().getSimpleName();
        else
            this.className = "Null";
        this.node = root;
        fullName = className;
        id = System.identityHashCode(this.toString());
        init(root, false, false, 1, api);
    }

    /**
     * This is the constructor used during the traversal of the AST
     * @param root
     * @param name
     * @param isList
     * @param isOpt
     * @param level
     * @param api
     */
    public Node(Object root, String name, boolean isList, boolean isOpt, int level, ASTAPI api){
        this.children = new ArrayList<>();
        this.isNull = root == null;
        if(!isNull)
            this.className = root.getClass().getSimpleName();
        else
            this.className = "Null";
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

    /**
     * Continues the traversal.
     * If the isList is true, this method will iterate over all the children in the "List"
     * @param root
     * @param isList
     * @param isOpt
     * @param level
     * @param api
     */
    private void init(Object root, boolean isList, boolean isOpt, int level, ASTAPI api){
        this.isOpt = isOpt;
        this.isList = isList;
        this.nodeContent = new NodeContent(this);
        this.level = level;
        if(!isNull) {
            api.addObjectReference(node);
            if (isList) {
                for (Object child : (Iterable<?>) root) {
                    if (child instanceof Collection && child.getClass().getSimpleName().equals("List") && isOpt)
                        api.putWarning(ASTAPI.AST_STRUCTURE_WARNING, "A List is a direct child to a Opt parent, parent : " + root + ", -> child : " + child);
                    children.add(new Node(child, isOpt ? name : "", child instanceof Collection, false, 1, api));
                }
            }
            traversDown(root, api);
        }
    }

    /**
     * Iterates through the methods of the root and fins the references to the children, for each child is traverses down
     * @param root
     * @param api
     */
    private void traversDown(Object root, ASTAPI api) {
        try {
            for (Method m : root.getClass().getMethods()) {
                for (Annotation a: m.getAnnotations()) {
                    if(ASTAnnotation.isChild(a)) {
                        Object obj = m.invoke(root, new Object[m.getParameterCount()]);
                        nullCheck(obj, api, ASTAnnotation.getName(a));
                        children.add(new Node(obj, ASTAnnotation.getName(a),
                                !ASTAnnotation.isSingleChild(a),
                                ASTAnnotation.isOptChild(a),
                                level + 1, api));
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void nullCheck(Object obj, ASTAPI api, String name){
        if(obj == null) {
            api.putError(ASTAPI.AST_STRUCTURE_ERROR, String.format("The child %s is null, can't continue the traversal of this path", name));
        }
    }

    public String nodeName() { return isNull() ? "null" : node.toString(); }
    public boolean isOpt(){return isOpt;}
    public boolean isList(){ return isList; }
    public boolean isNull(){ return isNull; }
    public String toString() {
        return className;
    }

    public int getLevel(){ return level;}

    public NodeContent getNodeContent(){ return nodeContent;}

}
