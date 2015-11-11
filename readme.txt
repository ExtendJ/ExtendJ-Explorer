=======================================
Getting started
=======================================
> Warning: These instructions may not be the best and up to date
1. Clone the project to you machine.
2. Open your terminal.
3. Go to the directory where you cloned the project.
4. write ./gradlew build.
5. Add created Jar to your projects Library path.
6. In your compilers Java code, add the following code after the parsing is done and the AST tree is generated:
     new JastAddAd(topNode);
   Where topNode is the Object to the top node of your AST.
7. Happy debugging!


=======================================
Lib deps
=======================================
libgtk+-x11-2.0_0 - X11 backend of The GIMP ToolKit (GTK+)​ ﻿     
libgtk+2.0_0 - The GIMP ToolKit (GTK+), a library for creating GUIs​ ﻿     

=======================================
Filtering the tree
=======================================
In order to view the nodes your intressted in, a filter configuration language have been implemented into JastAddAd. 

------------ Basic example: 
Here is an basic example:

-include{
  Stmt;
  div;
}

The node names present inside the "include" will not be filtered out by JastAddJ. In this case Stmt and div nodes will be visible. 

NOTE: 
This example will achive the exact same result:

-include{
  Stmt {}
  div {}
}

------------ Configurations
It is possible to add another section before the "-include". This is "-configs", and it can contain a number of different
configurations:

-configs{
  ignore-include = true;
}
-include{
  ...
}

In this example the ignore-include is set to true and therefore the "-include" will be ignored, and all nodes will be visible
and any styles defined inside include will be ignored.

IMPLEMENTED CONFIGS:]
- ignore-filter == [Bool] // ignore all -filter parts in the code
- ignore-global == [Bool] // ignore everything inside -global. 
- ignore-include == [Bool // ignore everything inside -include. 

------------ Filter nodes on attributes
It is possible to be more specific on which nodes should be part of the tree. 

-include{
  Expr:Right {
    -filter{
      5 > x;
      y == "Hello Filter";
      z == [1,2.5,61];
    }
  }
}

Ok, now this is what happends. Only Expr nodes with the name Right will be visible. The expr node must also contain all attributes
listed whithin filter {} and be set to the specified values. 

Each filter expression must contain one or two attribute names, and then a Boolean-, Integer-, String- or array value. Integer 
values can use the following operands: ==, <, >, <=, >=. z in this example must be one of the values specified in the array 
[1,2.5,61]. Strings and Integers can use the array.

Here is a list of the different combinations of filter expressions: (Note that the attribute, x in this case, can be either on left
or right side of the expression)

x [==, !=, <, >, <=, >=] <Integer>;
x [==, !=, <, >, <=, >=] y;
x [==, !=] <String>;
x [==, !=] <Boolean>;
x [==, !=] Array(<String> | <Integer>);

------------ Styles
It is possible for the UI part of JastAddAd to get information about how nodes should look by reading the configuration file. This
can be done by adding a -style{} inside a node after -filter{}, if it exist. See example: 

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

Stmt nodes will now have red nodes with dashed borders around them.

Supported styles so far
node-color    =     #<6 digit color code>;
node-shape    =     "small_circle" | "rounded_rectangle" | "rectangle";
border-style  =     "dashed" | "line";

------------ Attributes
In the UI part of the JastAddAd, it is possible to display attributes directly in the graph. This is done by adding the -displayed-attributes
inside a node. the attributes listed there will be shown directly in the graph.

-include{
  Stmt {
    -filter{ ... }
    -style{ ... }
    -displayed-attributes{
      Value(); 
      getInt(); 
      NumInList();
    }
  }
}

If the attribute is a primitive value, it will be displayed as Value = 1, but if it is a reference to another node, the reference will be 
pointed out in the graph as an edge.

------------ Global
Now sometimes you want to style your graph differently than the original UI, or maybe all nodes have an attribute that you like to filter on.
Instead of writing a lot of code for each node in your graph use the -global block. The global works exactly like a node inside the -include
block, but is applyed on every node.

-configs{
  ...
}
-global{
    -filter{ ... }
    -style{ ... }
    -displayed-attributes{ ... }
}
-include{
  ...
}

------------ Hierarchy of -include and -global
Ok, we have -global and -include, but what happends if they define different values for the same attribute? 
Answare: The -include is always stronger.

In the example below, the attribute Value must be set to 2 in every node, except the Stmt node were the value must be 1.

-global{
    -filter{ 
	Value() == 2; 
    }
}
-include{
    Stmt {
      -filter{
	  Value() == 1;
      }
    }
}

------------ Big Example:
Here is just a big example that you should be able to understand after reading the above:

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