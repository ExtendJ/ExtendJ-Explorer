package jastaddad.api.filteredtree;

/**
 * Created by gda10jth on 10/21/15.
 */
public class TreeCluster extends GenericTreeCluster {

    private GenericTreeNode node;

    public TreeCluster(GenericTreeNode node, GenericTreeNode parent){
        super(parent);
        this.node = node;
    }

    public void addToTypeList(GenericTreeNode child) {
        nodeCount++;
        if(!child.isNode())
            return;
        TreeNode node = (TreeNode) child;
        Integer nbr = typeList.get(node.getNode().simpleNameClass);
        if(nbr == null)
            nbr = 0;
        nbr++;
        typeList.put(node.getNode().simpleNameClass, nbr);
    }

    /**
     * The top node of the Cluster.
     * @return
     */
    public GenericTreeNode getClusterRoot(){
        return node;
    }

    @Override
    public boolean isCluster() {
        return true;
    }

    @Override
    public boolean isClusterParent() {
        return false;
    }
}
