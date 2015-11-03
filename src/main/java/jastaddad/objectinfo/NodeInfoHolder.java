package jastaddad.objectinfo;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by gda10jli on 11/3/15.
 */
public class NodeInfoHolder {

    private final SimpleStringProperty name;
    private final SimpleObjectProperty value;
    public final NodeInfo nodeInfo;

    public NodeInfoHolder(String name, Object value){
        this.name = new SimpleStringProperty(name);
        this.value = new SimpleObjectProperty(value);
        nodeInfo = null;
    }

    public NodeInfoHolder(String name, Object value, NodeInfo nodeInfo){
        this.name = new SimpleStringProperty(name);
        this.value = new SimpleObjectProperty(value);
        this.nodeInfo = nodeInfo;
    }

    public String getName() {
        return name.get();
    }
    public void setName(String name) {
        this.name.set(name);
    }

    public Object getValue() {
        return value.get();
    }
    public void setValue(Object value) {
        this.value.set(value);
    }

}
