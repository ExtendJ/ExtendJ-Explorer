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
 * Concrete subclass of FolderPath. Represents a source file folder.
 * @ast class
 * @aspect PathPart
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PathPart.jadd:447
 */
public class SourceFolderPath extends FolderPath {

    /**
     * Construct a new source file folder path.
     * @param path
     */
    public SourceFolderPath(String path) {
      super(path, true);
    }


}
