package network_to_game;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import game.Player;
import interfaces.PlayerInterface;

public class JSON {
			
	public static String generateSingleJson( PlayerInterface player ) {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			return mapper.writeValueAsString(player);
		} catch (JsonGenerationException e) {
			throw new IllegalArgumentException("Invalid Json");
		} catch (JsonMappingException e) {
			throw new IllegalArgumentException("Invalid mapping");
		} catch (IOException e) {
			throw new IllegalArgumentException();
		}		
	}
	
	public static String generateMultipleJson( ArrayList<Player> all_players ) {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			return mapper.writeValueAsString(all_players);
		} catch (JsonGenerationException e) {
			throw new IllegalArgumentException("Invalid Json");
		} catch (JsonMappingException e) {
			throw new IllegalArgumentException("Invalid mapping");
		} catch (IOException e) {
			throw new IllegalArgumentException();
		}		
	}
	
	public static Player parseSingleJson( String json ) {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			return mapper.readValue(json , Player.class);
		} catch (JsonParseException e) {
			throw new IllegalArgumentException("Invalid json");
		} catch (JsonMappingException e) {
			throw new IllegalArgumentException("Invalid mapping");
		} catch (IOException e) {
			throw new IllegalArgumentException();
		}
	}
	
	public static ArrayList<Player> parseMultipleJson( String json ) {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			return mapper.readValue(json , new TypeReference<ArrayList<Player>>(){});
		} catch (JsonParseException e) {
			throw new IllegalArgumentException("Invalid json");
		} catch (JsonMappingException e) {
			throw new IllegalArgumentException("Invalid mapping");
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
				mapper.readValue(possible_json , new TypeReference<ArrayList<Player>>(){}); // If Json is a list of players
			} catch (IOException e1) {
				return false;
			}
		}
		
		return !possible_json.equals("{}") && !possible_json.equals("[]"); // Special cases where there a blank object is passed and it parses correctly
	}
}
