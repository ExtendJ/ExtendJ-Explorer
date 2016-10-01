package drast.views.gui;

import drast.Log;
import drast.model.DrAST;
import drast.model.DrASTSettings;
import drast.model.FilteredTreeBuilder;
import drast.model.TreeFilter;
import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.controllers.Controller;
import drast.views.gui.dialogs.DrDialog;
import drast.views.gui.graph.GraphEdge;
import drast.views.gui.graph.GraphView;
import drast.views.gui.guicomponent.nodeinfotreetableview.TerminalValueTreeItemView;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Data container for the GUI classes.
 */
public class GUIData {
  private GenericTreeNode lastRealNode;
  private GenericTreeNode selectedNode;
  private TerminalValueTreeItemView selectedInfo;
  private List<GraphEdge> refEdges;
  private Map<GenericTreeNode, List<GraphEdge>> displayedRefEdges;
  private Controller controller;
  private GraphView graphView;
  private Stage parentStage;
  private List<DrDialog> subWindows;
  private List<GenericTreeNode> dialogSelectedNodes;
  private List<GenericTreeNode> selectedParameterNodes;
  private List<GenericTreeNode> referenceHighlightNodes;
  private boolean functionRunning;
  private List<String> highlightedSimpleClassNames;
  private DrAST jaaAPI;
  private DrASTGUI jaaUI;
  private Stage stage;

  public GUIData() {
    reset(new DrAST(null, new TreeFilter()));
  }

  public void reset(DrAST drAST) {
    this.jaaAPI = drAST;
    subWindows = new ArrayList<>();
    dialogSelectedNodes = new ArrayList<>();
    selectedParameterNodes = new ArrayList<>();
    referenceHighlightNodes = new ArrayList<>();
    highlightedSimpleClassNames = new ArrayList<>();
    functionRunning = false;
    selectedNode = null;
    lastRealNode = null;
    selectedInfo = null;
  }


  /**
   * This method tries to get the threshold from the configuration file and see if optimizations needs to be done.
   * No optimization will be enabled if something goes wrong with reading the configuration value.
   */
  public boolean showUglyEdges() {
    int nodeThreshold = DrASTSettings.getInt(DrASTSettings.NODE_THRESHOLD, 1000);

    boolean optimization = getTreeTraverser() != null && getTreeTraverser().getClusteredASTSize() > nodeThreshold;
    if (optimization) {
      Log.warning("Number of nodes exceed optimization threshold of %s nodes. "
          + "The graph will not be as nice, but performance will be better.",
          nodeThreshold);
    }
    return optimization;
  }

  public void setDrASTUI(DrASTGUI jaaUI) {
    this.jaaUI = jaaUI;
  }

  public DrASTGUI getDrASTUI() {
    return jaaUI;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public Stage getStage() {
    return stage;
  }

  public void functionStart() {
    functionRunning = true;
  }

  public void functionDone() {
    functionRunning = false;
  }

  public boolean isFunctionRunning() {
    return functionRunning;
  }

  public List<DrDialog> getSubWindows() {
    return subWindows;
  }

  public void addSubWindow(DrDialog window) {
    subWindows.add(window);
  }

  public void removeSubWindow(DrDialog window) {
    subWindows.remove(window);
  }

  public List<String> gethighlightedSimpleClassNames() {
    return highlightedSimpleClassNames;
  }

  public void addhighlightedSimpleClassName(String className) {
    highlightedSimpleClassNames.add(className);
  }

  public void removehighlightedSimpleClassName(String className) {
    highlightedSimpleClassNames.remove(className);
  }

  public List<GenericTreeNode> getSelectedParameterNodes() {
    return selectedParameterNodes;
  }

  public void addSelectedParameterNodes(GenericTreeNode node) {
    selectedParameterNodes.add(node);
  }

  public void clearSelectedParameterNodes() {
    selectedParameterNodes.clear();
  }


  public void addHighlightReferencesNodes(GenericTreeNode node) {
    referenceHighlightNodes.add(node);
  }

  public List<GenericTreeNode> getHighlightReferencesNodes() {
    return referenceHighlightNodes;
  }

  public List<GenericTreeNode> getDialogSelectedNodes() {
    return dialogSelectedNodes;
  }

  public void addDialogSelectedNodes(GenericTreeNode node) {
    dialogSelectedNodes.add(node);
  }

  public void removeDialogSelectedNodes(GenericTreeNode node) {
    if (node != null) {
      dialogSelectedNodes.remove(node);
    }
  }

  public void clearDialogSelectedNodes() {
    dialogSelectedNodes.clear();
  }

  public FilteredTreeBuilder getTreeTraverser() {
    return jaaAPI.getTraverser();
  }

  public DrAST getDrASTAPI() {
    return jaaAPI;
  }

  public GenericTreeNode getRootNode() {
    return getTreeTraverser().getFilteredTree();
  }

  public Controller getController() {
    return controller;
  }

  public void setController(Controller controller) {
    this.controller = controller;
  }

  /**
   * Sets the node to last selected node.
   * If it is a "real" node it will also set the real node. Otherwise that will be null, or keep its value
   */
  public void setSelectedNode(GenericTreeNode node) {
    selectedNode = node;
    if (node != null && node.isNonCluster()) {
      lastRealNode = node;
    }
    if (node == null) {
      lastRealNode = null;
    }
  }

  public Stage getParentStage() {
    return parentStage;
  }

  public void setParentStage(Stage stage) {
    parentStage = stage;
  }

  public TerminalValueTreeItemView getSelectedInfo() {
    return selectedInfo;
  }

  public void setSelectedInfo(TerminalValueTreeItemView info) {
    this.selectedInfo = info;
  }

  public GenericTreeNode getSelectedNode() {
    return selectedNode;
  }

  /**
   * The last non cluster node that was selected
   */
  public GenericTreeNode getLastRealNode() {
    return lastRealNode;
  }

  public void setReferenceEdges(ArrayList<GraphEdge> edges) {
    this.refEdges = edges;
  }

  public List<GraphEdge> getReferenceEdges() {
    return refEdges;
  }

  public void setDisplayedReferenceEdges(Map<GenericTreeNode, List<GraphEdge>> edges) {
    this.displayedRefEdges = edges;
  }

  public Map<GenericTreeNode, List<GraphEdge>> getDisplayedReferenceEdges() {
    return displayedRefEdges;
  }

  public void setGraphView(GraphView graphView) {
    this.graphView = graphView;
  }

  public GraphView getGraphView() {
    return graphView;
  }

  public File getDefaultSettingsDirectory() {
    return new File(".");
  }
}
