package drast.model.filteredtree;

import drast.model.FilteredTreeBuilder;
import drast.model.Node;
import org.jastadd.drast.filterlang.Attribute;
import org.jastadd.drast.filterlang.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Abstract class which contains the similarities between Cluster and ClusterParent
 * Created by gda10jli on 11/11/15.
 */
public abstract class GenericTreeCluster extends GenericTreeNode {

  protected int nodeCount;
  protected final ArrayList<GenericTreeCluster> clusters;

  public GenericTreeCluster(GenericTreeNode parent) {
    super(parent);
    nodeCount = 0;
    this.clusters = new ArrayList<>();

    // Default styles.
    if (isExpandable()) {
      styles.put("node-color", "#DCDCaa");
    } else {
      styles.put("node-color", "#DCDCDC");
    }
    styles.put("node-shape", "small_circle");
    styles.put("border-style", "dashed");
  }

  protected abstract HashMap<String, Integer> fillTypeList(HashMap<String, Integer> types);

  public HashMap<String, Integer> getTypeList() {
    return fillTypeList(new HashMap<>());
  }

  public ArrayList<GenericTreeCluster> getClusters() {
    return clusters;
  }

  public int getNodeCount() {
    return nodeCount;
  }

  @Override public boolean isNonCluster() {
    return false;
  }

  @Override public String toString() {
    return "...";
  }

  @Override public String toGraphString() {
    return String.valueOf(nodeCount);
  }

  @Override public void setStyles(Rule rule) {
  }

  /** Cluster has no "real" node. */
  @Override public Node getNode() {
    return null;
  }

  /** Clusters can not be NTANodes. */
  @Override public boolean isNTANode() {
    return false;
  }

  /**
   * Clusters contains no references
   */
  @Override public void setDisplayedAttributes(List<NodeReference> allReferences, List<Attribute> attributes,
      FilteredTreeBuilder traverser) {
  }

  /** Clusters can not be nullNodes. */
  @Override public boolean isNullNode() {
    return false;
  }

  /**
   * Clusters contains no references
   */
  @Override public HashSet<NodeReference> getOutwardNodeReferences() {
    return null;
  }


  /** Clusters can not contain references. */
  @Override public HashMap<NodeReference, NodeReference> getInwardNodeReferences() {
    return null;
  }


  /** Clusters can not contain references. */
  @Override public HashSet<NodeReference> getAllNodeReferences() {
    return null;
  }


  /** Clusters can not contain references. */
  @Override public void addInWardNodeReference(NodeReference ref) {
  }

}
