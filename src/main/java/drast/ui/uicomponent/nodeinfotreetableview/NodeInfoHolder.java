package drast.ui.uicomponent.nodeinfotreetableview;

import drast.api.nodeinfo.NodeInfo;

/**
 * Created by gda10jth on 11/30/15.
 * This class is a holder of information for the attribute tree table view.
 *
 * This class contains a NodeInfo.
 *
 */
public class NodeInfoHolder extends NodeInfoView {

    public NodeInfoHolder(NodeInfo info){
        super(info != null ? info.print() : "", info);
    }

    @Override
    public boolean isNodeInfo() {
        return true;
    }

    @Override
    public Object getValue() {
        return nodeInfo.getValue();
    }

}
