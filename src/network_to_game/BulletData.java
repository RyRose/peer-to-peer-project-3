package network_to_game;

import game.Bullet;
import game.Point;

public class BulletData {
	public double x;
	public double y;
	public double direction;
	
	@Override
	public String toString() {
		return "x: " + x + "|y: " + y + "|dir: " + direction;
	}
	
	public Bullet toBullet() {
		return new Bullet(new Point(x, y), direction);
	}
}
