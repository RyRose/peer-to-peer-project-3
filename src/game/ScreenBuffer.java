package game;

import java.util.List;

import network_to_game.NetworkMessage;
import network_to_game.PlayerData;
import interfaces.BulletInterface;
import interfaces.PlayerInterface;
import interfaces.PointInterface;

public class ScreenBuffer {
	private Map map;
	private Player me;
	
	public ScreenBuffer(Map m) {
		map = m;
	}
	
	
	public void updatePlayer(NetworkMessage message) {
		PlayerData playerUpdate = message.getClientPlayerData();
		int playerId = playerUpdate.id;
		PlayerInterface player = map.players.get(playerId);
		PointInterface updatedPosition = new Point(playerUpdate.x, playerUpdate.y);
		PlayerInterface updatedPlayer = new Player(updatedPosition, player.getBullets());
		map.players.set(playerId, updatedPlayer);
	}
	
	public void updatePlayers(NetworkMessage message) {
		List<PlayerData> allPlayerData = message.getAllPlayerData();
		for (PlayerData playerUpdate : allPlayerData) {
			int playerId = playerUpdate.id;
			PlayerInterface player = map.players.get(playerId);
			PointInterface updatedPosition = new Point(playerUpdate.x, playerUpdate.y);
			PlayerInterface updatedPlayer = new Player(updatedPosition, player.getBullets());
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
				bullet.move();
			}
		}
	}
	
	public void shootBullet() {
		BulletInterface newBullet = new Bullet(me.getCoordinates(), me.getHeadingAsDouble());
		me.getBullets().add(newBullet);
	}
}
