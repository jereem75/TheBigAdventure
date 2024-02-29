package fr.uge.project.parser;


/**
 * Enumeration of tokens used in the parser.
 * Each token is associated with a regular expression pattern.
 */
public enum Token {
  /**
   * Represents an identifier token in the parser.
   */
  IDENTIFIER("[A-Za-z]+"),

  /**
   * Represents a number token in the parser.
   */
  NUMBER("[0-9]+"),

  /**
   * Represents a left parenthesis token in the parser.
   */
  LEFT_PARENS("\\("),

  /**
   * Represents a right parenthesis token in the parser.
   */
  RIGHT_PARENS("\\)"),

  /**
   * Represents a left bracket token in the parser.
   */
  LEFT_BRACKET("\\["),

  /**
   * Represents a right bracket token in the parser.
   */
  RIGHT_BRACKET("\\]"),

  /**
   * Represents a comma token in the parser.
   */
  COMMA(","),

  /**
   * Represents a colon token in the parser.
   */
  COLON(":"),

  /**
   * Represents a quote token in the parser.
   */
  QUOTE("\"\"\"[^\"]+\"\"\""),

  /**
   * Represents a new line token in the parser.
   */
  NEW_LINE("\\n");

	/**
   * The regular expression pattern associated with the token.
   */
  public final String regex;

  Token(String regex) {
    this.regex = regex;
  }
}

