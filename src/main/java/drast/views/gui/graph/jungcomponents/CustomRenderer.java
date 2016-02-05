package drast.views.gui.graph.jungcomponents;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.BasicRenderer;
import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.Monitor;
import drast.views.gui.graph.GraphEdge;

import java.util.ConcurrentModificationException;

/**
 * Created by gda10jth on 1/21/16.
 *
 * This Class replaces the BasicRenderer as the renderer for the graph view. This is done so we could decide
 * which vertexes and edges that will be shown on the screen, and when. This is to optimize navigation in the tree.
 *
 * If the number of nodes in the tree exceeds a certain number of nodes, defined in hte config file, edges will not be
 * drawn onto the screen.
 *
 */
public class CustomRenderer extends BasicRenderer<GenericTreeNode, GraphEdge> {
    private boolean moving;
    private boolean optimization;
    public CustomRenderer(Monitor mon){
        moving = false;
        refresh(mon);
    }

    /**
     * This method tries to get the threshold from the configuration file and see if optimizations needs to be done.
     * No optimization will be enabled if something goes wrong with reading the configuration value.
     *
     * @param mon
     */

    public void refresh(Monitor mon){
        optimization = false;
        try {
            int nodeThreshold = 2000;
            if(mon.getConfig().get("nodeThreshold") != null)
                nodeThreshold = Integer.parseInt(mon.getConfig().get("nodeThreshold"));

            optimization = mon.getBrain().getClusteredASTSize() > nodeThreshold;
            if(optimization){
                mon.getController().addWarning("Number of nodes exceed optimization threshold of " + nodeThreshold + " nodes. Navigation will be a bit more ugly, but performance will be better. ");
            }
            mon.getConfig().put("nodeThreshold", String.valueOf(nodeThreshold));
        }catch (Exception e){
            e.printStackTrace();
            optimization = false;
        }
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    /**
     * This method is copied from BasicRenderer with some changes. If we are navigating ( panning, zooming ) the graph
     * some stuff will not be drawn to save power.
     *
     * @param renderContext
     * @param layout
     */
    @Override
    public void render(RenderContext<GenericTreeNode, GraphEdge> renderContext, Layout<GenericTreeNode, GraphEdge> layout) {

        if(!optimization || (optimization && !moving)) {
            // paint all the edges
            try {
                for (GraphEdge e : layout.getGraph().getEdges()) {

                    renderEdge(
                            renderContext,
                            layout,
                            e);
                    renderEdgeLabel(
                            renderContext,
                            layout,
                            e);
                }
            } catch (ConcurrentModificationException cme) {
                renderContext.getScreenDevice().repaint();
            }
        }
        // paint all the vertices
        try {
            for(GenericTreeNode v : layout.getGraph().getVertices()) {

                renderVertex(
                        renderContext,
                        layout,
                        v);
                renderVertexLabel(
                        renderContext,
                        layout,
                        v);
            }
        } catch(ConcurrentModificationException cme) {
            renderContext.getScreenDevice().repaint();
        }
    }
}
