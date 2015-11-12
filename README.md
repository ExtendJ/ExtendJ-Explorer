
Getting started
============

1. Clone the project..
2. Go to the directory where you cloned the project, with your terminal.
3. Run the bash file gradlew, with ./gradlew, this will create a .jar file which contains the debugger.
4. Add created Jar to your projects Library path, include all files in the .jar. Do not forget the .fxml and .css files. 
  For example if you are using an ant build script add something like this
  <zipfileset includes="**/*.*" src="/[path to debugger .jar]"/> to the build scripts jar target.
5. To run the debugger with your compiler, add the following code after the parsing is done and the AST tree is generated:
```
  new JastAddAd(topNode);
```
  Where topNode is the Object of the top node of your AST. Don't forget to import jastaddad.JastAddAd;
6. Now run your compiler on some source file!

Lib dependencies for Linux
------------ 
Add these if ToolKit errors are thrown when running the debugger.
libgtk+-x11-2.0_0 - X11 backend of The GIMP ToolKit (GTK+).
libgtk+2.0_0 - The GIMP ToolKit (GTK+), a library for creating GUIs.

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
  ignore-include = true;
}
-include{
  ...
}
```
In this example the ignore-include is set to true and therefore the "-include" block will be ignored.

IMPLEMENTED CONFIGS:

- ignore-filter == [Bool] // ignore all -filter parts in the code
- ignore-global == [Bool] // ignore everything inside -global. 
- ignore-include == [Bool // ignore everything inside -include. 

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
In the UI part of the JastAddAd, it is possible to display attributes directly in the graph. This is done by adding the -displayed-attributes
inside a node. The attributes listed there will be shown directly in the graph.
```
-include{
  Stmt {
    -filter{ ... }
    -style{ ... }
    -displayed-attributes{
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
    -displayed-attributes{ ... }
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
  ignore-include = false;
  ignore-filter = true;
}
-global{
      -filter{
	  x == 9;
      }
      -style{ 
	  node-color = #0000FF;
      }
      -displayed-attributes{
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
  	ignore-filter = true;
	ignore-include = false;
	ignore-global = false;
}
-global{
	-style{}
	-filter{getString == print;}
	-displayed-attributes{}
}
-include{
	Opt;
	List{
		-displayed-attributes{}
		-filter{ }
		-style{ node-color = #FF8000; }
	}
}
```