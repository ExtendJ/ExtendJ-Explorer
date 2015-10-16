package uicomponent.graph;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.picking.PickedState;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import uicomponent.ViewUtil;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import jastaddad.Node;
import javafx.embed.swing.SwingNode;
import javafx.scene.layout.Region;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;

/**
 * Created by gda10jli on 10/15/15.
 */
public class GraphView extends SwingNode {

    private int id;
    private Node root;
    private VisualizationViewer vs;

    private static final int CIRCLE_SIZE = 15; // default circle size

    public GraphView(Node root){
        this.id = 0;
        this.root = root;
        init();
    }

    public void init(){
        Forest<Node, String> g = new DelegateForest<Node, String>();
        addToTree(g, root);

        vs = new VisualizationViewer<Node, String>(new TreeLayout(g), new Dimension(200, 200));
        float dash[] = {10.0f};
        final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);

        Transformer<String, Stroke> edgeStrokeTransformer = new Transformer<String, Stroke>() {
            public Stroke transform(String s) {
                return edgeStroke;
            }
        };

        final PickedState<String> pickedState = vs.getPickedVertexState();

        // Attach the listener that will print when the vertices selection changes.
        pickedState.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                System.out.println("HATA LIVET LITE MER");
            }
        });

        DefaultModalGraphMouse<Node, String> picker = new DefaultModalGraphMouse<Node, String>();
        picker.setMode(ModalGraphMouse.Mode.PICKING);
        vs.setGraphMouse(picker);

        vs.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
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
