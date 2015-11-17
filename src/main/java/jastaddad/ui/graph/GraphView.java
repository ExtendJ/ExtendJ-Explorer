package jastaddad.ui.graph;

import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DirectedOrderedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.RenderContext;
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
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import org.apache.commons.collections15.Transformer;
import jastaddad.ui.UIMonitor;
import jastaddad.ui.controllers.Controller;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gda10jli on 10/15/15.
 */
public class GraphView extends SwingNode implements ItemListener {
    private UIMonitor mon;
    private Controller con;
    private VisualizationViewer vs;
    private DelegateForest<GenericTreeNode, UIEdge> graph;

    public GraphView(UIMonitor mon){
        this.mon = mon;
        this.con = mon.getController();
        DirectedOrderedSparseMultigraph n = new DirectedOrderedSparseMultigraph();
        graph = new DelegateForest<>(n);
        createTree(graph, mon.getRootNode());
        createLayout(graph);
        setListeners();
        addDisplayedReferences();
        setContent(vs);
    }

    private void createTree(Forest<GenericTreeNode, UIEdge> g, GenericTreeNode parent){

        for (GenericTreeNode child : parent.getChildren()) {
            UIEdge edge = null;
            if(child.isNode()){
                TreeNode n = (TreeNode) child;
                if(parent.isNode() && !((TreeNode)parent).getNode().isOpt())
                    edge = new UIEdge(parent.isRealChild(child), n.getNode().name);
                else
                    edge = new UIEdge(parent.isRealChild(child));
            }else {
                edge = new UIEdge(parent.isRealChild(child));
            }
            g.addEdge(edge, parent, child);
            createTree(g, child);
        }
    }

    public void updateGraph(){
        DirectedOrderedSparseMultigraph n = new DirectedOrderedSparseMultigraph();
        graph = new DelegateForest<>(n);
        createTree(graph, mon.getRootNode());
        vs.getGraphLayout().setGraph(graph);
        addDisplayedReferences();
        vs.repaint();
    }

    public void setSelectedNode(GenericTreeNode node){
        vs.getPickedVertexState().removeItemListener(this);
        vs.getPickedVertexState().pick(node, true);
        vs.getPickedVertexState().addItemListener(this);
    }

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

    public void createLayout(Forest<GenericTreeNode, UIEdge> g ){//Creates UI specific stuff

        TreeLayout<GenericTreeNode, UIEdge> layout = new TreeLayout<>(g, 150, 100);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Transformer <GenericTreeNode, Shape> vertexShape = fNode -> {
            //CompositeShape shape = new CompositeShape();
            String shape = fNode.getStyles().get("node-shape").getStr();
            //System.out.println("Node: " + "asdasd" + " shape: " + shape);
            if(shape != null) {
                if (shape.equals("rounded_rectangle"))
                    return new RoundRectangle2D.Double(-50, -20, 130, 40, 40, 40);
                if (shape.equals("small_circle"))
                    return new Ellipse2D.Float(-20, -20, 40, 40);
                if (shape.equals("rectangle"))
                    return new RoundRectangle2D.Double(-50, -20, 130, 40, 10, 10);
            }
            return new RoundRectangle2D.Double(-50, -20, 130, 40,40,40);
        };

        Transformer <GenericTreeNode, String> toStringTransformer = fNode -> fNode.toGraphString();

        Transformer<UIEdge, Paint> edgePaintTransformer = edge -> edge.getColor();

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

        Transformer<GenericTreeNode, Stroke> vertexStrokeTransformer = item -> {
            String border = item.getStyles().get("border-style").getStr();
            if(border.equals("dashed"))
                return dashedStroke;
            return normalStroke;
        };
        ScalingControl visualizationViewerScalingControl = new CrossoverScalingControl();

        vs = new VisualizationViewer<>(layout, screenSize);
        vs.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        vs.scaleToLayout(visualizationViewerScalingControl);

        vs.getRenderContext().setVertexStrokeTransformer(vertexStrokeTransformer);
        vs.getRenderContext().setVertexFillPaintTransformer(new VertexPaintTransformer(vs.getPickedVertexState(), mon));
        vs.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<>());
        vs.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vs.getRenderContext().setVertexLabelTransformer(toStringTransformer);
        vs.getRenderContext().setVertexShapeTransformer(new VertexShapeTransformer(mon, vs.getRenderContext()));
        vs.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        vs.getRenderContext().setEdgeDrawPaintTransformer(edgePaintTransformer);

    }

    public void setListeners(){//Sets UI listeners of the graph

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

    public void newNodeSelected(GenericTreeNode node) {
        vs.getPickedVertexState().clear();
        vs.getPickedVertexState().pick(node, true);
        vs.repaint();
    }

    public void deselectNode(){
        vs.getPickedVertexState().clear();
    }

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
        @Override
        public Paint transform(GenericTreeNode fNode) {
            String color = fNode.getStyles().get("node-color").getColor();
            if(pi.isPicked(fNode)) {
                return new Color(240, 240, 200);
            }
            if(fNode.isReferenceHighlight())
                return new Color(80, 180, 80);
            try{
                return Color.decode(color);
            }catch (NumberFormatException e){
                e.printStackTrace();
                return new Color(200, 240, 230);
            }
        }
    }

    private class VertexShapeTransformer extends VertexLabelAsShapeRenderer<GenericTreeNode, UIEdge> {
        private final UIMonitor mon;

        VertexShapeTransformer ( UIMonitor mon , RenderContext<GenericTreeNode, UIEdge> rc) {
            super(rc);
            this.mon = mon;

        }

        @Override
        public Shape transform(GenericTreeNode fNode) {
            Component component = prepareRenderer(rc, rc.getVertexLabelRenderer(), rc.getVertexLabelTransformer().transform(fNode),
                    rc.getPickedVertexState().isPicked(fNode), fNode);
            Dimension size = component.getPreferredSize();

            int centerX = -size.width/2 -10;
            int centerY = -size.height/2 -10;
            int height = size.height+20;
            int width = size.width+20;

            if(fNode.isNode() && width < 130){
                width = 130;
                centerX = -65;
            }
            if(!fNode.isNode()){
                width = 40;
                height = 40;
                centerX = -20;
                centerY = -20;
            }

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