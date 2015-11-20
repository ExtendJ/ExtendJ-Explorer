package jastaddad.ui;

import jastaddad.api.ASTAPI;
import jastaddad.api.JastAddAdAPI;
import jastaddad.api.filteredtree.GenericTreeNode;
import jastaddad.ui.controllers.Controller;
import jastaddad.ui.graph.GraphView;
import jastaddad.ui.graph.UIEdge;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Monitor class for the UI.
 * It keeps track of the controllers, the GraphView and the ASTAPI etc.
 */
public class UIMonitor {
    private GenericTreeNode lastRealNode;
    private GenericTreeNode selectedNode;
    private AttributeInfo selectedInfo;
    private ArrayList<UIEdge> refEdges;
    private HashMap<GenericTreeNode,ArrayList<UIEdge>> displayedRefEdges;
    private JastAddAdAPI jaaApi;
    private Controller controller;
    private GraphView graphView;

    public UIMonitor(JastAddAdAPI jaaApi){
        this.jaaApi = jaaApi;
    }

    public ASTAPI getApi(){
        return jaaApi.api();
    }
    public JastAddAdAPI getJastAddAdAPI(){
        return jaaApi;
    }

    public GenericTreeNode getRootNode(){ return jaaApi.getFilteredTree(); }
    public Controller getController(){return controller;}

    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Sets the node to last selected node.
     * If it is a "real" node it will also set the realnode. Otherwise that will be null, or keep its value
     * @param node
     */
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

    /**
     * The last non cluster node that was selected
     * @return
     */
    public GenericTreeNode getLastRealNode(){ return lastRealNode;}

    public void setReferenceEdges(ArrayList<UIEdge> edges){ this.refEdges = edges; }

    public ArrayList<UIEdge> getReferenceEdges(){ return refEdges; }

    public void setDisplayedReferenceEdges(HashMap<GenericTreeNode,ArrayList<UIEdge>>  edges){ this.displayedRefEdges = edges; }

    public HashMap<GenericTreeNode,ArrayList<UIEdge>> getDisplayedReferenceEdges(){ return displayedRefEdges; }

    public void setGraphView(GraphView graphView) {
        this.graphView = graphView;
    }
    public GraphView getGraphView(){return graphView;}
}
