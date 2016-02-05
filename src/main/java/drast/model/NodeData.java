package drast.model;

import drast.model.nodeinfo.Attribute;
import drast.model.nodeinfo.NodeInfo;
import drast.model.nodeinfo.Token;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * This class holds all information about the a node, all attribute values and tokens
 * It can invoke methods with the compute methods.
 * Created by gda10jli on 10/14/15.
 */
public class NodeData {

    private ArrayList<NodeInfo> attributes;
    private ArrayList<NodeInfo> tokens;
    private ArrayList<NodeInfo> NTAs;
    private HashMap<Method, HashMap<Object[], Object>> invokedValues;
    private Node node; //The node the content is a part of
    private Object nodeObject; //The node the content is a part of

    /**
     * Constructor for the NodeData, which will init the HashSet/HashMap
     * @param node
     */
    public NodeData(Node node){
        this.node = node;
        this.nodeObject = node.node;
        invokedValues = new HashMap<>();
    }

    public Collection<NodeInfo> getAttributes(){
        ArrayList temp = attributes;
        attributes = null;
        return temp;
    }

    public Collection<NodeInfo> getNTAs(){
        ArrayList temp = NTAs;
        NTAs = null;
        return temp;
    }

    public Collection<NodeInfo> getTokens(){
        ArrayList temp = tokens;
        tokens = null;
        return temp;
    }

    /**
     *
     * @param e
     */
    private void addInvocationErrors(ASTBrain api, Throwable e, Method m){
        String message = String.format("While computing %s in node %s. Cause : %s", m.getName(), nodeObject, e.getCause() != null ? e.getCause().toString() : e.getMessage());
        api.putError(AlertMessage.INVOCATION_ERROR, message);
        //e.printStackTrace();
    }

    /**
     * Computes the method in the NodeInfo, with the given parameters, and adds it to the cached list of the Attribute.
     * If the params == null and the method is not parametrized it will compute the method will 0 arguments, otherwise it will return null and add a error to the api.
     * @param nodeInfo
     * @param par
     * @return true if the invocation was successful.
     */
    protected Object compute(NodeInfo nodeInfo, Object[] par, ASTBrain api) {
        Object[] params = par;
        Method method = nodeInfo.getMethod();
        if ((par != null && par.length != method.getParameterCount()) || (par == null && method.getParameterCount() != 0)) {
            api.putError(AlertMessage.INVOCATION_ERROR, "Wrong number of arguments for the method: " + method);
            return null;
        }
        if(params == null)
            params = new Object[method.getParameterCount()];

        if(!nodeInfo.isAttribute()){
            api.putError(AlertMessage.INVOCATION_ERROR, "Can only do compute on attributes");
            return  null;
        }

        Attribute attribute = (Attribute) nodeInfo;
        if(attribute.containsValue(params))
            return attribute.getComputedValue(params);

        Object obj;
        try{
            obj = method.invoke(nodeObject, params);
        }catch(Throwable e){
            addInvocationErrors(api, e, attribute.getMethod());
            obj = e.getCause() != null ? e.getCause() : e.getMessage();
        }

        attribute.addComputedValue(params, obj);
        if(!invokedValues.containsKey(method))
            invokedValues.put(method, new HashMap<>());
        invokedValues.get(method).put(params, obj);
        return obj;
    }

    /**
     * Computes all methods of the NodeContents node, this will clear the old values except the invoked ones.
     * This is used for onDemand execution attributes values.
     * @return
     */
    protected void compute(ASTBrain api){
        if(node.isNull())
            return;
        attributes = new ArrayList<>();
        tokens = new ArrayList<>();
        NTAs = new ArrayList<>();
        for(Method m : nodeObject.getClass().getMethods()){
            NodeInfo info ;
            for (Annotation a : m.getAnnotations()) {
                if (ASTAnnotation.isAttribute(a)) {
                    info = computeAttribute(api, m, null);
                    if(info.isNTA())
                        NTAs.add(info);
                    else
                        attributes.add(info);
                    break;
                } else if (ASTAnnotation.isToken(a)) {
                    info = computeToken(api, m);
                    tokens.add(info);
                    break;
                }
            }
        }
    }

    protected Method getMethod(String methodName){
        try{
            return nodeObject.getClass().getMethod(methodName);
        } catch (NoSuchMethodException e) {}
        return null;
    }

    /**
     * Compute the attribute/token method with some given name.
     * If forceComputation is true it will compute the non-parametrized NTA:s
     * @param method
     * @return
     */
    protected Object computeMethod(String method){
        try{
            Method m = nodeObject.getClass().getMethod(method);
            if(m.getParameterCount() > 0)
                return null;
            return m.invoke(nodeObject);
        }
        catch (NoSuchMethodException e) {}
        catch (InvocationTargetException e) {}
        catch (IllegalAccessException e){}
        return null;
    }

    /**
     * Get the Token of the method in the obj.
     * @param m
     * @return
     */
    private Token computeToken(ASTBrain api, Method m){
        String name = m.getName();
        try{
            return new Token(name, m.invoke(nodeObject), m);
        } catch (Throwable e) {
            addInvocationErrors(api, e, m);
            return new Token(name, e.getCause().toString(), m);
        }
    }

