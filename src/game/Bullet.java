package game;

import interfaces.BulletInterface;
import interfaces.PointInterface;

public class Bullet implements BulletInterface{
	private PointInterface coordinates;
	private double direction;
	
	public Bullet(PointInterface c, double d) {
		coordinates = c;
		direction = d;
	}

	@Override
	public PointInterface getCoordinates() {
		return coordinates;
	}

	@Override
	public double getDirection() {
		return direction;
	}
	
	@Override
	public void move() {
		coordinates = new Point(coordinates.getX() + 5*Math.cos(direction), coordinates.getY() - 5*Math.sin(direction));
	}

}
