package com.backendless.persistence.offline.visitor;

import com.backendless.persistence.offline.visitor.bcknd.DateUtil;
import com.backendless.persistence.offline.visitor.gen.BackendlessQueryBaseVisitor;
import com.backendless.persistence.offline.visitor.gen.BackendlessQueryParser;
import com.backendless.persistence.offline.visitor.gen.BackendlessQueryParser.Expression_atomContext;
import com.backendless.persistence.offline.visitor.gen.BackendlessQueryParser.Select_list_elemContext;
import com.backendless.persistence.offline.visitor.gen.BackendlessQueryParser.Sort_by_elemContext;
import com.backendless.persistence.offline.visitor.res.*;

import java.util.*;

import static com.backendless.persistence.offline.visitor.res.AggregateOperator.*;
import static com.backendless.persistence.offline.visitor.res.ExpressionOperator.*;
import static com.backendless.persistence.offline.visitor.res.Operator.*;
import static com.backendless.persistence.offline.visitor.res.SortField.SortOrder.*;

public class AndroidBackendlessQueryVisitor extends BackendlessQueryBaseVisitor<QueryPart>
{
    private Table databaseTable;
    private Map<Table, Condition> joins;
    private Set<Field> fields;

    private final Map<String, Field> aliases = new HashMap<>();

    private boolean isInHavingClause = false;
    private boolean isInWhereClause = false;

    private final DateUtil dateUtil = new DateUtil();

    @Override
    public SelectQuery visitSelect_statement(BackendlessQueryParser.Select_statementContext ctx )
    {
        databaseTable = visitTable_name( ctx.table_name() );
        joins = new LinkedHashMap<>(); // order should be preserved for SQL joins
        fields = new HashSet<>();

        for (Select_list_elemContext context : ctx.select_list().select_list_elem()) {
            fields.add((Field) visit( context ));
        }

        final SelectQuery query = new SelectQuery();
        query.from = this.databaseTable;

        if( ctx.condition() != null )
        {
            isInWhereClause = true;
            query.condition = (Condition) visit( ctx.condition() );
            isInWhereClause = false;
        }

        if( ctx.group_by_clause() != null )
        {
            for (Expression_atomContext context : ctx.group_by_clause().expression_atom()) {
                query.groupBy.add((Field) visit( context ));
            }
        }

        if( ctx.having_clause() != null )
        {
            query.having =  (Condition) visit( ctx.having_clause() );
        }

        if( ctx.sort_by_clause() != null )
        {
            for (Sort_by_elemContext context : ctx.sort_by_clause().sort_by_elem()) {
                query.orderBy.add(visitSort_by_elem(context));
            }
        }

//        joins.forEach( ( table, condition ) -> query.addJoin( table, JoinType.LEFT_OUTER_JOIN, condition ) );

        if( ctx.limit_clause() != null )
        {
            query.limit =  Integer.parseInt( ctx.limit_clause().INT( 0 ).getText() );
            if( ctx.limit_clause().OFFSET() != null )
                query.offset = Integer.parseInt( ctx.limit_clause().INT( 1 ).getText() ) ;
        }

        query.selectList = ( fields );

        return query;
    }

    @Override
    public Field visitSimpleSelectElem( BackendlessQueryParser.SimpleSelectElemContext ctx )
    {
        return (Field) visit( ctx.expression_atom() );
    }

    @Override
    public Field visitAliasedSelectElem( BackendlessQueryParser.AliasedSelectElemContext ctx )
    {
        final Field field = (Field) visit( ctx.expression_atom() );
        final String alias = ctx.alias().getText();

        field.alias = alias;

        aliases.put( alias, field );

        return field;
    }

    @Override
    public SortField visitSort_by_elem( Sort_by_elemContext ctx )
    {
        Field field = (Field) visit( ctx.expression_atom() );

        if( ctx.ASC() != null )
            return new SortField(field, ASC);
        else if( ctx.DESC() != null )
            return new SortField(field, DESC);
        else
            return new SortField(field, ASC);
    }

    @Override
    public Table visitTable_name( BackendlessQueryParser.Table_nameContext ctx )
    {
        final String tableName = ctx.regular_id().getText();
        return new Table(tableName);
    }

    @Override
    public QueryPart visitHaving_clause( BackendlessQueryParser.Having_clauseContext ctx )
    {
        isInHavingClause = true;
        final QueryPart result = super.visitHaving_clause( ctx );
        isInHavingClause = false;
        return result;
    }

    // conditions

    @Override
    public Condition visitNotCondition( BackendlessQueryParser.NotConditionContext ctx )
    {
        final Condition condition = (Condition) visit( ctx.condition() );
        return new NotCondition( condition );
    }

