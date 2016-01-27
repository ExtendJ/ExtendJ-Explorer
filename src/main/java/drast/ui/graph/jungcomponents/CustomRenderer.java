package drast.ui.graph.jungcomponents;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.BasicRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import drast.api.filteredtree.GenericTreeNode;
import drast.ui.Config;
import drast.ui.UIMonitor;
import drast.ui.graph.UIEdge;

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
public class CustomRenderer extends BasicRenderer<GenericTreeNode, UIEdge> {
    private boolean moving;
    private boolean optimization;
    public CustomRenderer(UIMonitor mon){
        moving = false;
        refresh(mon);
    }

    /**
     * This method tries to get the threshold from the configuration file and see if optimizations needs to be done.
     * No optimization will be enabled if something goes wrong with reading the configuration value.
     *
     * @param mon
     */

    public void refresh(UIMonitor mon){
        optimization = false;
        try {
            int nodeThreshold = Integer.parseInt(mon.getConfig().get("nodeThreshold"));

            optimization = mon.getApi().getASTSize() > nodeThreshold;
            if(optimization){
                mon.getController().addWarning("Number of nodes exceed optimization threshold of " + nodeThreshold + " nodes. Navigation will be a bit more ugly, but performance will be better. ");
            }
        }catch (Exception e){
            e.printStackTrace();
            optimization = false;
        }
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    /**
     * This method is copyed from BasicRenderer with some changes. If we are navigating ( panning, zooming ) the graph
     * some stuff will not be drawn to save power.
     *
     * @param renderContext
     * @param layout
     */
    @Override
    public void render(RenderContext<GenericTreeNode, UIEdge> renderContext, Layout<GenericTreeNode, UIEdge> layout) {

        if(!optimization || (optimization && !moving)) {
            // paint all the edges
            try {
                for (UIEdge e : layout.getGraph().getEdges()) {

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
