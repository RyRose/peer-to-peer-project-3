package network_to_game;

import java.util.ArrayList;
import java.util.List;

import com.sun.istack.internal.Nullable;

import interfaces.NetworkMessageInterface;
import interfaces.PlayerInterface;

public class NetworkMessage implements NetworkMessageInterface{
	
	private GameToNetworkMessage game_to_network;
	private NetworkToGameMessage network_to_game;
		
	
	public void setGameToNetworkMessage( @Nullable PlayerInterface my_player, @Nullable ArrayList<PlayerInterface> all_players  ) {
		game_to_network = new GameToNetworkMessage(my_player, all_players);
	}
	
	public void setNetworkToGameMessage(String json, boolean isMultiple ) {
		network_to_game = new NetworkToGameMessage(json, isMultiple);
	}

	@Override
	public String getAllPlayerJson() {
		return game_to_network.getAllPlayersJson();
	}

	@Override
	public List<PlayerData> getAllPlayerData() {
		return network_to_game.getAllPlayers();
	}

	@Override
	public String getMyPlayerJson() {
		return game_to_network.getSingleJson();
	}

	@Override
	public PlayerData getClientPlayerData() {
		return network_to_game.getPlayer();
	}

}
