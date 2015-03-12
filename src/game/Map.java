package game;

import java.util.ArrayList;
import java.util.List;

/*import interfaces.MapInterface;
import interfaces.PlayerInterface;
import interfaces.PointInterface;*/

public class Map /*implements MapInterface*/{
	ArrayList<Player> players;
	ArrayList<Point> obstacles;
	
	public Map (ArrayList<Point> obstacles) {
		this.obstacles = obstacles;
	}
	
	public Map (ArrayList<Player> players, ArrayList<Point> obstacles) {
		this.players = players;
		this.obstacles = obstacles;
	}

	//@Override
	public List<Player> getPlayers() {
		return players;
	}

	//@Override
	public List<Point> getObstacles() {
		return obstacles;
	}
	
	public void setObstacles(ArrayList<Point> obstacles) {
		this.obstacles = obstacles;
	}

}
