package game;

import java.util.ArrayList;
import java.util.List;

import interfaces.MapInterface;
import interfaces.PlayerInterface;
import interfaces.PointInterface;

public class Map implements MapInterface{
	ArrayList<PlayerInterface> players;
	ArrayList<PointInterface> obstacles;
	
	public Map (ArrayList<PointInterface> obstacles) {
		this.obstacles = obstacles;
	}
	
	public Map (ArrayList<PlayerInterface> players, ArrayList<PointInterface> obstacles) {
		this.players = players;
		this.obstacles = obstacles;
	}

	@Override
	public List<PlayerInterface> getPlayers() {
		return players;
	}

	@Override
	public List<PointInterface> getObstacles() {
		return obstacles;
	}

}
