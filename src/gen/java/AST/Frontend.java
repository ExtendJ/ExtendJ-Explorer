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
 * Abstract Java compiler frontend.
 * @ast class
 * @aspect FrontendMain
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/FrontendMain.jrag:45
 */
abstract public class Frontend extends java.lang.Object {

    protected Program program;



    /**
     * Compile success
     */
    public static final int EXIT_SUCCESS = 0;



    /**
     * Lexical/semantic error.
     */
    public static final int EXIT_ERROR = 1;



    /**
     * Command-line configuration error.
     */
    public static final int EXIT_CONFIG_ERROR = 2;



    /**
     * The compiler terminated by system error.
     */
    public static final int EXIT_SYSTEM_ERROR = 3;



    /**
     * The compiler terminated abnormally.
     */
    public static final int EXIT_UNHANDLED_ERROR = 4;



    private final String name;


    private final String version;



    /**
     * Initialize the program object.
     */
    protected Frontend() {
      this("Unknown", "0");
    }



    /**
     * Initialize the program object and set compiler name and version.
     * @param name compiler name
     * @param version compiler version
     */
    protected Frontend(String name, String version) {
      this.name = name;
      this.version = version;
      program = new Program();
      program.state().reset();
    }



    /**
     * @deprecated Use run instead.
     */
    public boolean process(String[] args, BytecodeReader reader,
        JavaParser parser) {
      int result = run(args, reader, parser);
      return result == 0;
    }



    /**
     * Process all compilation units listed in the command-line arguments, and
     * all compilation units referenced from those.
     * 
     * @return 0 on success, 1 on error, 2 on configuration error, 3 on system
     * error, 4 on unhandled error
     */
    public int run(String[] args, BytecodeReader reader, JavaParser parser) {

      // reset statistics
      program.javaParseTime = 0;
      program.bytecodeParseTime = 0;
      program.codeGenTime = 0;
      program.errorCheckTime = 0;
      program.numJavaFiles = 0;
      program.numClassFiles = 0;

      program.initBytecodeReader(reader);
      program.initJavaParser(parser);

      initOptions();
      int argResult = processArgs(args);
      if (argResult != 0) {
        return argResult;
      }

      Collection<String> files = program.options().files();

      if (program.options().hasOption("-version")) {
        printVersion();
        return EXIT_SUCCESS;
      }

      if (program.options().hasOption("-help") || files.isEmpty()) {
        printUsage();
        return EXIT_SUCCESS;
      }

      Collection<CompilationUnit> work = new LinkedList<CompilationUnit>();

      try {
        for (String file: files) {
          program.addSourceFile(file);
        }

        int compileResult = EXIT_SUCCESS;

        // process source compilation units
        Iterator<CompilationUnit> iter = program.compilationUnitIterator();
        while (iter.hasNext()) {
          CompilationUnit unit = iter.next();
          work.add(unit);
          int result = processCompilationUnit(unit);
          switch (result) {
            case EXIT_SUCCESS:
              break;
            case EXIT_UNHANDLED_ERROR:
              return result;
            default:
              compileResult = result;
          }
        }

        // process library compilation units
        RobustMap<String, CompilationUnit> valueMap =
          (RobustMap<String, CompilationUnit>)
            program.getLibCompilationUnitValueMap();
        if (valueMap != null) {
          iter = valueMap.robustValueIterator();
          while (iter.hasNext()) {
            CompilationUnit unit = iter.next();
            work.add(unit);
            int result = processCompilationUnit(unit);
            switch (result) {
              case EXIT_SUCCESS:
                break;
              case EXIT_UNHANDLED_ERROR:
                return result;
              default:
                compileResult = result;
            }
          }
        }

        if (compileResult != EXIT_SUCCESS) {
          return compileResult;
        }

        for (CompilationUnit unit: work) {
          if (unit != null && unit.fromSource()) {
            long start = System.nanoTime();
            processNoErrors(unit);
            program.codeGenTime += System.nanoTime() - start;
          }
        }

      } catch (Throwable t) {
        System.err.println("Errors:");
        System.err.println("Fatal exception:");
        t.printStackTrace(System.err);
        return EXIT_UNHANDLED_ERROR;
      } finally {
        if (program.options().hasOption("-profile")) {
          System.out.println("javaParseTime: " + program.javaParseTime);
          System.out.println("numJavaFiles: " + program.numJavaFiles);
          System.out.println("bytecodeParseTime: " + program.javaParseTime);
          System.out.println("numClassFiles: " + program.numClassFiles);
          System.out.println("errorCheckTime: " + program.errorCheckTime);
          System.out.println("codeGenTime: " + program.codeGenTime);
        }
      }
      return EXIT_SUCCESS;
    }



    private Collection<Problem> EMPTY_PROBLEM_LIST = Collections.emptyList();



