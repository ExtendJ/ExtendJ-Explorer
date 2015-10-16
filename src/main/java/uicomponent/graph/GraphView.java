package uicomponent.graph;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
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
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * Created by gda10jli on 10/15/15.
 */
public class GraphView {

    private int id;
    private Node root;

    public GraphView(Node root){
        this.id = 0;
        this.root = root;
    }

    public SwingNode getChild(){
        Forest<Node, String> g = new DelegateForest<>();
        addToTree(g, root);
        TreeLayout<Node, String> layout = new TreeLayout<Node, String>(g, 100, 100);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        VisualizationImageServer vs = new VisualizationImageServer(layout, screenSize);

        float dash[] = {10.0f};
        final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);

        Transformer <Node, Shape> vertexShape = e -> new Rectangle(-25,-20,50,40);

        vs.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Node,String>());
        vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vs.getRenderContext().setVertexShapeTransformer(vertexShape);

        ScalingControl visualizationViewerScalingControl
                = new CrossoverScalingControl();
        vs.scaleToLayout(visualizationViewerScalingControl);
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
