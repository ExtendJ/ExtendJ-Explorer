package drast.views.gui.graph.jungextensions.renderers;

import drast.model.filteredtree.GenericTreeNode;
import drast.model.filteredtree.TreeNode;
import drast.views.gui.graph.GraphEdge;
import drast.views.gui.graph.jungextensions.transformers.VertexShapeTransformer;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.BasicVertexLabelRenderer;
import edu.uci.ics.jung.visualization.transform.BidirectionalTransformer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;
import edu.uci.ics.jung.visualization.transform.shape.ShapeTransformer;
import edu.uci.ics.jung.visualization.transform.shape.TransformingGraphics;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by gda10jth on 2/8/16.
 */
public class CustomVertexLabelRenderer extends BasicVertexLabelRenderer<GenericTreeNode, GraphEdge> {

    public CustomVertexLabelRenderer(){ }

    public void labelVertex(RenderContext<GenericTreeNode,GraphEdge> rc, Layout<GenericTreeNode,GraphEdge> layout, GenericTreeNode v){

        Point2D pt = layout.transform(v);
        pt = rc.getMultiLayerTransformer().transform(Layer.LAYOUT, pt);

        boolean pickedState = rc.getPickedVertexState().isPicked(v);
        Dimension dim = ((CustomDefaultVertexLabelRenderer)rc.getVertexLabelRenderer()).getLabelDimension(rc, v, pickedState);
        Shape shape = transformedVertexShape(rc, v, pt, dim);
        Rectangle2D bounds = shape.getBounds2D();
        double startX = bounds.getX() + (bounds.getWidth()-dim.getWidth())/2;
        int rowCount = !v.isNode() ? 1 : ((TreeNode)v).getLabelAttributes().size()+1;

        Component component = prepareRenderer(rc, rc.getVertexLabelRenderer(), rc.getVertexLabelTransformer().transform(v), pickedState, v);
        labelVertex(rc, layout, v, component, 0, rowCount, pt, bounds, startX);

        if(v.isNode()) {
            TreeNode node = (TreeNode) v;

            //System.out.println("asd: " + node.getLabelAttributes());
            int count = 1;
            for (String attribute : node.getLabelAttributes()) {
                component = prepareRenderer(rc, rc.getVertexLabelRenderer(), attribute, pickedState, v);
                labelVertex(rc, layout, v, component, count, rowCount, pt, bounds, startX);
                count++;
            }
        }
    }

    public void labelVertex(RenderContext<GenericTreeNode,GraphEdge> rc, Layout<GenericTreeNode,GraphEdge> layout, GenericTreeNode v, Component component, int index, double nodeCount, Point2D pt, Rectangle2D bounds, double startX) {
        //System.out.println(label);
        Graph<GenericTreeNode, GraphEdge> graph = layout.getGraph();
        if (rc.getVertexIncludePredicate().evaluate(Context.getInstance(graph, v)) == false) {
            return;
        }

        float x = (float) pt.getX();
        float y = (float) pt.getY();

        GraphicsDecorator g = rc.getGraphicsContext();
        Dimension d = component.getPreferredSize();

        Point p;
        if(position == Position.AUTO) {
            Dimension vvd = rc.getScreenDevice().getSize();
            if(vvd.width == 0 || vvd.height == 0) {
                vvd = rc.getScreenDevice().getPreferredSize();
            }
            p = getAnchorPoint(bounds, d, getPositioner().getPosition(x, y, vvd));
        } else {
            p = getAnchorPoint(bounds, d, position);
        }

        if(nodeCount != 1)
            p.setLocation(startX, bounds.getY() + 10 + (index*d.getHeight()));
        g.draw(component, rc.getRendererPane(), p.x, p.y, d.width, d.height, true);
    }

    private Shape transformedVertexShape(RenderContext<GenericTreeNode,GraphEdge> rc, GenericTreeNode v, Point2D pt, Dimension dimension){
        AffineTransform xform = AffineTransform.getTranslateInstance(pt.getX(), pt.getY());
        Shape shape = ((VertexShapeTransformer)rc.getVertexShapeTransformer()).transform(v, dimension);
        shape = xform.createTransformedShape(shape);

        if(rc.getGraphicsContext() instanceof TransformingGraphics) {
            BidirectionalTransformer transformer = ((TransformingGraphics)rc.getGraphicsContext()).getTransformer();
            if(transformer instanceof ShapeTransformer) {
                ShapeTransformer shapeTransformer = (ShapeTransformer)transformer;
                shape = shapeTransformer.transform(shape);
            }
        }

        return shape;
    }
}
