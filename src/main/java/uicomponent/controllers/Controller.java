package uicomponent.controllers;

import jastaddad.FilteredTreeNode;
import jastaddad.objectinfo.NodeContent;
import jastaddad.objectinfo.NodeInfo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import uicomponent.UIMonitor;

import java.awt.event.ItemEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by gda10jth on 10/16/15.
 */
public class Controller implements Initializable {
    @FXML
    private ListView listView;

    @FXML
    private TreeView<TmpTreeItem> typeListView;

    private UIMonitor mon;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    public void init(UIMonitor mon){
        this.mon = mon;
        loadTreeView();
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
                    mon.getApi().newTypeFiltered(treeItem.getValue().fullName);
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

    private void setAttributeList(){
        if(!mon.getSelectedNode().isNode())
            return;
        NodeContent a = mon.getSelectedNode().node.getNodeContent();
        ArrayList<NodeInfo> al = a.toArray();  //Todo remove this when we change the UI, ie we add a proper node name label
        al.add(0, new NodeInfo(mon.getSelectedNode().node.toString(), "") {
            @Override
            public String print() {
                return "Node name: " + name;
            }
        });
        listView.setItems(FXCollections.observableList(al));
    }

    public void itemStateChanged(ItemEvent e){//Sets UI listeners of the graph
        Object subject = e.getItem();
        if(subject != null && subject instanceof FilteredTreeNode) {
            mon.setSelectedNode((FilteredTreeNode) subject);
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
            if(name.length() <= 0){
                return className;
            }
            return name;
        }

        public int compareTo(TmpTreeItem item) {
            return (className+":"+name).compareTo(item.className+":"+item.name) ;
        }
    }
}
