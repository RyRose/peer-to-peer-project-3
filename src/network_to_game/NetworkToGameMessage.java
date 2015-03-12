package network_to_game;

import java.util.ArrayList;

public class NetworkToGameMessage {
	
	private boolean isMultiple;
	
	private ArrayList<PlayerData> all_players;
	private PlayerData player;
	
	private JSONparser parser = new JSONparser();
	
	public NetworkToGameMessage( String json, boolean isMultiple ) {
		this.isMultiple = isMultiple;
		
		if ( isMultiple ) {
			all_players = parser.parseMultipleJson(json);
		} else {
			player = parser.parseOnePlayerJSON(json);
		}
	}

	public boolean isMultiple() {
		return isMultiple;
	}
	
	public PlayerData getPlayer() {
		return player;
	}
	
	public ArrayList<PlayerData> getAllPlayers() {
		return all_players;
	}
}
