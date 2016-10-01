package drast.model.terminalvalues;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * The class that holds the terminal Token attribute.
 * Created by gda10jli on 10/20/15.
 */
public class Token extends TerminalValue {

  public Token(String name, Object value, Method m) {
    super(name, value, m, true);
  }

  @Override public String toString() {
    return getName(method, null);
  }

  @Override protected void setChildInfo(ArrayList<AttributeInfo> al) {
  }

  @Override public boolean isParametrized() {
    return false;
  }

  @Override public boolean isNTA() {
    return false;
  }

  /**
   * Check if a attribute is parametrized
   */
  @Override public boolean isAttribute() {
    return false;
  }

  @Override public String getKind() {
    return null;
  }

}
