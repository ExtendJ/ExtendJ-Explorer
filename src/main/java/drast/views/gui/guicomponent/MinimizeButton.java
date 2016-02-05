package drast.views.gui.guicomponent;

import javafx.scene.control.Button;

/**
 * Created by gda10jth on 2/5/16.
 */
public class MinimizeButton extends Button {
    private boolean minimize;

    public MinimizeButton(){
        super();
        minimize = false;
        setText("-");
        setOnAction(e->{
            System.out.println("KLICKAD");
            minimize = !minimize;
        });
    }

    public boolean minimizeNext(){return minimize;}
    public boolean isMinimized(){return !minimize;}
}
