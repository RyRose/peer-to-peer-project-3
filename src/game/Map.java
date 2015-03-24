package game;

import java.util.ArrayList;
import java.util.List;

public class Map {
	
	private List<Player> players;
	private ArrayList<Point> obstacles;
	
	public Map (ArrayList<Point> obstacles) {
		this.obstacles = obstacles;
	}
	
	public Map (List<Player> players, ArrayList<Point> obstacles) {
		this.players = players;
		this.obstacles = obstacles;
	}

	public List<Player> getPlayers() {
		return players;
	}
		
	public void setPlayer(int player_id, Player player) {
		players.set(player_id, player);
	}
	
	public void removePlayer(Player p) {
		players.remove(p);
	}
	
	public List<Point> getObstacles() {
		return obstacles;
	}
	
	public void setObstacles(ArrayList<Point> obstacles) {
		this.obstacles = obstacles;
	}

}
