package jastaddad.api;

import jastaddad.api.nodeinfo.Attribute;
import jastaddad.api.nodeinfo.NodeInfo;
import jastaddad.api.nodeinfo.NodeInfoHolder;
import jastaddad.api.nodeinfo.Token;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class holds all information about the a node, all attribute values and tokens
 * It can invoke methods with the compute methods.
 * Created by gda10jli on 10/14/15.
 */
public class NodeContent {

    private ArrayList<NodeInfo> attributes;
    private ArrayList<NodeInfo> tokens;
    private ArrayList<NodeInfo> NTAs;
    private HashMap<Method, NodeInfo> computedMethods; //Error list for invoke calls
    private ArrayList<String> invocationErrors; //Error list for invoke calls
    private Node node; //The node the content is a part of
    private Object nodeObject; //The node the content is a part of

    public NodeContent(Node node){
        attributes = new ArrayList<>();
        tokens = new ArrayList<>();
        NTAs = new ArrayList<>();
        computedMethods = new HashMap<>();
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
     * Computes the method in the NodeInfo, with the given parameters, and adds it to the cached list of the Attribute.
     * If the params == null and the method is not parametrized it will compute the method will 0 arguments, otherwise it will return null and add a error to the api.
     * @param nodeInfo
     * @param par
     * @return true if the invocation was successful.
     */
    protected Object compute(NodeInfo nodeInfo, Object[] par, ASTAPI api) {
        invocationErrors.clear();
        Object[] params;
        Method method = nodeInfo.getMethod();
        if ((par != null && par.length != method.getParameterCount()) || (par == null && method.getParameterCount() != 0)) {
            api.putError(ASTAPI.INVOCATION_ERROR, "Wrong number of arguments for the method: " + method);
            invocationErrors.add("Wrong number of arguments for the method: " + method);
            return null;
        }
        if(par == null)
            params = new Object[method.getParameterCount()];
        else
            params = par;
        if(!nodeInfo.isAttribute()){
            api.putError(ASTAPI.INVOCATION_ERROR, "Can only do compute on attributes");
            return  null;
        }

        Attribute attribute = (Attribute) nodeInfo;
        if(attribute.containsValue(params)) {
            api.putWarning(ASTAPI.INVOCATION_WARNING, String.format("Method %s with the parameters %s have already been computed", attribute.getMethod(), params));
            return attribute.getComputedValue(params);
        }
        try{
            Object obj =  method.invoke(nodeObject, params);
            attribute.addComputedValue(params, obj);
            return obj;
        }catch(Throwable e){
            e.printStackTrace();
            addInvocationErrors(e);
            return null;
        }
    }

    /**
     * Computes all methods of the NodeContents node, this will clear the old values except the invoked ones.
     * This is used for onDemand execution attributes values.
     * If forceComputation is true it will compute the non-parametrized NTA:s
     * @return
     */
    protected ArrayList<String> compute(boolean reComputeNode, boolean forceComputation){
        invocationErrors.clear();
        if(reComputeNode){
            NTAs.clear();
            attributes.clear();
            tokens.clear();
        }
        compute(nodeObject, forceComputation);
        return invocationErrors;
    }

    /**
     * Computes all methods of the given object, the values will be added to NodeContents value Lists.
     * NOTE: it will only compute the methods with annotations of the ASTAnnotation type.
     * If forceComputation is true it will compute the non-parametrized NTA:s
     * @param obj
     */
    protected void compute(Object obj, boolean forceComputation){
        if(node.isNull())
            return;
        for(Method m : obj.getClass().getMethods()){
            computeMethod(m, null, forceComputation);
        }
    }

    /**
     * Compute the attribute/token method with some given name.
     * @param method
     * @return
     */

    protected NodeInfo computeMethod(String method){
        try{
            return computeMethod(nodeObject.getClass().getMethod(method), null, false);
        }  catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Computes the given method, and depending on the parameter @param add it to the list with the computed Attributes.
     * If forceComputation is true it will compute the non-parametrized NTA:s
     * @param m
     * @return
     */
    protected NodeInfo computeMethod(Method m, Object[] params, boolean forceComputation){
        if(computedMethods.containsKey(m))
            return computedMethods.get(m);
        NodeInfo info = null;
        for (Annotation a : m.getAnnotations()) {
            if (ASTAnnotation.isAttribute(a)) {
                info = computeAttribute(nodeObject, m, params, forceComputation);
                addAttribute((Attribute) info);
                break;
            } else if (ASTAnnotation.isToken(a)) {
                info = computeToken(nodeObject, m);
                tokens.add(info);
                break;
            }
        }
        computedMethods.put(m, info);
        return info;
    }

    /**
     * Creates a Attribute and invokes the method with the supplied parameters, if any.
     * Will also add the specific information about the Attribute, which is derived form the annotations.
     * If forceComputation is true it will compute the non-parametrized NTA:s
     * @param obj
     * @param m
     * @param params
     * @param forceComputation
     * @return
     */
    private Attribute computeAttribute(Object obj, Method m, Object[] params, boolean forceComputation){
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
        try {
            if ((attribute.isParametrized() || attribute.isNTA())) {
                if(attribute.isNTA() && !attribute.isParametrized() && forceComputation)
                    attribute.setValue(m.invoke(obj));
                else
                    attribute.setValue(null);
            }else if(params != null && params.length == m.getParameterCount())
                attribute.setValue(m.invoke(obj, params));
            else if(obj != null)
                attribute.setValue(m.invoke(obj));
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
    private Token computeToken(Object obj, Method m){
        String name = m.getName();
        try{
            return new Token(name, m.invoke(obj), m);
        } catch (Throwable e) {
            addInvocationErrors(e);
            return new Token(name, e.getCause().toString(), m);
        }
    }

    /**
     * Adds the attribute to the correct list
     * @param attribute
     */
    public void addAttribute(Attribute attribute){
        if(attribute.isNTA())
            NTAs.add(attribute);
        else
            attributes.add(attribute);
    }

    /**
     *
     * @param e
     */
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
        temp.addAll(NTAs.stream().map(NodeInfoHolder::new).collect(Collectors.toList()));
        temp.addAll(attributes.stream().map(NodeInfoHolder::new).collect(Collectors.toList()));
        temp.addAll(tokens.stream().map(NodeInfoHolder::new).collect(Collectors.toList()));
        return temp;
    }

    public ArrayList<NodeInfo> getAttributes(){ return attributes; }
    public ArrayList<NodeInfo> getNTAs(){ return NTAs; }
    public ArrayList<NodeInfo> gettokens(){ return tokens; }

}
