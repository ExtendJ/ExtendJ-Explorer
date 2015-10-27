package jastaddad.filteredtree;

import java.util.List;

/**
 * Created by gda10jth on 10/21/15.
 */
public interface FilteredTreeItem {
    boolean isNode();
    boolean isCluster();
    boolean isClusterParent();
    void addChild(FilteredTreeItem child);
    List<FilteredTreeItem> getChildren();
    boolean isRealChild(FilteredTreeItem child);
}

