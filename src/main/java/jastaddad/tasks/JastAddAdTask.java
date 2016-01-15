package jastaddad.tasks;

import jastaddad.api.JastAddAdAPI;

/**
 * Created by gda10jth on 11/20/15.
 * Interface for classes that will use the JastAddAdAPI.
 */
public interface JastAddAdTask {
    void run();
    JastAddAdAPI getAPI();
    void setRoot(Object root, String filterPath);
}
