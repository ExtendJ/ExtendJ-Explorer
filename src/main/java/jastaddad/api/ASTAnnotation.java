package jastaddad.api;

import configAST.Bool;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

/**
 * Util class that works as an intermediate for the annotations of the methods
 */
public class ASTAnnotation{

  private static final String AST_ANNO = "ASTNodeAnnotation.";

  public static final String AST_NODE_OPT_CHILD = AST_ANNO + "OptChild";
  public static final String AST_NODE_LIST_CHILD = AST_ANNO +  "ListChild";
  public static final String AST_NODE_CHILD = AST_ANNO + "Child";
  public static final String AST_NODE_ATTRIBUTE = AST_ANNO + "Attribute";
  public static final String AST_NODE_TOKEN = AST_ANNO + "Token";
  public static final String AST_NODE_SOURCE = AST_ANNO + "Source";

  public static final String AST_KIND = AST_ANNO + "Kind";
  public static final String AST_KIND_SYN = AST_KIND + "SYN";
  public static final String AST_KIND_INH = AST_KIND + "INH";
  public static final String AST_KIND_COLL = AST_KIND + "COLL";

  public static final String AST_METHOD_NAME = "name";
  public static final String AST_METHOD_KIND = "kind";
  public static final String AST_METHOD_DECLARED_AT = "declaredAt";
  public static final String AST_METHOD_ASPECT = "aspect";
  public static final String AST_METHOD_NTA = "isNTA";
  public static final String AST_METHOD_CIRCULAR = "isCircular";

  public static final int ATTRIBUTE = 0;
  public static final int TOKEN = 1;

  //Todo might wanna change so it's only one or two methods, it will lower the readability of the code though

  public static boolean isChild(Annotation a){ return isListChild(a) || isOptChild(a) || isSingleChild(a); }

  public static boolean isListChild(Annotation annotation){
    return annotation.annotationType().getCanonicalName().endsWith(AST_NODE_LIST_CHILD);
  }

  public static boolean isOptChild(Annotation annotation){
    return annotation.annotationType().getCanonicalName().endsWith(AST_NODE_OPT_CHILD);
  }

  public static boolean isSingleChild(Annotation annotation){
    return annotation.annotationType().getCanonicalName().endsWith(AST_NODE_CHILD);
  }

  public static boolean isAttribute(Annotation annotation){
    return annotation.annotationType().getCanonicalName().endsWith(AST_NODE_ATTRIBUTE);
  }

  public static boolean isToken(Annotation annotation){
    return annotation.annotationType().getCanonicalName().endsWith(AST_NODE_TOKEN);
  }

  public static boolean isSource(Annotation annotation){
    return annotation.annotationType().getCanonicalName().endsWith(AST_NODE_SOURCE);
  }

  public static Object get(Annotation annotation, String method)  {
    return compute(annotation, method);
  }

  public static String getString(Annotation annotation, String method)  {
    Object obj = compute(annotation, method);
    return obj != null ? obj.toString() : "null";
  }

  public static boolean is(Annotation annotation, String method) {
    Object obj = compute(annotation, method);
    return obj != null ? (Boolean) obj: false;
  }

  /**
   * Returns the name given by the parent node, by the annotations.
   * @param annotation
   * @return
   * @throws NoSuchMethodException
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   */
  public static Object compute(Annotation annotation, String methodName)  {
    try {
      if (annotation.getClass().getMethod(methodName) != null)
        return annotation.getClass().getMethod(methodName).invoke(annotation, new Object[]{});
    }catch (Throwable e){
      e.getStackTrace();
    }
    return null;
  }
}
