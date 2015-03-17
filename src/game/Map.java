package game;

import interfaces.PlayerInterface;

import java.util.ArrayList;
import java.util.List;

public class Map {
	
	private ArrayList<PlayerInterface> players;
	private ArrayList<Point> obstacles;
	
	public Map (ArrayList<Point> obstacles) {
		this.obstacles = obstacles;
	}
	
	public Map (ArrayList<PlayerInterface> players, ArrayList<Point> obstacles) {
		this.players = players;
		this.obstacles = obstacles;
	}

	public ArrayList<PlayerInterface> getPlayers() {
		return players;
	}
		
	public void setPlayer(int player_id, Player player) {
		players.set(player_id, player);
	}
	
	public List<Point> getObstacles() {
		return obstacles;
	}
	
	public void setObstacles(ArrayList<Point> obstacles) {
		this.obstacles = obstacles;
	}

}
