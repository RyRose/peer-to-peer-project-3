package interfaces;

import game.Point;


public interface BulletInterface {
	public PointInterface getCoordinates();
	public double getDirection(); // angle to where bullet is being fired
	public void shoot();
	public double distanceTo(Point p);
}
