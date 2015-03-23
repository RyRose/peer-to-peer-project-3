package test;

import static org.junit.Assert.*;
import game.Bullet;
import game.Direction;
import game.Player;

import java.util.ArrayList;
import java.util.Random;

import javax.json.stream.JsonParsingException;

import javafx.scene.paint.Color;
import network_to_game.JSON;

import org.junit.Test;

public class JsonTest {	
	Random rand = new Random();	

	@Test
	public void testPlayerWithoutBullets() {
		Player player = generatePlayer();
		String json = JSON.generateSingleJson(player);
		Player transmitted_player = JSON.parseSingleJson(json);
		assertEquals( player, transmitted_player);
	}
	
	@Test
	public void testSinglePlayerWithBullets() {
		Player player = generatePlayer();
		for( int i = 0; i < 10; i++) {
			player.addBullet( generateBullet() );
		}
		String json = JSON.generateSingleJson(player);
		Player transmitted_player = JSON.parseSingleJson(json);
		assertEquals(player, transmitted_player);
	}
	
	@Test
	public void testMultiplePlayersWithBullets() {
		ArrayList<Player> players = new ArrayList<>();
		for( int i = 0; i < 10; i++ ) {
			Player player = generatePlayer();
			
			for( int j = 0; j < 100; j++)
				player.addBullet( generateBullet() );
			
			players.add(player);
		}
		
		String json = JSON.generateMultipleJson(players);
		ArrayList<Player> transmitted_players = JSON.parseMultipleJson(json);
		for ( int i = 0; i < players.size(); i++ ) {
			assertEquals(players.get(i), transmitted_players.get(i));
		}
	}
	
	@Test(expected = JsonParsingException.class)
	public void testInvalidJson() {
		JSON.parseMultipleJson("This is not good JSON");
	}
	
	@Test(expected = NullPointerException.class)
	public void testIncompletePlayerGeneration() {
		Player player = new Player(); // No instance variables initialized
		JSON.generateSingleJson(player);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSingleJsonInMultipleJson() {
		JSON.parseMultipleJson( JSON.generateSingleJson(generatePlayer()) );
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMultipleJsoninSingleJson() {
		ArrayList<Player> players = new ArrayList<Player>();
		for (int i = 0; i < 10; i++) {
			players.add( generatePlayer() );
		}
		
		JSON.parseSingleJson(JSON.generateMultipleJson(players));
	}
	
	private Bullet generateBullet() {
		Bullet bullet = new Bullet();
		bullet.setX(rand.nextInt() + rand.nextDouble());
		bullet.setY(rand.nextInt() + rand.nextDouble());
		bullet.setDirection(rand.nextInt() + rand.nextDouble());
		return bullet;
	}
	
	private Player generatePlayer() {
		Player player = new Player();
		player.setUniqueId(rand.nextInt());
		player.setAlive(rand.nextBoolean());
		player.setX(rand.nextInt());
		player.setY(rand.nextInt());
		player.setColor(Color.BLACK.toString());
		
		switch( rand.nextInt(4) ) { // Generates random direction from 4 choices
		case 0:
			player.setHeading(Direction.DOWN);
			break;
		case 1:
			player.setHeading(Direction.UP);
			break;
		case 2:
			player.setHeading(Direction.LEFT);
			break;
		case 3:
			player.setHeading(Direction.RIGHT);
			break;
		}
		return player;
	}
}