    /**
     * Processes from-source compilation units by error-checking them.
     * This method only report semantic errors and warnings.
     *
     * @return zero on success, non-zero on error
     */
    protected int processCompilationUnit(CompilationUnit unit) {
      long start;

      if (unit != null && unit.fromSource()) {
        try {
          Collection errors = unit.parseErrors();
          Collection warnings = EMPTY_PROBLEM_LIST;
          // compute static semantic errors when there are no parse errors
          // or the recover from parse errors option is specified
          if (errors.isEmpty() || program.options().hasOption("-recover")) {
            start = System.nanoTime();
            unit.collectErrors();
            errors = unit.errors();
            warnings = unit.warnings();
            program.errorCheckTime += System.nanoTime() - start;
          }
          if (!errors.isEmpty()) {
            processErrors(errors, unit);
            return EXIT_ERROR;
          } else {
            if (!warnings.isEmpty() &&
                !program.options().hasOption("-nowarn")) {
              processWarnings(warnings, unit);
            }
          }
        } catch (Throwable t) {
          System.err.println("Errors:");
          System.err.println("Fatal exception while processing " +
              unit.pathName() + ":");
          t.printStackTrace(System.err);
          return EXIT_UNHANDLED_ERROR;
        }
      }
      return EXIT_SUCCESS;
    }



    /**
     * Initialize the command-line options.
     * Override this method to add your own command-line options.
     */
    protected void initOptions() {
      Options options = program.options();
      options.initOptions();
      options.addKeyOption("-version");
      options.addKeyOption("-print");
      options.addKeyOption("-g");
      options.addKeyOption("-g:none");
      options.addKeyOption("-g:lines,vars,source");
      options.addKeyOption("-nowarn");
      options.addKeyOption("-verbose");
      options.addKeyOption("-deprecation");
      options.addKeyValueOption("-classpath");
      options.addKeyValueOption("-cp");
      options.addKeyValueOption("-sourcepath");
      options.addKeyValueOption("-bootclasspath");
      options.addKeyValueOption("-extdirs");
      options.addKeyValueOption("-d");
      options.addKeyValueOption("-encoding");
      options.addKeyValueOption("-source");
      options.addKeyValueOption("-target");
      options.addKeyOption("-help");
      options.addKeyOption("-O");
      options.addKeyOption("-J-Xmx128M");
      options.addKeyOption("-recover");
      options.addKeyOption("-XprettyPrint");
      options.addKeyOption("-XdumpTree");

      // non-javac options
      options.addKeyOption("-profile"); // output profiling information
      options.addKeyOption("-debug"); // extra debug checks and information
    }



    /**
     * Configure the compiler with command-line arguments.
     * @return 0 if there were no configuration errors
     */
    protected int processArgs(String[] args) {
      program.options().addOptions(args);
      boolean error = false;
      Collection<String> files = program.options().files();
      for (String file: files) {
        if (!new File(file).isFile()) {
          System.err.println(
              "Error: neither a valid option nor a filename: " + file);
          error = true;
        }
      }
      return error ? EXIT_CONFIG_ERROR : EXIT_SUCCESS;
    }



    /**
     * Print the errors.
     *
     * @param errors collection of compile problems
     * @param unit affected compilation unit
     */
    protected void processErrors(Collection errors, CompilationUnit unit) {
      System.err.println("Errors:");
      for (Iterator iter2 = errors.iterator(); iter2.hasNext(); ) {
        System.err.println(iter2.next());
      }
    }



    /**
     * Print the warnings.
     *
     * @param warnings collection of compile problems
     * @param unit affected compilation unit
     */
    protected void processWarnings(Collection warnings, CompilationUnit unit) {
      System.err.println("Warnings:");
      for(Iterator iter2 = warnings.iterator(); iter2.hasNext(); ) {
        System.err.println(iter2.next());
      }
    }



    /**
     * Called for each from-source compilation unit with no errors.
     */
    protected void processNoErrors(CompilationUnit unit) {
    }



    /**
     * Echo the command-line usage help to sysout.
     */
    protected void printUsage() {
      System.out.println(name() + " " + version());
      System.out.println(
          "\n" +
          "Usage: java " + name() + " <options> <source files>\n" +
          "  -verbose                  Output messages about what the compiler is doing\n" +
          "  -classpath <path>         Specify where to find user class files\n" +
          "  -sourcepath <path>        Specify where to find input source files\n" +
          "  -bootclasspath <path>     Override location of bootstrap class files\n" +
          "  -extdirs <dirs>           Override location of installed extensions\n" +
          "  -d <directory>            Specify where to place generated class files\n" +
          "  -nowarn                   Disable warning messages\n" +
          "  -help                     Print a synopsis of standard options\n" +
          "  -version                  Print version information\n"
          );
    }



    /**
     * Echo the version to sysout.
     */
    protected void printVersion() {
      System.out.println(name() + " " + version());
    }



    /**
     * @return the name of the compiler
     */
    protected String name() {
      return name;
    }



    /**
     * @return the version of the compiler
     */
    protected String version() {
      return version;
    }


}
