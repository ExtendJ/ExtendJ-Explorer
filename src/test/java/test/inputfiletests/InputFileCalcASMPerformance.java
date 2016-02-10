package test.inputfiletests;

import CalcASM.src.java.gen.LangParser;
import CalcASM.src.java.gen.LangScanner;
import CalcASM.src.java.gen.Program;
import beaver.Parser;
import drast.model.DrAST;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by gda10jli on 2/10/16.
 */
public class InputFileCalcASMPerformance  {
    /**
     * Directory where test files live
     */
    private static final String TEST_DIR = "tests/inputFileTests/performance-time/";

    public InputFileCalcASMPerformance() {}

    @Test
    public void runInputTests() {
        System.out.println("start test in: " + TEST_DIR);
        try {
            String filename = TEST_DIR + "/input.calc";
            LangScanner scanner = new LangScanner(new FileReader(filename));
            LangParser parser = new LangParser();
            Program program = (Program) parser.parse(scanner);
            DrAST drast = new DrAST(program);
            drast.run();
            assertTrue("Error: The reflection of the AST took to long, exceeded 10 seconds ", drast.api().getReflectedTreeTime() < 10000l);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        } catch (Parser.Exception e) {
            e.printStackTrace();
        }
    }

}
