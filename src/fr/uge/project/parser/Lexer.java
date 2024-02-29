package fr.uge.project.parser;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * Lexer class for tokenizing input text.
 */
public class Lexer {
	private static int nbLines = 1;
	private static final List<Token> TOKENS = List.of(Token.values());
	private static final Pattern PATTERN = Pattern
			.compile(TOKENS.stream().map(token -> "(" + token.regex + ")").collect(Collectors.joining("|")));

	private final String text;
	private final Matcher matcher;

	
	/**
   * Constructs a new Lexer instance with the provided text.
   *
   * @param text The input text to tokenize.
   * @throws NullPointerException if text is null.
   */

	public Lexer(String text) {
		this.text = Objects.requireNonNull(text);
		this.matcher = PATTERN.matcher(text);
	}

	
	/**
	 * Gets the next token result from the lexer.
	 * Advances the internal state of the lexer to the next token.
	 *
	 * @return The next token result, or {@code null} if there are no more tokens.
	 * @throws AssertionError If unexpected internal state is encountered.
	 */
	public Result nextResult() {
		var matches = matcher.find();
		if (!matches) {
			return null;
		}
		for (var group = 1; group <= matcher.groupCount(); group++) {
			var start = matcher.start(group);
			if (start != -1) {
				var end = matcher.end(group);
				var content = text.substring(start, end);
				var result = new Result(TOKENS.get(group - 1), content);
				if (result.token() == Token.NEW_LINE) {
					nbLines++;
					return nextResult();
				}
				return result;

			}
		}
		throw new AssertionError();
	}

	
	/**
   * Gets the current line number.
   *
   * @return The current line number.
   */
	public static int getNbLines() {
		return nbLines;
	}

	
	/**
	 * Advances the line number by the specified amount.
	 *
	 * @param toAdd The number of lines to add.
	 */
	public void addLines(int toAdd) {
		nbLines += toAdd;
	}

}
