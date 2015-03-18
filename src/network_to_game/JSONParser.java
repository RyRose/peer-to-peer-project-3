package network_to_game;


import game.Direction;

import java.io.StringReader;
import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

public class JSONParser {

	public PlayerData parseOnePlayerJSON( String json ) {
		JsonParser parser = Json.createParser( new StringReader(json) );
		return getPlayerFromJson(parser);
	}
	
	public ArrayList<PlayerData> parseMultipleJson( String json ) {
		JsonParser parser = Json.createParser( new StringReader(json) );
		return extractMultiplePlayersFromJson(parser);
	}
	
	private ArrayList<PlayerData> extractMultiplePlayersFromJson( JsonParser parser ) {
		ArrayList<PlayerData> list = new ArrayList<>();
		while ( parser.hasNext() ) {
			list.add( getPlayerFromJson(parser) );
		}
		return list;
	}
	
	private PlayerData getPlayerFromJson( JsonParser parser ) {
		PlayerData player = new PlayerData();
		Event e = parser.next();
		while ( parser.hasNext() && !e.equals(Event.END_ARRAY) ) {
			if (e == Event.KEY_NAME) {
				switch (parser.getString()) {
				case "id":
					parser.next();
					player.id = parser.getInt();
					break;
				case "alive":
					e = parser.next();
					player.isAlive = (e == Event.VALUE_TRUE);
					break;
				case "x":
					parser.next();
					player.x = parser.getInt();
					break;
				case "y":
					parser.next();
					player.y = parser.getInt();
					break;
				case "heading_enum":
					parser.next();
					player.heading_enum = Direction.valueOf(parser.getString());
					break;
				case "heading_double":
					parser.next();
					player.heading_double = parser.getBigDecimal().doubleValue();
					break;
				case "color":
					parser.next();
					player.color = Paint.valueOf(parser.getString());
					break;
				case "bullet":
					parser.next();
					e = parser.next();
					if ( e != Event.END_OBJECT) {
						player.addBullet( extractBullet(parser, e) );
					}
				}
			}
			e = parser.next();
		}
		parser.next();
		return player;
	}

	private BulletData extractBullet( JsonParser parser, Event e ) {
		BulletData bullet = new BulletData();
		for( int i = 0; i < 3; ) {
			if (e == Event.KEY_NAME) {
				switch (parser.getString()) {
				case "x":
					parser.next();
					bullet.x = parser.getInt();
					i++;
					break;
				case "y":
					parser.next();
					bullet.y = parser.getInt();
					i++;
					break;
				case "direction":
					parser.next();
					bullet.direction = parser.getBigDecimal().doubleValue();
					i++;
				}
			}
			e = parser.next();
		}

		return bullet;
	}
}
