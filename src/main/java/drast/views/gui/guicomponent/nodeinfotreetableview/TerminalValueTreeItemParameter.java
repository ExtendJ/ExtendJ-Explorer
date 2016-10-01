package drast.views.gui.guicomponent.nodeinfotreetableview;

import drast.model.terminalvalues.TerminalValue;

/**
 * Created by gda10jth on 11/30/15.
 * This class is a holder of information for the attribute tree table view.
 * <p>
 * This class represent a value from a parameterized attribute. The class contains the NodeInfo to the attribute, the
 * name is the parameters as a string and the computed value.
 */
public class TerminalValueTreeItemParameter extends TerminalValueTreeItemView {
  private final Object value;
  private final Object[] params;

  public TerminalValueTreeItemParameter(Object[] params, Object value, TerminalValue info) {
    super("", info);
    this.params = params;
    String name = "";
    int i = params.length;
    for (Object param : params) {
      name += param == null ? "null" : param.toString();
      if (--i > 0) {
        name += ", ";
      }
    }
    this.label = name;
    this.value = value;
  }

  public Object[] getParams() {
    return params;
  }

  @Override public boolean isParameter() {
    return true;
  }

  @Override public Object getValue() {
    return value;
  }

}
