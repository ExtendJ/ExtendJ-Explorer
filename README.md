# JastAddAd readme #
This readme has the following content

* Getting Started 
    - Get the project on you machine
    - Assemble
    - Create jar file
    - See if things are working
    - Running JastAddAd on your project
    - Lib dependencies for Linux
* Filtering the AST
    - Basic example
    - Configurations
    - Filter nodes on attributes
    - Styles
    - Attributes
    - Global
    - Hierarchy of -include and -global
    - Big Example
    - Standard configuration file

# Getting started #
## Get the project on you machine ##
Start of by cloning 
```
git clone https://pokhead@bitbucket.org/jastadd/jastadddebugger-exjobb.git
```
Then go to the directory where you cloned the project, with your terminal

## Assemble ##
In order to run JastAddAd you first need to assemble the project and then create a jar file.

There are two ways to assemble the project. Either just assemble:
```
./gradlew assemble
```
or assemble and then after run tests:
```
./gradlew build
```
OBS! The second one will run both normal and UI tests.

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

## Running JastAddAd on your project ##
There are a few things that must be done to run JastAddAd on your own project.

* Add created Jar to your projects Library path.
* Include all files in the .jar to your compiler script. Do not forget the .fxml and .css files. 
For example if you are using an ant build script add something like this to the build scripts jar target:
```
<zipfileset includes="\*\*/\*.\*" src="/[path to debugger .jar]"/> 
```
Now JastAddAd is ready to run! In your own java code (typically the same class as you run your parser) add the following code when the AST is created: 
```
new JastAddAdUI(rootNode); // Where rootNode is the Object of your AST:s root.
```

Now your done! Next time you run your project on your own source code, JastAddAdUI will start up after the parsing and scanning is done. 

NOTE! You don't need to run the UI of JastAddAd. All computations are done in a class JastAddAdAPI. If you want to get the data without an UI, run the following code:
```
new JastAddAdAPI(rootNode); // Where rootNode is the Object of your AST:s root.
```

## Lib dependencies for Linux ##

Add these if ToolKit errors are thrown when running the debugger.

- libgtk+-x11-2.0_0 - X11 backend of The GIMP ToolKit (GTK+).
- libgtk+2.0_0 - The GIMP ToolKit (GTK+), a library for creating GUIs.

Filtering the AST
------------ 
In order to view the nodes your interested in, a filter configuration language have been implemented into JastAddAd. 

Basic example
------------ 
```
-include{
  Stmt;
  Div;
}
```

The node names inside the "-include" block will not be filtered out by JastAddAd. 
In this case Stmt and Div nodes will be visible, the other nodes will be clustered together in to so called cluster nodes. 

NOTE: 
This example will achieve the exact same result, but with a slight different syntax:
```
-include{
  Stmt {}
  Div {}
}
```
Configurations
------------ 
It is possible to add another section before the "-include". This is "-configs", and it can contain a number of different
configurations:
```
-configs{
  include = true;
}
-include{
  ...
}
```
In this example the "include" is set to true and therefore the "-include" block will be ignored.

IMPLEMENTED CONFIGS:

- filter == [Bool] // ignore all -filter parts in the code
- global == [Bool] // ignore everything inside -global. 
- include == [Bool // ignore everything inside -include. 

Filter nodes on attributes
------------ 
It is possible to be more specific on which nodes should be part of the tree. 
NOTE: Only unparameterized attributes are currently supported.
```
-include{
  Expr:Right {
    -filter{
      5 > x;
      y == "Hello Filter";
      z == [1,2.5,61];
    }
  }
}
```
Ok, now this is what happens. Only Expr nodes with the name Right will be visible. The Expr node must also contain all attributes
listed within -filter {} and be set to the specified values. 

Each expression in the -filter block must contain one attribute name followed by either one of these:
- A Boolean-, Integer- or String value.
- An array of Integers or Strings.
- Another attribute name with the same type.
    
Values can use the following operands: ==, <, >, <=, >=. In the example above z must be one of the values specified in the array 
[1,2.5,61]. Strings and Integers can use the array.

Here is a list of the different combinations of filter expressions: (NOTE: that the attribute, x in this case, can be either on left
or right side of the expression)
```
x [==, !=, <, >, <=, >=] <Integer>;
x [==, !=, <, >, <=, >=] y;
x [==, !=] <String>;
x [==, !=] <Boolean>;
x [==, !=] Array(<String> | <Integer>);
```
Styles
------------ 
It is possible for the UI part of JastAddAd to get information about how nodes should look by reading the configuration file. This
can be done by adding a -style{} inside a node block. See example: 
```
-include{
  Stmt {
    -filter{
      ...
    }
    -style{
      node-color = #ff0000;
      border-style = "dashed";
    }
  }
}
```
NOTE: The "ignore-include" configuration will override these styles.
Stmt nodes will now have red nodes with dashed borders around them.

Supported styles so far
- node-color = #<6 digit color code>;
- node-shape = "small_circle" | "rounded_rectangle" | "rectangle";
- border-style = "dashed" | "line";

Attributes
------------ 
In the UI part of the JastAddAd, it is possible to display attributes directly in the graph. This is done by adding the -display-attributes
inside a node. The attributes listed there will be shown directly in the graph.
```
-include{
  Stmt {
    -filter{ ... }
    -style{ ... }
    -display-attributes{
      Value; 
      getInt; 
      NumInList;
    }
  }
}
```
If the attribute is a primitive value, it will be displayed as "name : value", but if it is a reference to another node, the reference will be 
pointed out in the graph as an edge.

Global
------------ 
Now sometimes you want to style your graph differently than the original UI, or maybe all nodes have an attribute that you like to filter on.
Instead of writing code for each node in your graph you can use the -global block. The -global block works exactly like a node inside the -include
block, but is applied on every node.
```
-configs{
  ...
}
-global{
    -filter{ ... }
    -style{ ... }
    -display-attributes{ ... }
}MENTED CONFIGS:
  
-include{
  ...
}
```
NOTE: The nodes will be filtered on attributes from the -global- and the -include block combined.

Hierarchy of -include and -global
------------ 
Ok, we have -global and -include, but what happens if they define different values for the same attribute? 
Answer: The attribute value inside the -include block has higher precedence than the value in the -global block.

In the example below, the attribute Value must be set to 2 in every node, except the Stmt node were the value must be 1.
```
-global{
    -filter{ 
	  Value == 2; 
    }
}
-include{
    Stmt {
      -filter{
	    Value == 1;
      }
    }
}
```
Big Example
------------ 
Here is a slightly bigger example that you should be able to understand after reading the above:
```
-configs{
  include = false;
  filter = true;
}
-global{
      -filter{
	  x == 9;
      }
      -style{ 
	  node-color = #0000FF;
      }
      -display-attributes{
	  x;
      }
}
-include{
    div {}
    IdDecl:ClassName {
      -filter{ }
    }
    Mul{
      -filter{
	    x == 5;
	    y == ["foo", "bar"];
      }
      -style{ 
	    node-color = #00ff00;
      }
    }
    BinExpr {
      style{ 
	    node-color = #ff0000;
	    border-style = "dashed";
      }
    }
    Add:Child;
}
```
Standard configuration file
------------ 
This can be copy and pasted in to a "filter.cfg" file.
```
-configs{
  	filter = true;
	include = false;
	global = false;
}
-global{
	-style{}
	-filter{getString == print;}
	-display-attributes{}
}
-include{
	Opt;
	List{
		-display-attributes{}
		-filter{ }
		-style{ node-color = #FF8000; }
	}
}
```