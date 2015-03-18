package network_to_game;

import java.util.ArrayList;

import interfaces.PlayerInterface;

public class JSON {
	
	private JSONGenerator generator = new JSONGenerator();
	private JSONParser parser = new JSONParser();
	
	public String generateJson( PlayerInterface player ) {
		return generator.generateOnePlayerJson(player);
	}
	
	public String generateJson( ArrayList<PlayerInterface> players ) {
		return generator.generateMultiplePlayerJson(players);
	}
	
	public ArrayList<PlayerData> parseJson( String json ) {
		return parser.parseMultipleJson(json);
	}
	
	public PlayerData parserSingleJson( String json ) {
		return parser.parseOnePlayerJSON(json);
	}
}
