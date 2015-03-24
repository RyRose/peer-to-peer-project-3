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
		String json = JSON.generateJson(player);
		Player[] transmitted_player = JSON.parseJson(json);
		assertEquals( player, transmitted_player[0]);
	}
	
	@Test
	public void testSinglePlayerWithBullets() {
		Player player = generatePlayer(true);
		String json = JSON.generateJson(player);
		Player[] transmitted_player = JSON.parseJson(json);
		assertEquals(player, transmitted_player[0]);
	}
	
	@Test
	public void testMultiplePlayersWithoutBullets() {
		Player[] players = generateMultiplePlayers(false);
		String json = JSON.generateJson(players);
		Player[] transmitted_players = JSON.parseJson(json);
		assertArrayEquals(players, transmitted_players);
	}
	
	@Test
	public void testMultiplePlayersWithBullets() {
		Player[] players = generateMultiplePlayers(true);
		String json = JSON.generateJson(players);
		Player[] transmitted_players = JSON.parseJson(json);
		assertArrayEquals(players, transmitted_players);
	}
	
	@Test
	public void testIsJson() {
		assertFalse( JSON.isGameJson("Not valid Json") );
		assertFalse( JSON.isGameJson("\\dsafsdfc") );
		assertFalse( JSON.isGameJson(""));
		assertFalse( JSON.isGameJson("{}"));
		assertFalse( JSON.isGameJson("[]"));
		assertTrue ( JSON.isGameJson(JSON.generateJson(generatePlayer(false))) );
		assertTrue ( JSON.isGameJson(JSON.generateJson(generatePlayer(true))) );
		assertTrue ( JSON.isGameJson(JSON.generateJson(generateMultiplePlayers(false))) );
		assertTrue ( JSON.isGameJson(JSON.generateJson(generateMultiplePlayers(true))) );
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidSingleJson() {
		JSON.parseJson("This is not good JSON");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidMultipleJson() {
		JSON.parseJson("This is not good JSON");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testIncompletePlayerGeneration() {
		Player player = new Player(); // No instance variables initialized
		JSON.generateJson(player);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testIncompleteMultiplePlayerGeneration() {
		ArrayList<Player> players = new ArrayList<Player>();
		for( int i = 0; i < BIG_NUM; i++)
			players.add( new Player() );
		JSON.generateJson(players);
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
	
	private Player[] generateMultiplePlayers( boolean hasBullets ) {
		Player[] players = new Player[BIG_NUM];
		for( int i = 0; i < BIG_NUM; i++ ) {
			players[i] = generatePlayer(hasBullets);
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
