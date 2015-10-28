package jastaddad.filteredtree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gda10jth on 10/21/15.
 */
public class TreeCluster implements TreeItem {
    private TreeNode cluster;
    private List<TreeItem> children;

    public TreeCluster(TreeNode cluster){
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
    public boolean isRealChild(TreeItem child){
        return false;
    }

    @Override
    public void addChild(TreeItem child) {
        children.add(child);
    }

    @Override
    public List<TreeItem> getChildren(){
        return children;
    }

    @Override
     public String toString(){
        return "";
    }
}
