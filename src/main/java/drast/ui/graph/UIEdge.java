package drast.ui.graph;

import java.awt.*;

/**
 * This class represents the edge between the nodes in the GraphView.
 * It works as a holder for values which are then checked by the GraphView.
 */
public class UIEdge {

    public static final int STANDARD = 0;
    public static final int CLUSTER = 1;
    public static final int DISPLAYED_REF = 2;
    public static final int ATTRIBUTE_REF = 3;
    public static final int ATTRIBUTE_NTA = 4;

    private boolean reference;
    private String label;
    private int type = STANDARD;

    public UIEdge() {}

    public UIEdge(int type){
        this.type = type;
        reference = type != ATTRIBUTE_NTA;
    }

    public UIEdge setLabel(String label){ this.label = label; return this; }

    public boolean isReference(){ return reference; }

    public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public String toString() { // Always good for debugging
        return label;
    }

    public Color getColor(){
        switch (type){
            case STANDARD :
                return new Color(0, 0, 0);
            case ATTRIBUTE_NTA :
                return new Color(140, 140, 200);
            case ATTRIBUTE_REF :
                return new Color(80, 180, 80);
            case DISPLAYED_REF :
                return new Color(200, 80, 80);
            default:
                return new Color(0, 0, 0);
        }
    }
}
