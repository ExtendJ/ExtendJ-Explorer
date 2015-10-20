package uicomponent.controllers;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.picking.PickedState;
import jastaddad.Attributes;
import jastaddad.FilteredTreeNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import uicomponent.UIMonitor;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by gda10jth on 10/16/15.
 */
public class Controller implements Initializable {
    @FXML
    private ListView listView;
    @FXML
    private ListView typeListView;
    @FXML
    private ScrollPane scrollPane;

    @FXML protected void hideShowLeftPane(ActionEvent event) {
        System.out.println("godis");
    }

    private UIMonitor mon;

    public void setMonitor(UIMonitor mon){ this.mon = mon; }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    private void setAttributeList(){
        if(mon.getSelectedNode().isNode()) {
            Attributes a = mon.getSelectedNode().node.getAttributes();
            listView.setItems(FXCollections.observableList(a.getAttributes()));
        }
    }

    public void itemStateChanged(ItemEvent e){//Sets UI listeners of the graph
        Object subject = e.getItem();
        if(subject != null && subject instanceof FilteredTreeNode) {
            mon.setSelectedNode((FilteredTreeNode) subject);
            setAttributeList();
        }
    }

}
