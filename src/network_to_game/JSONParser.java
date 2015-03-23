package network_to_game;


import game.Bullet;
import game.Direction;
import game.Player;

import java.io.StringReader;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

public class JSONParser {
	
	JsonParser parser;

	public Player parseSingleJson( String json ) {
		parser = Json.createParser( new StringReader(json) );
		Player player = getPlayerFromJson();
		
		if( parser.hasNext() ) // This means there is more left to go. Or, that there are multiple players in the json..
			throw new IllegalArgumentException("Attempting to parse multiple players with the single json method.");
		
		return player;
	}
	
	public ArrayList<Player> parseMultipleJson( String json ) {
		parser = Json.createParser( new StringReader(json) );
		return extractMultiplePlayersFromJson();
	}
	
	private ArrayList<Player> extractMultiplePlayersFromJson() {
		ArrayList<Player> list = new ArrayList<>();
		while ( parser.hasNext() ) {
			list.add( getPlayerFromJson() );
		}
		
		if (list.size() == 1)
			throw new IllegalArgumentException("Attempting to parse only one player with the multiple player json method.");
		
		return list;
	}
	
	private Player getPlayerFromJson() {
		Player player = new Player();
		Event e = parser.next();
		while ( parser.hasNext() && !e.equals(Event.END_ARRAY) ) {
			if (e == Event.KEY_NAME) {
				switch (parser.getString()) {
				case "id":
					parser.next();
					player.setUniqueId(parser.getInt());
					break;
				case "alive":
					e = parser.next();
					player.setAlive(e == Event.VALUE_TRUE );
					break;
				case "x":
					parser.next();
					player.setX( parser.getBigDecimal().doubleValue() );
					break;
				case "y":
					parser.next();
					player.setY( parser.getBigDecimal().doubleValue() );
					break;
				case "heading":
					parser.next();
					player.setHeading( Direction.valueOf(parser.getString()) );
					break;
				case "color":
					parser.next();
					player.setColor(parser.getString());
					break;
				case "bullet":
					parser.next();
					e = parser.next();
					if ( e != Event.END_OBJECT) {
						player.addBullet( extractBullet(e) );
					}
				}
			}
			e = parser.next();
		}
		parser.next();
		return player;
	}

	private Bullet extractBullet( Event e ) {
		Bullet bullet = new Bullet();
		for( int i = 0; i < 3; ) {
			if (e == Event.KEY_NAME) {
				switch (parser.getString()) {
				case "x":
					parser.next();
					bullet.setX(parser.getBigDecimal().doubleValue());
					i++;
					break;
				case "y":
					parser.next();
					bullet.setY(parser.getBigDecimal().doubleValue());
					i++;
					break;
				case "direction":
					parser.next();
					bullet.setDirection(parser.getBigDecimal().doubleValue());
					i++;
				}
			}
			e = parser.next();
		}

		return bullet;
	}
}
