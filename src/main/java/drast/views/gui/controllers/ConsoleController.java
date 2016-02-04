package drast.views.gui.controllers;

import drast.model.AlertMessage;
import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.Monitor;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

/**
 * Created by gda10jth on 2/3/16.
 */
public class ConsoleController implements Initializable, ControllerInterface {
    private Monitor mon;

    // Console stuff
    @FXML private TextFlow consoleTextFlowAll;
    @FXML private TextFlow consoleTextFlowWarning;
    @FXML private TextFlow consoleTextFlowError;
    @FXML private TextFlow consoleTextFlowMessage;

    @FXML private ScrollPane consoleScrollPaneAll;
    @FXML private ScrollPane consoleScrollPaneError;
    @FXML private ScrollPane consoleScrollPaneMessage;
    @FXML private ScrollPane consoleScrollPaneWarning;

    @Override
    public void onNewAPI() {

    }

    @Override
    public void functionStarted() {

    }

    @Override
    public void functionStopped() {

    }

    @Override
    public void nodeSelected(GenericTreeNode node) {

    }

    @Override
    public void nodeDeselected() {

    }

    @Override
    public void updateGUI() {

    }

    private enum ConsoleFilter {
        ALL, ERROR, WARNING, MESSAGE
    }

    private DoubleProperty consoleHeightAll;
    private DoubleProperty consoleHeightError;
    private DoubleProperty consoleHeightWarning;
    private DoubleProperty consoleHeightMessage;

    public void init(Monitor mon){
        this.mon = mon;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        consoleHeightAll = new SimpleDoubleProperty();
        consoleHeightError = new SimpleDoubleProperty();
        consoleHeightWarning = new SimpleDoubleProperty();
        consoleHeightMessage = new SimpleDoubleProperty();

        setConsoleScrollHeightListener(consoleHeightAll, consoleScrollPaneAll, consoleTextFlowAll);
        setConsoleScrollHeightListener(consoleHeightError, consoleScrollPaneError, consoleTextFlowError);
        setConsoleScrollHeightListener(consoleHeightWarning, consoleScrollPaneWarning, consoleTextFlowWarning);
        setConsoleScrollHeightListener(consoleHeightMessage, consoleScrollPaneMessage, consoleTextFlowMessage);

        Platform.runLater(() -> {
            addWarnings(mon.getApi().getWarnings(AlertMessage.AST_STRUCTURE_WARNING));
            addWarnings(mon.getApi().getWarnings(AlertMessage.INVOCATION_WARNING));
            addWarnings(mon.getApi().getWarnings(AlertMessage.FILTER_WARNING));
            addErrors(mon.getApi().getErrors(AlertMessage.AST_STRUCTURE_ERROR));
            addErrors(mon.getApi().getErrors(AlertMessage.INVOCATION_ERROR));
            addErrors(mon.getApi().getErrors(AlertMessage.FILTER_ERROR));
        });
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

    /**
     * get the array with messages of a certain type (e.g. MESSAGE, ERROR, WARNING).
     *
     * @param filterType
     * @return
     */
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
}
