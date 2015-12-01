package jastaddad.ui.controllers;

import jastaddad.api.ASTAPI;
import jastaddad.api.Node;
import jastaddad.api.filteredtree.GenericTreeNode;
import jastaddad.api.filteredtree.TreeNode;
import jastaddad.api.nodeinfo.Attribute;
import jastaddad.api.nodeinfo.NodeInfo;
import jastaddad.ui.AttributeInfo;
import jastaddad.ui.AttributeInputDialog;
import jastaddad.ui.UIMonitor;
import jastaddad.ui.graph.GraphView;
import jastaddad.ui.uicomponent.nodeinfotreetableview.NodeInfoHolder;
import jastaddad.ui.uicomponent.nodeinfotreetableview.NodeInfoInterface;
import jastaddad.ui.uicomponent.nodeinfotreetableview.NodeInfoLabel;
import jastaddad.ui.uicomponent.nodeinfotreetableview.NodeInfoParameter;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.*;

/**
 * This is a controller class that keeps track of the attribute tab.
 */
public class AttributeTabController implements Initializable, ChangeListener<TreeItem<NodeInfoInterface>> {
    private UIMonitor mon;
    private GraphView graphView;
    private ContextMenu mouseMenu;

    @FXML private TreeTableView<NodeInfoInterface> attributeTableView;
    @FXML private TreeTableColumn<NodeInfoInterface, String> attributeNameCol;
    @FXML private TreeTableColumn<NodeInfoInterface, Object> attributeValueCol;

    @FXML private TableView<AttributeInfo> attributeInfoTableView;
    @FXML private TableColumn<AttributeInfo, String> attributeInfoNameCol;
    @FXML private TableColumn<AttributeInfo, Object> attributeInfoValueCol;

    @FXML private Label nodeNameLabel;
    @FXML private Label attributeInfoLabel;

    private ArrayList<TreeItem<NodeInfo>> labelTreeItems;
    private int indexOfSelectedAttribute;
    public void init(UIMonitor mon, GraphView graphView){
        this.mon = mon;
        this.graphView = graphView;
        labelTreeItems = new ArrayList<>();
        indexOfSelectedAttribute = 0;
    }

