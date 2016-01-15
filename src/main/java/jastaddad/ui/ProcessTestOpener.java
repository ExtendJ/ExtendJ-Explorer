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
    private TextField args;

    public ProcessTestOpener(UIMonitor mon) {
        super(mon);
    }

    @Override
    protected boolean yesButtonClicked() {
        JastAddAdSetup setup = new JastAddAdSetup(mon.getJastAddAdUI(), jarField.getText(), filterField.getText(), new String[]{args.getText()});
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
        Button jarButton = new Button();
        row1.getChildren().addAll(jarField, jarButton);

        Label label2 = new Label("path to filter:");
        filterField = new TextField();

        Label label3 = new Label("Arguments for compiler:");
        args = new TextField();

        parent.getChildren().addAll(label1,row1, label2, filterField, label3, args );

        buttonTypeOk.setText("Open");
        parent.getChildren().add(buttonTypeOk);
        return parent;
    }

    @Override
    public void attributeSelected(NodeInfo info) {

    }

    @Override
    protected void nodeSelectedChild(GenericTreeNode node) {

    }
}
