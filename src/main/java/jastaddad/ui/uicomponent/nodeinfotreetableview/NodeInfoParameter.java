package jastaddad.ui.uicomponent.nodeinfotreetableview;

import jastaddad.api.nodeinfo.NodeInfo;

/**
 * Created by gda10jth on 11/30/15.
 */
public class NodeInfoParameter implements NodeInfoInterface {
    public final NodeInfo info;
    private String name;
    private Object value;

    public NodeInfoParameter(String name, Object value, NodeInfo info){
        this.name = name;
        this.value = value;
        this.info = info;
    }

    @Override
    public boolean isNodeInfo() {
        return false;
    }

    @Override
    public boolean isLabel() {
        return false;
    }

    @Override
    public boolean isParameter() {
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public NodeInfo getNodeInfoOrNull() {
        return null;
    }
}
