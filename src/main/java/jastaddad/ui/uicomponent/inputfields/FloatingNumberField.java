package jastaddad.ui.uicomponent.inputfields;

import javafx.scene.control.TextField;

/**
 * Created by gda10jth on 11/27/15.
 */
public class FloatingNumberField extends TextField {
    @Override
    public void replaceText(int start, int end, String text) {
        String old = this.getText();
        super.replaceText(start, end, text);
        if(!this.getText().matches("^[-]?[0-9]*[.]?[0-9]*$")){
            this.setText(old);
            positionCaret(start);
            if(end != start)
                selectPositionCaret(end);
        }
    }

    @Override
    public void replaceSelection(String text) {
        String old = this.getText();
        super.replaceSelection(text);
        if(!this.getText().matches("^[-]?[0-9]*[.]?[0-9]*$")){
            this.setText(old);
        }
    }
}
