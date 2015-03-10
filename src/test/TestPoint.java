package test;

import interfaces.PointInterface;

public class TestPoint implements PointInterface {
	
	public double x;
	public double y;
	
	public TestPoint(double x, double y) {
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
