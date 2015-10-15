package uicomponent;

import edu.uci.ics.jung.visualization.VisualizationImageServer;
import javafx.embed.swing.SwingNode;

import javax.swing.*;

/**
 * Created by gda10jli on 10/15/15.
 */
public class ViewUtil {

    public static void createSwingContent(final SwingNode swingNode, VisualizationImageServer vs) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                swingNode.setContent(vs);
            }
        });
    }

}
