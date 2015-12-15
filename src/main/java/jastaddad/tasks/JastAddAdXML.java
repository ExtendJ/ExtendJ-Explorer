package jastaddad.tasks;

import jastaddad.api.JastAddAdAPI;
import jastaddad.api.filteredtree.GenericTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by gda10jth on 11/20/15.
 */
public class JastAddAdXML extends DecoratorTask {
    public JastAddAdXML(JastAddAdAPI api){
        super(api);
    }

    public JastAddAdXML(Object root){
        super(root);
    }

    protected void runThisTask(){
        printToXML(JastAddAdAPI.FILE_NAME + ".xml");
    }

    public boolean printXml(String toDirectory, String fileName, String ext){
        loadAPI();
        String filePath = fileName + ext;
        if(toDirectory.length() > 0){
            filePath = toDirectory + "/" + fileName + ext;
        }

        return printToXML(filePath);
    }

    public boolean printXml(String toDirectory, String ext){
        loadAPI();
        String filePath = JastAddAdAPI.FILE_NAME + ext;
        if(toDirectory.length() > 0){
            filePath = toDirectory + "/" + JastAddAdAPI.FILE_NAME + ext;
        }

        return printToXML(filePath);
    }

    public boolean printXml(String toDirectory){
        loadAPI();
        String filePath = JastAddAdAPI.FILE_NAME + ".xml";
        if(toDirectory.length() > 0){
            filePath = toDirectory + "/" + JastAddAdAPI.FILE_NAME + ".xml";
        }

        return printToXML(filePath);
    }

    public boolean printXml(){
        loadAPI();
        return printToXML(JastAddAdAPI.FILE_NAME + ".xml");
    }

    /**
     * Prints the generated Filtered AST as XML to a file in toDirecty with the file extension ext.
     *
     * @param filePath
     * @return true if successful.
     */
    private boolean printToXML(String filePath){
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
            StreamResult result = new StreamResult(filePath);

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
        Element element;
        if(!root.isNode())
            element = doc.createElement(JastAddAdAPI.CLUSTER_STRING);
        else
            element = doc.createElement(root.toString());
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
            element = doc.createElement(JastAddAdAPI.CLUSTER_STRING);
        else
            element = doc.createElement(parent.toString());
        parentElement.appendChild(element);
        for(GenericTreeNode child : parent.getChildren())
            traversTreeXML(child, element, doc);
    }
}
