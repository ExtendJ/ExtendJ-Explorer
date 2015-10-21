package uicomponent;

import jastaddad.ASTAPI;
import jastaddad.FilteredTreeNode;

/**
 * Created by gda10jli on 10/16/15.
 */
public class UIMonitor {
    private FilteredTreeNode selectedNode;
    private FilteredTreeNode root;
    private ASTAPI api;

    public UIMonitor(FilteredTreeNode root, ASTAPI api){
        this.root = root;
        this.api = api;
    }

    public ASTAPI getApi(){
        return api;
    }

    public FilteredTreeNode getRootNode(){ return root; }
    public void setRootNode(FilteredTreeNode root){ this.root = root; }

    public FilteredTreeNode getSelectedNode(){ return selectedNode; }
    public void setSelectedNode(FilteredTreeNode selectedNode){ this.selectedNode = selectedNode; }
}
