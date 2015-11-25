package jastaddad.ui;

import jastaddad.api.filteredtree.GenericTreeNode;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.util.ArrayList;

/**
 * Created by gda10jth on 11/24/15.
 */
public abstract class UIDialog extends Stage{
    protected boolean invokeButtonPressed;
    protected UIMonitor mon;
    protected Button buttonTypeOk;

    protected UIDialog(UIMonitor mon){
        super(StageStyle.UTILITY);
        this.mon = mon;
        mon.getController().functionStarted();
        invokeButtonPressed = false;

        mon.addSubWindow(this);

        initModality(Modality.WINDOW_MODAL);
        setAlwaysOnTop(true);
        initOwner(mon.getParentStage());
        setResizable(false);

        setOnHidden(event -> {
            mon.removeSubWindow(this);
            dialogClose();
            mon.getController().functionStoped();
        });
    }

    public void init(){
        buttonTypeOk = new Button("yes");
        buttonTypeOk.setOnMouseClicked(event -> {
            invokeButtonPressed = true;
            yesButtonClicked();
            fireEvent(
                new WindowEvent(
                    this,
                    WindowEvent.WINDOW_CLOSE_REQUEST
                )
            );

        });
        setScene(new Scene(buildDialogContent()));
    }
    protected abstract void yesButtonClicked();
    protected abstract void dialogClose();
    public abstract ArrayList<Object> getResult();
    protected abstract Parent buildDialogContent();

    public void nodeSelected(GenericTreeNode node){
        if(!invokeButtonPressed)
            nodeSelectedChild(node);
    }

    protected abstract void nodeSelectedChild(GenericTreeNode node);
}
