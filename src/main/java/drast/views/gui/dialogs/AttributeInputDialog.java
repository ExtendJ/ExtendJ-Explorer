package drast.views.gui.dialogs;

import drast.model.filteredtree.GenericTreeNode;
import drast.model.filteredtree.TreeNode;
import drast.model.terminalvalues.TerminalValue;
import drast.views.gui.Monitor;
import drast.views.gui.guicomponent.inputfields.CharField;
import drast.views.gui.guicomponent.inputfields.FloatingNumberField;
import drast.views.gui.guicomponent.inputfields.NumberField;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.util.Duration;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Dialog used to specify the parameters for a attribute method.
 * The values will be caught by the AttributeTabController.
 * When the dialog is closed the resulting parameters can be fetched with getResult().
 */
public class AttributeInputDialog extends DrDialog {
    private TerminalValue info;
    private TreeNode node;
    private Method m;
    private nodeParameter focusedNodeParameter;
    private ArrayList<nodeParameter> nodeParameters;
    private int nodeInfoPos;

    private boolean blinkAnimationWhite;
    Object[]  params;

    // styles
    private String backgroundColorLight = "-fx-background-color: #aaaaaa;";
    private String backgroundColorField = "-fx-background-color: #808080;";
    private String backgroundColorParam = "-fx-background-color: #707070;";
    private String backgroundColorTopBox = "-fx-background-color: #aaaaaa;";
    private String borderColorTopBox = "-fx-border-color: #606060;";
    private String borderWidthTopBox = "-fx-border-width: 0 0 1 0;";
    private String prefWidthLabel = "-fx-pref-width:150;";
    private String prefWidthInput = "-fx-pref-width:200;";
    private String labelColor = "-fx-text-fill:white;";
    private String labelPadding = "-fx-padding:5 10 5 10;";
    private String rowPadding = "-fx-padding:10;";
    private String textColorLight = "-fx-text-fill:#ffffff;";
    private String textColorGreyedOut = "-fx-text-fill:#999999;";
    private String paramSelectedBorderColor = "-fx-border-color: #FF8C8C;";
    private String paramSelectedTextColor = "-fx-text-fill: #FFC573;";
    private String paramUnSelectedBorderColor = "-fx-border-color: #707070;";
    private String paramSelectedBorderWidth = "-fx-border-width: 3;";
    private String paramLabelBorderColor = "-fx-border-color: #999999;";
    private String paramLabelBorderWidth = "-fx-border-width: 0 1 0 0;";

    private String clickHereString = "Click here";
    private String selectNodeString = "Click on a node";

