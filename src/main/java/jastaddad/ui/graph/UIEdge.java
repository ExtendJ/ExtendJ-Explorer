package jastaddad.ui.graph;

import java.awt.*;

/**
 * This class represents the edge between the nodes in the GraphView.
 * It works as a holder for values which are then checked by the GraphView.
 */
public class UIEdge {

    public static final int STANDARD = 0;
    public static final int DISPLAYED_REF = 1;
    public static final int ATTRIBUTE_REF = 2;
    public static final int ATTRIBUTE_NTA = 3;

    private boolean realChild;
    private boolean reference;
    private String label;
    private int type = STANDARD;

    public UIEdge(boolean realChild) {
        this.realChild = realChild;
        label = "";
    }
    public UIEdge(boolean realChild, String label) {
        this.label = label;
        this.realChild = realChild;
    }

    public UIEdge(int type){
        this.type = type;
        reference = type != ATTRIBUTE_NTA;
    }

    public UIEdge setLabel(String label){ this.label = label; return this; }

    public boolean isReference(){ return reference; }

    public boolean isRealChild(){ return realChild; }

    public void setType(int type) {
        this.type = type;
    }

    public String toString() { // Always good for debugging
        return label;
    }

    public Color getColor(){
        switch (type){
            case STANDARD :
                return new Color(0, 0, 0);
            case ATTRIBUTE_REF :
                return new Color(80, 180, 80);
            case DISPLAYED_REF :
                return new Color(200, 80, 80);
            case ATTRIBUTE_NTA :
                return new Color(140, 140, 200);
            default:
                return new Color(0, 0, 0);
        }
    }
}
