package drast.views.gui.guicomponent;

import java.util.HashSet;
import java.util.Set;

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
       return format(asString(obj));
    }

    public String format(String obj){ return obj.replaceAll(rules, ""); }


    private String asString(Object obj) {
        if(obj.getClass().isPrimitive() || WRAPPER_TYPES.contains(obj.getClass()))
            return String.valueOf(obj);
        return String.format("%s@%s", obj.getClass().getName(), Integer.toHexString(System.identityHashCode(obj)));
    }

    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

    private static Set<Class<?>> getWrapperTypes() {
        Set<Class<?>> ret = new HashSet<>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        ret.add(String.class);
        ret.add(("").getClass());
        return ret;
    }
}
