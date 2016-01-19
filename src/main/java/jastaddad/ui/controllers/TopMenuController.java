package jastaddad.ui.controllers;

import jastaddad.JastAddAdSetup;
import jastaddad.tasks.JastAddAdXML;
import jastaddad.ui.dialogs.OpenASTDialog;
import jastaddad.ui.UIMonitor;
import jastaddad.ui.graph.GraphView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;

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
public class TopMenuController implements Initializable {
    private UIMonitor mon;
    private GraphView graphView;
    private String prevJarPath;
    private String prevFilterPath;
    private String prevArgString;

    @FXML private Menu topMenuFileMenu;
    @FXML private Menu topMenuExportMenu;
    @FXML private MenuItem exitMenuItem;
    @FXML private MenuItem toggleMinimizeMenuItem;
    @FXML private MenuItem openMenuItem;
    @FXML private MenuItem rerunCompiler;

    public void init(UIMonitor mon, GraphView graphView){
        this.mon = mon;
        this.graphView = graphView;
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

        openMenuItem.setOnAction(event1 -> {
            OpenASTDialog test = new OpenASTDialog(mon);
            test.init();
            test.show();
        });

        toggleMinimizeMenuItem.setOnAction(e -> {
            mon.getController().toggleMinimizeWindows();
        });

        rerunCompiler.setOnAction(e->{
            if(mon.isRerunable()){
                JastAddAdSetup setup = new JastAddAdSetup(mon.getJastAddAdUI(), prevJarPath, prevFilterPath, prevArgString.split(" "));
                setup.run();
            }
        });

        MenuItem exportXml = new MenuItem("XML");
        exportXml.setOnAction(event -> {
            File dirPath = openDirectoryBrowser("Choose XML location");
            if(dirPath == null) return;

            JastAddAdXML xmlBuilder = new JastAddAdXML(mon.getJastAddAdAPI());
            boolean success = xmlBuilder.printXml(dirPath.getAbsolutePath());
            if(success)
                mon.getController().addMessage("XML file saved to: " + xmlBuilder.getAbsoluteFilePath());
            else
                mon.getController().addError("XML file where not genereated.");
        });

        Menu imageMenu = new Menu("Image (png)");
        MenuItem exportImage = new MenuItem("Whole graph");
        exportImage.setOnAction(event -> {
            File dirPath = openDirectoryBrowser("Choose image location");
            if(dirPath == null) return;

            String fileName = "whole_graph_" + new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
            String absPath = mon.getGraphView().saveGraphAsImage(dirPath.getAbsolutePath() + dirPath.separator, fileName, "png");
            if(absPath.length() > 1) {
                mon.getController().addMessage("Image saved to: " + absPath);
            }
        });
        MenuItem exportImagePrintScreen = new MenuItem("On screen");
        exportImagePrintScreen.setOnAction(event -> {
            File dirPath = openDirectoryBrowser("Choose image location");
            if(dirPath == null) return;
            String fileName = "screen_graph_" + new SimpleDateFormat("yyyyMMddhhmm").format(new Date());

            String absPath = mon.getGraphView().savePrintScreenGraph(dirPath.getAbsolutePath() + dirPath.separator, fileName, "png");
            if(absPath.length() > 1) {
                mon.getController().addMessage("Image saved to: " + absPath);
            }
        });
        topMenuExportMenu.getItems().add(exportXml);
        topMenuExportMenu.getItems().add(imageMenu);
        imageMenu.getItems().add(exportImage);
        imageMenu.getItems().add(exportImagePrintScreen);

    }

    private File openDirectoryBrowser(String title){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);
        File defaultDirectory = new File(mon.getDefaultDirectory());
        chooser.setInitialDirectory(defaultDirectory);
        return chooser.showDialog(mon.getStage());
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

    public void onNewAPI() {
        prevJarPath = mon.getConfig().getOrEmpty("prevJar");
        prevFilterPath = mon.getConfig().getOrEmpty("prevFilter");
        prevArgString = mon.getConfig().getOrEmpty("prevFullArgs");
    }
}
