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
 * A Jar file path listed in the classpath. Can contain many .class files.
 * This PathPart lazily initializes its package set and entry set.
 * @ast class
 * @aspect PathPart
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/PathPart.jadd:474
 */
public class JarFilePath extends PathPart {

    private Collection<String> packageIndex = null;


    private final ZipFile jar;


    private final String jarPath;



    public JarFilePath(String jarPath) throws IOException {
      super(false);
      this.jar = new ZipFile(jarPath);
      this.jarPath = jarPath;
    }



    public JarFilePath(File jarFile) throws IOException {
      super(false);
      this.jar = new ZipFile(jarFile);
      this.jarPath = jarFile.getPath();
    }



    @Override
    public String getPath() {
      return jarPath;
    }



    private static void scanJar(ZipFile jar, Collection<String> packages,
        String fileSuffix) {
      // Add all zip entries to a set so that we can quickly check if the Jar
      // contains a given class
      for (Enumeration entries = jar.entries(); entries.hasMoreElements(); ) {
        ZipEntry entry = (ZipEntry) entries.nextElement();
        String path = entry.getName();
        if (path.endsWith(fileSuffix)) {
          addPackages(packages, path);
        }
      }
    }



    private static void addPackages(Collection<String> packages, String path) {
      String name = path.replace('/', '.');
      int index = path.length();
      do {
        index = path.lastIndexOf('/', index-1);
      } while (index >= 0 && packages.add(name.substring(0, index)));
    }



    /**
     * Caches the package index from the Jar file so that subsequent calls to
     * this method are quicker.
     */
    @Override
    public boolean hasPackage(String name) {
      synchronized (this) {
        if (packageIndex == null) {
          packageIndex = new HashSet<String>();
          scanJar(jar, packageIndex, fileSuffix);
        }
      }
      return packageIndex.contains(name);
    }



    @Override
    public ClassSource findSource(String name) {
      // ZipFiles always use '/' as separator
      String jarName = name.replace('.', '/') + fileSuffix;
      ZipEntry entry = jar.getEntry(jarName);
      if (entry != null) {
        return new JarClassSource(this, jar, entry, jarPath);
      } else {
        return ClassSource.NONE;
      }
    }



    @Override
    public String toString() {
      return "jar:" + jarPath;
    }


}
