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

package drast.model;

import drast.Log;
import drast.model.filteredtree.GenericTreeNode;
import drast.model.filteredtree.NodeReference;
import drast.model.filteredtree.TreeCluster;
import drast.model.filteredtree.TreeClusterParent;
import drast.model.filteredtree.TreeNode;
import drast.model.reflection.NullNode;
import drast.model.reflection.Pair;
import drast.model.reflection.ReflectionNode;
import drast.model.terminalvalues.TerminalValue;
import org.jastadd.drast.filterlang.Attribute;
import org.jastadd.drast.filterlang.Rule;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Class for traversing the AST to generate a filtered AST. After this the AST can be
 * reached from the outside via this class.
 * <p>
 * Each node in the filtered AST is a subtype of GenericTreeNode.
 */
public class FilteredTreeBuilder {

  private final Node tree;
  private GenericTreeNode filteredTree;
  private final TreeFilter treeFilter;
  private final Set<Class<?>> allTypes = new HashSet<>();
  private final Map<Object, GenericTreeNode> treeNodes = new HashMap<>();
  private final Map<Node, Set<Node>> computedNTAs = new HashMap<>(); // TODO: This might be a temporary solution.

  private final Set<Object> astObjects = new HashSet<>(); // All AST nodes in the tree.
  private final Set<Object> astNtaObjects = new HashSet<>();  // All NTA nodes in the tree.

  private ArrayList<NodeReference> displayedReferences;

  private int normalNodes = 0;

  private final Map<Class, List<Method>> ntaChildMethods = new HashMap<>();
  private final Map<Class, List<Pair<Method, Annotation>>> childAccessMethods = new HashMap<>();
  private final Map<String, Set<Class>> classParents = new HashMap<>();
  private final Map<Class<?>, String[]> classHierarchyMap = new HashMap<>();
  private Method childIndexMethod;
  private Class<?> astBaseType;

  public FilteredTreeBuilder(Object root, TreeFilter filter) {
    this.treeFilter = filter;
    displayedReferences = new ArrayList<>();

    // Build DrAST internal representation of the compiler AST.
    tree = buildAst(root);

    if (root != null) {
      // Build the filtered representation of the AST.
      ArrayList<NodeReference> futureReferences = new ArrayList<>();
      filteredTree = buildFilteredTree(new ArrayList<>(), tree, null, true,
          DrASTSettings.getInt(DrASTSettings.NTA_DEPTH, 1), futureReferences);
      // Add reference edges that defined in the filter language:
      addReferences(futureReferences, false);
    }
  }

  /**
   * Builds the internal representation of the AST. This is later used to build
   * the filtered graph view of the AST.
   */
  private Node buildAst(Object root) {
    ReflectionNode.Builder builder = ReflectionNode.builder(root);
    if (root != null) {
      builder.children(findChildren(builder));
      builder.classHierarchy(getClassHierarchy(root.getClass()));
    }
    return builder.build();
  }

  private Collection<ReflectionNode.Builder> findChildren(ReflectionNode.Builder parentBuilder) {
    Object astNode = parentBuilder.astObject();
    addAstObject(astNode, parentBuilder.isNta());
    List<ReflectionNode.Builder> children = new ArrayList<>();
    try {
      if (parentBuilder.isOpt() && !(astNode instanceof Iterable<?>)) {
        children.addAll(optChildren(parentBuilder));
      } else if (parentBuilder.isList() || parentBuilder.isOpt()) {
        children.addAll(childrenFromIterable(parentBuilder, (Iterable<?>) astNode));
      }
    } catch (Exception e) {
      e.printStackTrace();
      Log.warning("Class cast exception when building filtered tree: " + e.getMessage());
    }

    try {
      for (Pair<Method, Annotation> pair : getChildAccessMethods(astNode.getClass())) {
        Method method = pair.getFirst();
        Annotation annotation = pair.getSecond();
        Object child = method.invoke(astNode, new Object[method.getParameterCount()]);
        String name = ASTAnnotation.getString(annotation, ASTAnnotation.AST_METHOD_NAME);
        if (name.isEmpty()) {
          name = method.getName();
        }
        ReflectionNode.Builder builder =
        ReflectionNode.builder(child)
            .name(name)
            .isList(!ASTAnnotation.isSingleChild(annotation))
            .isOpt(ASTAnnotation.isOptChild(annotation))
            .isNta(parentBuilder.isNta());
        if (child != null) {
          builder.children(findChildren(builder));
          builder.classHierarchy(getClassHierarchy(child.getClass()));
        }
        children.add(builder);
      }
      return orderChildren(astNode, children);
    } catch (Exception e) {
      e.printStackTrace();
      Log.warning("Exception when building filtered tree: " + e.getMessage());
      return Collections.emptyList();
    }
  }

