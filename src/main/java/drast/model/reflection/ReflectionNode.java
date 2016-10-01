/* Copyright (c) 2016, The JastAdd Team
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *
 *   2. Redistributions in binary form must reproduce the above
 *      copyright notice, this list of conditions and the following
 *      disclaimer in the documentation and/or other materials provided
 *      with the distribution.
 *
 *   3. The name of the author may not be used to endorse or promote
 *      products derived from this software without specific prior
 *      written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package drast.model.reflection;

import drast.Log;
import drast.model.ASTAnnotation;
import drast.model.DrASTSettings;
import drast.model.FilteredTreeBuilder;
import drast.model.Node;
import drast.model.terminalvalues.Attribute;
import drast.model.terminalvalues.TerminalValue;
import drast.model.terminalvalues.Token;
import org.apache.commons.lang3.reflect.MethodUtils;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class represents an AST node in the filtered tree.
 *
 * Created by Joel Lindholm on 2/18/16.
 */
public class ReflectionNode implements Node {

  /** The AST node which this filtered tree node corresponds to. */
  @Nonnull private final Object astNode;

  /** The parent node of this filtered tree node. */
  private final Node parent;

  /** The name of this node in the parent node. */
  private String nameFromParent = "";

  /** The AST node type name. */
  private final String simpleClassName;

  /** AST child nodes. */
  private final List<Node> children = new ArrayList<>();

  /** NTA child nodes. */
  private final Map<Object, Node> ntaChildren = new HashMap<>();

  private final boolean isList;
  private final boolean isOpt;
  private final boolean isNTA;

  private List<TerminalValue> attributes = Collections.emptyList();
  private List<TerminalValue> tokens = Collections.emptyList();
  private List<TerminalValue> nonterminalAttributes = Collections.emptyList();
  private final Map<Method, Map<Object[], Object>> invokedValues = new HashMap<>();
  private final Map<Method, Pair<Field, Field>> cachedMethodFields = new HashMap<>();

  /** This array contains the typenames of the AST supertypes for this node. */
  private final String[] classHierarchy;

  /**
   * @return a builder to construct the DrAST representation of the given compiler AST node.
   */
  public static Builder builder(Object astObject) {
    return new Builder().astObject(astObject);
  }

  /**
   * This is used to build reflection nodes for the DrAST representation of the
   * compiler AST.
   */
  public static class Builder {
    private Object astObject = null;
    private Node parent = null;
    private String name = "";
    private boolean isList;
    private boolean isOpt;
    private boolean isNta;
    private Collection<Builder> childBuilders = Collections.emptySet();
    private String[] classHierarchy = {};

    public Builder astObject(Object astObject) {
      this.astObject = astObject;
      return this;
    }

    public Builder parent(Node parent) {
      this.parent = parent;
      return this;
    }

    public Builder isList(boolean list) {
      isList = list;
      return this;
    }

    public Builder isOpt(boolean opt) {
      isOpt = opt;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder isNta(boolean nta) {
      isNta = nta;
      return this;
    }

    public Builder children(Collection<Builder> childBuilders) {
      this.childBuilders = childBuilders;
      return this;
    }

    public Builder classHierarchy(String[] classHierarchy) {
      this.classHierarchy = classHierarchy;
      return this;
    }

    public Node build() {
      if (astObject == null) {
        return new NullNode(name);
      } else {
        ReflectionNode node = new ReflectionNode(astObject, parent, name, isList, isOpt, isNta,
            classHierarchy);
        node.children.addAll(childBuilders.stream().map(builder -> builder.parent(node).build())
            .collect(Collectors.toList()));
        return node;
      }
    }

    public Object astObject() {
      return astObject;
    }

    public boolean isNta() {
      return isNta;
    }

    public boolean isOpt() {
      return isOpt;
    }

    public boolean isList() {
      return isList;
    }

    public String name() {
      return name;
    }
  }

  private ReflectionNode(@Nonnull Object astNode, Node parent, String name, boolean isList,
      boolean isOpt, boolean isNTA, String[] classHierarchy) {
    this.astNode = astNode;
    this.parent = parent;
    this.simpleClassName = this.astNode.getClass().getSimpleName();
    this.nameFromParent = name.equals(simpleClassName) || name.isEmpty() ? "" : name;
    this.isOpt = isOpt;
    this.isList = isList;
    this.isNTA = isNTA;
    this.classHierarchy = classHierarchy;
  }

