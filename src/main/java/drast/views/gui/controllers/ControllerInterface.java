package drast.views.gui.controllers;

import drast.model.filteredtree.GenericTreeNode;
import drast.views.gui.GUIData;

/**
 * Created by gda10jth on 2/3/16.
 */
interface ControllerInterface {
  void init(GUIData mon);

  void onSetRoot();

  void nodeSelected(GenericTreeNode node);

  void nodeDeselected();

  void updateGUI();
}
