package jastaddad.filteredtree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gda10jli on 11/2/15.
 */
public abstract class GenericTreeNode {
    protected List<GenericTreeNode> children;
    protected boolean referenceHighlight;
    protected GenericTreeNode clusterRef;

    public GenericTreeNode(){
        children = new ArrayList();
    }

    public List<GenericTreeNode> getChildren(){ return children; }

    public void addChild(GenericTreeNode child) {
        children.add(child);
    }

    public void setReferenceHighlight(boolean highlight){
        referenceHighlight = highlight;
        if(clusterRef != null){
            clusterRef.setReferenceHighlight(highlight);
        }
    }
    public boolean isReferenceHighlight(){ return referenceHighlight; }

    public abstract boolean isNode();
    public abstract boolean isCluster();
    public abstract boolean isClusterParent();
    public abstract boolean isRealChild(GenericTreeNode child);


    public boolean hasClusterReference(){ return clusterRef != null; }

    public GenericTreeNode getClusterReference(){ return clusterRef.clusterRef == null ? clusterRef : clusterRef.clusterRef; }

    public void setClusterReference(GenericTreeNode clusterRef){ this.clusterRef = clusterRef; }

    @Override
    public String toString(){
        return this.getClass().toString();
    }
}
