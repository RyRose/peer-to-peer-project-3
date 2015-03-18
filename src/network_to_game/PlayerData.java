package network_to_game;

import game.Bullet;
import game.Direction;
import game.Player;
import game.Point;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Paint;

public class PlayerData {
	public int id;
	public double x;
	public double y;
	public boolean isAlive;
	public Direction heading_enum;
	public double heading_double;
	public Paint color;
	
	private ArrayList<BulletData> bullets;
	
	public PlayerData() {
		bullets = new ArrayList<BulletData>();
	}
	
	public void addBullet( BulletData bullet ) {
		bullets.add(bullet);
	}
	
	public List<BulletData> getBullets() {
		return bullets;
	}
	
	@Override
	public String toString() {
		return "id: " + id + "|x: " + x + "|y: " + y + "|isAlive: " + isAlive + "|heading_enum " + heading_enum + "|heading_double " + heading_double + "\n" + bullets;
	}
	
	public Player toPlayer() {
		Player player = new Player(new Point(x, y), heading_enum);
		player.getBullets().addAll(toBullets());
		player.setUniqueId(id);
		return player;
	}
	
	private List<Bullet> toBullets() {
		ArrayList<Bullet> bullets = new ArrayList<Bullet>();
		for( BulletData bullet : this.bullets ) {
			bullets.add(bullet.toBullet());
		}
		return bullets;
	}
}
