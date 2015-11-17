package jastaddad;

import jastaddad.filteredtree.GenericTreeNode;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uicomponent.UIComponent;

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

public class JastAddAd{
	public final static String FILE_NAME = "jastAddAd-result";
    public final static String CLUSTER_STRING = "cluster";
	private ASTAPI api;
	private UIComponent ui;

	private boolean runUI;
    private Object root;
    private String filterDir;

	
	public JastAddAd(Object root){
		this(root, true);
	}

	public JastAddAd(Object root, boolean runUI){
        this.root = root;
		this.runUI = runUI;
        filterDir = "";
	}
      
	public UIComponent getUI(){
	    return ui;
	}
	
    public void setFilterDir(String dir){filterDir = dir;}

	public void run(){
        api = new ASTAPI(root, filterDir);
        if(runUI)
            ui = new UIComponent(api);
	}

	public GenericTreeNode getFilteredTree(){
		return api.getFilteredTree();
	}

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

	private void traversTreeXML(GenericTreeNode root, Document doc){
		Element element = doc.createElement(root.toString());
		doc.appendChild(element);
		for(GenericTreeNode child : root.getChildren())
			traversTreeXML(child, element, doc);
	}

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
				JastAddAd debugger = new JastAddAd(program);
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