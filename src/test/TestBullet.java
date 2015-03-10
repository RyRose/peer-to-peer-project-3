package test;

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
	public void move() {
		point = new TestPoint(point.getX() + 5*Math.cos(direction), point.getY() - 5*Math.sin(direction));

	}

}
