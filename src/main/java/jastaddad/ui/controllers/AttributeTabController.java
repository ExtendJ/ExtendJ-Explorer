package jastaddad.ui.controllers;

import jastaddad.api.Node;
import jastaddad.api.filteredtree.GenericTreeNode;
import jastaddad.api.filteredtree.TreeNode;
import jastaddad.api.nodeinfo.NodeInfo;
import jastaddad.ui.AttributeInfo;
import jastaddad.ui.AttributeInputDialog;
import jastaddad.ui.UIMonitor;
import jastaddad.ui.graph.GraphView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This is a controller class that keeps track of the attribute tab.
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

    /**
     * Will set all the listeners and the start values for the ui components.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        attributeTableView.getSelectionModel().selectedItemProperty().addListener(this);
        attributeInfoNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        attributeInfoValueCol.setCellValueFactory(new PropertyValueFactory("value"));
        attributeNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        attributeValueCol.setCellValueFactory(new PropertyValueFactory("value"));
        attributeValueCol.setCellFactory(param -> new AttributeValueCell());

        //Yey hiding the header for attribute info tableview, so ugly
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
        MenuItem cmItem1 = new MenuItem("Compute");
        /**
         * This sub method will call teh invocation of the method that has been clicked on, after the values have been added
         */
        cmItem1.setOnAction(e -> {
            TreeNode node = (TreeNode) mon.getSelectedNode();
            NodeInfo info  = attributeTableView.getSelectionModel().getSelectedItem().getNodeInfo();

            AttributeInputDialog dialog = new AttributeInputDialog(info, node, mon);
            dialog.init();
            dialog.setOnCloseRequest(event -> {
                if(dialog.invokeButtonPressed()) {
                    ArrayList<Object> result = dialog.getResult();
                    if(result == null)
                        return;
                    Object obj = null;
                    if(mon.getLastRealNode() != null){
                        obj = dialog.getTreeNode().getNode().getNodeContent().compute(dialog.getInfo(), result);
                        if(node.getNode().getNodeContent().noErrors() && obj != null){
                            mon.getController().addMessage("Invocation successful, result: " + obj);
                        }else{
                            mon.getController().addMessage("Invocation unsuccessful, result: " + null);
                            mon.getController().addErrors(node.getNode().getNodeContent().getInvocationErrors());
                        }
                        setAttributeList(node, false);
                    }
                }
                mon.getController().nodeSelected(dialog.getTreeNode(), false);
            });
            dialog.show();



        });
        mouseMenu.getItems().add(cmItem1);
    }

    public void functionStarted(){

    }

    public void functionStoped(){

    }

    /**
     * Sets the attributes in the tableview, called when a node has been selected.
     * Will Clear the list if the selected node is null or not a "real" node
     */
    public void setAttributes(){
        GenericTreeNode node = mon.getSelectedNode();
        if(node == null || !mon.getSelectedNode().isNode()) {
            nodeNameLabel.setText("");
            attributeTableView.getItems().clear();
            attributeInfoTableView.getItems().clear();
            return;
        }
        nodeNameLabel.setText(node.toString());
        setAttributeList((TreeNode) node, true);
    }

    /**
     * Sets the attributeTableView, which all the attributes for the given node
     * @param node
     * @param compute
     */
    public void setAttributeList(TreeNode node, boolean compute){
        Node n = node.getNode();
        if(compute) {
            mon.getController().addErrors(n.getNodeContent().compute());
            attributeTableView.getSelectionModel().clearSelection();
        }
        attributeTableView.setItems(FXCollections.observableList(AttributeInfo.toArray(n.getNodeContent().toArray())));
        if(attributeTableView.getItems().size() <= 0) {
            attributeInfoTableView.getItems().clear();
        }

    }

    /**
     * Listener for the attribute tableview, sets the attributeInfoTableView for the selected attribute.
     * It will also remove old references, and add new references if the value of selected attribute is a AST node.
     * Also tells the main controller that an attribute was selected.
     * @param observable
     * @param oldValue
     * @param newValue
     */
    @Override
    public void changed(ObservableValue<? extends AttributeInfo> observable, AttributeInfo oldValue, AttributeInfo newValue) {
        setAttributeInfo(newValue);
        mon.setSelectedInfo(newValue);
        mon.getController().attributeInNodeSelected(newValue);
        if(oldValue != null)
            mon.getApi().getNodeReferences(oldValue.getNodeInfo(), false);
        setReference(newValue);
    }

    /**
     * Fill the attributeInfoTableView with information about the attribute
     * @param info
     */
    private void setAttributeInfo(AttributeInfo info){
        if(info == null || info.getNodeInfo() == null) {
            attributeInfoLabel.setText("");
            return;
        }
        attributeInfoLabel.setText(info.getNodeInfo().print());
        attributeInfoTableView.setItems(FXCollections.observableArrayList(AttributeInfo.toArray(info.getNodeInfo().getInfo())));
    }

    /**
     * Adds references from the selected node in the monitor and the values found in the AttributeInfo supplied.
     * @param info
     */
    public void setReference(AttributeInfo info){
        ArrayList<GenericTreeNode> newRefs = null;
        if(info != null)
            newRefs = mon.getApi().getNodeReferences(info.getNodeInfo(), true);
        graphView.setReferenceEdges(newRefs, mon.getSelectedNode());
    }

    /**
     * Class for the cells in the tableviews
     */
    private class AttributeValueCell extends TableCell<AttributeInfo, Object>{
        @Override
        protected void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);
            setContextMenu(null);
            if(getTableRow().getItem() != null){
                NodeInfo info = ((AttributeInfo) getTableRow().getItem()).getNodeInfo();
                if (info != null && info.isParametrized()){
                    setText("Need input form user");
                }else if(info != null && info.isNTA())
                    setText("Is NTA, need to be run by user");
                else
                    setText(String.valueOf(item));
            }else if (empty || item == null) {
                setText(null);
                return;
            }else if(getTableRow().getItem() == null){
                setText(String.valueOf(item));
                return;
            }
            setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    if(getTableRow().getItem() != null &&
                            ((AttributeInfo )getTableRow().getItem()).getNodeInfo().isParametrized() &&
                            !mon.isFunctionRunning()) {
                        mouseMenu.show(this, event.getScreenX(), event.getScreenY());
                    }
                }
            });
        }

    }
}
