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
public class FilteredTreeNode implements FilteredTreeItem {
    public final Node node;
    private boolean enabled;
    private List<FilteredTreeItem> children;
    private HashMap<Integer, Boolean> realChildEdge;

    public FilteredTreeNode(Node data, Config cfgTypeList){
        node = data;
        children = new ArrayList<>();
        realChildEdge = new HashMap<>();
        enabled = setEnabled(cfgTypeList);
    }

    public void setEnabled(boolean enabled){ this.enabled = enabled; }

    private boolean setEnabled(Config cfgTypeList){
        return cfgTypeList.configCount() == 0
                || cfgTypeList.isEnabled(node.className)
                && (cfgTypeList.get(node.className + ":" + node.name) == null
                || cfgTypeList.isEnabled(node.className + ":" + node.name));
    }

    public boolean isEnabled(){ return enabled; }

    @Override
    public void addChild(FilteredTreeItem child){
        if(child.isNode()) {
            FilteredTreeNode c = (FilteredTreeNode)child;
            realChildEdge.put(c.node.id, node.children.contains(c.node));
        }
        children.add(child);
    }

    public boolean isRealChild(FilteredTreeItem child){
        //System.out.println(realChildEdge.get(child.node.id));
        if(!child.isNode())
            return false;

        return realChildEdge.get(((FilteredTreeNode)child).node.id);
    }

    @Override
    public List<FilteredTreeItem> getChildren(){
        return children;
    }

    public Iterator<FilteredTreeItem> iterator(){ return children.iterator(); }

    public boolean isNode(){ return true; }
    public boolean isCluster(){return false;}
    public boolean isClusterParent(){return false;}

    @Override
    public String toString(){
        return node.toString();
    }
}
