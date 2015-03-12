package network_to_game;

import java.util.ArrayList;

import com.sun.istack.internal.Nullable;

import game.Player;
import interfaces.PlayerInterface;

public class GameToNetworkMessage {	
	
	JSONGenerator generator = new JSONGenerator();
	
	private String client_json;
	private String all_players_json;
	
	public GameToNetworkMessage( @Nullable PlayerInterface my_player, @Nullable ArrayList<PlayerInterface> all_players ) { 
		if (my_player != null)
			client_json = generator.generateOnePlayerJson(my_player);
		else
			client_json = "NULL PLAYER";
		if (all_players != null)
			all_players_json = generator.generateMultiplePlayerJson(all_players);
		else
			all_players_json = "NULL PLAYERS";
	}
	
	public String getSingleJson() {
		return client_json;
	}
	
	public String getAllPlayersJson() {
		return all_players_json;
	}

}
