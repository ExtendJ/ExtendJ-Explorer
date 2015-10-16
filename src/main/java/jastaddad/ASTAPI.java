package jastaddad;

/**
 * Created by gda10jth on 10/16/15.
 */
public class ASTAPI {
    private Node tree;
    public final FilteredTreeNode filteredTree;

    public ASTAPI(Node tree){
        this.tree = tree;
        this.filteredTree = new FilteredTreeNode(tree);
        addToFilteredTree(filteredTree);
    }

    private void addToFilteredTree(FilteredTreeNode parent) {
        for (Node child : parent.node.children) {
            FilteredTreeNode childNode = new FilteredTreeNode(child);
            parent.addChild(childNode);
            addToFilteredTree(childNode);
        }
    }
}
