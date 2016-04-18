package drast.model;

import drast.model.terminalvalues.Attribute;
import drast.model.terminalvalues.TerminalValue;
import drast.model.terminalvalues.Token;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * This class holds all information about a node, all attribute values and tokens
 * It can invoke methods with the compute methods.
 * Created by gda10jli on 10/14/15.
 */
public interface NodeData {

    Collection<TerminalValue> getAttributes();

    Collection<TerminalValue> getNTAs();

    Collection<TerminalValue> getTokens();

    /**
     * Computes the method in the NodeInfo, with the given parameters, and adds it to the cached list of the Attribute.
     * If the params == null and the method is not parametrized it will compute the method will 0 arguments, otherwise it will return null and add a error to the api.
     * @param terminalValue
     * @param par
     * @return true if the invocation was successful.
     */
    Object compute(TerminalValue terminalValue, Object[] par, ASTBrain api);

    /**
     * Computes all methods of the NodeContents node, this will clear the old values except the invoked ones.
     * This is used for onDemand execution attributes values.
     * @return
     */
    void compute(ASTBrain api);

    Method getMethod(String methodName);

    /**
     * Compute the attribute/token method with some given name.
     * If forceComputation is true it will compute the non-parametrized NTA:s
     * @param method
     * @return
     */
    Object computeMethod(String method);


    /**
     * Creates a Attribute and invokes the method with the supplied parameters, if any.
     * Will also add the specific information about the Attribute, which is derived form the annotations.
     * If forceComputation is true it will compute the non-parametrized NTA:s
     * @param m
     * @param params
     * @return
     */
    Attribute computeAttribute(ASTBrain api, Method m, Object[] params);

    void addCachedValues(Method m, Attribute attribute);

    void setCachedNTAs(ASTBrain api);


}
