package drast.model.nodeinfo;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * The class that holds the terminal Token attribute.
 * Created by gda10jli on 10/20/15.
 */
public class Token extends NodeInfo {

    public Token(String name, Object value, Method m){ super(name, value, m);}

    @Override
    public String print(){ return getName(method, null); }

    @Override
    protected void setChildInfo(ArrayList<NodeInfoHolder> al) { }

    @Override
    public boolean isParametrized() { return false; }

    public boolean isNTA() { return false; }

    /**
     * Check if a attribute is parametrized
     * @return
     */
    public boolean isAttribute(){ return false; }

    @Override
    public String getKind() { return null; }

}