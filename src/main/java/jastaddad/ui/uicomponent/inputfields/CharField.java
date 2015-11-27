package jastaddad.ui.uicomponent.inputfields;

import javafx.scene.control.TextField;

/**
 * Created by gda10jth on 11/27/15.
 */
public class CharField extends TextField{
    @Override
    public void replaceText(int start, int end, String text) {
        int pos = getCaretPosition();
        String old = getText();
        super.replaceText(start, end, text);
        if(getText().length() > 1) {
            setText(old);
            positionCaret(pos);
        }

    }

    @Override
    public void replaceSelection(String text) {
        if(text.length() <= 1)
            super.replaceSelection(text);
    }
}
