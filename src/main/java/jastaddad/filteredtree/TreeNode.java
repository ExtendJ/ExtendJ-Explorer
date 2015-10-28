package jastaddad.filteredtree;

import jastaddad.Config;
import jastaddad.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gda10jth on 10/16/15.
 */
public class TreeNode implements TreeItem {
    public final Node node;
    private boolean enabled;
    private List<TreeItem> children;
    private HashMap<Integer, Boolean> realChildEdge;

    public TreeNode(Node data, Config cfgTypeList){
        node = data;
        children = new ArrayList<>();
        realChildEdge = new HashMap<>();
        enabled = setEnabled(cfgTypeList);
    }

    public void setEnabled(boolean enabled){ this.enabled = enabled; }

    private boolean setEnabled(Config cfgTypeList){
        return true;
        /*return cfgTypeList.configCount() == 0
                || cfgTypeList.isEnabled(node.className)
                && (cfgTypeList.get(node.className + ":" + node.name) == null
                || cfgTypeList.isEnabled(node.className + ":" + node.name));*/
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
        //System.out.println(realChildEdge.get(child.node.id));
        if(!child.isNode())
            return false;

        return realChildEdge.get(((TreeNode)child).node.id);
    }

    @Override
    public List<TreeItem> getChildren(){
        return children;
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
