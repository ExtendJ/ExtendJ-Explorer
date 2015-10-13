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
 * @aspect PathPart
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PathPart.jadd:199
 */
public class FileBytecodeClassSource extends BytecodeClassSource {

    private final String filePath;



    public FileBytecodeClassSource(PathPart sourcePath, String path) {
      super(sourcePath);
      this.filePath = path;
    }



    @Override
    public long getAge() {
      // last modification time computed only when needed
      File file = new File(filePath);
      return file.lastModified();
    }



    @Override
    public InputStream openInputStream() throws IOException {
      File file = new File(filePath);
      return new FileInputStream(file);
    }



    @Override
    public String pathName() {
      return filePath;
    }


}
