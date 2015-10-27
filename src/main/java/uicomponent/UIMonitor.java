package uicomponent;

import jastaddad.ASTAPI;
import jastaddad.filteredtree.FilteredTreeItem;
import jastaddad.filteredtree.FilteredTreeNode;

/**
 * Created by gda10jli on 10/16/15.
 */
public class UIMonitor {
    private FilteredTreeItem selectedNode;
    private ASTAPI api;

    public UIMonitor(ASTAPI api){
        this.api = api;
    }

    public ASTAPI getApi(){
        return api;
    }

    public FilteredTreeItem getRootNode(){ return api.getFilteredTree(); }

    public FilteredTreeItem getSelectedNode(){ return selectedNode; }
    public void setSelectedNode(FilteredTreeNode selectedNode){ this.selectedNode = selectedNode; }
}
