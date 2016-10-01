package drast.views.gui.graph.jungextensions.renderers;

import drast.model.DrASTSettings;
import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.GUIData;
import drast.views.gui.graph.GraphEdge;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.BasicRenderer;

import java.util.Collection;
import java.util.ConcurrentModificationException;

/**
 * Created by gda10jth on 1/21/16.
 * <p>
 * This Class replaces the BasicRenderer as the renderer for the graph view. This is done so we could decide
 * which vertexes and edges that will be shown on the screen, and when. This is to optimize navigation in the tree.
 * <p>
 * If the number of nodes in the tree exceeds a certain number of nodes, defined in hte config file, edges and labels will not be
 * drawn onto the screen.
 */
public class CustomRenderer extends BasicRenderer<GenericTreeNode, GraphEdge> {
  private boolean moving;
  private final CustomVertexLabelRenderer labelRenderer;
  private final GUIData mon;

  public CustomRenderer(GUIData mon) {
    this.mon = mon;
    moving = false;
    labelRenderer = new CustomVertexLabelRenderer();
    setVertexLabelRenderer(labelRenderer);
  }

  public void setMoving(boolean moving) {
    this.moving = moving;
  }

  /**
   * This method is copied from BasicRenderer with some changes. If we are navigating ( panning, zooming ) the graph
   * some stuff will not be drawn to save power.
   */
  @Override public void render(RenderContext<GenericTreeNode, GraphEdge> renderContext,
      Layout<GenericTreeNode, GraphEdge> layout) {
    boolean optimization = mon.showUglyEdges();
    if (!moving) {
      mon.getController().getGraphViewTabController().graphIsLoading();
    }
    if (DrASTSettings.getFlag("showEdges") && (!optimization || !moving)) {
      // Paint all the edges.
      try {
        Collection<GraphEdge> edges = layout.getGraph().getEdges();
        for (GraphEdge e : edges) {
          renderEdge(renderContext, layout, e);
          renderEdgeLabel(renderContext, layout, e);
        }
      } catch (ConcurrentModificationException cme) {
        renderContext.getScreenDevice().repaint();
      }
    }
    if (DrASTSettings.getFlag("showNodes")) {
      // Paint all the vertices.
      try {
        for (GenericTreeNode v : layout.getGraph().getVertices()) {
          renderVertex(renderContext, layout, v);
          if (!optimization || !moving) {
            renderVertexLabel(renderContext, layout, v);
          }
        }
      } catch (ConcurrentModificationException cme) {
        renderContext.getScreenDevice().repaint();
      }
    }
    if (!moving) {
      mon.getController().getGraphViewTabController().graphIsDone();
    }
  }

  @Override public void renderVertexLabel(RenderContext<GenericTreeNode, GraphEdge> rc,
      Layout<GenericTreeNode, GraphEdge> layout, GenericTreeNode v) {
    labelRenderer.labelVertex(rc, layout, v);
  }
}
