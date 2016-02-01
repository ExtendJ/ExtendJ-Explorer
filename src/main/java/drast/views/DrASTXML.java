package drast.views;

import drast.model.DrAST;
import drast.model.filteredtree.GenericTreeNode;
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
import java.io.File;

/**
 * Created by gda10jth on 11/20/15.
 *
 * This class uses the resulting filtered tree from the DrASTAPI and prints it to a xml file. The xml only contains
 * nodes, no attributes, tokens or anything else.
 *
 * usage:
 * There are a number of ways to create an xml. The easiest is:
 *      DrASTAPI debugger = new DrASTAPI(program);
 *      DrASTXML xmlPrinter = new DrASTXML(debugger);
 *      xmlPrinter.run();
 * This will run the API and create a xml file in the root directory with a default name.
 *
 * You can also use one of the following methods to decide where and what name and extension of the file.
 * NOTE: That run on the API must be called first!
 *      DrASTAPI debugger = new DrASTAPI(program);
 *      debugger.run();
 *      DrASTXML xmlPrinter = new DrASTXML(debugger);
 *      xmlPrinter.printXml(toDirectory, fileName, ext);    // OR
 *      xmlPrinter.printXml(toDirectory, ext);              // OR
 *      xmlPrinter.printXml(toDirectory);
 */
public class DrASTXML extends DecoratorView {
    private String absoluteFilePath;
    public DrASTXML(DrAST api){
        super(api);
        absoluteFilePath = "";
    }

    public DrASTXML(Object root){
        super(root);
        absoluteFilePath = "";
    }

    public String getAbsoluteFilePath(){ return absoluteFilePath;}

    /**
     * Called from DecoratorTask when the run() method is called from the outside
     */
    protected void runThisTask(){
        printToXML(DrAST.FILE_NAME + ".xml");
    }

    @Override
    protected void onNewRoot() {
        run();
    }

    /**
     * Print the AST to a file in toDirectory with the name fileName and extension ext.
     *
     * @param toDirectory
     * @param fileName
     * @param ext
     * @return
     */
    public boolean printXml(String toDirectory, String fileName, String ext){
        loadAPI();
        String filePath = fileName + ext;
        if(toDirectory.length() > 0){
            filePath = toDirectory + "/" + fileName + ext;
        }

        return printToXML(filePath);
    }

    /**
     * Print the AST to a file in toDirectory with the default name and extension ext.
     *
     * @param toDirectory
     * @param ext
     * @return
     */
    public boolean printXml(String toDirectory, String ext){
        loadAPI();
        String filePath = DrAST.FILE_NAME + ext;
        if(toDirectory.length() > 0){
            filePath = toDirectory + "/" + DrAST.FILE_NAME + ext;
        }

        return printToXML(filePath);
    }

    /**
     * Print the AST to a file in toDirectory with the default name and .xml extension
     *
     * @param toDirectory
     * @return
     */
    public boolean printXml(String toDirectory){
        loadAPI();
        String filePath = DrAST.FILE_NAME + ".xml";
        if(toDirectory.length() > 0){
            filePath = toDirectory + "/" + DrAST.FILE_NAME + ".xml";
        }

        return printToXML(filePath);
    }

    /**
     * Print the AST to a file in root with the default name and .xml extension
     * @return
     */
    public boolean printXml(){
        loadAPI();
        return printToXML(DrAST.FILE_NAME + ".xml");
    }

    /**
     * Prints the generated Filtered AST as XML to a file in toDirecty with the file extension ext.
     *
     * @param filePath
     * @return true if successful.
     */
    private boolean printToXML(String filePath){
        try {
            if(api.getFilteredTree() == null){
                System.out.println("No tree to print, root node is null");
                return false;
            }
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
            absoluteFilePath = new File(filePath).getAbsolutePath();
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
            element = doc.createElement(DrAST.CLUSTER_STRING);
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
            element = doc.createElement(DrAST.CLUSTER_STRING);
        else
            element = doc.createElement(parent.toString());
        parentElement.appendChild(element);
        for(GenericTreeNode child : parent.getChildren())
            traversTreeXML(child, element, doc);
    }
}
