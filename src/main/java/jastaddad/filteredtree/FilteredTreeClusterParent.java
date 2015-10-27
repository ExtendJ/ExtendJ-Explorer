package jastaddad.filteredtree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gda10jth on 10/21/15.
 */
public class FilteredTreeClusterParent implements FilteredTreeItem {
    private List<FilteredTreeItem> children;
    private List<FilteredTreeCluster> clusters;
    public FilteredTreeClusterParent(){
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
    public void addChild(FilteredTreeItem child) {
        children.add(child);
    }

    public void addCluster(FilteredTreeCluster cluster){
        clusters.add(cluster);
    }

    @Override
    public List<FilteredTreeItem> getChildren() {
        return children;
    }

    public List<FilteredTreeCluster> getClusters(){
        return clusters;
    }

    @Override
    public boolean isRealChild(FilteredTreeItem child) {
        return false;
    }

    @Override
     public String toString(){
        return "ClusterParent";
    }
}
