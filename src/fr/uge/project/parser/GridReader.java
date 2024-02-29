package fr.uge.project.parser;

import fr.uge.project.bigAdventure.ElementsSet;

/**
 * Class responsible for reading and processing grid-related information from
 * the input file.
 */
public class GridReader {
	private boolean sizeAlreadyRead = false;
	private boolean encodingsAlreadyRead = false;
	private boolean dataAlreadyRead = false;
	private int lineDataRead;

	/**
	 * Reads special identifiers such as "encodings," "data," and "size" from the
	 * lexer results. If the identifier is found, it triggers the corresponding
	 * method and returns true.
	 *
	 * @param lexer         The lexer object to retrieve the next result.
	 * @param result        The current result from the lexer (can be null).
	 * @param wantRecursive A boolean indicating whether recursive calls are
	 *                      allowed.
	 * @param fileAnalysis  The FileAnalysis object for processing the read data.
	 * @return True if a special identifier was successfully processed, false
	 *         otherwise.
	 */
	private boolean readSpecialIdentifier(Lexer lexer, Result result, boolean wantRecursive, FileAnalysis fileAnalysis) {
		if (result == null) {
			result = lexer.nextResult();
		}
		if (result != null) {
			if (result.content().equals("encodings")) {
				readEncoding(lexer, fileAnalysis);
				return true;
			}
			if (result.content().equals("data")) {
				readData(lexer, fileAnalysis);
				return true;
			}
			if (result.content().equals("size")) {
				readSize(lexer, fileAnalysis);
				return true;
			}
			if (result.token() == Token.LEFT_BRACKET) {
				return true;
			}
			if (wantRecursive) {
				return readSpecialIdentifier(lexer, null, true, fileAnalysis);
			}
			return false;
		}
		return true;
	}

	/**
	 * Checks if the specified integer value meets the size requirements and updates
	 * FileAnalysis accordingly.
	 *
	 * @param toBeChecked  The integer value to be checked.
	 * @param sizeNumber   A string indicating whether the check is for the first or
	 *                     second parameter of the size.
	 * @param fileAnalysis The FileAnalysis object to update based on the check
	 *                     results.
	 */
	private void checkSizeInteger(int toBeChecked, String sizeNumber, FileAnalysis fileAnalysis) {
		if (toBeChecked < 1) {
			fileAnalysis.setCanPlay(false);
			System.err.println(
					"At line " + Lexer.getNbLines() + ": The " + sizeNumber + " parameter of the size must be grater than one");
			return;
		}
		if (sizeNumber.equals("first")) {
			fileAnalysis.setColumn(toBeChecked);
			return;
		}
		fileAnalysis.setLine(toBeChecked);
	}

	/**
	 * Checks if the specified information type has already been read and updates
	 * FileAnalysis accordingly.
	 *
	 * @param toBeChecked  A boolean indicating whether the information type has
	 *                     already been read.
	 * @param type         The type of information to check (e.g., "Size",
	 *                     "Encodings", "Data").
	 * @param fileAnalysis The FileAnalysis object to update in case the information
	 *                     type has already been read.
	 * @return True if the information type has already been read, false otherwise.
	 */
	private boolean checkAlreadyRead(boolean toBeChecked, String type, FileAnalysis fileAnalysis) {
		if (toBeChecked) {
			fileAnalysis.setCanPlay(false);
			System.err.println("At line " + Lexer.getNbLines() + ": " + type + " have alteady been read");
			return true;
		}
		return false;
	}

