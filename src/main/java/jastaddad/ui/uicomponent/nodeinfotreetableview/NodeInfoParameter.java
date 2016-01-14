package jastaddad.ui.uicomponent.nodeinfotreetableview;

import jastaddad.api.nodeinfo.NodeInfo;

/**
 * Created by gda10jth on 11/30/15.
 * This class is a holder of information for the attribute tree table view.
 *
 * This class represent a value from a parameterized attribute. The class contains the NodeInfo to the attribute, the
 * name is the parameters as a string and the computed value.
 */
public class NodeInfoParameter extends NodeInfoView {
    private Object value;

    public NodeInfoParameter(String name, Object value, NodeInfo info){
        super(name, info);
        this.value = value;
    }

    @Override
    public boolean isParameter() {
        return true;
    }

    @Override
    public Object getValue() {
        return value;
    }

}
