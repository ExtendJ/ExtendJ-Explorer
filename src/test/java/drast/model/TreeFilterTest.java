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

import drast.model.reflection.Pair;
import drast.model.terminalvalues.TerminalValue;
import org.jastadd.drast.filterlang.AnyNode;
import org.jastadd.drast.filterlang.AnyNodeSequence;
import org.jastadd.drast.filterlang.IncludeRule;
import org.jastadd.drast.filterlang.Opt;
import org.jastadd.drast.filterlang.Rule;
import org.jastadd.drast.filterlang.TypedNode;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Jesper Öqvist
 */
public class TreeFilterTest {
  private static class MockNode implements Node {
    private final String name;

    public MockNode(String name) {
      this.name = name;
    }

    @Override public Pair<Field, Field> getCacheFields(Method method) {
      return null;
    }

    @Override public boolean isChildClassOf(Class parent, FilteredTreeBuilder traverser) {
      return false;
    }

    @Override public boolean isOpt() {
      return false;
    }

    @Override public boolean isList() {
      return false;
    }

    @Override public boolean isNullNode() {
      return false;
    }

    @Override public boolean isNTANode() {
      return false;
    }

    @Override public String getSimpleClassName() {
      return name;
    }

    @Override public String getNameFromParent() {
      return null;
    }

    @Override public Class getAstClass() {
      return null;
    }

    @Override public HashMap<Object, Node> getNtaChildren() {
      return null;
    }

    @Override public Object getAstObject() {
      return null;
    }

    @Override public Node getParent() {
      return null;
    }

    @Override public Collection<Node> getChildren() {
      return null;
    }

    @Override public void computeAttributes() {

    }

    @Override public Object computeMethod(String name, Object... args) {
      return null;
    }

    @Override public Object computeAttribute(TerminalValue terminalValue, Object[] par) {
      return null;
    }

    @Override public Collection<TerminalValue> getAttributes() {
      return null;
    }

    @Override public Collection<TerminalValue> getNTAs() {
      return null;
    }

    @Override public Collection<TerminalValue> getTokens() {
      return null;
    }

    @Override public String[] getClassHierarchy() {
      return new String[] { name };
    }

    @Override public void addNtaChild(Object childObject, Node node) {
    }
  }

  private static Rule rule(String... words) {
    Rule rule = new IncludeRule();
    for (String name : words) {
      switch (name) {
        case "*":
          rule.addPathPart(new AnyNode());
          break;
        case "**":
          rule.addPathPart(new AnyNodeSequence());
          break;
        default:
          rule.addPathPart(new TypedNode(name, new Opt<>()));
          break;
      }
    }
    return rule;
  }

  private static ArrayList<Node> path(String... words) {
    ArrayList<Node> nodes = new ArrayList<>();
    for (String name : words) {
      nodes.add(new MockNode(name));
    }
    return nodes;
  }

  private static boolean testMatch(String ruleSpec, String pathSpec) {
    Rule rule = rule(ruleSpec.split("\\s"));
    ArrayList<Node> path = path(pathSpec.split("\\s"));
    Node node = path.remove(path.size() - 1);
    return TreeFilter.ruleMatches(rule, path, node);
  }

  @Test public void testExact1() {
    assertTrue(testMatch("A", "A"));
  }

  @Test public void testExact2() {
    assertTrue(testMatch("A B", "A B"));
  }

  @Test public void testExact3() {
    assertTrue(testMatch("A B C", "A B C"));
  }

  @Test public void testWildcard1() {
    assertTrue(testMatch("A B *", "A B C"));
  }

  @Test public void testWildcard2() {
    assertTrue(testMatch("A * C", "A B C"));
  }

  @Test public void testWildcard3() {
    assertTrue(testMatch("* B C", "A B C"));
  }

  @Test public void testWildcard4() {
    assertTrue(testMatch("A * *", "A B C"));
  }

  @Test public void testWildcard5() {
    assertTrue(testMatch("* B *", "A B C"));
  }

  @Test public void testWildcard6() {
    assertTrue(testMatch("* * C", "A B C"));
  }

  @Test public void testWildcard7() {
    assertTrue(testMatch("* * *", "A B C"));
  }

  @Test public void testMultiWildcard1() {
    assertTrue(testMatch("A B C **", "A B C"));
  }

  @Test public void testMultiWildcard2() {
    assertTrue(testMatch("A ** B C", "A B C"));
  }

  @Test public void testMultiWildcard3() {
    assertTrue(testMatch("A ** C", "A B C"));
  }

  @Test public void testMultiWildcard4() {
    assertTrue(testMatch("** C", "A B C"));
  }

  @Test public void testMultiWildcard5() {
    assertTrue(testMatch("A **", "A B C"));
  }

  @Test public void testMultiWildcard6() {
    assertTrue(testMatch("A ** ** ** ** C", "A B C"));
  }

  @Test public void testMultiWildcard7() {
    assertTrue(testMatch("** * ** **", "A B C"));
  }

  @Test public void testMultiWildcard8() {
    assertTrue(testMatch("** * * * **", "A B C"));
  }

  @Test public void testMultiWildcard9() {
    assertTrue(testMatch("* * ** *", "A B C"));
  }

  @Test public void testPrefixPath2() {
    assertFalse(testMatch("A B *", "A B"));
  }

  @Test public void testPrefixPath3() {
    assertFalse(testMatch("A * C", "A B"));
  }

  @Test public void testPrefixPath4() {
    assertFalse(testMatch("* B C", "A B"));
  }

  @Test public void testPrefixPath5() {
    assertFalse(testMatch("* * C", "A B"));
  }

  @Test public void testPrefixRule1() {
    assertFalse(testMatch("A B", "A B C"));
  }

  @Test public void testPrefixRule2() {
    assertFalse(testMatch("A *", "A B C"));
  }

  @Test public void testPrefixRule3() {
    assertFalse(testMatch("* B", "A B C"));
  }

  @Test public void testPrefixRule4() {
    assertFalse(testMatch("* *", "A B C"));
  }
}
