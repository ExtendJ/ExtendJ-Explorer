package drast.views.gui.controllers;

import drast.model.AlertMessage;
import drast.model.filteredtree.GenericTreeNode;
import drast.model.nodeinfo.NodeInfo;
import drast.views.gui.Monitor;
import drast.views.gui.dialogs.DrDialog;
import drast.views.gui.graph.GraphView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * This is the main controller of the UI. It holds references to all sub controllers. If some part of the UI does
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

    @FXML
    private TabPane astViewTabs;

    @FXML
    private Button minimizeLeftSide;
    @FXML
    private Button minimizeRightSide;
    @FXML
    private Button minimizeConsole;

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
                if (centerSplitPane.getDividers().get(0).getPosition() < 0.05)
                    centerSplitPane.setDividerPosition(0, 0.2);
                else
                    centerSplitPane.setDividerPosition(0, 0);
            }
        });
        minimizeRightSide.setOnMouseClicked(event2 -> {
            if (event2.getButton() == MouseButton.PRIMARY) {
                if (centerSplitPane.getDividers().get(1).getPosition() > 0.95)
                    centerSplitPane.setDividerPosition(1, 0.8);
                else
                    centerSplitPane.setDividerPosition(1, 1);
            }

        });
        minimizeConsole.setOnMouseClicked(event2 -> {
            if(event2.getButton() == MouseButton.PRIMARY) {
                if (consoleAndGraphSplitPane.getDividers().get(0).getPosition() > 0.95)
                    consoleAndGraphSplitPane.setDividerPosition(0, 0.8);
                else
                    consoleAndGraphSplitPane.setDividerPosition(0, 1);
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

    /**
     * 
     */
    protected void updateAstInfoLabels(){
        nodeCountLabel.setText(mon.getApi().getClusteredASTSize() + "/" + mon.getApi().getASTSize() + ".");
        String filters = mon.getApi().getAppliedFilters();

        if(filters == null) {
            appliedFiltersLabelLabel.setText("No filters.");
            appliedFiltersLabel.setText("");
        }else{
            appliedFiltersLabelLabel.setText("Filters: ");
            appliedFiltersLabel.setText(filters);
        }
    }

    public void toggleMinimizeWindows(){
        if(centerSplitPane.getDividers().get(0).getPosition() < 0.05 &&
                centerSplitPane.getDividers().get(1).getPosition() > 0.95 &&
                consoleAndGraphSplitPane.getDividers().get(0).getPosition() > 0.95){
            centerSplitPane.setDividerPosition(0, 0.2);
            centerSplitPane.setDividerPosition(1, 0.8);
            consoleAndGraphSplitPane.setDividerPosition(0, 0.8);
        }else {
            centerSplitPane.setDividerPosition(0, 0);
            centerSplitPane.setDividerPosition(1, 1);
            consoleAndGraphSplitPane.setDividerPosition(0, 1);
        }
    }

    public void onNewAPI(){
        long timeStart = System.currentTimeMillis();
        mon.getDrASTAPI().run();
        if(mon.getApi().containsError(AlertMessage.AST_STRUCTURE_ERROR) || mon.getApi().containsError(AlertMessage.FILTER_ERROR)){
            addWarnings(mon.getApi().getWarnings(AlertMessage.FILTER_WARNING));
            addErrors(mon.getApi().getErrors(AlertMessage.FILTER_ERROR));
            addWarnings(mon.getApi().getWarnings(AlertMessage.AST_STRUCTURE_WARNING));
            addErrors(mon.getApi().getErrors(AlertMessage.AST_STRUCTURE_ERROR));
            return;
        }

        addMessages(mon.getApi().getMessages(AlertMessage.AST_STRUCTURE_WARNING));
        addWarnings(mon.getApi().getWarnings(AlertMessage.FILTER_WARNING));
        addMessages(mon.getApi().getMessages(AlertMessage.FILTER_MESSAGE));
        addMessage("Filter update: done after " + (System.currentTimeMillis() - timeStart) + " ms");

        for(ControllerInterface controller : controllers)
            controller.onNewAPI();

        updateAstInfoLabels();
        updateGUI();
    }

    public void saveNewFilter(){
        //addMessage("Filter update: starting");
        if(mon.getApi().getRoot() == null)
            return;

        long timeStart = System.currentTimeMillis();
        String filter = filterTabController.getFilterText();
        boolean noError = mon.getApi().saveNewFilter(filter);
        if (noError) {
            updateGUI();
            addMessages(mon.getApi().getMessages(AlertMessage.FILTER_MESSAGE));
            addWarnings(mon.getApi().getWarnings(AlertMessage.FILTER_WARNING));
            addMessage("Filter update: done after " + (System.currentTimeMillis() - timeStart) + " ms");
            updateAstInfoLabels();
        } else {
            //addError("Could not update graph: ");
            addWarnings(mon.getApi().getWarnings(AlertMessage.FILTER_WARNING));
            addErrors(mon.getApi().getErrors(AlertMessage.FILTER_ERROR));
            addWarning("New filter is not applied, old filter is enabled. ");
            //addMessage("Filter update: something is wrong!");
        }
    }

    public void updateGUI(){
        resetReferences();
        for(ControllerInterface controller : controllers){
            controller.updateGUI();
        }
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

    public void addErrors(Collection<AlertMessage> errors){
        consoleController.addErrors(errors);
    }

    public void addWarnings(Collection<AlertMessage> warnings){
        consoleController.addWarnings(warnings);
    }

    public void addMessages(Collection<AlertMessage> messages){
        consoleController.addMessages(messages);
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
        for(ControllerInterface controller : controllers){
            controller.functionStarted();
        }
    }

    /**
     * When a function is stopped,e.g. a dialog window is closed, this method is called.
     */
    public void functionStopped(){
        mon.functionDone();
        for(ControllerInterface controller : controllers){
            controller.functionStopped();
        }

        if(mon.getApi().containsError(AlertMessage.SETUP_FAILURE)){
            addErrors(mon.getApi().getErrors(AlertMessage.SETUP_FAILURE));
            return;
        }
    }

    /**
     * An attribute was selected for the selected node. This method tells different parts of the UI of this event.
     * @param info
     */
    public void attributeInNodeSelected(NodeInfo info){
        for(DrDialog subWindow : mon.getSubWindows())
            subWindow.attributeSelected(info);
    }

    /**
     * Method for selecting a node in the graph or tree. Used for example by other controllers.
     *
     * @param node
     * @param caller
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

    /**
     * Method for deselecting a node in the graph or tree. Used for example by other controllers.
     *
     * @param caller
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
        node = mon.getApi().getTreeNode(node.getNode().node);
        if(node == null)
            return;
        mon.setSelectedNode(node);
        graphViewTabController.nodeSelected(node.getClusterNode());
        if(mon.getSelectedInfo() != null)
            attributeTabController.setReference(mon.getSelectedInfo().getValue());
    }

}
