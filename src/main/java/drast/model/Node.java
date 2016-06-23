package drast.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * This class represents the Node in the AST, it holds all its terminal attributes and references to its children.
 * This class should only be created once, due to that it will traverse the tree from the given Object.
 */
public interface Node {

    boolean isChildClassOf(Class parent);

    boolean isOpt();

    boolean isList();

    boolean isNullNode();

    boolean isNTANode();

    NodeData getNodeData();

    String getSimpleClassName();

    String getNameFromParent();

    Class getASTClass();

    HashMap<Object, Node> getNTAChildren();

    ArrayList<Method> getNTAMethods(Class clazz);

    Object getASTObject();

    Node getParent();

    Collection<Node> getChildren();

    Node getNTATree(Object root, Node parent, ASTBrain astBrain);

    Node getNTATree(String methodName, ASTBrain astBra);

    boolean containsNTAMethod(String methodName);

    void putNTA(String methodName, Node node);

}
