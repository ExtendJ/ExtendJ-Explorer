import configAST.ConfigParser;
import configAST.ConfigScanner;
import configAST.DebuggerConfig;
import configAST.ErrorMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by gda10jli on 11/17/15.
 */
public class UIComponentTestSuite extends GuiTest {
    @Override
    protected Parent getRootNode() {
        try {
            System.out.println("start UI tests/\");
            try{
                ConfigScanner scanner = new ConfigScanner(new FileReader("tests/testInput.cfg"));
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
                    JastAddAd debugger = new JastAddAd(program, false);
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
        return null;
    }

    @Test
    public void TreeViewTest() {

    }
}
