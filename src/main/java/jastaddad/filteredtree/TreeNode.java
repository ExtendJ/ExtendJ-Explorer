package jastaddad.filteredtree;

import configAST.Color;
import configAST.Str;
import configAST.Value;
import jastaddad.ASTAPI;
import jastaddad.Config;
import jastaddad.Node;
import jastaddad.objectinfo.NodeInfo;

import java.util.*;

/**
 * Created by gda10jth on 10/16/15.
 */
public class TreeNode extends GenericTreeNode {
    public final Node node;
    private boolean enabled;
    private String graphName;
    private LinkedHashMap<Integer, Boolean> realChildEdge;

    public TreeNode(Node data, GenericTreeNode parent, Config filter){
        super(parent);
        node = data;
        realChildEdge = new LinkedHashMap<>();
        enabled = setEnabled(filter);
        setExpandable(true);
    }

    public void setEnabled(boolean enabled){ this.enabled = enabled; }

    private boolean setEnabled(Config filter){
        return filter.isEnabled(node);
    }

    public boolean isEnabled(){ return enabled; }

    @Override
    public void addChild(GenericTreeNode child){
        if(child.isNode()) {
            TreeNode c = (TreeNode)child;
            realChildEdge.put(c.node.id, node.children.contains(c.node));
        }
        children.add(child);
    }

    public boolean isRealChild(GenericTreeNode child){
        return !child.isNode() ? false : realChildEdge.get(((TreeNode)child).node.id);
    }

    public Iterator<GenericTreeNode> iterator(){ return children.iterator(); }

    public boolean isNode(){ return true; }
    public boolean isCluster(){return false;}
    public boolean isClusterParent(){return false;}

    @Override
    public String toString(){
        return node.toString();
    }

    @Override
    public String toGraphString(){ return graphName != null ? graphName : "<html>" + toString() + "</html>"; }


    @Override
    public void setStyles(Config filter) {
        if(node.isList() || node.isOpt()) {
            styles.put("node-color", new Color("#C8C8C8"));
            styles.put("node-shape", new Str("\"rectangle\""));
        }
        else {
            styles.put("node-color", new Color("#C8F0E6"));
            styles.put("node-shape", new Str("\"rounded_rectangle\""));
        }
        styles.put("border-style", new Str("\"line\""));

        HashMap<String, Value> userStyle = filter.getNodeStyle(node);
        //System.out.println(userStyle.size());
        Iterator it = userStyle.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Value> pair = (Map.Entry)it.next();
            styles.put(pair.getKey(), pair.getValue());
        }
    }

    public void setDisplayedAttributes(Config config, ArrayList<NodeReference> references, ASTAPI api){
        HashSet<String> set = config.getDisplayedAttributes(node);
        if(set.size() == 0)
            return;
        graphName = "<html>" + toString();
        for (String s : set){
            if(!node.getNodeContent().contains(s))
                continue;
            NodeInfo info = node.getAttributeOrTokenValue(s);
            ArrayList<GenericTreeNode> refs = api.getReferenceNodes(info, false);
            if(refs != null && refs.size() > 0)
                references.add(new NodeReference(s, this, refs));
            else
                graphName += String.format("<br>%s : %s </br>", s, info.getValue());
        }
        graphName += "</html>";
    }
}
