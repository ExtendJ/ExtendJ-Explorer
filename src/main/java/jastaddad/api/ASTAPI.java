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

    public static final String VERSION = "alphabuild-0.2.3";

    private Node tree;
    private GenericTreeNode filteredTree;
    private Config filterConfig;
    private HashSet<Class> allTypes;
    private HashMap<String, LinkedHashSet<Class>> inheritedTypes;
    private HashMap<Class, HashSet<Class>> parentChain;
    private HashMap<Object, GenericTreeNode> treeNodes;
    private HashMap<Node, HashSet<Node>> computedNTAs; //This might be a temporary solution,
    private HashSet<Object> ASTObjects;
    private HashSet<Object> ASTNTAObjects;
    private HashMap<String, ArrayList<AlertMessage>> errors;
    private HashMap<String, ArrayList<AlertMessage>> warnings;
    private ArrayList<NodeReference> displayedReferences;
    private String directoryPath;

    public ASTAPI(Object root, String filterDir){
        directoryPath = filterDir;
        displayedReferences = new ArrayList<>();
        treeNodes = new HashMap<>();
        allTypes = new HashSet<>();
        inheritedTypes = new HashMap<>();
        parentChain = new HashMap<>();
        errors = new HashMap<>();
        warnings = new HashMap<>();
        computedNTAs = new HashMap<>();
        ASTObjects = new HashSet<>();
        ASTNTAObjects = new HashSet<>();

        tree = new Node(root, this);
        this.filteredTree = null;
        filterConfig = new Config(this, filterDir);
        traversTree(this.tree, true);
    }

    public String getFilterFilePath(){return directoryPath + "filter.cfg"; }
    public String getDirectoryPath(){return directoryPath;}

    public boolean containsError(String type){ return errors.get(type).size() > 0; }

    public ArrayList<AlertMessage> getErrors(String type){ return getMessageLine(errors, type); }

    public ArrayList<AlertMessage> getWarnings(String type){ return getMessageLine(warnings, type); }

    public void putWarning(String type, String message){ putMessageLine(warnings, type, message); }

    public void putError(String type, String message){ putMessageLine(errors, type, message); }

    /**
     * Return all messages with the give type.
     * The messages will be removed and can not be fetched again through this method.
     * @param map
     * @param type
     * @return
     */
    private ArrayList<AlertMessage> getMessageLine(HashMap<String, ArrayList<AlertMessage>> map, String type){
        if(!map.containsKey(type)) {
            map.put(type, new ArrayList<>());
            return map.get(type);
        }
        ArrayList<AlertMessage> ret = map.get(type);
        map.put(type, new ArrayList<>());
        return ret;
    }

    private void putMessageLine(HashMap<String, ArrayList<AlertMessage>> map, String type, String message){
        if(!map.containsKey(type))
            map.put(type, new ArrayList<>());
        map.get(type).add(new AlertMessage(type,message));
    }

    /**
     * Saves all types and their class inheritance chain
     *
     * @param node
     */
    private void addTypeInheritance(Node node){
        // Add the type and it superclasses
        if(node.isNull() || inheritedTypes.containsKey(node.getSimpleNameClass()))
            return;
        LinkedHashSet<Class> set = new LinkedHashSet<>();
        Class c = node.node.getClass();
        while(c != null){
            set.add(c);
            allTypes.add(c);
            c = c.getSuperclass();
        }
        inheritedTypes.put(node.getSimpleNameClass(), set);
    }

    /**
     * Saves the direct parent of all nodes
     *
     * @param node
     */
    private void setParentChain(Node node){
        if(node.isNull() || node.parent == null) //Null node or Root node
            return;
        Class c = node.node.getClass();
        if(!parentChain.containsKey(c))
            parentChain.put(c, new HashSet<>());
        parentChain.get(c).add(node.parent.node.getClass());
    }

    /**
     * Traveses the tree, and after the traversal is done it will add the real nodes references
     * @param node
     * @param firstTime
     */
    private void traversTree(Node node, boolean firstTime){ //Todo christmas work, add the optimization you have been thinking of (Joel). Also reminder that you should fix so that null nodes are always present
        ArrayList<NodeReference> futureReferences = new ArrayList<>();
        traversTree(node, null, null, firstTime, filterConfig.getInt(Config.NTA_DEPTH), futureReferences);

        // Add reference edges that is defined in the filter language
        addReferences(futureReferences, false);

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

        if(node.isNull()){
            fNode.setEnabled(true);
            if(parent != null)

                parent.addChild(fNode);
            else
                filteredTree = fNode;
            return;
        }

        if(firstTime) {
            addTypeInheritance(node);
            setParentChain(node);
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

        if(tmpCluster == null) //Only add NTAs if the node is not a Cluster
            addNTAs(node, parent, tmpCluster, firstTime, depth, futureReferences,displayedAttributes);

        fNode.setClusterReference(tmpCluster);

        if(tmpCluster != null)
            tmpCluster.addToTypeList(fNode);
        clusterClusters(fNode);

        // travers down the tree
        for(Node child : node.children){
            traversTree(child, fNode, tmpCluster, firstTime, depth  ,futureReferences);
        }

        if(addToParent != null)
            parent.addChild(addToParent);

        fNode.setDisplayedAttributes(futureReferences, displayedAttributes, this);
    }

    /**
     * Adds the NTA that should be visible to the filtered tree
     */
    private void addNTAs(Node node, GenericTreeNode parent, TreeCluster cluster, boolean firstTime,
                         int depth, ArrayList<NodeReference> futureReferences, HashSet<String> displayedAttributes){
        // Create the nta nodes specified by the Configuration language, and traverse down THE NTA:s
        if(depth > 0) {
            for (String s : displayedAttributes) {
                if (!node.NTAChildren.containsKey(s))
                    continue;
                Node ntaNode = node.NTAChildren.get(s);
                if (ntaNode == null) {
                    ntaNode = new Node(node.getNodeContent().computeMethod(this, s, true).getValue(),node, true, this);
                    node.NTAChildren.put(s, ntaNode);
                    ASTNTAObjects.add(ntaNode.node);
                }
                traversTree(ntaNode, parent, cluster, firstTime, depth - 1, futureReferences);
            }
        }

        // travers down the tree for the Computed NTA:s
        if(computedNTAs.containsKey(node) && filterConfig.getBoolean(Config.NTA_COMPUTED)){
            for(Node child : computedNTAs.get(node)) {
                if(!treeNodes.containsKey(child.node))
                    traversTree(child, parent, cluster, firstTime, 0, futureReferences);
            }
        }
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
     * Adds the references to the mainList.
     * Which will be handled by an outside party to display.
     * This needs to be separated from the main structure
     * @param futureReferences
     * @param appendList
     */
    private void addReferences(ArrayList<NodeReference> futureReferences, boolean appendList){
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
        if(!appendList)
            displayedReferences = futureReferences;
        else
            displayedReferences.addAll(futureReferences);
    }
    /**
     * Write the new filter text to file and generate a new filtered AST
     * @param text
     * @return true if the filter was saved to file and a new filtered AST was successfully created, otherwise false
     */
    public boolean saveNewFilter(String text){
        boolean res = filterConfig.saveAndUpdateConfig(text);
        if (res) {
            ASTNTAObjects.forEach(treeNodes::remove);
            clearDisplayedReferences();
            filteredTree = null;
            traversTree(this.tree, false);
        }
        return res;
    }

    /**
     * Reevaluates the API:s filtered tree
     */
    public void buildFilteredSubTree(Node node, TreeNode parent){
        ArrayList<NodeReference> futureReferences = new ArrayList<>();
        parent.setDisplayedAttributes(futureReferences, filterConfig.getDisplayedAttributes(node), this);
        traversTree(node, parent, null, true, 0, displayedReferences);
        addReferences(futureReferences, true);
    }

    public Config getfilterConfig(){ return filterConfig; }
    public GenericTreeNode getFilteredTree() { return filteredTree; }

    public LinkedHashSet<Class> getInheritanceChain(String simpleClassName){ return inheritedTypes.get(simpleClassName); }

    public boolean isASTType(String className){ return inheritedTypes.containsKey(className); }
    public boolean isASTType(Class type){ return allTypes.contains(type); }
    public HashMap<Class, HashSet<Class>> getParentChains(){ return parentChain; }

    public GenericTreeNode getTreeNode(Object node){ return treeNodes.get(node); }
    public boolean isTreeNode(Object node){ return treeNodes.containsKey(node); }

    public boolean isASTObject(Object node){
        return ASTObjects.contains(node) || ASTNTAObjects.contains(node);
    }

    public boolean addASTObject(Object node, boolean nta){
        return nta ? ASTNTAObjects.add(node) : ASTObjects.add(node);
    }

    /**
     * Returns the Size of the graph, ie the number of Nodes in the AST.
     */
    public int getASTSize(){ return ASTObjects.size() + ASTNTAObjects.size(); }

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

    public NodeInfo computeMethod(Node node, String method) { return node.getNodeContent().computeMethod(this, method); }

    public void compute(Node node, boolean force) { node.getNodeContent().compute(this, false, force); }

    public void compute(Node node) { node.getNodeContent().compute(this, false, false); }

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
        if(!info.isNTA() || ASTNTAObjects.contains(obj))
            return obj;
        Node astNode = new Node(obj, node, true, this);
        if(!computedNTAs.containsKey(node))
            computedNTAs.put(node, new HashSet<>());
        computedNTAs.get(node).add(astNode);
        node.NTAChildren.put(NodeInfo.getName(info.getMethod(), params), astNode);
        if(filterConfig.getBoolean(Config.NTA_COMPUTED))
            buildFilteredSubTree(astNode, (TreeNode) treeNodes.get(node.node));
        return obj;
    }


}
