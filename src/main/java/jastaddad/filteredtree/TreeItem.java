package jastaddad.filteredtree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gda10jli on 11/2/15.
 */
public abstract class TreeItem {
    protected List<TreeItem> children;
    protected boolean refrenceHighlight;

    public TreeItem(){
        children = new ArrayList();
    }

    public List<TreeItem> getChildren(){ return children; }

    public void addChild(TreeItem child) {
        children.add(child);
    }

    public void setRefrenceHighlight(boolean highlight){ refrenceHighlight = highlight; }

    public boolean isRefrenceHighlight(){ return refrenceHighlight; }

    public abstract boolean isNode();
    public abstract boolean isCluster();
    public abstract boolean isClusterParent();
    public abstract boolean isRealChild(TreeItem child);

    @Override
    public String toString(){
        return "";
    }
}
