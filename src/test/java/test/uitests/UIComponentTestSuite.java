package test.uitests;

import CalcASM.src.java.gen.ErrorMessage;
import CalcASM.src.java.gen.LangParser;
import CalcASM.src.java.gen.LangScanner;
import CalcASM.src.java.gen.Program;
import beaver.Parser;
import drast.model.ASTBrain;
import drast.model.DrAST;
import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.Monitor;
import drast.views.gui.controllers.Controller;
import drast.views.gui.graph.GraphEdge;
import drast.views.gui.graph.GraphView;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Graph;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by gda10jli on 11/17/15.
 */
public class UIComponentTestSuite extends UIApplicationTestHelper {

    private String inDirectory = "tests/uiTests/allBlocks/";

    protected Object getRootNode() {
        try {
            System.out.println("start UI tests");
            try{
                LangScanner scanner = new LangScanner(new FileReader(inDirectory + "input.calc"));
                LangParser parser = new LangParser();
                Program program = (Program) parser.parse(scanner);
                return program;
            } catch (FileNotFoundException e) {
                System.out.println("File not found!");
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            } catch (Parser.Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            fail("- Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    protected static Monitor mon;
    protected static DrAST drAST;
    protected static Controller con;
    private static boolean init = true;

    @Override
    public void start(Stage stage) throws Exception {
        if(!init)
            return;
        init = false;
        drAST = new DrAST(getRootNode());
        drAST.setFilterPath(inDirectory + "/filter.fcl");
        drAST.run();
        mon = new Monitor(drAST);
        FXMLLoader loader = new FXMLLoader();
        Parent rootView = loader.load(getClass().getResource("/main.fxml").openStream());
        con = loader.<Controller>getController();
        mon.setController(con);
        GraphView graphview = new GraphView(mon);
        mon.setGraphView(graphview);
        con.init(mon, graphview);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setTitle("JastAddDebugger " + ASTBrain.VERSION);
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
        TreeView<GenericTreeNode> tree = find("#treeView");
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
        TreeView<GenericTreeNode> tree = find("#treeView");
        TreeItem<GenericTreeNode> treeItem = tree.getTreeItem(0);
        compareTreeView(treeItem, mon.getBrain().getFilteredTree());
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
        mon.getController().addWarning("Test warning");
        mon.getController().addError("Test error");
        mon.getController().addMessage("Test message");
        clickOn("#messagesBox");
        clickOn("#warningsBox");
        clickOn("#errorsBox");
        clickOn("#messagesBox");
        clickOn("#warningsBox");
        clickOn("#errorsBox");
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
        DelegateForest<GenericTreeNode, GraphEdge> graph = mon.getGraphView().getJungGraph();
        GenericTreeNode apiRoot = mon.getRootNode();
        //assertEquals("Graph tree and API tree does not have the same root. api root: " + apiRoot.toString() + " graph root: " + graph.getRoot().toString(), apiRoot, graph.getRoot());
        compareTrees(apiRoot, graph);
    }

    private void compareTrees(GenericTreeNode apiRoot, Graph<GenericTreeNode, GraphEdge> graph) {

        ArrayList<GenericTreeNode> graphChildren = new ArrayList<>();
        for(GraphEdge graphEdge : graph.getOutEdges(apiRoot)){
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