	/**
	 * Reads and verifies the size information from the lexer and updates the
	 * FileAnalysis accordingly.
	 *
	 * @param lexer        The lexer used for parsing the input.
	 * @param fileAnalysis The FileAnalysis object to update with size information.
	 */
	private void readSize(Lexer lexer, FileAnalysis fileAnalysis) {
		if (!checkAlreadyRead(sizeAlreadyRead, "Size", fileAnalysis)) {
			sizeAlreadyRead = true;
			var result = lexer.nextResult();
			if (fileAnalysis.exceptedToken(result, Token.COLON)) {
				if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.LEFT_PARENS)) {
					if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.NUMBER)) {
						checkSizeInteger(Integer.parseInt(result.content()), "first", fileAnalysis);
						if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.IDENTIFIER)
								&& fileAnalysis.exceptedIdentifier(result, "x")) {
							if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.NUMBER)) {
								checkSizeInteger(Integer.parseInt(result.content()), "second", fileAnalysis);
								if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.RIGHT_PARENS)) {
									readSpecialIdentifier(lexer, null, true, fileAnalysis);
									return;
								}
							}
						}
					}
				}
			}
			readSpecialIdentifier(lexer, result, true, fileAnalysis);
			return;
		}
		readSpecialIdentifier(lexer, null, true, fileAnalysis);
	}

	/**
	 * Verifies if the given element is valid and adds the corresponding encoding to
	 * FileAnalysis.
	 *
	 * @param element      The element to check for validity.
	 * @param code         The character code associated with the element.
	 * @param fileAnalysis The FileAnalysis object to update with the encoding.
	 */
	private void verifyAndAddEncoding(String element, char code, FileAnalysis fileAnalysis) {
		if (ElementsSet.contains(element)) {
			fileAnalysis.addGridEncoding(code, element);
			return;
		}
		fileAnalysis.setCanPlay(false);
		System.err
				.println("At line " + Lexer.getNbLines() + ": The element " + element + " cannot be used in the encoding");
	}

	/**
	 * Reads and processes the encoding section in the file using the provided lexer
	 * and updates FileAnalysis accordingly.
	 *
	 * @param lexer        The lexer to use for reading tokens.
	 * @param fileAnalysis The FileAnalysis object to update based on the read
	 *                     encoding.
	 */
	private void readEncoding(Lexer lexer, FileAnalysis fileAnalysis) {
		if (!checkAlreadyRead(encodingsAlreadyRead, "Encodings", fileAnalysis)) {
			encodingsAlreadyRead = true;
			Result result = lexer.nextResult();
			fileAnalysis.exceptedToken(result, Token.COLON);
			while (true) {
				result = lexer.nextResult();
				if (readSpecialIdentifier(lexer, result, false, fileAnalysis)) {
					return;
				}
				if (fileAnalysis.exceptedToken(result, Token.IDENTIFIER)) {
					String element = result.content();
					if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.LEFT_PARENS)) {
						if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.IDENTIFIER)
								&& result.content().length() == 1) {
							verifyAndAddEncoding(element, result.content().charAt(0), fileAnalysis);
							if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.RIGHT_PARENS)) {
								continue;
							}
						}
					}
					if (readSpecialIdentifier(lexer, result, false, fileAnalysis)) {
						return;
					}
				}
			}
		}
		readSpecialIdentifier(lexer, null, true, fileAnalysis);
	}

	/**
	 * Counts the number of lines in the given input string.
	 *
	 * @param inputString The input string to count lines for.
	 * @return The number of lines in the input string.
	 */
	private int countLines(String inputString) {
		String[] lines = inputString.split("\n");
		return lines.length;
	}

	/**
	 * Reads and processes the data section in the file using the provided lexer and
	 * updates FileAnalysis accordingly.
	 *
	 * @param lexer        The lexer to use for reading tokens.
	 * @param fileAnalysis The FileAnalysis object to update based on the read data.
	 */
	private void readData(Lexer lexer, FileAnalysis fileAnalysis) {
		if (!checkAlreadyRead(dataAlreadyRead, "Data", fileAnalysis)) {
			lineDataRead = Lexer.getNbLines();
			dataAlreadyRead = true;
			Result result = lexer.nextResult();
			if (fileAnalysis.exceptedToken(result, Token.COLON)) {
				if (fileAnalysis.exceptedToken((result = lexer.nextResult()), Token.QUOTE)) {
					fileAnalysis.setMapToRead(result.content());
					lexer.addLines(countLines(fileAnalysis.getMapToRead()));
					readSpecialIdentifier(lexer, null, true, fileAnalysis);
					return;
				}
			}
			readSpecialIdentifier(lexer, result, true, fileAnalysis);
		}
		readSpecialIdentifier(lexer, null, true, fileAnalysis);
	}

	/**
	 * Checks if there is a reference for the specified character in the encoding
	 * and updates FileAnalysis accordingly.
	 *
	 * @param actuel       The character for which to check the encoding reference.
	 * @param fileAnalysis The FileAnalysis object to check and update based on the
	 *                     encoding.
	 * @param line         The current line being processed.
	 */
	private void inEncoding(char actuel, FileAnalysis fileAnalysis, int line) {
		String res = fileAnalysis.getGridEncoding().get(actuel);
		if (res == null) {
			fileAnalysis.setCanPlay(false);
			System.err.println(
					"At line " + (line + lineDataRead + 1) + ": No reference for the letter " + actuel + " in the encoding");
		}
	}

	/**
	 * Finds the margin of data in the specified data string and updates
	 * FileAnalysis accordingly.
	 *
	 * @param data         The data string to find the margin for.
	 * @param fileAnalysis The FileAnalysis object to update based on the found
	 *                     margin.
	 * @return The found margin of data.
	 */
	private int foundMarginOfData(String data, FileAnalysis fileAnalysis) {
		int count = 0, read = 4, line = 1;
		while (true) {
			char current = data.charAt(read);
			switch (current) {
			case '\n':
				fileAnalysis.setCanPlay(false);
				System.err.println("At line " + (lineDataRead + line) + ": The line of data is empty");
				line++;
				read++;
				count = 0;
				break;
			case ' ':
				read++;
				count++;
				break;
			default:
				count++;
				fileAnalysis.setToReadMargin(count);
				return count;
			}
		}
	}

	/**
	 * Checks if the current column size matches the expected width and updates
	 * FileAnalysis accordingly.
	 *
	 * @param line         The current line being checked.
	 * @param column       The current column being checked.
	 * @param fileAnalysis The FileAnalysis object to check and update based on the
	 *                     column size.
	 */
	private void checkColumnSize(int line, int column, FileAnalysis fileAnalysis) {
		if (fileAnalysis.getColumn() != -1) {
			if ((column) != fileAnalysis.getColumn()) {
				fileAnalysis.setCanPlay(false);
				System.err.println("At line " + (line + lineDataRead + 1) + ": Error with the width");
			}
		}
	}

	/**
	 * Checks if the current line size matches the expected height in data and
	 * updates FileAnalysis accordingly.
	 *
	 * @param line         The current line being checked.
	 * @param fileAnalysis The FileAnalysis object to check and update based on the
	 *                     line size.
	 */
	private void checkLineSize(int line, FileAnalysis fileAnalysis) {
		if (fileAnalysis.getLine() != -1) {
			if ((line) != fileAnalysis.getLine()) {
				fileAnalysis.setCanPlay(false);
				System.err.println("At line " + (line + lineDataRead + 1) + ": Error with the height in data");
			}
		}
	}

	/**
	 * Analyzes the data in the specified map, updating FileAnalysis based on the
	 * map content.
	 *
	 * @param map          The map data to be analyzed.
	 * @param fileAnalysis The FileAnalysis object to update based on the analyzed
	 *                     map content.
	 */
	private void analysOfData(String map, FileAnalysis fileAnalysis) {
		int line = 0;
		int column = 0;
		int length = map.length();
		int margin = foundMarginOfData(map, fileAnalysis);
		int read = margin + 3;
		while (read != length) {
			char current = map.charAt(read);
			switch (current) {
			case '\n':
				checkColumnSize(line, column, fileAnalysis);
				line++;
				column = 0;
				read += margin;
				break;
			case '"':
				checkLineSize(line, fileAnalysis);
				return;
			default:
				inEncoding(current, fileAnalysis, line);
				column++;
				read++;
			}
		}
	}

	/**
	 * Checks if all required elements in the [grid] section have been read and
	 * updates FileAnalysis accordingly.
	 *
	 * @param fileAnalysis The FileAnalysis object to check and update based on the
	 *                     read status.
	 */
	private void everythingHasBeenRead(FileAnalysis fileAnalysis) {
		if (!sizeAlreadyRead) {
			fileAnalysis.setCanPlay(false);
			System.err.println("At line " + Lexer.getNbLines() + ": The size has not been read in [grid]");
		}
		if (!encodingsAlreadyRead) {
			fileAnalysis.setCanPlay(false);
			System.err.println("At line " + Lexer.getNbLines() + ": The encoding has not been read in [grid]");
		}
		if (!dataAlreadyRead) {
			fileAnalysis.setCanPlay(false);
			System.err.println("At line " + Lexer.getNbLines() + ": The data has not been read in [grid]");
		}
	}

	/**
	 * Reads and processes the grid information from the provided Lexer, updating
	 * the FileAnalysis.
	 *
	 * @param lexer        The Lexer used for tokenizing input.
	 * @param fileAnalysis The FileAnalysis object to update based on the analyzed
	 *                     grid information.
	 */
	public void grid(Lexer lexer, FileAnalysis fileAnalysis) {
		if (!checkAlreadyRead(fileAnalysis.isGreedHasBeenRead(), "[grid]", fileAnalysis)) {
			fileAnalysis.setGreedHasBeenRead(true);
			fileAnalysis.addGridEncoding(' ', "NOTHING");
			readSpecialIdentifier(lexer, null, true, fileAnalysis);
			if (fileAnalysis.getMapToRead() != null && fileAnalysis.getGridEncoding().size() > 1) {
				analysOfData(fileAnalysis.getMapToRead(), fileAnalysis);
			}
			everythingHasBeenRead(fileAnalysis);
		}
	}
}
