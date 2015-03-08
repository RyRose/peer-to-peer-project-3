package interfaces;

import java.util.List;

public interface PlayerInterface {
	
	public void setUniqueId( int new_id );
	
	public List<BulletInterface> getBullets(); // list of bullets that the player has fired
	public PointInterface getCoordinates(); // (x, y) representation of coordinates of player
	public int getUniqueId();
}
