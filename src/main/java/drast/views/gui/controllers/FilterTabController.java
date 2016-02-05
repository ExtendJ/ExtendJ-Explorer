package drast.views.gui.controllers;

import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.Monitor;
import drast.views.gui.guicomponent.FilterEditor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by gda10jth on 2/3/16.
 */
public class FilterTabController implements Initializable, ControllerInterface {
    private Monitor mon;
    private FilterEditor codeArea;

    @FXML private VBox codeAreaContainer;
    @FXML private Button saveNewFilterButton;

    public void init(Monitor mon){
        this.mon = mon;
        codeArea = new FilterEditor(mon.getController());
        codeAreaContainer.getChildren().add(codeArea);
        codeArea.getStyleClass().add("textAreaConfig");

        loadFilterFileText();

        // update the new filter. This is done in the API
        saveNewFilterButton.setOnAction((event) -> mon.getController().saveNewFilter());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public String getFilterText(){
        return codeArea.getText();
    }

    /**
     * Load and print the filter text in the textarea for filter text
     */
    private void loadFilterFileText() {
        String line;
        String textContent = "";
        if(mon.getBrain().getFilterFilePath() != null) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(mon.getBrain().getFilterFilePath()));
                while ((line = reader.readLine()) != null) {
                    textContent += line + "\n";
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                textContent = "Can not read the configuration file!";
            }
        }
        codeArea.setText(textContent);

    }

    @Override
    public void onNewAPI() {
        loadFilterFileText();
    }

    @Override
    public void functionStarted() {

    }

    @Override
    public void functionStopped() {

    }

    @Override
    public void nodeSelected(GenericTreeNode node) {

    }

    @Override
    public void nodeDeselected() {

    }

    @Override
    public void updateGUI() {

    }
}
