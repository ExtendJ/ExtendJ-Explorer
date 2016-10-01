package drast.model;

import beaver.Parser;
import drast.model.reflection.ReflectionNode;
import org.jastadd.drast.filterlang.Expr;
import org.jastadd.drast.filterlang.FilterParser;
import org.jastadd.drast.filterlang.FilterScanner;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static com.google.common.truth.Truth.assertThat;

/**
 * Tests for evaluating filter language expressions.
 */
public class FilterEvalTest {
  // Needs to be public for reflection access.
  public static class MockNode {
    private final String name;

    MockNode(String name) {
      this.name = name;
    }

    public String name() {
      return name;
    }

    public int mul2(Integer x) {
      return x * 2;
    }

    public int mul(int x, int y) {
      return x * y;
    }
  }

  private static Expr expr(String ruleString) {
    FilterParser parser = new FilterParser();
    try {
      return (Expr) parser.parse(new FilterScanner(new StringReader(ruleString)),
          FilterParser.AltGoals.expression);
    } catch (IOException | Parser.Exception e) {
      throw new Error(e);
    }
  }

  private static Node node(String name) {
    return ReflectionNode.builder(new MockNode(name)).build();
  }

  @Test public void testAttribute1() {
    // An unparameterized attribute can be called with empty parens.
    assertThat(expr("name()").eval(node("foo"))).isEqualTo("foo");

    // An unparameterized attribute can be called without parens.
    assertThat(expr("name").eval(node("foo"))).isEqualTo("foo");
  }

  @Test public void testAttribute2() {
    assertThat(expr("name == \"foo\"").eval(node("foo"))).isEqualTo(true);
  }

  @Test public void testAttribute3() {
    assertThat(expr("name == \"foo\"").eval(node("not foo"))).isEqualTo(false);
  }

  @Test public void testAttribute4() {
    assertThat(expr("name != \"foo\"").eval(node("not foo"))).isEqualTo(true);
  }

  @Test public void testStringLiteral() {
    Node node = node("abc");
    assertThat(expr("\"foo\"").eval(node)).isEqualTo("foo");
    assertThat(expr("\"bort\"").eval(node)).isEqualTo("bort");
  }

  @Test public void testNumericLiteral() {
    Node node = node("abc");
    assertThat(expr("0").eval(node)).isEqualTo(0);
    assertThat(expr("1").eval(node)).isEqualTo(1);
    assertThat(expr("20").eval(node)).isEqualTo(20);
    assertThat(expr("0.1 < 0.2").eval(node)).isEqualTo(true);
    assertThat(expr("0.1 > 0.01").eval(node)).isEqualTo(true);
  }

  @Test public void testParameterizedAttribute1() {
    // Test a parameterized attribute with one parameter.
    Node node = node("abc");
    assertThat(expr("mul2(3) > 1").eval(node)).isEqualTo(true);
    assertThat(expr("mul2(3) == 6").eval(node)).isEqualTo(true);
    assertThat(expr("mul2(3) != 6").eval(node)).isEqualTo(false);
    assertThat(expr("mul2(3) < 7").eval(node)).isEqualTo(true);
    assertThat(expr("mul2(3) <= 6").eval(node)).isEqualTo(true);
    assertThat(expr("mul2(3) >= 6").eval(node)).isEqualTo(true);
    assertThat(expr("mul2(mul2(2)) == 8").eval(node)).isEqualTo(true);
  }

  @Test public void testParameterizedAttribute2() {
    // Test a parameterized attribute with two parameters.
    // This also tests that boxed Integer types are converted properly to primitive arguments.
    Node node = node("abc");
    assertThat(expr("mul(2, 3)").eval(node)).isEqualTo(6);
    assertThat(expr("mul(mul(2, 2), 3)").eval(node)).isEqualTo(12);
    assertThat(expr("mul(1, 2) > mul(10, 0)").eval(node)).isEqualTo(true);
  }
}
