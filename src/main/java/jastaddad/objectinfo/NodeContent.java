package jastaddad.objectinfo;

import jastaddad.ASTAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;

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
            attributes.add(new Attribute(m.getName(), m.invoke(obj, new Object[m.getParameterCount()]).toString(), ""));
        } catch (Throwable e) {
            //e.printStackTrace();
            attributes.add(new Attribute(m.getName(),"Error message: " + e.getCause(), ""));
        }
        return true;
    }

    private boolean addToken(Object obj, Method m, Annotation a){
        try{
            tokens.add(new Token(m.getName(), m.invoke(obj, new Object[m.getParameterCount()]).toString()));
        } catch (Throwable e) {
            //e.printStackTrace();
            tokens.add(new Token(m.getName(), "Error message: " + e.getCause()));
        }
        return true;
    }

    private Object [] getParameters(Method m) throws IllegalAccessException, InstantiationException { //Todo check validity of this code, note sure if this is the right approach
        Object[] params = new Object[m.getParameterCount()];
        Class<?>[] types = m.getParameterTypes();
        for(int i = 0; i < params.length; i++){
            System.out.println(types[i]);
            if(types[i] == byte.class || types[i] == Byte.class)
                params[i] = new byte[0];
            else if(types[i] == short.class || types[i] == Short.class)
                params[i] = new short[0];
            else if(types[i] == int.class || types[i] == Integer.class)
                params[i] = new Integer(0);
            else if(types[i] == double.class || types[i] == Double.class)
                params[i] = new Double(0);
            else if(types[i] == long.class || types[i] == Long.class)
                params[i] = new Long(0);
            else if(types[i] == float.class || types[i] == Float.class)
                params[i] = new Float(0);
            else if(types[i] == char.class || types[i] == Character.class)
                params[i] = new char[0];
            else if(types[i] == boolean.class || types[i] == Boolean.class)
                params[i] = new Boolean(false);
            else
                params[i] = m.getParameterTypes()[i].newInstance();
        }
        return params;
    }

}
