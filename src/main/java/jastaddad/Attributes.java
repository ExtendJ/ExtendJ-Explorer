package jastaddad;

import javax.smartcardio.ATR;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by gda10jli on 10/14/15.
 */
public class Attributes {
    private ArrayList<Attribute> attributes;

    public Attributes(){
        attributes = new ArrayList<>();
    }

    public ArrayList<Attribute> getAttributes(){ return attributes; }

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

    public void add(Object obj, Method m, Annotation a)  {
        if(!ASTAnnotation.isAttribute(a))
            return;
        try{
            System.out.println(m.getName() + " : " + m.invoke(obj, new Object[m.getParameterCount()]));
            attributes.add(new Attribute(m.getName(), m.invoke(obj, new Object[m.getParameterCount()]) + ""));
        } catch (Throwable e) {
           // e.printStackTrace();
        }
    }

    private class Attribute{
        private String value;
        private String name;

        public Attribute(String name, String value){
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString(){
            return name + " : " + value;
         }
    }
}
