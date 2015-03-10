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

}
