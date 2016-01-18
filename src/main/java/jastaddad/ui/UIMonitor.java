package jastaddad.ui;

import jastaddad.api.ASTAPI;
import jastaddad.api.JastAddAdAPI;
import jastaddad.api.filteredtree.GenericTreeNode;
import jastaddad.ui.controllers.Controller;
import jastaddad.ui.dialogs.UIDialog;
import jastaddad.ui.graph.GraphView;
import jastaddad.ui.graph.UIEdge;
import jastaddad.ui.uicomponent.nodeinfotreetableview.NodeInfoView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Monitor class for the UI.
 * It keeps track of the controllers, the GraphView and the ASTAPI etc.
 */
public class UIMonitor {
    private GenericTreeNode lastRealNode;
    private GenericTreeNode selectedNode;
    private NodeInfoView selectedInfo;
    private ArrayList<UIEdge> refEdges;
    private HashMap<GenericTreeNode,ArrayList<UIEdge>> displayedRefEdges;
    private Controller controller;
    private GraphView graphView;
    private Stage parentStage;
    private ArrayList<UIDialog> subWindows;
    private ArrayList<GenericTreeNode> dialogSelectedNodes;
    private boolean functionRunning;
    private ArrayList<String> highlightedSimpleClassNames;
    private JastAddAdAPI jaaAPI;
    private JastAddAdUI jaaUI;
    private Stage stage;


    public UIMonitor(JastAddAdAPI jaaAPI){
        clean(jaaAPI);
    }

    public void clean(JastAddAdAPI jaaAPI) {
        this.jaaAPI = jaaAPI;
        subWindows = new ArrayList<>();
        dialogSelectedNodes = new ArrayList<>();
        highlightedSimpleClassNames = new ArrayList<>();
        functionRunning = false;
        selectedNode = null;
        lastRealNode = null;
        selectedInfo = null;
    }

    public void setJastAddAdUI(JastAddAdUI jaaUI){ this.jaaUI = jaaUI;}
    public JastAddAdUI getJastAddAdUI(){return jaaUI;}

    public void setStage(Stage stage){ this.stage = stage;}
    public Stage getStage(){return stage;}

    public void functionStart(){functionRunning = true;}
    public void functionDone(){functionRunning = false;}
    public boolean isFunctionRunning(){return functionRunning;}

    public ArrayList<UIDialog> getSubWindows() {
        return subWindows;
    }
    public void addSubWindow(UIDialog window){ subWindows.add(window); }
    public void removeSubWindow(UIDialog window){ subWindows.remove(window); }

    public ArrayList<String> gethighlightedSimpleClassNames() {
        return highlightedSimpleClassNames;
    }
    public void addhighlightedSimpleClassName(String className){ highlightedSimpleClassNames.add(className); }
    public void removehighlightedSimpleClassName(String className){ highlightedSimpleClassNames.remove(className); }

    public ArrayList<GenericTreeNode> getDialogSelectedNodes() {
        return dialogSelectedNodes;
    }
    public void addDialogSelectedNodes(GenericTreeNode node){
        dialogSelectedNodes.add(node);
    }
    public void removeDialogSelectedNodes(GenericTreeNode node){
        if(node != null)
            dialogSelectedNodes.remove(node);
    }
    public void clearDialogSelectedNodes(){ dialogSelectedNodes.clear();}

    public ASTAPI getApi(){    return jaaAPI.api();
    }
    public JastAddAdAPI getJastAddAdAPI(){
        return jaaAPI;
    }

    public GenericTreeNode getRootNode(){ return jaaAPI.getFilteredTree(); }
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

    public Stage getParentStage(){return parentStage;}
    public void setParentStage(Stage stage){parentStage = stage;}

    public NodeInfoView getSelectedInfo(){ return selectedInfo;}

    public void setSelectedInfo(NodeInfoView info){ this.selectedInfo = info; }

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
