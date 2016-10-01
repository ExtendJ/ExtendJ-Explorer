package drast.starter;

import drast.Log;
import org.extendj.ast.CompilationUnit;
import org.extendj.ast.FileClassSource;
import org.extendj.ast.Program;
import org.extendj.ast.SourceFilePath;
import org.extendj.parser.JavaParser;
import org.extendj.scanner.JavaScanner;

import java.io.StringReader;
import java.util.function.Consumer;

/**
 * Provides an AST by running the ExtendJ parser on Java source text.
 */
public class ASTProvider {
  /**
   * Runs the target compiler.
   */
  public static boolean parseAst(String text, Consumer<Object> rootConsumer) {
    long start = System.currentTimeMillis();
    try (StringReader reader = new StringReader(text)){
      JavaScanner scanner = new JavaScanner(reader);
      JavaParser parser = new JavaParser();
      Program program = new Program();
      CompilationUnit cu = (CompilationUnit) parser.parse(scanner);
      cu.setFromSource(true);
      cu.setClassSource(new FileClassSource(new SourceFilePath("inputfile"), "inputfile"));
      program.addCompilationUnit(cu);
      Log.info("Compiler finished after : %d ms", (System.currentTimeMillis() - start));
      rootConsumer.accept(program);
      return true;
    } catch (Throwable e) {
      e.printStackTrace();
      Log.error(e.getMessage());
      return false;
    }
  }
}

