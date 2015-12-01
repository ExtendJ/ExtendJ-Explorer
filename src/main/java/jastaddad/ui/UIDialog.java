package jastaddad.ui;

import jastaddad.api.filteredtree.GenericTreeNode;
import jastaddad.api.nodeinfo.NodeInfo;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
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
        super(StageStyle.UNIFIED);
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
            if(yesButtonClicked()) {
                invokeButtonPressed = true;
                closeDialog();
            }

        });
        Parent parent = buildDialogContent();
        parent.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ESCAPE)){
                closeDialog();
            }
        });
        setScene(new Scene(parent));
    }

    public void closeDialog(){
        fireEvent(
            new WindowEvent(
                    this,
                    WindowEvent.WINDOW_CLOSE_REQUEST
            )
        );
    }
    protected abstract boolean yesButtonClicked();
    protected abstract void dialogClose();
    public abstract Object[]  getResult();
    protected abstract Parent buildDialogContent();
    public abstract void attributeSelected(NodeInfo info);
    public void nodeSelected(GenericTreeNode node){
        if(!invokeButtonPressed)
            nodeSelectedChild(node);
    }

    protected abstract void nodeSelectedChild(GenericTreeNode node);
}
