# DrAST - Interactive JastAdd AST Viewer

[![Build Status](https://travis-ci.org/llbit/DrAST.svg?branch=master)](https://travis-ci.org/llbit/DrAST)
[![Test Coverage](https://codecov.io/gh/llbit/DrAST/branch/master/graph/badge.svg)](https://codecov.io/gh/llbit/DrAST)

This file has the following content:

* Change Log
* Dependencies
* Getting Started
    - Get the project on you machine
    - Building the project
    - Create jar file
    - Running DrAST
         - The easy way
         - The hard way
    - Try out an example project
    - Filter Language


# Change Log

* 1.2.0 - New filter language, major code rewrites
* 1.0.6 - Release after SLE artifact evaluation, before major rewrites

## Dependencies

This tool is requires Java 8 update 40 or later to run. If you have an older
version installed the tool will not work.

DrAST is a JavaFX application, and some Linux distributions don't come with
JavaFX even if they have Java available. To install JavaFX on Ubuntu 16.04, run
this command:

* `sudo apt-get install openjfx`

If you are missing the JavaFX dependency you may get this error when trying to start DrAST:

```
Error: Could not find or load main class drast.views.gui.DrASTGUI
```

When running on Linux the following library dependencies are required if any
ToolKit errors are thrown:

- `libgtk+-x11-2.0_0` - X11 backend of The GIMP ToolKit (GTK+).
- `libgtk+2.0_0` - The GIMP ToolKit (GTK+), a library for creating GUIs.

## JastAdd Compatibility

This tool requires at least JastAdd version 2.2.2. It will not work with older JastAdd-projects.

# Getting Started

## Download DrAST

If you only want to use DrAST, and are not interested in looking at the source code,
you can get the latest stable release from the Bitbucket page:

* https://bitbucket.org/jastadd/drast/downloads

The following Git command can be used to check out the source code:

```
git clone https://git@bitbucket.org/jastadd/drast.git
```

## Building DrAST

You can use the included Gradle wrapper script to build DrAST:

```
./gradlew
```

This will produce a Jar file in the project root directory with a name similar to "DrAST-1.2.0.jar".

If you want to run the tests, run this command:

```
./gradlew check
```

To test if the DrASTGUI is ready to run on your computer you can just run the jar with the following command:
```
java -jar DrAST.jar
```

This should should start up the graphical user interface for DrAST. See the
dependencies section if it does not seem to work.

## Running your compiler in DrAST

There are two ways of using DrAST with your compiler: an easy way, and a hard
way.  Both require some changes to your compiler code.

### The easy way

The easy way is to add a static field in you main class file, for example compiler.java .
The field you need to add is named ``` DrAST_root_node ```.
Just add this field to your main class and then simply assign it the root node of your AST, in your compiler.

Here is an example in how this is done:

```
...
public static Object DrAST_root_node;
...
YourParser parser = new YourParser();
RootNode rootNode = (RootNode) parser.parse(scanner);
DrAST_root_node = rootNode; // Where rootNode is the Object of your AST:s root.
...
```

Now simply build your compiler and start DrAST with:

´´´
java -jar DrAST.jar
´´´
This will start the GUI, so choose your compiler and add some arguments, for example a source file.

### The hard way

First you need to add the DrAST .jar file to your projects Library path.
For example if you are using an ant build script do something like this:
Add the jar as a library to the script:
```
<property name="your.libname" value="[Other libs]:[path to debugger .jar]"/>
```
And then add the jar to the to the scripts jar target:
```
<zipfileset includes="**/*.*" src="/[path to debugger .jar]"/>
```
Note! The jastadddebugger-exjobb.jar contatins a number of .fxml and .css files, these are required to run the GUI. The include="\*\*/\*.\*" above does this, for ant.

Now DrAST is ready to run! In your own java code (typically the same class as you run your parser) add the following code when the AST is created:
```
YourScanner scanner = new YourScanner(...);
YourParser parser = new YourParser();
RootNode rootNode = (RootNode) parser.parse(scanner);
DrAST.ui.DrASTUI debugger = new DrAST.ui.DrASTUI(rootNode);// Where rootNode is the Object of your AST:s root.
debugger.run();
```

Now you are done! Next time you run your project on your source code, DrAST
will start up after the parsing and scanning is done.

## Try out an example project
You can test DrAST on the small CaclASM compiler, which is located in the ``` CalcASM/ ``` folder.
First of you need to build this version of the CalcASM compiler, so simply go to the directory and run the following command:
```
ant jar
```
This will create a .jar file named compiler that will contain the compiler for
the CalcASM language.
Now you can just choose the compiler with DrAST and a argument file, for example nesting2.calc under ``` CalcASM/testfiles/asm/nesting2.calc ```.
This will now display the CalcASM AST.

## Filter Language

DrAST presents a filtered view of the compiler's AST.  The filtering can be
controlled using filter rules. Filter rules are stored in filter files with the
extension `.fl2`. Here is an example of a filter file showing only the source
compilation units in an ExtendJ AST:

    exclude Program *
    include ** CompilationUnit when{fromSource()}


For each node in the AST, the first matching rule is found. If that rule is an
`include` rule, the node is included in the tree. If the rule is an `exclude`
rule, then the node is filtered out of the AST. Otherwise, the node is included
in the tree if its parent is included in the filtered tree.

By default the root node is shown. An empty filter thus leads to all nodes being shown.

A third type of rule, `set`, is used when you only want to change the displayed
properties of a node, without altering its visibility in the filtered AST.

Filter rules match nodes in the AST using path patterns and conditional
expressions.  Path patterns consist of a list of AST types, or wildcards. The
conditional expressions may include attributes, comparison operators, and
constants.

A path pattern of the form `Program *` matches all children of the root
program node. A pattern of the form `** CompilationUnit` matches
`CompilationUnit` nodes, or nodes of a subtype, anywhere in the AST.
Note that the path pattern must match the whole path from root to node.

Conditional expressions can be supplied for each concrete path part by
using the form `Type { condition }`.

The context free grammar for the filter language is below:

```
filter_rules -> rule_list?

rule_list -> rule | rule_list rule
rule -> "include" path condition? show? stylesheet?
rule -> "exclude" path condition? show? stylesheet?
rule -> "set" path condition? show? stylesheet?

path -> path_part | path path_part
path_part -> "**"
path_part -> "*" inline_condition?
path_part -> ID inline_condition?

condition -> "when" "{" expression? "}"
inline_condition -> "{" expression? "}"

show -> "show" "{" show_attributes? "}"
show_attributes -> attrname | show_attributes attrname
attrname -> ID

stylesheet -> "style" "{" dict? "}"
dict -> style | dict style
style -> STRING ":" STRING

expression -> operand
expression -> expression AND operand
expression -> expression OR operand
expression -> expression EQ operand
operand -> constant | attribute
constant -> "true" | "false" | INT | STRING | FLOAT | DOUBLE
attribute -> ID | ID "(" arguments? ")"
arguments -> operand | arguments COMMA operand
```

