package jastaddad.objectinfo;

import java.lang.reflect.Method;

/**
 * Created by gda10jli on 10/20/15.
 */
public class Attribute extends NodeInfo {
    private String type;

    public Attribute(String name, Object value, Method m, String type)
    { super(name, value, m); this.type = type; }

    @Override
    public String print(){ return name; }

    public String getType(){ return type; }

}
