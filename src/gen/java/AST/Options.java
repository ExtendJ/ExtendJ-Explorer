package AST;

import java.util.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.io.File;
import java.util.Set;
import java.util.ArrayList;
import beaver.*;
import org.jastadd.util.*;
import java.util.zip.*;
import java.io.*;
import java.io.FileNotFoundException;
/**
 * @ast class
 * @declaredat /home/johan/Arbete/JastAddAd/jastaddj/java4/frontend/Options.jadd:19
 */
public class Options extends java.lang.Object {
public  Collection<String> files() {
    return files;
  }

public  void initOptions() {
    options.clear();
    optionDescriptions.clear();
    files.clear();
  }

public  void addKeyOption(String name) {
    if(optionDescriptions.containsKey(name))
      throw new Error("Command line definition error: option description for " + name + " is multiply declared");
    optionDescriptions.put(name, new Option(name, false, false));
  }

public  void addKeyValueOption(String name) {
    if(optionDescriptions.containsKey(name))
      throw new Error("Command line definition error: option description for " + name + " is multiply declared");
    optionDescriptions.put(name, new Option(name, true, false));
  }

public  void addKeyCollectionOption(String name) {
    if(optionDescriptions.containsKey(name))
      throw new Error("Command line definition error: option description for " + name + " is multiply declared");
    optionDescriptions.put(name, new Option(name, true, true));
  }

public  void addOptionDescription(String name, boolean value) {
    if(optionDescriptions.containsKey(name))
      throw new Error("Command line definition error: option description for " + name + " is multiply declared");
    optionDescriptions.put(name, new Option(name, value, false));
  }

public  void addOptionDescription(String name, boolean value, boolean isCollection) {
    if(optionDescriptions.containsKey(name))
      throw new Error("Command line definition error: option description for " + name + " is multiply declared");
    optionDescriptions.put(name, new Option(name, value, isCollection));
  }

public  void addOptions(String[] args) {
    java.util.List<String> argList = new ArrayList<String>();

    // expand argument files
    for (int i = 0; i < args.length; i++) {
      String arg = args[i];
      if (arg.length() > 1 && arg.startsWith("@")) {
        if (arg.startsWith("@@")) {
          // escape the double at
          argList.add(arg.substring(1));
        } else {
          String fileName = arg.substring(1);
          try {
            java.io.StreamTokenizer tokenizer = new java.io.StreamTokenizer(new java.io.FileReader(fileName));
            tokenizer.resetSyntax();
            tokenizer.whitespaceChars(' ',' ');
            tokenizer.whitespaceChars('\t','\t');
            tokenizer.whitespaceChars('\f','\f');
            tokenizer.whitespaceChars('\n','\n');
            tokenizer.whitespaceChars('\r','\r');
            tokenizer.wordChars(33,255);
            tokenizer.commentChar('#');
            tokenizer.quoteChar('"');
            tokenizer.quoteChar('\'');
            while (tokenizer.nextToken() != tokenizer.TT_EOF) {
              argList.add(tokenizer.sval);
            }
          } catch (java.io.FileNotFoundException e) {
            System.err.println("Argument file not found: " + fileName);
          } catch (java.io.IOException e) {
            System.err.println("Exception: "+e.getMessage());
          }
        }
      } else {
        argList.add(arg);
      }
    }

    Iterator<String> all = argList.iterator();
    while (all.hasNext()) {
      String arg = all.next();
      if (arg.startsWith("-")) {
        if (!optionDescriptions.containsKey(arg)) {
          throw new Error("Command line argument error: option " + arg + " is not defined");
        }
        Option o = (Option) optionDescriptions.get(arg);

        if (!o.isCollection && options.containsKey(arg)) {
          throw new Error("Command line argument error: option " + arg + " is multiply defined");
        }

        if (o.hasValue) {
          String value = null;
          if (!all.hasNext()) {
            throw new Error("Command line argument error: value missing for key " + arg);
          }
          value = all.next();
          if (value.startsWith("-")) {
            throw new Error("Command line argument error: expected value for key " + arg +
                ", but found option " + value);
          }

          if (o.isCollection) {
            Collection<String> c = (Collection<String>) options.get(arg);
            if (c == null) {
              c = new ArrayList<String>();
            }
            c.add(value);
            options.put(arg, c);
          } else {
            options.put(arg, value);
          }
        } else {
          options.put(arg, null);
        }
      } else {
        files.add(arg);
      }
    }
  }

public  boolean hasOption(String name) {
    return options.containsKey(name);
  }

public  void setOption(String name) {
    options.put(name, null);
  }

public  boolean hasValueForOption(String name) {
    return options.containsKey(name) && options.get(name) != null;
  }

public  String getValueForOption(String name) {
    if(!hasValueForOption(name))
      throw new Error("Command line argument error: key " + name + " does not have a value");
    return (String)options.get(name);
  }

public  void setValueForOption(String value, String option) {
    options.put(option, value);
  }

public  Collection getValueCollectionForOption(String name) {
    if(!hasValueForOption(name))
      throw new Error("Command line argument error: key " + name + " does not have a value");
    return (Collection)options.get(name);
  }

public  boolean verbose() {
    return hasOption("-verbose");
  }

static  class Option {
    public String name;
    public boolean hasValue;
    public boolean isCollection;
    public Option(String name, boolean hasValue, boolean isCollection) {
      this.name = name;
      this.hasValue = hasValue;
      this.isCollection = isCollection;
    }
  }

private final  Map<String,Object> options =
    new HashMap<String,Object>();

private final  Map<String,Option> optionDescriptions =
    new HashMap<String,Option>();

private final  Set<String> files = new LinkedHashSet<String>();


}
