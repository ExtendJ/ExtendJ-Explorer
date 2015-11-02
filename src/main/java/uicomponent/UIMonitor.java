package uicomponent;

import jastaddad.ASTAPI;
import jastaddad.filteredtree.GenericTreeNode;
import jastaddad.filteredtree.TreeNode;
import uicomponent.controllers.Controller;

/**
 * Created by gda10jli on 10/16/15.
 */
public class UIMonitor {
    private GenericTreeNode selectedNode;
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

    public GenericTreeNode getSelectedNode(){ return selectedNode; }
    public void setSelectedNode(GenericTreeNode selectedNode){ this.selectedNode = selectedNode; }
}
