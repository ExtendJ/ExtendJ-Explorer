package uicomponent.controllers;

import jastaddad.filteredtree.GenericTreeNode;
import jastaddad.filteredtree.TreeNode;
import jastaddad.objectinfo.NodeContent;
import jastaddad.objectinfo.NodeInfo;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.VBox;
import uicomponent.UIComponent;
import uicomponent.UIMonitor;
import uicomponent.graph.GraphView;

import javax.swing.*;
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
public class Controller implements Initializable {
    @FXML private VBox attributeTab;
    @FXML private AttributeTabController attributeTabController;
    @FXML private ScrollPane textTreeTab;
    @FXML private TextTreeTabController textTreeTabController;

    @FXML
    private TabPane graphViewTabs;

    @FXML
    private Button saveNewFilterButton;

    @FXML
    private TreeView<TmpTreeItem> typeListView;

    private UIMonitor mon;
    private GraphView graphView;

    @FXML
    private TextArea filteredConfigTextArea;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void init(UIMonitor mon, GraphView graphView, UIComponent uiComponent) throws IOException {
        this.mon = mon;
        this.graphView = graphView;

        attributeTabController.init(mon, graphView);
        textTreeTabController.init(mon);

        loadClassTreeView();
        loadFilterFileText();

        saveNewFilterButton.setOnAction((event) -> {
            mon.getApi().saveNewFilter(filteredConfigTextArea.getText());
            graphView.updateGraph();
            textTreeTabController.updateTree();
            if(mon.getSelectedNode() != null) {
                Platform.runLater(() -> textTreeTabController.newNodeSelected(mon.getSelectedNode()));
            }
            resetUI();
        });

        graphViewTabs.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> {
                    if(t1.getId().equals("graphViewTabNode")){
                        Platform.runLater(() -> {
                            graphView.repaint(); graphView.requestFocus();
                        });
                    } else if(t1.getId().equals("treeViewTabNode")){

                    }
                }
        );
    }

    public void newNodeSelected(GenericTreeNode node, boolean fromGraph){
        mon.setSelectedNode(node);
        mon.setReferenceNode(null);
        attributeTabController.setAttributeList();
        if(fromGraph)
            textTreeTabController.newNodeSelected(node);
        else
            graphView.newNodeSelected(node);
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

    private void loadClassTreeView(){
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
