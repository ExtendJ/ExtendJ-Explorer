package jastaddad.ui;

import jastaddad.api.filteredtree.GenericTreeNode;
import jastaddad.api.filteredtree.TreeNode;
import jastaddad.api.nodeinfo.NodeInfo;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Temporary class for the inputdialog, used when specifying the values for method that will be computed.
 * ATM we can only use primitive types, and they will be cast to string.
 * The values will be caught by the AttributeTabController
 */
public class AttributeInputDialog extends UIDialog { //Todo redesign this dialog
    private NodeInfo info;
    private TreeNode node;
    private Method m;
    int[] gridNodePosition;
    private Pair<Class, Integer> focusedFieldClass;
    ArrayList<Object> params;

    public AttributeInputDialog(NodeInfo attribute, TreeNode node, UIMonitor mon){
        super(mon);
        this.info = attribute;
        this.node = node;


        if(!attribute.isParametrized())
            return;

        setTitle("Choose Inputs");
        //setHeaderText("BEWARE: At the moment it only works with primitive types and String"); //TODO change so that more complex inputs can be added

        m = attribute.getMethod();
        params = new ArrayList();
        for(int i=0; i< m.getParameterCount();i++)
            params.add(null);

    }

    protected Parent buildDialogContent(){
        GridPane grid = new GridPane();
        gridNodePosition = new int[m.getParameterCount()];
        int fieldCounter;
        for(int i = 0; i < m.getParameterCount(); i++){
            fieldCounter = 0;
            Class type = m.getParameterTypes()[i];
            grid.add(new Label("Input: " + type + " "), 1, i+1);
            if(type == int.class){
                TextField field = new TextField() {
                    @Override public void replaceText(int start, int end, String text) {
                        if (text.matches("[0-9]*")) {
                            super.replaceText(start, end, text);
                        }
                    }

                    @Override public void replaceSelection(String text) {
                        if (text.matches("[0-9]*")) {
                            super.replaceSelection(text);
                        }
                    }
                };
                fieldCounter = 1;
                grid.add(field, 2, i+1);
            }else if(type == boolean.class){
                final ToggleGroup group = new ToggleGroup();

                RadioButton rb1 = new RadioButton("True");
                rb1.setToggleGroup(group);
                rb1.setSelected(true);

                RadioButton rb2 = new RadioButton("False");
                rb2.setToggleGroup(group);
                fieldCounter = 2;
                grid.add(rb1, 2, i+1  );
                grid.add(rb2, 3, i+1  );

            }else if(isNodeParam(type)) {
                TextField nodeRefField = new TextField();
                nodeRefField.setEditable(false);
                final int paramPos = i;
                nodeRefField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if(newValue){
                        focusedFieldClass = new Pair<>(type, paramPos);
                        mon.getController().addMessage("type in field: " + focusedFieldClass.getKey().getSimpleName());
                    }
                });
                fieldCounter = 1;
                grid.add(nodeRefField, 2, i + 1);
            }else {
                fieldCounter = 1;
                grid.add(new TextField(), 2, i + 1);
            }

            if(i != 0)
                gridNodePosition[i] += gridNodePosition[i-1] + 1 + fieldCounter;
            else
                gridNodePosition[i] = fieldCounter;
        }

        buttonTypeOk.setText("Invoke");
        grid.add(buttonTypeOk, 1, gridNodePosition.length+1);
        return grid;
    }

    protected void yesButtonClicked(){

    }

    private boolean isNodeParam(Class type){
        return mon.getApi().getTypeHash().containsKey(type.getSimpleName());
    }

    public ArrayList<Object> getResult(){
        if(!invokeButtonPressed)
            return null;
        GridPane grid = (GridPane)getScene().getRoot();
        for (int i = 0; i < m.getParameterCount(); i++) {
            Class type = m.getParameterTypes()[i];
            if(type == boolean.class){
                System.out.println("row: " + i + " pos: " + gridNodePosition[i]);
                RadioButton rb = (RadioButton)grid.getChildren().get(gridNodePosition[i]);
                rb = (RadioButton)rb.getToggleGroup().getSelectedToggle();
                params.set(i, rb.getText().equals("True"));
            }else if(isNodeParam(type)) {

            }else {
                System.out.println("row: " + i + " pos: " + gridNodePosition[i]);
                TextField field = (TextField) grid.getChildren().get(gridNodePosition[i]);
                params.set(i, field.getText());
            }
        }
        for(Object o : params){
            mon.getController().addMessage(o.toString());
        }
        return params;
    }

    public NodeInfo getInfo(){
        return info;
    }

    public TreeNode getTreeNode(){return node;}
    public boolean invokeButtonPressed(){
        return invokeButtonPressed;
    }

    public void nodeSelectedChild(GenericTreeNode node){
        if(node.isNode()) {
            TreeNode fNode = (TreeNode) node;
            if (focusedFieldClass.getKey().getSimpleName().equals(fNode.getNode().className)){
                params.set(focusedFieldClass.getValue(), fNode.getNode().node);
                System.out.println("asdasd: " + focusedFieldClass.toString());
            }
        }
    }

}
