package uicomponent.controllers;

import jastaddad.filteredtree.GenericTreeNode;
import jastaddad.filteredtree.TreeNode;
import jastaddad.objectinfo.NodeContent;
import jastaddad.objectinfo.NodeInfo;
import jastaddad.objectinfo.NodeInfoHolder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import uicomponent.UIMonitor;
import uicomponent.graph.GraphView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by gda10jth on 11/2/15.
 */
public class AttributeTabController implements Initializable, ChangeListener<NodeInfoHolder> {
    private UIMonitor mon;
    private GraphView graphView;

    @FXML private TableView<NodeInfoHolder> attributeTableView;
    @FXML private TableColumn<NodeInfoHolder, String> attributeNameCol;
    @FXML private TableColumn<NodeInfoHolder, Object> attributeValueCol;

    @FXML private TableView<NodeInfoHolder> attributeInfoTableView;
    @FXML private TableColumn<NodeInfoHolder, String> attributeInfoNameCol;
    @FXML private TableColumn<NodeInfoHolder, Object> attributeInfoValueCol;

    @FXML private Label nodeNameLabel;
    @FXML private Label attributeInfoLabel;

    public void init(UIMonitor mon, GraphView graphView){
        this.mon = mon;
        this.graphView = graphView;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        attributeTableView.getSelectionModel().selectedItemProperty().addListener(this);
        attributeInfoNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        attributeInfoValueCol.setCellValueFactory(new PropertyValueFactory("value"));
        attributeNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        attributeValueCol.setCellValueFactory(new PropertyValueFactory("value"));

        //Yey hiding the header for attribute info, so ugly
        attributeInfoTableView.widthProperty().addListener((source, oldWidth, newWidth) -> {
            Pane header = (Pane) attributeInfoTableView.lookup("TableHeaderRow");
            if (header.isVisible()){
                header.setMaxHeight(0);
                header.setMinHeight(0);
                header.setPrefHeight(0);
                header.setVisible(false);
            }
        });
    }

    public void setAttributes(){
        GenericTreeNode node = mon.getSelectedNode();
        if(node == null || !mon.getSelectedNode().isNode()) {
            attributeTableView.getItems().clear();
            attributeInfoTableView.getItems().clear();
            return;
        }
        nodeNameLabel.setText(node.toString());
        setAttributeList((TreeNode) node);
    }

    public void setAttributeList(TreeNode node){
        attributeTableView.getSelectionModel().clearSelection();
        attributeTableView.setItems(FXCollections.observableList(node.node.getNodeContent().toArray()));
        if(attributeTableView.getItems().size() > 0)
            attributeTableView.getSelectionModel().select(0);
        else
            attributeInfoTableView.getItems().clear();
    }

    @Override
    public void changed(ObservableValue<? extends NodeInfoHolder> observable, NodeInfoHolder oldValue, NodeInfoHolder newValue) {
        setAttributeInfo(newValue);
        setReference(oldValue, newValue);
    }

    private void setAttributeInfo(NodeInfoHolder info){
        if(info == null || info.nodeInfo == null)
            return;
        attributeInfoLabel.setText(info.nodeInfo.print());
        attributeInfoTableView.setItems(FXCollections.observableArrayList(info.nodeInfo.getInfo()));
    }

    private void setReference(NodeInfoHolder oldValue, NodeInfoHolder newValue){
        GenericTreeNode refNode = null;
        if (oldValue != null && mon.getApi().isReferenceNode(oldValue.getValue()))
            mon.getApi().getReferenceNode(oldValue.getValue()).setReferenceHighlight(false);
        if(newValue != null && mon.getApi().isReferenceNode(newValue.getValue())) {
            refNode = mon.getApi().getReferenceNode(newValue.getValue());
            refNode.setReferenceHighlight(true);
            mon.setReferenceNode(refNode);
        }else
            mon.setReferenceNode(null);
        graphView.setReferenceEdge(refNode, mon.getSelectedNode());
    }
}
