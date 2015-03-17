package game;

import java.util.ArrayList;
import java.util.List;


import network_to_game.PlayerData;
import interfaces.BulletInterface;
import interfaces.PlayerInterface;
import interfaces.PointInterface;

public class ScreenBuffer {
	
	private Map map;
	public Player myPlayer;
	
	public ScreenBuffer(Map m) {
		map = m;
	}
	
	public ScreenBuffer(List<PlayerData> players_data, int player_id) {
		List<PlayerData> allPlayerData = players_data;
		ArrayList<PlayerInterface> players = new ArrayList<PlayerInterface>();
		for (PlayerData playerData : allPlayerData) {
			players.add(playerData.toPlayer());
		}
		myPlayer = null;
		map = new Map(players, new ArrayList<Point>());
	}
	
	public void updatePlayer(PlayerData player2) {
		PlayerData playerUpdate = player2;
		int playerId = playerUpdate.id;
		Player player = (Player) map.getPlayers().get(playerId);
		Point updatedPosition = new Point(playerUpdate.x, playerUpdate.y);
		Player updatedPlayer = new Player(updatedPosition, player.getBullets(), player.getHeading());
		map.getPlayers().set(playerId, updatedPlayer);
	}
	
	public void updatePlayers(List<PlayerData> players_data) {
		List<PlayerData> allPlayerData = players_data;
		for (PlayerData playerUpdate : allPlayerData) {
			int playerId = playerUpdate.id;
			Player player = (Player) map.getPlayers().get(playerId);
			Point updatedPosition = new Point(playerUpdate.x, playerUpdate.y);
			Player updatedPlayer = new Player(updatedPosition, player.getBullets(), player.getHeading());
			map.getPlayers().set(playerId, updatedPlayer);
		}
	}
	
	public ArrayList<PlayerInterface> getPlayers() {
		return map.getPlayers();
	}
	
	public void move(Direction d) {
		PointInterface coordinates = myPlayer.getCoordinates();
		switch (d) {
		case UP:
			myPlayer.setCoordinates(coordinates.getX(), coordinates.getY() - 5);
		case DOWN:
			myPlayer.setCoordinates(coordinates.getX(), coordinates.getY() + 5);
		case LEFT:
			myPlayer.setCoordinates(coordinates.getX() - 5, coordinates.getY());
		case RIGHT:
			myPlayer.setCoordinates(coordinates.getX() + 5, coordinates.getY());
		}
		myPlayer.setHeading(d);
	}
	
	public void updateBullets() {
		for (PlayerInterface player : map.getPlayers()) {
			for (BulletInterface bullet : player.getBullets()) {
				bullet.shoot();
			}
		}
	}
	
	public void shootBullet() {
		BulletInterface newBullet = new Bullet(myPlayer.getCoordinates(), myPlayer.getHeadingAsDouble());
		myPlayer.addBullet(newBullet);
	}
	
	public Player getMe() {
		return myPlayer;
	}
	
	public Map getMap() {
		return map;
	}
}
