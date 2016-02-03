package drast.model;

import drast.model.filteredtree.*;
import drast.model.nodeinfo.NodeInfo;
import javafx.util.Pair;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * ASTBrain is the API of the DrAST system. It traverse the AST and generate a filtered AST. After this the AST can be
 * reached from the outside via this class.
 *
 * This file takes the root Object of the JastAdd AST and travers the tree.
 * Each node in the filtered AST is a sub class of the GenericTreeNode.
 */
public class ASTBrain {

    public static final String VERSION = "alphabuild-0.3.1";

    private HashMap<Class, ArrayList<Pair<Method, Annotation>>> methods;
    private HashMap<Class, ArrayList<Method>> NTAMethods;
    private HashMap<Method, Field> methodCacheField;

    private Node tree;
    private GenericTreeNode filteredTree;
    private Config filterConfig;
    private HashSet<Class> allTypes;
    private HashMap<String, LinkedHashSet<Class>> inheritedTypes;
    private HashMap<Class, HashSet<Class>> directParents;
    private HashMap<Class, HashSet<Class>> directChildren;
    private HashMap<Object, GenericTreeNode> treeNodes;
    private HashMap<Node, HashSet<Node>> computedNTAs; //This might be a temporary solution,
    private HashSet<Object> ASTObjects;
    private HashSet<Object> ASTNTAObjects;
    private HashMap<String, ArrayList<AlertMessage>> errors;
    private HashMap<String, ArrayList<AlertMessage>> warnings;
    private HashMap<String, ArrayList<AlertMessage>> messages;
    private ArrayList<NodeReference> displayedReferences;
    private String directoryPath;

    private int normalNodes = 0;
    private int clusterNodes = 0;

    public ASTBrain(Object root, String filterDir){ initialize(root, filterDir, false, false); }
    public ASTBrain(Object root, String filterDir, boolean listRoot){ initialize(root, filterDir, listRoot, false); }
    public ASTBrain(){ initialize(null, null, false, true); }

    public void initialize(Object root, String filterDir, boolean listRoot, boolean isAPIHolder){
        directoryPath = filterDir;
        displayedReferences = new ArrayList<>();
        treeNodes = new HashMap<>();
        allTypes = new HashSet<>();
        inheritedTypes = new HashMap<>();
        directParents = new HashMap<>();
        directChildren = new HashMap<>();
        errors = new HashMap<>();
        warnings = new HashMap<>();
        messages = new HashMap<>();
        computedNTAs = new HashMap<>();
        ASTObjects = new HashSet<>();
        ASTNTAObjects = new HashSet<>();
        if(isAPIHolder)
            return;
        NTAMethods = new HashMap<>();
        methods = new HashMap<>();
        methodCacheField = new HashMap<>();
        tree = new Node(root, this, listRoot);
        this.filteredTree = null;
        filterConfig = new Config(this, filterDir);
        traversTree(this.tree, true);
    }

    protected ArrayList<Pair<Method, Annotation>> getMethods(Class clazz){ return methods.get(clazz); }
    protected void putMethods(Class clazz, ArrayList<Pair<Method, Annotation>> methods){ this.methods.put(clazz, methods); }

    protected ArrayList<Method> getNTAMethods(Class clazz){ return NTAMethods.get(clazz); }
    protected void putNTAMethods(Class clazz, ArrayList<Method> methods){ this.NTAMethods.put(clazz, methods); }

    protected Field getCachedField(Method method){ return methodCacheField.get(method); }
    protected void putCachedField(Method method, Field field){ this.methodCacheField.put(method, field); }

    public Node getRoot(){return tree; }

    public String getFilterFilePath(){return directoryPath; }
    public String getDirectoryPath(){return directoryPath;}

    public boolean containsError(String type){ return errors.containsKey(type) && errors.get(type).size() > 0; }

    public ArrayList<AlertMessage> getMessages(String type){ return getMessageLine(messages, type); }

    public ArrayList<AlertMessage> getWarnings(String type){ return getMessageLine(warnings, type); }

    public ArrayList<AlertMessage> getErrors(String type){ return getMessageLine(errors, type); }

    public void putMessage(String type, String message){ putMessageLine(messages, type, message); }

    public void putWarning(String type, String message){ putMessageLine(warnings, type, message); }

    public void putError(String type, String message){ putMessageLine(errors, type, message); }

    public int getClusteredASTSize(){ return normalNodes; }

    public int getASTSize(){ return normalNodes + clusterNodes; }

    public String getAppliedFilters(){
        return filterConfig != null ? filterConfig.getEnabledFilterNames() : null;
    }
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
    private void setDirectParentsAndChildren(Node node){
        if(node.isNull()) //Null node or Root node
            return;
        Class c = node.node.getClass();
        if(!directParents.containsKey(c))
            directParents.put(c, new HashSet<>());
        if(node.parent != null)
            directParents.get(c).add(node.parent.node.getClass());
        if(!directChildren.containsKey(c))
            directChildren.put(c, new HashSet<>());
        for(Node child : node.children){
            if(!child.isNull())
                directChildren.get(c).add(child.node.getClass());
        }
    }

