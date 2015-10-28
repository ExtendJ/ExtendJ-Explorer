package jastaddad.filteredtree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gda10jth on 10/21/15.
 */
public class TreeClusterParent implements TreeItem {
    private List<TreeItem> children;
    private List<TreeCluster> clusters;
    public TreeClusterParent(){
        children = new ArrayList<>();
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

    @Override
    public void addChild(TreeItem child) {
        children.add(child);
    }

    public void addCluster(TreeCluster cluster){
        clusters.add(cluster);
    }

    @Override
    public List<TreeItem> getChildren() {
        return children;
    }

    public List<TreeCluster> getClusters(){
        return clusters;
    }

    @Override
    public boolean isRealChild(TreeItem child) {
        return false;
    }

    @Override
     public String toString(){
        return "";
    }
}
