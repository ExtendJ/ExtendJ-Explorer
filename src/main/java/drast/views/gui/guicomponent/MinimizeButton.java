package drast.views.gui.guicomponent;

import javafx.scene.control.Button;

/**
 * Created by gda10jth on 2/5/16.
 */
public class MinimizeButton extends Button {
    private boolean minimizeNext;

    public MinimizeButton(){
        super();
        minimizeNext = true;
        setText("-");
    }

    public void setMinimized(boolean minimized){ minimizeNext = !minimized;}

    public boolean minimizeNext(){return minimizeNext;}
    public boolean isMinimized(){return !minimizeNext;}
}
