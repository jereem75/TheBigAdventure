package fr.uge.project.bigAdventure;

/**
 * Represents an actor within the game.
 * Actors are entities that can interact and move within the game environment.
 */
public interface ActorOfTheGame {

    /**
     * Gets the skin of the actor.
     *
     * @return The skin of the actor.
     */
    public String getSkin();

    /**
     * Checks if the actor can move to a new position within the game environment.
     *
     * @param newX The new X-coordinate for the actor.
     * @param newY The new Y-coordinate for the actor.
     * @param map  The game information providing the environment details.
     * @return true if the actor can move to the new position, false otherwise.
     */
    public boolean canMooveAtNewPosition(int newX, int newY, GameInformation map);

    /**
     * Checks if the actor can move within the game environment.
     *
     * @return true if the actor can move, false otherwise.
     */
    public boolean canMoove();
}
