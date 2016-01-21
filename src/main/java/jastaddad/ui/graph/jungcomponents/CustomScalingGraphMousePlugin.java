package jastaddad.ui.graph.jungcomponents;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;

import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gda10jth on 1/21/16.
 */
public class CustomScalingGraphMousePlugin extends ScalingGraphMousePlugin {
    private Timer timer;
    private VisualizationViewer vv;
    public CustomScalingGraphMousePlugin(ScalingControl scaler, int modifiers) {
        super(scaler, modifiers);
        init();
    }

    public CustomScalingGraphMousePlugin(ScalingControl scaler, int modifiers, float in, float out) {
        super(scaler, modifiers, in, out);
        init();
    }

    private void init(){
        vv = null;
    }

    private void newTimer(){
        if(timer != null)
            timer.cancel();
        timer = new Timer();
        timer.schedule(new RemindTask(vv), 200);
    }

    /**
     * zoom the display in or out, depending on the direction of the
     * mouse wheel motion.
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
        boolean accepted = checkModifiers(e);
        if(accepted == true) {
            vv = (VisualizationViewer)e.getSource();
            Point2D mouse = e.getPoint();
            Point2D center = vv.getCenter();
            int amount = e.getWheelRotation();
            if(zoomAtMouse) {
                if(amount > 0) {
                    scaler.scale(vv, in, mouse);
                } else if(amount < 0) {
                    scaler.scale(vv, out, mouse);
                }
            } else {
                if(amount > 0) {
                    scaler.scale(vv, in, center);
                } else if(amount < 0) {
                    scaler.scale(vv, out, center);
                }
            }
            e.consume();
            vv.repaint();
            ((CustomRenderer)vv.getRenderer()).setMoving(true);
            newTimer();

        }
    }

    class RemindTask extends TimerTask {
        private VisualizationViewer vv;

        RemindTask(VisualizationViewer vv){
            this.vv = vv;
        }

        public void run() {
            ((CustomRenderer)vv.getRenderer()).setMoving(false);
            vv.repaint();
        }
    }

}
