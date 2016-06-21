package drast.model.reflection;

import drast.model.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by gda10jth on 2/18/16.
 * Do not use System.out.print(ln) here, atm is the model locked to the main thread, will crash DrAST.
 * This needs to bee fixed, cause is that if the model is run through the GUI which overrides the standard out/err it will cause a deadlock
 */
public class ReflectionNode implements Node {

    private Object ASTObject;
    private Node parent;
    private int id;
    private String nameFromParent;
    private String simpleNameClass;
    private ArrayList<Node> children;
    private HashMap<Object, Node> NTAChildren;
    private HashMap<String,Node> showNTAChildren;
    private boolean isList;
    private boolean isOpt;
    private boolean isNTA;
    private int level;
    private NodeData nodeData;

    private static Method orderMethod;
    private static Class orderSuperClass;

    private static HashMap<Class, ArrayList<Method>> cachedNTAMethods;
    private static HashMap<Class, ArrayList<AbstractMap.SimpleEntry<Method, Annotation>>> cachedMethods;

    /**
     * This is THE method for creating the Reflected tree from the root, only use this
     * on a new instance of the model as it cachesThings in static fields
     * @param root
     * @param astBrain used for contributing errors and warnings, during the traversal of the AST.
     */
    public static Node getReflectedTree(Object root, ASTBrain astBrain, boolean isList){
        return new ReflectionNode(root, astBrain, isList);
    }

    private ReflectionNode(Object root, ASTBrain astBrain, boolean isList){
        orderMethod = null;
        orderSuperClass = null;
        cachedMethods = null;
        cachedNTAMethods = null;
        this.nameFromParent = "";
        init(root, null, isList, false, false, 1, astBrain);
    }


    /**
     * This is THE method used to create NTA´s and their subtrees
     * Will traverse the children of the given root, and create its subtree
     * @param root
     * @param astBrain
     */

    public Node getNTATree(Object root, Node parent, ASTBrain astBrain){
        return new ReflectionNode(root, parent, ASTAnnotation.isList(root.getClass()), true, astBrain);
    }

    public static void createNTATree(Node node, Object root, ASTBrain astBrain){
        if (node.getNTAChildren().containsKey(root) || astBrain.isASTObject(root))
            return;
        Node temp = node.getNTATree(root, node, astBrain);
        node.getNTAChildren().put(root, temp);
    }

    public Node getNTATree(String s, ASTBrain astBrain){
        Node ntaNode = showNTAChildren.get(s);
        if (ntaNode == null) {
            ntaNode = getNTATree(getNodeData().computeMethod(s), this, astBrain);
            showNTAChildren.put(s, ntaNode);
            astBrain.addASTObject(ntaNode.getASTObject(), true);
        }
        return ntaNode;
    }

