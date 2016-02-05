package drast.views.gui;

import drast.model.ASTBrain;
import drast.model.DrAST;
import drast.views.DrASTView;
import drast.views.gui.controllers.Controller;
import drast.views.gui.dialogs.OpenASTDialog;
import drast.views.gui.graph.GraphView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * This is the main class for the DrAST system if the user wants the GUI. This class will create an DrAST
 * instance that in its turn creates an ASTAPI object that will generate the filtered AST. After this it opens the GUI.
 *
 * DrAST can be started by calling run() method on this class or DrASTGUI.
 *
 */

public class DrASTGUI extends Application implements DrASTView {
    protected static Monitor mon;
    protected static drast.model.DrAST DrAST;
    protected static Controller con;

    private Parent rootView;
    public DrASTGUI() { DrAST = new DrAST(); } // This one is used by Application

    public DrASTGUI(Object root) {
        DrAST = new DrAST(root);
    }

    public Parent getRoot(){
        return rootView;
    }
    /**
     * run() generates the AST and then opens the UI
     */
    public void run() {
        DrAST.run();
        this.mon = new Monitor(DrAST);
        launch(new String[0]);

    }

    @Override
    public drast.model.DrAST getAPI() {
        return DrAST;
    }

    @Override
    public void setRoot(Object root, String filterPath, String defaultDir, boolean opened) {
        DrAST = new DrAST(root);
        DrAST.setFilterPath(filterPath);
        mon.clean(DrAST);
        mon.setDefaultDirectory(defaultDir);
        mon.setRerunable(opened);
        con.onNewAPI();
    }

    @Override
    public void printMessage(int type, String message){
        if(mon == null || mon.getBrain() == null)
            return;
        mon.getBrain().putMessage(type, message);
    }

    public void setFilterDir(String dir){DrAST.setFilterPath(dir);}

    /**
     * start the UI and is by JavaFX. Load the FXML files and generates the GUI. It also embeds the Swing based graph.
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
        stage.setTitle("DrAST " + ASTBrain.VERSION);
        stage.setScene(new Scene(rootView, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight()-100));
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
        new DrASTGUI().run();
    }
}