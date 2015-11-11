package jastaddad.filteredtree;

import configAST.Color;
import configAST.Str;
import jastaddad.Config;

import java.util.ArrayList;

/**
 * Created by gda10jth on 10/21/15.
 */
public class TreeCluster extends GenericTreeNode {

    private GenericTreeNode node;
    public TreeCluster(GenericTreeNode node, GenericTreeNode parent){
        super(parent);
        this.node = node;
    }

    public GenericTreeNode getClusterRoot(){
        return node;
    }

    @Override
    public boolean isNode() {
        return false;
    }

    @Override
    public boolean isCluster() {
        return true;
    }

    @Override
    public boolean isClusterParent() {
        return false;
    }

    @Override
    public boolean isRealChild(GenericTreeNode child){
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
    public ArrayList<NodeReference> getNodeReferences(){ return null;}
}
