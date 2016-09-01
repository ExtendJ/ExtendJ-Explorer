package drast.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Util class that works as an intermediate for the annotations of the methods
 * Contains most of the hardcoded strings of DrAST
 */
public class ASTAnnotation {

    public static final String AST_SUPER_CLASS = "ASTNode";
    public static final String AST_ORDER_METHOD = "astChildren";

    private static final String AST_ANNO = "ASTNodeAnnotation.";

    public static final String AST_NODE_OPT_CHILD = AST_ANNO + "OptChild";
    public static final String AST_NODE_LIST_CHILD = AST_ANNO + "ListChild";
    public static final String AST_NODE_CHILD = AST_ANNO + "Child";
    public static final String AST_NODE_ATTRIBUTE = AST_ANNO + "Attribute";
    public static final String AST_NODE_TOKEN = AST_ANNO + "Token";
    public static final String AST_NODE_SOURCE = AST_ANNO + "Source";

    public static final String AST_KIND_SYN = "SYN";
    public static final String AST_KIND_INH = "INH";
    public static final String AST_KIND_COLL = "COLL";

    public static String AST_KIND_SYN_FULL_NAME = "Synthesised";
    public static String AST_KIND_INH_FULL_NAME = "Inherited";
    public static String AST_KIND_COLL_FULL_NAME = "Collection";

    public static final String AST_METHOD_NAME = "name";
    public static final String AST_METHOD_KIND = "kind";
    public static final String AST_METHOD_DECLARED_AT = "declaredAt";
    public static final String AST_METHOD_ASPECT = "aspect";
    public static final String AST_METHOD_NTA = "isNTA";
    public static final String AST_METHOD_CIRCULAR = "isCircular";

    public static boolean isChild(Annotation a) {
        return isListChild(a) || isOptChild(a) || isSingleChild(a);
    }

    public static boolean isListChild(Annotation annotation) {
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

    public static Object get(Annotation annotation, String method) {
        return compute(annotation, method);
    }

    public static String getKind(Annotation annotation) {
        Object obj = compute(annotation, AST_METHOD_KIND);
        if (obj == null)
            return null;
        String kind = String.valueOf(compute(annotation, AST_METHOD_KIND));
        if (kind.equals("null"))
            return null;
        switch (kind) {
            case AST_KIND_SYN:
                return AST_KIND_SYN_FULL_NAME;
            case AST_KIND_INH:
                return AST_KIND_INH_FULL_NAME;
            case AST_KIND_COLL:
                return AST_KIND_COLL_FULL_NAME;
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

    public static String getMethodCachedField(Method method) {
        String name = method.getName();
        if (method.getParameterCount() == 0)
            return name + "_value";
        for (Class par : method.getParameterTypes())
            name += "_" + par.getSimpleName();
        name += "_values";
        return name;
    }

    public static String getMethodComputedField(Method method) {
        String name = method.getName();
        for (Class par : method.getParameterTypes())
            name += "_" + par.getSimpleName();
        return name + "_computed";
    }

    /**
     * Returns the name given by the parent node, by the annotations.
     *
     * @param annotation
     * @return
     */
    public static Object compute(Annotation annotation, String methodName) {
        try {
            if (annotation.getClass().getMethod(methodName) != null)
                return annotation.getClass().getMethod(methodName).invoke(annotation);
        } catch (Throwable e) {
            e.getStackTrace();
        }
        return null;
    }
}
