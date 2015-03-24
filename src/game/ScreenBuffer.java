package game;

import java.util.ArrayList;
import java.util.List;

public class ScreenBuffer {
	
	private Map map;
	public Player myPlayer;
	
	public ScreenBuffer(Map m) {
		map = m;
	}
	
	public ScreenBuffer(List<Player> players, int player_id) {
		map = new Map(players, new ArrayList<Point>());
		myPlayer = null;
	}
	
	public void updatePlayer(Player player) {
		map.setPlayer(player.getUniqueId(), player);
	}
	
	public void updatePlayers(Player[] players) {
		for (Player player : players) {
			map.setPlayer(player.getUniqueId(), player);
		}
	}
	
	public void updateMyPlayer() {
		map.setPlayer(myPlayer.getUniqueId(), myPlayer);
	}
	
	public List<Player> getPlayers() {
		return map.getPlayers();
	}
	
	public void move(Direction d) {
		Point coordinates = myPlayer.getCoordinates();
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
			}
			for (int j = 0; j < map.getPlayers().size(); j++) {
				ArrayList<Bullet> bullets = map.getPlayers().get(j).getBullets();
				if (map.getPlayers().get(j).getUniqueId() != myPlayer.getUniqueId()) {
					for( int k = 0; k < bullets.size(); k++) {
						checkAlive(bullets.get(k));
					}
				}
			}
	}
	
	public void shootBullet() {
		Bullet newBullet = new Bullet(myPlayer.getCoordinates(), myPlayer.getHeadingAsDouble());
		myPlayer.addBullet(newBullet);
	}
	
	public void checkAlive(Bullet bullet) {
		if (bullet.distanceTo(myPlayer.getCoordinates()) <= 25) {
			killPlayer();	
		}
	}
	
	public void killPlayer() {
		myPlayer.kill();
	}
	
	public Player getMe() {
		return myPlayer;
	}
	
	public Map getMap() {
		return map;
	}
}
