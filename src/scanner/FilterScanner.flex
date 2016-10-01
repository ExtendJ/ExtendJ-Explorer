package org.jastadd.drast.filterlang;

import org.jastadd.drast.filterlang.FilterParser.Terminals;
import org.jastadd.drast.filterlang.FilterParser.SyntaxError;

%%

%public
%final
%class FilterScanner
%extends beaver.Scanner

%type beaver.Symbol
%function nextToken

%line
%column

%{
  private beaver.Symbol sym(short id) {
    return new beaver.Symbol(id, yyline + 1, yycolumn + 1, yylength(), yytext());
  }
%}

WhiteSpace = [ ] | \t | \f | \n | \r
ID = [a-zA-Z][\-a-zA-Z0-9]*
Numeral = [0-9]+
Float = {Numeral} \. {Numeral}
String = \"[^\"]*\"
CommentStandard = "/*" [^*] ~"*/" | "/*" "*"+ "/"
CommentEndOfLine = \/\/[^\n]*

%%

{WhiteSpace}  { }
{CommentStandard} {   }
{CommentEndOfLine} {   }

"include"       { return sym(Terminals.INCLUDE); }
"exclude"       { return sym(Terminals.EXCLUDE); }
"set"           { return sym(Terminals.SET); }
"when"          { return sym(Terminals.WHEN); }
"show"          { return sym(Terminals.SHOW); }
"style"         { return sym(Terminals.STYLE); }
"true"          { return sym(Terminals.TRUE); }
"false"         { return sym(Terminals.FALSE); }
"&&"            { return sym(Terminals.AND); }
"||"            { return sym(Terminals.OR); }
"=="            { return sym(Terminals.EQ); }
"!="            { return sym(Terminals.NOTEQ); }
">"             { return sym(Terminals.GT); }
">="            { return sym(Terminals.GTEQ); }
"<"             { return sym(Terminals.LT); }
"<="            { return sym(Terminals.LTEQ); }
"!"             { return sym(Terminals.NOT); }
"("             { return sym(Terminals.LPAREN); }
")"             { return sym(Terminals.RPAREN); }
"{"             { return sym(Terminals.LBRACE); }
"}"             { return sym(Terminals.RBRACE); }
":"             { return sym(Terminals.COLON); }
","             { return sym(Terminals.COMMA); }
"*"             { return sym(Terminals.STAR); }
"**"            { return sym(Terminals.DOUBLESTAR); }
{ID}            { return sym(Terminals.ID); }
{Numeral}       { return sym(Terminals.INT); }
{Float}         { return sym(Terminals.FLOAT); }
{String}        { return sym(Terminals.STRING); }
<<EOF>>         { return sym(Terminals.EOF); }

// Error token:
[^]             { throw new SyntaxError("Illegal character <"+yytext()+">"); }
