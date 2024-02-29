package fr.uge.project.bigAdventure;


/**
 * Represents a position with coordinates (i, j).
 *
 * @param i The X-coordinate.
 * @param j The Y-coordinate.
 */
public record Position(int i, int j) {

	
	/**
   * Constructs a Position with non-negative coordinates (i, j).
   *
   * @throws IllegalArgumentException If either coordinate is negative.
   */
	public Position {
		if (i < 0 || j < 0) {
			throw new IllegalArgumentException("Arguments for position are wrong");
		}
	}
}