    /**
     * Creates a Attribute and invokes the method with the supplied parameters, if any.
     * Will also add the specific information about the Attribute, which is derived form the annotations.
     * If forceComputation is true it will compute the non-parametrized NTA:s
     * @param m
     * @param params
     * @return
     */
    protected Attribute computeAttribute(ASTBrain api, Method m, Object[] params){
        Attribute attribute = new Attribute(m.getName(), null, m);
        attribute.setParametrized(m.getParameterCount() > 0);
        for(Annotation a : m.getAnnotations()) {
            if (ASTAnnotation.isAttribute(a)){
                attribute.setKind(ASTAnnotation.getKind(a));
                attribute.setCircular(ASTAnnotation.is(a, ASTAnnotation.AST_METHOD_CIRCULAR));
                attribute.setNTA(ASTAnnotation.is(a, ASTAnnotation.AST_METHOD_NTA));
            }else if(ASTAnnotation.isSource(a)){
                attribute.setAspect(ASTAnnotation.getString(a, ASTAnnotation.AST_METHOD_ASPECT));
                attribute.setDeclaredAt(ASTAnnotation.getString(a, ASTAnnotation.AST_METHOD_DECLARED_AT));
            }
        }
        if(invokedValues.containsKey(m)){
            for (Map.Entry<Object[], Object> e : invokedValues.get(m).entrySet()) {
                attribute.addComputedValue(e.getKey(), e.getValue());
            }
        }
        addCachedValues(api, m, attribute);
        if(api.getfilterConfig().getBoolean(FilterConfig.DYNAMIC_VALUES)){
            invokeValue(attribute, api, m, params);
        }
        return attribute;
    }

    private void invokeValue(Attribute attribute, ASTBrain api, Method m, Object[] params){
        try {
            if ((attribute.isParametrized() || attribute.isNTA())) {
                if(attribute.isNTA() && !attribute.isParametrized())
                    attribute.setValue(m.invoke(nodeObject));
            }else if(params != null && params.length == m.getParameterCount())
                attribute.setValue(m.invoke(nodeObject, params));
            else if(nodeObject != null)
                attribute.setValue(m.invoke(nodeObject));
        } catch (Throwable e) {
            addInvocationErrors(api, e, m);
            attribute.setValue(e.getCause());
        }
    }

    //HERE BE DRAGONS, this code is here for shits and giggles
    public void addCachedValues(ASTBrain api, Method m, Attribute attribute){
        if(attribute == null)
            return;
        Object obj = getFieldValue(getField(api, m, nodeObject.getClass()));
        if(attribute.isParametrized() && obj != null)
            setValues(attribute, obj, m);
        else
            attribute.setValue(obj);
    }

    protected Collection<Node> findCachedNTAs(ASTBrain api){
        HashMap<Object, Method> values = new HashMap<>();
        try {
            for(Map.Entry<Method, Object> e : findFieldNames(api).entrySet()){
                Attribute attr = computeAttribute(api, e.getKey(), null);
                setValues(attr, e.getValue(), e.getKey());
                if(attr.isParametrized()) {
                    for (Object obj : attr.getComputedValues())
                        values.put(obj, e.getKey());
                }else
                    values.put(attr.getValue(), e.getKey());
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

    private void setValues(Attribute attr, Object v, Method m){
        if (attr.isParametrized()) {
            if (v == null || !(v instanceof Map))
                return;
            Map map = (Map) v;
            for (Map.Entry par : (Set<Map.Entry>) map.entrySet()) {
                if (m.getParameterCount() > 1  && par.getKey() instanceof Collection)
                    attr.addComputedValue(((java.util.List) par.getKey()).toArray(), par.getValue());
                else
                    attr.addComputedValue(new Object[]{par.getKey()}, par.getValue());
            }
        } else
            attr.setValue(v);
    }

    private Field getField(ASTBrain api, Method method, Class clazz){
        if(api.getCachedField(method) != null)
            return api.getCachedField(method);
        String name = method.getName();
        if(method.getParameterCount() > 0) {
            for (Class par : method.getParameterTypes())
                name += "_" + par.getSimpleName();
            name += "_values";
        }else
            name += "_value";
        while(clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getName().contains(name)) {
                    api.putCachedField(method, field);
                    return field;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    private HashMap<Method, Object> findFieldNames(ASTBrain api){
        ArrayList<Method> methods = api.getNTAMethods(nodeObject.getClass());
        HashMap<Method, Object> values = new HashMap<>();
        if(methods == null || methods.size() == 0)
            return values;
        for(Method m : methods){
            Field f = api.getCachedField(m);
            if(f == null)
                f = getField(api, m, nodeObject.getClass());
            Object value = getFieldValue(f);
            if(value != null)
                values.put(m, value);
        }
        return values;
    }

    private Object getFieldValue(Field field){
        if(field == null)
            return null;
        try {
            field.setAccessible(true);
            return field.get(nodeObject);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
