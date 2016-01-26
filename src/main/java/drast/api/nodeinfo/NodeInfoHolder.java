package drast.api.nodeinfo;

/**
 * A Holder for attribute informations, can hold a reference to a NodeInfo
 * Created by gda10jli on 11/3/15.
 */
public class NodeInfoHolder implements Comparable<NodeInfoHolder>{

    private final String name;
    private final Object value;
    private final NodeInfo nodeInfo;
    private boolean filePointer;

    public NodeInfoHolder(String name, Object value, boolean filePointer){
        this.name = name;
        this.value = value;
        nodeInfo = null;
        this.filePointer = filePointer;
    }

    public NodeInfoHolder(String name, Object value){
        this.name = name;
        this.value = value;
        nodeInfo = null;
    }

    public NodeInfoHolder(NodeInfo nodeInfo){
        this.nodeInfo = nodeInfo;
        this.name = nodeInfo.print();
        this.value = nodeInfo.getValue();
    }

    public NodeInfo getNodeInfo(){ return nodeInfo; }
    public String getName() { return name; }
    public Object getValue() {
        return value;
    }
    public boolean getFilePointer() {
        return filePointer;
    }

    @Override
    public int compareTo(NodeInfoHolder node) {
        return name.compareTo(node.name);
    }
}
