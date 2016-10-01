package drast.views.gui.controllers;

import drast.Log;
import drast.export.DotExport;
import drast.export.XMLExport;
import drast.model.DrASTSettings;
import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.GUIData;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by gda10jth on 11/20/15.
 * <p>
 * Controller for the main menu.
 */
public class TopMenuController implements Initializable, ControllerInterface {
  private GUIData mon;

  @FXML private Menu topMenuFileMenu;
  @FXML private Menu topMenuExportMenu;
  @FXML private MenuItem exitMenuItem;
  @FXML private MenuItem toggleMinimizeMenuItem;
  @FXML private MenuItem openMenuItem;
  @FXML private CheckMenuItem showEdgesCheckMenuItem;
  @FXML private CheckMenuItem showNodesCheckMenuItem;
  @FXML private CheckMenuItem niceLookingEdgesCheckMenuItem;
  @FXML private CheckMenuItem dynamicValuesCheckMenuItem;
  @FXML private CheckMenuItem ntaCachedCheckMenuItem;
  @FXML private CheckMenuItem ntaComputedCheckMenuItem;

  @Override public void init(GUIData mon) {
    this.mon = mon;
    setValuesOnMenuItems();
    mon.getController().getGraphViewTabController()
        .setNiceEdges(niceLookingEdgesCheckMenuItem.isSelected());
  }

  private void setValuesOnMenuItems() {
    showEdgesCheckMenuItem.setSelected(DrASTSettings.getFlag(DrASTSettings.SHOW_EDGES));
    showNodesCheckMenuItem.setSelected(DrASTSettings.getFlag(DrASTSettings.SHOW_NODES));
    niceLookingEdgesCheckMenuItem.setSelected(DrASTSettings.getFlag(DrASTSettings.CURVED_EDGES));
    dynamicValuesCheckMenuItem.setSelected(DrASTSettings.getFlag(DrASTSettings.DYNAMIC_VALUES));
    ntaCachedCheckMenuItem.setSelected(DrASTSettings.getFlag(DrASTSettings.NTA_CACHED));
    ntaComputedCheckMenuItem.setSelected(DrASTSettings.getFlag(DrASTSettings.NTA_COMPUTED));
  }

  /**
   * Initialize all buttons in the menu.
   */
  @Override public void initialize(URL location, ResourceBundle resources) {
    exitMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
    exitMenuItem.setOnAction(event -> mon.getController().exitProgram());

    openMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
    openMenuItem.setOnAction(event -> mon.getController().loadFile());

    toggleMinimizeMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F));
    toggleMinimizeMenuItem.setOnAction(e -> mon.getController().toggleMinimizeWindows());

    showEdgesCheckMenuItem.setOnAction(e -> {
      saveConfig(DrASTSettings.SHOW_EDGES, showEdgesCheckMenuItem);
      mon.getController().getGraphViewTabController().repaintGraph();

    });

    showNodesCheckMenuItem.setOnAction(e -> {
      saveConfig(DrASTSettings.SHOW_NODES, showNodesCheckMenuItem);
      mon.getController().getGraphViewTabController().repaintGraph();
    });

    niceLookingEdgesCheckMenuItem.setOnAction(e -> {
      saveConfig(DrASTSettings.CURVED_EDGES, niceLookingEdgesCheckMenuItem);
      mon.getController().getGraphViewTabController()
          .setNiceEdges(niceLookingEdgesCheckMenuItem.isSelected());
    });

    dynamicValuesCheckMenuItem.setOnAction(e -> {
      saveConfig(DrASTSettings.DYNAMIC_VALUES, dynamicValuesCheckMenuItem);
      mon.getController().nodeSelected(mon.getSelectedNode(), this);
    });

    ntaCachedCheckMenuItem.setOnAction(e -> {
      saveConfig(DrASTSettings.NTA_CACHED, ntaCachedCheckMenuItem);
      mon.getController().saveFilter();
    });

    ntaComputedCheckMenuItem.setOnAction(e -> {
      saveConfig(DrASTSettings.NTA_COMPUTED, ntaComputedCheckMenuItem);
      mon.getController().saveFilter();
    });

    MenuItem exportXml = new MenuItem("XML");
    exportXml.setOnAction(event -> {
      String fileName = "graph_to_xml" + new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
      File dirPath = openDirectoryBrowser("Choose XML location", fileName);
      if (dirPath == null) {
        return;
      }

      String path =
          String.format("%s%s%s.xml", dirPath.getParent(), File.separator, dirPath.getName());
      boolean success = XMLExport.export(mon.getDrASTAPI(), path);
      if (success) {
        Log.info("XML file saved to: %s", new File(path).getAbsolutePath());
      } else {
        Log.error("XML file was not generated.");
      }
    });

    MenuItem exportDot = new MenuItem("GraphViz Dot");
    exportDot.setOnAction(event -> {
      String fileName = "graph_to_dot" + new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
      File dirPath = openDirectoryBrowser("Dot Output File", fileName);
      if (dirPath == null) {
        return;
      }

      String path =
          String.format("%s%s%s.dot", dirPath.getParent(), File.separator, dirPath.getName());
      boolean success = DotExport.export(mon.getDrASTAPI(), path);
      if (success) {
        Log.info("Dotfile saved to: %s", new File(path).getAbsolutePath());
      } else {
        Log.error("Dotfile was not generated.");
      }
    });

    Menu imageMenu = new Menu("Image (png)");
    MenuItem exportImage = new MenuItem("Whole graph");
    exportImage.setOnAction(event -> {
      String fileName = "whole_graph_" + new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
      File dirPath = openDirectoryBrowser("Choose image location", fileName);
      if (dirPath == null) {
        return;
      }

      String absPath = mon.getGraphView()
          .saveGraphAsImage(dirPath.getParent() + File.separator, dirPath.getName());
      if (absPath.length() > 1) {
        Log.info("Image saved to: " + absPath);
      }
    });
    MenuItem exportImagePrintScreen = new MenuItem("On screen");
    exportImagePrintScreen.setOnAction(event -> {
      String fileName = "screen_graph_" + new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
      File dirPath = openDirectoryBrowser("Choose image location", fileName);
      if (dirPath == null) {
        return;
      }

      String absPath = mon.getGraphView()
          .savePrintScreenGraph(dirPath.getParent() + File.separator, dirPath.getName());
      if (absPath.length() > 1) {
        Log.info("Image saved to: " + absPath);
      }
    });
    topMenuExportMenu.getItems().addAll(exportXml, exportDot, imageMenu);
    imageMenu.getItems().add(exportImage);
    imageMenu.getItems().add(exportImagePrintScreen);

  }

  private void saveConfig(String key, CheckMenuItem Value) {
    DrASTSettings.put(key, Value.isSelected() ? "1" : "0");
  }

  private File openDirectoryBrowser(String title, String fileName) {
    FileChooser chooser = new FileChooser();
    chooser.setTitle(title);
    chooser.setInitialFileName(fileName);
    chooser.setInitialDirectory(mon.getDefaultSettingsDirectory());
    return chooser.showSaveDialog(mon.getStage());
  }

  @Override public void nodeSelected(GenericTreeNode node) {
  }

  @Override public void nodeDeselected() {
  }

  @Override public void updateGUI() {
  }

  @Override public void onSetRoot() {
    setValuesOnMenuItems();
  }
}
