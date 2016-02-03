package drast.views.gui.controllers;

import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.Monitor;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Controller for the tree view part of the UI.
 *
 * Created by gda10jth on 11/2/15.
 */
public class TreeViewTabController implements Initializable, ChangeListener, ControllerInterface {
    private Monitor mon;
    private HashMap<GenericTreeNode, TreeItem<GenericTreeNode>> nodeToItemRef;
    private boolean ignoreChange;

    @FXML
    private TreeView treeView;

    public void init(Monitor mon){
        this.mon = mon;
        nodeToItemRef = new HashMap<>();
        ignoreChange = false;

        if(mon.getRootNode() != null)
            loadGraphTreeView();

        treeView.getSelectionModel().selectedItemProperty().addListener(this);
        treeView.setCellFactory(new Callback<TreeView, TreeCell>() {
            @Override
            public TreeCell call(TreeView param) {
                TreeCell cell = new TreeCell<GenericTreeNode>() {
                    @Override
                    protected void updateItem(GenericTreeNode item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty)
                            setText("");
                        else
                            setText(item.toTreeViewString());
                    }
                };
                return cell;
            }
        });
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
            mon.getController().nodeSelected(selectedItem.getValue(), this);
        ignoreChange = false;
        // do what ever you want
    }

    /**
     * Create the the Tree view tree.
     */
    private void loadGraphTreeView(){
        TreeItem<GenericTreeNode> root = new TreeItem<>(mon.getRootNode());
        createTree(root);
        treeView.setRoot(root);
        treeView.setShowRoot(true);
    }

    /**
     * Recursively creates all the gui TreeItems that will be used by the treeView.
     * @param parent
     */
    private void createTree(TreeItem<GenericTreeNode> parent){
        parent.setExpanded(true);
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
    public void nodeSelected(GenericTreeNode node){
        ignoreChange = true;
        treeView.getSelectionModel().select(nodeToItemRef.get(node));
    }

    /**
     * Deselects all vertexes in the tree. This method is used if the selected node is defined by some other part
     * of the UI, e.g. the Graph view.
     */
    public void nodeDeselected(){
        treeView.getSelectionModel().clearSelection();
    }

    @Override
    public void updateGUI() {
        updateTree();
        if (mon.getSelectedNode() != null) {
            Platform.runLater(() -> nodeSelected(mon.getSelectedNode()));
        }
    }

    public void updateTree(){
        TreeItem<GenericTreeNode> root = new TreeItem<>(mon.getRootNode());
        nodeToItemRef.clear();
        createTree(root);
        treeView.setRoot(root);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {}


    public void onNewAPI() {

    }
}
