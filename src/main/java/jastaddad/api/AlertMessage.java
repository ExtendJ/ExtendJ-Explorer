package jastaddad.api;

/**
 * Created by gda10jth on 12/15/15.
 *
 * Container class for a message within the API.
 *
 * An AlertMessage have a type and the message.
 */
public class AlertMessage {
    public static final String AST_STRUCTURE_WARNING = "AST structure warning";
    public static final String AST_STRUCTURE_ERROR = "AST structure error";
    public static final String FILTER_ERROR = "FILTER ERROR";
    public static final String FILTER_WARNING = "FILTER WARNING";
    public static final String INVOCATION_WARNING = "INVOCATION WARNING";
    public static final String INVOCATION_ERROR = "INVOCATION ERROR";

    public final String type;
    public final String message;
    public AlertMessage(String type, String message){
        this.type = type;
        this.message = message;
    }
}
