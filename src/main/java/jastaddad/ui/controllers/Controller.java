package jastaddad.ui.controllers;

import jastaddad.api.ASTAPI;
import jastaddad.api.AlertMessage;
import jastaddad.api.filteredtree.GenericTreeNode;
import jastaddad.api.filteredtree.TreeNode;
import jastaddad.api.nodeinfo.NodeInfo;
import jastaddad.ui.UIDialog;
import jastaddad.ui.UIMonitor;
import jastaddad.ui.graph.GraphView;
import jastaddad.ui.uicomponent.FilterEditor;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static org.fxmisc.wellbehaved.event.EventPattern.keyPressed;

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

    @FXML
    private TabPane graphViewTabs;
    @FXML
    private Button saveNewFilterButton;
    @FXML
    private Button showRootNodeButton;
    @FXML
    private Button showSelectedNodeButton;
    @FXML
    private Button minimizeLeftSide;
    @FXML
    private Button minimizeRightSide;
    @FXML
    private Button minimizeConsole;
    @FXML
    private Button showWholeGraphButton;
    @FXML
    private TextArea filteredConfigTextArea;
    @FXML
    private TreeView<String> typeListView;

    @FXML
    private SplitPane centerSplitPane;
    @FXML
    private SplitPane consoleAndGraphSplitPane;
    @FXML
    private VBox codeAreaContainer;

    // Console stuff
    @FXML private TextFlow consoleTextFlowAll;
    @FXML private TextFlow consoleTextFlowWarning;
    @FXML private TextFlow consoleTextFlowError;
    @FXML private TextFlow consoleTextFlowMessage;

    @FXML private ScrollPane consoleScrollPaneAll;
    @FXML private ScrollPane consoleScrollPaneError;
    @FXML private ScrollPane consoleScrollPaneMessage;
    @FXML private ScrollPane consoleScrollPaneWarning;

    private FilterEditor codeArea;

    private enum ConsoleFilter {
        ALL, ERROR, WARNING, MESSAGE
    }


    private DoubleProperty consoleHeightAll;
    private DoubleProperty consoleHeightError;
    private DoubleProperty consoleHeightWarning;
    private DoubleProperty consoleHeightMessage;

    private UIMonitor mon;
    private GraphView graphView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        codeArea = new FilterEditor();
        codeAreaContainer.getChildren().add(codeArea);
        codeArea.getStyleClass().add("textAreaConfig");
    }

    /**
     * Called by JastAddAdUI when the UI is created to initialize fields and event listeners in the UI.
     *
     * @param mon
     * @param graphView
     * @throws IOException
     */
    public void init(UIMonitor mon, GraphView graphView) throws IOException {
        this.mon = mon;
        this.graphView = graphView;

        attributeTabController.init(mon, graphView);
        textTreeTabController.init(mon);
        topMenuController.init(mon, graphView);

        loadClassTreeView();
        loadFilterFileText();

        consoleHeightAll = new SimpleDoubleProperty();
        consoleHeightError = new SimpleDoubleProperty();
        consoleHeightWarning = new SimpleDoubleProperty();
        consoleHeightMessage = new SimpleDoubleProperty();

        setConsoleScrollHeightListener(consoleHeightAll, consoleScrollPaneAll, consoleTextFlowAll);
        setConsoleScrollHeightListener(consoleHeightError, consoleScrollPaneError, consoleTextFlowError);
        setConsoleScrollHeightListener(consoleHeightWarning, consoleScrollPaneWarning, consoleTextFlowWarning);
        setConsoleScrollHeightListener(consoleHeightMessage, consoleScrollPaneMessage, consoleTextFlowMessage);

        // hide/show sidebars
        centerSplitPane.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.F)){
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
        });

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

            // update the new filter. This is done in the API
            saveNewFilterButton.setOnAction((event) -> {
                //addMessage("Filter update: starting");
                graphView.getJungGraph();
                long timeStart = System.currentTimeMillis();
                String filter = codeArea.getText();
                boolean noError = mon.getApi().saveNewFilter(filter);
                if (noError) {
                    updateUI();
                    addWarnings(mon.getApi().getWarnings(ASTAPI.FILTER_WARNING));
                    addMessage("Filter update: done after, " + (System.currentTimeMillis() - timeStart) + " ms");
                    addMessage("Number of nodes : " + mon.getApi().getASTSize());
                } else {
                    //addError("Could not update graph: ");
                    addWarnings(mon.getApi().getWarnings(ASTAPI.FILTER_WARNING));
                    addErrors(mon.getApi().getErrors(ASTAPI.FILTER_ERROR));
                    addWarning("New filter is not applied, old filter is enabled. ");
                    //addMessage("Filter update: something is wrong!");
                }

            });

            // not working right now. The graph does not repaint when moving between the tabs
            graphViewTabs.getSelectionModel().selectedItemProperty().addListener(
                    (ov, t, t1) -> {
                        if (t1.getId().equals("graphViewTabNode")) {
                            Platform.runLater(() -> {
                                graphView.repaint();
                                graphView.requestFocus();
                            });
                        } else if (t1.getId().equals("treeViewTabNode")) {

                        }
                    }
            );

            Platform.runLater(() -> {
                addWarnings(mon.getApi().getWarnings(ASTAPI.AST_STRUCTURE_WARNING));
                addWarnings(mon.getApi().getWarnings(ASTAPI.INVOCATION_WARNING));
                addWarnings(mon.getApi().getWarnings(ASTAPI.FILTER_WARNING));
                addErrors(mon.getApi().getErrors(ASTAPI.AST_STRUCTURE_ERROR));
                addErrors(mon.getApi().getErrors(ASTAPI.INVOCATION_ERROR));
                addErrors(mon.getApi().getErrors(ASTAPI.FILTER_ERROR));
            });
        showRootNodeButton.setOnAction(click -> {
            graphView.panToNode(mon.getRootNode());
        });
        showSelectedNodeButton.setOnAction(click -> {
            if (mon.getSelectedNode() != null)
                graphView.panToNode(mon.getSelectedNode());
        });
        showWholeGraphButton.setOnAction(click -> {

            graphView.showWholeGraphOnScreen();
        });
    }

    public void updateUI(){
        graphView.updateGraph();
        textTreeTabController.updateTree();
        resetReferences();
        if (mon.getSelectedNode() != null) {
            Platform.runLater(() -> textTreeTabController.newNodeSelected(mon.getSelectedNode()));
        }
    }

    public void exitProgram(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Exit");
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText("Are you sure you want to quit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
        } else {
        };
    }

    private void setConsoleScrollHeightListener(DoubleProperty consoleHeight, ScrollPane consoleScrollPane, TextFlow textFlow){
        consoleHeight.bind(textFlow.heightProperty());
        consoleHeight.addListener((ov, t, t1) -> {
            consoleScrollPane.setVvalue(consoleScrollPane.getVmax());
        }) ;
    }

    public void addErrors(Collection<AlertMessage> errors){
        addConsoleTexts("consoleTextError", ConsoleFilter.ERROR, errors);
    }

    public void addWarnings(Collection<AlertMessage> warnings){
        addConsoleTexts("consoleTextWarning", ConsoleFilter.WARNING, warnings);
    }

    public void addMessages(Collection<AlertMessage> messages){
        addConsoleTexts("consoleTextMessage", ConsoleFilter.MESSAGE, messages);
    }

    public void addMessage(String message) {
        Platform.runLater(() -> addConsoleText(message, "consoleTextMessage", ConsoleFilter.MESSAGE));
    }
    public void addError(String message){
        Platform.runLater(() -> addConsoleText(message, "consoleTextError", ConsoleFilter.ERROR));
    }

    public void addWarning(String message) {
        Platform.runLater(() -> addConsoleText(message, "consoleTextWarning", ConsoleFilter.WARNING));
    }

    private void addConsoleTexts(String console, ConsoleFilter filter, Collection<AlertMessage> warnings){
        Platform.runLater(() -> {
            for (AlertMessage warning : warnings)
                addConsoleText(warning.type + ": " + warning.message, console, filter);
        });
    }
    /**
     * used by the public methods addMessage, addError, addWarning
     *
     * @param message
     * @param style
     * @param filterType
     */
    private void addConsoleText(String message, String style, ConsoleFilter filterType){
        Text text1 = new Text(message + "\n");
        Text text2 = new Text(message + "\n");
        text1.getStyleClass().add(style);
        text1.getStyleClass().add("consoleText");
        text2.getStyleClass().add(style);
        text2.getStyleClass().add("consoleText");
        getConsoleArray(filterType).getChildren().add(text1);
        if(filterType != ConsoleFilter.ALL)
            consoleTextFlowAll.getChildren().add(text2);

        consoleScrollPaneAll.setVvalue(1.0);
    }

    private TextFlow getConsoleArray(ConsoleFilter filterType){
        switch (filterType) {
            case MESSAGE:
                return consoleTextFlowMessage;
            case ERROR:
                return consoleTextFlowError;
            case WARNING:
                return consoleTextFlowWarning;
            default:
                return consoleTextFlowAll;
        }
    }

    /**
     * When a function is started,e.g. a dialog window is opened, this method is called.
     */
    public void functionStarted(){
        mon.functionStart();
        attributeTabController.functionStarted();
        textTreeTabController.functionStarted();
        topMenuController.functionStarted();
    }

    /**
     * When a function is stopped,e.g. a dialog window is closed, this method is called.
     */
    public void functionStopped(){
        mon.functionDone();
        attributeTabController.functionStopped();
        textTreeTabController.functionStopped();
        topMenuController.functionStopped();
    }

    /**
     * An attribute was selected for the selected node. This method tells different parts of the UI of this event.
     * @param info
     */
    public void attributeInNodeSelected(NodeInfo info){
        for(UIDialog subWindow : mon.getSubWindows())
            subWindow.attributeSelected(info);
    }

    /**
     * Method for selecting a node in the graph or tree. Used for example by other controllers.
     *
     * @param node
     * @param fromGraph
     */
    public void nodeSelected(GenericTreeNode node, boolean fromGraph){
        for(UIDialog subWindow : mon.getSubWindows())
            subWindow.nodeSelected(node);
        mon.setSelectedNode(node);
        attributeTabController.nodeSelected();
        if(fromGraph)
            textTreeTabController.newNodeSelected(node);
        else
            graphView.setSelectedNode(node);
    }

    /**
     * Method for deselecting a node in the graph or tree. Used for example by other controllers.
     *
     * @param fromGraph
     */
    public void nodeDeselected(boolean fromGraph){
        mon.setSelectedNode(null);
        if(fromGraph)
            textTreeTabController.deselectNode();
        else
            graphView.deselectNode();
        attributeTabController.setAttributes();
    }

    /**
     * Load and print the filter text in the textarea for filter text
     */
    private void loadFilterFileText() {
        String line;
        String textContent = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(mon.getApi().getFilterFilePath()));
            while ((line = reader.readLine()) != null) {
                textContent += line + "\n";
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            textContent = "Can not read the configuration file!";
        }
        codeArea.setText(textContent);

    }

    /**
     * Compute node references again.
     */
    public void resetReferences(){
        GenericTreeNode node = mon.getLastRealNode();
        if(node == null)
            return;
        node = mon.getApi().getTreeNode(((TreeNode) node).getNode().node);
        if(node == null)
            return;
        mon.setSelectedNode(node);
        graphView.setSelectedNode(node.getClusterNode());
        if(mon.getSelectedInfo() != null)
            attributeTabController.setReference(mon.getSelectedInfo().getValue());
    }

    private void loadClassTreeView(){

        TreeItem<String> root = new TreeItem<>("ROOT");
        root.setExpanded(true);

        for (Map.Entry<Class, HashSet<Class>> chains : mon.getApi().getParentChains().entrySet()) {
            TreeItem<String> parent = new TreeItem<>(chains.getKey().getSimpleName());
            for(Class parentClass : chains.getValue()){
                parent.getChildren().add(new TreeItem<>(parentClass.getSimpleName()));
            }
            root.getChildren().add(parent);
        }
        typeListView.setRoot(root);
        typeListView.setShowRoot(false);
    }

}
