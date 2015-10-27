package jastaddad.filteredtree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gda10jth on 10/21/15.
 */
public class FilteredTreeCluster implements FilteredTreeItem {
    private FilteredTreeNode cluster;
    private List<FilteredTreeItem> children;

    public FilteredTreeCluster(FilteredTreeNode cluster){
        this.cluster = cluster;
        children = new ArrayList<>();
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
    public boolean isRealChild(FilteredTreeItem child){
        return false;
    }

    @Override
    public void addChild(FilteredTreeItem child) {
        children.add(child);
    }

    @Override
    public List<FilteredTreeItem> getChildren(){
        return children;
    }

    @Override
     public String toString(){
        return "Cluster";
    }
}
