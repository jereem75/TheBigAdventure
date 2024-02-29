package fr.uge.project.main;

import java.io.IOException;
import java.nio.file.Path;

import fr.uge.project.bigAdventure.GameInformation;
import fr.uge.project.graphic.Graphic;
import fr.uge.project.parser.FileAnalysis;


/**
 * The main class that starts the game.
 */
public class Main {
	private static boolean canPlay = true;
	

	
	/**
   * Reads the command line options to determine the map file.
   *
   * @param args The command line arguments.
   * @return The name of the map file or null if there's an error.
   */
	private static String readOptions(String[] args) {
		int argsLenght = args.length;
		String nameMap = null;
		for (int i = 0; i < argsLenght; i++) {
			if (args[i].equals("--level")) {
				if (i + 1 >= argsLenght) {
					System.err.println("Missing map file name");
					return null;
				}
				nameMap = args[i + 1];
				i++;
			}
			else if(args[i].equals("--validate")) {
				canPlay = false;
			}
			else {
				System.err.println("The option " + args[i] + " is unknown");
				return null;
			}
		}
		if (nameMap == null) {
			System.err.println("Missing map file name");
			return null;
		}
		return nameMap;
	}
	
	
	/**
   * The main function of the game.
   *
   * @param args The command line arguments.
   */
	public static void main(String[] args) {
		var nameMap = readOptions(args);
		if (nameMap == null) {
			return;
		}
		var path = Path.of(nameMap);
		FileAnalysis mapInformations = null;
		try {
			mapInformations = FileAnalysis.readParser(path);
			if (mapInformations.canPlay() && canPlay) {
				var map = new GameInformation();
				map.initialiseFromParseur(mapInformations);
				Graphic graphic = new Graphic();
				graphic.createGame(map);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
