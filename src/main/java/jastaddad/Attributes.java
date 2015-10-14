package jastaddad;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by gda10jli on 10/14/15.
 */
public class Attributes {
    private ArrayList<String> attributes;

    public Attributes(){
        attributes = new ArrayList<>();
    }

    public void add(Object root) throws InvocationTargetException, IllegalAccessException {
        for(Method m : root.getClass().getMethods()){
            add(m);
        }
    }

    public void add(Method m) throws InvocationTargetException, IllegalAccessException {
        for(Annotation a : m.getAnnotations()) {
            add(m, a);
        }
    }

    public void add(Method m, Annotation a) throws InvocationTargetException, IllegalAccessException {
        if(ASTAnnotation.isAttribute(a)) {
            System.out.println(m.getName());
            attributes.add(m.getClass().getCanonicalName());
        }
    }
}
