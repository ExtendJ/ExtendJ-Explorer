package jastaddad.tasks;

import jastaddad.api.JastAddAdAPI;

/**
 * Created by gda10jth on 11/20/15.
 * Interface for classes that will use the JastAddAdAPI.
 */
public interface JastAddAdTask {
    void run();
    void printMessage(String type, String message);
    JastAddAdAPI getAPI();
    void setRoot(Object root, String filterPath, String defaultDir, boolean opened);
}
