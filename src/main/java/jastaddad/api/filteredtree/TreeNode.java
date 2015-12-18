package jastaddad.api.filteredtree;

import configAST.Color;
import configAST.Str;
import configAST.Value;
import jastaddad.api.ASTAPI;
import jastaddad.api.Config;
import jastaddad.api.Node;
import jastaddad.api.nodeinfo.NodeInfo;

import java.util.*;

/**
 * Class for the "real" nodes in the filtered tree.
 * Created by gda10jth on 10/16/15.
 */
public class TreeNode extends GenericTreeNode {
    private final Node node;
    private boolean enabled;
    private String graphName;
    private LinkedHashMap<Integer, Boolean> realChildEdge;
    private HashSet<NodeReference> outwardReferences;
    private HashMap<NodeReference, NodeReference> inwardReferences;
    private HashSet<NodeReference> allRefs;

    public TreeNode(Node data, GenericTreeNode parent, Config filter){
        super(parent);
        node = data;
        realChildEdge = new LinkedHashMap<>();
        enabled = setEnabled(filter);
        allRefs = new HashSet<>();
        setExpandable(true);
    }

    /**
     * Returns the Object of the AST
     * @return
     */
    public Node getNode(){ return node; }
    /**
     * Sets the flag which determine that if the node is filtered of not.
     * @param enabled
     */

    public void setEnabled(boolean enabled){ this.enabled = enabled; }

    /**
     * Check with the filter if the node is filtered.
     * @param filter
     * @return
     */
    private boolean setEnabled(Config filter){
        return filter.isEnabled(node);
    }

    /**
     * Check if the node is filtered
     * @return
     */
    public boolean isEnabled(){ return enabled; }

    /**
     * Add a child node, and set its edge
     * @param child
     */
    @Override
    public void addChild(GenericTreeNode child){
        if(child.isNode()) {
            TreeNode c = (TreeNode)child;
            realChildEdge.put(c.node.id, node.children.contains(c.node));
        }
        children.add(child);
    }

    @Override
    public boolean isNTANode(){ return node.isNTA(); }

    @Override
    public boolean isNullNode(){ return node.isNull(); }

    /**
     * Returns a iterator for the children
     * @return
     */
    public Iterator<GenericTreeNode> iterator(){ return children.iterator(); }

    public boolean isNode(){ return true; }
    public boolean isCluster(){ return false;}
    public boolean isClusterParent(){return false;}

    @Override
    public String toString(){
        return node.toString();
    }

    @Override
    public String toGraphString(){ return graphName != null ? graphName : "<html>" + toString() + "</html>"; }

    /**
     * Set the node specific styles, derived from the config
     * @param filter
     */
    @Override
    public void setStyles(Config filter) {
        if(node.isList() || node.isOpt()) {
            styles.put("node-color", new Color("#dddddd"));
            styles.put("node-shape", new Str("\"rectangle\""));
        }
        else {
            styles.put("node-color", new Color("#ccffff"));
            styles.put("node-shape", new Str("\"rounded_rectangle\""));
        }
        styles.put("border-style", new Str("\"line\""));

        HashMap<String, Value> userStyle = filter.getNodeStyle(node);
        Iterator it = userStyle.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Value> pair = (Map.Entry)it.next();
            styles.put(pair.getKey(), pair.getValue());
        }
    }

    /**
     * This method will derive which attributes that should be displayed, it will also save the reference attributes as NodeReferences.
     * NOTE: The value which the method toGraphString() returns will be manipulated by this method.
     * @param allReferences
     * @param set
     * @param api
     */
    public void setDisplayedAttributes(ArrayList<NodeReference> allReferences, HashSet<String> set , ASTAPI api){
        if(set.size() == 0)
            return;
        graphName = "<html>" + toString();
        if(outwardReferences == null)
            outwardReferences = new HashSet<>();
        for (String s : set){
            NodeInfo info = api.computeMethod(node, s);
            if(info == null)
                continue;
            ArrayList<Object> refs = api.getNodeReferences(info.getValue());
            if(refs != null && refs.size() > 0) {
                NodeReference reference = new NodeReference(s, this, refs);
                outwardReferences.add(reference);
                allReferences.add(reference);
                allRefs.add(reference);
            } else
                graphName += String.format("<br>%s : %s </br>", s, info.getValue());
        }
        graphName += "</html>";
    }

    @Override
    public HashSet<NodeReference> getOutwardNodeReferences(){ return outwardReferences;}

    @Override
    public HashMap<NodeReference, NodeReference> getInwardNodeReferences(){ return inwardReferences;}

    @Override
    public boolean addInWardNodeReference(NodeReference ref){
        if(inwardReferences == null)
            inwardReferences = new HashMap<>();
        if(!inwardReferences.containsKey(ref))
            inwardReferences.put(ref, new NodeReference(ref.getLabel(), ref.getReferenceFrom()));
        allRefs.add(inwardReferences.get(ref));
        return inwardReferences.get(ref).addReference(this);
    }

    @Override
    public HashSet<NodeReference> getAllNodeReferences(){ return allRefs; }

}
