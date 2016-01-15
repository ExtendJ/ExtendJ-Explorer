package jastaddad.ui.uicomponent.nodeinfotreetableview;

import jastaddad.api.nodeinfo.NodeInfo;

/**
 * Created by gda10jth on 11/30/15.
 * This class is a holder of information for the attribute tree table view.
 *
 * This is the parent class and the class the tree table view expects to contain.
 *
 */
public class NodeInfoView {

    protected String label;
    protected NodeInfo nodeInfo;

    public NodeInfoView(String label){
        this.label = label;
    }

    public NodeInfoView(String label, NodeInfo nodeInfo){
        this.label = label;
        this.nodeInfo = nodeInfo;
    }

    public boolean isNodeInfo(){ return nodeInfo != null; }
    public boolean isParameter(){ return false; }
    public String getName(){ return label; }
    public Object getValue(){ return nodeInfo != null ? nodeInfo.getValue() : ""; }
    public NodeInfo getNodeInfo(){ return nodeInfo; }
}
