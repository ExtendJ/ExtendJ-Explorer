package drast.model.filteredtree;

import configAST.Color;
import configAST.Str;
import drast.model.ASTBrain;
import drast.model.FilterConfig;
import drast.model.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Abstract class which contains the similarities between Cluster and ClusterParent
 * Created by gda10jli on 11/11/15.
 */
public abstract  class GenericTreeCluster extends GenericTreeNode{

    protected int nodeCount;
    protected ArrayList<GenericTreeCluster> clusters;

    public GenericTreeCluster(GenericTreeNode parent) {
        super(parent);
        nodeCount = 0;
        this.clusters = new ArrayList<>();
    }

    protected abstract HashMap<String, Integer> fillTypeList(HashMap<String, Integer> types);

    public HashMap<String, Integer> getTypeList(){ return fillTypeList(new HashMap<>()); }

    public ArrayList<GenericTreeCluster> getClusters(){ return clusters; }

    public int getNodeCount(){ return nodeCount; }

    @Override
    public boolean isNode() {
        return false;
    }

    @Override
    public String toString(){ return "..."; }

    @Override
    public String toGraphString(){return String.valueOf(nodeCount); }

    @Override
    public String toTreeViewString(){return toString() + " " + getNodeCount(); }

    @Override
    public void setStyles(FilterConfig filter) {
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
    @Override
    public Node getNode(){ return null; }

    /**
     * Clusters can't be a NTANode
     * @return
     */
    @Override
    public boolean isNTANode(){ return false; }

    /**
     * Clusters contains no references
     * @return
     */
    @Override
    public void setDisplayedAttributes(ArrayList<NodeReference> allReferences, HashSet<String> set , ASTBrain api){}

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

}
