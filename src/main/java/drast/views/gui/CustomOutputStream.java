package drast.views.gui;

import drast.model.ASTBrain;
import drast.model.AlertMessage;
import drast.views.gui.controllers.ConsoleController;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by gda10jli on 3/2/16.
 */
public class CustomOutputStream extends OutputStream {

    private ASTBrain brain;
    private int type;
    private String message;


    public CustomOutputStream(ASTBrain brain, int type){
        this.brain = brain;
        this.type = type;
        this.message = "";
    }

    @Override
    public void write(int b) throws IOException { //Todo this is kinda ugly, and it wont handle the print statements due to that those wont have a newline
        if(b == '\r' || b == '\n') {
            brain.putMessage(type, message);
            message = "";
        }else
            message += String.valueOf((char)b);
    }
}
