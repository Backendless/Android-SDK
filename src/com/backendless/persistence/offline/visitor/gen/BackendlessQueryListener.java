// Generated from /home/max/Dev/Projects/IntellijProjects/TestVisitor/visitor/resources/BackendlessQuery.g4 by ANTLR 4.7.2
package com.backendless.persistence.offline.visitor.gen;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link BackendlessQueryParser}.
 */
public interface BackendlessQueryListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#select_statement}.
	 * @param ctx the parse tree
	 */
	void enterSelect_statement(BackendlessQueryParser.Select_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#select_statement}.
	 * @param ctx the parse tree
	 */
	void exitSelect_statement(BackendlessQueryParser.Select_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#select_list}.
	 * @param ctx the parse tree
	 */
	void enterSelect_list(BackendlessQueryParser.Select_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#select_list}.
	 * @param ctx the parse tree
	 */
	void exitSelect_list(BackendlessQueryParser.Select_listContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleSelectElem}
	 * labeled alternative in {@link BackendlessQueryParser#select_list_elem}.
	 * @param ctx the parse tree
	 */
	void enterSimpleSelectElem(BackendlessQueryParser.SimpleSelectElemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleSelectElem}
	 * labeled alternative in {@link BackendlessQueryParser#select_list_elem}.
	 * @param ctx the parse tree
	 */
	void exitSimpleSelectElem(BackendlessQueryParser.SimpleSelectElemContext ctx);
	/**
	 * Enter a parse tree produced by the {@code aliasedSelectElem}
	 * labeled alternative in {@link BackendlessQueryParser#select_list_elem}.
	 * @param ctx the parse tree
	 */
	void enterAliasedSelectElem(BackendlessQueryParser.AliasedSelectElemContext ctx);
	/**
	 * Exit a parse tree produced by the {@code aliasedSelectElem}
	 * labeled alternative in {@link BackendlessQueryParser#select_list_elem}.
	 * @param ctx the parse tree
	 */
	void exitAliasedSelectElem(BackendlessQueryParser.AliasedSelectElemContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#group_by_clause}.
	 * @param ctx the parse tree
	 */
	void enterGroup_by_clause(BackendlessQueryParser.Group_by_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#group_by_clause}.
	 * @param ctx the parse tree
	 */
	void exitGroup_by_clause(BackendlessQueryParser.Group_by_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#having_clause}.
	 * @param ctx the parse tree
	 */
	void enterHaving_clause(BackendlessQueryParser.Having_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#having_clause}.
	 * @param ctx the parse tree
	 */
	void exitHaving_clause(BackendlessQueryParser.Having_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#sort_by_clause}.
	 * @param ctx the parse tree
	 */
	void enterSort_by_clause(BackendlessQueryParser.Sort_by_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#sort_by_clause}.
	 * @param ctx the parse tree
	 */
	void exitSort_by_clause(BackendlessQueryParser.Sort_by_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#sort_by_elem}.
	 * @param ctx the parse tree
	 */
	void enterSort_by_elem(BackendlessQueryParser.Sort_by_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#sort_by_elem}.
	 * @param ctx the parse tree
	 */
	void exitSort_by_elem(BackendlessQueryParser.Sort_by_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#limit_clause}.
	 * @param ctx the parse tree
	 */
	void enterLimit_clause(BackendlessQueryParser.Limit_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#limit_clause}.
	 * @param ctx the parse tree
	 */
	void exitLimit_clause(BackendlessQueryParser.Limit_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#function_call}.
	 * @param ctx the parse tree
	 */
	void enterFunction_call(BackendlessQueryParser.Function_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#function_call}.
	 * @param ctx the parse tree
	 */
	void exitFunction_call(BackendlessQueryParser.Function_callContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#spatial_convert_function}.
	 * @param ctx the parse tree
	 */
	void enterSpatial_convert_function(BackendlessQueryParser.Spatial_convert_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#spatial_convert_function}.
	 * @param ctx the parse tree
	 */
	void exitSpatial_convert_function(BackendlessQueryParser.Spatial_convert_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#spatial_function_parameter}.
	 * @param ctx the parse tree
	 */
	void enterSpatial_function_parameter(BackendlessQueryParser.Spatial_function_parameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#spatial_function_parameter}.
	 * @param ctx the parse tree
	 */
	void exitSpatial_function_parameter(BackendlessQueryParser.Spatial_function_parameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#aswkb_function}.
	 * @param ctx the parse tree
	 */
	void enterAswkb_function(BackendlessQueryParser.Aswkb_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#aswkb_function}.
	 * @param ctx the parse tree
	 */
	void exitAswkb_function(BackendlessQueryParser.Aswkb_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#aswkt_function}.
	 * @param ctx the parse tree
	 */
	void enterAswkt_function(BackendlessQueryParser.Aswkt_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#aswkt_function}.
	 * @param ctx the parse tree
	 */
	void exitAswkt_function(BackendlessQueryParser.Aswkt_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#asgeojson_function}.
	 * @param ctx the parse tree
	 */
	void enterAsgeojson_function(BackendlessQueryParser.Asgeojson_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#asgeojson_function}.
	 * @param ctx the parse tree
	 */
	void exitAsgeojson_function(BackendlessQueryParser.Asgeojson_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#spatial_function}.
	 * @param ctx the parse tree
	 */
	void enterSpatial_function(BackendlessQueryParser.Spatial_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#spatial_function}.
	 * @param ctx the parse tree
	 */
	void exitSpatial_function(BackendlessQueryParser.Spatial_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#distance_function}.
	 * @param ctx the parse tree
	 */
	void enterDistance_function(BackendlessQueryParser.Distance_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#distance_function}.
	 * @param ctx the parse tree
	 */
	void exitDistance_function(BackendlessQueryParser.Distance_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#distance_on_sphere}.
	 * @param ctx the parse tree
	 */
	void enterDistance_on_sphere(BackendlessQueryParser.Distance_on_sphereContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#distance_on_sphere}.
	 * @param ctx the parse tree
	 */
	void exitDistance_on_sphere(BackendlessQueryParser.Distance_on_sphereContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ftUnitsFunction}
	 * labeled alternative in {@link BackendlessQueryParser#units_function}.
	 * @param ctx the parse tree
	 */
	void enterFtUnitsFunction(BackendlessQueryParser.FtUnitsFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ftUnitsFunction}
	 * labeled alternative in {@link BackendlessQueryParser#units_function}.
	 * @param ctx the parse tree
	 */
	void exitFtUnitsFunction(BackendlessQueryParser.FtUnitsFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code kmUnitsFunction}
	 * labeled alternative in {@link BackendlessQueryParser#units_function}.
	 * @param ctx the parse tree
	 */
	void enterKmUnitsFunction(BackendlessQueryParser.KmUnitsFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code kmUnitsFunction}
	 * labeled alternative in {@link BackendlessQueryParser#units_function}.
	 * @param ctx the parse tree
	 */
	void exitKmUnitsFunction(BackendlessQueryParser.KmUnitsFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code miUnitsFunction}
	 * labeled alternative in {@link BackendlessQueryParser#units_function}.
	 * @param ctx the parse tree
	 */
	void enterMiUnitsFunction(BackendlessQueryParser.MiUnitsFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code miUnitsFunction}
	 * labeled alternative in {@link BackendlessQueryParser#units_function}.
	 * @param ctx the parse tree
	 */
	void exitMiUnitsFunction(BackendlessQueryParser.MiUnitsFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ydUnitsFunction}
	 * labeled alternative in {@link BackendlessQueryParser#units_function}.
	 * @param ctx the parse tree
	 */
	void enterYdUnitsFunction(BackendlessQueryParser.YdUnitsFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ydUnitsFunction}
	 * labeled alternative in {@link BackendlessQueryParser#units_function}.
	 * @param ctx the parse tree
	 */
	void exitYdUnitsFunction(BackendlessQueryParser.YdUnitsFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code avgFunction}
	 * labeled alternative in {@link BackendlessQueryParser#aggregate_windowed_function}.
	 * @param ctx the parse tree
	 */
	void enterAvgFunction(BackendlessQueryParser.AvgFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code avgFunction}
	 * labeled alternative in {@link BackendlessQueryParser#aggregate_windowed_function}.
	 * @param ctx the parse tree
	 */
	void exitAvgFunction(BackendlessQueryParser.AvgFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code maxFunction}
	 * labeled alternative in {@link BackendlessQueryParser#aggregate_windowed_function}.
	 * @param ctx the parse tree
	 */
	void enterMaxFunction(BackendlessQueryParser.MaxFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code maxFunction}
	 * labeled alternative in {@link BackendlessQueryParser#aggregate_windowed_function}.
	 * @param ctx the parse tree
	 */
	void exitMaxFunction(BackendlessQueryParser.MaxFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code minFunction}
	 * labeled alternative in {@link BackendlessQueryParser#aggregate_windowed_function}.
	 * @param ctx the parse tree
	 */
	void enterMinFunction(BackendlessQueryParser.MinFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code minFunction}
	 * labeled alternative in {@link BackendlessQueryParser#aggregate_windowed_function}.
	 * @param ctx the parse tree
	 */
	void exitMinFunction(BackendlessQueryParser.MinFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code sumFunction}
	 * labeled alternative in {@link BackendlessQueryParser#aggregate_windowed_function}.
	 * @param ctx the parse tree
	 */
	void enterSumFunction(BackendlessQueryParser.SumFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code sumFunction}
	 * labeled alternative in {@link BackendlessQueryParser#aggregate_windowed_function}.
	 * @param ctx the parse tree
	 */
	void exitSumFunction(BackendlessQueryParser.SumFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code countFunction}
	 * labeled alternative in {@link BackendlessQueryParser#aggregate_windowed_function}.
	 * @param ctx the parse tree
	 */
	void enterCountFunction(BackendlessQueryParser.CountFunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code countFunction}
	 * labeled alternative in {@link BackendlessQueryParser#aggregate_windowed_function}.
	 * @param ctx the parse tree
	 */
	void exitCountFunction(BackendlessQueryParser.CountFunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code inCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterInCondition(BackendlessQueryParser.InConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code inCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitInCondition(BackendlessQueryParser.InConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code equalCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterEqualCondition(BackendlessQueryParser.EqualConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code equalCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitEqualCondition(BackendlessQueryParser.EqualConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code orCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterOrCondition(BackendlessQueryParser.OrConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code orCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitOrCondition(BackendlessQueryParser.OrConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lessThanCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterLessThanCondition(BackendlessQueryParser.LessThanConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lessThanCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitLessThanCondition(BackendlessQueryParser.LessThanConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code andCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterAndCondition(BackendlessQueryParser.AndConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code andCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitAndCondition(BackendlessQueryParser.AndConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code isNullCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterIsNullCondition(BackendlessQueryParser.IsNullConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code isNullCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitIsNullCondition(BackendlessQueryParser.IsNullConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code notCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterNotCondition(BackendlessQueryParser.NotConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code notCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitNotCondition(BackendlessQueryParser.NotConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code likeCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterLikeCondition(BackendlessQueryParser.LikeConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code likeCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitLikeCondition(BackendlessQueryParser.LikeConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code isNotNullCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterIsNotNullCondition(BackendlessQueryParser.IsNotNullConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code isNotNullCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitIsNotNullCondition(BackendlessQueryParser.IsNotNullConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nestedCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterNestedCondition(BackendlessQueryParser.NestedConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nestedCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitNestedCondition(BackendlessQueryParser.NestedConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code notEqualCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterNotEqualCondition(BackendlessQueryParser.NotEqualConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code notEqualCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitNotEqualCondition(BackendlessQueryParser.NotEqualConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code greaterThanCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterGreaterThanCondition(BackendlessQueryParser.GreaterThanConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code greaterThanCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitGreaterThanCondition(BackendlessQueryParser.GreaterThanConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code greaterThanOrEqualCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterGreaterThanOrEqualCondition(BackendlessQueryParser.GreaterThanOrEqualConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code greaterThanOrEqualCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitGreaterThanOrEqualCondition(BackendlessQueryParser.GreaterThanOrEqualConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lessThanOrEqualCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterLessThanOrEqualCondition(BackendlessQueryParser.LessThanOrEqualConditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lessThanOrEqualCondition}
	 * labeled alternative in {@link BackendlessQueryParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitLessThanOrEqualCondition(BackendlessQueryParser.LessThanOrEqualConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code divisionExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 */
	void enterDivisionExpressionAtom(BackendlessQueryParser.DivisionExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code divisionExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 */
	void exitDivisionExpressionAtom(BackendlessQueryParser.DivisionExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code moduloExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 */
	void enterModuloExpressionAtom(BackendlessQueryParser.ModuloExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code moduloExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 */
	void exitModuloExpressionAtom(BackendlessQueryParser.ModuloExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constantExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 */
	void enterConstantExpressionAtom(BackendlessQueryParser.ConstantExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constantExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 */
	void exitConstantExpressionAtom(BackendlessQueryParser.ConstantExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionCallExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCallExpressionAtom(BackendlessQueryParser.FunctionCallExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionCallExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCallExpressionAtom(BackendlessQueryParser.FunctionCallExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subtractionExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 */
	void enterSubtractionExpressionAtom(BackendlessQueryParser.SubtractionExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subtractionExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 */
	void exitSubtractionExpressionAtom(BackendlessQueryParser.SubtractionExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code fullColumnNameExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 */
	void enterFullColumnNameExpressionAtom(BackendlessQueryParser.FullColumnNameExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code fullColumnNameExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 */
	void exitFullColumnNameExpressionAtom(BackendlessQueryParser.FullColumnNameExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code multiplicationExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicationExpressionAtom(BackendlessQueryParser.MultiplicationExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code multiplicationExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicationExpressionAtom(BackendlessQueryParser.MultiplicationExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unaryMinusExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 */
	void enterUnaryMinusExpressionAtom(BackendlessQueryParser.UnaryMinusExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unaryMinusExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 */
	void exitUnaryMinusExpressionAtom(BackendlessQueryParser.UnaryMinusExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code additionExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 */
	void enterAdditionExpressionAtom(BackendlessQueryParser.AdditionExpressionAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code additionExpressionAtom}
	 * labeled alternative in {@link BackendlessQueryParser#expression_atom}.
	 * @param ctx the parse tree
	 */
	void exitAdditionExpressionAtom(BackendlessQueryParser.AdditionExpressionAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#full_column_name}.
	 * @param ctx the parse tree
	 */
	void enterFull_column_name(BackendlessQueryParser.Full_column_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#full_column_name}.
	 * @param ctx the parse tree
	 */
	void exitFull_column_name(BackendlessQueryParser.Full_column_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#single_column_name}.
	 * @param ctx the parse tree
	 */
	void enterSingle_column_name(BackendlessQueryParser.Single_column_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#single_column_name}.
	 * @param ctx the parse tree
	 */
	void exitSingle_column_name(BackendlessQueryParser.Single_column_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#relation_column}.
	 * @param ctx the parse tree
	 */
	void enterRelation_column(BackendlessQueryParser.Relation_columnContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#relation_column}.
	 * @param ctx the parse tree
	 */
	void exitRelation_column(BackendlessQueryParser.Relation_columnContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#inverse_relation_column}.
	 * @param ctx the parse tree
	 */
	void enterInverse_relation_column(BackendlessQueryParser.Inverse_relation_columnContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#inverse_relation_column}.
	 * @param ctx the parse tree
	 */
	void exitInverse_relation_column(BackendlessQueryParser.Inverse_relation_columnContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#unknown_function}.
	 * @param ctx the parse tree
	 */
	void enterUnknown_function(BackendlessQueryParser.Unknown_functionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#unknown_function}.
	 * @param ctx the parse tree
	 */
	void exitUnknown_function(BackendlessQueryParser.Unknown_functionContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#alias}.
	 * @param ctx the parse tree
	 */
	void enterAlias(BackendlessQueryParser.AliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#alias}.
	 * @param ctx the parse tree
	 */
	void exitAlias(BackendlessQueryParser.AliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#table_name}.
	 * @param ctx the parse tree
	 */
	void enterTable_name(BackendlessQueryParser.Table_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#table_name}.
	 * @param ctx the parse tree
	 */
	void exitTable_name(BackendlessQueryParser.Table_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#simple_column}.
	 * @param ctx the parse tree
	 */
	void enterSimple_column(BackendlessQueryParser.Simple_columnContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#simple_column}.
	 * @param ctx the parse tree
	 */
	void exitSimple_column(BackendlessQueryParser.Simple_columnContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#unknown_name}.
	 * @param ctx the parse tree
	 */
	void enterUnknown_name(BackendlessQueryParser.Unknown_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#unknown_name}.
	 * @param ctx the parse tree
	 */
	void exitUnknown_name(BackendlessQueryParser.Unknown_nameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringConstant}
	 * labeled alternative in {@link BackendlessQueryParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterStringConstant(BackendlessQueryParser.StringConstantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringConstant}
	 * labeled alternative in {@link BackendlessQueryParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitStringConstant(BackendlessQueryParser.StringConstantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code numericConstant}
	 * labeled alternative in {@link BackendlessQueryParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterNumericConstant(BackendlessQueryParser.NumericConstantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code numericConstant}
	 * labeled alternative in {@link BackendlessQueryParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitNumericConstant(BackendlessQueryParser.NumericConstantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code booleanConstant}
	 * labeled alternative in {@link BackendlessQueryParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterBooleanConstant(BackendlessQueryParser.BooleanConstantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code booleanConstant}
	 * labeled alternative in {@link BackendlessQueryParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitBooleanConstant(BackendlessQueryParser.BooleanConstantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nullConstant}
	 * labeled alternative in {@link BackendlessQueryParser#constant}.
	 * @param ctx the parse tree
	 */
	void enterNullConstant(BackendlessQueryParser.NullConstantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nullConstant}
	 * labeled alternative in {@link BackendlessQueryParser#constant}.
	 * @param ctx the parse tree
	 */
	void exitNullConstant(BackendlessQueryParser.NullConstantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code trueBooleanConstant}
	 * labeled alternative in {@link BackendlessQueryParser#boolean_literal}.
	 * @param ctx the parse tree
	 */
	void enterTrueBooleanConstant(BackendlessQueryParser.TrueBooleanConstantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code trueBooleanConstant}
	 * labeled alternative in {@link BackendlessQueryParser#boolean_literal}.
	 * @param ctx the parse tree
	 */
	void exitTrueBooleanConstant(BackendlessQueryParser.TrueBooleanConstantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code falseBooleanConstant}
	 * labeled alternative in {@link BackendlessQueryParser#boolean_literal}.
	 * @param ctx the parse tree
	 */
	void enterFalseBooleanConstant(BackendlessQueryParser.FalseBooleanConstantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code falseBooleanConstant}
	 * labeled alternative in {@link BackendlessQueryParser#boolean_literal}.
	 * @param ctx the parse tree
	 */
	void exitFalseBooleanConstant(BackendlessQueryParser.FalseBooleanConstantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code decimalNumericConstant}
	 * labeled alternative in {@link BackendlessQueryParser#numeric_literal}.
	 * @param ctx the parse tree
	 */
	void enterDecimalNumericConstant(BackendlessQueryParser.DecimalNumericConstantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code decimalNumericConstant}
	 * labeled alternative in {@link BackendlessQueryParser#numeric_literal}.
	 * @param ctx the parse tree
	 */
	void exitDecimalNumericConstant(BackendlessQueryParser.DecimalNumericConstantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code floatNumericConstant}
	 * labeled alternative in {@link BackendlessQueryParser#numeric_literal}.
	 * @param ctx the parse tree
	 */
	void enterFloatNumericConstant(BackendlessQueryParser.FloatNumericConstantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code floatNumericConstant}
	 * labeled alternative in {@link BackendlessQueryParser#numeric_literal}.
	 * @param ctx the parse tree
	 */
	void exitFloatNumericConstant(BackendlessQueryParser.FloatNumericConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#string_literal}.
	 * @param ctx the parse tree
	 */
	void enterString_literal(BackendlessQueryParser.String_literalContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#string_literal}.
	 * @param ctx the parse tree
	 */
	void exitString_literal(BackendlessQueryParser.String_literalContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#regular_id}.
	 * @param ctx the parse tree
	 */
	void enterRegular_id(BackendlessQueryParser.Regular_idContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#regular_id}.
	 * @param ctx the parse tree
	 */
	void exitRegular_id(BackendlessQueryParser.Regular_idContext ctx);
	/**
	 * Enter a parse tree produced by {@link BackendlessQueryParser#keyword_as_id}.
	 * @param ctx the parse tree
	 */
	void enterKeyword_as_id(BackendlessQueryParser.Keyword_as_idContext ctx);
	/**
	 * Exit a parse tree produced by {@link BackendlessQueryParser#keyword_as_id}.
	 * @param ctx the parse tree
	 */
	void exitKeyword_as_id(BackendlessQueryParser.Keyword_as_idContext ctx);
}