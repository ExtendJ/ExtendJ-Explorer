package drast.views.gui.controllers;

import com.sun.javafx.scene.control.skin.TreeTableViewSkin;
import drast.model.AlertMessage;
import drast.model.Node;
import drast.model.filteredtree.GenericTreeCluster;
import drast.model.filteredtree.GenericTreeNode;
import drast.model.filteredtree.TreeNode;
import drast.model.terminalvalues.Attribute;
import drast.model.terminalvalues.TerminalValue;
import drast.model.terminalvalues.TerminalValueInfo;
import drast.views.gui.DrASTGUI;
import drast.views.gui.Monitor;
import drast.views.gui.dialogs.AttributeInputDialog;
import drast.views.gui.guicomponent.TextFormatter;
import drast.views.gui.guicomponent.nodeinfotreetableview.TerminalValueTreeItem;
import drast.views.gui.guicomponent.nodeinfotreetableview.TerminalValueTreeItemParameter;
import drast.views.gui.guicomponent.nodeinfotreetableview.TerminalValueTreeItemView;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.control.*;
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
import java.util.*;

/**
 * This is a controller class that keeps track of the attribute tab. The tab shows information of the clicked node in
 * the tree. There are two VBoxes with stuff that is used depending on what is clicked on:
 *  - clusterInfoView when a cluster is clicked.
 *  - nodeInfoView when a node is clicked.
 *
 */
public class AttributeTabController implements Initializable, ChangeListener<TreeItem<TerminalValueTreeItemView>>, ControllerInterface {
    private Monitor mon;
    private ContextMenu mouseMenu;
    private TextFormatter formatter;

    @FXML private TreeTableView<TerminalValueTreeItemView> attributeTableView;
    @FXML private TreeTableColumn<TerminalValueTreeItemView, String> attributeNameCol;
    @FXML private TreeTableColumn<TerminalValueTreeItemView, Object> attributeValueCol;

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

    public void init(Monitor mon){
        this.mon = mon;
        setTextFormater();
    }

    /**
     * Will set all the listeners and the start values for the gui components.
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
        attributeNameCol.setCellValueFactory(param -> {
            TerminalValueTreeItemView info = param.getValue().getValue();
            if(info != null) {
                return new ReadOnlyStringWrapper(formatter.format(info.getName()));
            }
            return null;
        });
        attributeValueCol.setCellValueFactory(param -> {
            TerminalValueTreeItemView info = param.getValue().getValue();
            if(info != null)
                return new ReadOnlyObjectWrapper(formatter.format(info.getValue()));
            return null;
        });

        attributeValueCol.setCellFactory(param -> new AttributeValueCell());
        attributeInfoValueCol.setCellFactory(column -> new AttributeInfoValueCell());

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
        TreeViewSkinRefresher<TableViewAttributeInfo> a = new TreeViewSkinRefresher(attributeTableView);
        this.attributeTableView.setSkin(a);

        attributeNameCol.prefWidthProperty().bind(attributeTableView.widthProperty().multiply(0.50));
        attributeValueCol.prefWidthProperty().bind(attributeTableView.widthProperty().multiply(0.50));

        mouseMenu = new ContextMenu();
        MenuItem cmItem1 = new MenuItem("Compute");
        cmItem1.setOnAction(e ->  { onInvokeClicked();});
        mouseMenu.getItems().add(cmItem1);
        showThisInAttributeTab(nodeInfoView);
    }

    private void setTextFormater(){
        if(mon.getASTBrain().hasRoot())
            formatter = new TextFormatter(mon.getASTBrain().getRootClass());
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
        if(mon.getASTBrain().containsError(AlertMessage.INVOCATION_ERROR)) {
            mon.getController().addMessage("Computation unsuccessful");
            return false;
        }
        mon.getController().addMessage("Computation successful : " + value);
        return true;
    }

    @Override
    public void onApplicationClose(){}

    /**
     * Called when a funciton starts from the Controller. A function can be a dialog.
     */
    @Override
    public void functionStarted(){}

    /**
     * Called when a funciton stops from the Controller. A function can be a dialog.
     */
    @Override
    public void functionStopped(){}


