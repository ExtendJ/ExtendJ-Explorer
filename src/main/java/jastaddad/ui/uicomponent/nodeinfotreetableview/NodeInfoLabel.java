package jastaddad.ui.uicomponent.nodeinfotreetableview;

import jastaddad.api.nodeinfo.NodeInfo;

/**
 * Created by gda10jth on 11/30/15.
 * This class is a holder of information for the attribute tree table view.
 *
 * A label is a group that methods get put in. This class does not hold any actual methods, and is only used to get
 * labels inside the tree table view.
 *
 * For example Attributes, Tokens and Nonterminal
 */
public class NodeInfoLabel implements NodeInfoInterface{
    public final String label;
    public NodeInfoLabel(String label){
        this.label = label;
    }

    @Override
    public boolean isNodeInfo() {
        return false;
    }

    @Override
    public boolean isLabel() {
        return true;
    }

    @Override
    public boolean isParameter() {
        return false;
    }

    @Override
    public String getName() {
        return label;
    }

    @Override
    public Object getValue() {
        return "";
    }

    @Override
    public NodeInfo getNodeInfoOrNull() {
        return null;
    }
}
