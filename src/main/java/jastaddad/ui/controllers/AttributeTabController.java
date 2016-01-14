package jastaddad.ui.controllers;

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import com.sun.javafx.scene.control.skin.TreeTableViewSkin;
import jastaddad.api.ASTAPI;
import jastaddad.api.AlertMessage;
import jastaddad.api.JastAddAdAPI;
import jastaddad.api.Node;
import jastaddad.api.filteredtree.GenericTreeCluster;
import jastaddad.api.filteredtree.GenericTreeNode;
import jastaddad.api.filteredtree.TreeNode;
import jastaddad.api.nodeinfo.Attribute;
import jastaddad.api.nodeinfo.NodeInfo;
import jastaddad.ui.AttributeInfo;
import jastaddad.ui.AttributeInputDialog;
import jastaddad.ui.JastAddAdUI;
import jastaddad.ui.UIMonitor;
import jastaddad.ui.graph.GraphView;
import jastaddad.ui.uicomponent.nodeinfotreetableview.NodeInfoHolder;
import jastaddad.ui.uicomponent.nodeinfotreetableview.NodeInfoInterface;
import jastaddad.ui.uicomponent.nodeinfotreetableview.NodeInfoLabel;
import jastaddad.ui.uicomponent.nodeinfotreetableview.NodeInfoParameter;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import org.reactfx.Subscription;

import javax.swing.plaf.synth.Region;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * This is a controller class that keeps track of the attribute tab. The tab shows information of the clicked node in
 * the tree. There are two VBoxes with stuff that is used depending on what is clicked on:
 *  - clusterInfoView when a cluster is clicked.
 *  - nodeInfoView when a node is clicked.
 *
 */
public class AttributeTabController implements Initializable, ChangeListener<TreeItem<NodeInfoInterface>> {
    private UIMonitor mon;
    private GraphView graphView;
    private ContextMenu mouseMenu;

    @FXML private TreeTableView<NodeInfoInterface> attributeTableView;
    @FXML private TreeTableColumn<NodeInfoInterface, String> attributeNameCol;
    @FXML private TreeTableColumn<NodeInfoInterface, Object> attributeValueCol;

    @FXML private TableView<AttributeInfo> attributeInfoTableView;
    @FXML private TableColumn<AttributeInfo, String> attributeInfoNameCol;
    @FXML private TableColumn<AttributeInfo, Object> attributeInfoValueCol;

    @FXML private TableView<ClusterInfo> clusterInfoTableView;
    @FXML private TableColumn<ClusterInfo, String> clusterInfoNameCol;
    @FXML private TableColumn<ClusterInfo, Object> clusterInfoCountCol;

    @FXML private TextFlow nodeNameLabel;
    @FXML private Label attributeInfoLabel;

    @FXML private VBox clickedNodeInfoPane;
    @FXML private VBox nodeInfoView;
    @FXML private VBox clusterInfoView;
    @FXML private Label clusterInfoNumberLabel;


    public void init(UIMonitor mon, GraphView graphView){
        this.mon = mon;
        this.graphView = graphView;
    }

