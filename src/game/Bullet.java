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
	
	@Override
	public double distanceTo(Point p) {
		double x1 = this.getCoordinates().getX();
		double y1 = this.getCoordinates().getY();
		double x2 = p.getX();
		double y2 = p.getY();
		return Math.sqrt(  Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1-y2), 2)  );
	}

	
	@Override
	public String toString() {
		return "x: " + coordinates.getX() + "|y: " + coordinates.getY() + "|dir: " + direction;
	}
}
