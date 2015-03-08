package network_to_game;

import java.io.InputStream;

import interfaces.NetworkMessageInterface;
import interfaces.PlayerInterface;

public class NetworkMessage implements NetworkMessageInterface{
	
	private PlayerData player;
	String json;
	private JSONGenerator generator = new JSONGenerator();;
	private JSONparser parser = new JSONparser();;

	public NetworkMessage( PlayerInterface player ) {
		json = generator.generateJson(player);
	}
	
	public NetworkMessage( String json_string ) {
		player = parser.parseJSON(json);
	}
	
	public NetworkMessage( InputStream json_stream ) {
		player = parser.parseJSON(json_stream);
	}
	
	@Override
	public String getMessage() {
		return json;
	}

	@Override
	public PlayerData getPlayerData() {
		return player;
	}

}
