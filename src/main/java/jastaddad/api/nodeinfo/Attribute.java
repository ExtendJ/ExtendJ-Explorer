package jastaddad.api.nodeinfo;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Class that holds the terminal Attributes
 * Created by gda10jli on 10/20/15.
 */
public class Attribute extends NodeInfo {
    private boolean parametrized;
    private Object kind; // the kind of the attribute, i.e. inh, syn..
    private String aspect;
    private String declaredAt;
    private boolean isCircular;
    private boolean isNTA;

    public Attribute(String name, Object value, Method m) {
        super(name, value, m);
    }

    public Attribute(String name, Object value, Method m, String kind) {
        super(name, value, m); this.kind = kind;
    }

    @Override
    protected void setChildInfo(ArrayList<NodeInfoHolder> al) {
        al.add(new NodeInfoHolder("Is Circular", isCircular));
        al.add(new NodeInfoHolder("Is NTA", isNTA));
        al.add(new NodeInfoHolder("Kind", kind));
        al.add(new NodeInfoHolder("Aspect", aspect));
        al.add(new NodeInfoHolder("Declared at", declaredAt));
    }

    @Override
    public String print(){ return getName(method, null); }

    @Override
    public boolean isParametrized() { return parametrized; }
    public void setParametrized(boolean parametrized) { this.parametrized = parametrized; }

    /**
     * Returns if the Attribute isCircular
     * @return
     */
    public boolean isCircular() { return isCircular; }
    public void setCircular(boolean isCircular) { this.isCircular = isCircular; }

    /**
     * Check if the Attribute is a non-terminal attribute
     */
    public boolean isNTA() { return isNTA; }
    public void setNTA(boolean NTA) { this.isNTA = NTA; }

    /**
     * Returns the name of the Aspect in which the Attribute was declared.
     * @return
     */
    public String getAspect() { return aspect; }
    public void setAspect(String aspect) { this.aspect = aspect; }


    /**
     * Returns the file path to where the Attribute was declared
     */
    public String getDeclaredAt() { return declaredAt; }
    public void setDeclaredAt(String declaredAt) { this.declaredAt = declaredAt; }

    /**
     * Returns the kind of the method, i.e. syn, syn, etc.
     * @return
     */
    public String getKind(){ return kind.toString(); }
    public void setKind(Object kind) { this.kind = kind; }


}
