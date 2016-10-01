package drast.views.gui.graph.jungextensions.mouseplugins;

import drast.views.gui.graph.jungextensions.renderers.CustomRenderer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;

import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gda10jth on 1/21/16.
 * <p>
 * This class handles the mose wheel zoom event. It also contains a timer that is used to determine when the user stops
 * scrolling. This is done so some performance optimizations can be done during zooming.
 */
public class CustomScalingGraphMousePlugin extends ScalingGraphMousePlugin {

  private Timer timer;
  private VisualizationViewer vv;

  public CustomScalingGraphMousePlugin(ScalingControl scaler) {
    super(scaler, 0, 1.1f, 0.9f);
    init();
  }

  private void init() {
    vv = null;
  }

  /**
   * Restarts the timer
   */
  private void restartTimer() {
    if (timer != null) {
      timer.cancel();
    }
    timer = new Timer();
    timer.schedule(new RemindTask(vv), 400);
  }

  /**
   * zoom the display in or out, depending on the direction of the
   * mouse wheel motion.
   * <p>
   * The method also tells the graph renderer that the user is navigating (eventual optimizations could that place
   * because of this).
   */
  @Override public void mouseWheelMoved(MouseWheelEvent e) {
    boolean accepted = checkModifiers(e);
    if (accepted) {
      vv = (VisualizationViewer) e.getSource();
      Point2D mouse = e.getPoint();
      Point2D center = vv.getCenter();
      int amount = -e.getWheelRotation();
      if (zoomAtMouse) {
        if (amount > 0) {
          scaler.scale(vv, in, mouse);
        } else if (amount < 0) {
          scaler.scale(vv, out, mouse);
        }
      } else {
        if (amount > 0) {
          scaler.scale(vv, in, center);
        } else if (amount < 0) {
          scaler.scale(vv, out, center);
        }
      }
      e.consume();
      vv.repaint();
      ((CustomRenderer) vv.getRenderer()).setMoving(true);
      restartTimer();
    }
  }

  /**
   * Class to take care of the "stop scrolling event". tells the graph renderer that the user stopped its zooming.
   */
  class RemindTask extends TimerTask {
    private final VisualizationViewer vv;

    RemindTask(VisualizationViewer vv) {
      this.vv = vv;
    }

    @Override public void run() {
      ((CustomRenderer) vv.getRenderer()).setMoving(false);
      vv.repaint();
    }
  }

}
