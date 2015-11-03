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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

    @FXML private TableView<NodeInfo> attributeInfo;
    @FXML private TableColumn<NodeInfo, String> attributeNameCol;
    @FXML private TableColumn<NodeInfo, String> attributeValueCol;

    @FXML
    private Label nodeNameLabel;
    @FXML
    private ListView attributeList;

    public void init(UIMonitor mon, GraphView graphView){
        this.mon = mon;
        this.graphView = graphView;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Attribute Listener
        attributeList.getSelectionModel().selectedItemProperty().addListener(this);
    }

    public void setAttributes(){
        GenericTreeNode node = mon.getSelectedNode();
        if(node == null)
            return;
        nodeNameLabel.setText(node.toString());
        if(!mon.getSelectedNode().isNode()) {
            attributeList.getItems().clear();
            return;
        }
        setAttributeList((TreeNode) node);
    }

    public void setAttributeList(TreeNode node){
        NodeContent a = node.node.getNodeContent();
        ArrayList<NodeInfo> al = a.toArray();
        attributeList.getSelectionModel().clearSelection();
        attributeList.setItems(FXCollections.observableList(al));
    }

    @Override
    public void changed(ObservableValue<? extends NodeInfo> observable, NodeInfo oldValue, NodeInfo newValue) {
        setAttributeInfo(newValue);
        setReference(oldValue, newValue);
    }

    private void setAttributeInfo(NodeInfo info){
        if(info == null)
            return;
    }

    private void setReference(NodeInfo oldValue, NodeInfo newValue){
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
