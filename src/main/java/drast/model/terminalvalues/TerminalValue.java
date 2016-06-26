package drast.model.terminalvalues;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 This Class is a holder for the terminal attributes.
 * Created by gda10jli on 10/20/15.
 */
public abstract class TerminalValue implements Comparable<TerminalValue>{
    protected Object value; //The value of the attribute
    protected Method method; //The method which is used to invoke the attribute
    protected String name;
    boolean evaluated;

    public TerminalValue(String name, Object value, Method method, boolean evaluated){
        this.name = name;
        this.value = value;
        this.method = method;
        this.evaluated = evaluated;
    }

    public Object getValue(){ return value; }

    public void setValue(Object value){ this.value = value; }

    public String getName(){ return name; }

    public Class getReturnType(){ return method.getReturnType(); }

    public Method getMethod(){ return method; }

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
    public int compareTo(TerminalValue o) {
        return name.compareTo(o.name);
    }

    /**
     * Returns true or false if the this TerminalValue is computed or not
     */
    public boolean isEvaluated() { return evaluated; }
    public void setEvaluated(boolean evaluated) { this.evaluated = evaluated; }

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
    protected abstract void setChildInfo(ArrayList<TerminalValueInfo> al);

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
    public ArrayList<TerminalValueInfo> getInfo(Object value) {
        ArrayList<TerminalValueInfo> al = new ArrayList();
        al.add(new TerminalValueInfo("Name", name));
        al.add(new TerminalValueInfo("Value", isParametrized() ? value : this.value));
        al.add(new TerminalValueInfo("Return type", method.getReturnType().getName()));
        for (int i = 0; i < method.getParameterCount(); i++)
            al.add(new TerminalValueInfo("Parameter type: " + i, method.getParameterTypes()[i].getName()));
        al.add(new TerminalValueInfo("Is parametrized", isParametrized()));
        setChildInfo(al);
        return al;
    }

}
