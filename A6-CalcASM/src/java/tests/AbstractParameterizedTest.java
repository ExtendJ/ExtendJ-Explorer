package tests;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

/**
 * A parameterized test suite. Adds helper methods for
 * parameterized testing.
 */
abstract public class AbstractParameterizedTest extends AbstractTestSuite {

	/**
	 * File extension for test input files. EDIT ME
	 */
	protected static final String IN_EXTENSION = ".calc";
	/**
	 * Test output is written to a file with this extension
	 */
	private static final String OUT_EXTENSION = ".out";
	/**
	 * File extension for expected test output
	 */
	private static final String EXPECTED_EXTENSION = ".expected";

	protected final File inFile;
	protected final File outFile;
	protected final File expectedFile;

	/**
	 * @param testDirectory
	 * @param testFile
	 */
	public AbstractParameterizedTest(String testDirectory, String testFile) {
		super(testDirectory);
		inFile = getTestFile(testFile);
		outFile = getFileReplaceExtension(testFile, OUT_EXTENSION);
		expectedFile = getFileReplaceExtension(testFile, EXPECTED_EXTENSION);
	}

	protected File getTestFile(String filename) {
		return new File(testDirectory, filename);
	}

	protected File getFileReplaceExtension(File file, String extension) {
		return getFileReplaceExtension(file.getName(), extension);
	}

	protected File getFileReplaceExtension(String filename, String extension) {
		String simpleName = filename.substring(0, filename.lastIndexOf('.'));
		return getTestFile(simpleName+extension);
	}

	@SuppressWarnings("javadoc")
	public static Iterable<Object[]> getTestParameters(String testDirectory) {
		Collection<Object[]> tests = new LinkedList<Object[]>();
		File testDir = new File(testDirectory);
		if (!testDir.isDirectory()) {
			throw new Error("Could not find '" + testDirectory + "' directory!");
		}
		for (File f: testDir.listFiles()) {
			if (f.getName().endsWith(IN_EXTENSION)) {
				tests.add(new Object[] {f.getName()});
			}
		}
		return tests;
	}
}
