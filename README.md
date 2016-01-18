### Work in progress. please contact gda10jth@student.lu.se and gda10jli@student.lu.se if you want to try it out. ###

# JastAddAd readme #
This readme has the following content

* Getting Started 
    - Get the project on you machine
    - Building the project
    - Create jar file
    - See if things are working
    - Try out an example project
    - Running JastAddAd on your project
    - Lib dependencies for Linux
    - The Filter Configuration Language


# Getting started #
### Important 1: This tool is based on Java 8. If you have an older version installed the tool will not work. ###
### Important 2: This tool is based on a new JastAdd version that is not in production yet. It is included in this project at tools/jastadd2.jar. ###
## Get the project on you machine ##


Start of by cloning 
```
git clone https://git@bitbucket.org/jastadd/jastadddebugger-exjobb.git
```
Then go to the directory where you cloned the project, with your terminal

## Building the project ##
In order to run JastAddAd you first need to build the project and then create its jar file.

There are two ways to assemble the project. Either just assemble:
```
./gradlew assemble
```
or build which will additionally run tests:
```
./gradlew build
```
Note! The second one will run both normal and UI tests.

## Create jar file ##
After assembling the project, create the jar by writing the following command:
```
./gradlew fatJar
```
## See if things are working ##
To test if the JastAddAd is ready to run on your computer you can just run the jar with the following command:
```
java -jar jastadddebugger-exjobb.jar
```
This should run a sample JastAdd project and run the JastAddAdUI program. see Lib dependencis below if it's not working.

Note! The debugger will run on the sample.fcl file in your directory. The file sample.fcl contain some Configuration Language code which will be described later.

## Try out an example project ##
Alternatively you can look at the small project CaclASM, that follows when you clone this project. This version of CalcASM is configured to run JastAddUI after its parsing. See row 43-44 in the file CalcASM/src/java/lang/compiler.java to see how it works. 

To run this version of CalcASM go to the directory and run the following command: 
```
ant jar
```
This will create a jar named compiler which contains the compiler for the CalcASM language. 
To start the JastAddAdUI simply run:
```
java -jar compiler.jar testfiles/asm/mul2.calc
```

## Running JastAddAd on your project ##
There are a few things that must be done to run JastAddAd on your own project.

* Add created Jar to your projects Library path.
For example if you are using an ant build script do something like this:
Add the jar as a library to the script:
```
<property name="your.libname" value="[Other libs]:[path to debugger .jar]"/>
```
And then add the jar to the to the scripts jar target:
```
<zipfileset includes="**/*.*" src="/[path to debugger .jar]"/> 
```
Note! The jastadddebugger-exjobb.jar contatins a number of .fxml and .css files, these are required to run the UI. The include="\*\*/\*.\*" above does this, for ant. 

Now JastAddAd is ready to run! In your own java code (typically the same class as you run your parser) add the following code when the AST is created: 
```
YourScanner scanner = new YourScanner(...);
YourParser parser = new YourParser();
RootNode rootNode = (RootNode) parser.parse(scanner);
jastaddad.ui.JastAddAdUI debugger = new jastaddad.ui.JastAddAdUI(rootNode);// Where rootNode is the Object of your AST:s root.
debugger.run();
```

Now your done! Next time you run your project on your source code, JastAddAdUI will start up after the parsing and scanning is done. 

NOTE! You don't need to run the UI of JastAddAd. All computations are done in a class JastAddAdAPI. If you want to get the data without an UI, run the following code:
```
jastaddad.api.JastAddAdAPI debugger = new jastaddad.api.JastAddAdAPI(rootNode); // Where rootNode is the Object of your AST:s root
debugger.run();
```

## Lib dependencies for Linux ##

When running the debugger on Linux the following library dependencies are required if any ToolKit errors are thrown.

- libgtk+-x11-2.0_0 - X11 backend of The GIMP ToolKit (GTK+).
- libgtk+2.0_0 - The GIMP ToolKit (GTK+), a library for creating GUIs.

## The Filter Configuration Language ##

To filter the presentation of your AST we have implemented a simple language that helps you with this. 
Check out the Wiki page for a small [tutorial](https://bitbucket.org/jastadd/jastadddebugger-exjobb/wiki/The%20Filter%20Configuration%20Language) in how you write and use that language.

Below is a standard template for the filter. This filter means that all nodes that are of class ASTNode or a child class of ASTNode should be shown (this means all nodes will be shown).

```
configs{
  use = f1;
}
filter f1{
  :ASTNode{
    show{}
    when{}
    style{}
  }
}

```