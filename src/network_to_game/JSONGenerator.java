package network_to_game;

import game.Player;
import interfaces.BulletInterface;
import interfaces.PlayerInterface;

import java.io.StringWriter;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.stream.JsonGenerator;

public class JSONGenerator {

	StringWriter writer;
	private JsonGenerator generator;
	
	public JSONGenerator(){
		writer = new StringWriter();
		generator = Json.createGenerator( writer );
	}
	
	public String generateSingleJson( PlayerInterface player ) {
		generator.writeStartObject();
		
		addPlayerJson(player);
		
		generator.writeEnd();
		generator.flush();
		return writer.toString();
	}
	
	public String generateMultipleJson( ArrayList<Player> all_players ) {
		generator.writeStartObject();
		for( PlayerInterface player : all_players )
			addPlayerJson(player);
		generator.writeEnd();
		generator.flush();
		return writer.toString();
	}
	
	private void addPlayerJson( PlayerInterface player ) {
			generator.writeStartArray(String.valueOf(player.getUniqueId()) )
			.writeStartObject()
			.write("id", player.getUniqueId())
			.write("alive", player.isAlive())
			.write("x", player.getX())
			.write("y", player.getY())
			.write("heading", player.getHeading().name())
			.write("color", player.getColorAsString())
			.writeStartObject("bullets");
		
		for( int i = 0; i < player.getBullets().size(); i++) {
			BulletInterface bullet = player.getBullets().get(i);
			generator.writeStartObject("bullet")
				.write("x", bullet.getX())
				.write("y", bullet.getY())
				.write("direction", bullet.getDirection())
			.writeEnd();
		}
		
		generator.writeEnd().writeEnd().writeEnd();
	}
}
