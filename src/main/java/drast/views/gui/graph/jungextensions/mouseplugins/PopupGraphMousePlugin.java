package drast.views.gui.graph.jungextensions.mouseplugins;

import drast.model.filteredtree.GenericTreeNode;
import drast.model.filteredtree.NodeReference;
import drast.model.filteredtree.TreeCluster;
import drast.views.gui.GUIData;
import drast.views.gui.graph.GraphEdge;
import drast.views.gui.graph.GraphView;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import javafx.application.Platform;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * A PopupGraphMousePlugin that brings up distinct popup menus when an edge or vertex is
 * appropriately clicked in a graph.
 * <p>
 * (Might add EdgeMenuListener or VertexMenuListener later)
 */
public class PopupGraphMousePlugin extends AbstractPopupGraphMousePlugin {
  private ContextMenu vertexPopup;
  private final VisualizationViewer<GenericTreeNode, GraphEdge> vs;
  private GenericTreeNode lastClicked;
  private final GUIData mon;
  private final GraphView graphView;

  public PopupGraphMousePlugin(VisualizationViewer<GenericTreeNode, GraphEdge> vs, GUIData mon,
      GraphView graphView) {
    super(MouseEvent.BUTTON3_MASK);
    this.vs = vs;
    this.mon = mon;
    this.graphView = graphView;
    setVertexPopup();
  }

  /**
   * Implementation of the AbstractPopupGraphMousePlugin method. This is where the
   * work gets done.
   */
  @Override protected void handlePopup(MouseEvent e) {
    Point2D p = e.getPoint();
    GraphElementAccessor<GenericTreeNode, GraphEdge> pickSupport = vs.getPickSupport();
    if (pickSupport != null) {
      // Get the selected vertex (if there is one).
      lastClicked = pickSupport.getVertex(vs.getGraphLayout(), p.getX(), p.getY());
      if (lastClicked != null) {
        if (lastClicked.isExpandable()) {
          Platform.runLater(
              () -> vertexPopup.show(mon.getStage(), e.getXOnScreen(), e.getYOnScreen()));
        }
      }
    }
  }

  /**
   * Setter for the vertex popup.
   */
  private void setVertexPopup() {
    vertexPopup = new ContextMenu();
    MenuItem item;
    vertexPopup.getItems().add(item = new MenuItem("Toggle collapse"));
    item.setOnAction(event -> {
      if (lastClicked.isNonCluster()) {
        collapse();
      } else {
        expand();
      }
    });
  }

  /**
   * Called when it's time to collapse a vertex in the graph.
   * <p>
   * This collapse is a UI specific feature, and have nothing to do with the API. The changes are done in the Jung2
   * graph and not in the api filtered AST. That means that the collapse is only for as long as the session of the
   * program is alive or the filter button is clicked.
   * <p>
   * lastClicked is the node that is collapsed.
   */
  private void collapse() {
    if (lastClicked.isNonCluster()) {
      DelegateForest<GenericTreeNode, GraphEdge> inGraph =
          (DelegateForest<GenericTreeNode, GraphEdge>) vs.getGraphLayout().getGraph();
      TreeCluster newCluster = new TreeCluster(lastClicked.getNode(), lastClicked);
      newCluster.setExpandable();

      // remove all reference edges
      if (mon.getReferenceEdges() != null) {
        for (GraphEdge e : mon.getReferenceEdges()) {
          inGraph.removeEdge(e, false);
        }
      }

      // store the parent vertex in the edge pointing to the collapsed vertex
      GenericTreeNode parent = null;
      GraphEdge edge = null;
      if (lastClicked.getParent() != null) {
        parent = lastClicked.getParent().getTreeNode();
        for (GraphEdge e : inGraph.getInEdges(lastClicked)) {
          if (!e.isReference()) {
            edge = e;
          }
        }
      }
      // store the position of the node
      Point2D d = vs.getGraphLayout().transform(lastClicked);
      // set the cluster reference of all children to the new cluster
      setClusterRef(newCluster, lastClicked);

      // remove vertexes
      HashSet<NodeReference> nodeRef = new HashSet<>();
      removeVertexes(lastClicked, inGraph, nodeRef, newCluster);
      // add references to the new cluster
      graphView.addDisplayedReferences(new ArrayList<>(nodeRef));
      mon.getController().resetReferences();

      if (parent != null) {
        inGraph.addEdge(edge, parent, newCluster);
      } else {
        inGraph.addVertex(newCluster);
      }

      vs.getGraphLayout().setLocation(newCluster, d);
      vs.repaint();
    }
  }

