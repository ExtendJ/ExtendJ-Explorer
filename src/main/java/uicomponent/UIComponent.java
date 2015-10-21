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

public class UIComponent extends Application {

    private static UIMonitor mon;
    private static ASTAPI api;
    private static Controller con;

    public UIComponent() {} // This one is used by Application

    public UIComponent(ASTAPI api) {
        this.api = api;
        this.mon = new UIMonitor(api.getFilteredTree(), api);
        launch(new String[0]);
    }

    @Override
    public void start (Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent a = loader.load(getClass().getResource("/sample.fxml").openStream());

        con = loader.<Controller>getController();
        con.init(mon);

        stage.setTitle("JastAddDebugger");
        stage.setScene(new Scene(a, 1000, 1000));
        stage.setMaximized(true);
        stage.show();

        ScrollPane center = (ScrollPane) a.lookup("#graphView");
        if(center == null)
            System.out.println("NULL");
        center.setContent(new GraphView(mon, con));


    }

}