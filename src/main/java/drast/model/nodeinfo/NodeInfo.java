package drast.model.nodeinfo;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 This Class is a holder for the terminal attributes.
 * Created by gda10jli on 10/20/15.
 */
public abstract class NodeInfo implements Comparable<NodeInfo>{
    protected Object value; //The value of the attribute
    protected Method method; //The method which is used to invoke the attribute
    protected String name;

    public NodeInfo(String name, Object value, Method method){
        this.name = name;
        this.value = value;
        this.method = method;
    }

    public Object getValue(){ return value; }

    public void setValue(Object value){ this.value = value; }

    public String getName(){ return name; }

    public Class getReturnType(){ return method.getReturnType(); }

    public Method getMethod(){ return method; }

    public boolean hasCachedValues(){ return false; }

    /**
     * Returns the kind of the method, i.e. syn, syn, etc.
     * @return
     */
    public abstract String getKind();

    /**
     * This will print the name of the attribute with its parameters
     * @return
     */
    public abstract String print();

    @Override
    public String toString(){ return print(); }

    @Override
    public int compareTo(NodeInfo o) {
        return name.compareTo(o.name);
    }

    /**
     * Check if a attribute is parametrized
     * @return
     */
    public abstract boolean isParametrized();

    /**
     * Check if a attribute is parametrized
     * @return
     */
    public abstract boolean isNTA();

    /**
     * Check if a attribute is parametrized
     * @return
     */
    public abstract boolean isAttribute();

    /**
     * This will add more type specific information to the List, used by getInfo()
     * @param al
     */
    protected abstract void setChildInfo(ArrayList<NodeInfoHolder> al);

    /**
     * Prints the name with its parameters, if the parameters are null the type of the parameters will then be added.
     * * MethodName(1,2) or MethodName(int, String)
     * @param m
     * @param params
     * @return
     */
    public static String getName(Method m, Object[] params){
        String name = m.getName() + "(";
        if (m.getParameterCount() > 0){
            for(int i = 0; i < m.getParameterCount(); i++) {
                name += params != null ? params[i].toString() : m.getParameterTypes()[i].getName();
                if(i + 1 < m.getParameterCount())
                    name += ", ";
            }
        }
        return name + ")";
    }

    /**
     * Creates a List of NodeInfoHolders, a more abstract holder for the NodeInfo types. The List will contain the information about the NodeInfo,
     *  i.e. name, value, return type, kind and more.
     * @return
     */
    public ArrayList<NodeInfoHolder> getInfo(Object value) {
        ArrayList<NodeInfoHolder> al = new ArrayList();
        al.add(new NodeInfoHolder("Name", name));
        al.add(new NodeInfoHolder("Value", isParametrized() ? value : this.value));
        al.add(new NodeInfoHolder("Return type", method.getReturnType().getName()));
        for (int i = 0; i < method.getParameterCount(); i++)
            al.add(new NodeInfoHolder("Parameter type: " + i, method.getParameterTypes()[i].getName()));
        al.add(new NodeInfoHolder("Is parametrized", isParametrized()));
        setChildInfo(al);
        return al;
    }

}
