package jastaddad.api;

import jastaddad.api.ASTAPI;
import jastaddad.api.ASTAnnotation;
import jastaddad.api.Node;
import jastaddad.api.nodeinfo.Attribute;
import jastaddad.api.nodeinfo.NodeInfo;
import jastaddad.api.nodeinfo.NodeInfoHolder;
import jastaddad.api.nodeinfo.Token;

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

    private ArrayList<NodeInfo> attributes;
    private ArrayList<NodeInfo> tokens;
    private ArrayList<String> invocationErrors; //Error list for invoke calls
    private ArrayList<NodeInfo> invokedValues; //The cached values for invoked methods
    private Node node; //The node the content is a part of
    private Object nodeObject; //The node the content is a part of

    public NodeContent(Node node){
        attributes = new ArrayList<>();
        tokens = new ArrayList<>();
        invokedValues = new ArrayList<>();
        invocationErrors = new ArrayList<>();
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
     * Computes the method in the NodeInfo, with the given parameters, and adds it to the cached list.
     * If forceComputation is true it will compute even if it's parametrized or NTA
     * @param nodeInfo
     * @param par
     * @return true if the invocation was successful.
     */
    protected Object compute(NodeInfo nodeInfo, ArrayList<Object> par, boolean forcedComputation, ASTAPI api) {
        invocationErrors.clear();
        Object[] params;
        Method method = nodeInfo.getMethod();
        if ((par != null && par.size() != method.getParameterCount()) || (par == null && method.getParameterCount() != 0)) {
            invocationErrors.add("Wrong number of arguments for the method: " + method);
            return null;
        }
        if(par == null)
            params = new Object[method.getParameterCount()];
        else
            params = par.toArray();

        Attribute attribute;
        try{
            attribute = getAttribute(nodeObject, method, params, forcedComputation);
            if(!api.isObjectReference(attribute.getValue()))
                invokedValues.add(attribute);
            return attribute.getValue();
        }catch(Throwable e){
            attribute = getAttribute(null, method, params, forcedComputation);
            if(!api.isObjectReference(attribute.getValue()))
                invokedValues.add(attribute);
            e.printStackTrace();
            addInvocationErrors(e);
            return null;
        }
    }

    protected Object compute(NodeInfo nodeInfo, ArrayList<Object> par, ASTAPI api){
        return compute(nodeInfo, par, false, api);
    }


    /**
     * Computes all methods of the NodeContents node, this will clear the old values except the invoked ones.
     * This is used for onDemand execution attributes values.
     * If forceComputation is true it will compute even if it's parametrized or NTA
     * @return
     */
    protected ArrayList<String> compute(boolean forceComputation){
        invocationErrors.clear();
        attributes.clear();
        tokens.clear();
        compute(nodeObject, forceComputation);
        return invocationErrors;
    }

    protected ArrayList<String> compute(){
        return compute(false);
    }

    /**
     * Computes all methods of the given object, the values will be added to NodeContents value Lists.
     * NOTE: it will only compute the methods with annotations of the ASTAnnotation type.
     * If forceComputation is true it will compute even if it's parametrized or NTA
     * @param obj
     */
    protected void compute(Object obj, boolean forceComputation){
        if(node.isNull())
            return;
        for(Method m : obj.getClass().getMethods()){
            computeMethod(m, true, null, forceComputation);
        }
    }

    protected void compute(Object obj){
        compute(obj , false);
    }

    /**
     * Compute the attribute/token method with some given name.
     * @param method
     * @return
     */

    protected NodeInfo computeMethod(String method){
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
    protected NodeInfo computeMethod(Method m, boolean add, Object[] params, boolean forceComputation){
        NodeInfo info = null;
        for (Annotation a : m.getAnnotations()) {
            if (ASTAnnotation.isAttribute(a)) {
                info = getAttribute(nodeObject, m, params, forceComputation);
                if(add)
                    attributes.add(info);
                break;
            } else if (ASTAnnotation.isToken(a)) {
                info = getToken(nodeObject, m);
                if(add)
                    tokens.add(info);
                break;
            }
        }
        return info;
    }

    protected NodeInfo computeMethod(Method m, boolean add, Object[] params){
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
    protected ArrayList<NodeInfoHolder> toArray(){
        ArrayList<NodeInfoHolder> temp = new ArrayList<>();
        for (NodeInfo a : attributes)
            temp.add(new NodeInfoHolder(a.print(), a.getValue(), a));
        for (NodeInfo t : tokens)
            temp.add(new NodeInfoHolder(t.print(), t.getValue(), t));
        if(invokedValues != null) {
            for (NodeInfo i : invokedValues)
                temp.add(new NodeInfoHolder(i.getName(), i.getValue(), i));
        }
        Collections.sort(temp);
        return temp;
    }
}
