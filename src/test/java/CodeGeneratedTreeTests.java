import configAST.*;
import jastaddad.api.JastAddAdAPI;
import junit.framework.TestCase;
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
public class CodeGeneratedTreeTests {

    @Test
    public void nullRootTree(){
        DebuggerConfig d = null;
        runThisTree(d, "nullRootTree");
    }

    @Test
    public void nullNodeInTree(){
        List a = new List();
        a.add(new Include(null, new Opt<NodeConfigList>()));
        DebuggerConfig d = new DebuggerConfig(new Opt(), a);
        runThisTree(d, "nullNodeInTree");
    }

    private void runThisTree(Object program, String expectedFile){
        String inDirectory = "tests/codeGeneratedTreeTests/";
        JastAddAdAPI debugger = new JastAddAdAPI(program);
        //debugger.setFilterDir("/");
        debugger.run();
        debugger.printToXML(inDirectory, expectedFile, ".out");
        new OutoutXMLcomparer().checkOutput(debugger.getFilteredTree(), expectedFile + ".expected", inDirectory);
    }
}