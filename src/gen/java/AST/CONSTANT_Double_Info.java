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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BytecodeCONSTANT.jrag:83
 */
 class CONSTANT_Double_Info extends CONSTANT_Info {

    public double value;



    public CONSTANT_Double_Info(BytecodeParser parser) {
      super(parser);
      value = this.p.readDouble();
    }



    public String toString() {
      return "DoubleInfo: " + Double.toString(value);
    }



    public Expr expr() {
      return Literal.buildDoubleLiteral(value);
    }


}
