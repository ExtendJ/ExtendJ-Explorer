package drast.model.filteredtree;

import drast.model.Node;

import java.util.ArrayList;

/**
 * Created by gda10jth on 10/21/15.
 */
public class TreeCluster extends GenericTreeCluster {

    private Node node;
    private ArrayList<Node> nodes;
    public TreeCluster(Node node, GenericTreeNode parent){
        super(parent);
        this.node = node;
        this.nodes = new ArrayList<>();
        addToTypeList(node);
        nodes.add(node);
    }

    public void addToTypeList(Node node){
        nodeCount++;
        Integer nbr = typeList.get(node.simpleNameClass);
        if (nbr == null)
            nbr = 0;
        nbr++;
        typeList.put(node.simpleNameClass, nbr);
    }

    /**
     * The top node of the Cluster.
     * @return
     */
    public GenericTreeNode getClusterRoot(){ return parent; }

    @Override
    public void addChild(Node node, GenericTreeNode child) {
        if(child.isCluster()){
            nodes.add(node);
            addToTypeList(node);
        }else{
            children.add(child);
        }
    }

    @Override
    public boolean isCluster() {
        return true;
    }

    @Override
    public boolean isClusterParent() {
        return false;
    }
}
