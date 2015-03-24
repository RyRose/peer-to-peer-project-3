package test;

import static org.junit.Assert.*;
import game.Bullet;
import game.Direction;
import game.Player;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;
import network_to_game.JSON;

import org.junit.Test;

public class JsonTest {	
	private static final int BIG_NUM = 100;
	private final Random rand = new Random();	

	@Test
	public void testSinglePlayerWithoutBullets() {
		Player player = generatePlayer(false);
		String json = JSON.generateSingleJson(player);
		Player transmitted_player = JSON.parseSingleJson(json);
		assertEquals( player, transmitted_player);
	}
	
	@Test
	public void testSinglePlayerWithBullets() {
		Player player = generatePlayer(true);
		String json = JSON.generateSingleJson(player);
		Player transmitted_player = JSON.parseSingleJson(json);
		assertEquals(player, transmitted_player);
	}
	
	@Test
	public void testMultiplePlayersWithoutBullets() {
		ArrayList<Player> players = generateMultiplePlayers(false);
		String json = JSON.generateMultipleJson(players);
		ArrayList<Player> transmitted_players = JSON.parseMultipleJson(json);
		assertEquals(players, transmitted_players);
	}
	
	@Test
	public void testMultiplePlayersWithBullets() {
		ArrayList<Player> players = generateMultiplePlayers(true);
		String json = JSON.generateMultipleJson(players);
		ArrayList<Player> transmitted_players = JSON.parseMultipleJson(json);
		assertEquals(players, transmitted_players);
	}
	
	@Test
	public void testIsJson() {
		assertFalse( JSON.isGameJson("Not valid Json") );
		assertFalse( JSON.isGameJson("\\dsafsdfc") );
		assertFalse( JSON.isGameJson(""));
		assertFalse( JSON.isGameJson("{}"));
		assertFalse( JSON.isGameJson("[]"));
		assertTrue ( JSON.isGameJson(JSON.generateSingleJson(generatePlayer(false))) );
		assertTrue ( JSON.isGameJson(JSON.generateSingleJson(generatePlayer(true))) );
		assertTrue ( JSON.isGameJson(JSON.generateMultipleJson(generateMultiplePlayers(false))) );
		assertTrue ( JSON.isGameJson(JSON.generateMultipleJson(generateMultiplePlayers(true))) );
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidSingleJson() {
		JSON.parseSingleJson("This is not good JSON");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidMultipleJson() {
		JSON.parseMultipleJson("This is not good JSON");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testIncompletePlayerGeneration() {
		Player player = new Player(); // No instance variables initialized
		JSON.generateSingleJson(player);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testIncompleteMultiplePlayerGeneration() {
		ArrayList<Player> players = new ArrayList<Player>();
		for( int i = 0; i < BIG_NUM; i++)
			players.add( new Player() );
		JSON.generateMultipleJson(players);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSingleJsonInMultipleJson() {
		JSON.parseMultipleJson( JSON.generateSingleJson(generatePlayer(false)) );
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMultipleJsoninSingleJson() {
		JSON.parseSingleJson( JSON.generateMultipleJson(generateMultiplePlayers(false)) );
	}
	
	// Generating random players and bullets
		
	private Player generatePlayer( boolean hasBullets ) {
		Player player = new Player();
		player.setUniqueId(rand.nextInt());
		player.setAlive(rand.nextBoolean());
		player.setX(rand.nextInt());
		player.setY(rand.nextInt());
		player.setColor( Color.color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()) );
		player.setHeading( Direction.values()[rand.nextInt(4)] );
		
		if (hasBullets)
			for( int j = 0; j < BIG_NUM; j++)
				player.addBullet( generateBullet() );
		
		return player;
	}
	
	private ArrayList<Player> generateMultiplePlayers( boolean hasBullets ) {
		ArrayList<Player> players = new ArrayList<>();
		for( int i = 0; i < BIG_NUM; i++ ) {
			players.add(generatePlayer(hasBullets));
		}
		return players;
	}
	
	private Bullet generateBullet() {
		Bullet bullet = new Bullet();
		bullet.setX(rand.nextInt() + rand.nextDouble());
		bullet.setY(rand.nextInt() + rand.nextDouble());
		bullet.setDirection(rand.nextInt() + rand.nextDouble());
		return bullet;
	}
}
