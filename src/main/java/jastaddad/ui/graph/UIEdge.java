package jastaddad.ui.graph;

import java.awt.*;

/**
 * This class represents the edge between the nodes in the GraphView.
 * It works as a holder for values which are then checked by the GraphView.
 */
public class UIEdge {

    public static final int DISPLAYED_REF = 0;
    public static final int ATTRIBUTE_REF = 1;

    private boolean realChild;
    private boolean reference;
    private String label;
    private int reftype;

    public UIEdge(boolean realChild) {
        this.realChild = realChild;
        label = "";
    }
    public UIEdge(boolean realChild, String label) {
        this.label = label;
        this.realChild = realChild;
    }

    public UIEdge(int reftype){ this.reftype = reftype; reference = true; }

    public UIEdge setLabel(String label){ this.label = label; return this; }

    public boolean isReference(){ return reference; }

    public int getReferenceType(){ return reftype; }

    public boolean isRealChild(){ return realChild; }

    public String toString() { // Always good for debugging
        return label;
    }

    public Color getColor(){
        if(reference) { //Different color per reference type
            if (reftype == UIEdge.ATTRIBUTE_REF)
                return new Color(80, 180, 80);
            if (reftype == UIEdge.DISPLAYED_REF)
                return new Color(200, 80, 80);
        }
        return new Color(0, 0, 0);
    }
}
