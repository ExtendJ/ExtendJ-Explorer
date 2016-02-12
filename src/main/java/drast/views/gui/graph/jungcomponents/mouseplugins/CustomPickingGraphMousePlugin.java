package drast.views.gui.graph.jungcomponents.mouseplugins;

import drast.views.gui.graph.jungcomponents.renderers.CustomRenderer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import java.awt.event.MouseEvent;

/**
 * Created by gda10jth on 2/10/16.
 * This is an extension of the PickingGraphMousePlugin. It has the same behavior as its parent, but also tells
 * the CustomRenderer when the user is dragging the mouse. This is so some optimizations can take place by the
 * CustomRenderer.
 */
public class CustomPickingGraphMousePlugin<V, E> extends PickingGraphMousePlugin<V, E> {

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
    }

    @Override
    /**
     * See PickingGraphMousePlugin.mouseDragged(e) for full description.
     * This method extends the parent mouseDragged(e) by telling the CustomRenderer
     * That the user is dragging something, so that optimizations can take place when
     * rendering the graph
     */
    public void mouseDragged(MouseEvent e) {
        if(locked == false){
            VisualizationViewer<V,E> vv = (VisualizationViewer)e.getSource();
            ((CustomRenderer)vv.getRenderer()).setMoving(true);
            super.mouseDragged(e);
        }
    }

    /**
     * See PickingGraphMousePlugin.mouseDragged(e) for full description.
     * This method extends the parent mouseDragged(e) by telling the CustomRenderer
     * That the user is no longer dragging something, so that optimizations can take
     * place when rendering the graph
     */
    public void mouseReleased(MouseEvent e) {
        VisualizationViewer<V,E> vv = (VisualizationViewer)e.getSource();
        ((CustomRenderer)vv.getRenderer()).setMoving(false);
        super.mouseReleased(e);
        System.out.println("Picked: " + vv.getPickedVertexState().getPicked().size());
    }
}
