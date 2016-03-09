package drast.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * This class represents the Node in the AST, it holds all its terminal attributes and references to its children.
 * This class should only be created once, due to that it will traverse the tree from the given Object.
 */
public class Node{
    public final Object node;
    public final Node parent;
    public final int id;
    public final String nameFromParent;
    public final String simpleNameClass;
    public final String fullName;
    public final ArrayList<Node> children;
    public final HashMap<Object, Node> NTAChildren;
    public final HashMap<String,Node> showNTAChildren;
    private boolean isList;
    private boolean isOpt;
    private boolean isNTA;
    private int level;
    private NodeData nodeData;

    /**
     * This is THE constructor for the root node of the AST
     * @param root
     * @param astBrain used for contributing errors and warnings, during the traversal of the AST.
     */
    public Node(Object root, ASTBrain astBrain, boolean isList){
        this.children = new ArrayList<>();
        this.NTAChildren = new HashMap<>();
        this.showNTAChildren = new HashMap<>();
        this.nameFromParent = "";
        this.parent = null;
        if(root != null)
            this.simpleNameClass = root.getClass().getSimpleName();
        else
            this.simpleNameClass = "Null";
        this.node = root;
        fullName = simpleNameClass;
        id = System.identityHashCode(this.toString());
        init(root, isList, false, false, 1, astBrain);
    }


    /**
     * This is the constructor used for NTA:S during the traversal of the AST.
     * Will traverse the real childs of the node
     * @param root
     * @param astBrain
     */

    public static Node getNTANode(Object root, Node parent, ASTBrain astBrain){//Todo need an annotation that specify if the NTA is a List
        return new Node(root, parent, root.getClass().getSimpleName().equals("List"), true, astBrain);     }

    private Node(Object root, Node parent, boolean isList, boolean NTA, ASTBrain astBrain){
        this.children = new ArrayList<>();
        this.NTAChildren = new HashMap<>();
        this.showNTAChildren = new HashMap<>();
        this.nameFromParent = "";
        this.isNTA = NTA;
        this.isList = isList;
        if(root != null)
            this.simpleNameClass = root.getClass().getSimpleName();
        else
            this.simpleNameClass = "Null";
        this.node = root;
        this.parent = parent;
        fullName = simpleNameClass;
        id = System.identityHashCode(this.toString());
        init(root, false, isList, true,  1, astBrain);
    }

    /**
     * This is the constructor used during the traversal of the AST
     * @param root
     * @param name
     * @param isList
     * @param isOpt
     * @param level
     * @param astBrain
     */
    public Node(Object root, Node parent, String name, boolean isList, boolean isOpt, boolean isNTA, int level, ASTBrain astBrain){
        this.children = new ArrayList<>();
        this.NTAChildren = new HashMap<>();
        this.showNTAChildren = new HashMap<>();
        this.parent = parent;
        if(root != null)
            this.simpleNameClass = root.getClass().getSimpleName();
        else
            this.simpleNameClass = "Null";
        this.node = root;

        if(name.equals(simpleNameClass) || name.length() == 0){
            this.nameFromParent = "";
            fullName = simpleNameClass;
        }else {
            this.nameFromParent = name;
            fullName = simpleNameClass + ":" + name;
        }
        id = System.identityHashCode(this.toString());
        init(root, isList, isOpt, isNTA, level, astBrain);
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
     * @param astBrain
     */
    private void init(Object root, boolean isList, boolean isOpt, boolean isNTA, int level, ASTBrain astBrain){
        this.isOpt = isOpt;
        this.isList = isList;
        this.nodeData = new NodeData(this);
        this.level = level;
        if(root != null) {
            astBrain.addASTObject(node, isNTA);
            try {
                if (isList) {
                    for (Object child : (Iterable<?>) root) {
                        if (child instanceof Collection && child.getClass().getSimpleName().equals("List") && isOpt) //Todo remove this when we have the AST in the annotations
                            astBrain.putMessage(AlertMessage.AST_STRUCTURE_WARNING, "A List is a direct child to a Opt parent, parent : " + root + ", -> child : " + child);
                        children.add(new Node(child, this, isOpt ? nameFromParent : "", child instanceof Collection, false, isNTA, 1, astBrain));
                    }
                }
            }catch (ClassCastException e){
                String message = isNTA ? "Object : " + root + " is not a type of the AST" : e.getMessage();
                astBrain.putMessage(AlertMessage.AST_STRUCTURE_ERROR, message);
                return;
            }
            traversDown(root, astBrain);
        }
    }

    /**
     * Iterates through the methods of the root and fins the references to the children, for each child is traverses down
     * @param root
     * @param astBrain
     */
    private void traversDown(Object root, ASTBrain astBrain) {
        ArrayList<AbstractMap.SimpleEntry<Method, Annotation>> methods = astBrain.getMethods(root.getClass());
        if(methods == null) {
            methods = new ArrayList<>();
            ArrayList<Method> NTAMethods = new ArrayList<>();
            for (Method m : root.getClass().getMethods()) {
                for (Annotation a : m.getAnnotations()) {
                    if (ASTAnnotation.isChild(a)) {
                        methods.add(new AbstractMap.SimpleEntry<>(m,a));
                    } else if (ASTAnnotation.isAttribute(a) && ASTAnnotation.is(a, ASTAnnotation.AST_METHOD_NTA)) {
                        if (m.getParameterCount() == 0)
                            showNTAChildren.put(m.getName(), null);
                        NTAMethods.add(m);
                    }
                }
            }
            astBrain.putMethods(root.getClass(), methods);
            astBrain.putNTAMethods(root.getClass(), NTAMethods);
        }

        if(astBrain.getConfig().getBoolean(Config.NTA_CACHED))
            getNodeData().setCachedNTAs(astBrain);

        try {
            for (AbstractMap.SimpleEntry<Method, Annotation> p : methods) {
                Annotation a = p.getValue();
                Object obj = p.getKey().invoke(root, new Object[p.getKey().getParameterCount()]);
                String name = ASTAnnotation.getString(a, ASTAnnotation.AST_METHOD_NAME);
                nullCheck(obj, astBrain, name);
                children.add(new Node(obj, this, name,
                        !ASTAnnotation.isSingleChild(a),
                        ASTAnnotation.isOptChild(a),
                        isNTA, level + 1, astBrain));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        astBrain.getAnalyzer().executeDuringRTAnalyzers(this);
    }

    private void nullCheck(Object obj, ASTBrain astBrain, String name){
        if(obj == null) {
            astBrain.putMessage(AlertMessage.AST_STRUCTURE_ERROR, String.format("The child %s is null, can't continue the traversal of this path", name));
        }
    }

    public boolean isOpt(){return isOpt;}
    public boolean isList(){ return isList; }
    public boolean isNull(){ return node == null; }

    public boolean isNTA(){ return isNTA; }

    public String getSimpleNameClass() { return simpleNameClass; }
    public String toString() { return simpleNameClass; }
    public int getLevel(){ return level;}

    public NodeData getNodeData(){ return nodeData;}

}
