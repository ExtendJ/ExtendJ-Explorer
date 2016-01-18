package jastaddad.ui;

import jastaddad.JastAddAdSetup;
import jastaddad.api.JastAddAdAPI;
import jastaddad.api.filteredtree.GenericTreeNode;
import jastaddad.api.nodeinfo.NodeInfo;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.tools.ant.taskdefs.Jar;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessControlException;
import java.security.Permission;
import java.util.jar.JarFile;

/**
 * Created by gda10jth on 1/15/16.
 */
public class ProcessTestOpener extends UIDialog {
    private TextField jarField;
    private TextField filterField;
    private TextField arg1Field;
    private TextField args;

    public ProcessTestOpener(UIMonitor mon) {
        super(mon);
    }

    @Override
    protected boolean yesButtonClicked() {
        String[] argString = (arg1Field.getText().length() <= 0 ? args.getText() : arg1Field.getText() + " " + args.getText()).split(" ");
        if(argString[0].equals("")) argString = new String[0];
        String filterPath = filterField.getText();
        if(filterPath.length() <= 0){
            File file = new File(jarField.getText());
            if(file != null && !file.isDirectory()){
                filterPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(file.separator)) + file.separator + JastAddAdAPI.DEFAULT_FILTER_NAME;
            }
        }

        JastAddAdSetup setup = new JastAddAdSetup(mon.getJastAddAdUI(), jarField.getText(), filterPath, argString);
        setup.run();
        return true;
    }



    @Override
    protected void dialogClose() {

    }

    @Override
    public Object[] getResult() {
        return new Object[0];
    }

    @Override
    protected Parent buildDialogContent() {
        VBox parent = new VBox();

        Label label1 = new Label("Path to compiler jar:");
        HBox row1 = new HBox();
        jarField = new TextField();
        Button jarButton = new Button("browse");
        row1.getChildren().addAll(jarField, jarButton);

        Label label2 = new Label("Path to filter:");
        HBox row2 = new HBox();
        filterField = new TextField();
        Button filterButton = new Button("browse");
        row2.getChildren().addAll(filterField, filterButton);

        Label label3 = new Label("First compiler argument");
        HBox row3 = new HBox();
        arg1Field = new TextField();
        Button arg1Button = new Button("browse");
        row3.getChildren().addAll(arg1Field, arg1Button);


        Label label4 = new Label("Arguments for compiler:");
        args = new TextField();

        parent.getChildren().addAll(label1,row1, label2, row2, label3, row3, label4, args );

        buttonTypeOk.setText("Open");
        parent.getChildren().add(buttonTypeOk);

        jarButton.setOnAction(e->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Jar File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JAR", "*.jar"));
            File file = fileChooser.showOpenDialog(mon.getStage());
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
    public void attributeSelected(NodeInfo info) {

    }

    @Override
    protected void nodeSelectedChild(GenericTreeNode node) {

    }
}
