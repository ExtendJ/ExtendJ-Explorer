package jastaddad;

import jastaddad.filteredtree.*;
import jastaddad.objectinfo.NodeInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gda10jth on 10/16/15.
 */
public class ASTAPI {
    private Node tree;
    private GenericTreeNode filteredTree;
    private Config filterConfig;
    private HashMap<String, Integer> typeHash;
    private HashMap<String, List<TreeNode>> typeNodeHash;
    private HashMap<Object, GenericTreeNode> realNodeRefs;
    private HashMap<Object, Object> realReferences;
    private HashMap<String, ArrayList<String>> errors;
    private ArrayList<NodeReference> displayedReferences;

    public ASTAPI(Object root){
        displayedReferences = new ArrayList<>();
        realNodeRefs = new HashMap();
        realReferences = new HashMap<>();
        typeHash = new HashMap<>();
        typeNodeHash = new HashMap<>();
        errors = new HashMap<>();

        errors.put("filter", new ArrayList<>());
        tree = new Node(root, realReferences);
        this.filteredTree = null;
        filterConfig = new Config(errors);
        traversTree(this.tree, null, null, true);
    }

    public boolean newTypeFiltered(String type, boolean enabled){
        for(TreeNode fNode : typeNodeHash.get(type) ){
            fNode.setEnabled(enabled);
            addToConfigs(fNode);
        }
        traversTree(this.tree, null, null, true);
        return true;
    }

    public ArrayList<String> getErrors(String errorType){
        if(errors.containsKey(errorType)) {
            ArrayList<String> ret = errors.get(errorType);
            errors.put(errorType, new ArrayList<>());
            return ret;
        }
        return new ArrayList<>();
    }

    private void addToTypes(TreeNode fNode){
        // add the node to the hashmap of types
        if(!typeNodeHash.containsKey(fNode.node.className))
            typeNodeHash.put(fNode.node.className, new ArrayList<>());
        typeNodeHash.get(fNode.node.className).add(fNode);
        if(fNode.node.name != "") {
            if(!typeNodeHash.containsKey(fNode.node.fullName))
                typeNodeHash.put(fNode.node.fullName, new ArrayList<>());
            typeNodeHash.get(fNode.node.fullName).add(fNode);
        }
    }

    private void addToConfigs(TreeNode fNode){
        // Add the node to the config hash
        if(fNode.node.name != "")
            typeHash.put(fNode.node.fullName, fNode.isEnabled() ? 1:0);
        typeHash.put(fNode.node.className, fNode.isEnabled() ? 1:0);
    }

    private void traversTree(Node node, GenericTreeNode parent, TreeCluster cluster, boolean firstTime){
        ArrayList<NodeReference> futureReferences = new ArrayList<>();
        traversTree(node, parent, cluster, firstTime, futureReferences);
        for (NodeReference ref : futureReferences){
            ArrayList<GenericTreeNode> nodeRefs = new ArrayList<>();
            for (Object o : ref.getFutureReferences()){
                if(isReferenceNode(o))
                    nodeRefs.add(getReferenceNode(o));
            }
            ref.setReferences(nodeRefs);
        }
        displayedReferences = futureReferences;
    }

    private void traversTree(Node node, GenericTreeNode parent, TreeCluster cluster, boolean firstTime, ArrayList<NodeReference> futureReferences){
        if(node == null)
            return;
        GenericTreeNode addToParent = null;
        TreeNode fNode = new TreeNode(node, parent, filterConfig);
        fNode.setStyles(filterConfig);
        realNodeRefs.put(node.node, fNode);
        TreeCluster tmpCluster = cluster;

        if(firstTime) {
            addToTypes(fNode);
            addToConfigs(fNode);
        }

        // if the class is not filtered
        if(fNode.isEnabled()){
            // is this the root?
            if(parent == null){
                filteredTree = fNode;
            }else{
                // if the above node a cluster?
                if(tmpCluster == null){
                    addToParent = fNode;
                }else{
                    tmpCluster.addChild(fNode);
                }
            }
            tmpCluster = null;
        }else{
            // first node in this cluster?
            if(tmpCluster == null){
                tmpCluster = new TreeCluster(fNode, parent);
                tmpCluster.setStyles(filterConfig);
                // is this cluster the root of the tree?
                if(parent != null)
                    addToParent = tmpCluster;

            }else{
                // add node to cluster but not to the filtered tree
                addToParent = fNode;
            }
            if(parent == null) // first node in tree
                filteredTree = tmpCluster;
        }

        // travers down the tree
        for(Node child : node.children){
            traversTree(child, fNode, tmpCluster, firstTime, futureReferences);
        }
        fNode.setClusterReference(tmpCluster);
        clusterClusters(fNode);

        if(addToParent != null)
            parent.addChild(addToParent);

        fNode.setDisplayedAttributes(filterConfig, futureReferences , this);
    }

    private void clusterClusters(TreeNode fNode){
        // put child clusters together in a parent cluster if they have no children in the filtered tree
        if(fNode.isNode()) {
            //FilteredTreeNode n = (FilteredTreeNode) fNode;
            TreeClusterParent clusterParent = new TreeClusterParent(fNode);
            clusterParent.setStyles(filterConfig);
            // get all children cluster children that have no children
            for (GenericTreeNode fChild : fNode.getChildren()) {
                if (fChild.isCluster() && fChild.getChildren().size() == 0) {
                    clusterParent.addCluster((TreeCluster)fChild);
                }
            }

            if(clusterParent.getClusters().size() > 0) {
                for(GenericTreeNode cChild : clusterParent.getClusters()) {
                    fNode.getChildren().remove(cChild);
                }
                fNode.addChild(clusterParent);
            }
        }
    }

    public HashMap<String, Integer> getTypeHash(){ return typeHash; }

    public GenericTreeNode getReferenceNode(Object node){ return realNodeRefs.get(node); }
    public boolean isReferenceNode(Object node){ return realNodeRefs.containsKey(node); }
    public boolean isRealReferenceNode(Object node){ return realReferences.containsKey(node); }

    public void clearDisplayedReferences(){ displayedReferences.clear(); }

    public ArrayList<NodeReference> getDisplayedReferences(){ return displayedReferences; }

    public GenericTreeNode getFilteredTree(){
        return filteredTree;
    }

    public boolean saveNewFilter(String text){
        boolean res = filterConfig.saveAndUpdateFilter(text);
        if(res) {
            clearDisplayedReferences();
            filteredTree = null;
            traversTree(this.tree, null, null, false);
        }
        return res;
    }

    public Config getfilterConfig(){ return filterConfig; }

    public ArrayList<GenericTreeNode> getReferenceNodes(NodeInfo info, boolean highlight){
        ArrayList<GenericTreeNode> nodes = new ArrayList();
        for(Object o : getReferenceNodes(info)){
            nodes.add(getReferenceNode(o).setReferenceHighlight(highlight));
        }
        return nodes;
    }

    public ArrayList<Object> getReferenceNodes(NodeInfo info){
        ArrayList<Object> nodes = new ArrayList();
        if(info == null)
            return nodes;
        if(info.getValue() instanceof Collection<?>) {
            for (Object n : (Iterable<Object>) info.getValue()) {
                if (isRealReferenceNode(n))
                    nodes.add(n);
            }
        }else if (isRealReferenceNode(info.getValue()))
            nodes.add(info.getValue());
        return nodes;
    }
}
