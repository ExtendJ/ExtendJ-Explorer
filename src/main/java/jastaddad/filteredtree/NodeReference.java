package jastaddad.filteredtree;

import java.util.ArrayList;

/**
 * Created by gda10jli on 11/11/15.
 */
public class NodeReference {
    private String label;
    private GenericTreeNode from;
    private GenericTreeNode to;
    private ArrayList<GenericTreeNode> refList;
    private ArrayList<Object> futureRefList;

    public NodeReference(String label, GenericTreeNode from, ArrayList<Object> futureRefList){
        this.label = label;
        this.from =from;
        this.futureRefList = futureRefList;
    }

    public void setReferences(ArrayList<GenericTreeNode> treeNodes){ this.refList = treeNodes; }

    public ArrayList<Object> getFutureReferences(){ return futureRefList; }

    public ArrayList<GenericTreeNode> getReferences(){ return refList; }
    public GenericTreeNode getReferenceFrom(){ return from; }
    public String getLabel(){ return label; }


}
