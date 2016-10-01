package drast.views.gui.graph;

import drast.Log;
import drast.model.DrASTSettings;
import drast.model.filteredtree.GenericTreeNode;
import drast.model.filteredtree.NodeReference;
import drast.views.gui.GUIData;
import drast.views.gui.controllers.GraphViewController;
import drast.views.gui.graph.jungextensions.CustomVisualizationViewer;
import drast.views.gui.graph.jungextensions.ScalingControllerMinLimit;
import drast.views.gui.graph.jungextensions.mouseplugins.CustomPickingGraphMousePlugin;
import drast.views.gui.graph.jungextensions.mouseplugins.CustomScalingGraphMousePlugin;
import drast.views.gui.graph.jungextensions.mouseplugins.PanningGraphMousePlugin;
import drast.views.gui.graph.jungextensions.mouseplugins.PopupGraphMousePlugin;
import drast.views.gui.graph.jungextensions.renderers.CustomDefaultVertexLabelRenderer;
import drast.views.gui.graph.jungextensions.renderers.CustomRenderer;
import drast.views.gui.graph.jungextensions.renderers.EdgeLabelRenderer;
import drast.views.gui.graph.jungextensions.transformers.VertexEdgeTransformer;
import drast.views.gui.graph.jungextensions.transformers.VertexPaintTransformer;
import drast.views.gui.graph.jungextensions.transformers.VertexShapeTransformer;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DirectedOrderedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import org.apache.commons.collections15.Transformer;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the graph view in the GUI. It uses the Jung2 library, and therefore is based on Swing. Every event that happens
 * within the tree is handled here. Events that will should change other parts of the GUI will invoke methods in the class
 * GraphViewController reached by the field myController.
 * <p>
 * A Jung2 DelegateForest is used to present the tree. Vertexes in this tree are GenericTreeNodes and edges are
 * represented by the class GraphEdge defined in this package.
 * <p>
 * The class creates its tree representation from the constructor when an object gets instantiated. When a new tree
 * needs to be represented, The method updateGraph() should be called after a new root is set in its monitor Monitor.
 * <p>
 * Created by gda10jli on 10/15/15.
 */
public class GraphView extends SwingNode implements ItemListener {
  private final GUIData mon;
  private GraphViewController myController;
  private CustomVisualizationViewer<GenericTreeNode, GraphEdge> vs;
  private DelegateForest<GenericTreeNode, GraphEdge> graph;

  private final ScalingControllerMinLimit scaler;
  private boolean hugeGraph; // The graph is so big that we can not show the whole graph on screen.

  public GraphView(GUIData mon) {
    hugeGraph = false;
    scaler = new ScalingControllerMinLimit(this);
    this.mon = mon;
    DirectedOrderedSparseMultigraph<GenericTreeNode, GraphEdge> n =
        new DirectedOrderedSparseMultigraph<>();
    graph = new DelegateForest<>(n);
    if (mon.getRootNode() != null) {
      createTree(graph, mon.getRootNode(), true);
    }

    createLayout(graph);
    addDisplayedReferences();
    setListeners();
    setContent(vs);
  }

  public void setHugeGraph() {
    hugeGraph = true;
  }

  public void setMyController(GraphViewController controller) {
    myController = controller;
  }

