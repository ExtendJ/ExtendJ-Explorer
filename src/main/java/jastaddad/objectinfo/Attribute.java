package jastaddad.objectinfo;

/**
 * Created by gda10jli on 10/20/15.
 */
public class Attribute extends NodeInfo {
    private String type;

    public Attribute(String name, String value, String type){ super(name, value); this.type = type; }

    @Override
    public String print(){ return type + "" + name + " : " + value ; }

    public String getType(){ return type; }

}
