package drast.views;

import drast.model.DrAST;

/**
 * Created by gda10jth on 11/20/15.
 * Interface for classes that will use the DrASTAPI.
 */
public interface DrASTView {
    void run();
    void printMessage(int type, String message);
    DrAST getAPI();
    void setRoot(Object root, String filterPath, String defaultDir, boolean opened);
}
