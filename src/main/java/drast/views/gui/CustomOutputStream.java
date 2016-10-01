package drast.views.gui;

import drast.model.Message;
import drast.model.Severity;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;

/**
 * Created by gda10jli on 3/2/16.
 */
public class CustomOutputStream extends OutputStream {

  private final Consumer<Message> logger;
  private final Severity severity;
  private String message;


  public CustomOutputStream(Consumer<Message> logger, Severity severity) {
    this.logger = logger;
    this.severity = severity;
    this.message = "";
  }

  @Override public void write(int b) throws IOException {
    // TODO this is kinda ugly, and it wont handle the print statements due to that those wont have a newline
    if (b == '\n') {
      logger.accept(new Message(severity, message));
      message = "";
    } else if (b != '\r') {
      message += String.valueOf((char) b);
    }
  }
}
