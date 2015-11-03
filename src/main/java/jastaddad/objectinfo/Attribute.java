package jastaddad.objectinfo;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by gda10jli on 10/20/15.
 */
public class Attribute extends NodeInfo {
    private String type;

    public Attribute(String name, Object value, Method m, String type)
    { super(name, value, m); this.type = type; }

    @Override
    public String print(){ return getName(method); }

    @Override
    protected void setChildInfo(ArrayList<NodeInfoHolder> al) {
        al.add(new NodeInfoHolder("Type", type));
    }

    public String getType(){ return type; }

}
