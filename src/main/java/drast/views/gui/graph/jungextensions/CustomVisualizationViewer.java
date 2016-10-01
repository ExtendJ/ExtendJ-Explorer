package drast.views.gui.graph.jungextensions;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.ScalingControl;

import java.awt.Dimension;
import java.awt.geom.Point2D;

/**
 * Created by gda10jth on 2/10/16.
 */
public class CustomVisualizationViewer<V, E> extends VisualizationViewer<V, E> {
  public CustomVisualizationViewer(Layout<V, E> layout, Dimension dim) {
    super(layout, dim);
  }

  /**
   * This code is mostly copied from BasicVisualizationServer.scaleToLayout(ScalingControl scaler).
   * The difference is the point2D send into
   * old: scaler.scale(... , new Point2D.Double());
   * new: scaler.scale(... , getCenter());
   */
  public void zoomOutMax(ScalingControl scaler) {
    Dimension vd = getPreferredSize();
    if (this.isShowing()) {
      vd = getSize();
    }
    Dimension ld = getGraphLayout().getSize();
    if (!vd.equals(ld)) {
      scaler.scale(this, (float) (vd.getWidth() / ld.getWidth()), new Point2D.Double());
    }
    setPreferredSize(getSize());
  }
}
