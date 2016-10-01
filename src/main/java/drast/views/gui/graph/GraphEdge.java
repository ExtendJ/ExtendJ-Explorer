package drast.views.gui.graph;

import drast.model.DrASTSettings;
import drast.views.gui.GUIData;
import org.apache.commons.collections15.Transformer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

/**
 * This class represents the edge between the nodes in the GraphView.
 * It works as a holder for values which are then checked by the GraphView.
 */
public class GraphEdge implements Transformer<GraphEdge, Stroke> {

  private static final int STANDARD = 0;
  public static final int CLUSTER = 1;
  public static final int DISPLAYED_REF = 2;
  public static final int ATTRIBUTE_REF = 3;
  public static final int ATTRIBUTE_NTA = 4;

  private static final float[] dash = {5.0f};
  private static Stroke refStroke;
  private static Stroke dashedEdgeStroke;
  private static Stroke normalEdgeStroke;

  private boolean reference;
  private String label;
  private int type = STANDARD;

  public GraphEdge() {
  }

  /**
   * Constructor for the transformer
   */
  public GraphEdge(GUIData mon) {
    setNormalEdgeStroke();
    setDashedEdgeStroke();
    setRefEdgeStroke();
  }

  private void setNormalEdgeStroke() {
    float width = DrASTSettings.getFloat(DrASTSettings.NORMAL_EDGE_WIDTH, 1.0f);
    normalEdgeStroke = new BasicStroke(width);
  }

  private void setDashedEdgeStroke() {
    float width = DrASTSettings.getFloat(DrASTSettings.DASHED_EDGE_WIDTH, 0.2f);
    dashedEdgeStroke =
        new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, dash, 0.0f);
  }

  private void setRefEdgeStroke() {
    float width = DrASTSettings.getFloat(DrASTSettings.REF_EDGE_WIDTH, 2.0f);
    refStroke =
        new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, dash, 0.0f);
  }


  public GraphEdge(int type) {
    this.type = type;
    reference = type != ATTRIBUTE_NTA;
  }

  public GraphEdge setLabel(String label) {
    this.label = label;
    return this;
  }

  public boolean isReference() {
    return reference;
  }

  public void setType(int type) {
    this.type = type;
  }

  private int getType() {
    return type;
  }

  public String toString() { // Always good for debugging
    return label;
  }

  public Color getColor() {
    switch (type) {
      case STANDARD:
        return new Color(0, 0, 0);
      case ATTRIBUTE_NTA:
        return new Color(140, 140, 200);
      case ATTRIBUTE_REF:
        return new Color(80, 180, 80);
      case DISPLAYED_REF:
        return new Color(200, 80, 80);
      default:
        return new Color(0, 0, 0);
    }
  }

  @Override public Stroke transform(GraphEdge item) {
    switch (item.getType()) {
      case GraphEdge.STANDARD:
      case GraphEdge.ATTRIBUTE_NTA:
        return normalEdgeStroke;
      case GraphEdge.ATTRIBUTE_REF:
      case GraphEdge.DISPLAYED_REF:
        return refStroke;
      case GraphEdge.CLUSTER:
      default:
        return dashedEdgeStroke;
    }
  }
}
