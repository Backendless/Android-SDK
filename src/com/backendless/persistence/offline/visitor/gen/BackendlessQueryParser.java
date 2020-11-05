// Generated from /home/max/Dev/Projects/IntellijProjects/TestVisitor/visitor/resources/BackendlessQuery.g4 by ANTLR 4.7.2
package com.backendless.persistence.offline.visitor.gen;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class BackendlessQueryParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, AFTER=24, 
		AND=25, AT=26, AS=27, ASC=28, AVG=29, BEFORE=30, BY=31, COUNT=32, DESC=33, 
		DISTANCE=34, DISTANCE_ON_SPHERE=35, DIV=36, ESCAPE=37, FALSE=38, FT=39, 
		GROUP=40, HAVING=41, IN=42, IS=43, KM=44, LIMIT=45, LIKE=46, MAX=47, MI=48, 
		MIN=49, MOD=50, NOT=51, NULL=52, OFFSET=53, OR=54, SELECT=55, SORT=56, 
		SUM=57, TRUE=58, YD=59, AsWKB=60, AsWKT=61, AsGeoJSON=62, ID=63, INT=64, 
		CHAR_STRING=65, WS=66;
	public static final int
		RULE_select_statement = 0, RULE_select_list = 1, RULE_select_list_elem = 2, 
		RULE_group_by_clause = 3, RULE_having_clause = 4, RULE_sort_by_clause = 5, 
		RULE_sort_by_elem = 6, RULE_limit_clause = 7, RULE_function_call = 8, 
		RULE_spatial_convert_function = 9, RULE_spatial_function_parameter = 10, 
		RULE_aswkb_function = 11, RULE_aswkt_function = 12, RULE_asgeojson_function = 13, 
		RULE_spatial_function = 14, RULE_distance_function = 15, RULE_distance_on_sphere = 16, 
		RULE_units_function = 17, RULE_aggregate_windowed_function = 18, RULE_condition = 19, 
		RULE_expression_atom = 20, RULE_full_column_name = 21, RULE_single_column_name = 22, 
		RULE_relation_column = 23, RULE_inverse_relation_column = 24, RULE_unknown_function = 25, 
		RULE_alias = 26, RULE_table_name = 27, RULE_simple_column = 28, RULE_unknown_name = 29, 
		RULE_constant = 30, RULE_boolean_literal = 31, RULE_numeric_literal = 32, 
		RULE_string_literal = 33, RULE_regular_id = 34, RULE_keyword_as_id = 35;
	private static String[] makeRuleNames() {
		return new String[] {
			"select_statement", "select_list", "select_list_elem", "group_by_clause", 
			"having_clause", "sort_by_clause", "sort_by_elem", "limit_clause", "function_call", 
			"spatial_convert_function", "spatial_function_parameter", "aswkb_function", 
			"aswkt_function", "asgeojson_function", "spatial_function", "distance_function", 
			"distance_on_sphere", "units_function", "aggregate_windowed_function", 
			"condition", "expression_atom", "full_column_name", "single_column_name", 
			"relation_column", "inverse_relation_column", "unknown_function", "alias", 
			"table_name", "simple_column", "unknown_name", "constant", "boolean_literal", 
			"numeric_literal", "string_literal", "regular_id", "keyword_as_id"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'['", "']'", "'.'", "'{'", "','", "'}'", "'('", "')'", "'!'", 
			"'&&'", "'||'", "'='", "'!='", "'<>'", "'>='", "'>'", "'<='", "'<'", 
			"'-'", "'*'", "'/'", "'%'", "'+'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			"AFTER", "AND", "AT", "AS", "ASC", "AVG", "BEFORE", "BY", "COUNT", "DESC", 
			"DISTANCE", "DISTANCE_ON_SPHERE", "DIV", "ESCAPE", "FALSE", "FT", "GROUP", 
			"HAVING", "IN", "IS", "KM", "LIMIT", "LIKE", "MAX", "MI", "MIN", "MOD", 
			"NOT", "NULL", "OFFSET", "OR", "SELECT", "SORT", "SUM", "TRUE", "YD", 
			"AsWKB", "AsWKT", "AsGeoJSON", "ID", "INT", "CHAR_STRING", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "BackendlessQuery.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public BackendlessQueryParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class Select_statementContext extends ParserRuleContext {
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public Select_listContext select_list() {
			return getRuleContext(Select_listContext.class,0);
		}
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public Group_by_clauseContext group_by_clause() {
			return getRuleContext(Group_by_clauseContext.class,0);
		}
		public Having_clauseContext having_clause() {
			return getRuleContext(Having_clauseContext.class,0);
		}
		public Sort_by_clauseContext sort_by_clause() {
			return getRuleContext(Sort_by_clauseContext.class,0);
		}
		public Limit_clauseContext limit_clause() {
			return getRuleContext(Limit_clauseContext.class,0);
		}
		public Select_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_select_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterSelect_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitSelect_statement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitSelect_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Select_statementContext select_statement() throws RecognitionException {
		Select_statementContext _localctx = new Select_statementContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_select_statement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			table_name();
			setState(73);
			match(T__0);
			setState(75);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & ((1L << (T__2 - 3)) | (1L << (T__6 - 3)) | (1L << (T__8 - 3)) | (1L << (T__18 - 3)) | (1L << (AFTER - 3)) | (1L << (AND - 3)) | (1L << (AT - 3)) | (1L << (AS - 3)) | (1L << (ASC - 3)) | (1L << (AVG - 3)) | (1L << (BEFORE - 3)) | (1L << (BY - 3)) | (1L << (COUNT - 3)) | (1L << (DESC - 3)) | (1L << (DISTANCE - 3)) | (1L << (DISTANCE_ON_SPHERE - 3)) | (1L << (DIV - 3)) | (1L << (ESCAPE - 3)) | (1L << (FALSE - 3)) | (1L << (FT - 3)) | (1L << (GROUP - 3)) | (1L << (HAVING - 3)) | (1L << (IN - 3)) | (1L << (IS - 3)) | (1L << (KM - 3)) | (1L << (LIMIT - 3)) | (1L << (LIKE - 3)) | (1L << (MAX - 3)) | (1L << (MI - 3)) | (1L << (MIN - 3)) | (1L << (MOD - 3)) | (1L << (NOT - 3)) | (1L << (NULL - 3)) | (1L << (OFFSET - 3)) | (1L << (OR - 3)) | (1L << (SELECT - 3)) | (1L << (SORT - 3)) | (1L << (SUM - 3)) | (1L << (TRUE - 3)) | (1L << (YD - 3)) | (1L << (AsWKB - 3)) | (1L << (AsWKT - 3)) | (1L << (AsGeoJSON - 3)) | (1L << (ID - 3)) | (1L << (INT - 3)) | (1L << (CHAR_STRING - 3)))) != 0)) {
				{
				setState(74);
				condition(0);
				}
			}

			setState(77);
			match(T__1);
			setState(78);
			match(T__2);
			setState(79);
			select_list();
			setState(81);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==GROUP) {
				{
				setState(80);
				group_by_clause();
				}
			}

			setState(84);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==HAVING) {
				{
				setState(83);
				having_clause();
				}
			}

			setState(87);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SORT) {
				{
				setState(86);
				sort_by_clause();
				}
			}

			setState(90);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LIMIT) {
				{
				setState(89);
				limit_clause();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Select_listContext extends ParserRuleContext {
		public List<Select_list_elemContext> select_list_elem() {
			return getRuleContexts(Select_list_elemContext.class);
		}
		public Select_list_elemContext select_list_elem(int i) {
			return getRuleContext(Select_list_elemContext.class,i);
		}
		public Select_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_select_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterSelect_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitSelect_list(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitSelect_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Select_listContext select_list() throws RecognitionException {
		Select_listContext _localctx = new Select_listContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_select_list);
		int _la;
		try {
			setState(104);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__3:
				enterOuterAlt(_localctx, 1);
				{
				setState(92);
				match(T__3);
				setState(93);
				select_list_elem();
				setState(98);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(94);
					match(T__4);
					setState(95);
					select_list_elem();
					}
					}
					setState(100);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(101);
				match(T__5);
				}
				break;
			case T__2:
			case T__18:
			case AFTER:
			case AND:
			case AT:
			case AS:
			case ASC:
			case AVG:
			case BEFORE:
			case BY:
			case COUNT:
			case DESC:
			case DISTANCE:
			case DISTANCE_ON_SPHERE:
			case DIV:
			case ESCAPE:
			case FALSE:
			case FT:
			case GROUP:
			case HAVING:
			case IN:
			case IS:
			case KM:
			case LIMIT:
			case LIKE:
			case MAX:
			case MI:
			case MIN:
			case MOD:
			case NOT:
			case NULL:
			case OFFSET:
			case OR:
			case SELECT:
			case SORT:
			case SUM:
			case TRUE:
			case YD:
			case AsWKB:
			case AsWKT:
			case AsGeoJSON:
			case ID:
			case INT:
			case CHAR_STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(103);
				select_list_elem();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Select_list_elemContext extends ParserRuleContext {
		public Select_list_elemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_select_list_elem; }
	 
		public Select_list_elemContext() { }
		public void copyFrom(Select_list_elemContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SimpleSelectElemContext extends Select_list_elemContext {
		public Expression_atomContext expression_atom() {
			return getRuleContext(Expression_atomContext.class,0);
		}
		public SimpleSelectElemContext(Select_list_elemContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterSimpleSelectElem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitSimpleSelectElem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitSimpleSelectElem(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AliasedSelectElemContext extends Select_list_elemContext {
		public Expression_atomContext expression_atom() {
			return getRuleContext(Expression_atomContext.class,0);
		}
		public TerminalNode AS() { return getToken(BackendlessQueryParser.AS, 0); }
		public AliasContext alias() {
			return getRuleContext(AliasContext.class,0);
		}
		public AliasedSelectElemContext(Select_list_elemContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterAliasedSelectElem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitAliasedSelectElem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitAliasedSelectElem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Select_list_elemContext select_list_elem() throws RecognitionException {
		Select_list_elemContext _localctx = new Select_list_elemContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_select_list_elem);
		try {
			setState(111);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				_localctx = new SimpleSelectElemContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(106);
				expression_atom(0);
				}
				break;
			case 2:
				_localctx = new AliasedSelectElemContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(107);
				expression_atom(0);
				setState(108);
				match(AS);
				setState(109);
				alias();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Group_by_clauseContext extends ParserRuleContext {
		public TerminalNode GROUP() { return getToken(BackendlessQueryParser.GROUP, 0); }
		public TerminalNode BY() { return getToken(BackendlessQueryParser.BY, 0); }
		public List<Expression_atomContext> expression_atom() {
			return getRuleContexts(Expression_atomContext.class);
		}
		public Expression_atomContext expression_atom(int i) {
			return getRuleContext(Expression_atomContext.class,i);
		}
		public Group_by_clauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_group_by_clause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterGroup_by_clause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitGroup_by_clause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitGroup_by_clause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Group_by_clauseContext group_by_clause() throws RecognitionException {
		Group_by_clauseContext _localctx = new Group_by_clauseContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_group_by_clause);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			match(GROUP);
			setState(114);
			match(BY);
			setState(115);
			expression_atom(0);
			setState(120);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(116);
					match(T__4);
					setState(117);
					expression_atom(0);
					}
					} 
				}
				setState(122);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Having_clauseContext extends ParserRuleContext {
		public TerminalNode HAVING() { return getToken(BackendlessQueryParser.HAVING, 0); }
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public Having_clauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_having_clause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterHaving_clause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitHaving_clause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitHaving_clause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Having_clauseContext having_clause() throws RecognitionException {
		Having_clauseContext _localctx = new Having_clauseContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_having_clause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(123);
			match(HAVING);
			setState(124);
			condition(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Sort_by_clauseContext extends ParserRuleContext {
		public TerminalNode SORT() { return getToken(BackendlessQueryParser.SORT, 0); }
		public TerminalNode BY() { return getToken(BackendlessQueryParser.BY, 0); }
		public List<Sort_by_elemContext> sort_by_elem() {
			return getRuleContexts(Sort_by_elemContext.class);
		}
		public Sort_by_elemContext sort_by_elem(int i) {
			return getRuleContext(Sort_by_elemContext.class,i);
		}
		public Sort_by_clauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sort_by_clause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterSort_by_clause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitSort_by_clause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitSort_by_clause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Sort_by_clauseContext sort_by_clause() throws RecognitionException {
		Sort_by_clauseContext _localctx = new Sort_by_clauseContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_sort_by_clause);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
			match(SORT);
			setState(127);
			match(BY);
			setState(128);
			sort_by_elem();
			setState(133);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(129);
					match(T__4);
					setState(130);
					sort_by_elem();
					}
					} 
				}
				setState(135);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Sort_by_elemContext extends ParserRuleContext {
		public Expression_atomContext expression_atom() {
			return getRuleContext(Expression_atomContext.class,0);
		}
		public TerminalNode ASC() { return getToken(BackendlessQueryParser.ASC, 0); }
		public TerminalNode DESC() { return getToken(BackendlessQueryParser.DESC, 0); }
		public Sort_by_elemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sort_by_elem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterSort_by_elem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitSort_by_elem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitSort_by_elem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Sort_by_elemContext sort_by_elem() throws RecognitionException {
		Sort_by_elemContext _localctx = new Sort_by_elemContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_sort_by_elem);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(136);
			expression_atom(0);
			setState(138);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASC || _la==DESC) {
				{
				setState(137);
				_la = _input.LA(1);
				if ( !(_la==ASC || _la==DESC) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Limit_clauseContext extends ParserRuleContext {
		public TerminalNode LIMIT() { return getToken(BackendlessQueryParser.LIMIT, 0); }
		public List<TerminalNode> INT() { return getTokens(BackendlessQueryParser.INT); }
		public TerminalNode INT(int i) {
			return getToken(BackendlessQueryParser.INT, i);
		}
		public TerminalNode OFFSET() { return getToken(BackendlessQueryParser.OFFSET, 0); }
		public Limit_clauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_limit_clause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterLimit_clause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitLimit_clause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitLimit_clause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Limit_clauseContext limit_clause() throws RecognitionException {
		Limit_clauseContext _localctx = new Limit_clauseContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_limit_clause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(140);
			match(LIMIT);
			setState(141);
			match(INT);
			setState(144);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OFFSET) {
				{
				setState(142);
				match(OFFSET);
				setState(143);
				match(INT);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Function_callContext extends ParserRuleContext {
		public Aggregate_windowed_functionContext aggregate_windowed_function() {
			return getRuleContext(Aggregate_windowed_functionContext.class,0);
		}
		public Units_functionContext units_function() {
			return getRuleContext(Units_functionContext.class,0);
		}
		public Spatial_convert_functionContext spatial_convert_function() {
			return getRuleContext(Spatial_convert_functionContext.class,0);
		}
		public Spatial_functionContext spatial_function() {
			return getRuleContext(Spatial_functionContext.class,0);
		}
		public Unknown_functionContext unknown_function() {
			return getRuleContext(Unknown_functionContext.class,0);
		}
		public Function_callContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function_call; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterFunction_call(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitFunction_call(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitFunction_call(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Function_callContext function_call() throws RecognitionException {
		Function_callContext _localctx = new Function_callContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_function_call);
		try {
			setState(151);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(146);
				aggregate_windowed_function();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(147);
				units_function();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(148);
				spatial_convert_function();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(149);
				spatial_function();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(150);
				unknown_function();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Spatial_convert_functionContext extends ParserRuleContext {
		public Aswkb_functionContext aswkb_function() {
			return getRuleContext(Aswkb_functionContext.class,0);
		}
		public Aswkt_functionContext aswkt_function() {
			return getRuleContext(Aswkt_functionContext.class,0);
		}
		public Asgeojson_functionContext asgeojson_function() {
			return getRuleContext(Asgeojson_functionContext.class,0);
		}
		public Spatial_convert_functionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_spatial_convert_function; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterSpatial_convert_function(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitSpatial_convert_function(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitSpatial_convert_function(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Spatial_convert_functionContext spatial_convert_function() throws RecognitionException {
		Spatial_convert_functionContext _localctx = new Spatial_convert_functionContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_spatial_convert_function);
		try {
			setState(156);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case AsWKB:
				enterOuterAlt(_localctx, 1);
				{
				setState(153);
				aswkb_function();
				}
				break;
			case AsWKT:
				enterOuterAlt(_localctx, 2);
				{
				setState(154);
				aswkt_function();
				}
				break;
			case AsGeoJSON:
				enterOuterAlt(_localctx, 3);
				{
				setState(155);
				asgeojson_function();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Spatial_function_parameterContext extends ParserRuleContext {
		public Simple_columnContext simple_column() {
			return getRuleContext(Simple_columnContext.class,0);
		}
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public Spatial_function_parameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_spatial_function_parameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterSpatial_function_parameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitSpatial_function_parameter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitSpatial_function_parameter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Spatial_function_parameterContext spatial_function_parameter() throws RecognitionException {
		Spatial_function_parameterContext _localctx = new Spatial_function_parameterContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_spatial_function_parameter);
		try {
			setState(160);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(158);
				simple_column();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(159);
				constant();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Aswkb_functionContext extends ParserRuleContext {
		public TerminalNode AsWKB() { return getToken(BackendlessQueryParser.AsWKB, 0); }
		public Spatial_function_parameterContext spatial_function_parameter() {
			return getRuleContext(Spatial_function_parameterContext.class,0);
		}
		public Aswkb_functionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aswkb_function; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterAswkb_function(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitAswkb_function(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitAswkb_function(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Aswkb_functionContext aswkb_function() throws RecognitionException {
		Aswkb_functionContext _localctx = new Aswkb_functionContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_aswkb_function);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			match(AsWKB);
			setState(163);
			match(T__6);
			setState(164);
			spatial_function_parameter();
			setState(165);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Aswkt_functionContext extends ParserRuleContext {
		public TerminalNode AsWKT() { return getToken(BackendlessQueryParser.AsWKT, 0); }
		public Spatial_function_parameterContext spatial_function_parameter() {
			return getRuleContext(Spatial_function_parameterContext.class,0);
		}
		public Aswkt_functionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aswkt_function; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterAswkt_function(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitAswkt_function(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitAswkt_function(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Aswkt_functionContext aswkt_function() throws RecognitionException {
		Aswkt_functionContext _localctx = new Aswkt_functionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_aswkt_function);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(167);
			match(AsWKT);
			setState(168);
			match(T__6);
			setState(169);
			spatial_function_parameter();
			setState(170);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Asgeojson_functionContext extends ParserRuleContext {
		public TerminalNode AsGeoJSON() { return getToken(BackendlessQueryParser.AsGeoJSON, 0); }
		public Spatial_function_parameterContext spatial_function_parameter() {
			return getRuleContext(Spatial_function_parameterContext.class,0);
		}
		public Asgeojson_functionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_asgeojson_function; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterAsgeojson_function(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitAsgeojson_function(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitAsgeojson_function(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Asgeojson_functionContext asgeojson_function() throws RecognitionException {
		Asgeojson_functionContext _localctx = new Asgeojson_functionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_asgeojson_function);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(172);
			match(AsGeoJSON);
			setState(173);
			match(T__6);
			setState(174);
			spatial_function_parameter();
			setState(175);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Spatial_functionContext extends ParserRuleContext {
		public Distance_functionContext distance_function() {
			return getRuleContext(Distance_functionContext.class,0);
		}
		public Distance_on_sphereContext distance_on_sphere() {
			return getRuleContext(Distance_on_sphereContext.class,0);
		}
		public Spatial_functionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_spatial_function; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterSpatial_function(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitSpatial_function(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitSpatial_function(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Spatial_functionContext spatial_function() throws RecognitionException {
		Spatial_functionContext _localctx = new Spatial_functionContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_spatial_function);
		try {
			setState(179);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DISTANCE:
				enterOuterAlt(_localctx, 1);
				{
				setState(177);
				distance_function();
				}
				break;
			case DISTANCE_ON_SPHERE:
				enterOuterAlt(_localctx, 2);
				{
				setState(178);
				distance_on_sphere();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Distance_functionContext extends ParserRuleContext {
		public TerminalNode DISTANCE() { return getToken(BackendlessQueryParser.DISTANCE, 0); }
		public List<Spatial_function_parameterContext> spatial_function_parameter() {
			return getRuleContexts(Spatial_function_parameterContext.class);
		}
		public Spatial_function_parameterContext spatial_function_parameter(int i) {
			return getRuleContext(Spatial_function_parameterContext.class,i);
		}
		public List<Expression_atomContext> expression_atom() {
			return getRuleContexts(Expression_atomContext.class);
		}
		public Expression_atomContext expression_atom(int i) {
			return getRuleContext(Expression_atomContext.class,i);
		}
		public Distance_functionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_distance_function; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterDistance_function(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitDistance_function(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitDistance_function(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Distance_functionContext distance_function() throws RecognitionException {
		Distance_functionContext _localctx = new Distance_functionContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_distance_function);
		try {
			setState(199);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(181);
				match(DISTANCE);
				setState(182);
				match(T__6);
				setState(183);
				spatial_function_parameter();
				setState(184);
				match(T__4);
				setState(185);
				spatial_function_parameter();
				setState(186);
				match(T__7);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(188);
				match(DISTANCE);
				setState(189);
				match(T__6);
				setState(190);
				expression_atom(0);
				setState(191);
				match(T__4);
				setState(192);
				expression_atom(0);
				setState(193);
				match(T__4);
				setState(194);
				expression_atom(0);
				setState(195);
				match(T__4);
				setState(196);
				expression_atom(0);
				setState(197);
				match(T__7);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Distance_on_sphereContext extends ParserRuleContext {
		public TerminalNode DISTANCE_ON_SPHERE() { return getToken(BackendlessQueryParser.DISTANCE_ON_SPHERE, 0); }
		public List<Spatial_function_parameterContext> spatial_function_parameter() {
			return getRuleContexts(Spatial_function_parameterContext.class);
		}
		public Spatial_function_parameterContext spatial_function_parameter(int i) {
			return getRuleContext(Spatial_function_parameterContext.class,i);
		}
		public Expression_atomContext expression_atom() {
			return getRuleContext(Expression_atomContext.class,0);
		}
		public Distance_on_sphereContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_distance_on_sphere; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterDistance_on_sphere(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitDistance_on_sphere(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitDistance_on_sphere(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Distance_on_sphereContext distance_on_sphere() throws RecognitionException {
		Distance_on_sphereContext _localctx = new Distance_on_sphereContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_distance_on_sphere);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(201);
			match(DISTANCE_ON_SPHERE);
			setState(202);
			match(T__6);
			setState(203);
			spatial_function_parameter();
			setState(204);
			match(T__4);
			setState(205);
			spatial_function_parameter();
			setState(208);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(206);
				match(T__4);
				setState(207);
				expression_atom(0);
				}
			}

			setState(210);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Units_functionContext extends ParserRuleContext {
		public Units_functionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_units_function; }
	 
		public Units_functionContext() { }
		public void copyFrom(Units_functionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class KmUnitsFunctionContext extends Units_functionContext {
		public TerminalNode KM() { return getToken(BackendlessQueryParser.KM, 0); }
		public Numeric_literalContext numeric_literal() {
			return getRuleContext(Numeric_literalContext.class,0);
		}
		public KmUnitsFunctionContext(Units_functionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterKmUnitsFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitKmUnitsFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitKmUnitsFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class YdUnitsFunctionContext extends Units_functionContext {
		public TerminalNode YD() { return getToken(BackendlessQueryParser.YD, 0); }
		public Numeric_literalContext numeric_literal() {
			return getRuleContext(Numeric_literalContext.class,0);
		}
		public YdUnitsFunctionContext(Units_functionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterYdUnitsFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitYdUnitsFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitYdUnitsFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FtUnitsFunctionContext extends Units_functionContext {
		public TerminalNode FT() { return getToken(BackendlessQueryParser.FT, 0); }
		public Numeric_literalContext numeric_literal() {
			return getRuleContext(Numeric_literalContext.class,0);
		}
		public FtUnitsFunctionContext(Units_functionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterFtUnitsFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitFtUnitsFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitFtUnitsFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MiUnitsFunctionContext extends Units_functionContext {
		public TerminalNode MI() { return getToken(BackendlessQueryParser.MI, 0); }
		public Numeric_literalContext numeric_literal() {
			return getRuleContext(Numeric_literalContext.class,0);
		}
		public MiUnitsFunctionContext(Units_functionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterMiUnitsFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitMiUnitsFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitMiUnitsFunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Units_functionContext units_function() throws RecognitionException {
		Units_functionContext _localctx = new Units_functionContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_units_function);
		try {
			setState(232);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FT:
				_localctx = new FtUnitsFunctionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(212);
				match(FT);
				setState(213);
				match(T__6);
				setState(214);
				numeric_literal();
				setState(215);
				match(T__7);
				}
				break;
			case KM:
				_localctx = new KmUnitsFunctionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(217);
				match(KM);
				setState(218);
				match(T__6);
				setState(219);
				numeric_literal();
				setState(220);
				match(T__7);
				}
				break;
			case MI:
				_localctx = new MiUnitsFunctionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(222);
				match(MI);
				setState(223);
				match(T__6);
				setState(224);
				numeric_literal();
				setState(225);
				match(T__7);
				}
				break;
			case YD:
				_localctx = new YdUnitsFunctionContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(227);
				match(YD);
				setState(228);
				match(T__6);
				setState(229);
				numeric_literal();
				setState(230);
				match(T__7);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Aggregate_windowed_functionContext extends ParserRuleContext {
		public Aggregate_windowed_functionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aggregate_windowed_function; }
	 
		public Aggregate_windowed_functionContext() { }
		public void copyFrom(Aggregate_windowed_functionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class CountFunctionContext extends Aggregate_windowed_functionContext {
		public TerminalNode COUNT() { return getToken(BackendlessQueryParser.COUNT, 0); }
		public Expression_atomContext expression_atom() {
			return getRuleContext(Expression_atomContext.class,0);
		}
		public CountFunctionContext(Aggregate_windowed_functionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterCountFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitCountFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitCountFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SumFunctionContext extends Aggregate_windowed_functionContext {
		public TerminalNode SUM() { return getToken(BackendlessQueryParser.SUM, 0); }
		public Expression_atomContext expression_atom() {
			return getRuleContext(Expression_atomContext.class,0);
		}
		public SumFunctionContext(Aggregate_windowed_functionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterSumFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitSumFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitSumFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MinFunctionContext extends Aggregate_windowed_functionContext {
		public TerminalNode MIN() { return getToken(BackendlessQueryParser.MIN, 0); }
		public Expression_atomContext expression_atom() {
			return getRuleContext(Expression_atomContext.class,0);
		}
		public MinFunctionContext(Aggregate_windowed_functionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterMinFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitMinFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitMinFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MaxFunctionContext extends Aggregate_windowed_functionContext {
		public TerminalNode MAX() { return getToken(BackendlessQueryParser.MAX, 0); }
		public Expression_atomContext expression_atom() {
			return getRuleContext(Expression_atomContext.class,0);
		}
		public MaxFunctionContext(Aggregate_windowed_functionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterMaxFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitMaxFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitMaxFunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AvgFunctionContext extends Aggregate_windowed_functionContext {
		public TerminalNode AVG() { return getToken(BackendlessQueryParser.AVG, 0); }
		public Expression_atomContext expression_atom() {
			return getRuleContext(Expression_atomContext.class,0);
		}
		public AvgFunctionContext(Aggregate_windowed_functionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterAvgFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitAvgFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitAvgFunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Aggregate_windowed_functionContext aggregate_windowed_function() throws RecognitionException {
		Aggregate_windowed_functionContext _localctx = new Aggregate_windowed_functionContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_aggregate_windowed_function);
		try {
			setState(259);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case AVG:
				_localctx = new AvgFunctionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(234);
				match(AVG);
				setState(235);
				match(T__6);
				setState(236);
				expression_atom(0);
				setState(237);
				match(T__7);
				}
				break;
			case MAX:
				_localctx = new MaxFunctionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(239);
				match(MAX);
				setState(240);
				match(T__6);
				setState(241);
				expression_atom(0);
				setState(242);
				match(T__7);
				}
				break;
			case MIN:
				_localctx = new MinFunctionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(244);
				match(MIN);
				setState(245);
				match(T__6);
				setState(246);
				expression_atom(0);
				setState(247);
				match(T__7);
				}
				break;
			case SUM:
				_localctx = new SumFunctionContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(249);
				match(SUM);
				setState(250);
				match(T__6);
				setState(251);
				expression_atom(0);
				setState(252);
				match(T__7);
				}
				break;
			case COUNT:
				_localctx = new CountFunctionContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(254);
				match(COUNT);
				setState(255);
				match(T__6);
				setState(256);
				expression_atom(0);
				setState(257);
				match(T__7);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConditionContext extends ParserRuleContext {
		public ConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition; }
	 
		public ConditionContext() { }
		public void copyFrom(ConditionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class InConditionContext extends ConditionContext {
		public List<Expression_atomContext> expression_atom() {
			return getRuleContexts(Expression_atomContext.class);
		}
		public Expression_atomContext expression_atom(int i) {
			return getRuleContext(Expression_atomContext.class,i);
		}
		public TerminalNode IN() { return getToken(BackendlessQueryParser.IN, 0); }
		public List<Select_statementContext> select_statement() {
			return getRuleContexts(Select_statementContext.class);
		}
		public Select_statementContext select_statement(int i) {
			return getRuleContext(Select_statementContext.class,i);
		}
		public TerminalNode NOT() { return getToken(BackendlessQueryParser.NOT, 0); }
		public InConditionContext(ConditionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterInCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitInCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitInCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EqualConditionContext extends ConditionContext {
		public List<Expression_atomContext> expression_atom() {
			return getRuleContexts(Expression_atomContext.class);
		}
		public Expression_atomContext expression_atom(int i) {
			return getRuleContext(Expression_atomContext.class,i);
		}
		public TerminalNode AT() { return getToken(BackendlessQueryParser.AT, 0); }
		public EqualConditionContext(ConditionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterEqualCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitEqualCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitEqualCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OrConditionContext extends ConditionContext {
		public List<ConditionContext> condition() {
			return getRuleContexts(ConditionContext.class);
		}
		public ConditionContext condition(int i) {
			return getRuleContext(ConditionContext.class,i);
		}
		public TerminalNode OR() { return getToken(BackendlessQueryParser.OR, 0); }
		public OrConditionContext(ConditionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterOrCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitOrCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitOrCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LessThanConditionContext extends ConditionContext {
		public List<Expression_atomContext> expression_atom() {
			return getRuleContexts(Expression_atomContext.class);
		}
		public Expression_atomContext expression_atom(int i) {
			return getRuleContext(Expression_atomContext.class,i);
		}
		public TerminalNode BEFORE() { return getToken(BackendlessQueryParser.BEFORE, 0); }
		public LessThanConditionContext(ConditionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterLessThanCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitLessThanCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitLessThanCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AndConditionContext extends ConditionContext {
		public List<ConditionContext> condition() {
			return getRuleContexts(ConditionContext.class);
		}
		public ConditionContext condition(int i) {
			return getRuleContext(ConditionContext.class,i);
		}
		public TerminalNode AND() { return getToken(BackendlessQueryParser.AND, 0); }
		public AndConditionContext(ConditionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterAndCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitAndCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitAndCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IsNullConditionContext extends ConditionContext {
		public Expression_atomContext expression_atom() {
			return getRuleContext(Expression_atomContext.class,0);
		}
		public TerminalNode IS() { return getToken(BackendlessQueryParser.IS, 0); }
		public TerminalNode NULL() { return getToken(BackendlessQueryParser.NULL, 0); }
		public IsNullConditionContext(ConditionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterIsNullCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitIsNullCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitIsNullCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NotConditionContext extends ConditionContext {
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public TerminalNode NOT() { return getToken(BackendlessQueryParser.NOT, 0); }
		public NotConditionContext(ConditionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterNotCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitNotCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitNotCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LikeConditionContext extends ConditionContext {
		public List<Expression_atomContext> expression_atom() {
			return getRuleContexts(Expression_atomContext.class);
		}
		public Expression_atomContext expression_atom(int i) {
			return getRuleContext(Expression_atomContext.class,i);
		}
		public TerminalNode LIKE() { return getToken(BackendlessQueryParser.LIKE, 0); }
		public TerminalNode NOT() { return getToken(BackendlessQueryParser.NOT, 0); }
		public LikeConditionContext(ConditionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterLikeCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitLikeCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitLikeCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IsNotNullConditionContext extends ConditionContext {
		public Expression_atomContext expression_atom() {
			return getRuleContext(Expression_atomContext.class,0);
		}
		public TerminalNode IS() { return getToken(BackendlessQueryParser.IS, 0); }
		public TerminalNode NOT() { return getToken(BackendlessQueryParser.NOT, 0); }
		public TerminalNode NULL() { return getToken(BackendlessQueryParser.NULL, 0); }
		public IsNotNullConditionContext(ConditionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterIsNotNullCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitIsNotNullCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitIsNotNullCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NestedConditionContext extends ConditionContext {
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public NestedConditionContext(ConditionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterNestedCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitNestedCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitNestedCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NotEqualConditionContext extends ConditionContext {
		public List<Expression_atomContext> expression_atom() {
			return getRuleContexts(Expression_atomContext.class);
		}
		public Expression_atomContext expression_atom(int i) {
			return getRuleContext(Expression_atomContext.class,i);
		}
		public NotEqualConditionContext(ConditionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterNotEqualCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitNotEqualCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitNotEqualCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GreaterThanConditionContext extends ConditionContext {
		public List<Expression_atomContext> expression_atom() {
			return getRuleContexts(Expression_atomContext.class);
		}
		public Expression_atomContext expression_atom(int i) {
			return getRuleContext(Expression_atomContext.class,i);
		}
		public TerminalNode AFTER() { return getToken(BackendlessQueryParser.AFTER, 0); }
		public GreaterThanConditionContext(ConditionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterGreaterThanCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitGreaterThanCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitGreaterThanCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GreaterThanOrEqualConditionContext extends ConditionContext {
		public List<Expression_atomContext> expression_atom() {
			return getRuleContexts(Expression_atomContext.class);
		}
		public Expression_atomContext expression_atom(int i) {
			return getRuleContext(Expression_atomContext.class,i);
		}
		public TerminalNode AT() { return getToken(BackendlessQueryParser.AT, 0); }
		public TerminalNode OR() { return getToken(BackendlessQueryParser.OR, 0); }
		public TerminalNode AFTER() { return getToken(BackendlessQueryParser.AFTER, 0); }
		public GreaterThanOrEqualConditionContext(ConditionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterGreaterThanOrEqualCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitGreaterThanOrEqualCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitGreaterThanOrEqualCondition(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LessThanOrEqualConditionContext extends ConditionContext {
		public List<Expression_atomContext> expression_atom() {
			return getRuleContexts(Expression_atomContext.class);
		}
		public Expression_atomContext expression_atom(int i) {
			return getRuleContext(Expression_atomContext.class,i);
		}
		public TerminalNode AT() { return getToken(BackendlessQueryParser.AT, 0); }
		public TerminalNode OR() { return getToken(BackendlessQueryParser.OR, 0); }
		public TerminalNode BEFORE() { return getToken(BackendlessQueryParser.BEFORE, 0); }
		public LessThanOrEqualConditionContext(ConditionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterLessThanOrEqualCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitLessThanOrEqualCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitLessThanOrEqualCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionContext condition() throws RecognitionException {
		return condition(0);
	}

	private ConditionContext condition(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ConditionContext _localctx = new ConditionContext(_ctx, _parentState);
		ConditionContext _prevctx = _localctx;
		int _startState = 38;
		enterRecursionRule(_localctx, 38, RULE_condition, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(340);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				{
				_localctx = new NotConditionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(262);
				_la = _input.LA(1);
				if ( !(_la==T__8 || _la==NOT) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(263);
				condition(14);
				}
				break;
			case 2:
				{
				_localctx = new NestedConditionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(264);
				match(T__6);
				setState(265);
				condition(0);
				setState(266);
				match(T__7);
				}
				break;
			case 3:
				{
				_localctx = new InConditionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(268);
				expression_atom(0);
				setState(270);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NOT) {
					{
					setState(269);
					match(NOT);
					}
				}

				setState(272);
				match(IN);
				setState(273);
				match(T__6);
				setState(276);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
				case 1:
					{
					setState(274);
					select_statement();
					}
					break;
				case 2:
					{
					setState(275);
					expression_atom(0);
					}
					break;
				}
				setState(285);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__4) {
					{
					{
					setState(278);
					match(T__4);
					setState(281);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
					case 1:
						{
						setState(279);
						select_statement();
						}
						break;
					case 2:
						{
						setState(280);
						expression_atom(0);
						}
						break;
					}
					}
					}
					setState(287);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(288);
				match(T__7);
				}
				break;
			case 4:
				{
				_localctx = new IsNullConditionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(290);
				expression_atom(0);
				setState(291);
				match(IS);
				setState(292);
				match(NULL);
				}
				break;
			case 5:
				{
				_localctx = new IsNotNullConditionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(294);
				expression_atom(0);
				setState(295);
				match(IS);
				setState(296);
				match(NOT);
				setState(297);
				match(NULL);
				}
				break;
			case 6:
				{
				_localctx = new LikeConditionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(299);
				expression_atom(0);
				setState(301);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NOT) {
					{
					setState(300);
					match(NOT);
					}
				}

				setState(303);
				match(LIKE);
				setState(304);
				expression_atom(0);
				}
				break;
			case 7:
				{
				_localctx = new EqualConditionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(306);
				expression_atom(0);
				setState(307);
				_la = _input.LA(1);
				if ( !(_la==T__11 || _la==AT) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(308);
				expression_atom(0);
				}
				break;
			case 8:
				{
				_localctx = new NotEqualConditionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(310);
				expression_atom(0);
				setState(311);
				_la = _input.LA(1);
				if ( !(_la==T__12 || _la==T__13) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(312);
				expression_atom(0);
				}
				break;
			case 9:
				{
				_localctx = new GreaterThanOrEqualConditionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(314);
				expression_atom(0);
				setState(319);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__14:
					{
					setState(315);
					match(T__14);
					}
					break;
				case AT:
					{
					setState(316);
					match(AT);
					setState(317);
					match(OR);
					setState(318);
					match(AFTER);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(321);
				expression_atom(0);
				}
				break;
			case 10:
				{
				_localctx = new GreaterThanConditionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(323);
				expression_atom(0);
				setState(324);
				_la = _input.LA(1);
				if ( !(_la==T__15 || _la==AFTER) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(325);
				expression_atom(0);
				}
				break;
			case 11:
				{
				_localctx = new LessThanOrEqualConditionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(327);
				expression_atom(0);
				setState(332);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__16:
					{
					setState(328);
					match(T__16);
					}
					break;
				case AT:
					{
					setState(329);
					match(AT);
					setState(330);
					match(OR);
					setState(331);
					match(BEFORE);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(334);
				expression_atom(0);
				}
				break;
			case 12:
				{
				_localctx = new LessThanConditionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(336);
				expression_atom(0);
				setState(337);
				_la = _input.LA(1);
				if ( !(_la==T__17 || _la==BEFORE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(338);
				expression_atom(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(350);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(348);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
					case 1:
						{
						_localctx = new AndConditionContext(new ConditionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_condition);
						setState(342);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(343);
						_la = _input.LA(1);
						if ( !(_la==T__9 || _la==AND) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(344);
						condition(14);
						}
						break;
					case 2:
						{
						_localctx = new OrConditionContext(new ConditionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_condition);
						setState(345);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(346);
						_la = _input.LA(1);
						if ( !(_la==T__10 || _la==OR) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(347);
						condition(13);
						}
						break;
					}
					} 
				}
				setState(352);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Expression_atomContext extends ParserRuleContext {
		public Expression_atomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression_atom; }
	 
		public Expression_atomContext() { }
		public void copyFrom(Expression_atomContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class DivisionExpressionAtomContext extends Expression_atomContext {
		public List<Expression_atomContext> expression_atom() {
			return getRuleContexts(Expression_atomContext.class);
		}
		public Expression_atomContext expression_atom(int i) {
			return getRuleContext(Expression_atomContext.class,i);
		}
		public DivisionExpressionAtomContext(Expression_atomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterDivisionExpressionAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitDivisionExpressionAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitDivisionExpressionAtom(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ModuloExpressionAtomContext extends Expression_atomContext {
		public List<Expression_atomContext> expression_atom() {
			return getRuleContexts(Expression_atomContext.class);
		}
		public Expression_atomContext expression_atom(int i) {
			return getRuleContext(Expression_atomContext.class,i);
		}
		public ModuloExpressionAtomContext(Expression_atomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterModuloExpressionAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitModuloExpressionAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitModuloExpressionAtom(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ConstantExpressionAtomContext extends Expression_atomContext {
		public ConstantContext constant() {
			return getRuleContext(ConstantContext.class,0);
		}
		public ConstantExpressionAtomContext(Expression_atomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterConstantExpressionAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitConstantExpressionAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitConstantExpressionAtom(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FunctionCallExpressionAtomContext extends Expression_atomContext {
		public Function_callContext function_call() {
			return getRuleContext(Function_callContext.class,0);
		}
		public FunctionCallExpressionAtomContext(Expression_atomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterFunctionCallExpressionAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitFunctionCallExpressionAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitFunctionCallExpressionAtom(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SubtractionExpressionAtomContext extends Expression_atomContext {
		public List<Expression_atomContext> expression_atom() {
			return getRuleContexts(Expression_atomContext.class);
		}
		public Expression_atomContext expression_atom(int i) {
			return getRuleContext(Expression_atomContext.class,i);
		}
		public SubtractionExpressionAtomContext(Expression_atomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterSubtractionExpressionAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitSubtractionExpressionAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitSubtractionExpressionAtom(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FullColumnNameExpressionAtomContext extends Expression_atomContext {
		public Full_column_nameContext full_column_name() {
			return getRuleContext(Full_column_nameContext.class,0);
		}
		public FullColumnNameExpressionAtomContext(Expression_atomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterFullColumnNameExpressionAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitFullColumnNameExpressionAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitFullColumnNameExpressionAtom(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MultiplicationExpressionAtomContext extends Expression_atomContext {
		public List<Expression_atomContext> expression_atom() {
			return getRuleContexts(Expression_atomContext.class);
		}
		public Expression_atomContext expression_atom(int i) {
			return getRuleContext(Expression_atomContext.class,i);
		}
		public MultiplicationExpressionAtomContext(Expression_atomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterMultiplicationExpressionAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitMultiplicationExpressionAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitMultiplicationExpressionAtom(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryMinusExpressionAtomContext extends Expression_atomContext {
		public Expression_atomContext expression_atom() {
			return getRuleContext(Expression_atomContext.class,0);
		}
		public UnaryMinusExpressionAtomContext(Expression_atomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterUnaryMinusExpressionAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitUnaryMinusExpressionAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitUnaryMinusExpressionAtom(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AdditionExpressionAtomContext extends Expression_atomContext {
		public List<Expression_atomContext> expression_atom() {
			return getRuleContexts(Expression_atomContext.class);
		}
		public Expression_atomContext expression_atom(int i) {
			return getRuleContext(Expression_atomContext.class,i);
		}
		public AdditionExpressionAtomContext(Expression_atomContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterAdditionExpressionAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitAdditionExpressionAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitAdditionExpressionAtom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Expression_atomContext expression_atom() throws RecognitionException {
		return expression_atom(0);
	}

	private Expression_atomContext expression_atom(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Expression_atomContext _localctx = new Expression_atomContext(_ctx, _parentState);
		Expression_atomContext _prevctx = _localctx;
		int _startState = 40;
		enterRecursionRule(_localctx, 40, RULE_expression_atom, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(359);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				{
				_localctx = new ConstantExpressionAtomContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(354);
				constant();
				}
				break;
			case 2:
				{
				_localctx = new FullColumnNameExpressionAtomContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(355);
				full_column_name();
				}
				break;
			case 3:
				{
				_localctx = new FunctionCallExpressionAtomContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(356);
				function_call();
				}
				break;
			case 4:
				{
				_localctx = new UnaryMinusExpressionAtomContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(357);
				match(T__18);
				setState(358);
				expression_atom(6);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(378);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(376);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
					case 1:
						{
						_localctx = new MultiplicationExpressionAtomContext(new Expression_atomContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression_atom);
						setState(361);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(362);
						match(T__19);
						setState(363);
						expression_atom(6);
						}
						break;
					case 2:
						{
						_localctx = new DivisionExpressionAtomContext(new Expression_atomContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression_atom);
						setState(364);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(365);
						match(T__20);
						setState(366);
						expression_atom(5);
						}
						break;
					case 3:
						{
						_localctx = new ModuloExpressionAtomContext(new Expression_atomContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression_atom);
						setState(367);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(368);
						match(T__21);
						setState(369);
						expression_atom(4);
						}
						break;
					case 4:
						{
						_localctx = new AdditionExpressionAtomContext(new Expression_atomContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression_atom);
						setState(370);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(371);
						match(T__22);
						setState(372);
						expression_atom(3);
						}
						break;
					case 5:
						{
						_localctx = new SubtractionExpressionAtomContext(new Expression_atomContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression_atom);
						setState(373);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(374);
						match(T__18);
						setState(375);
						expression_atom(2);
						}
						break;
					}
					} 
				}
				setState(380);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Full_column_nameContext extends ParserRuleContext {
		public Single_column_nameContext single_column_name() {
			return getRuleContext(Single_column_nameContext.class,0);
		}
		public Relation_columnContext relation_column() {
			return getRuleContext(Relation_columnContext.class,0);
		}
		public Full_column_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_full_column_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterFull_column_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitFull_column_name(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitFull_column_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Full_column_nameContext full_column_name() throws RecognitionException {
		Full_column_nameContext _localctx = new Full_column_nameContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_full_column_name);
		try {
			setState(383);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(381);
				single_column_name();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(382);
				relation_column();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Single_column_nameContext extends ParserRuleContext {
		public Simple_columnContext simple_column() {
			return getRuleContext(Simple_columnContext.class,0);
		}
		public Inverse_relation_columnContext inverse_relation_column() {
			return getRuleContext(Inverse_relation_columnContext.class,0);
		}
		public Single_column_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_single_column_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterSingle_column_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitSingle_column_name(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitSingle_column_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Single_column_nameContext single_column_name() throws RecognitionException {
		Single_column_nameContext _localctx = new Single_column_nameContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_single_column_name);
		try {
			setState(387);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(385);
				simple_column();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(386);
				inverse_relation_column();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Relation_columnContext extends ParserRuleContext {
		public List<Single_column_nameContext> single_column_name() {
			return getRuleContexts(Single_column_nameContext.class);
		}
		public Single_column_nameContext single_column_name(int i) {
			return getRuleContext(Single_column_nameContext.class,i);
		}
		public Relation_columnContext relation_column() {
			return getRuleContext(Relation_columnContext.class,0);
		}
		public Relation_columnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relation_column; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterRelation_column(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitRelation_column(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitRelation_column(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Relation_columnContext relation_column() throws RecognitionException {
		Relation_columnContext _localctx = new Relation_columnContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_relation_column);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(389);
			single_column_name();
			setState(390);
			match(T__2);
			setState(393);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				{
				setState(391);
				relation_column();
				}
				break;
			case 2:
				{
				setState(392);
				single_column_name();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Inverse_relation_columnContext extends ParserRuleContext {
		public Table_nameContext table_name() {
			return getRuleContext(Table_nameContext.class,0);
		}
		public List<Full_column_nameContext> full_column_name() {
			return getRuleContexts(Full_column_nameContext.class);
		}
		public Full_column_nameContext full_column_name(int i) {
			return getRuleContext(Full_column_nameContext.class,i);
		}
		public Inverse_relation_columnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inverse_relation_column; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterInverse_relation_column(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitInverse_relation_column(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitInverse_relation_column(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Inverse_relation_columnContext inverse_relation_column() throws RecognitionException {
		Inverse_relation_columnContext _localctx = new Inverse_relation_columnContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_inverse_relation_column);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(395);
			table_name();
			setState(396);
			match(T__0);
			setState(397);
			full_column_name();
			setState(398);
			match(T__1);
			setState(399);
			match(T__2);
			setState(400);
			full_column_name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Unknown_functionContext extends ParserRuleContext {
		public Unknown_nameContext unknown_name() {
			return getRuleContext(Unknown_nameContext.class,0);
		}
		public Expression_atomContext expression_atom() {
			return getRuleContext(Expression_atomContext.class,0);
		}
		public Unknown_functionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unknown_function; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterUnknown_function(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitUnknown_function(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitUnknown_function(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Unknown_functionContext unknown_function() throws RecognitionException {
		Unknown_functionContext _localctx = new Unknown_functionContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_unknown_function);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(402);
			unknown_name();
			setState(403);
			match(T__6);
			setState(404);
			expression_atom(0);
			setState(405);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AliasContext extends ParserRuleContext {
		public Regular_idContext regular_id() {
			return getRuleContext(Regular_idContext.class,0);
		}
		public AliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterAlias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitAlias(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitAlias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AliasContext alias() throws RecognitionException {
		AliasContext _localctx = new AliasContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_alias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(407);
			regular_id();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Table_nameContext extends ParserRuleContext {
		public Regular_idContext regular_id() {
			return getRuleContext(Regular_idContext.class,0);
		}
		public Table_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_table_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterTable_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitTable_name(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitTable_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Table_nameContext table_name() throws RecognitionException {
		Table_nameContext _localctx = new Table_nameContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_table_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(409);
			regular_id();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Simple_columnContext extends ParserRuleContext {
		public Regular_idContext regular_id() {
			return getRuleContext(Regular_idContext.class,0);
		}
		public Simple_columnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simple_column; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterSimple_column(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitSimple_column(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitSimple_column(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Simple_columnContext simple_column() throws RecognitionException {
		Simple_columnContext _localctx = new Simple_columnContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_simple_column);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			regular_id();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Unknown_nameContext extends ParserRuleContext {
		public Regular_idContext regular_id() {
			return getRuleContext(Regular_idContext.class,0);
		}
		public Unknown_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unknown_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterUnknown_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitUnknown_name(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitUnknown_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Unknown_nameContext unknown_name() throws RecognitionException {
		Unknown_nameContext _localctx = new Unknown_nameContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_unknown_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(413);
			regular_id();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstantContext extends ParserRuleContext {
		public ConstantContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constant; }
	 
		public ConstantContext() { }
		public void copyFrom(ConstantContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class NumericConstantContext extends ConstantContext {
		public Numeric_literalContext numeric_literal() {
			return getRuleContext(Numeric_literalContext.class,0);
		}
		public NumericConstantContext(ConstantContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterNumericConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitNumericConstant(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitNumericConstant(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringConstantContext extends ConstantContext {
		public String_literalContext string_literal() {
			return getRuleContext(String_literalContext.class,0);
		}
		public StringConstantContext(ConstantContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterStringConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitStringConstant(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitStringConstant(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NullConstantContext extends ConstantContext {
		public TerminalNode NULL() { return getToken(BackendlessQueryParser.NULL, 0); }
		public NullConstantContext(ConstantContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterNullConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitNullConstant(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitNullConstant(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BooleanConstantContext extends ConstantContext {
		public Boolean_literalContext boolean_literal() {
			return getRuleContext(Boolean_literalContext.class,0);
		}
		public BooleanConstantContext(ConstantContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterBooleanConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitBooleanConstant(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitBooleanConstant(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstantContext constant() throws RecognitionException {
		ConstantContext _localctx = new ConstantContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_constant);
		try {
			setState(419);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CHAR_STRING:
				_localctx = new StringConstantContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(415);
				string_literal();
				}
				break;
			case T__2:
			case INT:
				_localctx = new NumericConstantContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(416);
				numeric_literal();
				}
				break;
			case FALSE:
			case TRUE:
				_localctx = new BooleanConstantContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(417);
				boolean_literal();
				}
				break;
			case NULL:
				_localctx = new NullConstantContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(418);
				match(NULL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Boolean_literalContext extends ParserRuleContext {
		public Boolean_literalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_boolean_literal; }
	 
		public Boolean_literalContext() { }
		public void copyFrom(Boolean_literalContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class TrueBooleanConstantContext extends Boolean_literalContext {
		public TerminalNode TRUE() { return getToken(BackendlessQueryParser.TRUE, 0); }
		public TrueBooleanConstantContext(Boolean_literalContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterTrueBooleanConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitTrueBooleanConstant(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitTrueBooleanConstant(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FalseBooleanConstantContext extends Boolean_literalContext {
		public TerminalNode FALSE() { return getToken(BackendlessQueryParser.FALSE, 0); }
		public FalseBooleanConstantContext(Boolean_literalContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterFalseBooleanConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitFalseBooleanConstant(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitFalseBooleanConstant(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Boolean_literalContext boolean_literal() throws RecognitionException {
		Boolean_literalContext _localctx = new Boolean_literalContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_boolean_literal);
		try {
			setState(423);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TRUE:
				_localctx = new TrueBooleanConstantContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(421);
				match(TRUE);
				}
				break;
			case FALSE:
				_localctx = new FalseBooleanConstantContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(422);
				match(FALSE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Numeric_literalContext extends ParserRuleContext {
		public Numeric_literalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numeric_literal; }
	 
		public Numeric_literalContext() { }
		public void copyFrom(Numeric_literalContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class DecimalNumericConstantContext extends Numeric_literalContext {
		public TerminalNode INT() { return getToken(BackendlessQueryParser.INT, 0); }
		public DecimalNumericConstantContext(Numeric_literalContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterDecimalNumericConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitDecimalNumericConstant(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitDecimalNumericConstant(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FloatNumericConstantContext extends Numeric_literalContext {
		public List<TerminalNode> INT() { return getTokens(BackendlessQueryParser.INT); }
		public TerminalNode INT(int i) {
			return getToken(BackendlessQueryParser.INT, i);
		}
		public FloatNumericConstantContext(Numeric_literalContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterFloatNumericConstant(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitFloatNumericConstant(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitFloatNumericConstant(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Numeric_literalContext numeric_literal() throws RecognitionException {
		Numeric_literalContext _localctx = new Numeric_literalContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_numeric_literal);
		int _la;
		try {
			setState(431);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				_localctx = new DecimalNumericConstantContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(425);
				match(INT);
				}
				break;
			case 2:
				_localctx = new FloatNumericConstantContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(427);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==INT) {
					{
					setState(426);
					match(INT);
					}
				}

				setState(429);
				match(T__2);
				setState(430);
				match(INT);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class String_literalContext extends ParserRuleContext {
		public TerminalNode CHAR_STRING() { return getToken(BackendlessQueryParser.CHAR_STRING, 0); }
		public String_literalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_string_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterString_literal(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitString_literal(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitString_literal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final String_literalContext string_literal() throws RecognitionException {
		String_literalContext _localctx = new String_literalContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_string_literal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(433);
			match(CHAR_STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Regular_idContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(BackendlessQueryParser.ID, 0); }
		public Keyword_as_idContext keyword_as_id() {
			return getRuleContext(Keyword_as_idContext.class,0);
		}
		public Regular_idContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_regular_id; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterRegular_id(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitRegular_id(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitRegular_id(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Regular_idContext regular_id() throws RecognitionException {
		Regular_idContext _localctx = new Regular_idContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_regular_id);
		try {
			setState(437);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(435);
				match(ID);
				}
				break;
			case AFTER:
			case AND:
			case AT:
			case AS:
			case ASC:
			case AVG:
			case BEFORE:
			case BY:
			case COUNT:
			case DESC:
			case DISTANCE:
			case DISTANCE_ON_SPHERE:
			case DIV:
			case ESCAPE:
			case FALSE:
			case FT:
			case GROUP:
			case HAVING:
			case IN:
			case IS:
			case KM:
			case LIMIT:
			case LIKE:
			case MAX:
			case MI:
			case MIN:
			case MOD:
			case NOT:
			case NULL:
			case OFFSET:
			case OR:
			case SELECT:
			case SORT:
			case SUM:
			case TRUE:
			case YD:
				enterOuterAlt(_localctx, 2);
				{
				setState(436);
				keyword_as_id();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Keyword_as_idContext extends ParserRuleContext {
		public TerminalNode AFTER() { return getToken(BackendlessQueryParser.AFTER, 0); }
		public TerminalNode AND() { return getToken(BackendlessQueryParser.AND, 0); }
		public TerminalNode AT() { return getToken(BackendlessQueryParser.AT, 0); }
		public TerminalNode AS() { return getToken(BackendlessQueryParser.AS, 0); }
		public TerminalNode ASC() { return getToken(BackendlessQueryParser.ASC, 0); }
		public TerminalNode AVG() { return getToken(BackendlessQueryParser.AVG, 0); }
		public TerminalNode BEFORE() { return getToken(BackendlessQueryParser.BEFORE, 0); }
		public TerminalNode BY() { return getToken(BackendlessQueryParser.BY, 0); }
		public TerminalNode COUNT() { return getToken(BackendlessQueryParser.COUNT, 0); }
		public TerminalNode DESC() { return getToken(BackendlessQueryParser.DESC, 0); }
		public TerminalNode DISTANCE() { return getToken(BackendlessQueryParser.DISTANCE, 0); }
		public TerminalNode DISTANCE_ON_SPHERE() { return getToken(BackendlessQueryParser.DISTANCE_ON_SPHERE, 0); }
		public TerminalNode DIV() { return getToken(BackendlessQueryParser.DIV, 0); }
		public TerminalNode ESCAPE() { return getToken(BackendlessQueryParser.ESCAPE, 0); }
		public TerminalNode FALSE() { return getToken(BackendlessQueryParser.FALSE, 0); }
		public TerminalNode FT() { return getToken(BackendlessQueryParser.FT, 0); }
		public TerminalNode GROUP() { return getToken(BackendlessQueryParser.GROUP, 0); }
		public TerminalNode HAVING() { return getToken(BackendlessQueryParser.HAVING, 0); }
		public TerminalNode IN() { return getToken(BackendlessQueryParser.IN, 0); }
		public TerminalNode IS() { return getToken(BackendlessQueryParser.IS, 0); }
		public TerminalNode KM() { return getToken(BackendlessQueryParser.KM, 0); }
		public TerminalNode LIMIT() { return getToken(BackendlessQueryParser.LIMIT, 0); }
		public TerminalNode LIKE() { return getToken(BackendlessQueryParser.LIKE, 0); }
		public TerminalNode MAX() { return getToken(BackendlessQueryParser.MAX, 0); }
		public TerminalNode MI() { return getToken(BackendlessQueryParser.MI, 0); }
		public TerminalNode MIN() { return getToken(BackendlessQueryParser.MIN, 0); }
		public TerminalNode MOD() { return getToken(BackendlessQueryParser.MOD, 0); }
		public TerminalNode NOT() { return getToken(BackendlessQueryParser.NOT, 0); }
		public TerminalNode NULL() { return getToken(BackendlessQueryParser.NULL, 0); }
		public TerminalNode OFFSET() { return getToken(BackendlessQueryParser.OFFSET, 0); }
		public TerminalNode OR() { return getToken(BackendlessQueryParser.OR, 0); }
		public TerminalNode SELECT() { return getToken(BackendlessQueryParser.SELECT, 0); }
		public TerminalNode SORT() { return getToken(BackendlessQueryParser.SORT, 0); }
		public TerminalNode SUM() { return getToken(BackendlessQueryParser.SUM, 0); }
		public TerminalNode TRUE() { return getToken(BackendlessQueryParser.TRUE, 0); }
		public TerminalNode YD() { return getToken(BackendlessQueryParser.YD, 0); }
		public Keyword_as_idContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyword_as_id; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).enterKeyword_as_id(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BackendlessQueryListener ) ((BackendlessQueryListener)listener).exitKeyword_as_id(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof BackendlessQueryVisitor ) return ((BackendlessQueryVisitor<? extends T>)visitor).visitKeyword_as_id(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Keyword_as_idContext keyword_as_id() throws RecognitionException {
		Keyword_as_idContext _localctx = new Keyword_as_idContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_keyword_as_id);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(439);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << AFTER) | (1L << AND) | (1L << AT) | (1L << AS) | (1L << ASC) | (1L << AVG) | (1L << BEFORE) | (1L << BY) | (1L << COUNT) | (1L << DESC) | (1L << DISTANCE) | (1L << DISTANCE_ON_SPHERE) | (1L << DIV) | (1L << ESCAPE) | (1L << FALSE) | (1L << FT) | (1L << GROUP) | (1L << HAVING) | (1L << IN) | (1L << IS) | (1L << KM) | (1L << LIMIT) | (1L << LIKE) | (1L << MAX) | (1L << MI) | (1L << MIN) | (1L << MOD) | (1L << NOT) | (1L << NULL) | (1L << OFFSET) | (1L << OR) | (1L << SELECT) | (1L << SORT) | (1L << SUM) | (1L << TRUE) | (1L << YD))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 19:
			return condition_sempred((ConditionContext)_localctx, predIndex);
		case 20:
			return expression_atom_sempred((Expression_atomContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean condition_sempred(ConditionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 13);
		case 1:
			return precpred(_ctx, 12);
		}
		return true;
	}
	private boolean expression_atom_sempred(Expression_atomContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return precpred(_ctx, 5);
		case 3:
			return precpred(_ctx, 4);
		case 4:
			return precpred(_ctx, 3);
		case 5:
			return precpred(_ctx, 2);
		case 6:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3D\u01bc\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\3\2\3\2\3\2\5\2N\n\2\3\2\3\2\3\2\3\2\5\2"+
		"T\n\2\3\2\5\2W\n\2\3\2\5\2Z\n\2\3\2\5\2]\n\2\3\3\3\3\3\3\3\3\7\3c\n\3"+
		"\f\3\16\3f\13\3\3\3\3\3\3\3\5\3k\n\3\3\4\3\4\3\4\3\4\3\4\5\4r\n\4\3\5"+
		"\3\5\3\5\3\5\3\5\7\5y\n\5\f\5\16\5|\13\5\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3"+
		"\7\7\7\u0086\n\7\f\7\16\7\u0089\13\7\3\b\3\b\5\b\u008d\n\b\3\t\3\t\3\t"+
		"\3\t\5\t\u0093\n\t\3\n\3\n\3\n\3\n\3\n\5\n\u009a\n\n\3\13\3\13\3\13\5"+
		"\13\u009f\n\13\3\f\3\f\5\f\u00a3\n\f\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16"+
		"\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20\5\20\u00b6\n\20\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\5\21\u00ca\n\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u00d3"+
		"\n\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\5\23\u00eb\n\23\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u0106\n\24\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u0111\n\25\3\25\3\25\3\25\3\25"+
		"\5\25\u0117\n\25\3\25\3\25\3\25\5\25\u011c\n\25\7\25\u011e\n\25\f\25\16"+
		"\25\u0121\13\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\5\25\u0130\n\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u0142\n\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u014f\n\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\5\25\u0157\n\25\3\25\3\25\3\25\3\25\3\25\3\25\7\25\u015f\n"+
		"\25\f\25\16\25\u0162\13\25\3\26\3\26\3\26\3\26\3\26\3\26\5\26\u016a\n"+
		"\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3"+
		"\26\3\26\7\26\u017b\n\26\f\26\16\26\u017e\13\26\3\27\3\27\5\27\u0182\n"+
		"\27\3\30\3\30\5\30\u0186\n\30\3\31\3\31\3\31\3\31\5\31\u018c\n\31\3\32"+
		"\3\32\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\35"+
		"\3\35\3\36\3\36\3\37\3\37\3 \3 \3 \3 \5 \u01a6\n \3!\3!\5!\u01aa\n!\3"+
		"\"\3\"\5\"\u01ae\n\"\3\"\3\"\5\"\u01b2\n\"\3#\3#\3$\3$\5$\u01b8\n$\3%"+
		"\3%\3%\2\4(*&\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64"+
		"\668:<>@BDFH\2\13\4\2\36\36##\4\2\13\13\65\65\4\2\16\16\34\34\3\2\17\20"+
		"\4\2\22\22\32\32\4\2\24\24  \4\2\f\f\33\33\4\2\r\r88\3\2\32=\2\u01da\2"+
		"J\3\2\2\2\4j\3\2\2\2\6q\3\2\2\2\bs\3\2\2\2\n}\3\2\2\2\f\u0080\3\2\2\2"+
		"\16\u008a\3\2\2\2\20\u008e\3\2\2\2\22\u0099\3\2\2\2\24\u009e\3\2\2\2\26"+
		"\u00a2\3\2\2\2\30\u00a4\3\2\2\2\32\u00a9\3\2\2\2\34\u00ae\3\2\2\2\36\u00b5"+
		"\3\2\2\2 \u00c9\3\2\2\2\"\u00cb\3\2\2\2$\u00ea\3\2\2\2&\u0105\3\2\2\2"+
		"(\u0156\3\2\2\2*\u0169\3\2\2\2,\u0181\3\2\2\2.\u0185\3\2\2\2\60\u0187"+
		"\3\2\2\2\62\u018d\3\2\2\2\64\u0194\3\2\2\2\66\u0199\3\2\2\28\u019b\3\2"+
		"\2\2:\u019d\3\2\2\2<\u019f\3\2\2\2>\u01a5\3\2\2\2@\u01a9\3\2\2\2B\u01b1"+
		"\3\2\2\2D\u01b3\3\2\2\2F\u01b7\3\2\2\2H\u01b9\3\2\2\2JK\58\35\2KM\7\3"+
		"\2\2LN\5(\25\2ML\3\2\2\2MN\3\2\2\2NO\3\2\2\2OP\7\4\2\2PQ\7\5\2\2QS\5\4"+
		"\3\2RT\5\b\5\2SR\3\2\2\2ST\3\2\2\2TV\3\2\2\2UW\5\n\6\2VU\3\2\2\2VW\3\2"+
		"\2\2WY\3\2\2\2XZ\5\f\7\2YX\3\2\2\2YZ\3\2\2\2Z\\\3\2\2\2[]\5\20\t\2\\["+
		"\3\2\2\2\\]\3\2\2\2]\3\3\2\2\2^_\7\6\2\2_d\5\6\4\2`a\7\7\2\2ac\5\6\4\2"+
		"b`\3\2\2\2cf\3\2\2\2db\3\2\2\2de\3\2\2\2eg\3\2\2\2fd\3\2\2\2gh\7\b\2\2"+
		"hk\3\2\2\2ik\5\6\4\2j^\3\2\2\2ji\3\2\2\2k\5\3\2\2\2lr\5*\26\2mn\5*\26"+
		"\2no\7\35\2\2op\5\66\34\2pr\3\2\2\2ql\3\2\2\2qm\3\2\2\2r\7\3\2\2\2st\7"+
		"*\2\2tu\7!\2\2uz\5*\26\2vw\7\7\2\2wy\5*\26\2xv\3\2\2\2y|\3\2\2\2zx\3\2"+
		"\2\2z{\3\2\2\2{\t\3\2\2\2|z\3\2\2\2}~\7+\2\2~\177\5(\25\2\177\13\3\2\2"+
		"\2\u0080\u0081\7:\2\2\u0081\u0082\7!\2\2\u0082\u0087\5\16\b\2\u0083\u0084"+
		"\7\7\2\2\u0084\u0086\5\16\b\2\u0085\u0083\3\2\2\2\u0086\u0089\3\2\2\2"+
		"\u0087\u0085\3\2\2\2\u0087\u0088\3\2\2\2\u0088\r\3\2\2\2\u0089\u0087\3"+
		"\2\2\2\u008a\u008c\5*\26\2\u008b\u008d\t\2\2\2\u008c\u008b\3\2\2\2\u008c"+
		"\u008d\3\2\2\2\u008d\17\3\2\2\2\u008e\u008f\7/\2\2\u008f\u0092\7B\2\2"+
		"\u0090\u0091\7\67\2\2\u0091\u0093\7B\2\2\u0092\u0090\3\2\2\2\u0092\u0093"+
		"\3\2\2\2\u0093\21\3\2\2\2\u0094\u009a\5&\24\2\u0095\u009a\5$\23\2\u0096"+
		"\u009a\5\24\13\2\u0097\u009a\5\36\20\2\u0098\u009a\5\64\33\2\u0099\u0094"+
		"\3\2\2\2\u0099\u0095\3\2\2\2\u0099\u0096\3\2\2\2\u0099\u0097\3\2\2\2\u0099"+
		"\u0098\3\2\2\2\u009a\23\3\2\2\2\u009b\u009f\5\30\r\2\u009c\u009f\5\32"+
		"\16\2\u009d\u009f\5\34\17\2\u009e\u009b\3\2\2\2\u009e\u009c\3\2\2\2\u009e"+
		"\u009d\3\2\2\2\u009f\25\3\2\2\2\u00a0\u00a3\5:\36\2\u00a1\u00a3\5> \2"+
		"\u00a2\u00a0\3\2\2\2\u00a2\u00a1\3\2\2\2\u00a3\27\3\2\2\2\u00a4\u00a5"+
		"\7>\2\2\u00a5\u00a6\7\t\2\2\u00a6\u00a7\5\26\f\2\u00a7\u00a8\7\n\2\2\u00a8"+
		"\31\3\2\2\2\u00a9\u00aa\7?\2\2\u00aa\u00ab\7\t\2\2\u00ab\u00ac\5\26\f"+
		"\2\u00ac\u00ad\7\n\2\2\u00ad\33\3\2\2\2\u00ae\u00af\7@\2\2\u00af\u00b0"+
		"\7\t\2\2\u00b0\u00b1\5\26\f\2\u00b1\u00b2\7\n\2\2\u00b2\35\3\2\2\2\u00b3"+
		"\u00b6\5 \21\2\u00b4\u00b6\5\"\22\2\u00b5\u00b3\3\2\2\2\u00b5\u00b4\3"+
		"\2\2\2\u00b6\37\3\2\2\2\u00b7\u00b8\7$\2\2\u00b8\u00b9\7\t\2\2\u00b9\u00ba"+
		"\5\26\f\2\u00ba\u00bb\7\7\2\2\u00bb\u00bc\5\26\f\2\u00bc\u00bd\7\n\2\2"+
		"\u00bd\u00ca\3\2\2\2\u00be\u00bf\7$\2\2\u00bf\u00c0\7\t\2\2\u00c0\u00c1"+
		"\5*\26\2\u00c1\u00c2\7\7\2\2\u00c2\u00c3\5*\26\2\u00c3\u00c4\7\7\2\2\u00c4"+
		"\u00c5\5*\26\2\u00c5\u00c6\7\7\2\2\u00c6\u00c7\5*\26\2\u00c7\u00c8\7\n"+
		"\2\2\u00c8\u00ca\3\2\2\2\u00c9\u00b7\3\2\2\2\u00c9\u00be\3\2\2\2\u00ca"+
		"!\3\2\2\2\u00cb\u00cc\7%\2\2\u00cc\u00cd\7\t\2\2\u00cd\u00ce\5\26\f\2"+
		"\u00ce\u00cf\7\7\2\2\u00cf\u00d2\5\26\f\2\u00d0\u00d1\7\7\2\2\u00d1\u00d3"+
		"\5*\26\2\u00d2\u00d0\3\2\2\2\u00d2\u00d3\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4"+
		"\u00d5\7\n\2\2\u00d5#\3\2\2\2\u00d6\u00d7\7)\2\2\u00d7\u00d8\7\t\2\2\u00d8"+
		"\u00d9\5B\"\2\u00d9\u00da\7\n\2\2\u00da\u00eb\3\2\2\2\u00db\u00dc\7.\2"+
		"\2\u00dc\u00dd\7\t\2\2\u00dd\u00de\5B\"\2\u00de\u00df\7\n\2\2\u00df\u00eb"+
		"\3\2\2\2\u00e0\u00e1\7\62\2\2\u00e1\u00e2\7\t\2\2\u00e2\u00e3\5B\"\2\u00e3"+
		"\u00e4\7\n\2\2\u00e4\u00eb\3\2\2\2\u00e5\u00e6\7=\2\2\u00e6\u00e7\7\t"+
		"\2\2\u00e7\u00e8\5B\"\2\u00e8\u00e9\7\n\2\2\u00e9\u00eb\3\2\2\2\u00ea"+
		"\u00d6\3\2\2\2\u00ea\u00db\3\2\2\2\u00ea\u00e0\3\2\2\2\u00ea\u00e5\3\2"+
		"\2\2\u00eb%\3\2\2\2\u00ec\u00ed\7\37\2\2\u00ed\u00ee\7\t\2\2\u00ee\u00ef"+
		"\5*\26\2\u00ef\u00f0\7\n\2\2\u00f0\u0106\3\2\2\2\u00f1\u00f2\7\61\2\2"+
		"\u00f2\u00f3\7\t\2\2\u00f3\u00f4\5*\26\2\u00f4\u00f5\7\n\2\2\u00f5\u0106"+
		"\3\2\2\2\u00f6\u00f7\7\63\2\2\u00f7\u00f8\7\t\2\2\u00f8\u00f9\5*\26\2"+
		"\u00f9\u00fa\7\n\2\2\u00fa\u0106\3\2\2\2\u00fb\u00fc\7;\2\2\u00fc\u00fd"+
		"\7\t\2\2\u00fd\u00fe\5*\26\2\u00fe\u00ff\7\n\2\2\u00ff\u0106\3\2\2\2\u0100"+
		"\u0101\7\"\2\2\u0101\u0102\7\t\2\2\u0102\u0103\5*\26\2\u0103\u0104\7\n"+
		"\2\2\u0104\u0106\3\2\2\2\u0105\u00ec\3\2\2\2\u0105\u00f1\3\2\2\2\u0105"+
		"\u00f6\3\2\2\2\u0105\u00fb\3\2\2\2\u0105\u0100\3\2\2\2\u0106\'\3\2\2\2"+
		"\u0107\u0108\b\25\1\2\u0108\u0109\t\3\2\2\u0109\u0157\5(\25\20\u010a\u010b"+
		"\7\t\2\2\u010b\u010c\5(\25\2\u010c\u010d\7\n\2\2\u010d\u0157\3\2\2\2\u010e"+
		"\u0110\5*\26\2\u010f\u0111\7\65\2\2\u0110\u010f\3\2\2\2\u0110\u0111\3"+
		"\2\2\2\u0111\u0112\3\2\2\2\u0112\u0113\7,\2\2\u0113\u0116\7\t\2\2\u0114"+
		"\u0117\5\2\2\2\u0115\u0117\5*\26\2\u0116\u0114\3\2\2\2\u0116\u0115\3\2"+
		"\2\2\u0117\u011f\3\2\2\2\u0118\u011b\7\7\2\2\u0119\u011c\5\2\2\2\u011a"+
		"\u011c\5*\26\2\u011b\u0119\3\2\2\2\u011b\u011a\3\2\2\2\u011c\u011e\3\2"+
		"\2\2\u011d\u0118\3\2\2\2\u011e\u0121\3\2\2\2\u011f\u011d\3\2\2\2\u011f"+
		"\u0120\3\2\2\2\u0120\u0122\3\2\2\2\u0121\u011f\3\2\2\2\u0122\u0123\7\n"+
		"\2\2\u0123\u0157\3\2\2\2\u0124\u0125\5*\26\2\u0125\u0126\7-\2\2\u0126"+
		"\u0127\7\66\2\2\u0127\u0157\3\2\2\2\u0128\u0129\5*\26\2\u0129\u012a\7"+
		"-\2\2\u012a\u012b\7\65\2\2\u012b\u012c\7\66\2\2\u012c\u0157\3\2\2\2\u012d"+
		"\u012f\5*\26\2\u012e\u0130\7\65\2\2\u012f\u012e\3\2\2\2\u012f\u0130\3"+
		"\2\2\2\u0130\u0131\3\2\2\2\u0131\u0132\7\60\2\2\u0132\u0133\5*\26\2\u0133"+
		"\u0157\3\2\2\2\u0134\u0135\5*\26\2\u0135\u0136\t\4\2\2\u0136\u0137\5*"+
		"\26\2\u0137\u0157\3\2\2\2\u0138\u0139\5*\26\2\u0139\u013a\t\5\2\2\u013a"+
		"\u013b\5*\26\2\u013b\u0157\3\2\2\2\u013c\u0141\5*\26\2\u013d\u0142\7\21"+
		"\2\2\u013e\u013f\7\34\2\2\u013f\u0140\78\2\2\u0140\u0142\7\32\2\2\u0141"+
		"\u013d\3\2\2\2\u0141\u013e\3\2\2\2\u0142\u0143\3\2\2\2\u0143\u0144\5*"+
		"\26\2\u0144\u0157\3\2\2\2\u0145\u0146\5*\26\2\u0146\u0147\t\6\2\2\u0147"+
		"\u0148\5*\26\2\u0148\u0157\3\2\2\2\u0149\u014e\5*\26\2\u014a\u014f\7\23"+
		"\2\2\u014b\u014c\7\34\2\2\u014c\u014d\78\2\2\u014d\u014f\7 \2\2\u014e"+
		"\u014a\3\2\2\2\u014e\u014b\3\2\2\2\u014f\u0150\3\2\2\2\u0150\u0151\5*"+
		"\26\2\u0151\u0157\3\2\2\2\u0152\u0153\5*\26\2\u0153\u0154\t\7\2\2\u0154"+
		"\u0155\5*\26\2\u0155\u0157\3\2\2\2\u0156\u0107\3\2\2\2\u0156\u010a\3\2"+
		"\2\2\u0156\u010e\3\2\2\2\u0156\u0124\3\2\2\2\u0156\u0128\3\2\2\2\u0156"+
		"\u012d\3\2\2\2\u0156\u0134\3\2\2\2\u0156\u0138\3\2\2\2\u0156\u013c\3\2"+
		"\2\2\u0156\u0145\3\2\2\2\u0156\u0149\3\2\2\2\u0156\u0152\3\2\2\2\u0157"+
		"\u0160\3\2\2\2\u0158\u0159\f\17\2\2\u0159\u015a\t\b\2\2\u015a\u015f\5"+
		"(\25\20\u015b\u015c\f\16\2\2\u015c\u015d\t\t\2\2\u015d\u015f\5(\25\17"+
		"\u015e\u0158\3\2\2\2\u015e\u015b\3\2\2\2\u015f\u0162\3\2\2\2\u0160\u015e"+
		"\3\2\2\2\u0160\u0161\3\2\2\2\u0161)\3\2\2\2\u0162\u0160\3\2\2\2\u0163"+
		"\u0164\b\26\1\2\u0164\u016a\5> \2\u0165\u016a\5,\27\2\u0166\u016a\5\22"+
		"\n\2\u0167\u0168\7\25\2\2\u0168\u016a\5*\26\b\u0169\u0163\3\2\2\2\u0169"+
		"\u0165\3\2\2\2\u0169\u0166\3\2\2\2\u0169\u0167\3\2\2\2\u016a\u017c\3\2"+
		"\2\2\u016b\u016c\f\7\2\2\u016c\u016d\7\26\2\2\u016d\u017b\5*\26\b\u016e"+
		"\u016f\f\6\2\2\u016f\u0170\7\27\2\2\u0170\u017b\5*\26\7\u0171\u0172\f"+
		"\5\2\2\u0172\u0173\7\30\2\2\u0173\u017b\5*\26\6\u0174\u0175\f\4\2\2\u0175"+
		"\u0176\7\31\2\2\u0176\u017b\5*\26\5\u0177\u0178\f\3\2\2\u0178\u0179\7"+
		"\25\2\2\u0179\u017b\5*\26\4\u017a\u016b\3\2\2\2\u017a\u016e\3\2\2\2\u017a"+
		"\u0171\3\2\2\2\u017a\u0174\3\2\2\2\u017a\u0177\3\2\2\2\u017b\u017e\3\2"+
		"\2\2\u017c\u017a\3\2\2\2\u017c\u017d\3\2\2\2\u017d+\3\2\2\2\u017e\u017c"+
		"\3\2\2\2\u017f\u0182\5.\30\2\u0180\u0182\5\60\31\2\u0181\u017f\3\2\2\2"+
		"\u0181\u0180\3\2\2\2\u0182-\3\2\2\2\u0183\u0186\5:\36\2\u0184\u0186\5"+
		"\62\32\2\u0185\u0183\3\2\2\2\u0185\u0184\3\2\2\2\u0186/\3\2\2\2\u0187"+
		"\u0188\5.\30\2\u0188\u018b\7\5\2\2\u0189\u018c\5\60\31\2\u018a\u018c\5"+
		".\30\2\u018b\u0189\3\2\2\2\u018b\u018a\3\2\2\2\u018c\61\3\2\2\2\u018d"+
		"\u018e\58\35\2\u018e\u018f\7\3\2\2\u018f\u0190\5,\27\2\u0190\u0191\7\4"+
		"\2\2\u0191\u0192\7\5\2\2\u0192\u0193\5,\27\2\u0193\63\3\2\2\2\u0194\u0195"+
		"\5<\37\2\u0195\u0196\7\t\2\2\u0196\u0197\5*\26\2\u0197\u0198\7\n\2\2\u0198"+
		"\65\3\2\2\2\u0199\u019a\5F$\2\u019a\67\3\2\2\2\u019b\u019c\5F$\2\u019c"+
		"9\3\2\2\2\u019d\u019e\5F$\2\u019e;\3\2\2\2\u019f\u01a0\5F$\2\u01a0=\3"+
		"\2\2\2\u01a1\u01a6\5D#\2\u01a2\u01a6\5B\"\2\u01a3\u01a6\5@!\2\u01a4\u01a6"+
		"\7\66\2\2\u01a5\u01a1\3\2\2\2\u01a5\u01a2\3\2\2\2\u01a5\u01a3\3\2\2\2"+
		"\u01a5\u01a4\3\2\2\2\u01a6?\3\2\2\2\u01a7\u01aa\7<\2\2\u01a8\u01aa\7("+
		"\2\2\u01a9\u01a7\3\2\2\2\u01a9\u01a8\3\2\2\2\u01aaA\3\2\2\2\u01ab\u01b2"+
		"\7B\2\2\u01ac\u01ae\7B\2\2\u01ad\u01ac\3\2\2\2\u01ad\u01ae\3\2\2\2\u01ae"+
		"\u01af\3\2\2\2\u01af\u01b0\7\5\2\2\u01b0\u01b2\7B\2\2\u01b1\u01ab\3\2"+
		"\2\2\u01b1\u01ad\3\2\2\2\u01b2C\3\2\2\2\u01b3\u01b4\7C\2\2\u01b4E\3\2"+
		"\2\2\u01b5\u01b8\7A\2\2\u01b6\u01b8\5H%\2\u01b7\u01b5\3\2\2\2\u01b7\u01b6"+
		"\3\2\2\2\u01b8G\3\2\2\2\u01b9\u01ba\t\n\2\2\u01baI\3\2\2\2+MSVY\\djqz"+
		"\u0087\u008c\u0092\u0099\u009e\u00a2\u00b5\u00c9\u00d2\u00ea\u0105\u0110"+
		"\u0116\u011b\u011f\u012f\u0141\u014e\u0156\u015e\u0160\u0169\u017a\u017c"+
		"\u0181\u0185\u018b\u01a5\u01a9\u01ad\u01b1\u01b7";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}