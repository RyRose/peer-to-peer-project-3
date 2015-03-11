package network_to_game;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import interfaces.NetworkMessageInterface;
import interfaces.PlayerInterface;

public class NetworkMessage implements NetworkMessageInterface{
	
	private PlayerData player;
	private ArrayList<PlayerData> all_players;
	
	private String client_json;
	private String all_players_json;
	
	private JSONGenerator generator = new JSONGenerator();
	private JSONparser parser = new JSONparser();

	public NetworkMessage( PlayerInterface player ) { 
		client_json = generator.generateOnePlayerJson(player); 
		this.player = parser.parseOnePlayerJSON(client_json);
	}
	
	public NetworkMessage( ArrayList<PlayerInterface> players ) {
		all_players_json = generator.generateMultiplePlayerJson(players);
		all_players = parser.parseMultipleJson(all_players_json);
	}
	
	public NetworkMessage( String json_string, boolean isMultiple ) { 
		if ( isMultiple ) {
			all_players = parser.parseMultipleJson(json_string);
			all_players_json = json_string;
		} else {
			player = parser.parseOnePlayerJSON(json_string); 
			client_json = json_string;
		}
	}
	
	public NetworkMessage( InputStream json_stream, boolean isMultiple ) {
		if ( isMultiple ) {
			all_players = parser.parseMultipleJson(json_stream);
		} else {
			player = parser.parseOnePlayerJSON(json_stream);
		}
	}

	@Override
	public String getAllPlayerJson() {
		return all_players_json;
	}

	@Override
	public List<PlayerData> getAllPlayerData() {
		return all_players;
	}

	@Override
	public String getMyPlayerJson() {
		return client_json;
	}

	@Override
	public PlayerData getClientPlayerData() {
		return player;
	}

}
