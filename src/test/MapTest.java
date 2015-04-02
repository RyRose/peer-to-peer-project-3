package test;

import static org.junit.Assert.*;
import game.Direction;
import game.Map;
import game.Player;
import game.Point;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

public class MapTest {

	@Test(expected = IndexOutOfBoundsException.class)
	public void test() {
		List<Player> players = new ArrayList<Player>();
		players.add(new Player(new Point(0,0), Direction.UP));
		players.add(new Player(new Point(1,0), Direction.DOWN));
		
		ArrayList<Point> obstacles = new ArrayList<Point>();
		obstacles.add(new Point(0,1));
		
		Map map = new Map(players , obstacles);
		assertTrue(map.isConsistent());
		
		Player newPlayer = new Player(new Point(1,1), Direction.UP);
		for (int i = 0; i<4; i++) {checkSetPlayer(map, i, newPlayer);}
		
		ArrayList<Point> newObstacles = new ArrayList<Point>();
		newObstacles.add(new Point(1,1));
		map.setObstacles(newObstacles);
		assertFalse(map.isConsistent());
}
	
	private void checkSetPlayer(Map m, int id, Player p) {
		m.setPlayer(id, p);
		assertTrue(m.isConsistent());
	}
	
}
