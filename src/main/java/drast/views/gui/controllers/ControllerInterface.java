package drast.views.gui.controllers;

import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.Monitor;

/**
 * Created by gda10jth on 2/3/16.
 */
public interface ControllerInterface {
    void init(Monitor mon);

    void onNewAPI();
    /**
     * Called when a funciton starts from the Controller. A function can be a dialog.
     */
    void functionStarted();
    /**
     * Called when a funciton stops from the Controller. A function can be a dialog.
     */
    void functionStopped();
    void nodeSelected(GenericTreeNode node);
    void nodeDeselected();

    void updateGUI();

    void onApplicationClose();
}
