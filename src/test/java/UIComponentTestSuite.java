import configAST.ConfigParser;
import configAST.ConfigScanner;
import configAST.DebuggerConfig;
import configAST.ErrorMessage;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Graph;
import jastaddad.api.ASTAPI;
import jastaddad.api.JastAddAdAPI;
import jastaddad.api.filteredtree.GenericTreeNode;
import jastaddad.ui.UIMonitor;
import jastaddad.ui.controllers.Controller;
import jastaddad.ui.graph.GraphView;
import jastaddad.ui.graph.UIEdge;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import javafx.stage.Stage;
import junit.framework.Assert;
import org.junit.Test;

import java.awt.geom.RoundRectangle2D;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;

/**
 * Created by gda10jli on 11/17/15.
 */
public class UIComponentTestSuite extends UIApplicationTestHelper {

    protected Object getRootNode() {
        try {
            System.out.println("start UI tests");
            try{
                ConfigScanner scanner = new ConfigScanner(new FileReader("tests/uiTests/allBlocks/testInput.cfg"));
                ConfigParser parser = new ConfigParser();
                DebuggerConfig program = (DebuggerConfig) parser.parse(scanner);
                if (!program.errors().isEmpty()) {
                    System.err.println();
                    System.err.println("Errors: ");
                    for (ErrorMessage e: program.errors()) {
                        System.err.println("- " + e);
                    }
                } else {
                    return program;
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found!");
                //System.exit(1);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
        }
        return null;
    }

    protected static UIMonitor mon;
    protected static JastAddAdAPI jastAddAd;
    protected static Controller con;
    private static boolean init = true;

    @Override
    public void start(Stage stage) throws Exception {
        if(!init)
            return;
        init = false;
        jastAddAd = new JastAddAdAPI(getRootNode());
        jastAddAd.run();
        mon = new UIMonitor(jastAddAd.api());
        FXMLLoader loader = new FXMLLoader();
        Parent rootView = loader.load(getClass().getResource("/main.fxml").openStream());
        con = loader.<Controller>getController();
        mon.setController(con);
        GraphView graphview = new GraphView(mon);
        mon.setGraphView(graphview);
        con.init(mon, graphview);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setTitle("JastAddDebugger " + ASTAPI.VERSION);
        stage.setScene(new Scene(rootView, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight()));
        stage.setMaximized(true);
        stage.show();
        ScrollPane center = (ScrollPane) rootView.lookup("#graphViewScrollPane");
        center.setContent(graphview);
    }

    @Test
    public void testGraphClick(){
        mon.getGraphView();
    }//Todo add some checks, atm it does nothing

    @Test
    public void treeViewTab(){ //Todo add some checks, atm it does nothing
        clickOn("#treeViewTabNode");
        TreeView<GenericTreeNode> tree = find("#graphTreeView");
        expandTreeView(tree.getTreeItem(0));
        tree.getTreeItem(0).setExpanded(false);
        clickOn("#graphViewTabNode");
    }

   private void expandTreeView(TreeItem<GenericTreeNode> node) {
        if (node == null)
            return;
        node.setExpanded(true);
        for(TreeItem<GenericTreeNode> child : node.getChildren())
            expandTreeView(child);
    }

    @Test
    public void treeViewStructure(){
        TreeView<GenericTreeNode> tree = find("#graphTreeView");
        TreeItem<GenericTreeNode> treeItem = tree.getTreeItem(0);
        compareTreeView(treeItem, mon.getApi().getFilteredTree());
    }

    private void compareTreeView(TreeItem<GenericTreeNode> parent, GenericTreeNode node) {
        assertTrue("TreeView nodes does not match, TreeItem = " +
                parent.getValue() + " : ASTAPI = " + node,
                node.equals(parent.getValue()));

        assertTrue("TreeView has wrong number of children, TreeView = " +
                parent.getChildren().size() + " : ASTAPI = " +
                node.getChildren().size(),
                parent.getChildren().size() == node.getChildren().size());

        for(int i = 0; i < parent.getChildren().size(); i++) {
            compareTreeView(parent.getChildren().get(i), node.getChildren().get(i));
        }
    }

    @Test
    public void testMinimize() { //Todo add some checks, atm it does nothing
        clickOn("#minimizeConsole");
        clickOn("#minimizeRightSide");
        clickOn("#minimizeLeftSide");

        clickOn("#minimizeLeftSide");
        clickOn("#minimizeRightSide");
        clickOn("#minimizeConsole");

        push(KeyCode.F);
        push(KeyCode.F);
    }

   @Test
    public void testConsole() {//Todo add some checks, atm it does nothing
        clickOn("#consoleTabWarning");
        mon.getController().addWarning("Test warning");
        clickOn("#consoleTabError");
        mon.getController().addError("Test error");
        clickOn("#consoleTabMessage");
        mon.getController().addMessage("Test message");
        clickOn("#consoleTabAll");
    }

    @Test
    public void leftMinimizeButton(){
        testMinimizeButton("#centerSplitPane", 0, "#minimizeLeftSide", 0.5, 0.1, false);
    }

    @Test
    public void rightMinimizeButton(){
        testMinimizeButton("#centerSplitPane", 1, "#minimizeRightSide", 0.5, 0.9, true);
    }

    private void testMinimizeButton(String splitter, int divider, String button, double defaultPos, double minimizedPos, boolean bg){
        SplitPane splitPane = find(splitter);
        splitPane.getDividers().get(divider).setPosition(0.5);
        clickOn(button);
        boolean res = bg ? splitPane.getDividers().get(divider).getPosition() > minimizedPos : splitPane.getDividers().get(divider).getPosition() < minimizedPos;
        assertTrue("Minimize button: " + button + " minimizing does not work correctly. ", res);
        clickOn(button);
        res = bg ? splitPane.getDividers().get(0).getPosition() < minimizedPos : splitPane.getDividers().get(0).getPosition() > minimizedPos;
        assertTrue("Minimize button: " + button + " expanding does not work correctly. ", res);
    }

    @Test
    public void compareApiTreeAndGraphTree(){
        DelegateForest<GenericTreeNode, UIEdge> graph = mon.getGraphView().getJungGraph();

        GenericTreeNode apiRoot = mon.getRootNode();
        System.out.println("NULL? " + graph.getRoot());
        //assertEquals("Graph tree and API tree does not have the same root. api root: " + apiRoot.toString() + " graph root: " + graph.getRoot().toString(), apiRoot, graph.getRoot());
        compareTrees(apiRoot, graph);
    }

    private void compareTrees(GenericTreeNode apiRoot, Graph<GenericTreeNode, UIEdge> graph) {

        ArrayList<GenericTreeNode> graphChildren = new ArrayList<>();
        for(UIEdge graphEdge : graph.getOutEdges(apiRoot)){
            if(!graphEdge.isReference()){
                graphChildren.add(graph.getDest(graphEdge));
            }
        }

        // same children count?
        assertEquals("Graph tree has to many children: " + graphChildren.size() + "should be: " + graphChildren.size(),
                apiRoot.getChildren().size(),
                graphChildren.size());

        // same children??
        for(GenericTreeNode child : apiRoot.getChildren()){
            boolean s = graphChildren.contains(child);
            assertTrue("API tree node: " + apiRoot.toString() + " is missing in graph.",
                    s);
        }

        // next iteration
        for(GenericTreeNode child : apiRoot.getChildren())
            compareTrees(child, graph);
    }

    private GenericTreeNode getChild(ArrayList<GenericTreeNode> list, GenericTreeNode child){
        for(GenericTreeNode b : list){
            if(b.equals(child))
                return b;
        }
        return null;
    }

}
