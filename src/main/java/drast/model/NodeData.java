package drast.model;

import drast.model.nodeinfo.Attribute;
import drast.model.nodeinfo.NodeInfo;
import drast.model.nodeinfo.NodeInfoHolder;
import drast.model.nodeinfo.Token;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class holds all information about the a node, all attribute values and tokens
 * It can invoke methods with the compute methods.
 * Created by gda10jli on 10/14/15.
 */
public class NodeData {

    private HashMap<Method, NodeInfo> attributes;
    private HashMap<Method,NodeInfo> tokens;
    private HashMap<Method,NodeInfo> NTAs;
    private HashMap<Method, NodeInfo> computedMethods;
    private Node node; //The node the content is a part of
    private Object nodeObject; //The node the content is a part of
    private boolean hasComputedCachedNTAS = false;

    /**
     * Constructor for the NodeData, which will init the HashSet/HashMap
     * @param node
     */
    public NodeData(Node node){
        attributes = new HashMap<>();
        tokens = new HashMap<>();
        NTAs = new HashMap<>();
        computedMethods = new HashMap<>();
        this.node = node;
        this.nodeObject = node.node;
    }

    protected NodeInfo getComputed(Method method){
        return computedMethods.get(method);
    }


    /**
     * Computes the method in the NodeInfo, with the given parameters, and adds it to the cached list of the Attribute.
     * If the params == null and the method is not parametrized it will compute the method will 0 arguments, otherwise it will return null and add a error to the api.
     * @param nodeInfo
     * @param par
     * @return true if the invocation was successful.
     */
    protected Object compute(NodeInfo nodeInfo, Object[] par, ASTBrain api) {
        Object[] params;
        Method method = nodeInfo.getMethod();
        if ((par != null && par.length != method.getParameterCount()) || (par == null && method.getParameterCount() != 0)) {
            api.putError(AlertMessage.INVOCATION_ERROR, "Wrong number of arguments for the method: " + method);
            return null;
        }
        if(par == null)
            params = new Object[method.getParameterCount()];
        else
            params = par;
        if(!nodeInfo.isAttribute()){
            api.putError(AlertMessage.INVOCATION_ERROR, "Can only do compute on attributes");
            return  null;
        }

        Attribute attribute = (Attribute) nodeInfo;
        if(attribute.containsValue(params))
            return attribute.getComputedValue(params);

        try{
            Object obj =  method.invoke(nodeObject, params);
            attribute.addComputedValue(params, obj);
            return obj;
        }catch(Throwable e){
            addInvocationErrors(api, e, attribute.getMethod());
            Object message = e.getCause() != null ? e.getCause() : e.getMessage();
            attribute.addComputedValue(params, message);
            return message;
        }
    }

    /**
     * Computes all methods of the NodeContents node, this will clear the old values except the invoked ones.
     * This is used for onDemand execution attributes values.
     * If forceComputation is true it will compute the non-parametrized NTA:s
     * @return
     */
    protected void compute(ASTBrain api, boolean reComputeNode, boolean forceComputation){
        if(reComputeNode){
            NTAs.clear();
            attributes.clear();
            tokens.clear();
            computedMethods.clear();
        }
        compute(api, nodeObject, forceComputation);
    }

    /**
     * Computes all methods of the given object, the values will be added to NodeContents value Lists.
     * NOTE: it will only compute the methods with annotations of the ASTAnnotation type.
     * If forceComputation is true it will compute the non-parametrized NTA:s
     * @param obj
     */
    protected void compute(ASTBrain api, Object obj, boolean forceComputation){
        if(node.isNull())
            return;
        for(Method m : obj.getClass().getMethods()){
            compute(api, m, null, forceComputation);
        }
    }

    /**
     * Compute the attribute/token method with some given name.
     * If forceComputation is true it will compute the non-parametrized NTA:s
     * @param method
     * @return
     */
    protected NodeInfo computeMethod(ASTBrain api, String method){
        return computeMethod(api, method, false);
    }

