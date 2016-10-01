package drast.model.reflection;

import drast.model.FilteredTreeBuilder;
import drast.model.Node;
import drast.model.terminalvalues.TerminalValue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class NullNode implements drast.model.Node {
  private final String nameFromParent;

  public NullNode(String nameFromParent) {
    this.nameFromParent = nameFromParent;
  }

  @Override public Pair<Field, Field> getCacheFields(Method method) {
    return new Pair<>(null, null);
  }

  @Override public boolean isChildClassOf(Class parent, FilteredTreeBuilder traverser) {
    return false;
  }

  @Override public boolean isOpt() {
    return false;
  }

  @Override public boolean isList() {
    return false;
  }

  @Override public boolean isNullNode() {
    return true;
  }

  @Override public boolean isNTANode() {
    return false;
  }

  @Override public String getSimpleClassName() {
    return "Null";
  }

  @Override public String getNameFromParent() {
    return nameFromParent;
  }

  @Override public Class<?> getAstClass() {
    return null;
  }

  @Override public Map<Object, ? extends Node> getNtaChildren() {
    return Collections.emptyMap();
  }

  @Override public Object getAstObject() {
    return null;
  }

  @Override public Node getParent() {
    return null;
  }

  @Override public Collection<? extends Node> getChildren() {
    return null;
  }

  @Override public void computeAttributes() {
  }

  @Override public Object computeMethod(String name, Object... args) {
    return null;
  }

  @Override public Object computeAttribute(TerminalValue terminalValue, Object[] par) {
    return null;
  }

  @Override public Collection<TerminalValue> getAttributes() {
    return Collections.emptyList();
  }

  @Override public Collection<TerminalValue> getNTAs() {
    return Collections.emptyList();
  }

  @Override public Collection<TerminalValue> getTokens() {
    return Collections.emptyList();
  }

  @Override public String[] getClassHierarchy() {
    return new String[0];
  }

  @Override public void addNtaChild(Object childObject, Node node) {
  }

  @Override public String toString() {
    return "Null";
  }
}
