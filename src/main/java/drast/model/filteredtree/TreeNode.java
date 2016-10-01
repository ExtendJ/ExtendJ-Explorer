package drast.model.filteredtree;

import drast.Log;
import drast.model.FilteredTreeBuilder;
import drast.model.Node;
import org.jastadd.drast.filterlang.Attribute;
import org.jastadd.drast.filterlang.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * This class contains the node representation used in the graph view
 * of the filtered AST.
 *
 * Created by gda10jth on 10/16/15.
 */
public class TreeNode extends GenericTreeNode {
  private final Node node;
  private final ArrayList<String> labelAttributes;
  private HashSet<NodeReference> outwardReferences;
  private HashMap<NodeReference, NodeReference> inwardReferences;
  private final HashSet<NodeReference> allRefs;

  public TreeNode(Node data, GenericTreeNode parent) {
    super(parent);
    node = data;
    allRefs = new HashSet<>();
    labelAttributes = new ArrayList<>();
    setExpandable();

    // Default styles.
    if (node.isList() || node.isOpt()) {
      styles.put("node-color", "#dddddd");
      styles.put("node-shape", "rectangle");
    } else {
      styles.put("node-color", "#ccffff");
      styles.put("node-shape", "rounded_rectangle");
    }
    styles.put("border-style", "line");
  }

  public ArrayList<String> getLabelAttributes() {
    return labelAttributes;
  }

  /**
   * Returns the Object of the AST
   */
  @Override public Node getNode() {
    return node;
  }

  @Override public void addChild(Node node, GenericTreeNode child) {
    children.add(child);
  }

  @Override public boolean isNTANode() {
    return node.isNTANode();
  }

  @Override public boolean isNullNode() {
    return node.isNullNode();
  }

  @Override public boolean isNonCluster() {
    return true;
  }

  @Override public boolean isCluster() {
    return false;
  }

  @Override public boolean isClusterParent() {
    return false;
  }

  @Override public String toString() {
    return node.getSimpleClassName();
  }

  @Override public String toGraphString() {
    return toString();
  }

  /**
   * Set the node specific styles, derived from the config
   */
  @Override public void setStyles(Rule rule) {
    styles.putAll(rule.styleMap());
  }

  /**
   * This method will derive which attributes that should be displayed, it will also save the reference attributes as NodeReferences.
   * NOTE: The value which the method toGraphString() returns will be manipulated by this method.
   */
  @Override public void setDisplayedAttributes(List<NodeReference> allReferences, List<Attribute> attributes,
      FilteredTreeBuilder traverser) {
    if (attributes.size() == 0) {
      return;
    }
    if (outwardReferences == null) {
      outwardReferences = new HashSet<>();
    }
    for (Attribute attribute : attributes) {
      Object obj = attribute.evalSafely(node, Log::log);
      if (obj == null) {
        continue;
      }
      List<Object> refs = traverser.getNodeReferences(obj);
      if (refs != null && refs.size() > 0) {
        NodeReference reference = new NodeReference(attribute.getNAME(), this, refs);
        outwardReferences.add(reference);
        allReferences.add(reference);
        allRefs.add(reference);
      } else {
        labelAttributes.add(attribute + " : " + obj);
      }
    }
  }

  @Override public HashSet<NodeReference> getOutwardNodeReferences() {
    return outwardReferences;
  }

  @Override public HashMap<NodeReference, NodeReference> getInwardNodeReferences() {
    return inwardReferences;
  }

  @Override public void addInWardNodeReference(NodeReference ref) {
    if (inwardReferences == null) {
      inwardReferences = new HashMap<>();
    }
    if (!inwardReferences.containsKey(ref)) {
      inwardReferences.put(ref, new NodeReference(ref.getLabel(), ref.getReferenceFrom()));
    }
    allRefs.add(inwardReferences.get(ref));
    inwardReferences.get(ref).addReference(this);
  }

  @Override public HashSet<NodeReference> getAllNodeReferences() {
    return allRefs;
  }

}
