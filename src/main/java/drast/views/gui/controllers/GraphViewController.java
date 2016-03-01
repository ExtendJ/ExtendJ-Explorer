package drast.views.gui.controllers;

import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.Monitor;
import drast.views.gui.graph.GraphView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by gda10jth on 2/3/16.
 *
 * Controller for the graph view and the navigation menu above it.
 */
public class GraphViewController implements Initializable, ControllerInterface {
    private Monitor mon;
    private GraphView graphView;

    @FXML
    private Button zoomInButton;
    @FXML
    private Button zoomOutButton;
    @FXML
    private Button showRootNodeButton;
    @FXML
    private Button showSelectedNodeButton;
    @FXML
    private Button showWholeGraphButton;
    @FXML
    private Button autoLayoutGraphButton;
    /*
    @FXML
    private Button FPSCOUNTERTEST;
    */
    @FXML
    private ScrollPane graphViewScrollPane;


    /**
     * Initialize all buttons in the menu.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showSelectedNodeButton.setOnAction(click -> {
            if (mon.getSelectedNode() != null)
                graphView.panToNode(mon.getSelectedNode());
        });

        showRootNodeButton.setOnAction(click -> graphView.panToNode(mon.getRootNode()));

        showWholeGraphButton.setOnAction(click -> graphView.showWholeGraphOnScreen());

        autoLayoutGraphButton.setOnAction(click -> graphView.updateGraph());

        zoomInButton.setOnMouseClicked(e -> graphView.zoomIn());

        zoomOutButton.setOnMouseClicked(e-> graphView.zoomOut());

        /*
        FPSCOUNTERTEST.setOnAction(e->{
            System.out.println("GOGOOG");
            long start = System.currentTimeMillis();
            while((System.currentTimeMillis() - start) < 13000){
                graphView.repaint();
            }
        });
        */
    }

    public void setNiceEdges(boolean niceEdges){
        if(graphView != null)
            graphView.setNiceEdges(niceEdges);
    }

    public void repaintGraph(){
        graphView.repaint();
    }

    public void init(Monitor mon){
        this.mon = mon;
        this.graphView = mon.getGraphView();
        graphView.setMyController(this);
    }

    public void graphIsLoading(){
        if(graphView != null)
            graphView.setCursor(Cursor.WAIT);
    }

    public void graphIsDone(){
        if(graphView != null)
            graphView.setCursor(Cursor.DEFAULT);
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

    @Override
    public void nodeSelected(GenericTreeNode node) {
        graphView.setSelectedNode(node);
    }

    @Override
    public void nodeDeselected() {
        graphView.deselectNode();
    }

    @Override
    public void updateGUI() {
        graphView.updateGraph();
    }

    public void onNewAPI() {
    }
}
