package interfaces;

import game.Direction;

import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

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

	public void setColor(Paint c);

	public Paint getColor();
}
