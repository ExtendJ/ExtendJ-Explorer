package jastaddad.filteredtree;

import jastaddad.Config;
import jastaddad.Node;

import java.util.*;

/**
 * Created by gda10jth on 10/16/15.
 */
public class TreeNode extends TreeItem {
    public final Node node;
    private boolean enabled;
    private LinkedHashMap<Integer, Boolean> realChildEdge;

    public TreeNode(Node data, Config filter){
        super();
        node = data;
        realChildEdge = new LinkedHashMap<>();
        enabled = setEnabled(filter);
    }

    public void setEnabled(boolean enabled){ this.enabled = enabled; }

    private boolean setEnabled(Config filter){
        return filter.isEnabled(node);
    }

    public boolean isEnabled(){ return enabled; }

    @Override
    public void addChild(TreeItem child){
        if(child.isNode()) {
            TreeNode c = (TreeNode)child;
            realChildEdge.put(c.node.id, node.children.contains(c.node));
        }
        children.add(child);
    }

    public boolean isRealChild(TreeItem child){
        return !child.isNode() ? false : realChildEdge.get(((TreeNode)child).node.id);
    }

    public Iterator<TreeItem> iterator(){ return children.iterator(); }

    public boolean isNode(){ return true; }
    public boolean isCluster(){return false;}
    public boolean isClusterParent(){return false;}

    @Override
    public String toString(){
        return node.toString();
    }
}
