package drast.views.gui.graph.jungcomponents;

import drast.views.gui.Monitor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.ScalingControl;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by gda10jth on 2/10/16.
 */
public class CustomVisualizationViewer<V, E> extends VisualizationViewer<V, E> {
    private Monitor mon;
    public CustomVisualizationViewer(Layout<V, E> layout) {
        super(layout);
    }
    public CustomVisualizationViewer(Layout<V, E> layout, Dimension dim) {
        super(layout, dim);
       // this.mon = mon;
    }

    /**
     * This code is mostly copyed from BasicVisualizationServer.scaleToLayout(ScalingControl scaler).
     * The diffrence is the point2D send into
     *      old: scaler.scale(... , new Point2D.Double());
     *      new: scaler.scale(... , getCenter());
     * @param scaler
     */
    public void zoomOutMax(ScalingControl scaler){
        Dimension vd = getPreferredSize();
        if(this.isShowing()) {
            vd = getSize();
        }
        Dimension ld = getGraphLayout().getSize();
        if(vd.equals(ld) == false) {
            scaler.scale(this, (float)(vd.getWidth()/ld.getWidth()), getCenter());
        }
        //
        // setPreferredSize(getSize());
    }
}
