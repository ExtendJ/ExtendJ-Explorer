package jastaddad.api;

import jastaddad.api.filteredtree.*;
import jastaddad.api.nodeinfo.NodeInfo;

import java.util.*;

/**
 * ASTAPI is the API of the JastAddAd system. It traverse the AST and generate a filtered AST. After this the AST can be
 * reached from the outside via this class.
 *
 * This file takes the root Object of the JastAdd AST and travers the tree.
 * Each node in the filtered AST is a sub class of the GenericTreeNode.
 */
public class ASTAPI {

    public static final String VERSION = "alphabuild-0.2.2";

    public static final String AST_STRUCTURE_WARNING = "AST structure warning";
    public static final String AST_STRUCTURE_ERROR = "AST structure error";
    public static final String FILTER_ERROR = "filter error";
    public static final String INVOCATION_WARNING = "invocation warning";
    public static final String INVOCATION_ERROR = "invocation error";

    private Node tree;
    private GenericTreeNode filteredTree;
    private Config filterConfig;
    private HashMap<String, Integer> typeHash;
    private HashSet<Class> allTypes;
    private HashMap<String, List<TreeNode>> typeNodeHash;  // will probably be removed
    private HashMap<Object, GenericTreeNode> treeNodes;
    private HashMap<Node, HashSet<Node>> computedNTAs; //This might be a temporary solution,
    private HashSet<Object> ASTObjects;
    private HashSet<Object> ASTNTAObjects;
    private HashMap<String, ArrayList<String>> errors;
    private HashMap<String, ArrayList<String>> warnings;
    private ArrayList<NodeReference> displayedReferences;
    private String directoryPath;

    public ASTAPI(Object root, String filterDir){
        directoryPath = filterDir;
        displayedReferences = new ArrayList<>();
        treeNodes = new HashMap<>();
        typeHash = new HashMap<>();
        typeNodeHash = new HashMap<>(); // will probably be removed
        errors = new HashMap<>();
        warnings = new HashMap<>();
        computedNTAs = new HashMap<>();
        ASTObjects = new HashSet<>();
        ASTNTAObjects = new HashSet<>();
        allTypes = new HashSet<>();

        tree = new Node(root, this);
        this.filteredTree = null;
        filterConfig = new Config(this, filterDir);
        traversTree(this.tree, null, null, true);
    }

    public String getFilterFilePath(){return directoryPath + "filter.cfg"; }
    public String getDirectoryPath(){return directoryPath;}

