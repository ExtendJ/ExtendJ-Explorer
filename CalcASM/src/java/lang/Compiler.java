package lang;

import beaver.Parser.Exception;
import drast.model.DrAST;
import lang.ast.ErrorMessage;
import lang.ast.LangParser;
import lang.ast.LangScanner;
import lang.ast.Program;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.System;

/**
 * Computes the maximum statement nesting depth for a Calc program.
 */
public class Compiler {
	private static final long MEGABYTE = 1024L * 1024L;

	public static long bytesToMegabytes(long bytes) {
		return bytes / MEGABYTE;
	}

	//public static Object DrAST_root_node;

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
			if (!program.errors().isEmpty()) {
				System.err.println();
				System.err.println("Errors: ");
				for (ErrorMessage e: program.errors()) {
					System.err.println("- " + e);
				}
			} else {
				///DrAST_root_node = program;

				printMemoryUse("After compiler");

				DrAST drast = new DrAST(program);
				drast.run();

				printMemoryUse("After DrAST model");
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printMemoryUse(String prepend){
		Runtime runtime = Runtime.getRuntime();
		// Run the garbage collector
		runtime.gc();
		// Calculate the used memory
		long memory = runtime.totalMemory() - runtime.freeMemory();
		System.out.println(prepend + ": " + memory + "bytes (" + bytesToMegabytes(memory) + "megabytes)");
	}

	private static void printUsage() {
		System.err.println("Usage: Compiler FILE");
		System.err.println("  where FILE is the file to be compiled");
	}
}

