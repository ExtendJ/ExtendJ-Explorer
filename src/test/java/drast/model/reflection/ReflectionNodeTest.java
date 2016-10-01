package drast.model.reflection;

import drast.model.Node;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class ReflectionNodeTest {
  @Test public void testBuilder01() {
    Node node = ReflectionNode.builder(null).build();
    assertThat(node).isInstanceOf(NullNode.class);
    assertThat(node.getAstClass()).isNull();
  }

  @Test public void testBuilder02() {
    Object obj = new Object();
    Node node = ReflectionNode.builder(obj).build();
    assertThat(node).isInstanceOf(ReflectionNode.class);
    assertThat(node.getAstObject()).isSameAs(obj);
    assertThat(node.getAstClass()).isSameAs(obj.getClass());
  }

  @Test public void testBuilder03() {
    // Test that the class hierarchy supplied to the builder is preserved.
    Object obj = new Object();
    String[] hierarchy = { "foo", "bar", "baz" };
    Node node = ReflectionNode.builder(obj).classHierarchy(hierarchy).build();
    assertThat(node.getClassHierarchy()).isSameAs(hierarchy);
  }
}