    /**
     * Compute the attribute/token method with some given name.
     * If forceComputation is true it will compute the non-parametrized NTA:s
     * @param method
     * @return
     */
    protected NodeInfo computeMethod(ASTBrain api, String method, boolean forceComputation){
        try{
            Method m = nodeObject.getClass().getMethod(method);
            return compute(api, m, null, forceComputation);
        }  catch (NoSuchMethodException e) {
            //api.putError(ASTAPI.INVOCATION_ERROR, "No such Method : " + e.getCause());
        }
        return null;
    }

    /**
     * Computes the given method, and depending on the parameter @param add it to the list with the computed Attributes.
     * If forceComputation is true it will compute the non-parametrized NTA:s
     * @param m
     * @return
     */
    protected NodeInfo compute(ASTBrain api, Method m, Object[] params, boolean forceComputation){
        if(computedMethods.containsKey(m) && !forceComputation) {
            NodeInfo info = computedMethods.get(m);
            if(info != null && info.isAttribute())
                invokeAndSetValue((Attribute)info, api, node.node, m, params, forceComputation);
            return info;
        }
        NodeInfo info = null;
        for (Annotation a : m.getAnnotations()) {
            if (ASTAnnotation.isAttribute(a)) {
                info = computeAttribute(api, nodeObject, m, params, forceComputation);
                if(info.isNTA())
                    NTAs.put(m, info);
                else
                    attributes.put(m ,info);
                break;
            } else if (ASTAnnotation.isToken(a)) {
                info = computeToken(api, nodeObject, m);
                tokens.put(m, info);
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
    protected Attribute computeAttribute(ASTBrain api, Object obj, Method m, Object[] params, boolean forceComputation){
        Attribute attribute = new Attribute(m.getName(), null, m);
        attribute.setParametrized(m.getParameterCount() > 0);
        for(Annotation a : m.getAnnotations()) { //To many attribute specific methods so I decided to iterate through the Annotations again instead of sending them as parameters.
            if (ASTAnnotation.isAttribute(a)){
                attribute.setKind(ASTAnnotation.getKind(a));
                attribute.setCircular(ASTAnnotation.is(a, ASTAnnotation.AST_METHOD_CIRCULAR));
                attribute.setNTA(ASTAnnotation.is(a, ASTAnnotation.AST_METHOD_NTA));
            }else if(ASTAnnotation.isSource(a)){
                attribute.setAspect(ASTAnnotation.getString(a, ASTAnnotation.AST_METHOD_ASPECT));
                attribute.setDeclaredAt(ASTAnnotation.getString(a, ASTAnnotation.AST_METHOD_DECLARED_AT));
            }
        }
        invokeAndSetValue(attribute, api, obj, m, params, forceComputation);
        return attribute;
    }

    private void invokeAndSetValue(Attribute attribute, ASTBrain api, Object obj, Method m, Object[] params, boolean forceComputation){
        try {
            if ((attribute.isParametrized() || attribute.isNTA())) {
                if(attribute.isNTA() && !attribute.isParametrized() && forceComputation)
                    attribute.setValue(m.invoke(obj));
            }else if(params != null && params.length == m.getParameterCount())
                attribute.setValue(m.invoke(obj, params));
            else if(obj != null)
                attribute.setValue(m.invoke(obj));
        } catch (Throwable e) {
            addInvocationErrors(api, e, m);
            attribute.setValue(e.getCause());
        }
        if(api.getfilterConfig().getBoolean(Config.CACHED_VALUES)) {
            addCachedValues(api, m, attribute, false);
        }
    }

    /**
     * Get the Token of the method in the obj.
     * @param obj
     * @param m
     * @return
     */
    private Token computeToken(ASTBrain api, Object obj, Method m){
        String name = m.getName();
        try{
            return new Token(name, m.invoke(obj), m);
        } catch (Throwable e) {
            addInvocationErrors(api, e, m);
            return new Token(name, e.getCause().toString(), m);
        }
    }

    /**
     *
     * @param e
     */
    private void addInvocationErrors(ASTBrain api, Throwable e, Method m){
        String message = String.format("While computing %s in node %s. Cause : %s", m.getName(), node.node, e.getCause() != null ? e.getCause().toString() : e.getMessage());
        api.putError(AlertMessage.INVOCATION_ERROR, message);
        //e.printStackTrace();
    }

    /**
     * Creates a list of all attributes, tokens and invokedValues
     * @return
     */
    protected ArrayList<NodeInfoHolder> toArray(){
        ArrayList<NodeInfoHolder> temp = new ArrayList<>();
        temp.addAll(NTAs.values().stream().map(NodeInfoHolder::new).collect(Collectors.toList()));
        temp.addAll(attributes.values().stream().map(NodeInfoHolder::new).collect(Collectors.toList()));
        temp.addAll(tokens.values().stream().map(NodeInfoHolder::new).collect(Collectors.toList()));
        return temp;
    }

    public Collection<NodeInfo> getAttributes(){ return attributes.values(); }
    public Collection<NodeInfo> getNTAs(){ return NTAs.values(); }
    public Collection<NodeInfo> getTokens(){ return tokens.values(); }

    //HERE BE DRAGONS, this cod is here for shits and giggles
    public void addCachedValues(ASTBrain api, Method m, Attribute attribute, boolean force){
        if(attribute == null || (attribute.isNTA() && !force))
            return;
        Object obj = getFieldValue(node.node, getField(api, m, node.node.getClass()));
        if(attribute.isParametrized() && obj != null)
            setValues(attribute, obj, m);
        else if(attribute.isNTA() && obj != null)
            attribute.setValue(obj);
    }

    protected Collection<Node> findCachedNTAs(ASTBrain api){
        HashMap<Object, Method> values = new HashMap<>();
        try {
            for(Map.Entry<Method, Object> e : findFieldNames(api).entrySet()){
                Attribute attri = (Attribute) computedMethods.get(e.getKey());
                if(attri == null)
                    attri = (Attribute) compute(api, e.getKey(), null, false);
                setValues(attri, e.getValue(), e.getKey());
                if(attri.isParametrized()) {
                    for (Object obj : attri.getComputedValues())
                        values.put(obj, e.getKey());
                }else
                    values.put(attri.getValue(), e.getKey());
            }
        }catch (ClassCastException e){
            api.putError(AlertMessage.INVOCATION_ERROR, e.getMessage());
        }
        ArrayList<Node> nodes = new ArrayList<>();
        for (Map.Entry<Object, Method> e : values.entrySet()) {
            Object obj = e.getKey();
            if (node.NTAChildren.containsKey(obj))
                nodes.add(node.NTAChildren.get(obj));
            else {
                Node temp = Node.getNTANode(obj, node, api);
                node.NTAChildren.put(obj, temp);
                nodes.add(temp);
            }
        }
        return nodes;
    }

    private void setValues(Attribute attri, Object v, Method m){
        if (attri.isParametrized()) {
            if (v == null || !(v instanceof Map))
                return;
            Map map = (Map) v;
            for (Map.Entry par : (Set<Map.Entry>) map.entrySet()) {
                if (m.getParameterCount() > 1  && par.getKey() instanceof Collection)
                    attri.addComputedValue(((java.util.List) par.getKey()).toArray(), par.getValue());
                else
                    attri.addComputedValue(new Object[]{par.getKey()}, par.getValue());
            }
        } else
            attri.setValue(v);
    }

    private Field getField(ASTBrain api, Method method, Class clazz){
        if(clazz == null)
            return null;
        if(api.getCachedField(method) != null)
            return api.getCachedField(method);
        while(clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getName().contains(method.getName() + "_") && field.getName().contains("_value")) {
                    api.putCachedField(method, field);
                    return field;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    private HashMap<Method, Object> findFieldNames(ASTBrain api){
        ArrayList<Method> methods = api.getNTAMethods(node.node.getClass());
        HashMap<Method, Object> values = new HashMap<>();
        if(methods == null || methods.size() == 0)
            return values;
        for(Method m : methods){
            Field f;
            if(api.getCachedField(m) != null)
                f = api.getCachedField(m);
            else
                f = getField(api, m, node.node.getClass());
            Object value = getFieldValue(node.node, f);
            if(value != null)
                values.put(m, value);
        }
        return values;
    }

    private Object getFieldValue(Object obj, Field field){
        if(obj == null || field == null)
            return null;
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
