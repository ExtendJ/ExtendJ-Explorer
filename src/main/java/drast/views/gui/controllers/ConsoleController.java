package drast.views.gui.controllers;

import drast.model.AlertMessage;
import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.CustomOutputStream;
import drast.views.gui.Monitor;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.PrintStream;
import java.net.URL;
import java.util.*;

/**
 * Created by gda10jth on 2/3/16.
 */
public class ConsoleController implements Initializable, ControllerInterface, Observer {
    private Monitor mon;
    private ArrayList<AlertMessage> messages;


    // Console stuff
    @FXML private TextFlow consoleTextFlow;
    @FXML private ScrollPane consoleScrollPane;

    @FXML private CheckBox allBox;
    @FXML private CheckBox messagesBox;
    @FXML private CheckBox warningsBox;
    @FXML private CheckBox errorsBox;

    private DoubleProperty consoleHeightNewShit;

    public void init(Monitor mon){
        this.mon = mon;
        mon.getBrain().addObserver(this);
        messages = new ArrayList<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        consoleHeightNewShit = new SimpleDoubleProperty();

        setConsoleScrollHeightListener(consoleHeightNewShit, consoleScrollPane, consoleTextFlow);

        allBox.setOnAction(e-> buildConsoleText() );
        messagesBox.setOnAction(e->checkBoxClickEvent(messagesBox));
        warningsBox.setOnAction(e->checkBoxClickEvent(warningsBox));
        errorsBox.setOnAction(e->checkBoxClickEvent(errorsBox));
    }

    private void checkBoxClickEvent(CheckBox box){
        if (!box.isSelected() && allBox.isSelected())
            allBox.setSelected(false);
        else if (messagesBox.isSelected() && warningsBox.isSelected() && errorsBox.isSelected())
            allBox.setSelected(true);
        buildConsoleText();
    }

    private void setConsoleScrollHeightListener(DoubleProperty consoleHeight, ScrollPane consoleScrollPane, TextFlow textFlow){
        consoleHeight.bind(textFlow.heightProperty());
        consoleHeight.addListener((ov, t, t1) -> {
            consoleScrollPane.setVvalue(consoleScrollPane.getVmax());
        }) ;
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

    @Override
    public void update(Observable o, Object arg) {
        if(!(arg instanceof AlertMessage))
            return;
        Platform.runLater(() -> addMessage((AlertMessage) arg));
    }

    public void addMessage(AlertMessage message){
        messages.add(message);
        buildConsoleText();
    }

    private void buildConsoleText(){
        consoleTextFlow.getChildren().clear();
        for(AlertMessage message : messages){
            if(isVisible(message)){
                Text text1 = new Text(message.message + "\n");
                text1.getStyleClass().add(getStyleString(message));
                text1.getStyleClass().add("consoleText");
                consoleTextFlow.getChildren().add(text1);
                //addConsoleText(message.message, getStyleString(message), ConsoleFilter.NEWSHIT);
            }
        }
        consoleScrollPane.setVvalue(consoleScrollPane.getVmax());
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

    @Override
    public void onNewAPI() {
        mon.getBrain().addObserver(this);
    }

    @Override
    public void onApplicationClose(){}

    @Override
    public void functionStarted() {}

    @Override
    public void functionStopped() {}

    @Override
    public void nodeSelected(GenericTreeNode node) {}

    @Override
    public void nodeDeselected() {}

    @Override
    public void updateGUI() {}
}
