// Generated from /home/max/Dev/Projects/IntellijProjects/TestVisitor/visitor/resources/BackendlessQuery.g4 by ANTLR 4.7.2
package com.backendless.persistence.offline.visitor.gen;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class BackendlessQueryLexer extends Lexer {
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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
			"T__17", "T__18", "T__19", "T__20", "T__21", "T__22", "AFTER", "AND", 
			"AT", "AS", "ASC", "AVG", "BEFORE", "BY", "COUNT", "DESC", "DISTANCE", 
			"DISTANCE_ON_SPHERE", "DIV", "ESCAPE", "FALSE", "FT", "GROUP", "HAVING", 
			"IN", "IS", "KM", "LIMIT", "LIKE", "MAX", "MI", "MIN", "MOD", "NOT", 
			"NULL", "OFFSET", "OR", "SELECT", "SORT", "SUM", "TRUE", "YD", "AsWKB", 
			"AsWKT", "AsGeoJSON", "ID", "INT", "CHAR_STRING", "WS", "DIGIT", "LETTER", 
			"NEWLINE", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", 
			"M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
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


	public BackendlessQueryLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "BackendlessQuery.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2D\u0224\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\4_\t_\4"+
		"`\t`\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3"+
		"\n\3\n\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17"+
		"\3\20\3\20\3\20\3\21\3\21\3\22\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25"+
		"\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32"+
		"\3\32\3\32\3\33\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\36\3\36"+
		"\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3 \3 \3 \3!\3!\3!\3!\3!"+
		"\3!\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3#\3#\3#\3#\3$\3$\3$\3$\3$\3$\3"+
		"$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3$\3%\3%\3%\3%\3&\3&\3&\3&\3&\3&\3"+
		"&\3\'\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3)\3)\3)\3)\3)\3)\3*\3*\3*\3*\3*\3"+
		"*\3*\3+\3+\3+\3,\3,\3,\3-\3-\3-\3.\3.\3.\3.\3.\3.\3/\3/\3/\3/\3/\3\60"+
		"\3\60\3\60\3\60\3\61\3\61\3\61\3\62\3\62\3\62\3\62\3\63\3\63\3\63\3\63"+
		"\3\64\3\64\3\64\3\64\3\65\3\65\3\65\3\65\3\65\3\66\3\66\3\66\3\66\3\66"+
		"\3\66\3\66\3\67\3\67\3\67\38\38\38\38\38\38\38\39\39\39\39\39\3:\3:\3"+
		":\3:\3;\3;\3;\3;\3;\3<\3<\3<\3=\3=\3=\3=\3=\3=\3>\3>\3>\3>\3>\3>\3?\3"+
		"?\3?\3?\3?\3?\3?\3?\3?\3?\3@\3@\5@\u01c6\n@\3@\3@\3@\7@\u01cb\n@\f@\16"+
		"@\u01ce\13@\3A\6A\u01d1\nA\rA\16A\u01d2\3B\3B\3B\3B\3B\7B\u01da\nB\fB"+
		"\16B\u01dd\13B\3B\3B\3C\6C\u01e2\nC\rC\16C\u01e3\3C\3C\3D\3D\3E\3E\3F"+
		"\5F\u01ed\nF\3F\3F\3G\3G\3H\3H\3I\3I\3J\3J\3K\3K\3L\3L\3M\3M\3N\3N\3O"+
		"\3O\3P\3P\3Q\3Q\3R\3R\3S\3S\3T\3T\3U\3U\3V\3V\3W\3W\3X\3X\3Y\3Y\3Z\3Z"+
		"\3[\3[\3\\\3\\\3]\3]\3^\3^\3_\3_\3`\3`\2\2a\3\3\5\4\7\5\t\6\13\7\r\b\17"+
		"\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+"+
		"\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+"+
		"U,W-Y.[/]\60_\61a\62c\63e\64g\65i\66k\67m8o9q:s;u<w=y>{?}@\177A\u0081"+
		"B\u0083C\u0085D\u0087\2\u0089\2\u008b\2\u008d\2\u008f\2\u0091\2\u0093"+
		"\2\u0095\2\u0097\2\u0099\2\u009b\2\u009d\2\u009f\2\u00a1\2\u00a3\2\u00a5"+
		"\2\u00a7\2\u00a9\2\u00ab\2\u00ad\2\u00af\2\u00b1\2\u00b3\2\u00b5\2\u00b7"+
		"\2\u00b9\2\u00bb\2\u00bd\2\u00bf\2\3\2!\4\2%&aa\5\2\f\f\17\17))\5\2\13"+
		"\f\17\17\"\"\3\2\62;\4\2C\\c|\4\2CCcc\4\2DDdd\4\2EEee\4\2FFff\4\2GGgg"+
		"\4\2HHhh\4\2IIii\4\2JJjj\4\2KKkk\4\2LLll\4\2MMmm\4\2NNnn\4\2OOoo\4\2P"+
		"Ppp\4\2QQqq\4\2RRrr\4\2SSss\4\2TTtt\4\2UUuu\4\2VVvv\4\2WWww\4\2XXxx\4"+
		"\2YYyy\4\2ZZzz\4\2[[{{\4\2\\\\||\2\u0210\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3"+
		"\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2"+
		"\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35"+
		"\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)"+
		"\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2"+
		"\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2"+
		"A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3"+
		"\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2"+
		"\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2"+
		"g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3"+
		"\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177\3"+
		"\2\2\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2\2\u0085\3\2\2\2\3\u00c1\3\2\2\2"+
		"\5\u00c3\3\2\2\2\7\u00c5\3\2\2\2\t\u00c7\3\2\2\2\13\u00c9\3\2\2\2\r\u00cb"+
		"\3\2\2\2\17\u00cd\3\2\2\2\21\u00cf\3\2\2\2\23\u00d1\3\2\2\2\25\u00d3\3"+
		"\2\2\2\27\u00d6\3\2\2\2\31\u00d9\3\2\2\2\33\u00db\3\2\2\2\35\u00de\3\2"+
		"\2\2\37\u00e1\3\2\2\2!\u00e4\3\2\2\2#\u00e6\3\2\2\2%\u00e9\3\2\2\2\'\u00eb"+
		"\3\2\2\2)\u00ed\3\2\2\2+\u00ef\3\2\2\2-\u00f1\3\2\2\2/\u00f3\3\2\2\2\61"+
		"\u00f5\3\2\2\2\63\u00fb\3\2\2\2\65\u00ff\3\2\2\2\67\u0102\3\2\2\29\u0105"+
		"\3\2\2\2;\u0109\3\2\2\2=\u010d\3\2\2\2?\u0114\3\2\2\2A\u0117\3\2\2\2C"+
		"\u011d\3\2\2\2E\u0122\3\2\2\2G\u012b\3\2\2\2I\u013e\3\2\2\2K\u0142\3\2"+
		"\2\2M\u0149\3\2\2\2O\u014f\3\2\2\2Q\u0152\3\2\2\2S\u0158\3\2\2\2U\u015f"+
		"\3\2\2\2W\u0162\3\2\2\2Y\u0165\3\2\2\2[\u0168\3\2\2\2]\u016e\3\2\2\2_"+
		"\u0173\3\2\2\2a\u0177\3\2\2\2c\u017a\3\2\2\2e\u017e\3\2\2\2g\u0182\3\2"+
		"\2\2i\u0186\3\2\2\2k\u018b\3\2\2\2m\u0192\3\2\2\2o\u0195\3\2\2\2q\u019c"+
		"\3\2\2\2s\u01a1\3\2\2\2u\u01a5\3\2\2\2w\u01aa\3\2\2\2y\u01ad\3\2\2\2{"+
		"\u01b3\3\2\2\2}\u01b9\3\2\2\2\177\u01c5\3\2\2\2\u0081\u01d0\3\2\2\2\u0083"+
		"\u01d4\3\2\2\2\u0085\u01e1\3\2\2\2\u0087\u01e7\3\2\2\2\u0089\u01e9\3\2"+
		"\2\2\u008b\u01ec\3\2\2\2\u008d\u01f0\3\2\2\2\u008f\u01f2\3\2\2\2\u0091"+
		"\u01f4\3\2\2\2\u0093\u01f6\3\2\2\2\u0095\u01f8\3\2\2\2\u0097\u01fa\3\2"+
		"\2\2\u0099\u01fc\3\2\2\2\u009b\u01fe\3\2\2\2\u009d\u0200\3\2\2\2\u009f"+
		"\u0202\3\2\2\2\u00a1\u0204\3\2\2\2\u00a3\u0206\3\2\2\2\u00a5\u0208\3\2"+
		"\2\2\u00a7\u020a\3\2\2\2\u00a9\u020c\3\2\2\2\u00ab\u020e\3\2\2\2\u00ad"+
		"\u0210\3\2\2\2\u00af\u0212\3\2\2\2\u00b1\u0214\3\2\2\2\u00b3\u0216\3\2"+
		"\2\2\u00b5\u0218\3\2\2\2\u00b7\u021a\3\2\2\2\u00b9\u021c\3\2\2\2\u00bb"+
		"\u021e\3\2\2\2\u00bd\u0220\3\2\2\2\u00bf\u0222\3\2\2\2\u00c1\u00c2\7]"+
		"\2\2\u00c2\4\3\2\2\2\u00c3\u00c4\7_\2\2\u00c4\6\3\2\2\2\u00c5\u00c6\7"+
		"\60\2\2\u00c6\b\3\2\2\2\u00c7\u00c8\7}\2\2\u00c8\n\3\2\2\2\u00c9\u00ca"+
		"\7.\2\2\u00ca\f\3\2\2\2\u00cb\u00cc\7\177\2\2\u00cc\16\3\2\2\2\u00cd\u00ce"+
		"\7*\2\2\u00ce\20\3\2\2\2\u00cf\u00d0\7+\2\2\u00d0\22\3\2\2\2\u00d1\u00d2"+
		"\7#\2\2\u00d2\24\3\2\2\2\u00d3\u00d4\7(\2\2\u00d4\u00d5\7(\2\2\u00d5\26"+
		"\3\2\2\2\u00d6\u00d7\7~\2\2\u00d7\u00d8\7~\2\2\u00d8\30\3\2\2\2\u00d9"+
		"\u00da\7?\2\2\u00da\32\3\2\2\2\u00db\u00dc\7#\2\2\u00dc\u00dd\7?\2\2\u00dd"+
		"\34\3\2\2\2\u00de\u00df\7>\2\2\u00df\u00e0\7@\2\2\u00e0\36\3\2\2\2\u00e1"+
		"\u00e2\7@\2\2\u00e2\u00e3\7?\2\2\u00e3 \3\2\2\2\u00e4\u00e5\7@\2\2\u00e5"+
		"\"\3\2\2\2\u00e6\u00e7\7>\2\2\u00e7\u00e8\7?\2\2\u00e8$\3\2\2\2\u00e9"+
		"\u00ea\7>\2\2\u00ea&\3\2\2\2\u00eb\u00ec\7/\2\2\u00ec(\3\2\2\2\u00ed\u00ee"+
		"\7,\2\2\u00ee*\3\2\2\2\u00ef\u00f0\7\61\2\2\u00f0,\3\2\2\2\u00f1\u00f2"+
		"\7\'\2\2\u00f2.\3\2\2\2\u00f3\u00f4\7-\2\2\u00f4\60\3\2\2\2\u00f5\u00f6"+
		"\5\u008dG\2\u00f6\u00f7\5\u0097L\2\u00f7\u00f8\5\u00b3Z\2\u00f8\u00f9"+
		"\5\u0095K\2\u00f9\u00fa\5\u00afX\2\u00fa\62\3\2\2\2\u00fb\u00fc\5\u008d"+
		"G\2\u00fc\u00fd\5\u00a7T\2\u00fd\u00fe\5\u0093J\2\u00fe\64\3\2\2\2\u00ff"+
		"\u0100\5\u008dG\2\u0100\u0101\5\u00b3Z\2\u0101\66\3\2\2\2\u0102\u0103"+
		"\5\u008dG\2\u0103\u0104\5\u00b1Y\2\u01048\3\2\2\2\u0105\u0106\5\u008d"+
		"G\2\u0106\u0107\5\u00b1Y\2\u0107\u0108\5\u0091I\2\u0108:\3\2\2\2\u0109"+
		"\u010a\5\u008dG\2\u010a\u010b\5\u00b7\\\2\u010b\u010c\5\u0099M\2\u010c"+
		"<\3\2\2\2\u010d\u010e\5\u008fH\2\u010e\u010f\5\u0095K\2\u010f\u0110\5"+
		"\u0097L\2\u0110\u0111\5\u00a9U\2\u0111\u0112\5\u00afX\2\u0112\u0113\5"+
		"\u0095K\2\u0113>\3\2\2\2\u0114\u0115\5\u008fH\2\u0115\u0116\5\u00bd_\2"+
		"\u0116@\3\2\2\2\u0117\u0118\5\u0091I\2\u0118\u0119\5\u00a9U\2\u0119\u011a"+
		"\5\u00b5[\2\u011a\u011b\5\u00a7T\2\u011b\u011c\5\u00b3Z\2\u011cB\3\2\2"+
		"\2\u011d\u011e\5\u0093J\2\u011e\u011f\5\u0095K\2\u011f\u0120\5\u00b1Y"+
		"\2\u0120\u0121\5\u0091I\2\u0121D\3\2\2\2\u0122\u0123\5\u0093J\2\u0123"+
		"\u0124\5\u009dO\2\u0124\u0125\5\u00b1Y\2\u0125\u0126\5\u00b3Z\2\u0126"+
		"\u0127\5\u008dG\2\u0127\u0128\5\u00a7T\2\u0128\u0129\5\u0091I\2\u0129"+
		"\u012a\5\u0095K\2\u012aF\3\2\2\2\u012b\u012c\5\u0093J\2\u012c\u012d\5"+
		"\u009dO\2\u012d\u012e\5\u00b1Y\2\u012e\u012f\5\u00b3Z\2\u012f\u0130\5"+
		"\u008dG\2\u0130\u0131\5\u00a7T\2\u0131\u0132\5\u0091I\2\u0132\u0133\5"+
		"\u0095K\2\u0133\u0134\7a\2\2\u0134\u0135\5\u00a9U\2\u0135\u0136\5\u00a7"+
		"T\2\u0136\u0137\7a\2\2\u0137\u0138\5\u00b1Y\2\u0138\u0139\5\u00abV\2\u0139"+
		"\u013a\5\u009bN\2\u013a\u013b\5\u0095K\2\u013b\u013c\5\u00afX\2\u013c"+
		"\u013d\5\u0095K\2\u013dH\3\2\2\2\u013e\u013f\5\u0093J\2\u013f\u0140\5"+
		"\u009dO\2\u0140\u0141\5\u00b7\\\2\u0141J\3\2\2\2\u0142\u0143\5\u0095K"+
		"\2\u0143\u0144\5\u00b1Y\2\u0144\u0145\5\u0091I\2\u0145\u0146\5\u008dG"+
		"\2\u0146\u0147\5\u00abV\2\u0147\u0148\5\u0095K\2\u0148L\3\2\2\2\u0149"+
		"\u014a\5\u0097L\2\u014a\u014b\5\u008dG\2\u014b\u014c\5\u00a3R\2\u014c"+
		"\u014d\5\u00b1Y\2\u014d\u014e\5\u0095K\2\u014eN\3\2\2\2\u014f\u0150\5"+
		"\u0097L\2\u0150\u0151\5\u00b3Z\2\u0151P\3\2\2\2\u0152\u0153\5\u0099M\2"+
		"\u0153\u0154\5\u00afX\2\u0154\u0155\5\u00a9U\2\u0155\u0156\5\u00b5[\2"+
		"\u0156\u0157\5\u00abV\2\u0157R\3\2\2\2\u0158\u0159\5\u009bN\2\u0159\u015a"+
		"\5\u008dG\2\u015a\u015b\5\u00b7\\\2\u015b\u015c\5\u009dO\2\u015c\u015d"+
		"\5\u00a7T\2\u015d\u015e\5\u0099M\2\u015eT\3\2\2\2\u015f\u0160\5\u009d"+
		"O\2\u0160\u0161\5\u00a7T\2\u0161V\3\2\2\2\u0162\u0163\5\u009dO\2\u0163"+
		"\u0164\5\u00b1Y\2\u0164X\3\2\2\2\u0165\u0166\5\u00a1Q\2\u0166\u0167\5"+
		"\u00a5S\2\u0167Z\3\2\2\2\u0168\u0169\5\u00a3R\2\u0169\u016a\5\u009dO\2"+
		"\u016a\u016b\5\u00a5S\2\u016b\u016c\5\u009dO\2\u016c\u016d\5\u00b3Z\2"+
		"\u016d\\\3\2\2\2\u016e\u016f\5\u00a3R\2\u016f\u0170\5\u009dO\2\u0170\u0171"+
		"\5\u00a1Q\2\u0171\u0172\5\u0095K\2\u0172^\3\2\2\2\u0173\u0174\5\u00a5"+
		"S\2\u0174\u0175\5\u008dG\2\u0175\u0176\5\u00bb^\2\u0176`\3\2\2\2\u0177"+
		"\u0178\5\u00a5S\2\u0178\u0179\5\u009dO\2\u0179b\3\2\2\2\u017a\u017b\5"+
		"\u00a5S\2\u017b\u017c\5\u009dO\2\u017c\u017d\5\u00a7T\2\u017dd\3\2\2\2"+
		"\u017e\u017f\5\u00a5S\2\u017f\u0180\5\u00a9U\2\u0180\u0181\5\u0093J\2"+
		"\u0181f\3\2\2\2\u0182\u0183\5\u00a7T\2\u0183\u0184\5\u00a9U\2\u0184\u0185"+
		"\5\u00b3Z\2\u0185h\3\2\2\2\u0186\u0187\5\u00a7T\2\u0187\u0188\5\u00b5"+
		"[\2\u0188\u0189\5\u00a3R\2\u0189\u018a\5\u00a3R\2\u018aj\3\2\2\2\u018b"+
		"\u018c\5\u00a9U\2\u018c\u018d\5\u0097L\2\u018d\u018e\5\u0097L\2\u018e"+
		"\u018f\5\u00b1Y\2\u018f\u0190\5\u0095K\2\u0190\u0191\5\u00b3Z\2\u0191"+
		"l\3\2\2\2\u0192\u0193\5\u00a9U\2\u0193\u0194\5\u00afX\2\u0194n\3\2\2\2"+
		"\u0195\u0196\5\u00b1Y\2\u0196\u0197\5\u0095K\2\u0197\u0198\5\u00a3R\2"+
		"\u0198\u0199\5\u0095K\2\u0199\u019a\5\u0091I\2\u019a\u019b\5\u00b3Z\2"+
		"\u019bp\3\2\2\2\u019c\u019d\5\u00b1Y\2\u019d\u019e\5\u00a9U\2\u019e\u019f"+
		"\5\u00afX\2\u019f\u01a0\5\u00b3Z\2\u01a0r\3\2\2\2\u01a1\u01a2\5\u00b1"+
		"Y\2\u01a2\u01a3\5\u00b5[\2\u01a3\u01a4\5\u00a5S\2\u01a4t\3\2\2\2\u01a5"+
		"\u01a6\5\u00b3Z\2\u01a6\u01a7\5\u00afX\2\u01a7\u01a8\5\u00b5[\2\u01a8"+
		"\u01a9\5\u0095K\2\u01a9v\3\2\2\2\u01aa\u01ab\5\u00bd_\2\u01ab\u01ac\5"+
		"\u0093J\2\u01acx\3\2\2\2\u01ad\u01ae\5\u008dG\2\u01ae\u01af\5\u00b1Y\2"+
		"\u01af\u01b0\5\u00b9]\2\u01b0\u01b1\5\u00a1Q\2\u01b1\u01b2\5\u008fH\2"+
		"\u01b2z\3\2\2\2\u01b3\u01b4\5\u008dG\2\u01b4\u01b5\5\u00b1Y\2\u01b5\u01b6"+
		"\5\u00b9]\2\u01b6\u01b7\5\u00a1Q\2\u01b7\u01b8\5\u00b3Z\2\u01b8|\3\2\2"+
		"\2\u01b9\u01ba\5\u008dG\2\u01ba\u01bb\5\u00b1Y\2\u01bb\u01bc\5\u0099M"+
		"\2\u01bc\u01bd\5\u0095K\2\u01bd\u01be\5\u00a9U\2\u01be\u01bf\5\u009fP"+
		"\2\u01bf\u01c0\5\u00b1Y\2\u01c0\u01c1\5\u00a9U\2\u01c1\u01c2\5\u00a7T"+
		"\2\u01c2~\3\2\2\2\u01c3\u01c6\5\u0089E\2\u01c4\u01c6\7a\2\2\u01c5\u01c3"+
		"\3\2\2\2\u01c5\u01c4\3\2\2\2\u01c6\u01cc\3\2\2\2\u01c7\u01cb\5\u0089E"+
		"\2\u01c8\u01cb\5\u0087D\2\u01c9\u01cb\t\2\2\2\u01ca\u01c7\3\2\2\2\u01ca"+
		"\u01c8\3\2\2\2\u01ca\u01c9\3\2\2\2\u01cb\u01ce\3\2\2\2\u01cc\u01ca\3\2"+
		"\2\2\u01cc\u01cd\3\2\2\2\u01cd\u0080\3\2\2\2\u01ce\u01cc\3\2\2\2\u01cf"+
		"\u01d1\5\u0087D\2\u01d0\u01cf\3\2\2\2\u01d1\u01d2\3\2\2\2\u01d2\u01d0"+
		"\3\2\2\2\u01d2\u01d3\3\2\2\2\u01d3\u0082\3\2\2\2\u01d4\u01db\7)\2\2\u01d5"+
		"\u01da\n\3\2\2\u01d6\u01d7\7)\2\2\u01d7\u01da\7)\2\2\u01d8\u01da\5\u008b"+
		"F\2\u01d9\u01d5\3\2\2\2\u01d9\u01d6\3\2\2\2\u01d9\u01d8\3\2\2\2\u01da"+
		"\u01dd\3\2\2\2\u01db\u01d9\3\2\2\2\u01db\u01dc\3\2\2\2\u01dc\u01de\3\2"+
		"\2\2\u01dd\u01db\3\2\2\2\u01de\u01df\7)\2\2\u01df\u0084\3\2\2\2\u01e0"+
		"\u01e2\t\4\2\2\u01e1\u01e0\3\2\2\2\u01e2\u01e3\3\2\2\2\u01e3\u01e1\3\2"+
		"\2\2\u01e3\u01e4\3\2\2\2\u01e4\u01e5\3\2\2\2\u01e5\u01e6\bC\2\2\u01e6"+
		"\u0086\3\2\2\2\u01e7\u01e8\t\5\2\2\u01e8\u0088\3\2\2\2\u01e9\u01ea\t\6"+
		"\2\2\u01ea\u008a\3\2\2\2\u01eb\u01ed\7\17\2\2\u01ec\u01eb\3\2\2\2\u01ec"+
		"\u01ed\3\2\2\2\u01ed\u01ee\3\2\2\2\u01ee\u01ef\7\f\2\2\u01ef\u008c\3\2"+
		"\2\2\u01f0\u01f1\t\7\2\2\u01f1\u008e\3\2\2\2\u01f2\u01f3\t\b\2\2\u01f3"+
		"\u0090\3\2\2\2\u01f4\u01f5\t\t\2\2\u01f5\u0092\3\2\2\2\u01f6\u01f7\t\n"+
		"\2\2\u01f7\u0094\3\2\2\2\u01f8\u01f9\t\13\2\2\u01f9\u0096\3\2\2\2\u01fa"+
		"\u01fb\t\f\2\2\u01fb\u0098\3\2\2\2\u01fc\u01fd\t\r\2\2\u01fd\u009a\3\2"+
		"\2\2\u01fe\u01ff\t\16\2\2\u01ff\u009c\3\2\2\2\u0200\u0201\t\17\2\2\u0201"+
		"\u009e\3\2\2\2\u0202\u0203\t\20\2\2\u0203\u00a0\3\2\2\2\u0204\u0205\t"+
		"\21\2\2\u0205\u00a2\3\2\2\2\u0206\u0207\t\22\2\2\u0207\u00a4\3\2\2\2\u0208"+
		"\u0209\t\23\2\2\u0209\u00a6\3\2\2\2\u020a\u020b\t\24\2\2\u020b\u00a8\3"+
		"\2\2\2\u020c\u020d\t\25\2\2\u020d\u00aa\3\2\2\2\u020e\u020f\t\26\2\2\u020f"+
		"\u00ac\3\2\2\2\u0210\u0211\t\27\2\2\u0211\u00ae\3\2\2\2\u0212\u0213\t"+
		"\30\2\2\u0213\u00b0\3\2\2\2\u0214\u0215\t\31\2\2\u0215\u00b2\3\2\2\2\u0216"+
		"\u0217\t\32\2\2\u0217\u00b4\3\2\2\2\u0218\u0219\t\33\2\2\u0219\u00b6\3"+
		"\2\2\2\u021a\u021b\t\34\2\2\u021b\u00b8\3\2\2\2\u021c\u021d\t\35\2\2\u021d"+
		"\u00ba\3\2\2\2\u021e\u021f\t\36\2\2\u021f\u00bc\3\2\2\2\u0220\u0221\t"+
		"\37\2\2\u0221\u00be\3\2\2\2\u0222\u0223\t \2\2\u0223\u00c0\3\2\2\2\13"+
		"\2\u01c5\u01ca\u01cc\u01d2\u01d9\u01db\u01e3\u01ec\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}