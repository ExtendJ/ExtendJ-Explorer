package jastaddad.objectinfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by gda10jli on 10/20/15.
 */
public class Token extends NodeInfo {

    public Token(String name, String value){ super(name, value);}

    @Override
    public String print(){ return name + " : " + value; }


}
