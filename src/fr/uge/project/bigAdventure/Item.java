package fr.uge.project.bigAdventure;

import java.util.HashMap;
import java.util.Objects;

/**
 * Represents an item within the game.
 */
public class Item implements AnyElement {
	private String name;
	private String skin;
	private int damage;

	
	/**
   * Constructs an Item based on the provided information.
   *
   * @param map A HashMap containing information about the Item.
   * @throws NullPointerException If the provided map is null.
   */
	public Item(HashMap<String, String> map) {
		Objects.requireNonNull(map);
		createNewElemnt(map);
	}

	
	/**
	 * Initializes the attributes of the Item based on the provided information.
	 *
	 * @param map A HashMap containing information about the Item.
	 * @throws NullPointerException If the provided map is null.
	 */
	private void createNewElemnt(HashMap<String, String> map) {
		Objects.requireNonNull(map);
		name = map.get("name");
		skin = map.get("skin");
		if (map.get("damage") != null) {
			damage = Integer.parseInt(map.get("damage"));
		}
	}
	
	
	/**
   * Gets the name of the Item.
   *
   * @return The name of the Item.
   */
	public String getName() {
		return name;
	}

	
	/**
	 * Gets the skin of the Item.
	 *
	 * @return The skin of the Item.
	 */
	@Override
	public String getSkin() {
		return skin;
	}

	
	/**
   * Gets the damage value associated with the Item.
   *
   * @return The damage value of the Item.
   */
	public int getDamage() {
		return damage;
	}

}
