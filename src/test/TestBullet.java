package test;

import game.Point;
import interfaces.BulletInterface;
import interfaces.PointInterface;

public class TestBullet implements BulletInterface {
	
	private TestPoint point;
	private double direction;
	
	public TestBullet(int x, int y, double direction) {
		point = new TestPoint(x, y);
		this.direction = direction;	}

	@Override
	public PointInterface getCoordinates() {
		return point;
	}

	@Override
	public double getDirection() {
		return direction;
	}
	
	@Override
	public String toString() {
		return "x: " + point.x + "|y: " + point.y + "|dir: " + direction;
	}

	@Override
	public void shoot() {
		point = new TestPoint(point.getX() + 5*Math.cos(direction), point.getY() - 5*Math.sin(direction));
	}
	
	@Override
	public double distanceTo(Point p) {
		double x1 = this.getCoordinates().getX();
		double y1 = this.getCoordinates().getY();
		double x2 = p.getX();
		double y2 = p.getY();
		return Math.sqrt(  Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1-y2), 2)  );
	}

}
