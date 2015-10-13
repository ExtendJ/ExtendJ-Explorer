package jastaddad;

import java.lang.annotation.Annotation;

public class ASTAnnotation{
  public static boolean isListChild(Annotation annotation){
    String annotationName = annotation.annotationType().getCanonicalName();
    return annotationName.endsWith("ASTNodeAnnotation.ListChild");
  }

  public static boolean isOptChild(Annotation annotation){
    String annotationName = annotation.annotationType().getCanonicalName();
    return annotationName.endsWith("ASTNodeAnnotation.OptChild");
  }

  public static boolean isChild(Annotation annotation){
    String annotationName = annotation.annotationType().getCanonicalName();
    return annotationName.endsWith("ASTNodeAnnotation.Child");
  }
}
