package jastaddad.objectinfo;

import java.lang.reflect.Method;

/**
 * Created by gda10jli on 10/20/15.
 */
public abstract class NodeInfo implements Comparable<NodeInfo> {

    protected Object value;
    protected Method method;
    protected String name;

    public NodeInfo(String name, Object value, Method method){
        this.name = name;
        this.value = value;
        this.method = method;
    }

    public Object getValue(){ return value; }

    public Class getReturnType(){ return method.getReturnType(); }

    public abstract String print();

    @Override
    public String toString(){ return print(); }

    @Override
    public int compareTo(NodeInfo o) {
        return name.compareTo(o.name);
    }
}
