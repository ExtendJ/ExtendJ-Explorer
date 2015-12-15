package test.codegeneratedtests;

import CalcASM.src.gen.lang.ast.Mul;
import CalcASM.src.gen.lang.ast.Numeral;
import CalcASM.src.gen.lang.ast.Program;
import jastaddad.api.JastAddAdAPI;
import jastaddad.tasks.JastAddAdXML;
import jastaddad.ui.JastAddAdUI;
import org.junit.Test;
import test.OutoutXMLcomparer;

/**
 * Created by gda10jth on 11/18/15.
 */
public class CodeGeneratedTreeTests {

    @Test
    public void nullRootTree(){
        runThisTree(null, "nullRootTree");
    }

// add this one agains!!!!
    @Test
    public void nullNodeInTree(){
        Program program = new Program(new Mul(new Numeral("1"), null));
        runThisTree(program, "nullNodeInTree");
    }

    private void runThisTree(Object program, String expectedFile){
        String inDirectory = "tests/codeGeneratedTreeTests/";
        JastAddAdAPI debugger = new JastAddAdAPI(program);
        debugger.setFilterDir("tests/codeGeneratedTreeTests/");
        debugger.run();
        JastAddAdXML xmlPrinter = new JastAddAdXML(debugger);
        xmlPrinter.printXml(inDirectory, expectedFile, ".out");
        //new OutoutXMLcomparer().checkOutput(debugger.getFilteredTree(), expectedFile + ".expected", inDirectory);
    }
}