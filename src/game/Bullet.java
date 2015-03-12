package game;

import interfaces.BulletInterface;

public class Bullet implements BulletInterface{
	private Point coordinates;
	private double direction;
	
	public Bullet(Point c, double d) {
		coordinates = c;
		direction = d;
	}

	@Override
	public Point getCoordinates() {
		return coordinates;
	}

	@Override
	public double getDirection() {
		return direction;
	}
	
	@Override
	public void shoot() {
		coordinates = new Point(coordinates.getX() + 10*Math.cos(direction), coordinates.getY() - 10*Math.sin(direction));
	}

}
