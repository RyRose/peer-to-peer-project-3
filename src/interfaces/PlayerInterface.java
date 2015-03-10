package interfaces;

import game.Direction;

import java.util.List;

public interface PlayerInterface {
	
	public void setUniqueId( int new_id );
	
	public List<BulletInterface> getBullets(); // list of bullets that the player has fired
	public PointInterface getCoordinates(); // (x, y) representation of coordinates of player
	public int getUniqueId();
	public boolean isAlive();
	public Direction getHeading();
	public double getHeadingAsDouble();
	public void setHeading(Direction d);
	public void setCoordinates(double x, double y);
}
