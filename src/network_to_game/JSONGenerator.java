package network_to_game;

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
	
	public String generateOnePlayerJson( PlayerInterface player ) {
		generator.writeStartObject();
		
		addPlayerJson(player);
		
		generator.writeEnd();
		generator.flush();
		return writer.toString();
	}
	
	public String generateMultiplePlayerJson( ArrayList<PlayerInterface> players ) {
		generator.writeStartObject();
		for( PlayerInterface player : players )
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
			.write("x", player.getCoordinates().getX())
			.write("y", player.getCoordinates().getY())
			.write("heading_enum", player.getHeading().name())
			.write("heading_double", player.getHeadingAsDouble())
			.write("color", player.getColor().toString())
			.writeStartObject("bullets");
		
		for( int i = 0; i < player.getBullets().size(); i++) {
			BulletInterface bullet = player.getBullets().get(i);
			generator.writeStartObject("bullet")
				.write("x", bullet.getCoordinates().getX())
				.write("y", bullet.getCoordinates().getY())
				.write("direction", bullet.getDirection())
			.writeEnd();
		}
		
		generator.writeEnd().writeEnd().writeEnd();
	}
}
