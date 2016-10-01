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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public abstract class DotExport {

  public static boolean export(DrAST drAst, String filePath) {
    if (drAst.getTraverser().getFilteredTree() == null) {
      System.out.println("No tree to print, root node is null");
      return false;
    }
    File targetFile = new File(filePath);
    try (PrintStream out = new PrintStream(new FileOutputStream(targetFile))) {
      generateDot(drAst.getTraverser().getFilteredTree(), out);
      System.out.println("File saved: " + targetFile.getAbsolutePath());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * Used to generate the XML code.
   */
  private static void generateDot(GenericTreeNode root, PrintStream out) {
    int nextId = 0;
    out.println("digraph AST {");
    out.println("  edge [lblstyle=\"above,sloped\"];");
    String nodeId = "n" + (nextId++);
    printNode(out, root, nodeId);
    for (GenericTreeNode child : root.getChildren()) {
      nextId = generateDot(child, nodeId, out, nextId);
    }
    out.println("}");
  }

  private static void printNode(PrintStream out, GenericTreeNode node, String nodeId) {
    if (node.isCluster()) {
      out.println(nodeId + ";");
    } else {
      out.format("%s [label=\"%s\"];%n", nodeId, node.toString());

    }
  }

  /**
   * Used to generate the XML code.
   */
  private static int generateDot(GenericTreeNode node, String parentId, PrintStream out, int nextId) {
    String nodeId = "n" + (nextId++);
    printNode(out, node, nodeId);

    if (node.isNonCluster() && !node.isNullNode()) {
      out.format("%s ->  %s [label=\"%s\"];%n", parentId, nodeId, node.getNode().getNameFromParent());
    } else {
      out.format("%s ->  %s;%n", parentId, nodeId);
    }

    for (GenericTreeNode child : node.getChildren()) {
      nextId = generateDot(child, nodeId, out, nextId);
    }
    return nextId;
  }

}
