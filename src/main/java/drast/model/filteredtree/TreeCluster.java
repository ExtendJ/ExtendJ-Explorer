package drast.model.filteredtree;

import drast.model.FilterConfig;
import drast.model.Node;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gda10jth on 10/21/15.
 */
public class TreeCluster extends GenericTreeCluster {

    private ArrayList<Node> nodes;
    public TreeCluster(Node node, GenericTreeNode parent, FilterConfig filterConfig){
        super(parent);
        this.nodes = new ArrayList<>();
        nodes.add(node);
        nodeCount++;
        setStyles(filterConfig);
    }

    @Override
    protected HashMap<String, Integer> fillTypeList(HashMap<String, Integer> types) {
        for (Node node : nodes) {
            Integer nbr = types.get(node.simpleNameClass);
            if (nbr == null)
                nbr = 0;
            nbr++;
            types.put(node.simpleNameClass, nbr);
        }
        int cc = 0;
        for(GenericTreeCluster cNode : getClusters()) {
            if(cNode.isCluster() && cNode.getClusters().size() > 0){
                for (GenericTreeCluster cluster : cNode.getClusters())
                    types.put("Cluster " + ++cc, cluster.getNodeCount());
            }else
                types.put("Cluster " + ++cc, cNode.getNodeCount());
        }
        return types;
    }

    /**
     * The top node of the Cluster.
     * @return
     */
    public GenericTreeNode getClusterRoot(){ return parent; }

    @Override
    public void addChild(Node node, GenericTreeNode child) {
        if(node != null && child.isCluster()) {
            nodes.add(node);
            nodeCount++;
        } else
            children.add(child);
    }

    public void addToCluster(GenericTreeNode child) {
        nodeCount++;
        if(child.isCluster() || child.isClusterParent())
            clusters.add((GenericTreeCluster) child);
         else
            nodes.add(child.getNode());
    }

    @Override
    public boolean isCluster() { return true; }

    @Override
    public boolean isClusterParent() { return false; }
}
