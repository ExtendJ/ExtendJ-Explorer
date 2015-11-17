package jastaddad.ui;

import configAST.ConfigParser;
import configAST.ConfigScanner;
import configAST.DebuggerConfig;
import configAST.ErrorMessage;
import jastaddad.api.ASTAPI;
import jastaddad.api.JastAddAdAPI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import jastaddad.ui.controllers.Controller;
import jastaddad.ui.graph.GraphView;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JastAddAdUI extends Application {

    private static UIMonitor mon;
    private static JastAddAdAPI jastAddAd;
    private static Controller con;

    public JastAddAdUI() {} // This one is used by Application

    public JastAddAdUI(Object root) {
        jastAddAd = new JastAddAdAPI(root);
    }

    public void run(){
        System.out.println("asdasdasd");
        jastAddAd.run();
        this.mon = new UIMonitor(jastAddAd.api());
        launch(new String[0]);
    }

    public void setFilterDir(String dir){jastAddAd.setFilterDir(dir);}

    @Override
    public void start (Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Parent a = loader.load(getClass().getResource("/main.fxml").openStream());
        con = loader.<Controller>getController();
        mon.setController(con);
        GraphView graphview = new GraphView(mon);
        con.init(mon, graphview, this);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setTitle("JastAddDebugger " + ASTAPI.VERSION);
        stage.setScene(new Scene(a, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight()));
        stage.setMaximized(true);
        stage.show();

        ScrollPane center = (ScrollPane) a.lookup("#graphView");
        center.setContent(graphview);
    }

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