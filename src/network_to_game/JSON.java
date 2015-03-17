package network_to_game;

import java.util.ArrayList;

import interfaces.PlayerInterface;

public class JSON {
	
	private static JSONGenerator generator = new JSONGenerator();
	private static JSONParser parser = new JSONParser();
	
	public static String generateJson( PlayerInterface player ) {
		return generator.generateOnePlayerJson(player);
	}
	
	public static String generateJson( ArrayList<PlayerInterface> players ) {
		return generator.generateMultiplePlayerJson(players);
	}
	
	public static ArrayList<PlayerData> parseJson( String json ) {
		return parser.parseMultipleJson(json);
	}
}
