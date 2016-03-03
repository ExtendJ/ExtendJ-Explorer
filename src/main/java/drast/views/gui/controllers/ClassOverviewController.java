package drast.views.gui.controllers;

import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.Monitor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;

/**
 * Created by gda10jth on 2/3/16.
 */
public class ClassOverviewController implements Initializable, ControllerInterface {
    private Monitor mon;

    @FXML private TreeView<String> typeListView;

    @Override
    public void init(Monitor mon) {
        this.mon = mon;

        loadClassTreeView();
    }

    private void loadClassTreeView(){

        TreeItem<String> root = new TreeItem<>("ROOT");
        TreeItem<String> astRoot = new TreeItem<>("AST classes");
        TreeItem<String> superRoot = new TreeItem<>("Abstract AST classes");
        astRoot.setExpanded(true);
        root.setExpanded(true);
        HashMap<Class, HashSet<Class>> parents = mon.getBrain().getDirectParents();
        HashMap<Class, HashSet<Class>> children = mon.getBrain().getDirectChildren();
        int superClass;
        for (Class type : mon.getBrain().getAllASTTypes()) {
            TreeItem<String> parent = new TreeItem<>(type.getSimpleName());
            superClass = loadChildrenOrParents(parent, "AST Parents", parents.get(type));
            superClass += loadChildrenOrParents(parent, "AST children", children.get(type));
            if(superClass == 0) //We have found no direct children or parents, this type is then treated as a superClass
                superRoot.getChildren().add(parent);
            else
                astRoot.getChildren().add(parent);
        }
        root.getChildren().addAll(astRoot, superRoot);
        typeListView.setRoot(root);
        typeListView.setShowRoot(false);
    }

    private int loadChildrenOrParents(TreeItem<String> parent, String name, HashSet<Class> set){
        if(set == null || set.size() == 0)
            return 0;
        TreeItem<String> level = new TreeItem<>(name);
        for(Class parentClass : set){
            level.getChildren().add(new TreeItem<>(parentClass.getSimpleName()));
        }
        parent.getChildren().add(level);
        return 1;
    }

    @Override
    public void onNewAPI() {
        loadClassTreeView();
    }

    @Override
    public void onApplicationClose(){}

    @Override
    public void functionStarted() {}

    @Override
    public void functionStopped() {}

    @Override
    public void nodeSelected(GenericTreeNode node) {}

    @Override
    public void nodeDeselected() {}

    @Override
    public void updateGUI() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {}
}
