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
    public final boolean isCluster;
    public final boolean isClusterParent;

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
    }

    public FilteredTreeNode(FilteredTreeNode cluster){
        node = null;
        this.cluster = cluster;
        isCluster = true;
        isClusterParent = false;
        clusterContainer = null;
        children = new ArrayList<>();
        realChildEdge = new HashMap<>();
    }

    public FilteredTreeNode(Node data){
        node = data;
        isCluster = false;
        isClusterParent = false;
        clusterContainer = null;
        cluster = null;
        children = new ArrayList<>();
        realChildEdge = new HashMap<>();
    }

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

    @Override
    public String toString(){
        if(isCluster)
            return "Cluster";
        if(isClusterParent)
            return "Cluster Parent";
        return node.toString();
    }
}
