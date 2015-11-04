package uicomponent.controllers;

import jastaddad.filteredtree.GenericTreeNode;
import jastaddad.filteredtree.TreeNode;
import jastaddad.objectinfo.Attribute;
import jastaddad.objectinfo.NodeInfo;
import uicomponent.AttributeInfo;
import uicomponent.AttributeInputDialog;
import jastaddad.objectinfo.NodeInfoHolder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import uicomponent.UIMonitor;
import uicomponent.graph.GraphView;

import java.net.URL;
import java.util.*;

/**
 * Created by gda10jth on 11/2/15.
 */
public class AttributeTabController implements Initializable, ChangeListener<AttributeInfo> {
    private UIMonitor mon;
    private GraphView graphView;
    private ContextMenu mouseMenu;

    @FXML private TableView<AttributeInfo> attributeTableView;
    @FXML private TableColumn<AttributeInfo, String> attributeNameCol;
    @FXML private TableColumn<AttributeInfo, Object> attributeValueCol;

    @FXML private TableView<AttributeInfo> attributeInfoTableView;
    @FXML private TableColumn<AttributeInfo, String> attributeInfoNameCol;
    @FXML private TableColumn<AttributeInfo, Object> attributeInfoValueCol;

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
        attributeValueCol.setCellFactory(param -> new AttributeValueCell());

        //Yey hiding the header for attribute info, so ugly
        attributeInfoTableView.widthProperty().addListener((source, oldWidth, newWidth) -> {
            Pane header = (Pane) attributeInfoTableView.lookup("TableHeaderRow");
            if (header.isVisible()) {
                header.setMaxHeight(0);
                header.setMinHeight(0);
                header.setPrefHeight(0);
                header.setVisible(false);
            }
        });

        mouseMenu = new ContextMenu();
        MenuItem cmItem1 = new MenuItem("Invoke with parameters");
        cmItem1.setOnAction(e -> {
            Attribute attr = (Attribute) attributeTableView.getSelectionModel().getSelectedItem().getNodeInfo();
            AttributeInputDialog dialog = new AttributeInputDialog(attr);
            Optional<ArrayList<Object>> result = dialog.showAndWait();
            if(result == null || result.get() == null)
                return;
            Object obj = null;
            if(mon.getLastRealNode() != null)
                obj = attr.invokeMethod(((TreeNode) mon.getSelectedNode()).node, result.get());
            if(obj != null){
                mon.getController().addMessage("Invoke successful, result: " + obj);
                setAttributeInfo(attributeTableView.getSelectionModel().getSelectedItem());
            }else
                mon.getController().addMessage("Invoke unsuccessful, result: " + obj);
        });
        mouseMenu.getItems().add(cmItem1);
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
        attributeTableView.setItems(FXCollections.observableList(AttributeInfo.toArray(node.node.getNodeContent().toArray())));
        if(attributeTableView.getItems().size() > 0)
            attributeTableView.getSelectionModel().select(0);
        else
            attributeInfoTableView.getItems().clear();
    }

    @Override
    public void changed(ObservableValue<? extends AttributeInfo> observable, AttributeInfo oldValue, AttributeInfo newValue) {
        setAttributeInfo(newValue);
        setReference(oldValue, newValue);
    }

    private void setAttributeInfo(AttributeInfo info){
        if(info == null || info.getNodeInfo() == null)
            return;
        mon.getController().addError("" + (info.getValue() instanceof Collection<?>  || info.getValue() instanceof Map<?,?>));
        attributeInfoLabel.setText(info.getNodeInfo().print());
        attributeInfoTableView.setItems(FXCollections.observableArrayList(AttributeInfo.toArray(info.getNodeInfo().getInfo())));
    }

    private void setReference(AttributeInfo oldValue, AttributeInfo newValue){
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

    private class AttributeValueCell extends TableCell<AttributeInfo, Object>{
        @Override
        protected void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setContextMenu(null);
                return;
            }
            setText(String.valueOf(item));
            if(getTableRow().getItem() != null && ((AttributeInfo )getTableRow().getItem()).getNodeInfo().isParametrized())
                setContextMenu(mouseMenu);
            else
                setContextMenu(null);
        }

    }
}
