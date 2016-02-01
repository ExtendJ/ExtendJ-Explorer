package drast.views.gui.guicomponent.inputfields;

import javafx.scene.control.TextField;

/**
 * Created by gda10jth on 11/27/15.
 * An TextField that only accepts one character, nothing more.
 */
public class CharField extends TextField{
    /**
     * When text is added to the field, this method is called.
     * It checks if the field text is to long, and ignore the new changes in that case.
     * @param start
     * @param end
     * @param text
     */
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
