package jastaddad.api.nodeinfo;

import jastaddad.api.ASTAnnotation;
import jastaddad.api.Node;

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

    private HashMap<String, NodeInfo> attributes;
    private HashMap<String, NodeInfo> tokens;
    private ArrayList<String> invocationErrors; //Error list for invoke calls
    private HashMap<String, NodeInfo> invokedValues; //The cached values for invoked methods
    private Node node; //The node the content is a part of
    private Object nodeObject; //The node the content is a part of

    public NodeContent(Node node){
        attributes = new HashMap<>();
        tokens = new HashMap<>();
        invocationErrors = new ArrayList<>();
        invokedValues = new HashMap<>();
        this.node = node;
        this.nodeObject = node.node;
    }

    /**
     * Will return the errors that may have been cast during the invocation of a method or methods.
     * NOTE: Will also clear the list.
     * @return
     */
    public ArrayList<String> getInvocationErrors(){
        ArrayList<String> temp = invocationErrors;
        invocationErrors.clear();
        return temp;
    }

    /**
     * Will check if any error have been cast during some invocation.
     * @return
     */
    public boolean noErrors(){ return invocationErrors.size() == 0; }

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
     * Prints the name with its parameters, if the parameters are null the type of the parameters will then be added.
     * * MethodName(1,2) or MethodName(int, String)
     * @param m
     * @param params
     * @return
     */
    protected static String getName(Method m, Object[] params){
        String name = m.getName() + "(";
        if (m.getParameterCount() > 0){
            for(int i = 0; i < m.getParameterCount(); i++) {
                name += params != null ? params[i].toString() : m.getParameterTypes()[i].toString();
                if(i + 1 < m.getParameterCount())
                    name += ",";
            }
        }
        return name + ")";
    }

    /**
     * Computes the method in the NodeInfo, with the given parameters, and adds it to the cached list.
     * If forceComputation is true it will compute even if it's parametrized or NTA
     * @param nodeInfo
     * @param par
     * @return true if the invocation was successful.
     */
    public boolean compute(NodeInfo nodeInfo, ArrayList<Object> par, boolean forcedComputation){
        invocationErrors.clear();
        Object[] params = null;
        Method method = nodeInfo.getMethod();
        try{
            params = new Object[method.getParameterCount()];
            Object obj;
            for(int i = 0; i < method.getParameterCount(); i++){
                obj = par.get(i);
                Class c = method.getParameterTypes()[i];
                if(obj == null)
                    return false;
                obj = getParam(obj, c);
                if(obj == null)
                    return false;
                params[i] = obj;
            }
            invokedValues.put(method.getName(), getAttribute(nodeObject, method, params, forcedComputation));
            return true;
        }catch(Throwable e){
            invokedValues.put(method.getName(), getAttribute(null, method, params, forcedComputation));
            addInvocationErrors(e);
            return false;
        }
    }

    public boolean compute(NodeInfo nodeInfo, ArrayList<Object> par){
        return compute(nodeInfo, par, false);
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
        return obj;
    }

    /**
     * Computes all methods of the NodeContents node, this will clear the old values except the invoked ones.
     * This is used for onDemand execution attributes values.
     * If forceComputation is true it will compute even if it's parametrized or NTA
     * @return
     */
    public ArrayList<String> compute(boolean forceComputation){
        invocationErrors.clear();
        attributes.clear();
        tokens.clear();
        compute(nodeObject, forceComputation);
        return invocationErrors;
    }

    public ArrayList<String> compute(){
        return compute(false);
    }

    /**
     * Computes all methods of the given object, the values will be added to NodeContents value Lists.
     * NOTE: it will only compute the methods with annotations of the ASTAnnotation type.
     * If forceComputation is true it will compute even if it's parametrized or NTA
     * @param obj
     */
    public void compute(Object obj, boolean forceComputation){
        if(node.isNull())
            return;
        for(Method m : obj.getClass().getMethods()){
            computeMethod(m, true, null, forceComputation);
        }
    }

    public void compute(Object obj){
        compute(obj , false);
    }

    /**
     * Compute the attribute/token method with some given name.
     * @param method
     * @return
     */

    public NodeInfo computeMethod(String method){
        try{
            return computeMethod(nodeObject.getClass().getMethod(method), false, null);
        }  catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Computes the given method, and depending on the parameter @param add it to the list with the computed Attributes.
     * If forceComputation is true it will compute even if it's parametrized or NTA
     * @param m
     * @param add
     * @return
     */
    public NodeInfo computeMethod(Method m, boolean add, Object[] params, boolean forceComputation){
        NodeInfo info = null;
        for (Annotation a : m.getAnnotations()) {
            if (ASTAnnotation.isAttribute(a)) {
                info = getAttribute(nodeObject, m, params, forceComputation);
                if(add)
                    attributes.put(getName(m , params), info);
                break;
            } else if (ASTAnnotation.isToken(a)) {
                info = getToken(nodeObject, m);
                if(add)
                    tokens.put(m.getName(), info);
                break;
            }
        }
        return info;
    }

    public NodeInfo computeMethod(Method m, boolean add, Object[] params){
        return computeMethod(m, add, params, false);
    }

    /**
     * Creates a Attribute and invokes the method with the supplied parameters, if any.
     * Will also add the specific information about the Attribute, which is derived form the annotations.
     * @param obj
     * @param m
     * @param params
     * @return
     */
    private Attribute getAttribute(Object obj, Method m, Object[] params){
        return getAttribute(obj, m, params, false);
    }

    /**
     * Creates a Attribute and invokes the method with the supplied parameters, if any.
     * Will also add the specific information about the Attribute, which is derived form the annotations.
     * If forceComputation is true it will compute even if it's parametrized or NTA
     * @param obj
     * @param m
     * @param params
     * @param forceComputation
     * @return
     */
    private Attribute getAttribute(Object obj, Method m, Object[] params, boolean forceComputation){
        Attribute attribute = new Attribute(m.getName(), null, m);
        attribute.setParametrized(m.getParameterCount() > 0);
        for(Annotation a : m.getAnnotations()) { //To many attribute specific methods so I decided to iterate through the Annotations again instead of sending them as parameters.
            if (ASTAnnotation.isAttribute(a)){
                attribute.setKind(ASTAnnotation.get(a, ASTAnnotation.AST_METHOD_KIND));
                attribute.setCircular(ASTAnnotation.is(a, ASTAnnotation.AST_METHOD_CIRCULAR));
                attribute.setNTA(ASTAnnotation.is(a, ASTAnnotation.AST_METHOD_NTA));
            }else if(ASTAnnotation.isSource(a)){
                attribute.setAspect(ASTAnnotation.getString(a, ASTAnnotation.AST_METHOD_ASPECT));
                attribute.setDeclaredAt(ASTAnnotation.getString(a, ASTAnnotation.AST_METHOD_DECLARED_AT));
            }
        }
        try{
            if((attribute.isParametrized() || attribute.isNTA()) && !forceComputation) {
                attribute.setValue(null);
            }else if(params != null && params.length == m.getParameterCount()) {
                attribute.setValue(m.invoke(obj, params));
            } else if(obj != null)
                attribute.setValue(m.invoke(obj, new Object[0]));
        } catch (Throwable e) {
            addInvocationErrors(e);
            attribute.setValue(e.getCause());
        }
        return attribute;
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
        String message = e.getCause() != null ? e.getCause().toString() : e.getMessage();
        invocationErrors.add(message);
        e.printStackTrace();
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
}
