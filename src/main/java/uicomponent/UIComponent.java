package uicomponent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import jastaddad.Node;
import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import uicomponent.graph.GraphView;

import java.io.IOException;

public class UIComponent extends Application {
    private static Node root;

    public UIComponent() {} // This one is used by Application

    public UIComponent(Node root) {
        this.root = root;
        launch(new String[0]);
    }

    @Override
    public void start (Stage stage) throws IOException {
        Parent a = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        ScrollPane graph = (ScrollPane) a.lookup("#graphView");
        graph.setContent(new GraphView(root).getChild());
        graph.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        graph.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        stage.setTitle("JastAddDebugger");
        Scene scene = new Scene(a, 1000,1000);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

    }
}