package uicomponent.controllers;

import jastaddad.filteredtree.GenericTreeNode;
import jastaddad.filteredtree.TreeCluster;
import jastaddad.filteredtree.TreeNode;
import jastaddad.objectinfo.NodeContent;
import jastaddad.objectinfo.NodeInfo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import uicomponent.UIMonitor;
import uicomponent.graph.GraphView;

import java.awt.event.ItemEvent;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by gda10jth on 10/16/15.
 */
public class Controller implements Initializable, ChangeListener<NodeInfo> {
    @FXML
    private Button saveNewFilterButton;

    @FXML
    private ListView listView;

    @FXML
    private VBox vBoxFilterTab;

    @FXML
    private TreeView<TmpTreeItem> typeListView;

    private UIMonitor mon;
    private GraphView graphView;

    @FXML
    private TextArea filteredConfigTextArea;

    @FXML
    private ScrollPane scrollPaneClassFilter;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Attribute Listener
        listView.getSelectionModel().selectedItemProperty().addListener(this);
    }

    public void init(UIMonitor mon, GraphView graphView){
        this.mon = mon;
        this.graphView = graphView;
        loadTreeView();
        loadFilterFileText();
        saveNewFilterButton.setOnAction((event) -> {
            mon.getApi().saveNewFilter(filteredConfigTextArea.getText());
            graphView.updateGraph();
            resetUI();
        });
        vBoxFilterTab.setVgrow(scrollPaneClassFilter, Priority.ALWAYS);
    }

    private void loadFilterFileText() {
        String line;
        String textContent = "";
        int lineCount = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("filter.cfg"));
            while ((line = reader.readLine()) != null) {
                textContent += line + "\n";
                lineCount++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            textContent = "Can not read the configuration file!";
        }

        filteredConfigTextArea.setText(textContent);
        filteredConfigTextArea.setPrefColumnCount(lineCount);

    }

    private void resetUI(){
        GenericTreeNode node = getNode(mon.getSelectedNode());
        if(node == null)
            return;
        mon.setSelectedNode(node);
        graphView.setSelectedNode(node);
        node = getNode(mon.getReferenceNode());
        if(node == null)
            return;
        mon.setReferenceNode(node);
        node.setReferenceHighlight(true);
        graphView.setReferenceEdge(mon.getReferenceNode(), mon.getSelectedNode());

    }

    private GenericTreeNode getNode(GenericTreeNode node){
        if(node == null || !node.isNode())
            return null;
        TreeNode treeNode = ((TreeNode) node);
        return mon.getApi().getReferenceNode(treeNode.node.node);
    }

    private void loadTreeView(){
        ArrayList<TmpTreeItem> treeItems = new ArrayList<>();
        for (Map.Entry<String, Integer> config : mon.getApi().getTypeHash().entrySet()) {
            treeItems.add(new TmpTreeItem(config.getKey(), config.getValue()));
        }

        Collections.sort(treeItems);

        CheckBoxTreeItem<TmpTreeItem> root = new CheckBoxTreeItem<>(new TmpTreeItem("kuk", 1));
        root.setExpanded(true);

        CheckBoxTreeItem parent = root;
        for(TmpTreeItem item : treeItems){
            final CheckBoxTreeItem<TmpTreeItem> treeItem = new CheckBoxTreeItem<>(item);
            treeItem.setIndependent(true);
            treeItem.setSelected(item.enabled);

            treeItem.selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    mon.getApi().newTypeFiltered(treeItem.getValue().fullName, newValue);
                    graphView.updateGraph();
                }
            });

            if(item.name.length() <= 0) {
                parent.setExpanded(false);
                parent = treeItem;
                root.getChildren().add(treeItem);
            }else{
                parent.getChildren().add(treeItem);
            }
        }

        typeListView.setRoot(root);
        typeListView.setShowRoot(false);
        typeListView.setCellFactory(CheckBoxTreeCell.<TmpTreeItem>forTreeView());

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

    private void setAttributeList(){
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

    public void itemStateChanged(ItemEvent e){//Sets UI listeners of the graph
        Object subject = e.getItem();
        if(subject != null && subject instanceof TreeNode) {
            mon.setSelectedNode((TreeNode) subject);
            mon.setReferenceNode(null);
            setAttributeList();
        }
    }

    private class TmpTreeItem implements Comparable<TmpTreeItem>{
        public final String name;
        public final String className;
        public final String fullName;
        public final Boolean enabled;

        public TmpTreeItem(String name, int enabled){
            this.enabled = enabled == 1;
            String[] parts = name.split(":");
            className = parts[0];
            if(parts.length > 1) {
                this.name = parts[1];
                fullName = name;
            }else {
                this.name = "";
                fullName = className;
            }
        }

        @Override
        public String toString() {
            return name.length() <= 0 ? className : name;
        }

        public int compareTo(TmpTreeItem item) {
            return (fullName).compareTo(item.fullName) ;
        }
    }
}
