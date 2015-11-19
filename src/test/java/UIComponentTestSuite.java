import configAST.ConfigParser;
import configAST.ConfigScanner;
import configAST.DebuggerConfig;
import configAST.ErrorMessage;
import jastaddad.ui.JastAddAdUI;
import jastaddad.ui.UIMonitor;
import jastaddad.ui.controllers.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
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
            System.out.println("start UI tests");
            try{
                ConfigScanner scanner = new ConfigScanner(new FileReader("tests/uiTests/allBlocks/testInput.cfg"));
                ConfigParser parser = new ConfigParser();
                DebuggerConfig program = (DebuggerConfig) parser.parse(scanner);
                if (!program.errors().isEmpty()) {
                    System.err.println();
                    System.err.println("Errors: ");
                    for (ErrorMessage e: program.errors()) {
                        System.err.println("- " + e);
                    }
                } else {
                    JastAddAdUITest ui = new JastAddAdUITest(program);
                    ui.initRootView();
                    return ui.getRootView();
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
        }
        return null;
    }

    private class JastAddAdUITest extends JastAddAdUI{
        public JastAddAdUITest(Object root){
            super(root);
        }
        @Override
        protected void initRootView() throws IOException{
            jastAddAd.run();
            mon = new UIMonitor(jastAddAd.api());
            FXMLLoader loader = new FXMLLoader();
            rootView = loader.load(getClass().getResource("/main.fxml").openStream());
            con = loader.<Controller>getController();
            mon.setController(con);
        }

        public Parent getRootView(){
            return rootView;
        }
    }

    @Test
    public void TreeViewTest() {
        final Button button = find("#minimizeConsole");
        clickOn(button);
        final Button buttonRight = find("#minimizeRightSide");
        clickOn(buttonRight);
        final Button buttonleft = find("#minimizeLeftSide");
        clickOn(buttonleft);
    }
}
