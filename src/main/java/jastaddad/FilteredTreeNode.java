package jastaddad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gda10jth on 10/16/15.
 */
public class FilteredTreeNode{
    public final Node node;
    public final FilteredTreeNode cluster;
    private List<FilteredTreeNode> clusterContainer;
    private boolean isCluster;
    private boolean isClusterParent;
    private boolean enabled;
    private List<FilteredTreeNode> children;
    private HashMap<Integer, Boolean> realChildEdge;

    public FilteredTreeNode(){
        node = null;
        cluster = null;
        isCluster = false;
        isClusterParent = true;
        clusterContainer = new ArrayList<>();

        children = new ArrayList<>();
        realChildEdge = new HashMap<>();

        enabled = false;
    }

    public FilteredTreeNode(FilteredTreeNode cluster){
        node = null;
        this.cluster = cluster;
        isCluster = true;
        isClusterParent = false;
        clusterContainer = null;
        children = new ArrayList<>();
        realChildEdge = new HashMap<>();

        enabled = false;
    }

    public FilteredTreeNode(Node data, Config cfgTypeList){
        node = data;
        isCluster = false;
        isClusterParent = false;
        clusterContainer = null;
        cluster = null;
        children = new ArrayList<>();
        realChildEdge = new HashMap<>();

        enabled = setEnabled(cfgTypeList);
    }

    private boolean setEnabled(Config cfgTypeList){
        return cfgTypeList.configCount() == 0
                || cfgTypeList.isEnabled(node.className)
                && (cfgTypeList.get(node.className + ":" + node.name) == null
                || cfgTypeList.isEnabled(node.className + ":" + node.name));
    }

    public boolean isEnabled(){ return enabled; }

    public void addChild(FilteredTreeNode child){
        if(isNode() && child.isNode()) {
            realChildEdge.put(child.node.id, node.children.contains(child.node));
        }
        children.add(child);
    }

    public void addCluster(FilteredTreeNode cluster){

        if(isClusterParent)
            clusterContainer.add(cluster);
    }

    public boolean isRealChild(FilteredTreeNode child){
        //System.out.println(realChildEdge.get(child.node.id));
        if(!isNode() || !child.isNode())
            return false;
        return realChildEdge.get(child.node.id);
    }

    public boolean isNode(){ return !isCluster && !isClusterParent; }

    public List<FilteredTreeNode> getChildren(){
        return children;
    }
    public List<FilteredTreeNode> getClusterContainer(){
        return clusterContainer;
    }

    public Iterator<FilteredTreeNode> iterator(){ return children.iterator(); }

    public boolean isCluster(){return isCluster;}

    public boolean isClusterParent(){return isClusterParent;}

    @Override
    public String toString(){
        if(isCluster)
            return "Cluster";
        if(isClusterParent)
            return "Cluster Parent";
        return node.toString();
    }
}
