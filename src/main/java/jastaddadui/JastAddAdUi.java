package jastaddadui;

import java.awt.*;
import javax.swing.JFrame;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import jastaddad.Node;
import org.apache.commons.collections15.Transformer;

public class JastAddAdUi {
    private int id;
    public JastAddAdUi( Node root){
        id = 0;
        Forest<Node, String> g = new DelegateForest<Node, String>();

        addToTree(g, root);

        VisualizationImageServer vs =
                new VisualizationImageServer(
                        new TreeLayout(g), new Dimension(200, 200));


        float dash[] = {10.0f};
        final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        Transformer<String, Stroke> edgeStrokeTransformer = new Transformer<String, Stroke>() {
                    public Stroke transform(String s) {
                        return edgeStroke;
                    }
                };

        vs.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);

        vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

        JFrame frame = new JFrame();
        frame.getContentPane().add(vs);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    private void addToTree(Forest<Node, String> g, Node parent){
        for(Node child : parent.children){
            g.addEdge("jup"+id, parent, child);
            id++;
            addToTree(g, child);
        }
    }
}