    /**
     * Will set all the listeners and the start values for the ui components.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // node information views
        attributeTableView.getSelectionModel().selectedItemProperty().addListener(this);
        attributeInfoNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        attributeInfoValueCol.setCellValueFactory(new PropertyValueFactory("value"));

        // cluster information views
        clusterInfoNameCol.setCellValueFactory(new PropertyValueFactory("typeName"));
        clusterInfoCountCol.setCellValueFactory(new PropertyValueFactory("count"));
        attributeInfoValueCol.setCellFactory(column -> new TableCell<AttributeInfo, Object>() {
                @Override
                protected void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty);

                    if(empty) {
                        setText("");
                        return;
                    }
                    AttributeInfo info = (AttributeInfo)getTableRow().getItem();

                    String text = item.toString();
                    if(info.isFilePointer()){
                        setText(text.substring(text.lastIndexOf('/') + 1) + " (" + text + ")");
                        HBox box= new HBox();
                        //box.setSpacing(10) ;

                        ImageView imageview = new ImageView();
                        imageview.setFitHeight(16);
                        imageview.setFitWidth(16);
                        imageview.setImage(new Image("images/folder.png"));
                        imageview.setCursor(Cursor.HAND);

                        String filePath = text.substring(0, text.indexOf(':'));
                        imageview.setOnMouseClicked(e ->{
                            boolean success = JastAddAdUI.openFile(new File(filePath));
                            if(!success){
                                mon.getController().addError("File \"" + filePath + "\" could not be opened on this system");
                            }
                        });
                        box.getChildren().addAll(imageview);
                        //SETTING ALL THE GRAPHICS COMPONENT FOR CELL
                        setGraphic(box);
                        Tooltip.install(imageview, new Tooltip("open \"" + filePath + "\""));
                        setContentDisplay(ContentDisplay.RIGHT);
                    }else{
                        setText(text);
                        setCursor(Cursor.DEFAULT);
                    }

                }
            });

    attributeNameCol.setCellValueFactory(param -> {
        NodeInfoInterface info = param.getValue().getValue();
        if (info != null) {
            return new ReadOnlyStringWrapper(param.getValue().getValue().getName());
        }
        return null;
    });
    attributeValueCol.setCellValueFactory(
            param -> {
                if(param.getValue().getValue() != null)
                    return new ReadOnlyObjectWrapper(param.getValue().getValue().getValue());
                return null;
            }
    );

    attributeValueCol.setCellFactory(param -> new AttributeValueCell());

    //Yey hiding the header for attribute info tableview, so ugly
    attributeInfoTableView.widthProperty().addListener((source, oldWidth, newWidth) -> {
        Pane header = (Pane) attributeInfoTableView.lookup("TableHeaderRow");
        if (header.isVisible()) {
            header.setMaxHeight(0);
            header.setMinHeight(0);
            header.setPrefHeight(0);
            header.setVisible(false);
        }
    });
    TreeViewSkinRefresher<AttributeInfo> a = new TreeViewSkinRefresher(attributeTableView);
    this.attributeTableView.setSkin(a);

    attributeNameCol.prefWidthProperty().bind(attributeTableView.widthProperty().multiply(0.50));
    attributeValueCol.prefWidthProperty().bind(attributeTableView.widthProperty().multiply(0.50));

    mouseMenu = new ContextMenu();
    MenuItem cmItem1 = new MenuItem("Compute");
    /**
     * This sub method will call the invocation of the method that has been clicked on, after the values have been added
     */
    cmItem1.setOnAction(e -> {
        TreeNode node = (TreeNode) mon.getSelectedNode();
        NodeInfoInterface selectedInfo  = attributeTableView.getSelectionModel().getSelectedItem().getValue();
        if(!selectedInfo.isNodeInfo())
            return;
        NodeInfo info = selectedInfo.getNodeInfoOrNull();
        if(info.isNTA() && !info.isParametrized()){ //Handle the non-parametrized NTA:s
            Object obj = mon.getApi().compute(node.getNode(), info);
            if(!printToConsole(obj))
                return;
            setAttributeList(node, false);
            mon.getController().updateUI();
            return;
        }

        AttributeInputDialog dialog = new AttributeInputDialog(info, node, mon);
        dialog.setNodeInfoPos(attributeTableView.getSelectionModel().getSelectedIndex());
        dialog.init();

        dialog.setOnCloseRequest(event -> {
            mon.getController().nodeSelected(dialog.getTreeNode(), false);
            if(!dialog.invokeButtonPressed())
                return;

            Object[] result = dialog.getResult();
            if(result == null ||  mon.getLastRealNode() == null)
                return;
            Object value = mon.getApi().compute(dialog.getTreeNode().getNode(), dialog.getInfo(), result);
            if(!printToConsole(value))
                return;

            if(info.isNTA())
                mon.getController().updateUI();
            setAttributeList(node, false);

            // Below code is for setting the selected position to the last computed value, in case the selected row
            // is a parameterized attribute
            attributeTableView.getSelectionModel().select(dialog.getNodeInfoPos());
            TreeItem<NodeInfoInterface> selected = attributeTableView.getSelectionModel().getSelectedItem();

            if(selected == null || !selected.getValue().isNodeInfo())
                return;
            NodeInfo infoSelected = selected.getValue().getNodeInfoOrNull();
            if(!info.isAttribute() && !info.isParametrized())
                return;
            Attribute attribute = (Attribute) infoSelected;
            int i = 0;
            for(Map.Entry<String, Object> entry : attribute.getComputedEntry()){
                i++;
                if(entry.getKey().equals(attribute.getLastComputedkey())) {
                    attributeTableView.getSelectionModel().select(dialog.getNodeInfoPos() + i);
                    break;
                }
            }
        });
        dialog.show();
    });
    mouseMenu.getItems().add(cmItem1);
    showThisInAttributeTab(nodeInfoView);
}

