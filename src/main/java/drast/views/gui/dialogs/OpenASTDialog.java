package drast.views.gui.dialogs;

import drast.model.DrASTSettings;
import drast.model.filteredtree.GenericTreeNode;
import drast.model.terminalvalues.TerminalValue;
import drast.views.gui.GUIData;
import drast.views.gui.guicomponent.nodeinfotreetableview.TerminalValueTreeItemView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by gda10jth on 1/15/16.
 */
public class OpenASTDialog extends DrDialog
    implements Initializable, ChangeListener<TreeItem<TerminalValueTreeItemView>> {
  private TextField jarField;
  private TextField arg1Field;
  private TextField args;
  private String[] allArgs;

  public OpenASTDialog(GUIData mon) {
    super(mon, StageStyle.UNIFIED);
    initModality(Modality.NONE);
    setTitle("Open compiler...");

    buildDialogContent(parentNode);
  }

  @Override protected boolean yesButtonClicked() {
    allArgs = (arg1Field.getText().isEmpty() ?
        args.getText() :
        arg1Field.getText() + " " + args.getText()).split(" ");
    if (allArgs[0].equals("")) {
      allArgs = new String[0];
    }

    boolean done = true;

    File jarFile = new File(jarField.getText());
    if (!jarFile.exists()) {
      jarField.setStyle("-fx-background-color: #ffcccc");
      done = false;
    }
    return done;
  }

  @Override protected void dialogClose() {
    if (yesButtonPressed) {
      mon.getController().runCompiler(mon.getDrASTUI(), jarField.getText(), allArgs);
    }
  }

  private void buildDialogContent(VBox parent) {
    parent.getStyleClass().add("our_root");
    parent.setFillWidth(true);
    VBox fieldContainer = new VBox();
    fieldContainer.getStyleClass().add("top_parent");
    fieldContainer.setFillWidth(true);

    Label label1 = new Label("Path to compiler jar:");
    HBox row1 = new HBox();
    row1.getStyleClass().add("out_row");
    jarField = new TextField();
    jarField.setText(DrASTSettings.get(DrASTSettings.PREV_JAR, ""));
    Button jarButton = new Button("...");
    row1.getChildren().addAll(jarField, jarButton);

    Separator separatorArg = new Separator();

    Label label3 = new Label("First compiler argument:");
    HBox row3 = new HBox();
    row3.getStyleClass().add("out_row");
    arg1Field = new TextField();
    arg1Field.setText(DrASTSettings.get(DrASTSettings.PREV_FIRST_ARG, ""));
    Button arg1Button = new Button("...");
    row3.getChildren().addAll(arg1Field, arg1Button);


    Label label4 = new Label("Additional arguments:");
    HBox row4 = new HBox();
    row4.getStyleClass().add("out_row");
    args = new TextField();
    args.setText(DrASTSettings.get(DrASTSettings.PREV_TAIL_ARGS, ""));
    row4.getChildren().add(args);

    Separator separator = new Separator();

    fieldContainer.getChildren()
        .addAll(label1, row1, separatorArg, label3, row3, label4, row4);
    parent.getChildren().addAll(fieldContainer, separator);

    buttonTypeOk.setText("Open");
    buttonTypeOk.getStyleClass().add("done_button");
    parent.getChildren().add(buttonTypeOk);

    jarButton.setOnAction(e -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open Jar File");
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JAR", "*.jar"));

      File file = fileChooser.showOpenDialog(getScene().getWindow());
      if (file != null) {
        jarField.setText(file.getAbsolutePath());
      }
    });

    arg1Button.setOnAction(e -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Open Input File");
      File prevFile = new File(arg1Field.getText());
      if (prevFile.isFile() && prevFile.getParentFile() != null
          && prevFile.getParentFile().isDirectory()) {
        fileChooser.setInitialDirectory(prevFile.getParentFile());
      }
      File file = fileChooser.showOpenDialog(getScene().getWindow());
      if (file != null) {
        arg1Field.setText(file.getAbsolutePath());
      }
    });
  }

  @Override protected void loadStyleSheets(Scene scene) {
    scene.getStylesheets().add("/style/dialog.css");
  }

  @Override public void attributeSelected(TerminalValue info) {

  }

  @Override protected void nodeSelectedChild(GenericTreeNode node) {

  }

  @Override
  public void changed(ObservableValue<? extends TreeItem<TerminalValueTreeItemView>> observable,
      TreeItem<TerminalValueTreeItemView> oldValue, TreeItem<TerminalValueTreeItemView> newValue) {

  }

  @Override public void initialize(URL location, ResourceBundle resources) {

  }
}
