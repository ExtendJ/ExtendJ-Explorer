package drast.views.gui.dialogs;

import drast.Log;
import drast.model.filteredtree.GenericTreeNode;
import drast.model.filteredtree.TreeNode;
import drast.model.terminalvalues.TerminalValue;
import drast.views.gui.GUIData;
import drast.views.gui.guicomponent.inputfields.CharField;
import drast.views.gui.guicomponent.inputfields.FloatingNumberField;
import drast.views.gui.guicomponent.inputfields.NumberField;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Dialog used to specify the parameters for a attribute method.
 * The values will be caught by the AttributeTabController.
 * When the dialog is closed the resulting parameters can be fetched with getResult().
 */
public class AttributeInputDialog extends DrDialog {
  private final TerminalValue info;
  private Method method;
  private NodeParameter focusedNodeParameter;
  private final ArrayList<NodeParameter> nodeParameters;

  private boolean blinkAnimationWhite;
  private Object[] params;

  private static final String backgroundColorLight = "-fx-background-color: #aaaaaa;";
  private static final String backgroundColorField = "-fx-background-color: #808080;";
  private static final String backgroundColorParam = "-fx-background-color: #707070;";
  private static final String backgroundColorTopBox = "-fx-background-color: #aaaaaa;";
  private static final String borderColorTopBox = "-fx-border-color: #606060;";
  private static final String borderWidthTopBox = "-fx-border-width: 0 0 1 0;";
  private static final String prefWidthLabel = "-fx-pref-width:150;";
  private static final String prefWidthInput = "-fx-pref-width:200;";
  private static final String labelColor = "-fx-text-fill:white;";
  private static final String labelPadding = "-fx-padding:5 10 5 10;";
  private static final String rowPadding = "-fx-padding:10;";
  private static final String textColorGreyedOut = "-fx-text-fill:#999999;";
  private static final String paramSelectedBorderColor = "-fx-border-color: #FF8C8C;";
  private static final String paramSelectedTextColor = "-fx-text-fill: #FFC573;";
  private static final String paramUnSelectedBorderColor = "-fx-border-color: #707070;";
  private static final String paramSelectedBorderWidth = "-fx-border-width: 3;";
  private static final String paramLabelBorderColor = "-fx-border-color: #999999;";
  private static final String paramLabelBorderWidth = "-fx-border-width: 0 1 0 0;";

  private static final String clickHereString = "Click here";
  private static final String selectNodeString = "Click on a node";

  public AttributeInputDialog(TerminalValue attribute, GUIData mon) {
    super(mon, StageStyle.UNIFIED);
    initModality(Modality.NONE);
    this.info = attribute;
    blinkAnimationWhite = true;
    nodeParameters = new ArrayList<>();

    if (!attribute.isParametrized()) {
      Log.error("Not a parameterized attribute.");
      return;
    }

    setTitle("Parameters");
    // TODO this currently only works with primitive types and String. Change so that more complex inputs can be handled.

    method = attribute.getMethod();
    params = new Object[method.getParameterCount()];
    for (int i = 0; i < method.getParameterCount(); i++) {
      params[i] = null;
    }

    buildDialogContent(parentNode);
  }

