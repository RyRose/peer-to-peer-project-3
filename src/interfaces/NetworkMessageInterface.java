package interfaces;


import network_to_game.PlayerData;

import java.util.List;

public interface NetworkMessageInterface {
	public String getMyPlayerJson();
	public String getAllPlayerJson();

	public PlayerData getClientPlayerData();
	public List<PlayerData> getAllPlayerData();
}
