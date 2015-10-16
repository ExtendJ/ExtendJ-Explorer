package jastaddad;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gda10jth on 10/16/15.
 */
public class FilteredTreeNode {
    public final Node node;
    private List<FilteredTreeNode> children;

    public FilteredTreeNode(Node data){
        node = data;
        children = new ArrayList<>();
    }

    public void addChild(FilteredTreeNode child){
        children.add(child);
    }

    public List<FilteredTreeNode> getChildren(){
        return children;
    }

    public Iterator<FilteredTreeNode> iterator(){
        return children.iterator();
    }
}
