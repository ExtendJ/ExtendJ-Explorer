package jastaddad.filteredtree;

/**
 * Created by gda10jth on 10/21/15.
 */
public class TreeCluster extends GenericTreeNode {
    private TreeNode cluster;

    public TreeCluster(TreeNode cluster){
        super();
        this.cluster = cluster;
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

}
