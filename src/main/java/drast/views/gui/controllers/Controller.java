package drast.views.gui.controllers;

import drast.Log;
import drast.model.DrAST;
import drast.model.DrASTSettings;
import drast.model.Severity;
import drast.model.TreeFilter;
import drast.model.filteredtree.GenericTreeNode;
import drast.model.terminalvalues.TerminalValue;
import drast.starter.ASTProvider;
import drast.views.gui.CustomOutputStream;
import drast.views.gui.DrASTGUI;
import drast.views.gui.GUIData;
import drast.views.gui.dialogs.DrDialog;
import drast.views.gui.dialogs.LoadingDialog;
import drast.views.gui.guicomponent.FilterEditor;
import drast.views.gui.guicomponent.JavaSourceEditor;
import drast.views.gui.guicomponent.MinimizeButton;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * This is the main controller of the GUI. It holds references to all sub controllers. If some part of the GUI does
 * not have its own controller, its events will be handled here.
 */
public class Controller implements Initializable {
  @FXML Parent root;
  @FXML private AttributeTabController attributeTabController;
  @FXML private TopMenuController topMenuController;
  @FXML private GraphViewController graphViewTabController;
  @FXML private ConsoleController consoleController;

  private FilterEditor filterEditor;
  @FXML private VBox filterContainer;
  @FXML private Button applyFilter;
  @FXML private Button saveFilter;
  @FXML private Button loadFilter;

  @FXML private Button loadFile;
  @FXML private VBox inputFileContainer;

  @FXML private VBox leftWindow;
  @FXML private VBox rightWindow;
  @FXML private VBox consoleWindow;

  @FXML private TabPane leftWindowContent;
  @FXML private SplitPane rightWindowContent;
  @FXML private VBox consoleWindowContent;


  @FXML private MinimizeButton minimizeLeftSide;
  @FXML private MinimizeButton minimizeRightSide;
  @FXML private MinimizeButton minimizeConsole;

  @FXML private SplitPane centerSplitPane;
  @FXML private SplitPane consoleAndGraphSplitPane;

  @FXML private Label nodeCountLabel;
  @FXML private Label compilerLabel;

  private static final boolean debugging = false;

  private PrintStream standardErr;
  private PrintStream standardOut;

  private GUIData mon;
  private ArrayList<ControllerInterface> controllers;
  private JavaSourceEditor sourceEditor;

  @Override public void initialize(URL url, ResourceBundle rb) {
  }

  /**
   * Called by DrASTGUI when the UI is created to initialize fields and event listeners in the UI.
   */
  public void init(GUIData mon) {
    this.mon = mon;

    sourceEditor = new JavaSourceEditor(this);
    sourceEditor.getStyleClass().add("textAreaConfig");
    inputFileContainer.getChildren().add(sourceEditor);

    controllers = new ArrayList<>();
    controllers.add(attributeTabController);
    controllers.add(topMenuController);
    controllers.add(graphViewTabController);
    controllers.add(consoleController);

    for (ControllerInterface controller : controllers) {
      controller.init(mon);
    }

    // Minimize buttons for each side bar.
    minimizeLeftSide.setOnMouseClicked(event2 -> {
      if (event2.getButton() == MouseButton.PRIMARY) {
        if (minimizeLeftSide.minimizeNext()) {
          minimizeLeftWindow();
        } else {
          maximizeLeftWindow();
        }
      }
    });

    minimizeRightSide.setOnMouseClicked(event2 -> {
      if (event2.getButton() == MouseButton.PRIMARY) {
        if (minimizeRightSide.minimizeNext()) {
          minimizeRightWindow();
        } else {
          maximizeRightWindow();
        }
      }
    });

    minimizeConsole.setOnMouseClicked(event2 -> {
      if (event2.getButton() == MouseButton.PRIMARY) {
        if (minimizeConsole.minimizeNext()) {
          minimizeConsoleWindow();
        } else {
          maximizeConsoleWindow();
        }
      }
    });

    filterEditor = new FilterEditor(mon.getController());
    filterContainer.getChildren().add(filterEditor);
    filterEditor.getStyleClass().add("textAreaConfig");

    // Update the new filter. This is done in the API.
    applyFilter.setOnAction(event -> mon.getController().saveFilter());

    saveFilter.setOnAction(event -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Save Filter As...");
      File prev = DrASTSettings.getFilterFile();
      if (prev != null && prev.isFile() && prev.getParentFile().isDirectory()) {
        fileChooser.setInitialDirectory(prev.getParentFile());
      } else {
        fileChooser.setInitialDirectory(mon.getDefaultSettingsDirectory());
      }
      fileChooser.getExtensionFilters()
          .add(new FileChooser.ExtensionFilter("Filter Files", "*" + DrAST.FILTER_EXTENSION));
      File file = fileChooser.showSaveDialog(loadFilter.getScene().getWindow());
      if (file != null) {
        try {
          if (!file.exists()) {
            // If the file does not exist yet, ensure it has the ".fcl" extension.
            if (!file.getName().endsWith(DrAST.FILTER_EXTENSION)) {
              file = new File(file.getPath() + DrAST.FILTER_EXTENSION);
            }
          }
          if (TreeFilter.writeFilter(file, mon.getController().getFilter())) {
            DrASTSettings.put(DrASTSettings.PREV_FILTER, file.getAbsolutePath());
            Log.info("Saved filter to file: %s", file.getAbsolutePath());
          }
        } catch (IOException e) {
          throw new Error(e);
        }
      }
    });

