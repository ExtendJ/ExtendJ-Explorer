package drast.views.gui.controllers;

import drast.DrASTStarter;
import drast.model.AlertMessage;
import drast.model.filteredtree.GenericTreeNode;
import drast.model.nodeinfo.NodeInfo;
import drast.views.DrASTView;
import drast.views.gui.Monitor;
import drast.views.gui.dialogs.DrDialog;
import drast.views.gui.dialogs.LoadingDialog;
import drast.views.gui.graph.GraphView;
import drast.views.gui.guicomponent.MinimizeButton;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * This is the main controller of the GUI. It holds references to all sub controllers. If some part of the GUI does
 * not have its own controller, its events will be handled here.
 */
public class Controller implements Initializable {
    @FXML Parent root;
    @FXML private VBox attributeTab;
    @FXML private AttributeTabController attributeTabController;
    @FXML private ScrollPane textTreeTab;
    @FXML private TreeViewTabController textTreeTabController;
    @FXML private TopMenuController topMenuController;
    @FXML private GraphViewController graphViewTabController;
    @FXML private ConsoleController consoleController;
    @FXML private FilterTabController filterTabController;
    @FXML private ClassOverviewController classOverviewController;

    @FXML private VBox leftWindow;
    @FXML private VBox rightWindow;
    @FXML private VBox consoleWindow;

    @FXML
    private TabPane astViewTabs;

    @FXML private TabPane leftWindowContent;
    @FXML private SplitPane rightWindowContent;
    @FXML private VBox consoleWindowContent;


    @FXML
    private MinimizeButton minimizeLeftSide;
    @FXML
    private MinimizeButton minimizeRightSide;
    @FXML
    private MinimizeButton minimizeConsole;

    @FXML
    private SplitPane centerSplitPane;
    @FXML
    private SplitPane consoleAndGraphSplitPane;

    @FXML private Label appliedFiltersLabel;
    @FXML private Label appliedFiltersLabelLabel;
    @FXML private Label nodeCountLabel;


    private Monitor mon;
    private ArrayList<ControllerInterface> controllers;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Called by drastUI when the UI is created to initialize fields and event listeners in the UI.
     *
     * @param mon
     * @param graphView
     * @throws IOException
     */
    public void init(Monitor mon, GraphView graphView) throws IOException {
        this.mon = mon;

        controllers = new ArrayList<>();
        controllers.add(attributeTabController);
        controllers.add(textTreeTabController);
        controllers.add(topMenuController);
        controllers.add(graphViewTabController);
        controllers.add(consoleController);
        controllers.add(filterTabController);
        controllers.add(classOverviewController);

        for(ControllerInterface controller : controllers)
            controller.init(mon);

        // minimize buttons for each side bar
        minimizeLeftSide.setOnMouseClicked(event2 -> {
            if(event2.getButton() == MouseButton.PRIMARY) {
                if(minimizeLeftSide.minimizeNext())
                    minimizeLeftWindow();
                else
                    maximizeLeftWindow();
            }
        });
        minimizeRightSide.setOnMouseClicked(event2 -> {
            if (event2.getButton() == MouseButton.PRIMARY) {
                if(minimizeRightSide.minimizeNext()){
                    minimizeRightWindow();
                }else{
                    maximizeRightWindow();
                }
            }

        });
        minimizeConsole.setOnMouseClicked(event2 -> {
            if(event2.getButton() == MouseButton.PRIMARY) {
                if(minimizeConsole.minimizeNext()){
                    minimizeConsoleWindow();
                }else{
                    maximizeConsoleWindow();
                }
            }

        });


        // not working right now. The graph does not repaint when moving between the tabs
        astViewTabs.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> {
                    if (t1.getId().equals("graphViewTabNode")) {
                        graphView.repaintHard();
                    }
                }
        );

