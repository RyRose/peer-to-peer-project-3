package game;

import interfaces.PointInterface;

public class Point implements PointInterface{ // TODO: Delete this class. It is pointless.
	private double x;
	private double y;
	
	public Point() {
		x = 0;
		x = 0;
	}
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override public double getX() { return x; }
	@Override public void setX(double new_x) { x = new_x; }
	
	@Override public double getY() { return y; }
	@Override public void setY(double new_y) { y = new_y; }
}
