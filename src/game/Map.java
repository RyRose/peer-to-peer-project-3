package game;

import interfaces.PlayerInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/*import interfaces.MapInterface;
import interfaces.PlayerInterface;
import interfaces.PointInterface;*/

public class Map /*implements MapInterface*/{
	
	ArrayList<PlayerInterface> players;
	ArrayList<Point> obstacles;
	
	public Map (ArrayList<Point> obstacles) {
		this.obstacles = obstacles;
	}
	
	public Map (ArrayList<PlayerInterface> players, ArrayList<Point> obstacles) {
		this.players = players;
		this.obstacles = obstacles;
	}

	//@Override
	public ArrayList<PlayerInterface> getPlayers() {
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
