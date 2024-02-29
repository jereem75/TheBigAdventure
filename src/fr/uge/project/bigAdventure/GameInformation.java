package fr.uge.project.bigAdventure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import fr.uge.project.parser.FileAnalysis;


/**
 * Constructs a new GameInformation object, initializing empty collections for enemies, friends,
 * items, obstacles, and images.
 */
public class GameInformation {
	private int gridColumn, gridLine;
	private ElementsOnGrid[][] array;
	private final Map<Position, Enemy> enemies;
	private final Map<Position, Friend> friends;
	private final Map<Position, Item> items;
	private final Map<Position, Obstacle> obstacles;
	private final Map<String, BufferedImage> allImages;
	private Friend perso = null;
	private Position positionOfPerso;

	
	/**
	 * Constructs a new GameInformation object, initializing empty collections for enemies, friends,
	 * items, obstacles, and images.
	 */
	public GameInformation() {
		enemies = new HashMap<>();
		friends = new HashMap<>();
		items = new HashMap<>();
		obstacles = new HashMap<>();
		allImages = new HashMap<>();
	}

	
	/**
   * Creates a 2D array to represent the game grid with the specified number of rows and columns.
   *
   * @param line   The number of rows in the grid.
   * @param column The number of columns in the grid.
   */
	private void createMapArray(int line, int column) {
		if (line < 1 || column < 1) {
			throw new IllegalArgumentException("The row and column must be greater than 1");
		}
		gridColumn = column;
		gridLine = line;
		array = new ElementsOnGrid[line][column];
	}

	/**
   * Adds an element to the grid at the specified position i,j.
   *
   * @param i        The row index.
   * @param j        The column index.
   * @param elements The ElementsOnGrid object to add.
   */
  private void addOnGrid(int i, int j, ElementsOnGrid elements) {
      Objects.requireNonNull(elements);
      array[i][j] = elements;
  }

  /**
   * Gets the player character.
   *
   * @return The player character.
   */
  public Friend getPerso() {
      return perso;
  }

  /**
   * Adds a BufferedImage to the allImages Map based on the specified skin identifier.
   *
   * @param skin The skin identifier.
   * @throws IOException If an I/O error occurs while reading the image.
   */
	private void addBufferedImage(String skin) throws IOException {
		if (allImages.get(skin) == null) {
			var input = GameInformation.class.getResourceAsStream("/images/" + skin + ".png");
			var image = ImageIO.read(input);
			allImages.put(skin, image);
		}
	}
	
	/**
	 * Retrieves the BufferedImage associated with a specific skin identifier.
	 *
	 * @param skin The skin identifier.
	 * @return The BufferedImage associated with the specified skin identifier, or null if not found.
	 */
	public BufferedImage getBufferedImage(String skin) {
		return allImages.get(skin);
	}
	
	
	/**
	 * Initializes the game grid based on the information provided in the FileAnalysis object.
	 *
	 * @param fileInfos The FileAnalysis object containing game grid data(String).
	 * @throws IOException If an I/O error occurs while reading the grid data.
	 */
	private void initialiseGrid(FileAnalysis fileInfos) throws IOException {
		int line = 0;
		int column = 0;
		var map = fileInfos.getMapToRead();
		int length = map.length();
		int read = 3 + fileInfos.getToReadMargin();
		while (read < length) {
			char current = map.charAt(read);
			switch (current) {
			case '\n':
				line++;
				column = 0;
				read += fileInfos.getToReadMargin();
				break;
			case '"':
				return;
			case ' ':
				addOnGrid(line, column, new ElementsOnGrid("NOTHING"));
				column++;
				read++;
				break;
			default:
				var skin = fileInfos.getGridEncoding().get(current);
				addOnGrid(line, column, new ElementsOnGrid(skin));
				addBufferedImage(skin);
				column++;
				read++;
			}
		}
	}

	/**
	 * Adds a new enemy to the enemies map.
	 *
	 * @param newEnemy A HashMap containing information about the new enemy.
	 * @throws NullPointerException If the newEnemy parameter is null.
	 */
	private void addEnemy(HashMap<String, String> newEnemy) {
		Objects.requireNonNull(newEnemy);
		var positionX = Integer.parseInt(newEnemy.get("positionX"));
		var positionY = Integer.parseInt(newEnemy.get("positionY"));
		enemies.put(new Position(positionY, positionX), new Enemy(newEnemy));
	}

	
	/**
	 * Adds a new friend to the Friends map.
	 *
	 * @param newFriend A HashMap containing information about the new friend.
	 * @throws NullPointerException If the newFriend parameter is null.
	 */
	private void addFriend(HashMap<String, String> newFriend) {
		Objects.requireNonNull(newFriend);
		var positionX = Integer.parseInt(newFriend.get("positionX"));
		var positionY = Integer.parseInt(newFriend.get("positionY"));
		friends.put(new Position(positionY, positionX), new Friend(newFriend));
	}

