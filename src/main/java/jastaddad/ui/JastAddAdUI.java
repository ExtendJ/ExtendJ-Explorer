package jastaddad.ui;

import configAST.*;
import jastaddad.api.ASTAPI;
import jastaddad.api.JastAddAdAPI;
import jastaddad.api.filteredtree.TreeNode;
import jastaddad.tasks.JastAddAdTask;
import jastaddad.tasks.JastAddAdXML;
import jastaddad.ui.controllers.Controller;
import jastaddad.ui.graph.GraphView;
import javafx.application.Application;
import javafx.event.EventHandler;
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
import javafx.stage.WindowEvent;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

/**
 * This is the main class for the JastAddAd system if the user wants the UI. This class will create an jastAddAd
 * instance that in its turn creates an ASTAPI object that will generate the filtered AST. After this it opens the UI.
 *
 * JastAddAd can be started by calling run() method on this class or JastAddAdUI.
 *
 */

public class JastAddAdUI extends Application implements JastAddAdTask {

    protected static UIMonitor mon;
    protected static JastAddAdAPI jastAddAd;
    protected static Controller con;

    private Parent rootView;
    public JastAddAdUI() {} // This one is used by Application

    public JastAddAdUI(Object root) {
        jastAddAd = new JastAddAdAPI(root);
    }

    public Parent getRoot(){
        return rootView;
    }
    /**
     * run() generates the AST and then opens the UI
     */
    public void run(){
        jastAddAd.run();
        this.mon = new UIMonitor(jastAddAd);
        launch(new String[0]);
    }

    public void setFilterDir(String dir){jastAddAd.setFilterDir(dir);}

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
        GraphView graphview = new GraphView(mon);
        graphview.setOnMouseClicked(event -> graphview.getParent().requestFocus());

        mon.setGraphView(graphview);
        con.init(mon, graphview);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setTitle("JastAddDebugger " + ASTAPI.VERSION);
        stage.setScene(new Scene(rootView, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight()));
        //stage.setMaximized(true);
        
        /*stage.setOnCloseRequest(we -> {
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
    }


    /**
     * main function for starting a JastAddAdUI session.
     * @param args
     */
    public static void main(String[] args) {
        try{
            String filename = "sample.cfg";
            ConfigScanner scanner = new ConfigScanner(new FileReader(filename));
            ConfigParser parser = new ConfigParser();
            DebuggerConfig program = (DebuggerConfig) parser.parse(scanner);
            if (!program.errors().isEmpty()) {
                System.err.println();
                System.err.println("Errors: ");
                for (ErrorMessage e: program.errors()) {
                    System.err.println("- " + e);
                }
            } else {
                JastAddAdUI debugger = new JastAddAdUI(program);
                debugger.run();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}