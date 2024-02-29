package fr.uge.project.bigAdventure;

import java.util.Objects;
import java.util.Set;


/**
 * Represents an element on the game grid with a specific visual representation (skin).
 * ElementsOnGrid can be obstacles in the game environment.
 */
public record ElementsOnGrid(String skin) {
	private static Set<String> obstacles = Set.of("BED", "BOG", "BOMB", "BRICK", "CHAIR", "CLIFF", "FENCE", "FORT",
			"HEDGE", "HUSK", "HUSKS", "LOCK", "MONITOR", "PIANO", "PILLAR", "PIPE", "ROCK", "RUBBLE", "SHELL", "SIGN",
			"SPIKE", "STATUE", "STUMP", "TABLE", "TREE", "TREES", "WALL", "ICE", "LAVA", "WATER", "DOOR", "GATE", "HOUSE",
			"TOWER");
	
	
	/**
	 * Represents an obstacle on the game grid with a specific visual representation (skin).
	 */
	public ElementsOnGrid {
		Objects.requireNonNull(skin);
	}

	
	/**
   * Checks whether the element is an obstacle.
   *
   * @return true if the element isn't an obstacle, false if it is in the set.
   */
	public boolean canWalk() {
		return !obstacles.contains(skin);
	}

}
