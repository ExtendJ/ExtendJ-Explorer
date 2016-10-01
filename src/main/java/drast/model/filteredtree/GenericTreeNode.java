package drast.model.filteredtree;

import drast.model.FilteredTreeBuilder;
import drast.model.Node;
import org.jastadd.drast.filterlang.Attribute;
import org.jastadd.drast.filterlang.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The GenericTreeNode is the class that represents the nodes in the filteredTree, i.e. the tree after
 * it has been filtered by the configurations.
 * The node itself contains references to its parent and children and which cluster it is part of, if any.
 * Created by gda10jli on 11/2/15.
 */
public abstract class GenericTreeNode {
  protected final List<GenericTreeNode> children;
  protected GenericTreeNode clusterRef;
  protected final GenericTreeNode parent;
  protected final HashMap<String, String> styles;
  private boolean isExpandable;

  public GenericTreeNode(GenericTreeNode parent) {
    this.parent = parent;
    children = new ArrayList<>();
    styles = new HashMap<>();
    isExpandable = false;
  }

  public GenericTreeNode getParent() {
    return parent;
  }

  /**
   * Set if that node can be collapsed into a cluster by someone else than the configurations.
   */
  public void setExpandable() {
    isExpandable = true;
  }

  /**
   * Check if the node can be collapsed into a cluster by someone else than the configurations.
   */
  public boolean isExpandable() {
    return isExpandable;
  }

  public List<GenericTreeNode> getChildren() {
    return children;
  }

  public abstract void addChild(Node node, GenericTreeNode child);

  /**
   * Returns the api.Node of the filterNode, the Node containing the AST object.
   */
  public abstract Node getNode();

  /**
   * Check if the node is an NTA
   */
  public abstract boolean isNTANode();

  /**
   * Check if the node is a null node, i.e. if the object for the node is null
   */
  public abstract boolean isNullNode();

  /**
   * Check if the node is a "real" node
   */
  public abstract boolean isNonCluster();

  /**
   * Check if the node is a cluster of nodes.
   */
  public abstract boolean isCluster();

  /**
   * Check if the node is a cluster of clusters
   */
  protected abstract boolean isClusterParent();

  /**
   * Creates the string containing the nodes name and the displayed attributes for the graph view
   */
  public abstract String toGraphString();

  /**
   * Sets the style of the nodes
   */
  public abstract void setStyles(Rule rule);

  /**
   * Returns a list of the node references pointing inwards
   */
  public abstract Map<NodeReference, NodeReference> getInwardNodeReferences();

  /**
   * Returns a list of the node references pointing outwards
   */
  public abstract Set<NodeReference> getOutwardNodeReferences();

  /**
   * Returns a list containing all node references, outwards and inwards.
   */
  public abstract Set<NodeReference> getAllNodeReferences();

  public abstract void addInWardNodeReference(NodeReference ref);

  public abstract void setDisplayedAttributes(List<NodeReference> allReferences, List<Attribute> attributes,
      FilteredTreeBuilder traverser);

  public String getStyle(String name) {
    String value = styles.get(name);
    return value == null ? "" : value;
  }

  /**
   * Returns the top cluster that this node is a part of, if it a "real" node it will return itself
   */
  public GenericTreeNode getTreeNode() {
    if (clusterRef == null) {
      return this;
    } else {
      return clusterRef.getTreeNode();
    }
  }

  /**
   * Sets a reference to which cluster the node is gonna be a part of.
   */
  public void setClusterReference(GenericTreeCluster clusterRef) {
    this.clusterRef = clusterRef;
  }

}
