package test;

import java.util.ArrayList;
import java.util.List;

import interfaces.BulletInterface;
import interfaces.PlayerInterface;
import interfaces.PointInterface;

public class TestPlayer implements PlayerInterface {
	
	private TestPoint point;
	private int id = 0;
	private boolean alive;
	public ArrayList<BulletInterface> bullets;
	

	public TestPlayer( int x, int y) {
		point = new TestPoint(x, y);
		bullets = new ArrayList<BulletInterface>();
	}
	
	public void addBullet( TestBullet bullet ) {
		bullets.add(bullet);
	}
	
	
	@Override
	public void setUniqueId( int new_id ) {
		id = new_id;
	}

	@Override
	public List<BulletInterface> getBullets() { 
		return (List<BulletInterface>) bullets;
	}

	@Override
	public PointInterface getCoordinates() {
		return point;
	}

	@Override
	public int getUniqueId() {
		return id;
	}
	
	@Override
	public boolean isAlive() {
		return alive;
	}
	
	@Override
	public String toString() {
		return "id: " + id + "|x: " + point.x + "|y: " + point.y + "\n" + bullets;
	}
}
