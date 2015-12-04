package jastaddad.ui.uicomponent.nodeinfotreetableview;

import jastaddad.api.nodeinfo.NodeInfo;

/**
 * Created by gda10jth on 11/30/15.
 */
public class NodeInfoHolder implements NodeInfoInterface {
    private NodeInfo info;
    public NodeInfoHolder(NodeInfo info){
        this.info = info;
    }

    @Override
    public boolean isNodeInfo() {
        return true;
    }

    @Override
    public boolean isLabel() {
        return false;
    }

    @Override
    public boolean isParameter() {
        return false;
    }

    @Override
    public String getName() {
        return info.print();
    }

    @Override
    public Object getValue() {
        return info.getValue();
    }

    @Override
    public NodeInfo getNodeInfoOrNull() {
        return info;
    }
}
