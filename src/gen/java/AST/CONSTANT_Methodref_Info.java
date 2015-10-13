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
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/BytecodeCONSTANT.jrag:197
 */
 class CONSTANT_Methodref_Info extends CONSTANT_Info {

    public int class_index;


    public int name_and_type_index;



    public CONSTANT_Methodref_Info(BytecodeParser parser) {
      super(parser);
      class_index = p.u2();
      name_and_type_index = p.u2();
    }



    public String toString() {
      return "MethodRefInfo: " + class_index + " " + name_and_type_index;
    }


}
