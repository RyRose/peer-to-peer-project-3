package interfaces;

import game.Bullet;
import game.Direction;

import java.util.ArrayList;


public interface PlayerInterface extends PointInterface {
		
	public ArrayList<Bullet> getBullets();
	
	public int getUniqueId();
	public void setUniqueId( int new_id );
	
	public boolean isAlive();
	public void setAlive( boolean isAlive );
	
	public Direction getHeading();
	public void setHeading(Direction d);
	
	public String getColorAsString();
	public void setColor(String color);
}
