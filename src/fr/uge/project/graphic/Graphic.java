package fr.uge.project.graphic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import fr.uge.project.bigAdventure.ActorOfTheGame;
import fr.uge.project.bigAdventure.AnyElement;
import fr.uge.project.bigAdventure.Friend;
import fr.uge.project.bigAdventure.GameInformation;
import fr.uge.project.bigAdventure.Position;
import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.KeyboardKey;
import fr.umlv.zen5.ScreenInfo;
import fr.umlv.zen5.Event.Action;


/**
 * The Graphic class manages the graphical aspects of the game, including rendering elements on the graphics window.
 */
public class Graphic {
	private int canMoovePerso = 0;
	private float width;
	private float height;
	private double scale;
	private int xOffset;
	private int yOffset;
	private ApplicationContext context;

	/**
	 * Draws the grid of elements on the graphics window based on the specified
	 * GameInformation map.
	 *
	 * @param graphics The Graphics2D object used for rendering.
	 * @param map      The GameInformation object containing details about the game
	 *                 map.
	 */
	private void drawGrid(Graphics2D graphics, GameInformation map) {
		for (int i = 0; i < map.getLine(); i++) {
			for (int j = 0; j < map.getColumn(); j++) {
				var image = map.getBufferedImage(map.getElementInGrid(i, j).skin());
				if (image != null) {
					graphics.drawImage(image, null, j * 24 + xOffset, i * 24 + yOffset);
				}
			}
		}
	}

	/**
	 * Chooses a color for the health bar based on the specified fill percentage.
	 *
	 * @param fillPercentage The percentage of the health bar to determine the
	 *                       color.
	 * @return The Color representing the chosen color for the health bar.
	 */
	private Color choicebarColor(double fillPercentage) {
		if (fillPercentage < 0.33) {
			return Color.RED;
		}
		if (fillPercentage > 0.66) {
			return Color.GREEN;
		}
		return Color.ORANGE;
	}

	/**
	 * Draws the health bar for a Friend actor at the specified position on the
	 * graphics window.
	 *
	 * @param graphics The Graphics2D object used for rendering.
	 * @param position The position of the Friend actor on the game map.
	 * @param actor    The Friend actor for which the health bar is to be drawn.
	 */
	private void drawHealthBar(Graphics2D graphics, Position position, Friend actor) {
		if (actor.haveHealth()) {
			int maxHealth = actor.getMaxHelth();
			int currentHealth = actor.getHealth();
			int barWidth = 24;
			double fillPercentage = (double) currentHealth / maxHealth;
			int fillWidth = (int) Math.round(fillPercentage * barWidth);
			graphics.setColor(choicebarColor(fillPercentage));
			graphics.fill(new Rectangle2D.Float(position.j() * 24 + xOffset, position.i() * 24 + yOffset - 2, fillWidth, 3));
		}
	}

	/**
	 * Draws a collection of elements on the graphics window based on the specified
	 * map of positions and corresponding elements.
	 *
	 * @param map         The GameInformation object containing details about the
	 *                    game map.
	 * @param elementsMap A map of positions and corresponding elements to be drawn.
	 * @param graphics    The Graphics2D object used for rendering.
	 */
	private void drawElement(GameInformation map, Map<Position, ? extends AnyElement> elementsMap, Graphics2D graphics) {
		elementsMap.entrySet().stream().forEach(entry -> {
			var position = entry.getKey();
			var perso = entry.getValue();
			var image = map.getBufferedImage(perso.getSkin());
			if (image == null) {
				throw new IllegalArgumentException("The bufferedImage for " + perso.getSkin() + " skin was not foud");
			}
			graphics.drawImage(image, null, position.j() * 24 + xOffset, position.i() * 24 + yOffset);
		});
	}

	/**
	 * Draws the player character on the graphics window along with their health
	 * bar.
	 *
	 * @param map      The GameInformation object containing details about the game
	 *                 map.
	 * @param graphics The Graphics2D object used for rendering.
	 */
	private void drawPerso(GameInformation map, Graphics2D graphics) {
		var image = map.getBufferedImage(map.getPerso().getSkin());
		if (image == null) {
			throw new IllegalArgumentException("The bufferedImage for " + map.getPerso().getSkin() + " skin was not foud");
		}
		graphics.drawImage(image, null, map.getPositionOfPerso().j() * 24 + xOffset,
				map.getPositionOfPerso().i() * 24 + yOffset);
		drawHealthBar(graphics, map.getPositionOfPerso(), map.getPerso());
	}

	/**
	 * Draws all game elements on the graphics window, including the grid, friends,
	 * enemies, items, obstacles, and the player character.
	 *
	 * @param map      The GameInformation object containing details about the game
	 *                 map.
	 * @param graphics The Graphics2D object used for rendering.
	 */
	private void drawAll(GameInformation map, Graphics2D graphics) {
		resetWindow(graphics);
		AffineTransform oldTransform = graphics.getTransform();
		graphics.scale(scale, scale);
		drawGrid(graphics, map);
		drawElement(map, map.getFriends(), graphics);
		drawElement(map, map.getEnemies(), graphics);
		drawElement(map, map.getItems(), graphics);
		drawElement(map, map.getObstacles(), graphics);
		drawPerso(map, graphics);
		graphics.setTransform(oldTransform);
	}

	/**
	 * Resets the graphics window by filling it with a black color.
	 *
	 * @param graphics The Graphics2D object used for rendering.
	 */
	private void resetWindow(Graphics2D graphics) {
		graphics.setColor(Color.BLACK);
		graphics.fill(new Rectangle2D.Float(0, 0, width, height));
	}

