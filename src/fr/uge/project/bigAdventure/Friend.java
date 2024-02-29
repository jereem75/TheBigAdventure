package fr.uge.project.bigAdventure;

import java.util.HashMap;
import java.util.Objects;


/**
 * Represents a friend within the game.
 * Friends are actors that can interact and move within the game environment.
 */
public class Friend implements AnyElement, ActorOfTheGame {
	private String skin;
	private Position zone;
	private int heightZone;
	private int widthZone;
	private String name;
	private int health;
	private int maxHelth = -1;
	private boolean haveHealth = false;

	
	/**
	 * Constructs a new Friend based on the provided information.
	 *
	 * @param map A HashMap containing information about the friend.
	 * @throws NullPointerException If the map parameter is null.
	 */
	public Friend(HashMap<String, String> map) {
		Objects.requireNonNull(map);
		createNewElemnt(map);
	}

	
	/**
	 * Creates a new Friend element based on the information provided in the map.
	 *
	 * @param map A HashMap containing information about the friend.
	 * @throws NullPointerException If the map parameter is null.
	 */
	private void createNewElemnt(HashMap<String, String> map) {
		Objects.requireNonNull(map);
		if (!map.get("kind").equals("player")) {
			int zoneX = Integer.parseInt(map.get("zoneX"));
			int zoneY = Integer.parseInt(map.get("zoneY"));
			heightZone = Integer.parseInt(map.get("ligne"));
			widthZone = Integer.parseInt(map.get("colonne"));
			zone = new Position(zoneX, zoneY);
		}
		name = map.get("name");
		skin = map.get("skin");
		if (map.get("health") != null) {
			haveHealth = true;
			maxHelth = health = Integer.parseInt(map.get("health"));
		}
	}

	
	/**
	 * Checks if the Friend can move.
	 *
	 * @return true if the Friend can move, false otherwise.
	 */
	@Override
	public boolean canMoove() {
		if (zone == null) {
			return false;
		}
		return true;
	}

	
	/**
	 * Checks if the Friend can move to the new position.
	 *
	 * @param newX The new X-coordinate.
	 * @param newY The new Y-coordinate.
	 * @param map The GameInformation containing the game map.
	 * @return true if the Friend can move to the new position, false otherwise.
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
	 * Gets the skin of the Friend.
	 *
	 * @return The skin of the Friend.
	 */
	@Override
	public String getSkin() {
		return skin;
	}

	/**
	 * Gets the name of the Friend.
	 *
	 * @return The name of the Friend.
	 */
	public String getName() {
	    return name;
	}


	/**
	 * Gets the current health of the Friend.
	 *
	 * @return The current health of the Friend.
	 */
	public int getHealth() {
	    return health;
	}


	/**
	 * Changes the health of the Friend to the specified value.
	 *
	 * @param newHealth The new health value for the Friend.
	 */
	public void changeHelth(int newHealth) {
	    health = newHealth;
	}


	/**
	 * Retrieves the maximum health of the Friend.
	 *
	 * @return The maximum health of the Friend.
	 */
	public int getMaxHelth() {
		return maxHelth;
	}

	/**
	 * Checks if the Friend has health information.
	 *
	 * @return True if the Friend has health information, false otherwise.
	 */
	public boolean haveHealth() {
		return haveHealth;
	}

}
