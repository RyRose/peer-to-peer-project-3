package game;

import java.util.List;

import interfaces.BulletInterface;
import interfaces.PlayerInterface;
import interfaces.PointInterface;

public class Player implements PlayerInterface{
	private int uniqueId;
	private List<BulletInterface> bullets;
	private PointInterface coordinates;
	private Direction heading;
	private boolean alive = true;
	
	public Player (PointInterface coordinates, List<BulletInterface> bullets) {
		this.coordinates = coordinates;
		this.bullets = bullets;
	}
	
	@Override
	public Direction getHeading() {
		return heading;
	}
	
	@Override
	public double getHeadingAsDouble() {
		double direction = 0;
		switch (heading) {
		case UP:
			direction = 90;
		case DOWN:
			direction = -90;
		case LEFT:
			direction = 180;
		case RIGHT:
			direction = 0;
		}
		return direction;
	}
	
	@Override
	public void setHeading(Direction d) {
		heading = d;
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
	public void setCoordinates(double x, double y) {
		coordinates = new Point(x,y);
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
