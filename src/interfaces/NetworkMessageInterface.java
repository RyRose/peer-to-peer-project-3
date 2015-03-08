package interfaces;

import network_to_game.PlayerData;

public interface NetworkMessageInterface {
	public String getMessage(); // String representation of what should be displayed to everybody over the network
	public PlayerData getPlayerData(); // Object representation of...
}
