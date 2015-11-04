package jastaddad.filteredtree;

import AST.Color;
import AST.Str;
import AST.Value;
import jastaddad.Config;
import jastaddad.Node;

import java.util.HashMap;

/**
 * Created by gda10jth on 10/21/15.
 */
public class TreeCluster extends GenericTreeNode {

    private TreeNode node;

    public TreeCluster(TreeNode node){
        super();
        this.node = node;
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
        styles.put("node-color", new Color("#DCDCDC"));
        styles.put("node-shape", new Str("\"small_circle\""));
        styles.put("border-style", new Str("\"dashed\""));
    }

}
