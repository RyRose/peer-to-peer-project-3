package game;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import interfaces.PlayerInterface;

public class Player implements PlayerInterface {
	private int uniqueId;
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private Point coordinates = new Point();
	private Direction heading;
	private boolean alive;
	private String color;
	
	public Player() {} // Used for JSON
	
	public Player (Point coordinates, Direction heading) {
		this.coordinates = coordinates;
		this.heading = heading;
		this.color = Color.BLACK.toString();
		this.alive = true;
		this.uniqueId = 0;
	}
		
	public double getHeadingAsDouble() {
		return heading.asDouble();
	}
	
	public void addBullet(Bullet bullet) {
		bullets.add(bullet);
	}

	public Point getCoordinates() {
		return coordinates;
	}
	
	public void setCoordinates(double x, double y) {
		setX(x);
		setY(y);
	}
	
	public void kill() {
		alive = false;
	}
	
	public void setColor(Paint c) {
		color = c.toString();
	}
	
	public Paint getColor() {
		return Paint.valueOf(color);
	}
	
	@Override
	public String toString() {
		return "id: " + getUniqueId() + "|x: " + getX() + "|y: " + getY() + 
				"|isAlive: " + isAlive() +  "|heading_enum " + getHeading() + 
				"|color " + getColor() + "\n" + getBullets();
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
					( other.color.equals(color) );
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return uniqueId;
	}
	
	// Getters and Setters

	@Override
	public ArrayList<Bullet> getBullets() { return bullets; }
	
	@Override public double getX() { return coordinates.getX(); }
	@Override public void setX(double new_x) { coordinates.setX(new_x); }
	@Override public double getY() { return coordinates.getY(); }
	@Override public void setY(double new_y) { coordinates.setY(new_y); }
	@Override public int getUniqueId() { return uniqueId;	}
	@Override public void setUniqueId(int new_id) { uniqueId = new_id; }
	@Override public boolean isAlive() { return alive; }
	@Override public void setAlive(boolean isAlive) { alive = isAlive; }
	@Override public String getColorAsString() { return color.toString(); }
	@Override public void setColor(String color) { this.color = color; }
	@Override public Direction getHeading() { return heading; }
	@Override public void setHeading(Direction d) {	heading = d; }
}
