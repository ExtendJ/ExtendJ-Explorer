package jastaddad.objectinfo;

/**
 * Created by gda10jli on 11/3/15.
 */
public class NodeInfoHolder implements Comparable<NodeInfoHolder>{

    private final String name;
    private final Object value;
    private final NodeInfo nodeInfo;

    public NodeInfoHolder(String name, Object value){
        this.name = name;
        this.value = value;
        nodeInfo = null;
    }

    public NodeInfoHolder(String name, Object value, NodeInfo nodeInfo){
        this.name = name;
        this.value = value;
        this.nodeInfo = nodeInfo;
    }

    public NodeInfo getNodeInfo(){ return nodeInfo; }
    public String getName() { return name; }
    public Object getValue() {
        return value;
    }

    @Override
    public int compareTo(NodeInfoHolder node) {
        return name.compareTo(node.name);
    }
}
