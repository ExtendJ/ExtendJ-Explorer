package drast.views.gui.graph.jungextensions.transformers;

import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.GUIConfig;
import drast.views.gui.Monitor;
import org.apache.commons.collections15.Transformer;

import java.awt.*;

/**
 * Created by currymama on 2016-06-26.
 */
public class VertexEdgeTransformer implements Transformer<GenericTreeNode, Stroke> {

    private static float dash[] = {5.0f};
    private static Stroke dashedVertexStroke;
    private static Stroke normalVertexStroke;

    public VertexEdgeTransformer(Monitor mon){
        GUIConfig config = mon.getConfig();
        float width = config.getFloat(GUIConfig.NORMAL_VERTEX_EDGE_WIDTH);
        if(width <= 0f)
            width = GUIConfig.F_NORMAL_VERTEX_EDGE_WIDTH;
        normalVertexStroke = new BasicStroke(width);

        width = config.getFloat(GUIConfig.DASHED_VERTEX_EDGE_WIDTH);
        if(width <= 0f)
            width = GUIConfig.F_DASHED_VERTEX_EDGE_WIDTH;
        dashedVertexStroke =  new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, dash, 0.0f);
    }

    @Override
    public Stroke transform(GenericTreeNode node) {
        if(node.getStyles().get("border-style").getStr().equals("dashed"))
            return dashedVertexStroke;
        return normalVertexStroke;
    }
}