  /**
   * Recursively iterate through the Filtered AST to create Jung vertexes and edges.
   */
  private void createTree(Forest<GenericTreeNode, GraphEdge> g, GenericTreeNode parent,
      boolean root) {
    if (root && parent.getChildren().size() <= 0) {
      g.addVertex(parent);
    }
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
      createTree(g, child, false);

    }
  }

  /**
   * This method creates the VisualizationViewer Object.
   */
  private void createLayout(Forest<GenericTreeNode, GraphEdge> g) {
    TreeLayout<GenericTreeNode, GraphEdge> layout = new TreeLayout<>(g, 150, 100);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    vs = new CustomVisualizationViewer<>(layout, screenSize);
    vs.setRenderer(new CustomRenderer(mon));
    setVisualizationTransformers();
  }

  /**
   * Create a new graph representation from the root in the Monitor.getRootNode().
   */
  public void updateGraph() {
    hugeGraph = false;
    scaler.resetScaler();
    DirectedOrderedSparseMultigraph<GenericTreeNode, GraphEdge> n =
        new DirectedOrderedSparseMultigraph<>();
    graph = new DelegateForest<>(n);

    if (mon.getRootNode() != null) {
      createTree(graph, mon.getRootNode(), true);
    }
    TreeLayout<GenericTreeNode, GraphEdge> layout = new TreeLayout<>(graph, 150, 100);
    vs.setGraphLayout(layout);
    setVisualizationTransformers();
    addDisplayedReferences();
    panToNode(mon.getRootNode());
    showWholeGraphOnScreen();
    vs.repaint();
  }

  /**
   * Zoom out so the whole graph is seen on the screen. If the graph is too big it will zoom out as
   * much as is permitted.
   */
  public void showWholeGraphOnScreen() {
    if (!hugeGraph) {
      vs.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).setToIdentity();
      vs.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).setToIdentity();
    }
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    vs.setPreferredSize(screenSize);
    vs.zoomOutMax(scaler);
    vs.repaint();
  }

  /**
   * Sets the size of the graph layout to the with and height
   */
  public void setPreferredSize(int width, int height) {
    vs.setPreferredSize(new Dimension(width, height));
    vs.scaleToLayout(scaler);
    vs.repaint();
  }

  /**
   * Move the "camera" to the GenericTreeNode node in the graph. This does not zoom, only moves the camera
   * in X and Y axis
   */
  public void panToNode(GenericTreeNode node) {
    Layout<GenericTreeNode, GraphEdge> layout = vs.getGraphLayout();
    Point2D q = layout.transform(node);
    Point2D lvc = vs.getRenderContext().getMultiLayerTransformer().inverseTransform(vs.getCenter());
    final double dx = (lvc.getX() - q.getX());
    final double dy = (lvc.getY() - q.getY());
    vs.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).translate(dx, dy);
  }

  /**
   * Add edges from the vertex from and to all vertexes in newRefs.
   */
  public void setReferenceEdges(ArrayList<GenericTreeNode> newRefs, GenericTreeNode from) {
    if (mon.getReferenceEdges() != null) {
      for (GraphEdge e : mon.getReferenceEdges()) {
        graph.removeEdge(e, false);
      }
    }
    if (newRefs == null || newRefs.size() == 0) {
      vs.repaint();
      return;
    }
    ArrayList<GraphEdge> edges = new ArrayList<>();
    for (GenericTreeNode ref : newRefs) {
      GraphEdge edge = new GraphEdge(GraphEdge.ATTRIBUTE_REF);
      edges.add(edge);
      graph.addEdge(edge, from.getTreeNode(), ref.getTreeNode());
    }
    mon.setReferenceEdges(edges);
    vs.repaint();
  }

  /**
   * Saves the Whole graph to an image file.
   */
  public String saveGraphAsImage(String dirPath, String filename) {

    VisualizationImageServer<GenericTreeNode, GraphEdge> vis =
        new VisualizationImageServer<>(vs.getGraphLayout(), vs.getGraphLayout().getSize());

    setVisualizationTransformers();

    try {
      // Create the buffered image
      BufferedImage image = (BufferedImage) vis.getImage(
          new Point2D.Double(vs.getGraphLayout().getSize().getWidth() / 2,
              vs.getGraphLayout().getSize().getHeight() / 2),
          new Dimension(vs.getGraphLayout().getSize()));

      // Write image to a png file
      File imageFile = new File(new File(dirPath), filename + ".png");
      ImageIO.write(image, "png", imageFile);
      return imageFile.getAbsolutePath();
    } catch (IOException e) {
      e.printStackTrace();
      Log.error("Error while capturing graph: " + e.getCause());
      return "";
    }
  }

  /**
   * Saves the part of the graph that is shown in the graph window to an image file.
   */
  public String savePrintScreenGraph(String dirPath, String filename) {

    BufferedImage bufImage = ScreenImage.createImage(getContent());
    try {
      File imageFile = new File(new File(dirPath), filename + ".png");
      ImageIO.write(bufImage, "png", imageFile);
      return imageFile.getAbsolutePath();
    } catch (Exception e) {
      System.out.println("writeToImageFile(): " + e.getMessage());
      Log.error("Could not save image. Cause: " + e.getCause());
      return "";
    }

  }

  /**
   * Add edges based on attributes. This is defined in the filter language by the user.
   */
  private void addDisplayedReferences() {
    List<NodeReference> refs = mon.getTreeTraverser().getDisplayedReferences();
    if (refs == null || refs.isEmpty()) {
      return;
    }
    Map<GenericTreeNode, List<GraphEdge>> displayedRefs = new HashMap<>();
    addReferences(refs, displayedRefs);
    mon.setDisplayedReferenceEdges(displayedRefs);
    vs.repaint();
  }

  public void addDisplayedReferences(ArrayList<NodeReference> nodeReferences) {
    if (nodeReferences == null || nodeReferences.size() == 0) {
      return;
    }
    Map<GenericTreeNode, List<GraphEdge>> displayedRefs = mon.getDisplayedReferenceEdges();
    addReferences(nodeReferences, displayedRefs);
    vs.repaint();
  }

  /**
   * private method to add edges based on attributes. Called from addDisplayedReferences(...).
   */
  private void addReferences(List<NodeReference> refs,
      Map<GenericTreeNode, List<GraphEdge>> displayedRefs) {
    for (NodeReference ref : refs) {
      GenericTreeNode from = ref.getReferenceFrom();
      if (!from.getTreeNode().isNonCluster()) {
        continue;
      }
      from = from.getTreeNode();
      for (GenericTreeNode to : ref.getReferences()) {
        GraphEdge edge = new GraphEdge(GraphEdge.DISPLAYED_REF).setLabel(ref.getLabel());
        graph.addEdge(edge, from, to.getTreeNode());
        if (!displayedRefs.containsKey(from)) {
          displayedRefs.put(from, new ArrayList<>());
        }
        displayedRefs.get(from).add(edge);
      }
    }
  }

  /**
   * This method sets all the transformers on the layout
   * <p>
   * Transformers in Jung2 are used to define the shape, size, color etc on vertexes and edges. A Transformer have two
   * generics that defines what (the first generic) will be transformed to what (the second generic). An example:
   * to define a color on a vertex, a Transformer<GenericTreeNode, Color> is used. it "transform" a GenericTreeNode to
   * a Color.
   */
  private void setVisualizationTransformers() {
    vs.setBackground(new Color(255, 255, 255));

    // Vertex text transformer
    Transformer<GenericTreeNode, String> toStringTransformer = GenericTreeNode::toGraphString;

    // Edge color transformer
    Transformer<GraphEdge, Paint> edgePaintTransformer = GraphEdge::getColor;

    Transformer<GenericTreeNode, Font> fontLabelTransformer =
        label -> new Font("someFontTextThisCanBeAnythingANYTHING", Font.PLAIN, 15);

    // Vertex border style transformer
    // Build the VisualizationViewer that holds the graph and all transformers.
    vs.scaleToLayout(scaler);

    vs.getRenderContext().setVertexStrokeTransformer(new VertexEdgeTransformer());
    vs.getRenderContext()
        .setVertexFillPaintTransformer(new VertexPaintTransformer(vs.getPickedVertexState(), mon));
    vs.getRenderContext().setVertexLabelTransformer(toStringTransformer);
    vs.getRenderContext()
        .setVertexLabelRenderer(new CustomDefaultVertexLabelRenderer(java.awt.Color.blue));
    vs.getRenderContext()
        .setVertexShapeTransformer(new VertexShapeTransformer(vs.getRenderContext()));

    vs.getRenderContext().setEdgeStrokeTransformer(new GraphEdge(mon));
    vs.getRenderContext().setEdgeDrawPaintTransformer(edgePaintTransformer);
    setNiceEdges(!mon.showUglyEdges() && DrASTSettings.getFlag(DrASTSettings.CURVED_EDGES));
    vs.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<>());
    vs.getRenderContext().setVertexFontTransformer(fontLabelTransformer);

    // Override the default renderers:
    vs.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
    vs.getRenderer().setEdgeLabelRenderer(new EdgeLabelRenderer<>());
  }

  /**
   * Set listeners in the Graph.
   */
  private void setListeners() {
    vs.getPickedVertexState().addItemListener(this);
    PluggableGraphMouse gm = new PluggableGraphMouse();
    gm.add(new PanningGraphMousePlugin(MouseEvent.BUTTON2_MASK));
    gm.add(new PanningGraphMousePlugin(MouseEvent.BUTTON1_MASK + MouseEvent.CTRL_MASK));
    gm.add(new PopupGraphMousePlugin(vs, mon, this));
    gm.add(new CustomPickingGraphMousePlugin());
    gm.add(new CustomScalingGraphMousePlugin(scaler));
    vs.setGraphMouse(gm);
  }

  public void setNiceEdges(boolean niceEdges) {
    if (niceEdges) {
      vs.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<>());
      vs.getRenderContext().setLabelOffset(-5);
    } else {
      vs.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<>());
      vs.getRenderContext().setLabelOffset(0);
    }
    vs.repaint();

  }

  public void repaint() {
    vs.repaint();
  }

  public void setSelectedNode(GenericTreeNode node) {
    vs.getPickedVertexState().removeItemListener(this);
    vs.getPickedVertexState().clear();
    vs.getPickedVertexState().pick(node, true);
    vs.getPickedVertexState().addItemListener(this);
  }

  /**
   * Deselects all vertexes in the tree. This method is used if the selected node is defined by some other part
   * of the UI, e.g. the Tree view.
   */
  public void deselectNode() {
    vs.getPickedVertexState().clear();
  }

  /**
   * Listener event method when a vertex is selected in the graph.
   */
  @Override public void itemStateChanged(ItemEvent e) {
    Platform.runLater(() -> {
      Object subject = e.getItem();
      if (subject != null && subject instanceof GenericTreeNode) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          mon.getController().nodeSelected((GenericTreeNode) subject, myController);
        } else {
          mon.getController().nodeDeselected(myController);
        }
      }
    });
  }

  public void zoomIn() {
    scaler.scale(vs, 1.1f, vs.getCenter());
  }

  public void zoomOut() {
    scaler.scale(vs, 1 / 1.1f, vs.getCenter());
  }
}
