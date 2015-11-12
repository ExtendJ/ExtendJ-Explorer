package uicomponent;

import jastaddad.ASTAPI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import uicomponent.controllers.Controller;
import uicomponent.graph.GraphView;

import java.io.IOException;
import java.lang.reflect.Field;

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
        Parent a = loader.load(getClass().getResource("/main.fxml").openStream());
        con = loader.<Controller>getController();
        mon.setController(con);
        GraphView graphview = new GraphView(mon);
        con.init(mon, graphview, this);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setTitle("JastAddDebugger " + ASTAPI.VERSION);
        stage.setScene(new Scene(a, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight()));
        stage.setMaximized(true);

        stage.show();

        ScrollPane center = (ScrollPane) a.lookup("#graphView");
        center.setContent(graphview);
    }
}