/**
 * Switch to the view child.
 * @param child
 */
private void showThisInAttributeTab(VBox child){
    clickedNodeInfoPane.getChildren().clear();
    clickedNodeInfoPane.getChildren().add(child);
}

/**
 * Used when computing and invoking methods in this controller. prints errors, values and warnings to the console.
 * @param value
 * @return
 */
private boolean printToConsole(Object value){
    if(mon.getApi().containsError(AlertMessage.INVOCATION_ERROR)) {
        mon.getController().addErrors(mon.getApi().getErrors(AlertMessage.INVOCATION_ERROR));
        mon.getController().addWarnings(mon.getApi().getWarnings(AlertMessage.INVOCATION_WARNING));
        mon.getController().addMessage("Computation unsuccessful");
        return false;
    }
    mon.getController().addWarnings(mon.getApi().getWarnings(AlertMessage.INVOCATION_WARNING));
    mon.getController().addMessage("Computation successful : " + value);
    return true;
}

/**
 * Called when a funciton starts from the Controller. A function can be a dialog.
 */
public void functionStarted(){

}

/**
 * Called when a funciton stops from the Controller. A function can be a dialog.
 */
public void functionStopped(){

}

/**
 * Sets the attributes in the tableview, called when a node has been selected.
 * Will Clear the list if the selected node is null or not a "real" node
 */
public void setAttributes(){
    GenericTreeNode node = mon.getSelectedNode();
    nodeNameLabel.getChildren().clear();
    if(node == null || !node.isNode() || node.isNullNode()) {
        if(attributeTableView.getRoot() != null && attributeTableView.getRoot().getChildren() != null) {
            attributeTableView.getRoot().getChildren().clear();
            attributeInfoTableView.getItems().clear();
        }
        clusterInfoNumberLabel.setText("");
        if(clusterInfoTableView != null){
            clusterInfoTableView.getItems().clear();
        }
        return;
    }

    LinkedList list = new LinkedList(mon.getApi().getInheritanceChain(node.getNode().simpleNameClass));
    Iterator<Class> it = list.descendingIterator();
    Text parent = null;
    String indent = "....";
    int i = 0;
    while (it.hasNext()){
        Class p = it.next();
        if(i == list.size()-1) {
            parent = new Text("   " + p.getTypeName() + "\n");
        }else {
            parent = new Text(" " + new String(new char[i]).replace("\0", indent) + p.getTypeName() + "\n");
        }

        i++;
        parent.getStyleClass().add("class-parent-type-text");
        //indent += "\t";
        nodeNameLabel.getChildren().add(parent);
    }
    if(parent != null)
        parent.getStyleClass().add("class-type-text");

    setAttributeList((TreeNode) node, true);
}

/**
 * Sets the attributeTableView, which all the attributes for the given node
 * @param node
 * @param compute
 */
