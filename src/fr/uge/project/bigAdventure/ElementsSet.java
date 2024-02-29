package fr.uge.project.bigAdventure;

import java.util.Set;


/**
 * Represents a set of valid elements in the game.
 * This set includes various skins representing different game elements.
 */
public class ElementsSet {
	private static final Set<String> elements = Set.of("ALGAE", "CLOUD", "FLOWER", "FOLIAGE", "GRASS", "LADDER", "LILY",
			"PLANK", "REED", "ROAD", "SPROUT", "TILE", "TRACK", "VINE", "BED", "BOG", "BOMB", "BRICK", "CHAIR", "CLIFF",
			"DOOR", "FENCE", "FORT", "GATE", "HEDGE", "HOUSE", "HUSK", "HUSKS", "LOCK", "MONITOR", "PIANO", "PILLAR", "PIPE",
			"ROCK", "RUBBLE", "SHELL", "SIGN", "SPIKE", "STATUE", "STUMP", "TABLE", "TOWER", "TREE", "TREES", "WALL",
			"BUBBLE", "DUST", "BABA", "BADBAD", "BAT", "BEE", "BIRD", "BUG", "BUNNY", "CAT", "CRAB", "DOG", "FISH", "FOFO",
			"FROG", "GHOST", "IT", "JELLY", "JIJI", "KEKE", "LIZARD", "ME", "MONSTER", "ROBOT", "SNAIL", "SKULL", "TEETH",
			"TURTLE", "WORM", "BOOK", "BOLT", "BOX", "CASH", "CLOCK", "COG", "CRYSTAL", "CUP", "DRUM", "FLAG", "GEM",
			"GUITAR", "HIHAT", "KEY", "LAMP", "LEAF", "MIRROR", "MOON", "ORB", "PANTS", "PAPER", "PLANET", "RING", "ROSE",
			"SAX", "SCISSORS", "SEED", "SHIRT", "SHOVEL", "STAR", "STICK", "SUN", "SWORD", "TRUMPET", "VASE", "BANANA",
			"BOBA", "BOTTLE", "BURGER", "CAKE", "CHEESE", "DONUT", "DRINK", "EGG", "FRUIT", "FUNGUS", "FUNGI", "LOVE",
			"PIZZA", "POTATO", "PUMPKIN", "TURNIP", "PLANE", "ROCKET", "UFO", "CAR", "TRAIN", "CART", "BOAT", "ICE", "LAVA",
			"WATER", "BUCKET", "FIRE", "WIND", "NOTHING");

	
	/**
   * Checks if a given element skin is valid and part of the game's set of elements.
   *
   * @param element The element skin to check.
   * @return true if the element is valid, false otherwise.
   */
	public static boolean contains(String element) {
		return elements.contains(element);
	}

}
