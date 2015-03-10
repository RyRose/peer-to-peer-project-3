package game;

import interfaces.PointInterface;

public class Point implements PointInterface{
	private double x;
	private double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public double getX() {
		return x;
	}
	@Override
	public double getY() {
		return y;
	}
}
