package network_to_game;

import game.Direction;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {
	public int id;
	public double x;
	public double y;
	public boolean isAlive;
	public Direction heading_enum;
	public double heading_double;
	
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
}
