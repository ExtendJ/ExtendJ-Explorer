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
 * Represents a bytecode class source (.class file).
 * @ast class
 * @aspect PathPart
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PathPart.jadd:159
 */
public abstract class BytecodeClassSource extends ClassSource {

    public BytecodeClassSource(PathPart sourcePath) {
      super(sourcePath);
    }



    @Override
    public CompilationUnit parseCompilationUnit(Program program)
      throws IOException {
      InputStream is = openInputStream();
      try {
        if (program.options().verbose()) {
          System.out.print("Loading " + sourceName());
        }

        long start = System.nanoTime();
        CompilationUnit u = program.bytecodeReader.read(is, sourceName(), program);
        long elapsed = System.nanoTime() - start;
        program.bytecodeParseTime += elapsed;
        program.numClassFiles += 1;

        u.setFromSource(false);
        u.setClassSource(this);

        if (program.options().verbose()) {
          System.out.println(" in " + (elapsed / 1000000) + " ms");
        }
        return u;
      } catch (Exception e) {
        throw new Error("Error loading " + sourceName(), e);
      } finally {
        if (is != null) {
          try {
            is.close();
          } catch (IOException e) {
          }
        }
      }
    }


}
