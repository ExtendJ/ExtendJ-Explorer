package jastaddad.api;

import jastaddad.api.filteredtree.*;
import jastaddad.api.objectinfo.NodeInfo;

import java.util.*;

/**
 * ASTAPI is the API of the JastAddAd system. It traverse the AST and generate a filtered AST. After this the AST can be
 * reached from the outside via this class.
 *
 * This file takes the root Object of the JastAdd AST and travers the tree.
 * Each node in the filtered AST is a sub class of the GenericTreeNode.
 */
public class ASTAPI {

    public static final String VERSION = "alphabuild-0.1.0";

    public static final String AST_STRUCTURE_WARNING = "AST structure warning";
    public static final String AST_STRUCTURE_ERROR = "AST structure error";
    public static final String FILTER_ERROR = "filter error";

    private Node tree;
    private GenericTreeNode filteredTree;
    private Config filterConfig;
    private HashMap<String, Integer> typeHash;
    private HashMap<String, List<TreeNode>> typeNodeHash;  // will probably be removed
    private HashMap<Object, GenericTreeNode> nodeReferences;
    private HashSet<Object> objectReferences;
    private HashMap<String, ArrayList<String>> errors;
    private HashMap<String, ArrayList<String>> warnings;
    private ArrayList<NodeReference> displayedReferences;
    private String directoryPath;

    public ASTAPI(Object root, String filterDir){
        directoryPath = filterDir;
        displayedReferences = new ArrayList<>();
        nodeReferences = new HashMap();
        objectReferences = new HashSet<>();
        typeHash = new HashMap<>();
        typeNodeHash = new HashMap<>(); // will probably be removed
        errors = new HashMap<>();
        warnings = new HashMap<>();

        tree = new Node(root, this);
        this.filteredTree = null;
        filterConfig = new Config(this, filterDir);
        traversTree(this.tree, null, null, true);
    }

    public String getFilterFilePath(){return directoryPath + "filter.cfg"; }
    public String getDirectoryPath(){return directoryPath;}
    /**
     * Old function, will probably be removed.
     *
     * Changes the enabled bool in all nodes of a sertain type.
     *
     * @param type
     * @param enabled
     * @return
     */
    public boolean newTypeFiltered(String type, boolean enabled){
        for(TreeNode fNode : typeNodeHash.get(type) ){
            fNode.setEnabled(enabled);
            addToConfigs(fNode);
        }
        traversTree(this.tree, null, null, true);
        return true;
    }

    public ArrayList<String> getErrors(String type){ return getMessageLine(errors, type); }

    public ArrayList<String> getWarnings(String type){ return getMessageLine(warnings, type); }

    public void putWarning(String type, String message){ putMessageLine(warnings, type, message); }

    public void putError(String type, String message){ putMessageLine(errors, type, message); }

    /**
     * Return all errors written under a certain catogory type.
     * The errors will be removed and can not be fetched again through this method.
     * @param map
     * @param type
     * @return
     */
    private ArrayList<String> getMessageLine(HashMap<String, ArrayList<String>> map, String type){
        if(!map.containsKey(type)) {
            map.put(type, new ArrayList<>());
            return map.get(type);
        }
        ArrayList<String> ret = map.get(type);
        map.put(type, new ArrayList<>());
        return ret;
    }

    private void putMessageLine(HashMap<String, ArrayList<String>> map, String type, String message){
        if(!map.containsKey(type))
            map.put(type, new ArrayList<>());
        map.get(type).add(message);
    }

    /**
     * Old function, will probably be removed.
     * @param fNode
     */
    private void addToTypes(TreeNode fNode){
        // add the node to the hashmap of types
        Node node = fNode.getNode();
        if(!typeNodeHash.containsKey(node.className))
            typeNodeHash.put(node.className, new ArrayList<>());
        typeNodeHash.get(node.className).add(fNode);
        if(node.name != "") {
            if(!typeNodeHash.containsKey(node.fullName))
                typeNodeHash.put(node.fullName, new ArrayList<>());
            typeNodeHash.get(node.fullName).add(fNode);
        }
    }

    /**
     * Old function, will probably be removed.
     *
     * @param fNode
     */
    private void addToConfigs(TreeNode fNode){
        // Add the node to the config hash
        Node node = fNode.getNode();
        if(node.name != "")
            typeHash.put(node.fullName, fNode.isEnabled() ? 1:0);
        typeHash.put(node.className, fNode.isEnabled() ? 1:0);
    }

    private void traversTree(Node node, GenericTreeNode parent, TreeCluster cluster, boolean firstTime){
        ArrayList<NodeReference> futureReferences = new ArrayList<>();
        traversTree(node, parent, cluster, firstTime, futureReferences);

        // Add reference edges that is defined in the filter language
        for (NodeReference ref : futureReferences){
            ArrayList<GenericTreeNode> nodeRefs = new ArrayList<>();
            GenericTreeNode to;
            for (Object obj : ref.getFutureReferences()){
                if(isNodeReference(obj)){
                    to = getNodeReference(obj);
                    nodeRefs.add(to);
                    to.addInWardNodeReference(ref);
                }
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
        nodeReferences.put(node.node, fNode);
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

    /**
     * Put child clusters together in a parent cluster if they have no children in the filtered tree
     *
     * @param fNode
     */
    private void clusterClusters(TreeNode fNode){
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

    public GenericTreeNode getNodeReference(Object node){ return nodeReferences.get(node); }
    public boolean isNodeReference(Object node){ return nodeReferences.containsKey(node); }

    public boolean isObjectReference(Object node){ return objectReferences.contains(node); }
    public boolean addObjectReference(Object node){ return objectReferences.add(node); }

    public void clearDisplayedReferences(){ displayedReferences.clear(); }
    public ArrayList<NodeReference> getDisplayedReferences(){ return displayedReferences; }

    public GenericTreeNode getFilteredTree(){
        return filteredTree;
    }

    /**
     * Write the new filter text to file and generate a new filtered AST
     * @param text
     * @return true if the filter was saved to file and a new filtered AST was successfully created, otherwise false
     */
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

    public ArrayList<GenericTreeNode> getNodeReferences(NodeInfo info, boolean highlight){
        ArrayList<GenericTreeNode> nodes = new ArrayList();
        for(Object o : getNodeReferences(info)){
            nodes.add(getNodeReference(o).setReferenceHighlight(highlight));
        }
        return nodes;
    }

    public ArrayList<Object> getNodeReferences(NodeInfo info){
        ArrayList<Object> nodes = new ArrayList();
        if(info == null)
            return nodes;
        if(info.getValue() instanceof Collection<?>) {
            for (Object n : (Iterable<Object>) info.getValue()) {
                if (isObjectReference(n))
                    nodes.add(n);
            }
        }else if (isObjectReference(info.getValue()))
            nodes.add(info.getValue());
        return nodes;
    }
}
