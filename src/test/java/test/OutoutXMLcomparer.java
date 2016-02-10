package test;

import drast.model.DrAST;
import drast.model.filteredtree.GenericTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by gda10jth on 11/18/15.
 */
public class OutoutXMLcomparer {

    public OutoutXMLcomparer(){}

    public void checkOutput(GenericTreeNode root, String expectedString, String inDirectory) {
        checkOutput(root, readXML(inDirectory + expectedString), inDirectory, 0);
    }

    private void checkOutput(GenericTreeNode root, Tree expectedRoot, String inDirectory, int level) {
        level++;
        System.out.println("==================" + nodeName(root) );
        System.out.println("==================" + expectedRoot.getName());
        assertEquals("Output is not the same", nodeName(root), expectedRoot.getName());
        for(GenericTreeNode child : root.getChildren()){
            //System.out.println(nodeName(child));
            boolean s = expectedRoot.containsChild(nodeName(child));
            assertEquals("Test Failed: " + inDirectory + ". Extra child: " + nodeName(child) + " in parent: " + root.toString(),
                    s,
                    true);
        }
        assertEquals("Test Failed: Level " + level + ". " + inDirectory + ". Missing child in parent: " + root.toString(),
                expectedRoot.isEveryChildChecked(), true);
        for(GenericTreeNode child : root.getChildren())
            checkOutput(child, expectedRoot.getChild(nodeName(child), child.getChildren().size()), inDirectory, level);
    }

    private String nodeName(GenericTreeNode node){
        String name = node.toString();
        if(!node.isNode())
            name = DrAST.CLUSTER_STRING;
        return name;
    }

    protected Tree readXML(String expected){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setNamespaceAware(true);
        dbf.setIgnoringElementContentWhitespace(true);

        Document doc = null;
        try {
            DocumentBuilder builder = dbf.newDocumentBuilder();
            InputSource is = new InputSource(expected);
            doc = builder.parse(is);
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        } catch (ParserConfigurationException e) {
            System.err.println(e);
            return null;
        } catch (IOException e) {
            System.err.println(e);
            return null;
        }
        Node docRoot = doc.getDocumentElement();
        Tree expectedRoot = new Tree(docRoot.getNodeName());
        NodeList list = docRoot.getChildNodes();
        for (int i = 0; i < list.getLength(); i++){
            readXML(list.item(i), expectedRoot);
        }
        return expectedRoot;
    }

    private void readXML(Node node, Tree parent){

        Tree treeNode = new Tree(node.getNodeName());
        //System.out.println(node.getNodeName() + " " + treeNode);
        parent.addChild(treeNode);
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++){
            readXML(list.item(i), treeNode);
        }
    }

    private class Tree{
        private String name;
        private ArrayList<Tree> children;
        private boolean checked;
        private int checkedCount;
        private boolean done;
        public Tree(String name){
            this.name = name;
            checkedCount = 0;
            checked = false;
            done = false;
            children = new ArrayList<>();
        }

        public ArrayList<Tree> getChildren(){return children;}
        public void addChild(Tree child){children.add(child);}
        public boolean containsChild(String childName){
            //System.out.println("---- " + name + " ----------- " +  childName + " " + checkedCount);
            for(Tree child : children){
                //System.out.println("Child: " + child.getName() + ": " + child.isChecked() + " " + child);
                if(!child.isChecked() && child.getName().equals(childName)){
                    child.setChecked();
                    checkedCount++;
                    return true;
                }
            }
            return false;
        }
        public Tree getChild(String childName, int size){
            for(Tree child : children){
                if(!child.isDone() && child.getName().equals(childName) && child.children.size() == size) {
                    child.setDone();
                    return child;
                }
            }
            return null;
        }
        public boolean isEveryChildChecked(){
            return checkedCount == children.size();
        }
        public void setChecked(){
            checked = true;
        }
        public void setDone(){
            done = true;
        }
        public boolean isChecked(){return checked;}
        public boolean isDone(){return done;}
        public String getName(){return name;}
    }
}
