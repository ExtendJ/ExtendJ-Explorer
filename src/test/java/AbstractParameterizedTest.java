import jastaddad.api.JastAddAdAPI;
import jastaddad.api.filteredtree.GenericTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

/**
 * A parameterized test suite. Adds helper methods for
 * parameterized testing.
 */
abstract public class AbstractParameterizedTest extends AbstractTestSuite {

	/**
	 * File extension for test input files. EDIT ME
	 */
	protected static final String IN_EXTENSION = ".cfg";
	/**
	 * Test output is written to a file with this extension
	 */
	protected static final String OUT_EXTENSION = ".out";
	/**
	 * File extension for expected test output
	 */
	protected static final String EXPECTED_EXTENSION = ".expected";

	protected final String inDirectory;
	protected final File inputFile;
	protected final File outFile;
	protected final String expectedFile;

	/**
	 * @param testDirectory
	 * @param fileName
	 */
	public AbstractParameterizedTest(String testDirectory, String inDirectory, String fileName) {
		super(testDirectory);
		this.inDirectory = inDirectory;
		inputFile = getTestinputFile(fileName);
		outFile = getTestOutputFile(fileName);
		expectedFile = getTestExpectedOutputFile(fileName);
	}

	protected File getTestinputFile(String fileName) {
		return new File(inDirectory, fileName+IN_EXTENSION);
	}

	protected File getTestOutputFile(String fileName) {
		return new File(inDirectory, fileName+OUT_EXTENSION);
	}

	protected String getTestExpectedOutputFile(String fileName) {
		return inDirectory + "/" + fileName + EXPECTED_EXTENSION;
	}

	@SuppressWarnings("javadoc")
	public static Iterable<Object[]> getTestParameters(String testDirectory) {
		Collection<Object[]> tests = new LinkedList<Object[]>();
		File testDir = new File(testDirectory);
		if (!testDir.isDirectory()) {
			throw new Error("Could not find '" + testDirectory + "' directory!");
		}
		for (File f: testDir.listFiles()) {
			if(f.isDirectory())
				tests.add(new Object[] {testDirectory + f.getName()});
		}
		return tests;
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
}
