package jastaddad;

import java.lang.annotation.Annotation;

public class ASTAnnotation{

  private static final String ASTNODE_OPT_CHILD = "ASTNodeAnnotation.OptChild";
  private static final String ASTNODE_LIST_CHILD = "ASTNodeAnnotation.ListChild";
  private static final String ASTNODE_CHILD = "ASTNodeAnnotation.Child";
  private static final String ASTNODE_ATTRIBUTE = "ASTNodeAnnotation.Attribute";

  public static boolean isListChild(Annotation annotation){
    String annotationName = annotation.annotationType().getCanonicalName();
    return annotationName.endsWith(ASTNODE_LIST_CHILD);
  }

  public static boolean isOptChild(Annotation annotation){
    String annotationName = annotation.annotationType().getCanonicalName();
    return annotationName.endsWith(ASTNODE_OPT_CHILD);
  }

  public static boolean isList(Annotation a){ return isListChild(a) || isOptChild(a); }

  public static boolean isChild(Annotation annotation){
    String annotationName = annotation.annotationType().getCanonicalName();
    return annotationName.endsWith(ASTNODE_CHILD);
  }

  public static boolean isAttribute(Annotation annotation){
    String annotationName = annotation.annotationType().getCanonicalName();
    return annotationName.endsWith(ASTNODE_ATTRIBUTE);
  }
}
