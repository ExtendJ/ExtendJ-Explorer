package drast.views.gui.controllers;

import drast.DrASTSetup;
import drast.model.filteredtree.GenericTreeNode;
import drast.views.DrASTXML;
import drast.views.gui.dialogs.OpenASTDialog;
import drast.views.gui.Monitor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.DirectoryChooser;
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

    public void init(Monitor mon){
        this.mon = mon;
        prevJarPath = "";
        prevFilterPath = "";
        prevArgString = "";
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
            if(mon.isRerunable()){
                reRunCompiler();
            }
        });

        showEdgesCheckMenuItem.setOnAction(e->{
            mon.getConfig().put("showEdges", showEdgesCheckMenuItem.isSelected() ? "1" : "0");
            mon.getController().getGraphViewTabController().repaintGraph();
        });
        showNodesCheckMenuItem.setOnAction(e->{
            mon.getConfig().put("showNodes", showNodesCheckMenuItem.isSelected() ? "1" : "0");
            mon.getController().getGraphViewTabController().repaintGraph();
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

    private File openDirectoryBrowser(String title, String fileName){
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.setInitialFileName(fileName);
        File defaultDirectory = new File(mon.getDefaultDirectory());
        chooser.setInitialDirectory(defaultDirectory);
        return chooser.showSaveDialog(mon.getStage());
    }

    public void reRunCompiler(){
        DrASTSetup setup = new DrASTSetup(mon.getDrASTUI(), prevJarPath, prevFilterPath, prevArgString.split(" "));
        setup.run();
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

    }

    @Override
    public void nodeDeselected() {

    }

    @Override
    public void updateGUI() {

    }

    public void onNewAPI() {
        prevJarPath = mon.getConfig().getOrEmpty("prevJar");
        prevFilterPath = mon.getConfig().getOrEmpty("prevFilter");
        prevArgString = mon.getConfig().getOrEmpty("prevFullArgs");
    }
}
