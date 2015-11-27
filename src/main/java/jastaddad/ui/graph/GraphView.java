package jastaddad.ui.graph;

import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DirectedOrderedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.*;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.renderers.VertexLabelAsShapeRenderer;
import jastaddad.api.filteredtree.GenericTreeNode;
import jastaddad.api.filteredtree.NodeReference;
import jastaddad.api.filteredtree.TreeNode;
import jastaddad.ui.UIMonitor;
import jastaddad.ui.controllers.Controller;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import org.apache.commons.collections15.Transformer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
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
    private UIMonitor mon;
    private Controller con;
    private VisualizationViewer<GenericTreeNode, UIEdge> vs;
    private DelegateForest<GenericTreeNode, UIEdge> graph;

    public GraphView(UIMonitor mon){
        this.mon = mon;
        this.con = mon.getController();
        DirectedOrderedSparseMultigraph<GenericTreeNode, UIEdge> n = new DirectedOrderedSparseMultigraph<GenericTreeNode, UIEdge>();
        graph = new DelegateForest<>(n);
        createTree(graph, mon.getRootNode(), true);
        createLayout(graph);
        setListeners();
        addDisplayedReferences();
        setContent(vs);
    }

    /**
     * Recursively iterate through the Filtered AST to create Jung vertexes and edges.
     * @param g
     * @param parent
     */
    private void createTree(Forest<GenericTreeNode, UIEdge> g, GenericTreeNode parent, boolean root){
        if(root && parent.getChildren().size() <= 0){
            g.addVertex(parent);
        }
        for (GenericTreeNode child : parent.getChildren()) {
            UIEdge edge = null;
            if(child.isNode()){
                TreeNode n = (TreeNode) child;
                if(parent.isNode() && !((TreeNode)parent).getNode().isOpt())
                    edge = new UIEdge(parent.isRealChild(child), n.getNode().nameFromParent);
                else
                    edge = new UIEdge(parent.isRealChild(child));
            }else {
                edge = new UIEdge(parent.isRealChild(child));
            }
            edge.setType(child.isNTANode() ? UIEdge.ATTRIBUTE_NTA : UIEdge.STANDARD);
            g.addEdge(edge, parent, child);
            createTree(g, child, false);
        }
    }

    public void updateGraph(){
        DirectedOrderedSparseMultigraph<GenericTreeNode, UIEdge> n = new DirectedOrderedSparseMultigraph<GenericTreeNode, UIEdge>();
        graph = new DelegateForest<>(n);
        createTree(graph, mon.getRootNode(), true);
        TreeLayout<GenericTreeNode, UIEdge> layout = new TreeLayout<>(graph, 150, 100);
        vs.setGraphLayout(layout);
        //vs.getGraphLayout().setGraph(graph);
        addDisplayedReferences();
        vs.repaint();
    }

    public void setSelectedNode(GenericTreeNode node){
        vs.getPickedVertexState().removeItemListener(this);
        vs.getPickedVertexState().pick(node, true);
        vs.getPickedVertexState().addItemListener(this);
    }

    /**
     * Add edges from the vertex from and to all vertexes in newRefs.
     *
     * @param newRefs
     * @param from
     */
    public void setReferenceEdges(ArrayList<GenericTreeNode> newRefs, GenericTreeNode from){
        if(mon.getReferenceEdges() != null) {
            for (UIEdge e : mon.getReferenceEdges()){
                graph.removeEdge(e, false);
            }
        }
        if(newRefs == null || newRefs.size() == 0) {
            vs.repaint();
            return;
        }
        ArrayList<UIEdge> edges = new ArrayList<>();
        for(GenericTreeNode ref : newRefs) {
            UIEdge edge = new UIEdge(UIEdge.ATTRIBUTE_REF);
            edges.add(edge);
            graph.addEdge(edge, from.getClusterNode(), ref.getClusterNode());
        }
        mon.setReferenceEdges(edges);
        vs.repaint();
    }

    public void saveGraphAsImage(String filename, String ext){

        VisualizationImageServer<GenericTreeNode, UIEdge> vis =
                new VisualizationImageServer<>(vs.getGraphLayout(),
                        vs.getGraphLayout().getSize());

        setVisualizationTransformers(vis);

        // Create the buffered image
        BufferedImage image = (BufferedImage) vis.getImage(
                new Point2D.Double(vs.getGraphLayout().getSize().getWidth() / 2,
                        vs.getGraphLayout().getSize().getHeight() / 2),
                new Dimension(vs.getGraphLayout().getSize()));

        // Write image to a png file
        File outputfile = new File(filename + "." + ext);

        try {
            ImageIO.write(image, ext, outputfile);
        } catch (IOException e) {
            // Exception handling
            e.printStackTrace();
        }
    }

    public void savePrintScreenGraph(String filename, String ext) {

        BufferedImage bufImage = ScreenImage.createImage(getContent());
        try {
            File outputfile = new File(filename + "." + ext);
            ImageIO.write(bufImage, ext, outputfile);
        } catch (Exception e) {
            System.out.println("writeToImageFile(): " + e.getMessage());
        }
    }

    /**
     * Add edges based on attributes. This is defined in the filter language by the user.
     */
    public void addDisplayedReferences(){
        ArrayList<NodeReference> refs = mon.getApi().getDisplayedReferences();
        if(refs == null || refs.size() == 0)
            return;
        HashMap<GenericTreeNode, ArrayList<UIEdge>> displayedRefs = new HashMap<>();
        addReferences(refs, displayedRefs);
        mon.setDisplayedReferenceEdges(displayedRefs);
        vs.repaint();
    }

    public void addDisplayedReferences(ArrayList<NodeReference> nodeReferences){
        if(nodeReferences == null || nodeReferences.size() == 0)
            return;
        HashMap<GenericTreeNode, ArrayList<UIEdge>> displayedRefs = mon.getDisplayedReferenceEdges();
        addReferences(nodeReferences, displayedRefs);
        vs.repaint();
    }

    /**
     * private method to add edges based on attributes. Called from addDisplayedReferences(...).
     *
     * @param refs
     * @param displayedRefs
     */
    private void addReferences(ArrayList<NodeReference> refs, HashMap<GenericTreeNode, ArrayList<UIEdge>> displayedRefs){
        for(NodeReference ref : refs) {
            GenericTreeNode from  = ref.getReferenceFrom();
            if(!from.getClusterNode().isNode())
                continue;
            from = from.getClusterNode();
            for(GenericTreeNode to : ref.getReferences()) {
                UIEdge edge = new UIEdge(UIEdge.DISPLAYED_REF).setLabel(ref.getLabel());
                graph.addEdge(edge, from, to.getClusterNode());
                if(!displayedRefs.containsKey(from))
                    displayedRefs.put(from, new ArrayList<>());
                displayedRefs.get(from).add(edge);
            }
        }
    }

    public void setVisualizationTransformers(BasicVisualizationServer<GenericTreeNode, UIEdge> bvs){

        // Vertex text transformer
        Transformer <GenericTreeNode, String> toStringTransformer = fNode -> fNode.toGraphString();
        // Edge color transformer
        Transformer<UIEdge, Paint> edgePaintTransformer = edge -> edge.getColor();

        // Edge stroke transformer
        float dash[] = {5.0f};
        final Stroke refStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, dash, 0.0f);
        final Stroke dashedStroke = new BasicStroke(0.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, dash, 0.0f);
        final Stroke normalStroke = new BasicStroke(1.0f);
        Transformer<UIEdge, Stroke> edgeStrokeTransformer = edge -> {
            if(edge.isRealChild())
                return normalStroke;
            if(edge.isReference())
                return refStroke;
            return dashedStroke;
        };

        // Vertex border style transformer
        Transformer<GenericTreeNode, Stroke> vertexStrokeTransformer = item -> {
            String border = item.getStyles().get("border-style").getStr();
            if(border.equals("dashed"))
                return dashedStroke;
            return normalStroke;
        };

        // Build the VisualizationViewer that holds the graph and all transformers.
        ScalingControl visualizationViewerScalingControl = new CrossoverScalingControl();


        bvs.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        bvs.scaleToLayout(visualizationViewerScalingControl);

        bvs.getRenderContext().setVertexStrokeTransformer(vertexStrokeTransformer);
        bvs.getRenderContext().setVertexFillPaintTransformer(new VertexPaintTransformer(vs.getPickedVertexState(), mon));
        bvs.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<>());
        bvs.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<>());
        bvs.getRenderContext().setVertexLabelTransformer(toStringTransformer);
        bvs.getRenderContext().setVertexShapeTransformer(new VertexShapeTransformer(mon, vs.getRenderContext()));
        bvs.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        bvs.getRenderContext().setEdgeDrawPaintTransformer(edgePaintTransformer);
    }

    /**
     * This function creates the VisualizationViewer Object and defines all Transformers.
     *
     * Transformers in Jung2 are used to define the shape, size, color etc on vertexes and edges. A Transformer have two
     * generics that defines what (the first generic) will be transformed to what (the second generic). An example:
     * to define a color on a vertex, a Transformer<GenericTreeNode, Color> is used. it "transform" a GenericTreeNode to
     * a Color.
     * @param g
     */
    public void createLayout(Forest<GenericTreeNode, UIEdge> g){

        TreeLayout<GenericTreeNode, UIEdge> layout = new TreeLayout<>(g, 150, 100);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        vs = new VisualizationViewer<>(layout, screenSize);
        setVisualizationTransformers(vs);

    }

    public DelegateForest<GenericTreeNode, UIEdge> getJungGraph(){
        return (DelegateForest<GenericTreeNode, UIEdge> )vs.getGraphLayout().getGraph();
    }

    /**
     * Set listeners in the Graph.
     */
    public void setListeners(){
        vs.getPickedVertexState().addItemListener(this);
        PluggableGraphMouse gm = new PluggableGraphMouse();
        gm.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON2_MASK));
        gm.add(new PopupGraphMousePlugin(vs, mon, this));
        gm.add(new PickingGraphMousePlugin());
        gm.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, 1.1f, 0.9f));
        vs.setGraphMouse(gm);

    }

    public void repaint(){
        vs.repaint();
    }

    /**
     * Sets the selected vertex if the tree. This method is used if the selected vertex is defined by some other part
     * of the UI, e.g. the Tree view.
     * @param node
     */
    public void newNodeSelected(GenericTreeNode node) {
        vs.getPickedVertexState().clear();
        vs.getPickedVertexState().pick(node, true);
        vs.repaint();
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
                    con.nodeSelected((GenericTreeNode) subject, true);
                else
                    con.nodeDeselected(true);
            }
        });
    }

    /**
     * A transformer Class for setting the color of a vertex in the graph.
     */
    private static class VertexPaintTransformer implements Transformer<GenericTreeNode,Paint> {
        private final PickedInfo<GenericTreeNode> pi;
        private final UIMonitor mon;

        VertexPaintTransformer ( PickedInfo<GenericTreeNode> pi, UIMonitor mon ) {
            super();
            this.mon = mon;
            if (pi == null)
                throw new IllegalArgumentException("PickedInfo instance must be non-null");
            this.pi = pi;

        }

        /**
         * Sets the color of the vertex fNode based on picked state, highlight or a vertex specific color.
         * @param fNode
         * @return
         */
        @Override
        public Paint transform(GenericTreeNode fNode) {
            String color = fNode.getStyles().get("node-color").getColor();
            if(mon.getDialogSelectedNodes().contains(fNode))
                return new Color(120, 148, 86);
            if (pi.isPicked(fNode)) {
                return new Color(240, 240, 200);
            }
            if(fNode.isNullNode()){
                return new Color(254, 160, 160);
            }
            if(fNode.isReferenceHighlight()) {
                return new Color(80, 180, 80);
            }
            if(fNode.isNTANode()){
                return new Color(120, 160, 200);
            }
            try{
                return Color.decode(color);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return new Color(200, 240, 230);
            }
        }
    }

    /**
     * A transformer Class for setting the shape of a vertex in the graph.
     */
    private class VertexShapeTransformer extends VertexLabelAsShapeRenderer<GenericTreeNode, UIEdge> {
        private final UIMonitor mon;

        VertexShapeTransformer ( UIMonitor mon , RenderContext<GenericTreeNode, UIEdge> rc) {
            super(rc);
            this.mon = mon;

        }

        /**
         * Sets the shape of the vertex fNode based on style or type of the vertex (cluster or whatever)
         * @param fNode
         * @return
         */
        @Override
        public Shape transform(GenericTreeNode fNode) {
            Component component = prepareRenderer(rc, rc.getVertexLabelRenderer(), rc.getVertexLabelTransformer().transform(fNode),
                    rc.getPickedVertexState().isPicked(fNode), fNode);
            Dimension size = component.getPreferredSize();

            int centerX = -size.width/2 -10;
            int centerY = -size.height/2 -10;
            int height = size.height+20;
            int width = size.width+20;

            // make sure nodes have a minimum width
            if(fNode.isNode() && !fNode.isNullNode() && width < 130){
                width = 130;
                centerX = -65;
            }else if(!fNode.isNode()){
                width = 40;
                height = 40;
                centerX = -20;
                centerY = -20;
            }

            if(fNode.isNullNode())
                return new Ellipse2D.Float(centerX, centerY, width, height);
            //Rectangle bounds = new Rectangle(-size.width/2 -2, -size.height/2 -2, size.width+4, size.height);

            String shape = fNode.getStyles().get("node-shape").getStr();
            if(shape != null) {
                if (shape.equals("rounded_rectangle"))
                    return new RoundRectangle2D.Double(centerX, centerY, width, height, 40, 40);
                if (shape.equals("small_circle"))
                    return new Ellipse2D.Float(centerX, centerY, width, height);
                if (shape.equals("rectangle"))
                    return new RoundRectangle2D.Double(centerX, centerY, width, height, 10, 10);
            }
            return new RoundRectangle2D.Double(-50, -20, 130, 40,40,40);
        }
    }
}
