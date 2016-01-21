package jastaddad.ui.graph.jungcomponents;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.BasicRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import jastaddad.api.filteredtree.GenericTreeNode;
import jastaddad.ui.Config;
import jastaddad.ui.UIMonitor;
import jastaddad.ui.graph.UIEdge;

import java.util.ConcurrentModificationException;

/**
 * Created by gda10jth on 1/21/16.
 */
public class CustomRenderer extends BasicRenderer<GenericTreeNode, UIEdge> {
    private boolean moving;
    private boolean optimization;
    public CustomRenderer(UIMonitor mon){
        moving = false;
        refresh(mon);
    }

    public void refresh(UIMonitor mon){
        try {
            int nodeThreshold = Integer.parseInt(mon.getConfig().get("nodeThreshold"));

            optimization = mon.getApi().getNonFilteredNodes() > nodeThreshold;
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
     * This method is copyed from BasicRenderer, so we could do some optimizations.
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
