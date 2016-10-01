package drast.views.gui.controllers;

import drast.Log;
import drast.model.Node;
import drast.model.filteredtree.GenericTreeCluster;
import drast.model.filteredtree.GenericTreeNode;
import drast.model.filteredtree.TreeNode;
import drast.model.terminalvalues.Attribute;
import drast.model.terminalvalues.AttributeInfo;
import drast.model.terminalvalues.TerminalValue;
import drast.views.gui.DrASTGUI;
import drast.views.gui.GUIData;
import drast.views.gui.dialogs.AttributeInputDialog;
import drast.views.gui.guicomponent.TextFormatter;
import drast.views.gui.guicomponent.nodeinfotreetableview.TerminalValueTreeItem;
import drast.views.gui.guicomponent.nodeinfotreetableview.TerminalValueTreeItemParameter;
import drast.views.gui.guicomponent.nodeinfotreetableview.TerminalValueTreeItemView;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * This is a controller class that keeps track of the attribute tab. The tab shows information of the clicked node in
 * the tree. There are two VBoxes with stuff that is used depending on what is clicked on:
 * - clusterInfoView when a cluster is clicked.
 * - nodeInfoView when a node is clicked.
 */
public class AttributeTabController
    implements Initializable, ChangeListener<TreeItem<TerminalValueTreeItemView>>,
    ControllerInterface {
  private GUIData mon;
  private TextFormatter formatter = new TextFormatter();

  @FXML private TreeTableView<TerminalValueTreeItemView> attributeTableView;
  @FXML private TreeTableColumn<TerminalValueTreeItemView, String> attributeNameCol;
  @FXML private TreeTableColumn<TerminalValueTreeItemView, String> attributeValueCol;

  @FXML private TableView<TableViewAttributeInfo> attributeInfoTableView;
  @FXML private TableColumn<TableViewAttributeInfo, String> attributeInfoNameCol;
  @FXML private TableColumn<TableViewAttributeInfo, Object> attributeInfoValueCol;

  @FXML private TableView<ClusterInfo> clusterInfoTableView;
  @FXML private TableColumn<ClusterInfo, String> clusterInfoNameCol;
  @FXML private TableColumn<ClusterInfo, Object> clusterInfoCountCol;

  @FXML private TextFlow nodeNameLabel;
  @FXML private Label attributeInfoLabel;
  @FXML private Label objectInheritanceLabel;
  @FXML private Label attributeLabel;

  @FXML private VBox clickedNodeInfoPane;
  @FXML private VBox nodeInfoView;
  @FXML private VBox clusterInfoView;
  @FXML private Label clusterInfoNumberLabel;

  @FXML private TextField attributeNameFilter;

  private final TreeItem<TerminalValueTreeItemView> attributeRootItem = new TreeItem<>();

  @Override public void init(GUIData mon) {
    this.mon = mon;
  }

  /**
   * Will set all the listeners and the start values for the gui components.
   */
  @Override public void initialize(URL url, ResourceBundle rb) {
    setTextFormatter();

    attributeNameFilter.textProperty()
        .addListener((observable, oldValue, newValue) -> filterTree(newValue));

    // Node information views.
    attributeTableView.getSelectionModel().selectedItemProperty().addListener(this);

    attributeInfoNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    attributeInfoValueCol.setCellValueFactory(new PropertyValueFactory<>("value"));

    // Cluster information views.
    clusterInfoNameCol.setCellValueFactory(new PropertyValueFactory<>("typeName"));
    clusterInfoCountCol.setCellValueFactory(new PropertyValueFactory<>("count"));
    attributeNameCol.setCellValueFactory(param -> {
      TerminalValueTreeItemView info = param.getValue().getValue();
      if (info != null) {
        return new ReadOnlyStringWrapper(formatter.format(info.getName()));
      }
      return null;
    });
    attributeValueCol.setCellValueFactory(param -> {
      TerminalValueTreeItemView info = param.getValue().getValue();
      if (info != null) {
        return new ReadOnlyStringWrapper(formatter.format(info.getValue()));
      }
      return null;
    });

    attributeValueCol.setCellFactory(param -> new AttributeValueCell());
    attributeInfoValueCol.setCellFactory(column -> new AttributeInfoValueCell());

    // Hide the header for the attribute info table.
    attributeInfoTableView.widthProperty().addListener((source, oldWidth, newWidth) -> {
      Pane header = (Pane) attributeInfoTableView.lookup("TableHeaderRow");
      if (header.isVisible()) {
        header.setMaxHeight(0);
        header.setMinHeight(0);
        header.setPrefHeight(0);
        header.setVisible(false);
      }
    });

    attributeNameCol.prefWidthProperty().bind(attributeTableView.widthProperty().multiply(0.50));
    attributeValueCol.prefWidthProperty().bind(attributeTableView.widthProperty().multiply(0.50));

    showThisInAttributeTab(nodeInfoView);

    attributeRootItem.setExpanded(true);
  }

  private void setTextFormatter() {
    if (mon != null && mon.getTreeTraverser().hasRoot()) {
      formatter = new TextFormatter(mon.getTreeTraverser().getRootClass());
    } else {
      formatter = new TextFormatter();
    }
  }

  /**
   * Switch to the view child.
   */
  private void showThisInAttributeTab(VBox child) {
    clickedNodeInfoPane.getChildren().clear();
    clickedNodeInfoPane.getChildren().add(child);
  }

  /**
   * Used when computing and invoking methods in this controller. prints errors, values and warnings to the console.
   */
  private void logAttributeValue(Object value) {
    Log.info("Computation successful : " + value);
  }


  @Override public void nodeSelected(GenericTreeNode node) {
    if (node == null) {
      return;
    }
    if (node.isNonCluster()) {
      objectInheritanceLabel.setText("Object inheritance");
      attributeLabel.setText("Attributes");
      showThisInAttributeTab(nodeInfoView);
      updateAttributeTable();
    } else {
      showThisInAttributeTab(clusterInfoView);
      setClusterInfo();
    }
  }

  @Override public void nodeDeselected() {
    updateAttributeTable();
  }

  @Override public void updateGUI() {
    updateAttributeTable();
  }

  /**
   * Sets the attributes in the table view, called when a node has been selected.
   * Will Clear the list if the selected node is null or not a "real" node
   */
  private void updateAttributeTable() {
    GenericTreeNode node = mon.getSelectedNode();
    nodeNameLabel.getChildren().clear();
    if (node == null || !node.isNonCluster() || node.isNullNode()) {
      if (attributeTableView.getRoot() != null
          && attributeTableView.getRoot().getChildren() != null) {
        attributeTableView.getRoot().getChildren().clear();
        attributeInfoTableView.getItems().clear();
        objectInheritanceLabel.setText("");
        attributeLabel.setText("");
      }
      clusterInfoNumberLabel.setText("");
      if (clusterInfoTableView != null) {
        clusterInfoTableView.getItems().clear();
      }
      return;
    }

    Class<?> klass = node.getNode().getAstClass();
    Text parent = null;
    while (klass != null) {
      Text text = new Text("    " + klass.getName() + "\n");
      if (parent != null) {
        text.getStyleClass().add("class-parent-type-text");
      } else {
        text.getStyleClass().add("class-type-text");
      }
      parent = text;
      nodeNameLabel.getChildren().add(0, parent);
      klass = klass.getSuperclass();
    }

    updateAttributeTable((TreeNode) node, true);
  }

  /**
   * Sets the attributeTableView, which all the attributes for the given node
   */
  private void updateAttributeTable(TreeNode node, boolean compute) {
    Node n = node.getNode();
    if (compute) {
      mon.getController().setOutStreams();
      n.computeAttributes();
      attributeTableView.getSelectionModel().clearSelection();
      mon.getController().resetOutStreams();
    }

    attributeRootItem.getChildren().clear();
    addSectionToAttributeList("Attributes", n.getAttributes(), attributeRootItem);
    addSectionToAttributeList("Nonterminal attributes", n.getNTAs(), attributeRootItem);
    addSectionToAttributeList("Tokens", n.getTokens(), attributeRootItem);

    // Re-filter the tree.
    filterTree(attributeNameFilter.getText());
  }

  private void filterTree(String filter) {
    TreeItem<TerminalValueTreeItemView> rootItem = new TreeItem<>();
    filter(attributeRootItem, rootItem, filter.trim().toLowerCase());
    rootItem.setExpanded(true);
    attributeTableView.setRoot(rootItem);
    attributeTableView.setShowRoot(false);

    // Re-sort the table.
    attributeTableView.getSortOrder().clear();
    attributeTableView.getSortOrder().add(attributeNameCol);
    attributeTableView.sort();
  }

  private void filter(TreeItem<TerminalValueTreeItemView> root,
      TreeItem<TerminalValueTreeItemView> filteredRoot, String filter) {
    for (TreeItem<TerminalValueTreeItemView> child : root.getChildren()) {
      TreeItem<TerminalValueTreeItemView> filteredChild = new TreeItem<>(child.getValue());
      filteredChild.setExpanded(true);
      filter(child, filteredChild, filter);
      if (filter.isEmpty() || !filteredChild.getChildren().isEmpty()
          || child.getValue().getName().toLowerCase().contains(filter)) {
        filteredRoot.getChildren().add(filteredChild);
      }
    }
  }

  /**
   * Add all NodeInfo from list to the parent with the name label.
   */
  private void addSectionToAttributeList(String label, Collection<TerminalValue> list,
      TreeItem<TerminalValueTreeItemView> parent) {
    if (list.isEmpty()) {
      return;
    }

    TreeItem<TerminalValueTreeItemView> treeItem =
        new TreeItem<>(new TerminalValueTreeItemView(label));
    treeItem.setExpanded(true);

    // Go through all methods and sort them on kind.
    Map<String, ArrayList<TerminalValue>> methods = new HashMap<>();
    for (TerminalValue info : list) {
      if (!methods.containsKey(info.getKind())) {
        methods.put(info.getKind(), new ArrayList<>());
      }
      methods.get(info.getKind()).add(info);
    }

    // Add all methods to the list
    for (Map.Entry<String, ArrayList<TerminalValue>> entry : methods.entrySet()) {
      if (!entry.getValue().isEmpty()) {
        TreeItem<TerminalValueTreeItemView> kindLabel;
        if (entry.getValue().get(0).getKind() != null) {
          kindLabel = new TreeItem<>(new TerminalValueTreeItemView(entry.getKey()));
          kindLabel.setExpanded(true);
          treeItem.getChildren().add(kindLabel);
        } else {
          kindLabel = treeItem;
        }

        for (TerminalValue info : entry.getValue()) {
          TreeItem<TerminalValueTreeItemView> methodItem =
              new TreeItem<>(new TerminalValueTreeItem(info));
          kindLabel.getChildren().add(methodItem);
          if (!info.isAttribute() || !info.isParametrized()) {
            continue;
          }

          Attribute attr = (Attribute) info;
          methodItem.setExpanded(true);
          for (Map.Entry<String, Object> computedEntry : attr.getComputedEntries()) {
            Object[] params = attr.getUsedParameters().get(computedEntry.getKey());
            TreeItem<TerminalValueTreeItemView> computedItem = new TreeItem<>(
                new TerminalValueTreeItemParameter(params, computedEntry.getValue(), info));
            methodItem.getChildren().add(computedItem);
          }
        }
      }
    }
    parent.getChildren().add(treeItem);
  }

  /**
   * Listener for the attribute TreeTableView, sets the attributeInfoTableView for the selected attribute.
   * It will also remove old references, and add new references if the value of selected attribute is a AST node.
   * Also tells the main controller that an attribute was selected.
   */
  @Override public void changed(
      ObservableValue<? extends TreeItem<TerminalValueTreeItemView>> observable,
      TreeItem<TerminalValueTreeItemView> oldValue, TreeItem<TerminalValueTreeItemView> newValue) {
    mon.clearSelectedParameterNodes();
    if (oldValue != null && oldValue.getValue() != null) {
      mon.getHighlightReferencesNodes().clear();
    }

    if (newValue != null) {
      setNewTerminalInfoValue(newValue.getValue());
    } else {
      mon.setSelectedInfo(null);
      setAttributeInfo(null, null);
      setReference(null);
    }
  }

  private void setNewTerminalInfoValue(TerminalValueTreeItemView newValue) {
    TerminalValue info = null;
    Object value = null;
    if (newValue.getTerminalValue()) {
      info = newValue.getNodeInfo();
      value = newValue.getValue();
    }

    if (newValue.isParameter()) {
      for (Object param : ((TerminalValueTreeItemParameter) newValue).getParams()) {
        GenericTreeNode tmp = mon.getTreeTraverser().getTreeNode(param);
        if (tmp != null) {
          mon.addSelectedParameterNodes(tmp);
        }
      }

    }

    mon.setSelectedInfo(newValue);
    mon.getController().attributeInNodeSelected(info);
    setAttributeInfo(info, value);
    setReference(value);
  }

  /**
   * Fill the attributeInfoTableView with information about the attribute
   */
  private void setAttributeInfo(TerminalValue info, Object value) {
    if (info == null) {
      attributeInfoLabel.setText("");
      attributeInfoTableView.getItems().clear();
      return;
    }
    attributeInfoLabel.setText("Information about selected attribute");
    attributeInfoTableView
        .setItems(FXCollections.observableArrayList(toArray(info.getInfo(value))));
  }

  /**
   * Adds references from the selected node in the monitor and the values found in the AttributeInfo supplied.
   */
  public void setReference(Object value) {
    ArrayList<GenericTreeNode> newRefs = null;
    if (value != null) {
      newRefs = getNodeReferencesAndHighlightThem(value);
    }
    mon.getGraphView().setReferenceEdges(newRefs, mon.getSelectedNode());
  }

  private ArrayList<GenericTreeNode> getNodeReferencesAndHighlightThem(Object value) {
    ArrayList<GenericTreeNode> nodes = new ArrayList<>();
    for (Object o : mon.getTreeTraverser().getNodeReferences(value)) {
      GenericTreeNode node = mon.getTreeTraverser().getTreeNode(o);
      if (node == null) {
        continue;
      }
      nodes.add(node);
      mon.addHighlightReferencesNodes(node);
    }
    return nodes;
  }

  private ArrayList<TableViewAttributeInfo> toArray(ArrayList<AttributeInfo> infoList) {
    return infoList.stream().map(
        info -> new TableViewAttributeInfo(info.getName(), info.getValue(), info.isFilePointer()))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * set the information of the cluster table view.
   */
  private void setClusterInfo() {
    GenericTreeCluster cluster = (GenericTreeCluster) mon.getSelectedNode();
    clusterInfoNumberLabel.setText(String.format("Cluster: %s node%s",
        cluster.getNodeCount(), cluster.getNodeCount() == 1 ? "" : "s"));

    ArrayList<ClusterInfo> itemList = cluster.getTypeList().entrySet().stream()
        .map(type -> new ClusterInfo(type.getKey(), type.getValue()))
        .collect(Collectors.toCollection(ArrayList::new));
    ObservableList<ClusterInfo> items = FXCollections.observableArrayList(itemList);
    clusterInfoTableView.setItems(items);
  }

  /**
   * This method will call the invocation of the method that has been clicked on, after the values have been added
   */
  private void onComputeClicked() {
    mon.getController().setOutStreams();
    onCompute();
    mon.getController().resetOutStreams();
  }

  private void onCompute() {
    TreeNode node = (TreeNode) mon.getSelectedNode();
    TerminalValueTreeItemView selectedInfo =
        attributeTableView.getSelectionModel().getSelectedItem().getValue();
    if (!selectedInfo.getTerminalValue()) {
      return;
    }

    TerminalValue info = selectedInfo.getNodeInfo();
    if (info.isParametrized()) {
      // Handle the non-parametrized Terminal values.
      computeParameterizedTerminal(node, info);
    } else {
      computeNonParameterizedTerminal(node, info, selectedInfo);
    }
  }

  private void computeNonParameterizedTerminal(TreeNode node, TerminalValue info,
      TerminalValueTreeItemView row) {
    Object obj = mon.getTreeTraverser().compute(node.getNode(), info, null);
    if (info.isNTA() && obj != null) {
      logAttributeValue(obj);
      updateAttributeTable(node, false);
      mon.getController().updateGUI();
    } else {
      attributeTableView.refresh();
      setNewTerminalInfoValue(row);
    }
  }

  private void computeParameterizedTerminal(TreeNode node, TerminalValue info) {
    AttributeInputDialog dialog = new AttributeInputDialog(info, mon);
    int selectedAttribute = attributeTableView.getSelectionModel().getSelectedIndex();

    dialog.setOnHiding(event -> {
      mon.getController().nodeSelected(node, this);
      if (!dialog.invokeButtonPressed()) {
        return;
      }

      Object[] result = dialog.getResult();
      if (result == null || mon.getLastRealNode() == null) {
        return;
      }

      TerminalValue dialogInfo = dialog.getInfo();
      Object value = mon.getTreeTraverser().compute(node.getNode(), dialog.getInfo(), result);
      logAttributeValue(value);
      if (info.isNTA()) {
        mon.getController().updateGUI();
      }

      if (!dialogInfo.isAttribute() || !dialogInfo.isParametrized()) {
        attributeTableView.refresh();
        return;
      }

      TreeItem<TerminalValueTreeItemView> treeItem =
          attributeTableView.getTreeItem(selectedAttribute);
      TerminalValueTreeItemView infoView = treeItem.getValue();
      if (infoView == null || infoView.getNodeInfo() == null
          || !infoView.getNodeInfo().isAttribute()) {
        return;
      }
      Attribute at = (Attribute) infoView.getNodeInfo();
      Attribute atd = (Attribute) dialogInfo;
      String lastKey = atd.getLastComputedKey();
      for (Map.Entry<String, Object> entry : at.getComputedEntries()) {
        if (entry.getKey().equals(lastKey)) {
          for (TreeItem<TerminalValueTreeItemView> view : treeItem.getChildren()) {
            if (view.getValue().getName().equals(lastKey)) {
              attributeTableView.getSelectionModel().select(view);
              return;
            }
          }
        }
      }
      TerminalValueTreeItemView temp =
          new TerminalValueTreeItemParameter(atd.getUsedParameters().get(lastKey), value, atd);
      TreeItem<TerminalValueTreeItemView> tempTree = new TreeItem<>(temp);
      treeItem.getChildren().add(tempTree);
      attributeTableView.getSelectionModel().select(tempTree);

    });
    dialog.show();
  }

  @Override public void onSetRoot() {
    setTextFormatter();
    updateAttributeTable();
  }

  /**
   * Class for the cells in the table views.
   */
  private class AttributeInfoValueCell extends TableCell<TableViewAttributeInfo, Object> {
    @Override protected void updateItem(Object item, boolean empty) {
      super.updateItem(item, empty);

      if (getTableRow().getItem() == null || empty || item == null) {
        setText(null);
        setGraphic(null);
        return;
      }

      TableViewAttributeInfo info = (TableViewAttributeInfo) getTableRow().getItem();

      String text = item.toString();
      if (info.isFilePointer()) {
        setText(text.substring(text.lastIndexOf('/') + 1) + " (" + text + ")");
        HBox box = new HBox();

        ImageView imageview = new ImageView();
        imageview.setFitHeight(16);
        imageview.setFitWidth(16);
        imageview.setImage(new Image("images/folder.png"));
        imageview.setCursor(Cursor.HAND);

        String filePath = text.substring(0, text.lastIndexOf(':'));
        imageview.setOnMouseClicked(e -> {
          boolean success = false;
          success = DrASTGUI.openFile(new File(filePath));
          if (!success) {
            Log.error("File \"%s\" could not be opened on this system", filePath);
          }
        });
        box.getChildren().addAll(imageview);
        //SETTING ALL THE GRAPHICS COMPONENT FOR CELL
        setGraphic(box);
        Tooltip.install(imageview, new Tooltip("open \"" + filePath + "\""));
        setContentDisplay(ContentDisplay.RIGHT);
      } else {
        setText(formatter.format(text));
        setCursor(Cursor.DEFAULT);
      }
    }

  }

  /**
   * Class for the cells in the table views.
   */
  private class AttributeValueCell extends TreeTableCell<TerminalValueTreeItemView, String> {
    @Override protected void updateItem(String item, boolean empty) {
      super.updateItem(item, empty);

      if (getTreeTableRow() == null || getTreeTableRow().getItem() == null || empty) {
        setText(null);
        setGraphic(null);
        return;
      }

      setText(String.valueOf(item));
      setStyle("-fx-text-fill:#ffffff;");

      TerminalValueTreeItemView view = getTreeTableRow().getItem();
      if (view.isParameter()) {
        return;
      }

      TerminalValue info = view.getNodeInfo();
      if (info == null || item != null) {
        return;
      }
      if (view.isComputable()) {
        setText("right click to compute");
        setStyle("-fx-text-fill:#999999;");
      }

      setOnMouseClicked(event -> {
        if (event.getButton() == MouseButton.SECONDARY) {
          if (getTreeTableRow().getItem() != null && (view.isComputable() && !mon
              .isFunctionRunning())) {
            onComputeClicked();
          }
        }
      });
    }
  }

  public class ClusterInfo {
    private final String typeName;
    private final int count;

    public ClusterInfo(String typeName, int count) {
      this.typeName = typeName;
      this.count = count;
    }

    public int getCount() {
      return count;
    }

    public String getTypeName() {
      return typeName;
    }
  }

  /**
   * This is a factory class for the tableViews in the AttributeTabController
   * It has basically the same fields as NodeInfoHolder, but other types.
   * This is done to ease the work of the programmer, so the fields in the
   * table views will auto update on change.
   */
  public class TableViewAttributeInfo {

    private final SimpleStringProperty name;
    private final SimpleObjectProperty<Object> value;
    private final boolean filePointer;

    public TableViewAttributeInfo(String name, Object value, boolean filePointer) {
      this.name = new SimpleStringProperty(name);
      this.value = new SimpleObjectProperty<>(value);
      this.filePointer = filePointer;
    }

    public String getName() {
      return name.get();
    }

    public void setName(String name) {
      this.name.set(name);
    }

    public Object getValue() {
      return value.get();
    }

    public void setValue(Object value) {
      this.value.set(value);
    }

    public boolean isFilePointer() {
      return filePointer;
    }

  }
}
