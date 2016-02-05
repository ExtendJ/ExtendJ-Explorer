package drast.views.gui.controllers;

import drast.model.AlertMessage;
import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.Monitor;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.*;

/**
 * Created by gda10jth on 2/3/16.
 */
public class ConsoleController implements Initializable, ControllerInterface, Observer {
    private Monitor mon;
    private ArrayList<AlertMessage> messages;


    // Console stuff
    @FXML private TextFlow consoleTextFlowAll;
    @FXML private TextFlow consoleTextFlowWarning;
    @FXML private TextFlow consoleTextFlowError;
    @FXML private TextFlow consoleTextFlowMessage;
    @FXML private TextFlow consoleTextFlowNewShit;

    @FXML private ScrollPane consoleScrollPaneAll;
    @FXML private ScrollPane consoleScrollPaneError;
    @FXML private ScrollPane consoleScrollPaneMessage;
    @FXML private ScrollPane consoleScrollPaneWarning;
    @FXML private ScrollPane consoleScrollPaneNewShit;

    @FXML private CheckBox allBox;
    @FXML private CheckBox messagesBox;
    @FXML private CheckBox warningsBox;
    @FXML private CheckBox errorsBox;

    @Override
    public void onNewAPI() {
        mon.getBrain().addObserver(this);
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
        ALL, ERROR, WARNING, MESSAGE, NEWSHIT
    }

    private DoubleProperty consoleHeightAll;
    private DoubleProperty consoleHeightError;
    private DoubleProperty consoleHeightWarning;
    private DoubleProperty consoleHeightMessage;
    private DoubleProperty consoleHeightNewShit;

    public void init(Monitor mon){
        this.mon = mon;
        mon.getBrain().addObserver(this);
        messages = new ArrayList<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        consoleHeightAll = new SimpleDoubleProperty();
        consoleHeightError = new SimpleDoubleProperty();
        consoleHeightWarning = new SimpleDoubleProperty();
        consoleHeightMessage = new SimpleDoubleProperty();
        consoleHeightNewShit = new SimpleDoubleProperty();

        setConsoleScrollHeightListener(consoleHeightAll, consoleScrollPaneAll, consoleTextFlowAll);
        setConsoleScrollHeightListener(consoleHeightError, consoleScrollPaneError, consoleTextFlowError);
        setConsoleScrollHeightListener(consoleHeightWarning, consoleScrollPaneWarning, consoleTextFlowWarning);
        setConsoleScrollHeightListener(consoleHeightMessage, consoleScrollPaneMessage, consoleTextFlowMessage);
        setConsoleScrollHeightListener(consoleHeightNewShit, consoleScrollPaneNewShit, consoleTextFlowNewShit);
    }

    private void setConsoleScrollHeightListener(DoubleProperty consoleHeight, ScrollPane consoleScrollPane, TextFlow textFlow){
        consoleHeight.bind(textFlow.heightProperty());
        consoleHeight.addListener((ov, t, t1) -> {
            consoleScrollPane.setVvalue(consoleScrollPane.getVmax());
        }) ;
    }

    public void addErrors(Collection<AlertMessage> errors){
        addConsoleTexts(errors);
    }

    public void addWarnings(Collection<AlertMessage> warnings){
        addConsoleTexts(warnings);
    }

    public void addMessages(Collection<AlertMessage> messages){
        addConsoleTexts(messages);
    }

    public void addMessage(String message) {
        Platform.runLater(() -> addMessage(new AlertMessage(AlertMessage.VIEW_MESSAGE, message)));
    }
    public void addError(String message){
        Platform.runLater(() -> addMessage(new AlertMessage(AlertMessage.VIEW_ERROR, message)));
    }

    public void addWarning(String message) {
        Platform.runLater(() -> addMessage(new AlertMessage(AlertMessage.VIEW_WARNING, message)));
    }

    private void addConsoleTexts(Collection<AlertMessage> messages){
        Platform.runLater(() -> {
            for (AlertMessage message : messages)
                addMessage(message);
                //addConsoleText(warning.type + ": " + warning.message, console, filter);
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o != mon.getBrain())
            return;
        addMessage((AlertMessage)arg);
    }

    public void addMessage(AlertMessage message){
        messages.add(message);
        buildConsoleText();
    }

    private void buildConsoleText(){
        consoleTextFlowNewShit.getChildren().clear();
        for(AlertMessage message : messages){
            if(isVisible(message)){
                Text text1 = new Text(message.message + "\n");
                text1.getStyleClass().add(getStyleString(message));
                text1.getStyleClass().add("consoleText");
                consoleTextFlowNewShit.getChildren().add(text1);
                consoleScrollPaneNewShit.setVvalue(1.0);

                //addConsoleText(message.message, getStyleString(message), ConsoleFilter.NEWSHIT);
            }
        }
    }

    private boolean isVisible(AlertMessage message){
        return allBox.isSelected() ||
                (errorsBox.isSelected() && message.isError()) ||
                (warningsBox.isSelected() && message.isWarning()) ||
                (messagesBox.isSelected() && message.isMessage());
    }

    private String getStyleString(AlertMessage message){
        if(message.isError()) return "consoleTextError";
        if(message.isWarning()) return "consoleTextWarning";
        return "consoleTextMessage";
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
            case NEWSHIT:
                return consoleTextFlowNewShit;
            default:
                return consoleTextFlowAll;
        }
    }
}
