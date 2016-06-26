package drast.views.gui.controllers;

import drast.model.Config;
import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.GUIConfig;
import drast.views.gui.Monitor;
import drast.views.gui.dialogs.OpenASTDialog;
import drast.views.xml.DrASTXML;
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
 *
 * Controller for the top menu.
 */
public class TopMenuController implements Initializable, ControllerInterface {
    private Monitor mon;
    private String prevJarPath;
    private String prevFilterPath;
    private String prevArgString;

    @FXML private Menu topMenuFileMenu;
    @FXML private Menu topMenuExportMenu;
    @FXML private MenuItem exitMenuItem;
    @FXML private MenuItem toggleMinimizeMenuItem;
    @FXML private MenuItem openMenuItem;
    @FXML private MenuItem rerunCompiler;
    @FXML private CheckMenuItem showEdgesCheckMenuItem;
    @FXML private CheckMenuItem showNodesCheckMenuItem;
    @FXML private CheckMenuItem niceLookingEdgesCheckMenuItem;
    @FXML private CheckMenuItem dynamicValuesCheckMenuItem;
    @FXML private CheckMenuItem ntaCachedCheckMenuItem;
    @FXML private CheckMenuItem ntaComputedCheckMenuItem;

    public void init(Monitor mon){
        this.mon = mon;
        prevJarPath = "";
        prevFilterPath = "";
        prevArgString = "";
        setValuesOnMenuItems();
        mon.getController().getGraphViewTabController().setNiceEdges(niceLookingEdgesCheckMenuItem.isSelected());
    }

    private void setValuesOnMenuItems(){
        Config config = mon.getConfig();
        showEdgesCheckMenuItem.setSelected(config.isEnabled(GUIConfig.SHOW_EDGES));
        showNodesCheckMenuItem.setSelected(config.isEnabled(GUIConfig.SHOW_NODES));
        niceLookingEdgesCheckMenuItem.setSelected(config.isEnabled(GUIConfig.NICE_EDGES));
        //Model Configs
        config = mon.getASTBrain().getConfig();
        dynamicValuesCheckMenuItem.setSelected(config.isEnabled(GUIConfig.DYNAMIC_VALUES));
        ntaCachedCheckMenuItem.setSelected(config.isEnabled(GUIConfig.NTA_CACHED));
        ntaComputedCheckMenuItem.setSelected(config.isEnabled(GUIConfig.NTA_COMPUTED));
    }