    /**
     * Traveses the tree, and after the traversal is done it will add the real nodes references
     * @param node
     * @param firstTime
     */
    private void traversTree(Node node, boolean firstTime){
        normalNodes = 0;
        clusterNodes = 0;
        ArrayList<NodeReference> futureReferences = new ArrayList<>();
        long time = System.currentTimeMillis();
        traversTree(node, null, null, firstTime, filterConfig.getInt(Config.NTA_DEPTH), futureReferences);
        // Add reference edges that is defined in the filter language
        addReferences(futureReferences, false);
        System.out.println("Time for AST filter : " + (System.currentTimeMillis() - time));
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
            setDirectParentsAndChildren(node);
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

       // if(tmpCluster == null) //Only add NTAs if the node is not a Cluster
        addNTAs(node, fNode, tmpCluster, depth, futureReferences, displayedAttributes);

        fNode.setClusterReference(tmpCluster);

        if(tmpCluster != null)
            tmpCluster.addToTypeList(fNode);

        // travers down the tree
        for(Node child : node.children)
            traversTree(child, fNode, tmpCluster, firstTime, depth,futureReferences);

        if(tmpCluster == null)
            normalNodes++;
        else
            clusterNodes++;


        clusterClusters(fNode);

        if(addToParent != null){
            parent.addChild(addToParent);
        }

        fNode.setDisplayedAttributes(futureReferences, displayedAttributes, this);
    }

    /**
     * Adds the NTA that should be visible to the filtered tree
     */
    private void addNTAs(Node node, GenericTreeNode parent, TreeCluster cluster, int depth, ArrayList<NodeReference> futureReferences, HashSet<String> displayedAttributes){
        // Create the nta nodes specified by the Configuration language, and traverse down the NTA:s
        if(depth > 0) {
            for (String s : displayedAttributes) {
                if (!node.showNTAChildren.containsKey(s))
                    continue;
                Node ntaNode = node.showNTAChildren.get(s);
                if (ntaNode == null) {
                    ntaNode = Node.getNTANode(node.getNodeData().computeMethod(this, s, true).getValue(),node, this);
                    node.showNTAChildren.put(s, ntaNode);
                    ASTNTAObjects.add(ntaNode.node);
                }
                traversTree(ntaNode, parent, cluster,  true, depth - 1, futureReferences);
            }
        }

        // travers down the tree for the Computed NTA:s
        if(computedNTAs.containsKey(node) && filterConfig.getBoolean(Config.NTA_COMPUTED)){
            for(Node child : computedNTAs.get(node)) {
                if(!treeNodes.containsKey(child.node)) {
                    traversTree(child, parent, cluster, true, 0, futureReferences);
                }
            }
        }
        if(filterConfig.getBoolean(Config.CACHED_VALUES)){
            for(Node child : node.getNodeData().findCachedNTAs(this)) {
                if(child == null || treeNodes.containsKey(child.node))
                    continue;
                if(!ASTNTAObjects.contains(child.node))
                    ASTNTAObjects.add(child.node);
                traversTree(child, parent, cluster, true, 0, futureReferences);
            }
        }

    }


    /**
     * Put child clusters together in a parent cluster if they have no children in the filtered tree
     * @param fNode
     */
    private void clusterClusters(TreeNode fNode){
        if(fNode.isNode()) {
            TreeClusterParent clusterParent = new TreeClusterParent(fNode);
            clusterParent.setStyles(filterConfig);
            // get all children cluster children that have no children
            for (GenericTreeNode fChild : fNode.getChildren()) {
                if (fChild.isCluster() && fChild.getChildren().size() == 0)
                    clusterParent.addCluster((TreeCluster)fChild);
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

    public HashSet<Class> getAllASTTypes(){ return allTypes; }
    public HashMap<Class, HashSet<Class>> getDirectParents(){ return directParents; }
    public HashMap<Class, HashSet<Class>> getDirectChildren(){ return directChildren; }

    public GenericTreeNode getTreeNode(Object node){ return treeNodes.get(node); }
    public boolean isTreeNode(Object node){ return treeNodes.containsKey(node); }

    public boolean isASTObject(Object node){
        return ASTObjects.contains(node) || ASTNTAObjects.contains(node);
    }
    public boolean addASTObject(Object node, boolean nta){ return nta ? ASTNTAObjects.add(node) : ASTObjects.add(node); }

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

    public NodeInfo computeMethod(Node node, String method) { return node.getNodeData().computeMethod(this, method); }

    public void compute(Node node, boolean force) { node.getNodeData().compute(this, false, force); }

    public void compute(Node node) { node.getNodeData().compute(this, false, false); }

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
        Object obj = node.getNodeData().compute(info, params, this);
        if(!info.isNTA() || ASTNTAObjects.contains(obj) || containsError(AlertMessage.INVOCATION_ERROR))
            return obj;
        Node astNode = Node.getNTANode(obj, node, this);
        if(!computedNTAs.containsKey(node))
            computedNTAs.put(node, new HashSet<>());
        computedNTAs.get(node).add(astNode);
        node.showNTAChildren.put(NodeInfo.getName(info.getMethod(), params), astNode);
        if(filterConfig.getBoolean(Config.NTA_COMPUTED))
            buildFilteredSubTree(astNode, (TreeNode) treeNodes.get(node.node));
        else {
            String message = String.format("Computed NTA successfully, but the configuration %s is either not set or off, so the NTA will not be shown.", Config.NTA_COMPUTED);
            putWarning(AlertMessage.INVOCATION_WARNING, message);
        }
        return obj;
    }


}
