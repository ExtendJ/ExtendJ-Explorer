package uicomponent.graph;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import jastaddad.Node;
import javafx.embed.swing.SwingNode;
import org.apache.commons.collections15.Transformer;
import uicomponent.UIMonitor;

import javax.management.monitor.Monitor;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by gda10jli on 10/15/15.
 */
public class GraphView extends SwingNode {

    private int id;
    private UIMonitor mon;
    private VisualizationViewer vs;

    public GraphView(UIMonitor mon){
        this.id = 0;
        this.mon = mon;
        init();
    }

    public void init(){
        //Creates the tree with the nodes
        Forest<Node, String> g = new DelegateForest<Node, String>();
        addToTree(g, mon.getRootNode());

        //Set ui specific stuff
        TreeLayout<Node, String> layout = new TreeLayout<Node, String>(g, 100, 100);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        vs = new VisualizationViewer<Node, String>(layout, screenSize);
        float dash[] = {10.0f};
        final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);

        Transformer<String, Stroke> edgeStrokeTransformer = new Transformer<String, Stroke>() {
            public Stroke transform(String s) {
                return edgeStroke;
            }
        };

        final PickedState<String> pickedState = vs.getPickedVertexState();

        pickedState.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Object subject = e.getItem();
                if(subject instanceof Node)
                    mon.setSelectedNode((Node) subject);
            }
        });

        DefaultModalGraphMouse<Node, String> picker = new DefaultModalGraphMouse<Node, String>();
        picker.setMode(ModalGraphMouse.Mode.PICKING);
        vs.setGraphMouse(picker);

        Transformer <Node, Shape> vertexShape = e -> new Rectangle(-25,-20,50,40);
        vs.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Node,String>());
        vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vs.getRenderContext().setVertexShapeTransformer(vertexShape);
        ScalingControl visualizationViewerScalingControl= new CrossoverScalingControl();
        vs.scaleToLayout(visualizationViewerScalingControl);

        setContent(vs);
    }

    private void addToTree(Forest<Node, String> g, Node parent) {
        for (Node child : parent.children) {
            g.addEdge("jup" + id, parent, child);
            id++;
            addToTree(g, child);
        }
        
    }


}
