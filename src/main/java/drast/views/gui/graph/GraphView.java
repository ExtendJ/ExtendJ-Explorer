package drast.views.gui.graph;

import drast.views.gui.controllers.GraphViewController;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DirectedOrderedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.*;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.BasicVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import drast.model.filteredtree.GenericTreeNode;
import drast.model.filteredtree.NodeReference;
import drast.views.gui.Monitor;
import drast.views.gui.controllers.Controller;
import drast.views.gui.graph.jungcomponents.*;
import edu.uci.ics.jung.visualization.renderers.VertexLabelAsShapeRenderer;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import org.apache.commons.collections15.Transformer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is the graph view in the UI. It uses the Jung2 library, and therefore is based on Swing. Every event that happens
 * within the tree is handled here. Events that will have changes other parts of the UI will invoke methods in the class
 * Controller reached by the field con.
 *
 * A Jung2 DelegateForest is used to present the tree. Vertexes in this tree are GenericTreeNodes and edges are
 * represented by the class UIEdge defined in this package.
 *
 * Created by gda10jli on 10/15/15.
 */
public class GraphView extends SwingNode implements ItemListener { //TODO needs a performance overhaul when it comes to HUGE graphs
    private Monitor mon;
    private Controller con;
    private GraphViewController myController;
    private VisualizationViewer<GenericTreeNode, GraphEdge> vs;
    private DelegateForest<GenericTreeNode, GraphEdge> graph;

    private ScalingControllerMinLimit scaler;
    public GraphView(Monitor mon){
        scaler = new ScalingControllerMinLimit();
        this.mon = mon;
        this.con = mon.getController();
        DirectedOrderedSparseMultigraph<GenericTreeNode, GraphEdge> n = new DirectedOrderedSparseMultigraph<GenericTreeNode, GraphEdge>();
        graph = new DelegateForest<>(n);
        if(mon.getRootNode() != null) {
            createTree(graph, mon.getRootNode(), true);
        }
        addDisplayedReferences();
        createLayout(graph);
        setListeners();
        setContent(vs);
    }

    public void setMyController(GraphViewController controller){ myController = controller;}

    /**
     * Recursively iterate through the Filtered AST to create Jung vertexes and edges.
     * @param g
     * @param parent
     */
    private void createTree(Forest<GenericTreeNode, GraphEdge> g, GenericTreeNode parent, boolean root){
        if(root && parent.getChildren().size() <= 0){
            g.addVertex(parent);
        }
        for (GenericTreeNode child : parent.getChildren()) {
            GraphEdge edge = new GraphEdge();
            boolean nodeToNode = parent.isNode() && child.isNode();

            if(nodeToNode && !parent.getNode().isOpt())
                edge.setLabel(child.getNode().nameFromParent);

            if(nodeToNode && child.isNTANode())
                edge.setType(GraphEdge.ATTRIBUTE_NTA);
            else if(!nodeToNode)
                edge.setType(GraphEdge.CLUSTER);

            g.addEdge(edge, parent, child);
            createTree(g, child, false);

        }
    }

    /**
     * This function creates the VisualizationViewer Object.
     *
     * @param g
     */
    public void createLayout(Forest<GenericTreeNode, GraphEdge> g){
        TreeLayout<GenericTreeNode, GraphEdge> layout = new TreeLayout<>(g, 150, 100);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        vs = new VisualizationViewer<>(layout, screenSize);
        vs.setRenderer(new CustomRenderer(mon));
        setVisualizationTransformers(vs);
    }

    /**
     *
     */
    public void updateGraph(){
        DirectedOrderedSparseMultigraph<GenericTreeNode, GraphEdge> n = new DirectedOrderedSparseMultigraph<GenericTreeNode, GraphEdge>();
        graph = new DelegateForest<>(n);
        ((CustomRenderer)vs.getRenderer()).refresh(mon);
        if(mon.getRootNode() != null)
            createTree(graph, mon.getRootNode(), true);
        TreeLayout<GenericTreeNode, GraphEdge> layout = new TreeLayout<>(graph, 150, 100);
        vs.setGraphLayout(layout);
        addDisplayedReferences();
        panToNode(mon.getRootNode());
        //showWholeGraphOnScreen();
        vs.repaint();
    }

