package jastaddad.ui.uicomponent.nodeinfotreetableview;

import jastaddad.api.nodeinfo.NodeInfo;

/**
 * Created by gda10jth on 11/30/15.
 * This class is a holder of information for the attribute tree table view.
 *
 * This class represent a value from a parameterized attribute. The class contains the NodeInfo to the attribute, the
 * name is the parameters as a string and the computed value.
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
