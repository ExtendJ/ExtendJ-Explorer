package drast.views.gui.guicomponent;

import drast.views.gui.controllers.Controller;
import javafx.scene.control.IndexRange;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.NavigationActions;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;
import org.fxmisc.richtext.StyledText;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gda10jth on 12/14/15.
 * This is the filter editor in the program. It uses a library called RichTextFx by Tomas Mikula.
 * This library gives us line numbers and syntax highlighting.
 * It also lets us handel events in the text area.
 */
public class FilterEditor extends CodeArea {
  private final boolean removeHistory;

  // Words that will be highlighted with orange.
  private static final String[] KEYWORDS =
      new String[] {"include", "exclude", "set", "when", "show", "style", "true", "false", "\\*\\*",
          "\\*"};

  // Patterns for different highlighter objects.
  private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
  private static final String PAREN_PATTERN = "\\(|\\)";
  private static final String BRACE_PATTERN = "\\{|\\}";
  private static final String BRACKET_PATTERN = "\\[|\\]";
  private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
  private static final String COMMENT_PATTERN = "//[^\n]*|/\\*(.|\\R)*?\\*/";

  private static final Pattern PATTERN = Pattern.compile(
      "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
      + "|(?<PAREN>" + PAREN_PATTERN + ")"
      + "|(?<BRACE>" + BRACE_PATTERN + ")"
      + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
      + "|(?<STRING>" + STRING_PATTERN + ")"
      + "|(?<COMMENT>" + COMMENT_PATTERN + ")");

  // The editor uses spaces instead of tabs.
  private static final String CODE_AREA_TAB = "    ";

