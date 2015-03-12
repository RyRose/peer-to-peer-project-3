package game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import network_to_game.NetworkMessage;
import network_to_game.NetworkToGameMessage;
import network_to_game.PlayerData;
import interfaces.BulletInterface;
import interfaces.PlayerInterface;
import interfaces.PointInterface;

public class ScreenBuffer {
	
	private Map map;
	public Player me;
	
	public ScreenBuffer(Map m) {
		map = m;
	}
	
	public ScreenBuffer(NetworkToGameMessage networkMessage, int player_id) {
		List<PlayerData> allPlayerData = networkMessage.getAllPlayers();
		ArrayList<Player> players = new ArrayList<Player>();
		for (PlayerData playerData : allPlayerData) {
			players.add(playerData.toPlayer());
			if (playerData.id == player_id) {me = playerData.toPlayer();}
		}
		map = new Map(players, new ArrayList<Point>());
	}
	
	public void updatePlayer(NetworkMessage message) {
		PlayerData playerUpdate = message.getClientPlayerData();
		int playerId = playerUpdate.id;
		Player player = map.players.get(playerId);
		Point updatedPosition = new Point(playerUpdate.x, playerUpdate.y);
		Player updatedPlayer = new Player(updatedPosition, player.getBullets(), player.getHeading());
		map.players.set(playerId, updatedPlayer);
	}
	
	public void updatePlayers(NetworkToGameMessage networkMessage) {
		List<PlayerData> allPlayerData = networkMessage.getAllPlayers();
		for (PlayerData playerUpdate : allPlayerData) {
			int playerId = playerUpdate.id;
			Player player = map.players.get(playerId);
			Point updatedPosition = new Point(playerUpdate.x, playerUpdate.y);
			Player updatedPlayer = new Player(updatedPosition, player.getBullets(), player.getHeading());
			map.players.set(playerId, updatedPlayer);
		}
	}
	
	public void move(Direction d) {
		PointInterface coordinates = me.getCoordinates();
		switch (d) {
		case UP:
			me.setCoordinates(coordinates.getX(), coordinates.getY() - 5);
		case DOWN:
			me.setCoordinates(coordinates.getX(), coordinates.getY() + 5);
		case LEFT:
			me.setCoordinates(coordinates.getX() - 5, coordinates.getY());
		case RIGHT:
			me.setCoordinates(coordinates.getX() + 5, coordinates.getY());
		}
		me.setHeading(d);
	}
	
	public void updateBullets() {
		for (PlayerInterface player : map.players) {
			for (BulletInterface bullet : player.getBullets()) {
				bullet.shoot();
			}
		}
	}
	
	public void shootBullet() {
		BulletInterface newBullet = new Bullet(me.getCoordinates(), me.getHeadingAsDouble());
		me.getBullets().add(newBullet);
	}
	
	public Player getMe() {
		return me;
	}
}
