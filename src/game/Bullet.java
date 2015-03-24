package game;

import com.fasterxml.jackson.annotation.JsonIgnore;

import interfaces.BulletInterface;

public class Bullet implements BulletInterface {
	private Point coordinates = new Point();
	private double direction;
	
	public Bullet() {} // Used for JSON and testing
	
	public Bullet(Point c, double d) {
		coordinates.setX(c.getX());
		coordinates.setY(c.getY());
		direction = d;
	}
	
	public void shoot() {
		setX(getX() + 5*Math.cos(direction));
		setY(getY() - 5*Math.sin(direction));
	}
	
	@JsonIgnore
	public boolean isOffScreen() {
		double x = getX();
		double y = getY();
		
		return ( x < 0 || x > 800) || ( y < 0 || y > 600 );
	}
	
	public double distanceTo(Point p) {
		double x1 = getX();
		double y1 = getY();
		double x2 = p.getX();
		double y2 = p.getY();
		return Math.sqrt(  Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1-y2), 2)  );
	}

	
	@Override
	public String toString() {
		return "Bullet={x=" + getX() + ", y=" + getY() + ", direction=" + getDirection() + "}";
	}
	
	@Override
	public int hashCode() {
		return (int) (getX() + getY() + direction);
	}

	@Override
	public boolean equals(Object obj) {
		if ( obj instanceof Bullet ) {
			Bullet other = (Bullet) obj;
			return  ( other.direction == direction ) && 
					( other.getX() == getX() ) &&
					( other.getY() == getY() );
		} else {
			return false;
		}
	}
	
	// Getters and Setters

	@Override public double getDirection() { return direction; }
	@Override public void setDirection(double new_direction) { direction = new_direction; }
	
	@Override public double getX() { return coordinates.getX(); }
	@Override public void setX(double new_x) { coordinates.setX(new_x); }
	
	@Override public double getY() { return coordinates.getY(); }
	@Override public void setY(double new_y) { coordinates.setY(new_y); }
}
