package jastaddad.ui.controllers;

import jastaddad.api.filteredtree.GenericTreeNode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import jastaddad.ui.UIMonitor;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Created by gda10jth on 11/2/15.
 */
public class TextTreeTabController implements Initializable, ChangeListener {
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

    @Override
     public void changed(ObservableValue observable, Object oldValue,
                         Object newValue) {

        TreeItem<GenericTreeNode> selectedItem = (TreeItem<GenericTreeNode>) newValue;
        if(selectedItem != null && !ignoreChange)
            mon.getController().nodeSelected(selectedItem.getValue(), false);
        ignoreChange = false;
        // do what ever you want
    }

    private void loadGraphTreeView(){
        TreeItem<GenericTreeNode> root = new TreeItem<>(mon.getRootNode());
        createTree(root);
        graphTreeView.setRoot(root);
        graphTreeView.setShowRoot(true);
    }

    private void createTree(TreeItem<GenericTreeNode> parent){
        GenericTreeNode parentGNode = parent.getValue();
        for (GenericTreeNode child : parentGNode.getChildren()) {

            TreeItem<GenericTreeNode> childItem = new TreeItem<>(child);
            parent.getChildren().add(childItem);
            nodeToItemRef.put(child, childItem);
            createTree(childItem);
        }
    }

    public void newNodeSelected(GenericTreeNode node){
        ignoreChange = true;
        graphTreeView.getSelectionModel().select(nodeToItemRef.get(node));
    }

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
