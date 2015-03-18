package game;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import interfaces.BulletInterface;
import interfaces.PlayerInterface;

public class Player implements PlayerInterface{
	private int uniqueId;
	private List<BulletInterface> bullets;
	private Point coordinates;
	private Direction heading;
	private boolean alive = true;
	private Color color;
	
	public Player (Point coordinates, Direction heading) {
		this.coordinates = coordinates;
		this.heading = heading;
		this.bullets = new ArrayList<BulletInterface>();
	}
	
	public Player (Point coordinates, List<BulletInterface> bullets) {
		this.coordinates = coordinates;
		this.bullets = bullets;		
	}
	
	public Player (Point coordinates, List<BulletInterface> bullets, Direction heading) {
		this.coordinates = coordinates;
		this.bullets = bullets;
		this.heading = heading;
	}
	
	@Override
	public Direction getHeading() {
		return heading;
	}
	
	@Override
	public double getHeadingAsDouble() {
		double direction = 0;
		switch (heading) {
		case UP:
			direction = 90;
		case DOWN:
			direction = -90;
		case LEFT:
			direction = 180;
		case RIGHT:
			direction = 0;
		}
		return direction;
	}
	
	@Override
	public void setHeading(Direction d) {
		heading = d;
	}

	@Override
	public void setUniqueId(int new_id) {
		uniqueId = new_id;
	}

	@Override
	public List<BulletInterface> getBullets() {
		return bullets;
	}
	
	public void addBullet(BulletInterface bullet) {
		bullets.add(bullet);
	}

	@Override
	public Point getCoordinates() {
		return coordinates;
	}
	
	@Override
	public void setCoordinates(double x, double y) {
		coordinates = new Point(x,y);
	}

	@Override
	public int getUniqueId() {
		return uniqueId;
	}
	
	@Override
	public boolean isAlive() {
		return alive;
	}
	
	@Override
	public String toString() {
		return "id: " + uniqueId + "|x: " + coordinates.getX() + "|y: " + coordinates.getY() + 
				"|isAlive: " + alive +  "|heading_enum " + heading + "|heading_double " + 
				getHeadingAsDouble() + "\n" + bullets;
	}
	
	public void kill() {
		alive = false;
	}
	
	@Override
	public void setColor(Color c) {
		color = c;
	}
	
	@Override
	public Color getColor() {
		return color;
	}

}
