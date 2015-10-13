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
 * @aspect BytecodeDescriptor
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java5/frontend/BytecodeDescriptor.jrag:96
 */
 class MethodDescriptor extends java.lang.Object {

    private BytecodeParser p;


    private String parameterDescriptors;


    private String typeDescriptor;



    public MethodDescriptor(BytecodeParser parser, String name) {
      p = parser;
      int descriptor_index = p.u2();
      String descriptor = ((CONSTANT_Utf8_Info) p.constantPool[descriptor_index]).string();
      if(BytecodeParser.VERBOSE)
        p.println("  Method: " + name + ", " + descriptor);
      //String[] strings = descriptor.substring(1).split("\\)");
      //parameterDescriptors = strings[0];
      //typeDescriptor = strings[1];
      int pos = descriptor.indexOf(')');
      parameterDescriptors = descriptor.substring(1, pos);
      typeDescriptor = descriptor.substring(pos+1, descriptor.length());
    }



    public List parameterList() {
      TypeDescriptor d = new TypeDescriptor(p, parameterDescriptors);
      return d.parameterList();
    }


    public List parameterListSkipFirst() {
      TypeDescriptor d = new TypeDescriptor(p, parameterDescriptors);
      return d.parameterListSkipFirst();
    }



    public Access type() {
      TypeDescriptor d = new TypeDescriptor(p, typeDescriptor);
      return d.type();
    }


}
