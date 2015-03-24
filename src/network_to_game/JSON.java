package network_to_game;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import game.Player;

public class JSON {
			
	public static String generateJson( Object obj ) {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			throw new IllegalArgumentException("Invalid Object");
		} catch (JsonMappingException e) {
			throw new IllegalArgumentException("Invalid mapping");
		} catch (IOException e) {
			throw new IllegalArgumentException();
		}		
	}
	
	public static Player[] parseJson( String json ) {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			return new Player[] {mapper.readValue(json , Player.class)}; // If the json just contains a player
		} catch (JsonParseException e) {
			throw new IllegalArgumentException("Invalid json");
		} catch (JsonMappingException e) {
			try {
				return mapper.readValue(json, Player[].class); // If the json contains multiple players or a list with just one player.
			} catch (IOException e1) {
				throw new IllegalArgumentException("Invalid json");
			}
		} catch (IOException e) {
			throw new IllegalArgumentException();
		}
	}
	
	public static boolean isGameJson( String possible_json ) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.readValue(possible_json, Player.class); // If Json is a player
		} catch (IOException e) {
			try {
				mapper.readValue(possible_json , Player[].class); // If Json is a list of players
			} catch (IOException e1) {
				return false;
			}
		}
		
		return !possible_json.equals("{}") && !possible_json.equals("[]"); // Special cases where there a blank object is passed and it parses correctly
	}
}
