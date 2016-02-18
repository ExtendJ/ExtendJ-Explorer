package drast.views.gui.guicomponent;

import java.util.HashSet;

/**
 * Created by gda10jli on 1/14/16.
 *
 * Removes some unwanted "words" from String values. Examples are java.lang and java.util.
 * This is to make nice, short names for the GUI.
 *
 */
public class TextFormatter {
    private HashSet<String> rules;

    public TextFormatter(Class rootClass){
        rules = new HashSet<>();
        rules.add("java.lang.");
        rules.add("java.util.");
        rules.add(rootClass.getPackage().getName() + ".");
    }

    public String format(Object obj){
        if(obj == null)
            return null;
       return format(obj.toString());
    }

    public String format(String obj){
        String name = obj;
        for(String rule : rules)
            name = name.replace(rule, "");
        return name;
    }
}