    public AttributeInputDialog(TerminalValue attribute, TreeNode node, Monitor mon){
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

    /**
     * Builds the dialog fields and labels. How the dialog will look depends on which parameters are used by the method.
     * @return
     */
    protected Parent buildDialogContent(){
        VBox parent = new VBox();
        parent.getStyleClass().add("our_root");
        // The labels above the parameters in the dialog
        HBox topBox = new HBox();
        Label label1 = new Label("Type");
        Label label2 = new Label("Input");
        label1.setStyle(labelPadding);
        label1.setStyle(prefWidthLabel+labelPadding);
        label2.setStyle(prefWidthInput + labelPadding);
        topBox.setStyle(backgroundColorTopBox + borderColorTopBox + borderWidthTopBox);
        topBox.getChildren().addAll(label1, label2);

        parent.getChildren().add(topBox);

        // for each parameter
        for(int i = 0; i < m.getParameterCount(); i++) {
            Class type = m.getParameterTypes()[i];
            HBox rowContainer = new HBox();
            rowContainer.setStyle((isNodeParam(type) ? backgroundColorParam + rowPadding + backgroundColorParam + paramSelectedBorderWidth + paramUnSelectedBorderColor : backgroundColorField) + rowPadding);
            rowContainer.setAlignment(Pos.CENTER_LEFT);

            // label for left hand side
            Label label = new Label(type.getSimpleName() + ":");
            label.setStyle(labelColor + prefWidthLabel + labelPadding + paramLabelBorderColor + paramLabelBorderWidth);
            rowContainer.getChildren().add(label);

            parent.getChildren().add(rowContainer);

            // container for right hand side
            HBox fieldContainer = new HBox();
            fieldContainer.setStyle(labelPadding);

            Node field;
            String style = prefWidthInput;

            // Depending on the type of the parameter, different fields will be used (text fields, radio buttons, lala..)
            if (isIntegerType(type)) {
                // int, short, long, byte
                field = new NumberField();
            }else if(isFloatType(type)){
                // float, double
                field = new FloatingNumberField();
            }else if(type == boolean.class || type == Boolean.class){
                final ToggleGroup group = new ToggleGroup();
                HBox rButtons = new HBox();

                RadioButton rb1 = new RadioButton("True");
                rb1.setToggleGroup(group);
                rb1.setSelected(true);

                RadioButton rb2 = new RadioButton("False");
                rb2.setToggleGroup(group);

                rButtons.getChildren().add(rb1);
                rButtons.getChildren().add(rb2);
                field = rButtons;

            }else if(isNodeParam(type)) {
                // This input i a object from the AST.
                // This will be selected by clicking in the graph, tree view or a attribute value
                Label nodeRefField = new Label();
                nodeRefField.getStyleClass().clear();
                nodeRefField.setText(clickHereString);
                final int paramPos = i;
                nodeParameter param = new nodeParameter(paramPos, type, nodeRefField, rowContainer);
                nodeParameters.add(param);
                nodeRefField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if(newValue){
                        paramTextFieldSelected(param);
                    }
                });
                rowContainer.setOnMouseClicked(event -> {
                    nodeRefField.requestFocus();
                });

                field = nodeRefField;
                style += textColorGreyedOut;
            }else if(type == char.class || type == Character.class){
                field = new CharField();
            } else {
                field = new TextField();
            }
            field.setStyle(style);
            fieldContainer.getChildren().add(field);
            rowContainer.getChildren().add(fieldContainer);
        }

