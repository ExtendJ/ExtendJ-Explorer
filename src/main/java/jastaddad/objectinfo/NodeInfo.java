package jastaddad.objectinfo;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by gda10jli on 10/20/15.
 */
public abstract class NodeInfo implements Comparable<NodeInfo>{

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

    public Method getMethod(){ return method; }

    public abstract String print();

    @Override
    public String toString(){ return print(); }

    @Override
    public int compareTo(NodeInfo o) {
        return name.compareTo(o.name);
    }

    public abstract boolean isParametrized();
    protected abstract void setChildInfo(ArrayList<NodeInfoHolder> al);

    protected static String getName(Method m){
        String name = m.getName() + "(";
        if (m.getParameterCount() > 0){
            for(int i = 0; i < m.getParameterCount(); i++)
                name += m.getParameterTypes()[i] + (i + 1 < m.getParameterCount() ? ", " : "");
        }
        return name + ")";
    }

    public ArrayList<NodeInfoHolder> getInfo() {
        ArrayList<NodeInfoHolder> al = new ArrayList();
        al.add(new NodeInfoHolder("Name", name));
        al.add(new NodeInfoHolder("Value", value));
        al.add(new NodeInfoHolder("Return type", method.getReturnType()));
        al.add(new NodeInfoHolder("isParameterized", isParametrized()));
        for (int i = 0; i < method.getParameterCount(); i++)
            al.add(new NodeInfoHolder("Parameter type: " + i, method.getParameterTypes()[i]));
        setChildInfo(al);
        return al;
    }
}
