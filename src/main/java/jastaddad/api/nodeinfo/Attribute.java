package jastaddad.api.nodeinfo;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Class that holds the terminal Attributes
 * Created by gda10jli on 10/20/15.
 */
public class Attribute extends NodeInfo {
    private boolean parametrized;
    private String kind; // the kind of the attribute, i.e. inh, syn..

    public Attribute(String name, Object value, Method m, String kind) {
        super(name, value, m); this.kind = kind;
    }

    public Attribute(String name, Object value, Method m, String kind, boolean parametrized) {
        super(name, value, m); this.kind = kind; this.parametrized = parametrized;
    }

    @Override
    public String print(){ return getName(method); }

    @Override
    public boolean isParametrized() { return parametrized; }

    @Override
    protected void setChildInfo(ArrayList<NodeInfoHolder> al) {
        al.add(new NodeInfoHolder("Kind", kind));
    }

    /**
     * Returns the kind of the method, i.e. syn, syn, etc.
     * @return
     */
    public String getKind(){ return kind; }

}
