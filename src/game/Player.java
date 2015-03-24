package game;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import interfaces.PlayerInterface;

public class Player implements PlayerInterface {
	private int uniqueId;
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private Point coordinates = new Point();
	private Direction heading;
	private boolean alive;
	private Paint color;
	
	public Player() {} // Used for JSON and testing
	
	public Player (Point coordinates, Direction heading) {
		this.coordinates = coordinates;
		this.heading = heading;
		this.color = Color.BLACK;
		this.alive = true;
		this.uniqueId = 0;
	}
	
	public void addBullet(Bullet bullet) {
		bullets.add(bullet);
	}
	
	public void kill() {
		alive = false;
	}
	
	@Override
	public String toString() {
		return "Player={uniqueId=" + getUniqueId() + ", bullets=" + getBullets() + 
				", heading=" + getHeading() + ", alive=" + isAlive() + ", x=" + getX() + 
				", y=" + getY() + ", color=" + getColor() + "}";
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( obj instanceof Player ) {
			Player other = (Player) obj;
			return  ( other.uniqueId == uniqueId ) &&
					( other.alive == alive ) &&
					( other.bullets.containsAll(bullets) ) && // this player's bullets subsets the other's
					( bullets.containsAll(other.bullets) ) && // other's bullets subset this players. Therefore, sets are equal.
					( other.getX() == getX() ) &&
					( other.getY() == getY() ) &&
					( other.heading == heading ) &&
					( other.getColorAsString().equals(getColorAsString()) );
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return uniqueId;
	}
	
	// Getters and Setters
	
	@JsonIgnore
	public double getHeadingAsDouble() { return heading.asDouble();	}
	
	@JsonIgnore
	public Point getCoordinates() {	return coordinates; }
	public void setCoordinates(double x, double y) { coordinates = new Point(x, y); }
	
	
	@JsonIgnore
	public Paint getColor() { return color; }	
	public void setColor(Paint color) { this.color = color; }
	

	@Override public ArrayList<Bullet> getBullets() { return bullets; }
	
	@Override public double getX() { return coordinates.getX(); }
	@Override public void setX(double new_x) { coordinates.setX(new_x); }
	
	@Override public double getY() { return coordinates.getY(); }
	@Override public void setY(double new_y) { coordinates.setY(new_y); }
	
	@Override public int getUniqueId() { return uniqueId;	}
	@Override public void setUniqueId(int new_id) { uniqueId = new_id; }
	
	@Override public boolean isAlive() { return alive; }
	@Override public void setAlive(boolean isAlive) { alive = isAlive; }
	
	@Override public Direction getHeading() { return heading; }
	@Override public void setHeading(Direction d) {	heading = d; }
	
	@Override public String getColorAsString() { return color.toString(); }
	@Override public void setColorAsString( String new_color ){ color = Paint.valueOf(new_color); }

}
