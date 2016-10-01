package drast.model;

import drast.model.reflection.Pair;
import drast.model.terminalvalues.TerminalValue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * This class represents the Node in the AST, it holds all its terminal attributes and references to its children.
 * This class should only be created once, due to that it will traverse the tree from the given Object.
 */
public interface Node {

  Pair<Field, Field> getCacheFields(Method method);

  boolean isChildClassOf(Class parent, FilteredTreeBuilder traverser);

  boolean isOpt();

  boolean isList();

  boolean isNullNode();

  boolean isNTANode();

  String getSimpleClassName();

  String getNameFromParent();

  Class<?> getAstClass();

  Map<Object, ? extends Node> getNtaChildren();

  Object getAstObject();

  Node getParent();

  Collection<? extends Node> getChildren();

  /**
   * Computes all methods of the node. This will clear the old values except the invoked ones.
   * This is used for onDemand execution attributes values.
   */
  void computeAttributes();

  Object computeMethod(String name, Object... args);

  Object computeAttribute(TerminalValue terminalValue, Object[] par);

  Collection<TerminalValue> getAttributes();

  Collection<TerminalValue> getNTAs();

  Collection<TerminalValue> getTokens();

  String[] getClassHierarchy();

  void addNtaChild(Object childObject, Node node);
}