    @Override
    public Condition visitAndCondition( BackendlessQueryParser.AndConditionContext ctx )
    {
        final Condition condition1 = (Condition) visit( ctx.condition( 0 ) );
        final Condition condition2 = (Condition) visit( ctx.condition( 1 ) );

        return new CombinedCondition(condition1, Operator.AND, condition2);
    }

    @Override
    public Condition visitOrCondition( BackendlessQueryParser.OrConditionContext ctx )
    {
        final Condition condition1 = (Condition) visit( ctx.condition( 0 ) );
        final Condition condition2 = (Condition) visit( ctx.condition( 1 ) );

        return new CombinedCondition(condition1, Operator.OR, condition2);
    }

    @Override
    public Condition visitNestedCondition( BackendlessQueryParser.NestedConditionContext ctx )
    {
        return (Condition) visit( ctx.condition() );
    }

    @Override
    public Condition visitIsNullCondition( BackendlessQueryParser.IsNullConditionContext ctx )
    {
        final Field field = (Field) visit( ctx.expression_atom() );

        return new NullCondition(field, true);
    }

    @Override
    public Condition visitIsNotNullCondition( BackendlessQueryParser.IsNotNullConditionContext ctx )
    {
        final Field field = (Field) visit( ctx.expression_atom() );

        return new NullCondition(field, false);
    }

    @Override
    public Condition visitLikeCondition( BackendlessQueryParser.LikeConditionContext ctx )
    {
        Field param1 = (Field) visit( ctx.expression_atom( 0 ) );
        Field param2 = (Field) visit( ctx.expression_atom( 1 ) );

        return new LikeCondition(param1, ctx.NOT() == null, param2);
    }

    // comparison conditions

    @Override
    public Condition visitEqualCondition( BackendlessQueryParser.EqualConditionContext ctx )
    {
        final Field field1 = (Field) visit( ctx.expression_atom( 0 ) );
        final Field field2 = (Field) visit( ctx.expression_atom( 1 ) );

        return new ComparisonCondition(field1, EQUAL, field2);
    }

    @Override
    public Condition visitNotEqualCondition( BackendlessQueryParser.NotEqualConditionContext ctx )
    {
        final Field field1 = (Field) visit( ctx.expression_atom( 0 ) );
        final Field field2 = (Field) visit( ctx.expression_atom( 1 ) );

        return new ComparisonCondition(field1, NOT_EQUAL, field2);
    }

    @Override
    public Condition visitGreaterThanOrEqualCondition( BackendlessQueryParser.GreaterThanOrEqualConditionContext ctx )
    {
        final Field field1 = (Field) visit( ctx.expression_atom( 0 ) );
        final Field field2 = (Field) visit( ctx.expression_atom( 1 ) );

        return new ComparisonCondition(field1, GREATER_THAN_OR_EQUAL, field2);
    }

    @Override
    public Condition visitGreaterThanCondition( BackendlessQueryParser.GreaterThanConditionContext ctx )
    {
        final Field field1 = (Field) visit( ctx.expression_atom( 0 ) );
        final Field field2 = (Field) visit( ctx.expression_atom( 1 ) );

        return new ComparisonCondition(field1, Operator.GREATER_THAN, field2);
    }

    @Override
    public Condition visitLessThanOrEqualCondition( BackendlessQueryParser.LessThanOrEqualConditionContext ctx )
    {
        final Field field1 = (Field) visit( ctx.expression_atom( 0 ) );
        final Field field2 = (Field) visit( ctx.expression_atom( 1 ) );

        return new ComparisonCondition(field1, LESS_THAN_OR_EQUAL, field2);
    }

    @Override
    public Condition visitLessThanCondition( BackendlessQueryParser.LessThanConditionContext ctx )
    {
        final Field field1 = (Field) visit( ctx.expression_atom( 0 ) );
        final Field field2 = (Field) visit( ctx.expression_atom( 1 ) );

        return new ComparisonCondition(field1, LESS_THAN, field2);
    }

    // expression atoms

    @Override
    public Field visitUnaryMinusExpressionAtom( BackendlessQueryParser.UnaryMinusExpressionAtomContext ctx )
    {
        final Field param = (Field) visit( ctx.expression_atom() );
        return new Expression(param, MULTIPLY, new Value<>(-1));
    }

    @Override
    public Field visitMultiplicationExpressionAtom(
            BackendlessQueryParser.MultiplicationExpressionAtomContext ctx )
    {
        final Field param1 = (Field) visit( ctx.expression_atom( 0 ) );
        final Field param2 = (Field) visit( ctx.expression_atom( 1 ) );

        return new Expression(param1, MULTIPLY, param2);
    }

