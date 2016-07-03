package drast.model.reflection;

import drast.model.*;
import drast.model.terminalvalues.Attribute;
import drast.model.terminalvalues.TerminalValue;
import drast.model.terminalvalues.Token;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by gda10jth on 2/18/16.
 */
public class ReflectionNodeData implements NodeData {

    private ArrayList<TerminalValue> attributes;
    private ArrayList<TerminalValue> tokens;
    private ArrayList<TerminalValue> NTAs;
    private HashMap<Method, HashMap<Object[], Object>> invokedValues;
    private Node node; //The node the content is a part of
    private Object ASTObject; //The node the content is a part of
    private HashMap<Method, Pair<Field, Field>> cachedMethodFields;

    /**
     * Constructor for the NodeData, which will init the HashSet/HashMap
     * @param node
     */
    public ReflectionNodeData(Node node){
        this.node = node;
        this.ASTObject = node.getASTObject();
        invokedValues = new HashMap<>();
        if(cachedMethodFields == null)
            cachedMethodFields = new HashMap<>();
    }

    public Collection<TerminalValue> getAttributes(){
        ArrayList<TerminalValue> temp = attributes;
        attributes = null;
        return temp;
    }

    public Collection<TerminalValue> getNTAs(){
        ArrayList<TerminalValue> temp = NTAs;
        NTAs = null;
        return temp;
    }

    public Collection<TerminalValue> getTokens(){
        ArrayList<TerminalValue> temp = tokens;
        tokens = null;
        return temp;
    }

    /**
     *
     * @param e
     */
    private void addInvocationErrors(ASTBrain api, Throwable e, Method m){
        String message = String.format("Error when computing %s in node %s. Cause : %s", m.getName(), ASTObject, e.getCause() != null ? e.getCause().toString() : e.getMessage());
        api.putMessage(AlertMessage.INVOCATION_ERROR, message);
        //e.printStackTrace();
    }

