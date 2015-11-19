import configAST.ConfigParser;
import configAST.ConfigScanner;
import configAST.DebuggerConfig;
import configAST.ErrorMessage;
import jastaddad.api.JastAddAdAPI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


@RunWith(Parameterized.class)
public class InputFileTreeTest extends AbstractParameterizedTest {
	/**
	 * Directory where test files live
	 */
	private static final String TEST_DIR = "tests/inputFileTests/";

	/**
	 * Construct a new JastAdd test
	 * @param dir filename of test input file
	 */
	public InputFileTreeTest(String dir) {
		super(TEST_DIR, dir, JastAddAdAPI.FILE_NAME);
	}


	/**
	 * Run the JastAdd test
	 */
	@Test
	public void runTest() {
		try {
			System.out.println("start test in: " + inDirectory);
			try{
				String filename = inDirectory + "/testInput.cfg";
				ConfigScanner scanner = new ConfigScanner(new FileReader(filename));
				ConfigParser parser = new ConfigParser();
				DebuggerConfig program = (DebuggerConfig) parser.parse(scanner);
				if (!program.errors().isEmpty()) {
					System.err.println();
					System.err.println("Errors: ");
					for (ErrorMessage e: program.errors()) {
						System.err.println("- " + e);
					}
				} else {
					// everything went well!
					JastAddAdAPI debugger = new JastAddAdAPI(program);
					debugger.setFilterDir(inDirectory + "/");
					debugger.run();
					debugger.printToXML(inDirectory, OUT_EXTENSION);
					new OutoutXMLcomparer().checkOutput(debugger.getFilteredTree(), expectedFile, inDirectory);
				}
			} catch (FileNotFoundException e) {
				System.out.println("File not found!");
				//System.exit(1);
			} catch (IOException e) {
				e.printStackTrace(System.err);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			//fail(e.getMessage());
		}
	}

	@SuppressWarnings("javadoc")
	@Parameters(name = "{0}")
	public static Iterable<Object[]> getTests() {
		return getTestParameters(TEST_DIR);
	}
}
