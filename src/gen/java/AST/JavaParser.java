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
 * @ast interface
 * @aspect ClassPath
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ClassPath.jrag:22
 */
public interface JavaParser {

     
    CompilationUnit parse(InputStream is, String fileName) throws IOException, beaver.Parser.Exception;
}
