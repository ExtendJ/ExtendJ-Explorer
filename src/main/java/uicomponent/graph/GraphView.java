package uicomponent.graph;

import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DirectedOrderedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.*;
import edu.uci.ics.jung.visualization.decorators.AbstractEdgeShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.AbstractVertexShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import jastaddad.filteredtree.GenericTreeNode;
import jastaddad.filteredtree.TreeNode;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import org.apache.commons.collections15.Transformer;
import uicomponent.UIMonitor;
import uicomponent.controllers.Controller;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by gda10jli on 10/15/15.
 */
public class GraphView extends SwingNode {

    private int id;
    private UIMonitor mon;
    private Controller con;
    private VisualizationViewer vs;
    private DelegateForest<GenericTreeNode, UIEdge> g;
    private UIEdge root;

    public GraphView(UIMonitor mon, Controller con){
        this.id = 0;
        this.mon = mon;
        this.con = con;
        DirectedOrderedSparseMultigraph n = new DirectedOrderedSparseMultigraph();
        g = new DelegateForest<>(n);
        root = null;
        createTree(g, mon.getRootNode());
        createLayout(g);
        setListeners();
        setContent(vs);
    }

    private void createTree(Forest<GenericTreeNode, UIEdge> g, GenericTreeNode parent){
        for (GenericTreeNode child : parent.getChildren()) {
            UIEdge edge = null;
            if(child.isNode()){
                TreeNode n = (TreeNode) child;
                if(parent.isNode() && !((TreeNode)parent).node.isOpt())
                    edge = new UIEdge(parent.isRealChild(child), n.node.name);
                else
                    edge = new UIEdge(parent.isRealChild(child));
            }else {
                edge = new UIEdge(parent.isRealChild(child));
            }
            g.addEdge(edge, parent, child);
            id++;
            createTree(g, child);
        }
    }

    public void updateGraph(){
        DirectedOrderedSparseMultigraph n = new DirectedOrderedSparseMultigraph();
        g = new DelegateForest<>(n);
        createTree(g, mon.getRootNode());
        vs.getGraphLayout().setGraph(g);
        vs.repaint();
    }

    public void setReferenceEdge(GenericTreeNode newRef, GenericTreeNode ref){
        UIEdge edge = mon.getReferenceEdge();
        if(edge != null)
            g.removeEdge(edge, false);
        if(newRef == null) {
            vs.repaint();
            return;
        }
        edge = new UIEdge();
        g.addEdge(edge, ref, newRef.hasClusterReference() ? newRef.getClusterReference() : newRef);
        mon.setReferenceEdge(edge);
        vs.repaint();
    }

    public void createLayout(Forest<GenericTreeNode, UIEdge> g ){//Creates UI specific stuff

        TreeLayout<GenericTreeNode, UIEdge> layout = new TreeLayout<>(g, 150, 100);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Transformer <GenericTreeNode, Shape> vertexShape = fNode -> {
            if(!fNode.isNode())
                return new Ellipse2D.Float(-20, -20, 40, 40);
            if(((TreeNode)fNode).node.isList())
                return new RoundRectangle2D.Double(-50, -20, 130, 40,10,10);
            return new RoundRectangle2D.Double(-50, -20, 130, 40,40,40);
        };

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
            if(!item.isNode())
                return dashedStroke;
            return normalStroke;
        };

        Transformer<UIEdge, Paint> edgePaintTransformer = edge -> {
            if(edge.isReference())
                return new Color(80, 180 , 80);
            return new Color(0, 0, 0);
        };

        ScalingControl visualizationViewerScalingControl = new CrossoverScalingControl();

        vs = new VisualizationViewer<>(layout, screenSize);
        vs.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        vs.scaleToLayout(visualizationViewerScalingControl);

        vs.getRenderContext().setVertexStrokeTransformer(vertexStrokeTransformer);
        vs.getRenderContext().setVertexFillPaintTransformer(new VertexPaintTransformer(vs.getPickedVertexState()));
        vs.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<>());
        vs.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vs.getRenderContext().setVertexShapeTransformer(vertexShape);
        vs.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        vs.getRenderContext().setEdgeDrawPaintTransformer(edgePaintTransformer);

    }

    public void setListeners(){//Sets UI listeners of the graph

        final PickedState<String> pickedState = vs.getPickedVertexState();
        pickedState.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        con.itemStateChanged(e);
                    }
                });
            }
        });
        PluggableGraphMouse gm = new PluggableGraphMouse();
        gm.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON2_MASK));
        gm.add(new PickingGraphMousePlugin());
        gm.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, 1.1f, 0.9f));
        vs.setGraphMouse(gm);
        
    }

    private static class VertexPaintTransformer implements Transformer<GenericTreeNode,Paint> {
        private final PickedInfo<GenericTreeNode> pi;
        VertexPaintTransformer ( PickedInfo<GenericTreeNode> pi ) {
            super();
            if (pi == null)
                throw new IllegalArgumentException("PickedInfo instance must be non-null");
            this.pi = pi;
        }
        @Override
        public Paint transform(GenericTreeNode fNode) {
            if(pi.isPicked(fNode))
                return new Color(240, 240, 200);
            if(fNode.isReferenceHighlight())
                return new Color(80, 180, 80);
            if(!fNode.isNode())
                return new Color(220,220, 220);
            if(((TreeNode)fNode).node.isList()) return new Color(200, 200, 200);
            return new Color(200, 240, 230);
        }
    }

}
