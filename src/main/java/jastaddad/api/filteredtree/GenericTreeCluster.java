package jastaddad.api.filteredtree;

import configAST.Color;
import configAST.Str;
import jastaddad.api.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Abstract class which contains the similarities between Cluster and ClusterParent
 * Created by gda10jli on 11/11/15.
 */
public abstract  class GenericTreeCluster extends GenericTreeNode{

    protected HashMap<String, Integer> typeList;

    public GenericTreeCluster(GenericTreeNode parent) {
        super(parent);
        typeList = new HashMap<>();
    }
    public abstract int getNodeCount();

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

    @Override
    public boolean isRealChild(GenericTreeNode child) {
        return false;
    }

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
