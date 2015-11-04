package uicomponent.controllers;

import jastaddad.filteredtree.GenericTreeNode;
import jastaddad.filteredtree.TreeNode;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import uicomponent.UIComponent;
import uicomponent.UIMonitor;
import uicomponent.graph.GraphView;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by gda10jth on 10/16/15.
 */
public class Controller implements Initializable {
    @FXML private VBox attributeTab;
    @FXML private AttributeTabController attributeTabController;
    @FXML private ScrollPane textTreeTab;
    @FXML private TextTreeTabController textTreeTabController;

    @FXML
    private TabPane graphViewTabs;
    @FXML
    private Button saveNewFilterButton;
    @FXML
    private Button minimizeLeftSide;
    @FXML
    private Button minimizeRightSide;
    @FXML
    private Button minimizeConsole;
    @FXML
    private TextArea filteredConfigTextArea;
    @FXML
    private TreeView<TmpTreeItem> typeListView;

    @FXML
    private SplitPane centerSplitPane;
    @FXML
    private SplitPane consoleAndGraphSplitPane;

    // Console stuff
    @FXML private TextFlow consoleTextFlowAll;
    @FXML private TextFlow consoleTextFlowWarning;
    @FXML private TextFlow consoleTextFlowError;
    @FXML private TextFlow consoleTextFlowMessage;

    @FXML private ScrollPane consoleScrollPaneAll;
    @FXML private ScrollPane consoleScrollPaneError;
    @FXML private ScrollPane consoleScrollPaneMessage;
    @FXML private ScrollPane consoleScrollPaneWarning;

    private DoubleProperty consoleHeightAll;
    private DoubleProperty consoleHeightError;
    private DoubleProperty consoleHeightWarning;
    private DoubleProperty consoleHeightMessage;

    private UIMonitor mon;
    private GraphView graphView;

    private enum ConsoleFilter {
        ALL, ERROR, WARNING, MESSAGE
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void init(UIMonitor mon, GraphView graphView, UIComponent uiComponent) throws IOException {
        this.mon = mon;
        this.graphView = graphView;

        attributeTabController.init(mon, graphView);
        textTreeTabController.init(mon);

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

        minimizeLeftSide.setOnAction((event1 -> {
            if(centerSplitPane.getDividers().get(0).getPosition() < 0.05)
                centerSplitPane.setDividerPosition(0,0.2);
            else
                centerSplitPane.setDividerPosition(0,0);
        }));
        minimizeRightSide.setOnAction((event1 -> {
            if(centerSplitPane.getDividers().get(1).getPosition() > 0.95)
                centerSplitPane.setDividerPosition(1,0.8);
            else
                centerSplitPane.setDividerPosition(1,1);

        }));
        minimizeConsole.setOnAction((event1 -> {
            if(consoleAndGraphSplitPane.getDividers().get(0).getPosition() > 0.95)
                consoleAndGraphSplitPane.setDividerPosition(0,0.8);
            else
                consoleAndGraphSplitPane.setDividerPosition(0,1);

        }));

        saveNewFilterButton.setOnAction((event) -> {
            addMessage("Update of filter: START");
            mon.getApi().saveNewFilter(filteredConfigTextArea.getText());
            graphView.updateGraph();
            textTreeTabController.updateTree();
            resetUI();
            if(mon.getSelectedNode() != null) {
                Platform.runLater(() -> textTreeTabController.newNodeSelected(mon.getSelectedNode()));
            }
            addMessage("Update of filter: DONE");
        });

        graphViewTabs.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> {
                    if(t1.getId().equals("graphViewTabNode")){
                        Platform.runLater(() -> {
                            graphView.repaint(); graphView.requestFocus();
                        });
                    } else if(t1.getId().equals("treeViewTabNode")){

                    }
                }
        );

        //centerSplitPane.setDividerPosition(1,0.5);
    }

    private void setConsoleScrollHeightListener(DoubleProperty consoleHeight, ScrollPane consoleScrollPane, TextFlow textFlow){
        consoleHeight.bind(textFlow.heightProperty());
        consoleHeight.addListener((ov, t, t1) -> {
            consoleScrollPane.setVvalue(consoleScrollPane.getVmax());
        }) ;
    }

    public void addMessage(String message){
        addConsoleText(message, "consoleTextMessage", ConsoleFilter.MESSAGE);
    }

    public void addError(String message){
        addConsoleText(message, "consoleTextError", ConsoleFilter.ERROR);
    }

