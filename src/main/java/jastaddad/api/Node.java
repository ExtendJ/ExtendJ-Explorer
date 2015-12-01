package jastaddad.api;


import jastaddad.api.nodeinfo.NodeInfo;
import jastaddad.api.nodeinfo.NodeInfoHolder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * This class represents the Node in the AST, it holds all its terminal attributes and references to its children.
 * This class should only be created once, due to that it will traverse the tree from the given Object.
 */
public class Node{
    public final Object node;
    public final int id;
    public final String nameFromParent;
    public final String simpleNameClass;
    public final String fullName;
    public final ArrayList<Node> children;
    public final HashMap<String,Node> NTAChildren;
    private boolean isList;
    private boolean isOpt;
    private boolean isNTA;
    private int level;
    private NodeContent nodeContent;

    /**
     * This is THE constructor for the root node of the AST
     * @param root
     * @param api used for contributing errors and warnings, during the traversal of the AST.
     */
    public Node(Object root, ASTAPI api){
        this.children = new ArrayList<>();
        this.NTAChildren = new HashMap<>();
        this.nameFromParent = "";
        if(root != null)
            this.simpleNameClass = root.getClass().getSimpleName();
        else
            this.simpleNameClass = "Null";
        this.node = root;
        fullName = simpleNameClass;
        id = System.identityHashCode(this.toString());
        init(root, false, false, false, 1, api);
    }

    /**
     * This is the constructor used for NTA:S during the traversal of the AST.
     * Will traverse the real childs of the node
     * @param root
     * @param api
     */
    public Node(Object root, boolean NTA, ASTAPI api){
        this.children = new ArrayList<>();
        this.NTAChildren = new HashMap<>();
        this.nameFromParent = "";
        this.isNTA = NTA;
        if(root != null)
            this.simpleNameClass = root.getClass().getSimpleName();
        else
            this.simpleNameClass = "Null";
        this.node = root;
        fullName = simpleNameClass;
        id = System.identityHashCode(this.toString());
        init(root, true, false, true,  1, api);
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
    public Node(Object root, String name, boolean isList, boolean isOpt, boolean isNTA, int level, ASTAPI api){
        this.children = new ArrayList<>();
        this.NTAChildren = new HashMap<>();
        if(root != null)
            this.simpleNameClass = root.getClass().getSimpleName();
        else
            this.simpleNameClass = "Null";
        this.node = root;

        if(name == simpleNameClass || name.length() == 0){
            this.nameFromParent = "";
            fullName = simpleNameClass;
        }else {
            this.nameFromParent = name;
            fullName = simpleNameClass + ":" + name;
        }
        id = System.identityHashCode(this.toString());
        init(root, isList, isOpt, isNTA, level, api);
    }

    public boolean isChildClassOf(Class parent){
        return isChildClassOf(parent, node.getClass());

    }

    private boolean isChildClassOf(Class parent, Class child){

        if(child == null)
            return false;
        if(child == parent)
            return true;
        return isChildClassOf(parent, child.getSuperclass());
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
    private void init(Object root, boolean isList, boolean isOpt, boolean isNTA, int level, ASTAPI api){
        this.isOpt = isOpt;
        this.isList = isList;
        this.nodeContent = new NodeContent(this);
        this.level = level;
        if(root != null) {
            api.addASTObject(node, isNTA);
            if (isList) {
                for (Object child : (Iterable<?>) root) {
                    if (child instanceof Collection && child.getClass().getSimpleName().equals("List") && isOpt)
                        api.putWarning(ASTAPI.AST_STRUCTURE_WARNING, "A List is a direct child to a Opt parent, parent : " + root + ", -> child : " + child);
                    children.add(new Node(child, isOpt ? nameFromParent : "", child instanceof Collection, false, isNTA, 1, api));
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
                        String name = ASTAnnotation.getString(a, ASTAnnotation.AST_METHOD_NAME);
                        nullCheck(obj, api, name);
                        children.add(new Node(obj, name,
                                !ASTAnnotation.isSingleChild(a),
                                ASTAnnotation.isOptChild(a),
                                isNTA, level + 1, api));
                    }else if(ASTAnnotation.isAttribute(a) && ASTAnnotation.is(a, ASTAnnotation.AST_METHOD_NTA) && m.getParameterCount() == 0){
                        NTAChildren.put(m.getName(), null);
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
    public boolean isNull(){ return node == null; }

    public boolean isNTA(){ return isNTA; }

    public String toString() { return simpleNameClass; }
    public int getLevel(){ return level;}

    public NodeContent getNodeContent(){ return nodeContent;}
    public ArrayList<NodeInfoHolder> getNodeContentArray(){ return nodeContent.toArray();}

}
