package fr.uge.project.bigAdventure;

import java.util.HashMap;
import java.util.Objects;


/**
 * Represents an enemy within the game.
 * Enemies are actors that can interact and move within the game environment.
 */
public class Enemy implements AnyElement, ActorOfTheGame {
	private String skin;
	private Position zone;
	private int heightZone;
	private int widthZone;
	private String name;
	private String behavior;
	private int health;
	private int maxHelth = -1;
	private int damage;

	
	/**
   * Constructs an Enemy object using the provided map of attributes.
   *
   * @param map A HashMap containing attributes for initializing the enemy.
   * @throws NullPointerException If the map parameter is null.
   */
	public Enemy(HashMap<String, String> map) {
		Objects.requireNonNull(map);
		createNewElemnt(map);
	}

	
	/**
	 * Creates a new Enemy element based on the provided attributes in the map.
	 *
	 * @param map A HashMap containing attributes for initializing the enemy.
	 * @throws NullPointerException If the map parameter is null.
	 */
	private void createNewElemnt(HashMap<String, String> map) {
		Objects.requireNonNull(map);
		if (map.get("zoneX") != null) {
			int zoneX = Integer.parseInt(map.get("zoneX"));
			int zoneY = Integer.parseInt(map.get("zoneY"));
			heightZone = Integer.parseInt(map.get("colonne"));
			widthZone = Integer.parseInt(map.get("ligne"));
			zone = new Position(zoneY, zoneX);
		} else {
			zone = null;
		}
		name = map.get("name");
		skin = map.get("skin");
		maxHelth = health = Integer.parseInt(map.get("health"));
		damage = Integer.parseInt(map.get("damage"));
		behavior = map.get("behavior");
	}
	
	
	/**
	 * Checks if the enemy can move to the specified position on the game map.
	 *
	 * @param newX The new X-coordinate.
	 * @param newY The new Y-coordinate.
	 * @param map The GameInformation object representing the game map.
	 * @return true if the enemy can move to the new position, false otherwise.
	 */
	@Override
	public boolean canMooveAtNewPosition(int newX, int newY, GameInformation map) {
		if (newX < 1 || newX >= map.getLine() || newY < 1 || newY >= map.getColumn()) {
			return false;
		}
		if (newX <= (zone.i() + heightZone) && newX >= (zone.i() - heightZone) && newY <= (zone.j() + widthZone)
				&& newY >= (zone.j() - widthZone) && map.getElementInGrid(newX, newY).canWalk()
				&& map.getEnemies().get(new Position(newX, newY)) == null
				&& map.getObstacles().get(new Position(newX, newY)) == null) {
			return true;
		}
		return false;
	}

	
	/**
	 * Gets the skin of the enemy.
	 *
	 * @return The skin of the enemy.
	 */
	@Override
	public String getSkin() {
		return skin;
	}

	
	/**
	 * Checks if the enemy can move.
	 *
	 * @return true if the enemy can move, false otherwise.
	 */
	@Override
	public boolean canMoove() {
		if (zone == null) {
			return false;
		}
		return true;
	}

	
	/**
	 * Gets the damage inflicted by the enemy.
	 *
	 * @return The damage inflicted by the enemy.
	 */
	public int getDamage() {
		return damage;
	}

	
	/**
	 * Gets the behavior of the enemy.
	 *
	 * @return The behavior of the enemy.
	 */
	public String getBehavior() {
		return behavior;
	}

	
	/**
	 * Gets the name of the enemy.
	 *
	 * @return The name of the enemy.
	 */
	public String getName() {
		return name;
	}

	
	/**
	 * Gets the current health of the enemy.
	 *
	 * @return The current health of the enemy.
	 */
	public int getHealth() {
		return health;
	}

	
	/**
	 * Gets the maximum health of the enemy.
	 *
	 * @return The maximum health of the enemy.
	 */
	public int getMaxHelth() {
		return maxHelth;
	}

	

}