    public void addWarning(String message){
        addConsoleText(message, "consoleTextWarning", ConsoleFilter.WARNING);
    }

    private void addConsoleText(String message, String style, ConsoleFilter filterType){
        Text text1 = new Text(message + "\n");
        Text text2 = new Text(message + "\n");
        text1.getStyleClass().add(style);
        text2.getStyleClass().add(style);
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

    public void newNodeSelected(GenericTreeNode node, boolean fromGraph){
        mon.setSelectedNode(node);
        mon.setReferenceNode(null);
        attributeTabController.setAttributes();
        if(fromGraph)
            textTreeTabController.newNodeSelected(node);
        else
            graphView.newNodeSelected(node);
    }

    public void nodeDeselected(boolean fromGraph){
        mon.setSelectedNode(null);
        mon.setReferenceNode(null);
        if(fromGraph)
            textTreeTabController.deselectNode();
        else
            graphView.deselectNode();
        attributeTabController.setAttributes();
    }

    private void loadFilterFileText() {
        String line;
        String textContent = "";
        int lineCount = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("filter.cfg"));
            while ((line = reader.readLine()) != null) {
                textContent += line + "\n";
                lineCount++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            textContent = "Can not read the configuration file!";
        }

        filteredConfigTextArea.setText(textContent);
        filteredConfigTextArea.setPrefColumnCount(lineCount);

    }

    private void resetUI(){
        GenericTreeNode node = getNode(mon.getSelectedNode(), true);
        mon.getController().addError("REAL : " + node);
        if(node == null)
            return;
        node = node.hasClusterReference() ? node.getClusterReference() : node;
        mon.setSelectedNode(node);
        graphView.setSelectedNode(node);
        node = getNode(mon.getReferenceNode(), false);
        mon.getController().addError("REF: " + node );
        if(node == null)
            return;
        mon.setReferenceNode(node);
        node.setReferenceHighlight(true);
        graphView.setReferenceEdge(mon.getReferenceNode(), mon.getSelectedNode());

    }

    private GenericTreeNode getNode(GenericTreeNode node, boolean real){
        TreeNode treeNode;
        if(mon.getLastRealNode() != null && real)
            treeNode = (TreeNode) mon.getLastRealNode();
        else if(node != null)
            treeNode = (TreeNode) node;
        else
            return null;
        return mon.getApi().getReferenceNode(treeNode.node.node);
    }

    private void loadClassTreeView(){
        ArrayList<TmpTreeItem> treeItems = new ArrayList<>();
        for (Map.Entry<String, Integer> config : mon.getApi().getTypeHash().entrySet()) {
            treeItems.add(new TmpTreeItem(config.getKey(), config.getValue()));
        }

        Collections.sort(treeItems);

        CheckBoxTreeItem<TmpTreeItem> root = new CheckBoxTreeItem<>(new TmpTreeItem("kuk", 1));
        root.setExpanded(true);

        CheckBoxTreeItem parent = root;
        for(TmpTreeItem item : treeItems){
            final CheckBoxTreeItem<TmpTreeItem> treeItem = new CheckBoxTreeItem<>(item);
            treeItem.setIndependent(true);
            treeItem.setSelected(item.enabled);

            treeItem.selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    mon.getApi().newTypeFiltered(treeItem.getValue().fullName, newValue);
                    graphView.updateGraph();
                }
            });

            if(item.name.length() <= 0) {
                parent.setExpanded(false);
                parent = treeItem;
                root.getChildren().add(treeItem);
            }else{
                parent.getChildren().add(treeItem);
            }
        }

        typeListView.setRoot(root);
        typeListView.setShowRoot(false);
        typeListView.setCellFactory(CheckBoxTreeCell.<TmpTreeItem>forTreeView());
    }

    private class TmpTreeItem implements Comparable<TmpTreeItem>{
        public final String name;
        public final String className;
        public final String fullName;
        public final Boolean enabled;

        public TmpTreeItem(String name, int enabled){
            this.enabled = enabled == 1;
            String[] parts = name.split(":");
            className = parts[0];
            if(parts.length > 1) {
                this.name = parts[1];
                fullName = name;
            }else {
                this.name = "";
                fullName = className;
            }
        }

        @Override
        public String toString() {
            return name.length() <= 0 ? className : name;
        }

        public int compareTo(TmpTreeItem item) {
            return (fullName).compareTo(item.fullName) ;
        }
    }
}
