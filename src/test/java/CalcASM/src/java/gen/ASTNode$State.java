package CalcASM.src.java.gen;

import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.TreeSet;
/**
 * @ast class
 * @declaredat ASTNode$State:4
 */
public class ASTNode$State extends Object {
  
  /**
   * @apilevel internal
   */
  public boolean LAST_CYCLE = false;

  

  /**
   * @apilevel internal
   */
  public boolean INTERMEDIATE_VALUE = false;

  

  /**
   * @apilevel internal
   */
  public boolean IN_CIRCLE = false;

  

  /**
   * @apilevel internal
   */
  public int CIRCLE_INDEX = 1;

  

  /**
   * @apilevel internal
   */
  public boolean CHANGE = false;

  

  /**
   * @apilevel internal
   */
  public boolean RESET_CYCLE = false;

  

  /**
   * @apilevel internal
   */
  static public class CircularValue {
    Object value;
    int visited = -1;
  }

  public void reset() {
    IN_CIRCLE = false;
    CIRCLE_INDEX = 1;
    CHANGE = false;
    LAST_CYCLE = false;
  }


}
