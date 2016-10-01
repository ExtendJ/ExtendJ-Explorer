package drast.views.gui.guicomponent.nodeinfotreetableview;

import drast.model.terminalvalues.TerminalValue;

/**
 * Created by gda10jth on 11/30/15.
 * This class is a holder of information for the attribute tree table view.
 * <p>
 * This class contains a NodeInfo.
 */
public class TerminalValueTreeItem extends TerminalValueTreeItemView {

  public TerminalValueTreeItem(TerminalValue value) {
    super(value != null ? value.toString() : "", value);
  }

  @Override public boolean getTerminalValue() {
    return true;
  }

  @Override public Object getValue() {
    return terminalValue.getValue();
  }

}
