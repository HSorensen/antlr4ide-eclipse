package org.github.antlr4ide.editor;

import java.util.HashMap;
import org.antlr.parser.antlr4.ANTLRv4Lexer;
import org.eclipse.jface.text.rules.*;
import org.github.antlr4ide.editor.antlr.AntlrScanner;

public class ANTLRv4Scanner extends AntlrScanner  {

  	public ANTLRv4Scanner(boolean debug) { super(debug); }

	private static IToken stmtTextToken    = new org.eclipse.jface.text.rules.Token(IANTLRv4ColorConstants.STMT);
  	private static IToken constTextToken   = new org.eclipse.jface.text.rules.Token(IANTLRv4ColorConstants.CONST);
  	private static IToken commentTextToken = new org.eclipse.jface.text.rules.Token(IANTLRv4ColorConstants.COMMENT);
	
  	private static IToken parserRuleTextToken = new org.eclipse.jface.text.rules.Token(IANTLRv4ColorConstants.PARSER_RULE);
  	private static IToken lexerRuleTextToken = new org.eclipse.jface.text.rules.Token(IANTLRv4ColorConstants.LEXER_RULE);

  	
  	
	/**
	 * Map Antlr token types to eclipse text attribute tokens
	 * Keep the hilite map in sync with SyntaxColoringPreferencePage.java
	 */
	static {
	   setHilite(new HashMap<Integer, IToken>());
	   // Statements
	   getHilite().put(ANTLRv4Lexer.FRAGMENT, stmtTextToken);
	   getHilite().put(ANTLRv4Lexer.CHANNELS, stmtTextToken);
	   getHilite().put(ANTLRv4Lexer.PARSER,   stmtTextToken);
	   getHilite().put(ANTLRv4Lexer.LEXER,    stmtTextToken);
	   getHilite().put(ANTLRv4Lexer.OPTIONS,  stmtTextToken);
	   getHilite().put(ANTLRv4Lexer.TOKENS,   stmtTextToken);
	   getHilite().put(ANTLRv4Lexer.GRAMMAR,  stmtTextToken);
	   getHilite().put(ANTLRv4Lexer.IMPORT,   stmtTextToken);

	   // Constants
	   getHilite().put(ANTLRv4Lexer.INT,            constTextToken);
	   getHilite().put(ANTLRv4Lexer.STRING_LITERAL, constTextToken);
	   
	   // Comments
	   getHilite().put(ANTLRv4Lexer.DOC_COMMENT,   commentTextToken);
	   getHilite().put(ANTLRv4Lexer.BLOCK_COMMENT, commentTextToken);
	   getHilite().put(ANTLRv4Lexer.LINE_COMMENT,  commentTextToken);
	   
	   // References
	   getHilite().put(ANTLRv4Lexer.TOKEN_REF, lexerRuleTextToken);
	   getHilite().put(ANTLRv4Lexer.RULE_REF,  parserRuleTextToken);

	}
	
}
