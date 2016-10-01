package drast.model;

import beaver.Parser;
import org.jastadd.drast.filterlang.Attribute;
import org.jastadd.drast.filterlang.Expr;
import org.jastadd.drast.filterlang.FilterParser;
import org.jastadd.drast.filterlang.FilterScanner;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.stream.Collectors;

import static com.google.common.truth.Truth.assertThat;

public class FilteredTreeBuilderTest {
  private static Expr expr(String ruleString) {
    FilterParser parser = new FilterParser();
    try {
      return (Expr) parser.parse(new FilterScanner(new StringReader(ruleString)),
          FilterParser.AltGoals.expression);
    } catch (IOException | Parser.Exception e) {
      throw new Error(e);
    }
  }

  @Test public void testEmptyTree() {
    FilteredTreeBuilder builder = new FilteredTreeBuilder(null, new TreeFilter());
    assertThat(builder.hasRoot()).isFalse();
    assertThat(builder.getFilteredTree()).isNull();
  }

  @Test public void testBuildFilteredTree1() {
    Expr expr = expr("a == 3");
    FilteredTreeBuilder builder = new FilteredTreeBuilder(expr, new TreeFilter());
    assertThat(builder.getFilteredTree().getNode().getAstObject()).isSameAs(expr);
  }

  @Test public void testArgList1() {
    // Test that attribute arguments are included in the filtered tree.
    Expr expr = expr("attr(x(), \"foo\", 10)");
    Attribute attribute = (Attribute) expr;

    FilteredTreeBuilder builder = new FilteredTreeBuilder(expr, new TreeFilter());
    Node root = builder.getFilteredTree().getNode();
    assertThat(root.getAstObject()).isSameAs(expr);
    assertThat(root.getSimpleClassName()).isEqualTo("Attribute");

    Node argList = root.getChildren().iterator().next();
    assertThat(root.getChildren()).hasSize(1);
    assertThat(argList.getChildren()).hasSize(3);

    assertThat(argList.getChildren().stream().map(child -> child.getAstObject())
        .collect(Collectors.toList())).containsExactlyElementsIn(attribute.getArgList());
  }
}
