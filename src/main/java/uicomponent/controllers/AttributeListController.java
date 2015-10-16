package uicomponent.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by gda10jth on 10/16/15.
 */
public class AttributeListController implements Initializable {
    @FXML
    private ListView listView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // change next line to DB load
        List<String> values = Arrays.asList("one", "two", "three");

        listView.setItems(FXCollections.observableList(values));

    }
}