    /**
     * Computes the method in the NodeInfo, with the given parameters, and adds it to the cached list of the Attribute.
     * If the params == null and the method is not parametrized it will compute the method will 0 arguments, otherwise it will return null and add a error to the api.
     * @param terminalValue
     * @param par
     * @return true if the invocation was successful.
     */
    @Override
    public Object compute(TerminalValue terminalValue, Object[] par, ASTBrain api) {
        Object[] params = par;
        Method method = terminalValue.getMethod();
        if ((par != null && par.length != method.getParameterCount()) || (par == null && method.getParameterCount() != 0)) {
            api.putMessage(AlertMessage.INVOCATION_ERROR, "Wrong number of arguments for the method: " + method);
            return null;
        }
        if(params == null)
            params = new Object[method.getParameterCount()];

        if(!terminalValue.isAttribute()){
            api.putMessage(AlertMessage.INVOCATION_ERROR, "Can only do compute on attributes");
            return  null;
        }

        Attribute attribute = (Attribute) terminalValue;
        if(attribute.containsValue(params))
            return attribute.getComputedValue(params);

        Object obj;
        try{
            obj = method.invoke(ASTObject, params);
        }catch(Throwable e){
            addInvocationErrors(api, e, attribute.getMethod());
            obj = e.getCause() != null ? e.getCause() : e.getMessage();
        }
        attribute.setEvaluated(true);
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
    @Override
    public void compute(ASTBrain api){
        if(node.isNullNode())
            return;
        attributes = new ArrayList<>();
        tokens = new ArrayList<>();
        NTAs = new ArrayList<>();
        for(Method m : ASTObject.getClass().getMethods()){
            TerminalValue info ;
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

    @Override
    public Method getMethod(String methodName){
        try{
            return ASTObject.getClass().getMethod(methodName);
        } catch (NoSuchMethodException e) {}
        return null;
    }

    /**
     * Compute the attribute/token method with some given name.
     * If forceComputation is true it will compute the non-parametrized NTA:s
     * @param method
     * @return
     */
    @Override
    public Object computeMethod(String method){
        try{
            Method m = ASTObject.getClass().getMethod(method);
            if(m.getParameterCount() > 0)
                return null;
            return m.invoke(ASTObject);
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
            return new Token(name, m.invoke(ASTObject), m);
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
    @Override
    public Attribute computeAttribute(ASTBrain api, Method m, Object[] params){
        Attribute attribute = new Attribute(m.getName(), m);
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
        addCachedValues(m, attribute);
        if(api.getConfig().isEnabled(Config.DYNAMIC_VALUES)){
            invokeValue(attribute, api, m, params);
        }
        return attribute;
    }


    private void invokeValue(Attribute attribute, ASTBrain api, Method m, Object[] params){
        try {
            if (attribute.isParametrized() && params != null && params.length == m.getParameterCount())
                attribute.setValue(m.invoke(ASTObject, params));
            else if(!attribute.isParametrized() && ASTObject != null)
                attribute.setValue(m.invoke(ASTObject));
        } catch (Throwable e) {
            addInvocationErrors(api, e, m);
            attribute.setValue(e.getCause());
        }
    }

    @Override
    public void addCachedValues(Method m, Attribute attribute){
        if(attribute == null)
            return;
        Pair<Field, Field> fieldPair = getCacheFields(m, ASTObject.getClass());

        attribute.setEvaluated(isComputed(fieldPair.getSecond()));
        if(!attribute.isEvaluated() && !attribute.isParametrized())
            return;

        Object value = getFieldValue(fieldPair.getFirst());
        if (attribute.isParametrized()) {
            if (value == null || !(value instanceof Map))
                return;
            Map map = (Map) value;
            for (Map.Entry par : (Set<Map.Entry>) map.entrySet()) {
                if (m.getParameterCount() > 1  && par.getKey() instanceof Collection)
                    attribute.addComputedValue(((java.util.List) par.getKey()).toArray(), par.getValue());
                else
                    attribute.addComputedValue(new Object[]{par.getKey()}, par.getValue());
            }
        } else
            attribute.setValue(value);
    }

    @Override
    public void setCachedNTAs(ASTBrain api){

        HashMap<Object, Method> values = new HashMap<>();
        try {
            for(Map.Entry<Method, Object> e : findFieldNames().entrySet()){
                Method m = e.getKey();
                Object obj = e.getValue();
                if(m.getParameterCount() > 0) {
                    if (obj == null || !(obj instanceof Map))
                        continue;
                    Map map = (Map) obj;
                    for (Map.Entry par : (Set<Map.Entry>) map.entrySet())
                        values.put(par.getValue(), m);
                }else
                    values.put(obj, m);
            }
        }catch (ClassCastException e){
            api.putMessage(AlertMessage.INVOCATION_ERROR, e.getMessage());
        }
        for (Map.Entry<Object, Method> e : values.entrySet()) {
            ReflectionNode.createNTATree(node, e.getKey(), api);
        }
    }

    /** Finds the "correct" field value for the attribute.
     * Lets be clear on this, this method is not safe. It can find wrong field because we are shooting in the dark here.
     * This is due to that we are guessing the name of the field, this can be solved by an annotation that contain the name
     * @param method
     * @param clazz
     * @return
     */
    private Pair<Field, Field> getCacheFields(Method method, Class clazz){
        if(cachedMethodFields.get(method) != null) {
            return cachedMethodFields.get(method);
        }
        String name = ASTAnnotation.getMethodCachedField(method);
        String computed = ASTAnnotation.getMethodComputedField(method);
        Pair<Field, Field> fields = new Pair<>();
        while(clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if(fields.isComplete())
                    break;
                String filedName = field.getName();
                if (filedName.contains(name))
                    fields.putFirst(field);
                else if(filedName.contains(computed))
                    fields.putSecond(field);
            }
            clazz = clazz.getSuperclass();
        }
        cachedMethodFields.put(method, fields);
        return fields;
    }

    private HashMap<Method, Object> findFieldNames(){
        ArrayList<Method> methods = node.getNTAMethods(node.getASTClass());
        HashMap<Method, Object> values = new HashMap<>();
        if(methods == null || methods.size() == 0)
            return values;
        for(Method m : methods){
            Pair<Field, Field> fieldPair = getCacheFields(m, ASTObject.getClass());
            Field field = fieldPair.getSecond();
            if (!isComputed(field) && m.getParameterCount() == 0)
                continue;
            field = getCacheFields(m, ASTObject.getClass()).getFirst();
            Object value = getFieldValue(field);
            if(value != null)
                values.put(m, value);
        }
        return values;
    }

    private boolean isComputed(Field field){
        if (field == null)
            return false;
        Object obj = getFieldValue(field);
        if(obj != null && (obj.getClass() == Boolean.class || obj.getClass() == boolean.class))
            return (boolean) obj;
        return obj != null;
    }

    private Object getFieldValue(Field field){
        if(field == null)
            return null;
        try {
            field.setAccessible(true);
            return field.get(ASTObject);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class Pair<T, E> {
        private T first;
        private E sec;

        public Pair() {}

        public T getFirst() { return first; }
        public E getSecond() { return sec; }

        public void putFirst(T first) { this.first = first; }
        public void putSecond(E sec) { this.sec = sec; }

        public boolean isComplete(){ return first != null && sec != null; }
    }


}
