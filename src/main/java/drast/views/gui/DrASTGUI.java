package drast.views.gui;

import drast.model.AlertMessage;
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
 * This is the main class for the DrAST system if the user wants the GUI. This class will first run the DrAST model. If
 * the model is not passed to this class it will create an instance of it. Next it opens the GUI.
 *
 * The GUI is created by using the JavaFX library, except the graph view, which uses the Jung2 library which in turn
 * uses swing.
 *
 * The Run() method will run the model and then start the GUI
 * The setRoot() method will run the model, but use the already open GUI.
 *
 * Packages:
 *      gui.controllers: A number of controllers are handling all GUI interactions with the user.
 *      gui.dialogs: All dialogs that are used in the program.
 *      gui graph: All classes that uses the Jung2 library. This library have been extended and chaned in DrAST.
 *      gui.guicomponents: components that the user interact with like input fields, editors, buttons and so on.
 *
 * A Monitor is used to share data throughout all the GUI classes.
 *
 * A Config class is used to connect with the configuration file of the GUI.
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

    public DrASTGUI(DrAST drAST) {
        this.drAST = drAST;
    }

    public Parent getRoot(){
        return rootView;
    }
    /**
     * run() generates the AST and then opens the GUI
     */
    public void run() {
        drAST.run();
        hasRun = true;
        this.mon = new Monitor(drAST);
        launch(new String[0]);
        con.onApplicationClose();
    }

    @Override
    public drast.model.DrAST getAPI() {
        return drAST;
    }

    /**
     * When the GUI is already running, this should be used when a compiler has a new root that should be debugged.
     *
     * This method creates a new model, runs it and then updates the GUI.
     * @param root
     * @param filterPath
     * @param defaultDir
     * @param opened
     */
    @Override
    public void setRoot(Object root, String filterPath, String defaultDir, boolean opened) {
        drAST = new DrAST(root);
        drAST.setFilterPath(filterPath);
        if(!hasRun)
            run();
        else {
            mon.clean(drAST);
            mon.setDefaultDirectory(defaultDir);
            mon.setRerunable(opened);

            long timeStart = System.currentTimeMillis();
            mon.getDrASTAPI().run();
            if(mon.getBrain().containsError(AlertMessage.AST_STRUCTURE_ERROR) || mon.getBrain().containsError(AlertMessage.FILTER_ERROR)){
                return;
            }
            con.addMessage("Filter update: done after " + (System.currentTimeMillis() - timeStart) + " ms");

            if(mon.isOptimization())
                mon.getConfig().put("niceEdges", "0");
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
     * Start the GUI created by JavaFX. Load the FXML files and generates the GUI. It also embeds the Swing based graph.
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
        stage.setTitle("DrAST " + DrAST.VERSION);
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

    /**
     * This method opens the a file in its default program on the machine. How the default program is opened depends on
     * the operating system. An OSDetector class is used to solve this issue.
     *
     * @param file
     * @return
     */
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
        /*Tilldelning e = new Tilldelning();
        DrASTGUI gui = new DrASTGUI(e);
        gui.run();*/
        new DrASTGUI().run();
        System.exit(0);
    }
}