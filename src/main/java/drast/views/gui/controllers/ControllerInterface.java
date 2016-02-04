package drast.views.gui.controllers;

import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.Monitor;

/**
 * Created by gda10jth on 2/3/16.
 */
public interface ControllerInterface {
    public void init(Monitor mon);

    public void onNewAPI();
    /**
     * Called when a funciton starts from the Controller. A function can be a dialog.
     */
    public void functionStarted();
    /**
     * Called when a funciton stops from the Controller. A function can be a dialog.
     */
    public void functionStopped();
    public void nodeSelected(GenericTreeNode node);
    public void nodeDeselected();

    public void updateGUI();
}
