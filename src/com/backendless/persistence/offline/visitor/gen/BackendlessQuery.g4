grammar BackendlessQuery;

//@header{
//package com.backendless.query.gen;
//}

select_statement
    : table_name '[' condition? ']'
        '.'
        select_list
        group_by_clause?
        having_clause?
        sort_by_clause?
        limit_clause?
    ;

select_list
    : '{' select_list_elem (',' select_list_elem)* '}'
    | select_list_elem
    ;

select_list_elem
    : expression_atom                                           #simpleSelectElem
    | expression_atom AS alias                                  #aliasedSelectElem
    ;

group_by_clause
    : GROUP BY expression_atom (',' expression_atom)*
    ;

having_clause
    : HAVING condition
    ;

sort_by_clause
    : SORT BY sort_by_elem (',' sort_by_elem)*
    ;

sort_by_elem
    : expression_atom (ASC | DESC)?
    ;

limit_clause
    : LIMIT INT (OFFSET INT)?
    ;

function_call
    : aggregate_windowed_function
    | units_function
    | spatial_convert_function
    | spatial_function
    | unknown_function
    ;

spatial_convert_function
    : aswkb_function
    | aswkt_function
    | asgeojson_function
    ;

spatial_function_parameter
    : simple_column
    | constant
    ;

aswkb_function
    : AsWKB '(' spatial_function_parameter ')'
    ;

aswkt_function
    : AsWKT '(' spatial_function_parameter ')'
    ;

asgeojson_function
    : AsGeoJSON '(' spatial_function_parameter ')'
    ;

spatial_function
    : distance_function
    | distance_on_sphere
    ;

distance_function
    : DISTANCE '(' spatial_function_parameter ',' spatial_function_parameter ')'
    | DISTANCE '(' expression_atom ',' expression_atom ',' expression_atom ',' expression_atom ')'
    ;

distance_on_sphere
    : DISTANCE_ON_SPHERE '(' spatial_function_parameter ',' spatial_function_parameter (',' expression_atom)? ')'
    ;

units_function
    : FT '(' numeric_literal ')'                                #ftUnitsFunction
    | KM '(' numeric_literal ')'                                #kmUnitsFunction
    | MI '(' numeric_literal ')'                                #miUnitsFunction
    | YD '(' numeric_literal ')'                                #ydUnitsFunction
    ;

aggregate_windowed_function
    : AVG '(' expression_atom ')'                               #avgFunction
    | MAX '(' expression_atom ')'                               #maxFunction
    | MIN '(' expression_atom ')'                               #minFunction
    | SUM '(' expression_atom ')'                               #sumFunction
    | COUNT '(' expression_atom ')'                             #countFunction
    ;

condition
    : (NOT | '!') condition                                     #notCondition
    | condition (AND | '&&') condition                          #andCondition
    | condition (OR  | '||') condition                          #orCondition
    | '(' condition ')'                                         #nestedCondition
    | expression_atom NOT? IN
        '(' (select_statement | expression_atom)
            (','(select_statement | expression_atom))* ')'      #inCondition
    | expression_atom IS NULL                                   #isNullCondition
    | expression_atom IS NOT NULL                               #isNotNullCondition
    | expression_atom NOT? LIKE expression_atom                 #likeCondition
    | expression_atom ('=' | AT) expression_atom                #equalCondition
    | expression_atom ('!=' | '<>') expression_atom             #notEqualCondition
    | expression_atom ('>=' | AT OR AFTER) expression_atom      #greaterThanOrEqualCondition
    | expression_atom ('>' | AFTER) expression_atom             #greaterThanCondition
    | expression_atom ('<=' | AT OR BEFORE) expression_atom     #lessThanOrEqualCondition
    | expression_atom ('<' | BEFORE) expression_atom            #lessThanCondition
    ;

expression_atom
    : constant                                                  #constantExpressionAtom
    | full_column_name                                          #fullColumnNameExpressionAtom
    | function_call                                             #functionCallExpressionAtom
    | '-' expression_atom                                       #unaryMinusExpressionAtom
    | expression_atom '*' expression_atom                       #multiplicationExpressionAtom
    | expression_atom '/' expression_atom                       #divisionExpressionAtom
    | expression_atom '%' expression_atom                       #moduloExpressionAtom
    | expression_atom '+' expression_atom                       #additionExpressionAtom
    | expression_atom '-' expression_atom                       #subtractionExpressionAtom
    ;

full_column_name
    : single_column_name
    | relation_column
    ;

