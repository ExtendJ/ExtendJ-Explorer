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
    private GenericTreeNode refNode;
    private UIEdge refEdge;
    private ASTAPI api;

    public UIMonitor(ASTAPI api){
        this.api = api;
    }

    public ASTAPI getApi(){
        return api;
    }

    public GenericTreeNode getRootNode(){ return api.getFilteredTree(); }

    public GenericTreeNode getSelectedNode(){ return selectedNode; }

    public void setSelectedNode(GenericTreeNode selectedNode){ this.selectedNode = selectedNode; }

    public void setReferenceEdge(UIEdge edge){ this.refEdge = edge; }

    public UIEdge getReferenceEdge(){ return refEdge; }

    public void setReferenceNode(GenericTreeNode node){ this.refNode = node; }

    public GenericTreeNode getReferenceNode(){ return refNode; }
}
