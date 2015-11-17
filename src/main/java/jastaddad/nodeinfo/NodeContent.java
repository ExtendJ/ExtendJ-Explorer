package jastaddad.nodeinfo;

import jastaddad.ASTAnnotation;
import jastaddad.Node;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This class holds all information about the a node, all attribute values and invokedvalues.
 * It can invoke methods with the compute methods.
 * Created by gda10jli on 10/14/15.
 */
public class NodeContent {

    public static final String USER_INPUT = "Need input from user";

    private HashMap<String, NodeInfo> attributes;
    private HashMap<String, NodeInfo> tokens;
    private ArrayList<String> innovationErrors; //Error list for invoke calls
    private HashMap<String, NodeInfo> invokedValues; //The cached values for invoked methods
    private Node node; //The node the content is a part of

    public NodeContent(Node node){
        attributes = new HashMap();
        tokens = new HashMap();
        innovationErrors = new ArrayList<>();
        this.node = node;
    }

    /**
     * Will return the errors that may have been cast during the invocation of a method or methods.
     * NOTE: Will also clear the list.
     * @return
     */
    public ArrayList<String> getInnvokationErrors(){
        ArrayList<String> temp = innovationErrors;
        innovationErrors.clear();
        return temp;
    }

    /**
     * Will check if any error have been cast during some invocation.
     * @return
     */
    public boolean noErrors(){ return innovationErrors.size() == 0; }

    /**
     * Add some invoked value to the cachedMap
     * @param info
     */
    public void addInvokedValue(NodeInfo info){
        if(invokedValues == null)
            invokedValues = new HashMap<>();
        invokedValues.put(info.getName(), info);
    }

    /**
     * Will return an Attribute or Token for the given key.
     * @param key
     * @return
     */
    public NodeInfo get(String key){
        NodeInfo ret = attributes.get(key);
        if(ret == null)
            ret = tokens.get(key);
        return ret;
    }

    /**
     * Computes the method in the NodeInfo, with the given parameters.
     * @param nodeInfo
     * @param par
     * @return the value of the computation.
     */
    public Object compute(NodeInfo nodeInfo, ArrayList<Object> par){
        if(!nodeInfo.isParametrized())
            return null;
        innovationErrors.clear();
        Object[] params = null;
        Method method = nodeInfo.getMethod();
        try{
            params = new Object[method.getParameterCount()];
            Object obj;
            for(int i = 0; i < method.getParameterCount(); i++){
                obj = par.get(i);
                Class c = method.getParameterTypes()[i];
                if(obj == null)
                    return null;
                obj = getParam(obj, c);
                if(obj == null)
                    return null;
                params[i] = obj;
            }
            obj = method.invoke(node.node, params);
            if(obj == null)
                return null;
            node.getNodeContent().addInvokedValue(new Attribute(NodeInfo.getName(method, params), obj, method, "", true));
            return obj;
        }catch(Throwable e){
            node.getNodeContent().addInvokedValue(new Attribute(NodeInfo.getName(method, params), e.getCause(), method, "", true));
            addInvocationErrors(e);
            return null;
        }
    }

    /**
     * Creates a object of the given class type, where the Object will be cast.
     * @param obj
     * @param c
     * @return The newly created object.
     */
    private Object getParam(Object obj, Class c){ //Todo expand this shit, and do something smarter with the parameters
        if(c == int.class || c == Integer.class)
            return new Integer(Integer.parseInt(obj.toString()));
        else if(c == boolean.class || c == Boolean.class)
            return Boolean.parseBoolean(obj.toString());
        else if(c == String.class)
            return obj;
        return null;
    }

    /**
     * Computes all methods of the NodeContents node, this will clear the old values except the invoked ones.
     * This is used for onDemand execution attributes values.
     * @return
     */
    public ArrayList<String> compute(){
        innovationErrors.clear();
        attributes.clear();
        tokens.clear();
        compute(node.node);
        return innovationErrors;
    }

