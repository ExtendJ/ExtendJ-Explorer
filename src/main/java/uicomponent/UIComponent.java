package uicomponent;

import jastaddad.ASTAPI;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import jastaddad.Node;
import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import uicomponent.controllers.Controller;
import uicomponent.graph.GraphView;

import java.io.IOException;

public class UIComponent extends Application {

    private static UIMonitor mon;
    private static ASTAPI api;
    private static Controller con;

    public UIComponent() {} // This one is used by Application

    public UIComponent(ASTAPI api) {
        this.api = api;
        this.mon = new UIMonitor(api.filteredTree);
        launch(new String[0]);
    }

    @Override
    public void start (Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent a = loader.load(getClass().getResource("/sample.fxml").openStream());
        ScrollPane center = (ScrollPane) a.lookup("#graphView");
        con = loader.<Controller>getController();
        con.setMonitor(mon);
        //center.setContent(new GraphView(mon, con));
        stage.setTitle("JastAddDebugger");
        stage.setScene(new Scene(a, 1000, 1000));
        stage.setMaximized(true);
        stage.show();
    }

}