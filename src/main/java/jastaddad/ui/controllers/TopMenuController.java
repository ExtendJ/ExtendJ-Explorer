package jastaddad.ui.controllers;

import jastaddad.tasks.JastAddAdXML;
import jastaddad.ui.UIMonitor;
import jastaddad.ui.graph.GraphView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by gda10jth on 11/20/15.
 */
public class TopMenuController implements Initializable {
    private UIMonitor mon;
    private GraphView graphView;

    @FXML private Menu topMenuFileMenu;
    @FXML private Menu topMenuExportMenu;
    @FXML private MenuItem exitMenuItem;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exitMenuItem.setOnAction(event1 -> {
            mon.getController().exitProgram();
        });

        MenuItem exportXml = new MenuItem("XML");
        exportXml.setOnAction(event -> {
            JastAddAdXML xmlBuilder = new JastAddAdXML(mon.getJastAddAdAPI());
            boolean success = xmlBuilder.printXml();
            if(success)
                mon.getController().addMessage("XML file saved to drive.");
            else
                mon.getController().addError("XML file where not genereated.");
        });

        MenuItem exportImage = new MenuItem("Image (png)");
        exportImage.setOnAction(event -> {
            mon.getGraphView().saveGraphAsImage(".png");
            mon.getController().addMessage("Image saved");
        });
        topMenuExportMenu.getItems().add(exportXml);
        topMenuExportMenu.getItems().add(exportImage);

    }

    public void init(UIMonitor mon, GraphView graphView){
        this.mon = mon;
        this.graphView = graphView;
    }
}
