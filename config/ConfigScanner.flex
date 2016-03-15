package configAST; // The generated parser will belong to package configAST

import configAST.ConfigParser.Terminals;
import configAST.ConfigParser.SyntaxError;

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
OnOff = (on|off)
ID = [a-zA-Z][\-a-zA-Z0-9]*
LANG_ID = :[a-zA-Z][\-a-zA-Z0-9]*
Numeral = [0-9]+
Color = #[0-9a-fA-F]{6}
String = \"[^\"]*\"
CommentStandard = "/*" [^*] ~"*/" | "/*" "*"+ "/"
CommentEndOfLine = \/\/[^\n]*
%%

// discard whitespace information
{WhiteSpace}  { }
{CommentStandard} {   }
{CommentEndOfLine} {   }
//token definitions
{LANG_ID}        	{ return sym(Terminals.LANG_ID); }
"configs"       { return sym(Terminals.CONFIGS); }
"filter"       { return sym(Terminals.FILTER); }
"when"       { return sym(Terminals.WHEN); }
"subtree"       { return sym(Terminals.SUBTREE); }
"style"       { return sym(Terminals.STYLE); }
"show"       { return sym(Terminals.SHOW); }
"="        		{ return sym(Terminals.ASSIGNMENT); }
"<"        		{ return sym(Terminals.LT); }
">"             { return sym(Terminals.GT); }
"=="            { return sym(Terminals.EQ); }
">="            { return sym(Terminals.GEQ); }
"<="            { return sym(Terminals.LEQ); }
"!="            { return sym(Terminals.NEQ); }
";"           	{ return sym(Terminals.SEMI); }
","         	{ return sym(Terminals.COMMA); }
"["       	    { return sym(Terminals.LSQBRACKET); }
"]"       	    { return sym(Terminals.RSQBRACKET); }
"{"       	    { return sym(Terminals.LBRACKET); }
"}"       	    { return sym(Terminals.RBRACKET); }
"use"  	        { return sym(Terminals.USE); }
"shown"       { return sym(Terminals.SHOWN); }
"child"  	    { return sym(Terminals.CHILD); }
"parent"  	    { return sym(Terminals.PARENT); }
"of"  	        { return sym(Terminals.OF); }
"not"  	        { return sym(Terminals.NOT); }
"in"  	        { return sym(Terminals.IN); }
{Color}  	    { return sym(Terminals.COLOR); }
{Bool}  	    { return sym(Terminals.BOOL); }
{OnOff}	        { return sym(Terminals.ONOFF); }
{ID}        	{ return sym(Terminals.ID); }
{Numeral}  	    { return sym(Terminals.NUMERAL); }
{String}  	    { return sym(Terminals.STRING); }
<<EOF>>     	{ return sym(Terminals.EOF); }

/* error fallback */
[^]           { throw new SyntaxError("Illegal character <"+yytext()+">"); }
