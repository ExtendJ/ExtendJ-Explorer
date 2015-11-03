package jastaddad.objectinfo;

import java.lang.reflect.Method;

/**
 * Created by gda10jli on 10/20/15.
 */
public class Token extends NodeInfo {

    public Token(String name, Object value, Method m){ super(name, value, m);}

    @Override
    public String print(){ return name; }


}
