package drast.views.gui.controllers;

import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.GUIData;
import drast.views.gui.graph.GraphView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by gda10jth on 2/3/16.
 * <p>
 * Controller for the graph view and the navigation menu above it.
 */
public class GraphViewController implements Initializable, ControllerInterface {
  private GUIData mon;
  private GraphView graphView;

  @FXML private Button zoomInButton;
  @FXML private Button zoomOutButton;
  @FXML private Button showRootNodeButton;
  @FXML private Button showSelectedNodeButton;
  @FXML private Button showWholeGraphButton;
  @FXML private Button autoLayoutGraphButton;

  /**
   * Initialize all buttons in the menu.
   */
  @Override public void initialize(URL location, ResourceBundle resources) {
    showSelectedNodeButton.setOnAction(click -> {
      if (mon.getSelectedNode() != null) {
        graphView.panToNode(mon.getSelectedNode());
      }
    });

    showRootNodeButton.setOnAction(click -> graphView.panToNode(mon.getRootNode()));

    showWholeGraphButton.setOnAction(click -> graphView.showWholeGraphOnScreen());

    autoLayoutGraphButton.setOnAction(click -> graphView.updateGraph());

    zoomInButton.setOnMouseClicked(e -> graphView.zoomIn());

    zoomOutButton.setOnMouseClicked(e -> graphView.zoomOut());

  }

  public void setNiceEdges(boolean niceEdges) {
    if (graphView != null) {
      graphView.setNiceEdges(niceEdges);
    }
  }

  public void repaintGraph() {
    graphView.repaint();
  }

  @Override public void init(GUIData mon) {
    this.mon = mon;
    this.graphView = mon.getGraphView();
    graphView.setMyController(this);
  }

  public void graphIsLoading() {
    if (graphView != null) {
      graphView.setCursor(Cursor.WAIT);
    }
  }

  public void graphIsDone() {
    if (graphView != null) {
      graphView.setCursor(Cursor.DEFAULT);
    }
  }

  @Override public void nodeSelected(GenericTreeNode node) {
    graphView.setSelectedNode(node);
  }

  @Override public void nodeDeselected() {
    graphView.deselectNode();
  }

  @Override public void updateGUI() {
    graphView.updateGraph();
  }

  @Override public void onSetRoot() {
  }
}
