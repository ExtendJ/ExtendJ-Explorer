package uicomponent;

import jastaddad.ASTAPI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import uicomponent.controllers.Controller;
import uicomponent.graph.GraphView;

import java.io.IOException;
import java.net.URL;

public class UIComponent extends Application {

    private static UIMonitor mon;
    private static ASTAPI api;
    private static Controller con;

    public UIComponent() {} // This one is used by Application

    public UIComponent(ASTAPI api) {
        this.api = api;
        this.mon = new UIMonitor(api);
        launch(new String[0]);
    }

    @Override
    public void start (Stage stage) throws IOException {
        //URL location=getClass().getResource("/sample.fxml");
        FXMLLoader loader = new FXMLLoader();
        //loader.setLocation(location);
        Parent a = loader.load(getClass().getResource("/sample.fxml").openStream());
        con = loader.<Controller>getController();
        mon.setController(con);
        GraphView graphview = new GraphView(mon);
        con.init(mon, graphview, this);


        stage.setTitle("JastAddDebugger");
        stage.setScene(new Scene(a, 1000, 1000));
        stage.setMaximized(true);
        stage.show();

        ScrollPane center = (ScrollPane) a.lookup("#graphView");
        center.setContent(graphview);
    }



}