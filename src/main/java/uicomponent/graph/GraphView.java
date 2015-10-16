package uicomponent.graph;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.picking.PickedState;
import jastaddad.ASTAPI;
import jastaddad.FilteredTreeNode;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import javafx.embed.swing.SwingNode;
import org.apache.commons.collections15.Transformer;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by gda10jli on 10/15/15.
 */
public class GraphView extends SwingNode {

    private int id;
    private ASTAPI api;
    private VisualizationViewer vs;

    public GraphView(ASTAPI api){
        this.id = 0;
        this.api = api;
        init();
    }

    public void init(){
        //Creates the tree with the nodes
        Forest<FilteredTreeNode, String> g = new DelegateForest<>();
        addToTree(g, api.filteredTree);

        //Set ui specific stuff
        TreeLayout<FilteredTreeNode, String> layout = new TreeLayout<>(g, 100, 100);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        vs = new VisualizationViewer<>(layout, screenSize);
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
                System.out.println("HATA LIVET LITE MER");
            }
        });

        DefaultModalGraphMouse<FilteredTreeNode, String> picker = new DefaultModalGraphMouse<>();
        picker.setMode(ModalGraphMouse.Mode.PICKING);
        vs.setGraphMouse(picker);

        Transformer <FilteredTreeNode, Shape> vertexShape = e -> new Rectangle(-25,-20,50,40);
        vs.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<FilteredTreeNode, String>());
        vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vs.getRenderContext().setVertexShapeTransformer(vertexShape);
        ScalingControl visualizationViewerScalingControl= new CrossoverScalingControl();
        vs.scaleToLayout(visualizationViewerScalingControl);

        setContent(vs);
    }

    private void addToTree(Forest<FilteredTreeNode, String> g, FilteredTreeNode parent) {
        for (FilteredTreeNode child : parent.getChildren()) {
            g.addEdge("jup" + id, parent, child);
            id++;
            addToTree(g, child);
        }
    }


}
