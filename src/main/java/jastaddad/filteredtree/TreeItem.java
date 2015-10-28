package jastaddad.filteredtree;

import java.util.List;

/**
 * Created by gda10jth on 10/21/15.
 */
public interface TreeItem {
    boolean isNode();
    boolean isCluster();
    boolean isClusterParent();
    void addChild(TreeItem child);
    List<TreeItem> getChildren();
    boolean isRealChild(TreeItem child);
}

