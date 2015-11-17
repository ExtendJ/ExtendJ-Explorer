package jastaddad.api.filteredtree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gda10jth on 10/21/15.
 */
public class TreeClusterParent extends GenericTreeCluster {

    private List<TreeCluster> clusters;
    public TreeClusterParent(GenericTreeNode parent){
        super(parent);
        clusters = new ArrayList<>();
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


}
