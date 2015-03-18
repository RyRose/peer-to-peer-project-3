package test;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import game.Direction;
import interfaces.BulletInterface;
import interfaces.PlayerInterface;
import interfaces.PointInterface;

public class TestPlayer implements PlayerInterface {
	
	private TestPoint point;
	private int id = 8080;
	private boolean alive;
	private Direction heading;
	public ArrayList<BulletInterface> bullets;
	private Paint color;
	

	public TestPlayer( int x, int y) {
		point = new TestPoint(x, y);
		bullets = new ArrayList<BulletInterface>();
		alive = true;
		heading = Direction.DOWN;
	}
	
	public void addBullet( TestBullet bullet ) {
		bullets.add(bullet);
	}
	
	
	@Override
	public void setUniqueId( int new_id ) {
		id = new_id;
	}

	@Override
	public List<BulletInterface> getBullets() { 
		return (List<BulletInterface>) bullets;
	}

	@Override
	public PointInterface getCoordinates() {
		return point;
	}

	@Override
	public int getUniqueId() {
		return id;
	}
	
	@Override
	public boolean isAlive() {
		return alive;
	}
	
	@Override
	public String toString() {
		return "id: " + id + "|x: " + point.x + "|y: " + point.y + "|isAlive: " + alive +  "|heading_enum " + heading + 
				"|heading_double " + getHeadingAsDouble() + "|color " + getColor().toString() + "\n" + bullets;
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
			break;
		case DOWN:
			direction = -90;
			break;
		case LEFT:
			direction = 180;
			break;
		case RIGHT:
			direction = 0;
			break;
		}
		return direction;
	}

	@Override
	public void setHeading(Direction d) {
		heading = d;
	}

	@Override
	public void setCoordinates(double x, double y) {
		point = new TestPoint(x,y);
	}

	@Override
	public void setColor(Paint c) {
		color = c;
	}

	@Override
	public Paint getColor() {
		return color;
	}
}
