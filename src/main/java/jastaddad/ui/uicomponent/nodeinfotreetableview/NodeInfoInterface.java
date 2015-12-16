package jastaddad.ui.uicomponent.nodeinfotreetableview;

import jastaddad.api.nodeinfo.NodeInfo;

/**
 * Created by gda10jth on 11/30/15.
 * This class is a holder of information for the attribute tree table view.
 *
 * This is the parent class and the class the tree table view expects to contain.
 *
 */
public interface NodeInfoInterface {
    boolean isNodeInfo();
    boolean isLabel();
    boolean isParameter();
    String getName();
    Object getValue();
    NodeInfo getNodeInfoOrNull();
}
