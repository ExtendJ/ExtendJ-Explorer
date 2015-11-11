package uicomponent;

import jastaddad.ASTAPI;
import jastaddad.filteredtree.GenericTreeNode;
import uicomponent.controllers.Controller;
import uicomponent.graph.UIEdge;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by gda10jli on 10/16/15.
 */
public class UIMonitor {

    private GenericTreeNode lastRealNode;
    private GenericTreeNode selectedNode;
    private AttributeInfo selectedInfo;
    private ArrayList<UIEdge> refEdges;
    private HashMap<GenericTreeNode,ArrayList<UIEdge>> displayedRefEdges;
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
        if(node == null)
            lastRealNode = null;
    }

    public AttributeInfo getSelectedInfo(){ return selectedInfo;}

    public void setSelectedInfo(AttributeInfo info){ this.selectedInfo = info; }

    public GenericTreeNode getSelectedNode(){ return selectedNode;}

    public GenericTreeNode getLastRealNode(){ return lastRealNode;}

    public void setReferenceEdges(ArrayList<UIEdge> edges){ this.refEdges = edges; }

    public ArrayList<UIEdge> getReferenceEdges(){ return refEdges; }

    public void setDisplayedReferenceEdges(HashMap<GenericTreeNode,ArrayList<UIEdge>>  edges){ this.displayedRefEdges = edges; }

    public HashMap<GenericTreeNode,ArrayList<UIEdge>> getDisplayedReferenceEdges(){ return displayedRefEdges; }

}
