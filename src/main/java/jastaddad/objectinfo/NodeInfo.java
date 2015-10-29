package jastaddad.objectinfo;

/**
 * Created by gda10jli on 10/20/15.
 */
public abstract class NodeInfo implements Comparable<NodeInfo> {

    protected Object value;
    protected String name;

    public NodeInfo(String name, Object value){
        this.name = name;
        this.value = value;
    }

    public abstract String print();

    @Override
    public String toString(){ return print(); }

    @Override
    public int compareTo(NodeInfo o) {
        return name.compareTo(o.name);
    }
}
