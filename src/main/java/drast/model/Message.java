package drast.model;

/**
 * Created by gda10jth on 12/15/15.
 * <p>
 * Container class for a message within the API.
 * <p>
 * An AlertMessage have a type and the message.
 */
public class Message {
  private final Severity severity;
  public final String message;

  public Message(Severity severity, String message) {
    this.severity = severity;
    this.message = message;
  }

  public boolean isWarning() {
    return severity == Severity.WARNING;
  }

  public boolean isError() {
    return severity == Severity.ERROR;
  }

  public boolean isMessage() {
    return severity == Severity.INFO;
  }

  public static Message error(String format, Object... args) {
    return new Message(Severity.ERROR, String.format(format, args));
  }

  public static Message warning(String format, Object... args) {
    return new Message(Severity.WARNING, String.format(format, args));
  }

  public static Message info(String format, Object... args) {
    return new Message(Severity.INFO, String.format(format, args));
  }
}
