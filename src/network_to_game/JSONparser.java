package network_to_game;


import java.io.InputStream;
import java.io.StringReader;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;


public class JSONparser {

	public PlayerData parseJSON( InputStream in) {
		JsonParser parser = Json.createParser(in);
		return extractPlayerFromJson(parser);

	}

	public PlayerData parseJSON( String json ) {
		JsonParser parser = Json.createParser( new StringReader(json) );
		return extractPlayerFromJson(parser);
	}

	private PlayerData extractPlayerFromJson( JsonParser parser ) {
		PlayerData player = new PlayerData();
		while (parser.hasNext()) {
			Event e = parser.next();
			if (e == Event.KEY_NAME) {
				switch (parser.getString()) {
				case "id":
					parser.next();
					player.id = parser.getInt();
					break;
				case "x":
					parser.next();
					player.x = parser.getInt();
					break;
				case "y":
					parser.next();
					player.y = parser.getInt();
					break;
				case "bullet":
					parser.next();
					e = parser.next();
					if ( e != Event.END_OBJECT) {
						player.addBullet( extractBullet(parser, e) );
					}
				}
			}
		}
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
