package uicomponent.graph;

import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.*;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import jastaddad.filteredtree.FilteredTreeItem;
import jastaddad.filteredtree.FilteredTreeNode;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import org.apache.commons.collections15.Transformer;
import uicomponent.UIMonitor;
import uicomponent.controllers.Controller;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

/**
 * Created by gda10jli on 10/15/15.
 */
public class GraphView extends SwingNode {

    private int id;
    private UIMonitor mon;
    private Controller con;
    private VisualizationViewer vs;
    private Forest<FilteredTreeItem, UIEdge> g;
    private UIEdge root;

    public GraphView(UIMonitor mon, Controller con){
        this.id = 0;
        this.mon = mon;
        this.con = con;
        g = new DelegateForest<>();

        root = null;

        createTree(g, mon.getRootNode());
        createLayout(g);
        setListeners();
        setContent(vs);
    }

    private void createTree(Forest<FilteredTreeItem, UIEdge> g, FilteredTreeItem parent){
        for (FilteredTreeItem child : parent.getChildren()) {
            UIEdge edge;
            if(child.isNode()){
                FilteredTreeNode n = (FilteredTreeNode) child;
                edge = new UIEdge(parent.isRealChild(child), n.node.name);
            }else {
                edge = new UIEdge(parent.isRealChild(child));
            }
            g.addEdge(edge, parent, child);
            id++;
            createTree(g, child);
        }
    }

    public void updateGraph(){

        g = new DelegateForest<>();
        createTree(g, mon.getRootNode());
        createLayout(g);
        setListeners();
        setContent(vs);
    }

    public void createLayout(Forest<FilteredTreeItem, UIEdge> g ){//Creates UI specific stuff

        TreeLayout<FilteredTreeItem, UIEdge> layout = new TreeLayout<>(g, 150, 100);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Transformer <FilteredTreeItem, Shape> vertexShape = fNode -> {
            //CompositeShape shape = new CompositeShape();
            if(!fNode.isNode())
                return new RoundRectangle2D.Double(-50, -20, 130, 40,0,0);
            if(((FilteredTreeNode)fNode).node.isList())
                return new RoundRectangle2D.Double(-50, -20, 130, 40,10,10);
            return new RoundRectangle2D.Double(-50, -20, 130, 40,40,40);
        };

        float dash[] = {10.0f};
        final Stroke dashedEdgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        final Stroke edgeStroke = new BasicStroke(1.0f);
        Transformer<UIEdge, Stroke> edgeStrokeTransformer = edge -> {
            if(edge.isRealChild())
                return edgeStroke;
            return dashedEdgeStroke;

        };

        ScalingControl visualizationViewerScalingControl = new CrossoverScalingControl();

        vs = new VisualizationViewer<>(layout, screenSize);
        vs.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        vs.scaleToLayout(visualizationViewerScalingControl);


        vs.getRenderContext().setVertexFillPaintTransformer(new VertexPaintTransformer(vs.getPickedVertexState()));
        vs.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<FilteredTreeNode, String>());
        vs.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vs.getRenderContext().setVertexShapeTransformer(vertexShape);
        vs.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
    }

    public void setListeners(){//Sets UI listeners of the graph

        final PickedState<String> pickedState = vs.getPickedVertexState();
        pickedState.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(con);
                        con.itemStateChanged(e);
                    }
                });
            }
        });
        PluggableGraphMouse gm = new PluggableGraphMouse();
        gm.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON2_MASK));
        gm.add(new PickingGraphMousePlugin());
        gm.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, 1.1f, 0.9f));

        //DefaultModalGraphMouse<FilteredTreeNode, UIEdge> picker = new DefaultModalGraphMouse<>();
        //gm.setMode(ModalGraphMouse.Mode.PICKING);
        vs.setGraphMouse(gm);
    }

    private static class VertexPaintTransformer implements Transformer<FilteredTreeItem,Paint> {
        private final PickedInfo<FilteredTreeItem> pi;
        VertexPaintTransformer ( PickedInfo<FilteredTreeItem> pi ) {
            super();
            if (pi == null)
                throw new IllegalArgumentException("PickedInfo instance must be non-null");
            this.pi = pi;
        }
        @Override
        public Paint transform(FilteredTreeItem fNode) {
            if(pi.isPicked(fNode))
                return new Color(240, 240, 240);
            if(!fNode.isNode())
                return new Color(140,150, 32);
            if(((FilteredTreeNode)fNode).node.isList()) return new Color(200, 200, 200);
            return new Color(200, 240, 230);
        }
    }

}
