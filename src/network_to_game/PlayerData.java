package network_to_game;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {
	public int id;
	public double x;
	public double y;
	
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
		return "id: " + id + "|x: " + x + "|y: " + y + "\n" + bullets;
	}
}
