package uicomponent;

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
    private static Node root;
    private static UIMonitor mon;

    public UIComponent() {} // This one is used by Application

    public UIComponent(Node root) {
        this.mon = new UIMonitor(root);
        this.root = root;
        launch(new String[0]);
    }

    @Override
    public void start (Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent a = loader.load(getClass().getResource("/sample.fxml"));
        ScrollPane center = (ScrollPane) a.lookup("#graphView");
        Controller controller = loader.<Controller>getController();
        System.out.println(controller);
        center.setContent(new GraphView(mon));
        stage.setTitle("JastAddDebugger");
        stage.setScene(new Scene(a,1000,1000));
        stage.setMaximized(true);
        stage.show();
    }
}