package AST;

import java.util.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.io.File;
import java.util.Set;
import java.util.ArrayList;
import beaver.*;
import org.jastadd.util.*;
import java.util.zip.*;
import java.io.*;
import java.io.FileNotFoundException;
/**
 * @ast class
 * @aspect BytecodeCONSTANT
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BytecodeCONSTANT.jrag:147
 */
 class CONSTANT_Integer_Info extends CONSTANT_Info {

    public int value;



    public CONSTANT_Integer_Info(BytecodeParser parser) {
      super(parser);
      value = p.readInt();
    }



    public String toString() {
      return "IntegerInfo: " + Integer.toString(value);
    }



    public Expr expr() {
      return Literal.buildIntegerLiteral(value);
    }


    public Expr exprAsBoolean() {
      return Literal.buildBooleanLiteral(value == 0);
    }


}