    @Override
    public Field visitDivisionExpressionAtom( BackendlessQueryParser.DivisionExpressionAtomContext ctx )
    {
        final Field param1 = (Field) visit( ctx.expression_atom( 0 ) );
        final Field param2 = (Field) visit( ctx.expression_atom( 1 ) );

        return new Expression(param1, DIVIDE, param2);
    }

    @Override
    public Field visitModuloExpressionAtom( BackendlessQueryParser.ModuloExpressionAtomContext ctx )
    {
        final Field param1 = (Field) visit( ctx.expression_atom( 0 ) );
        final Field param2 = (Field) visit( ctx.expression_atom( 1 ) );

        return new Expression(param1, MODULO, param2);
    }

    @Override
    public Field visitAdditionExpressionAtom( BackendlessQueryParser.AdditionExpressionAtomContext ctx )
    {
        final Field param1 = (Field) visit( ctx.expression_atom( 0 ) );
        final Field param2 = (Field) visit( ctx.expression_atom( 1 ) );

        return new Expression(param1, ADD, param2);
    }

    @Override
    public Field visitSubtractionExpressionAtom(
            BackendlessQueryParser.SubtractionExpressionAtomContext ctx )
    {
        final Field param1 = (Field) visit( ctx.expression_atom( 0 ) );
        final Field param2 = (Field) visit( ctx.expression_atom( 1 ) );

        return new Expression(param1, SUBTRACT, param2);
    }

    // expression atom constants

    @Override
    public Value<String> visitStringConstant( BackendlessQueryParser.StringConstantContext ctx )
    {
        final String text = ctx.string_literal().getText();
        final String unquotedText = text.substring( 1, text.length() - 1 );
        final String unescapedText = text.replace( "''", "'" );

        // check whether this String is a date
        try {
            return new Value<>(dateUtil.getTimestampFromString(unquotedText));
        } catch (IllegalArgumentException e) {
            return new Value<>(unescapedText);
        }
    }

    @Override
    public Value<Long> visitDecimalNumericConstant( BackendlessQueryParser.DecimalNumericConstantContext ctx )
    {
        final long number = Long.parseLong( ctx.getText() );
        return new Value<>( number );
    }

    @Override
    public Value<Double> visitFloatNumericConstant( BackendlessQueryParser.FloatNumericConstantContext ctx )
    {
        final double number = Double.parseDouble( ctx.getText() );
        return new Value<>( number );
    }

    @Override
    public Value<Boolean> visitTrueBooleanConstant( BackendlessQueryParser.TrueBooleanConstantContext ctx )
    {
        return new Value<>( Boolean.TRUE );
    }

    @Override
    public Value<Boolean> visitFalseBooleanConstant( BackendlessQueryParser.FalseBooleanConstantContext ctx )
    {
        return new Value<>( Boolean.FALSE );
    }

    // function call visit methods
    @Override
    public QueryPart visitUnknown_function( BackendlessQueryParser.Unknown_functionContext ctx )
    {
        throw new IllegalArgumentException( "No such function " + ctx.unknown_name().regular_id().getText() );
    }

    @Override
    public AggregateFunction visitAvgFunction( BackendlessQueryParser.AvgFunctionContext ctx )
    {
        final Field field= (Field) visit( ctx.expression_atom() );
        return new AggregateFunction(AVG, field );
    }

    @Override
    public AggregateFunction visitMaxFunction( BackendlessQueryParser.MaxFunctionContext ctx )
    {
        final Field field= (Field) visit( ctx.expression_atom() );
        return new AggregateFunction(MAX, field );
    }

    @Override
    public AggregateFunction visitMinFunction( BackendlessQueryParser.MinFunctionContext ctx )
    {
        final Field field= (Field) visit( ctx.expression_atom() );
        return new AggregateFunction(MIN, field );
    }

    @Override
    public AggregateFunction visitSumFunction( BackendlessQueryParser.SumFunctionContext ctx )
    {
        final Field field= (Field) visit( ctx.expression_atom() );
        return new AggregateFunction(SUM, field );
    }

    @Override
    public AggregateFunction visitCountFunction( BackendlessQueryParser.CountFunctionContext ctx )
    {
        final Field field= (Field) visit( ctx.expression_atom() );
        return new AggregateFunction(COUNT, field );
    }

    @Override
    public Field visitSimple_column( BackendlessQueryParser.Simple_columnContext ctx )
    {
        final String columnName = ctx.regular_id().getText();
        return new Field(columnName);
    }


}
