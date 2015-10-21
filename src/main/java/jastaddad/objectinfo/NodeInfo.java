package jastaddad.objectinfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by gda10jli on 10/20/15.
 */
public abstract class NodeInfo {

    protected String value;
    protected String name;

    public NodeInfo(String name, String value){
        this.name = name;
        this.value = value;
    }

    public abstract String print();

    @Override
    public String toString(){ return print(); }


}
