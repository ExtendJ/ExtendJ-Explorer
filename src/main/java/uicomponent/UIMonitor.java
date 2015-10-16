package uicomponent;

import jastaddad.FilteredTreeNode;
import jastaddad.Node;

/**
 * Created by gda10jli on 10/16/15.
 */
public class UIMonitor {
    private FilteredTreeNode selectedNode;
    private FilteredTreeNode root;

    public UIMonitor(FilteredTreeNode root){ this.root = root; }

    public FilteredTreeNode getRootNode(){ return root; }
    public void setRootNode(FilteredTreeNode root){ this.root = root; }

    public FilteredTreeNode getSelectedNode(){ return selectedNode; }
    public void setSelectedNode(FilteredTreeNode selectedNode){ this.selectedNode = selectedNode; }
}
