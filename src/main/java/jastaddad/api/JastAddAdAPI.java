package jastaddad.api;

import jastaddad.api.filteredtree.GenericTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import configAST.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.System;

/**
 * This is the main class for the JastAddAd system. This class will create an ASTAPI object that will generate the
 * filtered AST.
 *
 * JastAddAd can be started by calling run() method on this class or JastAddAdUI.
 *
 */
public class JastAddAdAPI {
	public final static String FILE_NAME = "jastAddAd-result";
    public final static String CLUSTER_STRING = "cluster";
	private ASTAPI api;

    private Object root;
    private String filterDir;

	
	public JastAddAdAPI(Object root){
        this.root = root;
        filterDir = "";
	}

    /**
     * Sets the directory of the projects
     * @param dir
     */
    public void setFilterDir(String dir){filterDir = dir;}

	public void run(){
        api = new ASTAPI(root, filterDir);
	}

    public ASTAPI api(){return api;};

	public GenericTreeNode getFilteredTree(){
		return api.getFilteredTree();
	}

    /**
     * Prints the generated Filtered AST as XML to a file in toDirecty with the file extension ext.
     *
     * @param toDirectory
     * @param ext
     * @return true if successful.
     */
	public boolean printToXML(String toDirectory, String ext){
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();

			traversTreeXML(api.getFilteredTree(), doc);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(toDirectory + "/" + FILE_NAME + ext);

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
			return false;
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
			return false;
		}
		return true;
	}

    /**
     * Used to generate the XML code.
     *
     * @param root
     * @param doc
     */
	private void traversTreeXML(GenericTreeNode root, Document doc){
		Element element = doc.createElement(root.toString());
		doc.appendChild(element);
		for(GenericTreeNode child : root.getChildren())
			traversTreeXML(child, element, doc);
	}

    /**
     * Used to generate the XML code.
     *
     * @param parent
     * @param parentElement
     * @param doc
     */
	private void traversTreeXML(GenericTreeNode parent, Element parentElement, Document doc){
        Element element;
        if(!parent.isNode())
            element = doc.createElement(CLUSTER_STRING);
        else
            element = doc.createElement(parent.toString());
		parentElement.appendChild(element);
		for(GenericTreeNode child : parent.getChildren())
			traversTreeXML(child, element, doc);
	}

    /**
     * main function for starting a JastAddAdAPI session.
     * @param args
     */
	public static void main(String[] args) {
		try{
			String filename = "testInput.cfg";
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
				JastAddAdAPI debugger = new JastAddAdAPI(program);
                debugger.run();
				//program.genCode(System.out);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}