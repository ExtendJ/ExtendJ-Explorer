import configAST.DebuggerConfig;
import configAST.Include;
import configAST.List;
import configAST.Opt;
import jastaddad.api.JastAddAdAPI;
import org.junit.Test;

/**
 * Created by gda10jth on 11/18/15.
 */
public class CodeGeneratedTreeTests {

    @Test
    public void nullRootTree(){
        runThisTree(null, "nullRootTree");
    }

    @Test
    public void nullNodeInTree(){
        List a = new List();
        a.addChild(new Include(null, new Opt<>()));
        DebuggerConfig d = new DebuggerConfig(new Opt(), a);
        runThisTree(d, "nullNodeInTree");
    }

    private void runThisTree(Object program, String expectedFile){
        String inDirectory = "tests/codeGeneratedTreeTests/";
        JastAddAdAPI debugger = new JastAddAdAPI(program);
        debugger.setFilterDir("tests/codeGeneratedTreeTests/");
        debugger.run();
        debugger.printToXML(inDirectory, expectedFile, ".out");
        new OutoutXMLcomparer().checkOutput(debugger.getFilteredTree(), expectedFile + ".expected", inDirectory);
    }
}