    /**
     * Compute the attribute/token method with some given name.
     * @param method
     * @return
     */
    public NodeInfo compute(String method){
        try {
            Method m = node.node.getClass().getMethod(method);
            for (Annotation a : m.getAnnotations()) {
                if (ASTAnnotation.isAttribute(a))
                    return getAttribute(node.node, m);
                if (ASTAnnotation.isToken(a))
                    return getToken(node.node, m);
            }
        }  catch (NoSuchMethodException e) {
        }
        return null;
    }

    /**
     * Computes all methods of the given object, the values will be added to NodeContents value Lists.
     * NOTE: it will only compute the methods with annotations of the ASTAnnotation type.
     * @param obj
     */
    public void compute(Object obj){
        for(Method m : obj.getClass().getMethods()){
            compute(obj, m);
        }
    }

    /**
     * Computes the method of the given object if it has an annotation of the ASTAnnotation type.
     * The value will be added to a List in the NodeContent
     * @param obj
     */
    public void compute(Object obj, Method m) {
        for(Annotation a : m.getAnnotations()) {
            compute(obj, m, a);
        }
    }

    /**
     * Computes the method of the given object with if the annotation is a ASTAnnotation.
     * The value will be added to a List in the NodeContent
     * @param obj
     */
    public NodeInfo compute(Object obj, Method m, Annotation a)  {
        if(ASTAnnotation.isAttribute(a)) {
            return attributes.put(NodeInfo.getName(m),getAttribute(obj, m));
        }
        if(ASTAnnotation.isToken(a))
            return tokens.put(NodeInfo.getName(m), getToken(obj, m));
        return null;
    }

    /**
     * Get the Attribute of the method in the obj.
     * @param obj
     * @param m
     * @return
     */
    //Todo might move these methods to their specific classes
    private Attribute getAttribute(Object obj, Method m){
        String name = m.getName();
        boolean parametrized = m.getParameterCount() > 0;
        try{
            if(parametrized)
                return new Attribute(name, USER_INPUT, m, "", true);
            else
                return new Attribute(name, m.invoke(obj, new Object[0]), m, "");
        } catch (Throwable e) {
            addInvocationErrors(e);
            return new Attribute(name, e.getCause().toString(), m, "", parametrized);
        }
    }

    /**
     * Get the Token of the method in the obj.
     * @param obj
     * @param m
     * @return
     */
    private Token getToken(Object obj, Method m){
        String name = m.getName();
        try{
            return new Token(name, m.invoke(obj, new Object[0]), m);
        } catch (Throwable e) {
            addInvocationErrors(e);
            return new Token(name, e.getCause().toString(), m);
        }
    }


    private void addInvocationErrors(Throwable e){
        innovationErrors.add(e.getCause().toString());
        //e.printStackTrace();
    }

    /**
     * Creates a list of all attributes, tokens and invokedValues
     * @return
     */
    public ArrayList<NodeInfoHolder> toArray(){
        ArrayList<NodeInfoHolder> temp = new ArrayList<>();
        for (NodeInfo a : attributes.values())
            temp.add(new NodeInfoHolder(a.print(), a.getValue(), a));
        for (NodeInfo t : tokens.values())
            temp.add(new NodeInfoHolder(t.print(), t.getValue(), t));
        if(invokedValues != null) {
            for (NodeInfo i : invokedValues.values())
                temp.add(new NodeInfoHolder(i.getName(), i.getValue(), i));
        }
        Collections.sort(temp);
        return temp;
    }


    private HashMap<Object, Object> getCachedMapValues(Object obj, Method m) throws IllegalAccessException, InstantiationException {
        try {
            Field f = getField(obj.getClass(), m.getName(), "_values");
            if(f == null)
                return null;
            f.setAccessible(true);
            Object map = f.get(obj);
            return map != null ? (HashMap<Object, Object>) map : null;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    private Field getField(Class clazz, String mName, String fieldName) throws NoSuchFieldException {
        for(Field f : clazz.getDeclaredFields()){
            if(f.getName().contains(mName + '_') && f.getName().endsWith(fieldName))
                return f;
        }
        Class superClass = clazz.getSuperclass();
        if (superClass == null)
           return null;
        else
            return getField(superClass, mName, fieldName);
    }

}
