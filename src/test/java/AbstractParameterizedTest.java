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
	protected static final String IN_EXTENSION = ".cfg";
	/**
	 * Test output is written to a file with this extension
	 */
	protected static final String OUT_EXTENSION = ".out";
	/**
	 * File extension for expected test output
	 */
	protected static final String EXPECTED_EXTENSION = ".expected";

	protected final String inDirectory;
	protected final File inputFile;
	protected final File outFile;
	protected final String expectedFile;

	/**
	 * @param testDirectory
	 * @param fileName
	 */
	public AbstractParameterizedTest(String testDirectory, String inDirectory, String fileName) {
		super(testDirectory);
		this.inDirectory = inDirectory;
		inputFile = getTestinputFile(fileName);
		outFile = getTestOutputFile(fileName);
		expectedFile = getTestExpectedOutputFile(fileName);
	}

	protected File getTestinputFile(String fileName) {
		return new File(inDirectory, fileName+IN_EXTENSION);
	}

	protected File getTestOutputFile(String fileName) {
		return new File(inDirectory, fileName+OUT_EXTENSION);
	}

	protected String getTestExpectedOutputFile(String fileName) {
		return "/" + fileName + EXPECTED_EXTENSION;
	}

	@SuppressWarnings("javadoc")
	public static Iterable<Object[]> getTestParameters(String testDirectory) {
		Collection<Object[]> tests = new LinkedList<Object[]>();
		File testDir = new File(testDirectory);
		if (!testDir.isDirectory()) {
			throw new Error("Could not find '" + testDirectory + "' directory!");
		}
		for (File f: testDir.listFiles()) {
			if(f.isDirectory())
				tests.add(new Object[] {testDirectory + f.getName()});
		}
		return tests;
	}
}
