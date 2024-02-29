package fr.uge.project.bigAdventure;

import java.util.HashMap;
import java.util.Objects;

/**
 * Represents an obstacle within the game.
 */
public class Obstacle implements AnyElement {
	private String name;
	private String skin;
	private String lockedType;
	private String lockedName;
	private String teleport;

	
	/**
	 * Constructs a new Obstacle object based on the information provided in the map.
	 *
	 * @param map A HashMap containing information about the Obstacle.
	 * @throws NullPointerException If the map parameter is null.
	 */
	public Obstacle(HashMap<String, String> map) {
		Objects.requireNonNull(map);
		createNewElemnt(map);
	}

	
	/**
	 * Initializes the Obstacle object with the information provided in the map.
	 *
	 * @param map A HashMap containing information about the Obstacle.
	 * @throws NullPointerException If the map parameter is null.
	 */
	private void createNewElemnt(HashMap<String, String> map) {
		Objects.requireNonNull(map);
		name = map.get("name");
		skin = map.get("skin");
		lockedType = map.get("lockedType");
		lockedName = map.get("lockedName");
		teleport = map.get("teleport");
		return;
	}

	
	/**
	 * Gets the name of the Obstacle.
	 *
	 * @return The name of the Obstacle.
	 */
	public String getName() {
		return name;
	}

	
	/**
	 * Gets the skin of the Obstacle.
	 *
	 * @return The skin of the Obstacle.
	 */
	@Override
	public String getSkin() {
		return skin;
	}

	/**
	 * Gets the type of the lock associated with the Obstacle.
	 *
	 * @return The type of the lock associated with the Obstacle.
	 */
	public String getLockedType() {
		return lockedType;
	}

	/**
	 * Gets the name associated with the lock of the Obstacle.
	 *
	 * @return The name associated with the lock of the Obstacle.
	 */
	public String getLockedName() {
		return lockedName;
	}

	
	/**
	 * Gets the teleport information associated with the Obstacle.
	 *
	 * @return The teleport information associated with the Obstacle.
	 */
	public String getTeleport() {
		return teleport;
	}

}
