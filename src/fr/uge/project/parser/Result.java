package fr.uge.project.parser;

import java.util.Objects;


/**
 * Represents the result of a parsing operation, consisting of a token and its associated content.
 *
 * @param token   The token associated with the parsed content.
 * @param content The content that was parsed.
 */
public record Result(Token token, String content) {
	/**
   * Constructs a new Result.
   *
   * @param token   The token associated with the parsed content.
   * @param content The content that was parsed.
   * @throws NullPointerException if either token or content is null.
   */

	public Result {
		Objects.requireNonNull(token);
		Objects.requireNonNull(content);
	}
}