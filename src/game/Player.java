package game;

import java.util.ArrayList;
import java.util.List;

import interfaces.BulletInterface;
import interfaces.PlayerInterface;
import interfaces.PointInterface;

public class Player implements PlayerInterface{
	private int uniqueId;
	private ArrayList<BulletInterface> bullets;
	private PointInterface coordinates;
	private boolean alive = true;
	
	public Player (PointInterface position) {
		coordinates = position;
	}

	@Override
	public void setUniqueId(int new_id) {
		uniqueId = new_id;
	}

	@Override
	public List<BulletInterface> getBullets() {
		return bullets;
	}

	@Override
	public PointInterface getCoordinates() {
		return coordinates;
	}

	@Override
	public int getUniqueId() {
		return uniqueId;
	}
	
	@Override
	public boolean isAlive() {
		return alive;
	}

}
