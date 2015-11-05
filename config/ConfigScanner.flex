package AST; // The generated parser will belong to package AST

import AST.ConfigParser.Terminals;
import AST.ConfigParser.SyntaxError;

%%

// define the signature for the generated scanner
%public
%final
%class ConfigScanner
%extends beaver.Scanner


// the interface between the scanner and the parser is the nextToken() method
%type beaver.Symbol
%function nextToken

// store line and column information in the tokens
%line
%column

// this code will be inlined in the body of the generated scanner class
%{
  private beaver.Symbol sym(short id) {
    return new beaver.Symbol(id, yyline + 1, yycolumn + 1, yylength(), yytext());
  }
%}

// macros
WhiteSpace = [ ] | \t | \f | \n | \r
Bool = (false|true)
ID = [a-zA-Z][\-a-zA-Z0-9]*
IDCONFIG = \-[a-zA-Z\-][\-a-zA-Z0-9]*
Numeral = [0-9]+
Color = #[0-9a-fA-F]{6}
String = \"[^\"]*\"
Comment = \/\*[^]*\*\/
%%

// discard whitespace information
{WhiteSpace}  { }
{Comment} {   }
//token definitions
"-filter"        { return sym(Terminals.FILTER); }
"-include"       { return sym(Terminals.INCLUDE); }
"-global"       { return sym(Terminals.GLOBAL); }
"="        		{ return sym(Terminals.ASSIGNMENT); }
"<"        		{ return sym(Terminals.LT); }
">"             { return sym(Terminals.GT); }
"=="            { return sym(Terminals.EQ); }
">="            { return sym(Terminals.GEQ); }
"<="            { return sym(Terminals.LEQ); }
"!="            { return sym(Terminals.NEQ); }
":"           	{ return sym(Terminals.COL); }
";"           	{ return sym(Terminals.SEMI); }
","         	{ return sym(Terminals.COMMA); }
"["       	    { return sym(Terminals.LSQBRACKET); }
"]"       	    { return sym(Terminals.RSQBRACKET); }
"{"       	    { return sym(Terminals.LBRACKET); }
"}"       	    { return sym(Terminals.RBRACKET); }
{Color}  	    { return sym(Terminals.COLOR); }
{Bool}  	    { return sym(Terminals.BOOL); }
{ID}        	{ return sym(Terminals.ID); }
{IDCONFIG}      { return sym(Terminals.IDCONFIG); }
{Numeral}  	    { return sym(Terminals.NUMERAL); }
{String}  	    { return sym(Terminals.STRING); }
<<EOF>>     	{ return sym(Terminals.EOF); }

/* error fallback */
[^]           { throw new SyntaxError("Illegal character <"+yytext()+">"); }
