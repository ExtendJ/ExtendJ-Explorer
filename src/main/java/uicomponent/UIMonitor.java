package uicomponent;

import jastaddad.ASTAPI;
import jastaddad.filteredtree.GenericTreeNode;
import jastaddad.filteredtree.TreeNode;
import uicomponent.graph.UIEdge;

/**
 * Created by gda10jli on 10/16/15.
 */
public class UIMonitor {
    private GenericTreeNode selectedNode;
    private ASTAPI api;
    private UIEdge refEdge;

    public UIMonitor(ASTAPI api){
        this.api = api;
    }

    public ASTAPI getApi(){
        return api;
    }

    public GenericTreeNode getRootNode(){ return api.getFilteredTree(); }

    public GenericTreeNode getSelectedNode(){ return selectedNode; }

    public void setSelectedNode(TreeNode selectedNode){ this.selectedNode = selectedNode; }

    public void setReferenceEdge(UIEdge edge){ this.refEdge = edge; }

    public UIEdge getReferenceEdge(){ return refEdge; }

}
