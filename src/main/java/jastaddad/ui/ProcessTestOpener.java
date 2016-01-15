package jastaddad.ui;

import jastaddad.JastAddAdSetup;
import jastaddad.api.JastAddAdAPI;
import jastaddad.api.filteredtree.GenericTreeNode;
import jastaddad.api.nodeinfo.NodeInfo;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

    public ProcessTestOpener(UIMonitor mon) {
        super(mon);
    }

    @Override
    protected boolean yesButtonClicked() {
        JastAddAdSetup setup = new JastAddAdSetup(mon.getJastAddAdUI(), "CalcASM/compiler.jar", "CalcASM/filter.cfg", new String[]{"CalcASM/testfiles/asm/nesting2.calc"});
        setup.run();
        return false;
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
        HBox topBox = new HBox();
        Label label1 = new Label("Type");
        Label label2 = new Label("Input");
        topBox.getChildren().addAll(label1, label2);
        parent.getChildren().add(topBox);
        buttonTypeOk.setText("Invoke");
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
