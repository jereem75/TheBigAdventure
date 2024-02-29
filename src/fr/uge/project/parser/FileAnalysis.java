package fr.uge.project.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Represents an analysis of a file, extracting information for game initialization.
 * This class is responsible for parsing the file content and collecting data needed
 * to initialize the game environment.
 */
public class FileAnalysis {
	private Map<Character, String> gridEncoding = new HashMap<>();
	private boolean playerAlreadyRead = false;
	private int line = -1, column = -1;
	private String mapToRead = null;
	private int toReadMargin;
	private boolean greedHasBeenRead = false;
	private boolean canPlay = true;
	private List<HashMap<String, String>> dataOfElements = new ArrayList<>();

	
	/**
	 * Checks if the result matches the expected token.
	 *
	 * @param result   The result obtained from the lexer.
	 * @param excepted The expected token.
	 * @return True if the result matches the expected token, false otherwise.
	 */
	public boolean exceptedToken(Result result, Token excepted) {
		if (result == null) {
			canPlay = false;
			System.err.println("At line " + Lexer.getNbLines() + ": Excepted " + excepted + " received end of file");
			return false;
		}
		if (result.token() != excepted) {
			canPlay = false;
			System.err.println("At line " + Lexer.getNbLines() + ": Excepted " + excepted + " received " + result.token());
			return false;
		}
		return true;
	}

	
	/**
	 * Checks whether the expected identifier matches the content of the provided lexer result.
	 *
	 * @param result   The result from the lexer.
	 * @param excepted The expected identifier.
	 * @return true if the result contains the expected identifier, false otherwise.
	 */
	public boolean exceptedIdentifier(Result result, String excepted) {
		if (result == null) {
			canPlay = false;
			System.err.println("At line " + Lexer.getNbLines() + ": Excepted " + excepted + " received end of file");
			return false;
		}
		if (!result.content().equals(excepted)) {
			canPlay = false;
			System.err.println("At line " + Lexer.getNbLines() + ": Excepted " + excepted + " received " + result.content());
			return false;
		}
		return true;
	}
	
	
	/**
	 * Gets the line number associated with the current position in the parsed file.
	 *
	 * @return The line number.
	 */
	public int getLine() {
		return line;
	}

	
	/**
	 * Sets the line number associated with the current position in the parsed file.
	 *
	 * @param line The new line number to be set.
	 */
	public void setLine(int line) {
		this.line = line;
	}

	
	/**
	 * Gets the column number associated with the current position in the parsed file.
	 *
	 * @return The column number.
	 */
	public int getColumn() {
		return column;
	}

	
	/**
	 * Sets the column number associated with the current position in the parsed file.
	 *
	 * @param column The new column number to be set.
	 */
	public void setColumn(int column) {
		this.column = column;
	}

	
	/**
	 * Gets the name of the map file to be read.
	 *
	 * @return The name of the map file.
	 */
	public String getMapToRead() {
		return mapToRead;
	}

	
	/**
	 * Sets the String map to be read.
	 *
	 * @param mapToRead The name of the map file to set.
	 */
	public void setMapToRead(String mapToRead) {
		this.mapToRead = mapToRead;
	}

	
	/**
	 * Checks whether the game can be played based on the file analysis.
	 *
	 * @return true if the game can be played, false otherwise.
	 */
	public boolean canPlay() {
		return canPlay;
	}

	
	/**
	 * Sets the flag indicating whether the game can be played.
	 *
	 * @param canPlay The flag indicating whether the game can be played.
	 */
	public void setCanPlay(boolean canPlay) {
		this.canPlay = canPlay;
	}

	
	/**
	 * Gets the list of hash maps containing data about game elements.
	 *
	 * @return The list of hash maps with element data.
	 */
	public List<HashMap<String, String>> getDataOfElements() {
		return dataOfElements;
	}

	
	/**
	 * Adds a hash map containing data about a game element to the list.
	 *
	 * @param dataOfElement The hash map with element data to be added.
	 */
	public void addDataOfElement(HashMap<String, String> dataOfElement) {
		dataOfElements.add(dataOfElement);
	}

	/**
	 * Checks whether the player information has already been read.
	 *
	 * @return true if player information has been read, false otherwise.
	 */
	boolean isPlayerAlreadyRead() {
		return playerAlreadyRead;
	}

	
	/**
	 * Sets the flag indicating whether player information has already been read.
	 *
	 * @param playerAlreadyRead true if player information has been read, false otherwise.
	 */
	public void setPlayerAlreadyRead(boolean playerAlreadyRead) {
		this.playerAlreadyRead = playerAlreadyRead;
	}

	
	/**
	 * Gets the map of grid encoding.
	 *
	 * @return The map of grid encoding.
	 */
	public Map<Character, String> getGridEncoding() {
		return gridEncoding;
	}

	
	/**
	 * Add a new grid encoding to the map.
 *
 * @param newCharacter The letter of the new encoding.
 * @param element      The skin of the corresponding element.
	 */
	public void addGridEncoding(Character newCharacter, String element) {
		gridEncoding.put(newCharacter, element);
	}

	
	/**
	 * Gets the margin used for reading.
	 *
	 * @return The margin used for reading.
	 */
	public int getToReadMargin() {
		return toReadMargin;
	}

	
	/**
	 * Sets the margin used for reading.
	 *
	 * @param toReadMargin The new margin used for reading.
	 */
	public void setToReadMargin(int toReadMargin) {
		this.toReadMargin = toReadMargin;
	}

	
	/**
	 * Checks if the grid has been read.
	 *
	 * @return true if the grid has been read, false otherwise.
	 */
	public boolean isGreedHasBeenRead() {
		return greedHasBeenRead;
	}

	
	/**
	 * Sets the flag indicating whether the grid has been read.
	 *
	 * @param greedHasBeenRead The flag value to set.
	 */
	public void setGreedHasBeenRead(boolean greedHasBeenRead) {
		this.greedHasBeenRead = greedHasBeenRead;
	}

	
	/**
	 * Reads the content of a file and performs the parsing of the map.
	 *
	 * @param path The path of the file to be read and parsed.
	 * @return A FileAnalysis object containing the parsed information.
	 * @throws IOException If an I/O error occurs while reading the file.
	 */
	public static FileAnalysis readParser(Path path) throws IOException {
		var readGrid = new GridReader();
		var readElements = new ElementReader();
		var text = Files.readString(path);
		var lexer = new Lexer(text);
		var result = lexer.nextResult();
		var fileAnalysis = new FileAnalysis();
		if (result.token() != Token.LEFT_BRACKET) {
			throw new IllegalArgumentException("fichier map mal formate\n");
		}
		while ((result = lexer.nextResult()) != null) {
			if (result.content().equals("grid")) {
				if ((result = lexer.nextResult()).token() == Token.RIGHT_BRACKET) {
					readGrid.grid(lexer, fileAnalysis);
				} else {
					throw new IllegalArgumentException("fichier map mal formate\n");
				}
			}
			if (result.content().equals("element")) {
				if ((result = lexer.nextResult()).token() == Token.RIGHT_BRACKET) {
					readElements.readElement(lexer, fileAnalysis);
				}
			}
		}
		if (!fileAnalysis.isPlayerAlreadyRead()) {
			System.err.println("Player has not been informed or has been misinformed");
			fileAnalysis.setCanPlay(false);
		}
		if (!fileAnalysis.isGreedHasBeenRead()) {
			System.err.println("[grid] has not been informed or has been misinformed");
			fileAnalysis.setCanPlay(false);
		}
		return fileAnalysis;
	}
}
