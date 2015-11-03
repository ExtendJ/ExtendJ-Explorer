package jastaddad.filteredtree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gda10jth on 10/21/15.
 */
public class TreeClusterParent extends GenericTreeNode {
    private List<TreeCluster> clusters;
    public TreeClusterParent(){
        super();
        clusters = new ArrayList<>();
    }

    @Override
    public boolean isNode() {
        return false;
    }

    @Override
    public boolean isCluster() {
        return false;
    }

    @Override
    public boolean isClusterParent() {
        return true;
    }

    public void addCluster(TreeCluster cluster){ clusters.add(cluster); cluster.clusterRef = this; }

    public List<TreeCluster> getClusters(){
        return clusters;
    }

    @Override
    public boolean isRealChild(GenericTreeNode child) {
        return false;
    }

    @Override
    public String toString(){ return "..."; }
    public String toGraphString(){return ""; }
}
