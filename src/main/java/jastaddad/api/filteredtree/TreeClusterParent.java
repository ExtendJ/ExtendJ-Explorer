package jastaddad.api.filteredtree;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for cluster of clusters
 * Created by gda10jth on 10/21/15.
 */
public class TreeClusterParent extends GenericTreeCluster {
    private int nodeCount;

    private List<TreeCluster> clusters; //The clusters the cluster contains
    public TreeClusterParent(GenericTreeNode parent){
        super(parent);
        clusters = new ArrayList<>();
        nodeCount = 0;
    }

    @Override
    public int getNodeCount(){
        return nodeCount;
    }

    @Override
    public boolean isCluster() {
        return false;
    }

    @Override
    public boolean isClusterParent() {
        return true;
    }

    public void addCluster(TreeCluster cluster){
        clusters.add(cluster);
        cluster.clusterRef = this;
        nodeCount += cluster.getNodeCount();
    }

    public List<TreeCluster> getClusters(){
        return clusters;
    }


}
