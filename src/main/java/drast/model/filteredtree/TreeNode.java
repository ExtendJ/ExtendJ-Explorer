package drast.model.filteredtree;

import configAST.Color;
import configAST.Str;
import configAST.Value;
import drast.model.ASTBrain;
import drast.model.FilterConfig;
import drast.model.Node;

import java.util.*;

/**
 * Class for the "real" nodes in the filtered tree.
 * Created by gda10jth on 10/16/15.
 */
public class TreeNode extends GenericTreeNode {
    private final Node node;
    private String treeViewName;
    private ArrayList<String> labelAttributes;
    private HashSet<NodeReference> outwardReferences;
    private HashMap<NodeReference, NodeReference> inwardReferences;
    private HashSet<NodeReference> allRefs;

    public TreeNode(Node data, GenericTreeNode parent, FilterConfig filterConfig){
        super(parent);
        node = data;
        allRefs = new HashSet<>();
        labelAttributes = new ArrayList<>();
        setExpandable(true);
        setStyles(filterConfig);
    }

    public ArrayList<String> getLabelAttributes(){ return labelAttributes;}
    /**
     * Returns the Object of the AST
     * @return
     */
    public Node getNode(){ return node; }

    @Override
    public void addChild(Node node, GenericTreeNode child){
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
    public String toString(){ return node.toString(); }

    @Override
    public String toGraphString(){ return toString(); }

    @Override
    public String toTreeViewString() { return treeViewName != null ? treeViewName : toString(); }

    /**
     * Set the node specific styles, derived from the config
     * @param filter
     */
    @Override
    public void setStyles(FilterConfig filter) {
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
    @Override
    public void setDisplayedAttributes(ArrayList<NodeReference> allReferences, HashSet<String> set , ASTBrain api){
        if(set.size() == 0)
            return;
        treeViewName = toString() + " : ";
        if(outwardReferences == null)
            outwardReferences = new HashSet<>();
        for (String s : set){
            Object obj = api.computeMethod(node, s);
            if(obj == null)
                continue;
            ArrayList<Object> refs = api.getNodeReferences(obj);
            if(refs != null && refs.size() > 0) {
                NodeReference reference = new NodeReference(s, this, refs);
                outwardReferences.add(reference);
                allReferences.add(reference);
                allRefs.add(reference);
            } else {
                labelAttributes.add(s + " : " + obj);
                treeViewName += s + " : " + obj;
            }
        }
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
