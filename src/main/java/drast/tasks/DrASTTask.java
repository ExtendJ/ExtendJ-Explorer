package drast.tasks;

import drast.api.DrASTAPI;

/**
 * Created by gda10jth on 11/20/15.
 * Interface for classes that will use the DrASTAPI.
 */
public interface DrASTTask {
    void run();
    void printMessage(String type, String message);
    DrASTAPI getAPI();
    void setRoot(Object root, String filterPath, String defaultDir, boolean opened);
}
