package tests;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import AST.*;
import org.jastadd.jastaddj.*;
import org.jastadd.util.*;
import metricpce.MetricPCE;


@RunWith(Parameterized.class)
public class TestMetricPCE extends AbstractParameterizedTest {
	/**
	 * Directory where test files live
	 */
	private static final String TEST_DIR = "tests/";

	/**
	 * Construct a new JastAdd test
	 * @param testFile filename of test input file
	 */
	public TestMetricPCE(String dir) { 
		super(TEST_DIR, dir, MetricPCE.FILE_NAME);
	}

	/**
	 * Run the JastAdd test
	 */
	@Test
	public void runTest() {
		try {
			System.out.println(inDirectory);
			MetricPCE me = new MetricPCE();
			me.changeConfigFile(inDirectory + "/");
			me.changeIgnoreFile(inDirectory + "/");
			me.run(new String[] {inDirectory});
			compareOutput(readFileToString(inFile), outFile, expectedFile);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@SuppressWarnings("javadoc")
	@Parameters(name = "{0}")
	public static Iterable<Object[]> getTests() {
		return getTestParameters(TEST_DIR);
	}
}