    @Override
    public void nodeSelected(GenericTreeNode node) {
        if(node == null)
            return;
        if(node.isNode()) {
            objectInheritanceLabel.setText("Object inheritance");
            attributeLabel.setText("Attributes");
            showThisInAttributeTab(nodeInfoView);
            setAttributes();
        }else{
            showThisInAttributeTab(clusterInfoView);
            setClusterInfo();
        }
    }

    @Override
    public void nodeDeselected() {
        setAttributes();
    }

    @Override
    public void updateGUI() {
        setAttributes();
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
                objectInheritanceLabel.setText("");
                attributeLabel.setText("");
            }
            clusterInfoNumberLabel.setText("");
            if(clusterInfoTableView != null){
                clusterInfoTableView.getItems().clear();
            }
            return;
        }

        LinkedList<Class> list = new LinkedList<>(mon.getASTBrain().getInheritanceChain(node.getNode().getSimpleClassName()));
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
            mon.getController().setOutStreams();
            mon.getASTBrain().compute(n);
            attributeTableView.getSelectionModel().clearSelection();
            mon.getController().resetOutStreams();
        }

        ((TreeViewSkinRefresher)attributeTableView.getSkin()).refresh(); //Refreshing the css.

        TreeItem<TerminalValueTreeItemView> mainParent = new TreeItem<>(null);
        attributeTableView.setRoot(mainParent);
        attributeTableView.setShowRoot(false);
        mainParent.setExpanded(true);


        addSectionToAttributeList("Attributes", n.getNodeData().getAttributes(), mainParent);
        addSectionToAttributeList("Nonterminal attributes", n.getNodeData().getNTAs(), mainParent);
        addSectionToAttributeList("Tokens", n.getNodeData().getTokens(), mainParent);
    }

    /**
     * Add all NodeInfos from list to the parent with the name label.
     * @param label
     * @param list
     * @param parent
     */
    private void addSectionToAttributeList(String label, Collection<TerminalValue> list, TreeItem parent){
        if(list.size() == 0)
            return;

        TreeItem<TerminalValueTreeItemView> tokensItem = new TreeItem<>(new TerminalValueTreeItemView(label));
        tokensItem.setExpanded(true);

        // go through all methods and sort them on kind
        HashMap<String, ArrayList<TerminalValue>> methods = new HashMap<>();
        for (TerminalValue info : list) {
            if(!methods.containsKey(info.getKind())){
                methods.put(info.getKind(), new ArrayList<>());
            }
            methods.get(info.getKind()).add(info);
        }

        // Add all methods to the list
        for(Map.Entry<String, ArrayList<TerminalValue>> entry : methods.entrySet()){
            if(entry.getValue().size() > 0) {
                TreeItem<TerminalValueTreeItemView> kindLabel;
                if(entry.getValue().get(0).getKind() != null) {
                    kindLabel = new TreeItem<>(new TerminalValueTreeItemView(entry.getKey()));
                    kindLabel.setExpanded(true);
                    tokensItem.getChildren().add(kindLabel);
                }else
                    kindLabel = tokensItem;

                for (TerminalValue info : entry.getValue()) {
                    TreeItem<TerminalValueTreeItemView> methodItem = new TreeItem<>(new TerminalValueTreeItem(info));
                    kindLabel.getChildren().add(methodItem);
                    if(!info.isAttribute() || !info.isParametrized())
                        continue;

                    Attribute attr = (Attribute) info;
                    methodItem.setExpanded(true);
                    for(Map.Entry<String, Object> computedEntry : attr.getComputedEntrys()){
                        Object[] params = attr.getUsedParameters().get(computedEntry.getKey());
                        TreeItem<TerminalValueTreeItemView> computedItem = new TreeItem<>(new TerminalValueTreeItemParameter(params, computedEntry.getValue(), info));
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
    public void changed(ObservableValue<? extends TreeItem<TerminalValueTreeItemView>> observable, TreeItem<TerminalValueTreeItemView> oldValue, TreeItem<TerminalValueTreeItemView> newValue) {
        mon.clearSelectedParameterNodes();
        if(oldValue != null && oldValue.getValue() != null)
            mon.getHighlightReferencesNodes().clear();

        if(newValue != null) {
            TerminalValueTreeItemView infoHolder = newValue.getValue();
            TerminalValue info = null;
            Object value = null;
            if(infoHolder.getTerminalValue()) {
                info = infoHolder.getNodeInfo();
                value = infoHolder.getValue();
            }

            if(infoHolder.isParameter()){
                for(Object param : ((TerminalValueTreeItemParameter)infoHolder).getParams()){
                    GenericTreeNode tmp = mon.getASTBrain().getTreeNode(param);
                    if(tmp != null)
                        mon.addSelectedParameterNodes(tmp);
                }

            }

            mon.setSelectedInfo(infoHolder);
            mon.getController().attributeInNodeSelected(info);
            setAttributeInfo(info, value);
            setReference(value);

        }else{
            mon.setSelectedInfo(null);
            setAttributeInfo(null, null);
            setReference(null);
        }
    }

    /**
     * Fill the attributeInfoTableView with information about the attribute
     * @param info
     */
    private void setAttributeInfo(TerminalValue info, Object value){
        if(info == null ) {
            attributeInfoLabel.setText("");
            attributeInfoTableView.getItems().clear();
            return;
        }
        attributeInfoLabel.setText("Information about selected attribute");
        attributeInfoTableView.setItems(FXCollections.observableArrayList(toArray(info.getInfo(value))));
    }

    /**
     * Adds references from the selected node in the monitor and the values found in the AttributeInfo supplied.
     * @param value
     */
    public void setReference(Object value){
        ArrayList<GenericTreeNode> newRefs = null;
        if(value != null)
            newRefs = getNodeReferencesAndHighlightThem(value);
        mon.getGraphView().setReferenceEdges(newRefs, mon.getSelectedNode());
    }

    public ArrayList<GenericTreeNode> getNodeReferencesAndHighlightThem(Object value){
        ArrayList<GenericTreeNode> nodes = new ArrayList<>();
        for(Object o : mon.getASTBrain().getNodeReferences(value)){
            GenericTreeNode node = mon.getASTBrain().getTreeNode(o);
            if(node == null)
                continue;
            nodes.add(node);
            mon.addHighlightReferencesNodes(node);
        }
        return nodes;
    }

    private ArrayList<TableViewAttributeInfo> toArray(ArrayList<TerminalValueInfo> infoList){
        ArrayList<TableViewAttributeInfo> al = new ArrayList();
        for (TerminalValueInfo info : infoList)
            al.add(new TableViewAttributeInfo(info.getName(), info.getValue(), info.isFilePointer()));
        return al;
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
     * This method will call the invocation of the method that has been clicked on, after the values have been added
     */
    private void onInvokeClicked(){
        mon.getController().setOutStreams();
        onInvoke();
        mon.getController().resetOutStreams();
    }

    private void onInvoke(){
        TreeNode node = (TreeNode) mon.getSelectedNode();
        TerminalValueTreeItemView selectedInfo  = attributeTableView.getSelectionModel().getSelectedItem().getValue();
        if(!selectedInfo.getTerminalValue())
            return;

        TerminalValue info = selectedInfo.getNodeInfo();
        if(info.isNTA() && !info.isParametrized()){ //Handle the non-parametrized NTA:s
            Object obj = mon.getASTBrain().compute(node.getNode(), info);
            setAttributeList(node, false);
            if(printToConsole(obj))
                mon.getController().updateGUI();
            return;
        }

        AttributeInputDialog dialog = new AttributeInputDialog(info, node, mon);
        dialog.setNodeInfoPos(attributeTableView.getSelectionModel().getSelectedIndex());
        dialog.init();

        dialog.setOnCloseRequest(event -> {
            mon.getController().nodeSelected(dialog.getTreeNode(), this);
            if(!dialog.invokeButtonPressed())
                return;

            Object[] result = dialog.getResult();
            if(result == null ||  mon.getLastRealNode() == null)
                return;

            TerminalValue dialogInfo = dialog.getInfo();
            Object value = mon.getASTBrain().compute(dialog.getTreeNode().getNode(), dialog.getInfo(), result);
            printToConsole(value);
            if(info.isNTA())
                mon.getController().updateGUI();

            if(!dialogInfo.isAttribute() || !dialogInfo.isParametrized()){
                attributeTableView.refresh();
                return;
            }

            TreeItem<TerminalValueTreeItemView> treeItem = attributeTableView.getTreeItem(dialog.getNodeInfoPos());
            TerminalValueTreeItemView infoView = treeItem.getValue();
            if(infoView == null || infoView.getNodeInfo() == null || !infoView.getNodeInfo().isAttribute())
                return;
            Attribute at = (Attribute) infoView.getNodeInfo();
            Attribute atd = (Attribute) dialogInfo;
            for(Map.Entry<String, Object> entry : at.getComputedEntrys()){
                if(entry.getKey().equals(atd.getLastComputedKey())) {
                    for(TreeItem<TerminalValueTreeItemView> view : treeItem.getChildren()){
                        if(view.getValue().getName().equals(atd.getLastComputedKey())) {
                            attributeTableView.getSelectionModel().select(view);
                            return;
                        }
                    }
                }
            }
            TerminalValueTreeItemView temp = new TerminalValueTreeItemParameter(atd.getUsedParameters().get(atd.getLastComputedKey()), value, atd);
            TreeItem<TerminalValueTreeItemView> tempTree = new TreeItem<>(temp);
            treeItem.getChildren().add(tempTree);
            attributeTableView.getSelectionModel().select(tempTree);

        });
        dialog.show();

    }

    public void onNewAPI() {
        setTextFormater();
        setAttributes();
    }

    /**
     * Class for the cells in the tableviews
     */
    private class AttributeInfoValueCell extends TableCell<TableViewAttributeInfo, Object>{
        @Override
        protected void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);

            if (getTableRow().getItem() == null || empty || item == null) {
                setText(null);
                setGraphic(null);
                return;
            }

            TableViewAttributeInfo info = (TableViewAttributeInfo)getTableRow().getItem();

            String text = item.toString();
            if(info.isFilePointer()){
                setText(text.substring(text.lastIndexOf('/') + 1) + " (" + text + ")");
                HBox box= new HBox();

                ImageView imageview = new ImageView();
                imageview.setFitHeight(16);
                imageview.setFitWidth(16);
                imageview.setImage(new Image("images/folder.png"));
                imageview.setCursor(Cursor.HAND);

                String filePath = text.substring(0, text.indexOf(':'));
                imageview.setOnMouseClicked(e ->{
                    boolean success = DrASTGUI.openFile(new File(filePath));
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
                setText(formatter.format(text));
                setCursor(Cursor.DEFAULT);
            }
        }

    }

    /**
     * Class for the cells in the tableviews
     */
    private class AttributeValueCell extends TreeTableCell<TerminalValueTreeItemView, Object>{
        @Override
        protected void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);

            if (getTreeTableRow() == null || getTreeTableRow().getItem() == null || empty) {
                setText(null);
                setGraphic(null);
                return;
            }

            setText(String.valueOf(item));
            setStyle("-fx-text-fill:#ffffff;");

            TerminalValueTreeItemView view = getTreeTableRow().getItem();
            if(view.isParameter())
                return;

            TerminalValue info = view.getNodeInfo();
            if(info == null || item != null)
                return;
            if (info.isParametrized() || info.isNTA()) {
                setText("right click to compute");
                setStyle("-fx-text-fill:#999999;");
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

    /**
     * This is a factory class for the tableViews in the AttributeTabController
     * It has basically the same fields as NodeInfoHolder, but other types.
     * This is done to ease the work of the programmer, so the fields in the tableview:s will auto update on change.
     */
    public class TableViewAttributeInfo {

        private final SimpleStringProperty name;
        private final SimpleObjectProperty value;
        private boolean filePointer;

        public TableViewAttributeInfo(String name, Object value, boolean filePointer){
            this.name = new SimpleStringProperty(name);
            this.value = new SimpleObjectProperty(value);
            this.filePointer = filePointer;
        }

        public String getName(){ return name.get(); }

        public void setName(String name) {
            this.name.set(name);
        }

        public Object getValue() {
            return value.get();
        }

        public void setValue(Object value) {
            this.value.set(value);
        }

        public boolean isFilePointer(){return filePointer;}

    }
}