  /**
   * Optional children are iterated using the astChildren method on the Opt node.
   */
  @Nonnull
  private List<ReflectionNode.Builder> optChildren(ReflectionNode.Builder parentBuilder)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Object astNode = parentBuilder.astObject();
    Method method = astNode.getClass().getMethod(ASTAnnotation.AST_ORDER_METHOD);
    if (method == null) {
      return Collections.emptyList();
    }
    Object value = method.invoke(astNode);
    if (value == null) {
      return Collections.emptyList();
    }
    return childrenFromIterable(parentBuilder, (Iterable<?>) value);
  }

  /**
   * Add children from some iterable source.
   */
  private List<ReflectionNode.Builder> childrenFromIterable(ReflectionNode.Builder parentBuilder,
      Iterable<?> iterable) {
    List<ReflectionNode.Builder> children = new ArrayList<>();
    for (Object child : iterable) {
      if (child instanceof Collection && ASTAnnotation.isList(child.getClass())
          && parentBuilder.isOpt()) {
        Log.warning("A List is a direct child to a Opt parent, parent : %s, -> child : %s",
            parentBuilder.astObject(), child);
      }
      ReflectionNode.Builder builder = ReflectionNode.builder(child);
      if (parentBuilder.isOpt()) {
        builder.name(parentBuilder.name());
      }
      builder.isList(child instanceof Collection);
      builder.isNta(parentBuilder.isNta());
      if (child != null) {
        builder.children(findChildren(builder));
        builder.classHierarchy(getClassHierarchy(child.getClass()));
      }
      children.add(builder);
    }
    return children;
  }

  /**
   * Orders the children according to the ASTNode.getIndexOfChild() method.
   */
  @Nonnull
  private List<ReflectionNode.Builder> orderChildren(Object astNode,
      List<ReflectionNode.Builder> children)
      throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
    List<ReflectionNode.Builder> orderedChildren = new ArrayList<>(children);
    Method orderMethod = getChildIndexMethod(astNode.getClass());
    for (ReflectionNode.Builder child : children) {
      int newPos = (int) orderMethod.invoke(astNode, child.astObject());
      if (newPos >= children.size() || newPos < 0) {
        continue;
      }
      orderedChildren.set(newPos, child);
    }
    return orderedChildren;
  }

  public boolean hasRoot() {
    return !tree.isNullNode();
  }

  public Class<?> getRootClass() {
    return tree.getAstClass();
  }

  public int getClusteredASTSize() {
    return normalNodes;
  }

  public int getASTSize() {
    return astObjects.size() + astNtaObjects.size();
  }

  /**
   * This is used to construct the graph view of the AST.
   *
   * <p>The graph view collapses some nodes into clusters based on the
   * filter rules.
   */
  private GenericTreeNode buildFilteredTree(List<Node> path, Node node,
      GenericTreeNode parent, boolean parentIncluded, int depth,
      List<NodeReference> futureReferences) {
    int prefixSize = path.size();
    List<Attribute> displayedAttributes = Collections.emptyList();
    try {
      GenericTreeNode graphNode;
      Rule rule = treeFilter.findRuleForNode(path, node);
      boolean included = parentIncluded;
      if (rule != null) {
        if (rule.isInclude()) {
          included = true;
        } else if (rule.isExclude()) {
          included = false;
        }
      }
      if (included) {
        normalNodes++;
        graphNode = new TreeNode(node, parent);
        if (rule != null) {
          graphNode.setStyles(rule);
          displayedAttributes = rule.shownAttributes();
        }
      } else if (parent != null && parent.isCluster()) {
        graphNode = parent;
      } else {
        graphNode = new TreeCluster(node, parent);
      }

      if (!node.isNullNode()) {
        treeNodes.put(node.getAstObject(), graphNode);
      }

      if (parent != null) {
        // TODO(joqvist): if the parent is a cluster this adds the parent as a child of itself?
        parent.addChild(node, graphNode);
      }

      if (node.isNullNode()) {
        return graphNode;
      }

      path.add(node);

      addNtaChildren(path, node, graphNode, included, depth, futureReferences, displayedAttributes);

      if (included) {
        graphNode.setDisplayedAttributes(futureReferences, displayedAttributes, this);
      }

      for (Node child : node.getChildren()) {
        buildFilteredTree(path, child, graphNode, included, depth, futureReferences);
      }

      mergeClusters(graphNode);
      return graphNode;
    } finally {
      // Reset the prefix path length.
      while (path.size() > prefixSize) {
        path.remove(path.size() - 1);
      }
    }
  }

  /**
   * Merges all cluster children without children in the given node into a single
   * parent cluster child.
   */
  private void mergeClusters(GenericTreeNode graphNode) {
    if (graphNode.isNonCluster()) {
      // Get all children cluster children that have no children.
      List<GenericTreeNode> clusters = graphNode.getChildren().stream()
          .filter(child -> child.isCluster() && child.getChildren().isEmpty())
          .collect(Collectors.toList());
      if (!clusters.isEmpty()) {
        TreeClusterParent clusterParent = new TreeClusterParent(graphNode);
        for (GenericTreeNode cluster : clusters) {
          clusterParent.addChild(null, cluster);
          graphNode.getChildren().remove(cluster);
        }
        graphNode.addChild(graphNode.getNode(), clusterParent);
      }
    }
  }

  /**
   * Adds the NTA that should be visible to the filtered tree.
   */
  private void addNtaChildren(List<Node> path, Node node, GenericTreeNode parent, boolean included,
      int depth, List<NodeReference> futureReferences, List<Attribute> displayedAttributes) {
    if (depth > 0) {
      // Create the NTA nodes specified by the filter language, and traverse down the NTAs:
      for (Attribute attribute : displayedAttributes) {
        Object[] args = attribute.evalArgsSafely(node, Log::log);
        try {
          Method method = ReflectionNode.getMethodForArgs(node.getAstClass(),
              attribute.getNAME(), args);
          if (getNtaChildMethods(node.getAstClass()).contains(method)) {
            Object value = method.invoke(node.getAstObject(), args);
            Node ntaNode = buildNtaChild(node, value, method.getName());
            buildFilteredTree(path, ntaNode, parent, included, depth - 1, futureReferences);
          }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
          Log.error("Error while evaluating NTA '%s': %s", attribute, e.getMessage());
        }
      }
    }

    if (DrASTSettings.getFlag(DrASTSettings.NTA_CACHED)) {
      setCachedNtas(node);
      for (Node child : node.getNtaChildren().values()) {
        if (!astNtaObjects.contains(child.getAstObject())) {
          astNtaObjects.add(child.getAstObject());
        }
        buildFilteredTree(path, child, parent, included, 0, futureReferences);
      }
    }

    // Traverse down the tree for the Computed NTAs:
    if (computedNTAs.containsKey(node) && DrASTSettings.getFlag(DrASTSettings.NTA_COMPUTED)) {
      computedNTAs.get(node).stream().filter(child -> !treeNodes.containsKey(child.getAstObject()))
          .forEach(child -> buildFilteredTree(path, child, parent, included, 0,
              futureReferences));
    }
  }

  /**
   * Adds the references to the mainList.
   * Which will be handled by an outside party to display.
   * This needs to be separated from the main structure
   */
  private void addReferences(ArrayList<NodeReference> futureReferences, boolean appendList) {
    for (NodeReference ref : futureReferences) {
      ArrayList<GenericTreeNode> nodeRefs = new ArrayList<>();
      GenericTreeNode to;
      if (!ref.getReferenceFrom().isNonCluster()) {
        continue;
      }
      for (Object obj : ref.getFutureReferences()) {
        if (isTreeNode(obj)) {
          to = getTreeNode(obj);
          nodeRefs.add(to);
          to.addInWardNodeReference(ref);
        }
      }
      ref.setReferences(nodeRefs);
    }
    if (!appendList) {
      displayedReferences = futureReferences;
    } else {
      displayedReferences.addAll(futureReferences);
    }
  }

  /**
   * Reevaluates the API:s filtered tree
   */
  private void buildFilteredSubTree(Node node, TreeNode parent) {
    ArrayList<NodeReference> futureReferences = new ArrayList<>();
    Node temp = node.getParent();
    Stack<Node> stack = new Stack<>();
    while (temp != null) {
      stack.add(temp);
      temp = temp.getParent();
    }
    List<Node> path = new ArrayList<>(stack.size());
    while (!stack.isEmpty()) {
      path.add(stack.pop());
    }
    buildFilteredTree(path, node, parent, true, 0, displayedReferences);
    addReferences(futureReferences, true);
  }

  public GenericTreeNode getFilteredTree() {
    return filteredTree;
  }

  public boolean isAstType(Class type) {
    return allTypes.contains(type);
  }

  public GenericTreeNode getTreeNode(Node node) {
    return treeNodes.get(node.getAstObject());
  }

  public GenericTreeNode getTreeNode(Object node) {
    return treeNodes.get(node);
  }

  public boolean isTreeNode(Object node) {
    return treeNodes.containsKey(node);
  }

  private boolean isAstObject(Object node) {
    return astObjects.contains(node) || astNtaObjects.contains(node);
  }

  private void addAstObject(Object node, boolean nta) {
    if (nta) {
      astNtaObjects.add(node);
    } else {
      astObjects.add(node);
    }
  }

  public List<NodeReference> getDisplayedReferences() {
    return displayedReferences;
  }

  public List<Object> getNodeReferences(Object value) {
    if (value == null) {
      return Collections.emptyList();
    }

    if (value instanceof Collection<?>) {
      Collection<?> iterable = (Collection<?>) value;
      return iterable.stream().filter(this::isAstObject).collect(Collectors.toList());
    } else if (isAstObject(value)) {
      return Collections.singletonList(value);
    } else {
      return Collections.emptyList();
    }
  }

  /**
   * Computes the method for the NodeInfo.
   * If a NTA is found it will be added to the reflected and the filtered tree.
   */
  public Object compute(Node node, TerminalValue info, Object[] params) {
    if (info == null) {
      return null;
    }
    Object obj = node.computeAttribute(info, params);
    if (!info.isNTA() || astNtaObjects.contains(obj) || obj == null) {
      return obj;
    }

    Node astNode = buildNtaChild(node, obj, info.getName());

    if (!computedNTAs.containsKey(node)) {
      computedNTAs.put(node, new HashSet<>());
    }
    computedNTAs.get(node).add(astNode);
    GenericTreeNode parent = treeNodes.get(node.getAstObject());
    if (DrASTSettings.getFlag(DrASTSettings.NTA_COMPUTED) && parent.isNonCluster()) {
      buildFilteredSubTree(astNode, (TreeNode) parent);
    } else {
      Log.warning(
          "Computed NTA successfully, but the configuration %s is not enabled, so the NTA will not be shown. See the DrAST.cfg file.",
          DrASTSettings.NTA_COMPUTED);
    }
    return obj;
  }

  /**
   * Returns the methods used to access AST children for the given class.
   */
  private List<Pair<Method, Annotation>> getChildAccessMethods(Class klass) {
    identifyChildMethods(klass);
    return childAccessMethods.get(klass);
  }

  /**
   * Returns the methods used to access NTA children for the given class.
   */
  private List<Method> getNtaChildMethods(Class klass) {
    identifyChildMethods(klass);
    return ntaChildMethods.get(klass);
  }

  private void identifyChildMethods(Class klass) {
    if (!childAccessMethods.containsKey(klass)) {
      List<Pair<Method, Annotation>> methods = new ArrayList<>();
      List<Method> ntaMethods = new ArrayList<>();
      for (Method method : klass.getMethods()) {
        for (Annotation annotation : method.getAnnotations()) {
          if (ASTAnnotation.isChild(annotation)) {
            methods.add(new Pair<>(method, annotation));
          } else if (ASTAnnotation.isAttribute(annotation)
              && ASTAnnotation.is(annotation, ASTAnnotation.AST_METHOD_NTA)) {
            ntaMethods.add(method);
          }
        }
      }
      childAccessMethods.put(klass, methods);
      ntaChildMethods.put(klass, ntaMethods);
    }
  }

  private String[] getClassHierarchy(Class<?> klass) {
    if (!classHierarchyMap.containsKey(klass)) {
      List<String> hierarchy = new ArrayList<>();
      Class<?> superclass = klass;
      while (superclass != null) {
        allTypes.add(superclass);
        hierarchy.add(superclass.getSimpleName());
        superclass = superclass.getSuperclass();
      }
      String[] array = new String[hierarchy.size()];
      hierarchy.toArray(array);
      classHierarchyMap.put(klass, array);
    }
    return classHierarchyMap.get(klass);
  }

  /**
   * @return the ASTNode.getIndexOfChild() method.
   */
  private Method getChildIndexMethod(Class<?> someNodeClass) throws NoSuchMethodException {
    if (astBaseType == null) {
      Class tempClass = someNodeClass;
      while (tempClass != null && !tempClass.getSimpleName().equals("ASTNode")) {
        tempClass = tempClass.getSuperclass();
      }
      astBaseType = tempClass;
    }
    if (childIndexMethod == null) {
      assert astBaseType != null;
      childIndexMethod = astBaseType.getDeclaredMethod("getIndexOfChild", astBaseType);
    }
    return childIndexMethod;
  }

  public boolean isChildClass(Object ASTObject, String name) {
    if (ASTObject == null || name == null) {
      return false;
    }
    Class<?> klass = ASTObject.getClass();
    if (!classParents.containsKey(name)) {
      classParents.put(name, new HashSet<>());
    }
    Set<Class> children = classParents.get(name);
    if (children.contains(klass)) {
      return true;
    }
    while (klass != null) {
      if (klass.getName().equals(name)) {
        children.add(ASTObject.getClass());
        return true;
      }
      klass = klass.getSuperclass();
    }
    return false;
  }

  private void setCachedNtas(Node node) {
    try {
      for (Map.Entry<Method, Object> entry : getCachedNtaValues(node).entrySet()) {
        Method method = entry.getKey();
        Object value = entry.getValue();
        if (method.getParameterCount() > 0) {
          if (value instanceof Map) {
            // NB: must create copy of the attribute cache to avoid concurrent modification
            // while building the NTA tree (which may depend circularly on the attribute itself).
            Map<?, ?> map = (Map) value;
            Collection<?> values = new ArrayList<>(map.values());
            for (Object computedValue : values) {
              createNtaTree(node, computedValue, method.getName());
            }
          }
        } else {
          createNtaTree(node, value, method.getName());
        }
      }
    } catch (ClassCastException e) {
      Log.error(e.getMessage());
    }
  }

  private void createNtaTree(Node node, Object child, String name) {
    if (!node.getNtaChildren().containsKey(child) && !isAstObject(child)) {
      // Add the NTA tree only if there is not already a reference to it from the parent node.
      node.addNtaChild(child, buildNtaChild(node, child, name));
    }
  }

  /**
   * Builds a filtered tree node for a Nonterminal child.
   * @param name the name of the NTA in the parent node.
   */
  @Nonnull private Node buildNtaChild(Node parent, Object child, String name) {
    ReflectionNode.Builder builder = ReflectionNode.builder(child)
        .parent(parent).name(name).isNta(true);
    if (child != null) {
      builder.isList(ASTAnnotation.isList(child.getClass()));
      builder.children(findChildren(builder));
      builder.classHierarchy(getClassHierarchy(child.getClass()));
    }
    return builder.build();
  }

  private Map<Method, Object> getCachedNtaValues(Node node) {
    List<Method> methods = getNtaChildMethods(node.getAstClass());
    if (methods.isEmpty()) {
      return Collections.emptyMap();
    }
    Map<Method, Object> ntaValueMap = new HashMap<>();
    for (Method method : methods) {
      Pair<Field, Field> fieldPair = node.getCacheFields(method);
      Field field = fieldPair.getSecond();
      if (!isComputed(node.getAstObject(), field) && method.getParameterCount() == 0) {
        continue;
      }
      field = node.getCacheFields(method).getFirst();
      Object value = getFieldValue(node.getAstObject(), field);
      if (value != null) {
        ntaValueMap.put(method, value);
      }
    }
    return ntaValueMap;
  }

  public static boolean isComputed(Object object, Field field) {
    if (field == null) {
      return false;
    }
    Object obj = getFieldValue(object, field);
    if (obj != null && (obj.getClass() == Boolean.class || obj.getClass() == boolean.class)) {
      return (Boolean) obj;
    }
    return obj != null;
  }

  public static Object getFieldValue(Object object, Field field) {
    if (field == null) {
      return null;
    }
    try {
      field.setAccessible(true);
      return field.get(object);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }
}