  /**
   * Builds the dialog fields and labels. How the dialog will look depends on which parameters are used by the method.
   */
  private void buildDialogContent(VBox parent) {
    parent.getStyleClass().add("our_root");
    // The labels above the parameters in the dialog.
    HBox topBox = new HBox();
    Label label1 = new Label("Type");
    Label label2 = new Label("Input");
    label1.setStyle(labelPadding);
    label1.setStyle(prefWidthLabel + labelPadding);
    label2.setStyle(prefWidthInput + labelPadding);
    topBox.setStyle(backgroundColorTopBox + borderColorTopBox + borderWidthTopBox);
    topBox.getChildren().addAll(label1, label2);

    parent.getChildren().add(topBox);

    for (int i = 0; i < method.getParameterCount(); i++) {
      Class type = method.getParameterTypes()[i];
      HBox rowContainer = new HBox();
      rowContainer.setStyle((isNodeParam(type) ?
          backgroundColorParam + rowPadding + backgroundColorParam + paramSelectedBorderWidth
              + paramUnSelectedBorderColor :
          backgroundColorField) + rowPadding);
      rowContainer.setAlignment(Pos.CENTER_LEFT);

      // Label for left hand side.
      Label label = new Label(type.getSimpleName() + ":");
      label.setStyle(labelColor + prefWidthLabel + labelPadding + paramLabelBorderColor
          + paramLabelBorderWidth);
      rowContainer.getChildren().add(label);

      parent.getChildren().add(rowContainer);

      // Container for right hand side.
      HBox fieldContainer = new HBox();
      fieldContainer.setStyle(labelPadding);

      Node field;
      String style = prefWidthInput;

      // Depending on the type of the parameter, different fields will be used (text fields, radio buttons, etc...)
      if (isIntegerType(type)) {
        // int, short, long, byte
        field = new NumberField();
      } else if (isFloatType(type)) {
        // float, double
        field = new FloatingNumberField();
      } else if (type == boolean.class || type == Boolean.class) {
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

      } else if (isNodeParam(type)) {
        // This input i a object from the AST.
        // This will be selected by clicking in the graph, tree view or a attribute value
        Label nodeRefField = new Label();
        nodeRefField.getStyleClass().clear();
        nodeRefField.setText(clickHereString);
        NodeParameter param = new NodeParameter(i, type, nodeRefField, rowContainer);
        nodeParameters.add(param);
        nodeRefField.focusedProperty().addListener((observable, oldValue, newValue) -> {
          if (newValue) {
            paramTextFieldSelected(param);
          }
        });
        rowContainer.setOnMouseClicked(event -> nodeRefField.requestFocus());

        field = nodeRefField;
        style += textColorGreyedOut;
      } else if (type == char.class || type == Character.class) {
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
  }

  @Override public void attributeSelected(TerminalValue info) {
    if (info == null) {
      return;
    }
    if (mon.getTreeTraverser().isTreeNode(info.getValue())) {
      Log.info("1(%s)", info);
      GenericTreeNode aNode = mon.getTreeTraverser().getTreeNode(info.getValue());
      if (!aNode.isNonCluster()) {
        return;
      }
      Log.info("2(%s)", info);
      trySelectNode((TreeNode) aNode);
    }
  }

  @Override protected boolean yesButtonClicked() {
    Object[] tmpParam = params.clone();
    VBox grid = (VBox) getScene().getRoot();
    for (int i = 0; i < method.getParameterCount(); i++) {
      HBox fieldContainer = (HBox) ((HBox) grid.getChildren().get(i + 1)).getChildren().get(1);
      Class type = method.getParameterTypes()[i];
      if (type == boolean.class || type == Boolean.class) {
        HBox rButtons = (HBox) fieldContainer.getChildren().get(0);
        RadioButton rb = (RadioButton) rButtons.getChildren().get(0);
        rb = (RadioButton) rb.getToggleGroup().getSelectedToggle();
        tmpParam[i] = rb.getText().equals("True");
      } else if (isNodeParam(type)) {
        // This kind of parameter is set in function nodeSelectedChild(...);
      } else {
        TextField field = (TextField) fieldContainer.getChildren().get(0);

        // This makes sure the value is within bounds of its type.
        if (type == int.class || type == Integer.class) {
          try {
            tmpParam[i] = field.getText().length() <= 0 ? null : Integer.parseInt(field.getText());
          } catch (NumberFormatException e) {
            Log.error("The number is out of bounds. Integer should be between %d and %d",
                Integer.MIN_VALUE, Integer.MAX_VALUE);
          }
        } else if (type == byte.class || type == Byte.class) {
          try {
            tmpParam[i] = field.getText().length() <= 0 ? null : Byte.parseByte(field.getText());
          } catch (NumberFormatException e) {
            Log.error("The number is out of bounds. Byte should be between %d and %d",
                Byte.MIN_VALUE, Byte.MAX_VALUE);
          }
        } else if (type == short.class || type == Short.class) {
          try {
            tmpParam[i] = field.getText().length() <= 0 ? null : Short.parseShort(field.getText());
          } catch (NumberFormatException e) {
            Log.error("The number is out of bounds. Short should be between %d and %d",
                Short.MIN_VALUE, Short.MAX_VALUE);
          }
        } else if (type == long.class || type == Long.class) {
          try {
            tmpParam[i] = field.getText().length() <= 0 ? null : Long.parseLong(field.getText());
          } catch (NumberFormatException e) {
            Log.error("The number is out of bounds. Long should be between %d and %d",
                Long.MIN_VALUE, Long.MAX_VALUE);
          }
        } else if (type == float.class || type == Float.class) {
          try {
            tmpParam[i] = field.getText().length() <= 0 ? null : Float.parseFloat(field.getText());
          } catch (NumberFormatException e) {
            Log.error("The number is out of bounds. Float should be between %d and %d",
                Float.MIN_VALUE, Float.MAX_VALUE);
          }
        } else if (type == double.class || type == Double.class) {
          try {
            tmpParam[i] =
                field.getText().length() <= 0 ? null : Double.parseDouble(field.getText());
          } catch (NumberFormatException e) {
            Log.error("The number is out of bounds. Double should be between %d and %d",
                Double.MIN_VALUE, Double.MAX_VALUE);
          }
        } else if (type == boolean.class || type == Boolean.class) {
          tmpParam[i] =
              field.getText().length() <= 0 ? null : Boolean.parseBoolean(field.getText());
        } else if (type == char.class || type == Character.class) {
          try {
            tmpParam[i] = field.getText().length() <= 0 ? null : field.getText().charAt(0);
          } catch (NumberFormatException e) {
            Log.error("The number is out of bounds. Character should be between %d and %d",
                Character.MIN_VALUE, Character.MAX_VALUE);
          }
        } else {
          tmpParam[i] = field.getText();
        }
      }
    }

    boolean done = true;
    for (int i = 0; i < tmpParam.length; i++) {
      if (isPrimitive(method.getParameterTypes()[i])) {
        if (tmpParam[i] == null) {
          Log.error("Parameter of type %s can not be null", method.getParameterTypes()[i]);
          done = false;
        }

      }
    }
    if (done) {
      params = tmpParam;
    }
    return done;
  }

  @Override protected void loadStyleSheets(Scene scene) {
    scene.getStylesheets().add("/style/dialog.css");
  }

  private boolean isPrimitive(Class type) {
    return type == byte.class ||
        type == short.class ||
        type == int.class ||
        type == long.class ||
        type == float.class ||
        type == double.class ||
        type == boolean.class ||
        type == char.class;
  }

  private boolean isFloatType(Class type) {
    return type == float.class || type == Float.class ||
        type == double.class || type == Double.class;
  }

  private boolean isIntegerType(Class type) {
    return type == int.class || type == Integer.class ||
        type == short.class || type == Short.class ||
        type == long.class || type == Long.class ||
        type == byte.class || type == Byte.class;
  }

  @Override protected void dialogClose() {
    mon.clearDialogSelectedNodes();
    if (focusedNodeParameter != null) {
      mon.removehighlightedSimpleClassName(focusedNodeParameter.type.getSimpleName());
    }
  }

  /**
   * Called when parameter field with a type from the AST.
   * Toggles the styles of the selected and the deselected field.
   * Also tells the UI to highlight and stop highlight types in the tree.
   *
   * @param param information about the field selected
   */
  private void paramTextFieldSelected(NodeParameter param) {
    if (yesButtonPressed) {
      return;
    }
    // sets the old selected field to normal style settings and tell the UI to not highlight that type anymore
    if (focusedNodeParameter != null) {
      focusedNodeParameter.hBox.setStyle(
          rowPadding + backgroundColorParam + paramSelectedBorderWidth
              + paramUnSelectedBorderColor);
      if (focusedNodeParameter.label.getText().equals(selectNodeString)) {
        focusedNodeParameter.label.setText(clickHereString);
        focusedNodeParameter.label.setStyle(prefWidthInput + textColorGreyedOut);
      } else {
        focusedNodeParameter.label.setStyle(prefWidthInput + paramSelectedTextColor);
      }
      mon.removehighlightedSimpleClassName(focusedNodeParameter.type.getSimpleName());
    }
    focusedNodeParameter = param;
    mon.addhighlightedSimpleClassName(focusedNodeParameter.type.getSimpleName());
    focusedNodeParameter.hBox.setStyle(
        backgroundColorParam + paramSelectedBorderColor + rowPadding + paramSelectedBorderWidth);
    if (focusedNodeParameter.label.getText().equals(clickHereString)) {
      focusedNodeParameter.label.setText(selectNodeString);
      focusedNodeParameter.label.setStyle(prefWidthInput + textColorGreyedOut);
    } else {
      focusedNodeParameter.label.setStyle(prefWidthInput + paramSelectedTextColor);
    }

    if (mon.getSelectedInfo() != null) {
      attributeSelected(mon.getSelectedInfo().getNodeInfo());
    }
    if (focusedNodeParameter.getNode() == null && mon.getSelectedNode() != null && mon
        .getSelectedNode().isNonCluster()) {
      trySelectNode((TreeNode) mon.getSelectedNode());
    }
    mon.getGraphView().repaint();
  }

  private boolean isNodeParam(Class type) {
    return mon.getTreeTraverser().isAstType(type);
  }

  public Object[] getResult() {
    return yesButtonPressed ? params : null;
  }

  public TerminalValue getInfo() {
    return info;
  }

  public boolean invokeButtonPressed() {
    return yesButtonPressed;
  }

  @Override public void nodeSelectedChild(GenericTreeNode node) {
    if (node.isNonCluster() && focusedNodeParameter != null) {
      TreeNode fNode = (TreeNode) node;
      trySelectNode(fNode);
    }
  }

  private void trySelectNode(TreeNode fNode) {
    if (fNode == focusedNodeParameter.getNode()) {
      return;
    }
    if (focusedNodeParameter.type.getSimpleName().equals(fNode.getNode().getSimpleClassName())
        || fNode.getNode().isChildClassOf(focusedNodeParameter.type, mon.getTreeTraverser())) {
      int index = nodeParameters.indexOf(focusedNodeParameter);
      if (index >= 0) {
        mon.removeDialogSelectedNodes(nodeParameters.get(index).getNode());
      }
      focusedNodeParameter.setNode(fNode);
      mon.addDialogSelectedNodes(focusedNodeParameter.getNode());
      params[focusedNodeParameter.pos] = fNode.getNode().getAstObject();
      focusedNodeParameter.label.setText(focusedNodeParameter.type.getCanonicalName());

      Timeline timeline = new Timeline(
          new KeyFrame(Duration.millis(150), ae -> blinkTextField(focusedNodeParameter.label)));
      timeline.setCycleCount(2);
      timeline.play();

    }
  }

  private void blinkTextField(Label label) {
    String style = prefWidthInput + paramSelectedTextColor;
    if (blinkAnimationWhite) {
      label.setStyle(backgroundColorLight + style);
    } else {
      label.setStyle(backgroundColorParam + style);
    }
    blinkAnimationWhite = !blinkAnimationWhite;
  }

  private static class NodeParameter {
    final int pos;
    final Class type;
    final Label label;
    final HBox hBox;
    private TreeNode node;

    public NodeParameter(int pos, Class type, Label label, HBox hBox) {
      this.pos = pos;
      this.type = type;
      this.label = label;
      this.hBox = hBox;
      node = null;
    }

    void setNode(TreeNode node) {
      this.node = node;
    }

    TreeNode getNode() {
      return node;
    }
  }
}
