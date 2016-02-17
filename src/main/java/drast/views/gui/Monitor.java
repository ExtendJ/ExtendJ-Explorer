package drast.views.gui;

import drast.model.ASTBrain;
import drast.model.DrAST;
import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.controllers.Controller;
import drast.views.gui.dialogs.DrDialog;
import drast.views.gui.graph.GraphView;
import drast.views.gui.graph.GraphEdge;
import drast.views.gui.guicomponent.nodeinfotreetableview.NodeInfoView;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Monitor class for the UI.
 * It keeps track of the controllers, the GraphView and the ASTAPI etc.
 */
public class Monitor {
    private GenericTreeNode lastRealNode;
    private GenericTreeNode selectedNode;
    private NodeInfoView selectedInfo;
    private ArrayList<GraphEdge> refEdges;
    private HashMap<GenericTreeNode,ArrayList<GraphEdge>> displayedRefEdges;
    private Controller controller;
    private GraphView graphView;
    private Stage parentStage;
    private ArrayList<DrDialog> subWindows;
    private ArrayList<GenericTreeNode> dialogSelectedNodes;
    private ArrayList<GenericTreeNode> selectedParameterNodes;
    private ArrayList<GenericTreeNode> referenceHighlightNodes;
    private boolean functionRunning;
    private ArrayList<String> highlightedSimpleClassNames;
    private DrAST jaaAPI;
    private DrASTGUI jaaUI;
    private Stage stage;
    private String defaultDirectory;
    private Config config;
    private boolean rerunable;
    private boolean optimization;
    private boolean optimizationVarCalculated;

    public Monitor(){
        clean(null);
    }

    public Monitor(DrAST jaaAPI){
        clean(jaaAPI);
    }

    public void clean(DrAST jaaAPI) {
        this.jaaAPI = jaaAPI;
        subWindows = new ArrayList<>();
        dialogSelectedNodes = new ArrayList<>();
        selectedParameterNodes = new ArrayList<>();
        referenceHighlightNodes = new ArrayList<>();
        highlightedSimpleClassNames = new ArrayList<>();
        functionRunning = false;
        selectedNode = null;
        lastRealNode = null;
        selectedInfo = null;
        String tmp = new File(".").getAbsolutePath();
        defaultDirectory = tmp.substring(0,tmp.length()-1);
        config = new Config(defaultDirectory);
        rerunable = false;
        optimizationVarCalculated = false;
        optimization = false;
    }


    /**
     * This method tries to get the threshold from the configuration file and see if optimizations needs to be done.
     * No optimization will be enabled if something goes wrong with reading the configuration value.
     *
     */
    public boolean isOptimization(){
        if(optimizationVarCalculated)
            return optimization;

        try {
            int nodeThreshold = 1000;
            if(config.get("nodeThreshold") != null)
                nodeThreshold = Integer.parseInt(config.get("nodeThreshold"));

            optimization = getBrain().getClusteredASTSize() > nodeThreshold;
            if(optimization){
                controller.addWarning("Number of nodes exceed optimization threshold of " + nodeThreshold + " nodes. Navigation will be a bit more ugly, but performance will be better. ");
            }
            config.put("nodeThreshold", String.valueOf(nodeThreshold));
        }catch (Exception e){
            e.printStackTrace();
            optimization = false;
        }
        optimizationVarCalculated = true;
        return optimization;
    }

    public void setDrASTUI(DrASTGUI jaaUI){ this.jaaUI = jaaUI;}
    public DrASTGUI getDrASTUI(){return jaaUI;}

    public void setStage(Stage stage){ this.stage = stage;}
    public Stage getStage(){return stage;}

    public void functionStart(){functionRunning = true;}
    public void functionDone(){functionRunning = false;}
    public boolean isFunctionRunning(){return functionRunning;}

    public ArrayList<DrDialog> getSubWindows() {
        return subWindows;
    }
    public void addSubWindow(DrDialog window){ subWindows.add(window); }
    public void removeSubWindow(DrDialog window){ subWindows.remove(window); }

    public ArrayList<String> gethighlightedSimpleClassNames() {
        return highlightedSimpleClassNames;
    }
    public void addhighlightedSimpleClassName(String className){ highlightedSimpleClassNames.add(className); }
    public void removehighlightedSimpleClassName(String className){ highlightedSimpleClassNames.remove(className); }

    public ArrayList<GenericTreeNode> getSelectedParameterNodes() {return selectedParameterNodes; }

    public void addSelectedParameterNodes(GenericTreeNode node){
        selectedParameterNodes.add(node);
    }
    public void clearSelectedParameterNodes(){ selectedParameterNodes.clear();}


    public void addHighlightReferencesNodes(GenericTreeNode node){
        referenceHighlightNodes.add(node);
    }
    public ArrayList<GenericTreeNode> getHighlightReferencesNodes(){ return referenceHighlightNodes; }

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

    public ASTBrain getBrain(){ return jaaAPI.brain(); }
    public DrAST getDrASTAPI(){ return jaaAPI; }

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

    public void setReferenceEdges(ArrayList<GraphEdge> edges){ this.refEdges = edges; }

    public ArrayList<GraphEdge> getReferenceEdges(){ return refEdges; }

    public void setDisplayedReferenceEdges(HashMap<GenericTreeNode,ArrayList<GraphEdge>>  edges){ this.displayedRefEdges = edges; }

    public HashMap<GenericTreeNode,ArrayList<GraphEdge>> getDisplayedReferenceEdges(){ return displayedRefEdges; }

    public void setGraphView(GraphView graphView) {
        this.graphView = graphView;
    }
    public GraphView getGraphView(){return graphView;}

    public String getDefaultDirectory() { return defaultDirectory; }

    public void setDefaultDirectory(String defaultDirectory) { this.defaultDirectory = defaultDirectory; }

    public Config getConfig() {
        return config;
    }

    public boolean isRerunable() {
        return rerunable;
    }

    public void setRerunable(boolean rerunable) {
        this.rerunable = rerunable;
    }
}