public void setAttributeList(TreeNode node, boolean compute){
    Node n = node.getNode();
    if(compute) {
        mon.getApi().compute(n);
        if(mon.getApi().containsError(AlertMessage.INVOCATION_ERROR))
            mon.getController().addErrors(mon.getApi().getErrors(AlertMessage.INVOCATION_ERROR));
        attributeTableView.getSelectionModel().clearSelection();
    }

    ((TreeViewSkinRefresher)attributeTableView.getSkin()).refresh(); //Refreshing the css.

    TreeItem<NodeInfoInterface> mainParent = new TreeItem<>(null);
    attributeTableView.setRoot(mainParent);
    attributeTableView.setShowRoot(false);
    mainParent.setExpanded(true);


    addSectionToAttributeList("Attributes", n.getNodeContent().getAttributes(), mainParent);
    addSectionToAttributeList("Nonterminal attributes", n.getNodeContent().getNTAs(), mainParent);
    addSectionToAttributeList("Tokens", n.getNodeContent().getTokens(), mainParent);
}

/**
 * Add all NodeInfos from list to the parent with the name label.
 * @param label
 * @param list
 * @param parent
 */
private void addSectionToAttributeList(String label, Collection<NodeInfo> list, TreeItem parent){
    if(list.size() == 0)
        return;

    NodeInfoInterface fucker = new NodeInfoLabel(label);
    TreeItem<NodeInfoInterface> tokensItem = new TreeItem<>(fucker);
    tokensItem.setExpanded(true);

    // go through all methods and sort them on kind
    HashMap<String, ArrayList<NodeInfo>> methods = new HashMap<>();
    for (NodeInfo info : list) {
        if(!methods.containsKey(info.getKindString())){
            methods.put(info.getKindString(), new ArrayList<>());
        }
        methods.get(info.getKindString()).add(info);
    }

    // Add all methods to the list
    for(Map.Entry<String, ArrayList<NodeInfo>> entry : methods.entrySet()){
        if(entry.getValue().size() > 0) {

            TreeItem<NodeInfoInterface> kindLabel = new TreeItem<>(new NodeInfoLabel(entry.getKey()));
            kindLabel.setExpanded(true);
            tokensItem.getChildren().add(kindLabel);

            for (NodeInfo info : entry.getValue()) {
                TreeItem<NodeInfoInterface> methodItem = new TreeItem<>(new NodeInfoHolder(info));
                kindLabel.getChildren().add(methodItem);
                if(!info.isAttribute() || !info.isParametrized()){
                    continue;
                }
                Attribute attr = (Attribute) info;
                methodItem.setExpanded(true);
                for(Map.Entry<String, Object> computedEntry : attr.getComputedEntry()){
                    String name = "";
                    Object[] params = attr.getUsedParameters().get(computedEntry.getKey());
                    int i = params.length;
                    for(Object param : params){
                        name += param == null ? "null" : param.toString();
                        if(--i > 0){
                            name += ", ";
                        }
                    }
                    TreeItem<NodeInfoInterface> computedItem = new TreeItem<>(new NodeInfoParameter(name, computedEntry.getValue(), info));
                    methodItem.getChildren().add(computedItem);
                }
            }
        }
    }
    parent.getChildren().add(tokensItem);
}

/**
 * Listener for the attribute treetableview, sets the attributeInfoTableView for the selected attribute.
 * It will also remove old references, and add new references if the value of selected attribute is a AST node.
 * Also tells the main controller that an attribute was selected.
 * @param observable
 * @param oldValue
 * @param newValue
 */
@Override
public void changed(ObservableValue<? extends TreeItem<NodeInfoInterface>> observable, TreeItem<NodeInfoInterface> oldValue, TreeItem<NodeInfoInterface> newValue) {
    if(oldValue != null && oldValue.getValue() != null) {
        if(oldValue.getValue().isNodeInfo())
            mon.getApi().getNodeReferencesAndHighlightThem(oldValue.getValue().getNodeInfoOrNull().getValue(), false);
        else if(oldValue.getValue().isParameter())
            mon.getApi().getNodeReferencesAndHighlightThem(oldValue.getValue().getValue(), false);
    }

    if(newValue != null) {
        NodeInfoInterface infoHolder = newValue.getValue();
        NodeInfo info = null;
        Object value = null;
        if(infoHolder.isNodeInfo()) {
            info = newValue.getValue().getNodeInfoOrNull();
            value = info.getValue();
        } else if(infoHolder.isParameter()){
            NodeInfoParameter paramHolder = ((NodeInfoParameter)newValue.getValue());
            info = paramHolder.info;
            value = paramHolder.getValue();
        }
        mon.setSelectedInfo(info);
        mon.getController().attributeInNodeSelected(info);
        setAttributeInfo(info);
        setReference(value);

    }else{
        mon.setSelectedInfo(null);
        setAttributeInfo(null);
        setReference(null);
    }
}

