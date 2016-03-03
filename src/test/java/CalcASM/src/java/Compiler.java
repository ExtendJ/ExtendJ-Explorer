package CalcASM.src.java;

import CalcASM.src.java.gen.LangParser;
import CalcASM.src.java.gen.LangScanner;
import CalcASM.src.java.gen.Program;
import beaver.Parser.Exception;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Computes the maximum statement nesting depth for a Calc program.
 */
public class Compiler {

	public static Object DrAST_root_node;

	/**
	 * Entry point
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if (args.length != 1) {
				System.err.println(
						"You must specify a source file on the command line!");
				printUsage();
				System.exit(1);
				return;
			}

			String filename = args[0];

			LangScanner scanner = new LangScanner(new FileReader(filename));
			LangParser parser = new LangParser();
			Program program = (Program) parser.parse(scanner);
			DrAST_root_node = program;
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printUsage() {
		System.err.println("Usage: Compiler FILE");
		System.err.println("  where FILE is the file to be compiled");
	}
}

