package jastaddad.filteredtree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gda10jli on 11/2/15.
 */
public abstract class GenericTreeNode {
    protected List<GenericTreeNode> children;
    protected boolean refrenceHighlight;

    public GenericTreeNode(){
        children = new ArrayList();
    }

    public List<GenericTreeNode> getChildren(){ return children; }

    public void addChild(GenericTreeNode child) {
        children.add(child);
    }

    public void setRefrenceHighlight(boolean highlight){ refrenceHighlight = highlight; }

    public boolean isRefrenceHighlight(){ return refrenceHighlight; }

    public abstract boolean isNode();
    public abstract boolean isCluster();
    public abstract boolean isClusterParent();
    public abstract boolean isRealChild(GenericTreeNode child);

    @Override
    public String toString(){
        return "";
    }

}