/**
 * Fill the attributeInfoTableView with information about the attribute
 * @param info
 */
private void setAttributeInfo(NodeInfo info){
    if(info == null ) {
        attributeInfoLabel.setText("");
        attributeInfoTableView.getItems().clear();
        return;
    }
    attributeInfoLabel.setText(info.print());
    attributeInfoTableView.setItems(FXCollections.observableArrayList(AttributeInfo.toArray(info.getInfo())));
}

/**
 * Adds references from the selected node in the monitor and the values found in the AttributeInfo supplied.
 * @param value
 */
public void setReference(Object value){
    ArrayList<GenericTreeNode> newRefs = null;
    if(value != null)
        newRefs = mon.getApi().getNodeReferencesAndHighlightThem(value, true);
    graphView.setReferenceEdges(newRefs, mon.getSelectedNode());
}


public void nodeSelected() {
    GenericTreeNode node = mon.getSelectedNode();
    if(node.isNode()) {
        showThisInAttributeTab(nodeInfoView);
        setAttributes();
    }else{
        showThisInAttributeTab(clusterInfoView);
        setClusterInfo();
    }

}

/**
 * set the information of the cluster table view.
 */
private void setClusterInfo(){
    GenericTreeCluster cluster = (GenericTreeCluster)mon.getSelectedNode();
    clusterInfoNumberLabel.setText("Cluster. " + cluster.getNodeCount() + " node" + (cluster.getNodeCount() == 1 ? "" : "s"));

    ArrayList<ClusterInfo> itemList = new ArrayList<>();
    for(Map.Entry<String, Integer> type : cluster.getTypeList().entrySet()){
        itemList.add(new ClusterInfo(type.getKey(), type.getValue()));
    }
    ObservableList<ClusterInfo> items = FXCollections.observableArrayList (itemList);
    clusterInfoTableView.setItems(items);
}

/**
 * Class for the cells in the tableviews
 */
private class AttributeValueCell extends TreeTableCell<NodeInfoInterface, Object>{
    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);

        if (getTreeTableRow().getItem() == null || empty) {
            setText(null);
            setGraphic(null);
            return;
        }

        setText(String.valueOf(item));
        setStyle("-fx-text-fill:#ffffff;");

        NodeInfo info = (getTreeTableRow().getItem()).getNodeInfoOrNull();
        if(info == null)
            return;
        if(info == null || item != null)
            return;

        if (info.isParametrized() || info.isNTA()) {
            setText("right click to compute");
            setStyle("-fx-text-fill:#999999;");
        }else{

        }

        setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                boolean yep = (info.isNTA() || info.isParametrized()) && info.getValue() == null;
                if(getTreeTableRow().getItem() != null &&
                        (yep && !mon.isFunctionRunning())) {
                    mouseMenu.show(this, event.getScreenX(), event.getScreenY());
                }
            }
        });
    }
}

public class ClusterInfo{
    private String typeName;
    private int count;

    public ClusterInfo(String typeName, int count){
        this.typeName = typeName;
        this.count = count;
    }

    public String getTypeName(){return typeName;}

    public int getCount(){return count;}
}

private class TreeViewSkinRefresher<AttributeInfo> extends TreeTableViewSkin {

    public TreeViewSkinRefresher(TreeTableView treeView) {
        super(treeView);
    }

    public void refresh(){
        super.flow.recreateCells();
    }
}
}