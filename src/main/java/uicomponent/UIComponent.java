package uicomponent;

import javafx.embed.swing.SwingNode;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import jastaddad.Node;
import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import uicomponent.graph.GraphView;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        ScrollPane center = (ScrollPane) a.lookup("#graphView");
        center.setContent(new GraphView(root));
        stage.setTitle("JastAddDebugger");
        stage.setScene(new Scene(a,1000,1000));
        stage.show();
    }
}