single_column_name
    : simple_column
    | inverse_relation_column
    ;

relation_column
    : single_column_name '.' (relation_column | single_column_name)
    ;

inverse_relation_column
    : table_name '[' full_column_name ']' '.' full_column_name
    ;

unknown_function
    : unknown_name '(' expression_atom ')'
    ;

alias: regular_id;
table_name: regular_id;
simple_column: regular_id;
unknown_name: regular_id;

constant
    : string_literal                                            #stringConstant
    | numeric_literal                                           #numericConstant
    | boolean_literal                                           #booleanConstant
    | NULL                                                      #nullConstant
    ;

boolean_literal
    : TRUE                                                      #trueBooleanConstant
    | FALSE                                                     #falseBooleanConstant
    ;

numeric_literal
    : INT                                                       #decimalNumericConstant
    | INT? '.' INT                                              #floatNumericConstant
    ;

string_literal
    : CHAR_STRING
    ;

regular_id
    : ID
    // in order to make it possible to use keywords as identifiers (e.g. column or table names)
    // we need to specify them here separately, otherwise antlr4 parses them as keywords resulting in invalid query
    // see https://stackoverflow.com/a/41427981/1813669
    | keyword_as_id
    ;

keyword_as_id
    : AFTER
    | AND
    | AT
    | AS
    | ASC
    | AVG
    | BEFORE
    | BY
    | COUNT
    | DESC
    | DISTANCE
    | DISTANCE_ON_SPHERE
    | DIV
    | ESCAPE
    | FALSE
    | FT
    | GROUP
    | HAVING
    | IN
    | IS
    | KM
    | LIMIT
    | LIKE
    | MAX
    | MI
    | MIN
    | MOD
    | NOT
    | NULL
    | OFFSET
    | OR
    | SELECT
    | SORT
    | SUM
    | TRUE
    | YD
    ;

AFTER       :   A F T E R       ;
AND         :   A N D           ;
AT          :   A T             ;
AS          :   A S             ;
ASC         :   A S C           ;
AVG         :   A V G           ;
BEFORE      :   B E F O R E     ;
BY          :   B Y             ;
COUNT       :   C O U N T       ;
DESC        :   D E S C         ;
DISTANCE    :   D I S T A N C E ;
DISTANCE_ON_SPHERE  :   D I S T A N C E '_' O N '_' S P H E R E  ;
DIV         :   D I V           ;
ESCAPE      :   E S C A P E     ;
FALSE       :   F A L S E       ;
FT          :   F T             ;
GROUP       :   G R O U P       ;
HAVING      :   H A V I N G     ;
IN          :   I N             ;
IS          :   I S             ;
KM          :   K M             ;
LIMIT       :   L I M I T       ;
LIKE        :   L I K E         ;
MAX         :   M A X           ;
MI          :   M I             ;
MIN         :   M I N           ;
MOD         :   M O D           ;
NOT         :   N O T           ;
NULL        :   N U L L         ;
OFFSET      :   O F F S E T     ;
OR          :   O R             ;
SELECT      :   S E L E C T     ;
SORT        :   S O R T         ;
SUM         :   S U M           ;
TRUE        :   T R U E         ;
YD          :   Y D             ;
AsWKB       :   A S W K B       ;
AsWKT       :   A S W K T       ;
AsGeoJSON   :   A S G E O J S O N   ;

ID: (LETTER | '_') (LETTER | DIGIT | '$' | '_' | '#')*;

INT: DIGIT+;
CHAR_STRING: '\'' (~('\'' | '\r' | '\n') | '\'' '\'' | NEWLINE)* '\'';

WS: [ \t\r\n]+ -> skip;

fragment DIGIT      :   [0-9]                   ;
fragment LETTER     :   [a-zA-Z]                ;
fragment NEWLINE    :   '\r'? '\n'              ;

fragment A:     [aA];
fragment B:     [bB];
fragment C:     [cC];
fragment D:     [dD];
fragment E:     [eE];
fragment F:     [fF];
fragment G:     [gG];
fragment H:     [hH];
fragment I:     [iI];
fragment J:     [jJ];
fragment K:     [kK];
fragment L:     [lL];
fragment M:     [mM];
fragment N:     [nN];
fragment O:     [oO];
fragment P:     [pP];
fragment Q:     [qQ];
fragment R:     [rR];
fragment S:     [sS];
fragment T:     [tT];
fragment U:     [uU];
fragment V:     [vV];
fragment W:     [wW];
fragment X:     [xX];
fragment Y:     [yY];
fragment Z:     [zZ];