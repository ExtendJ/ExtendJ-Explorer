package jastaddad.api;

import configAST.Bool;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

/**
 * Util class that works as an intermediate for the annotations of the methods
 */
public class ASTAnnotation{


  private static final String AST_ANNO = "ASTNodeAnnotation";
  private static final String AST_NODE_OPT_CHILD = AST_ANNO + ".OptChild";
  private static final String AST_NODE_LIST_CHILD = AST_ANNO +  ".ListChild";
  private static final String AST_NODE_CHILD = AST_ANNO + ".Child";
  private static final String AST_NODE_ATTRIBUTE = AST_ANNO + ".Attribute";
  private static final String AST_NODE_TOKEN = AST_ANNO + ".Token";
  private static final String AST_NODE_SOURCE = AST_ANNO + ".Source";

  private static final String AST_KIND = AST_ANNO + ".Kind";
  private static final String AST_KIND_SYN = AST_KIND + ".SYN";
  private static final String AST_KIND_INH = AST_KIND + ".INH";
  private static final String AST_KIND_COLL = AST_KIND + ".COLL";

  public static final String AST_METHOD_NAME = "name";
  public static final String AST_METHOD_KIND = "kind";
  public static final String AST_METHOD_DECLARED_AT = "declaredAt";
  public static final String AST_METHOD_ASPECT = "aspect";
  public static final String AST_METHOD_NTA = "isNTA";
  public static final String AST_METHOD_CIRCULAR = "isCircular";

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

  public static Object getKind(Annotation annotation)  {
    return compute(annotation, AST_METHOD_KIND);
  }

  public static String getDeclaredAt(Annotation annotation)  {
    Object obj = compute(annotation, AST_METHOD_DECLARED_AT);
    return obj != null ? obj.toString() : "null";
  }

  public static String getAspect(Annotation annotation)  {
    Object obj = compute(annotation, AST_METHOD_ASPECT);
    return obj != null ? obj.toString() : "null";
  }

  public static boolean isNTA(Annotation annotation) {
    Object obj = compute(annotation, AST_METHOD_NTA);
    return obj != null ? (Boolean) obj: false;
  }

  public static boolean isCircular(Annotation annotation) {
    Object obj = compute(annotation, AST_METHOD_CIRCULAR);
    return obj != null ? (Boolean) obj : false;
  }

  public static String getName(Annotation a) {
    Object obj = compute(a, AST_METHOD_NAME);
    return obj != null ? obj.toString() : "null";
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
