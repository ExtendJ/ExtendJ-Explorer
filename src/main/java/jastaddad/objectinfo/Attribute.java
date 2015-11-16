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
        al.add(new NodeInfoHolder("Kind", type));
    }

    public String getType(){ return type; }

}
