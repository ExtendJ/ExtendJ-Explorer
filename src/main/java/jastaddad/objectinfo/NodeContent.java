package jastaddad.objectinfo;

import jastaddad.ASTAnnotation;
import jastaddad.Node;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by gda10jli on 10/14/15.
 */
public class NodeContent {

    public static final String USER_INPUT = "Need input from user";

    private HashMap<String, NodeInfo> attributes;
    private HashMap<String, NodeInfo> tokens;
    private ArrayList<String> invokationErrors;

    public NodeContent(){
        attributes = new HashMap();
        tokens = new HashMap();
        invokationErrors = new ArrayList<>();
    }

    public boolean containsToken(String key){
        return tokens.containsKey(key);
    }

    public boolean containsAttribute(String key){
        return attributes.containsKey(key);
    }

    public boolean contains(String key){
        return attributes.containsKey(key) || tokens.containsKey(key);
    }

    public NodeInfo get(String key){
        NodeInfo ret = attributes.get(key);
        if(ret == null)
            ret = tokens.get(key);
        return ret;
    }

    public ArrayList<String> compute(Node node){
        invokationErrors.clear();
        attributes.clear();
        tokens.clear();
        add(node.node);
        return invokationErrors;
    }

    public ArrayList<NodeInfoHolder> toArray(){
        ArrayList<NodeInfoHolder> temp = new ArrayList<>();
        for (NodeInfo a : attributes.values())
            temp.add(new NodeInfoHolder(a.print(), a.getValue(), a));
        for (NodeInfo t : tokens.values())
            temp.add(new NodeInfoHolder(t.print(), t.getValue(), t));
        Collections.sort(temp);
        return temp;
    }

    public void add(Object obj){
        for(Method m : obj.getClass().getMethods()){
            add(obj, m);
        }
    }

    public void add(Object obj, Method m) {
        for(Annotation a : m.getAnnotations()) {
            add(obj, m, a);
        }
    }

    public boolean add(Object obj, Method m, Annotation a)  {
        if(ASTAnnotation.isAttribute(a)) {
            return addAttribute(obj, m);
        }
        if(ASTAnnotation.isToken(a))
            return addToken(obj, m);
        return false;
    }

    //Todo might move these methods to their specific classes
    private boolean addAttribute(Object obj, Method m){
        String name = m.getName();
        boolean parametrized = m.getParameterCount() > 0;
        try{
            if(parametrized)
                attributes.put(NodeInfo.getName(m), new Attribute(name, USER_INPUT, m, "", true));
            else
                attributes.put(NodeInfo.getName(m), new Attribute(name, m.invoke(obj, new Object[0]), m, ""));
        } catch (Throwable e) {
            addInvocationErrors(e);
            attributes.put(NodeInfo.getName(m), new Attribute(name, e.getCause().toString(), m, "", parametrized));
        }
        return true;
    }

    private boolean addToken(Object obj, Method m){
        String name = m.getName();
        try{
            tokens.put(NodeInfo.getName(m), new Token(name, m.invoke(obj, new Object[0]), m));
        } catch (Throwable e) {
            addInvocationErrors(e);
            tokens.put(NodeInfo.getName(m), new Token(name, e.getCause().toString(), m));
        }
        return true;
    }

    private void addInvocationErrors(Throwable e){
        invokationErrors.add(e.getCause().toString());
        //e.printStackTrace();
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
