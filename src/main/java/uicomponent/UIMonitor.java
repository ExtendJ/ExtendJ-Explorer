package uicomponent;

import jastaddad.ASTAPI;
import jastaddad.filteredtree.TreeItem;
import jastaddad.filteredtree.TreeNode;

/**
 * Created by gda10jli on 10/16/15.
 */
public class UIMonitor {
    private TreeItem selectedNode;
    private ASTAPI api;

    public UIMonitor(ASTAPI api){
        this.api = api;
    }

    public ASTAPI getApi(){
        return api;
    }

    public TreeItem getRootNode(){ return api.getFilteredTree(); }

    public TreeItem getSelectedNode(){ return selectedNode; }
    public void setSelectedNode(TreeNode selectedNode){ this.selectedNode = selectedNode; }
}
