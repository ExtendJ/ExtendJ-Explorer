package drast.views.gui.graph.jungextensions.renderers;

import drast.model.filteredtree.GenericTreeNode;
import drast.model.filteredtree.TreeNode;
import drast.views.gui.graph.GraphEdge;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Created by gda10jth on 2/9/16.
 * <p>
 * This Class extends the DefaultVertexLabelRenderer with a method getLabelDimension(...).
 * DrAST extension of the Jung2 library makes it possible to have multiple labels in a node.
 * The getLabelDimension(...) method calculates the total Dimension of all labels within one node.
 */
public class CustomDefaultVertexLabelRenderer extends DefaultVertexLabelRenderer {

  /**
   * Creates a default table cell renderer.
   */
  public CustomDefaultVertexLabelRenderer(Color pickedVertexLabelColor) {
    super(pickedVertexLabelColor);
  }

  /**
   * The getLabelDimension(...) method calculates the total Dimension of all labels within one node.
   */
  public Dimension getLabelDimension(RenderContext<GenericTreeNode, GraphEdge> rc,
      GenericTreeNode v, boolean pickedState) {
    String nodeName = rc.getVertexLabelTransformer().transform(v);
    if (!v.isNonCluster()) {
      Component component = getVertexLabelRendererComponent(rc.getScreenDevice(), nodeName,
          rc.getVertexFontTransformer().transform(v), pickedState, v);
      Dimension compSize = component.getPreferredSize();
      return new Dimension(compSize.width, compSize.height);
    } else {
      TreeNode node = (TreeNode) v;
      String longestName = nodeName;
      for (String attribute : node.getLabelAttributes()) {
        if (attribute.length() > longestName.length()) {
          longestName = attribute;
        }
      }
      Component component = getVertexLabelRendererComponent(rc.getScreenDevice(), longestName,
          rc.getVertexFontTransformer().transform(v), pickedState, v);
      Dimension compSize = component.getPreferredSize();
      return new Dimension(compSize.width,
          compSize.height + (compSize.height * node.getLabelAttributes().size()));
    }
  }

  @Override
  public <V> Component getVertexLabelRendererComponent(JComponent vv, Object value, Font font,
      boolean isSelected, V vertex) {
    Component component = super.getVertexLabelRendererComponent(vv, value, font, isSelected, vertex);
    if (vertex instanceof GenericTreeNode) {
      GenericTreeNode node = (GenericTreeNode) vertex;
      try {
        String colorString = node.getStyle("text-color");
        if (!colorString.isEmpty()) {
          javafx.scene.paint.Color color = javafx.scene.paint.Color.valueOf(colorString);
          component.setForeground(new Color((float) color.getRed(), (float) color.getGreen(),
              (float) color.getBlue()));
        }
      } catch (NumberFormatException ignored) {
      }
    }
    return component;
  }
}
