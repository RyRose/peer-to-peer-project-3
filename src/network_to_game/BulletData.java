package network_to_game;

public class BulletData {
	public double x;
	public double y;
	public double direction;
	
	@Override
	public String toString() {
		return "x: " + x + "|y: " + y + "|dir: " + direction;
	}
}
