package tests;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.Scanner;

import lang.ast.LangParser;
import lang.ast.LangScanner;

/**
 * Utility methods for running tests
 */
abstract class AbstractTestSuite {
	private static String SYS_LINE_SEP = System.getProperty("line.separator");

	/**
	 * Directory where test files for this test suite live.
	 */
	protected final File testDirectory;

	public AbstractTestSuite(String testDirectory) {
		this.testDirectory = new File(testDirectory);
	}

	protected void testValidSyntax(String filename) {
		try {
			parse(new File(testDirectory, filename));
		} catch (Exception e) {
			fail("Unexpected error while parsing '" + filename + "': "
				+ e.getMessage());
		}
	}

	protected void testSyntaxError(String filename) {
		try {
			// Beaver reports syntax error on the standard error.
			// We want to discard these messages since we expect syntax error.
			System.setErr(new PrintStream(new ByteArrayOutputStream()));

			parse(new File(testDirectory, filename));

			fail("syntax is valid, expected syntax error");
		} catch (beaver.Parser.Exception | lang.ast.LangParser.SyntaxError e) {
			// ok (syntax error)!
		} catch (Exception e) {
			fail("IO error while trying to parse '" + filename + "': "
				+ e.getMessage());
		}
	}

	/**
	 * Parses the given file
	 * @param file
	 * @return parser result, if everything went OK
	 * @throws IOException
	 * @throws Exception
	 */
	protected static Object parse(File file) throws IOException, Exception {
		LangScanner scanner = new LangScanner(new FileReader(file));
		LangParser parser = new LangParser();
		return parser.parse(scanner);
	}

	/**
	 * Check that the string matches the contents of the given file.
	 * Also writes the actual output to file.
	 * @param actual actual output
	 * @param out file where output should be written
	 * @param expected file containing expected output
	 */
	protected static void compareOutput(String actual, File out, File expected) {
		try {
			Files.write(out.toPath(), actual.getBytes());
			assertEquals("Output differs",
				readFileToString(expected),
				normalizeText(actual));
		} catch (IOException e) {
			fail("IOException occurred while comparing output: " + e.getMessage());
		}
	}

	/**
	 * Reads an entire file to a string object.
	 * <p>If the file does not exist an empty string is returned.
	 * <p>The system dependent line separator char sequence is replaced by
	 * the newline character.
	 *
	 * @param file
	 * @return normalized text from file
	 * @throws FileNotFoundException
	 */
	private static String readFileToString(File file) throws FileNotFoundException {
		if (!file.isFile()) {
			return "";
		}

		Scanner scanner = new Scanner(file);
		scanner.useDelimiter("\\Z");
		String text = normalizeText(scanner.hasNext() ? scanner.next() : "");
		scanner.close();
		return text;
	}

	/**
	 * Trim whitespace and normalize newline characters to be only \n
	 * @param text
	 * @return normalized text
	 */
	private static String normalizeText(String text) {
		return text.replace(SYS_LINE_SEP, "\n").trim();
	}

}
