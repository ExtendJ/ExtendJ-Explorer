package drast.views.gui.dialogs;

import drast.model.filteredtree.GenericTreeNode;
import drast.model.terminalvalues.TerminalValue;
import drast.views.gui.GUIData;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

/**
 * Created by gda10jth on 2/17/16.
 */
public class LoadingDialog extends DrDialog {

  public LoadingDialog(GUIData mon) {
    super(mon, StageStyle.UNDECORATED);
    initModality(Modality.NONE);

    buildDialogContent(parentNode);
  }

  @Override protected boolean yesButtonClicked() {
    return false;
  }

  @Override protected void loadStyleSheets(Scene scene) {
    scene.getStylesheets().add("/style/loadingDialog.css");
  }

  @Override protected void dialogClose() {
  }

  private void buildDialogContent(VBox parent) {
    parent.getStyleClass().add("loading_parent");
    Label label = new Label("Waiting for compiler...");
    label.getStyleClass().add("loading_label");
    parent.getChildren().add(label);
  }

  @Override public void attributeSelected(TerminalValue info) {

  }

  @Override protected void nodeSelectedChild(GenericTreeNode node) {

  }
}
