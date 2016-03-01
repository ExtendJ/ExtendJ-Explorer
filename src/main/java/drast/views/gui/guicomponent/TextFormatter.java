package drast.views.gui.guicomponent;

/**
 * Created by gda10jli on 1/14/16.
 *
 * Removes some unwanted "words" from String values. Examples are java.lang and java.util.
 * This is to make nice, short names for the GUI.
 *
 */
public class TextFormatter {
    private String rules;

    public TextFormatter(Class rootClass){
        rules = "(java.lang.)";
        rules += "|(java.util.)";
        rules += "|(java.io.)";
        rules += "|(java.net.)";
        rules += "|(java.math.)";
        rules += "|(" +rootClass.getPackage().getName() + ".)";
    }

    public String format(Object obj){
        if(obj == null)
            return null;
       return format(obj.toString());
    }

    public String format(String obj){ return obj.replaceAll(rules, ""); }
}
