package drast.views.gui.graph.jungextensions.transformers;

import drast.model.DrASTSettings;
import drast.model.filteredtree.GenericTreeNode;
import org.apache.commons.collections15.Transformer;

import java.awt.BasicStroke;
import java.awt.Stroke;

/**
 * Created by currymama on 2016-06-26.
 */
public class VertexEdgeTransformer implements Transformer<GenericTreeNode, Stroke> {

  private static final float[] dash = {5.f};
  private static Stroke dashedVertexStroke;
  private static Stroke normalVertexStroke;

  public VertexEdgeTransformer() {
    float width = DrASTSettings.getFloat(DrASTSettings.NORMAL_VERTEX_EDGE_WIDTH, 1.0f);
    normalVertexStroke = new BasicStroke(width);

    width = DrASTSettings.getFloat(DrASTSettings.DASHED_VERTEX_EDGE_WIDTH, 0.2f);
    dashedVertexStroke =
        new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, dash, 0.0f);
  }

  @Override public Stroke transform(GenericTreeNode node) {
    if (node.getStyle("border-style").equals("dashed")) {
      return dashedVertexStroke;
    }
    return normalVertexStroke;
  }
}
