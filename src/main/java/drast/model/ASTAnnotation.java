package drast.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Util class that works as an intermediate for the annotations of the methods
 * Contains most of the hardcoded strings of DrAST
 */
public class ASTAnnotation {

  public static final String AST_SUPER_CLASS = "ASTNode";
  public static final String AST_ORDER_METHOD = "astChildren";

  private static final String AST_ANNO = "ASTNodeAnnotation.";

  private static final String AST_NODE_OPT_CHILD = AST_ANNO + "OptChild";
  private static final String AST_NODE_LIST_CHILD = AST_ANNO + "ListChild";
  private static final String AST_NODE_CHILD = AST_ANNO + "Child";
  private static final String AST_NODE_ATTRIBUTE = AST_ANNO + "Attribute";
  private static final String AST_NODE_TOKEN = AST_ANNO + "Token";
  private static final String AST_NODE_SOURCE = AST_ANNO + "Source";

  private static final String AST_KIND_SYN = "SYN";
  private static final String AST_KIND_INH = "INH";
  private static final String AST_KIND_COLL = "COLL";

  public static final String AST_METHOD_NAME = "name";
  private static final String AST_METHOD_KIND = "kind";
  public static final String AST_METHOD_DECLARED_AT = "declaredAt";
  public static final String AST_METHOD_ASPECT = "aspect";
  public static final String AST_METHOD_NTA = "isNTA";
  public static final String AST_METHOD_CIRCULAR = "isCircular";

  public static boolean isChild(Annotation a) {
    return isListChild(a) || isOptChild(a) || isSingleChild(a);
  }

  private static boolean isListChild(Annotation annotation) {
    return annotation.annotationType().getCanonicalName().endsWith(AST_NODE_LIST_CHILD);
  }

  public static boolean isOptChild(Annotation annotation) {
    return annotation.annotationType().getCanonicalName().endsWith(AST_NODE_OPT_CHILD);
  }

  public static boolean isSingleChild(Annotation annotation) {
    return annotation.annotationType().getCanonicalName().endsWith(AST_NODE_CHILD);
  }

  public static boolean isAttribute(Annotation annotation) {
    return annotation.annotationType().getCanonicalName().endsWith(AST_NODE_ATTRIBUTE);
  }

  public static boolean isToken(Annotation annotation) {
    return annotation.annotationType().getCanonicalName().endsWith(AST_NODE_TOKEN);
  }

  public static boolean isSource(Annotation annotation) {
    return annotation.annotationType().getCanonicalName().endsWith(AST_NODE_SOURCE);
  }

  public static String getKind(Annotation annotation) {
    Object obj = compute(annotation, AST_METHOD_KIND);
    if (obj == null) {
      return null;
    }
    String kind = String.valueOf(compute(annotation, AST_METHOD_KIND));
    if (kind.equals("null")) {
      return null;
    }
    switch (kind) {
      case AST_KIND_SYN:
        return "Synthesised";
      case AST_KIND_INH:
        return "Inherited";
      case AST_KIND_COLL:
        return "Collection";
      default:
        return kind;
    }
  }

  public static String getString(Annotation annotation, String method) {
    Object obj = compute(annotation, method);
    return obj != null ? obj.toString() : "null";
  }

  public static boolean is(Annotation annotation, String method) {
    Object obj = compute(annotation, method);
    return obj != null ? (Boolean) obj : false;
  }

  public static boolean isList(Class clazz) {
    return clazz.getSimpleName().equals("List");
  }

  public static String getCacheValueFieldName(Method method) {
    String name = method.getName();
    if (method.getParameterCount() == 0) {
      return name + "_value";
    }
    for (Class par : method.getParameterTypes()) {
      name += "_" + par.getSimpleName();
    }
    name += "_values";
    return name;
  }

  public static String getComputedFlagFieldName(Method method) {
    String name = method.getName();
    for (Class par : method.getParameterTypes()) {
      name += "_" + par.getSimpleName();
    }
    return name + "_computed";
  }

  /**
   * Returns the name given by the parent node, by the annotations.
   */
  private static Object compute(Annotation annotation, String methodName) {
    try {
      if (annotation.getClass().getMethod(methodName) != null) {
        return annotation.getClass().getMethod(methodName).invoke(annotation);
      }
    } catch (Throwable e) {
      e.getStackTrace();
    }
    return null;
  }
}
