package fr.uge.project.parser;

import java.util.HashMap;

import fr.uge.project.bigAdventure.ElementsSet;

/**
 * ElementReader class responsible for reading and processing elements from a
 * Lexer.
 */
public class ElementReader {
	private String kind = null;

	/**
	 * Checks if the specified attribute has already been read.
	 *
	 * @param toBeChecked  The attribute to be checked.
	 * @param element      The HashMap containing the analyzed element data.
	 * @param fileAnalysis The FileAnalysis object to check for already read
	 *                     attributes.
	 * @return True if the specified attribute has already been read, false
	 *         otherwise.
	 */
	private boolean hasBeenAlreadyRed(String toBeChecked, HashMap<String, String> element, FileAnalysis fileAnalysis) {
		if ((toBeChecked.equals("kind") && kind != null) || element.get(toBeChecked) != null) {
			fileAnalysis.setCanPlay(false);
			System.err.println("At line " + Lexer.getNbLines() + ": \"" + toBeChecked + "\" have alteady been read");
			return true;
		}
		return false;
	}

	/**
	 * Reads and processes the "name" parameter from the provided Lexer, updating
	 * the element data and FileAnalysis.
	 *
	 * @param lexer        The Lexer used for tokenizing input.
	 * @param element      The HashMap to store the analyzed element data.
	 * @param fileAnalysis The FileAnalysis object to update based on the analyzed
	 *                     "name" parameter.
	 */
	private void readName(Lexer lexer, HashMap<String, String> element, FileAnalysis fileAnalysis) {
		Result result;
		if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.COLON)) {
			if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.IDENTIFIER)) {
				if (!hasBeenAlreadyRed("name", element, fileAnalysis)) {
					element.put("name", result.content());
					return;
				}
			}
		}
		analyseTexte(lexer, result, element, fileAnalysis);
	}

	/**
	 * Verifies and sets the "kind" attribute based on the provided result content.
	 *
	 * @param result       The Result object containing information about the
	 *                     analyzed "kind" parameter.
	 * @param fileAnalysis The FileAnalysis object to update based on the verified
	 *                     "kind" parameter.
	 * @return True if the "kind" attribute is successfully verified and set, false
	 *         otherwise.
	 */
	private boolean verifyKind(Result result, FileAnalysis fileAnalysis) {
		switch (result.content()) {
		case "enemy":
			kind = result.content();
			return true;
		case "friend":
			kind = result.content();
			return true;
		case "item":
			kind = result.content();
			return true;
		case "obstacle":
			kind = result.content();
			return true;
		default:
			fileAnalysis.setCanPlay(false);
			System.err.println("At line " + Lexer.getNbLines() + ": The parameter for kind is incorrect");
			return false;
		}
	}

	/**
	 * Reads and processes the "kind" parameter from the provided Lexer, updating
	 * the element data and FileAnalysis.
	 *
	 * @param lexer        The Lexer used for tokenizing input.
	 * @param element      The HashMap to store the analyzed element data.
	 * @param fileAnalysis The FileAnalysis object to update based on the analyzed
	 *                     "kind" parameter.
	 */
	private void readKind(Lexer lexer, HashMap<String, String> element, FileAnalysis fileAnalysis) {
		if (kind == null) {
			Result result;
			if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.COLON)) {
				if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.IDENTIFIER)) {
					if (verifyKind(result, fileAnalysis)) {
						return;
					}
				}
				analyseTexte(lexer, result, element, fileAnalysis);
				return;
			}
		}
		fileAnalysis.setCanPlay(false);
		System.err.println("At line " + Lexer.getNbLines()
				+ ": Kind has already been read or the element is a player and cannot have a kind");
	}

	/**
	 * Reads and processes the "position" parameter from the provided Lexer,
	 * updating the element data and FileAnalysis.
	 *
	 * @param lexer        The Lexer used for tokenizing input.
	 * @param element      The HashMap to store the analyzed element data.
	 * @param fileAnalysis The FileAnalysis object to update based on the analyzed
	 *                     "position" parameter.
	 */
	private void readPosition(Lexer lexer, HashMap<String, String> element, FileAnalysis fileAnalysis) {
		Result result;
		if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.COLON)) {
			if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.LEFT_PARENS)) {
				if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.NUMBER)
						&& !hasBeenAlreadyRed("positionX", element, fileAnalysis)) {
					element.put("positionX", result.content());
					if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.COMMA)) {
						if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.NUMBER)
								&& !hasBeenAlreadyRed("positionY", element, fileAnalysis)) {
							element.put("positionY", result.content());
							if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.RIGHT_PARENS)) {
								return;
							}
						}
					}
				}
			}
		}
		analyseTexte(lexer, result, element, fileAnalysis);
	}

	/**
	 * Analyzes the player argument provided in the "player" parameter, updating
	 * FileAnalysis and element data.
	 *
	 * @param result       The Result object containing information about the
	 *                     analyzed player argument.
	 * @param fileAnalysis The FileAnalysis object to update based on the analyzed
	 *                     player argument.
	 * @return True if the player argument is valid and processed successfully,
	 *         false otherwise.
	 */
	private boolean analysPlayerArgument(Result result, FileAnalysis fileAnalysis) {
		if (result.content().equals("true")) {
			if (kind != null) {
				fileAnalysis.setCanPlay(false);
				System.err.println("At line " + Lexer.getNbLines() + ": The player is true but a kind has been alrady read");
				return false;
			}
			fileAnalysis.setPlayerAlreadyRead(true);
			kind = "player";
			return true;
		}
		if (result.content().equals("false")) {
			return true;
		}
		fileAnalysis.setCanPlay(false);
		System.err
				.println("At line " + Lexer.getNbLines() + ": Excepted true or false for player receved " + result.content());
		return false;
	}

	/**
	 * Reads and processes the "player" parameter from the provided Lexer, updating
	 * the element data and FileAnalysis.
	 *
	 * @param lexer        The Lexer used for tokenizing input.
	 * @param element      The HashMap to store the analyzed element data.
	 * @param fileAnalysis The FileAnalysis object to update based on the analyzed
	 *                     "player" parameter.
	 */
	private void readPlayer(Lexer lexer, HashMap<String, String> element, FileAnalysis fileAnalysis) {
		var result = lexer.nextResult();
		if (!fileAnalysis.isPlayerAlreadyRead()) {
			if (fileAnalysis.exceptedToken(result, Token.COLON)) {
				if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.IDENTIFIER)) {
					if (analysPlayerArgument(result, fileAnalysis)) {
						return;
					}
				}
			}
			analyseTexte(lexer, result = lexer.nextResult(), element, fileAnalysis);
			return;
		}
		fileAnalysis.setCanPlay(false);
		System.err.println("At line " + Lexer.getNbLines() + ": Player has already been read");
		analyseTexte(lexer, result, element, fileAnalysis);
	}

	/**
	 * Reads and processes the "health" parameter from the provided Lexer, updating
	 * the element data and FileAnalysis.
	 *
	 * @param lexer        The Lexer used for tokenizing input.
	 * @param element      The HashMap to store the analyzed element data.
	 * @param fileAnalysis The FileAnalysis object to update based on the analyzed
	 *                     "health" parameter.
	 */
	private void readHealth(Lexer lexer, HashMap<String, String> element, FileAnalysis fileAnalysis) {
		Result result;
		if ((result = lexer.nextResult()).token() == Token.COLON) {
			if ((result = lexer.nextResult()).token() == Token.NUMBER) {
				element.put("health", result.content());
				return;
			}
		}
		analyseTexte(lexer, result, element, fileAnalysis);
	}

	/**
	 * Reads and processes the "skin" parameter from the provided Lexer, updating
	 * the element data and FileAnalysis.
	 *
	 * @param lexer        The Lexer used for tokenizing input.
	 * @param element      The HashMap to store the analyzed element data.
	 * @param fileAnalysis The FileAnalysis object to update based on the analyzed
	 *                     "skin" parameter.
	 */
	private void readSkin(Lexer lexer, HashMap<String, String> element, FileAnalysis fileAnalysis) {
		Result result;
		if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.COLON)) {
			if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.IDENTIFIER)) {
				if (ElementsSet.contains(result.content()) && !hasBeenAlreadyRed("skin", element, fileAnalysis)) {
					element.put("skin", result.content());
					return;
				}
			}
		}
		analyseTexte(lexer, result, element, fileAnalysis);
	}

	/**
	 * Reads and processes the "damage" parameter from the provided Lexer, updating
	 * the element data and FileAnalysis.
	 *
	 * @param lexer        The Lexer used for tokenizing input.
	 * @param element      The HashMap to store the analyzed element data.
	 * @param fileAnalysis The FileAnalysis object to update based on the analyzed
	 *                     "damage" parameter.
	 */
	private void readDamage(Lexer lexer, HashMap<String, String> element, FileAnalysis fileAnalysis) {
		Result result;
		if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.COLON)) {
			if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.NUMBER)) {
				element.put("damage", result.content());
				return;
			}
		}
		analyseTexte(lexer, result, element, fileAnalysis);
	}

	/**
	 * Reads and processes the "behavior" parameter from the provided Lexer,
	 * updating the element data and FileAnalysis.
	 *
	 * @param lexer        The Lexer used for tokenizing input.
	 * @param element      The HashMap to store the analyzed element data.
	 * @param fileAnalysis The FileAnalysis object to update based on the analyzed
	 *                     "behavior" parameter.
	 */
	private void readBehavior(Lexer lexer, HashMap<String, String> element, FileAnalysis fileAnalysis) {
		Result result;
		if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.COLON)) {
			if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.IDENTIFIER)) {
				element.put("behavior", result.content());
				return;
			}
		}
		analyseTexte(lexer, result, element, fileAnalysis);
	}

	/**
	 * Reads and processes the "zone" parameter from the provided Lexer, updating
	 * the element data and FileAnalysis.
	 *
	 * @param lexer        The Lexer used for tokenizing input.
	 * @param element      The HashMap to store the analyzed element data.
	 * @param fileAnalysis The FileAnalysis object to update based on the analyzed
	 *                     "zone" parameter.
	 */
	private void readZone(Lexer lexer, HashMap<String, String> element, FileAnalysis fileAnalysis) {
		Result result;
		if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.COLON)) {
			if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.LEFT_PARENS)) {
				if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.NUMBER)) {
					element.put("zoneX", result.content());
					if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.COMMA)) {
						if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.NUMBER)) {
							element.put("zoneY", result.content());
							if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.RIGHT_PARENS)) {
								if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.LEFT_PARENS)) {
									if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.NUMBER)) {
										element.put("ligne", result.content());
										if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.IDENTIFIER)
												&& fileAnalysis.exceptedIdentifier(result, "x")) {
											if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.NUMBER)) {
												element.put("colonne", result.content());
												if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.RIGHT_PARENS)) {
													return;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		analyseTexte(lexer, result, element, fileAnalysis);
	}

	/**
	 * Analyzes the content of a Lexer result and delegates the processing based on
	 * the identified parameter.
	 *
	 * @param lexer        The Lexer used for tokenizing input.
	 * @param result       The Result object containing information about the
	 *                     analyzed content.
	 * @param element      The HashMap to store the analyzed element data.
	 * @param fileAnalysis The FileAnalysis object to update based on the analyzed
	 *                     content.
	 */
	private void analyseTexte(Lexer lexer, Result result, HashMap<String, String> element, FileAnalysis fileAnalysis) {
		if (result != null) {
			switch (result.content()) {
			case "name":
				readName(lexer, element, fileAnalysis);
				break;
			case "skin":
				readSkin(lexer, element, fileAnalysis);
				break;
			case "player":
				readPlayer(lexer, element, fileAnalysis);
				break;
			case "position":
				readPosition(lexer, element, fileAnalysis);
				break;
			case "health":
				readHealth(lexer, element, fileAnalysis);
				break;
			case "kind":
				readKind(lexer, element, fileAnalysis);
				break;
			case "damage":
				readDamage(lexer, element, fileAnalysis);
				break;
			case "zone":
				readZone(lexer, element, fileAnalysis);
				break;
			case "behavior":
				readBehavior(lexer, element, fileAnalysis);
				break;
			default:
				fileAnalysis.setCanPlay(false);
				System.err
						.println("At line " + Lexer.getNbLines() + ": The parameter \"" + result.content() + "\" is not valid");
				break;
			}
		}
	}

	/**
	 * Reads and analyzes an element from the provided Lexer, updating the
	 * FileAnalysis accordingly.
	 *
	 * @param lexer        The Lexer used for tokenizing input.
	 * @param fileAnalysis The FileAnalysis object to update with the analyzed
	 *                     element data.
	 */
	public void readElement(Lexer lexer, FileAnalysis fileAnalysis) {
		kind = null;
		Result result;
		HashMap<String, String> element = new HashMap<>();
		while ((result = lexer.nextResult()) != null) {
			if (result.token() == Token.LEFT_BRACKET) {
				break;
			}
			analyseTexte(lexer, result, element, fileAnalysis);
		}
		if (kind == null) {
			fileAnalysis.setCanPlay(false);
			System.err.println("At line " + (Lexer.getNbLines() - 1) + ": The element must have a kind or be a player");
		}
		element.put("kind", kind);
		fileAnalysis.addDataOfElement(element);
	}

}