    /**
     * Initialize all buttons in the menu.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exitMenuItem.setOnAction(event1 -> {
            mon.getController().exitProgram();
        });

        openMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        openMenuItem.setOnAction(event1 -> {
            OpenASTDialog test = new OpenASTDialog(mon);
            test.init();
            test.show();
        });

        toggleMinimizeMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F));
        toggleMinimizeMenuItem.setOnAction(e -> mon.getController().toggleMinimizeWindows());

        rerunCompiler.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));
        rerunCompiler.setOnAction(e->{
            if(mon.isReRunnable()){
                mon.getController().runCompiler(mon.getDrASTUI(), prevJarPath, prevFilterPath, prevArgString.split(" "));
            }
        });

        showEdgesCheckMenuItem.setOnAction(e->{
            saveConfig(GUIConfig.SHOW_EDGES, showEdgesCheckMenuItem, mon.getConfig());
            mon.getController().getGraphViewTabController().repaintGraph();

        });

        showNodesCheckMenuItem.setOnAction(e->{
            saveConfig(GUIConfig.SHOW_NODES, showNodesCheckMenuItem, mon.getConfig());
            mon.getController().getGraphViewTabController().repaintGraph();
        });

        niceLookingEdgesCheckMenuItem.setOnAction(e->{
            saveConfig(GUIConfig.NICE_EDGES, niceLookingEdgesCheckMenuItem, mon.getConfig());
            mon.getController().getGraphViewTabController().setNiceEdges(niceLookingEdgesCheckMenuItem.isSelected());
        });

        dynamicValuesCheckMenuItem.setOnAction(e->{
            saveConfig(GUIConfig.DYNAMIC_VALUES, dynamicValuesCheckMenuItem, mon.getASTBrain().getConfig());
            mon.getController().nodeSelected(mon.getSelectedNode(), this);
        });

        ntaCachedCheckMenuItem.setOnAction(e->{
            saveConfig(GUIConfig.NTA_CACHED, ntaCachedCheckMenuItem, mon.getASTBrain().getConfig());
            mon.getController().saveNewFilter();
        });

        ntaComputedCheckMenuItem.setOnAction(e->{
            saveConfig(GUIConfig.NTA_COMPUTED, ntaComputedCheckMenuItem, mon.getASTBrain().getConfig());
            mon.getController().saveNewFilter();
        });

        MenuItem exportXml = new MenuItem("XML");
        exportXml.setOnAction(event -> {
            String fileName = "graph_to_xml" + new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
            File dirPath = openDirectoryBrowser("Choose XML location", fileName);
            if(dirPath == null) return;

            DrASTXML xmlBuilder = new DrASTXML(mon.getDrASTAPI());
            boolean success = xmlBuilder.printXml(dirPath.getParent() + dirPath.separator, dirPath.getName(), ".xml");
            if(success)
                mon.getController().addMessage("XML file saved to: " + xmlBuilder.getAbsoluteFilePath());
            else
                mon.getController().addError("XML file where not genereated.");
        });

        Menu imageMenu = new Menu("Image (png)");
        MenuItem exportImage = new MenuItem("Whole graph");
        exportImage.setOnAction(event -> {
            String fileName = "whole_graph_" + new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
            File dirPath = openDirectoryBrowser("Choose image location", fileName);
            if(dirPath == null) return;

            String absPath = mon.getGraphView().saveGraphAsImage(dirPath.getParent() + dirPath.separator, dirPath.getName(), "png");
            if(absPath.length() > 1) {
                mon.getController().addMessage("Image saved to: " + absPath);
            }
        });
        MenuItem exportImagePrintScreen = new MenuItem("On screen");
        exportImagePrintScreen.setOnAction(event -> {
            String fileName = "screen_graph_" + new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
            File dirPath = openDirectoryBrowser("Choose image location", fileName);
            if(dirPath == null) return;

            String absPath = mon.getGraphView().savePrintScreenGraph(dirPath.getParent() + dirPath.separator, dirPath.getName(), "png");
            if(absPath.length() > 1) {
                mon.getController().addMessage("Image saved to: " + absPath);
            }
        });
        topMenuExportMenu.getItems().add(exportXml);
        topMenuExportMenu.getItems().add(imageMenu);
        imageMenu.getItems().add(exportImage);
        imageMenu.getItems().add(exportImagePrintScreen);

    }

    private void saveConfig(String key, CheckMenuItem Value, Config config){
        config.put(key, Value.isSelected() ? "1" : "0");
        config.saveConfigFile();
    }

    private File openDirectoryBrowser(String title, String fileName){
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.setInitialFileName(fileName);
        File defaultDirectory = new File(mon.getDefaultDirectory());
        chooser.setInitialDirectory(defaultDirectory);
        return chooser.showSaveDialog(mon.getStage());
    }

    /**
     * Called when a funciton starts from the Controller. A function can be a dialog.
     */
    @Override
    public void functionStarted(){}

    /**
     * Called when a funciton stops from the Controller. A function can be a dialog.
     */
    @Override
    public void functionStopped(){}

    @Override
    public void nodeSelected(GenericTreeNode node) {}

    @Override
    public void nodeDeselected() {}

    @Override
    public void updateGUI() {}

    @Override
    public void onApplicationClose(){}

    @Override
    public void onNewAPI() {
        prevJarPath = mon.getConfig().getOrEmpty(GUIConfig.PREV_JAR);
        prevFilterPath = mon.getConfig().getOrEmpty(GUIConfig.PREV_FILTER);
        prevArgString = mon.getConfig().getOrEmpty(GUIConfig.PREV_ARGS);
        setValuesOnMenuItems();
    }
}
