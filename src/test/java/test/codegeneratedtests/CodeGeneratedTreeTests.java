package test.codegeneratedtests;

import CalcASM.src.java.gen.Mul;
import CalcASM.src.java.gen.Numeral;
import CalcASM.src.java.gen.Program;
import drast.model.DrAST;
import drast.views.xml.DrASTXML;
import org.junit.Test;

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
        DrAST debugger = new DrAST(program);
        debugger.setFilterPath("tests/codeGeneratedTreeTests/filter.fcl");
        debugger.run();
        DrASTXML xmlPrinter = new DrASTXML(debugger);
        xmlPrinter.printXml(inDirectory, expectedFile, ".out");
        //new OutoutXMLcomparer().checkOutput(debugger.getFilteredTree(), expectedFile + ".expected", inDirectory);
    }
}