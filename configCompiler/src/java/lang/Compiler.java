package lang;

import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.System;

import beaver.Parser.Exception;
import lang.ast.DebuggerConfig;
import lang.ast.ErrorMessage;
import lang.ast.LangParser;
import lang.ast.LangScanner;
import jastaddad.JastAddAd;
import javafx.application.Application;

import javax.swing.*;

/**
 * Computes the maximum statement nesting depth for a Calc program.
 */
public class Compiler {
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
			DebuggerConfig program = (DebuggerConfig) parser.parse(scanner);
			if (!program.errors().isEmpty()) {
				System.err.println();
				System.err.println("Errors: ");
				for (ErrorMessage e: program.errors()) {
					System.err.println("- " + e);
				}
			} else {
				JastAddAd debugger = new JastAddAd(program);
				//program.genCode(System.out);
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

	private static void printUsage() {
		System.err.println("Usage: Compiler FILE");
		System.err.println("  where FILE is the file to be compiled");
	}
}
