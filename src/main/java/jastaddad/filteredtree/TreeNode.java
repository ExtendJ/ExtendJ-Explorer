package jastaddad.filteredtree;

import configAST.Color;
import configAST.Str;
import configAST.Value;
import jastaddad.Config;
import jastaddad.Node;

import java.util.*;

/**
 * Created by gda10jth on 10/16/15.
 */
public class TreeNode extends GenericTreeNode {
    public final Node node;
    private boolean enabled;
    private LinkedHashMap<Integer, Boolean> realChildEdge;

    public TreeNode(Node data, Config filter){
        super();
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
    public String toGraphString(){
        String name = "<html>"  + toString();
        if(displayedAttributes == null)
            return name + "</html>";
        for(Map.Entry<String, Object> s: displayedAttributes.entrySet())
            name += String.format("<br>%s : %s </br>", s.getKey(), s.getValue());
        return name + "</html>";
    }

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

    public void setDisplayedAttributes(Config config){
        HashSet<String> set = config.getDisplayedAttributes(node);
        if(set.size() == 0)
            return;
        displayedAttributes = new HashMap<>();
        for (String s : set){
            if(!node.getNodeContent().contains(s))
                continue;
            displayedAttributes.put(s, node.getAttributeOrTokenValue(s).getValue());
        }
    }
}
