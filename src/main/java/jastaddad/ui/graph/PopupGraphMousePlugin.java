package jastaddad.ui.graph;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import jastaddad.api.filteredtree.GenericTreeNode;
import jastaddad.api.filteredtree.NodeReference;
import jastaddad.api.filteredtree.TreeCluster;
import jastaddad.api.filteredtree.TreeNode;
import jastaddad.ui.UIMonitor;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * A PopupGraphMousePlugin that brings up distinct popup menus when an edge or vertex is
 * appropriately clicked in a graph.
 *
 * (Might add EdgeMenuListener or VertexMenuListener later)
 */
class PopupGraphMousePlugin<V, E> extends AbstractPopupGraphMousePlugin{
    private JPopupMenu edgePopup;
    private JPopupMenu vertexPopup;
    private VisualizationViewer<GenericTreeNode, UIEdge> vs;
    private GenericTreeNode lastClicked;
    private UIMonitor mon;
    private GraphView graphView;

    public PopupGraphMousePlugin(VisualizationViewer<GenericTreeNode, UIEdge> vs, UIMonitor mon, GraphView graphView) {
        this(MouseEvent.BUTTON3_MASK, vs, mon, graphView);
    }

    public PopupGraphMousePlugin(int modifiers, VisualizationViewer<GenericTreeNode, UIEdge> vs, UIMonitor mon, GraphView graphView) {
        super(modifiers);
        this.vs = vs;
        this.mon = mon;
        this.graphView = graphView;
        setVertexPopup();
    }

