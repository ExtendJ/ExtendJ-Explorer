/* Copyright (c) 2016, The JastAdd Team
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
package drast.model;

import drast.Log;
import org.jastadd.drast.filterlang.AnyNode;
import org.jastadd.drast.filterlang.AnyNodeSequence;
import org.jastadd.drast.filterlang.FilterParser;
import org.jastadd.drast.filterlang.FilterRules;
import org.jastadd.drast.filterlang.FilterScanner;
import org.jastadd.drast.filterlang.PathPart;
import org.jastadd.drast.filterlang.Rule;
import org.jastadd.drast.filterlang.TypedNode;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.List;

/**
 * This class contains the configurations specified by the configuration language.
 * It can read and parse the file with the configuration
 */
public class TreeFilter {
  private final FilterRules filterRules;

  public TreeFilter() {
    filterRules = new FilterRules();
  }

  private TreeFilter(FilterRules rules) {
    filterRules = rules;
  }

  /**
   * Scans and parses the file with the given filename.
   * If no such file can found it will create a standard Configuration file, and then parse that one.
   * Sets the configuration to the new one if no errors are thrown.
   */
  public static TreeFilter readFilter(String filter) {
    try (StringReader reader = new StringReader(filter)) {
      FilterScanner scanner = new FilterScanner(reader);
      FilterParser parser = new FilterParser();
      FilterRules rules = (FilterRules) parser.parse(scanner);
      return new TreeFilter(rules);
    } catch (IOException e) {
      Log.error("IOException when reading filter file: %s", e.getMessage());
      e.printStackTrace(System.err);
      return null;
    } catch (FilterParser.SyntaxError e) {
      Log.error(e.getMessage());
      return null;
    } catch (Exception e) {
      Log.error("Exception when parsing filter: %s", e.toString());
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Updates the configurations, will overwrite the old configs with the new if no errors has been thrown.'
   * Will also overwrite the old configuration file.
   */
  public static boolean writeFilter(File filterFile, String text)
      throws IOException {
    boolean errored = null == readFilter(text);
    if (!errored) {
      try (PrintWriter writer = new PrintWriter(filterFile, "UTF-8")) {
        writer.print(text);
        Log.info("Saved filter to file: %s", filterFile.getAbsolutePath());
      }
    }
    return !errored;
  }

  public Rule findRuleForNode(List<Node> path, Node node) {
    for (Rule rule : filterRules.getRuleList()) {
      if (ruleMatches(rule, path, node)) {
        return rule;
      }
    }
    return null;
  }

  protected static boolean ruleMatches(Rule rule, List<Node> path, Node node) {
    if (matchTail(rule, path, node, 0, 0)) {
      if (rule.hasCondition()) {
        return rule.getCondition().evalCondition(node, Log::log);
      }
      return true;
    } else {
      return false;
    }
  }

  private static boolean matchTail(Rule rule, List<Node> path, Node node, int partIndex,
      int nodeIndex) {
    for (; partIndex < rule.getNumPathPart(); ++partIndex) {
      PathPart part = rule.getPathPart(partIndex);
      if (part instanceof AnyNodeSequence) {
        for (int i = nodeIndex; i <= path.size() + 1; ++i) {
          if (matchTail(rule, path, node, partIndex + 1, i)) {
            return true;
          }
        }
        return false;
      }
      Node current = getNode(path, node, nodeIndex);
      if (current == null) {
        return false;
      }
      if (part.isTypedNode()) {
        boolean found = false;
        // Check if the typename matches any type in the node type hierarchy.
        for (String name : current.getClassHierarchy()) {
          if (name.equals(part.typeName())) {
            found = true;
            break;
          }
        }
        if (!found) {
          // The typename does not match this node.
          return false;
        }
        TypedNode typedNode = (TypedNode) part;
        if (typedNode.hasCondition()
            && !typedNode.getCondition().evalCondition(current, Log::log)) {
          return false;
        }
        nodeIndex += 1;
        continue;
      }
      if (part instanceof AnyNode) {
        AnyNode anyNode = (AnyNode) part;
        if (anyNode.hasCondition()
            && !anyNode.getCondition().evalCondition(current, Log::log)) {
          return false;
        }
        nodeIndex += 1;
      }
    }
    return nodeIndex > path.size();
  }

  private static Node getNode(List<Node> path, Node node, int index) {
    if (index < path.size()) {
      return path.get(index);
    }
    if (index == path.size()) {
      return node;
    }
    return null;
  }
}
