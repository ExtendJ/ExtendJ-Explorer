package drast.ui.graph.jungcomponents;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.BasicRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;

import java.util.ConcurrentModificationException;

/**
 * Created by gda10jth on 1/21/16.
 */
public class CustomRenderer<V, E> extends BasicRenderer<V, E> {
    private boolean moving;

    public CustomRenderer(){
        moving = false;
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
    public void render(RenderContext<V, E> renderContext, Layout<V, E> layout) {
        if(!moving) {
            // paint all the edges
            try {
                for (E e : layout.getGraph().getEdges()) {

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
            for(V v : layout.getGraph().getVertices()) {

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
