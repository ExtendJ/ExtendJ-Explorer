package drast.model;

import drast.model.analyzer.AnalyzerHolder;
import drast.model.filteredtree.*;
import drast.model.reflection.ReflectionNode;
import drast.model.terminalvalues.TerminalValue;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

/**
 * ASTBrain is the API of the DrAST system. It traverse the AST and generate a filtered AST. After this the AST can be
 * reached from the outside via this class.
 *
 * This file takes the root Object of the JastAdd AST and travers the tree.
 * Each node in the filtered AST is a sub class of the GenericTreeNode.
 */
public class ASTBrain extends Observable{

    private long reflectedTreeTime;
    private long filteredTreeTime;
    private Node tree;
    private GenericTreeNode filteredTree;
    private FilterConfig filterConfig;
    private AnalyzerHolder analyzer;
    private Config config;
    private HashSet<Class> allTypes;
    private HashMap<String, LinkedHashSet<Class>> inheritedTypes;
    private HashMap<Class, HashSet<Class>> directParents;
    private HashMap<Class, HashSet<Class>> directChildren;
    private HashMap<Object, GenericTreeNode> treeNodes;
    private HashMap<Node, HashSet<Node>> computedNTAs; //This might be a temporary solution,

    private HashSet<Object> ASTObjects; //Flat structure of the AST
    private HashSet<Object> ASTNTAObjects;  //Flat structure of the TNA AST

    private ArrayList<NodeReference> displayedReferences;
    private HashMap<Integer, Boolean> typeErrorTracker;
    private String directoryPath;

    private int normalNodes = 0;

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
        computedNTAs = new HashMap<>();
        ASTObjects = new HashSet<>();
        ASTNTAObjects = new HashSet<>();
        typeErrorTracker = new HashMap<>();
        analyzer = new AnalyzerHolder();

        String tmp = new File(".").getAbsolutePath();
        config = new Config(tmp.substring(0,tmp.length()-1));

        if(isAPIHolder) //No root node, will stop here
            return;

        // new Node will recreate the AST and be the low level data structure of this program.
        long time = System.currentTimeMillis();
        tree = ReflectionNode.getReflectedTree(root, this, listRoot);
        reflectedTreeTime = System.currentTimeMillis() - time;
        this.filteredTree = null;
        analyzer.executeRTAnalyzers(tree);