    /**
     * Will set all the listeners and the start values for the ui components.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        attributeTableView.getSelectionModel().selectedItemProperty().addListener(this);
        //attributeTableView.
        attributeInfoNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        attributeInfoValueCol.setCellValueFactory(new PropertyValueFactory("value"));

        attributeNameCol.setCellValueFactory(param -> {
            NodeInfoInterface info = param.getValue().getValue();
            if(info != null) {
                return new ReadOnlyStringWrapper(param.getValue().getValue().getName());
            }
            return null;
        });
        attributeValueCol.setCellValueFactory(
                param -> {
                    if(param.getValue().getValue() != null)
                        return new ReadOnlyObjectWrapper(param.getValue().getValue().getValue());
                    return null;
                }
        );

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

        attributeNameCol.prefWidthProperty().bind(attributeTableView.widthProperty().multiply(0.50));
        attributeValueCol.prefWidthProperty().bind(attributeTableView.widthProperty().multiply(0.50));

        mouseMenu = new ContextMenu();
        MenuItem cmItem1 = new MenuItem("Compute");
        /**
         * This sub method will call the invocation of the method that has been clicked on, after the values have been added
         */
        cmItem1.setOnAction(e -> {
            TreeNode node = (TreeNode) mon.getSelectedNode();
            NodeInfoInterface selectedInfo  = attributeTableView.getSelectionModel().getSelectedItem().getValue();
            if(!selectedInfo.isNodeInfo())
                return;
            NodeInfo info = selectedInfo.getNodeInfoOrNull();
            if(info.isNTA() && !info.isParametrized()){ //Handle the non-parametrized NTA:s
                Object obj = mon.getApi().compute(node.getNode(), info);
                if(!printToConsole(node, obj))
                    return;
                setAttributeList(node, false);
                mon.getApi().buildFilteredTree();
                mon.getController().updateUI();
                return;
            }

            AttributeInputDialog dialog = new AttributeInputDialog(info, node, mon);
            dialog.init();
            dialog.setOnCloseRequest(event -> {
                if(dialog.invokeButtonPressed()) {
                    Object[] result = dialog.getResult();
                    if(result == null ||  mon.getLastRealNode() == null)
                        return;
                    Object value = mon.getApi().compute(dialog.getTreeNode().getNode(), dialog.getInfo(), result);
                    if(!printToConsole(node, value))
                        return;

                    if(info.isNTA()){
                        mon.getApi().buildFilteredTree();
                        mon.getController().updateUI();
                    }
                    setAttributeList(node, false);
                }
                mon.getController().nodeSelected(dialog.getTreeNode(), false);
            });
            dialog.show();
        });
        mouseMenu.getItems().add(cmItem1);
    }

    private boolean printToConsole(TreeNode node, Object value){
        if(!node.getNode().getNodeContent().noErrors()) {
            mon.getController().addErrors(node.getNode().getNodeContent().getInvocationErrors());
            mon.getController().addErrors(mon.getApi().getErrors(ASTAPI.INVOCATION_ERROR));
            mon.getController().addWarnings(mon.getApi().getWarnings(ASTAPI.INVOCATION_WARNING));
            mon.getController().addMessage("Computation unsuccessful");
            return false;
        }

        mon.getController().addWarnings(mon.getApi().getWarnings(ASTAPI.INVOCATION_WARNING));
        mon.getController().addMessage("Computation successful : " + value);
        return true;
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
            attributeTableView.getRoot().getChildren().clear();
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
        //mon.getController().addMessage("--------- new ----------");
        Node n = node.getNode();
        if(compute) {
            mon.getController().addErrors(mon.getApi().compute(n));
            attributeTableView.getSelectionModel().clearSelection();
        }
        TreeItem<NodeInfoInterface> mainParent = new TreeItem<>(null);
        //final TreeTableView<String> treeTableView = new TreeTableView<>(mainParent);
        attributeTableView.setRoot(mainParent);
        attributeTableView.setShowRoot(false);
        mainParent.setExpanded(true);

        Collection<NodeInfo> attr = n.getNodeContent().getAttributes();
        Collection<NodeInfo> ntas = n.getNodeContent().getNTAs();
        Collection<NodeInfo> tokens = n.getNodeContent().getTokens();

        if(attr.size() > 0) {
            addSectionToAttributeList("Attributes", n.getNodeContent().getAttributes(), mainParent);
        }

        if(ntas.size() > 0) {
            addSectionToAttributeList("Nonterminal attributes", n.getNodeContent().getNTAs(), mainParent);
        }

        if(tokens.size() > 0) {
            addSectionToAttributeList("Tokens", n.getNodeContent().getTokens(), mainParent);
        }

        mon.getController().addMessage("index: " + indexOfSelectedAttribute);
        attributeTableView.getSelectionModel().select(indexOfSelectedAttribute);
        TreeItem<NodeInfoInterface> selected = attributeTableView.getSelectionModel().getSelectedItem();

        // Below code is for setting the selected position to the last computed value, in case the selected row
        // is a parameterized attribute
        if(selected == null)
            return;
        if(!selected.getValue().isNodeInfo())
            return;
        NodeInfo info = selected.getValue().getNodeInfoOrNull();
        if(!info.isAttribute() && !info.isParametrized())
            return;
        Attribute attribute = (Attribute)info;
        int i = 0;
        for(Map.Entry<String, Object> entry : attribute.getComputedEntry()){
            i++;
            if(entry.getKey().equals(attribute.getLastComputedkey())) {
                indexOfSelectedAttribute += i;
                attributeTableView.getSelectionModel().select(indexOfSelectedAttribute);
                return;
            }
        }


    }

    /**
     * Add all NodeInfos from list to the parent with the name label.
     * @param label
     * @param list
     * @param parent
     */
    private void addSectionToAttributeList(String label, Collection<NodeInfo> list, TreeItem parent){
        NodeInfoInterface fucker = new NodeInfoLabel(label);
        TreeItem<NodeInfoInterface> tokensItem = new TreeItem<>(fucker);
        tokensItem.setExpanded(true);

        // go through all methods and sort them on kind
        HashMap<String, ArrayList<NodeInfo>> methods = new HashMap<>();
        for (NodeInfo info : list) {
            if(!methods.containsKey(info.getKindString())){
                methods.put(info.getKindString(), new ArrayList<>());
            }
            methods.get(info.getKindString()).add(info);
        }

        // Add all methods to the list
        for(Map.Entry<String, ArrayList<NodeInfo>> entry : methods.entrySet()){
            if(entry.getValue().size() > 0) {

                TreeItem<NodeInfoInterface> kindLabel = new TreeItem<>(new NodeInfoLabel(entry.getKey()));
                kindLabel.setExpanded(true);
                tokensItem.getChildren().add(kindLabel);

                for (NodeInfo info : entry.getValue()) {
                    TreeItem<NodeInfoInterface> methodItem = new TreeItem<>(new NodeInfoHolder(info));
                    kindLabel.getChildren().add(methodItem);
                    if(!info.isAttribute() || !info.isParametrized()){
                        continue;
                    }
                    Attribute attr = (Attribute) info;
                    methodItem.setExpanded(true);
                    for(Map.Entry<String, Object> computedEntry : attr.getComputedEntry()){
                        String name = "";
                        Object[] params = attr.getUsedParameters().get(computedEntry.getKey());
                        int i = params.length;
                        for(Object param : params){
                            name += param == null ? "null" : param.toString();
                            if(--i > 0){
                                name += ", ";
                            }
                        }
                        TreeItem<NodeInfoInterface> computedItem = new TreeItem<>(new NodeInfoParameter(name, computedEntry.getValue(), info));
                        methodItem.getChildren().add(computedItem);
                    }
                }
            }
        }
        parent.getChildren().add(tokensItem);
    }

    /**
     * Listener for the attribute treetableview, sets the attributeInfoTableView for the selected attribute.
     * It will also remove old references, and add new references if the value of selected attribute is a AST node.
     * Also tells the main controller that an attribute was selected.
     * @param observable
     * @param oldValue
     * @param newValue
     */
    @Override
    public void changed(ObservableValue<? extends TreeItem<NodeInfoInterface>> observable, TreeItem<NodeInfoInterface> oldValue, TreeItem<NodeInfoInterface> newValue) {
        if(newValue != null) {
            indexOfSelectedAttribute = attributeTableView.getSelectionModel().getSelectedIndex();
            NodeInfoInterface infoHolder = newValue.getValue();
            NodeInfo info = null;
            Object value = null;
            if(infoHolder.isNodeInfo()) {
                info = newValue.getValue().getNodeInfoOrNull();
                value = info.getValue();
            } else if(infoHolder.isParameter()){
                NodeInfoParameter paramHolder = ((NodeInfoParameter)newValue.getValue());
                info = paramHolder.info;
                value = paramHolder.getValue();
            }
            setAttributeInfo(info);
            mon.setSelectedInfo(info);
            mon.getController().attributeInNodeSelected(info);
            setReference(value);

        }else{
            setAttributeInfo(null);
            setReference(null);
        }

        if(oldValue != null && oldValue.getValue() != null) {
            if(oldValue.getValue().isNodeInfo())
                mon.getApi().getNodeReferencesAndHighlightThem(oldValue.getValue().getNodeInfoOrNull().getValue(), false);
            else if(oldValue.getValue().isParameter())
                mon.getApi().getNodeReferencesAndHighlightThem((oldValue.getValue()).getValue(), false);
        }
    }

    /**
     * Fill the attributeInfoTableView with information about the attribute
     * @param info
     */
    private void setAttributeInfo(NodeInfo info){
        if(info == null ) {
            attributeInfoLabel.setText("");
            attributeInfoTableView.getItems().clear();
            return;
        }
        attributeInfoLabel.setText(info.print());
        attributeInfoTableView.setItems(FXCollections.observableArrayList(AttributeInfo.toArray(info.getInfo())));
    }

    /**
     * Adds references from the selected node in the monitor and the values found in the AttributeInfo supplied.
     * @param value
     */
    public void setReference(Object value){
        ArrayList<GenericTreeNode> newRefs = null;
        if(value != null)
            newRefs = mon.getApi().getNodeReferencesAndHighlightThem(value, true);
        graphView.setReferenceEdges(newRefs, mon.getSelectedNode());
    }

    /**
     * Class for the cells in the tableviews
     */
    private class AttributeValueCell extends TreeTableCell<NodeInfoInterface, Object>{
        @Override
        protected void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);

            if (getTreeTableRow().getItem() == null || empty) {
                setText(null);
                return;
            }

            setText(String.valueOf(item));
            setStyle("-fx-text-fill:#ffffff;");

            NodeInfo info = (getTreeTableRow().getItem()).getNodeInfoOrNull();
            if(info == null)
                return;
            if(info == null || item != null)
                return;
            if (info.isParametrized() || info.isNTA()) {
                setText("right click to compute");
                setStyle("-fx-text-fill:#999999;");
            }else{

            }

            setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    boolean yep = (info.isNTA() || info.isParametrized()) && info.getValue() == null;
                    if(getTreeTableRow().getItem() != null &&
                            (yep && !mon.isFunctionRunning())) {
                        mouseMenu.show(this, event.getScreenX(), event.getScreenY());
                    }
                }
            });
        }
    }


}