    private ReflectionNode(Object root, Node parent, boolean isList, boolean NTA, ASTBrain astBrain){
        this.nameFromParent = "";
        this.isNTA = NTA;
        init(root, parent, false, isList, true,  1, astBrain);
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

    private ReflectionNode(Object root, Node parent, String name, boolean isList, boolean isOpt, boolean isNTA, int level, ASTBrain astBrain){
        this.nameFromParent =  name.equals(simpleNameClass) || name.length() == 0 ?  "" : name;
        init(root, parent, isList, isOpt, isNTA, level, astBrain);
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
    private void init(Object root, Node parent, boolean isList, boolean isOpt, boolean isNTA, int level, ASTBrain astBrain){
        this.ASTObject = root;
        this.parent = parent;
        this.nodeData = new ReflectionNodeData(this);
        this.isOpt = isOpt;
        this.isList = isList;
        this.level = level;
        this.children = new ArrayList<>();
        this.NTAChildren = new HashMap<>();
        this.showNTAChildren = new HashMap<>();

        if(cachedMethods == null)
            cachedMethods = new HashMap<>();
        if(cachedNTAMethods == null)
            cachedNTAMethods = new HashMap<>();

        id = System.identityHashCode(this.toString());
        if(root != null)
            this.simpleNameClass = root.getClass().getSimpleName();
        else
            this.simpleNameClass = "Null";

        if(root != null) {
            astBrain.addASTObject(ASTObject, isNTA);
            try {
                if (isList) {
                    for (Object child : (Iterable<?>) root) {
                        checkASTStructure(child, root, astBrain);
                        children.add(new ReflectionNode(child, this, isOpt ? nameFromParent : "", child instanceof Collection, false, isNTA, 1, astBrain));
                    }
                }
            }catch (ClassCastException e){
                String message = isNTA ? "Object : " + root + " is not a type of the AST" : e.getMessage();
                astBrain.putMessage(AlertMessage.AST_STRUCTURE_WARNING, message);
                return;
            }
            traversDown(root, astBrain);
        }
    }

    private void checkASTStructure(Object child, Object root, ASTBrain astBrain){
        if (child instanceof Collection && ASTAnnotation.isList(child.getClass()) && isOpt)
            astBrain.putMessage(AlertMessage.AST_STRUCTURE_WARNING, "A List is a direct child to a Opt parent, parent : " + root + ", -> child : " + child);
    }

    /**
     * Iterates through the methods of the root and fins the references to the children, for each child is traverses down
     * @param root
     * @param astBrain
     */
    private void traversDown(Object root, ASTBrain astBrain) {
        ArrayList<AbstractMap.SimpleEntry<Method, Annotation>> methods = cachedMethods.get(root.getClass());

        if(methods == null)
            methods = getMethods(root);

        if(astBrain.getConfig().getBoolean(Config.NTA_CACHED)) //Find this nodes cached NTA:s
            getNodeData().setCachedNTAs(astBrain);

        try {
            for (AbstractMap.SimpleEntry<Method, Annotation> p : methods) {
                Annotation a = p.getValue();
                Object obj = p.getKey().invoke(root, new Object[p.getKey().getParameterCount()]);
                String name = ASTAnnotation.getString(a, ASTAnnotation.AST_METHOD_NAME);
                nullCheck(obj, astBrain, name);
                children.add(new ReflectionNode(obj, this, name,
                        !ASTAnnotation.isSingleChild(a),
                        ASTAnnotation.isOptChild(a),
                        isNTA, level + 1, astBrain));
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        reOrderChildren();
        astBrain.getAnalyzer().executeDuringRTAnalyzers(this);
    }

    private ArrayList<AbstractMap.SimpleEntry<Method, Annotation>> getMethods(Object root){
        ArrayList<AbstractMap.SimpleEntry<Method, Annotation>> methods = new ArrayList<>();
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
        cachedMethods.put(root.getClass(), methods);
        cachedNTAMethods.put(root.getClass(), NTAMethods);
        return methods;
    }


    private void reOrderChildren(){
        if(children.size() < 2)
            return;
        try {
            if(orderSuperClass == null) {
                Class tempClass = getASTClass();
                while (tempClass != null && !tempClass.getSimpleName().equals("ASTNode"))
                    tempClass = tempClass.getSuperclass();
                orderSuperClass = tempClass;
            }
            if(orderMethod == null)
                 orderMethod = orderSuperClass.getDeclaredMethod("getIndexOfChild", orderSuperClass);
            for(int i = 0; i < children.size(); i++){
                Node temp = children.get(i);
                int newPos = (int) orderMethod.invoke(getASTObject(), new Object[]{temp.getASTObject()});
                children.set(i, children.get(newPos));
                children.set(newPos, temp);
            }
        } catch (IllegalAccessException e){
        } catch (InvocationTargetException e){
        } catch (NoSuchMethodException e){
        } catch (ClassCastException e) {}
    }

    private void nullCheck(Object obj, ASTBrain astBrain, String name){
        if(obj == null)
            astBrain.putMessage(AlertMessage.AST_STRUCTURE_WARNING, String.format("The child %s is null, can't continue the traversal of this path", name));
    }

    @Override
    public boolean isChildClassOf(Class parent){
        Class clazz = ASTObject.getClass();
        while(clazz != null){
            if(clazz.getName().equals(parent.getName()))
                return true;
        }
        return false;

    }

    @Override
    public boolean isOpt(){return isOpt;}

    @Override
    public boolean isList(){ return isList; }

    @Override
    public boolean isNullNode() { return ASTObject == null; }

    @Override
    public boolean isNTANode() { return isNTA; }

    @Override
    public int getLevel(){ return level;}

    @Override
    public void putNTA(String methodName, Node node){ showNTAChildren.put(methodName, node);}

    @Override
    public boolean containsNTAMethod(String methodName) { return showNTAChildren.containsKey(methodName); }

    @Override
    public NodeData getNodeData(){ return nodeData;}

    @Override
    public String getSimpleClassName() { return simpleNameClass; }

    @Override
    public String getNameFromParent() { return nameFromParent; }

    @Override
    public Class getASTClass() { return ASTObject.getClass(); }

    @Override
    public HashMap<Object, Node> getNTAChildren() { return NTAChildren; }

    @Override
    public ArrayList<Method> getNTAMethods(Class clazz) { return cachedNTAMethods.get(clazz); }

    @Override
    public Object getASTObject() { return ASTObject; }

    @Override
    public Node getParent() {  return parent; }

    @Override
    public Collection<Node> getChildren() { return children; }

    @Override
    public String toString() { return simpleNameClass; }


}
