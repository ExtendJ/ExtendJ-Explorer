package test.inputfiletests;

import CalcASM.src.gen.lang.ast.LangParser;
import CalcASM.src.gen.lang.ast.LangScanner;
import CalcASM.src.gen.lang.ast.Program;
import beaver.Parser;
import DrAST.api.DrASTAPI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;


@RunWith(Parameterized.class)
public class InputFileCalcASM extends AbstractInputFiletest {
	/**
	 * Directory where test files live
	 */
	private static final String TEST_DIR = "tests/inputFileTests/CalcASM/";

	/**
	 * Construct a new JastAdd test
	 * @param dir filename of test input file
	 */
	public InputFileCalcASM(String dir) {
		super(TEST_DIR, dir, DrASTAPI.FILE_NAME);
	}


	@Test
	public void runInputTests() {
		System.out.println("start test in: " + inDirectory);
		try {
			String filename = inDirectory + "/input.calc";
			LangScanner scanner = new LangScanner(new FileReader(filename));
			LangParser parser = new LangParser();
			Program program = (Program) parser.parse(scanner);
			if (!program.errors().isEmpty()) {
				System.err.println();
				System.err.println("Errors: ");
				for (CalcASM.src.gen.lang.ast.ErrorMessage e : program.errors()) {
					System.err.println("- " + e);
				}
			} else {
				compareWithExpectedFile(program);
			}
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
		return AbstractInputFiletest.getTestParameters(TEST_DIR);
	}
}
