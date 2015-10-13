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
 * @aspect ClassPath
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/ClassPath.jrag:109
 */
public class ClassPath extends java.lang.Object {


    /**
     * Tracks all currently available packages in the program classpath.
     */
    private Set<String> packages =
      Collections.newSetFromMap(new HashMap<String,Boolean>());



    private boolean pathsInitialized = false;


    private ArrayList<PathPart> classPath = new ArrayList<PathPart>();


    private ArrayList<PathPart> sourcePath = new ArrayList<PathPart>();



    private final Program program;



    public ClassPath(Program program) {
      this.program = program;
    }



    /**
     * Used to make the classpath empty, in case you want more control
     * over the classpath initialization. Usually you would use
     * addClassPath to manually setup the classpath after this.
     */
    public synchronized void initEmptyPaths() {
      pathsInitialized = true;
    }



    /**
     * Set up the classpaths (standard + boot classpath).
     */
    private synchronized void initPaths() {
      if (pathsInitialized) {
        return;
      }
      pathsInitialized = true;

      ArrayList<String> classPaths = new ArrayList<String>();
      ArrayList<String> sourcePaths = new ArrayList<String>();

      String[] bootclasspaths;
      if (program.options().hasValueForOption("-bootclasspath")) {
        bootclasspaths = program.options().getValueForOption("-bootclasspath").split(File.pathSeparator);
      } else {
        bootclasspaths = System.getProperty("sun.boot.class.path").split(File.pathSeparator);
      }
      for (int i = 0; i < bootclasspaths.length; i++) {
        classPaths.add(bootclasspaths[i]);
      }

      String[] extdirs;
      if (program.options().hasValueForOption("-extdirs")) {
        extdirs = program.options().getValueForOption("-extdirs").split(File.pathSeparator);
      } else {
        extdirs = System.getProperty("java.ext.dirs").split(File.pathSeparator);
      }
      for (int i = 0; i < extdirs.length; i++) {
        classPaths.add(extdirs[i]);
      }

      String[] userClasses = null;
      if (program.options().hasValueForOption("-classpath")) {
        userClasses = program.options().getValueForOption("-classpath").split(File.pathSeparator);
      } else if (program.options().hasValueForOption("-cp")) {
        userClasses = program.options().getValueForOption("-cp").split(File.pathSeparator);
      } else {
        userClasses = new String[] { "." };
      }
      if (!program.options().hasValueForOption("-sourcepath")) {
        for (int i = 0; i < userClasses.length; i++) {
          classPaths.add(userClasses[i]);
          sourcePaths.add(userClasses[i]);
        }
      } else {
        for (int i = 0; i < userClasses.length; i++) {
          classPaths.add(userClasses[i]);
        }
        userClasses = program.options().getValueForOption("-sourcepath").split(File.pathSeparator);
        for (int i = 0; i < userClasses.length; i++) {
          sourcePaths.add(userClasses[i]);
        }
      }

      for (String path: classPaths) {
        PathPart part = PathPart.createClassPath(path);
        if (part != null) {
          addClassPath(part);
        } else if (program.options().verbose()) {
          System.out.println("Warning: Could not use " + path + " as class path");
        }
      }
      for (String path: sourcePaths) {
        PathPart part = PathPart.createSourcePath(path);
        if (part != null) {
          addSourcePath(part);
        } else if(program.options().verbose()) {
          System.out.println("Warning: Could not use " + path + " as source path");
        }
      }
    }



    /**
     * Get the input stream for a compilation unit specified using a canonical
     * name. This is used by the bytecode reader to load nested types.
     * @param name The canonical name of the compilation unit.
     */
    public synchronized InputStream getInputStream(String name) {
      try {
        for (Iterator iter = classPath.iterator(); iter.hasNext(); ) {
          PathPart part = (PathPart) iter.next();
          ClassSource source = part.findSource(name);
          if (source != ClassSource.NONE) {
            return source.openInputStream();
          }
        }
      } catch(IOException e) {
      }
      throw new Error("Could not find nested type " + name);
    }



    /**
     * Load a compilation unit from disk, selecting a class file
     * if one exists that is not older than a corresponding source
     * file, otherwise the source file is selected.
     *
     * This method is called by the LibCompilationUnit NTA.  We rely on the
     * result of this method being cached because it will return a newly parsed
     * compilation unit each time it is called.
     *
     * @param name Qualified name of class to load.
     * @return the loaded compilation unit, or {@code null} if no compilation
     * unit was found
     */
    public synchronized CompilationUnit getCompilationUnit(String name) {
      try {
        initPaths();
        ClassSource sourcePart = ClassSource.NONE;
        ClassSource classPart = ClassSource.NONE;
        for (PathPart part: sourcePath) {
          sourcePart = part.findSource(name);
          if (sourcePart != ClassSource.NONE) {
            break;
          }
        }
        for (PathPart part: classPath) {
          classPart = part.findSource(name);
          if (classPart != ClassSource.NONE) {
            break;
          }
        }

        if (sourcePart != ClassSource.NONE && (classPart == ClassSource.NONE ||
              classPart.getAge() < sourcePart.getAge())) {
          CompilationUnit unit = sourcePart.parseCompilationUnit(program);
          int index = name.lastIndexOf('.');
          if (index == -1) {
            return unit;
          }
          String pkgName = name.substring(0, index);
          if (pkgName.equals(unit.getPackageDecl())) {
            return unit;
          }
        }
        if (classPart != ClassSource.NONE) {
          CompilationUnit unit = classPart.parseCompilationUnit(program);
          int index = name.lastIndexOf('.');
          if (index == -1) {
            return unit;
          }
          String pkgName = name.substring(0, index);
          if (pkgName.equals(unit.getPackageDecl())) {
            return unit;
          }
        }
        return null;
      } catch(IOException e) {
      }
      return null;
    }



    /**
     * Add a package name to available package set.
     * @param name The qualified package name.
     */
    public synchronized void addPackage(String name) {
      int end = name.length();
      while (end > 0 && packages.add(name.substring(0, end))) {
        end = name.lastIndexOf('.', end-1);
      }
    }



    /**
     * Add a path part to the library class path.
     */
    public synchronized void addClassPath(PathPart pathPart) {
      classPath.add(pathPart);
    }



    /**
     * Add a path part to the user class path.
     */
    public synchronized void addSourcePath(PathPart pathPart) {
      sourcePath.add(pathPart);
    }



    /**
     * Quick pass, slow fail. Cache existing package names in a concurrent set.
     * @return <code>true</code> if there is a package with the given name on
     * the classpath
     */
    public synchronized boolean isPackage(String name) {
      initPaths();
      if (packages.contains(name)) {
        return true;
      }
      for (PathPart part: classPath) {
        if (part.hasPackage(name)) {
          addPackage(name);
          return true;
        }
      }
      for (PathPart part: sourcePath) {
        if (part.hasPackage(name)) {
          addPackage(name);
          return true;
        }
      }
      return false;
    }



    /**
     * @return a copy of the source path parts
     */
    public synchronized Collection<PathPart> getSourcePath() {
      return new ArrayList<PathPart>(sourcePath);
    }



    /**
     * @return a copy of the class path parts
     */
    public synchronized Collection<PathPart> getClassPath() {
      return new ArrayList<PathPart>(classPath);
    }


}
