package jastaddad.ui.controllers;

import jastaddad.api.filteredtree.GenericTreeNode;
import jastaddad.ui.UIMonitor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Controller for the tree view part of the UI.
 *
 * Created by gda10jth on 11/2/15.
 */
public class TreeViewTabController implements Initializable, ChangeListener {
    private UIMonitor mon;
    private HashMap<GenericTreeNode, TreeItem<GenericTreeNode>> nodeToItemRef;
    private boolean ignoreChange;

    @FXML
    private TreeView graphTreeView;

    public void init(UIMonitor mon){
        this.mon = mon;
        nodeToItemRef = new HashMap<>();
        ignoreChange = false;
        loadGraphTreeView();

        graphTreeView.getSelectionModel().selectedItemProperty().addListener(this);
    }

    /**
     * Called when a funciton starts from the Controller. A function can be a dialog.
     */
    public void functionStarted(){

    }

    /**
     * Called when a funciton stops from the Controller. A function can be a dialog.
     */
    public void functionStopped(){

    }

    /**
     * Called when something changes in the tree view, e.g. a node is selected.
     * Calls the nodeSelected(...) in the main controller Controller.
     * @param observable
     * @param oldValue
     * @param newValue
     */
    @Override
     public void changed(ObservableValue observable, Object oldValue,
                         Object newValue) {

        TreeItem<GenericTreeNode> selectedItem = (TreeItem<GenericTreeNode>) newValue;
        if(selectedItem != null && !ignoreChange)
            mon.getController().nodeSelected(selectedItem.getValue(), false);
        ignoreChange = false;
        // do what ever you want
    }

    /**
     * Create the the Tree view tree.
     */
    private void loadGraphTreeView(){
        TreeItem<GenericTreeNode> root = new TreeItem<>(mon.getRootNode());
        createTree(root);
        graphTreeView.setRoot(root);
        graphTreeView.setShowRoot(true);
    }

    /**
     * Recursively creates all the ui TreeItems that will be used by the treeView.
     * @param parent
     */
    private void createTree(TreeItem<GenericTreeNode> parent){
        GenericTreeNode parentGNode = parent.getValue();
        for (GenericTreeNode child : parentGNode.getChildren()) {
            TreeItem<GenericTreeNode> childItem = new TreeItem<>(child);
            parent.getChildren().add(childItem);
            nodeToItemRef.put(child, childItem);
            createTree(childItem);
        }
    }

    /**
     * Sets the selected vertex if the tree. This method is used if the selected node is defined by some other part
     * of the UI, e.g. the Graph view.
     * @param node
     */
    public void newNodeSelected(GenericTreeNode node){
        ignoreChange = true;
        graphTreeView.getSelectionModel().select(nodeToItemRef.get(node));
    }

    /**
     * Deselects all vertexes in the tree. This method is used if the selected node is defined by some other part
     * of the UI, e.g. the Graph view.
     */
    public void deselectNode(){
        graphTreeView.getSelectionModel().clearSelection();
    }

    public void updateTree(){
        TreeItem<GenericTreeNode> root = new TreeItem<>(mon.getRootNode());
        nodeToItemRef.clear();
        createTree(root);
        graphTreeView.setRoot(root);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {}


}
