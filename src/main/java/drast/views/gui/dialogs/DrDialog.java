package drast.views.gui.dialogs;

import drast.model.filteredtree.GenericTreeNode;
import drast.model.terminalvalues.TerminalValue;
import drast.views.gui.GUIData;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by gda10jth on 11/24/15.
 * This class is used as a parent to dialogs in the program. It has a "yes" button that invoke a method
 * yesButtonClicked() in the child class.
 */
public abstract class DrDialog extends Stage {
  final VBox parentNode;
  boolean yesButtonPressed;
  final GUIData mon;
  final Button buttonTypeOk;

  DrDialog(GUIData mon, StageStyle style) {
    super(style);
    this.mon = mon;
    mon.getController().functionStarted();
    yesButtonPressed = false;


    mon.addSubWindow(this);

    initModality(Modality.WINDOW_MODAL);
    setAlwaysOnTop(true);
    initOwner(mon.getParentStage());
    setResizable(false);

    setOnHidden(event -> {
      mon.removeSubWindow(this);
      dialogClose();
      mon.getController().functionStopped();
    });

    buttonTypeOk = new Button("yes");
    buttonTypeOk.setOnMouseClicked(event -> clickYesButton());
    parentNode = new VBox();
    parentNode.setOnKeyPressed(ke -> {
      if (ke.getCode().equals(KeyCode.ESCAPE)) {
        hide();
      } else if (ke.getCode().equals(KeyCode.ENTER)) {
        clickYesButton();
      }
    });
    Scene scene = new Scene(parentNode);
    loadStyleSheets(scene);
    setScene(scene);
  }

  private void clickYesButton() {
    if (yesButtonClicked()) {
      yesButtonPressed = true;
      hide();
    }
  }

  protected abstract boolean yesButtonClicked();

  protected abstract void loadStyleSheets(Scene scene);

  protected abstract void dialogClose();

  public abstract void attributeSelected(TerminalValue info);

  public void nodeSelected(GenericTreeNode node) {
    if (!yesButtonPressed) {
      nodeSelectedChild(node);
    }
  }

  protected abstract void nodeSelectedChild(GenericTreeNode node);


}
