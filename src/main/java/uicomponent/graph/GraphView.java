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
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import org.apache.commons.collections15.Transformer;
import uicomponent.UIMonitor;
import uicomponent.controllers.Controller;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by gda10jli on 10/15/15.
 */
public class GraphView extends SwingNode {

    private int id;
    private UIMonitor mon;
    private Controller con;
    private VisualizationViewer vs;

    public GraphView(UIMonitor mon, Controller con){
        this.id = 0;
        this.mon = mon;
        this.con = con;
        Forest<FilteredTreeNode, String> g = new DelegateForest<>();
        createTree(g, mon.getRootNode());
        createLayout(g);
        setListeners();
        setContent(vs);
    }

    private void createTree(Forest<FilteredTreeNode, String> g, FilteredTreeNode parent){
        for (FilteredTreeNode child : parent.getChildren()) {
            g.addEdge("jup" + id, parent, child);
            id++;
            createTree(g, child);
        }
    }

    public void createLayout(Forest<FilteredTreeNode, String> g ){//Creates UI specific stuff

        TreeLayout<FilteredTreeNode, String> layout = new TreeLayout<>(g, 100, 100);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Transformer <FilteredTreeNode, Shape> vertexShape = e -> new Rectangle(-25,-20,50,40);
        ScalingControl visualizationViewerScalingControl= new CrossoverScalingControl();

        vs = new VisualizationViewer<>(layout, screenSize);
        vs.scaleToLayout(visualizationViewerScalingControl);

        vs.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<FilteredTreeNode, String>());
        vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vs.getRenderContext().setVertexShapeTransformer(vertexShape);
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

        DefaultModalGraphMouse<FilteredTreeNode, String> picker = new DefaultModalGraphMouse<>();
        picker.setMode(ModalGraphMouse.Mode.PICKING);
        vs.setGraphMouse(picker);
    }



}
