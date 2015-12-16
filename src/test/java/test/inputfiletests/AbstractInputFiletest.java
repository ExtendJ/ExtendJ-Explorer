package test.inputfiletests;

import jastaddad.api.ASTAPI;
import jastaddad.api.AlertMessage;
import jastaddad.api.JastAddAdAPI;
import jastaddad.tasks.JastAddAdXML;
import test.OutoutXMLcomparer;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

/**
 * A parameterized test suite. Adds helper methods for
 * parameterized testing.
 */
abstract public class AbstractInputFiletest {

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

	protected final File testDirectory;
	protected final String inDirectory;
	protected final File inputFile;
	protected final File outFile;
	protected final String expectedFile;

	/**
	 * @param testDirectory
	 * @param fileName
	 */
	public AbstractInputFiletest(String testDirectory, String inDirectory, String fileName) {
		this.testDirectory = new File(testDirectory);
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

	protected void compareWithExpectedFile(Object program){
		// everything went well!
		JastAddAdAPI debugger = new JastAddAdAPI(program);
		debugger.setFilterDir(inDirectory + "/");
		debugger.run();
		JastAddAdXML xmlPrinter = new JastAddAdXML(debugger);
		xmlPrinter.run();
		xmlPrinter.printXml(inDirectory, AbstractInputFiletest.OUT_EXTENSION);
		int numberOfErrors = debugger.api().getErrors(AlertMessage.FILTER_ERROR).size();
		assertEquals("Errors parsing filter language", numberOfErrors, 0);
		new OutoutXMLcomparer().checkOutput(debugger.getFilteredTree(), expectedFile, inDirectory);
	}
}
