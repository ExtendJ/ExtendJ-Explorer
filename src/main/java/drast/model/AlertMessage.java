package drast.model;

/**
 * Created by gda10jth on 12/15/15.
 *
 * Container class for a message within the API.
 *
 * An AlertMessage have a type and the message.
 */
public class AlertMessage {
    public static final int SETUP_FAILURE = 1;

    public static final int AST_STRUCTURE_WARNING = 2;
    public static final int AST_STRUCTURE_ERROR = 3;

    public static final int FILTER_ERROR = 4;
    public static final int FILTER_WARNING = 5;
    public static final int FILTER_MESSAGE = 6;

    public static final int INVOCATION_WARNING = 7;
    public static final int INVOCATION_ERROR = 8;

    public static final int VIEW_ERROR = 9;
    public static final int VIEW_WARNING = 10;
    public static final int VIEW_MESSAGE = 11;



    public final int type;
    public final String message;
    public AlertMessage(int type, String message){
        this.type = type;
        this.message = message;
    }

    public boolean isWarning(){
        switch (type) {
            case AST_STRUCTURE_WARNING:
            case FILTER_WARNING:
            case VIEW_WARNING:
            case INVOCATION_WARNING: return true;
        }
        return false;
    }

    public boolean isError(){
        switch (type) {
            case AST_STRUCTURE_ERROR:
            case FILTER_ERROR:
            case VIEW_ERROR:
            case INVOCATION_ERROR: return true;
        }
        return false;
    }

    public boolean isMessage(){
        return !isError() && !isWarning();
    }

    public String typeString(){
        switch (type){
            case SETUP_FAILURE: return "Setup failure";
            case AST_STRUCTURE_WARNING: return "AST structure warning";
            case AST_STRUCTURE_ERROR: return "AST structure error";
            case FILTER_ERROR: return "Filter error";
            case FILTER_WARNING: return "Filter warning";
            case FILTER_MESSAGE: return "Filter message";
            case INVOCATION_WARNING: return "Invocation warning";
            case INVOCATION_ERROR: return "Invocation error";
            case VIEW_ERROR: return "";
            case VIEW_WARNING: return "";
            case VIEW_MESSAGE: return "";
        }
        return "";
    }
}
