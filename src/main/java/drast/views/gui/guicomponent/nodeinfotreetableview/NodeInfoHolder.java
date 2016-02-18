package drast.views.gui.guicomponent.nodeinfotreetableview;

import drast.model.terminalvalues.TerminalValue;

/**
 * Created by gda10jth on 11/30/15.
 * This class is a holder of information for the attribute tree table view.
 *
 * This class contains a NodeInfo.
 *
 */
public class NodeInfoHolder extends NodeInfoView {

    public NodeInfoHolder(TerminalValue info){
        super(info != null ? info.print() : "", info);
    }

    @Override
    public boolean getTerminalValue() {
        return true;
    }

    @Override
    public Object getValue() {
        return terminalValue.getValue();
    }

}
