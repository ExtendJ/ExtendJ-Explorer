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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BytecodeCONSTANT.jrag:181
 */
 class CONSTANT_Long_Info extends CONSTANT_Info {

    public long value;



    public CONSTANT_Long_Info(BytecodeParser parser) {
      super(parser);
      value = p.readLong();
    }



    public String toString() {
      return "LongInfo: " + Long.toString(value);
    }



    public Expr expr() {
      return Literal.buildLongLiteral(value);
    }


}
