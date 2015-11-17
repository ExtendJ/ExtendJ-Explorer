import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
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

	/**
	 * Check that the string matches the contents of the given file.
	 * Also writes the actual output to file.
	 * @param actual actual output
	 * @param out file where output should be written
	 * @param expected file containing expected output
	 */
	protected static void compareOutput(String actual, String expected) {
			assertEquals("Output differs",
					expected,
					actual);
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
	protected static String readFileToString(File file) throws FileNotFoundException {
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
