package jastaddad.filteredtree;

import jastaddad.Node;

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

}