    loadFilter.setOnAction(event -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open Filter File");
      File prev = DrASTSettings.getFilterFile();
      if (prev != null && prev.isFile() && prev.getParentFile() != null) {
        fileChooser.setInitialDirectory(prev.getParentFile());
      } else {
        fileChooser.setInitialDirectory(mon.getDefaultSettingsDirectory());
      }
      fileChooser.getExtensionFilters()
          .add(new FileChooser.ExtensionFilter("Filter Files", "*" + DrAST.FILTER_EXTENSION));
      File file = fileChooser.showOpenDialog(loadFilter.getScene().getWindow());
      if (file != null) {
        if (loadFilter(file)) {
          DrASTSettings.put(DrASTSettings.PREV_FILTER, file.getAbsolutePath());
        }
      }
    });

    loadFile.setOnAction(event -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open Input File");
      File prevFile = new File(DrASTSettings.get(DrASTSettings.PREV_FIRST_ARG, ""));
      if (prevFile.isFile() && prevFile.getParentFile() != null
          && prevFile.getParentFile().isDirectory()) {
        fileChooser.setInitialDirectory(prevFile.getParentFile());
      }
      File file = fileChooser.showOpenDialog(loadFile.getScene().getWindow());
      if (file != null && file.isFile()) {
        String[] tailArgs = DrASTSettings.get(DrASTSettings.PREV_TAIL_ARGS, "").split("\\s\\+", 0);
        List<String> args = new ArrayList<>(tailArgs.length + 1);
        args.add(file.getAbsolutePath());
        for (String arg : tailArgs) {
          if (!arg.isEmpty()) {
            args.add(arg);
          }
        }
        String[] allArgs = new String[args.size()];
        args.toArray(allArgs);
        runCompiler(mon.getDrASTUI(), DrASTSettings.get(DrASTSettings.PREV_JAR, ""), allArgs);
      }
    });
    updateAstInfoLabels();
  }

  private void maximizeLeftWindow() {

    if (minimizeLeftSide.isMinimized()) {
      minimizeLeftSide.setMinimized(false);
      leftWindow.getChildren().add(leftWindowContent);
      leftWindow.maxWidthProperty().bind(centerSplitPane.widthProperty());
      centerSplitPane.setDividerPosition(0, 0.2);
    }
  }

  private void maximizeRightWindow() {
    if (minimizeRightSide.isMinimized()) {
      minimizeRightSide.setMinimized(false);
      rightWindow.getChildren().add(rightWindowContent);
      rightWindow.maxWidthProperty().bind(centerSplitPane.widthProperty());
      centerSplitPane.setDividerPosition(1, 0.8);
    }

  }

  private void maximizeConsoleWindow() {
    if (minimizeConsole.isMinimized()) {
      minimizeConsole.setMinimized(false);
      consoleWindow.getChildren().add(consoleWindowContent);
      consoleWindow.maxHeightProperty().bind(consoleAndGraphSplitPane.heightProperty());
      consoleAndGraphSplitPane.setDividerPosition(0, 0.8);
    }
  }

  private void minimizeLeftWindow() {
    if (minimizeLeftSide.minimizeNext()) {
      minimizeLeftSide.setMinimized(true);
      leftWindow.getChildren().remove(leftWindowContent);
      leftWindow.maxWidthProperty().bind(minimizeLeftSide.widthProperty());
    }
  }

  private void minimizeRightWindow() {
    if (minimizeRightSide.minimizeNext()) {
      minimizeRightSide.setMinimized(true);
      rightWindow.getChildren().remove(rightWindowContent);
      rightWindow.maxWidthProperty().bind(minimizeRightSide.widthProperty());
    }
  }

  private void minimizeConsoleWindow() {
    if (minimizeConsole.minimizeNext()) {
      minimizeConsole.setMinimized(true);
      consoleWindow.getChildren().remove(consoleWindowContent);
      consoleWindow.maxHeightProperty().bind(minimizeConsole.heightProperty());
    }
  }

  /**
   * Under the graph and tree view there is a bar with labels. This method sets the right texts in these.
   */
  private void updateAstInfoLabels() {
    nodeCountLabel.setText(String.format("%s/%s",
        mon.getTreeTraverser().getClusteredASTSize(),
        mon.getTreeTraverser().getASTSize()));
    compilerLabel.setText(DrASTSettings.get(DrASTSettings.PREV_JAR, "N/A"));
  }

  public void toggleMinimizeWindows() {
    if (minimizeLeftSide.isMinimized() && minimizeRightSide.isMinimized() && minimizeConsole
        .isMinimized()) {
      maximizeLeftWindow();
      maximizeRightWindow();
      maximizeConsoleWindow();
    } else {
      minimizeLeftWindow();
      minimizeRightWindow();
      minimizeConsoleWindow();
    }
  }

  public void setOutStreams() {
    if (debugging) {
      return;
    }
    PrintStream printError =
        new PrintStream(new CustomOutputStream(Log::log, Severity.ERROR));
    PrintStream printMessage =
        new PrintStream(new CustomOutputStream(Log::log, Severity.INFO));
    standardErr = System.err;
    standardOut = System.out;
    System.setErr(printError);
    System.setOut(printMessage);
  }

  public void resetOutStreams() {
    System.setOut(standardOut);
    System.setErr(standardErr);
  }

  public void onSetRoot() {
    controllers.forEach(ControllerInterface::onSetRoot);
    updateAstInfoLabels();
    updateGUI();
  }

  public void saveFilter() {
    if (!mon.getTreeTraverser().hasRoot()) {
      return;
    }

    File filterFile = DrASTSettings.getFilterFile();
    if (filterFile == null || !filterFile.isFile()) {
      applyFilter();
      return;
    }

    boolean errored;
    String filter = getFilter();
    try {
      errored = !TreeFilter.writeFilter(filterFile, filter);
      if (!errored) {
        applyFilter();
      }
    } catch (IOException e) {
      e.printStackTrace();
      errored =  true;
    }
    if (errored) {
      Log.warning("The filter has errors and is not used. The previous filter is enabled.");
    }
  }

  private void applyFilter() {
    Object astRoot = mon.getDrASTAPI().getRoot();
    if (astRoot != null) {
      Task<Boolean> task = new Task<Boolean>() {
        @Override public Boolean call() {
          mon.getDrASTUI().setRoot(astRoot);
          return true;
        }
      };
      LoadingDialog loadingDialog = new LoadingDialog(mon);
      task.setOnRunning((e) -> loadingDialog.show());
      task.setOnSucceeded((e) -> loadingDialog.hide());
      task.setOnFailed((e) -> loadingDialog.hide());
      task.setOnCancelled((e) -> loadingDialog.hide());
      new Thread(task).start();
    }
  }

  public void updateGUI() {
    resetReferences();
    controllers.forEach(ControllerInterface::updateGUI);
    updateAstInfoLabels();
  }

  /**
   * Shows a exit program dialog box and waits for it. If yes then close the program.
   */
  public void exitProgram() {
    Stage stage = (Stage) root.getScene().getWindow();
    stage.close();
  }

  /**
   * When a function is started,e.g. a dialog window is opened, this method is called.
   */
  public void functionStarted() {
    mon.functionStart();
  }

  /**
   * When a function is stopped,e.g. a dialog window is closed, this method is called.
   */
  public void functionStopped() {
    mon.functionDone();
  }

  /**
   * An attribute was selected for the selected node. This method tells different parts of the GUI of this event.
   *
   * @param info the attribute selected.
   */
  public void attributeInNodeSelected(TerminalValue info) {
    for (DrDialog subWindow : mon.getSubWindows()) {
      subWindow.attributeSelected(info);
    }
  }

  /**
   * Method for selecting a node in the graph or tree. Used for example by other controllers.
   *
   * @param node The selected node.
   * @param caller the controller that called the method.
   */
  public void nodeSelected(GenericTreeNode node, ControllerInterface caller) {
    for (DrDialog subWindow : mon.getSubWindows()) {
      subWindow.nodeSelected(node);
    }

    mon.setSelectedNode(node);

    controllers.stream().filter(controller -> controller != caller)
        .forEach(controller -> controller.nodeSelected(node));
  }

  public void runCompiler(DrASTGUI view, String jarFile, String[] args) {
    String firstArg = args[0];
    sourceEditor.loadFile(new File(firstArg));
    LoadingDialog loadingDialog = new LoadingDialog(mon);

    Task<Boolean> task = new Task<Boolean>() {
      @Override public Boolean call() {
        try {
          setOutStreams();
          return ASTProvider.parseAst(jarFile, args, view::setRoot);
        } finally {
          resetOutStreams();
        }
      }
    };
    task.setOnRunning(e -> loadingDialog.show());
    task.setOnSucceeded(e -> {
      loadingDialog.hide();
      if (task.getValue()) {
        // The compiler run succeeded: save the compiler settings.
        String[] tailArgs = new String[args.length > 1 ? args.length - 1 : 0];
        if (args.length > 1) {
          System.arraycopy(args, 1, tailArgs, 0, args.length - 1);
        }

        DrASTSettings.put(DrASTSettings.PREV_JAR, jarFile);
        DrASTSettings.put(DrASTSettings.PREV_FIRST_ARG, args[0]);
        DrASTSettings.put(DrASTSettings.PREV_TAIL_ARGS, String.join(" ", tailArgs));
        DrASTSettings.put(DrASTSettings.PREV_ARGS, String.join(" ", args));
      } else {
        // Compiler failed - restore the old source file view.
        sourceEditor.loadFile(new File(DrASTSettings.get(DrASTSettings.PREV_FIRST_ARG, "")));
      }
    });
    task.setOnFailed(e -> loadingDialog.hide());
    task.setOnCancelled(e -> loadingDialog.hide());
    new Thread(task).start();
  }

  /**
   * Method for deselecting a node in the graph or tree. Used for example by other controllers.
   *
   * @param caller the controller that called the method.
   */
  public void nodeDeselected(ControllerInterface caller) {

    mon.setSelectedNode(null);

    controllers.stream().filter(controller -> controller != caller)
        .forEach(ControllerInterface::nodeDeselected);
  }

  /**
   * Method that is called when the application is destroyed, if something needs to be reset,
   * This will set System.err and System.out to standard stream.
   */
  public void onApplicationClose() {
    resetOutStreams();
  }

  /**
   * Compute node references again.
   */
  public void resetReferences() {
    GenericTreeNode node = mon.getLastRealNode();
    if (node == null) {
      return;
    }
    node = mon.getTreeTraverser().getTreeNode(node.getNode());
    if (node == null) {
      return;
    }
    mon.setSelectedNode(node);
    graphViewTabController.nodeSelected(node.getTreeNode());
    if (mon.getSelectedInfo() != null) {
      attributeTabController.setReference(mon.getSelectedInfo().getValue());
    }
  }

  public GraphViewController getGraphViewTabController() {
    return graphViewTabController;
  }

  public void loadPreviousFilter() {
    File previousFilter = DrASTSettings.getFilterFile();
    loadFilter(previousFilter);
  }

  public String getFilter() {
    return getFilterText();
  }

  private String getFilterText() {
    return filterEditor.getText();
  }

  /**
   * Load and print the filter text in the text area for filter text.
   */
  private boolean loadFilter(File file) {
    if (filterEditor.loadFile(file)) {
      mon.getController().applyFilter();
      return true;
    }
    return false;
  }
}
