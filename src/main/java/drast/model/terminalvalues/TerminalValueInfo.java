package drast.model.terminalvalues;

/**
 * A Holder for attribute informations, can hold a reference to a NodeInfo
 * Created by gda10jli on 11/3/15.
 */
public class TerminalValueInfo implements Comparable<TerminalValueInfo>{

    private final String name;
    private final Object value;
    private boolean filePointer;

    public TerminalValueInfo(String name, Object value, boolean filePointer){
        this.name = name;
        this.value = value;
        this.filePointer = filePointer;
    }

    public TerminalValueInfo(String name, Object value){
        this.name = name;
        this.value = value;
    }

    public String getName() { return name; }
    public Object getValue() {
        return value;
    }
    public boolean isFilePointer() {
        return filePointer;
    }

    @Override
    public int compareTo(TerminalValueInfo node) {
        return name.compareTo(node.name);
    }
}
