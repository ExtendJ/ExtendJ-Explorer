package jastaddad.objectinfo;

import jastaddad.ASTAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gda10jli on 10/14/15.
 */
public class NodeContent {
    private ArrayList<NodeInfo> attributes;
    private ArrayList<NodeInfo> tokens;

    public NodeContent(){
        attributes = new ArrayList<>();
        tokens = new ArrayList<>();
    }

    public ArrayList<NodeInfo> getAttributes(){ return attributes; }

    public ArrayList<NodeInfo> getTokens(){ return tokens; }

    public ArrayList<NodeInfo> toArray(){
        ArrayList<NodeInfo> temp = new ArrayList<>();
        if(attributes != null)
            temp.addAll(attributes);
        if(tokens != null)
            temp.addAll(tokens);
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
        if(ASTAnnotation.isAttribute(a))
            return addAttribute(obj, m, a);
        if(ASTAnnotation.isToken(a))
            return addToken(obj, m, a);
        return false;
    }

    //Todo might move these methods to their specific classes
    private boolean addAttribute(Object obj, Method m, Annotation a){
        try{
            HashMap<Object, Object> map = getCachedMapValues(obj, m);
            if(map != null){
                for(Map.Entry<Object, Object> e : map.entrySet())
                    attributes.add(new Attribute(m.getName(), e.getKey() + " : " + e.getValue(), ""));
            }else
                attributes.add(new Attribute(m.getName(), m.invoke(obj, new Object[m.getParameterCount()]).toString(), ""));
        } catch (Throwable e) {
            //System.out.println(obj.getClass() + ": " + m.getName() + ":" + m.getParameterCount());
            //e.printStackTrace();
            attributes.add(new Attribute(getName(m),"Error message: " + e.getCause(), ""));
        }
        return true;
    }

    private boolean addToken(Object obj, Method m, Annotation a){
        try{
            HashMap<Object, Object> map = getCachedMapValues(obj, m);
            if(map != null){
                for(Map.Entry<Object, Object> e : map.entrySet())
                    tokens.add(new Token(m.getName(), e.getKey() + " : " + e.getValue()));
            }else
                tokens.add(new Token(m.getName(), m.invoke(obj, new Object[m.getParameterCount()]).toString()));
        } catch (Throwable e) {
           // e.printStackTrace();
            tokens.add(new Token(getName(m), "Error message: " + e.getCause()));
        }
        return true;
    }

    private String getName(Method m){ return m.getName() + (m.getParameterCount() > 0 ? "(" + m.getParameterCount()+")" : ""); }

    private HashMap<Object, Object> getCachedMapValues(Object obj, Method m) throws IllegalAccessException, InstantiationException {
        try {
            if(m.getParameterCount() != 1)
                return null;
            Field f = getField(obj.getClass(), m.getName(), "_values");
            if(f == null)
                return null;
            f.setAccessible(true);
            Object map = f.get(obj);
            if(map == null)
                return null;
            return (HashMap<Object, Object>) map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Field getField(Class clazz, String mName, String fieldName) throws NoSuchFieldException {
        for(Field f : clazz.getDeclaredFields()){
            if(f.getName().contains(mName+'_') && f.getName().endsWith(fieldName))
                return f;
        }
        Class superClass = clazz.getSuperclass();
        if (superClass == null)
           return null;
        else
            return getField(superClass, mName, fieldName);
    }

}
