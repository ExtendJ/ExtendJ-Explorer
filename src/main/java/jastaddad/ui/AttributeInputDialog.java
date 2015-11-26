package jastaddad.ui;

import jastaddad.api.filteredtree.GenericTreeNode;
import jastaddad.api.filteredtree.TreeNode;
import jastaddad.api.nodeinfo.NodeInfo;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.util.Duration;

import java.lang.reflect.Method;
import java.util.ArrayList;

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
    private nodeParameter focusedNodeParameter;
    private ArrayList<nodeParameter> nodeParameters;

    private boolean blinkAnimationWhite;
    Object[]  params;

    private String backgroundColorLight = "-fx-background-color: #aaaaaa;";
    private String backgroundColorField = "-fx-background-color: #808080;";
    private String backgroundColorParam = "-fx-background-color: #606060;";
    private String backgroundColorTopBox = "-fx-background-color: #aaaaaa;";
    private String borderColorTopBox = "-fx-border-color: #dddddd;";
    private String borderWidthTopBox = "-fx-border-width: 0 0 1 0;";
    private String prefWidthLabel = "-fx-pref-width:150;";
    private String prefWidthInput = "-fx-pref-width:200;";
    private String labelColor = "-fx-text-fill:white;";
    private String labelPadding = "-fx-padding:0 10 0 10;";
    private String rowPadding = "-fx-padding:10;";
    private String textColorLight = "-fx-text-fill:#ffffff;";
    private String textColorGreyedOut = "-fx-text-fill:#999999;";
    private String paramSelectedBorderColor = "-fx-border-color: #FFC573;";
    private String paramUnSelectedBorderColor = "-fx-border-color: #606060;";
    private String paramSelectedBorderWidth = "-fx-border-width: 3;";
    private String paramLabelBorderColor = "-fx-border-color: #999999;";
    private String paramLabelBorderWidth = "-fx-border-width: 0 1 0 0;";

    private String clickHereString = "Click here";
    private String selectNodeString = "Click on a node";

    public AttributeInputDialog(NodeInfo attribute, TreeNode node, UIMonitor mon){
        super(mon);
        initModality(Modality.NONE);
        this.info = attribute;
        this.node = node;
        blinkAnimationWhite = true;
        nodeParameters = new ArrayList<>();

        if(!attribute.isParametrized())
            return;

        setTitle("Parameters");
        //setHeaderText("BEWARE: At the moment it only works with primitive types and String"); //TODO change so that more complex inputs can be added

        m = attribute.getMethod();
        params = new Object[m.getParameterCount()];
        for(int i=0; i< m.getParameterCount();i++)
            params[i] = null;

    }

    protected Parent buildDialogContent(){
        VBox grid = new VBox();
        gridNodePosition = new int[m.getParameterCount()];
        int fieldCounter;
        boolean firstNodeParamFound = false;
        HBox topBox = new HBox();
        Label label1 = new Label("Type");
        Label label2 = new Label("Input");
        label1.setStyle(prefWidthLabel+labelPadding);
        label2.setStyle(prefWidthInput + labelPadding);
        topBox.setStyle(backgroundColorTopBox + borderColorTopBox + borderWidthTopBox);
        topBox.getChildren().addAll(label1, label2);
        grid.getChildren().add(topBox);
        for(int i = 0; i < m.getParameterCount(); i++){
            fieldCounter = 0;
            Class type = m.getParameterTypes()[i];
            HBox hBox = new HBox();


            hBox.setStyle((isNodeParam(type) ? backgroundColorParam + rowPadding + backgroundColorParam + paramSelectedBorderWidth + paramUnSelectedBorderColor : backgroundColorField)+rowPadding );
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label(type.getSimpleName() + ":");
            label.setStyle(labelColor+prefWidthLabel+labelPadding + paramLabelBorderColor + paramLabelBorderWidth);

            hBox.getChildren().add(label);
            grid.getChildren().add(hBox);
            HBox fieldContainer = new HBox();
            fieldContainer.setStyle(labelPadding);
            if(type == int.class || type == Integer.class){
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
                field.setStyle(prefWidthInput);
                fieldCounter = 1;
                fieldContainer.getChildren().add(field);
            }else if(type == boolean.class || type == Boolean.class){
                final ToggleGroup group = new ToggleGroup();
                HBox rButtons = new HBox();
                rButtons.setStyle(prefWidthInput);

                RadioButton rb1 = new RadioButton("True");
                rb1.setToggleGroup(group);
                rb1.setSelected(true);

                RadioButton rb2 = new RadioButton("False");
                rb2.setToggleGroup(group);
                fieldCounter = 2;

                rButtons.getChildren().add(rb1);
                rButtons.getChildren().add(rb2);

                fieldContainer.getChildren().add(rButtons);

            }else if(isNodeParam(type)) {
                Label nodeRefField = new Label();
                nodeRefField.getStyleClass().clear();
                nodeRefField.setText(clickHereString);
                final int paramPos = i;
                nodeParameter param = new nodeParameter(paramPos, type, nodeRefField, hBox);
                nodeParameters.add(param);
                nodeRefField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if(newValue){
                        paramTextFieldSelected(param);
                    }
                });
                hBox.setOnMouseClicked(event -> {
                    nodeRefField.requestFocus();
                });
                /*if(!firstNodeParamFound){
                    firstNodeParamFound = true;
                    paramTextFieldSelected(param);
                }*/

                nodeRefField.setStyle(prefWidthInput + textColorGreyedOut);
                fieldCounter = 1;
                fieldContainer.getChildren().add(nodeRefField);
            }else {
                fieldCounter = 1;
                TextField field = new TextField();
                field.setStyle(prefWidthInput);
                fieldContainer.getChildren().add(field);
            }
            hBox.getChildren().add(fieldContainer);



            if(i != 0)
                gridNodePosition[i] += gridNodePosition[i-1] + 2 + fieldCounter;
            else
                gridNodePosition[i] = fieldCounter +1;
        }

        buttonTypeOk.setText("Invoke");
        grid.getChildren().add(buttonTypeOk);
        return grid;
    }

    @Override
    public void attributeSelected(AttributeInfo info) {
        if(info == null)
            return;
        if(mon.getApi().isNodeReference(info.getValue())) {
            mon.getController().addMessage("1(" + info + ")");
            GenericTreeNode aNode = mon.getApi().getNodeReference(info.getValue());
            if(!aNode.isNode())
                return;
            mon.getController().addMessage("2(" + info + ")");
            trySelectNode((TreeNode)aNode);
        }
    }

    protected void yesButtonClicked(){

    }

    @Override
    protected void dialogClose() {
        mon.clearDialogSelectedNodes();
    }

    private void paramTextFieldSelected(nodeParameter param){
        if(focusedNodeParameter != null){
            focusedNodeParameter.hBox.setStyle(rowPadding + backgroundColorParam + paramSelectedBorderWidth + paramUnSelectedBorderColor);
            if(focusedNodeParameter.label.getText().equals(selectNodeString)) {
                focusedNodeParameter.label.setText(clickHereString);
                focusedNodeParameter.label.setStyle(prefWidthInput + textColorGreyedOut);
            }else {
                focusedNodeParameter.label.setStyle(prefWidthInput + textColorLight);
            }
        }
        focusedNodeParameter = param;
        focusedNodeParameter.hBox.setStyle(backgroundColorParam + paramSelectedBorderColor + rowPadding + paramSelectedBorderWidth);
        if(focusedNodeParameter.label.getText().equals(clickHereString)) {
            focusedNodeParameter.label.setText(selectNodeString);
            focusedNodeParameter.label.setStyle(prefWidthInput + textColorGreyedOut);
        }else {
            focusedNodeParameter.label.setStyle(prefWidthInput + textColorLight);
        }
        attributeSelected(mon.getSelectedInfo());

    }


    /*
    <vbox>
        <hbox>
            <Label></label>
            <Hbox>
                <Label></label>
                <[field]></[field]>
            </hbox>
        </hbox>
        <hbox>
        </hbox>
        <hbox>
        </hbox>
    </vbox>




     */
    private boolean isNodeParam(Class type){
        return mon.getApi().getTypeHash().containsKey(type.getSimpleName());
    }

    public Object[] getResult(){
        if(!invokeButtonPressed)
            return null;
        VBox grid = (VBox)getScene().getRoot();
        for (int i = 0; i < m.getParameterCount(); i++) {
            HBox fieldContainer = (HBox)((HBox)grid.getChildren().get(i+1)).getChildren().get(1);
            Class type = m.getParameterTypes()[i];
            if(type == boolean.class || type == Boolean.class){
                //System.out.println("row: " + i + " pos: " + gridNodePosition[i]);
                HBox rButtons = (HBox) fieldContainer.getChildren().get(1);
                RadioButton rb = (RadioButton)rButtons.getChildren().get(0);
                rb = (RadioButton)rb.getToggleGroup().getSelectedToggle();
                params[i] = rb.getText().equals("True");
            }else if(isNodeParam(type)) {
                // this kind of parameter is set in function nodeSelectedChild(...);
            }else if(type == int.class || type == Integer.class) {
                TextField field = (TextField) fieldContainer.getChildren().get(0);
                params[i] = Integer.parseInt(field.getText());
            }else {
                //System.out.println("row: " + i + " pos: " + gridNodePosition[i]);
                TextField field = (TextField) fieldContainer.getChildren().get(0);
                params[i] = field.getText();
            }
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
        if(node.isNode() && focusedNodeParameter != null) {
            TreeNode fNode = (TreeNode) node;
            trySelectNode(fNode);
        }
    }

    private void trySelectNode(TreeNode fNode){
        mon.getController().addMessage(fNode.getNode().simpleNameClass);
        if (focusedNodeParameter.type.getSimpleName().equals(fNode.getNode().simpleNameClass)) {
            int index = nodeParameters.indexOf(focusedNodeParameter);
            mon.getController().addMessage("index: " + index);
            if(index >= 0){
                mon.removeDialogSelectedNodes(nodeParameters.get(index).getNode());
            }
            focusedNodeParameter.setNode(fNode);
            mon.addDialogSelectedNodes(focusedNodeParameter.getNode());
            params[focusedNodeParameter.pos] = fNode.getNode().node;
            focusedNodeParameter.label.setText(focusedNodeParameter.type.getCanonicalName());

            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(200),
                    ae -> blinkTextField(focusedNodeParameter.label)));
            timeline.setCycleCount(4);
            timeline.play();


            //System.out.println("asdasd: " + focusedNodeParameter.toString());
        }
    }

    private void blinkTextField(Label label){
        if(blinkAnimationWhite){
            label.setStyle(backgroundColorLight + prefWidthInput + textColorLight);
        }else{
            label.setStyle(backgroundColorParam + prefWidthInput + textColorLight);
        }
        blinkAnimationWhite = !blinkAnimationWhite;
    }

    private class nodeParameter{
        final int pos;
        final Class type;
        final Label label;
        final HBox hBox;
        private GenericTreeNode node;

        public nodeParameter(int pos, Class type, Label label, HBox hBox){
            this.pos = pos;
            this.type = type;
            this.label = label;
            this.hBox = hBox;
            node = null;
        }
        void setNode(GenericTreeNode node){
            this.node = node;
        }
        GenericTreeNode getNode(){
            return node;
        }

    }
}
