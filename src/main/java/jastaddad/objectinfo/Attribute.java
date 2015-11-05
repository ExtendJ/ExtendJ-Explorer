package jastaddad.objectinfo;

import jastaddad.Node;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by gda10jli on 10/20/15.
 */
public class Attribute extends NodeInfo {
    private boolean parametrized;
    private String type;
    private ArrayList<InvokedValue> hashedValues;

    public Attribute(String name, Object value, Method m, String type)
    { super(name, value, m); this.type = type; }

    public Attribute(String name, Object value, Method m, String type, boolean parametrized)
    { super(name, value, m); this.type = type; this.parametrized = parametrized; }

    @Override
    public String print(){ return getName(method); }

    @Override
    public boolean isParametrized() { return parametrized; }

    @Override
    protected void setChildInfo(ArrayList<NodeInfoHolder> al) {
        al.add(new NodeInfoHolder("Type", type));
        if(hashedValues == null)
            return;
        for(InvokedValue val: hashedValues)
            al.add(new NodeInfoHolder(val.print(), val.value, val.attr));
    }

    public String getType(){ return type; }

    public Object invokeMethod(Node node, ArrayList<Object> par){
        if(!parametrized)
            return null;
        try{
            Object[] params = new Object[method.getParameterCount()];
            Object obj;
            for(int i = 0; i < method.getParameterCount(); i++){
                obj = par.get(i);
                Class c = method.getParameterTypes()[i];
                if(obj == null)
                    return null;
                obj = getParam(obj, c);
                if(obj == null)
                    return null;
                params[i] = obj;
            }
            obj = method.invoke(node.node, params);
            if(obj == null)
                return null;
            addValue(obj, par);
            return obj;
         }catch(Throwable e){
            addValue(e.getMessage(), par);
            return null;
        }
    }

    private void addValue(Object obj, ArrayList<Object> par){
        if(hashedValues == null)
            hashedValues = new ArrayList();
        hashedValues.add(new InvokedValue(obj, par, this));
    }

    private Object getParam(Object obj, Class c){ //Todo expand this shit, and do something smarter with the parameters
        if(c == int.class || c == Integer.class)
            return new Integer(Integer.parseInt(obj.toString()));
        else if(c == String.class)
            return obj;
        else if(c == boolean.class || c == Boolean.class)
            return Boolean.parseBoolean(obj.toString());
        return null;
    }

    private class InvokedValue{
        private Object value;
        private ArrayList<Object> params;
        private Attribute attr;

        public InvokedValue(Object value, ArrayList<Object> params, Attribute attr){
            this.value = value;
            this.params = params;
            this.attr = attr;
        }

        private String print(){
            return "Invoked value, " + params;
        }
    }
}