        updateAstInfoLabels();
    }

    private void maximizeLeftWindow(){

        if(minimizeLeftSide.isMinimized()) {
            minimizeLeftSide.setMinimized(false);
            leftWindow.getChildren().add(leftWindowContent);
            leftWindow.maxWidthProperty().bind(centerSplitPane.widthProperty());
            centerSplitPane.setDividerPosition(0, 0.2);
        }
    }

    private void maximizeRightWindow(){
        if(minimizeRightSide.isMinimized()) {
            minimizeRightSide.setMinimized(false);
            rightWindow.getChildren().add(rightWindowContent);
            rightWindow.maxWidthProperty().bind(centerSplitPane.widthProperty());
            centerSplitPane.setDividerPosition(1, 0.8);
        }

    }

    private void maximizeConsoleWindow(){
        if(minimizeConsole.isMinimized()) {
            minimizeConsole.setMinimized(false);
            consoleWindow.getChildren().add(consoleWindowContent);
            consoleWindow.maxHeightProperty().bind(consoleAndGraphSplitPane.heightProperty());
            consoleAndGraphSplitPane.setDividerPosition(0, 0.8);
        }
    }

    private void minimizeLeftWindow(){
        if(minimizeLeftSide.minimizeNext()) {
            minimizeLeftSide.setMinimized(true);
            leftWindow.getChildren().remove(leftWindowContent);
            leftWindow.maxWidthProperty().bind(minimizeLeftSide.widthProperty());
        }
    }

    private void minimizeRightWindow(){
        if(minimizeRightSide.minimizeNext()) {
            minimizeRightSide.setMinimized(true);
            rightWindow.getChildren().remove(rightWindowContent);
            rightWindow.maxWidthProperty().bind(minimizeRightSide.widthProperty());
        }
    }

    private void minimizeConsoleWindow(){
        if(minimizeConsole.minimizeNext()) {
            minimizeConsole.setMinimized(true);
            consoleWindow.getChildren().remove(consoleWindowContent);
            consoleWindow.maxHeightProperty().bind(minimizeConsole.heightProperty());
        }
    }

    /**
     * Under the graph and tree view there is a bar with labels. This method sets the right texts in these.
     */
    protected void updateAstInfoLabels(){
        nodeCountLabel.setText(mon.getBrain().getClusteredASTSize() + "/" + mon.getBrain().getASTSize() + ".");
        String filters = mon.getBrain().getAppliedFilters();

        if(filters == null) {
            appliedFiltersLabelLabel.setText("No filters.");
            appliedFiltersLabel.setText("");
        }else{
            appliedFiltersLabelLabel.setText("Filters: ");
            appliedFiltersLabel.setText(filters);
        }
    }

    public void toggleMinimizeWindows(){
        if(minimizeLeftSide.isMinimized() && minimizeRightSide.isMinimized() && minimizeConsole.isMinimized()){
            maximizeLeftWindow();
            maximizeRightWindow();
            maximizeConsoleWindow();
        }else{
            minimizeLeftWindow();
            minimizeRightWindow();
            minimizeConsoleWindow();
        }
    }

    public void onNewAPI(){
        controllers.forEach(ControllerInterface::onNewAPI);

        updateAstInfoLabels();
        updateGUI();
    }

    public void saveNewFilter(){
        //addMessage("Filter update: starting");
        if(mon.getBrain().getRoot() == null)
            return;

        long timeStart = System.currentTimeMillis();
        String filter = filterTabController.getFilterText();
        boolean noError = mon.getBrain().saveNewFilter(filter);
        if (noError) {
            updateGUI();
            addMessage("Filter update: done after " + (System.currentTimeMillis() - timeStart) + " ms");
            updateAstInfoLabels();
        } else {
            addWarning("The filter has errors and is not used. The previous filter is enabled. ");
        }
    }

    public void updateGUI(){
        resetReferences();
        controllers.forEach(ControllerInterface::updateGUI);
        updateAstInfoLabels();
    }

    /**
     * Shows a exit program dialog box and waits for it. If yes then close the program.
     */
    public void exitProgram(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Exit");
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText("Are you sure you want to quit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
        }
    }

    public void addMessage(String message) {
        consoleController.addMessage(message);
    }
    public void addError(String error){
        consoleController.addError(error);
    }

    public void addWarning(String warning) {
        consoleController.addWarning(warning);
    }

    /**
     * When a function is started,e.g. a dialog window is opened, this method is called.
     */
    public void functionStarted(){
        mon.functionStart();
        controllers.forEach(ControllerInterface::functionStarted);
    }

    /**
     * When a function is stopped,e.g. a dialog window is closed, this method is called.
     */
    public void functionStopped(){
        mon.functionDone();
        controllers.forEach(ControllerInterface::functionStopped);
    }

    /**
     * An attribute was selected for the selected node. This method tells different parts of the GUI of this event.
     * @param info the attribute selected.
     */
    public void attributeInNodeSelected(NodeInfo info){
        for(DrDialog subWindow : mon.getSubWindows())
            subWindow.attributeSelected(info);
    }

    /**
     * Method for selecting a node in the graph or tree. Used for example by other controllers.
     *
     * @param node The selected node.
     * @param caller the controller that called the method.
     */
    public void nodeSelected(GenericTreeNode node, ControllerInterface caller){
        for(DrDialog subWindow : mon.getSubWindows())
            subWindow.nodeSelected(node);

        mon.setSelectedNode(node);

        for(ControllerInterface controller : controllers) {
            if(controller != caller)
                controller.nodeSelected(node);
        }
    }

    public void runCompiler(DrASTView view, String jarPath, String filterPath, String[] args){
            final DrASTStarter setup = new DrASTStarter(view, jarPath, filterPath, args);
            LoadingDialog loadingDialog = new LoadingDialog(mon, "Waiting for compiler");

            Task<Boolean> task = new Task<Boolean>() {
                @Override public Boolean call() {
                    setup.setRootExecution(false);
                    setup.addObserver(consoleController);
                    return setup.run();
                }
            };
            loadingDialog.init();
            task.setOnRunning((e) -> loadingDialog.show());
            task.setOnSucceeded((e) -> {
                loadingDialog.closeDialog();
                setup.setRoot();
            });
            new Thread(task).start();
    }

    /**
     * Method for deselecting a node in the graph or tree. Used for example by other controllers.
     *
     * @param caller the controller that called the method.
     */
    public void nodeDeselected(ControllerInterface caller){

        mon.setSelectedNode(null);

        for(ControllerInterface controller : controllers) {
            if(controller != caller)
                controller.nodeDeselected();
        }
    }

    /**
     * Compute node references again.
     */
    public void resetReferences(){
        GenericTreeNode node = mon.getLastRealNode();
        if(node == null)
            return;
        node = mon.getBrain().getTreeNode(node.getNode().node);
        if(node == null)
            return;
        mon.setSelectedNode(node);
        graphViewTabController.nodeSelected(node.getTreeNode());
        if(mon.getSelectedInfo() != null)
            attributeTabController.setReference(mon.getSelectedInfo().getValue());
    }

    public GraphViewController getGraphViewTabController(){ return graphViewTabController;}
}
