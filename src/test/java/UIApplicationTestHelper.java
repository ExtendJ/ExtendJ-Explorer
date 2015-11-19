import javafx.scene.Node;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.service.finder.NodeFinder;
import org.testfx.service.finder.NodeFinderException;

import static org.testfx.api.FxService.serviceContext;

/**
 * Created by gda10jth on 11/19/15.
 */
public abstract class UIApplicationTestHelper extends ApplicationTest {
    private static final NodeFinder nodeFinder = serviceContext().getNodeFinder();
    public static <T extends Node> T find(String query) {
        try {
            return nodeFinder.lookup(query).queryFirst();
        }
        catch (NodeFinderException exception) {
            throw exception;
        }
    }
}
