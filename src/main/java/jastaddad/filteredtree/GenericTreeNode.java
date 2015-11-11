package jastaddad.filteredtree;

import configAST.Color;
import configAST.Str;
import configAST.Value;
import jastaddad.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by gda10jli on 11/2/15.
 */
public abstract class GenericTreeNode {
    protected List<GenericTreeNode> children;
    protected boolean referenceHighlight;
    protected GenericTreeNode clusterRef;
    protected HashMap<String, Value> styles;
    protected HashMap<String, Object> displayedAttributes;
    protected HashMap<String, Object> ref;
    private boolean isExpandable;

    public GenericTreeNode(){
        children = new ArrayList();
        styles = new HashMap<>();
        isExpandable = false;
        styles.put("node-color", new Color("\"\""));
        styles.put("node-shape", new Str("\"\""));
        styles.put("border-style", new Str("\"\""));
    }

    public void setExpandable(boolean expandable){ isExpandable = expandable;}
    public boolean isExpandable(){return isExpandable;}

    public List<GenericTreeNode> getChildren(){ return children; }

    public void addChild(GenericTreeNode child) {
        children.add(child);
    }

    public GenericTreeNode setReferenceHighlight(boolean highlight){
        referenceHighlight = highlight;
        if(clusterRef != null){
            return clusterRef.setReferenceHighlight(highlight);
        }
        return this;
    }
    public boolean isReferenceHighlight(){ return referenceHighlight; }

    public abstract boolean isNode();
    public abstract boolean isCluster();
    public abstract boolean isClusterParent();
    public abstract boolean isRealChild(GenericTreeNode child);
    public abstract String toGraphString();
    public abstract void setStyles(Config filter);
    public HashMap<String, Value> getStyles(){ return styles;};

    public boolean hasDisplayedAttributes(){ return displayedAttributes != null && displayedAttributes.size() > 0; }
    public HashMap<String, Object> getDisplayedAttributes(){ return displayedAttributes; }

    public boolean hasClusterReference(){ return clusterRef != null; }
    public GenericTreeNode getClusterReference(){
        if(clusterRef == null)
            return this;
        return clusterRef.clusterRef != null ? clusterRef.clusterRef : clusterRef;
    }

    public void setClusterReference(GenericTreeNode clusterRef){ this.clusterRef = clusterRef; }

}