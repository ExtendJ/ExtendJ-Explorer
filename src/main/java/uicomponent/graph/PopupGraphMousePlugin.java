package uicomponent.graph;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import jastaddad.filteredtree.GenericTreeNode;
import jastaddad.filteredtree.TreeCluster;
import jastaddad.filteredtree.TreeNode;
import uicomponent.UIMonitor;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import javax.swing.*;

/**
 * A GraphMousePlugin that brings up distinct popup menus when an edge or vertex is
 * appropriately clicked in a graph.  If these menus contain components that implement
 * either the EdgeMenuListener or VertexMenuListener then the corresponding interface
 * methods will be called prior to the display of the menus (so that they can display
 * context sensitive information for the edge or vertex).
 * @author Dr. Greg M. Bernstein
 */
class PopupGraphMousePlugin<V, E> extends AbstractPopupGraphMousePlugin{
    private JPopupMenu edgePopup;
    private JPopupMenu vertexPopup;
    private VisualizationViewer<GenericTreeNode, UIEdge> vs;
    private GenericTreeNode lastClicked;
    private UIMonitor mon;

    /** Creates a new instance of PopupVertexEdgeMenuMousePlugin */
    public PopupGraphMousePlugin(VisualizationViewer<GenericTreeNode, UIEdge> vs, UIMonitor mon) {
        this(MouseEvent.BUTTON3_MASK, vs, mon);
    }

    /**
     * Creates a new instance of PopupVertexEdgeMenuMousePlugin
     * @param modifiers mouse event modifiers see the jung visualization Event class.
     */
    public PopupGraphMousePlugin(int modifiers, VisualizationViewer<GenericTreeNode, UIEdge> vs, UIMonitor mon) {
        super(modifiers);
        this.vs = vs;
        this.mon = mon;
        setVertexPopup();
    }

    /**
     * Implementation of the AbstractPopupGraphMousePlugin method. This is where the
     * work gets done. You shouldn't have to modify unless you really want to...
     * @param e
     */
    protected void handlePopup(MouseEvent e) {
        Point2D p = e.getPoint();
        GraphElementAccessor<GenericTreeNode, UIEdge> pickSupport = vs.getPickSupport();
        if(pickSupport != null) {
            lastClicked = pickSupport.getVertex(vs.getGraphLayout(), p.getX(), p.getY());
            if(lastClicked != null) {
                //System.out.println("Vertex " + v + " was right clicked");
                if(!(lastClicked.isCluster() && !((TreeCluster)lastClicked).isExpandable()))
                    vertexPopup.show(vs, e.getX(), e.getY());
            } else {
                /*
                final E edge = pickSupport.getEdge(vv.getGraphLayout(), p.getX(), p.getY());
                if(edge != null) {
                    System.out.println("Edge " + edge + " was right clicked");
                    updateEdgeMenu(edge, vv, p);
                    edgePopup.show(vv, e.getX(), e.getY());

                }
                */
            }
        }
    }

    /**
     * Getter for the edge popup.
     * @return
     */
    public JPopupMenu getEdgePopup() {
        return edgePopup;
    }

    /**
     * Setter for the Edge popup.
     */
    private void setEdgePopup() {
    }

    /**
     * Getter for the vertex popup.
     * @return
     */
    public JPopupMenu getVertexPopup() {
        return vertexPopup;
    }

    /**
     * Setter for the vertex popup.
     */
    public void setVertexPopup() {
        vertexPopup = new JPopupMenu();
        JMenuItem item;
        vertexPopup.add(item = new JMenuItem("Toggle collapse"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(e -> {
            if(lastClicked.isNode())
                collapse();
            else{
                expand();
            }
            });
        vertexPopup.addSeparator();
        vertexPopup.add(new JMenuItem("More is coming"));
    }

    private void expand(){
        if(lastClicked.isCluster()){
            DelegateForest<GenericTreeNode, UIEdge> inGraph = (DelegateForest<GenericTreeNode, UIEdge>) vs.getGraphLayout().getGraph();
            TreeCluster cluster = (TreeCluster) lastClicked;
            GenericTreeNode node = cluster.getClusterRoot();

            GenericTreeNode parent = null;
            UIEdge edge = null;
            if(inGraph.getPredecessorCount(lastClicked) > 0) {
                parent = inGraph.getPredecessors(lastClicked).iterator().next();
                edge = inGraph.getInEdges(lastClicked).iterator().next();
            }
            Point2D d = (Point2D) vs.getGraphLayout().transform(lastClicked);
            Point2D d1 = new Point((int)d.getX(), (int)d.getY()+200);

            if(mon.getReferenceEdges() != null) {
                for (UIEdge e : mon.getReferenceEdges()) {
                    inGraph.removeEdge(e, false);
                }
            }

            createTree(inGraph, node);

            inGraph.removeVertex(lastClicked, false);
            if (parent != null) {
                inGraph.addEdge(edge, parent, node);
            }else{
                System.out.println("Top node");
            }
            setClusterRef(null, node);

            mon.getController().resetReferences();

            inGraph.removeVertex(lastClicked, false);
            lastClicked = node;

            vs.getGraphLayout().setLocation(node, d);
            vs.repaint();
        }
    }

    private void collapse(){
        // get a sub tree from subRoot
        if(lastClicked.isNode()) {
            DelegateForest<GenericTreeNode, UIEdge> inGraph = (DelegateForest<GenericTreeNode, UIEdge>) vs.getGraphLayout().getGraph();
            TreeCluster newCluster = new TreeCluster(lastClicked);
            newCluster.setExpandable(true);
            newCluster.setStyles(mon.getApi().getfilterConfig());

            if(mon.getReferenceEdges() != null) {
                for (UIEdge e : mon.getReferenceEdges()) {
                    inGraph.removeEdge(e, false);
                }
            }

            GenericTreeNode parent = null;
            UIEdge edge = null;
            if(inGraph.getPredecessorCount(lastClicked) > 0) {
                parent = inGraph.getPredecessors(lastClicked).iterator().next();
                edge = inGraph.getInEdges(lastClicked).iterator().next();
            }

            Point2D d = (Point2D) vs.getGraphLayout().transform(lastClicked);
            setClusterRef(newCluster, newCluster.getClusterRoot());
            //Platform.runLater(() -> {
                mon.getController().resetReferences();
            //});

            inGraph.removeVertex(lastClicked);
            if (parent != null) {
                inGraph.addEdge(edge, parent, newCluster);
            } else {
                inGraph.addVertex(newCluster);
            }

            vs.getGraphLayout().setLocation(newCluster, d);
            vs.repaint();
        }
    }

    private void setClusterRef(GenericTreeNode cluster, GenericTreeNode node){
        node.setClusterReference(cluster);
        for(GenericTreeNode child : node.getChildren())
            setClusterRef(cluster, child);
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
            createTree(g, child);
        }
    }
}

