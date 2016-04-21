### Latest stable build is tagged as v1.0.4 ###

# DrAST readme #
This readme has the following content

* Getting Started 
    - Get the project on you machine
    - Building the project
    - Create jar file
    - Running DrAST
         - The easy way
         - The hard way
    - Try out an example project
    - Lib dependencies for Linux
    - The Filter Configuration Language


# Getting started #
### Important 1: This tool is based on Java 8. If you have an older version installed the tool will not work. ###
### Important 2: This tool requires at least JastAdd version 2.2.2. ###
### Important 3: If your only interested in using the tool, get the latest stable jar file from the Downloads section on Bitbucket. ###
## Get the project on you machine ##

Start of by cloning 
```
git clone https://git@bitbucket.org/jastadd/jastadddebugger-exjobb.git
```
OR checkout the latest stable build:
```
git fetch && git checkout v1.0.4
```

Then go to the directory where you cloned the project, with your terminal

## Building the project ##
In order to run DrAST you first need to build the project and then create its jar file.

There are two ways to assemble the project. Either just assemble:
```
./gradlew assemble
```
or build which will additionally run tests:
```
./gradlew build
```
Note! The second one will run both normal and GUI tests.

## Create jar file ##
After assembling the project, create the jar by writing the following command:
```
./gradlew gui
```
## See if things are working ##
To test if the DrASTGUI is ready to run on your computer you can just run the jar with the following command:
```
java -jar DrAST.jar
```
This should should start up the graphical user interface for DrAST. see Lib dependencis below if it's not working.

## Running DrAST ##
There are two ways of starting DrAST, the easy way and the hard way.
Both require some changes to your compiler code.

### The easy way ###
The easy way is to add a static field in you main class file, for example compiler.java .
The field you need to add is named ``` DrAST_root_node ```. 
Just add this field to your main class and then simply assign it the root node of your AST, in your compiler.

Here is an example in how this is done.
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

### The hard way ###
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

Now your done! Next time you run your project on your source code, DrASTGUI will start up after the parsing and scanning is done.

## Try out an example project ##
You can test DrAST on the small CaclASM compiler, which is located in the ``` CalcASM/ ``` folder.
First of you need to build this version of the CalcASM compiler, so simply go to the directory and run the following command: 
```
ant jar
```
This will create a .jar file named compiler that will contain the compiler for the CalcASM language. 
Now you can just choose the compiler with DrAST and a argument file, for example nesting2.calc under ``` CalcASM/testfiles/asm/nesting2.calc ```.
This will now display the CalcASM AST.

## Lib dependencies for Linux ##

When running the debugger on Linux the following library dependencies are required if any ToolKit errors are thrown.

- libgtk+-x11-2.0_0 - X11 backend of The GIMP ToolKit (GTK+).
- libgtk+2.0_0 - The GIMP ToolKit (GTK+), a library for creating GUIs.

## The Filter Configuration Language ##

To filter the presentation of your AST we have implemented a simple language that helps you with this. 
Check out the Wiki page for a small [tutorial](https://bitbucket.org/jastadd/drast/wiki/The%20Filter%20Configuration%20Language) in how you write and use that language.

Below is a standard template for the filter. This filter means that all nodes that are of class ASTNode or a child class of ASTNode should be shown (this means all nodes will be shown).

```
configs{
  use = f1;
}
filter f1{
  :ASTNode{
    when{}
    subtree{}
    show{}
    style{}
  }
}

```