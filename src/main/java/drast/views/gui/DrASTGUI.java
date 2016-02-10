package drast.views.gui;

import drast.DrASTSetup;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * This is the main class for the DrAST system if the user wants the GUI. This class will create an DrAST
 * instance that in its turn creates an ASTAPI object that will generate the filtered AST. After this it opens the GUI.
 *
 * DrAST can be started by calling run() method on this class or DrASTGUI.
 *
 */

public class DrASTGUI extends Application implements DrASTView {
    protected static Monitor mon;
    protected static DrAST drAST;
    protected static Controller con;
    private static boolean hasRun = false;

    private Parent rootView;

    public DrASTGUI() { // This one is used by Application
        if(drAST == null)
            drAST = new DrAST();
    }

    public DrASTGUI(Object root) {
        drAST = new DrAST(root);
    }

    public Parent getRoot(){
        return rootView;
    }
    /**
     * run() generates the AST and then opens the UI
     */
    public void run() {
        drAST.run();
        hasRun = true;
        this.mon = new Monitor(drAST);
        launch(new String[0]);
    }

    @Override
    public drast.model.DrAST getAPI() {
        return drAST;
    }

    @Override
    public void setRoot(Object root, String filterPath, String defaultDir, boolean opened) {
        drAST = new DrAST(root);
        drAST.setFilterPath(filterPath);
        if(!hasRun)
            run();
        else {
            drAST.run();
            mon.clean(drAST);
            mon.setDefaultDirectory(defaultDir);
            mon.setRerunable(opened);
            con.onNewAPI();
        }
    }

    @Override
    public void printMessage(int type, String message){
        if(mon == null || mon.getBrain() == null)
            return;
        mon.getBrain().putMessage(type, message);
    }

    public void setFilterDir(String dir){ drAST.setFilterPath(dir);}

    /**
     * start the UI and is by JavaFX. Load the FXML files and generates the GUI. It also embeds the Swing based graph.
     *
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws Exception {

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
        });
        stage.show();
        ScrollPane center = (ScrollPane) rootView.lookup("#graphViewScrollPane");
        center.setContent(graphview);
        Platform.runLater(() -> graphview.setPreferredSize((int) center.getWidth(), (int) center.getHeight()));
        if (!drAST.noRoot())
            return;
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
        /*String jarPath = "/home/gda10jli/Documents/extendj/extendj.jar";
        String[] args2 = {"/home/gda10jli/Documents/jastadddebugger-exjobb/test.java"};
        String filterPath = "/home/gda10jli/Documents/jastadddebugger-exjobb/filter.fcl";
        DrASTSetup setup = new DrASTSetup("DrASTGUI", jarPath, filterPath, args2);
        setup.run();*/
    }
}