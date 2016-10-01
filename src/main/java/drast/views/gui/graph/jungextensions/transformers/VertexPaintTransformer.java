package drast.views.gui.graph.jungextensions.transformers;

/**
 * Created by gda10jli on 12/4/15.
 */
import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.GUIData;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import org.apache.commons.collections15.Transformer;

import java.awt.Color;
import java.awt.Paint;

/**
 * A transformer Class for setting the color of a vertex in the graph.
 */
public class VertexPaintTransformer implements Transformer<GenericTreeNode, Paint> {
  private final PickedInfo<GenericTreeNode> pi;
  private final GUIData mon;

  public VertexPaintTransformer(PickedInfo<GenericTreeNode> pi, GUIData mon) {
    super();
    this.mon = mon;
    if (pi == null) {
      throw new IllegalArgumentException("PickedInfo instance must be non-null");
    }
    this.pi = pi;

  }

  /**
   * Sets the color of the vertex fNode based on picked state, highlight or a vertex specific color.
   */
  @Override public Paint transform(GenericTreeNode fNode) {
    if (mon.getDialogSelectedNodes().contains(fNode)) {
      return new Color(255, 197, 115);
    }
    if (mon.getSelectedParameterNodes().contains(fNode)) {
      return new Color(255, 197, 115);
    }
    if (mon.getHighlightReferencesNodes().contains(fNode)) {
      return new Color(80, 180, 80);
    }
    if (fNode.isNonCluster() && mon.gethighlightedSimpleClassNames()
        .contains(fNode.getNode().getSimpleClassName())) {
      return new Color(255, 140, 140);
    }
    if (pi.isPicked(fNode)) {
      return new Color(255, 255, 200);
    }
    if (fNode.isNullNode()) {
      return new Color(255, 50, 50);
    }
    if (fNode.isNTANode()) {
      return new Color(120, 160, 200);
    }
    try {
      String colorString = fNode.getStyle("node-color");
      if (!colorString.isEmpty()) {
        javafx.scene.paint.Color color = javafx.scene.paint.Color.valueOf(colorString);
        return new Color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue());
      }
    } catch (NumberFormatException ignored) {
    }
    return new Color(200, 240, 230);
  }
}