        buttonTypeOk.setText("Compute");
        buttonTypeOk.getStyleClass().add("done_button");
        Label filler = new Label("");
        parent.getChildren().add(filler);
        parent.getChildren().add(buttonTypeOk);
        return parent;
    }

    @Override
    public void attributeSelected(TerminalValue info) {
        if(info == null)
            return;
        if(mon.getBrain().isTreeNode(info.getValue())) {
            mon.getController().addMessage("1(" + info + ")");
            GenericTreeNode aNode = mon.getBrain().getTreeNode(info.getValue());
            if(!aNode.isNode())
                return;
            mon.getController().addMessage("2(" + info + ")");
            trySelectNode((TreeNode)aNode);
        }
    }

    protected boolean yesButtonClicked(){
        Object[] tmpParam = params.clone();
        /*
        for(Object param : tmpParam){
            mon.getController().addMessage(param == null ? "null" : param.toString());
        }
        for(Object param : params){
            mon.getController().addMessage(param == null ? "null" : param.toString());
        }
*/
        VBox grid = (VBox)getScene().getRoot();
        for (int i = 0; i < m.getParameterCount(); i++) {
            HBox fieldContainer = (HBox)((HBox)grid.getChildren().get(i+1)).getChildren().get(1);
            Class type = m.getParameterTypes()[i];
            if(type == boolean.class || type == Boolean.class){
                //System.out.println("row: " + i + " pos: " + gridNodePosition[i]);
                HBox rButtons = (HBox) fieldContainer.getChildren().get(0);
                RadioButton rb = (RadioButton)rButtons.getChildren().get(0);
                rb = (RadioButton)rb.getToggleGroup().getSelectedToggle();
                tmpParam[i] = rb.getText().equals("True");
            }else if(isNodeParam(type)) {
                // this kind of parameter is set in function nodeSelectedChild(...);
            } else {
                //System.out.println("row: " + i + " pos: " + gridNodePosition[i]);
                TextField field = (TextField) fieldContainer.getChildren().get(0);

                    // OH GOD MY EYES!!!!! x.x - author of this fine piece of code
                    // this code makes sure the value is within bounds of its type.
                if (type == int.class || type == Integer.class) {
                    try {
                        tmpParam[i] = field.getText().length() <= 0 ? null : Integer.parseInt(field.getText());
                    }catch (NumberFormatException e){
                        mon.getController().addError("The number is out of bounds. Integer should be between " + Integer.MIN_VALUE + " and " + Integer.MAX_VALUE);
                    }
                } else if (type == byte.class || type == Byte.class) {
                    try {
                        tmpParam[i] = field.getText().length() <= 0 ? null : Byte.parseByte(field.getText());
                    }catch (NumberFormatException e){
                        mon.getController().addError("The number is out of bounds. Byte should be between " + Byte.MIN_VALUE + " and " + Byte.MAX_VALUE);
                    }
                } else if (type == short.class || type == Short.class) {
                    try {
                        tmpParam[i] = field.getText().length() <= 0 ? null : Short.parseShort(field.getText());
                    }catch (NumberFormatException e){
                        mon.getController().addError("The number is out of bounds. Short should be between " + Short.MIN_VALUE + " and " + Short.MAX_VALUE);
                    }
                } else if (type == long.class || type == Long.class) {
                    try {
                        tmpParam[i] = field.getText().length() <= 0 ? null : Long.parseLong(field.getText());
                    }catch (NumberFormatException e){
                        mon.getController().addError("The number is out of bounds. Long should be between " + Long.MIN_VALUE + " and " + Long.MAX_VALUE);
                    }
                } else if (type == float.class || type == Float.class) {
                    try {
                        tmpParam[i] = field.getText().length() <= 0 ? null : Float.parseFloat(field.getText());
                    }catch (NumberFormatException e){
                        mon.getController().addError("The number is out of bounds. Float should be between " + Float.MIN_VALUE + " and " + Float.MAX_VALUE);
                    }
                } else if (type == double.class || type == Double.class) {
                    try {
                        tmpParam[i] = field.getText().length() <= 0 ? null : Double.parseDouble(field.getText());
                    }catch (NumberFormatException e){
                        mon.getController().addError("The number is out of bounds. Double should be between " + Double.MIN_VALUE + " and " + Double.MAX_VALUE);
                    }
                } else if (type == boolean.class || type == Boolean.class) {
                    tmpParam[i] = field.getText().length() <= 0 ? null : Boolean.parseBoolean(field.getText());
                } else if (type == char.class || type == Character.class) {
                    try {
                        tmpParam[i] = field.getText().length() <= 0 ? null : field.getText().charAt(0);
                    }catch (NumberFormatException e){
                        mon.getController().addError("The number is out of bounds. Character should be between " + Character.MIN_VALUE + " and " + Character.MAX_VALUE);
                    }
                } else
                    tmpParam[i] = field.getText();
            }
        }
        /*
        for(Object param : tmpParam){
            mon.getController().addMessage(param == null ? "null" : param.toString());
        }*/

        boolean done = true;
        for(int i=0;i<tmpParam.length;i++){
            if(isPrimitive(m.getParameterTypes()[i])){
                if(tmpParam[i] == null){
                    mon.getController().addMessage("Parameter of type " + m.getParameterTypes()[i] + " can not be null");
                    done = false;
                }

            }
        }
        if(done){
            params = tmpParam;
        }
        return done;
    }

    @Override
    protected void loadStyleSheets(Scene scene) {
            scene.getStylesheets().add("/style/dialog.css");

    }

    private boolean isPrimitive(Class type){
        return type == byte.class ||
               type == short.class ||
               type == int.class ||
               type == long.class ||
               type == float.class ||
               type == double.class ||
               type == boolean.class ||
               type == char.class;
    }

    private boolean isFloatType(Class type){
        return type == float.class || type == Float.class ||
                type == double.class || type == Double.class;
    }

    private boolean isIntegerType(Class type){
        return type == int.class || type == Integer.class ||
                type == short.class || type == Short.class ||
                type == long.class || type == Long.class||
                type == byte.class || type == Byte.class;
    }

    @Override
    protected void dialogClose() {
        mon.clearDialogSelectedNodes();
        if(focusedNodeParameter != null)
            mon.removehighlightedSimpleClassName(focusedNodeParameter.type.getSimpleName());
    }

    /**
     * Called when parameter field with a type from the AST.
     * Toggles the styles of the selected and the deselected field.
     * Also tells the UI to highlight and stop highlight types in the tree.
     *
     * @param param information about the field selected
     */
    private void paramTextFieldSelected(nodeParameter param){
        if(yesButtonPressed) return;
        // sets the old selected field to normal style settings and tell the UI to not highlight that type anymore
        if(focusedNodeParameter != null){
            focusedNodeParameter.hBox.setStyle(rowPadding + backgroundColorParam + paramSelectedBorderWidth + paramUnSelectedBorderColor);
            if(focusedNodeParameter.label.getText().equals(selectNodeString)) {
                focusedNodeParameter.label.setText(clickHereString);
                focusedNodeParameter.label.setStyle(prefWidthInput + textColorGreyedOut);
            }else {
                focusedNodeParameter.label.setStyle(prefWidthInput + paramSelectedTextColor);
            }
            mon.removehighlightedSimpleClassName(focusedNodeParameter.type.getSimpleName());
        }
        focusedNodeParameter = param;
        mon.addhighlightedSimpleClassName(focusedNodeParameter.type.getSimpleName());
        focusedNodeParameter.hBox.setStyle(backgroundColorParam + paramSelectedBorderColor + rowPadding + paramSelectedBorderWidth);
        if(focusedNodeParameter.label.getText().equals(clickHereString)) {
            focusedNodeParameter.label.setText(selectNodeString);
            focusedNodeParameter.label.setStyle(prefWidthInput + textColorGreyedOut);
        }else {
            focusedNodeParameter.label.setStyle(prefWidthInput + paramSelectedTextColor);
        }

        if (mon.getSelectedInfo() != null)
            attributeSelected(mon.getSelectedInfo().getNodeInfo());
        if(focusedNodeParameter.getNode() == null && mon.getSelectedNode() != null && mon.getSelectedNode().isNode())
            trySelectNode((TreeNode)mon.getSelectedNode());
        mon.getGraphView().repaint();
    }

    private boolean isNodeParam(Class type){
        return mon.getBrain().isASTType(type);
    }

    public Object[] getResult(){
        return yesButtonPressed ? params : null;
    }

    public TerminalValue getInfo(){
        return info;
    }

    public void setNodeInfoPos(int pos){ this.nodeInfoPos = pos; }

    public int getNodeInfoPos(){ return nodeInfoPos; }

    public TreeNode getTreeNode(){return node;}
    public boolean invokeButtonPressed(){
        return yesButtonPressed;
    }

    public void nodeSelectedChild(GenericTreeNode node){
        if(node.isNode() && focusedNodeParameter != null) {
            TreeNode fNode = (TreeNode) node;
            trySelectNode(fNode);
        }
    }

    private void trySelectNode(TreeNode fNode){
        //mon.getController().addMessage(fNode.getNode().simpleNameClass);
        if(fNode == focusedNodeParameter.getNode())
            return;
        if (focusedNodeParameter.type.getSimpleName().equals(fNode.getNode().simpleNameClass) ||
                fNode.getNode().isChildClassOf(focusedNodeParameter.type)) {
            int index = nodeParameters.indexOf(focusedNodeParameter);
            if(index >= 0){
                mon.removeDialogSelectedNodes(nodeParameters.get(index).getNode());
            }
            focusedNodeParameter.setNode(fNode);
            mon.addDialogSelectedNodes(focusedNodeParameter.getNode());
            params[focusedNodeParameter.pos] = fNode.getNode().node;
            focusedNodeParameter.label.setText(focusedNodeParameter.type.getCanonicalName());

            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(150),
                    ae -> blinkTextField(focusedNodeParameter.label)));
            timeline.setCycleCount(2);
            timeline.play();

        }
    }

    private void blinkTextField(Label label){
        String style = prefWidthInput + paramSelectedTextColor;
        if(blinkAnimationWhite){
            label.setStyle(backgroundColorLight + style);
        }else{
            label.setStyle(backgroundColorParam + style);
        }
        blinkAnimationWhite = !blinkAnimationWhite;
    }

    private class nodeParameter{
        final int pos;
        final Class type;
        final Label label;
        final HBox hBox;
        private TreeNode node;

        public nodeParameter(int pos, Class type, Label label, HBox hBox){
            this.pos = pos;
            this.type = type;
            this.label = label;
            this.hBox = hBox;
            node = null;
        }
        void setNode(TreeNode node){
            this.node = node;
        }
        TreeNode getNode(){
            return node;
        }

    }
}