  /**
   * Called when it's time to expand a collapsed vertex in the graph.
   * <p>
   * This expand is a UI specific feature, and have nothing to do with the API. The changes are done in the Jung2
   * graph and not in the api filtered AST. That means that the expand is only for as long as the session of the
   * program is alive or the filter button is clicked.
   * <p>
   * lastClicked is the node that will be expanded.
   */
  private void expand() {
    if (lastClicked.isCluster()) {
      DelegateForest<GenericTreeNode, GraphEdge> inGraph =
          (DelegateForest<GenericTreeNode, GraphEdge>) vs.getGraphLayout().getGraph();
      GenericTreeNode node = ((TreeCluster) lastClicked).getClusterRoot();

      if (mon.getSelectedNode() == lastClicked) {
        Platform.runLater(() -> mon.getController().nodeDeselected(null));
      }
      // get the parent vertex and the edge from this one.
      GenericTreeNode parent = null;
      GraphEdge edge = null;
      if (node.getParent() != null) {
        parent = node.getParent().getTreeNode();
        for (GraphEdge e : inGraph.getInEdges(lastClicked)) {
          if (!e.isReference()) {
            edge = e;
          }
        }
      }
      // remember the position of the vertex
      Point2D d = vs.getGraphLayout().transform(lastClicked);

      // remove all references to this vertex
      if (mon.getReferenceEdges() != null) {
        for (GraphEdge e : mon.getReferenceEdges()) {
          inGraph.removeEdge(e, false);
        }
      }

      ArrayList<NodeReference> nodeRef = new ArrayList<>();
      // add all child vertexes
      createTree(inGraph, node, nodeRef);
      setClusterRef(null, node);

      // add references to the children
      graphView.addDisplayedReferences(nodeRef);

      // remove the cluster vertex
      inGraph.removeVertex(lastClicked, false);

      if (parent != null && edge != null) {
        inGraph.addEdge(edge, parent, node);
      }

      // add references to the children
      mon.getController().resetReferences();

      //inGraph.removeVertex(lastClicked, false);
      lastClicked = node;

      vs.getGraphLayout().setLocation(node, d);
      vs.repaint();
    }
  }

  /**
   * Recursively remove all child vertexes of the vertex parent.
   * First store all reference edges that points to this vertex and put them inside nodeRef. Then remove all reference
   * edges from this vertex. And last remove the vertex.
   */
  private void removeVertexes(GenericTreeNode parent,
      DelegateForest<GenericTreeNode, GraphEdge> inGraph, HashSet<NodeReference> nodeRef,
      TreeCluster newCluster) {
    if (parent != newCluster.getClusterRoot()) {
      newCluster.addToCluster(parent);
    }

    // Store references to the vertex
    if (parent.getAllNodeReferences() != null) {
      nodeRef.addAll(parent.getAllNodeReferences());
    }

    // Remove references from the vertex
    if (mon.getDisplayedReferenceEdges() != null
        && mon.getDisplayedReferenceEdges().get(parent) != null) {
      for (GraphEdge e : mon.getDisplayedReferenceEdges().get(parent)) {
        inGraph.removeEdge(e, false);
      }
    }

    // Remove references from the vertex
    if (mon.getReferenceEdges() != null) {
      for (GraphEdge e : mon.getReferenceEdges()) {
        inGraph.removeEdge(e, false);
      }
    }

    // Remove all children
    for (GenericTreeNode child : inGraph.getChildren(parent)) {
      removeVertexes(child, inGraph, nodeRef, newCluster);
    }

    // Remove this vertex
    inGraph.removeVertex(parent, false);

  }

  /**
   * Sets the cluster reference field in each child vertex recursively. (Used during collapse and expand)
   */
  private void setClusterRef(TreeCluster cluster, GenericTreeNode node) {
    node.setClusterReference(cluster);
    for (GenericTreeNode child : node.getChildren()) {
      setClusterRef(cluster, child);
    }
  }

  /**
   * Recursively add all child vertexes of the vertex parent.
   */
  private void createTree(Forest<GenericTreeNode, GraphEdge> g, GenericTreeNode parent,
      ArrayList<NodeReference> nodeRef) {

    // add all references to this vertex to nodeRef
    if (parent.getOutwardNodeReferences() != null) {
      nodeRef.addAll(parent.getOutwardNodeReferences());
    }
    if (parent.getInwardNodeReferences() != null) {
      nodeRef.addAll(parent.getInwardNodeReferences().values().stream()
          .filter(ref -> !ref.getReferenceFrom().getTreeNode().equals(parent.getTreeNode()))
          .collect(Collectors.toList()));
    }
    // calculate the edge for the child
    for (GenericTreeNode child : parent.getChildren()) {
      GraphEdge edge = new GraphEdge();
      boolean nodeToNode = parent.isNonCluster() && child.isNonCluster();

      if (nodeToNode && !parent.getNode().isOpt()) {
        edge.setLabel(child.getNode().getNameFromParent());
      }

      if (nodeToNode && child.isNTANode()) {
        edge.setType(GraphEdge.ATTRIBUTE_NTA);
      } else if (!nodeToNode) {
        edge.setType(GraphEdge.CLUSTER);
      }

      g.addEdge(edge, parent, child);
      createTree(g, child, nodeRef);
    }
  }
}

