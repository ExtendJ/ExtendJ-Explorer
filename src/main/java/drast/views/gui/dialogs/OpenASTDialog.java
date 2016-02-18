package drast.views.gui.dialogs;

import drast.model.DrAST;
import drast.model.filteredtree.GenericTreeNode;
import drast.model.terminalvalues.TerminalValue;
import drast.views.gui.Monitor;
import drast.views.gui.guicomponent.nodeinfotreetableview.NodeInfoView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by gda10jth on 1/15/16.
 */
public class OpenASTDialog extends DrDialog implements Initializable, ChangeListener<TreeItem<NodeInfoView>> {
    private TextField jarField;
    private TextField filterField;
    private TextField arg1Field;
    private TextField args;
    private String filterPath;
    private String[] argString;

    public OpenASTDialog(Monitor mon) {
        super(mon);
        initModality(Modality.NONE);
        setTitle("Open compiler...");
    }

    @Override
    protected boolean yesButtonClicked() {
        argString = (arg1Field.getText().length() == 0 ? args.getText() : arg1Field.getText() + " " + args.getText()).split(" ");
        if(argString[0].equals(""))
            argString = new String[0];
        filterPath = filterField.getText();

        boolean done = true;

        File file;

        if(filterPath.length() > 0) {
            file = new File(filterPath);
            if (!file.exists()) {
                filterField.setStyle("-fx-background-color: #ffcccc");
                done = false;
            }
        }
        file = new File(jarField.getText());
        if(!file.exists()) {
            jarField.setStyle("-fx-background-color: #ffcccc");
            done = false;
        }

        if(!done) return false;

        if (filterPath.length() == 0) {
            if (file != null && !file.isDirectory()) {
                filterPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(file.separator)) + file.separator + DrAST.DEFAULT_FILTER_NAME;
            }
        }


        mon.getConfig().put("prevJar", jarField.getText());
        mon.getConfig().put("prevFilter", filterField.getText());
        mon.getConfig().put("prevFirstArg", arg1Field.getText());
        mon.getConfig().put("prevRestArgs", args.getText());
        String fullArgString = "";
        for (String s : argString)
            fullArgString += s + " ";
        mon.getConfig().put("prevFullArgs", fullArgString);
        mon.getConfig().saveConfigFile();
        return true;
    }



    @Override
    protected void dialogClose() {
        mon.getController().runCompiler(mon.getDrASTUI(), jarField.getText(), filterPath, argString);
    }

    @Override
    public Object[] getResult() {
        return new Object[0];
    }

    private String lastValue(String key){
        String value = mon.getConfig().get(key);
        return value == null ? "" : value;
    }

    @Override
    protected Parent buildDialogContent() {
        VBox parent = new VBox();
        parent.getStyleClass().add("our_root");
        parent.setFillWidth(true);
        VBox fieldContainer = new VBox();
        fieldContainer.getStyleClass().add("top_parent");
        fieldContainer.setFillWidth(true);

        Label label1 = new Label("Path to compiler jar:");
        HBox row1 = new HBox();
        row1.getStyleClass().add("out_row");
        jarField = new TextField();
        jarField.setText(lastValue("prevJar"));
        Button jarButton = new Button("...");
        row1.getChildren().addAll(jarField, jarButton);

        Label label2 = new Label("Path to filter:");
        HBox row2 = new HBox();
        row2.getStyleClass().add("out_row");
        filterField = new TextField();
        filterField.setText(lastValue("prevFilter"));
        Button filterButton = new Button("...");
        row2.getChildren().addAll(filterField, filterButton);

        Separator separatorArg = new Separator();

        Label label3 = new Label("First compiler argument:");
        HBox row3 = new HBox();
        row3.getStyleClass().add("out_row");
        arg1Field = new TextField();
        arg1Field.setText(lastValue("prevFirstArg"));
        Button arg1Button = new Button("...");
        row3.getChildren().addAll(arg1Field, arg1Button);


        Label label4 = new Label("Compiler arguments:");
        HBox row4 = new HBox();
        row4.getStyleClass().add("out_row");
        args = new TextField();
        args.setText(lastValue("prevRestArgs"));
        row4.getChildren().add(args);
        //args.getStyleClass().add("out_row");

        Separator separator = new Separator();

        fieldContainer.getChildren().addAll(label1,row1, label2, row2,separatorArg, label3, row3, label4, row4 );
        parent.getChildren().addAll(fieldContainer, separator);

        buttonTypeOk.setText("Open");
        buttonTypeOk.getStyleClass().add("done_button");
        parent.getChildren().add(buttonTypeOk);

        jarButton.setOnAction(e->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Jar File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JAR", "*.jar"));
            
            File file = fileChooser.showOpenDialog(getScene().getWindow());
            if(file != null){
                String path = file.getAbsolutePath();
                jarField.setText(file.getAbsolutePath());

                File filterFile = new File(path.substring(0, path.lastIndexOf(file.separator)) + file.separator + "filter.fcl");
                //System.out.println(filterFile.getAbsolutePath());
                if(filterField.getText().length() <= 0 && filterFile.exists() && !filterFile.isDirectory()){
                    filterField.setText(filterFile.getAbsolutePath());
                }
            }
        });

        filterButton.setOnAction(e->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Filter File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("fcl", "*.fcl"));
            File file = fileChooser.showOpenDialog(mon.getStage());
            if(file != null){
                filterField.setText(file.getAbsolutePath());
            }
        });

        arg1Button.setOnAction(e->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Argument File");
            File file = fileChooser.showOpenDialog(mon.getStage());
            if(file != null){
                arg1Field.setText(file.getAbsolutePath());
            }
        });
        return parent;
    }

    @Override
    protected void loadStyleSheets(Scene scene){
        scene.getStylesheets().add("/style/dialog.css");
    }

    @Override
    public void attributeSelected(TerminalValue info) {

    }

    @Override
    protected void nodeSelectedChild(GenericTreeNode node) {

    }

    @Override
    public void changed(ObservableValue<? extends TreeItem<NodeInfoView>> observable, TreeItem<NodeInfoView> oldValue, TreeItem<NodeInfoView> newValue) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