  @Override public void addNtaChild(Object childObject, @Nonnull Node node) {
    ntaChildren.put(childObject, node);
  }

  /**
   * Finds the fields storing the cache flag and cache value for attributes.
   * The field identification is based on the field name, and may produce false positives.
   * If cache fields were annotated we could ensure that we don't identify false cache fields.
   */
  @Override public Pair<Field, Field> getCacheFields(Method method) {
    if (cachedMethodFields.get(method) != null) {
      return cachedMethodFields.get(method);
    }
    Class<?> klass = getAstClass();
    String valueFieldName = ASTAnnotation.getCacheValueFieldName(method);
    String computedFlagFieldName = ASTAnnotation.getComputedFlagFieldName(method);
    Field cachedField = null;
    Field computedField = null;
    outer:
    while (klass != null) {
      for (Field field : klass.getDeclaredFields()) {
        String filedName = field.getName();
        // TODO(joqvist): why use contains here instead of equals?
        if (filedName.contains(valueFieldName)) {
          cachedField = field;
          if (computedField != null) {
            break outer;
          }
        } else if (filedName.contains(computedFlagFieldName)) {
          computedField = field;
          if (cachedField != null) {
            break outer;
          }
        }
      }
      klass = klass.getSuperclass();
    }
    Pair<Field, Field> fields = new Pair<>(cachedField, computedField);
    cachedMethodFields.put(method, fields);
    return fields;
  }

  @Override public boolean isChildClassOf(Class parent, FilteredTreeBuilder traverser) {
    return traverser.isChildClass(astNode, parent.getName());
  }

  @Override public boolean isOpt() {
    return isOpt;
  }

  @Override public boolean isList() {
    return isList;
  }

  @Override public boolean isNullNode() {
    return false;
  }

  @Override public boolean isNTANode() {
    return isNTA;
  }

  @Override public String getSimpleClassName() {
    return simpleClassName;
  }

  @Override public String getNameFromParent() {
    return nameFromParent;
  }

  @Override public Class<?> getAstClass() {
    return astNode.getClass();
  }

  @Override public Map<Object, Node> getNtaChildren() {
    return ntaChildren;
  }

  @Override public Object getAstObject() {
    return astNode;
  }

  @Override public Node getParent() {
    return parent;
  }

  @Override public Collection<Node> getChildren() {
    return children;
  }

  @Override public String toString() {
    return simpleClassName;
  }

  @Override public void computeAttributes() {
    attributes = new ArrayList<>();
    tokens = new ArrayList<>();
    nonterminalAttributes = new ArrayList<>();
    for (Method method : astNode.getClass().getMethods()) {
      TerminalValue info;
      for (Annotation a : method.getAnnotations()) {
        if (ASTAnnotation.isAttribute(a)) {
          info = getAttributeInfo(method);
          if (info.isNTA()) {
            nonterminalAttributes.add(info);
          } else {
            attributes.add(info);
          }
          break;
        } else if (ASTAnnotation.isToken(a)) {
          info = computeToken(method);
          tokens.add(info);
          break;
        }
      }
    }
  }

  /**
   * Creates a Attribute and invokes the method with the supplied parameters, if any.
   * Will also add the specific information about the Attribute, which is derived form the annotations.
   * If forceComputation is true it will compute the non-parametrized NTA:s
   */
  private Attribute getAttributeInfo(Method method) {
    Attribute attribute = new Attribute(method.getName(), method);
    attribute.setParametrized(method.getParameterCount() > 0);
    for (Annotation a : method.getAnnotations()) {
      if (ASTAnnotation.isAttribute(a)) {
        attribute.setKind(ASTAnnotation.getKind(a));
        attribute.setCircular(ASTAnnotation.is(a, ASTAnnotation.AST_METHOD_CIRCULAR));
        attribute.setNTA(ASTAnnotation.is(a, ASTAnnotation.AST_METHOD_NTA));
      } else if (ASTAnnotation.isSource(a)) {
        attribute.setAspect(ASTAnnotation.getString(a, ASTAnnotation.AST_METHOD_ASPECT));
        attribute.setDeclaredAt(ASTAnnotation.getString(a, ASTAnnotation.AST_METHOD_DECLARED_AT));
      }
    }
    if (invokedValues.containsKey(method)) {
      for (Map.Entry<Object[], Object> e : invokedValues.get(method).entrySet()) {
        attribute.addComputedValue(e.getKey(), e.getValue());
      }
    }
    addCachedValues(method, attribute);
    if (DrASTSettings.getFlag(DrASTSettings.DYNAMIC_VALUES)) {
      try {
        if (!attribute.isParametrized()) {
          attribute.setValue(method.invoke(astNode));
        }
      } catch (Throwable e) {
        addInvocationErrors(e, method);
        attribute.setValue(e.getCause());
      }
    }
    return attribute;
  }

