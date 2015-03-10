package test;

import static org.junit.Assert.*;
import interfaces.PlayerInterface;

import java.util.ArrayList;

import network_to_game.JSONGenerator;
import network_to_game.JSONparser;
import network_to_game.PlayerData;

import org.junit.Test;

public class JsonTest {
	
	JSONGenerator generator = new JSONGenerator();
	JSONparser parser = new JSONparser();

	@Test
	public void testJsonGenerateAndParse() {
		TestPlayer player = new TestPlayer(0, 1);
		player.addBullet( new TestBullet( 0, 1, 90 ));
		player.addBullet( new TestBullet( 25, 50, 10.32442));
		String json = generator.generateOnePlayerJson(player);
		PlayerData transmitted_player = parser.parseOnePlayerJSON(json);
		assertTrue( player.toString().equals(transmitted_player.toString()));
	}
	
	@Test
	public void testParseJson() {
		TestPlayer player = new TestPlayer(13213, 12124);
		for( int i = 0; i < 10000; i++) {
			player.addBullet( new TestBullet(i, i*10, i*1000));
		}
		String json = generator.generateOnePlayerJson(player);
		PlayerData transmitted_player = parser.parseOnePlayerJSON(json);
		assertTrue( player.toString().equals(transmitted_player.toString()));
	}
	
	@Test
	public void testMultipleJsonGenerateAndParse() {
		ArrayList<PlayerInterface> players = new ArrayList<>();
		for( int i = 0; i < 10; i++ ) {
			TestPlayer player = new TestPlayer(i , i + 1 );
			player.addBullet( new TestBullet( i + 2, i + 4,( i + 100 ) / 3));
			player.addBullet( new TestBullet( i + 3, i + 5, (i + 50) / 4));
			players.add(player);
		}
		
		String json = generator.generateMultiplePlayerJson(players);
		ArrayList<PlayerData> players_data = parser.parseMultipleJson(json);
		for ( int i = 0; i < players.size(); i++ ) {
			assertTrue( players.get(i).toString().equals(players_data.get(i).toString()) );
		}
	}
	
}