    public boolean isTypeFromAst(Class type){
        return allTypes.contains(type);
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
        if(!typeNodeHash.containsKey(node.simpleNameClass))
            typeNodeHash.put(node.simpleNameClass, new ArrayList<>());
        typeNodeHash.get(node.simpleNameClass).add(fNode);
        if(!node.nameFromParent.equals("")) {
            if(!typeNodeHash.containsKey(node.fullName))
                typeNodeHash.put(node.fullName, new ArrayList<>());
            typeNodeHash.get(node.fullName).add(fNode);
        }
        if(fNode.getNode().node != null) {
            Class subclass = fNode.getNode().node.getClass();
            allTypes.add(subclass);
            while ((subclass = subclass.getSuperclass()) != null) {
                allTypes.add(subclass);
            }
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
        if(node.nameFromParent != "")
            typeHash.put(node.fullName, fNode.isEnabled() ? 1:0);
        typeHash.put(node.simpleNameClass, fNode.isEnabled() ? 1:0);
    }

    /**
     * Traveses the tree, and after the traversal is done it will add the real nodes references
     * @param node
     * @param parent
     * @param cluster
     * @param firstTime
     */
    private void traversTree(Node node, GenericTreeNode parent, TreeCluster cluster, boolean firstTime){
        ArrayList<NodeReference> futureReferences = new ArrayList<>();
        traversTree(node, parent, cluster, firstTime, filterConfig.getInt(Config.NTA_DEPTH), futureReferences);

        // Add reference edges that is defined in the filter language
        for (NodeReference ref : futureReferences){
            ArrayList<GenericTreeNode> nodeRefs = new ArrayList<>();
            GenericTreeNode to;
            if(!ref.getReferenceFrom().isNode())
                continue;
            for (Object obj : ref.getFutureReferences()){
                if(isTreeNode(obj)){
                    to = getTreeNode(obj);
                    nodeRefs.add(to);
                    to.addInWardNodeReference(ref);
                }
            }
            ref.setReferences(nodeRefs);
        }
        displayedReferences = futureReferences;
    }

    private void traversTree(Node node, GenericTreeNode parent, TreeCluster cluster, boolean firstTime, int depth, ArrayList<NodeReference> futureReferences){
        if(node == null) {
            return;
        }
        GenericTreeNode addToParent = null;
        TreeNode fNode = new TreeNode(node, parent, filterConfig);
        fNode.setStyles(filterConfig);
        treeNodes.put(node.node, fNode);
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

        HashSet<String> displayedAttributes = filterConfig.getDisplayedAttributes(node);

        // Create the nta nodes specified by the Configuration language, and traverse down THE NTA:s
        if(depth > 0) {
            for (String s : displayedAttributes) {
                if (!node.NTAChildren.containsKey(s))
                    continue;
                Node ntaNode = node.NTAChildren.get(s);
                if (ntaNode == null) {
                    ntaNode = new Node(node.getNodeContent().computeMethod(s, true).getValue(), true, this);
                    node.NTAChildren.put(s, ntaNode);
                    ASTNTAObjects.add(ntaNode.node);
                }
                traversTree(ntaNode, fNode, tmpCluster, firstTime, depth - 1, futureReferences);
            }
        }

        // travers down the tree
        for(Node child : node.children){
            traversTree(child, fNode, tmpCluster, firstTime, depth  ,futureReferences);
        }

        // travers down the tree for the Computed NTA:s
        if(computedNTAs.containsKey(node) && filterConfig.hasConfig(Config.NTA_SHOW_COMPUTED)){
            for(Node child : computedNTAs.get(node)) {
                traversTree(child, fNode, tmpCluster, firstTime, 0, futureReferences);
            }
        }

        fNode.setClusterReference(tmpCluster);
        clusterClusters(fNode);

        if(addToParent != null)
            parent.addChild(addToParent);

        fNode.setDisplayedAttributes(futureReferences, displayedAttributes, this);
    }

    /**
     * Put child clusters together in a parent cluster if they have no children in the filtered tree
     *
     * @param fNode
     */
    private void clusterClusters(TreeNode fNode){
        if(fNode.isNode()) {
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

    /**
     * Write the new filter text to file and generate a new filtered AST
     * @param text
     * @return true if the filter was saved to file and a new filtered AST was successfully created, otherwise false
     */
    public boolean saveNewFilter(String text){
        boolean res = filterConfig.saveAndUpdateConfig(text);
        if (res) {
            for(Collection<Node> list : computedNTAs.values()){
                list.forEach(treeNodes::remove);
            }
            ASTNTAObjects.forEach(treeNodes::remove);
            clearDisplayedReferences();
            filteredTree = null;
            traversTree(this.tree, null, null, false);
        }
        return res;
    }

    /**
     * Reevaluates the API:s filtered tree
     */
    public void buildFilteredTree(){
        clearDisplayedReferences();
        filteredTree = null;
        traversTree(this.tree, null, null, false);
    }

    public Config getfilterConfig(){ return filterConfig; }
    public GenericTreeNode getFilteredTree() { return filteredTree; }

    public HashMap<String, Integer> getTypeHash(){ return typeHash; }

    public GenericTreeNode getTreeNode(Object node){ return treeNodes.get(node); }
    public boolean isTreeNode(Object node){ return treeNodes.containsKey(node); }

    public boolean isASTObject(Object node){
        return ASTObjects.contains(node) || ASTNTAObjects.contains(node) || (filterConfig.hasConfig(Config.NTA_SHOW_COMPUTED) && computedNTAs.containsKey(node));
    }

    public boolean addASTObject(Object node, boolean nta){
        return nta ? ASTNTAObjects.add(node) : ASTObjects.add(node);
    }

    /**
     * Returns the Size of the graph, ie the number of Nodes in the AST.
     */
    public int getASTSize(){ return ASTObjects.size() + ASTNTAObjects.size() + computedNTAs.size(); }

    public void clearDisplayedReferences(){ displayedReferences.clear(); }
    public ArrayList<NodeReference> getDisplayedReferences(){ return displayedReferences; }

    public ArrayList<GenericTreeNode> getNodeReferencesAndHighlightThem(Object value, boolean highlight){
        ArrayList<GenericTreeNode> nodes = new ArrayList<>();
        for(Object o : getNodeReferences(value)){
            if(isTreeNode(o))
                nodes.add(getTreeNode(o).setReferenceHighlight(highlight));
        }
        return nodes;
    }

    public ArrayList<Object> getNodeReferences(Object value){
        ArrayList<Object> nodes = new ArrayList<>();
        if(value == null)
            return nodes;

        if(value instanceof Collection<?>) {
            for (Object n : (Iterable<Object>) value) {
                if (isASTObject(n))
                    nodes.add(n);
            }
        }else if (isASTObject(value))
            nodes.add(value);
        return nodes;
    }

    public NodeInfo computeMethod(Node node, String method) { return node.getNodeContent().computeMethod(method); }

    public ArrayList<String> compute(Node node, boolean force) { return node.getNodeContent().compute(false, force); }

    public ArrayList<String> compute(Node node) { return node.getNodeContent().compute(false, false); }

    /**
     * Computes the method for the NodeInfo,
     * @param node
     * @param info
     * @return
     */
    public Object compute(Node node, NodeInfo info) { return compute(node, info, null); }

    /**
     * Computes the method for the NodeInfo.
     * If a NTA is found it will be added to the low-level api, the represented AST, and to the filtered tree.
     * @param node
     * @param info
     * @return
     */
    public Object compute(Node node, NodeInfo info, Object[]  params) {
        if (info == null)
            return null;
        Object obj = node.getNodeContent().compute(info, params, this);
        if(!info.isNTA() ||  obj == null || ASTNTAObjects.contains(obj))//Todo might want to add null nodes
            return obj;
        Node astNode = new Node(obj, true, this);
        if(!computedNTAs.containsKey(node))
            computedNTAs.put(node, new HashSet<>());
        computedNTAs.get(node).add(astNode);
        node.NTAChildren.put(NodeInfo.getName(info.getMethod(), params), astNode);
        if(filterConfig.hasConfig(Config.NTA_SHOW_COMPUTED)) {
            treeNodes.put(obj, new TreeNode(astNode, getTreeNode(node), filterConfig));
        }
        return obj;
    }


}
