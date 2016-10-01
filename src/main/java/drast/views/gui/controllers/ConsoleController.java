package drast.views.gui.controllers;

import drast.Log;
import drast.model.Message;
import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.GUIData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by gda10jth on 2/3/16.
 */
public class ConsoleController implements Initializable, ControllerInterface {
  private List<Message> messages;

  @FXML private TextFlow consoleTextFlow;
  @FXML private ScrollPane consoleScrollPane;

  @FXML private CheckBox allBox;
  @FXML private CheckBox messagesBox;
  @FXML private CheckBox warningsBox;
  @FXML private CheckBox errorsBox;

  @Override public void init(GUIData mon) {
    messages = new ArrayList<>();
  }

  @Override public void initialize(URL location, ResourceBundle resources) {
    Log.addReceiver(message -> Platform.runLater(() -> {
      messages.add(message);
      buildConsoleText();
    }));
    setConsoleScrollHeightListener(consoleScrollPane, consoleTextFlow);

    allBox.setOnAction(e -> buildConsoleText());
    messagesBox.setOnAction(e -> checkBoxClickEvent(messagesBox));
    warningsBox.setOnAction(e -> checkBoxClickEvent(warningsBox));
    errorsBox.setOnAction(e -> checkBoxClickEvent(errorsBox));
  }

  private void checkBoxClickEvent(CheckBox box) {
    if (!box.isSelected() && allBox.isSelected()) {
      allBox.setSelected(false);
    } else if (messagesBox.isSelected() && warningsBox.isSelected() && errorsBox.isSelected()) {
      allBox.setSelected(true);
    }
    buildConsoleText();
  }

  private void setConsoleScrollHeightListener(ScrollPane consoleScrollPane, TextFlow textFlow) {
    textFlow.heightProperty().addListener(observable -> {
      consoleScrollPane.setVvalue(consoleScrollPane.getVmax());
    });
  }

  private void buildConsoleText() {
    consoleTextFlow.getChildren().clear();
    messages.stream().filter(this::isVisible).forEach(message -> {
      Text text1 = new Text(message.message + "\n");
      text1.getStyleClass().add(getStyleString(message));
      text1.getStyleClass().add("consoleText");
      consoleTextFlow.getChildren().add(text1);
    });
    consoleScrollPane.setVvalue(consoleScrollPane.getVmax());
  }

  private boolean isVisible(Message message) {
    return allBox.isSelected() ||
        (errorsBox.isSelected() && message.isError()) ||
        (warningsBox.isSelected() && message.isWarning()) ||
        (messagesBox.isSelected() && message.isMessage());
  }

  private String getStyleString(Message message) {
    if (message.isError()) {
      return "consoleTextError";
    }
    if (message.isWarning()) {
      return "consoleTextWarning";
    }
    return "consoleTextMessage";
  }

  @Override public void onSetRoot() {
  }

  @Override public void nodeSelected(GenericTreeNode node) {
  }

  @Override public void nodeDeselected() {
  }

  @Override public void updateGUI() {
  }
}