    /**
     *
     */
    public void showWholeGraphOnScreen(){
        vs.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).setToIdentity();
        vs.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).setToIdentity();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        vs.setPreferredSize(screenSize);
        vs.scaleToLayout(scaler);
        vs.repaint();
    }

    public void setPreferredSize(int width, int height){
        vs.setPreferredSize(new Dimension(width, height));
        vs.scaleToLayout(scaler);
        vs.repaint();
    }

    public DelegateForest<GenericTreeNode, GraphEdge> getJungGraph(){
        return graph;
    }

    /**
     *
     * @param node
     */
    public void panToNode(GenericTreeNode node){
        Layout<GenericTreeNode,GraphEdge> layout = vs.getGraphLayout();
        Point2D q = layout.transform(node);
        Point2D lvc = vs.getRenderContext().getMultiLayerTransformer().inverseTransform(vs.getCenter());
        final double dx = (lvc.getX() - q.getX());
        final double dy = (lvc.getY() - q.getY());
        vs.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).translate(dx, dy);
    }

    /**
     * Add edges from the vertex from and to all vertexes in newRefs.
     *
     * @param newRefs
     * @param from
     */
    public void setReferenceEdges(ArrayList<GenericTreeNode> newRefs, GenericTreeNode from){
        if(mon.getReferenceEdges() != null) {
            for (GraphEdge e : mon.getReferenceEdges()){
                graph.removeEdge(e, false);
            }
        }
        if(newRefs == null || newRefs.size() == 0) {
            vs.repaint();
            return;
        }
        ArrayList<GraphEdge> edges = new ArrayList<>();
        for(GenericTreeNode ref : newRefs) {
            GraphEdge edge = new GraphEdge(GraphEdge.ATTRIBUTE_REF);
            edges.add(edge);
            graph.addEdge(edge, from.getTreeNode(), ref.getTreeNode());
        }
        mon.setReferenceEdges(edges);
        vs.repaint();
    }

    public String saveGraphAsImage(String dirPath, String filename, String ext){

        VisualizationImageServer<GenericTreeNode, GraphEdge> vis =
                new VisualizationImageServer<>(vs.getGraphLayout(),
                        vs.getGraphLayout().getSize());

        setVisualizationTransformers(vis);

        try {
            // Create the buffered image
            BufferedImage image = (BufferedImage) vis.getImage(
                    new Point2D.Double(vs.getGraphLayout().getSize().getWidth() / 2,
                            vs.getGraphLayout().getSize().getHeight() / 2),
                    new Dimension(vs.getGraphLayout().getSize()));

            // Write image to a png file
            File imageFile = new File(dirPath + filename + "." + ext);
            ImageIO.write(image, ext, imageFile);
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            mon.getController().addError("Error while capturing graph: " + e.getCause());
            return "";
        }
    }

    public String savePrintScreenGraph(String dirPath, String filename, String ext) {

        BufferedImage bufImage = ScreenImage.createImage(getContent());
        try {
            File imageFile = new File(dirPath + filename + "." + ext);
            ImageIO.write(bufImage, ext, imageFile);
            return imageFile.getAbsolutePath();
        } catch (Exception e) {
            System.out.println("writeToImageFile(): " + e.getMessage());
            mon.getController().addMessage("Could not save image. Cause: " + e.getCause());
            return "";
        }

    }

    /**
     * Add edges based on attributes. This is defined in the filter language by the user.
     */
    public void addDisplayedReferences(){
        ArrayList<NodeReference> refs = mon.getBrain().getDisplayedReferences();
        if(refs == null || refs.size() == 0)
            return;
        HashMap<GenericTreeNode, ArrayList<GraphEdge>> displayedRefs = new HashMap<>();
        addReferences(refs, displayedRefs);
        mon.setDisplayedReferenceEdges(displayedRefs);
        vs.repaint();
    }

    public void addDisplayedReferences(ArrayList<NodeReference> nodeReferences){
        if(nodeReferences == null || nodeReferences.size() == 0)
            return;
        HashMap<GenericTreeNode, ArrayList<GraphEdge>> displayedRefs = mon.getDisplayedReferenceEdges();
        addReferences(nodeReferences, displayedRefs);
        vs.repaint();
    }

    /**
     * private method to add edges based on attributes. Called from addDisplayedReferences(...).
     *
     * @param refs
     * @param displayedRefs
     */
    private void addReferences(ArrayList<NodeReference> refs, HashMap<GenericTreeNode, ArrayList<GraphEdge>> displayedRefs){
        for(NodeReference ref : refs) {
            GenericTreeNode from  = ref.getReferenceFrom();
            if(!from.getTreeNode().isNode())
                continue;
            from = from.getTreeNode();
            for(GenericTreeNode to : ref.getReferences()) {
                GraphEdge edge = new GraphEdge(GraphEdge.DISPLAYED_REF).setLabel(ref.getLabel());
                graph.addEdge(edge, from, to.getTreeNode());
                if(!displayedRefs.containsKey(from))
                    displayedRefs.put(from, new ArrayList<>());
                displayedRefs.get(from).add(edge);
            }
        }
    }

    private final static float dash[] = {5.0f};
    private final static Stroke refStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, dash, 0.0f);
    private final static Stroke dashedStroke = new BasicStroke(0.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, dash, 0.0f);
    private final static Stroke normalStroke = new BasicStroke(1.0f);

    /**
     * This method sets all the transformers on the layout
     *
     * Transformers in Jung2 are used to define the shape, size, color etc on vertexes and edges. A Transformer have two
     * generics that defines what (the first generic) will be transformed to what (the second generic). An example:
     * to define a color on a vertex, a Transformer<GenericTreeNode, Color> is used. it "transform" a GenericTreeNode to
     * a Color.
     * @param bvs
     */
    public void setVisualizationTransformers(BasicVisualizationServer<GenericTreeNode, GraphEdge> bvs){
        bvs.setBackground(new Color(255,255,255));

        // Vertex text transformer
        Transformer <GenericTreeNode, String> toStringTransformer = fNode -> fNode.toGraphString();
        // Edge color transformer
        Transformer<GraphEdge, Paint> edgePaintTransformer = edge -> edge.getColor();

        // Edge stroke transformer
        Transformer<GraphEdge, Stroke> edgeStrokeTransformer = edge -> {
            switch (edge.getType()){
                case GraphEdge.STANDARD :
                case GraphEdge.ATTRIBUTE_NTA :
                    return normalStroke;
                case GraphEdge.ATTRIBUTE_REF :
                case GraphEdge.DISPLAYED_REF :
                    return refStroke;
                case GraphEdge.CLUSTER :
                default :
                    return dashedStroke;
            }
        };

        // Vertex border style transformer
        Transformer<GenericTreeNode, Stroke> vertexStrokeTransformer = item -> {
            if(item.getStyles().get("border-style").getStr().equals("dashed"))
               return dashedStroke;
            return normalStroke;
        };

        // Build the VisualizationViewer that holds the graph and all transformers.
        bvs.scaleToLayout(new ScalingControllerMinLimit());

        bvs.getRenderContext().setVertexStrokeTransformer(vertexStrokeTransformer);
        bvs.getRenderContext().setVertexFillPaintTransformer(new VertexPaintTransformer(vs.getPickedVertexState(), mon));
        bvs.getRenderContext().setVertexLabelTransformer(toStringTransformer);
        bvs.getRenderContext().setVertexLabelRenderer(new CustomDefaultVertexLabelRenderer(java.awt.Color.blue));
        bvs.getRenderContext().setVertexShapeTransformer(new VertexShapeTransformer(vs.getRenderContext()));

        bvs.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        bvs.getRenderContext().setEdgeDrawPaintTransformer(edgePaintTransformer);
        bvs.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<>());
        bvs.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<>());
        bvs.getRenderContext().setLabelOffset(15);

        //Override the default renderers
        bvs.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        bvs.getRenderer().setEdgeLabelRenderer(new EdgeLabelRenderer<>());
    }

    /**
     * Set listeners in the Graph.
     */
    public void setListeners(){
        vs.getPickedVertexState().addItemListener(this);
        PluggableGraphMouse gm = new PluggableGraphMouse();
        gm.add(new DraggingGraphMousePlugin(MouseEvent.BUTTON2_MASK));
        gm.add(new DraggingGraphMousePlugin(MouseEvent.BUTTON1_MASK + MouseEvent.CTRL_MASK));
        gm.add(new PopupGraphMousePlugin(vs, mon, this));
        gm.add(new PickingGraphMousePlugin());
        gm.add(new CustomScalingGraphMousePlugin(new ScalingControllerMinLimit(), 0, 1.1f, 0.9f));
        vs.setGraphMouse(gm);
    }

    public void repaintHard(){
        vs.repaint();
        setContent(vs);
    }

    public void repaint(){
        vs.repaint();
    }

    public void setSelectedNode(GenericTreeNode node){
        vs.getPickedVertexState().removeItemListener(this);
        vs.getPickedVertexState().clear();
        vs.getPickedVertexState().pick(node, true);
        vs.getPickedVertexState().addItemListener(this);
    }

    /**
     * Deselects all vertexes in the tree. This method is used if the selected node is defined by some other part
     * of the UI, e.g. the Tree view.
     */
    public void deselectNode(){
        vs.getPickedVertexState().clear();
    }

    /**
     * Listener event method when a vertex is selected in the graph.
     * @param e
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        Platform.runLater(() -> {
            Object subject = e.getItem();
            if (subject != null && subject instanceof GenericTreeNode) {
                if(e.getStateChange() == ItemEvent.SELECTED)
                    con.nodeSelected((GenericTreeNode) subject, myController);
                else
                    con.nodeDeselected(myController);
            }
        });
    }

    public void zoomIn() {
        scaler.scale(vs, 1 / 1.1f, vs.getCenter());
    }

    public void zoomOut() {
        scaler.scale(vs, 1 / 1.1f, vs.getCenter());
    }
}
