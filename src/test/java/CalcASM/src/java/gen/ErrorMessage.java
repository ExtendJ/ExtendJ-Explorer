package CalcASM.src.java.gen;

/**
 * @ast class
 * @aspect Errors
 * @declaredat /home/gda10jli/Documents/jastadddebugger-exjobb/CalcASM/src/jastadd/Errors.jrag:5
 */
public class ErrorMessage extends Object implements Comparable<ErrorMessage> {

  protected final String message;


  protected final int lineNumber;


  public ErrorMessage(String message, int lineNumber) {
    this.message = message;
    this.lineNumber = lineNumber;
  }


  public int compareTo(ErrorMessage other) {
    if (lineNumber == other.lineNumber) {
      return message.compareTo(other.message);
    }
    return Integer.compare(lineNumber, other.lineNumber);
  }


  public String toString() {
    return "Error at line " + lineNumber + ": " + message;
  }


}
