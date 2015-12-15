package jastaddad.api.filteredtree;

import configAST.Color;
import configAST.Str;
import jastaddad.api.Config;
import jastaddad.api.Node;

import java.util.*;

/**
 * Abstract class which contains the similarities between Cluster and ClusterParent
 * Created by gda10jli on 11/11/15.
 */
public abstract  class GenericTreeCluster extends GenericTreeNode{

    protected HashMap<String, Integer> typeList;
    protected int nodeCount;

    public GenericTreeCluster(GenericTreeNode parent) {
        super(parent);
        typeList = new HashMap<>();
        nodeCount = 0;
    }

    public void setNodeCount(int count){ nodeCount=count; }
    public void addToTypeList(GenericTreeNode node, String addToName){
        if(node.isNode()) {
            String name = node.getNode().simpleNameClass + addToName;
            typeList.put(name, typeList.containsKey(name) ? typeList.get(name) + 1 : 1);
        }else{
            GenericTreeCluster cluster = (GenericTreeCluster) node;
            typeList.put("Cluster" + addToName, cluster.getNodeCount());
        }
    }

    public int getNodeCount(){
        return nodeCount;
    }

    @Override
    public boolean isNode() {
        return false;
    }

    @Override
    public String toString(){ return "..."; }

    @Override
    public String toGraphString(){return getNodeCount()+""; }

    @Override
    public void setStyles(Config filter) {
        if(isExpandable())
            styles.put("node-color", new Color("#DCDCaa"));
        else
            styles.put("node-color", new Color("#DCDCDC"));
        styles.put("node-shape", new Str("\"small_circle\""));
        styles.put("border-style", new Str("\"dashed\""));
    }

    /**
    * Cluster has not "real" node
    */
    public Node getNode(){ return null; }

    /**
     * Clusters can't be a NTANode
     * @return
     */
    @Override
    public boolean isNTANode(){ return false; }

    /**
     * Clusters can't be nullNodes
     * @return
     */
    @Override
    public boolean isNullNode(){ return false; }
    /**
     * Clusters contains no references
     * @return
     */
    @Override
    public HashSet<NodeReference> getOutwardNodeReferences(){ return null;}


    /**
     * Clusters contains no references
     * @return
     */
    @Override
    public HashMap<NodeReference, NodeReference> getInwardNodeReferences(){ return null;}


    /**
     * Clusters contains no references
     * @return
     */
    @Override
    public HashSet<NodeReference> getAllNodeReferences(){ return null;}


    /**
     * Clusters cant contain references
     * @return
     */
    @Override
    public boolean addInWardNodeReference(NodeReference ref){ return false; }

    public HashMap<String, Integer> getTypeList() {
        return typeList;
    }
}