    /**
     * Implementation of the AbstractPopupGraphMousePlugin method. This is where the
     * work gets done.
     * @param e
     */
    protected void handlePopup(MouseEvent e) {
        Point2D p = e.getPoint();
        GraphElementAccessor<GenericTreeNode, UIEdge> pickSupport = vs.getPickSupport();
        if(pickSupport != null) {
            // get the selected vertex (if there is one)
            lastClicked = pickSupport.getVertex(vs.getGraphLayout(), p.getX(), p.getY());
            if(lastClicked != null) {
                //System.out.println("Vertex " + v + " was right clicked");
                if(lastClicked.isExpandable())
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

    /**
     * Called when it's time to collapse a vertex in the graph.
     *
     * This collapse is a UI specific feature, and have nothing to do with the API. The changes are done in the Jung2
     * graph and not in the api filtered AST. That means that the collapse is only for as long as the session of the
     * program is alive or the filter button is clicked.
     *
     * lastClicked is the node that is collapsed.
     */
    private void collapse(){
        if(lastClicked.isNode()) {
            DelegateForest<GenericTreeNode, UIEdge> inGraph = (DelegateForest<GenericTreeNode, UIEdge>) vs.getGraphLayout().getGraph();
            TreeCluster newCluster = new TreeCluster(lastClicked, lastClicked.getParent());
            newCluster.setExpandable(true);
            newCluster.setStyles(mon.getApi().getfilterConfig());

            // remove all reference edges
            if(mon.getReferenceEdges() != null) {
                for (UIEdge e : mon.getReferenceEdges()) {
                    inGraph.removeEdge(e, false);
                }
            }

            // store the parent vertex the edge pointing to the collapsed vertex
            GenericTreeNode parent = null;
            UIEdge edge = null;
            if(lastClicked.getParent() != null) {
                parent = lastClicked.getParent().getClusterNode();
                for(UIEdge e : inGraph.getInEdges(lastClicked)) {
                    if (!e.isReference())
                        edge = e;
                }
            }
            // store the position of the node
            Point2D d = vs.getGraphLayout().transform(lastClicked);
            // set the cluster reference of all children to the new cluster
            setClusterRef(newCluster, newCluster.getClusterRoot());

            mon.getController().resetReferences();

            // remove vertexes
            HashSet<NodeReference> nodeRef = new HashSet<>();
            removeVertexes(lastClicked, inGraph, nodeRef);

            // add references to the new cluster
            graphView.addDisplayedReferences(new ArrayList<>(nodeRef));

            if (parent != null) {
                inGraph.addEdge(edge, parent, newCluster);
            } else {
                inGraph.addVertex(newCluster);
            }

            vs.getGraphLayout().setLocation(newCluster, d);
            vs.repaint();
        }
    }

    /**
     * Called when it's time to expand a collapsed vertex in the graph.
     *
     * This expand is a UI specific feature, and have nothing to do with the API. The changes are done in the Jung2
     * graph and not in the api filtered AST. That means that the expand is only for as long as the session of the
     * program is alive or the filter button is clicked.
     *
     * lastClicked is the node that will be expanded.
     */
    private void expand(){
        if(lastClicked.isCluster()){
            DelegateForest<GenericTreeNode, UIEdge> inGraph = (DelegateForest<GenericTreeNode, UIEdge>) vs.getGraphLayout().getGraph();
            TreeCluster cluster = (TreeCluster) lastClicked;
            GenericTreeNode node = cluster.getClusterRoot();

            // get the parent vertex and the edge from this one.
            GenericTreeNode parent = null;
            UIEdge edge = null;
            if(lastClicked.getParent() != null) {
                parent = node.getParent().getClusterNode();
                for(UIEdge e : inGraph.getInEdges(lastClicked)) {
                    if (!e.isReference()) {
                        edge = e;
                    }
                }
            }
            // remember the position of the vertex
            Point2D d =  vs.getGraphLayout().transform(lastClicked);

            // remove all references to this vertex
            if(mon.getReferenceEdges() != null) {
                for (UIEdge e : mon.getReferenceEdges()) {
                    inGraph.removeEdge(e, false);
                }
            }

            ArrayList<NodeReference> nodeRef = new ArrayList<>();
            // add all child vertexes
            createTree(inGraph, node, nodeRef);
            setClusterRef(null, node);

            // add references to the children
            graphView.addDisplayedReferences(nodeRef);


            // remove the cluster vertex
            inGraph.removeVertex(lastClicked, false);
            if (parent != null && edge != null) {
                inGraph.addEdge(edge, parent, node);
            }else{
                System.out.println("Top node");
            }

            // add references to the children
            mon.getController().resetReferences();

            //inGraph.removeVertex(lastClicked, false);
            lastClicked = node;

            vs.getGraphLayout().setLocation(node, d);
            vs.repaint();
        }
    }

    /**
     * Recursively remove all child vertexes of the vertex parent.
     * First store all reference edges that points to this vertex and put them inside nodeRef. Then remove all reference
     * edges from this vertex. And last remove the vertex.
     *
     * @param parent
     * @param inGraph
     * @param nodeRef
     */
    private void removeVertexes(GenericTreeNode parent, DelegateForest<GenericTreeNode, UIEdge> inGraph, HashSet<NodeReference> nodeRef){
        // Store references to the vertex
        if(parent.getAllNodeReferences() != null)
            nodeRef.addAll(parent.getAllNodeReferences());

        // Remove references from the vertex
        if(mon.getDisplayedReferenceEdges() != null && mon.getDisplayedReferenceEdges().get(parent) != null) {
            for (UIEdge e : mon.getDisplayedReferenceEdges().get(parent)) {
                inGraph.removeEdge(e, false);
            }
        }

        // Remove all children
        for(GenericTreeNode child : inGraph.getChildren(parent))
            removeVertexes(child, inGraph, nodeRef);

        // Remove this vertex
        inGraph.removeVertex(parent, false);

    }

    /**
     * Sets the cluster reference field in each child vertex recursively. (Used during collapse and expand)
     * @param cluster
     * @param node
     */
    private void setClusterRef(GenericTreeNode cluster, GenericTreeNode node){
        node.setClusterReference(cluster);
        for(GenericTreeNode child : node.getChildren())
            setClusterRef(cluster, child);
    }

    /**
     * Recursively add all child vertexes of the vertex parent.
     * @param g
     * @param parent
     * @param nodeRef
     */
    private void createTree(Forest<GenericTreeNode, UIEdge> g, GenericTreeNode parent, ArrayList<NodeReference> nodeRef){

        // add all references to this vertex to nodeRef
        if(parent.getOutwardNodeReferences() != null)
            nodeRef.addAll(parent.getOutwardNodeReferences());
        if(parent.getInwardNodeReferences() != null) {
            for(NodeReference ref : parent.getInwardNodeReferences().values()) {
                if(!ref.getReferenceFrom().getClusterNode().equals(parent.getClusterNode()))
                    nodeRef.add(ref);
            }
        }
        // calculate the edge for the chil
        for (GenericTreeNode child : parent.getChildren()) {
            UIEdge edge;
            if(child.isNode()){
                TreeNode n = (TreeNode) child;
                if(parent.isNode() && !((TreeNode)parent).getNode().isOpt())
                    edge = new UIEdge(parent.isRealChild(child), n.getNode().name);
                else
                    edge = new UIEdge(parent.isRealChild(child));
            }else {
                edge = new UIEdge(parent.isRealChild(child));
            }

            // add the edge to the child
            g.addEdge(edge, parent, child);

            createTree(g, child, nodeRef);
        }
    }
}