	/**
	 * Adds a new obstacle to the Obstacles map.
	 *
	 * @param newObstacle A HashMap containing information about the new obstacle.
	 * @throws NullPointerException If the newObstacle parameter is null.
	 */
	private void addObstacle(HashMap<String, String> newObstacle) {
		Objects.requireNonNull(newObstacle);
		var positionX = Integer.parseInt(newObstacle.get("positionX"));
		var positionY = Integer.parseInt(newObstacle.get("positionY"));
		obstacles.put(new Position(positionY, positionX), new Obstacle(newObstacle));
	}

	
	/**
	 * Adds a new item to the Items map.
	 *
	 * @param newItem A HashMap containing information about the new item.
	 * @throws NullPointerException If the newItem parameter is null.
	 */
	private void addItem(HashMap<String, String> newItem) {
		Objects.requireNonNull(newItem);
		var positionX = Integer.parseInt(newItem.get("positionX"));
		var positionY = Integer.parseInt(newItem.get("positionY"));
		items.put(new Position(positionY, positionX), new Item(newItem));
	}

	
	/**
	 * Adds a new player character to the game.
	 *
	 * @param newPlayer A HashMap containing information about the new player character.
	 * @throws NullPointerException If the newPlayer parameter is null.
	 */
	private void addPlayer(HashMap<String, String> newPlayer) {
		Objects.requireNonNull(newPlayer);
		var positionX = Integer.parseInt(newPlayer.get("positionX"));
		var positionY = Integer.parseInt(newPlayer.get("positionY"));
		positionOfPerso = new Position(positionY, positionX);
		perso = new Friend(newPlayer);
	}

	
	/**
	 * Initializes various game elements based on the information provided in the list of HashMaps.
	 *
	 * @param allElements A List of HashMaps, where each HashMap contains information about a game element.
	 *                    Each HashMap must include a "kind" key indicating the type of the element and a "skin" key
	 *                    indicating the visual representation of the element. The specific keys required depend on the
	 *                    type of element.
	 * @throws IOException           If an I/O error occurs while processing image data.
	 * @throws NullPointerException  If any element in the list or its required information is null.
	 * @throws IllegalArgumentException If the "kind" key is missing or has an unsupported value.
	 */

	private void initialiseOtherElements(List<HashMap<String, String>> allElements) throws IOException {
		for (var informations : allElements) {
			addBufferedImage(informations.get("skin"));
			switch (informations.get("kind")) {
			case "enemy":
				addEnemy(informations);
				break;
			case "item":
				addItem(informations);
				break;
			case "obstacle":
				addObstacle(informations);
				break;
			case "friend":
				addFriend(informations);
				break;
			case "player":
				addPlayer(informations);
				break;
			}
		}
	}

	
	/**
	 * Initializes the game information based on the parsing results from a FileAnalysis object.
	 *
	 * @param fileInfos The FileAnalysis object containing parsed game information.
	 * @throws IOException If an I/O error occurs while processing grid or element data.
	 */
	public void initialiseFromParseur(FileAnalysis fileInfos) throws IOException {
		createMapArray(fileInfos.getLine(), fileInfos.getColumn());
		initialiseGrid(fileInfos);
		initialiseOtherElements(fileInfos.getDataOfElements());
	}

	
	/**
	 * Gets the number of columns in the game grid.
	 *
	 * @return The number of columns in the game grid.
	 */
	public int getColumn() {
		return gridColumn;
	}

	
	/**
	 * Retrieves the game element at the specified grid coordinates.
	 *
	 * @param i The row index.
	 * @param j The column index.
	 * @return The game element at the specified grid coordinates.
	 * @throws IllegalArgumentException If the provided indices are outside the valid range.
	 */
	public ElementsOnGrid getElementInGrid(int i, int j) {
		if (i < 0 || j < 0 || i > gridLine - 1 || j > gridColumn - 1) {
			throw new IllegalArgumentException("i or j is false for the array");
		}
		return array[i][j];
	}

	/**
	 * Gets the number of rows in the game grid.
	 *
	 * @return The number of rows in the game grid.
	 */
	public int getLine() {
		return gridLine;
	}

	
	/**
	 * Retrieves the collection of enemies in the game, mapped by their positions.
	 *
	 * @return The map containing enemy positions as keys and corresponding Enemy objects.
	 */
	public Map<Position, Enemy> getEnemies() {
		return enemies;
	}

	
	/**
	 * Retrieves the collection of friends in the game, mapped by their positions.
	 *
	 * @return The map containing friend positions as keys and corresponding Friend objects.
	 */
	public Map<Position, Friend> getFriends() {
		return friends;
	}

	
	/**
	 * Retrieves the collection of items in the game, mapped by their positions.
	 *
	 * @return The map containing item positions as keys and corresponding Item objects.
	 */
	public Map<Position, Item> getItems() {
		return items;
	}

	/**
	 * Retrieves the collection of obstacles in the game, mapped by their positions.
	 *
	 * @return The map containing obstacle positions as keys and corresponding Obstacle objects.
	 */
	public Map<Position, Obstacle> getObstacles() {
		return obstacles;
	}

	
	/**
	 * Gets the current position of the player character.
	 *
	 * @return The Position object representing the current position of the player character.
	 */
	public Position getPositionOfPerso() {
		return positionOfPerso;
	}

	/**
	 * Changes the position of the player character.
	 *
	 * @param newPosition The new Position object representing the desired position.
	 */
	public void changePlayerPosition(Position newPosition) {
		positionOfPerso = newPosition;
	}

	
	/**
	 * Changes the position of an enemy or a friend in the game.
	 *
	 * @param oldPosition The current Position of the character.
	 * @param newPosition The new Position to move the character to.
	 */
	public void changeActorPosition(Position oldPosition, Position newPosition) {
		if (enemies.get(oldPosition) != null) {
			var enemy = enemies.get(oldPosition);
			enemies.remove(oldPosition);
			enemies.put(newPosition, enemy);
		} else {
			var friend = friends.get(oldPosition);
			friends.remove(oldPosition);
			friends.put(newPosition, friend);
		}
	}
	
	/**
	 * Removes an item from the game at the specified position.
	 *
	 * @param position The Position of the item to be removed.
	 */
	public void removeItem(Position position) {
		items.remove(position);
	}

}