  public FilterEditor(Controller con) {
    removeHistory = true;
    // When a change happens in the text area, see if something should be highlighted or not.
    setParagraphGraphicFactory(LineNumberFactory.get(this));
    richChanges().subscribe(change -> {
      String changeText = change.getInserted().getText();
      if (changeText.equals("{")) {
        replaceSelection("}");
        setCaretPosition(getCaretPosition() - 1);
      }
      FilterEditor.this.setStyleSpans(0, computeHighlighting(getText()));
    });

    addEventHandler(KeyEvent.KEY_PRESSED, event -> {
      if (event.getCode() == KeyCode.BACK_SPACE) {
        backspaceClicked();
      } else if (event.getCode() == KeyCode.TAB) {
        tabClicked(event);
      } else if (event.getCode() == KeyCode.DELETE) {
        deleteClicked();
      } else if (event.getCode() == KeyCode.ENTER) {
        enterClicked(event);
      } else if (event.isControlDown() && event.getCode() == KeyCode.V) {
        pastePerformed(event);
      } else if (event.isControlDown() && event.getCode() == KeyCode.S) {
        con.saveFilter();
      }
    });

    /* TODO: This should be removed in the future when a new release for richtextfx comes.
     * The problem: When double clicking a word, the word is selected PLUS 1+ white spaces before it.
     * Apparently it is fixed but not pushed in a release at this time.
     * The richtextfx version used here when writing this is: 0.6.10
     */
    addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
      if (event.getClickCount() == 2) {
        IndexRange range = getSelection();
        String selectedText = getSelectedText();
        int i;
        for (i = 0; i < selectedText.length(); i++) {
          if (selectedText.charAt(i) != ' ') {
            break;
          }
        }
        selectRange(range.getStart() + i, range.getEnd());

      }
    });

    // Set the default filter as the initial text:
    setText(String.join("\n",
        "// Example filter rules:",
        "// exclude Program *",
        "// include ** CompilationUnit when {fromSource()}"));
  }

  /**
   * Enter button event. This method computes the indention of the new line. The indention is based
   * on the current row indention, and maybe one more tab if the row ends with a {.
   */
  private void enterClicked(KeyEvent event) {
    List<StyledText<Collection<String>>> segments =
        getParagraph(getCurrentParagraph()).getSegments();
    String row = getParagraph(getCurrentParagraph()).toString().replace(" ", "");
    if (segments.size() <= 0) {
      return;
    }

    StyledText<Collection<String>> indentSegment = segments.get(0);

    String indent = "";
    int pos = -1;
    for (int i = 0; i < indentSegment.length(); i++) {
      if (i % 4 == 3) {
        indent += CODE_AREA_TAB;
      }
      // End condition of the loop.
      if (indentSegment.charAt(i) != ' ' || i == indentSegment.length() - 1) {

        int masPos = row.indexOf('}');
        if (masPos > 0 && row.charAt(masPos - 1) == '{') {
          pos = getCaretPosition() + indent.length() + CODE_AREA_TAB.length() + 1;
          indent = indent + CODE_AREA_TAB + "\n" + indent;
          break;
        }
        // If a { is at the end of the line, add an indention.
        if (segments.get(segments.size() - 1).charAt(0) == '{' || (segments.size() > 2
            && segments.get(segments.size() - 2).charAt(0) == '{')) {

          indent += CODE_AREA_TAB;
        }
        break;
      }
    }
    replaceSelection("\n" + indent);
    if (pos > -1) {
      setCaretPosition(pos);
    }
    event.consume();
  }

  private void setCaretPosition(int pos) {
    positionCaret(pos);
    selectRange(pos, pos);
  }

  /**
   * Delete button event. Checks if the next three characters are white spaces (a tab in this editor). If this is the
   * case, remove all 4 else behave like a normal delete event
   */
  private void deleteClicked() {
    int caretPos = getCaretPosition();
    if (getSelection().getLength() == 0 && caretPos + 4 <= getText().length()) {
      String things = getText().substring(caretPos, caretPos + 4);
      boolean tabDel = true;
      for (int i = 3; i >= 0; i--) {
        if (things.charAt(i) != ' ') {
          tabDel = false;
          break;
        }
      }
      if (tabDel) {
        moveTo(caretPos + 4, NavigationActions.SelectionPolicy.EXTEND);
      }
    }
  }

  /**
   * Backspace button event. checks two things
   * - if the current row only consists of white spaces, remove the whole row.
   * - if the previous three characters are white spaces (a tab in this editor) remove them all.
   * else behave like a normal backspace event.
   */
  private void backspaceClicked() {
    int caretPos = getCaretPosition();
    if (getSelection().getLength() == 0 && caretPos - 4 >= 0) {

      if (getParagraph(getCurrentParagraph()).toString().matches("^[\\s]+$")) {
        lineEnd(SelectionPolicy.CLEAR);
        lineStart(SelectionPolicy.EXTEND);
        replaceSelection("");
        return;
      }

      String things = getText().substring(caretPos - 4, caretPos);
      boolean tab = true;
      for (int i = 3; i >= 0; i--) {
        if (things.charAt(i) != ' ') {
          tab = false;
          break;
        }
      }
      if (tab) {
        moveTo(caretPos - 4, NavigationActions.SelectionPolicy.EXTEND);
      }
    }
  }

  /**
   * Tab button event. This method checks multiple things
   * - If there is a selection, add a tab in front of every selected row
   * or if shift button is down, remove a tab in each selected row instead.
   * - If no selection is made, add a CODE_AREA_TAB like normal
   * or if shift button is down, remove a tab if there is one
   */
  private void tabClicked(KeyEvent event) {
    // First see if anything is selected and act accordingly
    if (getSelection().getLength() != 0) {
      int start = getSelection().getStart();
      int end = getSelection().getEnd();

      // Extend the selection to the whole line of the first row
      boolean anchorFirst = getAnchor() < getCaretPosition();
      if (anchorFirst) {
        positionCaret(start);
        lineStart(NavigationActions.SelectionPolicy.CLEAR);
        moveTo(end, SelectionPolicy.EXTEND);
      } else {
        lineStart(NavigationActions.SelectionPolicy.EXTEND);
      }

      // get the selected text
      start = getSelection().getStart();
      String selected = getSelectedText();

      // add or replace tabs in the selection string
      if (event.isShiftDown()) {
        // divide the selection string into the first row and the rest and then remove tabs in each
        positionCaret(start);
        lineEnd(SelectionPolicy.CLEAR);
        int subStringEnd = getCaretPosition();
        selectRange(start, subStringEnd);
        String subFirstLine = getSelectedText();
        selectRange(subStringEnd, end);
        String rest = getSelectedText().replace("\n" + CODE_AREA_TAB, "\n");
        subFirstLine = subFirstLine.replaceFirst(CODE_AREA_TAB, "");
        selected = subFirstLine + rest;
      } else {
        selected = CODE_AREA_TAB + selected.replace("\n", "\n" + CODE_AREA_TAB);
      }

      // Replace the selection with the new string.
      selectRange(start, end);
      replaceSelection(selected);

      // Reset the selection for the user.
      if (anchorFirst) {
        selectRange(start, start + selected.length());
      } else {
        selectRange(start + selected.length(), start);
      }
    } else {
      // No selection goes here.
      if (event.isShiftDown()) {
        int posExtra = 0;
        int pos = getCaretPosition();
        lineStart(SelectionPolicy.CLEAR);
        lineEnd(SelectionPolicy.EXTEND);
        String newRow = getSelectedText().replaceFirst(CODE_AREA_TAB, "");
        if (!newRow.equals(getSelectedText())) {
          replaceSelection(newRow);
          //System.out.println("diff: " + (pos - getSelection().getStart()));
          lineStart(SelectionPolicy.CLEAR);
          if (pos - getCaretPosition() >= 3) {
            posExtra = 4;
          }
        }
        int newPos = pos - posExtra;
        selectRange(newPos, newPos);
        setCaretPosition(newPos);
      } else {
        replaceSelection(CODE_AREA_TAB);
      }
    }
    event.consume();
  }

  /**
   * Paste text from clipboard and refractorize the code, for example if tabs need to be removed.
   */
  private void pastePerformed(KeyEvent event) {
    paste();
    refract();
    event.consume();
  }

  /**
   * Changes the text in the area to something we want.
   * - Tabs is replaced with the custom CODE_AREA_TAB.
   */
  private void refract() {
    if (getText().contains("\t")) {
      int pos = getCaretPosition();
      replaceText(getText().replace("\t", CODE_AREA_TAB));
      setCaretPosition(pos);
    }
  }

  /**
   * set text of code area
   */
  private void setText(String filter) {
    replaceText(filter.replace("\t", CODE_AREA_TAB));
    if (removeHistory) {
      getUndoManager().forgetHistory();
      getUndoManager().mark();
    }
  }

  /**
   * Matches the text with the pattern, and add styles according to matches.
   */
  private static StyleSpans<Collection<String>> computeHighlighting(String text) {
    Matcher matcher = PATTERN.matcher(text);

    int lastKwEnd = 0;
    StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
    spansBuilder.add(Collections.singleton("whiteText"), 0);
    while (matcher.find()) {
      String styleClass;
      if (matcher.group("KEYWORD") != null) {
        styleClass = "keyword";
      } else if (matcher.group("PAREN") != null) {
        styleClass = "brace";
      } else if (matcher.group("BRACE") != null) {
        styleClass = "brace";
      } else if (matcher.group("BRACKET") != null) {
        styleClass = "bracket";
      } else if (matcher.group("STRING") != null) {
        styleClass = "string";
      } else if (matcher.group("COMMENT") != null) {
        styleClass = "comment";
      } else {
        continue;
      }
      spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
      spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
      lastKwEnd = matcher.end();
    }
    spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
    return spansBuilder.create();
  }

  public boolean loadFile(File file) {
    try {
      // Check if file exists.
      if (file != null && file.isFile()) {
        setText(String.join("\n", Files.readAllLines(file.toPath())));
      }
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }
}