        // Here we use our recreated tree to build our filtered tree
        filterConfig = new FilterConfig(this, filterDir);
        createFilteredTree(this.tree, true);
        analyzer.executeFTAnalyzers(filteredTree);

    }

    public AnalyzerHolder getAnalyzer(){ return analyzer; }

    public Node getRoot(){return tree; }

    public boolean hasRoot(){ return tree != null && !tree.isNullNode(); }

    public Class getRootClass(){return tree.getASTClass(); }

    public long getReflectedTreeTime(){ return reflectedTreeTime; }

    public long getFilteredTreeTime(){ return filteredTreeTime; }

    public String getFilterFilePath(){return directoryPath; }

    public String getDirectoryPath(){return directoryPath;}

    public boolean containsError(int type){ return typeErrorTracker.containsKey(type) && typeErrorTracker.get(type); }

    public void putMessage(int type, String message){
        AlertMessage newMessage = new AlertMessage(type, message);
        if(newMessage.isError()){
            typeErrorTracker.put(type, true);
        }
        setChanged();
        notifyObservers(newMessage);
    }

    public int getClusteredASTSize(){ return normalNodes; }

    public int getASTSize(){ return ASTObjects.size() + ASTNTAObjects.size(); }

    public String getAppliedFilters(){
        return filterConfig != null ? filterConfig.getEnabledFilterNames() : null;
    }

    /**
     * Saves all types and their class inheritance chain
     *
     * @param node
     */
    private void addTypeInheritance(Node node){
        // Add the type and it superclasses
        if(node.isNullNode() || inheritedTypes.containsKey(node.getSimpleClassName()))
            return;
        LinkedHashSet<Class> set = new LinkedHashSet<>();
        Class c = node.getASTClass();
        while(c != null){
            set.add(c);
            allTypes.add(c);
            c = c.getSuperclass();
        }
        inheritedTypes.put(node.getSimpleClassName(), set);
    }

    /**
     * Saves the direct parent of all nodes
     *
     * @param node
     */
    private void setDirectParentsAndChildren(Node node){
        if(node.isNullNode()) //Null node or Root node
            return;
        Class c = node.getASTClass();
        if(!directParents.containsKey(c))
            directParents.put(c, new HashSet<>());
        if(node.getParent() != null)
            directParents.get(c).add(node.getParent().getASTClass());
        if(!directChildren.containsKey(c))
            directChildren.put(c, new HashSet<>());
        for(Node child : node.getChildren()){
            if(!child.isNullNode())
                directChildren.get(c).add(child.getASTClass());
        }
    }

    /**
     * Traveses the tree, and after the traversal is done it will add the real nodes references
     * @param node
     * @param firstTime
     */
    private void createFilteredTree(Node node, boolean firstTime){
        normalNodes = 0;
        long time = System.currentTimeMillis();
        ArrayList<NodeReference> futureReferences = new ArrayList<>();
        createFilteredTree(node, null, false ,firstTime, config.getInt(Config.NTA_DEPTH), futureReferences);
        addReferences(futureReferences, false); // Add reference edges that is defined in the filter language
        filteredTreeTime = System.currentTimeMillis() - time;
    }

    private GenericTreeNode getNode(Node node, GenericTreeNode parent, boolean collapse) {
        if (node == null)
            return null;

        if (collapse && parent != null && parent.isCluster())
            return parent;
        else if (collapse)
            return new TreeCluster(node, parent, filterConfig);

        boolean enabled = filterConfig.isEnabled(node);
        if (enabled) {
            normalNodes++;
            return new TreeNode(node, parent, filterConfig);
        } else if (node.isNullNode())
            return new TreeNode(node, parent, filterConfig);
        else if(parent != null && parent.isCluster())
            return parent;
        else
            return new TreeCluster(node, parent, filterConfig);
    }

    private void createFilteredTree(Node node, GenericTreeNode parent, boolean collapseTree, boolean firstTime, int depth, ArrayList<NodeReference> futureReferences){
        if(node == null)
            return;
        boolean collapse = collapseTree;

        GenericTreeNode gNode = getNode(node, parent, collapse);

        if (!collapse)
            collapse = !filterConfig.hasSubTree(node);

        treeNodes.put(node.getASTObject(), gNode);

        if(parent != null)
            parent.addChild(node, gNode);
        else
            filteredTree = gNode;

        if(gNode.isNullNode())
            return;

        if(firstTime) {
            addTypeInheritance(node);
            setDirectParentsAndChildren(node);
        }

        HashSet<String> displayedAttributes = filterConfig.getDisplayedAttributes(node);

        addNTAs(node, gNode, collapse, depth, futureReferences, displayedAttributes);

        if(!collapse)
            gNode.setDisplayedAttributes(futureReferences, displayedAttributes, this);

        for(Node child : node.getChildren())
            createFilteredTree(child, gNode, collapse, firstTime, depth, futureReferences);

        clusterClusters(gNode);
    }

    /**
     * Put child clusters together in a parent cluster if they have no children in the filtered tree
     * @param gNode
     */
    private void clusterClusters(GenericTreeNode gNode){
        if(gNode.isNode()) {
            TreeClusterParent clusterParent = new TreeClusterParent(gNode);
            clusterParent.setStyles(filterConfig);
            // get all children cluster children that have no children
            for (GenericTreeNode fChild : gNode.getChildren()) {
                if (fChild.isCluster() && fChild.getChildren().size() == 0) {
                    clusterParent.addChild(null, fChild);
                }
            }
            if(clusterParent.getClusters().size() > 0) {
                for(GenericTreeNode cChild : clusterParent.getClusters()) {
                    gNode.getChildren().remove(cChild);
                }
                gNode.addChild(gNode.getNode(), clusterParent);
            }
        }
    }

    /**
     * Adds the NTA that should be visible to the filtered tree
     */
   private void addNTAs(Node node, GenericTreeNode parent, boolean collapse, int depth, ArrayList<NodeReference> futureReferences, HashSet<String> displayedAttributes){
        // Create the nta nodes specified by the Configuration language, and traverse down the NTA:s
        if(depth > 0) {
            for (String s : displayedAttributes) {
                if (!node.containsNTAMethod(s))
                    continue;
                Node ntaNode = node.getNTATree(s, this);
                createFilteredTree(ntaNode, parent, collapse, true, depth - 1, futureReferences);
            }
        }

        if(config.isEnabled(Config.NTA_CACHED)) {
            node.getNodeData().setCachedNTAs(this);
            for (Node child : node.getNTAChildren().values()) {
                if (child == null)
                    continue;
                if (!ASTNTAObjects.contains(child.getASTObject()))
                    ASTNTAObjects.add(child.getASTObject());
                createFilteredTree(child, parent, collapse, true, 0, futureReferences);
            }
        }
       // travers down the tree for the Computed NTA:s
       if(computedNTAs.containsKey(node) && config.isEnabled(Config.NTA_COMPUTED)){
           for(Node child : computedNTAs.get(node)) {
               if(!treeNodes.containsKey(child.getASTObject())) {
                   createFilteredTree(child, parent, collapse, true, 0, futureReferences);
               }
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
     * Reapplies the filter from file.
     * @return true if the filter was saved to file and a new filtered AST was successfully created, otherwise false
     */
    public boolean reApplyFilter(){
        boolean res = filterConfig.readFilter(filterConfig.getFilterFileName());
        return applyFilter(res);
    }

    /**
     * Write the new filter text to file and generate a new filtered AST
     * @param text
     * @return true if the filter was saved to file and a new filtered AST was successfully created, otherwise false
     */
    public boolean saveNewFilter(String text){
        boolean res = filterConfig.saveAndUpdateConfig(text);
        return applyFilter(res);
    }

    private boolean applyFilter(boolean res){
        if(res) {
            clearDisplayedReferences();
            for (Object n : ASTNTAObjects)
                treeNodes.put(n, null);
            filteredTree = null;
            createFilteredTree(this.tree, false);
        }
        return res;
    }

    /**
     * Reevaluates the API:s filtered tree
     */
    public void buildFilteredSubTree(Node node, TreeNode parent){
        ArrayList<NodeReference> futureReferences = new ArrayList<>();
        parent.setDisplayedAttributes(futureReferences, filterConfig.getDisplayedAttributes(node), this);
        createFilteredTree(node, parent, false, true, 0, displayedReferences);
        addReferences(futureReferences, true);
    }

    public Config getConfig(){ return config; }
    public FilterConfig getFilterConfig(){ return filterConfig; }
    public GenericTreeNode getFilteredTree() { return filteredTree; }

    public LinkedHashSet<Class> getInheritanceChain(String simpleClassName){ return inheritedTypes.get(simpleClassName); }

    public boolean isASTType(Class type){ return allTypes.contains(type); }

    public HashSet<Class> getAllASTTypes(){ return allTypes; }
    public HashMap<Class, HashSet<Class>> getDirectParents(){ return directParents; }
    public HashMap<Class, HashSet<Class>> getDirectChildren(){ return directChildren; }

    public GenericTreeNode getTreeNode(Node node){ return treeNodes.get(node.getASTObject()); }
    public GenericTreeNode getTreeNode(Object node){ return treeNodes.get(node); }
    public boolean isTreeNode(Object node){ return treeNodes.containsKey(node); }

    public boolean isASTObject(Object node){ return ASTObjects.contains(node) || ASTNTAObjects.contains(node); }
    public boolean addASTObject(Object node, boolean nta){ return nta ? ASTNTAObjects.add(node) : ASTObjects.add(node); }

    public void clearDisplayedReferences(){ displayedReferences.clear(); }
    public ArrayList<NodeReference> getDisplayedReferences(){ return displayedReferences; }

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

    public Method getMethod(Node node, String method) { return node.getNodeData().getMethod(method); }

    public Object computeMethod(Node node, String method) { return node.getNodeData().computeMethod(method); }

    public void compute(Node node) { node.getNodeData().compute(this); }

    public Object compute(Node node, TerminalValue info) { return compute(node, info, null); }

    /**
     * Computes the method for the NodeInfo.
     * If a NTA is found it will be added to the reflected and the filtered tree.
     * @param node
     * @param info
     * @return
     */
    public Object compute(Node node, TerminalValue info, Object[]  params) {
        if (info == null)
            return null;
        Object obj = node.getNodeData().compute(info, params, this);
        if(!info.isNTA() || ASTNTAObjects.contains(obj) || containsError(AlertMessage.INVOCATION_ERROR))
            return obj;

        Node astNode = node.getNTATree(obj, node, this);
        if(!computedNTAs.containsKey(node))
            computedNTAs.put(node, new HashSet<>());
        computedNTAs.get(node).add(astNode);
        node.putNTA(TerminalValue.getName(info.getMethod(), params), astNode);
        GenericTreeNode parent = treeNodes.get(node.getASTObject());
        if(config.isEnabled(Config.NTA_COMPUTED) && parent.isNode())
            buildFilteredSubTree(astNode, (TreeNode) parent);
        else {
            String message = String.format("Computed NTA successfully, but the configuration %s is not enabled, so the NTA will not be shown. See the DrAST.cfg file.", Config.NTA_COMPUTED);
            putMessage(AlertMessage.INVOCATION_WARNING, message);
        }
        return obj;
    }
}
