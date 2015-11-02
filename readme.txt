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

include{
  Stmt;
  div;
}

The node names present inside the "include" will not be filtered out by JastAddJ. In this case Stmt and div nodes will be visible. 

NOTE: 
This example will achive the exact same result:

include{
  Stmt {}
  div {}
}

------------ Global configurations
It is possible to add another section before the "include". This is "configs", and it can contain a number of different global 
configurations:

configs{
  ignoreInclude = true;
}
include{
  ...
}

In this example the ignoreInclude is set to true and therefore the "include" will be ignored, and all nodes will be visible.

IMPLEMENTED CONFIGS:
- ignoreInclude == [Bool]

------------ Filter nodes on attributes
It is possible to be more specific on which nodes should be part of the tree. 

include{
  Expr:Right {
    filter{
      5 > x;
      y == "Hello Filter";
      z == [1,2.5,61];
    }
  }
}

Ok, now this is what happends. Only Expr nodes with the name Right will be visible. The expr node must also contain all attributes
listed whithin filter {} and contain the specified values. 

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
It is possible for the UI part of JastAddJ to get information about how nodes should look by reading the configuration file. This
can be done by adding a style {} inside a node after filter {}, if it exist. See example: 

include{
  Stmt {
    filter{
      ...
    }
    style{
      background = #ff0000;
      stroke = "dotted";
    }
  }
}

Stmt nodes will now have red nodes with dotted strokes around them. 

------------ Big Example:
Here is just a big example that you should be able to understand after reading the above:

configs{
  ignoreInclude = false;
  thisConfigDoesNotExistButCanBeHereAnyway = true;
}
include{
  div {}
  IdDecl:ClassName {
    filter{ }
  }
  Mul{
    filter{
      x == 5;
      y == ["foo", "bar"];
    }
    style{ 
      background = #00ff00;
    }
  }
  BinExpr {
    style{ 
      background = #00ff00;
      stroke = "line";
    }
  }
  Add:Child;
}

------------ For the future:
- Add global attribute filter