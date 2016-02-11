package test.inputfiletests;

import CalcASM.src.java.gen.LangParser;
import CalcASM.src.java.gen.LangScanner;
import CalcASM.src.java.gen.Program;
import beaver.Parser;
import drast.model.DrAST;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;


@RunWith(Parameterized.class)
public class ExpectedFileInputTestCalcASM extends AbstractFileInputTest {
	/**
	 * Directory where test files live
	 */
	private static final String TEST_DIR = "tests/inputFileTests/CalcASM/";

	/**
	 * Construct a new JastAdd test
	 * @param dir filename of test input file
	 */
	public ExpectedFileInputTestCalcASM(String dir) {
		super(TEST_DIR, dir, DrAST.FILE_NAME);
	}

	@Test
	public void runInputTests() {
		System.out.println("start test in: " + inDirectory);
		try {
			String filename = inDirectory + "/input.calc";
			LangScanner scanner = new LangScanner(new FileReader(filename));
			LangParser parser = new LangParser();
			Program program = (Program) parser.parse(scanner);
			compareWithExpectedFile(program);
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		} catch (Parser.Exception e) {
			e.printStackTrace();
		}
	}


	@SuppressWarnings("javadoc")
	@Parameters(name = "{0}")
	public static Iterable<Object[]> getTests() {
		return AbstractFileInputTest.getTestParameters(TEST_DIR);
	}
}
