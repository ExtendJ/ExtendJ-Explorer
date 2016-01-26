package drast.ui.graph.jungcomponents;

/**
 * Created by gda10jli on 12/4/15.
 */

import edu.uci.ics.jung.visualization.picking.PickedInfo;
import drast.api.filteredtree.GenericTreeNode;
import drast.api.filteredtree.TreeNode;
import drast.ui.UIMonitor;
import org.apache.commons.collections15.Transformer;

import java.awt.*;

/**
 * A transformer Class for setting the color of a vertex in the graph.
 */
public class VertexPaintTransformer implements Transformer<GenericTreeNode,Paint> {
    private final PickedInfo<GenericTreeNode> pi;
    private final UIMonitor mon;

   public VertexPaintTransformer ( PickedInfo<GenericTreeNode> pi, UIMonitor mon ) {
        super();
        this.mon = mon;
        if (pi == null)
            throw new IllegalArgumentException("PickedInfo instance must be non-null");
        this.pi = pi;

    }

    /**
     * Sets the color of the vertex fNode based on picked state, highlight or a vertex specific color.
     * @param fNode
     * @return
     */
    @Override
    public Paint transform(GenericTreeNode fNode) {
        if(mon.getDialogSelectedNodes().contains(fNode))
            return new Color(255, 197, 115);
        if(mon.getSelectedParameterNodes().contains(fNode))
            return new Color(255, 197, 115);
        if(fNode.isNode() && mon.gethighlightedSimpleClassNames().contains(fNode.getNode().simpleNameClass))
            return new Color(255, 140, 140);
        if (pi.isPicked(fNode))
            return new Color(255, 255, 200);
        if(fNode.isNullNode())
            return new Color(255, 50, 50);
        if(fNode.isReferenceHighlight())
            return new Color(80, 180, 80);
        if(fNode.isNTANode())
            return new Color(120, 160, 200);
        try{
            return Color.decode( fNode.getStyles().get("node-color").getColor());
        }catch (NumberFormatException e){
            e.printStackTrace();
            return new Color(200, 240, 230);
        }
    }
}


