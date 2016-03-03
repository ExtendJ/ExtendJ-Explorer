package test.inputfiletests;

import beaver.Parser;
import configAST.ConfigParser;
import configAST.ConfigScanner;
import configAST.DebuggerConfig;
import configAST.ErrorMessage;
import drast.model.DrAST;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by gda10jth on 12/15/15.
 */
@RunWith(Parameterized.class)
public class ExpectedFileInputTestFilter extends AbstractFileInputTest {
    /**
     * Directory where test files live
     */
    private static final String TEST_DIR = "tests/inputFileTests/Filter/";

    /**
     * Construct a new JastAdd test
     * @param dir filename of test input file
     */
    public ExpectedFileInputTestFilter(String dir) {
        super(TEST_DIR, dir, DrAST.FILE_NAME);
    }

    /**
     * Run the JastAdd test
     */
    @Test
    public void runInputTests() {
            System.out.println("start Filter input file test in: " + inDirectory);
            try{
                String filename = inDirectory + "/input.fcl";
                ConfigScanner scanner = new ConfigScanner(new FileReader(filename));
                ConfigParser parser = new ConfigParser();
                DebuggerConfig program = (DebuggerConfig) parser.parse(scanner);
                if (!program.errors().isEmpty()) {
                    System.err.println();
                    System.err.println("Errors: ");
                    for (ErrorMessage e: program.errors()) {
                        System.err.println("- " + e);
                    }
                    assertEquals("Error while parsing input file. ", program.errors().isEmpty(), true);
                } else {
                    compareWithExpectedFile(program);
                }
            } catch (FileNotFoundException e) {
                fail("FileNotFoundException: " + e.getMessage());
                //System.exit(1);
            } catch (IOException e) {
                fail("IOException: " + e.getMessage());
                e.printStackTrace(System.err);
            } catch (Parser.Exception e) {
                fail("IOException: " + e.getMessage());
                e.printStackTrace();
            }
    }

    @SuppressWarnings("javadoc")
    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object[]> getTests() {
        return AbstractFileInputTest.getTestParameters(TEST_DIR);
    }
}
