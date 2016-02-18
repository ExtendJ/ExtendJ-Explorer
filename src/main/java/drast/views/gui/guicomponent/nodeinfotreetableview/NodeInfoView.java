package drast.views.gui.guicomponent.nodeinfotreetableview;

import drast.model.terminalvalues.TerminalValue;

/**
 * Created by gda10jth on 11/30/15.
 * This class is a holder of information for the attribute tree table view.
 *
 * This is the parent class and the class the tree table view expects to contain.
 *
 */
public class NodeInfoView {

    protected String label;
    protected TerminalValue terminalValue;

    public NodeInfoView(String label){
        this.label = label;
    }

    public NodeInfoView(String label, TerminalValue terminalValue){
        this.label = label;
        this.terminalValue = terminalValue;
    }

    public boolean getTerminalValue(){ return terminalValue != null; }
    public boolean isParameter(){ return false; }
    public String getName(){ return label; }
    public Object getValue(){ return terminalValue != null ? terminalValue.getValue() : ""; }
    public TerminalValue getNodeInfo(){ return terminalValue; }
}
