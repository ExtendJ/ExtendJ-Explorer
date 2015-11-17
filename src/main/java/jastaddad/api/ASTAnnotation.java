package jastaddad.api;

import java.lang.annotation.Annotation;

/**
 * Util class that works as an intermediate for the annotations of the methods
 */
public class ASTAnnotation{

  private static final String AST_NODE_OPT_CHILD = "ASTNodeAnnotation.OptChild";
  private static final String AST_NODE_LIST_CHILD = "ASTNodeAnnotation.ListChild";
  private static final String AST_NODE_CHILD = "ASTNodeAnnotation.Child";
  private static final String AST_NODE_ATTRIBUTE = "ASTNodeAnnotation.Attribute";
  private static final String AST_NODE_TOKEN = "ASTNodeAnnotation.Token";

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
}
