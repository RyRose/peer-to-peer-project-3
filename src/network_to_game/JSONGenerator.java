package network_to_game;

import interfaces.BulletInterface;
import interfaces.PlayerInterface;

import java.io.StringWriter;

import javax.json.Json;
import javax.json.stream.JsonGenerator;



public class JSONGenerator {

	StringWriter writer;
	private JsonGenerator generator;
	
	public JSONGenerator(){
		writer = new StringWriter();
		generator = Json.createGenerator( writer );
	}
	
	public String generateJson( PlayerInterface player ) {
		generator.writeStartObject()
			.write("id", player.getUniqueId())
			.write("x", player.getCoordinates().getX())
			.write("y", player.getCoordinates().getY())
			.writeStartObject("bullets");
		
		for( BulletInterface b : player.getBullets()) {
			generator.writeStartObject("bullet")
				.write("x", b.getCoordinates().getX())
				.write("y", b.getCoordinates().getY())
				.write("direction", b.getDirection())
			.writeEnd();
		}
		
		generator.writeEnd().writeEnd();
		generator.flush();
		return writer.toString();
	}
}
