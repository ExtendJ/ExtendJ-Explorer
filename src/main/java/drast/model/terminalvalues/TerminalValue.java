package drast.model.terminalvalues;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * This Class is a holder for the terminal attributes.
 * Created by gda10jli on 10/20/15.
 */
public abstract class TerminalValue implements Comparable<TerminalValue> {
  protected Object value; // The value of the attribute.
  protected final Method method; // The method which is used to invoke the attribute.
  private final String name;
  private boolean evaluated;

  public TerminalValue(String name, Object value, Method method, boolean evaluated) {
    this.name = name;
    this.value = value;
    this.method = method;
    this.evaluated = evaluated;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public Method getMethod() {
    return method;
  }

  /**
   * Returns the kind of the method, i.e. syn, syn, etc.
   */
  public abstract String getKind();

  @Override public int compareTo(TerminalValue o) {
    return name.compareTo(o.name);
  }

  /**
   * Returns true if this TerminalValue has been computed.
   */
  public boolean isEvaluated() {
    return evaluated;
  }

  public void setEvaluated(boolean evaluated) {
    this.evaluated = evaluated;
  }

  /**
   * Check if a attribute is parametrized
   */
  public abstract boolean isParametrized();

  /**
   * Check if a attribute is parametrized
   */
  public abstract boolean isNTA();

  /**
   * Check if a attribute is parametrized
   */
  public abstract boolean isAttribute();

  /**
   * This will add more type specific information to the List, used by getInfo()
   */
  protected abstract void setChildInfo(ArrayList<AttributeInfo> al);

  /**
   * Prints the name with its parameters, if the parameters are null the type of the parameters will then be added.
   * * MethodName(1,2) or MethodName(int, String)
   */
  public static String getName(Method m, Object[] params) {
    String name = m.getName() + "(";
    if (m.getParameterCount() > 0) {
      for (int i = 0; i < m.getParameterCount(); i++) {
        name += params != null ? params[i].toString() : m.getParameterTypes()[i].getName();
        if (i + 1 < m.getParameterCount()) {
          name += ", ";
        }
      }
    }
    return name + ")";
  }

  /**
   * Creates a List of NodeInfoHolders, a more abstract holder for the NodeInfo types. The List will contain the information about the NodeInfo,
   * i.e. name, value, return type, kind and more.
   */
  public ArrayList<AttributeInfo> getInfo(Object value) {
    ArrayList<AttributeInfo> al = new ArrayList<>();
    al.add(new AttributeInfo("Name", name));
    al.add(new AttributeInfo("Value", isParametrized() ? value : this.value));
    al.add(new AttributeInfo("Return type", method.getReturnType().getName()));
    for (int i = 0; i < method.getParameterCount(); i++) {
      al.add(
          new AttributeInfo("Parameter type: " + i, method.getParameterTypes()[i].getName()));
    }
    al.add(new AttributeInfo("Is parametrized", isParametrized()));
    setChildInfo(al);
    return al;
  }

}
