package jastaddad.filteredtree;

import configAST.Color;
import configAST.Str;
import jastaddad.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by gda10jli on 11/11/15.
 */
public abstract  class GenericTreeCluster extends  GenericTreeNode{

    public GenericTreeCluster(GenericTreeNode parent) {
        super(parent);
    }

    @Override
    public boolean isNode() {
        return false;
    }

    @Override
    public String toString(){ return "..."; }

    @Override
    public String toGraphString(){return ""; }

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

    @Override
    public HashSet<NodeReference> getOutwardNodeReferences(){ return null;}

    @Override
    public HashMap<NodeReference, NodeReference> getInwardNodeReferences(){ return null;}

    @Override
    public HashSet<NodeReference> getAllNodeReferences(){ return null;}

    @Override
    public boolean addInWardNodeReference(NodeReference ref){ return false; }
}
