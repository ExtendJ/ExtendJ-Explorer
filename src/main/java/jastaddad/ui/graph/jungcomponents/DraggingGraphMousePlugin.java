package jastaddad.ui.graph.jungcomponents;

import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

/**
 * Created by gda10jli on 12/14/15.
 */
public class DraggingGraphMousePlugin extends TranslatingGraphMousePlugin {

    public DraggingGraphMousePlugin(int modifiers) {
        super(modifiers);
    }

    /**
     * chack the modifiers. If accepted, translate the graph according
     * to the dragging of the mouse pointer
     * @param e the event
     * !NOTE! method almost unchanged, added a null check for down an exception was thrown otherwise
     */
    public void mouseDragged(MouseEvent e) {
        VisualizationViewer vv = (VisualizationViewer)e.getSource();
        boolean accepted = checkModifiers(e);
        if(accepted && down != null) { //
            MutableTransformer modelTransformer = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
            vv.setCursor(cursor);
            try {
                Point2D q = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(down);
                Point2D p = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(e.getPoint());
                float dx = (float) (p.getX()-q.getX());
                float dy = (float) (p.getY()-q.getY());

                modelTransformer.translate(dx, dy);
                down.x = e.getX();
                down.y = e.getY();
            } catch(RuntimeException ex) {
                System.err.println("down = "+down+", e = "+e);
                throw ex;
            }

            e.consume();
            vv.repaint();
        }
    }
}
