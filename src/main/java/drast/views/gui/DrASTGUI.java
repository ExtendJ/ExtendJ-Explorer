package drast.views.gui;

import drast.Log;
import drast.model.DrAST;
import drast.model.TreeFilter;
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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 * This is the main class for the DrAST system if the user wants the GUI. This class will first run the DrAST model. If
 * the model is not passed to this class it will create an instance of it. Next it opens the GUI.
 * <p>
 * The GUI is created by using the JavaFX library, except the graph view, which uses the Jung2 library which in turn
 * uses swing.
 * <p>
 * The Run() method will run the model and then start the GUI
 * The setRoot() method will run the model, but use the already open GUI.
 * <p>
 * Packages:
 * gui.controllers: A number of controllers are handling all GUI interactions with the user.
 * gui.dialogs: All dialogs that are used in the program.
 * gui graph: All classes that uses the Jung2 library. This library have been extended and changed in DrAST.
 * gui.guicomponents: components that the user interact with like input fields, editors, buttons and so on.
 * <p>
 * A Monitor is used to share data throughout all the GUI classes.
 * <p>
 * A Config class is used to connect with the configuration file of the GUI.
 */

public class DrASTGUI extends Application {
  private static final GUIData mon = new GUIData();
  private static Controller con;
  private static boolean guiHasBeenCreated = false;

  /** Used by JavaFx Application when building the GUI. */
  public DrASTGUI() {
  }

  /**
   * Generates the AST and then opens the GUI.
   */
  private static void openView() {
    guiHasBeenCreated = true;
    launch();
    con.onApplicationClose();
  }

  /**
   * When the GUI is already running, this should be used when a compiler has a new root that should be debugged.
   * <p>
   * This method creates a new model, runs it and then updates the GUI.
   */
  public void setRoot(Object root) {
    long timeStart = System.currentTimeMillis();
    DrAST newAst = new DrAST(root, TreeFilter.readFilter(con.getFilter()));
    Log.info("Filter update: done after %d ms", System.currentTimeMillis() - timeStart);
    Platform.runLater(() -> {
      mon.reset(newAst);
      if (guiHasBeenCreated) {
        con.onSetRoot();
      } else {
        openView();
      }
    });
  }

  /**
   * Start the GUI created by JavaFX. Load the FXML files and generates the GUI. It also embeds the Swing based graph.
   */
  @Override public void start(Stage stage) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    Parent rootView = loader.load(getClass().getResource("/main.fxml").openStream());
    con = loader.<Controller>getController();
    mon.setParentStage(stage);
    mon.setController(con);
    mon.setDrASTUI(this);
    mon.setStage(stage);
    GraphView graphview = new GraphView(mon);
    graphview.setOnMouseClicked(event -> graphview.getParent().requestFocus());

    mon.setGraphView(graphview);
    con.init(mon);
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    stage.setTitle("DrAST " + DrAST.DRAST_VERSION);
    stage.setScene(
        new Scene(rootView, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight() - 100));

    stage.show();
    ScrollPane center = (ScrollPane) rootView.lookup("#graphViewScrollPane");
    center.setContent(graphview);
    Platform.runLater(
        () -> graphview.setPreferredSize((int) center.getWidth(), (int) center.getHeight()));
    con.loadPreviousFilter();
    OpenASTDialog dialog = new OpenASTDialog(mon);
    dialog.show();
  }

  /**
   * This method opens the a file in its default program on the machine. How the default program is opened depends on
   * the operating system. An OSDetector class is used to solve this issue.
   */
  public static boolean openFile(File file) {
    if (Desktop.isDesktopSupported()) {
      // Using a new thread here to workaround this issue:
      // http://stackoverflow.com/questions/23176624/javafx-freeze-on-desktop-openfile-desktop-browseuri
      new Thread(() -> {
        try {
          Desktop.getDesktop().open(file);
        } catch (IOException e) {
          Log.error("Failed to open file on desktop: " + file.getAbsolutePath());
        }
      }).start();
      return true;
    }
    return false;
  }

  /** Entry point when running the DrAST Jar file. */
  public static void main(String[] args) {
    openView();
    System.exit(0);
  }
}
