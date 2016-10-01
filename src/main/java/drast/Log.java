package drast;

import drast.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Log {
  private static final List<Consumer<Message>> receivers = new ArrayList<>();

  public static void log(Message message) {
    for (Consumer<Message> receiver : receivers) {
      receiver.accept(message);
    }
  }

  public static void error(String format, Object... args) {
    log(Message.error(format, args));
  }

  public static void warning(String format, Object... args) {
    log(Message.warning(format, args));
  }

  public static void info(String format, Object... args) {
    log(Message.info(format, args));
  }

  public static void addReceiver(Consumer<Message> receiver) {
    receivers.add(receiver);
  }
}
