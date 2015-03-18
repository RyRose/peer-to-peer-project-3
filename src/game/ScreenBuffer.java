package game;

import java.util.ArrayList;
import java.util.List;

import user_interface.GameController;
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
	
	public ScreenBuffer(List<PlayerData> allPlayerData, int player_id) {
		ArrayList<PlayerInterface> players = new ArrayList<PlayerInterface>();
		for (PlayerData playerData : allPlayerData) {
			players.add(playerData.toPlayer());
		}
		map = new Map(players, new ArrayList<Point>());
		myPlayer = null;
	}
	
	public void updatePlayer(PlayerData playerUpdate) {
		map.setPlayer(playerUpdate.id, playerUpdate.toPlayer());
	}
	
	public void updatePlayers(List<PlayerData> allPlayerData) {
		for (PlayerData playerUpdate : allPlayerData) {
			map.setPlayer(playerUpdate.id, playerUpdate.toPlayer());
		}
	}
	
	public void updateMyPlayer() {
		map.setPlayer(myPlayer.getUniqueId(), myPlayer);
	}
	
	public ArrayList<PlayerInterface> getPlayers() {
		return map.getPlayers();
	}
	
	public void move(Direction d) {
		PointInterface coordinates = myPlayer.getCoordinates();
		switch (d) {
		case UP:
			myPlayer.setCoordinates(coordinates.getX(), coordinates.getY() - 5);
			break;
		case DOWN:
			myPlayer.setCoordinates(coordinates.getX(), coordinates.getY() + 5);
			break;
		case LEFT:
			myPlayer.setCoordinates(coordinates.getX() - 5, coordinates.getY());
			break;
		case RIGHT:
			myPlayer.setCoordinates(coordinates.getX() + 5, coordinates.getY());
			break;
		}
		myPlayer.setHeading(d);
	}
	
	public void updateBullets() {
			for (int i = 0; i < myPlayer.getBullets().size(); i++) {
				Bullet bullet = (Bullet) myPlayer.getBullets().get(i);
				bullet.shoot();
				if (bullet.isOffScreen()) {
					myPlayer.getBullets().remove(bullet);
				}
				// checkAlive(bullet);
			}
	}
	
	public void shootBullet() {
		BulletInterface newBullet = new Bullet(myPlayer.getCoordinates(), myPlayer.getHeadingAsDouble());
		myPlayer.addBullet(newBullet);
	}
	
	public void checkAlive(BulletInterface bullet) {
		if (bullet.distanceTo(myPlayer.getCoordinates()) <= 25) {
			killPlayer();	
		}
	}
	
	public void killPlayer() {
		map.removePlayer(myPlayer);
		myPlayer.kill();
	}
	
	public Player getMe() {
		return myPlayer;
	}
	
	public Map getMap() {
		return map;
	}
}
