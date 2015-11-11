package jastaddad.filteredtree;

import java.util.ArrayList;

/**
 * Created by gda10jli on 11/11/15.
 */
public class NodeReference {
    private String label;
    private GenericTreeNode from;
    private GenericTreeNode to;
    private ArrayList<GenericTreeNode> toList;

    public NodeReference(String label, GenericTreeNode from, ArrayList<GenericTreeNode> toList){
        this.label = label;
        this.from =from;
        this.toList = toList;
    }

    public ArrayList<GenericTreeNode> getReferences(){ return toList; }
    public GenericTreeNode getReferenceFrom(){ return from; }
    public String getLabel(){ return label; }


}
