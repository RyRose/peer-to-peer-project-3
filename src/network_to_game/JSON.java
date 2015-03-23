package network_to_game;

import java.util.ArrayList;

import game.Player;
import interfaces.PlayerInterface;

public class JSON {
		
	public static String generateSingleJson( PlayerInterface player ) {
		JSONGenerator generator = new JSONGenerator();
		return generator.generateSingleJson(player);
	}
	
	public static String generateMultipleJson( ArrayList<Player> all_players ) {
		JSONGenerator generator = new JSONGenerator();
		return generator.generateMultipleJson(all_players);
	}
	
	
	public static Player parseSingleJson( String json ) {
		JSONParser parser = new JSONParser();
		return parser.parseSingleJson(json);
	}
	
	public static ArrayList<Player> parseMultipleJson( String json ) {
		JSONParser parser = new JSONParser();
		return parser.parseMultipleJson(json);
	}
}
