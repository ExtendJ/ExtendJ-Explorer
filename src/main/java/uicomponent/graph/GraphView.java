package uicomponent.graph;

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

import java.awt.*;

/**
 * Created by gda10jli on 10/15/15.
 */
public class GraphView {

    private int id;
    private Node root;

    private static final int CIRCLE_SIZE = 15; // default circle size

    public GraphView(Node root){
        this.id = 0;
        this.root = root;
    }

    public SwingNode getChild(){
        Forest<Node, String> g = new DelegateForest<Node, String>();
        addToTree(g, root);
        VisualizationImageServer vs = new VisualizationImageServer(new TreeLayout(g), new Dimension(200, 200));

        float dash[] = {10.0f};
        final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        Transformer<String, Stroke> edgeStrokeTransformer = new Transformer<String, Stroke>() {
            public Stroke transform(String s) {
                return edgeStroke;
            }
        };

        vs.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        SwingNode sw = new SwingNode();
        ViewUtil.createSwingContent(sw, vs);
        return sw;
    }

    private void addToTree(Forest<Node, String> g, Node parent) {
        for (Node child : parent.children) {
            g.addEdge("jup" + id, parent, child);
            id++;
            addToTree(g, child);
        }
    }

}
