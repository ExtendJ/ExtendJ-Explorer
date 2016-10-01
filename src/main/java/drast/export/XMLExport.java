/*
 * Copyright (c) 2016, The JastAdd Team
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *
 *   2. Redistributions in binary form must reproduce the above
 *      copyright notice, this list of conditions and the following
 *      disclaimer in the documentation and/or other materials provided
 *      with the distribution.
 *
 *   3. The name of the author may not be used to endorse or promote
 *      products derived from this software without specific prior
 *      written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package drast.export;

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
 * <p>
 * This class uses the resulting filtered tree from the DrASTAPI and prints it to a xml file. The xml only contains
 * nodes, no attributes, tokens or anything else.
 * <p>
 * usage:
 * There are a number of ways to create an xml. The easiest is:
 * DrASTAPI debugger = new DrASTAPI(program);
 * DrASTXML xmlPrinter = new DrASTXML(debugger);
 * xmlPrinter.run();
 * This will run the API and create a xml file in the root directory with a default name.
 * <p>
 * You can also use one of the following methods to decide where and what name and extension of the file.
 * NOTE: That run on the API must be called first!
 * DrASTAPI debugger = new DrASTAPI(program);
 * debugger.run();
 * DrASTXML xmlPrinter = new DrASTXML(debugger);
 * xmlPrinter.printXml(toDirectory, fileName, ext);    // OR
 * xmlPrinter.printXml(toDirectory, ext);              // OR
 * xmlPrinter.printXml(toDirectory);
 */
public abstract class XMLExport {

  /**
   * Prints the generated Filtered AST as XML to a file in toDirecty with the file extension ext.
   *
   * @return true if successful.
   */
  public static boolean export(DrAST drAst, String filePath) {
    try {
      if (drAst.getTraverser().getFilteredTree() == null) {
        System.out.println("No tree to print, root node is null");
        return false;
      }
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

      // Root elements.
      Document doc = docBuilder.newDocument();

      traverseTreeXML(drAst.getTraverser().getFilteredTree(), doc);

      // Write the content into xml file.
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(filePath);

      transformer.transform(source, result);
      String absoluteFilePath = new File(filePath).getAbsolutePath();
      System.out.println("File saved: " + absoluteFilePath);
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
   */
  private static void traverseTreeXML(GenericTreeNode root, Document doc) {
    Element element;
    if (!root.isNonCluster()) {
      element = doc.createElement(DrAST.CLUSTER_STRING);
    } else {
      element = doc.createElement(root.toString());
    }
    doc.appendChild(element);
    for (GenericTreeNode child : root.getChildren()) {
      traverseTreeXML(child, element, doc);
    }
  }

  /**
   * Used to generate the XML code.
   */
  private static void traverseTreeXML(GenericTreeNode parent, Element parentElement, Document doc) {
    Element element;

    if (!parent.isNonCluster()) {
      element = doc.createElement(DrAST.CLUSTER_STRING);
    } else {
      element = doc.createElement(parent.toString());
    }
    parentElement.appendChild(element);
    for (GenericTreeNode child : parent.getChildren()) {
      traverseTreeXML(child, element, doc);
    }
  }

}
