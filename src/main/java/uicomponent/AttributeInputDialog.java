package uicomponent;

import jastaddad.objectinfo.Attribute;
import jastaddad.objectinfo.NodeInfo;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import javax.xml.soap.Text;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by gda10jli on 11/4/15.
 */
public class AttributeInputDialog extends Dialog<ArrayList<Object>> {
    public AttributeInputDialog(Attribute attribute){
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
