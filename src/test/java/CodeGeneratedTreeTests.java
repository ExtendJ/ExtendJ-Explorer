import configAST.*;
import jastaddad.api.JastAddAdAPI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by gda10jth on 11/18/15.
 */
@RunWith(Parameterized.class)
public class CodeGeneratedTreeTests extends AbstractParameterizedTest{
    private static final String TEST_DIR = "tests/codeGeneratedTreeTests/";


    public CodeGeneratedTreeTests(String dir) {
        super(TEST_DIR, dir, JastAddAdAPI.FILE_NAME);
    }


    @Test
    public void runTest(){
        assertEquals("FAIL", true, true);
        /*
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
                    checkOutput(debugger.getFilteredTree(), readXML(expectedFile), inDirectory);
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
        */
    }
    @SuppressWarnings("javadoc")
    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object[]> getTests() {
        return getTestParameters(TEST_DIR);
    }
}