	/**
	 * Analyzes the new position of the player character on the game map, performing
	 * actions based on the contents of the cell at the player's new position.
	 *
	 * @param map The GameInformation object containing details about the game map.
	 */
	private void analisePersoNewCase(GameInformation map) {
		if (map.getItems().get(map.getPositionOfPerso()) != null) {
			map.removeItem(map.getPositionOfPerso());
		}
		if (map.getEnemies().get(map.getPositionOfPerso()) != null) {
			int newHealth = map.getPerso().getHealth() - map.getEnemies().get(map.getPositionOfPerso()).getDamage();
			map.getPerso().changeHelth(newHealth);
		}
	}

	/**
	 * Handles the key pressed event for player character movement in response to
	 * the specified KeyboardKey.
	 *
	 * @param map     The GameInformation object containing details about the game
	 *                map.
	 * @param pressed The KeyboardKey indicating the direction in which the player
	 *                character should move.
	 */
	private void gestionKeyPressed(GameInformation map, KeyboardKey pressed) {
		int newY = map.getPositionOfPerso().j(), newX = map.getPositionOfPerso().i();
		switch (pressed) {
		case UP:
			newX -= 1;
			if (newX > 0 && map.getElementInGrid(newX, newY).canWalk()
					&& map.getObstacles().get(new Position(newX, newY)) == null) {
				map.changePlayerPosition(new Position(newX, newY));
			}
			break;
		case DOWN:
			newX += 1;
			if (newX > 0 && map.getElementInGrid(newX, newY).canWalk()
					&& map.getObstacles().get(new Position(newX, newY)) == null)
				map.changePlayerPosition(new Position(newX, newY));
			break;
		case LEFT:
			newY -= 1;
			if (newY > 0 && map.getElementInGrid(newX, newY).canWalk()
					&& map.getObstacles().get(new Position(newX, newY)) == null)
				map.changePlayerPosition(new Position(newX, newY));
			break;
		case RIGHT:
			newY += 1;
			if (newY > 0 && map.getElementInGrid(newX, newY).canWalk()
					&& map.getObstacles().get(new Position(newX, newY)) == null)
				map.changePlayerPosition(new Position(newX, newY));
			break;
		default:
			break;
		}
		analisePersoNewCase(map);
	}

	/**
	 * Moves all actors represented by the provided map of positions and
	 * corresponding ActorOfTheGame objects.
	 *
	 * @param map      The GameInformation object containing details about the game
	 *                 map.
	 * @param graphics The Graphics2D object used for rendering.
	 * @param actors   A map of positions and corresponding ActorOfTheGame objects
	 *                 to be moved.
	 */
	private void mooveAllActors(GameInformation map, Graphics2D graphics,
			Map<Position, ? extends ActorOfTheGame> actors) {
		Random random = new Random();
		Map<Position, ActorOfTheGame> enemiesCopy = new HashMap<>(actors);
		enemiesCopy.entrySet().stream().forEach(entry -> {
			var position = entry.getKey();
			var perso = entry.getValue();
			if (perso.canMoove()) {
				int newY = 0, newX = 0;
				boolean res = false;
				while (res == false) {
					newY = position.j();
					newX = position.i();
					int randomNumber = random.nextInt(4);
					switch (randomNumber) {
					case 0:
						newY = position.j() - 1;
						res = perso.canMooveAtNewPosition(position.i(), newY, map);
						break;
					case 1:
						newY = position.j() + 1;
						res = perso.canMooveAtNewPosition(position.i(), newY, map);
						break;
					case 2:
						newX = position.i() - 1;
						res = perso.canMooveAtNewPosition(newX, position.j(), map);
						break;
					case 3:
						newX = position.i() + 1;
						res = perso.canMooveAtNewPosition(newX, position.j(), map);
						break;
					default:
						break;
					}
				}
				map.changeActorPosition(position, new Position(newX, newY));
			}
		});
	}

	/**
	 * Initiates and runs the game loop, continuously processing events and
	 * rendering frames until the player's character health drops to zero.
	 *
	 * @param map The GameInformation object representing the game map and its
	 *            entities.
	 */
	private void game(GameInformation map) {
		while (map.getPerso().getHealth() > 0) {
			Event event = context.pollOrWaitEvent(10);
			context.renderFrame(graphics -> {
				if (canMoovePerso == 30) {
					mooveAllActors(map, graphics, map.getEnemies());
					mooveAllActors(map, graphics, map.getFriends());
					analisePersoNewCase(map);
					canMoovePerso = -1;
					drawAll(map, graphics);
				}
				if (event == null) { // no event
					canMoovePerso++;
					return;
				}
				Action action = event.getAction();
				if (action == Action.KEY_PRESSED) {
					if (event.getKey() == KeyboardKey.Q) {
						context.exit(0);
					}
					gestionKeyPressed(map, event.getKey());
					drawAll(map, graphics);
				}
				canMoovePerso++;
			});
		}
		context.exit(0);
	}

	/**
	 * Creates a new game using the specified GameInformation, initializing the game
	 * environment and rendering.
	 *
	 * @param map The GameInformation object containing details about the game map.
	 */
	public void createGame(GameInformation map) {
		Application.run(Color.BLACK, context -> {
			this.context = context;
			ScreenInfo screenInfo = context.getScreenInfo();
			this.width = screenInfo.getWidth();
			this.height = screenInfo.getHeight();
			int mapWidth = map.getColumn() * 24;
			double scaleX = (double) width / mapWidth;
			int mapHeight = map.getLine() * 24;
			double scaleY = (double) height / mapHeight;
			scale = Math.min(scaleX, scaleY);
			xOffset = ((int) width - (int) (mapWidth * scale)) / 2;
			yOffset = ((int) height - (int) (mapHeight * scale)) / 2;
			game(map);
		});
	}

}