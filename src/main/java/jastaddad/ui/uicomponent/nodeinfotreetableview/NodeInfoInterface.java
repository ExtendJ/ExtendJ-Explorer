package jastaddad.ui.uicomponent.nodeinfotreetableview;

import jastaddad.api.nodeinfo.NodeInfo;

/**
 * Created by gda10jth on 11/30/15.
 */
public interface NodeInfoInterface {
    boolean isNodeInfo();
    boolean isLabel();
    boolean isParameter();
    String getName();
    Object getValue();
    NodeInfo getNodeInfoOrNull();
}