  /**
   * Get the Token of the method in the obj.
   */
  private Token computeToken(Method m) {
    String name = m.getName();
    try {
      return new Token(name, m.invoke(astNode), m);
    } catch (Throwable e) {
      addInvocationErrors(e, m);
      return new Token(name, e.getCause().toString(), m);
    }
  }

  private void addCachedValues(Method method, Attribute attribute) {
    if (attribute == null) {
      return;
    }
    Pair<Field, Field> fieldPair = getCacheFields(method);

    attribute.setEvaluated(FilteredTreeBuilder.isComputed(astNode, fieldPair.getSecond()));
    if (!attribute.isEvaluated() && !attribute.isParametrized()) {
      return;
    }

    Object value = FilteredTreeBuilder.getFieldValue(astNode, fieldPair.getFirst());
    if (attribute.isParametrized()) {
      if (value == null || !(value instanceof Map)) {
        return;
      }
      Map<?, ?> map = (Map) value;
      for (Map.Entry par : map.entrySet()) {
        if (method.getParameterCount() > 1 && par.getKey() instanceof Collection) {
          attribute.addComputedValue(((java.util.List) par.getKey()).toArray(), par.getValue());
        } else {
          attribute.addComputedValue(new Object[] {par.getKey()}, par.getValue());
        }
      }
    } else {
      attribute.setValue(value);
    }
  }

  private void addInvocationErrors(Throwable e, Method m) {
    Log.error("Error when computing %s in node %s. Cause : %s",
        m.getName(), astNode,
        e.getCause() != null ? e.getCause().toString() : e.getMessage());
  }

  private static Class classOf(Object arg) {
    if (arg == null) {
      return Object.class;
    } else {
      return arg.getClass();
    }
  }

  @Override public Object computeMethod(String name, Object... args) {
    try {
      return MethodUtils.invokeMethod(astNode, name, args);
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
      throw new Error(e);
    }
  }

  public static Method getMethodForArgs(Class<?> klass, String name, Object[] args)
      throws NoSuchMethodException {
    Method method;
    if (args != null) {
      Class[] paramTypes = new Class[args.length];
      for (int i = 0; i < args.length; ++i) {
        paramTypes[i] = classOf(args[i]);
      }
      method = klass.getMethod(name, paramTypes);
    } else {
      method = klass.getMethod(name);
    }
    return method;
  }

  /**
   * Computes the method in the NodeInfo, with the given parameters, and adds it to the cached list of the Attribute.
   * If the params == null and the method is not parametrized it will compute the method will 0 arguments, otherwise it will return null and add a error to the api.
   *
   * @return true if the invocation was successful.
   */
  @Override public Object computeAttribute(TerminalValue terminalValue, Object[] par) {
    Object[] params = par;
    Method method = terminalValue.getMethod();
    if ((par != null && par.length != method.getParameterCount()) || (par == null
        && method.getParameterCount() != 0)) {
      Log.error("Wrong number of arguments for the method: " + method);
      return null;
    }
    if (params == null) {
      params = new Object[method.getParameterCount()];
    }

    if (!terminalValue.isAttribute()) {
      Log.error("Can only do compute on attributes");
      return null;
    }

    Attribute attribute = (Attribute) terminalValue;
    if (attribute.containsValue(params)) {
      return attribute.getComputedValue(params);
    }

    Object obj;
    try {
      obj = method.invoke(astNode, params);
    } catch (Throwable e) {
      addInvocationErrors(e, attribute.getMethod());
      obj = e.getCause() != null ? e.getCause() : e.getMessage();
    }
    attribute.setEvaluated(true);
    attribute.addComputedValue(params, obj);
    if (!invokedValues.containsKey(method)) {
      invokedValues.put(method, new HashMap<>());
    }
    invokedValues.get(method).put(params, obj);
    return obj;
  }

  @Override public Collection<TerminalValue> getAttributes() {
    return attributes;
  }

  @Override public Collection<TerminalValue> getNTAs() {
    return nonterminalAttributes;
  }

  @Override public Collection<TerminalValue> getTokens() {
    return tokens;
  }

  @Override public String[] getClassHierarchy() {
    return classHierarchy;
  }
}
