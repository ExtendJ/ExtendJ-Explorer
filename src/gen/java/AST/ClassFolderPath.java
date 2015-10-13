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
 * Concrete subclass of FolderPath. Represents a classfile folder.
 * @ast class
 * @aspect PathPart
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PathPart.jadd:460
 */
public class ClassFolderPath extends FolderPath {

    /**
     * Construct a new classfile folder path.
     * @param path
     */
    public ClassFolderPath(String path) {
      super(path, false);
    }


}
