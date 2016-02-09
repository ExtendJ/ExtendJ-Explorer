package drast.model.filteredtree;

import drast.model.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for cluster of clusters
 * Created by gda10jth on 10/21/15.
 */
public class TreeClusterParent extends GenericTreeCluster {

    public TreeClusterParent(GenericTreeNode parent){
        super(parent);
    }

    @Override
    protected HashMap<String, Integer> fillTypeList(HashMap<String, Integer> types) {
        for (GenericTreeNode node : clusters) {
            TreeCluster cluster = (TreeCluster) node;
            for (Map.Entry<String, Integer> e : cluster.getTypeList().entrySet()) {
                types.put(e.getKey(), types.containsKey(e.getKey()) ? types.get(e.getKey()) + e.getValue() : e.getValue());
            }
        }
        return types;
    }

    @Override
    public void addChild(Node node, GenericTreeNode child) {
        if(!child.isCluster())
            return;
        TreeCluster cluster = (TreeCluster) child;
        clusters.add(cluster);
        cluster.clusterRef = this;
        nodeCount += cluster.getNodeCount();
    }

    @Override
    public boolean isCluster() {
        return false;
    }

    @Override
    public boolean isClusterParent() {
        return true;
    }

}
