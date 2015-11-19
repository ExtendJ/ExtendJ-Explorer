package jastaddad.ui;

import configAST.*;
import jastaddad.api.ASTAPI;
import jastaddad.api.JastAddAdAPI;
import jastaddad.ui.controllers.Controller;
import jastaddad.ui.graph.GraphView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * This is the main class for the JastAddAd system if the user wants the UI. This class will create an jastAddAd
 * instance that in its turn creates an ASTAPI object that will generate the filtered AST. After this it opens the UI.
 *
 * JastAddAd can be started by calling run() method on this class or JastAddAdUI.
 *
 */

public class JastAddAdUI extends Application {

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
        this.mon = new UIMonitor(jastAddAd.api());
        launch(new String[0]);
    }

    public void setFilterDir(String dir){jastAddAd.setFilterDir(dir);}

    /**
     * start the UI and is by JavaFX. Load the FXML files and genereates the UI. It also embeds the Swing based graph.
     *
     * @param stage
     * @throws IOException
     */
    @Override
    public void start (Stage stage) throws IOException, Exception {
        FXMLLoader loader = new FXMLLoader();
        rootView = loader.load(getClass().getResource("/main.fxml").openStream());
        con = loader.<Controller>getController();
        mon.setController(con);
        GraphView graphview = new GraphView(mon);
        con.init(mon, graphview);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setTitle("JastAddDebugger " + ASTAPI.VERSION);
        stage.setScene(new Scene(rootView, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight()));
        stage.setMaximized(true);
        stage.show();
        ScrollPane center = (ScrollPane) rootView.lookup("#graphView");
        center.setContent(graphview);
    }


    /**
     * main function for starting a JastAddAdUI session.
     * @param args
     */
    public static void main(String[] args) {
        try{
            String filename = "testInput.cfg";
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
                List a = new List();
                a.add(new Include(null, new Opt<NodeConfigList>()));
                DebuggerConfig d = new DebuggerConfig(new Opt(), a);
                JastAddAdUI debugger = new JastAddAdUI(program);
                debugger.run();
                //program.genCode(System.out);
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