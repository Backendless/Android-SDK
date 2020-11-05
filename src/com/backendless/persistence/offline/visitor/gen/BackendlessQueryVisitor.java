// Generated from /home/max/Dev/Projects/IntellijProjects/TestVisitor/visitor/resources/BackendlessQuery.g4 by ANTLR 4.7.2
package com.backendless.persistence.offline.visitor.gen;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link BackendlessQueryParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface BackendlessQueryVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#select_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_statement(BackendlessQueryParser.Select_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#select_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect_list(BackendlessQueryParser.Select_listContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleSelectElem}
	 * labeled alternative in {@link BackendlessQueryParser#select_list_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleSelectElem(BackendlessQueryParser.SimpleSelectElemContext ctx);
	/**
	 * Visit a parse tree produced by the {@code aliasedSelectElem}
	 * labeled alternative in {@link BackendlessQueryParser#select_list_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAliasedSelectElem(BackendlessQueryParser.AliasedSelectElemContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#group_by_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroup_by_clause(BackendlessQueryParser.Group_by_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#having_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHaving_clause(BackendlessQueryParser.Having_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#sort_by_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSort_by_clause(BackendlessQueryParser.Sort_by_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#sort_by_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSort_by_elem(BackendlessQueryParser.Sort_by_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#limit_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLimit_clause(BackendlessQueryParser.Limit_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#function_call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_call(BackendlessQueryParser.Function_callContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#spatial_convert_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpatial_convert_function(BackendlessQueryParser.Spatial_convert_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#spatial_function_parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpatial_function_parameter(BackendlessQueryParser.Spatial_function_parameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#aswkb_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAswkb_function(BackendlessQueryParser.Aswkb_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#aswkt_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAswkt_function(BackendlessQueryParser.Aswkt_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#asgeojson_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsgeojson_function(BackendlessQueryParser.Asgeojson_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#spatial_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpatial_function(BackendlessQueryParser.Spatial_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#distance_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDistance_function(BackendlessQueryParser.Distance_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#distance_on_sphere}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDistance_on_sphere(BackendlessQueryParser.Distance_on_sphereContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ftUnitsFunction}
	 * labeled alternative in {@link BackendlessQueryParser#units_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFtUnitsFunction(BackendlessQueryParser.FtUnitsFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code kmUnitsFunction}
	 * labeled alternative in {@link BackendlessQueryParser#units_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKmUnitsFunction(BackendlessQueryParser.KmUnitsFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code miUnitsFunction}
	 * labeled alternative in {@link BackendlessQueryParser#units_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMiUnitsFunction(BackendlessQueryParser.MiUnitsFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ydUnitsFunction}
	 * labeled alternative in {@link BackendlessQueryParser#units_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitYdUnitsFunction(BackendlessQueryParser.YdUnitsFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code avgFunction}
	 * labeled alternative in {@link BackendlessQueryParser#aggregate_windowed_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAvgFunction(BackendlessQueryParser.AvgFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code maxFunction}
	 * labeled alternative in {@link BackendlessQueryParser#aggregate_windowed_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaxFunction(BackendlessQueryParser.MaxFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code minFunction}
	 * labeled alternative in {@link BackendlessQueryParser#aggregate_windowed_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinFunction(BackendlessQueryParser.MinFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code sumFunction}
	 * labeled alternative in {@link BackendlessQueryParser#aggregate_windowed_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSumFunction(BackendlessQueryParser.SumFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code countFunction}
	 * labeled alternative in {@link BackendlessQueryParser#aggregate_windowed_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCountFunction(BackendlessQueryParser.CountFunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code inCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInCondition(BackendlessQueryParser.InConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code equalCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualCondition(BackendlessQueryParser.EqualConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code orCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrCondition(BackendlessQueryParser.OrConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code lessThanCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLessThanCondition(BackendlessQueryParser.LessThanConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code andCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndCondition(BackendlessQueryParser.AndConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code isNullCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsNullCondition(BackendlessQueryParser.IsNullConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code notCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotCondition(BackendlessQueryParser.NotConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code likeCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLikeCondition(BackendlessQueryParser.LikeConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code isNotNullCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsNotNullCondition(BackendlessQueryParser.IsNotNullConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nestedCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNestedCondition(BackendlessQueryParser.NestedConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code notEqualCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotEqualCondition(BackendlessQueryParser.NotEqualConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code greaterThanCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGreaterThanCondition(BackendlessQueryParser.GreaterThanConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code greaterThanOrEqualCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGreaterThanOrEqualCondition(BackendlessQueryParser.GreaterThanOrEqualConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code lessThanOrEqualCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLessThanOrEqualCondition(BackendlessQueryParser.LessThanOrEqualConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code divisionExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDivisionExpressionAtom(BackendlessQueryParser.DivisionExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code moduloExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModuloExpressionAtom(BackendlessQueryParser.ModuloExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constantExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantExpressionAtom(BackendlessQueryParser.ConstantExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionCallExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCallExpressionAtom(BackendlessQueryParser.FunctionCallExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code subtractionExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubtractionExpressionAtom(BackendlessQueryParser.SubtractionExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code fullColumnNameExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFullColumnNameExpressionAtom(BackendlessQueryParser.FullColumnNameExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code multiplicationExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicationExpressionAtom(BackendlessQueryParser.MultiplicationExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unaryMinusExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryMinusExpressionAtom(BackendlessQueryParser.UnaryMinusExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code additionExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditionExpressionAtom(BackendlessQueryParser.AdditionExpressionAtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#full_column_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFull_column_name(BackendlessQueryParser.Full_column_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#single_column_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingle_column_name(BackendlessQueryParser.Single_column_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#relation_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelation_column(BackendlessQueryParser.Relation_columnContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#inverse_relation_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInverse_relation_column(BackendlessQueryParser.Inverse_relation_columnContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#unknown_function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnknown_function(BackendlessQueryParser.Unknown_functionContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlias(BackendlessQueryParser.AliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#table_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable_name(BackendlessQueryParser.Table_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#simple_column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple_column(BackendlessQueryParser.Simple_columnContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#unknown_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnknown_name(BackendlessQueryParser.Unknown_nameContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringConstant}
	 * labeled alternative in {@link BackendlessQueryParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringConstant(BackendlessQueryParser.StringConstantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numericConstant}
	 * labeled alternative in {@link BackendlessQueryParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericConstant(BackendlessQueryParser.NumericConstantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code booleanConstant}
	 * labeled alternative in {@link BackendlessQueryParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanConstant(BackendlessQueryParser.BooleanConstantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nullConstant}
	 * labeled alternative in {@link BackendlessQueryParser#constant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullConstant(BackendlessQueryParser.NullConstantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code trueBooleanConstant}
	 * labeled alternative in {@link BackendlessQueryParser#boolean_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrueBooleanConstant(BackendlessQueryParser.TrueBooleanConstantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code falseBooleanConstant}
	 * labeled alternative in {@link BackendlessQueryParser#boolean_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalseBooleanConstant(BackendlessQueryParser.FalseBooleanConstantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code decimalNumericConstant}
	 * labeled alternative in {@link BackendlessQueryParser#numeric_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimalNumericConstant(BackendlessQueryParser.DecimalNumericConstantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code floatNumericConstant}
	 * labeled alternative in {@link BackendlessQueryParser#numeric_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatNumericConstant(BackendlessQueryParser.FloatNumericConstantContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#string_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_literal(BackendlessQueryParser.String_literalContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#regular_id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegular_id(BackendlessQueryParser.Regular_idContext ctx);
	/**
	 * Visit a parse tree produced by {@link BackendlessQueryParser#keyword_as_id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyword_as_id(BackendlessQueryParser.Keyword_as_idContext ctx);
}