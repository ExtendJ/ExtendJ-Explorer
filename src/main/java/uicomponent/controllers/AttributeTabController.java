package uicomponent.controllers;

import jastaddad.filteredtree.GenericTreeNode;
import jastaddad.filteredtree.TreeNode;
import jastaddad.objectinfo.NodeContent;
import jastaddad.objectinfo.NodeInfo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;
import uicomponent.UIMonitor;
import uicomponent.graph.GraphView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by gda10jth on 11/2/15.
 */
public class AttributeTabController implements Initializable, ChangeListener<NodeInfo> {
    private UIMonitor mon;
    private GraphView graphView;

    @FXML
    private ListView listView;

    public void init(UIMonitor mon, GraphView graphView){
        this.mon = mon;
        this.graphView = graphView;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Attribute Listener
        listView.getSelectionModel().selectedItemProperty().addListener(this);
    }

    public void setAttributeList(){
        if(!mon.getSelectedNode().isNode())
            return;
        TreeNode fNode = ((TreeNode)mon.getSelectedNode());
        NodeContent a = fNode.node.getNodeContent();
        ArrayList<NodeInfo> al = a.toArray();  //Todo remove this when we change the UI, ie we add a proper node name label
        al.add(0, new NodeInfo(fNode.node.nodeName(), "", null) {
            @Override
            public String print() {
                return "Node name: " + name;
            }
        });
        listView.getSelectionModel().clearSelection();
        listView.setItems(FXCollections.observableList(al));
    }

    @Override
    public void changed(ObservableValue<? extends NodeInfo> observable, NodeInfo oldValue, NodeInfo newValue) {
        GenericTreeNode refNode = null;
        if (oldValue != null && mon.getApi().isReferenceNode(oldValue.getValue()))
            mon.getApi().getReferenceNode(oldValue.getValue()).setReferenceHighlight(false);
        if(newValue != null && mon.getApi().isReferenceNode(newValue.getValue())) {
            refNode = mon.getApi().getReferenceNode(newValue.getValue());
            refNode.setReferenceHighlight(true);
            mon.setReferenceNode(refNode);
        }
        graphView.setReferenceEdge(refNode, mon.getSelectedNode());
    }
}
