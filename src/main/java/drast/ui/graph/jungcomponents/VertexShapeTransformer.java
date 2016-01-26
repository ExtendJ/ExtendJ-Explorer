package drast.ui.graph.jungcomponents;

/**
 * Created by gda10jli on 12/4/15.
 */

import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.VertexLabelAsShapeRenderer;
import drast.api.filteredtree.GenericTreeNode;
import drast.ui.graph.UIEdge;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 * A transformer Class for setting the shape of a vertex in the graph.
 */
public class VertexShapeTransformer extends VertexLabelAsShapeRenderer<GenericTreeNode, UIEdge> {

    public VertexShapeTransformer (RenderContext<GenericTreeNode, UIEdge> rc) {
        super(rc);
    }

    /**
     * Sets the shape of the vertex fNode based on style or type of the vertex (cluster or whatever)
     * @param fNode
     * @return
     */
    @Override
    public Shape transform(GenericTreeNode fNode) {
        Component component = prepareRenderer(rc, rc.getVertexLabelRenderer(), rc.getVertexLabelTransformer().transform(fNode),
                rc.getPickedVertexState().isPicked(fNode), fNode);
        Dimension size = component.getPreferredSize();

        int centerX = -size.width/2 -10;
        int centerY = -size.height/2 -10;
        int height = size.height+20;
        int width = size.width+20;

        // make sure nodes have a minimum width
        if(fNode.isNode() && !fNode.isNullNode() && width < 130){
            width = 130;
            centerX = -65;
        }else if(!fNode.isNode()){
            width = 40;
            height = 40;
            centerX = -20;
            centerY = -20;
        }

        if(fNode.isNullNode()){
            return new Ellipse2D.Float(centerX, centerY, width, height);
        }

        String shape = fNode.getStyles().get("node-shape").getStr();
        if(shape != null) {
            if (shape.equals("rounded_rectangle"))
                return new RoundRectangle2D.Double(centerX, centerY, width, height, 40, 40);
            if (shape.equals("circle"))
                return new Ellipse2D.Float(centerX, centerY, width, height);
            if (shape.equals("rectangle"))
                return new RoundRectangle2D.Double(centerX, centerY, width, height, 10, 10);
        }
        return new RoundRectangle2D.Double(-50, -20, 130, 40,40,40);
    }
}
