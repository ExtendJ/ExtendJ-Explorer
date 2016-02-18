package drast.views.gui.graph.jungextensions.renderers;

import drast.model.filteredtree.GenericTreeNode;
import drast.model.filteredtree.TreeNode;
import drast.views.gui.graph.GraphEdge;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;

import java.awt.*;

/**
 * Created by gda10jth on 2/9/16.
 *
 * This Class is extendeds the DefaultVertexLabelRenderer with a mehtod getLabelDimension(...).
 * DrAST extension of the Jung2 library makes it possible to have multiple labels in a node.
 * The getLabelDimension(...) method calculates the total Dimension of all labels within one node.
 */
public class CustomDefaultVertexLabelRenderer extends DefaultVertexLabelRenderer {

    /**
     * Creates a default table cell renderer.
     *
     * @param pickedVertexLabelColor
     */
    public CustomDefaultVertexLabelRenderer(Color pickedVertexLabelColor) { super(pickedVertexLabelColor); }

    /**
     * The getLabelDimension(...) method calculates the total Dimension of all labels within one node.
     * @param rc
     * @param v
     * @param pickedState
     * @return
     */
    public Dimension getLabelDimension(RenderContext<GenericTreeNode,GraphEdge> rc, GenericTreeNode v, boolean pickedState){
        String nodeName = rc.getVertexLabelTransformer().transform(v);
        if(!v.isNode()){
            Component component = getVertexLabelRendererComponent(rc.getScreenDevice(), nodeName, rc.getVertexFontTransformer().transform(v), pickedState, v);
            Dimension compSize = component.getPreferredSize();
            return new Dimension(compSize.width, compSize.height);
        }else{
            TreeNode node = (TreeNode) v;
            String longestName = nodeName;
            for(String attribute : node.getLabelAttributes()){
                if(attribute.length() > longestName.length())
                    longestName = attribute;
            }
            Component component = getVertexLabelRendererComponent(rc.getScreenDevice(), longestName, rc.getVertexFontTransformer().transform(v), pickedState, v);
            Dimension compSize = component.getPreferredSize();
            Dimension dim = new Dimension(compSize.width, compSize.height + (compSize.height*node.getLabelAttributes().size()));
            return dim;
        }
    }
}
