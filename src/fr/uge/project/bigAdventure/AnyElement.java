package fr.uge.project.bigAdventure;

/**
 * Represents any element within the game.
 * Elements are entities that have a skin (appearance) in the game environment.
 */
public interface AnyElement {

    /**
     * Gets the skin (appearance) of the element.
     *
     * @return The skin of the element.
     */
    public String getSkin();
}