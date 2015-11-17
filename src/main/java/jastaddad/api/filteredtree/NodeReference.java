package jastaddad.api.filteredtree;

import java.util.ArrayList;

/**
 * Class that keeps track of the references generated by a attribute method.
 * Contains the node the references are from and to which nodes they point to.
 */
public class NodeReference {
    private String label;
    private GenericTreeNode from;
    private GenericTreeNode to;
    private ArrayList<GenericTreeNode> refList;
    private ArrayList<Object> futureRefList;

    /**
     * A NodeReference with a label and a list of eventual references.
     * @param label
     * @param from
     * @param futureRefList
     */
    public NodeReference(String label, GenericTreeNode from, ArrayList<Object> futureRefList){
        this.label = label;
        this.from =from;
        this.futureRefList = futureRefList;
    }

    /**
     * A NodeReference with a label, will create a empty list of references
     * @param label
     * @param from
     */
    public NodeReference(String label, GenericTreeNode from){
        this.label  = label;
        this.from = from;
        this.refList = new ArrayList<>();
    }

    public void setReferences(ArrayList<GenericTreeNode> treeNodes){ this.refList = treeNodes; }
    public boolean addReference(GenericTreeNode treeNode){ return refList.add(treeNode); }

    /**
     * Returns the list of "future" references, these needs to be controlled that they really are real node references.
     * @return
     */
    public ArrayList<Object> getFutureReferences(){ return futureRefList; }

    public ArrayList<GenericTreeNode> getReferences(){ return refList; }
    public GenericTreeNode getReferenceFrom(){ return from; }
    public String getLabel(){ return label; }


}
