package drast.ui;

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import configAST.*;
import drast.api.ASTAPI;
import drast.api.DrASTAPI;
import drast.tasks.DrASTTask;
import drast.ui.controllers.Controller;
import drast.ui.dialogs.OpenASTDialog;
import drast.ui.graph.GraphView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * This is the main class for the DrAST system if the user wants the UI. This class will create an DrAST
 * instance that in its turn creates an ASTAPI object that will generate the filtered AST. After this it opens the UI.
 *
 * DrAST can be started by calling run() method on this class or DrASTUI.
 *
 */

public class DrASTUI extends Application implements DrASTTask {
    protected static UIMonitor mon;
    protected static DrASTAPI DrAST;
    protected static Controller con;

    private Parent rootView;
    public DrASTUI() { DrAST = new DrASTAPI(); } // This one is used by Application

    public DrASTUI(Object root) {
        DrAST = new DrASTAPI(root);
    }

    public Parent getRoot(){
        return rootView;
    }
    /**
     * run() generates the AST and then opens the UI
     */
    public void run() {
        DrAST.run();
        this.mon = new UIMonitor(DrAST);
        launch(new String[0]);
    }

    @Override
    public DrASTAPI getAPI() {
        return DrAST;
    }

    @Override
    public void setRoot(Object root, String filterPath, String defaultDir, boolean opened) {
        DrAST = new DrASTAPI(root);
        DrAST.setFilterPath(filterPath);
        mon.clean(DrAST);
        mon.setDefaultDirectory(defaultDir);
        mon.setRerunable(opened);
        con.onNewAPI();
    }

    @Override
    public void printMessage(String type, String message){
        if(mon == null || mon.getApi() == null)
            return;
        mon.getApi().putError(type, message);
    }

    public void setFilterDir(String dir){DrAST.setFilterPath(dir);}

    /**
     * start the UI and is by JavaFX. Load the FXML files and generates the UI. It also embeds the Swing based graph.
     *
     * @param stage
     * @throws IOException
     */
    @Override
    public void start (Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        rootView = loader.load(getClass().getResource("/main.fxml").openStream());
        con = loader.<Controller>getController();
        mon.setParentStage(stage);
        mon.setController(con);
        mon.setDrASTUI(this);
        mon.setStage(stage);
        GraphView graphview = new GraphView(mon);
        graphview.setOnMouseClicked(event -> graphview.getParent().requestFocus());

        mon.setGraphView(graphview);
        con.init(mon, graphview);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setTitle("DrAST " + ASTAPI.VERSION);
        stage.setScene(new Scene(rootView, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight()));
/*
        stage.setOnCloseRequest(we -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Exit");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setContentText("Are you sure you want to quit?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
            } else {
                we.consume();
            }
        });*/
        stage.show();
        ScrollPane center = (ScrollPane) rootView.lookup("#graphViewScrollPane");
        center.setContent(graphview);
        graphview.setPreferredSize((int)center.getWidth(), (int)center.getHeight());
        Platform.runLater(() -> {
            OpenASTDialog dialog = new OpenASTDialog(mon);
            dialog.init();
            dialog.show();
        });

    }

    public static boolean openFile(File file) {
        try {
            if (OSDetector.isWindows()) {
                Runtime.getRuntime().exec(new String[]
                        {"rundll32", "url.dll,FileProtocolHandler", file.getAbsolutePath()});
                return true;
            } else if (OSDetector.isLinux()){
                Runtime.getRuntime().exec(new String[]{"xdg-open",file.getAbsolutePath()});
                System.out.println(file.getAbsolutePath());
            } else if(OSDetector.isMac()) {
                Runtime.getRuntime().exec(new String[]{"open", file.getAbsolutePath()});
                return true;
            }else{
                // Unknown OS, try with desktop
                if (Desktop.isDesktopSupported()){
                    Desktop.getDesktop().open(file);
                    return true;
                }
                return false;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * main function for starting a DrASTUI session.
     * @param args
     */
    public static void main(String[] args) {
        new DrASTUI().run();
    }
}