import configAST.*;
import jastaddad.api.JastAddAdAPI;
import jastaddad.tasks.JastAddAdXML;
import org.junit.Test;

/**
 * Created by gda10jth on 11/18/15.
 */
public class CodeGeneratedTreeTests {

    @Test
    public void nullRootTree(){
        runThisTree(null, "nullRootTree");
    }
/*
// add this one agains!!!!
    @Test
    public void nullNodeInTree(){
        List a = new List();
        List idDecls = new List();
        idDecls.add(new IdDecl("f1"));
        Use use = new Use(idDecls);
        List bindings = new List();
        FilterConfig filterConfig = new FilterConfig(new Opt<>(use), bindings);
        List nodes = new List();
        nodes.add(new Node(new LangDecl(":ASTNode"), new List()));
        Filter filter = new Filter(new IdDecl("f1"), nodes);
        List filterList = new List();
        filterList.add(filter);
        DebuggerConfig d = new DebuggerConfig(filterConfig, filterList);
        runThisTree(d, "nullNodeInTree");
    }
*/
    private void runThisTree(Object program, String expectedFile){
        String inDirectory = "tests/codeGeneratedTreeTests/";
        JastAddAdAPI debugger = new JastAddAdAPI(program);
        debugger.setFilterDir("tests/codeGeneratedTreeTests/");
        debugger.run();
        JastAddAdXML xmlPrinter = new JastAddAdXML(debugger);
        xmlPrinter.printXml(inDirectory, expectedFile, ".out");
        new OutoutXMLcomparer().checkOutput(debugger.getFilteredTree(), expectedFile + ".expected", inDirectory);
    }
}