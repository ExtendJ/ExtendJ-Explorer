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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BytecodeCONSTANT.jrag:133
 */
 class CONSTANT_Info extends java.lang.Object {

    protected BytecodeParser p;


    public CONSTANT_Info(BytecodeParser parser) {
      p = parser;

    }


    public Expr expr() {
      throw new Error("CONSTANT_info.expr() should not be computed for " + getClass().getName());
    }


    public Expr exprAsBoolean() {
      return expr();
    }


}
