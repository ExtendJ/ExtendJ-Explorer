package jastaddad.objectinfo;

import jastaddad.ASTAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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

    private boolean addAttribute(Object obj, Method m, Annotation a){
        try{
            attributes.add(new Attribute(m.getName(), m.invoke(obj, m.getParameters()).toString(), ""));
        } catch (Throwable e) {
            attributes.add(new Attribute("EX: " + m.getName(), m.getParameterTypes().toString(), ""));
        }
        return true;
    }

    private boolean addToken(Object obj, Method m, Annotation a){
        try{
            tokens.add(new Token(m.getName(), m.invoke(obj, m.getParameters()).toString()));
        } catch (Throwable e) {
            tokens.add(new Token("EX: " + m.getName(), m.getParameterTypes().toString()));
        }
        return true;
    }
}
