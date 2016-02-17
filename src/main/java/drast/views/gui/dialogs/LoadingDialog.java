package drast.views.gui.dialogs;

import drast.model.filteredtree.GenericTreeNode;
import drast.model.nodeinfo.NodeInfo;
import drast.views.gui.Monitor;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gda10jth on 2/17/16.
 */
public class LoadingDialog extends DrDialog {
    private String text;
    private Label label;
    private String dotts;
    Toolkit toolkit;

    Timer timer;
    public LoadingDialog(Monitor mon, String text) {
        super(mon, StageStyle.UNDECORATED);
        initModality(Modality.NONE);

        this.text = text;
        toolkit = Toolkit.getDefaultToolkit();
        timer = new Timer();
        dotts = "...";
    }

    @Override
    protected boolean yesButtonClicked() {
        return false;
    }

    @Override
    protected void loadStyleSheets(Scene scene) {
        scene.getStylesheets().add("/style/loadingDialog.css");
    }

    @Override
    protected void dialogClose() {
        System.out.println("kukar");
        timer.cancel();
        timer = null;
    }

    @Override
    public Object[] getResult() {
        return new Object[0];
    }

    @Override
    protected Parent buildDialogContent() {
        VBox parent = new VBox();
        parent.getStyleClass().add("loading_parent");
        label = new Label();
        label.getStyleClass().add("loading_label");
        parent.getChildren().add(label);
        loadingAnimation();
        return parent;
    }

    protected void loadingAnimation(){
        label.setText(text + dotts);
        dotts = dotts.length() > 2 ? "" : dotts + ".";
        //if(timer != null)
            //timer.schedule(new RemindTask(), 1000);
    }

    @Override
    public void attributeSelected(NodeInfo info) {

    }

    @Override
    protected void nodeSelectedChild(GenericTreeNode node) {

    }

    class RemindTask extends TimerTask {

        public void run() {
            Platform.runLater(() -> loadingAnimation());
        }
    }
}
