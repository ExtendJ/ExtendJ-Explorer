package uicomponent;

import uicomponent.graph.GraphView;
import jastaddad.Node;
import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class UIComponent extends Application {
    private static Node root;

    public UIComponent() {} // This one is used by Application

    public UIComponent(Node root) {
        this.root = root;
        launch(new String[0]);
    }

    @Override
    public void start (Stage stage){
        StackPane pane = new StackPane();
        pane.getChildren().add(new GraphView(root).getChild());
        Scene scene = new Scene(pane, 800, 400, Color.WHITE);
        stage.setTitle("Display the ASTNODES");
        stage.setScene(scene);
        stage.show();
    }
}