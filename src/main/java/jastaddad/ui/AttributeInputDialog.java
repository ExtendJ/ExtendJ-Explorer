package jastaddad.ui;

import jastaddad.api.nodeinfo.NodeInfo;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Temporary class for the inputdialog, used when specifying the values for method that will be computed.
 * ATM we can only use primitive types, and they will be cast to string.
 * The values will be caught by the AttributeTabController
 */
public class AttributeInputDialog extends Dialog<ArrayList<Object>> { //Todo redesign this dialog
    public AttributeInputDialog(NodeInfo attribute){
        if(!attribute.isParametrized())
            return;
        setTitle("Choose Inputs");
        setHeaderText("BEWARE: At the moment it only works with primitive types"); //TODO change so that more complex inputs can be added
        setResizable(false);
        Method m = attribute.getMethod();
        GridPane grid = new GridPane();
        for(int i = 0; i < m.getParameterCount(); i++){
            grid.add(new Label("Input: " + m.getParameterTypes()[i] + " "), 1, i+1);
            grid.add(new TextField(), 2, i+1);
        }

        getDialogPane().setContent(grid);
        ButtonType buttonTypeOk = new ButtonType("Invoke", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.FINISH);
        getDialogPane().getButtonTypes().setAll(buttonTypeOk);
        setResultConverter(b -> {
            if(b != buttonTypeOk)
                return null;
            ArrayList<Object> params = new ArrayList();
            for (int i = 0; i < m.getParameterCount(); i++) {
                TextField field = (TextField) grid.getChildren().get(i*2+1);
                params.add(field.getText());
            }
            return params;
        });
    }
    
}
