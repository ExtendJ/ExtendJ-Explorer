package drast.views.gui;

import drast.model.nodeinfo.NodeInfo;
import drast.model.nodeinfo.NodeInfoHolder;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

/**
 * This is a factory class for the tableViews in the AttributeTabController
 * It has basically the same fields as NodeInfoHolder, but other types.
 * This is done to ease the work of the programmer, so the fields in the tableview:s will auto update on change.
 */
public class AttributeInfo {

    private final SimpleStringProperty name;
    private final SimpleObjectProperty value;
    private final NodeInfo nodeInfo;
    private boolean filePointer;

    public AttributeInfo(String name, Object value, NodeInfo nodeInfo, boolean filePointer){
        this.name = new SimpleStringProperty(name);
        this.value = new SimpleObjectProperty(value);
        this.nodeInfo = nodeInfo;
        this.filePointer = filePointer;
    }

    public NodeInfo getNodeInfo(){ return nodeInfo; }

    public String getName(){ return name.get(); }

    public void setName(String name) {
        this.name.set(name);
    }

    public Object getValue() {
        return value.get();
    }
    public void setValue(Object value) {
        this.value.set(value);
    }

    public boolean isFilePointer(){return filePointer;}
    public static ArrayList<AttributeInfo> toArray(ArrayList<NodeInfoHolder> infoList){
        ArrayList<AttributeInfo> al = new ArrayList();
        for (NodeInfoHolder info : infoList)
            al.add(new AttributeInfo(info.getName(), info.getValue(),info.getNodeInfo(), info.getFilePointer()));
        return al;
    }

}
