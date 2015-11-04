package uicomponent;

import jastaddad.ASTAPI;
import jastaddad.filteredtree.GenericTreeNode;

import uicomponent.controllers.Controller;

import uicomponent.graph.UIEdge;


/**
 * Created by gda10jli on 10/16/15.
 */
public class UIMonitor {

    private GenericTreeNode lastRealNode;
    private GenericTreeNode selectedNode;
    private GenericTreeNode refNode;
    private UIEdge refEdge;
    private ASTAPI api;
    private Controller controller;

    public UIMonitor(ASTAPI api){
        this.api = api;
    }

    public ASTAPI getApi(){
        return api;
    }

    public GenericTreeNode getRootNode(){ return api.getFilteredTree(); }
    public Controller getController(){return controller;}

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setSelectedNode(GenericTreeNode node){
        selectedNode = node;
        if(node!= null && node.isNode())
            lastRealNode = node;
    }

    public GenericTreeNode getSelectedNode(){ return selectedNode;}

    public GenericTreeNode getLastRealNode(){ return lastRealNode;}

    public void setReferenceEdge(UIEdge edge){ this.refEdge = edge; }

    public UIEdge getReferenceEdge(){ return refEdge; }

    public void setReferenceNode(GenericTreeNode node){ this.refNode = node; }

    public GenericTreeNode getReferenceNode(){ return refNode; }
}
