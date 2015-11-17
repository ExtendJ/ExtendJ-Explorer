import static org.junit.Assert.*;

import configAST.ConfigParser;
import configAST.ConfigScanner;
import configAST.DebuggerConfig;
import configAST.ErrorMessage;
import jastaddad.api.JastAddAdAPI;
import jastaddad.api.filteredtree.GenericTreeNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


@RunWith(Parameterized.class)
public class TestJastAddAd extends AbstractParameterizedTest {
	/**
	 * Directory where test files live
	 */
	private static final String TEST_DIR = "tests/";

	/**
	 * Construct a new JastAdd test
	 * @param dir filename of test input file
	 */
	public TestJastAddAd(String dir) {
		super(TEST_DIR, dir, JastAddAdAPI.FILE_NAME);
	}

	/**
	 * Run the JastAdd test
	 */
	@Test
	public void runTest() {
		try {
			System.out.println("start test in: " + inDirectory);
			try{
				String filename = inDirectory + "/testInput.cfg";
				ConfigScanner scanner = new ConfigScanner(new FileReader(filename));
				ConfigParser parser = new ConfigParser();
				DebuggerConfig program = (DebuggerConfig) parser.parse(scanner);
				if (!program.errors().isEmpty()) {
					System.err.println();
					System.err.println("Errors: ");
					for (ErrorMessage e: program.errors()) {
						System.err.println("- " + e);
					}
				} else {
					// everything went well!
					JastAddAdAPI debugger = new JastAddAdAPI(program);
					debugger.setFilterDir(inDirectory + "/");
					debugger.run();
					debugger.printToXML(inDirectory, OUT_EXTENSION);
					checkOutput(debugger.getFilteredTree(), readXML(expectedFile), inDirectory);
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
			//fail(e.getMessage());
		}
	}

	protected void checkOutput(GenericTreeNode root, Tree expectedRoot, String inDirectory) {

        //System.out.println("==================" + nodeName(root);
		assertEquals("Output is not the same", nodeName(root), expectedRoot.getName());
		for(GenericTreeNode child : root.getChildren()){
			boolean s = expectedRoot.containsChild(nodeName(child));
			assertEquals("Test Failed: " + inDirectory + ". Extra child: " + nodeName(child) + " in parent: " + root.toString(),
					s,
					true);
		}
		assertEquals("Test Failed: " + inDirectory + ". Missing child in parent: " + root.toString(),
				expectedRoot.isEveryChildChecked(), true);
		for(GenericTreeNode child : root.getChildren())
			checkOutput(child, expectedRoot.getChild(nodeName(child)), inDirectory);
	}

    private String nodeName(GenericTreeNode node){
        String name = node.toString();
        if(!node.isNode())
            name = JastAddAdAPI.CLUSTER_STRING;
        return name;
    }

	private Tree readXML(String expected){
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
		public Tree getChild(String childName){
			for(Tree child : children){
				if(!child.isDone() && child.getName().equals(childName)) {
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

	@SuppressWarnings("javadoc")
	@Parameters(name = "{0}")
	public static Iterable<Object[]> getTests() {
		return getTestParameters(TEST_DIR);
	}
}
