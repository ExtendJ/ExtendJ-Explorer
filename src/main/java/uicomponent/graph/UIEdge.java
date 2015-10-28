package uicomponent.graph;

/**
 * Created by gda10jth on 10/19/15.
 */
public class UIEdge {
    private boolean realChild;
    private String label;

    public UIEdge(boolean realChild) {
        this.realChild = realChild;
        label = "";
    }
    public UIEdge(boolean realChild, String label) {
        this.label = label;
        this.realChild = realChild;
    }

    public boolean isRealChild(){ return realChild; }

    public String toString() { // Always good for debugging
        return label;
    }
}
