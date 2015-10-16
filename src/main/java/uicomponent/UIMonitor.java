package uicomponent;

import jastaddad.Node;

/**
 * Created by gda10jli on 10/16/15.
 */
public class UIMonitor {
    private Node selectedNode;
    private Node root;

    public UIMonitor(Node root){ this.root = root; }

    public Node getRootNode(){ return root; }
    public void setRootNode(Node root){ this.root = root; }

    public Node getSelectedNode(){ return selectedNode; }
    public void setSelectedNode(Node selectedNode){ this.selectedNode = selectedNode; }
}
