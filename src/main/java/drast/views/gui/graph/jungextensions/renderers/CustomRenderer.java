package drast.views.gui.graph.jungextensions.renderers;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.BasicRenderer;
import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.Monitor;
import drast.views.gui.graph.GraphEdge;

import java.util.Collection;
import java.util.ConcurrentModificationException;

/**
 * Created by gda10jth on 1/21/16.
 *
 * This Class replaces the BasicRenderer as the renderer for the graph view. This is done so we could decide
 * which vertexes and edges that will be shown on the screen, and when. This is to optimize navigation in the tree.
 *
 * If the number of nodes in the tree exceeds a certain number of nodes, defined in hte config file, edges and labels will not be
 * drawn onto the screen.
 *
 */
public class CustomRenderer extends BasicRenderer<GenericTreeNode, GraphEdge> {
    private boolean moving;
    private CustomVertexLabelRenderer labelRenderer;
    private Monitor mon;

    private int frameCount;
    private long elapsedTime;
    private long totalTime;
    private long lastFrame;

    public CustomRenderer(Monitor mon){
        this.mon = mon;
        moving = false;
        labelRenderer = new CustomVertexLabelRenderer();
        setVertexLabelRenderer(labelRenderer);
        lastFrame = 0;
    }

    /**
     * Calculate the number of times the render method gets called.
     *
     * Remember that the render method only gets called when the repaint method gets called,
     * in order for the FPS count to be true, we need to be sure the repaint method
     * as many times as possible.
     */
    private void calculateFPS(){
        frameCount++;
        elapsedTime = System.currentTimeMillis() - lastFrame;
        totalTime += elapsedTime;
        lastFrame = System.currentTimeMillis();
        if(totalTime > 1000) {
            System.out.println(frameCount);
            frameCount = 0;
            totalTime = 0;
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
        boolean optimization = mon.isOptimization();
        // calculateFPS();
        if(!moving)
            mon.getController().getGraphViewTabController().graphIsLoading();
        if(mon.getConfig().isEnabled("showEdges") && (!optimization || (optimization && !moving))) {
            // paint all the edges
            try {
                Collection<GraphEdge> edges = layout.getGraph().getEdges();
                for (GraphEdge e : edges) {
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
        if(mon.getConfig().isEnabled("showNodes")) {
            // paint all the vertices
            try {
                for (GenericTreeNode v : layout.getGraph().getVertices()) {
                    renderVertex(
                            renderContext,
                            layout,
                            v);
                    if (!optimization || (optimization && !moving)) {
                        renderVertexLabel(
                                renderContext,
                                layout,
                                v);
                    }
                }
            } catch (ConcurrentModificationException cme) {
                renderContext.getScreenDevice().repaint();
            }
        }
        if(!moving)
            mon.getController().getGraphViewTabController().graphIsLoading();if(!moving)
            mon.getController().getGraphViewTabController().graphIsLoading();
        if(mon.getConfig().isEnabled("showEdges") && (!optimization || (optimization && !moving))) {
            // paint all the edges
            try {
                Collection<GraphEdge> edges = layout.getGraph().getEdges();
                for (GraphEdge e : edges) {
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
        if(mon.getConfig().isEnabled("showNodes")) {
            // paint all the vertices
            try {
                for (GenericTreeNode v : layout.getGraph().getVertices()) {
                    renderVertex(
                            renderContext,
                            layout,
                            v);
                    if (!optimization || (optimization && !moving)) {
                        renderVertexLabel(
                                renderContext,
                                layout,
                                v);
                    }
                }
            } catch (ConcurrentModificationException cme) {
                renderContext.getScreenDevice().repaint();
            }
        }
        if(!moving)
            mon.getController().getGraphViewTabController().graphIsLoading();
    }

    @Override
    public void renderVertexLabel(RenderContext<GenericTreeNode,GraphEdge> rc, Layout<GenericTreeNode,GraphEdge> layout, GenericTreeNode v) {
        labelRenderer.labelVertex(rc, layout, v);
    }
}
